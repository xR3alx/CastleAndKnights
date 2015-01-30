package com.cak.worldobjects;

public class ContactFilters {

	public static final short NONE = 0;
	
	public static final short CAT_MAP = 0x0001;
	public static final short CAT_ENTITIES = 0x0002;
	public static final short CAT_MOVEABLE = 0x0002;
	public static final short CAT_LIGHT = 0x0008;
	
	public static final short MASK_ENTITIES = CAT_MAP;
	public static final short MASK_MOVEABLE = CAT_MAP;
	public static final short MASK_MAP = CAT_LIGHT;
	public static final short MASK_LIGHT = CAT_MAP;

	public static final short GROUP_ENTITIES = 1;
	public static final short GROUP_LIGHT = -2;
	public static final short GROUP_WORLD = 1;
}
