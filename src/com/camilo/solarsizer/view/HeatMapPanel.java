package com.camilo.solarsizer.view;

/**
 * This is a panel that displays the heat map of the interval Data
 * @author Jose Camilo Uzquiano
 *
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jsoup.Jsoup;

import com.camilo.solarsizer.SolarSizerAppMain;
import com.camilo.solarsizer.factories.ChartComponentsFactory;
import com.camilo.solarsizer.model.Meter;
import com.camilo.solarsizer.model.XYZConsumptionArrayDataset;
import com.camilo.solarsizer.model.XYZGenerationArrayDataset;
import com.camilo.solarsizer.util.Assistant;
import com.camilo.solarsizer.util.DateNumberFormat;

public class HeatMapPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	// graphics, chart and chart components instances
	private ChartPanel chartPanel;
	private JFreeChart timeSeriesChart;
	private XYPlot timeSeriesPlot;
	private NumberAxis dateAxis;
	private NumberAxis scaleAxis;
	private XYBlockRenderer consumptionRenderer;
	private XYZDataset consumptionDataset, generationDataset;
	private PaintScaleLegend legend;
	

	// String instances
	private String chartTitle, xAxisLabel, yAxisLabel;

	public HeatMapPanel() {
		chartTitle = "TimeDisplay Chart";
		xAxisLabel = "Date";
		yAxisLabel = "Time";
		consumptionDataset = new XYZConsumptionArrayDataset();
		generationDataset =new XYZGenerationArrayDataset();

		timeSeriesChart = ChartComponentsFactory.createBlockChart(chartTitle, xAxisLabel, yAxisLabel,
				consumptionDataset, consumptionRenderer, "15 minutes");

		timeSeriesPlot = timeSeriesChart.getXYPlot();

		consumptionRenderer = (XYBlockRenderer) timeSeriesPlot.getRenderer();

		dateAxis = (NumberAxis) timeSeriesPlot.getDomainAxis();
		dateAxis.setVerticalTickLabels(true);

		NumberAxis axis = (NumberAxis) timeSeriesPlot.getRangeAxis();

		axis.setAutoRangeIncludesZero(true);

		timeSeriesPlot.setDataset(consumptionDataset);

		timeSeriesPlot.setRenderer(consumptionRenderer);

		chartPanel = new ChartPanel(timeSeriesChart, true);
		
		//set the listener to send the event directly to the energyOverviewController
		chartPanel.addChartMouseListener(SolarSizerAppMain.getController());
		createScaleLegend();
		setLayout(new BorderLayout());
		this.add(chartPanel, BorderLayout.CENTER);
	}

	/**
	 * Method that creates the scale legend
	 */
	private void createScaleLegend() {
		// Setting up the scale axis
		scaleAxis = new NumberAxis("Scale");
		scaleAxis.setAxisLinePaint(Color.white);
		scaleAxis.setTickMarkPaint(Color.white);
		scaleAxis.setTickLabelFont(new Font("Dialog", Font.PLAIN, 7));
		scaleAxis.setTickLabelPaint(Color.WHITE);
		scaleAxis.setRange(0, 1);

		// setting up the legend
		LookupPaintScale ps = ChartComponentsFactory.createHeatMapGradient(0, 1);
		legend = new PaintScaleLegend(ps, scaleAxis);
		legend.setScale(ps);
		legend.setAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
		legend.setAxisOffset(5.0);
		legend.setMargin(new RectangleInsets(5, 5, 5, 5));
		legend.setPadding(new RectangleInsets(10, 10, 10, 10));
		legend.setStripWidth(10);
		legend.setPosition(RectangleEdge.RIGHT);
		legend.setBackgroundPaint(Color.BLACK);

		chartPanel.getChart().addSubtitle(legend);

		consumptionRenderer.setPaintScale(ps);
		
	}

	public void addHeatMapConsumption(XYZDataset arrayDataset, String timeDisplay, double maxValue, double minValue,
			Meter meter) {
		consumptionDataset = arrayDataset;
		
		DateNumberFormat format = new DateNumberFormat();
		format.setInitDateValue(meter.getStartDate().getTime());
		dateAxis.setNumberFormatOverride(format);
		// handle if they are equal
		LookupPaintScale ps = null;
		if (minValue == maxValue) { // if equal create a legend between 0 and 1
			ps = ChartComponentsFactory.createHeatMapGradient(0, 1);
			scaleAxis.setRange(0, 1);
		} else { // if not regular
			ps = ChartComponentsFactory.createHeatMapGradient(minValue, maxValue);
			scaleAxis.setRange(0, Assistant.getMaxValueConsumpion(meter.getIntervalDataList()));
		}

		legend.setScale(ps);

		consumptionRenderer.setPaintScale(ps);

		consumptionRenderer.setBlockHeight(1f);
		consumptionRenderer.setBlockWidth(1f);
		timeSeriesPlot.setDataset(consumptionDataset);

		chartPanel.update(getGraphics());
	}

	/**
	 * This method adds the heat map generation to the block chart
	 * @param arrayDataset
	 * @param timeDisplay
	 * @param maxValue
	 * @param minValue
	 * @param meter
	 */
	public void addHeatMapGeneration(XYZDataset arrayDataset, String timeDisplay, double maxValue, double minValue,
			Meter meter) {
		generationDataset = arrayDataset;
		
		DateNumberFormat format = new DateNumberFormat();
		format.setInitDateValue(meter.getStartDate().getTime());
		dateAxis.setNumberFormatOverride(format);
		// handle if they are equal
		LookupPaintScale ps = null;
		if (minValue == maxValue) { // if equal create a legend between 0 and 1
			ps = ChartComponentsFactory.createHeatMapGradient(0, 1);
			scaleAxis.setRange(0, 1);
		} else { // if not regular
			ps = ChartComponentsFactory.createHeatMapGradient(minValue, maxValue);
			scaleAxis.setRange(0, Assistant.getMaxValueGeneration(meter.getIntervalDataList()));
		}

		legend.setScale(ps);

		consumptionRenderer.setPaintScale(ps);

		consumptionRenderer.setBlockHeight(0.99f);
		consumptionRenderer.setBlockWidth(0.99f);

		timeSeriesPlot.setDataset(generationDataset);
		chartPanel.update(getGraphics());
		
		
	}

	/**
	 * This method simply removes the heat map from 
	 */
	public void removeHeatMap() {
		timeSeriesPlot.setDataset(null);
		scaleAxis.setRange(0, 1);
		chartPanel.update(getGraphics());
	}

	public JFreeChart getTimeSeriesChart() {
		return timeSeriesChart;
	}

	public void setTimeSeriesChart(JFreeChart timeSeriesChart) {
		this.timeSeriesChart = timeSeriesChart;
	}

}
