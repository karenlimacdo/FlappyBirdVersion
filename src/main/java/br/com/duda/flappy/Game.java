package br.com.duda.flappy;

public interface Game {

    public static int WIDTH = 384;
    public static int HEIGHT = 512;
    public static String TITLE = "Flappy Bird";
    
    boolean isGameOver();
    
    void initialize();

    void step(double dt);

    void onKeyPressed(String tecla);
    
    int onClicked(double x, double y);

    void draw(Screen tela);
    
    void drawMenu(Screen tela);
    
    void drawRecords(Screen tela);
}
