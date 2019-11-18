package com.cefalo.assignment.service.business;

import com.cefalo.assignment.model.business.StoryProperties;
import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.model.orm.User;
import com.cefalo.assignment.repositories.StoryRepository;
import com.cefalo.assignment.security.LoggedInUserInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;

import java.util.Optional;

import static com.cefalo.assignment.TestHelper.stringToDate;
import static com.cefalo.assignment.security.LoggedInUserInfo.getLoggedInUser;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class TestGetStoryById {
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
    }

    @Test
    public void findStoryByIdSuccesfully() {
        Story story = new Story("t1", "b1", stringToDate("22 NOV 1994"));
        story.setId(1L);

        when(storyRepository.findById(anyLong())).thenReturn(Optional.of(story));

        Optional<Story> result = storyServiceImpl.getStoryById(1L);

    }
}
