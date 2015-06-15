package com.camilo.solarsizer.listeners;

import com.camilo.solarsizer.events.WeatherDataProcessedEvent;

/**
 * This interface listens whenever the data finished processing, 
 * it is called from the readWeatherDataTask class
 * @author Jose Camilo Uzquiano
 *
 */
public interface WeatherDataProcessedListener {

	public void processedWeatherDataEmitted(WeatherDataProcessedEvent event);
}
