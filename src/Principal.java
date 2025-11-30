/**
 * Classe principal para iniciar a aplicação de simulação.
 * @author GRUPO 05
 * @version 2025
 */
public class Principal {
  public static void main(String[] args) {
    Simulator simulator = new Simulator(80, 120);
    // simulator.runLongSimulation();
    simulator.simulate(150);
  }
}
