package yoshihide.nishimoto.stamprally;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen extends ScreenAdapter implements GestureDetector.GestureListener {

    static final float CAMERA_WIDTH = 10;
    static final float CAMERA_HEIGHT = 15;

    private float mBg_width,mBg_height;

    private double latitude,longitude;
    private String message,provider;
    private boolean networkenabled;
    private float testZoom,testIni;

    private Stamprally mGame;

    Sprite mBg;
    OrthographicCamera mCamera;
    OrthographicCamera mGuiCamera;
    FitViewport mViewPort;
    FitViewport mGuiViewPort;
    Vector3 mTouchPoint;
    BitmapFont mFont;

    Player mPlayer;

    GestureDetector gestureDetector;

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
        mPlayer.setPosition(CAMERA_WIDTH / 2 - mPlayer.getWidth() / 2 , CAMERA_HEIGHT / 2 - mPlayer.getHeight() / 2);

        mFont = new BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"), false);
        mFont.getData().setScale(0.05f);

        mTouchPoint = new Vector3();

        gestureDetector = new GestureDetector(this);
        Gdx.input.setInputProcessor(gestureDetector);

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

        mFont.draw(mGame.batch, "x" + mTouchPoint.x,1, CAMERA_HEIGHT - 1);
        mFont.draw(mGame.batch, "y" + mTouchPoint.y,1, CAMERA_HEIGHT - 2);
        mFont.draw(mGame.batch, "latitude" + this.latitude,1, CAMERA_HEIGHT - 3);
        mFont.draw(mGame.batch, "longitude" + this.longitude,1, CAMERA_HEIGHT - 4);
        mFont.draw(mGame.batch, "message" + this.message,1, CAMERA_HEIGHT - 5);
        mFont.draw(mGame.batch, "provider" + "1.1",1, CAMERA_HEIGHT - 6);
        mFont.draw(mGame.batch, "network" + this.networkenabled,1, CAMERA_HEIGHT - 7);
        mFont.draw(mGame.batch, "correction" + (mCamera.position.x - CAMERA_WIDTH / 2 - cor_latitude * CAMERA_WIDTH / (float) 0.000220 * 4),1, CAMERA_HEIGHT - 8);
        mFont.draw(mGame.batch, "camera" + mCamera.position.x,1, CAMERA_HEIGHT - 9);
        mFont.draw(mGame.batch, "dis" + testZoom,1, CAMERA_HEIGHT - 10);
        mFont.draw(mGame.batch, "ini" + testIni,1, CAMERA_HEIGHT - 11);
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

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        mBg_width = CAMERA_WIDTH * (float)0.6;
        mBg_height = CAMERA_HEIGHT * (float)0.6;
        mCamera.setToOrtho(false, mBg_width, mBg_height);
        mPlayer.setPosition(mBg_width / 2 - mPlayer.getWidth() / 2 , mBg_height / 2 - mPlayer.getHeight() / 2);
        testZoom = 1;
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
//        mBg.setSize(distance,distance);
        testZoom = distance;
        testIni = initialDistance;
        mBg_width = mBg_width * distance / initialDistance;
        mBg_height = mBg_height * distance / initialDistance;


        if (mBg_width > CAMERA_WIDTH) {

            mBg_width = CAMERA_WIDTH;
            mBg_height = CAMERA_HEIGHT;

        } else if (mBg_width < CAMERA_WIDTH * 0.8) {

            mBg_width = CAMERA_WIDTH * (float)0.8;
            mBg_height = CAMERA_HEIGHT * (float)0.8;

        }

        mCamera.setToOrtho(false, mBg_width, mBg_height);
        mPlayer.setPosition(CAMERA_WIDTH / 2 - mPlayer.getWidth() / 2 , CAMERA_HEIGHT / 2 - mPlayer.getHeight() / 2);

        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
