package com.cefalo.assignment.service.business;

import com.cefalo.assignment.model.business.StoryProperties;
import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.model.orm.User;
import com.cefalo.assignment.service.orm.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

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

    private String getLoggedInUserName(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public Story saveNewStoryObject(Story story) throws Exception {
        if(story.getId() != null) throw new Exception("Request Body Should not contain 'ID' field!");

        /** setting current logged in user as creator */
        /**TODO assign on prepersist*/
        story.setCreator(new User( getLoggedInUserName() ));
        return storyRepository.save(story);
    }

    @Override
    public List<Story> getAllStory(){
        List<Story> stories = new ArrayList<>();
        storyRepository.findAll().forEach(story -> {
            story.setCreatorName();
            stories.add(story);
        });
        return stories;
    }

    @Override
    public Optional<Story> getStoryById(Long storyId){
        Optional<Story> story = storyRepository.findById(storyId);
        if(story.isPresent()) story.get().setCreatorName();
        return story;
    }

    @Override
    public Optional<Story> checkAuthorityThenUpdateStoryById(Long storyId, Story newVersionOfStory) throws Exception{
        newVersionOfStory.setId(storyId);
        Optional<Story> olderVersionOfStory = storyRepository.findById(storyId);

        if(!olderVersionOfStory.isPresent()) {
            return olderVersionOfStory;
        }

        String storyCreatorName = olderVersionOfStory.get().getCreatorName();

        if(getLoggedInUserName().equals(storyCreatorName)){
            newVersionOfStory = updateOldStoryByNewStory(olderVersionOfStory.get(), newVersionOfStory);
            return Optional.ofNullable(storyRepository.save(newVersionOfStory));
        } else {
            throw new Exception(getLoggedInUserName() + " is not authorized to update " + storyId);
        }
    }

    /**Using java reflection API*/
    @Override
    public Story updateOldStoryByNewStory(Story olderVersionOfStory, Story newVersionOfStory) throws IllegalArgumentException, IllegalAccessException {
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

    @Override
    public int checkAuthorityThenDeleteStoryById(Long storyId) {
        Optional<Story> story = storyRepository.findById(storyId);

        if(!story.isPresent()) {
            return HttpStatus.NOT_FOUND.value();// storyProperties.getDeleteNotFoundStatusCode();
        }

        String storyCreatorName = story.get().getCreatorName();

        if(getLoggedInUserName().equals(storyCreatorName)) {
            storyRepository.delete(story.get());
            return HttpStatus.OK.value();
        }
        return HttpStatus.UNAUTHORIZED.value();
    }


    @Override
    public List<Story> findAll(int pageNumber, String columnName){
        HashSet<String> columnNameForPagination = storyProperties.getSetOfFieldsNameToUseInPagination();

        if(!columnNameForPagination.contains(columnName)){
            columnName = storyProperties.getDefaultPaginationColumnName();
        }

        Pageable pageable = PageRequest.of(
                (pageNumber < 0 ? 0 : pageNumber), storyProperties.getArticlePerPage(), Sort.by(columnName).ascending()
        );
        return storyRepository.findAll(pageable).toList();
    }
}
