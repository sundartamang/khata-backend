package khata_backend.com.common.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * This is a base entity class that contains common fields (like createdBy, createdAt)
 * which will be inherited by other entities (like Department) to avoid duplicating code.
 */
// Tells JPA to use AuditingEntityListener, which automatically captures when and who created/modified this entity
@EntityListeners(AuditingEntityListener.class)
// Tells JPA that this class is NOT a separate table, but its fields should be mapped as columns in the tables of its subclasses
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private Integer createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    private Integer updatedBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
