import java.util.List;

/**
 * Common superclass for all actors in the simulation.
 * Actors include animals, but could also include hunters, weather, etc.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-11
 */
public interface Actor {
    /**
     * Make this actor act - that is: make it do
     * whatever it wants/needs to do.
     * 
     * @param currentField The field currently occupied.
     * @param updatedField The field to transfer to.
     * @param newActors    A list to receive newly created actors.
     */
    void act(Field currentField, Field updatedField, List newActors);

    /**
     * Check whether the actor is alive or active.
     * 
     * @return True if the actor is still active/alive.
     */
    boolean isAlive();
}