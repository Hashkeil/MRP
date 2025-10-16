package model;
import Instances.Instances;
import at.technikum.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    @Test
    void testUserBasicFields() {
        User user = Instances.TEST_USER_1;

        assertEquals(1L, user.getId());
        assertEquals("string1", user.getUsername());
        assertEquals("string1", user.getPassword());
        assertEquals("Action", user.getFavoriteGenre());
        assertEquals(3, user.getTotalRatings());
        assertEquals(4.3, user.getAverageGivenRating());
    }

    @Test
    void testEqualsAndHashCode() {
        User u1 = new User("string1", "string1");
        u1.setId(1L);

        User u2 = new User("string1", "string1");
        u2.setId(1L);

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void testToStringContainsUsername() {
        String result = Instances.TEST_USER_1.toString();
        assertTrue(result.contains("string1"));
    }

    @Test
    void testSettersChangeValues() {
        User user = new User();
        user.setId(10L);
        user.setUsername("newUser");
        user.setPassword("newPass");
        user.setFavoriteGenre("Comedy");

        assertEquals(10L, user.getId());
        assertEquals("newUser", user.getUsername());
        assertEquals("newPass", user.getPassword());
        assertEquals("Comedy", user.getFavoriteGenre());
    }
}
