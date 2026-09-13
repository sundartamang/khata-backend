package khata_backend.com.authentication.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration class to enable JPA Auditing.
 * This tells Spring Data JPA to automatically populate fields like @CreatedDate, @LastModifiedDate, @CreatedBy, and @LastModifiedBy.
 */
@Configuration // Marks this class as a source of bean definitions for the application context
// Enables the auditing feature and tells it to look for a bean named "auditorAware" to find the current user's ID
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaAuditingConfig {
}
