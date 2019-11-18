package com.cefalo.assignment.service.business;


import com.cefalo.assignment.model.business.StoryProperties;
import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.model.orm.User;
import com.cefalo.assignment.repositories.StoryRepository;
import com.cefalo.assignment.security.LoggedInUserInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.cefalo.assignment.TestHelper.stringToDate;
import static com.cefalo.assignment.security.LoggedInUserInfo.getLoggedInUser;
import static org.junit.Assert.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ LoggedInUserInfo.class})
public class TestSaveNewStory {

    @Mock
    StoryProperties storyProperties;

    @Mock
    StoryRepository storyRepository;

    @Captor
    ArgumentCaptor<Story> storyArgumentCaptor;

    @InjectMocks
    StoryServiceImpl storyServiceImpl;

    @Before
    public void setMockBeforeSaveAStory() {
        System.out.println("Here...");

        PowerMockito.mockStatic(LoggedInUserInfo.class);
        when(getLoggedInUser()).thenReturn(new User("fish"));

        when(storyRepository.save(ArgumentMatchers.any(Story.class)))
                .then(returnsFirstArg());
    }

    @DisplayName("Save a story and add logged in user as creator")
    @Test
    public void successfullySaveStory() {
        Story story = new Story("t1", "b1", stringToDate("22 NOV 1994"));
        Story result = storyServiceImpl.saveNewStoryObject(story);

        verify(storyRepository).save(storyArgumentCaptor.capture());
        story.setCreator(getLoggedInUser());

        assertNotNull(result.getCreator());
        assertEquals(story, result);

    }

    @DisplayName("Save a new story and remove ID if provided")
    @Test
    public void successfullySaveStory_idWillBeNullEvenIfProvided() {
        Story story = new Story("t1", "b1", stringToDate("22 NOV 1994"));
        story.setId(1L);

        storyServiceImpl.saveNewStoryObject(story);
        assertNull(story.getId());

    }
}