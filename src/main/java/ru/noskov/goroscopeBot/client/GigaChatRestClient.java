package ru.noskov.goroscopeBot.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.noskov.goroscopeBot.config.FeignConfig;
import ru.noskov.goroscopeBot.dto.AccessTokenResponse;
import ru.noskov.goroscopeBot.utils.HeaderUtils;

import java.util.Map;
//
//@FeignClient(name = "giga-chat-client", url = "https://ngw.devices.sberbank.ru:9443/api", configuration = FeignConfig.class)
//public interface GigaChatRestClient {
//
//    @PostMapping(value = "/v2/oauth", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    AccessTokenResponse getAccessToken(@RequestBody Map<String, String> scope,
//                                       @RequestHeader(HeaderUtils.RqUID_HEADER) String rqUid,
//                                       @RequestHeader(HeaderUtils.AUTHORIZATION_HEADER) String authorization,
//                                       @RequestHeader(HeaderUtils.ACCEPT_HEADER) String accept);
//}
//
