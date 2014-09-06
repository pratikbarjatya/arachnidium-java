package org.arachnidium.core.settings;

import org.arachnidium.util.configuration.AbstractConfigurationAccessHelper;
import org.arachnidium.util.configuration.Configuration;

public class AlertIsPresentTimeOut extends AbstractConfigurationAccessHelper {
	private final String alertIsPresentTimeOutGroup = "alertIsPresentTimeOut";	
	private final String alertIsPresentTimeOutSetting = "alertIsPresentTimeOut";	
	
	public AlertIsPresentTimeOut(Configuration configuration) {
		super(configuration);
	}

	@Override
	public <T extends Object> T getSetting(String name) {
		return getSettingValue(alertIsPresentTimeOutGroup, name);
	}
	
	public Long getAlertIsPresentTimeOut(){
		return getSetting(alertIsPresentTimeOutSetting);
	}

}
