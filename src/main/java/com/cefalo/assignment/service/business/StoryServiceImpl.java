package com.cefalo.assignment.service.business;

import com.cefalo.assignment.model.business.StoryPropertiesConfig;
import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.model.orm.User;
import com.cefalo.assignment.service.orm.StoryRepository;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StoryServiceImpl implements StoryService{
    @Autowired StoryRepository storyRepository;

    @Autowired
    @Qualifier("storyPropertiesConfig")
    StoryPropertiesConfig storyPropertiesConfig;

    String getLoggedInUserName(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public Optional<Story> postStoryObject(Story story) {
        if(story.getId() != null) return Optional.empty();

        /** setting current logged in user as creator */
        story.setCreator(new User( getLoggedInUserName() ));
        return Optional.ofNullable(storyRepository.save(story));
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
    public Optional<Story> updateStoryById(Long storyId, Story newVersionOfStory) throws Exception{
        newVersionOfStory.setId(storyId);
        Optional<Story> olderVersionOfStory = storyRepository.findById(storyId);

        if(olderVersionOfStory.isPresent() == false) {
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
        HashSet<String> setOfFieldsToReplace = storyPropertiesConfig.getSetOfReplaceFieldsOnUpdate();

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
    public long deleteStoryById(Long storyId) {
        Optional<Story> story = storyRepository.findById(storyId);
        if(!story.isPresent()) return storyPropertiesConfig.getDeleteNotFoundStatusCode();

        String storyCreatorName = story.get().getCreatorName();

        if(getLoggedInUserName().equals(storyCreatorName)) storyRepository.delete(story.get());
        return (getLoggedInUserName().equals(storyCreatorName)) ? storyPropertiesConfig.getDeleteOnSuccess()
                : storyPropertiesConfig.getDeleteNotAuthorizedStatusCode();
    }


    @Override
    public List<Story> findAll(int pageNumber, String columnName){
        HashSet<String> columnNameForPagination = storyPropertiesConfig.getSetOfFieldsNameToUseInPagination();

        if(!columnNameForPagination.contains(columnName)){
            columnName = storyPropertiesConfig.getDefaultPaginationColumnName();
        }

        Pageable pageable = PageRequest.of(
                (pageNumber < 0 ? 0 : pageNumber), storyPropertiesConfig.getArticlePerPage(), Sort.by(columnName).ascending()
        );
        return storyRepository.findAll(pageable).toList();
    }

    List<String> makeStringToStringList(String mainString, String seperator){
        return Arrays.stream(mainString.split(seperator))
                .collect(Collectors.toList());
    }
}
