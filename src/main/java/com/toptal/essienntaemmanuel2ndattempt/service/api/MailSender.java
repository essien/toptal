package com.toptal.essienntaemmanuel2ndattempt.service.api;

/**
 * @author bodmas
 */
public interface MailSender {

    /**
     * Sends an email described by the supplied parameters.
     * @param destination the destination email address
     * @param subject the subject of the email
     * @param body the body of the email
     */
    void sendMail(String destination, String subject, String body);

    /**
     * Sends an email to verify a newly registered account
     * @param destination the destination email address
     * @param tokenLink the verification link
     */
    void sendVerificationMail(String destination, String tokenLink);
}
