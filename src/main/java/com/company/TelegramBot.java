package com.company;

import com.company.container.Comp;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "quicksmsbot";
    }

    @Override
    public String getBotToken() {
        return "5211049085:AAFMTw8vrL-YZDa2TvCXiFNNj7qEJM0fNhQ";
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();
            User user = message.getFrom();
            log(user, message);
            Comp.messageController.messageHandle(user, message);
        } else if (update.hasCallbackQuery()) {
            Comp.messageController.CallbackQueryHandle(update.getCallbackQuery());
        } else {
            var sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(update.getMessage().getFrom()));
            sendMessage.setText("Botda mavjud bo'lmagan buyruq kiritildi! " +
                    "\n Iltimos, qaytadan urinib ko'ring. \uD83D\uDE15");
            sendMsg(sendMessage);
        }
    }

    public void sendMsg(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(EditMessageText sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void log(User user, Message message) {
        System.out.println("Name: " + user.getFirstName() + " username: @" + user.getUserName() + " Message: " + message.getText());
    }
}
