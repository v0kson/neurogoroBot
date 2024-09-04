package ru.noskov.goroscopeBot.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.noskov.goroscopeBot.service.TelegramBotApi;

@Slf4j
@Component
@RequiredArgsConstructor
public class BotInitializer {

    final private TelegramBotApi bot;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        var telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            log.error("Error occured:", e);
        }
    }

}
