import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class MainWindow extends JFrame {
    
    private ImageIcon white, grey, green, red;
    private JButton[] buttons;
    private JLabel turnLabel;
    private int[] spaces;

    public MainWindow(Game g) {

        final Game game = g;

        this.spaces = new int[9];

        this.white = new ImageIcon(getClass().getResource("/images/white.png"));
        this.grey = new ImageIcon(getClass().getResource("/images/grey.png"));
        this.green = new ImageIcon(getClass().getResource("/images/green.png"));
        this.red = new ImageIcon(getClass().getResource("/images/red.png"));

        this.setBackground(Color.WHITE);

        JPanel corePanel = new JPanel();
        corePanel.setLayout(new BorderLayout());
        corePanel.setBackground(Color.WHITE);

        this.turnLabel = new JLabel("Connecting");

        JPanel northPanel = new JPanel();
        northPanel.setBackground(Color.WHITE);
        northPanel.add(this.turnLabel);
        northPanel.add(Box.createVerticalStrut(40));

        corePanel.add(northPanel, BorderLayout.NORTH);

        // Spacers

        JPanel eastPanel = new JPanel();
        eastPanel.setBackground(Color.WHITE);
        eastPanel.add(Box.createHorizontalStrut(16));

        corePanel.add(eastPanel, BorderLayout.EAST);

        JPanel westPanel = new JPanel();
        westPanel.setBackground(Color.WHITE);
        westPanel.add(Box.createHorizontalStrut(16));

        corePanel.add(westPanel, BorderLayout.WEST);

        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.WHITE);
        southPanel.add(Box.createVerticalStrut(16));

        corePanel.add(southPanel, BorderLayout.SOUTH);

        // Board

        JPanel boardPanel = new JPanel(new GridLayout(3, 3, 8, 8));
        boardPanel.setBackground(Color.WHITE);

        this.buttons = new JButton[9];

        for (int i = 0; i < 9; i++) {
            final JButton btn = new JButton(this.white);
            btn.setRolloverIcon(this.grey);
            btn.setPressedIcon(this.grey);
            btn.setDisabledIcon(this.white);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBorder(null);
            btn.setEnabled(false);
            btn.setActionCommand(Integer.toString(i));

            this.buttons[i] = btn;

            boardPanel.add(btn);

            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    String cmd = evt.getActionCommand();
                    try {
                        int space = Integer.parseInt(cmd);
                        game.move(space);
                    } catch (NumberFormatException e) {
                        // Not a space
                    } catch (Exception e) {
                        setTurnLabel("Error communicating with server");
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

    public void resetBoard() {
        for (int i = 0; i < this.buttons.length; i++) {
            this.setSpaceColor(i, 0);
        }
    }

    public void setInputEnabled(boolean enabled) {
        for (int i = 0; i < this.buttons.length; i++) {
            if (this.spaces[i] == 0) {
                this.buttons[i].setEnabled(enabled);
            }
        }
    }

    public void setSpaceColor(int space, int colorId) {
        switch (colorId) {
            case 0:
                this.buttons[space].setDisabledIcon(this.white);
                break;
            case 1:
                this.buttons[space].setDisabledIcon(this.green);
                this.buttons[space].setEnabled(false);
                break;
            case 2:
                this.buttons[space].setDisabledIcon(this.red);
                this.buttons[space].setEnabled(false);
                break;
        }
        this.spaces[space] = colorId;
    }

    public void setTurnLabel(String text) {
        this.turnLabel.setText(text);
    }
}
