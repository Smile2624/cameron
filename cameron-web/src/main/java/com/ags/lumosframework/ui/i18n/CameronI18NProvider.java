package com.ags.lumosframework.ui.i18n;

import com.ags.lumosframework.web.common.i18.I18NProvider;
import org.springframework.stereotype.Component;

@Component
public class CameronI18NProvider implements I18NProvider {

	@Override
	public String[] getBaseNames() {
		return new String[]{"local/Cameron_I18_Normal"};

	}

	@Override
	public int getPriority() {
		return 0;
	}

}
