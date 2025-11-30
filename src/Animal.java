import java.util.List;
import java.util.Random;

/**
 * Uma classe que representa características compartilhadas de animais.
 * Estende de Actor para se adequar à estrutura geral de simulação.
 * 
 * @author GRUPO 5
 * @version 2025
 */
public abstract class Animal implements Actor {
    /** Se o animal está vivo ou não */
    private boolean alive;
    /** A localização do animal */
    private Location location;
    /** A idade do animal */
    private int age;
    /** Um gerador de números aleatórios compartilhado para controlar a reprodução. */
    private static final Random rand = new Random();

    /**
     * Cria um novo animal com idade 0.
     */
    public Animal() {
        age = 0;
        alive = true;
    }

    /**
     * Faz este animal agir - isto é: faz com que ele faça
     * o que quer que queira ou precise fazer.
     * (Definido em Actor, implementado em subclasses concretas como Fox/Rabbit, etc)
     */
    abstract public void act(Field currentField, Field updatedField, List<Actor> newAnimals);

    /**
     * Retorna a idade máxima permitida para esta espécie.
     * @return A idade máxima.
     */
    abstract public int getMaxAge();

    /**
     * Retorna a probabilidade de reprodução deste animal.
     * @return A probabilidade de reprodução (entre 0 e 1).
     */
    abstract public double getBreedingProbability();

    /**
     * Retorna o número máximo de filhotes que podem nascer de uma vez.
     * @return O tamanho máximo da ninhada.
     */
    abstract public int getMaxLitterSize();

    /**
     * Verifica se o animal atingiu a idade reprodutiva.
     * @return True se o animal tiver idade suficiente para procriar.
     */
    abstract public boolean canBreed();

    /**
     * Verifica se o animal está vivo ou não.
     * 
     * @return True se o animal ainda estiver vivo.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Indica que o animal não está mais vivo.
     * Ele é removido do campo automaticamente.
     */
    protected void setDead() {
        alive = false;
    }

    /**
     * Retorna a localização do animal.
     * 
     * @return A localização do animal.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Define a localização do animal.
     * 
     * @param location A nova localização.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Define a localização do animal.
     * 
     * @param row A coordenada vertical da localização.
     * @param col A coordenada horizontal da localização.
     */
    public void setLocation(int row, int col) {
        this.location = new Location(row, col);
    }

    /**
     * Retorna a idade do animal.
     * 
     * @return A idade do animal.
     */
    protected int getAge() {
        return age;
    }

    /**
     * Define a idade do animal.
     */
    protected void setAge(int age) {
        this.age = age;
    }

    /**
     * Aumenta a idade em uma unidade.
     */
    protected void incrementAge() {
        age++;
        if (age > getMaxAge()) {
            setDead();
        }
    }

    /**
     * Gera um número de nascimentos, se o animal puder procriar.
     * O número de nascimentos é aleatório, baseado na probabilidade de reprodução
     * e no tamanho máximo da ninhada.
     * * @return O número de nascimentos (pode ser zero).
     */
    protected int breed() {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }
}