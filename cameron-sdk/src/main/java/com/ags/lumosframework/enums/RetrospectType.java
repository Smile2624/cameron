package com.ags.lumosframework.enums;

public enum RetrospectType {
	SINGLE("S"),
	BATCH("B");


	private String type;

	private RetrospectType(String type) {

		this.type=type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
