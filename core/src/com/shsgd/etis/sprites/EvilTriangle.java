package com.shsgd.etis.sprites;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.shsgd.etis.states.PlayState;


/**
 * Created by ryananderson on 1/31/16.
 */
public class EvilTriangle {
    private static int HEIGHT = 80;
    private static int WIDTH = 80;
    private static int ZOOM_ATTACK_SPEED = 2000;

    private Vector2 position;
    private Vector2 velocity;
    private boolean facingUp;
    private boolean playerSpotted;
    private Player player;
    private Rectangle bounds;
    private PlayState playState;

    public EvilTriangle(float x, float y, boolean facingUp, Player player, PlayState playState){
        position = new Vector2(x, y);
        velocity = new Vector2(0f, 0f);
        this.facingUp = facingUp;
        this.player = player;
        bounds = new Rectangle((int)position.x-WIDTH/2, (int)position.y-HEIGHT/2, WIDTH, HEIGHT);
        this.playState = playState;
    }

    public boolean update(float dt){ //@ returns game over
        //If I'm touching the player, initiate the game over
        //if (player.getBounds().overlaps(bounds)){
        if (Intersector.overlaps(player.getBounds(), bounds)){
            return true;
        }

        //See if I can spot the player If I haven't done so already.
        else if(!playerSpotted && overlapsOnlyInTermsOfX(player.getBounds(), this.bounds)){

                playerSpotted = true;

                //increase score
                playState.increaseScore(1);


        }

        //If I have spotted the player, move hella fast in the direction I'm facing
        if(playerSpotted){
            if(facingUp) velocity.set(velocity.x, ZOOM_ATTACK_SPEED);
            else velocity.set(velocity.x, -ZOOM_ATTACK_SPEED);

            velocity.scl(dt);
            position.add(velocity.x, velocity.y);
            velocity.scl(1/dt);
            bounds.setPosition(position.x-WIDTH/2, position.y-HEIGHT/2);
        }

        return false;
    }

    private boolean overlapsOnlyInTermsOfX(Circle player, Rectangle myBounds){
        //if(bounds1.x>=bounds2.getX() && bounds1.getX() <= bounds2.getX()+bounds2.getWidth() ||
        //        bounds1.getX()+bounds1.getWidth() >= bounds2.getX() && bounds1.getX()+bounds1.getWidth() <= bounds2.getX()+bounds2.getWidth())
        //    return true;
        if (player.x >= myBounds.x && player.x <= myBounds.x + myBounds.width) return true;
        return false;
    }

    public Vector2 getPosition() {
        return position;
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public boolean isFacingUp() {
        return facingUp;
    }
}
