public class Game {
    private MainWindow window;
    private int[] spaces;

    public Game(MainWindow window) {
        this.window = window;
        spaces = new int[9];
    }

    public void move(int space) {
        this.spaces[space] = 1;
        this.window.setSpaceColor(space, 1);
        this.window.setInputEnabled(false);
    }
}
