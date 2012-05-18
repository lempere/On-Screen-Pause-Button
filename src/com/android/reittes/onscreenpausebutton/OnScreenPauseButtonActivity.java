package com.android.reittes.onscreenpausebutton;


import org.andengine.entity.util.FPSLogger;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FixedResolutionPolicy;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.modifier.ease.EaseSineInOut;
import org.andengine.entity.modifier.PathModifier.Path;

import android.view.MotionEvent;


/**
 * @author Nicolas Gramlich
 * @since 01:30:15 - 02.04.2010
 */

/**
 * On Screen Pause Button
 * @author Reittes
 */

public class OnScreenPauseButtonActivity extends SimpleBaseGameActivity implements IOnMenuItemClickListener{

	private Scene scene;
	private Camera camera;

	private float CAMERA_HEIGHT= 480;
	private float CAMERA_WIDTH= 800;
	
	private BitmapTextureAtlas btnTexture;
	ITextureRegion btn_pauseRegion;
	private ITextureRegion btn_playRegion;
	private Sprite pauseBtn;

	// Menu Scene with Play Button 
	
	private MenuScene pauseScene(){
		final MenuScene pauseGame= new MenuScene(camera);
		
		final SpriteMenuItem btnPlay = new SpriteMenuItem(1, this.btn_playRegion, getVertexBufferObjectManager());
		btnPlay.setPosition(CAMERA_WIDTH- this.btn_pauseRegion.getWidth()-100, 50);
		btnPlay.setScale(2);
		pauseGame.addMenuItem(btnPlay);
		
		pauseGame.setBackgroundEnabled(false);
		pauseGame.setOnMenuItemClickListener(this);
		return pauseGame;
	}


	@Override
	public boolean onMenuItemClicked(MenuScene arg0, IMenuItem arg1,
			float arg2, float arg3) {
		switch(arg1.getID()){
		case 1:
			if(scene.hasChildScene()){
				scene.clearChildScene();
				pauseBtn.setVisible(true);
			}
			return true;
		default:
			return false;
		}
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final EngineOptions eOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_SENSOR,
				new FixedResolutionPolicy((int)CAMERA_WIDTH, (int)CAMERA_HEIGHT), camera);
		return eOptions;
	}

	@Override
	protected void onCreateResources() {
		btnTexture= new BitmapTextureAtlas(this.getTextureManager(), 64,32, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		btn_pauseRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(btnTexture, this, "gfx/pause.png", 0 ,0);
		btn_playRegion= BitmapTextureAtlasTextureRegionFactory.createFromAsset(btnTexture, this, "gfx/play.png", 32 ,0);
		
		getEngine().getTextureManager().loadTexture(btnTexture);
		
	}

	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler((IUpdateHandler) new FPSLogger());
		
		scene= new Scene();
				
		final Shape rectSprite= new Rectangle(200, 230, 50, 50, getVertexBufferObjectManager());
		rectSprite.setColor(1, 1, 1);
		float[][] vx = {{200,230},{400,230},
						{200,230},{400,230},
						{400,230},{200,230}};

		
		rectSprite.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(
				new PathModifier(0.8f, new Path(vx[0], vx[1]),  EaseSineInOut.getInstance()),
				new PathModifier(0.9f, new Path(vx[2],vx[3]), EaseSineInOut.getInstance()),
				new PathModifier(0.9f, new Path(vx[4],vx[5]), EaseSineInOut.getInstance())
				)));
		scene.attachChild(rectSprite);
		
		pauseBtn= new Sprite(CAMERA_WIDTH- this.btn_pauseRegion.getWidth()-100, 50, this.btn_pauseRegion, getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
	   			 if(pSceneTouchEvent.getAction()==MotionEvent.ACTION_UP){
	   				 this.setVisible(false);
	   				 scene.setChildScene(pauseScene(), false, true, true);
	   			 }
	   			 return true;
       	}};
       	pauseBtn.setScale(2);
       	scene.attachChild(pauseBtn);
       	scene.registerTouchArea(pauseBtn);
		
		return scene;
	}

	
}