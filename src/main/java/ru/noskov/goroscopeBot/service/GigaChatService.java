package ru.noskov.goroscopeBot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.noskov.goroscopeBot.config.ServerSpec;
import ru.noskov.goroscopeBot.dto.AccessTokenResponse;
import ru.noskov.goroscopeBot.utils.HeaderUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GigaChatService {
    private final ServerSpec serverSpec;

    @Value("${gigachat.authorization}")
    String authorization;

    public String getAccessToken() throws NoSuchAlgorithmException, KeyManagementException, IOException {
        var client = serverSpec.getUnsafeOkHttpClient();
        var rqUUID = UUID.randomUUID().toString();
        var mediaType = MediaType.parse("application/x-www-form-urlencoded");
        var body = RequestBody.create("scope=GIGACHAT_API_PERS", mediaType);


        var responseJson = client.newCall(new Request.Builder()
                .url("https://ngw.devices.sberbank.ru:9443/api/v2/oauth")
                .method("POST", body)
                .addHeader(HeaderUtils.Content_Type_HEADER, "application/x-www-form-urlencoded")
                .addHeader(HeaderUtils.ACCEPT_HEADER, "application/json")
                .addHeader(HeaderUtils.RqUID_HEADER, rqUUID)
                .addHeader(HeaderUtils.AUTHORIZATION_HEADER, authorization)
                .build());

        var response = responseJson.execute().body().string();

        var objectMapper = new ObjectMapper();

        return objectMapper.readValue(response, AccessTokenResponse.class).getAccessToken();
    }


}
