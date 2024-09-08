package ru.noskov.goroscopeBot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class MessageDto {
    private String role;
    private String content;
}
