package com.etz.authorisationserver.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;



@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class BaseEntity  {

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    @UpdateTimestamp
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
 
    @Column(name = "updated_by")
    private String updatedBy;

    @Column(nullable = false, name = "deleted", columnDefinition = "TINYINT default 0", length = 1)
    private Boolean deleted = Boolean.FALSE;

//    /**
//     * The entity instance version used for optimistic locking.
//     */
//    @Version
//    @Column(name = "version", columnDefinition = "bigint DEFAULT 0", nullable = false)
//    private Long version = 0L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity)) return false;
        BaseEntity that = (BaseEntity) o;
        return createdAt.equals(that.createdAt) && Objects.equals(createdBy, that.createdBy) && updatedAt.equals(that.updatedAt) && updatedBy.equals(that.updatedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, createdBy, updatedAt, updatedBy);
    }
}