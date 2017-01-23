package yoshihide.nishimoto.stamprally;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen extends ScreenAdapter {

    static final float CAMERA_WIDTH = 10;
    static final float CAMERA_HEIGHT = 15;

    private double latitude,longitude;
    private String message,provider;
    private boolean networkenabled;

    private Stamprally mGame;

    Sprite mBg;
    OrthographicCamera mCamera;
    OrthographicCamera mGuiCamera;
    FitViewport mViewPort;
    FitViewport mGuiViewPort;
    Vector3 mTouchPoint;
    BitmapFont mFont;

    Player mPlayer;

    public GameScreen(Stamprally game) {

        mGame = game;

        // 背景の準備
        Texture bgTexture = new Texture("bjy.png");
        // TextureRegionで切り出す時の原点は左上
        mBg = new Sprite(new TextureRegion(bgTexture, 0, 0, 300, 450));
        mBg.setSize(CAMERA_WIDTH, CAMERA_HEIGHT);
        mBg.setPosition(0, 0);

        // カメラ、ViewPortを生成、設定する
        mCamera = new OrthographicCamera();
        mCamera.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT);
        mViewPort = new FitViewport(CAMERA_WIDTH - 2, CAMERA_HEIGHT - 3, mCamera);

        // GUI用のカメラを設定する
        mGuiCamera = new OrthographicCamera();
        mGuiCamera.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT);
        mGuiViewPort = new FitViewport(CAMERA_WIDTH, CAMERA_HEIGHT, mGuiCamera);

        //  mViewPort.setScreenBounds(0,0,8,12);


        Texture playerTexture = new Texture("player.png");
        mPlayer = new Player(playerTexture, 0, 0, 300, 300);
        //mPlayer.setPosition(CAMERA_WIDTH / 2 - mPlayer.getWidth() , CAMERA_HEIGHT / 2 - mPlayer.getHeight());
        mPlayer.setPosition(CAMERA_WIDTH / 2 - mPlayer.getWidth() / 2 , CAMERA_HEIGHT / 2 - mPlayer.getHeight() / 2);

        mFont = new BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"), false);
        mFont.getData().setScale(0.05f);

        mTouchPoint = new Vector3();

    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // カメラの座標をアップデート（計算）し、スプライトの表示に反映させる
        mCamera.update();
        mGame.batch.setProjectionMatrix(mCamera.combined);

        mGame.batch.begin();

        float cor_latitude = (float) 0.0;
        float cor_longitude = (float) 0.0;


        if (latitude != 0 && longitude != 0) {
            cor_latitude = (float) (latitude - 135.142187);
            cor_longitude = (float) (longitude - 35.252235);
        }


        // 原点は左下
        mBg.setPosition( mCamera.position.x - CAMERA_WIDTH / 2 - cor_latitude * CAMERA_WIDTH / (float) 0.000220 * 4, mCamera.position.y - CAMERA_HEIGHT / 2 - cor_longitude * CAMERA_HEIGHT / (float) 0.000176 * 4 );
        //mBg.setPosition(mCamera.position.x - CAMERA_WIDTH / 2 , mCamera.position.y - CAMERA_HEIGHT / 2 );
        mBg.draw(mGame.batch);
        mPlayer.draw(mGame.batch);

        mGame.batch.end();

        mGuiCamera.update();
        mGame.batch.setProjectionMatrix(mGuiCamera.combined);
        mGame.batch.begin();

        if (Gdx.input.isTouched()) {
            mGuiViewPort.unproject(mTouchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
        }

        mFont.draw(mGame.batch, "X: " + mTouchPoint.x,1, CAMERA_HEIGHT - 1);
        mFont.draw(mGame.batch, "Y: " + mTouchPoint.y,1, CAMERA_HEIGHT - 2);
        mFont.draw(mGame.batch, "経度: " + this.latitude,1, CAMERA_HEIGHT - 3);
        mFont.draw(mGame.batch, "緯度: " + this.longitude,1, CAMERA_HEIGHT - 4);
        mFont.draw(mGame.batch, "メッセージ: " + this.message,1, CAMERA_HEIGHT - 5);
        mFont.draw(mGame.batch, "プロバイダー: " + "1.1",1, CAMERA_HEIGHT - 6);
        mFont.draw(mGame.batch, "ネットワーク: " + this.networkenabled,1, CAMERA_HEIGHT - 7);
        mFont.draw(mGame.batch, "補正: " + (mCamera.position.x - CAMERA_WIDTH / 2 - cor_latitude * CAMERA_WIDTH / (float) 0.000220 * 4),1, CAMERA_HEIGHT - 8);
        mFont.draw(mGame.batch, "カメラ: " + mCamera.position.x,1, CAMERA_HEIGHT - 9);
        mGame.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        mViewPort.update(width, height);
        mGuiViewPort.update(width, height);
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setNetworkenabled(boolean networkenabled) {
        this.networkenabled = networkenabled;
    }

}
