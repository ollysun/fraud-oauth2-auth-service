package com.etz.authorisationserver.entity;

import com.etz.authorisationserver.util.RequestContext;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

import org.joda.time.DateTime;


@Getter
@Setter
@ToString
@MappedSuperclass
public class BaseEntity implements Serializable {

    @Column(name = "created_at")
    private DateTime createdAt;

    @NotBlank(message = "Please enter the name of the creator")
    @Column(name = "created_by")
    private String createdBy;
 
    @Column(name = "updated_at")
    private DateTime updatedAt;
 
    @Column(name = "updated_by")
    private String updatedBy;

    /**
     * The entity instance version used for optimistic locking.
     */
    @Version
    private Integer version;

    @PrePersist
    public void beforePersist() {
        String username = RequestContext.getUsername();
        if (username == null) {
            throw new IllegalArgumentException(
                    "Cannot persist a TransactionalEntity without a username "
                            + "in the RequestContext for this thread.");
        }
        setCreatedBy(username);

        setCreatedAt(new DateTime());
    }

    /**
     * A listener method which is invoked on instances of TransactionalEntity
     * (or their subclasses) prior to being updated. Sets the
     * <code>updated</code> audit values for the entity. Attempts to obtain this
     * thread's instance of username from the RequestContext. If none exists,
     * throws an IllegalArgumentException. The username is used to set the
     * <code>updatedBy</code> value. The <code>updatedAt</code> value is set to
     * the current timestamp.
     */
    @PreUpdate
    public void beforeUpdate() {
        String username = RequestContext.getUsername();
        if (username == null) {
            throw new IllegalArgumentException(
                    "Cannot update a TransactionalEntity without a username "
                            + "in the RequestContext for this thread.");
        }
        setUpdatedBy(username);

        setUpdatedAt(new DateTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity)) return false;
        BaseEntity that = (BaseEntity) o;
        return createdAt.equals(that.createdAt) && Objects.equals(createdBy, that.createdBy) && updatedAt.equals(that.updatedAt) && updatedBy.equals(that.updatedBy) && version.equals(that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, createdBy, updatedAt, updatedBy, version);
    }
}