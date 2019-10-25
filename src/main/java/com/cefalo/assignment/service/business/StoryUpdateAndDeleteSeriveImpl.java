package com.cefalo.assignment.service.business;

import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.service.orm.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;

@Service
public class StoryUpdateAndDeleteSeriveImpl implements StoryUpdateAndDeleteSerive{

    @Autowired StoryRepository storyRepository;

    public Optional<Story> handleStoryUpdate(Long storyId, Story newVersionOfStory) throws IllegalAccessException {
        newVersionOfStory.setId(storyId);
        Optional<Story> olderVersionOfStory = storyRepository.findById(storyId);

        if(olderVersionOfStory.isPresent() == false) {
            return olderVersionOfStory;
        }

        newVersionOfStory = updateOldStoryByNewStory(olderVersionOfStory.get(), newVersionOfStory);
        return Optional.of(storyRepository.save(newVersionOfStory));
    }

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
}
