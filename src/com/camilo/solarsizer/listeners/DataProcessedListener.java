package com.camilo.solarsizer.listeners;

import com.camilo.solarsizer.events.DataProcessedEvent;

public interface DataProcessedListener {

	public void processedIntervalDataEmitted(DataProcessedEvent event);
}
