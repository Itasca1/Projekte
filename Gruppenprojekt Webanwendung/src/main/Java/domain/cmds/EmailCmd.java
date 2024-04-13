package gpse.example.domain.cmds;

/**
 * Command object for Email.
 * Encapsulates all relevant information needed by api action.
 */
public class EmailCmd {
    private String subject;
    private String message;
    private String sentBy;
    private String sentTo;
    private String host;

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

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }
}
