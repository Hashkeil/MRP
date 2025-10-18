package service;

import at.technikum.model.User;
import at.technikum.repository.UserRepository;
import at.technikum.exception.user.UserAlreadyExistsException;
import at.technikum.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;

import static Instances.Instances.TEST_USER_1;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private UserRepository userRepository;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        authService = new AuthService(userRepository);
    }

    @Test
    void testRegister_Success() throws SQLException {
        when(userRepository.existsByUsername("newUser")).thenReturn(false);
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User user = authService.register("newUser", "password");
        assertEquals("newUser", user.getUsername());
    }

    @Test
    void testRegister_AlreadyExists() throws SQLException {
        when(userRepository.existsByUsername("existing")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> authService.register("existing", "password"));
    }

    @Test
    void testLogin_Success() throws Exception {
        User u = new User("user", "pass");
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(u));
        doNothing().when(userRepository).updateUser(u);

        User logged = authService.login("user", "pass");
        assertNotNull(logged.getToken());
        assertTrue(logged.getToken().endsWith("-mrpToken"));
        verify(userRepository).updateUser(u);
    }

    @Test
    void testLogin_InvalidUsername() throws SQLException {
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
        Exception ex = assertThrows(Exception.class, () -> authService.login("user", "pass"));
        assertEquals("Invalid username or password", ex.getMessage());
    }

    @Test
    void testLogin_InvalidPassword() throws SQLException {
        User u = new User("user", "pass");
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(u));
        Exception ex = assertThrows(Exception.class, () -> authService.login("user", "wrong"));
        assertEquals("Invalid username or password", ex.getMessage());
    }

    @Test
    void testValidateToken_Success() throws Exception {
        when(userRepository.findByToken("token")).thenReturn(Optional.of(TEST_USER_1));
        User u = authService.validateToken("token");
        assertEquals(TEST_USER_1, u);
    }

    @Test
    void testValidateToken_Failure() throws SQLException {
        when(userRepository.findByToken("token")).thenReturn(Optional.empty());
        Exception ex = assertThrows(Exception.class, () -> authService.validateToken("token"));
        assertEquals("Invalid or expired token", ex.getMessage());
    }

    @Test
    void testExtractToken_Success() throws Exception {
        String token = authService.extractToken("Bearer myToken");
        assertEquals("myToken", token);
    }

    @Test
    void testExtractToken_Failure() {
        Exception ex = assertThrows(Exception.class, () -> authService.extractToken("InvalidHeader"));
        assertEquals("Invalid authorization format. Expected: Bearer <token>", ex.getMessage());
    }
}
