import java.util.List;
import java.util.Random;

/**
 * A class representing shared characteristics of animals.
 * Now extends Actor to fit into the general simulation framework.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-11
 */
public abstract class Animal implements Actor {
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's position
    private Location location;
    // The animal's age
    private int age;
    // A shared random number generator to control breeding.
    private static final Random rand = new Random();

    /**
     * Create a new animal at age 0.
     */
    public Animal() {
        age = 0;
        alive = true;
    }

    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * (Defined in Actor, implemented in concrete subclasses like Fox/Rabbit)
     */
    abstract public void act(Field currentField, Field updatedField, List newAnimals);

    abstract public int getBreedingAge();

    abstract public int getMaxAge();

    abstract public double getBreedingProbability();

    abstract public int getMaxLitterSize();

    /**
     * Check whether the animal is alive or not.
     * 
     * @return True if the animal is still alive.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field automatically.
     */
    protected void setDead() {
        alive = false;
    }

    /**
     * Return the animal's location.
     * 
     * @return The animal's location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Set the animal's location.
     * 
     * @param location The new location.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Set the animal's location.
     * 
     * @param row The vertical coordinate of the location.
     * @param col The horizontal coordinate of the location.
     */
    public void setLocation(int row, int col) {
        this.location = new Location(row, col);
    }

    /**
     * Return the animal's age.
     * 
     * @return The animal's age.
     */
    protected int getAge() {
        return age;
    }

    /**
     * Set the animal's age.
     */
    protected void setAge(int age) {
        this.age = age;
    }

    /**
     * Increase the age.
     */
    protected void incrementAge() {
        age++;
        if (age > getMaxAge()) {
            setDead();
        }
    }

    private boolean canBreed() {
        return age >= getBreedingAge();
    }

    protected int breed() {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }
}