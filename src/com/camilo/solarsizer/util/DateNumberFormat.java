package com.camilo.solarsizer.util;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A custom formatter that will take numbers and display them as if they were
 * timestamps (<em>not</em> date stamps). To achieve this, it actually delegates
 * down to a DateFormatter (using a format of HH:mm). This date formatter is
 */
public final class DateNumberFormat extends NumberFormat {

	private static final long serialVersionUID = -3472067357351362183L;
	private SimpleDateFormat formatter = new SimpleDateFormat("MMM/dd/yyyy ");
	private long initDateValue = 0;

	/**
	 * Creates a new instance
	 */
	public DateNumberFormat() {
	}

	/**
	 * Formats the specified number as a string of the form HH:MM:SS. The
	 * decimal fraction is ignored
	 */
	@Override
	public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
		return format((long) number, toAppendTo, pos);
	}

	/** Delegates down to date format */
	@Override
	public Number parse(String arg0, ParsePosition pos) {
		return null;
	}

	@Override
	public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
		StringBuffer sb = new StringBuffer();
		long dateValue = initDateValue + number*Constants.DAY;
		
		Date date = new Date(dateValue);
		sb.append(this.formatter.format(date));
		return sb;
	}

	public void setInitDateValue(long initDateValue) {
		this.initDateValue = initDateValue;
	}

}
