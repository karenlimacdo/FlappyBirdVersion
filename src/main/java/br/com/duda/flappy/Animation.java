package br.com.duda.flappy;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import javax.swing.ImageIcon;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Animation {

    public static boolean clicked = false;

    public Game game;
    public ImageIcon img = new ImageIcon("flappyIcon.png");

    private boolean running = true;
    private boolean paused = false;
    private long t0 = 0;
    private long t1 = 0;
    private double dt = 0;

    private int menu = 1;

    public BufferStrategy strategy;

    public Animation(Game g) {
        this.game = g;

        // Interface Grafica
        Canvas canvas = new Canvas();
        JFrame container = new JFrame(Game.TITLE);

        container.setIconImage(img.getImage());

        JPanel panel = (JPanel) container.getContentPane();
        panel.setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        panel.setLayout(null);

        container.setResizable(false);
        canvas.setBounds(0, 0, Game.WIDTH, Game.HEIGHT);
        panel.add(canvas);
        canvas.setIgnoreRepaint(true);

        container.pack();
        container.setVisible(true);

        // Fecha janela
        container.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Tratamento do teclado
        canvas.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent evt) {
                game.onKeyPressed(keyString(evt));
            }

            @Override
            public void keyReleased(KeyEvent evt) {
            }

            @Override
            public void keyTyped(KeyEvent evt) {
            }
        });

        canvas.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Clique do mouse
                menu = game.onClicked(e.getPoint().x, e.getPoint().y);
            }
        });

        canvas.createBufferStrategy(2);
        strategy = canvas.getBufferStrategy();
        canvas.requestFocus();

        container.pack();
        container.setLocationRelativeTo(null);

    }

    // pega a tecla
    private static String keyString(KeyEvent evt) {
        if (evt.getKeyChar() != KeyEvent.CHAR_UNDEFINED) {
            return String.valueOf(evt.getKeyChar()).toLowerCase();
        }
        return "";
    }

    // Loop menu da animacao na tela
    private void menuLoop() {
        int contador = 0;
        while (this.running) {
            // if (this.paused) {
            //      return;
            //  }

            this.t1 = System.currentTimeMillis();
            this.dt = (this.t1 - this.t0) * 0.001; // tempo em segundos para 1 frame
            this.t0 = this.t1;

            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

       //     game.drawMenu(new Screen(g), contador); // desenha situacao atual
            contador++;
            if (contador > 150) {
                contador = 0;
            }

            strategy.show();

            if (menu == 1) {
                break;
            } else if (menu == 2) {
                break;
            }
            System.out.println("MENU LOOP");
        }
        if (menu == 1) {
            this.mainLoop();
        } else if (menu == 2) {
            this.menuRecords();
        }
    }

    // Menu Score
    private void menuRecords() {
        while (this.running) {
            //if (!this.running) {
            //   return;
            //}

            this.t1 = System.currentTimeMillis();
            this.dt = (this.t1 - this.t0) * 0.001; // tempo em segundos para 1 frame
            this.t0 = this.t1;

            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

            game.drawRecords(new Screen(g)); // desenha situacao atual

            strategy.show();

            if (menu == 3) {
                // this.menuLoop();
                break;
            }
            System.out.println("MENU RECORDS");
        }
        if (menu == 3) {
            this.menuLoop();

        }
    }

    // Loop principal da animacao na tela
    private void mainLoop() {
        double timeReady = 0;
        while (this.running) {
            if (!this.running) {
                return;
            }

            this.t1 = System.currentTimeMillis();
            this.dt = (this.t1 - this.t0) * 0.001; // tempo em segundos para 1 frame
            this.t0 = this.t1;

            /*       timeReady += dt;

            if (timeReady > 2) {
            
             */
       //     if (menu == 1) {
                game.step(this.dt); // passo/frame do jogo
                Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
                game.draw(new Screen(g)); // desenha situacao atual
                strategy.show();
      //      }
            
         //   if (this.game.isGameOver()) {
         //       menu = 3;
          //      this.turnOff();

                // if (menu == 3) {
                //this.running = true;
                //this.turnOn();
         //       this.menuLoop();
                //      break;
           // }
            
        }
        /*     } else {
                Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
                Screen s = new Screen(g);

                // desenha background
                s.drawImage(0, 0, 288, 512, 0, 0, 0);
                s.drawImage(0, 0, 288, 512, 0, 288, 0);
                s.drawImage(0, 0, 288, 512, 0, 288 * 2, 0);

                // desenha ground
                s.drawImage(292, 0, 308, 112, 0, 0, Game.HEIGHT - 112);
                s.drawImage(292, 0, 308, 112, 0, 308, Game.HEIGHT - 112);
                s.drawImage(292, 0, 308, 112, 0, 308 * 2, Game.HEIGHT - 112);

                // desenha Get Ready
                s.drawImage(290, 441, 176, 46, 0, (Game.WIDTH - 200) / 2, 60);

                // desenha passaro
                s.drawImage(528, 128, 34, 24, 0, 35, (Game.WIDTH - 112) / 2 + 24 / 2);

                strategy.show();
            
        }  */

 /*game.step(this.dt); // passo/frame do jogo

            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

            game.draw(new Screen(g)); // desenha situacao atual
            strategy.show();

            if (this.game.isGameOver()) {
                this.turnOff();
                
            }*/
      //  System.out.println("MAIN LOOP");
   // }
   /* if (menu
        == 3) {
            this.running = true;
        this.menuLoop();
    } */
    
}

// inicia animacao
public void turnOn() {
        // this.menu = 1;
        this.t0 = System.currentTimeMillis();
        this.running = true;

        this.mainLoop();
        // this.menuLoop();
    }

    // para animacao
    public void turnOff() {
        this.running = false;
        //if (menu == 3) {
        //  this.turnOn();
        // this.menuLoop();
        // }
    }

}
