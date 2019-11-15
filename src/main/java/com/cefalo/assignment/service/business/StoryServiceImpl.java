package com.cefalo.assignment.service.business;

import com.cefalo.assignment.exception.EntityNotFoundException;
import com.cefalo.assignment.exception.UnAuthorizedRequestException;
import com.cefalo.assignment.model.business.StoryProperties;
import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.repositories.StoryRepository;
import com.cefalo.assignment.security.LoggedInUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Stream;

@Service
public class StoryServiceImpl implements StoryService{
    private final StoryRepository storyRepository;
    private final StoryProperties storyProperties;

    @Autowired
    public StoryServiceImpl(StoryRepository storyRepository,
                            @Qualifier("storyProperties")StoryProperties storyProperties){
        this.storyRepository = storyRepository;
        this.storyProperties = storyProperties;
    }

    @Override
    public Story saveNewStoryObject(Story story) {

        story.setId(null);
        story.setCreator(LoggedInUserInfo.getLoggedInUser());
        return storyRepository.save(story);
    }

    @Override
    public Optional<Story> getStoryById(Long storyId) throws EntityNotFoundException {
        Optional<Story> story = storyRepository.findById(storyId);

        if(!story.isPresent()) {
            throw  new EntityNotFoundException(Story.class, "ID", storyId.toString());
        }

        story.get().setCreatorName();
        return story;
    }

    /**method should have smaller size and single purpose but separating the concern cost a DB call*/
    @Override
    public Optional<Story> checkAuthorityThenUpdateStoryById
            (Long storyId, Story newVersionOfStory,Boolean isPatchUpdate)
            throws EntityNotFoundException, UnAuthorizedRequestException, IllegalAccessException {

        newVersionOfStory.setId(storyId);
        Optional<Story> olderVersionOfStory = storyRepository.findById(storyId);

        throwExceptionForInvalidStoryUpdateorDeleteRequest(storyId, olderVersionOfStory.orElse(null));

        if(isPatchUpdate)
            newVersionOfStory = updateOldStoryByNewStory(olderVersionOfStory.get(), newVersionOfStory);

        return Optional.ofNullable(storyRepository.save(newVersionOfStory));
    }

    /**method should have smaller size and single purpose but separating the concern cost a DB call*/
    @Override
    public int checkAuthorityThenDeleteStoryById(Long storyId) throws EntityNotFoundException, UnAuthorizedRequestException {
        Optional<Story> story = storyRepository.findById(storyId);

        throwExceptionForInvalidStoryUpdateorDeleteRequest(storyId, story.orElse(null));
        storyRepository.delete(story.get());
        return HttpStatus.OK.value();
    }

    /**Using java reflection API*/
    @Override
    public Story updateOldStoryByNewStory(Story olderVersionOfStory, Story newVersionOfStory)
            throws IllegalAccessException {

        HashSet<String> setOfFieldsToReplace = storyProperties.getSetOfReplaceFieldsOnUpdate();

        for(Field field: Story.class.getDeclaredFields()) {

            if (!setOfFieldsToReplace.contains(field.getName())) continue;

            if (Modifier.isPrivate(field.getModifiers()))  {
                field.setAccessible(true);
            }

            if(field.get(newVersionOfStory) == null) {
                field.set(newVersionOfStory, field.get(olderVersionOfStory));
            }
        }
        return newVersionOfStory;

    }

    private void throwExceptionForInvalidStoryUpdateorDeleteRequest(Long storyId, Story story) throws EntityNotFoundException, UnAuthorizedRequestException {
        if(story == null) {
            throw  new EntityNotFoundException(Story.class, "ID", storyId.toString());
        }

        if(!LoggedInUserInfo.getLoggedInUserName().equals(story.getCreatorName()))
            throw new UnAuthorizedRequestException(LoggedInUserInfo.getLoggedInUserName() + " is not authorized to update or delete story-" + storyId);
    }


    @Override
    public List<Story> findAllForPagination(int pageNumber,int limit, String columnName){
        HashSet<String> columnNameForPagination = storyProperties.getSetOfFieldsNameToUseInPagination();

        if(!columnNameForPagination.contains(columnName)){
            columnName = storyProperties.getDefaultPaginationColumnName();
        }

        Pageable pageable = PageRequest.of(
                (pageNumber < 0 ? 0 : pageNumber), limit, Sort.by(columnName).ascending()
        );

        List<Story> stories = storyRepository.findAll(pageable).toList();
        for(Story story : stories) { story.setCreatorName(); }
        return stories;
    }
}
