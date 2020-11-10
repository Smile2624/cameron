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
@Table(name = "STATION_EQUIPMENT")
public class StationEquipmentEntity extends BaseEntity {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6170484445039960986L;
	
	public static final String STATION = "station";//工位
	public static final String EQUIPMENT_NO = " equipmentNo";//设备编号
	public static final String EQUIPMENT_TYPE = " equipmentType";//设备编号
	public static final String IP_ADRESS = "ipAdress";//工位ip
	public static final String PROCEDURE_NO = "procedureNo";//工位ip

	@Column(name = "STATION", length = 80)
    private String station;

    @Column(name = "EQUIPMENT_NO", length = 80)
    private String equipmentNo;

	@Column(name = "EQUIPMENT_TYPE", length = 80)
	private String equipmentType;

    @Column(name = "IP_ADRESS", length = 80)
	private String ipAdress;

	@Column(name = "PROCEDURE_NO", length = 80)
	private String procedureNo;

}
