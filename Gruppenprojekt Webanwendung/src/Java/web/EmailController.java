package gpse.example.web;

import gpse.example.domain.entities.Email;
import gpse.example.domain.cmds.EmailCmd;
import gpse.example.domain.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

/**
 * Controller for emails.
 */
@SuppressWarnings("ClassCanBeRecord")
@RestController
@CrossOrigin
@RequestMapping("/api")
public class EmailController {
    private final EmailService emailService;

    @Autowired
    public EmailController(final EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Method to send Emails.
     *
     * @param emailCmd Commandobject for sending Emails
     */
    @PostMapping("/sendEmail")
    public void sendMail(final @RequestBody EmailCmd emailCmd) throws MessagingException {
        final Email tempEmail = new Email(
            emailCmd.getSubject(),
            emailCmd.getMessage(),
            emailCmd.getSentBy(),
            emailCmd.getSentTo());
        emailService.sendMail(tempEmail);
    }


}
