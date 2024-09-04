package ru.noskov.goroscopeBot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;
import ru.noskov.goroscopeBot.mapper.GigaChatMapper;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GigaChatService {

    private static final String scope = "GIGACHAT_API_PERS";

    public void getAccessToken() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create("scope=GIGACHAT_API_PERS", mediaType);

        Request request = new Request.Builder()
                .url("http://ngw.devices.sberbank.ru:9443/api/v2/oauth")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Accept", "application/json")
                .addHeader("RqUID", "6f0b1291-c7f3-43c6-bb2e-9f3efb2dc98e")
                .addHeader("Authorization", "Bearer NDNmZjA1MTktNmI0Mi00ZGViLTgzNTctMGQwNzYzMGM4OTE2OjA2YTRkYzA5LWUxNzctNDQ5My05ZjNjLWQ3N2YxY2JjYjRjMw==")
                .build();

        try {
            String test = client.newCall(request).execute().body().string();
            System.out.println(test);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
