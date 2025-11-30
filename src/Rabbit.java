import java.util.List;
import java.util.Random;

/**
 * Um modelo simples de um coelho.
 * Coelhos envelhecem, se movem, procriam e morrem.
 * 
 * @author GRUPO 05
 * @version 2025
 */
public class Rabbit extends Animal implements HuntersPreys {
    // Características compartilhadas por todos os coelhos (campos estáticos).

    /** A idade na qual um coelho pode começar a procriar. */
    private static final int BREEDING_AGE = 5;
    /** A idade até a qual um coelho pode viver. */
    private static final int MAX_AGE = 50;
    /** A probabilidade de um coelho procriar. */
    private static final double BREEDING_PROBABILITY = 0.15;
    /** O número máximo de nascimentos (tamanho da ninhada). */
    private static final int MAX_LITTER_SIZE = 5;
    /**
     * Um gerador de números aleatórios compartilhado para controlar a reprodução.
     */
    private static final Random rand = new Random();

    /**
     * Cria um novo coelho. Um coelho pode ser criado com idade
     * zero (um recém-nascido) ou com uma idade aleatória.
     * 
     * @param randomAge Se true, o coelho terá uma idade aleatória.
     */
    public Rabbit(boolean randomAge) {
        super();
        if (randomAge) {
            setAge(rand.nextInt(MAX_AGE));
        }
    }

    /**
     * Isso é o que o coelho faz na maior parte do tempo - ele corre
     * por aí. Às vezes ele procria ou morre de velhice.
     * 
     * @param currentField O campo atualmente ocupado.
     * @param updatedField O campo para o qual transferir (campo atualizado).
     * @param newRabbits   Uma lista para receber atores recém-criados.
     */
    public void act(Field currentField, Field updatedField, List<Actor> newRabbits) {
        incrementAge();
        if (isAlive()) {
            int births = breed();
            for (int b = 0; b < births; b++) {
                Rabbit newRabbit = new Rabbit(false);
                newRabbits.add(newRabbit);
                Location loc = updatedField.randomAdjacentLocation(getLocation());
                newRabbit.setLocation(loc);
                updatedField.place(newRabbit, loc);
            }
            Location newLocation = updatedField.freeAdjacentLocation(getLocation());
            // Transfere para o campo atualizado apenas se houver uma localização livre
            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                // não pode nem se mover nem ficar - superpopulação - todas as localizações
                // ocupadas
                setDead();
            }
        }
    }

    @Override
    public boolean canBreed() {
        return getAge() >= BREEDING_AGE;
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
}