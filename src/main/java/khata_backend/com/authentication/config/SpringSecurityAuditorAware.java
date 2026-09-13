package khata_backend.com.authentication.config;

import khata_backend.com.authentication.user.model.entity.Users;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * This class provides the current logged-in user's ID to Spring Data JPA Auditing.
 * Whenever an entity with @CreatedBy or @LastModifiedBy is saved, Spring will call getCurrentAuditor()
 * to know whose ID to store in the database.
 */
@Component("auditorAware") // Registers this class as a Spring Bean with the name "auditorAware"
public class SpringSecurityAuditorAware implements AuditorAware<Integer> {

    /**
     * Retrieves the current user's ID.
     * Returns Optional.empty() if no user is logged in, or Optional.of(userId) if a user is authenticated.
     */
    @Override
    public Optional<Integer> getCurrentAuditor() {
        // 1. Get the current authentication context from Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 2. Check if the authentication is missing, not authenticated, or is just an anonymous visitor
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty(); // No user is logged in
        }

        // 3. Retrieve the principal (the actual logged-in user object)
        Object principal = authentication.getPrincipal();

        // 4. If the principal is our custom Users entity, extract and return its ID
        if (principal instanceof Users) {
            return Optional.of(((Users) principal).getId());
        }

        // 5. Fallback in case the principal isn't what we expect
        return Optional.empty();
    }
}
