package com.camilo.solarsizer.model;


import javafx.collections.ObservableList;

import org.jfree.data.xy.AbstractXYZDataset;
/**
 * 
 * @author Uzquianoj1
 *
 */
public class XYZGenerationArrayDataset extends AbstractXYZDataset{
	
	private static final long serialVersionUID = -8561495231572631991L;
	private ObservableList<DailyData> intervalDataList;
	private int rowCount = 0;
	private int columnCount = 0;
	private IntervalData[][] data;
	
	public XYZGenerationArrayDataset(String type,ObservableList<DailyData> dayTimeInterval) {
		this.intervalDataList = dayTimeInterval;
		rowCount = dayTimeInterval.size();
		columnCount = dayTimeInterval.get(0).getDailyIntervalList().size();
		
		data = new IntervalData[rowCount+1][columnCount+1];
		createArray();
		
	}
	
	public XYZGenerationArrayDataset() {
	}

	private void createArray() {
		int rc = 0;
		for(DailyData dayDataList: intervalDataList){
			
			int cc=0;
			for (IntervalData dataTemp : dayDataList.getDailyIntervalList()){
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
	public Comparable<Integer> getSeriesKey(int series) {
		return series;
	}
	
	@Override
	public int getItemCount(int series) {
		return rowCount*columnCount;
	}
	
	@Override
	public Number getX(int series, int item) {
		return new Double(item/columnCount);
	}
	
	@Override
	public Number getY(int series, int item) {
		return new Double(item % columnCount);
	}
	
	@Override
	public Number getZ(int series, int item) {
		
		try{
			return new Double(data[item/columnCount][item % columnCount].getGenkW());
		}catch (Exception e){
			
		}
		return new Double(0);
	}
}
