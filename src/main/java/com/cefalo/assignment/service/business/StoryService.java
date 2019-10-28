package com.cefalo.assignment.service.business;

import com.cefalo.assignment.model.orm.Story;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface StoryService {
    Story saveNewStoryObject(Story story) throws Exception;
    List<Story> getAllStory();
    Optional<Story> getStoryById(Long storyId);
    Optional<Story> checkAuthorityThenUpdateStoryById(Long storyId, Story newVersionOfStory) throws Exception;
    int checkAuthorityThenDeleteStoryById(Long storyId);
    List<Story> findAll(int pageNumber, String columnName);
    Story updateOldStoryByNewStory(Story olderVersionOfStory, Story newVersionOfStory) throws IllegalArgumentException, IllegalAccessException;
}
