import java.util.List;
import java.util.Random;

public abstract class Consumer extends Animal {
    private int foodLevel;
    private static final Random rand = new Random();

    public Consumer(boolean randomAge, Field field, Location location) {
        super(randomAge, field, location);
        foodLevel = getFoodValue();
        if (randomAge) {
            // fome aleatória também, semelhante ao livro
            foodLevel = rand.nextInt(getFoodValue()) + 1;
        }
    }

}
