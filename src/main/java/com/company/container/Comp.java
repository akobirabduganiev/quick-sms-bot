package com.company.container;

import com.company.SendSms;
import com.company.TelegramBot;
import com.company.controller.MessageController;
import com.company.entity.Users;

import java.util.HashMap;
import java.util.Map;


public abstract class Comp {
    public static TelegramBot telegrambot = new TelegramBot();
    public static MessageController messageController = new MessageController();
    public static Map<String, Users> map = new HashMap<>();
    public static SendSms sendSms = new SendSms();
}
