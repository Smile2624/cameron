package com.ags.lumosframework.pojo;

import com.ags.lumosframework.entity.InspectionPlanEntity;
import com.ags.lumosframework.sdk.base.domain.ObjectBaseImpl;

public class InspectionPlan extends ObjectBaseImpl<InspectionPlanEntity>  {


    public InspectionPlan(InspectionPlanEntity entity) {
        super(entity);
    }

    public InspectionPlan() {
        super(null);
    }
    @Override
    public String getName() {
        return null;
    }

    public String getProductNo(){return this.getInternalObject().getProductNo();}

    public void setProductNo(String productNo){this.getInternalObject().setProductNo(productNo);}

    public String getProductDesc(){return this.getInternalObject().getProductDesc();}

    public void setProductDesc(String productDesc){this.getInternalObject().setProductDesc(productDesc);}

    public String getInsPlan(){return this.getInternalObject().getInsPlan();}

    public void setInsPlan(String insPlan){this.getInternalObject().setInsPlan(insPlan);}

    public String getQCode(){return this.getInternalObject().getQCode();}

    public void setQCode(String qCode){this.getInternalObject().setQCode(qCode);}

    public String getChemicalAnalysis(){return this.getInternalObject().getChemicalAnalysis();}

    public void setChemicalAnalysis(String chemicalAnalysis){this.getInternalObject().setChemicalAnalysis(chemicalAnalysis);}

    public String getPreHeatDimension(){return this.getInternalObject().getPreHeatDimension();}

    public void setPreHeatDimension(String preHeatDimension){this.getInternalObject().setPreHeatDimension(preHeatDimension);}

    public String getForgHeatControl(){return this.getInternalObject().getForgHeatControl();}

    public void setForgHeatControl(String forgHeatControl){this.getInternalObject().setForgHeatControl(forgHeatControl);}

    public String getMechanicalTest(){return this.getInternalObject().getMechanicalTest();}

    public void setMechanicalTest(String mechanicalTest){this.getInternalObject().setMechanicalTest(mechanicalTest);}

    public String getVolumNde(){return this.getInternalObject().getVolumNde();}

    public void setVolumNde(String volumNde){this.getInternalObject().setVolumNde(volumNde);}

    public String getTraceMark(){return this.getInternalObject().getTraceMark();}

    public void setTraceMark(String traceMark){this.getInternalObject().setTraceMark(traceMark);}

    public String getSurNde(){return this.getInternalObject().getSurNde();}

    public void setSurNde(String surNde){this.getInternalObject().setSurNde(surNde);}

    public String getPartHardness(){return this.getInternalObject().getPartHardness();}

    public void setPartHardness(String partHardness){this.getInternalObject().setPartHardness(partHardness);}

    public String getVisualExam(){return this.getInternalObject().getVisualExam();}

    public void setVisualExam(String visualExam){this.getInternalObject().setVisualExam(visualExam);}

    public String getWeldOverlay(){return this.getInternalObject().getWeldOverlay();}

    public void setWeldOverlay(String weldOverlay){this.getInternalObject().setWeldOverlay(weldOverlay);}

    public String getWeldPrepNde(){return this.getInternalObject().getWeldPrepNde();}

    public void setWeldPrepNde(String weldPrepNde){this.getInternalObject().setWeldPrepNde(weldPrepNde);}

    public String getFinalNde(){return this.getInternalObject().getFinalNde();}

    public void setFinalNde(String finalNde){this.getInternalObject().setFinalNde(finalNde);}

    public String getDimensionInpection(){return this.getInternalObject().getDimensionInpection();}

    public void setDimensionInpection(String dimensionInpection){this.getInternalObject().setDimensionInpection(dimensionInpection);}

    public String getCoatPaint(){return this.getInternalObject().getCoatPaint();}

    public void setCoatPaint(String coatPaint){this.getInternalObject().setCoatPaint(coatPaint);}

    public String getCocElastomer(){return this.getInternalObject().getCocElastomer();}

    public void setCocElastomer(String cocElastomer){this.getInternalObject().setCocElastomer(cocElastomer);}

    public String getCocCameron(){return this.getInternalObject().getCocCameron();}

    public void setCocCameron(String cocCameron){this.getInternalObject().setCocCameron(cocCameron);}
}
