import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A simple predator-prey simulator, based on a field containing
 * rabbits and foxes.
 * * @author David J. Barnes and Michael Kolling
 * 
 * @version 2002-04-09
 */
public class Simulator {
    // The private static final variables represent
    // configuration information for the simulation.
    private static final int DEFAULT_WIDTH = 50;
    private static final int DEFAULT_DEPTH = 50;
    private static final double FOX_CREATION_PROBABILITY = 0.02;
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;
    private static final double HUNTER_CREATION_PROBABILITY = 0.01; // Baixa densidade
    private static final double TREE_CREATION_PROBABILITY = 0.05;

    // The list of actors in the field (renamed from animals)
    private List<Actor> actors;
    // The list of actors just born
    private List<Actor> newActors;

    private List<Obstacles> obstacles;
    // The current state of the field.
    private Field field;
    // A second field, used to build the next stage of the simulation.
    private Field updatedField;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;

    private String currentSeason;

    private static final int SEASON_LENGTH = 50;

    private Random rand = new Random();

    /**
     * Construct a simulation field with default size.
     */
    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     */
    public Simulator(int depth, int width) {
        if (width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        // Use a generic list for any Actor
        actors = new ArrayList<>();
        newActors = new ArrayList<>();
        obstacles = new ArrayList<>();
        field = new Field(depth, width);
        updatedField = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Fox.class, Color.blue);
        view.setColor(Rabbit.class, Color.orange);
        view.setColor(Hunter.class, Color.magenta);
        view.setColor(Tree.class, Color.green);
        view.setColor(Stone.class, Color.gray);
        
        // Conectar o botão da View à lógica do Simulator
        view.setStepListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                simulateOneStep();
            }
        });

        currentSeason = "spring";

        // Setup a valid starting point.
        reset();
    }

    /**
     * Run the simulation from its current state for a reasonably long period.
     */
    public void runLongSimulation() {
        simulate(500);
    }

    /**
     * Run the simulation from its current state for the given number of steps.
     */
    public void simulate(int numSteps) {
        for (int step = 1; step <= numSteps && view.isViable(field); step++) {
            try {
                // Pausa por 1000 milissegundos (1 segundo) entre cada passo.
                // Aumente este número para deixar mais lento (ex: 500 para meio segundo).
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            simulateOneStep();
        }
    }

    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each actor.
     */
    public void simulateOneStep() {
        step++;
        newActors.clear();

        // let all actors act
        for (Iterator iter = actors.iterator(); iter.hasNext();) {
            Object obj = iter.next();
            // Using Actor instead of Animal allows for polymorphism
            if (obj instanceof Actor) {
                Actor actor = (Actor) obj;
                if (actor.isAlive()) {
                    actor.act(field, updatedField, newActors);
                } else {
                    iter.remove(); // remove dead actors from collection
                }
            } else {
                System.out.println("found unknown actor");
            }
        }
        // add new born actors to the list of actors
        actors.addAll(newActors);

        for (Obstacles obstacle : obstacles) {
            if (obstacle instanceof Stone) {
                Stone stone = (Stone) obstacle;
                Location loc = stone.getLocation();
                // Garante que a pedra esteja presente no campo “novo”
                updatedField.place(stone, loc);
            }
        }

        // Swap the field and updatedField at the end of the step.
        Field temp = field;
        field = updatedField;
        updatedField = temp;
        updatedField.clear();

        updateSeason();

        // display the new field on screen
        view.showStatus(step, field, currentSeason);
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        step = 0;
        actors.clear();
        field.clear();
        updatedField.clear();
        putStonesInField(field);
        populate(field);

        currentSeason = "spring";
        updateSeason();

        // Show the starting state in the view.
        view.showStatus(step, field, currentSeason);
    }

    /**
     * Place stones randomly in the field.
     */
    private void putStonesInField(Field field) {
        obstacles.clear();
        for (int i = 0; i < Stone.NUM_STONES; i++) {
            int row = rand.nextInt(field.getDepth());
            int col = rand.nextInt(field.getWidth());
            // Verifica se já existe um objeto na posição
            if (field.getObjectAt(row, col) == null) {
                Location loc = new Location(row, col);
                Stone stone = new Stone(loc);
                field.place(stone, loc);
                obstacles.add(stone);
                placeAndPropagateStone(row, col, field);
            } else {
                i--; // Tenta novamente se a posição já estiver ocupada
            }
        }
    }

    /**
     * Place a stone and propagate its effect to surrounding locations.
     */
    private void placeAndPropagateStone(int row, int col, Field field) {
        for (int i = 0; i < 6; i++) {
            Location base = new Location(row, col);
            Location loc = field.freeAdjacentLocation(base);

            // Se não há mais lugar livre em volta, para o laço
            if (loc != null) {
                Stone stone = new Stone(loc);
                field.place(stone, loc);
                obstacles.add(stone);
            }
        }
    }

    /**
     * Populate the field with foxes and rabbits.
     */
    private void populate(Field field) {
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                // Se já tem uma pedra, não coloca nada
                if (field.getObjectAt(row, col) instanceof Stone) {
                    continue;
                }
                // Se já tem algo (outro ator), não faz nada
                if (field.getObjectAt(row, col) != null) {
                    continue;
                }

                if (rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Fox fox = new Fox(true);
                    actors.add(fox);
                    fox.setLocation(row, col);
                    field.place(fox, row, col);
                } else if (rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Rabbit rabbit = new Rabbit(true);
                    actors.add(rabbit);
                    rabbit.setLocation(row, col);
                    field.place(rabbit, row, col);
                } else if (rand.nextDouble() <= HUNTER_CREATION_PROBABILITY) {
                    Hunter hunter = new Hunter(true);
                    actors.add(hunter);
                    hunter.setLocation(row, col);
                    field.place(hunter, row, col);
                } else if (rand.nextDouble() <= TREE_CREATION_PROBABILITY) {
                    Tree tree = new Tree();
                    actors.add(tree);
                    tree.setLocation(new Location(row, col));
                    field.place(tree, row, col);
                }
                // senão, deixa o local vazio
            }
        }
        Collections.shuffle(actors);
    }

    private void updateSeason() {
        int seasonIndex = (step / SEASON_LENGTH) % 4;
        switch (seasonIndex) {
            case 0:
                currentSeason = "spring";
                break;
            case 1:
                currentSeason = "summer";
                break;
            case 2:
                currentSeason = "autumn";
                break;
            case 3:
            default:
                currentSeason = "winter";
                break;
        }
    }
}