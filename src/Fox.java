import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * Um modelo simples de uma raposa.
 * Raposas envelhecem, se movem, comem coelhos e morrem.
 * 
 * @author GRUPO 5
 * @version 2025
 */
public class Fox extends Animal implements HuntersPreys {
    // Características compartilhadas por todas as raposas (campos estáticos).

    /** A idade na qual uma raposa pode começar a procriar. */
    private static final int BREEDING_AGE = 10;
    /** A idade até a qual uma raposa pode viver. */
    private static final int MAX_AGE = 150;
    /** A probabilidade de uma raposa procriar. */
    private static final double BREEDING_PROBABILITY = 0.09;
    /** O número máximo de nascimentos (tamanho da ninhada). */
    private static final int MAX_LITTER_SIZE = 3;
    /** O valor nutricional de um único coelho. Com efeito, este é o
     * número de passos que uma raposa pode dar antes de ter que comer novamente. */
    private static final int RABBIT_FOOD_VALUE = 4;
    /** Um gerador de números aleatórios compartilhado para controlar a reprodução. */
    private static final Random rand = new Random();

    // Características individuais (campos de instância).

    /** O nível de comida da raposa, que é aumentado ao comer coelhos. */
    private int foodLevel;

    /**
     * Cria uma raposa. Uma raposa pode ser criada como recém-nascida (idade zero
     * e sem fome) ou com idade aleatória.
     * 
     * @param randomAge Se true, a raposa terá idade e nível de fome aleatórios.
     */
    public Fox(boolean randomAge) {
        super();
        if (randomAge) {
            setAge(rand.nextInt(MAX_AGE));
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
        } else {
            // deixa a idade em 0
            foodLevel = RABBIT_FOOD_VALUE;
        }
    }

    /**
     * Isso é o que a raposa faz na maior parte do tempo: ela caça
     * coelhos. No processo, ela pode procriar, morrer de fome
     * ou morrer de velhice.
     * 
     * @param currentField O campo atualmente ocupado.
     * @param updatedField O campo para o qual transferir (campo atualizado).
     * @param newFoxes    Uma lista para receber atores recém-criados.
     */
    public void act(Field currentField, Field updatedField, List<Actor> newFoxes) {
        incrementAge();
        incrementHunger();
        if (isAlive()) {
            // Novas raposas nascem em localizações adjacentes.
            int births = breed();
            for (int b = 0; b < births; b++) {
                Fox newFox = new Fox(false);
                newFoxes.add(newFox);
                Location loc = updatedField.randomAdjacentLocation(getLocation());
                newFox.setLocation(loc);
                updatedField.place(newFox, loc);
            }
            // Move-se em direção à fonte de comida se encontrada.
            Location newLocation = findFood(currentField, getLocation());
            if (newLocation == null) { // no food found - move randomly
                newLocation = updatedField.freeAdjacentLocation(getLocation());
            }
            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                // não pode nem se mover nem ficar - superpopulação - todas as localizações ocupadas
                setDead();
            }
        }
    }

    /**
     * Torna esta raposa mais faminta. Isso pode resultar na morte da raposa.
     */
    private void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Diz à raposa para procurar coelhos adjacentes à sua localização atual.
     * 
     * @param field    O campo no qual ela deve procurar.
     * @param location Onde no campo ela está localizada.
     * @return Onde a comida foi encontrada, ou null se não foi.
     */
    private Location findFood(Field field, Location location) {
        Iterator adjacentLocations = field.adjacentLocations(location);

        while (adjacentLocations.hasNext()) {
            Location where = (Location) adjacentLocations.next();
            Object animal = field.getObjectAt(where);
            if (animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if (rabbit.isAlive()) {
                    rabbit.setDead(); // Substituiu setEaten por setDead
                    foodLevel = RABBIT_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    public double getBreedingProbability() {
        return BREEDING_PROBABILITY;
    }

    @Override
    public int getMaxLitterSize() {
        return MAX_LITTER_SIZE;
    }

    @Override
    public boolean canBreed() {
        return getAge() >= BREEDING_AGE;
    }
}