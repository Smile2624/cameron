//Changed by Cameron: 根据工单实际数量显示外观检验结果输入框，并允许修改覆盖结果

package com.ags.lumosframework.ui.view.inspectionqc;

import com.ags.lumosframework.common.exception.PlatformException;
import com.ags.lumosframework.pojo.AppearanceInstrumentationResult;
import com.ags.lumosframework.pojo.Assembling;
import com.ags.lumosframework.pojo.PaintingSpecification;
import com.ags.lumosframework.pojo.ProductionOrder;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.Role;
import com.ags.lumosframework.sdk.service.UserService;
import com.ags.lumosframework.sdk.service.api.IRoleService;
import com.ags.lumosframework.service.IAppearanceInstrumentationResultService;
import com.ags.lumosframework.service.IPaintingSpecificationService;
import com.ags.lumosframework.service.IProductionOrderService;
import com.ags.lumosframework.ui.util.RegExpValidatorUtils;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.base.NotificationUtils;
import com.google.common.base.Strings;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.List;

@SpringComponent
@Scope("prototype")
public class InputPaintThicknessDialog extends BaseDialog {

	private static final long serialVersionUID = -5469810969622716462L;
	GridLayout gridLayout = new GridLayout();
	List<Role> role = null;
	private Grid<Assembling> grid = new Grid<Assembling>();
	@I18Support(caption = "Gague No", captionKey = "paintthicknessdialog.gagueno")
	private TextField tfGageNo = new TextField();
	@I18Support(caption = "Visual Inspection Explain", captionKey = "paintthicknessdialog.visualexplain")
	private TextArea taComments = new TextArea();
	//    @I18Support(caption = "01", captionKey = "")
//    private TextField tfPaintThickness01 = new TextField();
//    @I18Support(caption = "02", captionKey = "")
//    private TextField tfPaintThickness02 = new TextField();
//    @I18Support(caption = "03", captionKey = "")
//    private TextField tfPaintThickness03 = new TextField();
//    @I18Support(caption = "04", captionKey = "")
//    private TextField tfPaintThickness04 = new TextField();
//    @I18Support(caption = "05", captionKey = "")
//    private TextField tfPaintThickness05 = new TextField();
//    @I18Support(caption = "06", captionKey = "")
//    private TextField tfPaintThickness06 = new TextField();
//    @I18Support(caption = "07", captionKey = "")
//    private TextField tfPaintThickness07 = new TextField();
//    @I18Support(caption = "08", captionKey = "")
//    private TextField tfPaintThickness08 = new TextField();
//    @I18Support(caption = "09", captionKey = "")
//    private TextField tfPaintThickness11 = new TextField();
//    @I18Support(caption = "10", captionKey = "")
//    private TextField tfPaintThickness12 = new TextField();
//    @I18Support(caption = "11", captionKey = "")
//    private TextField tfPaintThickness13 = new TextField();
//    @I18Support(caption = "12", captionKey = "")
//    private TextField tfPaintThickness14 = new TextField();
//    @I18Support(caption = "13", captionKey = "")
//    private TextField tfPaintThickness15 = new TextField();
//    @I18Support(caption = "14", captionKey = "")
//    private TextField tfPaintThickness16 = new TextField();
//    @I18Support(caption = "15", captionKey = "")
//    private TextField tfPaintThickness17 = new TextField();
//    @I18Support(caption = "16", captionKey = "")
//    private TextField tfPaintThickness18 = new TextField();
//    @I18Support(caption = "17", captionKey = "")
//    private TextField tfPaintThickness21 = new TextField();
//    @I18Support(caption = "18", captionKey = "")
//    private TextField tfPaintThickness22 = new TextField();
//    @I18Support(caption = "19", captionKey = "")
//    private TextField tfPaintThickness23 = new TextField();
//    @I18Support(caption = "20", captionKey = "")
//    private TextField tfPaintThickness24 = new TextField();
//    @I18Support(caption = "21", captionKey = "")
//    private TextField tfPaintThickness25 = new TextField();
//    @I18Support(caption = "22", captionKey = "")
//    private TextField tfPaintThickness26 = new TextField();
//    @I18Support(caption = "23", captionKey = "")
//    private TextField tfPaintThickness27 = new TextField();
//    @I18Support(caption = "24", captionKey = "")
//    private TextField tfPaintThickness28 = new TextField();
//    @I18Support(caption = "25", captionKey = "")
//    private TextField tfPaintThickness31 = new TextField();
//    @I18Support(caption = "26", captionKey = "")
//    private TextField tfPaintThickness32 = new TextField();
//    @I18Support(caption = "27", captionKey = "")
//    private TextField tfPaintThickness33 = new TextField();
//    @I18Support(caption = "28", captionKey = "")
//    private TextField tfPaintThickness34 = new TextField();
//    @I18Support(caption = "29", captionKey = "")
//    private TextField tfPaintThickness35 = new TextField();
//    @I18Support(caption = "30", captionKey = "")
//    private TextField tfPaintThickness36 = new TextField();
//    @I18Support(caption = "31", captionKey = "")
//    private TextField tfPaintThickness37 = new TextField();
//    @I18Support(caption = "32", captionKey = "")
//    private TextField tfPaintThickness38 = new TextField();
//    @I18Support(caption = "33", captionKey = "")
//    private TextField tfPaintThickness41 = new TextField();
//    @I18Support(caption = "34", captionKey = "")
//    private TextField tfPaintThickness42 = new TextField();
//    @I18Support(caption = "35", captionKey = "")
//    private TextField tfPaintThickness43 = new TextField();
//    @I18Support(caption = "36", captionKey = "")
//    private TextField tfPaintThickness44 = new TextField();
//    @I18Support(caption = "37", captionKey = "")
//    private TextField tfPaintThickness45 = new TextField();
//    @I18Support(caption = "38", captionKey = "")
//    private TextField tfPaintThickness46 = new TextField();
//    @I18Support(caption = "39", captionKey = "")
//    private TextField tfPaintThickness47 = new TextField();
//    @I18Support(caption = "40", captionKey = "")
//    private TextField tfPaintThickness48 = new TextField();
//    @I18Support(caption = "41", captionKey = "")
//    private TextField tfPaintThickness51 = new TextField();
//    @I18Support(caption = "42", captionKey = "")
//    private TextField tfPaintThickness52 = new TextField();
//    @I18Support(caption = "43", captionKey = "")
//    private TextField tfPaintThickness53 = new TextField();
//    @I18Support(caption = "44", captionKey = "")
//    private TextField tfPaintThickness54 = new TextField();
//    @I18Support(caption = "45", captionKey = "")
//    private TextField tfPaintThickness55 = new TextField();
//    @I18Support(caption = "46", captionKey = "")
//    private TextField tfPaintThickness56 = new TextField();
//    @I18Support(caption = "47", captionKey = "")
//    private TextField tfPaintThickness57 = new TextField();
//    @I18Support(caption = "48", captionKey = "")
//    private TextField tfPaintThickness58 = new TextField();
//    @I18Support(caption = "49", captionKey = "")
//    private TextField tfPaintThickness61 = new TextField();
//    @I18Support(caption = "50", captionKey = "")
//    private TextField tfPaintThickness62 = new TextField();
//    @I18Support(caption = "51", captionKey = "")
//    private TextField tfPaintThickness63 = new TextField();
//    @I18Support(caption = "52", captionKey = "")
//    private TextField tfPaintThickness64 = new TextField();
//    @I18Support(caption = "53", captionKey = "")
//    private TextField tfPaintThickness65 = new TextField();
//    @I18Support(caption = "54", captionKey = "")
//    private TextField tfPaintThickness66 = new TextField();
//    @I18Support(caption = "55", captionKey = "")
//    private TextField tfPaintThickness67 = new TextField();
//    @I18Support(caption = "56", captionKey = "")
//    private TextField tfPaintThickness68 = new TextField();
//    @I18Support(caption = "57", captionKey = "")
//    private TextField tfPaintThickness71 = new TextField();
//    @I18Support(caption = "58", captionKey = "")
//    private TextField tfPaintThickness72 = new TextField();
//    @I18Support(caption = "59", captionKey = "")
//    private TextField tfPaintThickness73 = new TextField();
//    @I18Support(caption = "60", captionKey = "")
//    private TextField tfPaintThickness74 = new TextField();
//    @I18Support(caption = "61", captionKey = "")
//    private TextField tfPaintThickness75 = new TextField();
//    @I18Support(caption = "62", captionKey = "")
//    private TextField tfPaintThickness76 = new TextField();
//    @I18Support(caption = "63", captionKey = "")
//    private TextField tfPaintThickness77 = new TextField();
//    @I18Support(caption = "64", captionKey = "")
//    private TextField tfPaintThickness78 = new TextField();
//    @I18Support(caption = "65", captionKey = "")
//    private TextField tfPaintThickness81 = new TextField();
//    @I18Support(caption = "66", captionKey = "")
//    private TextField tfPaintThickness82 = new TextField();
//    @I18Support(caption = "67", captionKey = "")
//    private TextField tfPaintThickness83 = new TextField();
//    @I18Support(caption = "68", captionKey = "")
//    private TextField tfPaintThickness84 = new TextField();
//    @I18Support(caption = "69", captionKey = "")
//    private TextField tfPaintThickness85 = new TextField();
//    @I18Support(caption = "70", captionKey = "")
//    private TextField tfPaintThickness86 = new TextField();
//    @I18Support(caption = "71", captionKey = "")
//    private TextField tfPaintThickness87 = new TextField();
//    @I18Support(caption = "72", captionKey = "")
//    private TextField tfPaintThickness88 = new TextField();
//    TextField[] fields = new TextField[]{tfPaintThickness01, tfPaintThickness02, tfPaintThickness03, tfPaintThickness04, tfPaintThickness05, tfPaintThickness06, tfPaintThickness07, tfPaintThickness08,
//            tfPaintThickness11, tfPaintThickness12, tfPaintThickness13, tfPaintThickness14, tfPaintThickness15, tfPaintThickness16, tfPaintThickness17, tfPaintThickness18,
//            tfPaintThickness21, tfPaintThickness22, tfPaintThickness23, tfPaintThickness24, tfPaintThickness25, tfPaintThickness26, tfPaintThickness27, tfPaintThickness28,
//            tfPaintThickness31, tfPaintThickness32, tfPaintThickness33, tfPaintThickness34, tfPaintThickness35, tfPaintThickness36, tfPaintThickness37, tfPaintThickness38,
//            tfPaintThickness41, tfPaintThickness42, tfPaintThickness43, tfPaintThickness44, tfPaintThickness45, tfPaintThickness46, tfPaintThickness47, tfPaintThickness48,
//            tfPaintThickness51, tfPaintThickness52, tfPaintThickness53, tfPaintThickness54, tfPaintThickness55, tfPaintThickness56, tfPaintThickness57, tfPaintThickness58,
//            tfPaintThickness61, tfPaintThickness62, tfPaintThickness63, tfPaintThickness64, tfPaintThickness65, tfPaintThickness66, tfPaintThickness67, tfPaintThickness68,
//            tfPaintThickness71, tfPaintThickness72, tfPaintThickness73, tfPaintThickness74, tfPaintThickness75, tfPaintThickness76, tfPaintThickness77, tfPaintThickness78,
//            tfPaintThickness81, tfPaintThickness82, tfPaintThickness83, tfPaintThickness84, tfPaintThickness85, tfPaintThickness86, tfPaintThickness87, tfPaintThickness88,};
	private String caption = "喷漆厚度测量结果录入";
	private int productNumber;
	private String orderNo;
	private List<AppearanceInstrumentationResult> appearanceInstrumentationResultList;
	@Autowired
	private IAppearanceInstrumentationResultService appearanceInstrumentationResultService;
	private boolean visualExaminationResult;//外观检测结果
	private String loginUserName = "";
	@Autowired
	private IProductionOrderService orderService;
	@Autowired
	private UserService userService;
	@Autowired
	private IRoleService roleService;
	@Autowired
	private IPaintingSpecificationService paintSpecificationService;

	@Override
	public void show(UI parentUI, DialogCallBack callBack) {
		setHeightUnDefinedMode();
		showDialog(parentUI, caption, "100%", "100%", true, true, callBack);
	}

	public void setData(ProductionOrder productionOrder, List<AppearanceInstrumentationResult> appearanceInstrumentationResultList) {
		if (productionOrder != null) {
			productNumber = productionOrder.getProductNumber();
			orderNo = productionOrder.getProductOrderId();
		}
		gridLayout.removeAllComponents();
		gridLayout.setColumns(8);
		gridLayout.setRows((int) Math.ceil(((double) productNumber) / 8));
		for (int j = 0; j < gridLayout.getRows(); j++) {
			for (int i = 0; i < gridLayout.getColumns(); i++) {
				if (j * 8 + i + 1 <= productNumber) {
					TextField tfTemp = new TextField();
					tfTemp.setCaption(String.format("%02d", (j * 8 + i + 1)));
					tfTemp.setWidth("100%");
					tfTemp.addBlurListener(new BlurListener() {

						private static final long serialVersionUID = 1506984193746285611L;

						@Override
						public void blur(BlurEvent event) {
							TextField textField = (TextField) event.getSource();
							String value = textField.getValue().trim();
							if (!Strings.isNullOrEmpty(value) && !RegExpValidatorUtils.isIsNumber(value)) {
								NotificationUtils.notificationError("请在第:" + textField.getCaption() + "漆厚度输入框中输入数字");
								textField.clear();
								return;
							}
						}
					});
					gridLayout.addComponent(tfTemp, i, j);
				}
			}
		}
		if (appearanceInstrumentationResultList != null && appearanceInstrumentationResultList.size() > 0) {
			this.appearanceInstrumentationResultList = appearanceInstrumentationResultList;
			tfGageNo.setValue(appearanceInstrumentationResultList.get(0).getGageInfo() == null ? "" : appearanceInstrumentationResultList.get(0).getGageInfo());
			for (AppearanceInstrumentationResult appearanceInstrumentationRes : appearanceInstrumentationResultList) {
				String sn = appearanceInstrumentationRes.getSn();
				String caption = sn.substring(sn.length() - 2);
//                for (TextField textField : fields) {
//                    if (textField.getCaption().equals(caption)) {
//                        textField.setValue(appearanceInstrumentationRes.getPaintingThicknessResult() == null ? "" : appearanceInstrumentationRes.getPaintingThicknessResult().toString());
//                        break;
//                    }
//                }
				for (int j = 0; j < gridLayout.getRows(); j++) {
					for (int i = 0; i < gridLayout.getColumns(); i++) {
						if (j * 8 + i + 1 <= productNumber) {
							TextField tfTemp = (TextField) gridLayout.getComponent(i, j);
							if (tfTemp.getCaption().equals(caption)) {
								tfTemp.setValue(appearanceInstrumentationRes.getPaintingThicknessResult() == null ? "" : appearanceInstrumentationRes.getPaintingThicknessResult().toString());
								break;
							}
						}
					}
				}
			}
		} else {
//            for (TextField field : fields) {
//                field.clear();
//            }
			for (int j = 0; j < gridLayout.getRows(); j++) {
				for (int i = 0; i < gridLayout.getColumns(); i++) {
					if (j * 8 + i + 1 <= productNumber) {
						TextField tfTemp = (TextField) gridLayout.getComponent(i, j);
						tfTemp.clear();
					}
				}
			}
		}

		//权限设置
		loginUserName = RequestInfo.current().getUserName();
		role = userService.getByName(loginUserName).getRole();
		if (role.contains(roleService.getByName("qc"))) {
			this.setOKButtonVisible(true);
		} else {
			this.setOKButtonVisible(false);
		}
	}

	@Override
	protected void okButtonClicked() throws Exception {
		float max = 0;
		float min = 0;
		//获取工单的Routing信息
		PaintingSpecification paintSpecification = getPaintRuler(orderNo);
		if (paintSpecification != null) {
			float[] array = sumPaintThinckness(paintSpecification);
			max = array[1];
			min = array[0];
		}

		boolean flag = true;
		int saveNum = 0;
//        for (int i = 0; i < productNumber; i++) {
//            String resultValue = fields[i].getValue().trim();
//            if (!"".equals(resultValue)) {
//                String sn = fields[i].getCaption();
//                int snInt = Integer.parseInt(sn);
//                Float paintingThickness = Float.parseFloat(resultValue);
//                AppearanceInstrumentationResult appearanceInstrumentationResult = appearanceInstrumentationResultService.getBySN(orderNo + String.format("%04d", snInt));
//                if (appearanceInstrumentationResult == null) {
//                    appearanceInstrumentationResult = new AppearanceInstrumentationResult();
//                    appearanceInstrumentationResult.setSn(orderNo + String.format("%04d", snInt));// .substring(caption.length() -2,caption.length())
//                    appearanceInstrumentationResult.setOrderNo(orderNo);
//                }
//                appearanceInstrumentationResult.setPaintingThicknessResult(paintingThickness);
//                appearanceInstrumentationResult.setVisualExaminationDesc(taComments.getValue().trim());
//                appearanceInstrumentationResult.setGageInfo(tfGageNo.getValue().trim());
//                appearanceInstrumentationResultService.save(appearanceInstrumentationResult);
//                if (paintSpecification != null) {
//                    if (paintingThickness > max || paintingThickness < min) {
//                        flag = false;
//                    }
//                }
//                saveNum++;
//            }
//        }
		for (int j = 0; j < gridLayout.getRows(); j++) {
			for (int i = 0; i < gridLayout.getColumns(); i++) {
				if (j * 8 + i + 1 <= productNumber) {
					TextField tfTemp = (TextField) gridLayout.getComponent(i, j);
					String resultValue = tfTemp.getValue().trim();
					if (!"".equals(resultValue)) {
						String sn = tfTemp.getCaption();
						int snInt = Integer.parseInt(sn);
						Float paintingThickness = Float.parseFloat(resultValue);
						AppearanceInstrumentationResult appearanceInstrumentationResult = appearanceInstrumentationResultService.getBySN(orderNo + String.format("%04d", snInt));
						if (appearanceInstrumentationResult == null) {
							appearanceInstrumentationResult = new AppearanceInstrumentationResult();
							appearanceInstrumentationResult.setSn(orderNo + String.format("%04d", snInt));// .substring(caption.length() -2,caption.length())
							appearanceInstrumentationResult.setOrderNo(orderNo);
						}
						appearanceInstrumentationResult.setPaintingThicknessResult(paintingThickness);
						appearanceInstrumentationResult.setVisualExaminationDesc(taComments.getValue().trim());
						appearanceInstrumentationResult.setGageInfo(tfGageNo.getValue().trim());
						appearanceInstrumentationResultService.save(appearanceInstrumentationResult);
						if (paintSpecification != null) {
							if (paintingThickness > max || paintingThickness < min) {
								flag = false;
							}
						}
						saveNum++;
					}
				}
			}
		}
		if (saveNum == 0) {
			throw new PlatformException("工单号：" + orderNo + " 的喷漆厚度测量结果录入数据为空,请输入数据或点击取消键！");
		}
		this.setVisualExaminationResult(flag);
	}


	@Override
	protected void cancelButtonClicked() {

	}

	@Override
	protected Component getDialogContent() {
		VerticalLayout vlLayout = new VerticalLayout();
		vlLayout.setWidth("100%");
		taComments.setSizeFull();
		taComments.setHeight("20%");
		tfGageNo.setSizeFull();
//        //第一行gridLayout
//        gridLayout.setSizeFull();
//        gridLayout.addComponent(tfPaintThickness01, 0, 0);
//        gridLayout.addComponent(tfPaintThickness02, 1, 0);
//        gridLayout.addComponent(tfPaintThickness03, 2, 0);
//        gridLayout.addComponent(tfPaintThickness04, 3, 0);
//        gridLayout.addComponent(tfPaintThickness05, 4, 0);
//        gridLayout.addComponent(tfPaintThickness06, 5, 0);
//        gridLayout.addComponent(tfPaintThickness07, 6, 0);
//        gridLayout.addComponent(tfPaintThickness08, 7, 0);
//        //第二行
//        gridLayout.addComponent(tfPaintThickness11, 0, 1);
//        gridLayout.addComponent(tfPaintThickness12, 1, 1);
//        gridLayout.addComponent(tfPaintThickness13, 2, 1);
//        gridLayout.addComponent(tfPaintThickness14, 3, 1);
//        gridLayout.addComponent(tfPaintThickness15, 4, 1);
//        gridLayout.addComponent(tfPaintThickness16, 5, 1);
//        gridLayout.addComponent(tfPaintThickness17, 6, 1);
//        gridLayout.addComponent(tfPaintThickness18, 7, 1);
//
//        gridLayout.addComponent(tfPaintThickness21, 0, 2);
//        gridLayout.addComponent(tfPaintThickness22, 1, 2);
//        gridLayout.addComponent(tfPaintThickness23, 2, 2);
//        gridLayout.addComponent(tfPaintThickness24, 3, 2);
//        gridLayout.addComponent(tfPaintThickness25, 4, 2);
//        gridLayout.addComponent(tfPaintThickness26, 5, 2);
//        gridLayout.addComponent(tfPaintThickness27, 6, 2);
//        gridLayout.addComponent(tfPaintThickness28, 7, 2);
//
//        gridLayout.addComponent(tfPaintThickness31, 0, 3);
//        gridLayout.addComponent(tfPaintThickness32, 1, 3);
//        gridLayout.addComponent(tfPaintThickness33, 2, 3);
//        gridLayout.addComponent(tfPaintThickness34, 3, 3);
//        gridLayout.addComponent(tfPaintThickness35, 4, 3);
//        gridLayout.addComponent(tfPaintThickness36, 5, 3);
//        gridLayout.addComponent(tfPaintThickness37, 6, 3);
//        gridLayout.addComponent(tfPaintThickness38, 7, 3);
//
//        gridLayout.addComponent(tfPaintThickness41, 0, 4);
//        gridLayout.addComponent(tfPaintThickness42, 1, 4);
//        gridLayout.addComponent(tfPaintThickness43, 2, 4);
//        gridLayout.addComponent(tfPaintThickness44, 3, 4);
//        gridLayout.addComponent(tfPaintThickness45, 4, 4);
//        gridLayout.addComponent(tfPaintThickness46, 5, 4);
//        gridLayout.addComponent(tfPaintThickness47, 6, 4);
//        gridLayout.addComponent(tfPaintThickness48, 7, 4);
//
//        gridLayout.addComponent(tfPaintThickness51, 0, 5);
//        gridLayout.addComponent(tfPaintThickness52, 1, 5);
//        gridLayout.addComponent(tfPaintThickness53, 2, 5);
//        gridLayout.addComponent(tfPaintThickness54, 3, 5);
//        gridLayout.addComponent(tfPaintThickness55, 4, 5);
//        gridLayout.addComponent(tfPaintThickness56, 5, 5);
//        gridLayout.addComponent(tfPaintThickness57, 6, 5);
//        gridLayout.addComponent(tfPaintThickness58, 7, 5);
//
//        gridLayout.addComponent(tfPaintThickness61, 0, 6);
//        gridLayout.addComponent(tfPaintThickness62, 1, 6);
//        gridLayout.addComponent(tfPaintThickness63, 2, 6);
//        gridLayout.addComponent(tfPaintThickness64, 3, 6);
//        gridLayout.addComponent(tfPaintThickness65, 4, 6);
//        gridLayout.addComponent(tfPaintThickness66, 5, 6);
//        gridLayout.addComponent(tfPaintThickness67, 6, 6);
//        gridLayout.addComponent(tfPaintThickness68, 7, 6);
//
//        gridLayout.addComponent(tfPaintThickness71, 0, 7);
//        gridLayout.addComponent(tfPaintThickness72, 1, 7);
//        gridLayout.addComponent(tfPaintThickness73, 2, 7);
//        gridLayout.addComponent(tfPaintThickness74, 3, 7);
//        gridLayout.addComponent(tfPaintThickness75, 4, 7);
//        gridLayout.addComponent(tfPaintThickness76, 5, 7);
//        gridLayout.addComponent(tfPaintThickness77, 6, 7);
//        gridLayout.addComponent(tfPaintThickness78, 7, 7);
//
//        gridLayout.addComponent(tfPaintThickness81, 0, 8);
//        gridLayout.addComponent(tfPaintThickness82, 1, 8);
//        gridLayout.addComponent(tfPaintThickness83, 2, 8);
//        gridLayout.addComponent(tfPaintThickness84, 3, 8);
//        gridLayout.addComponent(tfPaintThickness85, 4, 8);
//        gridLayout.addComponent(tfPaintThickness86, 5, 8);
//        gridLayout.addComponent(tfPaintThickness87, 6, 8);
//        gridLayout.addComponent(tfPaintThickness88, 7, 8);

//        for (TextField field : fields) {
//            field.setWidth("100%");
//            field.addBlurListener(new BlurListener() {
//
//                private static final long serialVersionUID = 1506984193746285611L;
//
//                @Override
//                public void blur(BlurEvent event) {
//                    TextField textField = (TextField) event.getSource();
//                    String value = textField.getValue().trim();
//                    if (!Strings.isNullOrEmpty(value) && !RegExpValidatorUtils.isIsNumber(value)) {
//                        NotificationUtils.notificationError("请在第:" + textField.getCaption() + "漆厚度输入框中输入数字");
//                        textField.clear();
//                        return;
//                    }
//                }
//            });
//        }
		vlLayout.addComponent(gridLayout);
		vlLayout.addComponent(tfGageNo);
		vlLayout.addComponent(taComments);
		return vlLayout;
	}

	@Override
	protected void initUIData() {

	}

	public boolean getVisualExaminationResult() {
		return this.visualExaminationResult;
	}

	public void setVisualExaminationResult(boolean visualExaminationResult) {
		this.visualExaminationResult = visualExaminationResult;
	}

	public boolean valueIsNumber(String value) {
		if (!Strings.isNullOrEmpty(value)) {
			boolean isNumber = RegExpValidatorUtils.isIsNumber(value);
			if (!isNumber) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	//通过工单的Routing信息获取喷漆标准
	public PaintingSpecification getPaintRuler(String orderNo) {
		ProductionOrder order = orderService.getByNo(orderNo);
		String paintSpecificationName = order.getPaintSpecification();
		PaintingSpecification paintSpecification = paintSpecificationService.getBySpecificationFile(paintSpecificationName);
		return paintSpecification;
	}

	public float[] sumPaintThinckness(PaintingSpecification instance) {

		float minSum = 0;
		float maxSum = 0;
		if (instance != null) {
			String primer = instance.getPrimer();
			String finals = instance.getFinals();
			String intermediate = instance.getIntermediate();
			if (!Strings.isNullOrEmpty(primer)) {
				if (primer.contains("-")) {
					float min = Float.parseFloat(primer.split("-")[0].trim());
					minSum += min;
					float max = Float.parseFloat(primer.split("-")[1].trim());
					maxSum += max;
				} else {
					minSum += Float.parseFloat(primer.trim());
					maxSum += Float.parseFloat(primer.trim());
				}
			}
			if (!Strings.isNullOrEmpty(finals)) {
				if (primer.contains("-")) {
					float min = Float.parseFloat(finals.split("-")[0].trim());
					minSum += min;
					float max = Float.parseFloat(finals.split("-")[1].trim());
					maxSum += max;
				} else {
					minSum += Float.parseFloat(finals.trim());
					maxSum += Float.parseFloat(finals.trim());
				}
			}
			if (!Strings.isNullOrEmpty(intermediate)) {
				if (primer.contains("-")) {
					float min = Float.parseFloat(intermediate.split("-")[0].trim());
					minSum += min;
					float max = Float.parseFloat(intermediate.split("-")[1].trim());
					maxSum += max;
				} else {
					minSum += Double.parseDouble(intermediate.trim());
					maxSum += Double.parseDouble(intermediate.trim());
				}
			}
		}
		return new float[]{minSum, maxSum};
	}
}
