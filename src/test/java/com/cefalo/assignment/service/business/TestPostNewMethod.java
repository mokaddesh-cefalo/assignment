package com.cefalo.assignment.service.business;


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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

import static com.cefalo.assignment.TestHelper.stringToDate;
import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LoggedInUserInfo.class, StoryServiceImpl.class})
public class TestPostNewMethod {

    StoryServiceImpl storyService;
    @Mock
    StoryRepository storyRepository;
    @Mock
    StoryProperties storyProperties;

    @Before
    public void init() {
        PowerMockito.mockStatic(LoggedInUserInfo.class);
        when(LoggedInUserInfo.getLoggedInUserName()).thenReturn("fish");
        when(LoggedInUserInfo.getLoggedInUser()).thenReturn(new User("fish"));

        storyService = spy(new StoryServiceImpl(storyRepository, storyProperties));
    }

    @Test
    public void postNewUser() throws Exception {

        Story story = new Story("t1", "b1", stringToDate("24 nov 1994"));

        when(storyRepository.save(story)).thenReturn(story);
        Story result = storyService.saveNewStoryObject(story);
        result.setCreatorName();
        System.out.println(result.getCreatorName());
    }

    @DisplayName("checkAuthorityThenUpdateStoryById")
    @Test
    public void checkAuthorityThenUpdateStoryById() throws Exception {

        Story story = new Story("t1", "b1", stringToDate("24 nov 1994"));
        story.setId(1L);
        story.setCreator(new User("fish"));

        when(storyRepository.findById(anyLong())).thenReturn(Optional.of(story));
        when(storyRepository.save(Mockito.any(Story.class))).then(returnsFirstArg());

        Optional<Story> result = storyService.checkAuthorityThenUpdateStoryById(story.getId(), story, false);
        assertEquals(story, result.get());
    }

    @DisplayName("checkAuthorityThenUpdateStoryById_getException_FOR_Wrong_User")
    @Test(expected = UnAuthorizedRequestException.class)
    public void checkAuthorityThenUpdateStoryById_getException() throws Exception {

        Story story = new Story("t1", "b1", stringToDate("24 nov 1994"));
        story.setId(1L);
        story.setCreator(new User("i"));

        when(storyRepository.findById(anyLong())).thenReturn(Optional.of(story));
        when(storyRepository.save(Mockito.any(Story.class))).then(returnsFirstArg());

        Optional<Story> result = storyService.checkAuthorityThenUpdateStoryById(story.getId(), story, false);
        assertEquals(story, result.get());
    }
}
