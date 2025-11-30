/**
 * Representa uma pedra na simulação.
 * Pedras agem como obstáculos estáticos que bloqueiam o movimento dos atores
 * ocupando uma localização no campo.
 * 
 * @author GRUPO 05
 * @version 20025
 */
public class Stone implements Obstacles {
    /** A localização da pedra. */
    private Location location;
    /**
     * O número padrão de pedras a serem geradas aleatoriamente (se não usar mapa).
     */
    public static final int NUM_STONES = 10;

    /**
     * Cria uma nova pedra em uma localização específica.
     * * @param location A localização onde a pedra será colocada.
     */
    public Stone(Location location) {
        this.location = location;
    }

    /**
     * Retorna a localização da pedra.
     * 
     * @return A localização atual da pedra.
     */
    @Override
    public Location getLocation() {
        return location;
    }
}
