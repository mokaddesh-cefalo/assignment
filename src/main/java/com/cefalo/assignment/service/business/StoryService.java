package com.cefalo.assignment.service.business;

import com.cefalo.assignment.model.orm.Story;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface StoryService {
    Optional<Story> postStoryObject(Story story);
    List<Story> getAllStory();
    Optional<Story> getStoryById(Long storyId);
    Optional<Story> updateStoryById(Long storyId, Story newVersionOfStory) throws Exception;
    void deleteStoryById(Long storyId) throws Exception;
    Story updateOldStoryByNewStory(Story olderVersionOfStory, Story newVersionOfStory) throws IllegalArgumentException, IllegalAccessException;
}
