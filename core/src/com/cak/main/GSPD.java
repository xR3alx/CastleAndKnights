package com.cak.main;


public interface GSPD {
		public boolean getSignedIn();
		public void login();
		public void submitScore(int score);
		public void unlockAchievement(String achievementId);
		public void showLeaderboard();
		public void showAchievements();
		public String getAccountName();
}
