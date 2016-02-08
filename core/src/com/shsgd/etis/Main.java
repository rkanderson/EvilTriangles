package com.shsgd.etis;

		import com.badlogic.gdx.ApplicationAdapter;
		import com.badlogic.gdx.Gdx;
		import com.badlogic.gdx.graphics.GL20;
		import com.badlogic.gdx.graphics.g2d.SpriteBatch;
		import com.shsgd.etis.states.GameStateManager;
		import com.shsgd.etis.states.MenuState;

public class Main extends ApplicationAdapter {
	public static final int WIDTH = 800, HEIGHT = 480;
	public static final String title = "Evil Triangles in Space";

	SpriteBatch batch;
	GameStateManager gsm;

	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}
}
