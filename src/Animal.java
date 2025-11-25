import java.util.List;
import java.util.Random;

public abstract class Animal {
    private int age;
    private boolean alive;
    private static final Random rand = new Random();
    private Field field;
    private Location location;

    // --- Contrato polimórfico ---
    public abstract void act(List<Animal> newAnimals);

    protected abstract int getBreedingAge();

    protected abstract int getMaxAge();

    protected abstract int getFoodValue();

    // --- Construtor comum ---
    public Animal(boolean randomAge, Field field, Location location) {
        this.age = 0;
        this.alive = true;
        this.field = field;
        setLocation(location); // usa o setter para já posicionar no field
        if (randomAge) {
            this.age = rand.nextInt(getMaxAge()); // 0 .. maxAge-1
        }
    }

    // --- Utilidades comuns ---
    protected boolean canBreed() {
        return age >= getBreedingAge();
    }

    public boolean isAlive() {
        return alive;
    }

    protected void setDead() {
        alive = false;
        if (location != null && field != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    protected void incrementAge() {
        age++;
        if (age > getMaxAge()) {
            setDead();
        }
    }

    // --- Acesso controlado ao ambiente ---
    protected Field getField() {
        return field;
    }

    protected Location getLocation() {
        return location;
    }

    protected void setLocation(Location newLocation) {
        if (this.location != null && field != null) {
            field.clear(this.location);
        }
        this.location = newLocation;
        if (field != null && newLocation != null) {
            field.place(this, newLocation);
        }
    }
}
