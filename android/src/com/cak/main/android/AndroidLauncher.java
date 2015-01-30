package com.cak.main.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.cak.main.GSPD;
import com.cak.main.IActivityRequestHandler;
import com.cak.main.Main;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;

public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler, GameHelperListener, GSPD {
	
	private GameHelper gameHelper;
	
	protected AdView adView, gameAdView;
	protected InterstitialAd bigAd;
	
	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;
	private final int SHOW_ADS_INGAME = 2;
	private final int HIDE_ADS_INGAME = 3;
	private final int SHOW_ADS_BIG = 4;

	protected Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case SHOW_ADS:
					{
						adView.setVisibility(View.VISIBLE);
						break;
					}
				case HIDE_ADS:
					{
						adView.setVisibility(View.GONE);
						break;
					}
				case SHOW_ADS_INGAME:
				{
					gameAdView.setVisibility(View.VISIBLE);
					break;
				}
				case HIDE_ADS_INGAME:
				{
					gameAdView.setVisibility(View.GONE);
					break;
				}
				case SHOW_ADS_BIG:
					{
						bigAd.show();
						break;
					}
			}
		}
	};
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Create the layout
        RelativeLayout layout = new RelativeLayout(this);

        // Do the stuff that initialize() would do for you
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        // Create the libgdx View
        View gameView = initializeForView(new Main("res", this, this));

        // Create and setup the AdMob view
//        adView = new AdView(this);
//        adView.setAdSize(AdSize.BANNER);
//        adView.setAdUnitId("ca-app-pub-9119128006124488/6621130252");
//        AdRequest adRequest = new AdRequest.Builder()
//				.build();
//        adView.loadAd(adRequest);
        
//        gameAdView = new AdView(this);
//        gameAdView.setAdSize(AdSize.BANNER);
//        gameAdView.setAdUnitId("ca-app-pub-9119128006124488/7421709059");
//        gameAdView.loadAd(adRequest);
        
//        bigAd = new InterstitialAd(this);
//        bigAd.setAdUnitId("");
//        bigAd.loadAd(adRequest);

        // Add the libgdx view
        layout.addView(gameView);

        // Add the AdMob view
//        RelativeLayout.LayoutParams adParams = 
//            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
//                    RelativeLayout.LayoutParams.WRAP_CONTENT);
//        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        adParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//
//        layout.addView(adView, adParams);
//        
//        RelativeLayout.LayoutParams adIngameParams = 
//                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
//                        RelativeLayout.LayoutParams.WRAP_CONTENT);
//        adIngameParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//        adIngameParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//
//        layout.addView(gameAdView, adIngameParams);
        
        // Hook it all up
        setContentView(layout);
        
        if (gameHelper == null) {
        	gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        	gameHelper.enableDebugLog(true);
        }
        gameHelper.setup(this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
//		gameHelper.onStart(this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
//		gameHelper.onStop();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		gameHelper.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void showAds(boolean show) {
		handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
	}
	
	@Override
	public void showAdsIngame(boolean show) {
		handler.sendEmptyMessage(show ? SHOW_ADS_INGAME : HIDE_ADS_INGAME);
	}
	
	@Override
	public void showBig() {
		handler.sendEmptyMessage(SHOW_ADS_BIG);
	}

	@Override
	public void onSignInFailed() {
	}

	@Override
	public void onSignInSucceeded() {
	}

	@Override
	public boolean getSignedIn() {
		return gameHelper.isSignedIn();
	}

	@Override
	public void login() {
		try {
			runOnUiThread(new Runnable(){
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (final Exception ex) {
		}
	}

	@Override
	public void submitScore(int score) {
		Games.Leaderboards.submitScore(gameHelper.getApiClient(), "CgkIt6fUnfQTEAIQCw", score);
	}

	@Override
	public void unlockAchievement(String achievementId) {
		Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);
	}

	@Override
	public void showLeaderboard() {
		if(gameHelper.isSignedIn()) {
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), "CgkIt6fUnfQTEAIQCw"), 100);
		}else if(!gameHelper.isConnecting()) {
		  login();
		}
	}

	@Override
	public void showAchievements() {
		if(gameHelper.isSignedIn()) {
			startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), 101);
		}else if(!gameHelper.isConnecting()) {
			login();
		}
	}

	@Override
	public String getAccountName() {
		return null;
	}
}
