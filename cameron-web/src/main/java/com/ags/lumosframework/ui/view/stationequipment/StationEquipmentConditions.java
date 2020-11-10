package com.ags.lumosframework.ui.view.stationequipment;

import com.ags.lumosframework.common.spring.BeanManager;
import com.ags.lumosframework.entity.StationEquipmentEntity;
import com.ags.lumosframework.sdk.base.filter.EntityFilter;
import com.ags.lumosframework.service.IStationEquipmentService;
import com.ags.lumosframework.web.common.i18.I18NUtility;
import com.ags.lumosframework.web.common.i18.I18Support;
import com.ags.lumosframework.web.vaadin.component.searchpanel.conditions.BaseConditions;
import com.ags.lumosframework.web.vaadin.component.searchpanel.conditions.IConditions;
import com.vaadin.data.HasValue;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
public class StationEquipmentConditions extends BaseConditions implements IConditions{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8788115868676532550L;

		// 查询区域控件
		private TextField tfStation = new TextField();

		@I18Support(caption = "Equipment No", captionKey = "view.StationEquipment.EquipmentNo")
		private TextField tfEquipmentNo = new TextField();

		private HasValue<?>[] fields = { tfStation, tfEquipmentNo };

		private Component[] components = { tfStation };

		private Component[] temp = { tfEquipmentNo };

		FormLayout hlSearchPanel = new FormLayout();

		public StationEquipmentConditions() {
			tfStation.setPlaceholder(I18NUtility.getValue("view.StationEquipment.Station", "Station"));
			hlSearchPanel.setWidthUndefined();
			for (Component component : temp) {
				component.setWidth("100%");
				hlSearchPanel.addComponent(component);
			}
			setElementsId();
		}

		private void setElementsId() {
			tfStation.setId("tf_Code");
			tfEquipmentNo.setId("tf_Name");
		}

		@Override
		public Component[] getComponent() {
			return components;
		}

		@Override
		public EntityFilter getFilter() {
			IStationEquipmentService stationEquipmentService = BeanManager.getService(IStationEquipmentService.class);
			EntityFilter createFilter = stationEquipmentService.createFilter();
			if (tfStation.getValue() != null && !tfStation.getValue().equals("")) {
				createFilter.fieldEqualTo(StationEquipmentEntity.STATION, tfStation.getValue());
			}
			if (tfEquipmentNo.getValue() != null && !tfEquipmentNo.getValue().equals("")) {
				createFilter.fieldContains(StationEquipmentEntity.EQUIPMENT_NO, tfEquipmentNo.getValue());
			}
			return createFilter;
		}

		@Override
		public AbstractLayout getLayout() {
			return hlSearchPanel;
		}

		@Override
		public void reset() {
			for (HasValue<?> field : fields) {
				field.clear();
			}
		}
}
