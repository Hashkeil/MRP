package model;
import Instances.Instances;
import at.technikum.model.Favorite;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FavoriteTest  {

    @Test
    void testBelongsToUser() {
        Favorite favorite = Instances.TEST_FAVORITE_1;

        assertTrue(favorite.belongsTo(Instances.TEST_USER_1.getId()));
        assertFalse(favorite.belongsTo(Instances.TEST_USER_2.getId()));
    }

    @Test
    void testToStringIncludesMediaId() {
        String str = Instances.TEST_FAVORITE_1.toString();
        assertTrue(str.contains(Instances.TEST_MEDIA_1.getId().toString()));
    }
}