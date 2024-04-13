package gpse.example.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AuthorizationFilter.
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthorizationFilter.class);
    private final UserDetailsService userDetailsService;
    private final SecurityConstants securityConstants;

    /**
     * Sets securityConstant and userDetailService.
     *
     * @param authenticationManager the AuthenticationManager
     * @param userDetailsService    the UserDetailsService
     * @param securityConstants     the SecurityConstants
     */
    public JwtAuthorizationFilter(final AuthenticationManager authenticationManager,
                                  final UserDetailsService userDetailsService,
                                  final SecurityConstants securityConstants) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.securityConstants = securityConstants;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws IOException, ServletException {
        final UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if (authentication == null) {
            filterChain.doFilter(request, response);
            return;
        }

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(final HttpServletRequest request) {
        final String token = request.getHeader(securityConstants.getTokenHeader());
        if (token != null && !token.equals("") && token.startsWith(securityConstants.getTokenPrefix())) {
            try {
                final byte[] signingKey = securityConstants.getJwtSecret().getBytes();

                final Jws<Claims> parsedToken = Jwts.parserBuilder()
                        .setSigningKey(signingKey).build()
                        .parseClaimsJws(token.replace(securityConstants.getTokenPrefix(), "").strip());

                final String username = parsedToken.getBody().getSubject();

                final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (username != null && !username.equals("")) {
                    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                }
            } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
                    | IllegalArgumentException ex) {
                LOG.warn("Exeption: {} message: {}", ex.getClass(), ex.getMessage());
            }
        }
        return null;
    }
}

