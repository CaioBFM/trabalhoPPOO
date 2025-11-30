/**
 * Fornece um contador para um participante na simulação.
 * Isso inclui uma string de identificação e uma contagem de quantos
 * participantes deste tipo existem atualmente dentro da simulação.
 * 
 * @author GRUPO 5
 * @version 2025
 */
public class Counter {
    /** Um nome para este tipo de participante da simulação */
    private String name;
    /** Quantos deste tipo existem na simulação. */
    private int count;

    /**
     * Fornece um nome para um dos tipos de simulação.
     * 
     * @param name Um nome, por exemplo, "Raposa".
     */
    public Counter(String name) {
        this.name = name;
        count = 0;
    }

    /**
     * @return A descrição curta deste tipo.
     */
    public String getName() {
        return name;
    }

    /**
     * @return A contagem atual para este tipo.
     */
    public int getCount() {
        return count;
    }

    /**
     * Incrementa a contagem atual em um.
     */
    public void increment() {
        count++;
    }

    /**
     * Reinicia a contagem atual para zero.
     */
    public void reset() {
        count = 0;
    }
}
