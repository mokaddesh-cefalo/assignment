package com.cefalo.assignment.model.orm;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
@ApiModel(description = "To Add a User send post request to '/api/users'<br>" +
        "To get all stories by user send get request to '/api/users/{userName}/stories'<br>" +
        "To get a User by UserName send get request to '/api/users/{userName}'<br>")
@Entity
@Table(name = "User")
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @ApiModelProperty(notes = "Should be unique and will be used as 'ID' for user table")
    @Id
    @Column(unique = true, nullable = false)
    private String userName;

    /**TODO password should be encoded*/
    @Column(nullable = false)
    private String password;

    private Boolean active;

    @ApiModelProperty(notes = "Roles user want to play if user want to play role of 'USER' and 'ADMIN'<br>" +
            "roles should be 'ROLE_USER,ROLE_ADMIN'<br>" +
            "Multiple roles will be seperated by ','<br>" +
            "Default role is 'ROLE_USER'")
    @Column
    private String roles;

    @Column(updatable = false)
    @ApiModelProperty(notes = "Created Date will be will be automatically assigned during creation of User")
    private LocalDateTime createdDate;
    @ApiModelProperty(notes = "Last modified date will be will be automatically assigned after each update of User")
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
