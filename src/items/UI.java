package items;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Random;

public class UI extends JFrame {
    private static final int CELL_SIZE = 60;
    private final Field field;
    private final JPanel gamePanel;
    private final JLabel statusLabel;
    private boolean gameStarted = false;
    private boolean gameEnded = false;

    public UI(Field field) {
        this.field = field;
        setTitle("Поймай кота!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        statusLabel = new JLabel("Выберите начальную позицию кота", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(statusLabel, BorderLayout.NORTH);

        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawField(g);
            }
        };
        gamePanel.setLayout(new GridLayout(field.SIDE_Y, field.SIDE_X));
        gamePanel.setPreferredSize(new Dimension(field.SIDE_X * CELL_SIZE, field.SIDE_Y * CELL_SIZE));

        for (int y = 0; y < field.SIDE_Y; y++) {
            for (int x = 0; x < field.SIDE_X; x++) {
                JPanel cellPanel = new JPanel();
                cellPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cellPanel.setBackground(Color.WHITE);

                final int cellX = x;
                final int cellY = y;

                cellPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleCellClick(cellX, cellY);
                    }
                });

                gamePanel.add(cellPanel);
            }
        }

        add(gamePanel, BorderLayout.CENTER);

        JButton restartButton = new JButton("Новая игра");
        restartButton.addActionListener(e -> resetGame());
        add(restartButton, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void drawField(Graphics g) {
        for (Cell cell : field.CELLS) {
            int x = cell.X() * CELL_SIZE;
            int y = cell.Y() * CELL_SIZE;

            if (cell.IS_BLOCKED) {
                g.setColor(Color.GRAY);
                g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            } else if (cell.IS_FROZEN) {
                g.setColor(Color.BLUE);
                g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            } else {
                g.setColor(Color.WHITE);
                g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            }

            g.setColor(Color.BLACK);
            g.drawRect(x, y, CELL_SIZE, CELL_SIZE);

            if (cell.IS_OCCUPIED && field.CAT != null) {
                g.setColor(Color.ORANGE);
                g.fillOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);
                g.setColor(Color.BLACK);
                g.drawString("Кот", x + CELL_SIZE / 2 - 10, y + CELL_SIZE / 2);
            }
        }
    }

    private void handleCellClick(int x, int y) {
        if (!gameStarted && !gameEnded) {
            for (Cell cell : field.CELLS) {
                if (cell.X() == x && cell.Y() == y) {
                    Cat cat = new Cat(cell);
                    field.setCatOnField(cat);
                    gameStarted = true;
                    statusLabel.setText("Игра началась! Блокируйте клетки вокруг кота");
                    drawField(gamePanel.getGraphics());
                    return;
                }
            }
        } else if (!gameEnded) {
            for (Cell cell : field.CELLS) {
                if (cell.X() == x && cell.Y() == y && !cell.IS_OCCUPIED) {
                    cell.block(true);
                    drawField(gamePanel.getGraphics());
                    checkGameStatus();
                    moveCat();
                    return;
                }
            }
        }
    }

    //TODO написать алгоритм перемещения и нахождения пути проще
    private void moveCat() {
        if (field.CAT == null) return;

        Side[] possibleMoves = Side.values();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            Side move = possibleMoves[random.nextInt(possibleMoves.length)];
            Cell nextCell = field.CAT.CELL.getNeighbor(move);

            if (nextCell != null && !nextCell.IS_BLOCKED && !nextCell.IS_FROZEN) {
                field.CAT.move(move);
                drawField(gamePanel.getGraphics());
                checkGameStatus();
                return;
            }
        }
    }

    private void checkGameStatus() {
        if (field.isCatOnBorder()) {
            statusLabel.setText("Вы проиграли! Кот сбежал.");
            gameEnded = true;
        } else if (field.isCatClosed()) {
            statusLabel.setText("Поздравляем! Вы поймали кота!");
            gameEnded = true;
        }
    }

    private void resetGame() {
        for (Cell cell : field.CELLS) {
            cell.block(false);
            cell.freeze(false);
            cell.IS_OCCUPIED = false;
        }
        field.CAT = null;
        gameStarted = false;
        gameEnded = false;
        statusLabel.setText("Выберите начальную позицию кота");
        drawField(gamePanel.getGraphics());
    }

    public static void main(String[] args) {
        int sideX = 10;
        int sideY = 10;
        List<Cell> cells = new java.util.ArrayList<>();

        for (int y = 0; y < sideY; y++) {
            for (int x = 0; x < sideX; x++) {
                cells.add(new Cell(x, y));
            }
        }

        for (Cell cell : cells) {
            int x = cell.X();
            int y = cell.Y();

            if (x > 0) cell.setNeighbor(Side.LEFT, getCell(cells, x - 1, y));
            if (x < sideX - 1) cell.setNeighbor(Side.RIGHT, getCell(cells, x + 1, y));
            if (y > 0) cell.setNeighbor(Side.TOP, getCell(cells, x, y - 1));
            if (y < sideY - 1) cell.setNeighbor(Side.BOTTOM, getCell(cells, x, y + 1));
        }

        Field field = new Field(cells, sideX, sideY);

        SwingUtilities.invokeLater(() -> {
            UI UI = new UI(field);
            UI.setVisible(true);
        });
    }

    private static Cell getCell(List<Cell> cells, int x, int y) {
        for (Cell cell : cells) {
            if (cell.X() == x && cell.Y() == y) {
                return cell;
            }
        }
        return null;
    }
}