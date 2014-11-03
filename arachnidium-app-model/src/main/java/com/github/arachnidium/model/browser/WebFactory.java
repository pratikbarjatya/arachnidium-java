package com.github.arachnidium.model.browser;

import io.appium.java_client.remote.MobileCapabilityType;

import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.github.arachnidium.core.WindowManager;
import com.github.arachnidium.core.settings.AlertIsPresentTimeOut;
import com.github.arachnidium.core.settings.CapabilitySettings;
import com.github.arachnidium.core.settings.ChromeDriverServerBin;
import com.github.arachnidium.core.settings.HandleWaitingTimeOut;
import com.github.arachnidium.core.settings.IEDriverServerBin;
import com.github.arachnidium.core.settings.PhantomJSDriverBin;
import com.github.arachnidium.core.settings.ScreenShots;
import com.github.arachnidium.core.settings.WebDriverSettings;
import com.github.arachnidium.core.settings.WebDriverTimeOuts;
import com.github.arachnidium.core.settings.WindowIsClosedTimeOut;
import com.github.arachnidium.core.settings.supported.ESupportedDrivers;
import com.github.arachnidium.model.common.Application;
import com.github.arachnidium.model.common.DefaultApplicationFactory;
import com.github.arachnidium.util.configuration.Configuration;

public final class WebFactory extends DefaultApplicationFactory {
	private static WebDriverDesignationChecker objectWhichChecksWebDriver = givenWebDriverDesignation -> {
			if (!givenWebDriverDesignation.isForBrowser()){
				throw new IllegalArgumentException(givenWebDriverDesignation.toString() + 
						" is not for browser launching!");
			}
		};
	/**
	 * Common method that creates an instance of the application by defined
	 * {@link Configuration}. Performs navigation to the required URL 
	 * 
	 * <br/><br/>
	 * Supported {@link WebDriver} designations:<br/>
	 * - {@link ESupportedDrivers#FIREFOX}<br/>
	 * - {@link ESupportedDrivers#CHROME}<br/>
	 * - {@link ESupportedDrivers#INTERNETEXPLORER}<br/>
	 * - {@link ESupportedDrivers#PHANTOMJS}<br/>
	 * - {@link ESupportedDrivers#SAFARI}<br/>
	 * - {@link ESupportedDrivers#REMOTE}<br/>
	 * - {@link ESupportedDrivers#ANDROID_CHROME}<br/>
	 * - {@link ESupportedDrivers#IOS_SAFARI}<br/>
	 * All these should be defined in {@link WebDriverSettings}
	 * <br/><br/>
	 * Presence of {@link Capabilities} is optional, except {@link ESupportedDrivers#REMOTE},
	 * {@link ESupportedDrivers#ANDROID_CHROME} and {@link ESupportedDrivers#IOS_SAFARI}.
	 * Following capabilities are required in this case:<br/>
	 * - {@link CapabilityType#BROWSER_NAME} is actual for {@link ESupportedDrivers#REMOTE}<br/>
	 * - {@link MobileCapabilityType#DEVICE_NAME} - if {@link ESupportedDrivers#ANDROID_CHROME}, 
	 * - {@link ESupportedDrivers#IOS_SAFARI} and {@link ESupportedDrivers#REMOTE} launched on
	 * mobile device<br/>
	 * - {@link MobileCapabilityType#PLATFORM_VERSION} this parameter is actual for {@link ESupportedDrivers#IOS_SAFARI}
	 * and {@link ESupportedDrivers#REMOTE} launched on IPhone/IPad (simulator/device)<br/> 
	 * 
	 * All these should be defined in {@link CapabilitySettings}
	 * 
	 * @see 
	 * {@link Configuration}<br/>
	 * {@link WebDriverTimeOuts}<br/>
	 * {@link HandleWaitingTimeOut}<br/>
	 * {@link AlertIsPresentTimeOut}<br/>
	 * {@link WindowIsClosedTimeOut}<br/>
	 * {@link ChromeDriverServerBin}<br/>
	 * {@link IEDriverServerBin}<br/>
	 * {@link PhantomJSDriverBin}<br/>
	 * {@link ScreenShots}<br/>
	 */
	public static <T extends Application<?, ?>> T getApplication(
			Class<T> appClass, Configuration config, String urlToBeLoaded) {
		return load(
				getApplication(WindowManager.class, appClass, config,
						new BrowserApplicationInterceptor(), objectWhichChecksWebDriver), 
						urlToBeLoaded);
	}

	/**
	 * Common method that creates an instance of the application by required
	 * {@link RemoteWebDriver} class and its {@link Capabilities}.
	 * The class of {@link RemoteWebDriver} subclass 
	 * is contained by {@link ESupportedDrivers} 
	 * 
     * <br/><br/>
	 * Supported {@link WebDriver} designations:<br/>
	 * - {@link ESupportedDrivers#FIREFOX}<br/>
	 * - {@link ESupportedDrivers#CHROME}<br/>
	 * - {@link ESupportedDrivers#INTERNETEXPLORER}<br/>
	 * - {@link ESupportedDrivers#PHANTOMJS}<br/>
	 * - {@link ESupportedDrivers#SAFARI}<br/>
	 * - {@link ESupportedDrivers#REMOTE}<br/>
	 * - {@link ESupportedDrivers#ANDROID_CHROME}<br/>
	 * - {@link ESupportedDrivers#IOS_SAFARI}<br/>
	 * 
	 * Performs navigation to the required URL 
	 * 
	 * 
	 */
	public static <T extends Application<?, ?>> T getApplication(
			Class<T> appClass, ESupportedDrivers supportedDriver,
			Capabilities capabilities, String urlToBeLoaded) {
		return load(
				getApplication(WindowManager.class, appClass, supportedDriver,
						capabilities, new BrowserApplicationInterceptor(), objectWhichChecksWebDriver),
				urlToBeLoaded);
	}


	/**
	 * Common method that creates an instance of the application by required
	 * {@link RemoteWebDriver} class and its {@link Capabilities} and URL of remote
	 * host where it should be launched.
     * The class of {@link RemoteWebDriver} subclass 
	 * is contained by {@link ESupportedDrivers} 
	 * 
	 * Performs navigation to the required URL<br/><br/> 
	 * 
	 * Supported {@link WebDriver} designations:<br/>
	 * - {@link ESupportedDrivers#FIREFOX}<br/>
	 * - {@link ESupportedDrivers#CHROME}<br/>
	 * - {@link ESupportedDrivers#INTERNETEXPLORER}<br/>
	 * - {@link ESupportedDrivers#PHANTOMJS}<br/>
	 * - {@link ESupportedDrivers#SAFARI}<br/>
	 * - {@link ESupportedDrivers#REMOTE}<br/>
	 * - {@link ESupportedDrivers#ANDROID_CHROME}<br/>
	 * - {@link ESupportedDrivers#IOS_SAFARI}<br/>
	 */
	public static <T extends Application<?, ?>> T getApplication(
			Class<T> appClass, ESupportedDrivers supportedDriver,
			Capabilities capabilities, URL remoteAddress, String urlToBeLoaded) {
		return load(
				getApplication(WindowManager.class, appClass, supportedDriver,
						capabilities, remoteAddress,
						new BrowserApplicationInterceptor(), objectWhichChecksWebDriver), urlToBeLoaded);
	}

	/**
	 * Common method that creates an instance of the application by required
	 * {@link RemoteWebDriver} class. This class is contained by {@link ESupportedDrivers} 
	 * 
	 * Performs navigation to the required URL<br/><br/> 
	 * 
	 * Supported {@link WebDriver} designations:<br/>
	 * - {@link ESupportedDrivers#FIREFOX}<br/>
	 * - {@link ESupportedDrivers#CHROME}<br/>
	 * - {@link ESupportedDrivers#INTERNETEXPLORER}<br/>
	 * - {@link ESupportedDrivers#PHANTOMJS}<br/>
	 * - {@link ESupportedDrivers#SAFARI}<br/>
	 * - {@link ESupportedDrivers#REMOTE}<br/>
	 * - {@link ESupportedDrivers#ANDROID_CHROME}<br/>
	 * - {@link ESupportedDrivers#IOS_SAFARI}<br/>
	 */
	public static <T extends Application<?, ?>> T getApplication(
			Class<T> appClass, ESupportedDrivers supportedDriver,
			String urlToBeLoaded) {
		return load(
				getApplication(WindowManager.class, appClass, supportedDriver,
						new BrowserApplicationInterceptor(), objectWhichChecksWebDriver), urlToBeLoaded);
	}

	/**
	 * Common method that creates an instance of the application by required
	 * {@link RemoteWebDriver} class and URL of remote
	 * host where it should be launched.
     * The class of {@link RemoteWebDriver} subclass 
	 * is contained by {@link ESupportedDrivers} 
	 * 
	 * Performs navigation to the required URL 
	 */
	public static <T extends Application<?, ?>> T getApplication(
			Class<T> appClass, ESupportedDrivers supportedDriver,
			URL remoteAddress, String urlToBeLoaded) {
		return load(
				getApplication(WindowManager.class, appClass, supportedDriver,
						remoteAddress, new BrowserApplicationInterceptor(), objectWhichChecksWebDriver),
				urlToBeLoaded);
	}

	/**
	 * Common method that creates an instance of the application by default
	 * configuration - {@link Configuration#byDefault}
	 * 
	 * Performs navigation to the required URL<br/><br/> 
	 * 
     * Supported {@link WebDriver} designations:<br/>
	 * - {@link ESupportedDrivers#FIREFOX}<br/>
	 * - {@link ESupportedDrivers#CHROME}<br/>
	 * - {@link ESupportedDrivers#INTERNETEXPLORER}<br/>
	 * - {@link ESupportedDrivers#PHANTOMJS}<br/>
	 * - {@link ESupportedDrivers#SAFARI}<br/>
	 * - {@link ESupportedDrivers#REMOTE}<br/>
	 * - {@link ESupportedDrivers#ANDROID_CHROME}<br/>
	 * - {@link ESupportedDrivers#IOS_SAFARI}<br/>
	 * All these should be defined in {@link WebDriverSettings}
	 * <br/><br/>
	 * Presence of {@link Capabilities} is optional, except {@link ESupportedDrivers#REMOTE},
	 * {@link ESupportedDrivers#ANDROID_CHROME} and {@link ESupportedDrivers#IOS_SAFARI}.
	 * Following capabilities are required in this case:<br/>
	 * - {@link CapabilityType#BROWSER_NAME} is actual for {@link ESupportedDrivers#REMOTE}<br/>
	 * - {@link MobileCapabilityType#DEVICE_NAME} - if {@link ESupportedDrivers#ANDROID_CHROME}, 
	 * - {@link ESupportedDrivers#IOS_SAFARI} and {@link ESupportedDrivers#REMOTE} launched on
	 * mobile device<br/>
	 * - {@link MobileCapabilityType#PLATFORM_VERSION} this parameter is actual for {@link ESupportedDrivers#IOS_SAFARI}
	 * and {@link ESupportedDrivers#REMOTE} launched on IPhone/IPad (simulator/device)<br/> 
	 * 
	 * All these should be defined in {@link CapabilitySettings}
	 * 
	 * @see 
	 * {@link Configuration}<br/>
	 * {@link WebDriverTimeOuts}<br/>
	 * {@link HandleWaitingTimeOut}<br/>
	 * {@link AlertIsPresentTimeOut}<br/>
	 * {@link WindowIsClosedTimeOut}<br/>
	 * {@link ChromeDriverServerBin}<br/>
	 * {@link IEDriverServerBin}<br/>
	 * {@link PhantomJSDriverBin}<br/>
	 * {@link ScreenShots}<br/>
	 */
	public static <T extends Application<?, ?>> T getApplication(
			Class<T> appClass, String urlToBeLoaded) {
		return load(
				getApplication(WindowManager.class, appClass,
						new BrowserApplicationInterceptor(), objectWhichChecksWebDriver), urlToBeLoaded);
	}

	private static <T extends Application<?, ?>> T load(T instance,
			String urlToBeLoaded) {
		instance.getWebDriverEncapsulation().getWrappedDriver().navigate()
				.to(urlToBeLoaded);
		return instance;
	}
}
