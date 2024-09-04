package ru.noskov.goroscopeBot.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import ru.noskov.goroscopeBot.entity.UserEntity;

@Component
public class Utils {
    public UserEntity createUser(Long chatId, Chat chat) {
        var user = new UserEntity();
        user.setId(chatId);
        user.setUsername(chat.getUserName());

        return user;
    }

    public SendMessage createMessage(Long chatId) {
        var message = new SendMessage();
        message.setChatId(chatId);

        return message;
    }

}
