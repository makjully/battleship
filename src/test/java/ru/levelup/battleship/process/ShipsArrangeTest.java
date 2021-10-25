package ru.levelup.battleship.process;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.levelup.battleship.TestConfiguration;
import ru.levelup.battleship.model.Ship;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ShipsArrangeTest {

    @Autowired
    private ShipsArrange shipsArrange;

    @Test
    public void arrangeShips() {
        List<Ship> ships = shipsArrange.arrangeAllShips();

        ships.forEach(ship -> ship.getLocation().forEach(cell ->
                System.out.println(cell.getCoordinateX() + " " + cell.getCoordinateY())));

        Assert.assertEquals(10, ships.size());
        Assert.assertEquals(4, ships.get(0).getLocation().size());
        Assert.assertEquals(3, ships.get(2).getLocation().size());
        Assert.assertEquals(2, ships.get(3).getLocation().size());
        Assert.assertEquals(1, ships.get(9).getLocation().size());
    }
}