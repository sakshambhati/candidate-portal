package com.portal.candidate.service;

import com.portal.candidate.exception.UpdationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;
    @Async    // using different thread
    public String sendEmail(String to, String subject, String text) throws UpdationFailedException {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("xyz@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            javaMailSender.send(message);
            return "Mail sent successfully";
        } catch (Exception e)
        {
            throw new UpdationFailedException("Failed to send email");
        }
    }
}


