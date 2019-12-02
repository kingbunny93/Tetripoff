package TetripoffPack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.awt.GraphicsConfiguration;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
Very Heavy Influence from Jan Bodnar who created a tutorial on zetcode:
http://zetcode.com/tutorials/javagamestutorial/tetris/ which taught me step by step
how to recreate this game. Out of all the videos and tutorials, I found his/her's to be
the simplest and easiest to understand.
*/

public class Tetripoff extends JFrame {

    private JLabel statusbar;
    static GraphicsConfiguration gc;

    int diffLev = 0;

    public Tetripoff(int df) {
        setDiffLev(df);
        initUI();
    }

    private void setDiffLev(int d){
        this.diffLev = d;
    }
    private int getDiffLev(){
        return this.diffLev;
    }
    private void initUI() {
        statusbar = new JLabel(" 0");
        add(statusbar, BorderLayout.SOUTH);
        var board = new Board(this, diffLev);
        add(board);
        board.start();
        setTitle("Tetripoff");
        setSize(400, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    JLabel getStatusBar() {
        return statusbar;
    }
    public static void main(String[] args) {
        int ez = 600;
        int norm = 300;
        int hard = 150;

        JButton bPlay = new JButton("Play");
        JButton bHiScores = new JButton("Hi-Scores");
        JButton bHowToPlay = new JButton("How to Play");
        JButton bExit = new JButton("Exit");
        JLabel tetripoff = new JLabel(("TET-RI-POFF"));


        JFrame frame = new JFrame(gc);
        frame.setTitle("Tet-ri-poff");
        frame.setSize(600, 800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);

        frame.add(tetripoff);
        frame.add(bPlay);
        frame.add(bHiScores);
        frame.add(bHowToPlay);
        frame.add(bExit);

        tetripoff.setBounds(263, 20, 200, 40);
        bPlay.setBounds(200, 100, 200, 80);
        bHiScores.setBounds(200, 250, 200, 80);
        bHowToPlay.setBounds(200, 400, 200, 80);
        bExit.setBounds(200, 550, 200, 80);

        /** Difficulty Menu Pops up when you press the Play button */
        bPlay.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {

                JFrame DiffMenu = new JFrame(gc);
                DiffMenu.setTitle("Difficulty");
                DiffMenu.setSize(600, 800);
                DiffMenu.setVisible(true);
                DiffMenu.setResizable(false);
                DiffMenu.setLayout(null);

                JButton bEasy = new JButton("EASY");
                JButton bNormal = new JButton("NORMAL");
                JButton bHard = new JButton("HARD");

                DiffMenu.add(bEasy);
                DiffMenu.add(bNormal);
                DiffMenu.add(bHard);

                bEasy.setBounds(200, 150, 200, 80);
                bNormal.setBounds(200, 325, 200, 80);
                bHard.setBounds(200, 500, 200, 80);

                // was trying to figure out how to do this in an easier way.
                bEasy.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        EventQueue.invokeLater(() -> {
                            var game = new Tetripoff(ez);      // EASY MODE
                            game.setVisible(true);
                        });
                    }
                });
                bNormal.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        EventQueue.invokeLater(() -> {
                            var game = new Tetripoff(norm);    // NORMAL MODE
                            game.setVisible(true);
                        });
                    }
                });
                bHard.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        EventQueue.invokeLater(() -> {
                            var game = new Tetripoff(hard);    // HARD MODE
                            game.setVisible(true);
                        });
                    }
                });
            }
        });



        bHowToPlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                EventQueue.invokeLater(() -> {

                    JFrame htpframe = new JFrame(gc);
                    htpframe.setTitle("How To Play");
                    htpframe.setSize(600, 800);
                    htpframe.setVisible(true);
                    htpframe.setResizable(false);
                    htpframe.setLayout(null);

                    JButton bhtpOK = new JButton ("Okie-Dokie");
                    bhtpOK.setBounds(250, 650, 100, 50);

                    htpframe.add(bhtpOK);
                    //create function to read text from HowToPlay file and display on frame.
                    bhtpOK.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent arg0) {
                            EventQueue.invokeLater(() -> {
                                htpframe.dispose();
                            });
                        }
                    });

                });
            }
        });

        bHiScores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*
                    Scanner sc = new Scanner("HiScores.txt");
                    System.out.println("Name : Number\n" +  "--------------" );
                    while(sc.hasNextLine()){
                        System.out.println(sc.nextLine() + " : " + sc.nextLine());
                    }
                    System.out.println("--------------");
                   */
                try (BufferedReader br = new BufferedReader(new FileReader(new File("HiScores.txt")))) {
                    String text = null;
                    while ((text = br.readLine()) != null)
                    {
                            System.out.println(br.readLine() + " : " + br.readLine());
                        }
                        System.out.println("--------------");
                    } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        bExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                EventQueue.invokeLater(() -> {
                    frame.dispose();
                });
            }
        });
    }
}

/*

bhtpOK.addActionListener(new ActionListener() {
         @Override
          public void actionPerformed(ActionEvent arg0) {
           EventQueue.invokeLater(() -> {

           });
         }
});*/