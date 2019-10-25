package com.cefalo.assignment.service.business;

import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.service.orm.StoryRepository;

import java.util.Optional;

public interface StoryUpdateAndDeleteSerive {
    Optional<Story> handleStoryUpdate(Long storyId, Story newVersionOfStory) throws IllegalAccessException;
    Story updateOldStoryByNewStory(Story olderVersionOfStory, Story newVersionOfStory) throws IllegalArgumentException, IllegalAccessException;
    void handleStoryDelete(Long storyId) throws Exception;
}
