package com.example.demo.bean;

import java.util.List;
import java.util.Map;

public class WeatherBean {
	private String cityName;
	private String cityDescription;
	private String liveWeather;
	private List<Map<String, Object>> list;

	
	public WeatherBean() {
		super();
	}

	public WeatherBean(String cityName, String cityDescription,
			String liveWeather, List<Map<String, Object>> list) {
		super();
		this.cityName = cityName;
		this.cityDescription = cityDescription;
		this.liveWeather = liveWeather;
		this.list = list;
	}


	public String getCityName() {
		return cityName;
	}

	public String getCityDescription() {
		return cityDescription;
	}

	public String getLiveWeather() {
		return liveWeather;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public void setCityDescription(String cityDescription) {
		this.cityDescription = cityDescription;
	}

	public void setLiveWeather(String liveWeather) {
		this.liveWeather = liveWeather;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "WeatherBean [cityName=" + cityName + ", cityDescription="
				+ cityDescription + ", liveWeather=" + liveWeather + ", list="
				+ list + "]";
	}

	
}
