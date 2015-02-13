import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class MainWindow extends JFrame {
    
    private ImageIcon white, grey, green, red;
    private Game game;

    public MainWindow() {

        this.game = new Game(this);

        this.white = new ImageIcon("white.png");
        this.grey = new ImageIcon("grey.png");
        this.green = new ImageIcon("green.png");
        this.red = new ImageIcon("red.png");

        this.setBackground(Color.WHITE);

        JPanel corePanel = new JPanel();
        corePanel.setLayout(new BorderLayout());
        corePanel.setBackground(Color.WHITE);

        final JLabel turn = new JLabel("Your turn");

        JPanel northPanel = new JPanel();
        northPanel.setBackground(Color.WHITE);
        northPanel.add(turn);

        corePanel.add(northPanel, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel(new GridLayout(3, 3, 8, 8));
        boardPanel.setBackground(Color.WHITE);

        final JButton[] buttons = new JButton[9];

        for (int i = 0; i < 9; i++) {
            final JButton btn = new JButton(this.white);
            btn.setRolloverIcon(this.grey);
            btn.setPressedIcon(this.white);
            btn.setDisabledIcon(this.white);
            btn.setBorder(null);
            buttons[i] = btn;
            btn.setActionCommand(Integer.toString(i));

            if (i == 3) {
                btn.setEnabled(false);
            }

            boardPanel.add(btn);

            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    Object source = evt.getSource();
                    if (source instanceof JButton) {
                        String cmd = evt.getActionCommand();

                        try {
                            int space = Integer.parseInt(cmd);
                            game.move(space);
                        } catch (NumberFormatException e) {}

                    }
                }
            });
        }

        corePanel.add(boardPanel, BorderLayout.CENTER);

        this.add(corePanel, BorderLayout.CENTER);
        
        this.pack();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }
    
    public static void main(String[] args) {
        new MainWindow();
    }
}