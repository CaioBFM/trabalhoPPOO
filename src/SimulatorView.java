import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.HashMap;

/**
 * Uma visualização gráfica da grade de simulação.
 * A visualização exibe um retângulo colorido para cada localização
 * representando seu conteúdo. Ela usa uma cor de fundo padrão.
 * Cores para cada tipo de espécie podem ser definidas usando o
 * método setColor.
 * 
 * @author GRUPO 05
 * @version 2025
 */
public class SimulatorView extends JFrame {
    /** Cores usadas para localizações vazias. */
    private static final Color EMPTY_COLOR = Color.white;

    /** Cor usada para objetos que não têm cor definida. */
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private JLabel stepLabel, population;
    private FieldView fieldView;

    /** Botão que controla simulação passo por passo */
    private JButton stepButton;

    /** Um mapa para armazenar cores para participantes na simulação */
    private HashMap colors;
    /**
     * Um objeto de estatística calculando e armazenando informações da simulação
     */
    private FieldStats stats;

    /**
     * Cria uma visualização com a largura e altura fornecidas.
     * 
     * @param height A altura da simulação (linhas).
     * @param width  A largura da simulação (colunas).
     */
    public SimulatorView(int height, int width) {
        stats = new FieldStats();
        colors = new HashMap();

        setTitle("Fox and Rabbit Simulation - GRUPO 5");

        // Configuração para fechar a janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);

        setLocation(100, 50);

        fieldView = new FieldView(height, width);

        // Configura botão para continuar
        stepButton = new JButton("Próximo Passo");

        // Cria uma fonte maior (Estilo: Negrito, Tamanho: 16 ou 18)
        Font fonteGrande = new Font("SansSerif", Font.CENTER_BASELINE, 20);

        // Aplica a fonte nos componentes
        stepLabel.setFont(fonteGrande);
        population.setFont(fonteGrande);
        stepButton.setFont(fonteGrande);

        // Aumentar o botão em si adicionando margem interna
        stepButton.setPreferredSize(new Dimension(200, 40));

        // Painel superior para o Label de passos
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(stepLabel, BorderLayout.CENTER);

        // Painel inferior para População e Botão
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(stepButton, BorderLayout.NORTH);
        bottomPanel.add(population, BorderLayout.SOUTH);

        Container contents = getContentPane();
        contents.add(topPanel, BorderLayout.NORTH);
        contents.add(fieldView, BorderLayout.CENTER);
        contents.add(bottomPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    /**
     * Adiciona um listener para o botão de passo.
     * Permite que a classe Simulator controle o que acontece quando o botão é
     * clicado.
     * 
     * @param listener O ouvinte de ação a ser anexado.
     */
    public void setStepListener(ActionListener listener) {
        stepButton.addActionListener(listener);
    }

    /**
     * Define uma cor a ser usada para uma dada classe de objeto.
     * 
     * @param actorClass A classe do ator.
     * @param color      A cor a ser usada.
     */
    public void setColor(Class actorClass, Color color) {
        colors.put(actorClass, color);
    }

    /**
     * Retorna a cor a ser usada para uma dada classe de objeto.
     * 
     * @param actorClass A classe do objeto.
     * @return A cor definida ou a cor desconhecida padrão.
     */
    private Color getColor(Class actorClass) {
        Color col = (Color) colors.get(actorClass);
        if (col == null) {
            // nenhuma cor definida para esta classe
            return UNKNOWN_COLOR;
        } else {
            return col;
        }
    }

    /**
     * Mostra o estado atual do campo.
     * 
     * @param step          Qual é o passo da iteração.
     * @param field         O campo a ser representado.
     * @param currentSeason A estação atual da simulação.
     */
    public void showStatus(int step, Field field, String currentSeason) {
        if (!isVisible())
            setVisible(true);

        stepLabel.setText(STEP_PREFIX + step);

        stats.reset();
        fieldView.preparePaint();

        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Object object = field.getObjectAt(row, col);
                if (object != null) {
                    stats.incrementCount(object.getClass());

                    Color color;

                    // Lógica para mudar a cor da árvore dependendo da estação
                    if (object instanceof Tree && currentSeason != null) {
                        color = getTreeColorForSeason(currentSeason);
                    } else {
                        color = getColor(object.getClass());
                    }

                    fieldView.drawMark(col, row, color);
                } else {
                    fieldView.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }
        stats.countFinished();

        population.setText(
                POPULATION_PREFIX + stats.getPopulationDetails(field)
                        + "  Season: " + (currentSeason != null ? currentSeason : ""));
        fieldView.repaint();
    }

    /**
     * Retorna a cor da árvore baseada na estação do ano atual.
     */
    private Color getTreeColorForSeason(String season) {
        switch (season) {
            case "spring":
                return Color.green; // Verde claro
            case "summer":
                return new Color(0, 200, 55); // Verde escuro
            case "autumn":
                return Color.red; // vermelho
            case "winter":
                return new Color(136, 70, 20); // Marrom
            default:
                return getColor(Tree.class); // Cor padrão
        }
    }

    /**
     * Determina se a simulação deve continuar rodando.
     * 
     * @param field O campo atual.
     * @return true Se houver mais de uma espécie viva.
     */
    public boolean isViable(Field field) {
        return stats.isViable(field);
    }

    /**
     * Fornece uma visualização gráfica de um campo retangular. Esta é
     * uma classe aninhada (uma classe definida dentro de uma classe) que
     * define um componente customizado para a interface de usuário. Este
     * componente exibe o campo.
     * Isso é algo de GUI bastante avançado - você pode ignorar isso
     * para o seu projeto se quiser.
     */
    private class FieldView extends JPanel {
        private final int GRID_VIEW_SCALING_FACTOR = 8;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Cria um novo componente FieldView.
         */
        public FieldView(int height, int width) {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Diz ao gerenciador de GUI quão grande gostaríamos de ser.
         */
        public Dimension getPreferredSize() {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR,
                    gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepara para uma nova rodada de pintura. Como o componente
         * pode ser redimensionado, calcula o fator de escala novamente.
         */
        public void preparePaint() {
            if (!size.equals(getSize())) { // if the size has changed...
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if (xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if (yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }

        /**
         * Pinta uma localização da grade neste campo com uma cor dada.
         */
        public void drawMark(int x, int y, Color color) {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale - 1, yScale - 1);
        }

        /**
         * O componente de visualização de campo precisa ser reexibido. Copia a
         * imagem interna para a tela.
         */
        public void paintComponent(Graphics g) {
            if (fieldImage != null) {
                g.drawImage(fieldImage, 0, 0, null);
            }
        }
    }
}
