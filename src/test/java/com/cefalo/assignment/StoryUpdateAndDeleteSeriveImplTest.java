package com.cefalo.assignment;

import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.service.business.StoryUpdateAndDeleteSerive;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class StoryUpdateAndDeleteSeriveImplTest {

    @Autowired
    StoryUpdateAndDeleteSerive storyUpdateAndDeleteSerive;



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
