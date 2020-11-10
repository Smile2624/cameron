package com.ags.lumosframework.ui.view.inspectionqc;

import com.ags.lumosframework.pojo.FunctionDetectionResult;
import com.ags.lumosframework.pojo.ProductionOrder;
import com.ags.lumosframework.sdk.RequestInfo;
import com.ags.lumosframework.sdk.domain.Role;
import com.ags.lumosframework.sdk.service.UserService;
import com.ags.lumosframework.sdk.service.api.IRoleService;
import com.ags.lumosframework.service.IFunctionDetectionResultService;
import com.ags.lumosframework.ui.constant.AppConstant;
import com.ags.lumosframework.ui.util.RegExpValidatorUtils;
import com.ags.lumosframework.ui.util.SocketClient;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.base.BaseDialog;
import com.ags.lumosframework.web.vaadin.base.DialogCallBack;
import com.ags.lumosframework.web.vaadin.base.NotificationUtils;
import com.ags.lumosframework.web.vaadin.constants.VaadinCommonConstant;
import com.google.common.base.Strings;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.List;

@SpringComponent
@Scope("prototype")
public class FunctionInspectionDialog extends BaseDialog  implements Button.ClickListener{

	private static final long serialVersionUID = -5000631064579454600L;

	private GridLayout gridLayout = new GridLayout(9,2);
	
	@I18Support(caption = "Start", captionKey = "common.start")
	private Button btnStart = new Button();
	
	private TextField tfGageNo = new TextField();
	private TextField tfRuler= new TextField();
	private TextField tfOrderSN1 = new TextField();
	private TextField tfOrderSN2 = new TextField();
	private TextField tfOrderSN3 = new TextField();
	private TextField tfOrderSN4 = new TextField();
	private TextField tfOrderSN5 = new TextField();
	private TextField tfOrderSN6 = new TextField();
	private TextField tfOrderSN7 = new TextField();
	private TextField tfOrderSN8 = new TextField();
	private ComboBox<String> cbResult1 = new ComboBox<String>();
	private ComboBox<String> cbResult2 = new ComboBox<String>();
	private ComboBox<String> cbResult3 = new ComboBox<String>();
	private ComboBox<String> cbResult4 = new ComboBox<String>();
	private ComboBox<String> cbResult5 = new ComboBox<String>();
	private ComboBox<String> cbResult6 = new ComboBox<String>();
	private ComboBox<String> cbResult7 = new ComboBox<String>();
	private ComboBox<String> cbResult8 = new ComboBox<String>();
	
	private String orderNo;
	
	private boolean functionInspectionResult;//功能检查结果
	
	private List<FunctionDetectionResult> functionDetectionResultList;
	
	@Autowired
	private IFunctionDetectionResultService functionDetectionResultService;
	
	TextField[] textFields = new TextField[] {tfOrderSN1,tfOrderSN2,tfOrderSN3,tfOrderSN4,tfOrderSN5,tfOrderSN6,tfOrderSN7,tfOrderSN8};
	ComboBox[] fields = new ComboBox[] {cbResult1,cbResult2,cbResult3,cbResult4,cbResult5,cbResult6,cbResult7,cbResult8};
	String caption = "功能检验结果输入";
	
	private String loginUserName = "";
	List<Role> role = null;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private IRoleService roleService;
	
	// x,y 当前数据输入位置
	private int x = 0;
	private int y = 0;
	
	private String ipAddress = "";
	private String prefixSend = "";// 当前发送的语音指令，用于判断返回信息并执行什么操作
	
	StringBuilder returnMessage = new StringBuilder();
	SocketClient client = null;
	
	boolean isInputOk = false;
	String errorMessage = "";
	
	String[] checkSnArr = new String[8];
	
	@Override
	public void show(UI parentUI, DialogCallBack callBack) {
		setHeightUnDefinedMode();
		showDialog(parentUI, caption, VaadinCommonConstant.MEDIUM_DIALOG_WIDTH, null, true, true, callBack);
	}
	
	@SuppressWarnings("unchecked")
	public void setData(ProductionOrder productionOrder,List<FunctionDetectionResult> functionDetectionResultList) {//
		if(productionOrder!=null) {
			this.orderNo = productionOrder.getProductOrderId();
		}
		
		if(functionDetectionResultList!=null && functionDetectionResultList.size()>0) {
			this.functionDetectionResultList = functionDetectionResultList;
			tfGageNo.setValue(functionDetectionResultList.get(0).getGageNO());
			tfGageNo.setReadOnly(true);
			for(int i=0;i<functionDetectionResultList.size();i++) {//FunctionDetectionResult functionDetectionResult: functionDetectionResultList
				String sn = functionDetectionResultList.get(i).getSn();
				String result = functionDetectionResultList.get(i).getFunctionInspectionResult();
				String caption = sn.split("_")[1];
				textFields[i].setValue(caption);
				textFields[i].setReadOnly(true);
				fields[i].setSelectedItem(result);
				fields[i].setEnabled(false);
			}
			
		}else {
			for(int i=0;i<8;i++) {
				textFields[i].clear();
				textFields[i].setReadOnly(false);
				fields[i].setSelectedItem("");
				fields[i].setEnabled(true);
			}
		}
		
		//权限设置
		loginUserName = RequestInfo.current().getUserName();
		role = userService.getByName(loginUserName).getRole();
		if(role.contains(roleService.getByName("qc"))) {
			this.setOKButtonVisible(true);
			btnStart.setEnabled(true);
		}else {
			this.setOKButtonVisible(false);
			btnStart.setEnabled(false);
//			NotificationUtils.notificationError("当前登录人员："+loginUserName+" 没有QC检测权限！");
		}
	}
	
	@Override
	protected void okButtonClicked() throws Exception {
		//返回的总结果
		boolean flag = true;
		//新增对象为空   才可保存
		if(functionDetectionResultList==null || functionDetectionResultList.size()<=0) {
			for(int i=0;i<textFields.length;i++) {//TextField textField:textFields
				String caption = textFields[i].getValue().trim();
				String resultValue = (String) fields[i].getValue();
				if(!Strings.isNullOrEmpty(caption) && !Strings.isNullOrEmpty(resultValue)) {
					FunctionDetectionResult functionDetectionResult = new FunctionDetectionResult();
					functionDetectionResult.setOrderNo(orderNo);
					functionDetectionResult.setSn(orderNo+"_"+caption);//.substring(caption.length() -2,caption.length())
					functionDetectionResult.setGageNO(tfRuler.getValue().trim());
					functionDetectionResult.setFunctionInspectionResult(resultValue);
					functionDetectionResultService.save(functionDetectionResult);
					if(flag && ("NG".equals(resultValue))) {
						flag = false;
					}
				}
			}
			this.setFunctionInspectionResult(flag);
		}
	}
	
	@Override
	protected void cancelButtonClicked() {
		
	}

	@Override
	protected Component getDialogContent() {
		VerticalLayout vlLayout = new VerticalLayout();
		gridLayout.addComponent(tfGageNo,0,0);
		tfGageNo.setPlaceholder("输入量具编号");
		gridLayout.addComponent(tfOrderSN1,1,0);
		gridLayout.addComponent(tfOrderSN2,2,0);
		gridLayout.addComponent(tfOrderSN3,3,0);
		gridLayout.addComponent(tfOrderSN4,4,0);
		gridLayout.addComponent(tfOrderSN5,5,0);
		gridLayout.addComponent(tfOrderSN6,6,0);
		gridLayout.addComponent(tfOrderSN7,7,0);
		gridLayout.addComponent(tfOrderSN8,8,0);
		
		gridLayout.addComponent(tfRuler,0,1);
		gridLayout.addComponent(cbResult1,1,1);
		gridLayout.addComponent(cbResult2,2,1);
		gridLayout.addComponent(cbResult3,3,1);
		gridLayout.addComponent(cbResult4,4,1);
		gridLayout.addComponent(cbResult5,5,1);
		gridLayout.addComponent(cbResult6,6,1);
		gridLayout.addComponent(cbResult7,7,1);
		gridLayout.addComponent(cbResult8,8,1);
		tfRuler.setValue(AppConstant.TORQUE_RULER);
		tfRuler.setEnabled(false);
		tfGageNo.setWidth("100px");
		tfRuler.setWidth("100px");
		
		for(int i=0;i<textFields.length;i++) {
			textFields[i].setPlaceholder("序号");
			textFields[i].setWidth("60px");
//Changed by Cameron: 允许QC输入任意字符
//			textFields[i].addBlurListener(new BlurListener() {
//
//				private static final long serialVersionUID = 5352794819665253442L;
//
//				@Override
//				public void blur(BlurEvent event) {
//					TextField textField = (TextField)event.getSource();
//					String textValue = textField.getValue().trim();
//					if(!Strings.isNullOrEmpty(textValue)) {
//						if(!valueIsPositive(textValue)) {
//							textField.clear();
//							NotificationUtils.notificationError("请填入大于零的正整数！");
//						}else {
//							if(getPosition(textField)!=textFields.length) {
//								if(checkIsAdded(getPosition(textField),textValue)) {
//									textField.clear();
//									NotificationUtils.notificationError("填入的正整数序号已经被使用，请选择其他正整数！");
//								}else {
//									checkSnArr[getPosition(textField)] = textValue;
//								}
//							}
//						}
//					}else {
//						if(getPosition(textField)!=textFields.length) {
//							checkSnArr[getPosition(textField)] = "";
//						}
//					}
//				}
//
//			});

		}
		for(ComboBox<String> field : fields ) {
			field.setWidth("60px");
			field.setItems("OK","NG");
		}
		btnStart.setIcon(VaadinIcons.START_COG);
		btnStart.addClickListener(this);
		vlLayout.addComponent(btnStart);
		vlLayout.addComponent(gridLayout);
		return vlLayout;
	}
	
	
	public int getPosition(TextField textField) {
		for(int i=0;i<textFields.length;i++) {
			if(textField.equals(textFields[i])) {
				return i;
			}
		}
		return textFields.length;
	}
	
	public boolean checkIsAdded(int i,String textValue) {
		boolean flag = false;
		for(int j=0;j<checkSnArr.length;j++) {
			if(j!=i) {
				if(textValue.equals(checkSnArr[j])) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	@Override
	protected void initUIData() {
		
	}
	
	public boolean getFunctionInspectionResult() {
		return this.functionInspectionResult;
	}
	
	public void setFunctionInspectionResult(boolean functionInspectionResult) {
		this.functionInspectionResult = functionInspectionResult;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		Button button = event.getButton();
        button.setEnabled(true);
        if (btnStart.equals(button)) {
        	// 检验开始,光标聚焦gridlayout(2,0)
			TextField field = (TextField) gridLayout.getComponent(2, 0);
			x = 0;
			y = 0;
			// 光标聚焦之后，连接Socket，并且发送语音播报第一条指令
			ipAddress = RequestInfo.current().getUserIpAddress();
			try {
				client = new SocketClient(ipAddress, AppConstant.PORT);

				client.setReceiveListener(message -> {
					// 定义 OnMessage 后执行事件。
					if (message.startsWith(AppConstant.PREFIXPLATTEXTEND)//OC
							&& AppConstant.PREFIXPLAYTEXT.equals(prefixSend)) {
						// [OC]语音播放结束
						System.out.println("Receive Message -> " + message);
						// 发送请求语音识别的指令
						try {
							prefixSend = AppConstant.PREFIXSTARTRECORD;
							client.sendMessage("[BR]");
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else if (message.startsWith(AppConstant.PREFIXRECORDRESULT)//OR
							&& AppConstant.PREFIXSTARTRECORD.equals(prefixSend)) {
						// 发送语音识别请求并且返回的是语音识别结束字串
						// 判断返回结果是否复核标准范围
						// 获取检验项名称
						// message去除信息头
						String messageBody = message.split("]")[1];
						if (!message.split("\\|")[1].equals("1")) {
							returnMessage.append(messageBody.split("\\|")[0]);
						}
					} else if (message.startsWith(AppConstant.PREFIXRECORDRESULTEND)) {//ES
						if (returnMessage.length() == 0) {
							prefixSend = AppConstant.PREFIXPLAYTEXT;
							try {
								if (y == 0) {
									TextField textField = (TextField)gridLayout.getComponent(x, 0);
									if(x==0 && textField!=null) {
										client.sendMessage("[PT]请输入量具编号的值");
									}else if(x>0 && textField!=null) {
										client.sendMessage("[PT]请输入第"+x+"个序号");
									}
								} else if(y==1 && x>0) {
									ComboBox comboBox = ((ComboBox) gridLayout.getComponent(x, 1));
									if(comboBox!=null) {
										client.sendMessage("[PT]请输入第"+x+"个序号的值");
									}
								}
							} catch (Exception e) {
								NotificationUtils.notificationError("Socket连接中断");
							}
							// 没有识别当前检测项的值，任然播报当前检测项的信息
						} else {
							getUI().accessSynchronously(new Runnable() {
								@Override
								public void run() {
									String returnMessageStr = returnMessage.toString();
									if(y==0) {
										TextField component = (TextField)gridLayout.getComponent(x, 0);
										if(x==0) {
											component.setValue(returnMessageStr);
											isInputOk = true;
										}else if(x>0) {
											if(valueIsPositive(returnMessageStr)) {
												if(getPosition(component)!=textFields.length) {
													if(!checkIsAdded(getPosition(component),returnMessageStr)) {
														component.setValue(returnMessageStr);
														checkSnArr[getPosition(component)] = returnMessageStr;
														isInputOk = true;
													}else {
														errorMessage = "填入第"+x+"个序号的正整数已经被使用请选择其他正整数";
													}
												}
											}else {
												errorMessage = "第"+x+"个序号请输入大于零的正整数";
											}
										}
									}else if(y==1 && x>0) {
										ComboBox comboBox = ((ComboBox) gridLayout.getComponent(x, 1));
										if("OK".equals(returnMessageStr) || "NG".equals(returnMessageStr)) {
											if(comboBox!=null) {
												comboBox.setSelectedItem(returnMessageStr.toUpperCase());
												isInputOk = true;
											}
										}else {
											errorMessage = "第"+x+"个序号的值请输入OK或NG中的一个";
										}
									}
									returnMessage.delete(0, returnMessage.length());
								}
							});
							// 播报下一个输入项
							prefixSend = AppConstant.PREFIXPLAYTEXT;
							try {
								if(isInputOk && x==0) {
									isInputOk = false;
									x++;
									y=0;
									TextField textField = ((TextField) gridLayout.getComponent(x, 0));
									if(textField!=null) {
										client.sendMessage("[PT]请输入第" + x + "个序号");
									}
								}else if(isInputOk && x>0 && x<=gridLayout.getColumns()) {
									isInputOk = false;
									y++;
									ComboBox comboBox = ((ComboBox) gridLayout.getComponent(x, y));
									if(comboBox!=null) {
										client.sendMessage("[PT]请输入第" + x + "个序号的值");
									}else {
										x++;
										y=0;
										TextField textField = ((TextField) gridLayout.getComponent(x, 0));
										if(textField!=null) {
											client.sendMessage("[PT]请输入第" + x + "个序号");
										}else {
											if(x==gridLayout.getColumns()) {
												prefixSend = AppConstant.INSPECTIONDONE;
												client.sendMessage("[PT]功能检验录入结束");
											}
										}
									}
								}else if(!isInputOk) {
									client.sendMessage("[PT]"+errorMessage);
									errorMessage = "";
								}
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				});
				prefixSend = AppConstant.PREFIXPLAYTEXT;
				client.sendMessage("[PT]现在开始功能检验录入");
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
	}
	
	//匹配正整数
	public boolean valueIsPositive(String value){
		if (!Strings.isNullOrEmpty(value)) {
            boolean isNumber = RegExpValidatorUtils.isIsPositive(value);
            if (!isNumber) {
                return false;
            } else {
                return true;
            }
        }
		return false;
	}

}
