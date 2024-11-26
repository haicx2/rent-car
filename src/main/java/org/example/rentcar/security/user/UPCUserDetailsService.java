package org.example.rentcar.security.user;

import lombok.RequiredArgsConstructor;
import org.example.rentcar.model.User;
import org.example.rentcar.repository.UserRepository;
import org.example.rentcar.utils.FeedBackMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UPCUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(FeedBackMessage.NOT_FOUND));
        return UPCUserDetails.buildUserDetails(user);
    }
}
