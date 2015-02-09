package com.cak.worldobjects;

public class ContactFilters {

	public static final short NONE = 0;
	
	public static final short CAT_MAP = 0x1;
	public static final short CAT_ENTITIES = 0x2;
	public static final short CAT_MOVEABLE = 0x2;
	public static final short CAT_LIGHT = 0x4;
	
	public static final short MASK_ENTITIES = CAT_MAP | ~CAT_ENTITIES;
	public static final short MASK_MOVEABLE = CAT_MAP;
	public static final short MASK_MAP = CAT_LIGHT | CAT_ENTITIES;
	public static final short MASK_LIGHT = CAT_MAP;

	public static final short GROUP_ENTITIES = -1;
	public static final short GROUP_LIGHT = -2;
	public static final short GROUP_SCENERY = 1;
}
