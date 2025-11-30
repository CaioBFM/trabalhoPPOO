import java.util.Random;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Um simulador simples de predador-presa, baseado em um campo contendo
 * coelhos e raposas.
 * @author GRUPO 05
 * @version 2025
 */
public class Simulator {
    // As variáveis estáticas finais privadas representam
    // informações de configuração para a simulação.

    /** A largura padrão do campo. */
    private static final int DEFAULT_WIDTH = 50;
    /** A profundidade padrão do campo. */
    private static final int DEFAULT_DEPTH = 50;
    /** A probabilidade de uma raposa ser criada em qualquer posição do campo. */
    private static final double FOX_CREATION_PROBABILITY = 0.02;
    /** A probabilidade de um coelho ser criado em qualquer posição do campo. */
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;
    /** A probabilidade de um caçador ser criado (Baixa densidade). */
    private static final double HUNTER_CREATION_PROBABILITY = 0.05; 
    /** A probabilidade de uma árvore ser criada. */
    private static final double TREE_CREATION_PROBABILITY = 0.05;
    
    /** A lista de atores no campo (renomeado de animais para ser genérico) */
    private List<Actor> actors;
    /** A lista de atores que acabaram de nascer */
    private List<Actor> newActors;
    /** Lista de obstáculos (pedras, etc) */
    private List<Obstacles> obstacles;
    /** O estado atual do campo */
    private Field field;
    /** Um segundo campo, usado para construir o próximo estágio da simulação. */
    private Field updatedField;
    /** O passo atual da simulação. */
    private int step;
    /** Uma visualização gráfica da simulação. */
    private SimulatorView view;
    /** A estação do ano atual (string). */
    private String currentSeason;
    /** Duração de cada estação em passos. */
    private static final int SEASON_LENGTH = 50;
    /** Gerador de números aleatórios */
    private Random rand = new Random();
    /** Flag para controlar se a simulação está rodando ou parada. */
    private boolean notSimulating;

    /**
     * Constrói um campo de simulação com tamanho padrão.
     */
    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Cria um campo de simulação com o tamanho fornecido.
     * @param depth A profundidade (altura) do campo.
     * @param width A largura do campo.
     */
    public Simulator(int depth, int width) {
        if (width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        // Usa uma lista genérica para qualquer Actor
        actors = new ArrayList<>();
        newActors = new ArrayList<>();
        obstacles = new ArrayList<>();
        field = new Field(depth, width);
        updatedField = new Field(depth, width);

        // Cria uma visualização do estado de cada localização no campo.
        view = new SimulatorView(depth, width);

        // Define as cores para cada classe de ator na visualização
        view.setColor(Fox.class, Color.blue);
        view.setColor(Rabbit.class, Color.orange);
        view.setColor(Hunter.class, Color.magenta);
        view.setColor(Tree.class, Color.green);
        view.setColor(Stone.class, Color.gray);

        // Conectar o botão da View à lógica do Simulator
        // Isso permite controlar o "passo a passo" pela interface gráfica
        view.setStepListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (notSimulating) {
                    simulateOneStep();
                } else {
                    JOptionPane.showMessageDialog(null,
                            "A simulação está em andamento. Aguarde a conclusão antes de usar o modo passo a passo.");
                }
            }
        });

        notSimulating = true;
        currentSeason = "spring"; // Começa na primavera

        // Configura um ponto de partida válido.
        reset();
    }

    /**
     * Lê um arquivo de mapa e coloca pedras onde houver um 'X'.
     * @param filename O caminho do arquivo de texto.
     * @param field O campo onde as pedras serão colocadas.
     */
    private void loadStonesFromFile(String filename, Field field) {
        obstacles.clear(); // Limpa pedras antigas

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            int row = 0;

            // Lê linha por linha
            while ((line = reader.readLine()) != null && row < field.getDepth()) {
                // Lê caractere por caractere da linha
                for (int col = 0; col < line.length() && col < field.getWidth(); col++) {
                    char symbol = line.charAt(col);

                    if (symbol == 'X' || symbol == 'x') {
                        // Verifica se a posição é válida e está vazia
                        if (field.getObjectAt(row, col) == null) {
                            Location loc = new Location(row, col);
                            Stone stone = new Stone(loc);
                            field.place(stone, loc);
                            obstacles.add(stone);
                        }
                    }
                }
                row++;
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Erro ao carregar mapa: " + e.getMessage());
            // Opcional: Se der erro ao ler o arquivo, gera pedras aleatórias como fallback
            putStonesInField(field);
        }
    }

    /**
     * Executa a simulação a partir do seu estado atual por um período razoavelmente longo.
     * (Define 500 passos como padrão).
     */
    public void runLongSimulation() {
        simulate(500);
    }

    /**
     * Executa a simulação a partir do seu estado atual pelo número fornecido de passos.
     * Para antes se a simulação deixar de ser viável (ex: extinção).
     * @param numSteps O número de passos a executar.
     */
    public void simulate(int numSteps) {
        for (int step = 1; step <= numSteps && view.isViable(field); step++) {
            notSimulating = false;
            try {
                // Pausa por _ milissegundos entre cada passo.
                // Aumente este número para deixar mais lento (ex: 500 para meio segundo).
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            simulateOneStep();
        }
        notSimulating = true;
    }

    /**
     * Executa a simulação a partir do seu estado atual por um único passo.
     * Itera sobre todo o campo atualizando o estado de cada ator.
     */
    public void simulateOneStep() {
        step++;
        newActors.clear();

        // Deixa todos os atores agirem
        for (Iterator iter = actors.iterator(); iter.hasNext();) {
            Object obj = iter.next();
            // Usar Actor em vez de Animal permite polimorfismo (animais, caçadores, etc.)
            if (obj instanceof Actor) {
                Actor actor = (Actor) obj;
                if (actor.isAlive()) {
                    actor.act(field, updatedField, newActors);
                } else {
                    iter.remove(); // remove atores mortos da coleção
                }
            } else {
                System.out.println("found unknown actor");
            }
        }
        // Adiciona atores recém-nascidos à lista principal
        actors.addAll(newActors);

        // Obstáculos (como pedras) não agem, mas precisam ser copiados para o novo campo
        for (Obstacles obstacle : obstacles) {
            if (obstacle instanceof Stone) {
                Stone stone = (Stone) obstacle;
                Location loc = stone.getLocation();
                // Garante que a pedra esteja presente no campo “novo”
                updatedField.place(stone, loc);
            }
        }

        // Troca o campo (field) e o campo atualizado (updatedField) ao final do passo.
        Field temp = field;
        field = updatedField;
        updatedField = temp;
        updatedField.clear();

        updateSeason();

        // Exibe o novo campo na tela
        view.showStatus(step, field, currentSeason);
    }

    /**
     * Redefine a simulação para uma posição inicial.
     * Limpa o campo e repopula.
     */
    public void reset() {
        step = 0;
        actors.clear();
        field.clear();
        updatedField.clear();

        // Adicionando as pedras com base nos arquivos de mapa dentro da pasta mapas
        loadStonesFromFile("src/mapas/map.txt", field);

        populate(field);

        currentSeason = "spring";
        updateSeason();

        // Mostra o estado inicial na visualização.
        view.showStatus(step, field, currentSeason);
    }

    /**
     * Coloca pedras aleatoriamente no campo (Fallback caso o mapa falhe).
     * @param field O campo onde colocar as pedras.
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
     * Coloca uma pedra e propaga seu efeito para localizações vizinhas
     * (cria aglomerados de pedras).
     * @param row A linha central.
     * @param col A coluna central.
     * @param field O campo atual.
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
     * Popula o campo com raposas, coelhos, caçadores e árvores.
     * @param field O campo a ser populado.
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

    /**
     * Atualiza a estação do ano atual baseada no número de passos.
     * A estação muda a cada SEASON_LENGTH passos.
     */
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