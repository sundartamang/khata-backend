package khata_backend.com.authentication.user.service;

import khata_backend.com.authentication.user.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService  {

    @Autowired
    private UserRepo userRepo;

    /**
     * Load a user by username (email) for Spring Security.
     *
     * <p>This method is called by Spring Security during authentication.
     * It looks up the user by email and returns the matching user object
     * that implements UserDetails.</p>
     *
     * @param username the email used as the login username
     * @return the authenticated user as a UserDetails instance
     * @throws UsernameNotFoundException if no user exists with the given email
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}
