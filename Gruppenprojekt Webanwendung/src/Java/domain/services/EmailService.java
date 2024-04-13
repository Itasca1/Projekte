package gpse.example.domain.services;

import gpse.example.domain.entities.Email;

import javax.mail.MessagingException;


/**
 * Service for sending Emails.
 */
public interface EmailService {
    void sendMail(Email email) throws MessagingException;
}
