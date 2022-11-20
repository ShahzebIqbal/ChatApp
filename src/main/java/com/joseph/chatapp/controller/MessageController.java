package com.joseph.chatapp.controller;


import com.joseph.chatapp.dto.MessageDTO;
import com.joseph.chatapp.dto.StatusDTO;
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

    @Autowired
    public MessageController(SmsSenderImple senderImpple) {
        this.senderImpple = senderImpple;
    }


    private final Map<String, Integer> messageCounts = new ConcurrentHashMap<>();

    @PostMapping(value = "/reply", produces = "application/xml")
    @ResponseBody
    public ResponseEntity<StatusDTO> handleSmsWebhook(@RequestParam("From") String from, @RequestParam("Body") String body){

        if (from!=null && from.length()>1) {
            int thisMessageCount = messageCounts.compute(from, (k,v) -> (v == null) ? 1 : v+1);

            String plural = (thisMessageCount > 1) ? "messages" : "message";
//        String message = String.format(
//                "☎️ Hello from Twilio. You've sent %d %s, and this one said '%s'",
//                thisMessageCount, plural, body);

            String message = new MessagingResponse.Builder()
                    .message(new Message.Builder(body).build())
                    .build().toXml();

            if (message.length()>0 && !message.isEmpty()){
                return new ResponseEntity<>(new StatusDTO(HttpStatus.OK.value(), "Success","Your Reply : "+body),HttpStatus.OK);
            }

            return new ResponseEntity<>(new StatusDTO(HttpStatus.OK.value(), "Failed","Could not send message"),HttpStatus.OK);
        }
        return new ResponseEntity<>(new StatusDTO(HttpStatus.OK.value(),"Failed","Please Enter Mobile Number"),HttpStatus.OK);
    }


    @PostMapping(value = "/sms")
    public ResponseEntity<StatusDTO> message(MessageDTO messageDTO){
        if (messageDTO.getPhoneNumber()!=null){
            senderImpple.sendSms(messageDTO);
            return new ResponseEntity<>(new StatusDTO(HttpStatus.OK.value(), "Success","The Message Has Been Sent Successfully "),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new StatusDTO(HttpStatus.OK.value(), "Failed","The Message Could Not Send..!"),HttpStatus.OK);
        }
    }

}
