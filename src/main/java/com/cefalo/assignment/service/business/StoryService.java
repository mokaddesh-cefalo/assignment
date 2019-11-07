package com.cefalo.assignment.service.business;

import com.cefalo.assignment.exception.EntityNotFoundException;
import com.cefalo.assignment.exception.UnAuthorizedRequestException;
import com.cefalo.assignment.model.orm.Story;

import java.util.List;
import java.util.Optional;

public interface StoryService {
    Story saveNewStoryObject(Story story);
    List<Story> getAllStory();
    Optional<Story> getStoryById(Long storyId) throws EntityNotFoundException;
    Optional<Story> checkAuthorityThenUpdateStoryById(Long storyId, Story newVersionOfStory, Boolean isPatchUpdate)
            throws EntityNotFoundException, UnAuthorizedRequestException, IllegalAccessException ;
    int checkAuthorityThenDeleteStoryById(Long storyId) throws EntityNotFoundException, UnAuthorizedRequestException;
    List<Story> findAllForPagination(int pageNumber,int limit, String columnName);
    Story updateOldStoryByNewStory(Story olderVersionOfStory, Story newVersionOfStory) throws IllegalArgumentException, IllegalAccessException;
}
