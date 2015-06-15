package com.camilo.solarsizer.model;

import java.util.Date;

/**
 * This class contains monthly values of energy
 * @author Camilo Uzquiano
 *
 */
public class MonthlyData {

	private Date date;
	private int month; 
	private int year;
	private float monthlyKWh;
	
	/*
	 * Constructor
	 */
	public MonthlyData(Date date){
		this.date = date;
		month = date.getMonth();
		year = date.getYear();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public float getMonthlyKWh() {
		return monthlyKWh;
	}

	public void setMonthlyKWh(float monthlyKWh) {
		this.monthlyKWh = monthlyKWh;
	}
}
