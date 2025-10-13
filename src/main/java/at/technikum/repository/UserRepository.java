package at.technikum.repository;

import at.technikum.model.User;

import java.util.*;

public class UserRepository {


    private final Map<Long, User> users = new HashMap<>();
    private final Map<String, User> usersByUsername = new HashMap<>();
    private final Map<String, User> usersPassword = new HashMap<>();

    private Long nextId = 1L;

    //add User
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(nextId++);
        }
        users.put(user.getId(), user);
        usersByUsername.put(user.getUsername(), user);
        usersPassword.put(user.getPassword(), user);
        return user;
    }

    //find user by id or username
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(usersByUsername.get(username));
    }
    public Optional<User> findByToken(String token) {
        return users.values().stream()
                .filter(user -> token.equals(user.getToken()))
                .findFirst();
    }



    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public boolean existsByUsername(String username) {
        return usersByUsername.containsKey(username);
    }



    public boolean delete(Long id) {
        User user = users.remove(id);
        if (user != null) {
            usersByUsername.remove(user.getUsername());
            return true;
        }
        return false;
    }

    public void updateUser(User user) {
        if (user.getId() != null && users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            usersByUsername.put(user.getUsername(), user);
        }
    }


}