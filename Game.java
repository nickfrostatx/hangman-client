import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Game {
    private static final String HOSTNAME = "nfrost.me";
    private static final int PORT = 33376;

    private MainWindow window;
    private boolean connected;
    private SocketChannel channel;
    private String command;

    public Game() {
        this.window = new MainWindow(this);
        this.connected = false;
        this.command = "";
    }

    public void connect() throws IOException {
        this.channel = SocketChannel.open();
        this.channel.configureBlocking(false);
        this.channel.connect(new InetSocketAddress(HOSTNAME, PORT));
    }

    public void update() throws IOException {
        if (this.channel.finishConnect()) {
            if (!this.connected) {
                this.connected = true;
                this.window.setTurnLabel("Waiting for opponent");
            }

            ByteBuffer buf = ByteBuffer.allocate(20);
            while (this.channel.read(buf) > 0) {
                buf.flip();
                this.command += Charset.defaultCharset().decode(buf);
                if (this.command.indexOf('\n') != -1) {
                    this.executeCommand(this.command.substring(0, this.command.indexOf('\n')));
                    this.command = this.command.substring(this.command.indexOf('\n') + 1);
                }
            }
        }
    }

    public void executeCommand(String str) {
        System.out.println(str);
        Scanner scanner = new Scanner(str);
        String cmd = scanner.next();
        String data = "";
        if (scanner.hasNext()) {
            data = scanner.next();
        }
        if (cmd.equals("newgame")) {
            if (data.equals("1")) {
                this.window.setTurnLabel("Your turn");
                this.window.setInputEnabled(true);
            } else {
                this.window.setTurnLabel("Their turn");
                this.window.setInputEnabled(false);
            }
        } else if (cmd.equals("move")) {
            int space = Integer.parseInt(data);
            this.window.setSpaceColor(space, 2);
            this.window.setTurnLabel("Your turn");
            this.window.setInputEnabled(true);
        } else if (cmd.equals("left")) {
            this.window.setTurnLabel("Opponent left, waiting for another player");
            this.window.resetBoard();
            this.window.setInputEnabled(false);
        } else if (cmd.equals("win")) {
            this.window.setTurnLabel("You won!");
            this.window.setInputEnabled(false);
        } else if (cmd.equals("loss")) {
            this.window.setTurnLabel("You lost");
            this.window.setInputEnabled(false);
            for (int i = 0; i < data.length(); i++) {
                int s = Integer.parseInt("" + data.charAt(i));
                this.window.setSpaceColor(s, 2);
            }
        }
        this.window.repaint();
    }

    public void move(int space) throws IOException {
        this.window.setSpaceColor(space, 1);
        this.window.setInputEnabled(false);
        this.window.setTurnLabel("Their turn");
        CharBuffer buffer = CharBuffer.wrap("" + space);
        System.out.println(space);
        System.out.println(buffer);
        System.out.println();
        while (buffer.hasRemaining()) {
            this.channel.write(Charset.defaultCharset().encode(buffer));
        }
    }

    public static void main(String[] args) throws Exception {
        Game game = new Game();
        game.connect();
        while (true) {
            game.update();
        }
    }
}
