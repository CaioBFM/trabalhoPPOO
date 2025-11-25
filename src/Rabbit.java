import java.util.List;
import java.util.Random;

public class Rabbit extends Animal {
    private static final int BREEDING_AGE = 5;
    private static final int MAX_AGE = 40;
    private static final double BREEDING_PROBABILITY = 0.15;
    private static final int MAX_LITTER_SIZE = 4;
    private static final Random RAND = new Random();

    public Rabbit(boolean randomAge, Field field, Location location) {
        super(randomAge, field, location);
    }

    @Override
    public void act(List<Animal> newAnimals) {
        incrementAge();
        if (!isAlive()) return;

        // nascimento
        giveBirth(newAnimals);

        // movimentação
        Field field = getField();
        Location next = field.freeAdjacentLocation(getLocation());
        if (next != null) {
            setLocation(next);
        } else {
            // superlotação
            setDead();
        }
    }

    private void giveBirth(List<Animal> newAnimals) {
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for (int b = 0; b < births && !free.isEmpty(); b++) {
            Location loc = free.remove(0);
            newAnimals.add(new Rabbit(false, field, loc));
        }
    }

    private int breed() {
        if (canBreed() && RAND.nextDouble() <= BREEDING_PROBABILITY) {
            return RAND.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return 0;
    }

    @Override
    protected int getBreedingAge() { 
        return BREEDING_AGE; 
    }

    @Override
    protected int getMaxAge() { 
        return MAX_AGE; 
    }
}
