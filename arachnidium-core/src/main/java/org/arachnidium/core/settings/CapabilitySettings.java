package org.arachnidium.core.settings;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.arachnidium.util.configuration.AbstractConfigurationAccessHelper;
import org.arachnidium.util.configuration.Configuration;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * There are specified {@link WebDriver} {@link Capabilities}
 * 
 * @see Configuration
 * 
 * Specification:
 * 
 * ...
 * "DesiredCapabilities":
  {
      "browserName":{
          "type":"STRING",
          "value":"some browser name"   //firefox, chrome etc. 
      },      
      "version": {
          "type":"STRING",
          "value":"some version" 
      },
      "platform": {
          "type":"STRING",
          "value":"some platform"  @see Platform        
      },
      "javascriptEnabled":{
          "type":"BOOL",
          "value":"some flag"           
      }
  }
  ...
 */
public class CapabilitySettings extends AbstractConfigurationAccessHelper
implements HasCapabilities, Capabilities {

	// specified settings for capabilities
	private final String capabilityGroup = "DesiredCapabilities";
	private final DesiredCapabilities builtCapabilities = new DesiredCapabilities();
	private final String appCapability = "app";

	public CapabilitySettings(Configuration configuration) {
		super(configuration);
		buildCapabilities();
	}

	/**
	 * @see org.openqa.selenium.Capabilities#asMap()
	 */
	@Override
	public Map<String, ?> asMap() {
		return builtCapabilities.asMap();
	}

	private void buildCapabilities() {
		HashMap<String, Object> capabilities = getGroup(capabilityGroup);
		if (capabilities == null)
			return;

		List<String> capabilityStrings = new ArrayList<String>(
				capabilities.keySet());
		capabilityStrings.forEach((capabilityStr) -> {
			if (capabilities.get(capabilityStr) != null)
				builtCapabilities.setCapability(capabilityStr,
						capabilities.get(capabilityStr));
		});
		transformCapabilities();
	}

	/**
	 * @see org.openqa.selenium.Capabilities#getBrowserName()
	 */
	@Override
	public String getBrowserName() {
		return builtCapabilities.getBrowserName();
	}

	/**
	 * @see org.openqa.selenium.HasCapabilities#getCapabilities()
	 */
	@Override
	public Capabilities getCapabilities() {
		return builtCapabilities;
	}

	/**
	 * @see org.openqa.selenium.Capabilities#getCapability(java.lang.String)
	 */
	@Override
	public Object getCapability(String capabilityName) {
		return builtCapabilities.getCapability(capabilityName);
	}

	/**
	 * @see org.openqa.selenium.Capabilities#getPlatform()
	 */
	@Override
	public Platform getPlatform() {
		return builtCapabilities.getPlatform();
	}

	/**
	 * @see org.arachnidium.util.configuration.AbstractConfigurationAccessHelper#getSetting(java.lang.String)
	 */
	@Override
	public <T extends Object> T getSetting(String name) {
		return getSettingValue(capabilityGroup, name);
	}

	/**
	 * @see org.openqa.selenium.Capabilities#getVersion()
	 */
	@Override
	public String getVersion() {
		return builtCapabilities.getVersion();
	}

	/**
	 * @see org.openqa.selenium.Capabilities#is(java.lang.String)
	 */
	@Override
	public boolean is(String capabilityName) {
		return builtCapabilities.is(capabilityName);
	}

	/**
	 * @see org.openqa.selenium.Capabilities#isJavascriptEnabled()
	 */
	@Override
	public boolean isJavascriptEnabled() {
		return builtCapabilities.isJavascriptEnabled();
	}

	// transforms capabilities values if they need to be changed
	//I think it is not final implementation 
	private void transformCapabilities() {
		// transforms relative path to application into absolute
		Object pathToApp = getCapability(appCapability);
		if (pathToApp != null) {
			File app = new File(String.valueOf(pathToApp));
			builtCapabilities.setCapability(appCapability,
					app.getAbsolutePath());
		}
		// if other actions need to be implemented code will be below
	}
}
