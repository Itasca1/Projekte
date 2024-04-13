package gpse.example.domain.entities;


/**
 * Model representation of an Email.
 */
public class Email {
    private String subject;
    private String message;
    private String sentBy;
    private String sentTo;
    private String smtpHost;

    /**
     * Constructor of email.
     * @param subject is Headline of the Email.
     * @param message is the message of the Email.
     * @param sentBy is the email that sends the Email.
     * @param sentTo is the email that will receive the email.
     */
    public Email(final String subject, final String message, final String sentBy, final String sentTo) {
        this.subject = subject;
        this.message = message;
        this.sentBy = sentBy;
        this.sentTo = sentTo;
        this.smtpHost = "smtp-mail.outlook.com";
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(final String sentBy) {
        this.sentBy = sentBy;
    }

    public String getSentTo() {
        return sentTo;
    }

    public void setSentTo(final String sentTo) {
        this.sentTo = sentTo;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(final String smtpHost) {
        this.smtpHost = smtpHost;
    }
}
