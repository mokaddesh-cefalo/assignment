package com.cefalo.assignment.service.business;

import com.cefalo.assignment.exception.EntityNotFoundException;
import com.cefalo.assignment.exception.UnAuthorizedRequestException;
import com.cefalo.assignment.model.business.StoryProperties;
import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.repositories.StoryRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Optional;

import static com.cefalo.assignment.TestHelper.stringToDate;
import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TestCheckAuthorityThenUpdateStoryById {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    StoryProperties storyProperties;

    @Mock
    StoryRepository storyRepository;

    @Captor
    ArgumentCaptor<Story> storyArgumentCaptor;

    @InjectMocks
    StoryServiceImpl storyServiceImpl;

    public StoryServiceImpl spyService;

    @Before
    public void setMockBeforeSaveAStory() {
        System.out.println("Here...");
        when(storyRepository.save(ArgumentMatchers.any(Story.class)))
                .then(returnsFirstArg());
        spyService = Mockito.spy(storyServiceImpl);
    }

    @Test
    public void successfullyPutOldStory() throws IllegalAccessException {

        doNothing()
                .when(spyService).throwExceptionForInvalidStoryUpdateOrDeleteRequest
                (any(), any());

        Story newStory
                = new Story("tin", null, stringToDate("22 NOV 1994"));

        Story result = spyService.checkAuthorityThenUpdateStoryById(1L, newStory, false);

        verify(storyRepository).save(storyArgumentCaptor.capture());
        assertEquals("tin", storyArgumentCaptor.getValue().getTitle());
        assertEquals((Long)1L, result.getId());
    }

    ///by default mocked method return null
    @Test(expected = EntityNotFoundException.class)
    public void putOldStory_getEntityNotFound_Oldstory_is_null() throws IllegalAccessException {

        Story newStory
                = new Story("tin", null, stringToDate("22 NOV 1994"));

        spyService.checkAuthorityThenUpdateStoryById(1L, newStory, false);
    }

    @Test(expected = UnAuthorizedRequestException.class)
    public void putOldStory_get_UnAuthorizedRequestException() throws IllegalAccessException {

        doThrow(new UnAuthorizedRequestException())
                .when(spyService).throwExceptionForInvalidStoryUpdateOrDeleteRequest
                (any(), any());

        Story newStory
                = new Story("tin", null, stringToDate("22 NOV 1994"));

        spyService.checkAuthorityThenUpdateStoryById(1L, newStory, false);
    }

    @Test
    public void successfullyPatchOldStory() throws IllegalAccessException {

        Story newStory
                = new Story("tin", null, stringToDate("22 NOV 1994"));

        doNothing()
                .when(spyService)
                .throwExceptionForInvalidStoryUpdateOrDeleteRequest(any(), any());

        doReturn(newStory).when(spyService).updateOldStoryByNewStory
                (null, newStory);

        spyService.checkAuthorityThenUpdateStoryById(1L, newStory, true);

        verify(spyService, times(1))
                .updateOldStoryByNewStory(any(), storyArgumentCaptor.capture());

        assertEquals(newStory, storyArgumentCaptor.getValue());

    }

}