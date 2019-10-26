package com.cefalo.assignment;

import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.service.business.StoryService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest
public class StoryUpdateAndDeleteSeriveImplTest {

    @MockBean
    StoryService storyUpdateAndDeleteSerive;

    @Test
    void contextLoads() throws Exception {
        Story newStory = new Story();
        newStory.setTitle("Birth Day");
        newStory.setPublishedDate("24 nov 1994");

        Story oldStory = new Story();
        oldStory.setTitle("Papi");
        oldStory.setBody("Please try to be a good person");

        newStory = storyUpdateAndDeleteSerive.updateOldStoryByNewStory(oldStory, newStory);
        System.out.println(newStory);
    }
}
