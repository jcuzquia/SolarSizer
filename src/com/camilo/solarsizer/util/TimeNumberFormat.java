package com.camilo.solarsizer.util;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * A custom formatter that will take numbers and display them as if they were timestamps.
 * To achieve this, it actually delegates down to a formatter.
 *
 */
public final class TimeNumberFormat extends NumberFormat {
        
	private static final long serialVersionUID = 9196587428849805421L;
	private NumberFormat formatter = new DecimalFormat("00");

        /**
         * Creates a new instance
         */
        public TimeNumberFormat() {
            
        }
        
        
        /**
         * Formats the specified number as a string of the form HH:MM:SS.
         * The decimal fraction is ignored
         */
        @Override
		public StringBuffer format(double number, StringBuffer toAppendTo,
                FieldPosition pos) {
            return format((long)number, toAppendTo, pos);
        }


        /** Delegates down to date format */
        @Override
		public Number parse(String arg0, ParsePosition pos) {
            return null;
        }

        @Override
		public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
        	 StringBuffer sb = new StringBuffer();
        	 if (number < 0){
        		 number = number + 96;
        	 } else if (number > 95){
        		 number = number - 96;
        	 }
             long hours = number / 4;
             sb.append(this.formatter.format(hours)).append(":");
             long remaining = number - (hours * 4);
             long minutes = remaining*15;
             sb.append(this.formatter.format(minutes));
             return sb;
		}
        
        
}