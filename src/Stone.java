public class Stone implements Obstacles {
    private Location location;
    public static final int NUM_STONES = 10;

    public Stone(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

}
