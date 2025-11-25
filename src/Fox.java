import java.util.List;
import java.util.Iterator;
import java.util.Random;

public class Fox extends Animal {
    private static final int BREEDING_AGE = 10;
    private static final int MAX_AGE = 150;
    private static final double BREEDING_PROBABILITY = 0.09;
    private static final int MAX_LITTER_SIZE = 3;
    private static final int RABBIT_FOOD_VALUE = 7;
    private static final Random rand = new Random();

    private int foodLevel;

    public Fox(boolean randomAge, Field field, Location location) {
        super(randomAge, field, location);
        // raposa recém-nascida começa alimentada
        foodLevel = RABBIT_FOOD_VALUE;
        if (randomAge) {
            // fome aleatória também, semelhante ao livro
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE) + 1;
        }
    }

    @Override
    public void act(List<Animal> newAnimals) {
        incrementAge();
        incrementHunger();
        if (!isAlive()) return;

        // nascimento
        giveBirth(newAnimals);

        // caça e movimento
        Location newLocation = findFood();
        if (newLocation == null) {
            newLocation = getField().freeAdjacentLocation(getLocation());
        }
        if (newLocation != null) {
            setLocation(newLocation);
        } else {
            setDead(); // superlotação
        }
    }

    private void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) setDead();
    }

    private Location findFood() {
        Field field = getField();
        Iterator<Location> it = field.adjacentLocations(getLocation());
        while (it.hasNext()) {
            Location where = it.next();
            Object obj = field.getObjectAt(where);
            if (obj instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) obj;
                if (rabbit.isAlive()) {
                    rabbit.setDead();               // come o coelho
                    foodLevel = RABBIT_FOOD_VALUE;  // recarrega “energia”
                    return where;
                }
            }
        }
        return null;
    }

    private void giveBirth(List<Animal> newAnimals) {
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for (int b = 0; b < births && !free.isEmpty(); b++) {
            Location loc = free.remove(0);
            newAnimals.add(new Fox(false, field, loc));
        }
    }

    private int breed() {
        if (canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            return rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return 0;
    }

    @Override
    protected int getBreedingAge() { return BREEDING_AGE; }

    @Override
    protected int getMaxAge() { return MAX_AGE; }
}
