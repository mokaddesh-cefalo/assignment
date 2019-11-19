package com.cefalo.assignment.service.business;

import com.cefalo.assignment.exception.EntityNotFoundException;
import com.cefalo.assignment.exception.UnAuthorizedRequestException;
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

import static com.cefalo.assignment.security.LoggedInUserInfo.getLoggedInUser;
import static com.cefalo.assignment.security.LoggedInUserInfo.getLoggedInUserName;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ LoggedInUserInfo.class})
public class TestThrowExceptionForInvalidStoryUpdateOrDeleteRequest {

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
        when(getLoggedInUserName()).thenReturn("fish");

        when(storyRepository.save(ArgumentMatchers.any(Story.class)))
                .then(returnsFirstArg());
    }

    @DisplayName("Logged in userName is 'fish' and story is null")
    @Test(expected = EntityNotFoundException.class)
    public void getEntityNotFoundException() {
        storyServiceImpl.throwExceptionForInvalidStoryUpdateOrDeleteRequest(1L, null);
    }

    @DisplayName("Logged in userName is 'fish' and story is not null")
    @Test(expected = UnAuthorizedRequestException.class)
    public void getUnAuthorizedRequestException() {

        Story story = new Story();
        story.setCreator(new User("i"));
        storyServiceImpl.throwExceptionForInvalidStoryUpdateOrDeleteRequest(1L, story);
    }


    @DisplayName("Logged in userName is 'fish' and story is not null")
    @Test
    public void isAOkRequest() {

        Story story = new Story();
        story.setCreator(new User("fish"));
        storyServiceImpl.throwExceptionForInvalidStoryUpdateOrDeleteRequest(1L, story);
    }
}
