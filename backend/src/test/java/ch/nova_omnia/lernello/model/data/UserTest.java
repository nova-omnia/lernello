package ch.nova_omnia.lernello.model.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("test@example.com", "password123", "en", User.Role.TRAINEE);
    }

    @Test
    void testConstructor() {
        assertEquals("test@example.com", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("en", user.getLanguage());
        assertEquals(User.Role.TRAINEE, user.getRole());
    }

    @Test
    void testSettersAndGetters() {
        user.setUsername("new@example.com");
        user.setPassword("newpassword");
        user.setLanguage("de");


        assertEquals("new@example.com", user.getUsername());
        assertEquals("newpassword", user.getPassword());
        assertEquals("de", user.getLanguage());
    }


    @Test
    void testChangedPasswordDefault() {
        assertFalse(user.isChangedPassword());
    }

    @Test
    void testTokenAndExpires() {
        user.setToken("sampleToken");
        user.setExpires(3600);

        assertEquals("sampleToken", user.getToken());
        assertEquals(3600, user.getExpires());
    }

    @Test
void testRoleCannotBeChanged() {
    User.Role initialRole = user.getRole();
    // Since the role is final, there is no setter for it. 
    // We can only verify that the role remains unchanged.
    assertEquals(User.Role.TRAINEE, initialRole);

    // Verify that the role is still the same
    assertEquals(initialRole, user.getRole());
}

}
