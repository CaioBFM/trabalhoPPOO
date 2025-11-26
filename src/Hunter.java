import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * Modelo de um Caçador (Humano).
 * Caçadores se movem, caçam animais por esporte/comércio e comem frutos para
 * energia.
 * Eles se reproduzem baseados no sucesso de sua caça.
 * 
 * @author Grupo 05
 * @version 2025
 */
public class Hunter extends Animal {
    // Características estáticas.

    // Depois de matar certa quantidade de animais, o caçador morre
    private static final int MAX_KILLS = 100;
    // Quantidade de abates necessários para se reproduzir.
    private static final int KILLS_TO_BREED = 2;
    // Energia máxima.
    private static final int MAX_ENERGY = 200;
    // Energia perdida por passo.
    private static final int ENERGY_LOSS = 1;
    // Tamanho máximo da ninhada.
    private static final int MAX_LITTER_SIZE = 3;
    // Probabilidade de reprodução.
    private static final double BREEDING_PROBABILITY = 1;
    // Idade máxima.
    private static final int MAX_AGE = 70;
    // Gerador de números aleatórios.
    private static final Random rand = new Random();

    // Características individuais.

    // Nível de energia do caçador.
    private int energy;
    // Contador de animais abatidos.
    private int killCount;

    /**
     * Cria um novo caçador.
     * 
     * @param randomAge Se true, o caçador terá idade e energia aleatórias.
     */
    public Hunter(boolean randomAge) {
        super();
        killCount = 0;
        if (randomAge) {
            energy = rand.nextInt(MAX_ENERGY);
        } else {
            energy = MAX_ENERGY;
        }
    }

    /**
     * O comportamento do caçador a cada passo.
     * Ele perde energia, caça ou coleta frutos, e tenta se reproduzir.
     */
    public void act(Field currentField, Field updatedField, List<Actor> newHunters) {
        incrementAge();
        decresceEnergy();

        if (energy <= 0 || killCount >= MAX_KILLS) {
            setDead(); // Morre de fome/exaustão
        }

        if (isAlive()) {
            int births = breed();
            for (int b = 0; b < births; b++) {
                Hunter newHunter = new Hunter(false);
                newHunters.add(newHunter);
                Location loc = updatedField.randomAdjacentLocation(getLocation());
                newHunter.setLocation(loc);
                updatedField.place(newHunter, loc);
            }
            killCount = 0;

            // Move-se procurando comida (Frutos ou Presas)
            Location newLocation = findResources(currentField, getLocation());

            if (newLocation == null) {
                // Se não encontrou recurso, move-se aleatoriamente
                newLocation = updatedField.freeAdjacentLocation(getLocation());
            }

            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                // Superpopulação - sem lugar para ir
                setDead();
            }
        }
    }

    /**
     * Procura por recursos (Frutos ou Animais).
     * Se a energia estiver baixa (< 30%), prioriza Frutos.
     * Caso contrário, caça Animais.
     * 
     * @return A localização para onde mover (onde estava o recurso) ou null.
     */
    private Location findResources(Field field, Location location) {
        // Verifica segurança para evitar NPE caso location seja nulo
        if (location == null)
            return null;

        Iterator adjacentLocations = field.adjacentLocations(location);
        while (adjacentLocations.hasNext()) {
            Location where = (Location) adjacentLocations.next();
            Object object = field.getObjectAt(where);

            // Estratégia: Se energia baixa, procura Árvore com fruto.
            if (energy < (MAX_ENERGY * 0.4)) {
                if (object instanceof Tree) {
                    Tree tree = (Tree) object;
                    if (tree.hasFruit()) {
                        int food = tree.pickFruit();
                        energy += food;
                        if (energy > MAX_ENERGY)
                            energy = MAX_ENERGY;
                        // Não movemos para cima da árvore, apenas comemos.
                        return null;
                    }
                }
            }

            // Se energia ok, ou não achou fruta, caça coelhos ou raposas.
            if (object instanceof HuntersPreys) {
                Animal prey = (Animal) object;
                if (prey.isAlive()) {
                    prey.setDead(); // Mata a presa
                    killCount++;
                    // Caçadores ocupam o lugar da presa morta
                    return where;
                }
            }
        }
        return null;
    }

    private void decresceEnergy() {
        energy -= ENERGY_LOSS;
    }

    @Override
    public boolean isAlive() {
        return energy > 0;
    }

    @Override
    public boolean canBreed() {
        return killCount >= KILLS_TO_BREED;
    }

    @Override
    public int getMaxLitterSize() {
        return MAX_LITTER_SIZE;
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    @Override
    public double getBreedingProbability() {
        return BREEDING_PROBABILITY;
    }
}