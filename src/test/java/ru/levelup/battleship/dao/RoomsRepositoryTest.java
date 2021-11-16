package ru.levelup.battleship.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.levelup.battleship.TestConfiguration;
import ru.levelup.battleship.model.Room;
import ru.levelup.battleship.model.User;

import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class RoomsRepositoryTest {

    @Autowired
    private RoomsRepository roomsRepository;

    @Autowired
    private UsersRepository usersRepository;

    private List<User> users;
    private List<Room> rooms;
    private Random random;

    @Before
    public void init() {
        random = new Random();
        users = new ArrayList<>();
        rooms = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            User user = new User("user" + i, "pswd_" + i);
            users.add(user);
            usersRepository.save(user);
        }

        for (int i = 0; i < 10; i++) {
            Room room = new Room(users.get(i));
            rooms.add(room);
            if (i >= 5)
                room.setAccepting(users.get(i + 5));

            roomsRepository.save(room);
        }
    }

    @Test
    public void findAvailableRoomsTest() {
        Page<Room> actual = roomsRepository.findRoomsByAcceptingIsNullOrderByTimestampDesc(
                PageRequest.of(0, 10));

        List<Room> expected = rooms.stream()
                .filter(room -> Objects.isNull(room.getAccepting()))
                .sorted(Comparator.comparing(Room::getTimestamp).reversed())
                .collect(Collectors.toList());

        Assert.assertEquals(expected, actual.toList());
    }

    @Test
    public void createRoomTest() {
        User user = users.get(random.nextInt(users.size()));
        usersRepository.save(user);
        Room room = new Room(user);
        Room saved = roomsRepository.save(room);

        Assert.assertNotNull(saved);
        Assert.assertEquals(user.getLogin(), saved.getInviter().getLogin());
    }

    @Test
    public void updateRoomTest() {
        Room room = rooms.stream()
                .filter(r -> Objects.isNull(r.getAccepting()))
                .findAny()
                .orElseThrow();

        User accepting = users.stream()
                .filter(user -> !Objects.equals(user, room.getInviter()))
                .findAny()
                .orElseThrow();

        Room saved = roomsRepository.updateRoom(room, accepting);

        Assert.assertNotNull(saved);
        Assert.assertEquals(accepting.getId(), saved.getAccepting().getId());
    }

    @Test
    public void findRoomByUserTest() {
        User user = users.get(random.nextInt(users.size()));

        Room expecting = rooms.stream()
                .filter(r -> Objects.equals(r.getAccepting(), user) || Objects.equals(r.getInviter(), user))
                .findFirst()
                .orElse(null);

        Room actual = roomsRepository.findRoomByUser(user);

        Assert.assertEquals(expecting, actual);
    }
}