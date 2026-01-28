package com.projects.project2.repository;

import com.projects.project2.model.User;
import com.projects.project2.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepo;

    @Test
    public void UserRepository_Save_ReturnSavedUser() {
        User user = User.builder().username("antonio").password("123").build();

        User savedUser = userRepo.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void UserRepository_FindAll_ReturnMoreThanOneTask() {
        User user1 = User.builder().username("antonio").password("123").build();
        User user2 = User.builder().username("joao").password("123").build();

        userRepo.save(user1);
        userRepo.save(user2);
        List<User> userList = userRepo.findAll();

        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(2);
    }

    @Test
    public void UserRepository_FindById_ReturnNotNull() {
        User user = User.builder().username("antonio").password("123").build();

        userRepo.save(user);
        User returnedUser = userRepo.findById(user.getId()).get();

        assertThat(returnedUser).isNotNull();
    }

    @Test
    public void UserRepository_FindByUsername_ReturnNotNull() {
        User user = User.builder().username("antonio").password("123").build();

        userRepo.save(user);
        User returnedUser = userRepo.findByUsername(user.getUsername()).get();

        assertThat(returnedUser).isNotNull();
    }

    @Test
    public void UserRepository_FindByUsername_ReturnEmpty() {
        Optional<User> returnedUser = userRepo.findByUsername("nonexistent");

        assertThat(returnedUser).isEmpty();
    }

    @Test
    public void UserRepository_UpdateUser_ReturnNotNull() {
        User user = User.builder().username("antonio").password("123").build();
        userRepo.save(user);

        User returnedUser = userRepo.findByUsername(user.getUsername()).get();
        returnedUser.setUsername("jose");
        User updatedUser = userRepo.save(returnedUser);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getUsername()).isEqualTo("jose");
    }

    @Test
    public void UserRepository_DeleteUser_ReturnUserEmpty() {
        User user = User.builder().username("antonio").password("123").build();

        userRepo.save(user);
        userRepo.deleteById(user.getId());
        Optional<User> returnedUser = userRepo.findById(user.getId());

        assertThat(returnedUser).isEmpty();
    }
}
