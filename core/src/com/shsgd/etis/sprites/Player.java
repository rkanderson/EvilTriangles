package com.shsgd.etis.sprites;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by ryananderson on 1/31/16.
 */
public class Player {

    //The player is circle shaped.

    private float X_SPEED_ORG = 100, X_SPEED = 100;
    private float Y_SPEED_ORG = 80, Y_SPEED = 80; //Can be set to + or -
    private float speedFactor = 1;
    private int RADIUS = 30;
    private static final float MAX_X_SPEED = 400.0f;

    private Vector2 position;
    private Vector2 velocity;
    private Circle bounds;

    private int horizontalDistanceTraveledSinceLastTriangle;

    public Player(int x, int y){
        position = new Vector2(x, y);
        velocity = new Vector2(X_SPEED, -Y_SPEED);
        bounds = new Circle(position.x, position.y, RADIUS);
        horizontalDistanceTraveledSinceLastTriangle=0;
    }

    public void update(float dt){
        velocity.scl(dt);
        float prevX = position.x;
        position.add(velocity.x, velocity.y);
        velocity.scl(1 / dt);
        bounds.setPosition(position.x - RADIUS / 2, position.y - RADIUS / 2);
        horizontalDistanceTraveledSinceLastTriangle += position.x - prevX;
    }


    public void dispose(){

    }



    public void touchDown(){
        velocity.set(X_SPEED, +Y_SPEED);
    }

    public void touchUp(){
        velocity.set(X_SPEED, -Y_SPEED);
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getRADIUS() {
        return RADIUS;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Circle getBounds() {
        return bounds;
    }

    public int getHorizontalDistanceTraveledSinceLastTriangle() {
        return horizontalDistanceTraveledSinceLastTriangle;
    }

    public void resetHorizontalDistanceTraveledSinceLastTriangle(){
        horizontalDistanceTraveledSinceLastTriangle=0;
    }

    public void increaseSpeed(float factor){
        if (velocity.x >= MAX_X_SPEED) return;
        speedFactor+=factor;
        float newXSpeed, newYSpeed;
        newXSpeed = X_SPEED_ORG*speedFactor;
        newYSpeed = Y_SPEED_ORG*speedFactor;
        if(velocity.y<0) newYSpeed*=-1;
        velocity.set(newXSpeed, newYSpeed);
        X_SPEED = newXSpeed;
        Y_SPEED = Y_SPEED_ORG*speedFactor;
    }
}
