package TetripoffPack;

import TetripoffPack.Shape.Tetrominoe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;

public class Board extends JPanel{

    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 22;
    static GraphicsConfiguration gc;

    private String diff = "";               // to set difficulty
    private int finalScore = 0;             // Score after calculating LinesCleared * Difficulty
    private Timer timer;
    private boolean isFallDone = false;
    private boolean isPaused = false;
    private int linesCleared = 0;
    private int curX = 0;
    private int curY = 0;

    private JLabel statusbar;
    private Shape currentPiece;
    private Tetrominoe[] board;

    private int dropRate = 0;      // Base speed (will change based on difficulty level)

    public Board(Tetripoff parent, int df) {
        this.dropRate = df;
        initBoard(parent);
    }

    private void initBoard(Tetripoff parent) {
        setFocusable(true);
        statusbar = parent.getStatusBar();
        addKeyListener(new TAdapter());
    }

    private int squareWidth() {
        return (int) getSize().getWidth() / BOARD_WIDTH;
    }

    private int squareHeight() {
        return (int) getSize().getHeight() / BOARD_HEIGHT;
    }

    private Tetrominoe shapeAt(int x, int y) {
        return board[(y * BOARD_WIDTH) + x];
    }

    /** starts the game, begins with a clear board, starts the timer and adds a new tetrominoe */
    void start() {
        currentPiece = new Shape();
        board = new Tetrominoe[BOARD_WIDTH * BOARD_HEIGHT];
        clearBoard();
        newPiece();
        timer = new Timer(dropRate, new GameCycle());
        timer.start();

    }
    /** Method temporarily pauses the games current status. Can be unpaused */
    private void pause() {
        isPaused = !isPaused;
        if (isPaused) {
            statusbar.setText("paused");
        } else {
            statusbar.setText(String.valueOf(linesCleared));
        }
        repaint();
    }

    /** Method will pause the game, and open up the Hi Scores window */
    private void checkScores() {
        pause();
        System.out.println("It doesn't do anything at the moment");
    }

    /** Method called will remove the current piece, save it's information on the side, create a new piece,
     * and after that piiece is placed then it will bring back this exact piece. */
    private void holdPiece() {  // this is to be incorporated
        pause();
        System.out.println("It doesn't do anything at the moment");
    }

    /** Using the difficulty chosen by the User, the speed/period Interval will change and affect
     * the score as well.*/
    private int calculateScore() {
        if (dropRate == 150) {
            finalScore = 300 * linesCleared; }
        else if (dropRate == 300) {
            finalScore = 200 * linesCleared; }
        else if (dropRate == 600) {
            finalScore = 100 * linesCleared; }
        else {                                  // something went wrong
            return 1000000;
        }
        return finalScore;
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        var size = getSize();
        int boardTop = (int) size.getHeight() - BOARD_HEIGHT * squareHeight();
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                Tetrominoe shape = shapeAt(j, BOARD_HEIGHT - i - 1);
                if (shape != Tetrominoe.NoShape) {
                    drawSquare(g, j * squareWidth(),
                            boardTop + i * squareHeight(), shape);
                }
            }
        }
        if (currentPiece.getShape() != Tetrominoe.NoShape) {
            for (int i = 0; i < 4; i++) {
                int x = curX + currentPiece.x(i);
                int y = curY - currentPiece.y(i);
                drawSquare(g, x * squareWidth(),
                        boardTop + (BOARD_HEIGHT - y - 1) * squareHeight(),
                        currentPiece.getShape());
            }
        }
    }
    private void dropDown() {
        int newY = curY;
        while (newY > 0) {
            if (!tryMove(currentPiece, curX, newY - 1))
            {
                break;
            }
            newY--;
        }
        pieceDropped();
    }
    private void oneLineDown() {
        if (!tryMove(currentPiece, curX, curY - 1)) {
            pieceDropped();
        }
    }
    private void clearBoard() {
        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {
            board[i] = Tetrominoe.NoShape;
        }
    }
    private void pieceDropped() {
        for (int i = 0; i < 4; i++) {
            int x = curX + currentPiece.x(i);
            int y = curY - currentPiece.y(i);
            board[(y * BOARD_WIDTH) + x] = currentPiece.getShape();
        }
        removeFullLines();
        if (!isFallDone) {
            newPiece();
        }
    }
    private void newPiece() {
        currentPiece.setRandomShape();
        curX = BOARD_WIDTH / 2 + 1;
        curY = BOARD_HEIGHT - 1 + currentPiece.minY();
        if (!tryMove(currentPiece, curX, curY)) {
            currentPiece.setShape(Tetrominoe.NoShape);
            timer.stop();
            var msg = String.format("Game over. Lines Removed: %d", linesCleared);
            statusbar.setText(msg);
            saveMenu();
        }
    }
    private boolean tryMove(Shape newPiece, int newX, int newY) {
        for (int i = 0; i < 4; i++) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
                return false;
            }
            if (shapeAt(x, y) != Tetrominoe.NoShape) {
                return false;
            }
        }
        currentPiece = newPiece;
        curX = newX;
        curY = newY;
        repaint();
        return true;
    }
    private void removeFullLines() {
        int numFullLines = 0;
        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
            boolean lineIsFull = true;
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (shapeAt(j, i) == Tetrominoe.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }
            if (lineIsFull) {
                numFullLines++;
                for (int k = i; k < BOARD_HEIGHT - 1; k++) {
                    for (int j = 0; j < BOARD_WIDTH; j++) {
                        board[(k * BOARD_WIDTH) + j] = shapeAt(j, k + 1);
                    }
                }
            }
        }
        if (numFullLines > 0) {
            linesCleared += numFullLines;
            statusbar.setText(String.valueOf(linesCleared));
            isFallDone = true;
            currentPiece.setShape(Tetrominoe.NoShape);
        }
    }
    private void drawSquare(Graphics g, int x, int y, Tetrominoe shape) {
        Color colors[] = {new Color(0, 0, 0),   // NOBlock
                new Color(20, 255, 20),   // RSBlock
                new Color(255, 10, 10),   // SBlock
                new Color(100, 200, 200),   // LNBlock
                new Color(170, 50, 255),    // TBlock
                new Color(200, 180, 0),     // SQBlock
                new Color(220, 150, 0),   // LBlock
                new Color(20, 10, 250)      // RLBlock
        };
        var color = colors[shape.ordinal()];
        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);
        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);
        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + 1);
    }
    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }
    private void doGameCycle() {
        update();
        repaint();
    }
    private void update() {
        if (isPaused) {
            return;
        }
        if (isFallDone) {
            isFallDone = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }
    private void saveMenu(){

        JFrame saveframe = new JFrame(gc);
        saveframe.setTitle("Pause");
        saveframe.setSize(400, 250);
        saveframe.setVisible(true);
        saveframe.setResizable(false);

        JButton bSave = new JButton ("Save");
        bSave.setBounds(60, 100, 140, 40);

        JButton bSNo = new JButton ("No");
        bSave.setBounds(200, 100, 140, 40);

        saveframe.add(bSave);
        saveframe.add(bSNo);

        saveframe.setLayout(null);

        pause();

        bSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0){
                EventQueue.invokeLater(() -> {
                    try {
                        saveScore();
                        timer.stop();
                        var msg = String.format("Game over. Lines Removed: %d", linesCleared);
                        statusbar.setText(msg);
                    } catch (IOException e) {
                        saveframe.dispose();
                    }
                });
            }
        });
        bSNo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0){
                EventQueue.invokeLater(() -> {
                    saveframe.dispose();
                    isPaused = !isPaused;
                });
            }
        });
    }
    private void saveScore() throws IOException {
        File hs = new File("HiScores.txt");
        JFrame hsframe = new JFrame("Save Score");
        JButton bSaveToFile= new JButton("Save");
        bSaveToFile.setBounds(60,100,140, 40);
        JButton bCancelToFile = new JButton ("Cancel");
        bCancelToFile.setBounds(200,100,140, 40);

        JLabel entName = new JLabel();
        entName.setText("Enter your Name:");
        entName.setBounds(30, 10, 100, 100);

        JLabel label = new JLabel();
        label.setBounds(10, 110, 200, 100);

        JTextField tfName = new JTextField();
        tfName.setBounds(130, 50, 130, 30);

        hsframe.add(entName);
        hsframe.add(tfName);
        hsframe.add(label);


        hsframe.add(bSaveToFile);
        hsframe.setSize(400,250);
        hsframe.setLayout(null);
        hsframe.setVisible(true);
        hsframe.add(bCancelToFile);


        bCancelToFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hsframe.dispose();
            }
        });

        bSaveToFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = tfName.getText();
                int score = calculateScore();
                try {
                    FileWriter fw = new FileWriter(hs, true);
                    fw.append(name + "\n" + score + "\n");
                    fw.close();
                    label.setText("Score Saved.");
                }
                catch (IOException el){
                    hsframe.dispose();
                    label.setText("Error.");
                }
            }
        });
    }
    class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (currentPiece.getShape() == Tetrominoe.NoShape) {
                return;
            }
            int keycode = e.getKeyCode();
            // Java 12 switch expressions
            if (keycode == KeyEvent.VK_P)
                pause();
            if (keycode == KeyEvent.VK_LEFT)
                tryMove(currentPiece, curX - 1, curY);
            if (keycode == KeyEvent.VK_RIGHT)
                tryMove(currentPiece, curX + 1, curY);
            if (keycode == KeyEvent.VK_UP)
                tryMove(currentPiece.rotateRight(), curX, curY);
            if (keycode == KeyEvent.VK_DOWN)
                tryMove(currentPiece.rotateLeft(), curX, curY);;
            if (keycode == KeyEvent.VK_SPACE)
                dropDown();
            if (keycode == KeyEvent.VK_D)
                oneLineDown();
            if (keycode == KeyEvent.VK_S)
                saveMenu();
            if (keycode == KeyEvent.VK_C)
                holdPiece();
            if(keycode == KeyEvent.VK_TAB)
                checkScores();
        }
    }


}
