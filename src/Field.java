import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

/**
 * Representa um campo bidimensional retangular.
 * Cada posição pode conter um único objeto (por exemplo, um Animal).
 * 
 * Baseado no código do Capítulo 10 do livro de Barnes & Kolling.
 */
public class Field {
    // matriz de objetos que compõem o campo
    private Object[][] field;

    // dimensões do campo
    private int depth;
    private int width;

    /**
     * Cria um campo com profundidade (número de linhas) e largura (número de colunas) dadas.
     */
    public Field(int depth, int width) {
        this.depth = depth;
        this.width = width;
        field = new Object[depth][width];
    }

    /**
     * Limpa todo o campo (remove todos os objetos).
     */
    public void clear() {
        for (int row = 0; row < depth; row++) {
            for (int col = 0; col < width; col++) {
                field[row][col] = null;
            }
        }
    }

    /**
     * Limpa uma posição específica do campo.
     */
    public void clear(Location location) {
        field[location.getRow()][location.getCol()] = null;
    }

    /**
     * Coloca um objeto em uma localização específica.
     * Qualquer objeto anterior naquela posição será sobrescrito.
     */
    public void place(Object obj, int row, int col) {
        field[row][col] = obj;
    }

    /**
     * Coloca um objeto em uma localização específica.
     */
    public void place(Object obj, Location location) {
        field[location.getRow()][location.getCol()] = obj;
    }

    /**
     * Retorna o objeto localizado em (row, col).
     * Retorna null se a posição estiver vazia.
     */
    public Object getObjectAt(int row, int col) {
        return field[row][col];
    }

    /**
     * Retorna o objeto localizado em uma determinada Location.
     * Retorna null se a posição estiver vazia.
     */
    public Object getObjectAt(Location location) {
        return field[location.getRow()][location.getCol()];
    }

    /**
     * Retorna a profundidade (número de linhas) do campo.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Retorna a largura (número de colunas) do campo.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gera uma lista embaralhada de localizações adjacentes (vizinhas) a uma dada posição.
     * Pode incluir até 8 posições (diagonais + ortogonais).
     */
    public Iterator<Location> adjacentLocations(Location location) {
        int row = location.getRow();
        int col = location.getCol();
        List<Location> locations = new LinkedList<>();

        for (int roffset = -1; roffset <= 1; roffset++) {
            int nextRow = row + roffset;
            if (nextRow >= 0 && nextRow < depth) {
                for (int coffset = -1; coffset <= 1; coffset++) {
                    int nextCol = col + coffset;
                    if (nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                        locations.add(new Location(nextRow, nextCol));
                    }
                }
            }
        }
        // Embaralha a lista para aleatorizar as buscas
        Collections.shuffle(locations);
        return locations.iterator();
    }

    /**
     * Retorna uma lista embaralhada de todas as posições adjacentes livres.
     */
    public List<Location> getFreeAdjacentLocations(Location location) {
        List<Location> free = new LinkedList<>();
        Iterator<Location> it = adjacentLocations(location);
        while (it.hasNext()) {
            Location loc = it.next();
            if (getObjectAt(loc) == null) {
                free.add(loc);
            }
        }
        return free;
    }

    /**
     * Retorna uma única localização adjacente livre, ou null se não houver nenhuma.
     */
    public Location freeAdjacentLocation(Location location) {
        List<Location> free = getFreeAdjacentLocations(location);
        if (free.isEmpty()) {
            return null;
        } else {
            return free.get(0);
        }
    }

    /**
     * Retorna uma localização aleatória do campo (útil para inicializações).
     */
    public Location randomLocation() {
        int row = (int) (Math.random() * depth);
        int col = (int) (Math.random() * width);
        return new Location(row, col);
    }
}
