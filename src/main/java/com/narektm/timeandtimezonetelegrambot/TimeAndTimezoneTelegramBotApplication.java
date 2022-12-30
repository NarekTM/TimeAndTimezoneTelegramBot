package com.narektm.timeandtimezonetelegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.*;
import org.telegram.telegrambots.meta.exceptions.*;
import org.telegram.telegrambots.updatesreceivers.*;

@SpringBootApplication
public class TimeAndTimezoneTelegramBotApplication {

    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new TimeBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        SpringApplication.run(TimeAndTimezoneTelegramBotApplication.class, args);
    }
}
