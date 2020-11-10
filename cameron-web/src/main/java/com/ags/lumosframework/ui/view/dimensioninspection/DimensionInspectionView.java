package com.ags.lumosframework.ui.view.dimensioninspection;

import com.ags.lumosframework.pojo.*;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.Media;
import com.ags.lumosframework.service.*;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.ui.util.RegExpValidatorUtils;
import com.ags.lumosframework.ui.util.SocketClient;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.*;
import com.ags.lumosframework.web.vaadin.base.ConfirmResult.Result;
import com.ags.lumosframework.web.vaadin.component.LabelWithSamleLineCaption;
import com.google.common.base.Strings;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

//@Menu(caption = "DimensionInspection", captionI18NKey = "view.dimensioninspection.caption", iconPath = "images/icon/text-blob.png", groupName = "UserOperation", order = 2)
//@SpringView(name = "DimensionInspection", ui = CameronUI.class)
public class DimensionInspectionView extends BaseView implements Button.ClickListener {

	private static final long serialVersionUID = -5881921658681194159L;

	private ComboBox<String> cbPurchasingNo = new ComboBox();

	@I18Support(caption = "Confirm", captionKey = "common.confirm")
	private Button btnConfirm = new Button();

	@I18Support(caption = "Start", captionKey = "common.start")
	private Button btnStart = new Button();

	private Button btnGetData = new Button();

	private Button send = new Button();

	@I18Support(caption = "MaterialNo", captionKey = "view.dimensioninspection.materialno")
	private LabelWithSamleLineCaption lblMaterialNo = new LabelWithSamleLineCaption();

	@I18Support(caption = "MaterialRev", captionKey = "view.dimensioninspection.materialrev")
	private LabelWithSamleLineCaption lblMaterialRev = new LabelWithSamleLineCaption();

	@I18Support(caption = "Vendor", captionKey = "view.dimensioninspection.vendor")
	private LabelWithSamleLineCaption lblVendor = new LabelWithSamleLineCaption();

	@I18Support(caption = "MaterialDesc", captionKey = "view.dimensioninspection.materialdesc")
	private LabelWithSamleLineCaption lblMaterialDesc = new LabelWithSamleLineCaption();

	@I18Support(caption = "QualityPlan", captionKey = "view.dimensioninspection.qualityplan")
	private LabelWithSamleLineCaption lblQualityPlan = new LabelWithSamleLineCaption();

	@I18Support(caption = "DrawingNo", captionKey = "view.dimensioninspection.drawingno")
	private LabelWithSamleLineCaption lblDrawingNo = new LabelWithSamleLineCaption();

	@I18Support(caption = "SerialNo", captionKey = "view.dimensioninspection.serialno")
	private TextField tfSerialNos = new TextField();

	private TextField tfInspectionQuantity = new TextField();

	// private ComboBox<String> cbInspectionMode = new ComboBox<>();// 检验方式 抽检，全检

	private Grid<PurchasingOrderInfo> gridObject = new Grid<>();

	private ComboBox<String> cbInspectionType = new ComboBox<>();// 检验类型 单件，分组，无SN

	private GridLayout gridLayout = new GridLayout();

	VerticalLayout hlToolBox = new VerticalLayout();

	private CheckBox cbCheckAll = new CheckBox();

	Panel inspectionValue = new Panel();

	VerticalLayout vlDisplay = new VerticalLayout();

	HorizontalLayout hlTemp1 = new HorizontalLayout();
	HorizontalLayout hlTemp2 = new HorizontalLayout();
	HorizontalLayout hlTemp3 = new HorizontalLayout();
	private AbstractComponent[] components = new AbstractComponent[] { cbPurchasingNo, tfInspectionQuantity,
			cbInspectionType, btnGetData, btnStart, btnConfirm };// cbInspectionMode,

	List<DimensionRuler> listInstance = null;

	@Autowired
	private IPurchasingOrderService purchasingOrderService;

	@Autowired
	private ISparePartService sparePartService;

	@Autowired
	IDimensionInspectionService dimensionInspectionService;

	@Autowired
	IDimensionRulerService dimensionRulerService;

	@Autowired
	private ICaMediaService caMediaService;

	@Autowired
	ICaConfigService caConfigService;

	// x,y 当前数据输入位置
	private int x = 0;
	private int y = 0;
	// 选中的checkBox序号
	private int checkedIndex = 0;
	private String ipAddress = "";
	private String prefixSend = "";// 当前发送的语音指令，用于判断返回信息并执行什么操作
	private String materialNo = "";
	private String materialRev = "";
	private int inspectionQuantitySetted = 0;
	// 抽检数量
	private int inspectionQuantity = 0;
	// 检验的SN的编号
	private String inspectionSn = "";

	StringBuilder returnMessage = new StringBuilder();
	SocketClient client = null;

	private String inspectionItemName = "";// 检验项的名称
	private String gageInfo = "";
	PurchasingOrderInfo purchasingOrderInfo = null;

	CaConfig con = null;

	private boolean ensureMessage = false;
	StringBuilder ifSave = new StringBuilder();
	// 表示当前检验的采购单是否通过检验
	private boolean pass = true;
	boolean reSpeak = false;
	private List<String> checkedItemsList = new ArrayList<>();

	boolean isFirst = true;
	String checkedSn = "";

	public DimensionInspectionView() {
		VerticalLayout vlRoot = new VerticalLayout();
		vlRoot.setMargin(false);
		vlRoot.setSizeFull();

		hlToolBox.setWidth("100%");
		hlToolBox.addStyleName(CoreTheme.TOOLBOX);
		hlToolBox.setMargin(true);
		vlRoot.addComponent(hlToolBox);
		HorizontalLayout hlTempToolBox = new HorizontalLayout();
		hlToolBox.addComponent(hlTempToolBox);
		for (Component component : components) {
			hlTempToolBox.addComponent(component);
		}
		btnStart.setIcon(VaadinIcons.START_COG);
		btnStart.addClickListener(this);
		btnStart.setEnabled(false);
		btnConfirm.setIcon(VaadinIcons.CHECK);
		btnConfirm.addClickListener(this);
		btnConfirm.setEnabled(false);
		send.addClickListener(this);
		btnGetData.addClickListener(this);
		btnGetData.setIcon(VaadinIcons.GAVEL);
		tfInspectionQuantity.setPlaceholder("检验数量");
		tfInspectionQuantity.setEnabled(false);
		tfInspectionQuantity.addValueChangeListener(new ValueChangeListener<String>() {

			private static final long serialVersionUID = -547168792572880770L;

			@Override
			public void valueChange(ValueChangeEvent<String> event) {
				String inputValue = event.getValue();
				if (!Strings.isNullOrEmpty(inputValue)) {
					if (RegExpValidatorUtils.isIsPositive(inputValue)) {
						if (Integer.parseInt(inputValue) > purchasingOrderInfo.getMaterialQuantity()) {
							NotificationUtils.notificationError("输入数量大于订单数量，请重新输入");
							return;
						}
						if (Integer.parseInt(inputValue) == 0) {
							tfInspectionQuantity.setValue("");
							NotificationUtils.notificationError("检验数量需要输入大于0的数量");
							return;
						}
						gridLayout.removeAllComponents();
						inspectionQuantity = Integer.valueOf(inputValue);
						inspectionValue.setSizeFull();
						inspectionValue.setContent(gridLayout);
						purchasingOrderInfo = gridObject.asSingleSelect().getValue();
						listInstance = dimensionRulerService.getByNoRev(purchasingOrderInfo.getMaterialNo(),
								purchasingOrderInfo.getMaterialRev());
						int inspectionItems = listInstance.size();// rows
						gridLayout.setRows(inspectionItems + 1);
						gridLayout.setColumns(inspectionQuantity + 3);
						gridLayout.setMargin(true);
						Label lblDrawing = new Label("图纸尺寸");
						lblDrawing.addStyleName(CoreTheme.FONT_PRIMARY);
						gridLayout.addComponent(lblDrawing, 1, 0);
						gridLayout.addComponent(cbCheckAll, 0, 0);
						cbCheckAll.addValueChangeListener(new ValueChangeListener<Boolean>() {

							private static final long serialVersionUID = -4392227730330074158L;

							@Override
							public void valueChange(ValueChangeEvent<Boolean> event) {
								int rowCount = gridLayout.getRows();
								if (event.getValue()) {
									// 全选
									for (int index = 1; index < rowCount; index++) {
										CheckBox cbInstance = (CheckBox) gridLayout.getComponent(0, index);
										cbInstance.setValue(true);
									}
								} else {
									for (int index = 1; index < rowCount; index++) {
										CheckBox cbInstance = (CheckBox) gridLayout.getComponent(0, index);
										cbInstance.setValue(false);
									}
								}
							}
						});
						// 初始化第一列
						for (int i = 1; i < inspectionItems + 1; i++) {
							CheckBox cbBox = new CheckBox();
							// cbBox.setVisible(false);
							gridLayout.addComponent(cbBox, 0, i);
						}
						// 初始化gridlayout第二列，检验项
						for (int i = 1; i < inspectionItems + 1; i++) {
							Label label = new Label();
							label.addStyleName(CoreTheme.FONT_PRIMARY);
							String inspectionName = listInstance.get(i - 1).getInspectionItemName();
							label.setValue(inspectionName);
							gridLayout.addComponent(label, 1, i);
						}
						// 初始化GridLayout第一行,SN序列号
						for (int i = 2; i < inspectionQuantity + 2; i++) {
							TextField tfSN = new TextField();
							tfSN.setWidth("55px");
							tfSN.setPlaceholder("零件SN");
							tfSN.setValueChangeTimeout(1000);
							gridLayout.addComponent(tfSN, i, 0);
						}
						Label lblGageInfo = new Label("量具信息");
						lblGageInfo.addStyleName(CoreTheme.FONT_PRIMARY);
						gridLayout.addComponent(lblGageInfo, inspectionQuantity + 2, 0);// 量具信息
						gridLayout.setComponentAlignment(lblGageInfo, Alignment.MIDDLE_CENTER);
						for (int i = 2; i < inspectionQuantity + 3; i++) {
							for (int j = 1; j < inspectionItems + 1; j++) {
								TextField tfInspectionValue = new TextField();
								if (i == inspectionQuantity + 2) {
									tfInspectionValue.setWidth("100px");
								} else {
									tfInspectionValue.setWidth("55px");
								}
								tfInspectionValue.setValueChangeTimeout(1000);
								gridLayout.addComponent(tfInspectionValue, i, j);
							}
						}
					}
				}
			}
		});
		cbInspectionType
				.setPlaceholder(I18NUtility.getValue("view.dimensioninspection.inspectiontype", "InspectionType"));
		cbInspectionType.setItems("单件", "分组");// , "无SN"
		cbInspectionType.setEnabled(false);// 单件:每一个件需要检验完所有的检验项才开始下一个件的检验;分组:检验完一组中所有的SN的一个检验项才会检查下一个检验项;无SN:只有结果，没有SN
		cbInspectionType.addValueChangeListener(new ValueChangeListener<String>() {
			private static final long serialVersionUID = 8850451531559355360L;

			@Override
			public void valueChange(ValueChangeEvent<String> event) {

				if (Strings.isNullOrEmpty(event.getValue())) {
					btnConfirm.setEnabled(false);
					btnStart.setEnabled(false);
				} else {
					btnConfirm.setEnabled(true);
					btnStart.setEnabled(true);
				}

			}
		});
		cbPurchasingNo.setPlaceholder("采购单号");

		cbPurchasingNo.addValueChangeListener(new ValueChangeListener<String>() {

			private static final long serialVersionUID = -6254353796216761054L;

			@Override
			public void valueChange(ValueChangeEvent<String> event) {
				if (!event.getValue().equals(event.getOldValue())) {
					String purchasingNo = cbPurchasingNo.getValue().trim();
					if (!Strings.isNullOrEmpty(purchasingNo)) {
						List<PurchasingOrderInfo> listPurchasingOrderInfo = purchasingOrderService
								.getUncheckedOrder(purchasingNo, "DIMENSION");
						if (listPurchasingOrderInfo != null && listPurchasingOrderInfo.size() > 0) {
							// 数据填入grid
							gridObject.setDataProvider(DataProvider.ofCollection(listPurchasingOrderInfo));
						} else {
							NotificationUtils.notificationError(I18NUtility.getValue(
									"view.dimensioninspection.purchasingnotexist",
									"Purchasing No Not Exist or This Order Has Finished Dimension Inspection."));
						}
					} else {
						NotificationUtils.notificationError(I18NUtility
								.getValue("view.dimensioninspection.purchasingnotnull", "PurchasingNo can't be null"));
					}
				}
			}
		});
		vlRoot.addComponent(vlDisplay);
		vlDisplay.setWidth("100%");
		vlDisplay.setMargin(true);
		vlDisplay.addComponent(hlTemp1);
		vlDisplay.addComponent(hlTemp2);
		vlDisplay.addComponent(hlTemp3);
		hlTemp1.addStyleName(CoreTheme.INPUT_DISPLAY_INLINE);
		hlTemp2.addStyleName(CoreTheme.INPUT_DISPLAY_INLINE);
		hlTemp3.addStyleName(CoreTheme.INPUT_DISPLAY_INLINE);
		hlTemp1.setWidth("100%");
		hlTemp2.setWidth("100%");
		hlTemp1.addComponent(lblMaterialNo);
		hlTemp1.addComponent(lblMaterialRev);
		hlTemp1.addComponent(tfSerialNos);
		hlTemp2.addComponent(lblVendor);
		hlTemp2.addComponent(lblQualityPlan);
		hlTemp2.addComponent(lblDrawingNo);
		hlTemp3.addComponent(lblMaterialDesc);
		HorizontalSplitPanel hlSplitPanel = new HorizontalSplitPanel();
		hlSplitPanel.setSplitPosition(300.0F, Unit.PIXELS);
		hlSplitPanel.setSizeFull();
		vlRoot.addComponent(hlSplitPanel);
		vlRoot.setExpandRatio(hlSplitPanel, 1);
		gridObject.setSizeFull();
		gridObject.addColumn(PurchasingOrderInfo::getPurchasingItemNo)
				.setCaption(I18NUtility.getValue("view.materialinspection.purchasingitemno", "PurchasingItemNo"))
				.setWidth(100.0);
		gridObject.addColumn(PurchasingOrderInfo::getSapInspectionLot)
				.setCaption(I18NUtility.getValue("view.materialinspection.sapinspectionlot", "SapInspectionLot"))
				.setWidth(120.0);
		gridObject.addColumn(PurchasingOrderInfo::getMaterialQuantity)
				.setCaption(I18NUtility.getValue("view.materialinspection.materialquantity", "Quantity"))
				.setWidth(80.0);
		gridObject.addSelectionListener(new SelectionListener<PurchasingOrderInfo>() {

			private static final long serialVersionUID = -454658178418921022L;

			@Override
			public void selectionChange(SelectionEvent<PurchasingOrderInfo> event) {
				isFirst = true;
				if (event.getFirstSelectedItem().isPresent()) {
					purchasingOrderInfo = event.getFirstSelectedItem().get();
					materialNo = purchasingOrderInfo.getMaterialNo();
					materialRev = purchasingOrderInfo.getMaterialRev();
					inspectionQuantitySetted = purchasingOrderInfo.getInspectionQuantity();
					tfInspectionQuantity.setValue("");
					// 判断该订单是否需要进行尺寸检验
					setDataToDisplayArea(purchasingOrderInfo);
					List<DimensionRuler> list = dimensionRulerService.getByNoRev(materialNo, materialRev);
					if (list == null || list.size() == 0) {
						NotificationUtils.notificationError("当前零件没有维护尺寸检验项或者不需要尺寸检验，请确认");
						gridLayout.removeAllComponents();
						return;
					}
					if (inspectionQuantitySetted == 0) {
						NotificationUtils.notificationError("请联系管理员配置该订单需要检验的数量");
						gridLayout.removeAllComponents();
						return;
					}
					tfInspectionQuantity.setValue(String.valueOf(purchasingOrderInfo.getInspectionQuantity()));
					cbInspectionType.setEnabled(true);
					cbInspectionType.setSelectedItem(null);
				} else {
					setDataToDisplayArea(null);
					purchasingOrderInfo = null;
					materialNo = "";
					materialRev = "";
					cbInspectionType.setEnabled(false);
					cbInspectionType.setSelectedItem(null);
					gridLayout.removeAllComponents();
				}
			}
		});
		hlSplitPanel.setFirstComponent((Component) gridObject);
		inspectionValue.setSizeFull();
		inspectionValue.setContent(gridLayout);
		hlSplitPanel.setSecondComponent((Component) inspectionValue);
		this.setSizeFull();
		this.setCompositionRoot(vlRoot);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		Button button = event.getButton();
		if (btnConfirm.equals(button)) {
			con = caConfigService.getConfigByType(AppConstant.CA_CONFIRM_REPORT_SAVE_PATH);
			if (con == null) {
				NotificationUtils.notificationError("请先配置报告存放的根目录!");
				return;
			}
			Media mediaImage = caMediaService.getMediaByName(RequestInfo.current().getUserName());
			if (mediaImage == null) {
				NotificationUtils
						.notificationError("当前没有配置用户:" + RequestInfo.current().getUserName() + "的电子签名,请首先配置该用户的电子签名");
				return;
			}
			if (Strings.isNullOrEmpty(tfSerialNos.getValue())) {
				NotificationUtils.notificationError("请输入检验的序列号!");
				return;
			}
			String rootPath = con.getConfigValue();
			// if(ifSingleInspection()) {
			ConfirmDialog.show(getUI(), "确定要保存当前检验信息吗", new DialogCallBack() {
				@Override
				public void done(ConfirmResult result) {
					if (result.getResult().equals(Result.OK)) {
						if (ifSingleInspection()) {
							if (!saveData()) {
								return;
							}
							// 判断数据是否全部录入完成
							if (inspectionDone()) {
								createReport(rootPath);
								purchasingOrderInfo.setDimensionChecked(true);
							}
							if ("OK".equals(purchasingOrderInfo.getDimensionCheckedRlt()) && pass) {
								purchasingOrderInfo.setDimensionCheckedRlt("OK");
							} else {
								purchasingOrderInfo.setDimensionCheckedRlt("NG");
							}
							purchasingOrderInfo.setDescription(cbInspectionType.getValue());
							purchasingOrderInfo.setCheckedSn(checkedSn());
							purchasingOrderService.save(purchasingOrderInfo);
							gridLayout.removeAllComponents();
							gridObject.setDataProvider(DataProvider.ofCollection(purchasingOrderService
									.getUncheckedOrder(cbPurchasingNo.getValue().trim(), "DIMENSION")));
						} else {
							if (!saveDataPartical()) {
								return;
							}
							if (inspectionDone()) {
								createReport(rootPath);
								purchasingOrderInfo.setDimensionChecked(true);
							}
							if ("OK".equals(purchasingOrderInfo.getDimensionCheckedRlt()) && pass) {
								purchasingOrderInfo.setDimensionCheckedRlt("OK");
							} else {
								purchasingOrderInfo.setDimensionCheckedRlt("NG");
							}
							purchasingOrderInfo.setCheckedSn(checkedSn());
							purchasingOrderInfo.setDescription(cbInspectionType.getValue());
							purchasingOrderService.save(purchasingOrderInfo);
							gridLayout.removeAllComponents();
							gridObject.setDataProvider(DataProvider.ofCollection(purchasingOrderService
									.getUncheckedOrder(cbPurchasingNo.getValue().trim(), "DIMENSION")));
						}
						checkedItemsList = null;
						inspectionItemName = "";
						tfSerialNos.clear();
						pass = true;
						isFirst = true;
						checkedSn = "";
					}
				}
			});
			try {
				if (client != null) {
					client.close();
					btnStart.setEnabled(true);
				}
			} catch (Exception e) {
			}

		} else if (btnGetData.equals(button)) {
			String inspectionType = purchasingOrderInfo.getDescription();
			if (!Strings.isNullOrEmpty(inspectionType)) {
				cbInspectionType.setSelectedItem(inspectionType);
				String sapLot = purchasingOrderInfo.getSapInspectionLot();
				String snPrefix = purchasingOrderInfo.getPurchasingNo() + "-"
						+ purchasingOrderInfo.getPurchasingItemNo();
				String snChecked = purchasingOrderInfo.getCheckedSn();
				String[] arraySn =snChecked.split(",");
				if(arraySn.length > 0) {
					isFirst = false;
				}
				for (int k = 0; k < arraySn.length; k++) {
					((TextField) gridLayout.getComponent(2 + k, 0)).setValue(arraySn[k]);
				}
				boolean flag = true;
				if ("分组".equals(inspectionType)) {
					for (int rowIndex = 1; rowIndex < gridLayout.getRows(); rowIndex++) {
						for (int colmnIndex = 0; colmnIndex < arraySn.length; colmnIndex++) {
							// 获取该零件SN对应的行的检验项的检验值
							String inspectionName = ((Label) gridLayout.getComponent(1, rowIndex)).getValue();
							String value = dimensionInspectionService.getInspectionValue(sapLot, inspectionName,
									snPrefix + "-" + arraySn[colmnIndex]);
							if (!Strings.isNullOrEmpty(value)) {
								((TextField) gridLayout.getComponent(2 + colmnIndex, rowIndex)).setValue(value);
								isFirst = false;
							} else {
								((TextField) gridLayout.getComponent(2 + colmnIndex, rowIndex)).setValue("");
								break;
							}
						}
					}
					// 最后加载量具信息
					for (int rowIndex = 1; rowIndex < gridLayout.getRows(); rowIndex++) {
						String inspectionName = ((Label) gridLayout.getComponent(1, rowIndex)).getValue();
						String gageNo = dimensionInspectionService.getGageNo(sapLot, inspectionName);
						((TextField) gridLayout.getComponent(gridLayout.getColumns() - 1, rowIndex)).setValue(gageNo);
					}
				} else {
					for (int k = 0; k < arraySn.length; k++) {
						((TextField) gridLayout.getComponent(2 + k, 0)).setValue(arraySn[k]);
					}
					for (int colmnIndex = 0; colmnIndex < arraySn.length; colmnIndex++) {
						for (int rowIndex = 1; rowIndex < gridLayout.getRows(); rowIndex++) {
							String inspectionName = ((Label) gridLayout.getComponent(1, rowIndex)).getValue();
							String value = dimensionInspectionService.getInspectionValue(sapLot, inspectionName,
									snPrefix + "-" + arraySn[colmnIndex]);
							if (!Strings.isNullOrEmpty(value)) {
								((TextField) gridLayout.getComponent(2 + colmnIndex, rowIndex)).setValue(value);
								isFirst = false;
							} else {
								((TextField) gridLayout.getComponent(2 + colmnIndex, rowIndex)).setValue("");
							}
						}
					}
				}
			}

		} else {
			// 检验开始,光标聚焦gridlayout(2,0)
			if (isFirst) {
				x = 2;
				y = 0;
				System.out.println(x + "," + y);
			} else {
				x = getStartXY()[0];
				y = getStartXY()[1];
				System.out.println(x + "," + y);
			}
			ipAddress = RequestInfo.current().getUserIpAddress();

			try {
				client = new SocketClient(ipAddress, AppConstant.PORT);
				btnStart.setEnabled(false);
				client.setReceiveListener(message -> {
					// 定义 OnMessage 后执行事件。
					System.out.println("message from server:" + message);
					if (message.startsWith(AppConstant.PREFIXPLATTEXTEND)
							&& (AppConstant.PREFIXPLAYTEXT.equals(prefixSend))) {
						// [OC]语音播放结束
						System.out.println("Receive Message -> " + message);
						// 发送请求语音识别的指令
						try {
							prefixSend = AppConstant.PREFIXSTARTRECORD;
							client.sendMessage("[BR]");
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (message.startsWith(AppConstant.PREFIXRECORDRESULT)
							&& AppConstant.PREFIXSTARTRECORD.equals(prefixSend)) {
						String messageBody = message.split("]")[1];
						if (ensureMessage) {
							if (!message.split("\\|")[1].equals("1")) {
								ifSave.append(messageBody.split("\\|")[0]);
							}
						} else {
							if (!message.split("\\|")[1].equals("1")) {
								returnMessage.append(messageBody.split("\\|")[0]);
							}
						}
					} else if (message.startsWith(AppConstant.PREFIXRECORDRESULTEND)) {
						if (returnMessage.length() == 0) {
							prefixSend = AppConstant.PREFIXPLAYTEXT;
							try {
								if (y == 0) {
									// sn序列号
									client.sendMessage("[PT]请输入检测的第" + (x - 1) + "个SN序列号");
								} else {
									// 检验项
									if (x == gridLayout.getColumns() - 1) {
										inspectionItemName = ((Label) gridLayout.getComponent(1, y)).getValue();
										client.sendMessage("[PT]请输入检验项" + inspectionItemName + "的量具编号");
									} else {
										inspectionItemName = ((Label) gridLayout.getComponent(1, y)).getValue();
										client.sendMessage("[PT]请输入检验项" + inspectionItemName + "的值");
									}

								}
							} catch (Exception e) {
								NotificationUtils.notificationError("Socket连接中断");
							}
							// 没有识别当前检测项的值，任然播报当前检测项的信息
						} else {
							// 通过零件号和版本号获取尺寸检验项的标准
							DimensionRuler ruler = dimensionRulerService.getByNoRevItemName(materialNo, materialRev,
									inspectionItemName);
							double maxValue = ruler.getMaxValue();
							double minValue = ruler.getMinValue();
							String type = ruler.getInspectionItemType();
							System.out.println("type= " + type + ",x= " + x + ",y= " + y);
							if ("NUMBRIC".equals(type) && y != 0 && x <= gridLayout.getColumns() - 2) {
								// 检验项需要输入值类型，需要进行比较
								String displayMessage = returnMessage.toString().trim().replace("酒吧", "98")
										.replace(":", ".").replace("吧", "8");
								if (RegExpValidatorUtils.isIsNumber(displayMessage)) {
									if (Double.parseDouble(displayMessage) >= minValue
											&& Double.parseDouble(displayMessage) <= maxValue) {
										getUI().accessSynchronously(new Runnable() {
											@Override
											public void run() {
												TextField component = (TextField) gridLayout.getComponent(x, y);
												component.setValue(displayMessage);
												returnMessage.delete(0, returnMessage.length());
												saveInspectionItem(x, y, true, purchasingOrderInfo);
											}
										});
										try {
											prefixSend = AppConstant.PREFIXPLAYTEXT;
											// 区分检验类型 -->单件;分组
											if (cbInspectionType.getValue().equals("单件")) {
												if (ifSingleInspection()) {
													// 表示单人检验
													if (y < gridLayout.getRows() - 1) {
														y++;
														inspectionItemName = ((Label) gridLayout.getComponent(1, y))
																.getValue();
														client.sendMessage("[PT]请输入检验项" + inspectionItemName + "的值");
													} else {
														if (x < gridLayout.getColumns() - 2) {
															y = 0;
															x++;
															client.sendMessage("[PT]请输入检测的第" + (x - 1) + "个SN序列号");
														} else {
															x++;
															y = 1;
															// prefixSend = AppConstant.INSPECTIONDONE;
															inspectionItemName = ((Label) gridLayout.getComponent(1, y))
																	.getValue();
															client.sendMessage(
																	"[PT]请输入检验项" + inspectionItemName + "的量具编号");
														}
													}
												} else {
													// 多人检验
													List<Integer> checkedIndexList = getCheckedItemsIndex();
													int checkedCount = checkedIndexList.size();
													// 获取选中的行的inspectionName
													if (checkedIndex < checkedCount) {
														inspectionItemName = ((Label) gridLayout.getComponent(1,
																checkedIndexList.get(checkedIndex))).getValue();
														if (x != gridLayout.getColumns() - 1) {
															client.sendMessage(
																	"[PT]请输入检验项" + inspectionItemName + "的值");
														} else {
															client.sendMessage(
																	"[PT]请输入检验项" + inspectionItemName + "的量具编号");
														}

														y = checkedIndexList.get(checkedIndex);
														checkedIndex++;
													} else {
														if (x < gridLayout.getColumns() - 2) {
															checkedIndex = 0;
															y = 0;
															x++;
															client.sendMessage("[PT]请输入检测的第" + (x - 1) + "个SN序列号");
														} else {
															x++;
															checkedIndex = 0;
															y = checkedIndexList.get(checkedIndex);
															inspectionItemName = ((Label) gridLayout.getComponent(1,
																	checkedIndexList.get(checkedIndex))).getValue();
															client.sendMessage(
																	"[PT]请输入检验项" + inspectionItemName + "的量具编号");
															// prefixSend = AppConstant.INSPECTIONDONE;
															// client.sendMessage("[PT]检验结束");
															checkedIndex++;
														}
													}
												}

											} else if (cbInspectionType.getValue().equals("分组")) {
												if (ifSingleInspection()) {
													// 单人检验
													if (x < gridLayout.getColumns() - 2) {
														// 如果在第一行，则播报输入SN
														if (y == 0) {
															x++;
															client.sendMessage("[PT]请输入检测的第" + (x - 1) + "个SN序列号");
														} else {
															x++;
															inspectionItemName = ((Label) gridLayout.getComponent(1, y))
																	.getValue();
															inspectionSn = ((TextField) gridLayout.getComponent(x, 0))
																	.getValue();
															if (x == 2) {
																client.sendMessage("[PT]请输入SN" + inspectionSn + "的检验项"
																		+ inspectionItemName + "的值");
															} else {
																client.sendMessage("[PT]请输入SN" + inspectionSn + "的检验值");
															}

														}
													} else {
														if (x == gridLayout.getColumns() - 2 && y != 0) {
															x++;
															inspectionItemName = ((Label) gridLayout.getComponent(1, y))
																	.getValue();
															client.sendMessage(
																	"[PT]请输入检验项" + inspectionItemName + "的量具编号");
														} else {
															if (y < gridLayout.getRows() - 1) {
																x = 2;
																y++;
																inspectionItemName = ((Label) gridLayout.getComponent(1,
																		y)).getValue();
																inspectionSn = ((TextField) gridLayout.getComponent(x,
																		0)).getValue();
																client.sendMessage("[PT]请输入SN" + inspectionSn + "的检验项"
																		+ inspectionItemName + "的值");
															} else {
																prefixSend = AppConstant.INSPECTIONDONE;
																client.sendMessage("[PT]检验结束");
															}
														}
													}
												} else {
													// 多人检验
													List<Integer> checkedIndexList = getCheckedItemsIndex();
													int checkedCount = checkedIndexList.size();
													if (x < gridLayout.getColumns() - 2) {
														if (y == 0) {
															x++;
															client.sendMessage("[PT]请输入检测的第" + (x - 1) + "个SN序列号");
														} else {
															x++;
															inspectionItemName = ((Label) gridLayout.getComponent(1, y))
																	.getValue();
															inspectionSn = ((TextField) gridLayout.getComponent(x, 0))
																	.getValue();
															if (x == 2) {
																client.sendMessage("[PT]请输入SN" + inspectionSn + "的检验项"
																		+ inspectionItemName + "的值");
															} else {
																client.sendMessage("[PT]请输入SN" + inspectionSn + "的检验值");
															}
														}
													} else {
														if (x == gridLayout.getColumns() - 2 && y != 0) {
															x++;
															inspectionItemName = ((Label) gridLayout.getComponent(1, y))
																	.getValue();
															client.sendMessage(
																	"[PT]请输入检验项" + inspectionItemName + "的量具编号");
														} else {
															if (checkedIndex < checkedCount) {
																x = 2;
																y = checkedIndexList.get(checkedIndex);
																inspectionItemName = ((Label) gridLayout.getComponent(1,
																		checkedIndexList.get(checkedIndex))).getValue();
																inspectionSn = ((TextField) gridLayout.getComponent(x,
																		0)).getValue();
																client.sendMessage("[PT]请输入SN" + inspectionSn + "的检验项"
																		+ inspectionItemName + "的值");
																checkedIndex++;
															} else {
																prefixSend = AppConstant.INSPECTIONDONE;
																client.sendMessage("[PT]检验结束");
															}
														}
													}
												}
											} else {
												// 无SN
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									} else {
										try {
											if (ensureMessage) {
												if (ifSave != null && ifSave.length() > 0
														&& "是".equals(ifSave.toString())) {
													getUI().accessSynchronously(new Runnable() {
														@Override
														public void run() {
															TextField component = (TextField) gridLayout.getComponent(x,
																	y);
															component.setValue(displayMessage);
															component.setStyleName(CoreTheme.BACKGROUND_RED);
															returnMessage.delete(0, returnMessage.length());
															ifSave.delete(0, ifSave.length());
															saveInspectionItem(x, y, false, purchasingOrderInfo);
															ensureMessage = false;
															pass = false;// 数据不在范围,却录入了
														}
													});
													prefixSend = AppConstant.PREFIXPLAYTEXT;
													// 区分检验类型 -->单件;分组
													if (cbInspectionType.getValue().equals("单件")) {
														if (ifSingleInspection()) {
															// 表示单人检验
															if (y < gridLayout.getRows() - 1) {
																y++;
																inspectionItemName = ((Label) gridLayout.getComponent(1,
																		y)).getValue();
																client.sendMessage(
																		"[PT]请输入检验项" + inspectionItemName + "的值");
															} else {
																if (x < gridLayout.getColumns() - 2) {
																	y = 0;
																	x++;
																	client.sendMessage(
																			"[PT]请输入检测的第" + (x - 1) + "个SN序列号");
																} else {
																	x++;
																	y = 1;
																	// prefixSend = AppConstant.INSPECTIONDONE;
																	inspectionItemName = ((Label) gridLayout
																			.getComponent(1, y)).getValue();
																	client.sendMessage("[PT]请输入检验项" + inspectionItemName
																			+ "的量具编号");
																}
															}
														} else {
															// 多人检验

															// 多人检验
															List<Integer> checkedIndexList = getCheckedItemsIndex();
															int checkedCount = checkedIndexList.size();
															// 获取选中的行的inspectionName
															if (checkedIndex < checkedCount) {
																inspectionItemName = ((Label) gridLayout.getComponent(1,
																		checkedIndexList.get(checkedIndex))).getValue();
																if (x != gridLayout.getColumns() - 1) {
																	client.sendMessage(
																			"[PT]请输入检验项" + inspectionItemName + "的值");
																} else {
																	client.sendMessage("[PT]请输入检验项" + inspectionItemName
																			+ "的量具编号");
																}

																y = checkedIndexList.get(checkedIndex);
																checkedIndex++;
															} else {
																if (x < gridLayout.getColumns() - 2) {
																	checkedIndex = 0;
																	y = 0;
																	x++;
																	client.sendMessage(
																			"[PT]请输入检测的第" + (x - 1) + "个SN序列号");
																} else {
																	x++;
																	checkedIndex = 0;
																	y = checkedIndexList.get(checkedIndex);
																	inspectionItemName = ((Label) gridLayout
																			.getComponent(1,
																					checkedIndexList.get(checkedIndex)))
																							.getValue();
																	client.sendMessage("[PT]请输入检验项" + inspectionItemName
																			+ "的量具编号");
																	// prefixSend = AppConstant.INSPECTIONDONE;
																	// client.sendMessage("[PT]检验结束");
																	checkedIndex++;
																}
															}
														}
													} else if (cbInspectionType.getValue().equals("分组")) {
														if (ifSingleInspection()) {
															// 单人检验
															if (x < gridLayout.getColumns() - 2) {
																// 如果在第一行，则播报输入SN
																if (y == 0) {
																	x++;
																	client.sendMessage(
																			"[PT]请输入检测的第" + (x - 1) + "个SN序列号");
																} else {
																	x++;
																	inspectionItemName = ((Label) gridLayout
																			.getComponent(1, y)).getValue();
																	inspectionSn = ((TextField) gridLayout
																			.getComponent(x, 0)).getValue();
																	if (x == 2) {
																		client.sendMessage("[PT]请输入SN" + inspectionSn
																				+ "的检验项" + inspectionItemName + "的值");
																	} else {
																		client.sendMessage(
																				"[PT]请输入SN" + inspectionSn + "的检验值");
																	}

																}
															} else {
																if (x == gridLayout.getColumns() - 2 && y != 0) {
																	x++;
																	inspectionItemName = ((Label) gridLayout
																			.getComponent(1, y)).getValue();
																	client.sendMessage("[PT]请输入检验项" + inspectionItemName
																			+ "的量具编号");
																} else {
																	if (y < gridLayout.getRows() - 1) {
																		x = 2;
																		y++;
																		inspectionItemName = ((Label) gridLayout
																				.getComponent(1, y)).getValue();
																		inspectionSn = ((TextField) gridLayout
																				.getComponent(x, 0)).getValue();
																		client.sendMessage("[PT]请输入SN" + inspectionSn
																				+ "的检验项" + inspectionItemName + "的值");
																	} else {
																		prefixSend = AppConstant.INSPECTIONDONE;
																		client.sendMessage("[PT]检验结束");
																	}
																}
															}
														} else {
															// 多人检验
															List<Integer> checkedIndexList = getCheckedItemsIndex();
															int checkedCount = checkedIndexList.size();
															if (x < gridLayout.getColumns() - 2) {
																if (y == 0) {
																	x++;
																	client.sendMessage(
																			"[PT]请输入检测的第" + (x - 1) + "个SN序列号");
																} else {
																	x++;
																	inspectionItemName = ((Label) gridLayout
																			.getComponent(1, y)).getValue();
																	inspectionSn = ((TextField) gridLayout
																			.getComponent(x, 0)).getValue();
																	if (x == 2) {
																		client.sendMessage("[PT]请输入SN" + inspectionSn
																				+ "的检验项" + inspectionItemName + "的值");
																	} else {
																		client.sendMessage(
																				"[PT]请输入SN" + inspectionSn + "的检验值");
																	}

																}
															} else {
																if (x == gridLayout.getColumns() - 2 && y != 0) {
																	x++;
																	inspectionItemName = ((Label) gridLayout
																			.getComponent(1, y)).getValue();
																	client.sendMessage("[PT]请输入检验项" + inspectionItemName
																			+ "的量具编号");
																} else {
																	if (checkedIndex < checkedCount) {
																		x = 2;
																		y = checkedIndexList.get(checkedIndex);
																		inspectionItemName = ((Label) gridLayout
																				.getComponent(1,
																						checkedIndexList
																								.get(checkedIndex)))
																										.getValue();
																		inspectionSn = ((TextField) gridLayout
																				.getComponent(x, 0)).getValue();
																		client.sendMessage("[PT]请输入SN" + inspectionSn
																				+ "的检验项" + inspectionItemName + "的值");
																		checkedIndex++;
																	} else {
																		prefixSend = AppConstant.INSPECTIONDONE;
																		client.sendMessage("[PT]检验结束");
																	}
																}
															}
														}
													} else {
														// 无SN
													}
												} else {
													prefixSend = AppConstant.PREFIXPLAYTEXT;
													client.sendMessage("[PT]重新输入检验项" + inspectionItemName + "的值");
													returnMessage.delete(0, returnMessage.length());
													ifSave.delete(0, ifSave.length());
													ensureMessage = false;
												}
											} else {
												ensureMessage = true;// 表示当前信息是确认信息
												prefixSend = AppConstant.PREFIXPLAYTEXT;
												client.sendMessage("[PT]检测项" + inspectionItemName + "的值不在标准范围，是否录入");
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								} else {
									try {
										prefixSend = AppConstant.PREFIXPLAYTEXT;
										client.sendMessage("[PT]输入数据" + returnMessage.toString() + "不是数字类型，请重新录入");
										returnMessage.delete(0, returnMessage.length());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							} else {
								getUI().accessSynchronously(new Runnable() {
									@Override
									public void run() {
										TextField component = (TextField) gridLayout.getComponent(x, y);
										String displayMessage = returnMessage.toString().trim().replace("酒吧", "98")
												.replace(":", ".").replace("吧", "8").toUpperCase(); 
										System.out.println(displayMessage);
										if (x == gridLayout.getColumns() - 1) {// 量具信息
											if (displayMessage.startsWith("SJ")) {
												component.setValue("SJQ-" + displayMessage.substring(3));
											} else {
												// String prefix =displayMessage.substring(0, 3).toUpperCase();
												// String subfix = displayMessage.substring(3).replace(":",
												// ".").replace("吧", "8");
												component.setValue("CSI-" + displayMessage);
											}
										} else {
											if (y == 0) {
												component.setValue(displayMessage);
											} else {
												if ("OK".equals(displayMessage)) {
													component.setValue(displayMessage);
												} else {
													component.setValue("NG");
													component.setStyleName(CoreTheme.BACKGROUND_RED);
												}
											}
										}
										returnMessage.delete(0, returnMessage.length());
										if (x != gridLayout.getColumns() - 1) {
											saveInspectionItem(x, y, true, purchasingOrderInfo);
										}
									}
								});
								// 播报下一个检验项
								prefixSend = AppConstant.PREFIXPLAYTEXT;
								try {
										// 区分检验类型 -->单件;分组
										if (cbInspectionType.getValue().equals("单件")) {
											if (ifSingleInspection()) {
												// 表示单人检验
												if (y < gridLayout.getRows() - 1) {
													y++;
													inspectionItemName = ((Label) gridLayout.getComponent(1, y))
															.getValue();
													if (x != gridLayout.getColumns() - 1) {
														client.sendMessage("[PT]请输入检验项" + inspectionItemName + "的值");
													} else {
														client.sendMessage("[PT]请输入检验项" + inspectionItemName + "的量具编号");
													}
												} else {
													if (x < gridLayout.getColumns() - 2) {
														y = 0;
														x++;
														client.sendMessage("[PT]请输入检测的第" + (x - 1) + "个SN序列号");
													} else {
														if (x != gridLayout.getColumns() - 1) {
															x++;
															y = 1;
															inspectionItemName = ((Label) gridLayout.getComponent(1, y))
																	.getValue();
															client.sendMessage(
																	"[PT]请输入检验项" + inspectionItemName + "的量具编号");
														} else {
															prefixSend = AppConstant.INSPECTIONDONE;
															client.sendMessage("[PT]检验结束");
														}

													}
												}
											} else {
												// 多人检验
												List<Integer> checkedIndexList = getCheckedItemsIndex();
												int checkedCount = checkedIndexList.size();
												// 获取选中的行的inspectionName
												if (checkedIndex < checkedCount) {
													inspectionItemName = ((Label) gridLayout.getComponent(1,
															checkedIndexList.get(checkedIndex))).getValue();
													y = checkedIndexList.get(checkedIndex);
													checkedIndex++;
													if (x != gridLayout.getColumns() - 1) {
														client.sendMessage("[PT]请输入检验项" + inspectionItemName + "的值");
													} else {
														client.sendMessage("[PT]请输入检验项" + inspectionItemName + "的量具编号");
													}
												} else {
													if (x < gridLayout.getColumns() - 2) {
														y = 0;
														checkedIndex = 0;
														x++;
														client.sendMessage("[PT]请输入检测的第" + (x - 1) + "个SN序列号");
													} else {
														if (x != gridLayout.getColumns() - 1) {
															checkedIndex = 0;
															x++;
															y = checkedIndexList.get(checkedIndex);
															inspectionItemName = ((Label) gridLayout.getComponent(1,
																	checkedIndexList.get(checkedIndex))).getValue();
															client.sendMessage(
																	"[PT]请输入检验项" + inspectionItemName + "的量具编号");
															checkedIndex++;
														} else {
															prefixSend = AppConstant.INSPECTIONDONE;
															client.sendMessage("[PT]检验结束");
														}
													}
												}
											}

										} else if (cbInspectionType.getValue().equals("分组")) {
											if (ifSingleInspection()) {
												// 单人检验
												if (x < gridLayout.getColumns() - 2) {
													// 如果在第一行，则播报输入SN
													if (y == 0) {
														x++;
														client.sendMessage("[PT]请输入检测的第" + (x - 1) + "个SN序列号");
													} else {
														x++;
														inspectionItemName = ((Label) gridLayout.getComponent(1, y))
																.getValue();
														inspectionSn = ((TextField) gridLayout.getComponent(x, 0))
																.getValue();
														if (x == 2) {
															client.sendMessage("[PT]请输入SN" + inspectionSn + "的检验项"
																	+ inspectionItemName + "的值");
														} else {
															client.sendMessage("[PT]请输入SN" + inspectionSn + "的检验值");
														}

													}
												} else {
													if (x == gridLayout.getColumns() - 2 && y != 0) {
														x++;
														inspectionItemName = ((Label) gridLayout.getComponent(1, y))
																.getValue();
														client.sendMessage("[PT]请输入检验项" + inspectionItemName + "的量具编号");
													} else {
														if (y < gridLayout.getRows() - 1) {
															x = 2;
															y++;
															inspectionItemName = ((Label) gridLayout.getComponent(1, y))
																	.getValue();
															inspectionSn = ((TextField) gridLayout.getComponent(x, 0))
																	.getValue();
															client.sendMessage("[PT]请输入SN" + inspectionSn + "的检验项"
																	+ inspectionItemName + "的值");
														} else {
															prefixSend = AppConstant.INSPECTIONDONE;
															client.sendMessage("[PT]检验结束");
														}
													}
												}
											} else {
												// 多人检验
												List<Integer> checkedIndexList = getCheckedItemsIndex();
												int checkedCount = checkedIndexList.size();
												if (x < gridLayout.getColumns() - 2) {
													if (y == 0) {
														x++;
														client.sendMessage("[PT]请输入检测的第" + (x - 1) + "个SN序列号");
													} else {
														x++;
														inspectionItemName = ((Label) gridLayout.getComponent(1, y))
																.getValue();
														inspectionSn = ((TextField) gridLayout.getComponent(x, 0))
																.getValue();
														if (x == 2) {
															client.sendMessage("[PT]请输入SN" + inspectionSn + "的检验项"
																	+ inspectionItemName + "的值");
														} else {
															client.sendMessage("[PT]请输入SN" + inspectionSn + "的检验值");
														}
													}
												} else {
													if (x == gridLayout.getColumns() - 2 && y != 0) {
														x++;
														inspectionItemName = ((Label) gridLayout.getComponent(1, y))
																.getValue();
														client.sendMessage("[PT]请输入检验项" + inspectionItemName + "的量具编号");
													} else {
														if (checkedIndex < checkedCount) {
															x = 2;
															y = checkedIndexList.get(checkedIndex);
															inspectionItemName = ((Label) gridLayout.getComponent(1,
																	checkedIndexList.get(checkedIndex))).getValue();
															inspectionSn = ((TextField) gridLayout.getComponent(x, 0))
																	.getValue();
															client.sendMessage("[PT]请输入SN" + inspectionSn + "的检验项"
																	+ inspectionItemName + "的值");
															checkedIndex++;
														} else {
															prefixSend = AppConstant.INSPECTIONDONE;
															client.sendMessage("[PT]检验结束");
														}
													}
												}
											}
										} else {
											// 无SN
										}
									
									reSpeak = false;
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					} else if (message.startsWith(AppConstant.PALYEXCEPTION)) {
						System.out.println("语音服务网络断开，正尝试重新连接");
						prefixSend = AppConstant.PREFIXPLAYTEXT;
						try {
							if (y == 0) {
								// sn序列号
								client.sendMessage("[PT]请输入检测的第" + (x - 1) + "个SN序列号");
							} else {
								// 检验项
								if (x == gridLayout.getColumns() - 1) {
									inspectionItemName = ((Label) gridLayout.getComponent(1, y)).getValue();
									client.sendMessage("[PT]请输入检验项" + inspectionItemName + "的量具编号");
								} else {
									inspectionItemName = ((Label) gridLayout.getComponent(1, y)).getValue();
									client.sendMessage("[PT]请输入检验项" + inspectionItemName + "的值");
								}
							}
						} catch (Exception e) {
							NotificationUtils.notificationError("Socket连接中断");
						}
					}
				});
				prefixSend = AppConstant.PREFIXPLAYTEXT;
				if (x == gridLayout.getColumns() - 1) {
					String item = ((Label) gridLayout.getComponent(1, y)).getValue();
					client.sendMessage("[PT]请输入检验项" + item + "量具编号");
				} else {
					if (y == 0) {
						client.sendMessage("[PT]请输入检测的第" + (x - 1) + "个SN序列号");
					} else {
						String item = ((Label) gridLayout.getComponent(1, y)).getValue();
						String sn = ((TextField) gridLayout.getComponent(x, 0)).getValue();
						client.sendMessage("[PT]请输入SN" + sn + "的检验项" + item + "的值");
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setDataToDisplayArea(PurchasingOrderInfo purchasingOrderInfo) {
		if (purchasingOrderInfo == null) {
			// 清空显示区的信息
			lblMaterialNo.clear();
			lblMaterialRev.clear();
			lblMaterialDesc.clear();
			lblVendor.clear();
			lblQualityPlan.clear();
			lblDrawingNo.clear();
		} else {
			// 填充信息
			String materialNo = purchasingOrderInfo.getMaterialNo();
			String materialRev = purchasingOrderInfo.getMaterialRev();
			String materialDesc = purchasingOrderInfo.getMaterialDesc();
			String vendor = purchasingOrderInfo.getVendorName();
			String quality = "";
			String drawingNo = "";
			// 获取零件信息，并得到质量计划和图纸编号
			SparePart part = sparePartService.getByNoRev(materialNo, materialRev);
			if (part != null) {
				quality = part.getQaPlan() + "/" + part.getQaPlanRev();
				drawingNo = part.getDrawNo();
			}

			lblMaterialNo.setValue(materialNo);
			lblMaterialRev.setValue(materialRev);
			lblMaterialDesc.setValue(materialDesc);
			lblVendor.setValue(vendor);
			lblQualityPlan.setValue(quality);
			lblDrawingNo.setValue(drawingNo);
		}
	}

	// 全选或者全不选时，表示一个人检验，选择部分检验项时，表示多人检验(2人)
	public boolean ifSingleInspection() {
		int rowCount = gridLayout.getRows();
		int checked = 0;
		int unChecked = 0;
		for (int index = 1; index < rowCount; index++) {
			CheckBox cbInstance = (CheckBox) gridLayout.getComponent(0, index);
			if (cbInstance.getValue()) {
				checked++;
			} else {
				unChecked++;
			}
		}

		if (checked > 0 && unChecked > 0) {
			return false;
		} else {
			return true;
		}
	}

	// 在多人检验的情况下，获取检验项index
	public List<Integer> getCheckedItemsIndex() {
		int rows = gridLayout.getRows();
		List<Integer> indexList = new ArrayList<>();
		for (int index = 1; index < rows; index++) {
			CheckBox cbInstance = (CheckBox) gridLayout.getComponent(0, index);
			if (cbInstance.getValue()) {
				indexList.add(index);
			}
		}
		return indexList;
	}

	public void saveInspectionItem(int columnIndex, int rowIndex, boolean pass, PurchasingOrderInfo orderInfo) {
		if (y == 0) {
			return;
		}
		String sn = ((TextField) gridLayout.getComponent(columnIndex, 0)).getValue();
		String itemName = ((Label) gridLayout.getComponent(1, rowIndex)).getValue();
		String itemValue = ((TextField) gridLayout.getComponent(columnIndex, rowIndex)).getValue();
		int columnCount = gridLayout.getColumns();
		gageInfo = ((TextField) gridLayout.getComponent(columnCount - 1, rowIndex)).getValue();
		DimensionInspectionResult result = new DimensionInspectionResult();

		result.setGageInfo(gageInfo);
		result.setInspectionValue(itemValue);
		result.setMaterialNo(orderInfo.getMaterialNo());
		result.setMaterialRev(orderInfo.getMaterialRev());
		result.setOrderItem(orderInfo.getPurchasingItemNo());
		result.setPurchasingNo(orderInfo.getPurchasingNo());
		result.setSapInspectionNo(orderInfo.getSapInspectionLot());
		String serialNo = orderInfo.getPurchasingNo() + "-" + orderInfo.getPurchasingItemNo() + "-" + sn;
		result.setMaterialSN(serialNo);
		result.setInspectionName(itemName);
		result.setIsPass(pass);

		dimensionInspectionService.save(result);
	}

	public void createReport(String path) {
		// 由于尺寸检验的检验数量个检验项都是不可固定的，都有可能超出一页纸范围的可能，这里采用固定列(SN数量)的方式，来动态加载检验项，超过一页的继续往下一页加载，如果检验数量超过一页的范围，则需要在生成一份word保存检验结果
		int dataColumn = gridLayout.getColumns() - 3;// 实际检验数量
		int pageNum = (int) Math.ceil((float) dataColumn / 10);// 实际的检验数量需要几页纸可以加载数据
		path = path + AppConstant.MATERIAL_PREFIX + AppConstant.DIMENSION_REPORT;
		List<File> fileList = new ArrayList<>();
		for (int index = 0; index < pageNum; index++) {
			try {
				Map<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("partNo", purchasingOrderInfo.getMaterialNo());
				dataMap.put("Rev", purchasingOrderInfo.getMaterialRev());
				dataMap.put("purchasingOrder",
						purchasingOrderInfo.getPurchasingNo() + "-" + purchasingOrderInfo.getPurchasingItemNo());
				dataMap.put("vendor", purchasingOrderInfo.getVendorName());
				dataMap.put("description", purchasingOrderInfo.getMaterialDesc().replaceAll("&", "&amp;")
						.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
				dataMap.put("qpRev", lblQualityPlan.getValue());
				dataMap.put("qty", purchasingOrderInfo.getMaterialQuantity());
				dataMap.put("serialNos", tfSerialNos.getValue());
				dataMap.put("drawingNo", lblDrawingNo.getValue());
				// 循环写入SN
				// List<Map<String, String>> sList = new ArrayList<Map<String, String>>();
				for (int snIndex = 2; snIndex < 12; snIndex++) {
					// 加载SN
					int colIndex = 10 * index + snIndex;
					if (colIndex < gridLayout.getColumns() - 1) {
						TextField tfSn = (TextField) gridLayout.getComponent(10 * index + snIndex, 0);
						String snValue = tfSn.getValue() == null ? "" : tfSn.getValue();
						dataMap.put("SN" + (snIndex - 1), snValue);
					} else {
						dataMap.put("SN" + (snIndex - 1), "");
					}

				}

				// 循环写入值，以行为单位
				List<Map<String, String>> sList = new ArrayList<Map<String, String>>();
				for (int rowIndex = 1; rowIndex < gridLayout.getRows(); rowIndex++) {
					Map<String, String> sMap = new HashMap<>();
					Label lbl = (Label) gridLayout.getComponent(1, rowIndex);
					String item = lbl.getValue();
					sMap.put("item", item);
					// 获取当前检验项的检验人员信息
					String userName = dimensionInspectionService.getInspector(purchasingOrderInfo.getSapInspectionLot(),
							item);
					// 循环当前行的每一列
					for (int valueIndex = 2; valueIndex < 12; valueIndex++) {
						int colIndex = 10 * index + valueIndex;
						if (colIndex < gridLayout.getColumns() - 1) {
							TextField tfValue = (TextField) gridLayout.getComponent(10 * index + valueIndex, rowIndex);
							String value = tfValue.getValue() == null ? "" : tfValue.getValue();
							sMap.put("value" + (valueIndex - 1), value);
						} else {
							sMap.put("value" + (valueIndex - 1), "");
						}

					}
					// 量具信息
					TextField tfGauge = (TextField) gridLayout.getComponent(gridLayout.getColumns() - 1, rowIndex);
					String gaugeValue = tfGauge.getValue() == null ? "" : tfGauge.getValue();
					sMap.put("gague", gaugeValue);
					BASE64Encoder encoder = new BASE64Encoder();
					Media mediaImage = caMediaService.getMediaByName(userName);

					sMap.put("signature", encoder.encode(inputStream2byte(mediaImage.getMediaStream())));
					sMap.put("date", new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
					sList.add(sMap);

				}
				dataMap.put("slist", sList);

				Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
				configuration.setDefaultEncoding("utf-8");
				configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

				String jPath = AppConstant.DOC_XML_FILE_PATH;
				configuration.setDirectoryForTemplateLoading(new File(jPath));
				// 以utf-8的编码读取模板文件
				Template template = configuration.getTemplate("dimension.xml", "utf-8");

				// 输出文件
				String fileName = path + (index + 1) + ".doc";
				File outFile = new File(fileName);
				// 将模板和数据模型合并生成文件
				Writer out = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(outFile), Charset.forName("utf-8")), 1024 * 1024);
				template.process(dataMap, out);
				out.flush();
				out.close();
				fileList.add(outFile);
				System.out.println("*********成功生成尺寸检验报告***********");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 合并文档
		int fileCount = fileList.size();
		String fileName = fileList.get(0).getAbsolutePath();
		Document document = new Document(fileName, FileFormat.Doc);

		for (int index = 1; index < fileCount; index++) {
			if (fileCount > 1) {
				// document.addSection().addParagraph();
				document.insertTextFromFile(fileList.get(index).getAbsolutePath(), FileFormat.Doc);
				// fileList.get(index).delete();
			}
		}
		document.saveToFile(path + purchasingOrderInfo.getPurchasingNo() + "-"
				+ purchasingOrderInfo.getPurchasingItemNo() + "-" + purchasingOrderInfo.getSapInspectionLot() + ".doc",
				FileFormat.Doc);
		deleteFiles(fileList);
	}

	private byte[] inputStream2byte(InputStream inputStream) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, len);
		}
		return outputStream.toByteArray();
	}

	// 点击确认按钮,保存数据
	public boolean saveData() {
		// 将pass置为true
		pass = true;
		// 删除已有数据
		List<DimensionInspectionResult> list = new ArrayList<>();
		int rowCount = gridLayout.getRows();
		int columnCount = gridLayout.getColumns();
		String sapLot = purchasingOrderInfo.getSapInspectionLot();
		String snPrefix = purchasingOrderInfo.getPurchasingNo() + "-" + purchasingOrderInfo.getPurchasingItemNo();
		// 淇濆瓨鏁版嵁
		for (int rowIndex = 1; rowIndex < rowCount; rowIndex++) {
			inspectionItemName = ((Label) gridLayout.getComponent(1, rowIndex)).getValue();
			gageInfo = ((TextField) gridLayout.getComponent(columnCount - 1, rowIndex)).getValue();
			for (int columnIndex = 2; columnIndex < columnCount - 1; columnIndex++) {
				DimensionInspectionResult dimensionInspectionResult = new DimensionInspectionResult();
				String sn = ((TextField) gridLayout.getComponent(columnIndex, 0)).getValue();
				String value = ((TextField) gridLayout.getComponent(columnIndex, rowIndex)).getValue();
				if (Strings.isNullOrEmpty(value) || Strings.isNullOrEmpty(sn)) {
					break;
				}
				DimensionInspectionResult instance = dimensionInspectionService.getCheckedItem(sapLot,
						snPrefix + "-" + sn, inspectionItemName);
				if (instance != null) {
					dimensionInspectionResult = instance;
					if (Strings.isNullOrEmpty(dimensionInspectionResult.getGageInfo())) {
						dimensionInspectionResult.setGageInfo(gageInfo);
						list.add(dimensionInspectionResult);
					}
				} else {
					dimensionInspectionResult = new DimensionInspectionResult();
					dimensionInspectionResult.setInspectionValue(value);
					dimensionInspectionResult.setMaterialNo(materialNo);
					dimensionInspectionResult.setMaterialRev(materialRev);
					dimensionInspectionResult.setOrderItem(purchasingOrderInfo.getPurchasingItemNo());
					dimensionInspectionResult.setPurchasingNo(purchasingOrderInfo.getPurchasingNo());
					dimensionInspectionResult.setSapInspectionNo(purchasingOrderInfo.getSapInspectionLot());
					dimensionInspectionResult.setMaterialSN(snPrefix + "-" + sn);
					dimensionInspectionResult.setInspectionName(inspectionItemName);
					Object[] objStr = checkItemPass(materialNo, materialRev, inspectionItemName, value);
					if (objStr.length == 1) {
						dimensionInspectionResult.setIsPass((boolean) objStr[0]);
					} else {
						NotificationUtils.notificationError((String) objStr[1]);
						return false;
					}
					dimensionInspectionResult.setGageInfo(gageInfo);
					list.add(dimensionInspectionResult);
				}
			}
		}
		dimensionInspectionService.saveAll(list);
		return true;
	}

	public Object[] checkItemPass(String materialNo, String materialRev, String itemName, String value) {
		DimensionRuler ruler = dimensionRulerService.getByNoRevItemName(materialNo, materialRev, inspectionItemName);
		String itemType = ruler.getInspectionItemType();
		if (Strings.isNullOrEmpty(value)) {
			return new Object[] { false, "检验值不能为空" };
		}
		if ("NUMBRIC".equals(itemType)) {
			if (!RegExpValidatorUtils.isIsNumber(value)) {
				return new Object[] { false, "数字型检验项的值必须为数字" };
			}
			double floatValue = Double.parseDouble(value);
			double maxValue = ruler.getMaxValue();
			double minValue = ruler.getMinValue();
			if (floatValue >= minValue && floatValue <= maxValue) {
				{
					return new Object[] { true };
				}
			} else {
				pass = false;
				return new Object[] { false };
			}
		} else {
			if ("OK".equalsIgnoreCase(value)) {
				return new Object[] { true };
			} else {
				pass = false;
				return new Object[] { false };
			}
		}
	}

	public void deleteFiles(List<File> list) {
		for (int i = 0; i < list.size(); i++) {
			list.get(i).delete();
		}
	}

	// 针对多人检验的情况
	public void createReportPartical(String path) {
		// 由于尺寸检验的检验数量个检验项都是不可固定的，都有可能超出一页纸范围的可能，这里采用固定列(SN数量)的方式，来动态加载检验项，超过一页的继续往下一页加载，如果检验数量超过一页的范围，则需要在生成一份word保存检验结果
		int dataColumn = gridLayout.getColumns() - 3;// 实际检验数量
		int pageNum = (int) Math.ceil((float) dataColumn / 10);// 实际的检验数量需要几页纸可以加载数据
		path = path + AppConstant.MATERIAL_PREFIX + AppConstant.DIMENSION_REPORT;
		List<File> fileList = new ArrayList<>();
		for (int index = 0; index < pageNum; index++) {
			try {
				Map<String, Object> dataMap = new HashMap<String, Object>();
				dataMap.put("partNo", purchasingOrderInfo.getMaterialNo());
				dataMap.put("Rev", purchasingOrderInfo.getMaterialRev());
				dataMap.put("purchasingOrder",
						purchasingOrderInfo.getPurchasingNo() + "-" + purchasingOrderInfo.getPurchasingItemNo());
				dataMap.put("vendor", purchasingOrderInfo.getVendorName());
				dataMap.put("description", purchasingOrderInfo.getMaterialDesc().replaceAll("&", "&amp;")
						.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
				dataMap.put("qpRev", lblQualityPlan.getValue());
				dataMap.put("qty", purchasingOrderInfo.getMaterialQuantity());
				dataMap.put("serialNos", tfSerialNos.getValue());
				dataMap.put("drawingNo", lblDrawingNo.getValue());
				// 循环写入SN
				// List<Map<String, String>> sList = new ArrayList<Map<String, String>>();
				for (int snIndex = 2; snIndex < 12; snIndex++) {
					// 加载SN
					int colIndex = 10 * index + snIndex;
					if (colIndex < gridLayout.getColumns() - 1) {
						TextField tfSn = (TextField) gridLayout.getComponent(10 * index + snIndex, 0);
						String snValue = tfSn.getValue() == null ? "" : tfSn.getValue();
						dataMap.put("SN" + (snIndex - 1), snValue);
					} else {
						dataMap.put("SN" + (snIndex - 1), "");
					}

				}

				// 循环写入值，以行为单位
				List<Map<String, String>> sList = new ArrayList<Map<String, String>>();
				for (int rowIndex = 1; rowIndex < gridLayout.getRows(); rowIndex++) {
					CheckBox checkbox = (CheckBox) gridLayout.getComponent(0, rowIndex);
					if (!checkbox.getValue()) {
						continue;
					}
					Map<String, String> sMap = new HashMap<>();
					Label lbl = (Label) gridLayout.getComponent(1, rowIndex);
					String item = lbl.getValue();
					sMap.put("item", item);
					// 循环当前行的每一列
					for (int valueIndex = 2; valueIndex < 12; valueIndex++) {
						int colIndex = 10 * index + valueIndex;
						if (colIndex < gridLayout.getColumns() - 1) {
							TextField tfValue = (TextField) gridLayout.getComponent(10 * index + valueIndex, rowIndex);
							String value = tfValue.getValue() == null ? "" : tfValue.getValue();
							sMap.put("value" + (valueIndex - 1), value);
						} else {
							sMap.put("value" + (valueIndex - 1), "");
						}

					}
					// 量具信息
					TextField tfGauge = (TextField) gridLayout.getComponent(gridLayout.getColumns() - 1, rowIndex);
					String gaugeValue = tfGauge.getValue() == null ? "" : tfGauge.getValue();
					sMap.put("gague", gaugeValue);
					BASE64Encoder encoder = new BASE64Encoder();
					Media mediaImage = caMediaService.getMediaByName(RequestInfo.current().getUserName());
					sMap.put("signature", encoder.encode(inputStream2byte(mediaImage.getMediaStream())));
					sList.add(sMap);

				}
				dataMap.put("slist", sList);
				dataMap.put("date", new SimpleDateFormat("yyyy/MM/dd").format(new Date()));

				Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
				configuration.setDefaultEncoding("utf-8");
				configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);

				String jPath = AppConstant.DOC_XML_FILE_PATH;
				configuration.setDirectoryForTemplateLoading(new File(jPath));
				// 以utf-8的编码读取模板文件
				Template template = configuration.getTemplate("dimension.xml", "utf-8");

				// 输出文件
				String fileName = path + (index + 1) + ".doc";
				File outFile = new File(fileName);
				// 将模板和数据模型合并生成文件
				Writer out = new BufferedWriter(
						new OutputStreamWriter(new FileOutputStream(outFile), Charset.forName("utf-8")), 1024 * 1024);
				template.process(dataMap, out);
				out.flush();
				out.close();
				fileList.add(outFile);
				System.out.println("*********成功生成尺寸检验报告***********");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 合并文档
		int fileCount = fileList.size();
		String fileName = fileList.get(0).getAbsolutePath();
		Document document = new Document(fileName, FileFormat.Doc);

		for (int index = 1; index < fileCount; index++) {
			if (fileCount > 1) {
				// document.addSection().addParagraph();
				document.insertTextFromFile(fileList.get(index).getAbsolutePath(), FileFormat.Doc);
				// fileList.get(index).delete();
			}
		}
		String docPath = path + purchasingOrderInfo.getPurchasingNo() + "-" + purchasingOrderInfo.getPurchasingItemNo()
				+ "-" + purchasingOrderInfo.getSapInspectionLot() + "-01" + ".doc";
		if (!new File(docPath).exists()) {
			document.saveToFile(docPath, FileFormat.Doc);
		} else {
			docPath = path + purchasingOrderInfo.getPurchasingNo() + "-" + purchasingOrderInfo.getPurchasingItemNo()
					+ "-" + purchasingOrderInfo.getSapInspectionLot() + "-02" + ".doc";
			document.saveToFile(docPath, FileFormat.Doc);
		}
		deleteFiles(fileList);
	}

	public boolean saveDataPartical() {
		// 将pass置为true
		pass = true;
		// 删除已有数据
		List<DimensionInspectionResult> list = new ArrayList<>();
		int rowCount = gridLayout.getRows();
		int columnCount = gridLayout.getColumns();
		String sapLot = purchasingOrderInfo.getSapInspectionLot();
		String snPrefix = purchasingOrderInfo.getPurchasingNo() + "-" + purchasingOrderInfo.getPurchasingItemNo();
		// 淇濆瓨鏁版嵁
		for (int rowIndex = 1; rowIndex < rowCount; rowIndex++) {
			CheckBox checkBox = ((CheckBox) gridLayout.getComponent(0, rowIndex));
			if (!checkBox.getValue()) {
				continue;
			}
			inspectionItemName = ((Label) gridLayout.getComponent(1, rowIndex)).getValue();
			gageInfo = ((TextField) gridLayout.getComponent(columnCount - 1, rowIndex)).getValue();
			for (int columnIndex = 2; columnIndex < columnCount - 1; columnIndex++) {
				DimensionInspectionResult dimensionInspectionResult = new DimensionInspectionResult();
				String sn = ((TextField) gridLayout.getComponent(columnIndex, 0)).getValue();
				String value = ((TextField) gridLayout.getComponent(columnIndex, rowIndex)).getValue();
				if (Strings.isNullOrEmpty(value)) {
					break;
				}
				DimensionInspectionResult instance = dimensionInspectionService.getCheckedItem(sapLot,
						snPrefix + "-" + sn, inspectionItemName);
				if (instance != null) {
					dimensionInspectionResult = instance;
					if (Strings.isNullOrEmpty(dimensionInspectionResult.getGageInfo())) {
						dimensionInspectionResult.setGageInfo(gageInfo);
						list.add(dimensionInspectionResult);
					}
				} else {
					dimensionInspectionResult = new DimensionInspectionResult();
					dimensionInspectionResult.setInspectionValue(value);
					dimensionInspectionResult.setMaterialNo(materialNo);
					dimensionInspectionResult.setMaterialRev(materialRev);
					dimensionInspectionResult.setOrderItem(purchasingOrderInfo.getPurchasingItemNo());
					dimensionInspectionResult.setPurchasingNo(purchasingOrderInfo.getPurchasingNo());
					dimensionInspectionResult.setSapInspectionNo(purchasingOrderInfo.getSapInspectionLot());
					dimensionInspectionResult.setMaterialSN(snPrefix + "-" + sn);
					dimensionInspectionResult.setInspectionName(inspectionItemName);
					Object[] objStr = checkItemPass(materialNo, materialRev, inspectionItemName, value);
					if (objStr.length == 1) {
						dimensionInspectionResult.setIsPass((boolean) objStr[0]);
					} else {
						NotificationUtils.notificationError((String) objStr[1]);
						return false;
					}
					dimensionInspectionResult.setGageInfo(gageInfo);
					list.add(dimensionInspectionResult);
				}
			}
		}
		dimensionInspectionService.saveAll(list);
		return true;
	}

	public List<String> getPurchasingOrder(String type) {
		return purchasingOrderService.getPurchasingNo(type);
	}

	@Override
	public void _init() {
		cbPurchasingNo.setItems(getPurchasingOrder("DIMENSION"));
	}

	public int[] getStartXY() {
		if (ifSingleInspection()) {
			if ("分组".equals(cbInspectionType.getValue())) {
				int length = purchasingOrderInfo.getCheckedSn().split(",").length;
				if( length< gridLayout.getColumns() - 3) {
					return  new int[] { length + 2, 0 };
				}else {
					for (int rowIndex = 1; rowIndex < gridLayout.getRows(); rowIndex++) {
						for (int columnIndex = 2; columnIndex < gridLayout.getColumns(); columnIndex++) {
							String value = ((TextField) gridLayout.getComponent(columnIndex, rowIndex)).getValue();
							if (Strings.isNullOrEmpty(value)) {
								return new int[] { columnIndex, rowIndex };
							}
						}
					}
				}
			} else {
				for (int columnIndex = 2; columnIndex < gridLayout.getColumns(); columnIndex++) {
					for (int rowIndex = 0; rowIndex < gridLayout.getRows(); rowIndex++) {
						if (rowIndex == 0 && columnIndex == gridLayout.getColumns() - 1) {
							continue;
						}
						String value = ((TextField) gridLayout.getComponent(columnIndex, rowIndex)).getValue();
						if (Strings.isNullOrEmpty(value)) {
							return new int[] { columnIndex, rowIndex };
						}
					}
				}
			}
		} else {
			List<Integer> indexList = getCheckedItemsIndex();
			if ("分组".equals(cbInspectionType.getValue())) {
				int length = purchasingOrderInfo.getCheckedSn().split(",").length;
				if( length< gridLayout.getColumns() - 3) {
					return  new int[] { length + 2, 0 };
				}else {
					int firstIndex = indexList.get(0);
					for (int columnIndex = 2; columnIndex < gridLayout.getColumns(); columnIndex++) {
						String value = ((TextField) gridLayout.getComponent(columnIndex, firstIndex)).getValue();
						if (Strings.isNullOrEmpty(value)) {
							return new int[] { columnIndex, firstIndex };
						}
					}
				}
			} else {
				for (int columnIndex = 2; columnIndex < gridLayout.getColumns(); columnIndex++) {
					for (int rowIndex = 0; rowIndex < indexList.size(); rowIndex++) {
						String value = ((TextField) gridLayout.getComponent(columnIndex, indexList.get(rowIndex)))
								.getValue();
						if (Strings.isNullOrEmpty(value)) {
							if (rowIndex == 0) {
								value = ((TextField) gridLayout.getComponent(columnIndex, 0)).getValue();// 获取sn列的值
								if (Strings.isNullOrEmpty(value)) {
									return new int[] { columnIndex, 0 };
								}
							}
							return new int[] { columnIndex, indexList.get(rowIndex) };
						}
					}
				}
			}
		}
		return new int[] { 2, 0 };
	}

	public String checkedSn() {
		for(int columnIndex = 2 ; columnIndex < gridLayout.getColumns() - 1;columnIndex ++) {
			TextField tfsn = (TextField) gridLayout.getComponent(columnIndex, 0);
			String sn = tfsn.getValue();
			if(!Strings.isNullOrEmpty(sn)) {
				checkedSn = checkedSn + sn + ",";
			}else {
				break;
			}
		}
		return checkedSn.substring(0,checkedSn.length() - 1);
	}
	
	public boolean inspectionDone() {
		boolean flag = true;
		for (int k = 1; k < gridLayout.getRows(); k++) {
			for (int j = 2; j < gridLayout.getColumns(); j++) {
				Component c = gridLayout.getComponent(j, k);
				if (c instanceof TextField) {
					if (Strings.isNullOrEmpty(((TextField) c).getValue())) {
						flag = false;
						break;
					}
				}
			}
			if (!flag) {
				break;
			}
		}
		return flag;
	}
}
