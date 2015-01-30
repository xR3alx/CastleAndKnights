package com.cak.utils;

import com.cak.files.UserData;

public class Utils {

	public static int calculateUpgradeCost(int standardprice, int level){
		int newprice = (int) ((31.8216f * (13.8216f * standardprice / 100) * level + standardprice) / 100) * level + standardprice;
		return newprice;
	}
	
	public static void calculateExpToLevel(){
		int exp = (int) (129.17935 * UserData.getInt("levelprogression_max") / 100);
		UserData.setProperty("levelprogression_max", exp + "");
	}
	
	
}
