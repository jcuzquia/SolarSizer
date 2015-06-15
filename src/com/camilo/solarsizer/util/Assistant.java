package com.camilo.solarsizer.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.camilo.solarsizer.model.IntervalData;
import com.camilo.solarsizer.model.TMY3DataDirectory;

/**
 * Static class that helps make the transition of certain values
 * @author Camilo Uzquiano
 *
 */

public class Assistant {
	
	/**
	 * turns the string into a property
	 * @param s
	 * @return StringProperty
	 */
	public static StringProperty parsePropertiesString(String s){
	    
		SimpleStringProperty p = new SimpleStringProperty(s);
	    
	    return p;
	}

	/**
	 * 
	 * @param activated
	 * @return BooleanProperty
	 */
	public static BooleanProperty parsePropertiesBoolean(boolean activated) {
		
		SimpleBooleanProperty p = new SimpleBooleanProperty(activated);
		return p;
	}
	
	/**
	 * Method that returns a string of the color 
	 * @param color
	 * @return
	 */
	public static String toRGBCodeFX( javafx.scene.paint.Color color )
    {
        return String.format( "#%02X%02X%02X",
            (int)( color.getRed() * 255 ),
            (int)( color.getGreen() * 255 ),
            (int)( color.getBlue() * 255 ) );
    }
	
	/**
	 * Method that returns a string of the color 
	 * @param color
	 * @return
	 */
	public static String toRGBCodeAWT(Color color )
    {
        return String.format( "#%02X%02X%02X",
            ( color.getRed()),
            ( color.getGreen()),
            ( color.getBlue()) );
    }
	
	/**
	 * Method that gets the pixel from the heat map image and sends the rgb
	 * @param img
	 * @param x
	 * @param y
	 * @return
	 */
	public static int[] getPixelData(BufferedImage img, int x, int y){
		int argb= img.getRGB(x, y);
		int rgb[] = new int[]{
				(argb >> 16) & 0xff,
				(argb >> 8) & 0xff,
				(argb     ) & 0xff
		};
		
		return rgb;
	}
	
	/**
	 * returns the max value of an observable list of interval data
	 * @param intervalDataList
	 * @return
	 */
	public static double getMaxValueConsumpion(ObservableList<IntervalData> intervalDataList) {
		double maxValue = 0;

		for (IntervalData data : intervalDataList) {
			if (data.getkW() > maxValue) {
				maxValue = data.getkW();
			}
		}

		return maxValue;
	}
	
	/**
	 * returns the max value of an observable list
	 * @param intervalDataList
	 * @return
	 */
	public static double getMaxValueGeneration(ObservableList<IntervalData> intervalDataList) {
		double maxValue = 0;

		for (IntervalData data : intervalDataList) {
			if (data.getGenkW() > maxValue) {
				maxValue = data.getGenkW();
			}
		}

		return maxValue;
	}
	
	
	/**
	 * This method will return the list of TMY3 data from the NREL Website
	 * this method is called from the root layout controller at the initialize();
	 * @return 
	 * @throws IOException 
	 */
	synchronized public static ObservableList<TMY3DataDirectory> getTMY3ListFromNREL(String key) {
		
		Document doc=null;
		try {
			doc = Jsoup.connect(Constants.NREL_URL_USAFN).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//get the value which returns a to letter state i.e. CA for California
		String value = Constants.STATE_MAP.get(key);
		
		Elements links = doc.select("a[href]");
		ObservableList<TMY3DataDirectory> tmy3Directories = FXCollections.observableArrayList();
		for (int i = 0; i < links.size()-2; i++){
			Element row = links.get(i).parent().parent();
			Element el = row.select("td").get(3);
			TMY3DataDirectory tmy3Dir = Assistant.getListItemParsed(el.text(), links.get(i).text(), links.get(i).absUrl("href"));
			//if the value sent equals o the state then added it to the list
			if(value.equals(tmy3Dir.getState())){
				tmy3Directories.add(tmy3Dir);
			}
			
		}
		//sort the list alphabetically
		Collections.sort(tmy3Directories, new Comparator<TMY3DataDirectory>() {
			public int compare(TMY3DataDirectory o1, TMY3DataDirectory o2) {
				return o1.getLocation().compareTo(o2.getLocation());
			}
		});
		return tmy3Directories;
		
	}

	/**
	 * Method that simply handles the strings read by the tool, then exports it as first
	 * 
	 * @param text
	 * @param usafn 
	 * @param url 
	 * @return
	 */
	private static TMY3DataDirectory getListItemParsed(String location, String usafn, String url) {
		String stateStr = location.substring(Math.max(location.length() -2, 0));
		
		String infoStr = location.substring(0,location.length() -3);
		
		
		String newInfo = stateStr + "," + infoStr;
		TMY3DataDirectory dir = new TMY3DataDirectory(usafn, newInfo, url,stateStr);
		
		return dir;
	}
}
