package ru.noskov.goroscopeBot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.noskov.goroscopeBot.config.ServerSpec;
import ru.noskov.goroscopeBot.dto.AccessTokenResponse;
import ru.noskov.goroscopeBot.dto.MessageDto;
import ru.noskov.goroscopeBot.mapper.GigaChatMapper;
import ru.noskov.goroscopeBot.utils.HeaderUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GigaChatService {
    private final ServerSpec serverSpec;
    private final GigaChatMapper mapper;

    @Value("${gigachat.authorization}")
    String authorization;

    @Cacheable(value = "access", key = "#root.methodName")
    public String getAccessToken() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        var client = serverSpec.getUnsafeOkHttpClient();
        var rqUUID = UUID.randomUUID().toString();
        var mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody requestBody = RequestBody.create("scope=GIGACHAT_API_PERS", mediaType
        );

        var responseJson = client.newCall(new Request.Builder()
                .url("https://ngw.devices.sberbank.ru:9443/api/v2/oauth")
                .method("POST", requestBody)
                .addHeader(HeaderUtils.Content_Type_HEADER, "application/x-www-form-urlencoded")
                .addHeader(HeaderUtils.ACCEPT_HEADER, "application/json")
                .addHeader(HeaderUtils.RqUID_HEADER, rqUUID)
                .addHeader(HeaderUtils.AUTHORIZATION_HEADER, String.format("Bearer %s", authorization))
                .build());

        var response = responseJson.execute().body().string();

        var objectMapper = new ObjectMapper();

        return objectMapper.readValue(response, AccessTokenResponse.class).getAccessToken();
    }

    public void generateText(String accessToken) throws NoSuchAlgorithmException, KeyManagementException, IOException {
        var client = serverSpec.getUnsafeOkHttpClient();

        var dto = mapper.toMessageRequestDto("GigaChat");
        MessageDto dto1 = new MessageDto();
        dto1.setContent("Отвечай как астролог/мошенник");
        dto1.setRole("system");

        MessageDto dto2 = new MessageDto();
        dto2.setContent("расскажи про мою судьбу");
        dto2.setRole("user");

        dto.setMessages(List.of(dto1, dto2));

        ObjectMapper objectMapper = new ObjectMapper();
        var json = objectMapper.writeValueAsString(dto);


        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        var responseJson = client.newCall(new Request.Builder()
                .url("https://gigachat.devices.sberbank.ru/api/v1/chat/completions")
                .method("POST", body)
                .addHeader(HeaderUtils.Content_Type_HEADER, "application/json")
                .addHeader(HeaderUtils.ACCEPT_HEADER, "application/json")
                .addHeader(HeaderUtils.AUTHORIZATION_HEADER, String.format("Bearer %s", accessToken))
                .build());

        var response = responseJson.execute();
    }

}