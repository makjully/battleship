package ru.levelup.battleship.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.levelup.battleship.TestConfiguration;
import ru.levelup.battleship.model.User;

import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class UsersRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    private User user;

    @Before
    public void init() {
        user = new User("ahsoka", "snips_000");
        usersRepository.save(user);
    }

    @Test
    public void createUserTest() {
        User user = usersRepository.createUser("login", "password");
        Optional<User> savedUser = usersRepository.findById(user.getId());

        Assert.assertTrue(savedUser.isPresent());
    }

    @Test
    public void updateRatingTest() {
        User updated = usersRepository.updateRating(user, 10.0);

        Assert.assertEquals(10.0, updated.getRating(), 0);
    }

    @Test
    public void isPlayerFieldArrangedTest() {
        usersRepository.updateWhenBoardPrepared(user);
        Optional<User> updated = usersRepository.findById(user.getId());

        Assert.assertTrue(updated.stream().iterator().next().isPlayerFieldArranged());
    }

    @Test
    public void findByLoginTest() {
        User user_1 = usersRepository.findByLogin("ahsoka");
        User user_2 = usersRepository.findByLogin("anakin");

        Assert.assertNotNull(user_1);
        Assert.assertNull(user_2);
    }
}