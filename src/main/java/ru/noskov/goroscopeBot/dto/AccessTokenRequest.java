package ru.noskov.goroscopeBot.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class AccessTokenRequest {
    private String scope;
}
