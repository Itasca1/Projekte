package gpse.example.domain.serviceimpl;

import gpse.example.domain.entities.Email;
import gpse.example.domain.services.EmailService;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Impl of EmailService.
 */
@Component
public class EmailServiceImpl implements EmailService {
    private static final String UTF = "UTF-8";
    /**
     * Email that will send Emails.
     */
    private static final String USERNAME = "no-reply.ACCESS.2_3@outlook.com";
    /**
     * Password of the Email for sending Emails.
     */
    private static final String PASSWORD = "tUNMnT49(hzF>9ZC";

    @Override
    public void sendMail(final Email email) throws MessagingException {

            final Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", email.getSmtpHost());
            properties.put("mail.smtp.starttls.enable", "true");
            final Session session = Session.getInstance(properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });
            final MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getSentTo()));
            //UTF-8: translate any Unicode character;
            message.setSubject(email.getSubject(), UTF);
            message.setText(email.getMessage(), UTF);
            Transport.send(message, USERNAME, PASSWORD);
    }
}
