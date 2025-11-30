import java.util.List;
import java.util.Random;

/**
 * Representa uma árvore no campo.
 * Árvores não se movem, mas produzem frutos periodicamente.
 * Elas servem de alimento para os Caçadores.
 * 
 * @author Grupo 05
 * @version 2025
 */
public class Tree implements Actor {
    // Características compartilhadas (campos estáticos).

    /** O número máximo de frutos que uma árvore pode ter acumulado. */
    private static final int MAX_FRUITS = 5;
    /** A taxa de crescimento (chances de produzir um fruto a cada passo). */
    private static final double GROWTH_RATE = 0.05;
    /** Gerador de números aleatório */
    private static final Random rand = new Random();

    // Características individuais (campos de instância).

    /** Quantidade atual de frutos na árvore. */
    private int fruitCount;
    /** Localização da árvore (necessário pois ela não herda de Animal). */
    private Location location;
    /** Árvores estão sempre vivas na simulação atual (a menos que sejam destruídas,
     * o que não foi especificado).  */
    private boolean alive;

    /**
     * Cria uma nova árvore.
     * Árvores iniciam com uma quantidade aleatória de frutos.
     */
    public Tree() {
        this.alive = true;
        this.fruitCount = rand.nextInt(MAX_FRUITS + 1);
    }

    /**
     * O que a árvore faz a cada etapa: tenta produzir novos frutos.
     * Como é um ator estático, ela deve se manter na mesma posição no campo
     * atualizado.
     * 
     * @param currentField O campo atualmente ocupado.
     * @param updatedField O campo para o qual transferir (campo atualizado).
     * @param newActors    Uma lista para receber atores recém-criados.
     */
    public void act(Field currentField, Field updatedField, List newActors) {
        if (isAlive()) {
            growFruit();
            // Árvores não se movem, então ocupam o mesmo lugar no novo campo.
            if (updatedField.getObjectAt(location) == null) {
                updatedField.place(this, location);
            } else {
                // Se algo tomou o lugar (erro de lógica ou colisão), a árvore deixa de existir.
                alive = false;
            }
        }
    }

    /**
     * Tenta crescer um fruto baseado na taxa de crescimento.
     */
    private void growFruit() {
        if (fruitCount < MAX_FRUITS && rand.nextDouble() <= GROWTH_RATE) {
            fruitCount++;
        }
    }

    /**
     * Permite que um caçador pegue um fruto.
     * 
     * @return Um valor nutricional (energia) se houver fruto, ou 0 se não houver.
     */
    public int pickFruit() {
        if (fruitCount > 0) {
            fruitCount--;
            // Valor de energia arbitrário para um fruto
            return 40;
        } else {
            return 0;
        }
    }

    /**
     * Verifica se a árvore tem frutos.
     * 
     * @return true se tiver frutos.
     */
    public boolean hasFruit() {
        return fruitCount > 0;
    }

    /**
     * Verifica se a árvore está ativa.
     * 
     * @return True se a árvore ainda existe.
     */
    @Override
    public boolean isAlive() {
        return alive;
    }

    /**
     * Define a localização da árvore.
     * 
     * @param location A localização.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Retorna a localização da árvore.
     * 
     * @return A localização.
     */
    public Location getLocation() {
        return location;
    }
}