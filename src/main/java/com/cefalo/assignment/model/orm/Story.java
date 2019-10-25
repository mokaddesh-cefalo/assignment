package com.cefalo.assignment.model.orm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
public class Story implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @JsonIgnore @Transient
    private String error;

    private String title;
    private String body;
    private String publishedDate;

    public Story(String title, String body, String publishedDate){
        this.title = title;
        this.body = body;
        this.publishedDate = publishedDate;
    }

    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime lastModified;

    @PrePersist
    void prePersist() {
        createdDate = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        lastModified = LocalDateTime.now();
    }
}
