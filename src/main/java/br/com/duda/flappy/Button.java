package br.com.duda.flappy;

public class Button extends Element{
    
    private int button;

    public Button(double x, double y, int button){
        this.x = x;
        this.y = y;
        this.width = 80;
        this.height = 28;
        this.button = button;
    }
    
    @Override
    public void draw(Screen s){
        switch (this.button) {
            case 1: s.drawImage(483, 425, this.width+1, this.height-1, 0, x, y);break;
            case 2: s.drawImage(488, 346, this.width, this.height-2, 0, x, y);break;
            case 3: s.drawImage(492, 236, this.width, this.height-2, 0, x, y);break;
        }
    }
    
    public boolean pointOnButton(double x, double y){
        return (x) > this.x && x < (this.x + this.width) &&
               (y) > this.y && y < (this.y + this.height);
    }
        
}
