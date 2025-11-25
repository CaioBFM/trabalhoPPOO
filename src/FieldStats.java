import java.util.HashMap;
import java.util.Iterator;

/**
 * Classe que coleta e fornece dados estatísticos sobre o estado do campo.
 * Mantém um contador para cada tipo de objeto (raposa, coelho etc.).
 * 
 * Versão simplificada — usando apenas HashMap sem generics,
 * conforme o estilo original do livro de Barnes & Kolling.
 */
public class FieldStats {
    // Contadores para cada tipo de animal no campo
    private HashMap counters;
    // Indica se os contadores estão atualizados
    private boolean countsValid;

    /**
     * Constrói um objeto FieldStats.
     */
    public FieldStats() {
        counters = new HashMap();
        countsValid = true;
    }

    /**
     * Retorna uma string descrevendo as populações no campo.
     */
    public String getPopulationDetails(Field field) {
        StringBuffer buffer = new StringBuffer();
        if (!countsValid) {
            generateCounts(field);
        }
        Iterator keys = counters.keySet().iterator();
        while (keys.hasNext()) {
            Counter info = (Counter) counters.get(keys.next());
            buffer.append(info.getName());
            buffer.append(": ");
            buffer.append(info.getCount());
            buffer.append(' ');
        }
        return buffer.toString();
    }

    /**
     * Invalida as estatísticas atuais e zera todas as contagens.
     */
    public void reset() {
        countsValid = false;
        Iterator keys = counters.keySet().iterator();
        while (keys.hasNext()) {
            Counter counter = (Counter) counters.get(keys.next());
            counter.reset();
        }
    }

    /**
     * Incrementa o contador de uma determinada classe de animal.
     */
    public void incrementCount(Class animalClass) {
        Counter counter = (Counter) counters.get(animalClass);
        if (counter == null) {
            // cria novo contador para essa classe
            counter = new Counter(animalClass.getName());
            counters.put(animalClass, counter);
        }
        counter.increment();
    }

    /**
     * Indica que a contagem foi finalizada.
     */
    public void countFinished() {
        countsValid = true;
    }

    /**
     * Determina se a simulação ainda é viável
     * (ou seja, se há mais de uma espécie viva).
     */
    public boolean isViable(Field field) {
        int nonZero = 0;
        if (!countsValid) {
            generateCounts(field);
        }
        Iterator keys = counters.keySet().iterator();
        while (keys.hasNext()) {
            Counter info = (Counter) counters.get(keys.next());
            if (info.getCount() > 0) {
                nonZero++;
            }
        }
        return nonZero > 1;
    }

    /**
     * Gera as contagens de todos os animais do campo.
     */
    private void generateCounts(Field field) {
        reset();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Object obj = field.getObjectAt(row, col);
                if (obj != null) {
                    incrementCount(obj.getClass());
                }
            }
        }
        countsValid = true;
    }
}
