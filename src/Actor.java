import java.util.List;

/**
 * Superclasse comum para todos os atores na simulação.
 * Atores incluem animais, mas também podem incluir caçadores, árvores, etc.
 * 
 * @author GRUPO 5
 * @version 2025
 */
public interface Actor {
    /**
     * Faz este ator agir - isto é: faz com que ele faça
     * o que quer que queira ou precise fazer.
     * 
     * @param currentField O campo atualmente ocupado.
     * @param updatedField O campo para o qual transferir (campo atualizado).
     * @param newActors    Uma lista para receber atores recém-criados.
     */
    void act(Field currentField, Field updatedField, List<Actor> newActors);

    /**
     * Verifica se o ator está vivo ou ativo.
     * 
     * @return True se o ator ainda estiver ativo/vivo.
     */
    boolean isAlive();
}