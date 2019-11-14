package com.cefalo.assignment.service.business;

import com.cefalo.assignment.exception.EntityNotFoundException;
import com.cefalo.assignment.model.business.StoryProperties;
import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.repositories.StoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.cefalo.assignment.TestHelper.stringToDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {StoryServiceImpl.class})
public class StoryServiceImplTest {

    @MockBean
    private StoryRepository storyRepository;
    @MockBean
    @Qualifier("storyProperties")
    private StoryProperties storyProperties;
    @SpyBean
    StoryServiceImpl storyService;
    @Captor
    ArgumentCaptor captor;


    @Test
    public void findByIdStory() {
        Story story = new Story("t1", "b1", stringToDate("24 nov 1994"));

        Mockito.when(storyRepository.findById(anyLong())).thenReturn(Optional.of(story));
        Story result = storyService.getStoryById(1L).orElse(null);

        Mockito.verify(storyRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(storyRepository).findById((Long)captor.capture());
        assertEquals(1L, (Long)captor.getValue());
        assertEquals(result, story, "Matched");
    }

    @Test
    public void findAllStory() {
        Story story = new Story("t1", "b1", stringToDate("24 nov 1994"));
        List<Story> stories = new ArrayList<>();

        stories.add(story); stories.add(story); stories.add(story); stories.add(story);
        Mockito.when(storyRepository.findAll()).thenReturn(stories);

        assertEquals(stories.size(), 4);
    }

    @Test(expected = EntityNotFoundException.class)
    public void findByIdStory_GetException() {
        storyService.getStoryById(1L);
    }


    @Test
    public void updateOldStoryByNewStoryTest() throws Exception{

        HashSet<String> newSet = new HashSet<>();

        newSet.add("title"); newSet.add("body"); newSet.add("publishedDate");
        when(storyProperties.getSetOfReplaceFieldsOnUpdate()).thenReturn(newSet);

        Story newStory = new Story();
        newStory.setTitle("toto");

        Story oldStory = new Story("t1", "b1", stringToDate("24 nov 1994"));

        storyService.updateOldStoryByNewStory(oldStory, newStory);

        assertEquals(oldStory.getBody(), newStory.getBody());
        assertEquals(oldStory.getPublishedDate(), newStory.getPublishedDate());
        assertNotEquals(oldStory.getTitle(), newStory.getTitle());
    }


    /*@Override
    public Optional<Story> checkAuthorityThenUpdateStoryById
            (Long storyId, Story newVersionOfStory,Boolean isPatchUpdate)
            throws EntityNotFoundException, UnAuthorizedRequestException, IllegalAccessException {

        newVersionOfStory.setId(storyId);
        Optional<Story> olderVersionOfStory = storyRepository.findById(storyId);

        throwExceptionForInvalidStoryUpdateorDeleteRequest(storyId, olderVersionOfStory.orElse(null));

        if(isPatchUpdate)
            newVersionOfStory = updateOldStoryByNewStory(olderVersionOfStory.get(), newVersionOfStory);

        return Optional.ofNullable(storyRepository.save(newVersionOfStory));
    }*/
}
