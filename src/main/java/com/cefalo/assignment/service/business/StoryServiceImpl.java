package com.cefalo.assignment.service.business;

import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.model.orm.User;
import com.cefalo.assignment.service.orm.StoryRepository;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StoryServiceImpl implements StoryService{
    @Autowired StoryRepository storyRepository;

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
        for(Field field: Story.class.getDeclaredFields()) {
            if(field.getName().equals("creator")) continue;

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
        if(!story.isPresent()) return 404;

        String storyCreatorName = story.get().getCreatorName();

        if(getLoggedInUserName().equals(storyCreatorName)) storyRepository.delete(story.get());
        return (getLoggedInUserName().equals(storyCreatorName)) ? 200 : 401;
    }


    @Override
    public List<Story> findAll(Pageable pageable){
        return storyRepository.findAll(pageable).toList();
    }
}
