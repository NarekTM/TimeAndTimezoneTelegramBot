package com.narektm.timeandtimezonetelegrambot;

import com.google.maps.*;
import com.google.maps.model.*;
import org.telegram.telegrambots.bots.*;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.*;
import java.time.format.*;
import java.util.*;

public class TimeBot extends TelegramLongPollingBot {
    private static final String BOT_TOKEN = System.getenv("TIME_BOT_TOKEN");
    private static final String BOT_USERNAME = System.getenv("TIME_BOT_USERNAME");

    private static final String API_KEY = System.getenv("TIME_BOT_GOOGLE_MAPS_API_KEY");

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String chatId = String.valueOf(update.getMessage().getChatId());

            if (messageText.equals("/start")) {
                sendMessage(chatId, "Please enter the city name:");
            } else {
                try {
                    String currentTime = getCurrentTime(messageText);
                    sendMessage(chatId, "The current time in " + messageText + " is: " + currentTime);
                } catch (Exception e) {
                    sendMessage(chatId, "Invalid city name. Please try again.");
                }
            }
        }
    }

    private String getCurrentTime(String city) throws Exception {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();

        GeocodingResult[] results = GeocodingApi.geocode(context, city).await();
        LatLng location = results[0].geometry.location;
        TimeZone timeZone = TimeZoneApi.getTimeZone(context, location).await();

        ZonedDateTime zonedDateTime = ZonedDateTime.now(timeZone.toZoneId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");

        return zonedDateTime.format(formatter);
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    private void sendMessage(String chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
