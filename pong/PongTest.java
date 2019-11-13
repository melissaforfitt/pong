import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import pong.Ball;

class PongTest {

    Pong testPong = new Pong();

    @Test
    void testMoveX() {

        Pong testPong = new Pong();
        Ball testBall = new Ball(300, 200, 2, 2, "test");

        testPong.move(testBall);

        assertEquals(302, testBall.getPositionX());

    }

    @Test
    void testMoveY() {

        Ball testBall = new Ball(452, 256, 2, 2, "test");

        testPong.move(testBall);

        assertEquals(258, testBall.getPositionY());

    }

    @Test
    void testOnWallCollisionX() {

        Ball testBall = new Ball(438, 342, 3, 3, "test");

        testPong.onWallCollision(testBall, true, false);

        assertEquals(-3, testBall.getSpeedX());

    }

    @Test
    void testOnWallCollisionY() {

        Ball testBall = new Ball(485, 523, -5, -5, "test");

        testPong.onWallCollision(testBall, false, true);

        assertEquals(5, testBall.getSpeedY());

    }

    @Test
    void testAddBall() {

        ArrayList<Ball> testBallsList = new ArrayList<Ball>();

        testPong.addBall(testBallsList);

        assertEquals(1, testBallsList.size());

    }

}
