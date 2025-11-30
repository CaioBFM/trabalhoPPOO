import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Representa uma grade retangular de posições de campo.
 * Cada posição é capaz de armazenar um único animal.
 * 
 * @author GRUPO 5
 * @version 2025
 */
public class Field {
    /** Um gerador de números aleatórios */
    private static final Random rand = new Random();

    /** A profundidade e largura do campo. */
    private int depth, width;
    /** Armazenamento para os animais. */
    private Object[][] field;

    /**
     * Representa um campo com as dimensões fornecidas.
     * 
     * @param depth A profundidade do campo.
     * @param width A largura do campo.
     */
    public Field(int depth, int width) {
        this.depth = depth;
        this.width = width;
        field = new Object[depth][width];
    }

    /**
     * Esvazia o campo.
     */
    public void clear() {
        for (int row = 0; row < depth; row++) {
            for (int col = 0; col < width; col++) {
                field[row][col] = null;
            }
        }
    }

    /**
     * Coloca um animal na localização fornecida.
     * Se já houver um animal na localização, ele será
     * perdido.
     * 
     * @param object O objeto a ser colocado.
     * @param row    Coordenada da linha da localização.
     * @param col    Coordenada da coluna da localização.
     */
    public void place(Object object, int row, int col) {
        place(object, new Location(row, col));
    }

    /**
     * Coloca um objeto na localização fornecida.
     * Se já houver um objeto na localização, ele será
     * perdido.
     * 
     * @param object   O objeto a ser colocado.
     * @param location Onde colocar o objeto.
     */
    public void place(Object object, Location location) {
        field[location.getRow()][location.getCol()] = object;
    }

    /**
     * Retorna o animal na localização fornecida, se houver.
     * 
     * @param location Onde no campo.
     * @return O animal na localização fornecida, ou null se não houver nenhum.
     */
    public Object getObjectAt(Location location) {
        return getObjectAt(location.getRow(), location.getCol());
    }

    /**
     * Retorna o animal na localização fornecida, se houver.
     * 
     * @param row A linha desejada.
     * @param col A coluna desejada.
     * @return O animal na localização fornecida, ou null se não houver nenhum.
     */
    public Object getObjectAt(int row, int col) {
        return field[row][col];
    }

    /**
     * Gera uma localização aleatória que é adjacente à
     * localização fornecida, ou é a mesma localização.
     * A localização retornada estará dentro dos limites válidos
     * do campo.
     * 
     * @param location A localização a partir da qual gerar uma adjacência.
     * @return Uma localização válida dentro da área da grade. Este
     * pode ser o mesmo objeto que o parâmetro location.
     */
    public Location randomAdjacentLocation(Location location) {
        int row = location.getRow();
        int col = location.getCol();
        // Gera um deslocamento de -1, 0, ou +1 para a linha e coluna atuais.
        int nextRow = row + rand.nextInt(3) - 1;
        int nextCol = col + rand.nextInt(3) - 1;
        // Verifica caso a nova localização esteja fora dos limites.
        if (nextRow < 0 || nextRow >= depth || nextCol < 0 || nextCol >= width) {
            return location;
        } else if (nextRow != row || nextCol != col) {
            return new Location(nextRow, nextCol);
        } else {
            return location;
        }
    }

    /**
     * Tenta encontrar uma localização livre que seja adjacente à
     * localização fornecida. Se não houver nenhuma, retorna a localização
     * atual se ela estiver livre. Se não, retorna null.
     * A localização retornada estará dentro dos limites válidos
     * do campo.
     * 
     * @param location A localização a partir da qual gerar uma adjacência.
     * @return Uma localização válida dentro da área da grade. Esta pode ser o
     * mesmo objeto que o parâmetro location, ou null se todas as
     * localizações ao redor estiverem cheias.
     */
    public Location freeAdjacentLocation(Location location) {
        Iterator adjacent = adjacentLocations(location);
        while (adjacent.hasNext()) {
            Location next = (Location) adjacent.next();
            if (field[next.getRow()][next.getCol()] == null) {
                return next;
            }
        }
        // verifica se a localização atual está livre
        if (field[location.getRow()][location.getCol()] == null) {
            return location;
        } else {
            return null;
        }
    }

    /**
     * Gera um iterador sobre uma lista embaralhada de localizações adjacentes
     * à fornecida. A lista não incluirá a própria localização.
     * Todas as localizações estarão dentro da grade.
     * 
     * @param location A localização a partir da qual gerar adjacências.
     * @return Um iterador sobre localizações adjacentes à fornecida.
     */
    public Iterator adjacentLocations(Location location) {
        int row = location.getRow();
        int col = location.getCol();
        LinkedList locations = new LinkedList();
        for (int roffset = -1; roffset <= 1; roffset++) {
            int nextRow = row + roffset;
            if (nextRow >= 0 && nextRow < depth) {
                for (int coffset = -1; coffset <= 1; coffset++) {
                    int nextCol = col + coffset;
                    // Exclui localizações inválidas e a localização original.
                    if (nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                        locations.add(new Location(nextRow, nextCol));
                    }
                }
            }
        }
        Collections.shuffle(locations, rand);
        return locations.iterator();
    }

    /**
     * @return A profundidade do campo.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * @return A largura do campo.
     */
    public int getWidth() {
        return width;
    }
}
