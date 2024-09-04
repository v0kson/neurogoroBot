package ru.noskov.goroscopeBot.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class AccessTokenResponse {
    private String accessToken;
    private Long expiresAt;
}
