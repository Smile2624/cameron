package com.ags.lumosframework.entity;

import com.ags.lumosframework.sdk.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author peyton
 * @date 2019/10/18 10:16
 */
@Getter
@Setter
@Entity
@Table(name = "CA_CONFIG")
public class CaConfigEntity  extends BaseEntity {
    private static final long serialVersionUID = -1249536769666263240L;

    public static final String CONFIG_TYPE = "configType";//类
    public static final String CONFIG_VALUE = "configValue";//值1
    public static final String CONFIG_REMARK = "configRemark";//值2

    @Column(name = "CONFIG_TYPE", length = 80)
    private String configType;

    @Column(name = "CONFIG_VALUE", length = 80)
    private String configValue;

    @Column(name = "CONFIG_REMARK", length = 255)
    private String configRemark;

}
