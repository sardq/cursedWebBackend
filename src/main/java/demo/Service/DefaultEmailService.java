package demo.Service;

import org.springframework.stereotype.Service;

import demo.DTO.MailDto;

@Service
public interface DefaultEmailService {

    void sendTextEmail(MailDto mailDto);
}