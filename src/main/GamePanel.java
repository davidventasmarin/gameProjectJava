package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import inputs.KeyboardInputs;
import inputs.MouseInputs;


public class GamePanel extends JPanel{
    MouseInputs mouseInputs = new MouseInputs(this);
    private float xDelta = 100, yDelta = 100;
    private BufferedImage img;
    private BufferedImage[] idleAnimation;
    private int animationTick, animationIndex, animationSpeed = 30;

    public GamePanel(){
        addKeyListener(new KeyboardInputs(this));
        importImg();
        loadAnimation();
        setPanelSize();
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void importImg(){
        InputStream is = getClass().getResourceAsStream("player_sprites.png");

        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadAnimation(){
        // Para saber el tamaño del array, debo de saber con antelación
        // cuantas animaciones voy a tener. En este caso, mirando el png
        // vemos que la primera animación va a tener 5 idle (imágenes)
        idleAnimation = new BufferedImage[5];

        for(int index = 0; index < idleAnimation.length; index++){
            idleAnimation[index] = img.getSubimage(index*64, 0, 64, 40);
        }

    }

    private void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setPreferredSize(size);
    }

    public void changeXDelta(int value){
        this.xDelta += value;
    }

    public void changeYDelta(int value){
        this.yDelta += value;
    }

    public void setRectPos(int x, int y){
        this.xDelta = x;
        this.yDelta = y;
    }

    public void updateAnimationTick(){
        animationTick++;
        if(animationTick >= animationSpeed){
            animationTick = 0;
            animationIndex++;
            if(animationIndex >= idleAnimation.length){
                animationIndex = 0;
            }
        }
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        updateAnimationTick();
        g.drawImage(idleAnimation[animationIndex], (int) xDelta, (int) yDelta, 128, 80, null);
    }

}
