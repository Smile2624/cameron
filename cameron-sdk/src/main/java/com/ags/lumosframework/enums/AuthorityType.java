package com.ags.lumosframework.enums;

public enum AuthorityType {
	HARDNESS("H","硬度"),
	QUALITYPLAN("QP","质量计划"),
	PRESSURE("P","压力");

	private String key;
	private String type;

	private AuthorityType(String key,String type) {
		this.key = key;
		this.type=type;
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
	
	public static AuthorityType getValue(String key) {
		for (AuthorityType a : AuthorityType.values()) {
            if (a.getKey().equals(key)) {
                return a;
            }
        }
        return null;
	}
}
