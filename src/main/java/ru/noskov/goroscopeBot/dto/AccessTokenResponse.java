package ru.noskov.goroscopeBot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@RequiredArgsConstructor
public class AccessTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_at")
    private String expiresAt;
}
