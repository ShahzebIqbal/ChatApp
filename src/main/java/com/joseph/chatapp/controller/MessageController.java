package com.joseph.chatapp.controller;


import com.joseph.chatapp.dto.MessageDTO;
import com.joseph.chatapp.dto.StatusDTO;
import com.joseph.chatapp.serviceimple.OtpService;
import com.joseph.chatapp.serviceimple.SmsSenderImple;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class MessageController {


    private final SmsSenderImple senderImpple;
    private final OtpService otpService;

    @Autowired
    public MessageController(SmsSenderImple senderImpple, OtpService otpService) {
        this.senderImpple = senderImpple;
        this.otpService = otpService;
    }

    @PostMapping("/generateOTP")
    public ResponseEntity<StatusDTO> generateOTP(MessageDTO messageDTO) {
        int otp = otpService.generateOTP();
        if (messageDTO.getPhoneNumber() != null) {
            if (otp > 0) {
                messageDTO.setMessageBody("Your OTP(One-Time-Password) is: " + otp);
                senderImpple.sendOTP(messageDTO);
                return new ResponseEntity<>(new StatusDTO(HttpStatus.OK.value(), "success", otp), HttpStatus.OK);
            }
            return new ResponseEntity<>(new StatusDTO(HttpStatus.OK.value(), "Failed", "Something went wrong"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new StatusDTO(HttpStatus.OK.value(), "Failed", "Phone number cannot be null...!"), HttpStatus.OK);
    }


    private final Map<String, Integer> messageCounts = new ConcurrentHashMap<>();

    @PostMapping(value = "/abc", produces = "application/xml")
    @ResponseBody
    public String handleSmsWebhook(
            @RequestParam("From") String from,
            @RequestParam("Body") String body) {

        System.out.println("reply sent successfully");

        int thisMessageCount = messageCounts.compute(from, (k, v) -> (v == null) ? 1 : v + 1);

        String plural = (thisMessageCount > 1) ? "messages" : "message";
        String message = String.format(
                "?????? Hello from Twilio. You've sent %d %s, and this one said '%s'",
                thisMessageCount, plural, body);

        return new MessagingResponse.Builder()
                .message(new Message.Builder(message).build())
                .build().toXml();
    }

    @PostMapping(value = "/sms")
    public ResponseEntity<StatusDTO> message(MessageDTO messageDTO) {
        if (messageDTO.getPhoneNumber() != null) {
            senderImpple.sendSms(messageDTO);
            return new ResponseEntity<>(new StatusDTO(HttpStatus.OK.value(), "Success", "The Message Has Been Sent Successfully "), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new StatusDTO(HttpStatus.OK.value(), "Failed", "The Message Could Not Send..!"), HttpStatus.OK);
        }

    }

}
