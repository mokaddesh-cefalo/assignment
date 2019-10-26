package com.cefalo.assignment;

import com.cefalo.assignment.model.orm.Story;
import com.cefalo.assignment.service.business.StoryService;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
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

        ///newStory = storyUpdateAndDeleteSerive.updateOldStoryByNewStory(oldStory, newStory);
           for(Field field: Story.class.getDeclaredFields()) {

               if(field.getName().equals("creator")) continue;
                if (Modifier.isPrivate(field.getModifiers()))  {
                    field.setAccessible(true);
                }
                System.out.println(field.getName());
                if(field.get(newStory) == null && field.get(oldStory) != null) {
                    field.set(newStory, field.get(oldStory));
                }
            }
        System.out.println(newStory);
    }
}
