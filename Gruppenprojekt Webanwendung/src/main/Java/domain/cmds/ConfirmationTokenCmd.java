package gpse.example.domain.cmds;

import gpse.example.domain.entities.User;

import java.util.Date;

/**
 * Command object for ConfirmationToken.
 * Encapsulates all relevant information needed by api action.
 */
public class ConfirmationTokenCmd {
    private Long tokenId;
    private String confirmationToken;
    private Date createdDate;
    private User user;

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(final Long tokenId) {
        this.tokenId = tokenId;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(final String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }
}
