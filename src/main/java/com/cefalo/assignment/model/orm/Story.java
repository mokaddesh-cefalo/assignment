package com.cefalo.assignment.model.orm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "To Add a story send post request to '/api/stories'<br>" +
        "To get all stories send get request to '/api/stories'<br>" +
        "To get a story by ID send get request to '/api/stoies/{storyId}'<br>" +
        "To update a story by ID send post request to '/api/stoies/{storyId}'<br>" +
        "To Delete a story by ID send delete request to '/api/stoies/{storyId}'<br>" +
        "Only user who created the story can delete or update the story<br>" +
        "To get story using pagination send get request to '/api/stories/pagination'")
public class Story implements Serializable {

    @ApiModelProperty(notes = "Do not include note 'ID' in create request, " +
            "a automatically generated 'ID' will assigned to ID on creation time" +
            "providing ID on creation time can throw exception")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false)
    private Long id;

    @ApiModelProperty(notes = "Name of the User who created the story, " +
            "it will be automatically assigned during creation of story")
    @Transient
    private String creatorName;

    @ManyToOne @JoinColumn(name = "user_name", nullable = false, updatable = false)
    @JsonIgnore
    private User creator;

    @ApiModelProperty(notes = "Title of the story should keep as small as possible, it can be null")
    private String title;
    @ApiModelProperty(notes = "Body of the story can be null")
    private String body;

    @ApiModelProperty(notes = "publishedDate should be a date converted into string will not check if it actually a date")
    private String publishedDate;

    public Story(String title, String body, String publishedDate){
        this.title = title;
        this.body = body;
        this.publishedDate = publishedDate;
    }

    @Column(updatable = false)
    @ApiModelProperty(notes = "Created Date will be will be automatically assigned during creation of story")
    private LocalDateTime createdDate;
    @ApiModelProperty(notes = "Last modified date will be will be automatically assigned after each update of story")
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
