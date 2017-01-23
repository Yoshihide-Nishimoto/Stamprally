package yoshihide.nishimoto.stamprally;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite {

    // 横幅、高さ
    public static final float PLAYER_WIDTH = 1.0f;
    public static final float PLAYER_HEIGHT = 1.0f;

    public Player(Texture texture, int srcX, int srcY, int srcWidth, int srcHeight) {
        super(texture, srcX, srcY, srcWidth, srcHeight);
        setSize(PLAYER_WIDTH, PLAYER_HEIGHT);
    }
}