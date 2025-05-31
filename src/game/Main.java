package game;

import game.model.Game;
import game.model.GameStatus;
import game.model.events.GameActionEvent;
import game.model.events.GameActionListener;
import game.model.field.ObstructionType;
import game.ui.FieldWidget;
import game.ui.WidgetFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GamePanel::new);
    }

    public static final int FIELD_SIDE_LENGTH = 5;

    static class GamePanel extends JFrame {

        private Game game;
        private WidgetFactory widgetFactory;

        private JPanel obstructionPanel;
        private final java.util.Map<ObstructionType, JButton> obstructionButtons = new java.util.EnumMap<>(ObstructionType.class);

        public GamePanel() throws HeadlessException {
            getContentPane().setLayout(new BorderLayout());
            setVisible(true);
            startGame();
            setResizable(false);

            JMenuBar menuBar = new JMenuBar();
            menuBar.add(createGameMenu());
            setJMenuBar(menuBar);

            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }

        private JMenu createGameMenu() {
            JMenu gameMenu = new JMenu("Игра");
            JMenuItem newGameMenuItem = new JMenuItem(new NewGameAction());
            JMenuItem exitMenuItem = new JMenuItem(new ExitAction());
            gameMenu.add(newGameMenuItem);
            gameMenu.add(exitMenuItem);
            return gameMenu;
        }

        private void startGame() {
            widgetFactory = new WidgetFactory();
            game = new Game(Main.FIELD_SIDE_LENGTH);

            game.addGameActionListener(new GameController());

            JPanel content = (JPanel) this.getContentPane();
            content.removeAll();
            FieldWidget fieldWidget = new FieldWidget(game.getGameField(), widgetFactory);
            content.add(fieldWidget);
            createOrUpdateObstructionPanel();

            pack();
        }

        private void createOrUpdateObstructionPanel() {
            if (obstructionPanel == null) {
                obstructionPanel = new JPanel();
                obstructionPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
                getContentPane().add(obstructionPanel, BorderLayout.NORTH);
            } else {
                obstructionPanel.removeAll();
                obstructionButtons.clear();
            }

            ObstructionType current = game.getCurrentObstructionType();

            for (ObstructionType type : ObstructionType.values()) {
                int count = game.getObstructionCount(type);
                String label = type.toString() + (count == -1 ? " ∞" : " " + count);

                JButton btn = new JButton(label);

                if (type == current) {
                    btn.setEnabled(false);
                    btn.setBackground(new Color(120, 220, 120)); // pleasant green
                    btn.setForeground(Color.BLACK);
                    btn.setOpaque(true);
                    btn.setBorderPainted(false);
                } else {
                    btn.setEnabled(count != 0);
                    btn.setBackground(UIManager.getColor("Button.background"));
                    btn.setForeground(UIManager.getColor("Button.foreground"));
                    btn.setOpaque(true);
                    btn.setBorderPainted(true);
                }

                btn.addActionListener(e -> {
                    boolean success = game.selectObstructionType(type);

                    if (!success) {
                        System.out.println("- Can't select obstruction type -");
                    }
                });

                obstructionButtons.put(type, btn);
                obstructionPanel.add(btn);
            }
            obstructionPanel.revalidate();
            obstructionPanel.repaint();
        }

        private class NewGameAction extends AbstractAction {

            public NewGameAction() {
                putValue(NAME, "Новая");
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(GamePanel.this,
                        "Начать новую игру?", "Новая игра", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) startGame();
            }
        }

        private static class ExitAction extends AbstractAction {

            public ExitAction() {
                putValue(NAME, "Выход");
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        }

        private final class GameController implements GameActionListener {

            @Override
            public void catIsMoved(@NotNull GameActionEvent event) {
                createOrUpdateObstructionPanel();
            }

            @Override
            public void gameStatusChanged(@NotNull GameActionEvent event) {
                GameStatus status = event.getStatus();
                if (status != GameStatus.GAME_IS_ON) {
                    String message = "";
                    switch (status) {
                        case WIN:
                            message = "Вы выиграли!";
                            break;
                        case GAME_ABORTED:
                            message = "Игра завершена досрочно";
                            break;
                        case LOSS:
                            message = "Вы проиграли - кот добрался к краю поля.";
                            break;
                    }

                    showMessage(message);
                }
            }

            @Override
            public void selectedObstructionChanged(@NotNull GameActionEvent event) {
                createOrUpdateObstructionPanel();
                System.out.println("+ Obstruction type changed +");
            }

            /**
             * Отображает диалогово окно.
             *
             * @param message - сообщение.
             */
            private void showMessage(String message) {
                String[] options = {"Ок"};
                JOptionPane.showOptionDialog(
                        GamePanel.this,
                        message,
                        "Игра окончена",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]
                );
            }
        }
    }
}