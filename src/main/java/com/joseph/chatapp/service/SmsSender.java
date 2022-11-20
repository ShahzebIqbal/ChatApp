package com.joseph.chatapp.service;

import com.joseph.chatapp.dto.MessageDTO;

public interface SmsSender {

    void sendSms(MessageDTO messageDTO);
}
