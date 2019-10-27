package com.cefalo.assignment.model.orm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Story implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false)
    private Long id;

    @Transient
    private String creatorName;

    @ManyToOne @JoinColumn(name = "user_name", nullable = false, updatable = false)
    @JsonIgnore
    private User creator;

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


    public String getCreatorName(){
        return (creator == null) ? null : creator.getUserName();
    }
    public void setCreatorName(){
        this.creatorName = getCreatorName();
    }

    @PrePersist
    void prePersist() {
        createdDate = LocalDateTime.now();
        lastModified = null;
    }

    @PreUpdate
    void preUpdate() {
        lastModified = LocalDateTime.now();
    }
}
