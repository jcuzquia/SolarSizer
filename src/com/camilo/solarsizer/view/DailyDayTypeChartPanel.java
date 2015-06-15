package com.camilo.solarsizer.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;

import javafx.collections.ObservableList;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
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
import com.camilo.solarsizer.model.DailyData;
import com.camilo.solarsizer.model.Meter;

/*
 * This class handles the display of daily data and separates it
 * by the type of day
 * @author Jose Camilo Uzquiano
 *
 */
public class DailyDayTypeChartPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	// graphics, chart and chart components instances
	private ChartPanel chartPanel;
	private JFreeChart timeSeriesChart;
	private XYPlot timeSeriesPlot;
	private DateAxis dateAxis;
	private TimeSeriesCollection schoolDayCollection, summerDayCollection, holidayCollection, weekendCollection;
	private XYLineAndShapeRenderer schoolDayRenderer, summerDayRenderer, holidayRenderer, weekendRenderer;

	// String instances
	private String chartTitle, xAxisLabel, yAxisLabel;

	// renderer instances
	private float monthlyThickness, dailyThickness, hourlyThickness, intervalThickness;

	// marker
	private Marker startMarker, endMarker;

	@SuppressWarnings("deprecation")
	public DailyDayTypeChartPanel() {
		// create the series collection
		summerDayCollection = new TimeSeriesCollection();
		schoolDayCollection = new TimeSeriesCollection();
		holidayCollection = new TimeSeriesCollection();
		weekendCollection = new TimeSeriesCollection();
		chartTitle = "Daily Interval Data by DayType";
		xAxisLabel = "Time";
		yAxisLabel = "Consumption kWh";

		intervalThickness = 0.3f;
		hourlyThickness = 1f;
		dailyThickness = 1.5f;
		monthlyThickness = 4f;

		timeSeriesChart = ChartComponentsFactory.createDayTypeTimeSeries(chartTitle, xAxisLabel, yAxisLabel,
				schoolDayCollection, "15 minutes");
		timeSeriesPlot = timeSeriesChart.getXYPlot();

		setUpRenderers();

		dateAxis = (DateAxis) timeSeriesPlot.getDomainAxis();
		dateAxis.setVerticalTickLabels(true);

		NumberAxis axis = (NumberAxis) timeSeriesPlot.getRangeAxis();
		axis.setAutoRangeIncludesZero(true);

		// map the dataset
		timeSeriesPlot.setDataset(0, schoolDayCollection);
		timeSeriesPlot.setDataset(1, weekendCollection);
		timeSeriesPlot.setDataset(2, summerDayCollection);
		timeSeriesPlot.setDataset(3, holidayCollection);

		// map the renderers
		timeSeriesPlot.setRenderer(0, schoolDayRenderer);
		timeSeriesPlot.setRenderer(1, weekendRenderer);
		timeSeriesPlot.setRenderer(2, summerDayRenderer);
		timeSeriesPlot.setRenderer(3, holidayRenderer);

		chartPanel = new ChartPanel(timeSeriesChart, true);
		setLayout(new BorderLayout());
		this.add(chartPanel, BorderLayout.CENTER);

	}

	/**
	 * Simple method called from the constructor to set up the renderers
	 */
	@SuppressWarnings("deprecation")
	private void setUpRenderers() {
		schoolDayRenderer = new XYLineAndShapeRenderer();
		schoolDayRenderer.setStroke(new BasicStroke(intervalThickness));
		schoolDayRenderer.setPaint(Color.GREEN);
		schoolDayRenderer.setShapesVisible(false);

		summerDayRenderer = new XYLineAndShapeRenderer();
		summerDayRenderer.setStroke(new BasicStroke(intervalThickness));
		summerDayRenderer.setPaint(Color.YELLOW);
		summerDayRenderer.setShapesVisible(false);

		holidayRenderer = new XYLineAndShapeRenderer();
		holidayRenderer.setStroke(new BasicStroke(intervalThickness));
		holidayRenderer.setPaint(Color.RED);
		holidayRenderer.setShapesVisible(false);

		weekendRenderer = new XYLineAndShapeRenderer();
		weekendRenderer.setStroke(new BasicStroke(intervalThickness));
		weekendRenderer.setPaint(Color.WHITE);
		weekendRenderer.setShapesVisible(false);

	}

	/**
	 * This method adds the simple consumption to the chart, the chart displays
	 * it
	 * 
	 * @param consumptionSeries
	 * @param timeDisplay
	 */
	public void addConsumption(TimeSeries consumptionSeries, String timeDisplay) {

	}

	/**
	 * THis is called from the controller after the meter has been imported
	 * @param dailyDataList
	 */
	public void addNewSeries(ObservableList<DailyData> dailyDataList) {
		ArrayList<TimeSeriesCollection> timeSeriesCollectionsList = new ArrayList<>();

		timeSeriesCollectionsList = ChartComponentsFactory.createDailyCollectionSeries(dailyDataList);
		schoolDayCollection = timeSeriesCollectionsList.get(0);
		weekendCollection = timeSeriesCollectionsList.get(1);
		summerDayCollection = timeSeriesCollectionsList.get(2);
		holidayCollection = timeSeriesCollectionsList.get(3);

		// map the dataset
		timeSeriesPlot.setDataset(0, schoolDayCollection);
		timeSeriesPlot.setDataset(1, weekendCollection);
		timeSeriesPlot.setDataset(2, summerDayCollection);
		timeSeriesPlot.setDataset(3, holidayCollection);

	}

	public void updateColors(int index, Color color) {
		XYItemRenderer renderer = timeSeriesPlot.getRenderer(0);
		renderer.setSeriesPaint(index, color);
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

	public ChartPanel getChartPanel() {
		return chartPanel;
	}

	public void setChartPanel(ChartPanel chartPanel) {
		this.chartPanel = chartPanel;
	}

	/**
	 * Method that filters only schooldays to be shown
	 */
	public void filterSchoolDays() {
		timeSeriesPlot.setDataset(0,schoolDayCollection);
		timeSeriesPlot.setDataset(1, null);
		timeSeriesPlot.setDataset(2, null);
		timeSeriesPlot.setDataset(3, null);
	}
	
	/**
	 * Method that filters only weekends to be shown
	 */
	public void filterWeekends() {
		timeSeriesPlot.setDataset(0, null);
		timeSeriesPlot.setDataset(1, weekendCollection);
		timeSeriesPlot.setDataset(2, null);
		timeSeriesPlot.setDataset(3, null);
	}
	
	/**
	 * Method that filters only weekends to be shown
	 */
	public void filterSummerDays() {
		timeSeriesPlot.setDataset(0, null);
		timeSeriesPlot.setDataset(1, null);
		timeSeriesPlot.setDataset(2, summerDayCollection);
		timeSeriesPlot.setDataset(3, null);
	}
	
	/**
	 * Method that filters only weekends to be shown
	 */
	public void filterHolidays() {
		timeSeriesPlot.setDataset(0, null);
		timeSeriesPlot.setDataset(1, null);
		timeSeriesPlot.setDataset(2, null);
		timeSeriesPlot.setDataset(3, holidayCollection);
	}

	public void includeAllDays() {
		timeSeriesPlot.setDataset(0, schoolDayCollection);
		timeSeriesPlot.setDataset(1, weekendCollection);
		timeSeriesPlot.setDataset(2, summerDayCollection);
		timeSeriesPlot.setDataset(3, holidayCollection);
	}
}
