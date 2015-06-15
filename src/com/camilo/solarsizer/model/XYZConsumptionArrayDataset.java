package com.camilo.solarsizer.model;

import javafx.collections.ObservableList;

import org.jfree.data.xy.AbstractXYZDataset;

/**
 * This class is an abstractXYZ dataset which is the dataset used to create the heat map
 * @author Uzquianoj1
 *
 */
public class XYZConsumptionArrayDataset extends AbstractXYZDataset {

	private static final long serialVersionUID = 2926377851027954860L;
	private ObservableList<DailyData> intervalDataList;
	private int rowCount = 0;
	private int columnCount = 0;
	private IntervalData[][] data;

	public XYZConsumptionArrayDataset(String type, ObservableList<DailyData> dayTimeInterval) {
		this.intervalDataList = dayTimeInterval;
		rowCount = dayTimeInterval.size();
		columnCount = dayTimeInterval.get(0).getDailyIntervalList().size();

		data = new IntervalData[rowCount + 1][columnCount + 1];
		createArray();

	}
	
	public XYZConsumptionArrayDataset(){
		
	}

	private void createArray() {
		int rc = 0;
		for (DailyData dayDataList : intervalDataList) {

			int cc = 0;
			for (IntervalData dataTemp : dayDataList.getDailyIntervalList()) {
				data[rc][cc] = dataTemp;
				cc++;
			}
			rc++;
		}
	}

	@Override
	public int getSeriesCount() {
		return 1;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Comparable getSeriesKey(int series) {
		return series;
	}

	@Override
	public int getItemCount(int series) {
		return rowCount * columnCount;
	}

	@Override
	public Number getX(int series, int item) {
		// return new double((int) item/columnCount);
		int x = item / columnCount;
		return x;
	}

	@Override
	public Double getY(int series, int item) {
		return new Double(item % columnCount);
	}

	/**
	 * this method is the main method in which Z is assigned in order
	 * for each specific point in the heat map
	 */
	@Override
	public Number getZ(int series, int item) {
		try {
			return new Double(data[item / columnCount][item % columnCount].getkW());
		} catch (Exception e) {

		}
		return new Double(0);
	}

	

	@Override
	public double getYValue(int series, int item) {
		// TODO Auto-generated method stub
		return super.getYValue(series, item);
	}

}
