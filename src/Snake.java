import java.awt.event.KeyEvent;
import java.util.Arrays;

public class Snake {
    // Hold x and y coordinates for body parts of snake
    private final int[] x;
    private final int[] y;
    private boolean isRunning = false;
    private int length = 5; // initial snake length
    private Directions direction = Directions.DOWN;

    public Snake(int NUMBER_OF_UNITS) {
        this.x = new int[NUMBER_OF_UNITS];
        this.y = new int[NUMBER_OF_UNITS];

        reset();
    }

    public void reset() {
        length = 5;
        direction = Directions.DOWN;

        // Clear the arrays
        Arrays.fill(x, 0);
        Arrays.fill(y, 0);
    }

    public int getX(int index) {
        if(index < 0) {
            return 0;
        }

        return x[index];
    }

    public int getY(int index) {
        if(index < 0) {
            return 0;
        }

        return y[index];
    }

    public void move(int UNIT_SIZE) {
        // Shift the snake one unit to the desired direction to mimic the movement
        for (int i = length; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        // Update the position of the head
        switch (direction) {
            case Directions.LEFT -> x[0] -= UNIT_SIZE;
            case Directions.RIGHT -> x[0] += UNIT_SIZE;
            case Directions.UP -> y[0] -= UNIT_SIZE;
            case Directions.DOWN -> y[0] += UNIT_SIZE;
        }
    }

    public void reverse() {
        // Reverse the position of the snake based on its current direction

        // Determine the direction of the tail movement
        int dx = x[length] - x[length - 1];
        int dy = y[length] - y[length - 1];

        // Update the direction based on the tail movement
        if (dx < 0) {
            direction = Directions.LEFT;
        } else if (dx > 0) {
            direction = Directions.RIGHT;
        } else if (dy < 0) {
            direction = Directions.UP;
        } else {
            direction = Directions.DOWN;
        }

        // Swap the positions of the head and tail
        int tempX = x[0];
        int tempY = y[0];
        x[0] = x[length];
        y[0] = y[length];
        x[length] = tempX;
        y[length] = tempY;

        // Update the positions of the rest of the body parts (this fixes the weired movement glitch)
        for (int i = 1; i <= length / 2; i++) {
            tempX = x[i];
            tempY = y[i];
            x[i] = x[length - i];
            y[i] = y[length - i];
            x[length - i] = tempX;
            y[length - i] = tempY;
        }
    }

    public void checkSelfCollision() {
        // Check if head run into it's body
        for (int i = length; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                setIsRunning(false);
            }
        }
    }

    public void checkWallCollision(int WIDTH, int HEIGHT) {
        // Check if head run into wall (out of bounds)
        if (x[0] < 0 || x[0] > WIDTH || y[0] < 0 || y[0] > HEIGHT) {
            setIsRunning(false);
        }

    }

    public boolean hasFood(Food food) {
        return x[0] == food.getX() && y[0] == food.getY();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public int getLength() {
        return length;
    }

    public void increaseLength() {
        length++;
    }

    public void setDirection(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT -> {
                if (direction != Directions.RIGHT) {
                    direction = Directions.LEFT;
                }

                break;
            }
            case KeyEvent.VK_RIGHT -> {
                if (direction != Directions.LEFT) {
                    direction = Directions.RIGHT;
                }

                break;
            }

            case KeyEvent.VK_UP -> {
                if (direction != Directions.DOWN) {
                    direction = Directions.UP;
                }

                break;
            }

            case KeyEvent.VK_DOWN -> {
                if (direction != Directions.UP) {
                    direction = Directions.DOWN;
                }

                break;
            }
        }
    }

    private enum Directions {
        UP, LEFT, DOWN, RIGHT
    }
}
