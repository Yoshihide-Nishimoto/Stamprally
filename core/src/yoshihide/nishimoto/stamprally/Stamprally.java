package yoshihide.nishimoto.stamprally;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Stamprally extends Game {

	public SpriteBatch batch;
	public GameScreen gamescreen;

	@Override
	public void create () {
		batch = new SpriteBatch();

		gamescreen = new GameScreen(this);
		// GameScreenを表示する
		setScreen(gamescreen);
	}

	public void setGPS (double latitude,double longitude) {

		if (gamescreen != null) {
			gamescreen.setLatitude(latitude);
			gamescreen.setLongitude(longitude);
		}

	}

	public void setMessage (String message) {
		if (gamescreen != null) {
			gamescreen.setMessage(message);
		}
	}

	public void setProvider (String provider) {
		if (gamescreen != null) {
			gamescreen.setProvider(provider);
		}
	}

	public void setNetworkenabled (boolean networkenabled) {
		if (gamescreen != null) {
			gamescreen.setNetworkenabled(networkenabled);
		}
	}

}
