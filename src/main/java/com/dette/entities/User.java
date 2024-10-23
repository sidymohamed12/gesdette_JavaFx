package com.dette.entities;

import com.dette.enums.Role;
import com.dette.enums.RoleConvert;

import javax.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "users")
public class User extends AbstractEntity {

    @Column(length = 30, unique = true)
    private String login;

    @Column(length = 60)
    private String password;

    @Convert(converter = RoleConvert.class)
    @Column(name = "roleId")
    private Role role;

    private Boolean etat;

    @OneToOne(mappedBy = "user")
    @EqualsAndHashCode.Exclude
    private Client client;

    public User(String login, String password, Role role, Boolean etat) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.etat = etat;
    }

    public User(String login, String password, Role role, Boolean etat, Client client) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.etat = etat;
        this.client = client;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", login=" + login + ", password=" + password + ", role=" + role + ", etat="
                + etat
                + "]";
    }

    // mappedby on le met dans laclasse qui ne dois pa avoir la clé etranger
}
