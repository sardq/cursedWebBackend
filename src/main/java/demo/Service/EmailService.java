package demo.Service;
// package com.example.Service;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.stereotype.Service;

// import com.example.DTO.MailDto;

// @Service
// public class EmailService implements DefaultEmailService {

// @Autowired
// private JavaMailSender javaMailSender;

// private static final Logger logger = LoggerFactory
// .getLogger(EmailService.class);

// @Override
// public void sendTextEmail(MailDto mailDto) {
// logger.info("Email sending start");

// SimpleMailMessage simpleMessage = new SimpleMailMessage();
// simpleMessage.setTo(mailDto.getTo());
// simpleMessage.setSubject(mailDto.getHeader());
// simpleMessage.setText(mailDto.getMessage());

// javaMailSender.send(simpleMessage);

// logger.info("Email sent");

// }
// }