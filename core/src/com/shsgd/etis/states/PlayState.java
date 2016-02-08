package com.shsgd.etis.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.shsgd.etis.Main;
import com.shsgd.etis.sprites.EvilTriangle;
import com.shsgd.etis.sprites.Player;

/**
 * Created by ryananderson on 1/30/16.
 */
public class PlayState extends State implements InputProcessor{

    private static float GRACE_PERIOD = 1.0f; //3 seconds before baddies appear
    private float graceTimer=0;
    private static int DISTANCE_BETWEEN_TRIANGLES=350;

    private Player player;
    private ShapeRenderer sr;
    private Array<EvilTriangle> evilTriangles;
    private Texture bg;
    private Vector2[] bgPositions;
    private int score = 0;
    private String scoreStr = "0";
    private BitmapFont scoreFont;


    public PlayState(GameStateManager gsm) {
        super(gsm);
        Gdx.input.setInputProcessor(this);
        cam.setToOrtho(false, Main.WIDTH, Main.HEIGHT);
        cam.update();
        player = new Player(Main.WIDTH/2, Main.HEIGHT/2);
        evilTriangles = new Array<EvilTriangle>();
        sr = new ShapeRenderer();
        bg = new Texture("spacebg.png");
        bgPositions = new Vector2[4];
        bgPositions[0] = new Vector2(cam.position.x-cam.viewportWidth/2, cam.position.y-cam.viewportHeight/2);
        bgPositions[1] = new Vector2(cam.position.x-cam.viewportWidth/2, cam.position.y+cam.viewportHeight/2);//top
        bgPositions[2] = new Vector2(cam.position.x+cam.viewportWidth/2, cam.position.y+cam.viewportHeight/2); //top right
        bgPositions[3] = new Vector2(cam.position.x+cam.viewportWidth/2, cam.position.y-cam.viewportHeight/2); //right

        scoreFont = new BitmapFont();

    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        handleInput();
        player.update(dt);
        for (EvilTriangle e : evilTriangles){
            if(e.update(dt)) gameOver();
        }
        cam.position.x = player.getPosition().x+100;
        cam.position.y = player.getPosition().y;
        cam.update();

        //Backgrounds movement, set on other side if too far in one direction
        for (Vector2 v: bgPositions){
            if(v.x>cam.position.x+cam.viewportWidth/2){
                v.x = cam.position.x - cam.viewportWidth/2 - cam.viewportWidth;
            } else if (v.x<cam.position.x-cam.viewportWidth/2 - cam.viewportWidth){
                v.x = cam.position.x + cam.viewportWidth/2;
            }
            if(v.y > cam.position.y+cam.viewportHeight/2){
                v.y = cam.position.y - cam.viewportHeight/2 - cam.viewportHeight;
            } else if (v.y < cam.position.y - cam.viewportHeight/2 - cam.viewportHeight){
                v.y = cam.position.y + cam.viewportHeight/2;
            }
        }

        //Update the grace period timer
        if(graceTimer < GRACE_PERIOD) graceTimer += dt;
        else {
            //Shall I spawn some evil triangles?
            if(player.getHorizontalDistanceTraveledSinceLastTriangle()>=DISTANCE_BETWEEN_TRIANGLES){
                //spawn new triangle
                if (Math.random()>=0.5) evilTriangles.add(new EvilTriangle((float)cam.position.x + cam.viewportWidth/2 + EvilTriangle.getWIDTH()/2, (float)player.getPosition().y, true, player, this));
                else evilTriangles.add(new EvilTriangle((float) cam.position.x + cam.viewportWidth / 2 + EvilTriangle.getWIDTH()/2, (float) player.getPosition().y, false, player, this));

                player.resetHorizontalDistanceTraveledSinceLastTriangle();
            }


        }

        player.increaseSpeed(0.001f);
        //System.out.println(player.getVelocity().x);

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        //Sprites are drawn here
        //...
        for (Vector2 bgPos : bgPositions){
            sb.draw(bg, bgPos.x, bgPos.y, cam.viewportWidth, cam.viewportHeight);
        }


        sb.end();

        sr.setProjectionMatrix(cam.combined);
        sr.begin(ShapeRenderer.ShapeType.Line);
        // lines are drawn here
        //...
        sr.setColor(1,1,1,1);
        float slopePlayer = player.getVelocity().y/player.getVelocity().x;
        float yPt1 = cam.position.y-cam.viewportHeight/2;
        float xPt1 = -((player.getPosition().y-yPt1)/slopePlayer - player.getPosition().x);
        float yPt2 = cam.position.y+cam.viewportHeight/2;
        float xPt2 = -((player.getPosition().y-yPt2)/slopePlayer - player.getPosition().x);

        sr.line(xPt1, yPt1, xPt2, yPt2);

        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Filled);
        //Draw the player
        sr.setColor(0, 0.9f, 0.5f, 1);
        sr.circle(player.getPosition().x, player.getPosition().y, player.getRADIUS());

        //Draw all evil triangles
        sr.setColor(0.9f, 0, 0, 1);
        for (EvilTriangle tri: evilTriangles){
            float pt1x = tri.getPosition().x-tri.getWIDTH()/2;
            float pt1y = tri.getPosition().y - tri.getHEIGHT()/2;
            float pt2x = tri.getPosition().x + tri.getWIDTH()/2;
            float pt2y = tri.getPosition().y - tri.getHEIGHT()/2;
            float pt3x = tri.getPosition().x;
            float pt3y = tri.getPosition().y + tri.getHEIGHT()/2;
            if(!tri.isFacingUp()) {
                pt3y -= tri.getHEIGHT();
                pt1y += tri.getHEIGHT();
                pt2y += tri.getHEIGHT();
            }

            sr.triangle(pt1x, pt1y, pt2x, pt2y, pt3x, pt3y);
        }

        sr.end();

        sb.begin();
        scoreFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        scoreFont.draw(sb, scoreStr, player.getPosition().x, player.getPosition().y);
        sb.end();


    }

    private void gameOver(){
        gsm.set(new PlayState(gsm));
    }

    public void increaseScore(int amount){
        score+=1;
        scoreStr = ""+score;
        System.out.println("Score "+score);
    }

    @Override
    public void dispose() {
        player.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode==Input.Keys.A) System.out.println(player.getVelocity().x);
        player.touchDown();
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        player.touchUp();
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        player.touchDown();
        return false;

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        player.touchUp();
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
