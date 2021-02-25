package com.thph.requestrobotpose.impl;

import java.util.HashMap;
import java.util.Map;

public enum Direction {
	ZNEGATIVE("zNegative"), ZPOSITIVE("zPositive"), YNEGATIVE("yNegative"), YPOSITIVE("yPositive"),
	XNEGATIVE("xNegative"), XPOSITIVE("xPositive"),
	
	RZNEGATIVE("rzNegative"), RZPOSITIVE("rzPositive"), RYNEGATIVE("ryNegative"), RYPOSITIVE("ryPositive"),
	RXNEGATIVE("rxNegative"), RXPOSITIVE("rxPositive");

	public final String label;
	private static final Map<String, Direction> LABEL = new HashMap<String, Direction>();

	static {
		for (Direction e : values()) {
			LABEL.put(e.label, e);
		}
	}

	Direction(String label) {
		this.label = label;
	}
	
    public static Direction valueOfLabel(String label) {
        return LABEL.get(label);
    }

}
