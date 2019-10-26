package com.cefalo.assignment.service.business;

import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.service.orm.StoryRepository;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StoryServiceImpl implements StoryService{
    @Autowired StoryRepository storyRepository;

    @Override
    public Optional<Story> postStoryObject(Story story) {
        return Optional.ofNullable(storyRepository.save(story));
    }

    @Override
    public List<Story> getAllStory(){
        List<Story> stories = new ArrayList<>();
        storyRepository.findAll().forEach(story -> stories.add(story));
        return stories;
    }

    @Override
    public Optional<Story> getStoryById(Long storyId){
        return storyRepository.findById(storyId);
    }

    @Override
    public Optional<Story> updateStoryById(Long storyId, Story newVersionOfStory) throws Exception{
        newVersionOfStory.setId(storyId);
        Optional<Story> olderVersionOfStory = storyRepository.findById(storyId);

        if(olderVersionOfStory.isPresent() == false) {
            return olderVersionOfStory;
        }else {
            newVersionOfStory = updateOldStoryByNewStory(olderVersionOfStory.get(), newVersionOfStory);
            return Optional.ofNullable(storyRepository.save(newVersionOfStory));
        }
    }

    @Override
    public Story updateOldStoryByNewStory(Story olderVersionOfStory, Story newVersionOfStory) throws IllegalArgumentException, IllegalAccessException {
        for(Field field: Story.class.getDeclaredFields()) {
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
    public void deleteStoryById(Long storyId) throws Exception{
        Optional<Story> story = storyRepository.findById(storyId);
        if(story.isPresent()) storyRepository.delete(story.get());
    }
}
