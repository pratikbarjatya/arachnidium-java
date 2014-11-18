package com.github.arachnidium.model.support.annotations.classdeclaration.rootelements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Reads class declarations and returns 
 * {@link By} instances when class is marked by
 * {@link RootElement}, {@link RootAndroidElement},
 * {@link RootIOSElement}
 *
 */
public interface RootElementReader {
	/**
	 * This method should return {@link By} if the given class is marked by
	 * {@link RootElement}, {@link RootAndroidElement},
     * {@link RootIOSElement}
	 */
	public By readClassAndGetBy(Class<?> readableClass, WebDriver driver);
}