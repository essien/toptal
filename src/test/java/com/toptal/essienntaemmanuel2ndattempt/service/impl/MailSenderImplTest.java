package com.toptal.essienntaemmanuel2ndattempt.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;

/**
 *
 * @author bodmas
 */
@RunWith(MockitoJUnitRunner.class)
public class MailSenderImplTest {

    @Mock
    private JavaMailSender emailSender;

    private MailSenderImpl mailSender;

    public MailSenderImplTest() {
    }

    @Before
    public void setUp() {
        mailSender = new MailSenderImpl(emailSender);
    }

    @Test
    public void sendMailShouldRunSuccessfully() {
        String destination = RandomStringUtils.randomAlphabetic(10);
        String subject = RandomStringUtils.randomAlphabetic(10);
        String body = RandomStringUtils.randomAlphabetic(10);

        mailSender.sendMail(destination, subject, body);
    }

    @Test
    public void sendVerificationMailShouldRunSuccessfully() {
        System.out.println("sendVerificationMail");
        String destination = RandomStringUtils.randomAlphabetic(10);
        String tokenLink = RandomStringUtils.randomAlphabetic(10);

        mailSender.sendVerificationMail(destination, tokenLink);
    }
}
