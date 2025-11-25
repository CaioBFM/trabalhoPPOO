import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * Modelo de um Caçador (Humano).
 * Caçadores se movem, caçam animais por esporte/comércio e comem frutos para energia.
 * Eles se reproduzem baseados no sucesso de sua caça.
 * @author Grupo 05
 * @version 2025
 */
public class Hunter extends Animal
{
    // Características estáticas.
    
    // Idade máxima de um caçador.
    private static final int MAX_AGE = 100;
    // Quantidade de abates necessários para se reproduzir.
    private static final int KILLS_TO_BREED = 3;
    // Energia máxima.
    private static final int MAX_ENERGY = 100;
    // Energia perdida por passo.
    private static final int ENERGY_LOSS = 2;
    // Gerador de números aleatórios.
    private static final Random rand = new Random();

    // Características individuais.

    // Nível de energia do caçador.
    private int energy;
    // Contador de animais abatidos.
    private int killCount;

    /**
     * Cria um novo caçador.
     * @param randomAge Se true, o caçador terá idade e energia aleatórias.
     */
    public Hunter(boolean randomAge)
    {
        super();
        killCount = 0;
        if(randomAge) {
            setAge(rand.nextInt(MAX_AGE));
            energy = rand.nextInt(MAX_ENERGY);
        }
        else {
            energy = MAX_ENERGY;
        }
    }

    /**
     * O comportamento do caçador a cada passo.
     * Ele perde energia, caça ou coleta frutos, e tenta se reproduzir.
     */
    public void act(Field currentField, Field updatedField, List newHunters)
    {
        incrementAge();
        energy -= ENERGY_LOSS;
        
        if(energy <= 0) {
            setDead(); // Morre de fome/exaustão
        }

        if(isAlive()) {
            // Tenta se reproduzir se tiver caçado o suficiente
            // Passamos updatedField para definir a posição do filho
            reproduce(updatedField, newHunters);

            // Move-se procurando comida (Frutos ou Presas)
            Location newLocation = findResources(currentField, getLocation());
            
            if(newLocation == null) { 
                // Se não encontrou recurso, move-se aleatoriamente
                newLocation = updatedField.freeAdjacentLocation(getLocation());
            }

            if(newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            }
            else {
                // Superpopulação - sem lugar para ir
                setDead();
            }
        }
    }
    
    /**
     * Sobrescreve o incremento de idade para usar a idade máxima do humano.
     */
    protected void incrementAge()
    {
        super.incrementAge();
        if(getAge() > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Reprodução baseada em mérito (quantidade de caça).
     */
    private void reproduce(Field updatedField, List newHunters)
    {
        // Se atingiu a meta de caça, nasce um novo caçador
        if(killCount >= KILLS_TO_BREED) {
            Hunter offspring = new Hunter(false);
            
            // Define a localização do filho (adjacente ao pai)
            Location loc = updatedField.randomAdjacentLocation(getLocation());
            offspring.setLocation(loc);
            
            // Adiciona ao campo e à lista
            updatedField.place(offspring, loc);
            newHunters.add(offspring);
            
            killCount = 0; // Reinicia a contagem após procriar
        }
    }

    /**
     * Procura por recursos (Frutos ou Animais).
     * Se a energia estiver baixa (< 30%), prioriza Frutos.
     * Caso contrário, caça Animais.
     * @return A localização para onde mover (onde estava o recurso) ou null.
     */
    private Location findResources(Field field, Location location)
    {
        // Verifica segurança para evitar NPE caso location seja nulo
        if (location == null) return null;

        Iterator adjacentLocations = field.adjacentLocations(location);
        while(adjacentLocations.hasNext()) {
            Location where = (Location) adjacentLocations.next();
            Object object = field.getObjectAt(where);
            
            // Estratégia: Se energia baixa, procura Árvore com fruto.
            if(energy < (MAX_ENERGY * 0.3)) {
                if(object instanceof Tree) {
                    Tree tree = (Tree) object;
                    if(tree.hasFruit()) {
                        int food = tree.pickFruit();
                        energy += food;
                        if(energy > MAX_ENERGY) energy = MAX_ENERGY;
                        // Não movemos para cima da árvore, apenas comemos.
                        return null; 
                    }
                }
            }
            
            // Se energia ok, ou não achou fruta, caça coelhos ou raposas.
            if(object instanceof Rabbit || object instanceof Fox) {
                Animal prey = (Animal) object;
                if(prey.isAlive()) {
                    prey.setDead(); // Mata a presa
                    killCount++;
                    // Caçadores ocupam o lugar da presa morta
                    return where;
                }
            }
        }
        return null;
    }
}