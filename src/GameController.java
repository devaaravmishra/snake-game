public class GameController {
    private final int PANEL_WIDTH;
    private final int PANEL_HEIGHT;
    private final int UNIT_SIZE;

    private final Snake snake;
    private final Food food;


    private int score = 0;

    public GameController(int PANEL_WIDTH, int PANEL_HEIGHT, int UNIT_SIZE, Snake snake, Food food) {
        this.PANEL_WIDTH = PANEL_WIDTH;
        this.PANEL_HEIGHT = PANEL_HEIGHT;
        this.UNIT_SIZE = UNIT_SIZE;

        this.snake = snake;
        this.food = food;
    }

    public void play() {
        generateFood();
        snake.setIsRunning(true);
    };

    public void reset() {
        snake.reset();
        score = 0;
    };

    public void generateFood() {
        // Handle the case where food might overlap the snake body
        do {
            food.generate(PANEL_WIDTH, PANEL_HEIGHT, UNIT_SIZE);
        }
        while (isFoodBeneathSnakeBody());
    }

    public boolean isFoodBeneathSnakeBody() {
        for (int i = 0; i <= snake.getLength(); i++) {
            if(snake.getX(i) == food.getX() && snake.getY(i) == food.getY()) {
                return true;
            }
        }

        return false;
    }

    public void handleMovement() {
        snake.move(UNIT_SIZE);
    }

    public void handleCollision() {
        if(snake.hasFood(food)) { // Snake collided with the food
            if(food.isSpecial()) { // If the food eaten was special?
                snake.reverse();
            }

            generateFood();
            score++;
            snake.increaseLength();
        }

        snake.checkSelfCollision();
        snake.checkWallCollision(PANEL_WIDTH, PANEL_HEIGHT);
    }

    public void handleKeyPress(int keyCode) {
        snake.setDirection(keyCode);
    }

    public boolean isGameRunning() {
        return snake.isRunning();
    }

    public int getScore() {
        return score;
    }
}
