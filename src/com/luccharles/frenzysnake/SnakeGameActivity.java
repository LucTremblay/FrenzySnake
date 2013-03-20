package com.luccharles.frenzysnake;

import java.io.IOException;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;

import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

import android.graphics.Color;
import android.opengl.GLES20;
import android.view.MotionEvent;

public class SnakeGameActivity extends SimpleBaseGameActivity implements SnakeConstants {
	
	private static final int CAMERA_WIDTH = CELLS_HORIZONTAL * CELL_WIDTH; // 640
	private static final int CAMERA_HEIGHT = CELLS_VERTICAL * CELL_HEIGHT; // 480

	private static final int LAYER_COUNT = 4;

	private static final int LAYER_BACKGROUND = 0;
	private static final int LAYER_FOOD = LAYER_BACKGROUND + 1;
	private static final int LAYER_SNAKE = LAYER_FOOD + 1;
	private static final int LAYER_SCORE = LAYER_SNAKE + 1;

	

	private Camera mCamera;

	float initialX = 0.0f;
	float initialY = 0.0f;

	private Font mFont;

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mTailPartTextureRegion;
	private TiledTextureRegion mHeadTextureRegion;
	private ITextureRegion mAppleTextureRegion;

	private BitmapTextureAtlas mBackgroundTexture;
	private ITextureRegion mBackgroundTextureRegion;

	private BitmapTextureAtlas mOnScreenControlTexture;


	private Scene mScene;
	

	private Snake mSnake;
	private Apple mApple;

	private int mScore = 0;
	private Text mScoreText;

	private Sound mGameOverSound;
	private Sound mMunchSound;
	protected boolean mGameRunning;
	private Text mGameOverText;


	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
		engineOptions.getAudioOptions().setNeedsSound(true);
		return engineOptions;
	}

	@Override
	public void onCreateResources() {

		FontFactory.setAssetBasePath("font/");
		this.mFont = FontFactory.createFromAsset(this.getFontManager(), this.getTextureManager(), 512, 512, TextureOptions.BILINEAR, this.getAssets(), "Plok.ttf", 32, true, Color.WHITE);
		this.mFont.load();

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 128, 128);
		this.mHeadTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "snake_head.png", 0, 0, 1, 1);
		this.mTailPartTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "snake_body_t.png", 96, 0);
		this.mAppleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "apple.png", 64, 0);
		this.mBitmapTextureAtlas.load();

		this.mBackgroundTexture = new BitmapTextureAtlas(this.getTextureManager(), 1024, 512);
		this.mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "snake_background.png", 0, 0);
		this.mBackgroundTexture.load();

		this.mOnScreenControlTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
	
		this.mOnScreenControlTexture.load();


		try {
			SoundFactory.setAssetBasePath("mfx/");
			this.mGameOverSound = SoundFactory.createSoundFromAsset(this.getSoundManager(), this, "game_over.ogg");
			this.mMunchSound = SoundFactory.createSoundFromAsset(this.getSoundManager(), this, "munch.ogg");
		} catch (final IOException e) {
			Debug.e(e);
		}
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mScene = new Scene();
		for(int i = 0; i < LAYER_COUNT; i++) {
			this.mScene.attachChild(new Entity());
		}

		
		this.mScene.setBackgroundEnabled(false);
		this.mScene.getChildByIndex(LAYER_BACKGROUND).attachChild(new Sprite(0, 0, this.mBackgroundTextureRegion, this.getVertexBufferObjectManager()));

		
		this.mScoreText = new Text(5, 5, this.mFont, "Score: 0", "Score: XXXX".length(), this.getVertexBufferObjectManager());
		this.mScoreText.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.mScoreText.setAlpha(0.5f);
		//this.mScene.getChildByIndex(LAYER_SCORE).attachChild(this.mScoreText);


		this.mSnake = new Snake(Direction.RIGHT, CELLS_HORIZONTAL/2, CELLS_VERTICAL / 2, this.mHeadTextureRegion, this.mTailPartTextureRegion, this.getVertexBufferObjectManager());
		
		
		this.mSnake.grow();
		this.mScene.getChildByIndex(LAYER_SNAKE).attachChild(this.mSnake);

		
		this.mApple = new Apple(0, 0, this.mAppleTextureRegion, this.getVertexBufferObjectManager());

		this.setAppleToRandomCell();
		this.mScene.getChildByIndex(LAYER_FOOD).attachChild(this.mApple);

		
		this.mScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
				float dernierX = 0.0f;
				float dernierY = 0.0f;
				float distanceX = 0.0f;
				float distanceY = 0.0f;
				
				
				if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN)
				{
					initialX = pSceneTouchEvent.getX();
					initialY = pSceneTouchEvent.getY();
				}
				if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP)
				{
					dernierX = pSceneTouchEvent.getX();
					dernierY = pSceneTouchEvent.getY();
					
					distanceX = dernierX - initialX;
					distanceY = dernierY - initialY;
					
					if (Math.abs(distanceX) > Math.abs(distanceY))
					{
						if(distanceX > 0)
						{
							SnakeGameActivity.this.mSnake.setDirection(Direction.RIGHT);
						}
						else
						{
							SnakeGameActivity.this.mSnake.setDirection(Direction.LEFT);
						}
					}
					else
					{
						if(distanceY > 0)
						{
							SnakeGameActivity.this.mSnake.setDirection(Direction.DOWN);
						}
						else
						{
							SnakeGameActivity.this.mSnake.setDirection(Direction.UP);
						}
					}
				}
				
				return true;
			}
		});
		
		
	
		this.mScene.registerUpdateHandler(new TimerHandler(0.5f, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				if(SnakeGameActivity.this.mGameRunning) {
					try {
						SnakeGameActivity.this.mSnake.move();
					} catch (final SnakeSuicideException e) {
						SnakeGameActivity.this.onGameOver();
					}

					SnakeGameActivity.this.handleNewSnakePosition();
				}
			}
		}));

		/*final Text titleText = new Text(0, 0, this.mFont, "Snake\non a Phone!", new TextOptions(HorizontalAlign.CENTER), this.getVertexBufferObjectManager());
		titleText.setPosition((CAMERA_WIDTH - titleText.getWidth()) * 0.5f, (CAMERA_HEIGHT - titleText.getHeight()) * 0.5f);
		titleText.setScale(0.0f);
		titleText.registerEntityModifier(new ScaleModifier(2, 0.0f, 1.0f));
		this.mScene.getChildByIndex(LAYER_SCORE).attachChild(titleText);*/

		this.mScene.registerUpdateHandler(new TimerHandler(3.0f, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				SnakeGameActivity.this.mScene.unregisterUpdateHandler(pTimerHandler);
				SnakeGameActivity.this.mGameRunning = true;
			}
		}));

	/*	this.mGameOverText = new Text(0, 0, this.mFont, "Game\nOver", new TextOptions(HorizontalAlign.CENTER), this.getVertexBufferObjectManager());
		this.mGameOverText.setPosition((CAMERA_WIDTH - this.mGameOverText.getWidth()) * 0.5f, (CAMERA_HEIGHT - this.mGameOverText.getHeight()) * 0.5f);
		this.mGameOverText.registerEntityModifier(new ScaleModifier(3, 0.1f, 2.0f));
		this.mGameOverText.registerEntityModifier(new RotationModifier(3, 0, 720));*/

		return this.mScene;
	}

	@Override
	public void onGameCreated() {

	}

	

	private void setAppleToRandomCell() {
		this.mApple.setCell(MathUtils.random(1, CELLS_HORIZONTAL - 2), MathUtils.random(1, CELLS_VERTICAL - 2));
	}

	private void handleNewSnakePosition() {
		final SnakeHead snakeHead = this.mSnake.getHead();

		if(snakeHead.getCellX() < 0 || snakeHead.getCellX() >= CELLS_HORIZONTAL || snakeHead.getCellY() < 0 || snakeHead.getCellY() >= CELLS_VERTICAL) {
			this.onGameOver();
		} else if(snakeHead.isInSameCell(this.mApple)) {
			this.mScore += 50;
			this.mScoreText.setText("Score: " + this.mScore);
			this.mSnake.grow();
			this.mMunchSound.play();
			this.setAppleToRandomCell();
		}
	}

	private void onGameOver() {
		//this.mGameOverSound.play();
		
		this.mBackgroundTexture = new BitmapTextureAtlas(this.getTextureManager(), 1024, 512);
		this.mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBackgroundTexture, this, "blue_screen.png", 0, 0);
		this.mBackgroundTexture.load();
		
		this.mScene.getChildByIndex(LAYER_BACKGROUND).attachChild(new Sprite(0, 0, this.mBackgroundTextureRegion, this.getVertexBufferObjectManager()));
		
		this.mGameRunning = false;
	}
}
