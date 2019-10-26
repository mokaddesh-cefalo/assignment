package com.cefalo.assignment.model.orm;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "User")
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @Id
    @Column(unique = true, nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    private Boolean active;

    @Column
    private String roles;

    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime lastModified;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Story> stories;

    public User(String userName){
        this.userName = userName;
    }

    @PrePersist
    void prePersist() {
        if(roles == null) roles = "ROLE_USER";
        createdDate = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        lastModified = LocalDateTime.now();
    }
}
