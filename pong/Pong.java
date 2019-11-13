import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import pong.Ball;
import pong.Bat;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.sound.SoundFile;

public class Pong extends PApplet {
    // Width of our window
    final private int width = 640;
    // Height of our window
    final private int height = 480;
    // Keep track of score - set to 0 at start of game
    private int score = 0;
    // Keep track of when game is playing
    private boolean playing = true;
    // Keep track of whether user hits ball
    private boolean hitBall = false;
    // Create instances of bat and ball
    Ball initialBall = new Ball(320, 240, 0, 0, "initial");
    Bat bat = new Bat(200, 200, 100);
    // Check if bounce has occured
    boolean bounce = false;
    // Check if user hit ball with bat
    boolean batCollision = false;
    // Set up image variable for background
    PImage backgroundImg;
    // ArrayList for storing which balls are in play
    ArrayList<Ball> ballsInPlay = new ArrayList<Ball>();
    // Counter for helping decide when to add more balls
    int collisionCount = 0;
    // Font for text on screen
    PFont font;
    Ball extraBall;
    // Check whether extra ball has been created
    boolean extraBalls = false;
    boolean removeInitialBall = false;
    // Random point during game that extra ball will be added
    int randomPoint;
    // Check if user has clicked to start game
    boolean clicked = false;
    // Set up variables for high score list
    boolean typingName;
    boolean printed = false;
    ArrayList<Score> highScores;
    String name = "";
    String scoreEntry;
    String[] readScores;

    public static void main(String[] args) {
        // Set up the processing library
        PApplet.main("Pong");
    }

    public void settings() {
        // Set our window size
        size(width, height);
    }

    public void setup() {

        String url = "https://i.imgur.com/JUzHyfP.jpg";
        backgroundImg = loadImage(url, "png");

        // Create a random initial position for the ball
        Random r = new Random();

        // Ensure that ball doesn't spawn in or too close to the wall
        boolean positionSet = false;

        while (positionSet == false) {
            initialBall.positionX = r.nextInt(width);
            initialBall.positionY = r.nextInt(height);
            if (initialBall.positionX <= 600 && initialBall.positionX >= 40) {
                if (initialBall.positionY <= 400 && initialBall.positionY >= 40) {
                    positionSet = true;
                }
            }
        }

        // Set the starting speed constant
        initialBall.speedX = 2;
        initialBall.speedY = 2;

        // Add initial ball that is in play to data structure
        ballsInPlay.add(initialBall);

        // Font for text on screen
        font = createFont("Arial Black", 30, true);
        textAlign(CENTER);

        // Create extra ball at random point in game
        Random rand = new Random();
        randomPoint = rand.nextInt(5);

        SoundFile music = new SoundFile(this, "pong_music.wav");
        music.play();

    }

    public void draw() {

        // Create start screen
        background(0, 0, 0);
        // Text to display score
        textFont(font, 30);
        fill(240, 240, 240);

        typingName = true;
        text("Welcome to Pong!", 320, 100);
        text("1. Start typing to enter your name.", 320, 140);
        text("2. Click anywhere to start!", 320, 180);
        text(name, 320, 240);

        if (clicked == true) {

            typingName = false;

            // Set background as galaxy image
            background(0);
            image(backgroundImg, 0, 0);

            // Draw the bat
            rect(20, bat.getPosition(), 10, 100); // Left bat
            fill(240, 240, 240);
            stroke(240, 240, 240);
            rect(618, 0, 20, 478); // Right wall
            fill(240, 240, 240);
            stroke(240, 240, 240);
            rect(0, 0, 640, 20); // Top wall
            fill(240, 240, 240);
            stroke(240, 240, 240);
            rect(0, 458, 640, 20); // Bottom wall
            fill(240, 240, 240);

            // Text to display score
            textFont(font, 30);
            fill(240, 240, 240);
            text("Pong!", 320, 100);

            // Start the game
            mainGame();

        }
    }

    public void mouseClicked() {

        clicked = true;

    }

    public void mainGame() {

        // Set up text to display user's score
        text("Score: " + score, 320, 140);

        if (playing == true) {

            // If initial ball is still in game, draw it
            if (removeInitialBall == false) {
                ellipse(initialBall.positionX, initialBall.positionY, 32, 32);
                fill(240, 240, 240);
            }

            // Boolean to check whether user has hit ball with bat or not
            hitBall = false;

            for (Ball ball : new ArrayList<Ball>(ballsInPlay)) {

                // Set up booleans for detecting which axis the ball needs to
                // bounce off
                boolean x = false;
                boolean y = false;

                // Move all the balls around the screen
                move(ball);

                // If another ball has been created, draw it on screen
                if (extraBalls == true) {
                    ellipse(extraBall.positionX, extraBall.positionY, 32, 32);
                    fill(255, 255, 255);
                }

                // Depending on which axis the ball hits, bounce it back into
                // game
                if (ball.positionX >= 610) {
                    x = true;
                    onWallCollision(ball, x, y);
                    collisionCount = collisionCount + 1;

                    playSoundEffect();

                }

                if ((ball.positionY <= 30) || (ball.positionY >= 450)) {
                    y = true;
                    onWallCollision(ball, x, y);
                    collisionCount = collisionCount + 1;

                    playSoundEffect();

                }

                if (hitBall == false) {

                    // If ball hits user's bat, hit ball back into game
                    if ((ball.getPositionX() <= 30) && (ball.getPositionY() >= bat.getTop())
                            && (ball.getPositionY() <= bat.getBottom())) {
                        x = true;
                        onWallCollision(ball, x, y);

                        playSoundEffect();

                        // Increase ball speed every other hit to make game harder
                        if (collisionCount % 2 == 0) {
                            ball.speedX = ball.speedX + 1;
                            ball.speedY = ball.speedY + 1;
                        }

                        collisionCount = collisionCount + 1;

                        // Create extra ball if it is at that point during game
                        if (score == randomPoint) {
                            addBall(ballsInPlay);
                            extraBalls = true;
                        }

                        // Increase score
                        score = score + 1;
                        hitBall = true;

                        // If ball does not hit bat, end game
                    } else if (((ball.positionX <= 30) && (ball.getPositionY() < bat.top))
                            || ((ball.positionX <= 30) && (ball.getPositionY() > bat.bottom))) {
                        if (ballsInPlay.size() > 1) {
                            // Check which ball has been lost from game, and remove it from screen
                            if (ball.getName() == "initial") {
                                removeInitialBall = true;
                            } else {
                                extraBalls = false;
                            }
                            ballsInPlay.remove(ball);
                        } else {
                            playing = false;
                        }
                    }
                }
            }

        } else {

            highScores = new ArrayList<Score>();

            // Create end screen
            background(0, 0, 0);
            text("You lost the game!", 320, 100);
            text("Your final score is: " + score, 320, 140);

            // Save score to file
            File highScoreFile = new File("highScores.txt");
            try {
                if (printed == false) {
                    PrintWriter printWriter = new PrintWriter(new FileWriter(highScoreFile, true));

                    scoreEntry = name + ": " + score;
                    printWriter.println(scoreEntry);
                    printed = true;
                    printWriter.close();
                }

            } catch (FileNotFoundException e) {
                text("Can not display high score table at this time.", 320, 180);
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Display high score list on screen
            text("High Scores:", 320, 200);
            BufferedReader reader = null;
            try {
                String line;
                int spacer = 40;
                reader = new BufferedReader(new FileReader("highScores.txt"));

                // Split strings in order to get scores
                while ((line = reader.readLine()) != null) {
                    readScores = line.split(" ");
                    name = readScores[0];
                    int score = Integer.parseInt(readScores[1]);
                    Score scores = new Score(name, score);
                    highScores.add(scores);
                }

                // Sort scores to get top 5
                Collections.sort(highScores, new SortByScore());
                ArrayList<Score> topFive = new ArrayList<Score>(
                        highScores.subList(highScores.size() - 5, highScores.size()));

                Collections.reverse(topFive);

                // Display top 5 scores
                int count = 0;
                for (Score score : topFive) {

                    String output = score.name + " " + score.score;
                    text(output, 320, (spacer * count) + 240);
                    count = count + 1;

                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void keyPressed() {

        // Allow user to type their name
        if (typingName == true) {
            if (keyCode == BACKSPACE || keyCode == DELETE) { // Let user delete previous letters
                if (name.length() > 0) {
                    name = name.substring(0, name.length() - 1);
                }
            } else if (keyCode != ALT && keyCode != SHIFT) {
                name = name + key;
            }
        }

        // Make left bat move with keyboard arrows only within window
        if (playing == true) {
            if (key == CODED) {
                if (keyCode == UP) { // Moving bat up
                    if (bat.top > 20) {
                        bat.position = bat.position - 15;
                        bat.top = bat.position;
                        bat.bottom = bat.position + 100;
                    }
                } else if (keyCode == DOWN) { // Moving bat down
                    if (bat.bottom < 460) {
                        bat.position = bat.position + 15;
                        bat.top = bat.position;
                        bat.bottom = bat.position + 100;
                    }
                }
            }
        }
    }

    public void move(Ball b) {

        // Move the ball
        b.positionX += b.speedX;
        b.positionY += b.speedY;

    }

    public void onWallCollision(Ball b, boolean x, boolean y) {

        // Depending on what axis ball hits (x or y), bounce the ball back into
        // the game
        if (x) {
            b.speedX = -b.speedX;
        }
        if (y) {
            b.speedY = -b.speedY;
        }

    }

    public void addBall(ArrayList<Ball> balls) {

        // Create more ball objects to make game harder
        extraBall = new Ball(320, 240, initialBall.getSpeedX(), initialBall.getSpeedY(), "extra");

        // Add new ball to array list
        balls.add(extraBall);

    }

    public void playSoundEffect() {

        // Set up sound effect for when ball hits bat
        SoundFile soundEffect = new SoundFile(this, "pong_beep.wav");

        // Play sound effect
        soundEffect.play();

    }

}
