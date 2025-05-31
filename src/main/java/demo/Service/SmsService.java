package demo.Service;
// package com.example.Service;

// import com.twilio.Twilio;
// import com.twilio.rest.api.v2010.account.Message;
// import com.twilio.type.PhoneNumber;

// import jakarta.annotation.PostConstruct;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;

// @Service
// public class SmsService implements DefaultSmsService {
// private final String accountSid;
// private final String authToken;
// private final String fromNumber;

// public SmsService(String accountSid, String authToken, String fromNumber) {
// this.accountSid = accountSid;
// this.authToken = authToken;
// this.fromNumber = fromNumber;
// Twilio.init(accountSid, authToken);
// }

// @Override
// public void sendSms(String phoneNumber, String message) {
// Message.creator(
// new PhoneNumber(phoneNumber),
// new PhoneNumber(fromNumber),
// message).create();
// }
// }