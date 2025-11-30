import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Esta classe coleta e fornece alguns dados estatísticos sobre o estado 
 * de um campo. Ela é flexível: criará e manterá um contador 
 * para qualquer classe de objeto encontrada dentro do campo.
 * 
 * @author GRUPO 5
 * @version 2025
 */
public class FieldStats
{
    /** Contadores para cada tipo de entidade (raposa, coelho, etc.) na simulação. */
    private HashMap counters;
    /** Se os contadores estão atualmente atualizados. */
    private boolean countsValid;

    /**
     * Constrói um objeto de estatísticas de campo.
     */
    public FieldStats()
    {
        // Configura uma coleção para contadores para cada tipo de animal que
        // podemos encontrar
        counters = new HashMap();
        countsValid = true;
    }

    /**
     * @return Uma string descrevendo quais animais estão no campo.
     */
    public String getPopulationDetails(Field field)
    {
        StringBuffer buffer = new StringBuffer();
        if(!countsValid) {
            generateCounts(field);
        }
        Iterator keys = counters.keySet().iterator();
        while(keys.hasNext()) {
            Counter info = (Counter) counters.get(keys.next());
            buffer.append(info.getName());
            buffer.append(": ");
            buffer.append(info.getCount());
            buffer.append(' ');
        }
        return buffer.toString();
    }
    
   /**
     * Invalida o conjunto atual de estatísticas; redefine todas as 
     * contagens para zero.
     */
    public void reset()
    {
        countsValid = false;
        Iterator keys = counters.keySet().iterator();
        while(keys.hasNext()) {
            Counter cnt = (Counter) counters.get(keys.next());
            cnt.reset();
        }
    }

    /**
     * Incrementa a contagem para uma classe de animal.
     */
    public void incrementCount(Class animalClass)
    {
        Counter cnt = (Counter) counters.get(animalClass);
        if(cnt == null) {
            // we do not have a counter for this species yet - create one
            cnt = new Counter(animalClass.getName());
            counters.put(animalClass, cnt);
        }
        cnt.increment();
    }

    /**
     * Indica que uma contagem de animais foi concluída.
     */
    public void countFinished()
    {
        countsValid = true;
    }

   /**
     * Determina se a simulação ainda é viável.
     * Isto é, se ela deve continuar rodando.
     * 
     * @return true Se houver mais de uma espécie viva.
     */
    public boolean isViable(Field field)
    {
        // Quantas contagens são diferentes de zero.
        int nonZero = 0;
        if(!countsValid) {
            generateCounts(field);
        }
        Iterator keys = counters.keySet().iterator();
        while(keys.hasNext()) {
            Counter info = (Counter) counters.get(keys.next());
            if(info.getCount() > 0) {
                nonZero++;
            }
        }
        return nonZero > 1;
    }
    
   /**
     * Gera contagens do número de raposas e coelhos.
     * Elas não são mantidas atualizadas conforme raposas e coelhos
     * são colocados no campo, mas apenas quando uma solicitação
     * é feita para a informação.
     */
    private void generateCounts(Field field)
    {
        reset();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                Object animal = field.getObjectAt(row, col);
                if(animal != null) {
                    incrementCount(animal.getClass());
                }
            }
        }
        countsValid = true;
    }
}
