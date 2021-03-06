package br.com.duda.flappy;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class FlappyBird implements Game {

    private double ground_offset = 0;
    private double background_offset = 0;
    private double groundVelocity = 70;

    private static Animation animation = new Animation(new FlappyBird());

    private Bird bird;
    private ArrayList<Pipe> pipes = new ArrayList<Pipe>();
    private Score score;
    private Button buttonStart;
    private Button buttonScore;
    private Button buttonMenu;
    private static int vet[] = new int[3];
    private static String dat[] = new String[3];

    private Random generator = new Random();

    private double timePipes = 0;
    private double limitPipes = 2.5;

    private boolean isGameOver = false;
    private boolean scoreCounted = false;

    private double timeMenu = 0;
    private int contador = 0;
    private double timeReady = 0;

    private int stateGame = 0; // 0 -- Menu de Opções ; 1 -- Get Ready ; 2-- Stated ; 3-- Records

    public FlappyBird() {
        this.initialize();
    }

    @Override
    public void initialize() {
        this.bird = new Bird(35, (Game.WIDTH - 112) / 2 + 24 / 2);
        this.score = new Score(0, 5, 5);
        this.buttonStart = new Button(((Game.WIDTH - (90 * 2)) / 2) - 20, Game.HEIGHT - 80, 1);
        this.buttonScore = new Button(((Game.WIDTH - (90 * 2)) / 2) + 118, Game.HEIGHT - 80, 2);
        this.buttonMenu =  new Button(((Game.WIDTH - (90 * 2)) / 2) + 49, Game.HEIGHT - 80, 3);

        // this.addPipes();
    }

    public void init() {
        this.bird = new Bird(35, (Game.WIDTH - 112) / 2 + 24 / 2);
        this.score = new Score(0, 5, 5);
        this.timePipes = 0;
        this.pipes.clear();
        this.isGameOver = false;
        this.addPipes();
    }

    private void addPipes() {
        //pipes.add(new Pipe(Game.WIDTH + 10, generator.nextInt(Game.HEIGHT - 112 - Pipe.HOLE_SIZE), -groundVelocity));
        int randomY = generator.nextInt(Pipe.MAX_HEIGHT - 20);
        pipes.add(new Pipe(Game.WIDTH + 10, randomY < 30 ? 30 : randomY, -groundVelocity));

    }

    @Override
    public void onKeyPressed(String key) {
        if (key.equals(" ")) {
            bird.jump();
        }
    }

    @Override
    public int onClicked(double x, double y) {
        if (buttonStart.pointOnButton(x, y)) {
            System.out.println("Cliquei Start");
            // Animation.clicked = true;
            this.stateGame = 1;

            this.init();

            return 1;
        } else if (buttonScore.pointOnButton(x, y)) {
            System.out.println("Cliquei Score");

            this.stateGame = 3;

            return 2;
        } else if (buttonMenu.pointOnButton(x, y)) {
            System.out.println("Cliquei Menu");
            /*  if (isGameOver()) {
                animation.turnOn();
                start();
                System.exit(0);
                this.isGameOver = false;
            } */
            this.stateGame = 0;
            return 3;
        } else {
            System.out.println("Nao cliquei");
            return -1;
        }
    }

    public void stepGame(double dt) {
        this.stepPipes(dt); // atualiza velocidade dos canos

        bird.update(dt); // atualiza passaro

        // checa colisao do passaro com os limites da tela
        if (bird.y + 24 >= Game.HEIGHT - 112) {
            this.gameOver();
        } else if (bird.y <= 0) {
            this.gameOver();
        }

        // checa colisao do passaro com os canos
        for (Pipe cano : pipes) {
            cano.update(dt);
            if (bird.box.collision(cano.getBoxTop()) || bird.box.collision(cano.getBoxBottom())) {
                this.gameOver();
            }
        }

        // remove cano da lista ao sair da tela
        if (pipes.size() > 0 && pipes.get(0).x < -60) {
            pipes.remove(0);
            this.scoreCounted = false;
        }

        if (!this.scoreCounted && pipes.size() > 0 && pipes.get(0).x < bird.x - 35) {
            score.update(dt);
            this.scoreCounted = true;
        }
    }

    private void stepMenu(double dt) {
        this.timeMenu += dt;
        contador++;
        if (contador > 150) {
            contador = 0;
        }
    }
    
    private void stepReady(double dt) {
        this.timeReady += dt;
        if (this.timeReady > 2) {
            this.stateGame = 2;
        }
        contador++;
        if (contador > 150) {
            contador = 0;
        }
    }
    

    @Override
    public void step(double dt) {

        // Incrementa background
        this.background_offset += dt * 25;
        this.background_offset = this.background_offset % 288;

        // Incrementa ground
        this.ground_offset += dt * groundVelocity;
        this.ground_offset = this.ground_offset % 308;

        if (this.stateGame == 2 && !this.isGameOver) {
            this.stepGame(dt);
        }

        if (this.stateGame == 0) {
            this.stepMenu(dt);
        }
        
        if (this.stateGame == 1) {
            this.stepReady(dt);
        }
        
    }

    public void stepPipes(double dt) {
        this.timePipes += dt;
        if (this.timePipes > this.limitPipes) {
            this.addPipes();
            this.timePipes -= this.limitPipes;
        }
    }

    @Override
    public void drawMenu(Screen s) {
        // desenha background
        /*    s.drawImage(0, 0, 288, 512, 0, -background_offset, 0);
        s.drawImage(0, 0, 288, 512, 0, 288 - background_offset, 0);
        s.drawImage(0, 0, 288, 512, 0, 288 * 2 - background_offset, 0);
         */
        // desenha pipes
        s.drawImage(604, 130, 52, 140, 0, 120, 0); //cano de cima
        s.drawImage(660, 0, 52, 250, 0, 120, 240); //cano de baixo
        s.drawImage(604, 180, 52, 90, 0, 270, 0); //cano de cima
        s.drawImage(660, 0, 52, 250, 0, 270, 200); //cano de baixo

        // desenha ground
        s.drawImage(292, 0, 308, 112, 0, -ground_offset, Game.HEIGHT - 112);
        s.drawImage(292, 0, 308, 112, 0, 308 - ground_offset, Game.HEIGHT - 112);
        s.drawImage(292, 0, 308, 112, 0, 308 * 2 - ground_offset, Game.HEIGHT - 112);

        // desenha bird       
        if (contador >= 0 && contador <= 50) {
            s.drawImage(528, 128, 34, 24, 0, 35, (Game.WIDTH - 112) / 2 + 24 / 2);
        } else if (contador <= 100) {
            s.drawImage(528, 180, 34, 24, 0, 35, (Game.WIDTH - 112) / 2 + 24 / 2);
        } else if (contador <= 150) {
            s.drawImage(446, 248, 34, 24, 0, 35, (Game.WIDTH - 112) / 2 + 24 / 2);
        }

        // desenha título
        s.drawImage(288, 346, 200, 44, 0, (Game.WIDTH - 200) / 2, 12);

        // desenha botao de Start e Score
        buttonStart.draw(s);
        buttonScore.draw(s);

    }

    public void drawReady(Screen s) {
        // desenha Get Ready
        s.drawImage(290, 441, 176, 46, 0, (Game.WIDTH - 200) / 2, 60);
        
        // desenha bird       
        if (contador >= 0 && contador <= 50) {
            s.drawImage(528, 128, 34, 24, 0, 35, (Game.WIDTH - 112) / 2 + 24 / 2);
        } else if (contador <= 100) {
            s.drawImage(528, 180, 34, 24, 0, 35, (Game.WIDTH - 112) / 2 + 24 / 2);
        } else if (contador <= 150) {
            s.drawImage(446, 248, 34, 24, 0, 35, (Game.WIDTH - 112) / 2 + 24 / 2);
        }
    }

    @Override
    public void drawRecords(Screen s) {
        // desenha background
        s.drawImage(0, 0, 288, 512, 0, -background_offset, 0);
        s.drawImage(0, 0, 288, 512, 0, 288 - background_offset, 0);
        s.drawImage(0, 0, 288, 512, 0, 288 * 2 - background_offset, 0);

        // desenha ground
        s.drawImage(292, 0, 308, 112, 0, -ground_offset, Game.HEIGHT - 112);
        s.drawImage(292, 0, 308, 112, 0, 308 - ground_offset, Game.HEIGHT - 112);
        s.drawImage(292, 0, 308, 112, 0, 308 * 2 - ground_offset, Game.HEIGHT - 112);

        // desenha quadros
        s.drawImage("quadroScore.png", 0, 0, 242, 123, 0, 71, 10);
        s.drawImage("quadroScore.png", 0, 0, 242, 123, 0, 71, 10);
        s.drawImage("quadroScore.png", 0, 0, 242, 123, 0, 71, 133);
        s.drawImage("quadroScore.png", 0, 0, 242, 123, 0, 71, 256);

        //desenha medalhas
        s.drawImage(470, (Game.HEIGHT - 54), 58, 54, 0, 86, 58);
        s.drawImage(528, (Game.HEIGHT - 54), 52, 54, 0, 96, 181);
        s.drawImage(604, 272, 58, 50, 0, 102, 300);

        // desenha botao Start
        buttonMenu.draw(s);
        
        //desenha recordes
        drawRecordes(s);
        drawDates(s);
    }
    
    public void drawRecordes(Screen s){
        String str;
        int x=250, y=88;
        
        for(int i=0; i<3; i++){
            str = Integer.toString(vet[i]);
            for(int j=0; j<str.length(); j++){
                switch(str.charAt(j)){
                    case '0': score.drawNumber(s, 0, x, y);
                              break;
                    case '1': score.drawNumber(s, 1, x, y);
                              break;
                    case '2': score.drawNumber(s, 2, x, y);
                              break;
                    case '3': score.drawNumber(s, 3, x, y);
                              break;
                    case '4': score.drawNumber(s, 4, x, y);
                              break;
                    case '5': score.drawNumber(s, 5, x, y);
                              break;
                    case '6': score.drawNumber(s, 6, x, y);
                              break;
                    case '7': score.drawNumber(s, 7, x, y);
                              break;
                    case '8': score.drawNumber(s, 8, x, y);
                              break;
                    case '9': score.drawNumber(s, 9, x, y);
                              break;
                    default: break;
                }
                x+=16;
            }
            y+=124;
            x=250;
        }
    }
    
    public void drawDates(Screen s){
        String str;
        int x=140, y=46;
        
        for(int i=0; i<3; i++){
            str = dat[i];
            for(int j=0; j<str.length(); j++){
                switch(str.charAt(j)){
                    case '0': score.drawNumber(s, 0, x, y);
                              break;
                    case '1': score.drawNumber(s, 1, x, y);
                              break;
                    case '2': score.drawNumber(s, 2, x, y);
                              break;
                    case '3': score.drawNumber(s, 3, x, y);
                              break;
                    case '4': score.drawNumber(s, 4, x, y);
                              break;
                    case '5': score.drawNumber(s, 5, x, y);
                              break;
                    case '6': score.drawNumber(s, 6, x, y);
                              break;
                    case '7': score.drawNumber(s, 7, x, y);
                              break;
                    case '8': score.drawNumber(s, 8, x, y);
                              break;
                    case '9': score.drawNumber(s, 9, x, y);
                              break;
                    case '/': s.drawImage(580, (Game.HEIGHT - 54), 10, 20, 0, x, y);
                              break;
                    default: break;
                }
                x+=16;
            }
            y+=124;
            x=140;
        }
    }

    public void drawGame(Screen s) {
        // desenha pipes
        for (Pipe pipe : pipes) {
            pipe.draw(s);
        }

        // desenha bird
        bird.draw(s);

        // desenha score
        score.draw(s);
    }

    @Override
    public void draw(Screen s) {
        String str = "";
        int month;
        
        // desenha background
        s.drawImage(0, 0, 288, 512, 0, -background_offset, 0);
        s.drawImage(0, 0, 288, 512, 0, 288 - background_offset, 0);
        s.drawImage(0, 0, 288, 512, 0, 288 * 2 - background_offset, 0);

        if (this.stateGame == 2 && !this.isGameOver) {
            this.drawGame(s);
        }

        // desenha ground
        s.drawImage(292, 0, 308, 112, 0, -ground_offset, Game.HEIGHT - 112);
        s.drawImage(292, 0, 308, 112, 0, 308 - ground_offset, Game.HEIGHT - 112);
        s.drawImage(292, 0, 308, 112, 0, 308 * 2 - ground_offset, Game.HEIGHT - 112);

        if (this.stateGame == 0) {
            this.drawMenu(s);
        }

        if (this.stateGame == 3) {
            this.drawRecords(s);
        }
        
        if (this.stateGame == 1) {
            this.drawReady(s);
        }

        // checa fim de jogo
        if (this.stateGame == 2 && this.isGameOver) {
            if (score.getScore() > vet[0]) {
                vet[0] = score.getScore();
                Calendar today = Calendar.getInstance();
                str += today.get(Calendar.DAY_OF_MONTH);
                str += "/";
                month = today.get(Calendar.MONTH);
                month++;
                str += Integer.toString(month);
                str += "/";
                str += today.get(Calendar.YEAR);
                dat[0] = str;
                score.setPos(1);
                score.setRecord(true);
            }
            else{
                if(!score.isRecord() && score.getScore() > vet[1]){
                    vet[1] = score.getScore();
                    Calendar today = Calendar.getInstance();
                    str += today.get(Calendar.DAY_OF_MONTH);
                    str += "/";
                    month = today.get(Calendar.MONTH);
                    month++;
                    str += Integer.toString(month);
                    str += "/";
                    str += today.get(Calendar.YEAR);
                    dat[1] = str;
                    score.setPos(2);
                    score.setRecord(true);
                }
                else{
                    if(!score.isRecord() && score.getScore() > vet[2]){
                        vet[2] = score.getScore();
                        Calendar today = Calendar.getInstance();
                        str += today.get(Calendar.DAY_OF_MONTH);
                        str += "/";
                        month = today.get(Calendar.MONTH);
                        month++;
                        str += Integer.toString(month);
                        str += "/";
                        str += today.get(Calendar.YEAR);
                        dat[2] = str;
                        score.setPos(3);
                    }
                }
            }
            
            s.drawImage(292, 398, 188, 38, 0, Game.WIDTH / 2 - 188 / 2, 100);
            s.drawImage(292, 116, 226, 116, 0, Game.WIDTH / 2 - 226 / 2, Game.HEIGHT / 2 - 116 / 2);
            score.drawScore(s, Game.WIDTH / 2 + 55, Game.HEIGHT / 2 - 25);
            score.drawRecord(s, Game.WIDTH / 2 + 55, Game.HEIGHT / 2 + 16, vet[0]);
            buttonMenu.draw(s);
            
            switch(score.getPos()){
                case 1: s.drawImage(470, (Game.HEIGHT-54), 58, 54, 0, 90, 242); break;
                case 2: s.drawImage(528, (Game.HEIGHT-54), 52, 54, 0, 100, 242); break;
                case 3: s.drawImage(604, 272, 58, 50, 0, 104, 238); break;
                default: break;
            }
            
            File.setRecordes(vet);
            File.setDates(dat);
            File.write("records.txt");
            File.writeDate("dates.txt");
        }

    }

    public void gameOver() {
        this.isGameOver = true;
    }

    @Override
    public boolean isGameOver() {
        return this.isGameOver;
    }

    private static void start() {
        File.read("records.txt");
        File.readDate("dates.txt");
        
        vet = File.getRecordes();
        dat = File.getDates();
        
        animation.turnOn();
    }

    public static void main(String[] args) {
        start();
    }

}
