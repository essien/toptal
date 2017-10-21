package com.toptal.essienntaemmanuel2ndattempt.service.impl;

import com.toptal.essienntaemmanuel2ndattempt.service.api.MailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author bodmas
 */
@Service
public class MailSenderImpl implements MailSender {

    private static final Logger log = LoggerFactory.getLogger(MailSenderImpl.class);
    private static final long serialVersionUID = 1L;

    private final JavaMailSender emailSender;

    public MailSenderImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendMail(String destination, String subject, String body) {
        log.info("Sending email to {}", destination);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destination);
        message.setSubject(subject);
        message.setText(body);
        emailSender.send(message);
    }

    @Override
    public void sendVerificationMail(String destination, String tokenLink) {
        log.info("Sending verification email to {}", destination);
        sendMail(destination, "Email Verify", "Please click the link below to verify your account.\n\n" + tokenLink);
    }
}
