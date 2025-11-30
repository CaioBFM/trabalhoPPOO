/**
 * Representa uma localização em uma grade retangular.
 * 
 * @author GRUPO 05
 * @version 2025
 */
public class Location {
    /** Posição da linha */
    private int row;
    /** Posição da coluna */
    private int col;

    /**
     * Representa uma linha e coluna.
     * 
     * @param row A linha.
     * @param col A coluna.
     */
    public Location(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Implementa igualdade de conteúdo.
     */
    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            Location other = (Location) obj;
            return row == other.getRow() && col == other.getCol();
        } else {
            return false;
        }
    }

    /**
     * Retorna uma string no formato linha,coluna.
     * 
     * * @return Uma representação em string da localização.
     */
    public String toString() {
        return row + "," + col;
    }

    /**
     * Usa os 16 bits superiores para o valor da linha e a parte inferior para
     * a coluna. Exceto para grades muito grandes, isso deve fornecer um
     * código hash único para cada par (linha, coluna).
     */
    public int hashCode() {
        return (row << 16) + col;
    }

    /**
     * @return A linha.
     */
    public int getRow() {
        return row;
    }

    /**
     * @return A coluna.
     */
    public int getCol() {
        return col;
    }
}
