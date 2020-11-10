package com.ags.lumosframework.ui;

import com.ags.lumosframework.web.vaadin.base.BaseView;
import com.vaadin.spring.annotation.SpringView;
/***
 * 
 * @author william_huang
 *每一个UI都需要写一个默认的View，否则在点击进入UI的时候会报错
 *默认View即name="".非默认name属性需要设置对应的值
 *此View不要改动
 */
@SpringView(name="" ,ui=CameronUI.class)
public class DafaultView extends BaseView{

	private static final long serialVersionUID = -1117891897269368411L;

}
