import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.awt.Color;

public class Simulator {
    // Tamanho padrão do campo
    private static final int DEFAULT_DEPTH = 50;
    private static final int DEFAULT_WIDTH = 50;

    // Probabilidades de criação
    private static final double FOX_CREATION_PROBABILITY = 0.02;
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;

    private final ArrayList<Animal> animals;
    private final Field field;
    private int step;
    private final SimulatorView view;
    private final Random rand = new Random();

    public Simulator() { this(DEFAULT_DEPTH, DEFAULT_WIDTH); }

    public Simulator(int depth, int width) {
        if (width <= 0 || depth <= 0) {
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        animals = new ArrayList<>();
        field = new Field(depth, width);
        view = new SimulatorView(depth, width);
        view.setColor(Rabbit.class, Color.ORANGE);
        view.setColor(Fox.class, Color.BLUE);
        reset();
    }

    public void runLongSimulation() { simulate(500); }

    public void simulate(int numSteps) {
        for (int i = 1; i <= numSteps && view.isViable(field); i++) {
            simulateOneStep();
        }
    }

    public void simulateOneStep() {
        step++;
        List<Animal> newAnimals = new ArrayList<>();

        // POLIMORFISMO: cada espécie executa seu próprio act()
        for (Iterator<Animal> it = animals.iterator(); it.hasNext();) {
            Animal animal = it.next();
            animal.act(newAnimals);
            if (!animal.isAlive()) it.remove();
        }

        animals.addAll(newAnimals);
        view.showStatus(step, field);
    }

    public void reset() {
        step = 0;
        animals.clear();
        field.clear();
        populate();
        view.showStatus(step, field);
    }

    private void populate() {
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                double r = rand.nextDouble();
                Location location = new Location(row, col);
                if (r <= FOX_CREATION_PROBABILITY) {
                    animals.add(new Fox(true, field, location));
                } else if (r <= FOX_CREATION_PROBABILITY + RABBIT_CREATION_PROBABILITY) {
                    animals.add(new Rabbit(true, field, location));
                }
                // senão, deixa vazio
            }
        }
    }
}
