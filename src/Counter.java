/**
 * Fornece uma contagem para um tipo particular de objeto.
 * Os objetos de Counter são usados para armazenar o número de
 * instâncias de uma classe específica encontradas no campo.
 * 
 * Baseado em: "Programação Orientada a Objetos com Java – Uma Introdução Prática Usando o BlueJ"
 * (Barnes & Kolling, 4ª edição)
 */
public class Counter {
    // Nome do tipo de objeto que está sendo contado
    private String name;
    // Valor atual da contagem
    private int count;

    /**
     * Cria um contador com o nome fornecido.
     * @param name O nome do tipo de objeto a ser contado.
     */
    public Counter(String name) {
        this.name = name;
        this.count = 0;
    }

    /**
     * @return O nome deste contador (ex: "Fox", "Rabbit")
     */
    public String getName() {
        return name;
    }

    /**
     * @return O valor atual da contagem.
     */
    public int getCount() {
        return count;
    }

    /**
     * Incrementa a contagem em 1.
     */
    public void increment() {
        count++;
    }

    /**
     * Reseta a contagem para zero.
     */
    public void reset() {
        count = 0;
    }
}
