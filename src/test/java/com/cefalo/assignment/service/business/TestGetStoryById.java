package com.cefalo.assignment.service.business;

import com.cefalo.assignment.exception.EntityNotFoundException;
import com.cefalo.assignment.model.business.StoryProperties;
import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.model.orm.User;
import com.cefalo.assignment.repositories.StoryRepository;
import com.cefalo.assignment.security.LoggedInUserInfo;
import org.assertj.core.error.ShouldBeAfterYear;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.*;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;

import java.util.Optional;

import static com.cefalo.assignment.TestHelper.stringToDate;
import static com.cefalo.assignment.security.LoggedInUserInfo.getLoggedInUser;
import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class TestGetStoryById {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    StoryProperties storyProperties;

    @Mock
    StoryRepository storyRepository;

    @Captor
    ArgumentCaptor<Long> longArgumentCaptor;

    @InjectMocks
    StoryServiceImpl storyServiceImpl;

    @Before
    public void setMockBeforeSaveAStory() {
        System.out.println("Here...");

    }

    @Test
    public void findStoryByIdSuccesfully() {
        Story story = new Story("t1", "b1", stringToDate("22 NOV 1994"));

        when(storyRepository.findById(anyLong())).thenReturn(Optional.of(story));

        Story result = storyServiceImpl.getStoryById(1L);
        Mockito.verify(storyRepository).findById(longArgumentCaptor.capture());

        assertEquals(result, story);
        assertEquals( (long)1, (longArgumentCaptor.getValue().longValue()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void findStoryById_EntityNotFoundException() {

        when(storyRepository.findById(anyLong()))
                .thenThrow(new EntityNotFoundException(Story.class, "not found for", 1L));

        storyServiceImpl.getStoryById(1L);

    }
}
