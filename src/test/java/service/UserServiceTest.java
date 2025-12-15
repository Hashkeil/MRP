package service;

import at.technikum.model.User;
import at.technikum.repository.RatingRepository;
import at.technikum.repository.UserRepository;
import Instances.Instances;
import at.technikum.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private RatingRepository ratingRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        ratingRepository = mock(RatingRepository.class);
        userService = new UserService(userRepository, ratingRepository);
    }

    @Test
    void testGetUserProfile() throws SQLException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(Instances.TEST_USER_1));

        User result = userService.getUserProfile(1L);

        assertEquals("string1", result.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }


    @Test
    void testUpdateUserProfile_ChangeGenre() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(Instances.TEST_USER_1));

        User updated = userService.updateUserProfile(1L, "Comedy");

        assertEquals("Comedy", updated.getFavoriteGenre());
        verify(userRepository, times(1)).updateUser(updated);
    }




}
