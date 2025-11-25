import java.util.List;

/**
 * A class representing shared characteristics of animals.
 * Now extends Actor to fit into the general simulation framework.
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-11
 */
public abstract class Animal extends Actor
{
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's position
    private Location location;
    // The animal's age
    private int age;

    /**
     * Create a new animal at age 0.
     */
    public Animal()
    {
        age = 0;
        alive = true;
    }

    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * (Defined in Actor, implemented in concrete subclasses like Fox/Rabbit)
     */
    abstract public void act(Field currentField, Field updatedField, List newAnimals);

    /**
     * Check whether the animal is alive or not.
     * @return True if the animal is still alive.
     */
    public boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field automatically.
     */
    public void setDead()
    {
        alive = false;
    }

    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    public Location getLocation()
    {
        return location;
    }

    /**
     * Set the animal's location.
     * @param location The new location.
     */
    public void setLocation(Location location)
    {
        this.location = location;
    }
    
    /**
     * Set the animal's location.
     * @param row The vertical coordinate of the location.
     * @param col The horizontal coordinate of the location.
     */
    public void setLocation(int row, int col)
    {
        this.location = new Location(row, col);
    }

    /**
     * Return the animal's age.
     * @return The animal's age.
     */
    protected int getAge()
    {
        return age;
    }

    /**
     * Set the animal's age.
     */
    protected void setAge(int age)
    {
        this.age = age;
    }
    
    /**
     * Increase the age.
     */
    protected void incrementAge()
    {
        age++;
    }
}