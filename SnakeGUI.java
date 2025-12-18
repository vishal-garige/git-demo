
//       


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SnakeGUI extends JPanel implements ActionListener, KeyListener {

    static final int rows = 20, cols = 20, cellSize = 25;
    static final int max = 300;
    int[][] snake = new int[max][2]; // snake[i] = {x, y}
    int snakelength = 1;
    int foodx, foody;
    char direction = 'R';
    boolean gameOver = false;
    Timer timer;

    public SnakeGUI() {
        setPreferredSize(new Dimension(cols * cellSize, rows * cellSize));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        initGame();
        timer = new Timer(200, this);
        timer.start();
    }

    public void initGame() {
        snake[0][0] = 10;
        snake[0][1] = 10;
        placeFood();
    }

    public void placeFood() {
        while (true) {
            foodx = (int)(Math.random() * rows);
            foody = (int)(Math.random() * cols);
            boolean onSnake = false;
            for (int i = 0; i < snakelength; i++) {
                if (snake[i][0] == foodx && snake[i][1] == foody) {
                    onSnake = true;
                    break;
                }
            }
            if (!onSnake) break;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Grid
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i <= rows; i++)
            g.drawLine(0, i * cellSize, cols * cellSize, i * cellSize);
        for (int j = 0; j <= cols; j++)
            g.drawLine(j * cellSize, 0, j * cellSize, rows * cellSize);

        // Snake
        g.setColor(Color.GREEN);
        for (int i = 0; i < snakelength; i++) {
            g.fillRect(snake[i][1] * cellSize, snake[i][0] * cellSize, cellSize, cellSize);
        }

        // Food
        g.setColor(Color.RED);
        g.fillOval(foody * cellSize, foodx * cellSize, cellSize, cellSize);

        // Game Over
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over!", 60, rows * cellSize / 2);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            timer.stop();
            repaint();
            return;
        }
        moveSnake();
        repaint();
    }

    public void moveSnake() {
        int headX = snake[0][0], headY = snake[0][1];
        int newX = headX, newY = headY;

        switch (direction) {
            case 'U': newX--; break;
            case 'D': newX++; break;
            case 'L': newY--; break;
            case 'R': newY++; break;
        }

        if (newX < 0 || newX >= rows || newY < 0 || newY >= cols) {
            gameOver = true;
            return;
        }

        for (int i = 0; i < snakelength; i++) {
            if (snake[i][0] == newX && snake[i][1] == newY) {
                gameOver = true;
                return;
            }
        }

        if (newX == foodx && newY == foody) {
            snakelength++;
            placeFood();
        } else {
            // Move body: shift all parts back
            for (int i = snakelength - 1; i > 0; i--) {
                snake[i][0] = snake[i - 1][0];
                snake[i][1] = snake[i - 1][1];
            }
        }

        // Update head
        snake[0][0] = newX;
        snake[0][1] = newY;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                if (direction != 'D') direction = 'U';
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') direction = 'D';
                break;
            case KeyEvent.VK_LEFT:
                if (direction != 'R') direction = 'L';
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') direction = 'R';
                break;
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game - Arrays Only");
        SnakeGUI game = new SnakeGUI();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
