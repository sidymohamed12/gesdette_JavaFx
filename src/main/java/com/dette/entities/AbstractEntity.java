package com.dette.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@MappedSuperclass
public class AbstractEntity {

    @Column(name = "createdAt")
    protected LocalDateTime createdAt;

    @Column(name = "updatedAt")
    protected LocalDateTime updatedAt;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @OneToOne
    @JoinColumn(name = "createdBy", referencedColumnName = "id")
    protected User createdBy;

    @OneToOne
    @JoinColumn(name = "updatedBy", referencedColumnName = "id")
    protected User updatedBy;

    @PrePersist
    public void onPrePersist() {
        this.setCreatedAt(LocalDateTime.now());
        this.setUpdatedAt(LocalDateTime.now());
        this.setCreatedBy(UserConnect.getUserConnecte());
        this.setUpdatedBy(UserConnect.getUserConnecte());
    }

    @PreUpdate
    public void onPreUpdated() {
        this.setUpdatedAt(LocalDateTime.now());
        this.setUpdatedBy(UserConnect.getUserConnecte());
    }

}
