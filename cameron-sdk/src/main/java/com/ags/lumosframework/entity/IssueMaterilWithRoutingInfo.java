package com.ags.lumosframework.entity;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueMaterilWithRoutingInfo {

    private String orderNo;
//    private String materialNo;
//    private String materialDesc;
//    private double requirementQuantity;
//    private double quantityWithdrawn;
//    private double shortage;
//    private String prodStorage;
    private String status;
    //发料信息结束
    private String remark;//routing种发料操作的注意事项

    public IssueMaterilWithRoutingInfo() {
    }

    public IssueMaterilWithRoutingInfo(String orderNo, String status, String remark) {
        this.orderNo = orderNo;
        this.status = status;
        this.remark = remark;
    }
}
