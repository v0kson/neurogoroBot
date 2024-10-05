package ru.noskov.goroscopeBot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.noskov.goroscopeBot.config.BotConfig;
import ru.noskov.goroscopeBot.entity.UserEntity;
import ru.noskov.goroscopeBot.repository.UserRepository;
import ru.noskov.goroscopeBot.utils.Utils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBotApi extends TelegramLongPollingBot {

    private final Utils utils;

    private final UserRepository userRepository;

    private final BotConfig botConfig;

    private final GigaChatService gigaChatService;

    private static final String HELP_TEXT = "Type /start for start using this bot!";

    private static final String DEFAULT_MESSAGE = "Sorry, now it is not work!";

    private Map<String, String> initCommands() {
        Map<String, String> commands = new TreeMap<>();
        commands.put("/start", "Get meeting message");
        commands.put("/stop", "Get stop message");
        commands.put("/help", "Get help message");
        commands.put("/delete", "Delete your`s profile");

        return commands;
    }

    private void registerCommands() {
        try {
            var botCommands = initCommands().entrySet()
                    .stream()
                    .map(entry -> new BotCommand(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());

            execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        registerCommands();
        var message = update.getMessage();

        if (update.hasMessage() && message.hasText()) {
            var messageText = message.getText();
            var chatId = message.getChatId();
            var name = message.getChat().getFirstName();
            var username = message.getChat().getUserName();
            var userOptional = userRepository.findByUsername(username);

            switch (messageText) {
                case "/start":
                    registerUser(update.getMessage());
                    startCommandReceived(chatId, name);
                    break;
                case "/stop":
                    stopCommandReceived(chatId, name, userOptional);
                    break;
                case "/help":
                    try {
                        handleStopOrHelpCommand(chatId, userOptional);
                    } catch (NoSuchAlgorithmException | IOException | KeyManagementException e) {
                        throw new RuntimeException("ошибка при получении accessToken: ", e);
                    }
                    break;
                case "/delete":
                    handleDeleteCommand(chatId, userOptional);
                    break;
                default:
                    sendMessage(chatId, DEFAULT_MESSAGE);
                    log.info("That was default text, that was Name: {}", name);
                    break;
            }
        }
    }

    private void handleStopOrHelpCommand(Long chatId, Optional<UserEntity> userOptional) throws NoSuchAlgorithmException, IOException, KeyManagementException {
        var token = gigaChatService.getAccessToken();

        gigaChatService.generateText(token);

        if (userOptional.isPresent()) {
            sendMessage(chatId, HELP_TEXT);
        } else {
            sendMessage(chatId, "Пройдите регистрацию! /start");
        }
    }

    private void handleDeleteCommand(Long chatId, Optional<UserEntity> userOptional) {
        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
            sendMessage(chatId, "Вы удалены из базы данных!");
        } else {
            sendMessage(chatId, "Вас нет в базе данных");
        }
    }

    private void registerUser(Message message) {

        if (userRepository.findByUsername(message.getChat().getUserName()).isEmpty()) {
            var chat = message.getChat();
            var chatId = chat.getId();
            sendMessage(chatId, "Введить дату рождения в формате: DD.MM.YYYY");

            var user = utils.createUser(chatId, chat);
            user.setFirstName(chat.getFirstName());
            user.setRegisterDate(Timestamp.valueOf(LocalDateTime.now
                    ()));

            userRepository.save(user);

            log.info("Registered user: {}", user);
        }
    }

    private void stopCommandReceived(Long chatId, String name, Optional<UserEntity> userOptional) {
        if (userOptional.isPresent()) {
            var stopAnswer = "Bye, my friend " + name;
            log.info("stop using by User: {}", stopAnswer);

            sendMessage(chatId, stopAnswer);
        } else {
            sendMessage(chatId, "Пройдите регистрацию! /start");
        }
    }

    private void startCommandReceived(Long chatId, String name) {
        var startAnswer = "Hi, " + name + ", nice to meet you";
        log.info("Replied to user: {}", name);

        sendMessage(chatId, startAnswer);
    }

    private void sendMessage(Long chatId, String textToSend) {
        var message = utils.createMessage(chatId);
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occured: ", e);
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {

        super.onUpdatesReceived(updates);
    }

    @Override
    public String getBotUsername() {

        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

}
