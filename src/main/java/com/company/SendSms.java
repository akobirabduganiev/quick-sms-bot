package com.company;

import com.company.container.Comp;
import com.company.util.InlineButtonUtil;
import com.squareup.okhttp.*;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.io.IOException;


public class SendSms {
    OkHttpClient client = new OkHttpClient();

    MediaType mediaType = MediaType.parse("application/json");

    @SneakyThrows
    public void sendSmsMessage(String smsBody, String phoneNumber, EditMessageText sendMessage) {
        RequestBody body = RequestBody.create(mediaType, "{\n  \"messages\": [\n    {\n      \"source\": \"mashape\",\n      \"from\": \"Test\",\n      \"body\": \"" + smsBody + "\",\n      \"to\": \"" + phoneNumber + "\",\n      \"schedule\": \"1452244637\",\n      \"custom_string\": \"this is a test\"\n    }\n  ]\n}");
        Request request = new Request.Builder()
                .url("https://clicksend.p.rapidapi.com/sms/send")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("authorization", "Basic YWtvYmlyam9uYWJkdWdhbmlldkBnbWFpbC5jb206QWtvYmlyXzIwMDM=")
                .addHeader("x-rapidapi-host", "clicksend.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "a4b2429facmshbe870fb11cbd8b4p1cd196jsn737950b40961")
                .build();

        Response response = client.newCall(request).execute();

        sendMessage.setText("Xabar muvaffaqiyatli jo'natildi ✅");
        sendMessage.setReplyMarkup(InlineButtonUtil.againButton());
        Comp.telegrambot.sendMsg(sendMessage);

    }
}
