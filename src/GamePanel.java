import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serial;

public class GamePanel extends JPanel implements ActionListener {
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    public static final int UNIT_SIZE = 20;
    public static final int NUMBER_OF_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    @Serial
    private static final long serialVersionUID = 1L;

    private final Snake snake = new Snake(NUMBER_OF_UNITS);
    private final Food food = new Food();
    private final GameController gameController;

    private final Color normalFoodColor = new Color(210, 119, 90);
    private final Color specialFoodColor = new Color(90, 90, 210);
    private final Color snakeColor = new Color(40, 200, 150);
    private final Font textFont = new Font("Sans serif", Font.PLAIN, 25);
    private final Font headingFont = new Font("Sans serif", Font.PLAIN, 50);

    String resetBtnTitle = "Restart";
    JButton resetButton = new JButton(resetBtnTitle);

    Timer timer;

    public GamePanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.DARK_GRAY);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        gameController = new GameController(WIDTH, HEIGHT, UNIT_SIZE, snake, food);

        timer = new Timer(80, this);

        drawResetButton();
        resetButton.addActionListener(e -> restartGame());

        startGame();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    private void startGame() {
        gameController.play();
        timer.start();
    }

    private void restartGame() {
        gameController.reset();
        gameController.play();

        timer.start();

        remove(resetButton);
        repaint();
    }

    private void drawResetButton() {
        // Calculate the position based on panel dimensions and button size
        int buttonX = (WIDTH - resetButton.getPreferredSize().width) / 2;
        int buttonY = (HEIGHT - resetButton.getPreferredSize().height + 50) / 2;

        resetButton.setBounds(buttonX, buttonY, resetButton.getPreferredSize().width, resetButton.getPreferredSize().height);
    }

    public void draw(Graphics graphics) {
        if (gameController.isGameRunning()) {
            drawFood(graphics);

            drawSnake(graphics);

            drawScore(graphics);

            return;
        }

        timer.stop();
        gameOver(graphics);
        add(resetButton);
    }

    private void drawScore(Graphics graphics) {
        graphics.setColor(Color.white);
        graphics.setFont(textFont);
        FontMetrics metrics = getFontMetrics(graphics.getFont());
        String title = "Score: " + gameController.getScore();
        graphics.drawString(title, (WIDTH - metrics.stringWidth(title)) / 2, graphics.getFont().getSize());
    }

    private void drawSnake(Graphics graphics) {
        graphics.setColor(Color.white);
        graphics.fillRoundRect(snake.getX(0), snake.getY(0), UNIT_SIZE, UNIT_SIZE, UNIT_SIZE , UNIT_SIZE);

        for (int i = 1; i < snake.getLength(); i++) {
            graphics.setColor(snakeColor);
            graphics.fillRoundRect(snake.getX(i), snake.getY(i), UNIT_SIZE, UNIT_SIZE, UNIT_SIZE , UNIT_SIZE);
        }
    }

    private void drawFood(Graphics graphics) {
        graphics.setColor(food.isSpecial() ? specialFoodColor : normalFoodColor);
        graphics.fillOval(food.getX(), food.getY(), UNIT_SIZE, UNIT_SIZE);
    }

    public void gameOver(Graphics graphics) {
        drawGameOver(graphics);

        drawScore(graphics);

        drawResetButton();
    }

    private void drawGameOver(Graphics graphics) {
        graphics.setColor(Color.red);
        graphics.setFont(headingFont);
        FontMetrics metrics = getFontMetrics(graphics.getFont());
        String gameOverHeading = "GAME OVER";
        graphics.drawString(gameOverHeading, (WIDTH - metrics.stringWidth(gameOverHeading)) / 2, HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameController.isGameRunning()) {
            gameController.handleMovement();
            gameController.handleCollision();
        }

        repaint();
    }

    class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if(gameController.isGameRunning()) {
                gameController.handleKeyPress(e.getKeyCode());
            }
        }
    }
}

