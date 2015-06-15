package com.camilo.solarsizer.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import com.camilo.solarsizer.factories.ChartComponentsFactory;
import com.camilo.solarsizer.model.Meter;

/**
 * This Panel draws all the interval data as a time series and handles all of
 * the drawing
 * 
 * @author Camilo Uzquiano
 *
 */
public class IntervalGraphPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	// graphics, chart and chart components instances
	private ChartPanel chartPanel;
	private JFreeChart timeSeriesChart;
	private XYPlot timeSeriesPlot;
	private DateAxis dateAxis;
	private NumberAxis temperatureAxis;
	private TimeSeriesCollection consumptionCollection, temperatureCollection;
	private XYLineAndShapeRenderer consumptionRenderer, temperatureRenderer;

	// String instances
	private String chartTitle, xAxisLabel, yAxisLabel;

	// renderer instances
	private float monthlyThickness, dailyThickness, hourlyThickness, intervalThickness;

	// marker
	private Marker startMarker, endMarker;
	private int index;

	@SuppressWarnings("deprecation")
	public IntervalGraphPanel() {
		consumptionCollection = new TimeSeriesCollection();
		temperatureCollection = new TimeSeriesCollection();
		chartTitle = "Interval Data";
		xAxisLabel = "Date";
		yAxisLabel = "Consumption kW";

		intervalThickness = 0.1f;
		hourlyThickness = 1f;
		dailyThickness = 1.5f;
		monthlyThickness = 4f;

		timeSeriesChart = ChartComponentsFactory.createTimeSeriesChart(chartTitle, xAxisLabel, yAxisLabel,
				consumptionCollection, consumptionRenderer, "15 minutes");
		timeSeriesPlot = timeSeriesChart.getXYPlot();

		temperatureAxis = new NumberAxis("Degrees F");
		temperatureAxis.setAutoRangeIncludesZero(true);
		temperatureAxis.setAxisLinePaint(Color.WHITE);
		temperatureAxis.setLabelPaint(Color.WHITE);
		temperatureAxis.setTickLabelPaint(Color.WHITE);
		timeSeriesPlot.setRangeAxis(1, temperatureAxis);
		timeSeriesPlot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);

		consumptionRenderer = (XYLineAndShapeRenderer) timeSeriesPlot.getRenderer();
		consumptionRenderer.setStroke(new BasicStroke(intervalThickness));

		temperatureRenderer = new XYLineAndShapeRenderer();
		temperatureRenderer.setShapesVisible(false);
		temperatureRenderer.setPaint(new Color(200, 200, 200));
		temperatureRenderer.setStroke(new BasicStroke(hourlyThickness));

		dateAxis = (DateAxis) timeSeriesPlot.getDomainAxis();
		dateAxis.setVerticalTickLabels(true);

		NumberAxis axis = (NumberAxis) timeSeriesPlot.getRangeAxis();
		axis.setAutoRangeIncludesZero(true);

		timeSeriesPlot.setDataset(0, consumptionCollection);
		timeSeriesPlot.mapDatasetToRangeAxis(0, 0);

		timeSeriesPlot.setDataset(1, temperatureCollection);
		timeSeriesPlot.mapDatasetToRangeAxis(1, 1);

		timeSeriesPlot.setRenderer(0, consumptionRenderer);
		timeSeriesPlot.setRenderer(1, temperatureRenderer);

		chartPanel = new ChartPanel(timeSeriesChart, true);
		setLayout(new BorderLayout());
		this.add(chartPanel, BorderLayout.CENTER);

	}

	@SuppressWarnings("deprecation")
	/**
	 * This method adds the simple consumption to the chart, the chart displays it
	 * @param consumptionSeries
	 * @param timeDisplay
	 */
	public void addConsumption(TimeSeries consumptionSeries, String timeDisplay) {
		consumptionCollection.removeAllSeries();
		consumptionCollection.addSeries(consumptionSeries);
		switch (timeDisplay) {
		case ("monthly"):
			consumptionRenderer.setOutlineStroke(new BasicStroke(monthlyThickness));
			dateAxis.setDateFormatOverride(new SimpleDateFormat("MMM-dd"));
			break;
		case ("daily"):
			consumptionRenderer.setOutlineStroke(new BasicStroke(dailyThickness));
			dateAxis.setDateFormatOverride(new SimpleDateFormat("MMM-dd-yy"));
			break;
		case ("hourly"):
			consumptionRenderer.setOutlineStroke(new BasicStroke(hourlyThickness));
			dateAxis.setDateFormatOverride(new SimpleDateFormat("MMM-dd-yy ' ' HH:mm"));
			break;
		case ("15 minutes"):
			consumptionRenderer.setOutlineStroke(new BasicStroke(intervalThickness));
			dateAxis.setDateFormatOverride(new SimpleDateFormat("MMM-dd-yy ' ' HH:mm"));
			break;

		}
	}

	/**
	 * This method simply renders the temperature time series
	 * @param temperatureDataSeries
	 */
	public void addTemperatureSeries(TimeSeries temperatureDataSeries) {
		temperatureCollection.removeAllSeries();
		temperatureCollection.addSeries(temperatureDataSeries);
		dateAxis.setDateFormatOverride(new SimpleDateFormat("MMM-dd-yy ' ' HH:mm"));
	}

	@SuppressWarnings("deprecation")
	public void addNewSeries(Meter meter) {
		TimeSeries timeSeries = ChartComponentsFactory.createIntervalSeries(meter.getMeterNumber().get(),
				meter.getIntervalDataList());
		consumptionCollection.addSeries(timeSeries);
		consumptionRenderer.setSeriesPaint(index, meter.getColor());
		consumptionRenderer.setStroke(new BasicStroke(0.5f));
		// timeSeriesPlot.setRenderer(0,consumptionRenderer);
		index++;
	}

	public void updateColors(int index, Color color) {
		XYItemRenderer renderer = timeSeriesPlot.getRenderer(0);
		renderer.setSeriesPaint(index, color);
	}

	/**
	 * This method sets line of the meter invisible if it is deactivated in the table
	 * @param index
	 * @param meter
	 */
	public void activateSeries(int index, Meter meter) {

		if (meter.isActivated()) {
			XYItemRenderer renderer = timeSeriesPlot.getRenderer(0);
			renderer.setSeriesPaint(index, meter.getColor());
		} else {
			XYItemRenderer renderer = timeSeriesPlot.getRenderer(0);
			renderer.setSeriesPaint(index, new Color(255, 255, 255, 0));
		}
	}

	public void setMarker(Calendar calendar) {
		long millis = calendar.getTimeInMillis();
		timeSeriesPlot.removeDomainMarker(startMarker);
		startMarker = new ValueMarker(millis);
		startMarker.setStroke(new BasicStroke(2f));
		startMarker.setPaint(Color.RED);
		startMarker.setLabelPaint(Color.RED);

		startMarker.setLabelAnchor(RectangleAnchor.TOP_LEFT);
		startMarker.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
		timeSeriesPlot.addDomainMarker(startMarker);

		calendar.add(Calendar.MONTH, -1);

		long minusYear = calendar.getTimeInMillis();
		timeSeriesPlot.removeDomainMarker(endMarker);
		endMarker = new ValueMarker(minusYear);
		endMarker.setStroke(new BasicStroke(2f));
		endMarker.setPaint(Color.RED);
		endMarker.setLabelPaint(Color.RED);

		endMarker.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
		endMarker.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
		timeSeriesPlot.addDomainMarker(endMarker);
	}

	/**
	 * Method that handles the deletion of the meter in the plot
	 * 
	 * @param selectedIndex
	 */
	public void removeMeter(int selectedIndex) {
		consumptionCollection.removeSeries(selectedIndex);
	}

	public ChartPanel getChartPanel() {
		return chartPanel;
	}

	public void setChartPanel(ChartPanel chartPanel) {
		this.chartPanel = chartPanel;
	}
	
	

}
