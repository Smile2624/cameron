package com.ags.lumosframework.enums;

public enum ElectronicSignatureLoGoType {
	ELECTRONICSIGNATURE("ES","电子签名"),
	LOGO("LG","Logo"),
	SEAL("SEAL","QA公章");

	private String key;
	private String type;

	private ElectronicSignatureLoGoType(String key,String type) {
		this.key = key;
		this.type = type;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
