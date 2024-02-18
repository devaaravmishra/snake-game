import java.util.Random;

public class Food {
    private int X;
    private int Y;

    private boolean isSpecial = false;

    private final Random random = new Random();

    public void generate(int PANEL_WIDTH, int PANEL_HEIGHT, int UNIT_SIZE) {
        isSpecial = false; // Guessing the food to be an ordinary one

        int randomNumber = random.nextInt(10);
        if(randomNumber < 2) { // 20% chance of getting a special one?
            isSpecial = true;
        }

        X = random.nextInt((int) (PANEL_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        Y = random.nextInt((int) (PANEL_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public int getX() {
        return this.X;
    }

    public int getY() {
        return this.Y;
    }

    public boolean isSpecial() {
        return isSpecial;
    }
}
