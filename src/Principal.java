public class Principal {
  public static void main(String[] args) {
    Simulator simulator = new Simulator(80, 120);
    // simulator.runLongSimulation();
    simulator.simulate(200);
  }
}
