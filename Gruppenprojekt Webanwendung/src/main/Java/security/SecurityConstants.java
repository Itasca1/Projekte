package gpse.example.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SecurityConstants.
 */
@ConfigurationProperties("security")
public final class SecurityConstants {

    private String authLoginUrl;

    private String jwtSecret;

    // JWT Token-Standardwerte
    private String tokenHeader;
    private String tokenPrefix;
    private String tokenType;
    private String tokenIssuer;
    private String tokenAudience;

    public String getAuthLoginUrl() {
        return authLoginUrl;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public String getTokenHeader() {
        return tokenHeader;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getTokenIssuer() {
        return tokenIssuer;
    }

    public String getTokenAudience() {
        return tokenAudience;
    }

    public void setAuthLoginUrl(final String authLoginUrl) {
        this.authLoginUrl = authLoginUrl;
    }

    public void setJwtSecret(final String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public void setTokenHeader(final String tokenHeader) {
        this.tokenHeader = tokenHeader;
    }

    public void setTokenPrefix(final String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public void setTokenType(final String tokenType) {
        this.tokenType = tokenType;
    }

    public void setTokenIssuer(final String tokenIssuer) {
        this.tokenIssuer = tokenIssuer;
    }

    public void setTokenAudience(final String tokenAudience) {
        this.tokenAudience = tokenAudience;
    }
}

