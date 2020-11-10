package com.ags.lumosframework.entity;

import com.ags.lumosframework.sdk.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "POST")
public class PostEntity extends BaseEntity {
	
	private static final long serialVersionUID = -1654515126669482627L;
	
	public static final String POST_CODE = "code";
	public static final String POST_NAME = "name";

	@Column(name = "POST_CODE",length = 255)
	private String code;
	
	@Column(name = "POST_NAME",length = 255)
	private String name;
}
