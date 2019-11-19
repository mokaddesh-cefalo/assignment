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
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Optional;

import static com.cefalo.assignment.TestHelper.stringToDate;
import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class TestCheckAuthorityThenDeleteStoryById {
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

    public StoryServiceImpl spyService;

    @Before
    public void setMockBeforeSaveAStory() {
        System.out.println("Here...");
        spyService = Mockito.spy(storyServiceImpl);

        Story newStory
                = new Story("tin", null, stringToDate("22 NOV 1994"));
        newStory.setId(1L);

        when(storyRepository.findById(1L)).thenReturn(Optional.of(newStory));
    }

    @Test
    public void successfullyDeleteStory() throws IllegalAccessException {

        doNothing()
                .when(spyService).throwExceptionForInvalidStoryUpdateOrDeleteRequest
                (any(), any());

        int result = spyService.checkAuthorityThenDeleteStoryById(1L);
        assertEquals(result, 200);
        verify(storyRepository, times(1)).delete(any());
        verify(storyRepository, times(1))
                .findById(longArgumentCaptor.capture());

        assertEquals((Long)1L, longArgumentCaptor.getValue());
    }

    @Test(expected = UnAuthorizedRequestException.class)
    public void deleteStory_getUnAuthorizedRequestException() throws IllegalAccessException {

        doThrow(new UnAuthorizedRequestException())
                .when(spyService).throwExceptionForInvalidStoryUpdateOrDeleteRequest
                (any(), any());

        spyService.checkAuthorityThenDeleteStoryById(1L);
    }
}
