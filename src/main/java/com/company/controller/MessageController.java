package com.company.controller;

import com.company.container.Comp;
import com.company.entity.Users;
import com.company.enums.Status;
import com.company.util.InlineButtonUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public class MessageController {
    public void messageHandle(User user, Message message) {
        String text = message.getText();
        var sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));
        if (text.equals("/start")) {
            var users = new Users();
            users.setStatus(Status.started);

            Comp.map.put(String.valueOf(user.getId()), users);

            sendMessage.setText(startMessage(user));
            sendMessage.setReplyMarkup(InlineButtonUtil.startButton());
            sendMessage.setParseMode("HTML");
            Comp.telegrambot.sendMsg(sendMessage);

        } else if (Comp.map.get(String.valueOf(user.getId())).getStatus().equals(Status.phoneNumber)) {
            if (text.length() == 13 && text.startsWith("+998") && checkPhoneNumber(text)) {
                Users users = Comp.map.get(String.valueOf(user.getId()));
                users.setPhoneNumber(text);
                users.setStatus(Status.messageText);
                Comp.map.put(String.valueOf(user.getId()), users);

                sendMessage.setText("SMS-xabarni kiriting:");
                Comp.telegrambot.sendMsg(sendMessage);

            } else {
                sendMessage.setText("Telefon raqam xato! Iltimos qaytadan kiriting:");
                Comp.telegrambot.sendMsg(sendMessage);
            }
        } else if (Comp.map.get(String.valueOf(user.getId())).getStatus().equals(Status.messageText)) {
            Users users = Comp.map.get(String.valueOf(user.getId()));
            users.setMessageText(text);
            users.setStatus(Status.sendSms);

            sendMessage.setText("Telefon raqam: " + users.getPhoneNumber() + "\nSMS-xabar: " + users.getMessageText());
            sendMessage.setReplyMarkup(InlineButtonUtil.sendSmsButton());
            Comp.telegrambot.sendMsg(sendMessage);

            Comp.map.put(String.valueOf(user.getId()), users);
        } else {
            sendMessage.setText("Xatolik!");
            Comp.telegrambot.sendMsg(sendMessage);
        }
    }

    public void CallbackQueryHandle(CallbackQuery callbackQuery) {
        User user = callbackQuery.getFrom();
        String data = callbackQuery.getData();

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessageText.setChatId(String.valueOf(user.getId()));

        var sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(user.getId()));

        switch (data) {
            case "start", "again" -> {
                Users users = Comp.map.get(String.valueOf(user.getId()));
                users.setStatus(Status.phoneNumber);

                Comp.map.put(String.valueOf(user.getId()), users);


                sendMessage.setText(askPhoneNumberMessage());
                sendMessage.setParseMode("HTML");
                Comp.telegrambot.sendMsg(sendMessage);
            }
            case "sendSms" -> {
                Users users = Comp.map.get(String.valueOf(user.getId()));
                users.setStatus(Status.started);

                Comp.sendSms.sendSmsMessage(users.getMessageText(), users.getPhoneNumber(), editMessageText);
                Comp.map.put(String.valueOf(user.getId()), users);


            }
            case "cancel" -> {
                editMessageText.setText("SMS-xabar jo'natish bekor qilindi ✅");
                editMessageText.setReplyMarkup(InlineButtonUtil.againButton());
                Comp.telegrambot.sendMsg(editMessageText);
            }
        }
    }

    private String startMessage(User user) {
        return "Salom <b>" + user.getFirstName() + " </b> botga xush kelibsiz! \uD83D\uDE0A" + "\nUshbu botdan foydalanish orqali siz boshqa kishilarga anonim tarzda SMS-xabar jo'natishingiz mumkin.";
    }

    private String askPhoneNumberMessage() {
        return """
                Siz SMS-xabarni jo'natmoqchi bo'lgan raqamni kiriting.

                Telefon raqam kiritilayotganda bo'sh joylarsiz, kiriting.
                 \uD83D\uDCDE Namuna: <b>+998xxxxxxxxx</b>""";
    }

    private boolean checkPhoneNumber(String phone) {
        for (int i = 3; i < phone.length(); i++) {
            if (!Character.isDigit(phone.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
