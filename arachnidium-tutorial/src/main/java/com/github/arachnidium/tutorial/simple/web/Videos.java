package com.github.arachnidium.tutorial.simple.web;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.github.arachnidium.core.Handle;
import com.github.arachnidium.model.common.FunctionalPart;

public class Videos<S extends Handle> extends HasSearchField<S> {

	@FindBy(className = "video_row_cont")
	private List<WebElement> videos;
	
	protected Videos(FunctionalPart<S> parent) {
		super(parent);
	}
	
	@InteractiveMethod
	public int getVideosCount(){
		return videos.size();
	}
	
	@InteractiveMethod
	public void playVideo(int index){
		videos.get(index).findElement(By.className("video_row_info_play")).click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}