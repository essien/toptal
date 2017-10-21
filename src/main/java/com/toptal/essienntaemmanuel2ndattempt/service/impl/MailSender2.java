package com.toptal.essienntaemmanuel2ndattempt.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author bodmas
 */
@Service
public class MailSender2 {

    private static final Logger log = LoggerFactory.getLogger(MailSender2.class);
    private static final long serialVersionUID = 1L;

    private final JavaMailSender emailSender;

    public MailSender2(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendMail(String destination, String subject, String body) {
        log.info("Sending email to {}", destination);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destination);
        message.setSubject(subject);
        message.setText(body);
        emailSender.send(message);
    }

    public void sendVerificationMail(String destination, String tokenLink) {
        sendMail(destination, "Email Verify", "Please click the link below to verify your account.\n\n" + tokenLink);
    }
}
