package com.etz.authorisationserver.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_log")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EventLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint")
    private long id;

    @Column(name = "event_time")
    private LocalDateTime eventTime;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "event_desc")
    private String eventDesc;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", columnDefinition = "bigint", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_USER_ROLE_USER_ID"))
    private User user;
}
