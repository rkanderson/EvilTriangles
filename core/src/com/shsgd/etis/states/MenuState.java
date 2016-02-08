package com.shsgd.etis.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.shsgd.etis.Main;

/**
 * Created by ryananderson on 1/30/16.
 */
public class MenuState extends State {

    private Texture startBtn;

    public MenuState(GameStateManager gsm){
        super(gsm);
        startBtn = new Texture("playbtn.png");
        cam.setToOrtho(false, Main.WIDTH, Main.HEIGHT);
        cam.update();

    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()){
            gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(startBtn, cam.position.x - startBtn.getWidth()/2, cam.position.y - startBtn.getHeight()/2);
        sb.end();
    }

    @Override
    public void dispose() {
        startBtn.dispose();
    }
}
