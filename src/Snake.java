import java.awt.event.KeyEvent;

class SnakeNodeList {
    public SnakeNode head = null;
    public SnakeNode tail = null;

    public SnakeNodeList(int x, int y) {
        SnakeNode node = new SnakeNode(x, y);
        head = node;
        tail = node;
    }

    public void growSnakeByANode(int x, int y) {
        SnakeNode newNode = new SnakeNode(x, y);

        if(head == null) {
            head = newNode;
            tail = newNode;

            return;
        }

        newNode.next = tail;
        tail = newNode;
    }

    public int getX(int index, int length) {
        if (index < 0 || index > length) {
            return 0;
        }

        SnakeNode current = tail;
        for (int i = 0; i < length - index - 1; i++) {
            current = current.next;
        }

        return current.x;
    }

    public int getY(int index, int length) {
        if (index < 0 || index > length) {
            return 0;
        }

        SnakeNode current = tail;
        for (int i = 0; i < length - index - 1; i++) {
            current = current.next;
        }

        return current.y;
    }

    public void move(int dx, int dy) {
        // Add a new head at the new position
        SnakeNode newHead = new SnakeNode(head.x + dx, head.y + dy);
        head.next = newHead;
        head = newHead;

        // Remove the old tail
        if (tail != null) {
            tail = tail.next;
        }
    }

    public boolean hasSelfCollision() {
        SnakeNode current = tail; // Start from the tail and traverse till second last node (last node is head)

        while (current != null && current != head) {
            if (head.x == current.x && head.y == current.y) {
                return true; // Head collided with another body part
            }
            current = current.next;
        }
        return false;
    }

    public boolean hasWallCollision(int WIDTH, int HEIGHT) {
        return head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT;
    }

    public boolean hasFood(Food food) {
        return head.x == food.getX() && head.y == food.getY();
    }

    public void reverse() {
        SnakeNode prev = null;
        SnakeNode current = tail;
        SnakeNode oldTail = tail;

        while (current != null) {
            SnakeNode nextNode = current.next;
            current.next = prev;
            prev = current;
            current = nextNode;
        }

        tail = prev;
        head = oldTail;
    }

    public static class SnakeNode {
        private int x;
        private int y;
        private SnakeNode next;

        public SnakeNode(int x, int y) {
            this.x = x;
            this.y = y;
            this.next = null;
        }
    }
}

public class Snake {
    private SnakeNodeList snakeBody; // Hold x and y coordinates for body parts of snake
    private boolean isRunning = false;
    private int length = 5; // Initial snake length
    private Directions direction = Directions.DOWN;

    public Snake(int NUMBER_OF_UNITS) {
        this.snakeBody = new SnakeNodeList(0, 0); // Reset snake position to (0, 0)
        reset();
    }

    public void reset() {
        isRunning = false;
        direction = Directions.DOWN;
        length = 5;

        if(snakeBody.head != snakeBody.tail) { // Means we are not starting the game first time
            snakeBody.head = null;
            snakeBody.tail = null;

            snakeBody = new SnakeNodeList(0, 0);
        }

        for (int i = 1; i < length; i++) {
            snakeBody.growSnakeByANode(i, 0);
        }
    }

    public int getX(int index) {
        return snakeBody.getX(index, length);
    }

    public int getY(int index) {
        return snakeBody.getY(index, length);
    }

    public void move(int UNIT_SIZE) {
        int dx = 0, dy = 0;

        // Update the position of the head
        switch (direction) {
            case Directions.LEFT -> dx -= UNIT_SIZE;
            case Directions.RIGHT -> dx += UNIT_SIZE;
            case Directions.UP -> dy -= UNIT_SIZE;
            case Directions.DOWN -> dy += UNIT_SIZE;
        }

        snakeBody.move(dx, dy);
    }

    public void reverse() {
        // Get the coordinates of the tail and the node next to the tail
        int tailX = snakeBody.getX(length-1, length);
        int tailY = snakeBody.getY(length - 1, length);
        int secondToTailX = snakeBody.getX(length - 2, length);
        int secondToTailY = snakeBody.getY(length - 2, length);

        // Calculate the direction from the second to last node to the tail
        int dx = tailX - secondToTailX;
        int dy = tailY - secondToTailY;

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

        snakeBody.reverse();
    }

    public void checkSelfCollision() {
        // Check if head run into it's body
        if(snakeBody.hasSelfCollision()) {
            isRunning = false;
        }
    }

    public void checkWallCollision(int WIDTH, int HEIGHT) {
        // Check if head run into wall (out of bounds)
        if(snakeBody.hasWallCollision(WIDTH, HEIGHT)) {
            isRunning = false;
        }
    }

    public boolean hasFood(Food food) {
        return snakeBody.hasFood(food);
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
        // Get the coordinates of the tail and the node next to the tail
        int tailX = snakeBody.getX(length, length);
        int tailY = snakeBody.getY(length, length);
        int secondToTailX = snakeBody.getX(length - 1, length);
        int secondToTailY = snakeBody.getY(length - 1, length);

        // Calculate the direction from the second to last node to the tail
        int dx = tailX - secondToTailX;
        int dy = tailY - secondToTailY;

        // Calculate the position of the new node to extend the snake
        int newX = tailX + dx;
        int newY = tailY + dy;

        // Add the new node at the appropriate position
        snakeBody.growSnakeByANode(newX, newY);
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

//public class Snake {
//    // Hold x and y coordinates for body parts of snake
//    private final int[] x;
//    private final int[] y;
//    private boolean isRunning = false;
//    private int length = 5; // initial snake length
//    private Directions direction = Directions.DOWN;
//
//    public Snake(int NUMBER_OF_UNITS) {
//        this.x = new int[NUMBER_OF_UNITS];
//        this.y = new int[NUMBER_OF_UNITS];
//
//        reset();
//    }
//
//    public void reset() {
//        length = 5;
//        direction = Directions.DOWN;
//
//        // Clear the arrays
//        Arrays.fill(x, 0);
//        Arrays.fill(y, 0);
//    }
//
//    public int getX(int index) {
//        if(index < 0) {
//            return 0;
//        }
//
//        return x[index];
//    }
//
//    public int getY(int index) {
//        if(index < 0) {
//            return 0;
//        }
//
//        return y[index];
//    }
//
//    public void move(int UNIT_SIZE) {
//        // Shift the snake one unit to the desired direction to mimic the movement
//        for (int i = length; i > 0; i--) {
//            x[i] = x[i - 1];
//            y[i] = y[i - 1];
//        }
//
//        // Update the position of the head
//        switch (direction) {
//            case Directions.LEFT -> x[0] -= UNIT_SIZE;
//            case Directions.RIGHT -> x[0] += UNIT_SIZE;
//            case Directions.UP -> y[0] -= UNIT_SIZE;
//            case Directions.DOWN -> y[0] += UNIT_SIZE;
//        }
//    }
//
//    public void reverse() {
//        // Reverse the position of the snake based on its current direction
//
//        // Determine the direction of the tail movement
//        int dx = x[length] - x[length - 1];
//        int dy = y[length] - y[length - 1];
//
//        // Update the direction based on the tail movement
//        if (dx < 0) {
//            direction = Directions.LEFT;
//        } else if (dx > 0) {
//            direction = Directions.RIGHT;
//        } else if (dy < 0) {
//            direction = Directions.UP;
//        } else {
//            direction = Directions.DOWN;
//        }
//
//        // Swap the positions of the head and tail
//        int tempX = x[0];
//        int tempY = y[0];
//        x[0] = x[length];
//        y[0] = y[length];
//        x[length] = tempX;
//        y[length] = tempY;
//
//        // Update the positions of the rest of the body parts (this fixes the weired movement glitch)
//        for (int i = 1; i <= length / 2; i++) {
//            tempX = x[i];
//            tempY = y[i];
//            x[i] = x[length - i];
//            y[i] = y[length - i];
//            x[length - i] = tempX;
//            y[length - i] = tempY;
//        }
//    }
//
//    public void checkSelfCollision() {
//        // Check if head run into it's body
//        for (int i = length; i > 0; i--) {
//            if (x[0] == x[i] && y[0] == y[i]) {
//                setIsRunning(false);
//            }
//        }
//    }
//
//    public void checkWallCollision(int WIDTH, int HEIGHT) {
//        // Check if head run into wall (out of bounds)
//        if (x[0] < 0 || x[0] > WIDTH || y[0] < 0 || y[0] > HEIGHT) {
//            setIsRunning(false);
//        }
//
//    }
//
//    public boolean hasFood(Food food) {
//        return x[0] == food.getX() && y[0] == food.getY();
//    }
//
//    public boolean isRunning() {
//        return isRunning;
//    }
//
//    public void setIsRunning(boolean isRunning) {
//        this.isRunning = isRunning;
//    }
//
//    public int getLength() {
//        return length;
//    }
//
//    public void increaseLength() {
//        length++;
//    }
//
//    public void setDirection(int keyCode) {
//        switch (keyCode) {
//            case KeyEvent.VK_LEFT -> {
//                if (direction != Directions.RIGHT) {
//                    direction = Directions.LEFT;
//                }
//
//                break;
//            }
//            case KeyEvent.VK_RIGHT -> {
//                if (direction != Directions.LEFT) {
//                    direction = Directions.RIGHT;
//                }
//
//                break;
//            }
//
//            case KeyEvent.VK_UP -> {
//                if (direction != Directions.DOWN) {
//                    direction = Directions.UP;
//                }
//
//                break;
//            }
//
//            case KeyEvent.VK_DOWN -> {
//                if (direction != Directions.UP) {
//                    direction = Directions.DOWN;
//                }
//
//                break;
//            }
//        }
//    }
//
//    private enum Directions {
//        UP, LEFT, DOWN, RIGHT
//    }
//}
