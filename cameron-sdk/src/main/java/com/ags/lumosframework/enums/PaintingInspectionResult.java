package com.ags.lumosframework.enums;

public enum PaintingInspectionResult {
	OK("OK","通过"),
	NG("NG","不良");

	private String key;
	private String result;

	private PaintingInspectionResult(String key,String result) {
		this.key = key;
		this.result = result;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
