package com.camilo.solarsizer.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Calendar;

import javafx.collections.ObservableList;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPosition;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.TimeSeries;

import com.camilo.solarsizer.factories.ChartComponentsFactory;
import com.camilo.solarsizer.model.DailyData;
import com.camilo.solarsizer.model.Meter;

/**
 * This Panel draws all the interval monthly data as a barchart and handles all of
 * the drawing
 * 
 * @author Camilo Uzquiano
 *
 */
public class MonthlyBarChartPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	// graphics, chart and chart components instances
	private ChartPanel chartPanel;
	private JFreeChart barChart;
	private CategoryPlot categoryPlot;
	private CategoryAxis dateAxis;
	private CategoryDataset monthlyConsumptionDataset;
	private XYLineAndShapeRenderer consumptionRenderer, temperatureRenderer;

	// String instances
	private String chartTitle, xAxisLabel, yAxisLabel;

	// marker
	private Marker startMarker, endMarker;
	private int index;

	public MonthlyBarChartPanel() {
		
		chartTitle = "Monthly";
		xAxisLabel = "Month";
		yAxisLabel = "Consumption kWh";


		monthlyConsumptionDataset = new DefaultCategoryDataset();
		barChart = ChartComponentsFactory.createBarChart(chartTitle, xAxisLabel, yAxisLabel, monthlyConsumptionDataset, consumptionRenderer, "Monthly");
		categoryPlot = barChart.getCategoryPlot();
		
		categoryPlot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);


		dateAxis = (CategoryAxis) categoryPlot.getDomainAxis();
		dateAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		
		NumberAxis axis = (NumberAxis) categoryPlot.getRangeAxis();
		axis.setAutoRangeIncludesZero(true);

//		categoryPlot.setDataset(0, monthlyConsumptionDataset);
//		categoryPlot.mapDatasetToRangeAxis(0, 0);

//		categoryPlot.setDataset(1, temperatureCollection);
//		categoryPlot.mapDatasetToRangeAxis(1, 1);

//		categoryPlot.setRenderer(0, consumptionRenderer);
//		categoryPlot.setRenderer(1, temperatureRenderer);

		chartPanel = new ChartPanel(barChart, true);
		setLayout(new BorderLayout());
		this.add(chartPanel, BorderLayout.CENTER);

	}

	/**
	 * This method adds the simple consumption to the chart, 
	 * this is called from the EnergyOverviewController
	 * @param timeDisplay
	 */
	public void addNewMonthlySeries(ObservableList<DailyData> dailyDataList) {
		DefaultCategoryDataset monthlyDataset = ChartComponentsFactory.createMonthlyCategoryDataset(dailyDataList);
		categoryPlot.setDataset(monthlyDataset);
	}

	public void updateColors(int index, Color color) {
		
	}

	/**
	 * This method sets line of the meter invisible if it is deactivated in the table
	 * @param index
	 * @param meter
	 */
	public void activateSeries(int index, Meter meter) {

	}

	/**
	 * This is a method that sets the marker given the calendar
	 * @param calendar
	 */
	public void setMarker(Calendar calendar) {
//		long millis = calendar.getTimeInMillis();
//		categoryPlot.removeDomainMarker(startMarker);
//		startMarker = new ValueMarker(millis);
//		startMarker.setStroke(new BasicStroke(2f));
//		startMarker.setPaint(Color.RED);
//		startMarker.setLabelPaint(Color.RED);
//
//		startMarker.setLabelAnchor(RectangleAnchor.TOP_LEFT);
//		startMarker.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
//		categoryPlot.addDomainMarker(startMarker);
//
//		calendar.add(Calendar.MONTH, -1);
//
//		long minusYear = calendar.getTimeInMillis();
//		categoryPlot.removeDomainMarker(endMarker);
//		endMarker = new ValueMarker(minusYear);
//		endMarker.setStroke(new BasicStroke(2f));
//		endMarker.setPaint(Color.RED);
//		endMarker.setLabelPaint(Color.RED);
//
//		endMarker.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
//		endMarker.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
//		categoryPlot.addDomainMarker(endMarker);
	}

	/**
	 * Method that handles the deletion of the meter in the plot
	 * 
	 * @param selectedIndex
	 */
	public void removeMeter(int selectedIndex) {
		categoryPlot.setDataset(null);
	}

	public ChartPanel getChartPanel() {
		return chartPanel;
	}

	public void setChartPanel(ChartPanel chartPanel) {
		this.chartPanel = chartPanel;
	}
	
	

}
