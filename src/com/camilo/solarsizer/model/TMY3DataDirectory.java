package com.camilo.solarsizer.model;


/**
 * This class handles the construction of the new url to where the TMY3 data is retrieved
 * @author Jose Camilo Uzquiano
 *
 */
public class TMY3DataDirectory {
	
	private String usafn;
	private String location;
	private String url;
	private String state;
	
	/**
	 * This constructor takes paramenter from the website to construct a new url to read the tmy3Data directly
	 * @param usafn
	 * @param location
	 * @param url 
	 * @param stateStr 
	 */
	public TMY3DataDirectory(String usafn, String location, String url, String state) {
		this.usafn = usafn;
		this.location = location.substring(3);
		this.state = state;
		
		this.url = url;
	}

	
	
	public String getUsafn() {
		return usafn;
	}

	public void setUsafn(String usafn) {
		this.usafn = usafn;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}


}
