package com.joseph.chatapp.serviceimple;

import com.joseph.chatapp.config.TwilioConfig;
import com.joseph.chatapp.dto.MessageDTO;
import com.joseph.chatapp.service.SmsSender;
import com.twilio.base.Creator;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsSenderImple implements SmsSender {

    private final TwilioConfig twilioConfig;

    @Autowired
    public SmsSenderImple(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }

    @Override
    public void sendSms(MessageDTO messageDTO) {
        PhoneNumber to = new PhoneNumber(messageDTO.getPhoneNumber());
        PhoneNumber from = new PhoneNumber(twilioConfig.getTrial_num());
        String messageBody = messageDTO.getMessageBody();

        MessageCreator creator = Message.creator(to, from, messageBody);
        creator.create();

    }
}
