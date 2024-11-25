package org.example.rentcar.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.rentcar.security.user.UPCUserDetailsService;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@NoArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private JwtUtils jwtUtils;
    private UPCUserDetailsService upcUserDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request); // Extract JWT from the header
            if (StringUtils.hasText(jwt) && jwtUtils.validateToken(jwt)) {
                String email = jwtUtils.getUserNameFromToken(jwt);

                // Load user details and set authentication
                UserDetails userDetails = upcUserDetailsService.loadUserByUsername(email);
                var authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Log the exception for debugging
            System.err.println("Authentication error: " + e.getMessage());
        }

        // Continue the filter chain even if authentication fails
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headAuth) && headAuth.startsWith("Bearer ")) {
            return headAuth.substring(7); // Extract the token part
        }
        return null; // No token found
    }
}
