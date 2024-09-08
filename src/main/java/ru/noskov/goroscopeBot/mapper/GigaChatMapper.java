package ru.noskov.goroscopeBot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.noskov.goroscopeBot.dto.GigaChatMessageRequestDto;

@Mapper
public interface GigaChatMapper {

    @Mapping(target = "model", source = "model")
    @Mapping(target = "stream", constant = "false")
    @Mapping(target = "updateInterval", constant = "0")
    @Mapping(target = "messages", ignore = true)
    GigaChatMessageRequestDto toMessageRequestDto(String model);
}
