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

import static utils.Constants.PlayerConstants.*;
import static utils.Constants.Directions.*;


public class GamePanel extends JPanel{
    MouseInputs mouseInputs = new MouseInputs(this);
    private float xDelta = 100, yDelta = 100;
    private BufferedImage img;
    private BufferedImage[][] animations;
    private int animationTick, animationIndex, animationSpeed = 15;
    private int playerAction = IDLE;
    private int playerDir = -1;
    private boolean moving = false;

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
        animations = new BufferedImage[9][6];

        for(int j = 0; j < animations.length; j++){
            for(int index = 0; index < animations[j].length; index++){
                animations[j][index] = img.getSubimage(index*64, j*40, 64, 40);
            }
        }
    }

    private void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setPreferredSize(size);
    }

    public void setDirection(int direction){
        this.playerDir = direction;
        moving = true;
    }

    public void setMoving(boolean moving){
        this.moving = moving;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        updateAnimationTick();
        setAnimation();
        updatePos();
        g.drawImage(animations[playerAction][animationIndex], (int) xDelta, (int) yDelta, 256, 160, null);
    }

    public void updateAnimationTick(){
        animationTick++;
        if(animationTick >= animationSpeed){
            animationTick = 0;
            animationIndex++;
            if(animationIndex >= GetSpriteAmount(playerAction)){
                animationIndex = 0;
            }
        }
    }

    public void setAnimation(){
        if(moving){
            playerAction = RUNNING;
        }else{
            playerAction = IDLE;
        }
    }

    public void updatePos(){
        if(moving){
            switch(playerDir){
                case LEFT:
                    xDelta -= 5; 
                    break;
                case UP:
                    yDelta -= 5;
                    break;
                case RIGHT:
                    xDelta += 5;
                    break;
                case DOWN:
                    yDelta += 5;
                    break;
            }
        }
    }

}
