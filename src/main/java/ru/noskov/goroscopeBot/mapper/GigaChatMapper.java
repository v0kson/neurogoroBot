package ru.noskov.goroscopeBot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.noskov.goroscopeBot.dto.AccessTokenRequest;

@Mapper
public interface GigaChatMapper {

    @Mapping(target = "scope", source = "scope")
    AccessTokenRequest toRequestDto(String scope);
}
