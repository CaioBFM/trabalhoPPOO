import java.util.List;
import java.util.Iterator;
import java.util.Random;

public class Hunter extends Consumer {
    private static final int BREEDING_AGE = 12;
    private static final int MAX_AGE = 40;
    private static final double BREEDING_PROBABILITY = 0.09;
    private static final int MIN_RABBITS_KILLED = 2;
    private static final int MIN_FOXES_KILLED = 1;
    private static final int FRUIT_FOOD_VALUE = 5;
    private static final Random rand = new Random();
    private int foodLevel;

    public Hunter(boolean randomAge, Field field, Location location) {
        super(randomAge, field, location);
        foodLevel = FRUIT_FOOD_VALUE;
        if (randomAge) {
            // fome aleatória também, semelhante ao livro
            foodLevel = rand.nextInt(FRUIT_FOOD_VALUE) + 1;
        }
    }

    @Override
    public void act(List<Animal> newAnimals) {
        // implementação do comportamento do caçador
    }

    @Override
    protected int getBreedingAge() {
        return BREEDING_AGE;
    }

    @Override
    protected int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    protected int getFoodValue() {
        return FRUIT_FOOD_VALUE;
    }

}