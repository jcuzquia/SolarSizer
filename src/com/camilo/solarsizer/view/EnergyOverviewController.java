package com.camilo.solarsizer.view;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.SwingUtilities;

import org.controlsfx.dialog.Dialogs;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.XYZDataset;

import com.camilo.solarsizer.SolarSizerAppMain;
import com.camilo.solarsizer.events.DataProcessedEvent;
import com.camilo.solarsizer.events.WeatherDataProcessedEvent;
import com.camilo.solarsizer.factories.ChartComponentsFactory;
import com.camilo.solarsizer.listeners.DataProcessedListener;
import com.camilo.solarsizer.listeners.WeatherDataProcessedListener;
import com.camilo.solarsizer.model.DailyData;
import com.camilo.solarsizer.model.DailyWeatherData;
import com.camilo.solarsizer.model.Database;
import com.camilo.solarsizer.model.DayType;
import com.camilo.solarsizer.model.IntervalData;
import com.camilo.solarsizer.model.Meter;
import com.camilo.solarsizer.model.WeatherData;
import com.camilo.solarsizer.model.XYZConsumptionArrayDataset;
import com.camilo.solarsizer.model.XYZGenerationArrayDataset;
import com.camilo.solarsizer.util.Assistant;
import com.camilo.solarsizer.util.BuildDayTimeDataTask;
import com.camilo.solarsizer.util.BuildWeatherMatrixTask;
import com.camilo.solarsizer.util.DateNumberFormat;
import com.camilo.solarsizer.util.TimeNumberFormat;

/**
 * Controller that handles all tasks within the window
 * 
 * @author Jose Camilo Uzquiano
 *
 */
@SuppressWarnings("deprecation")
public class EnergyOverviewController implements DataProcessedListener, WeatherDataProcessedListener,
		ChartMouseListener {

	@FXML
	private TableView<Meter> meterTable;

	@FXML
	private TableView<DailyData> dateTable;

	@FXML
	private TableColumn<DailyData, String> dateColumn;

	@FXML
	private TableColumn<DailyData, String> dayTypeColumn;

	@FXML
	private TableColumn<Meter, String> meterNumberColumn;

	@FXML
	private TableColumn<Meter, String> startDateColumn;

	@FXML
	private TableColumn<Meter, String> endDateColumn;

	@FXML
	private TableColumn<Meter, Boolean> activatedColumn;

	@FXML
	private TableColumn<Meter, Color> colorColumn;

	@FXML
	private Button addMeterButton, deleteMeterButton;

	@FXML
	private StackPane chartPane;

	@FXML
	private StackPane heatMapConsumption;

	@FXML
	private StackPane heatMapGeneration;

	@FXML
	private SplitPane mainSplitPane;

	@FXML
	private Label energyLabel; // label found in the heatMap tab

	@FXML
	private Label powerLabel; // label found in the heatMap tab

	@FXML
	private Label timeLabel; // label found in the heatMap tab

	@FXML
	private Label dateLabel; // label found in the heatMap tab

	@FXML
	private RadioButton monthlyRadioButton;

	@FXML
	private RadioButton fifteenMinRadioButton;

	@FXML
	private RadioButton hourlyRadioButton;

	@FXML
	private RadioButton dailyRadioButton;
	
	@FXML
	private RadioButton regularTimeSeriesRadioButton;
	
	@FXML
	private RadioButton scatterRadioButton;
	
	@FXML
	private RadioButton dailyTypeOfDayRadioButton;
	
	@FXML
	private RadioButton schoolDayRadioButton;
	
	@FXML
	private RadioButton weekendRadioButton;
	
	@FXML
	private RadioButton summerDayRadioButton;
	
	@FXML
	private RadioButton holidayRadioButton;
	
	@FXML 
	private RadioButton everyDayRadioButton;
	
	@FXML
	private ColorPicker schoolDayColorPicker;
	
	@FXML
	private ColorPicker weekendColorPicker;
	
	@FXML
	private ColorPicker summerDayColorPicker;
	
	@FXML
	private ColorPicker holidayColorPicker;

	// Where the chart is located
	private SwingNode chartNode;

	private ObservableList<DailyData> dailyIntervalData;
	private ObservableList<DailyWeatherData> dailyWeatherData;
	private ObservableList<IntervalData> intervalDataList;

	private Stage progressDialogStage;

	// reference to the database
	private Database database;

	// reference to the chartPanel
	private IntervalGraphPanel intervalGraphPanel;
	private DailyDayTypeChartPanel dailyDayTypeChartPanel;
	private MonthlyBarChartPanel barChartPanel;
	private HeatMapPanel consumptionHeatMap, generationHeatMap;

	/**
	 * Contructor of the controller
	 */
	public EnergyOverviewController() {

		dailyIntervalData = FXCollections.observableArrayList();
		dailyWeatherData = FXCollections.observableArrayList();
		intervalDataList = FXCollections.observableArrayList();
		chartNode = new SwingNode();

	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

		// setting up the radioButtons
		final ToggleGroup timeIntervalGroup = new ToggleGroup();
		monthlyRadioButton.setToggleGroup(timeIntervalGroup);
		dailyRadioButton.setToggleGroup(timeIntervalGroup);
		hourlyRadioButton.setToggleGroup(timeIntervalGroup);
		fifteenMinRadioButton.setToggleGroup(timeIntervalGroup);
		fifteenMinRadioButton.setSelected(true);
		
		final ToggleGroup typOfChartGroup = new ToggleGroup();
		regularTimeSeriesRadioButton.setToggleGroup(typOfChartGroup);
		scatterRadioButton.setToggleGroup(typOfChartGroup);
		dailyTypeOfDayRadioButton.setToggleGroup(typOfChartGroup);
		regularTimeSeriesRadioButton.setSelected(true);
		
		final ToggleGroup typeOfDayGroup = new ToggleGroup();
		schoolDayRadioButton.setToggleGroup(typeOfDayGroup);
		summerDayRadioButton.setToggleGroup(typeOfDayGroup);
		holidayRadioButton.setToggleGroup(typeOfDayGroup);
		weekendRadioButton.setToggleGroup(typeOfDayGroup);
		everyDayRadioButton.setToggleGroup(typeOfDayGroup);
		everyDayRadioButton.setSelected(true);
		
		
		
		updateEnabledButtons();
		
		// initialize a unique instance of the database
		database = Database.getInstance();
		// initialize the meter table with all the columns.
		meterNumberColumn.setCellValueFactory(cellData -> cellData.getValue().getMeterNumber());
		meterTable.setEditable(true);
		meterNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		meterNumberColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Meter, String>>() {
			@Override
			public void handle(CellEditEvent<Meter, String> event) {
				event.getTableView().getItems().get(event.getTablePosition().getRow())
						.setMeterNumber(Assistant.parsePropertiesString(event.getNewValue()));
			}
		});
		startDateColumn.setCellValueFactory(cellData -> Assistant.parsePropertiesString(cellData.getValue()
				.getStartDate().toString()));
		endDateColumn.setCellValueFactory(cellData -> Assistant.parsePropertiesString(cellData.getValue().getEndDate()
				.toString()));

		activatedColumn.setCellFactory(cellData -> new CheckBoxCell());

		colorColumn.setCellFactory(cellData -> new ColorButtonCell(java.awt.Color.GREEN));

		initializeDateTable();

	}

	/**
	 * This method initializes the DateColumn
	 */
	private void initializeDateTable() {
		// initialize datetable
		dateTable.setEditable(true);
		dateTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);;

		ContextMenu menu = new ContextMenu();
		MenuItem holidayChanger = new MenuItem("Holiday");
		MenuItem summerChanger = new MenuItem("Summer Day");
		MenuItem schoolDayChanger = new MenuItem("School Day");
		MenuItem weekendChanger = new MenuItem("Weekend");
		holidayChanger.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dateTable.getSelectionModel().getSelectedItem().setDayType(DayType.HOLIDAY);
				updateDayType(dateTable.getSelectionModel().getSelectedItems(), DayType.HOLIDAY);
				// update Column
				dateTable.getColumns().get(1).setVisible(false);
				dateTable.getColumns().get(1).setVisible(true);
				dailyDayTypeChartPanel.addNewSeries(dailyIntervalData);
			}
		});

		summerChanger.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dateTable.getSelectionModel().getSelectedItem().setDayType(DayType.SUMMER_DAY);
				updateDayType(dateTable.getSelectionModel().getSelectedItems(), DayType.SUMMER_DAY);
				// update Column
				dateTable.getColumns().get(1).setVisible(false);
				dateTable.getColumns().get(1).setVisible(true);
				dailyDayTypeChartPanel.addNewSeries(dailyIntervalData);
			}
		});
		
		schoolDayChanger.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dateTable.getSelectionModel().getSelectedItem().setDayType(DayType.SCHOOL_DAY);
				updateDayType(dateTable.getSelectionModel().getSelectedItems(), DayType.SCHOOL_DAY);
				// update Column
				dateTable.getColumns().get(1).setVisible(false);
				dateTable.getColumns().get(1).setVisible(true);
				dailyDayTypeChartPanel.addNewSeries(dailyIntervalData);
			}
		});
		
		weekendChanger.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dateTable.getSelectionModel().getSelectedItem().setDayType(DayType.WEEKEND);
				updateDayType(dateTable.getSelectionModel().getSelectedItems(), DayType.WEEKEND);
				// update Column
				dateTable.getColumns().get(1).setVisible(false);
				dateTable.getColumns().get(1).setVisible(true);
				dailyDayTypeChartPanel.addNewSeries(dailyIntervalData);
			}
		});
		
		
		
		menu.getItems().add(holidayChanger);
		menu.getItems().add(summerChanger);
		menu.getItems().add(schoolDayChanger);
		menu.getItems().add(weekendChanger);

		dateTable.setContextMenu(menu);

		dateColumn.setCellValueFactory(cellData -> Assistant.parsePropertiesString(cellData.getValue().getDate()
				.toString()));

		dayTypeColumn.setCellValueFactory(cellData -> Assistant.parsePropertiesString(cellData.getValue().getDayType()
				.toString()));

	}

	/**
	 * This method handles the switch of the daytype for multiple selections
	 * @param selectedItems
	 */
	protected void updateDayType(ObservableList<DailyData> selectedItems, DayType dayType) {
		for(DailyData data : selectedItems){
			data.setDayType(dayType);
		}
	}

	/**
	 * Handles the meter import once the btn has been clicked
	 */
	@FXML
	private void handleMeterImport() {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Import a new meter");

		// set the extension filter
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

		// show open file dialog
		File file = fileChooser.showOpenDialog(SolarSizerAppMain.getPrimaryStage());
		// starts the progress and process of reading only if a file is selected
		if (file != null) {
			showProgressDialog(file);
		}

	}

	/**
	 * Shows the progress dialog and start the progress of all the tasks while
	 * reading the file
	 * 
	 * @param file
	 */
	public void showProgressDialog(File file) {

		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(SolarSizerAppMain.class.getResource("view/ProgressWindow.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			progressDialogStage = new Stage();
			progressDialogStage.setTitle("Working...");
			progressDialogStage.initModality(Modality.WINDOW_MODAL);
			Scene scene = new Scene(page);
			progressDialogStage.setScene(scene);

			// Set the person into the controller.
			ProgressDialogController controller = loader.getController();
			controller.setFile(file);
			controller.setDataProcessedListener(this);
			// Show the dialog and wait until the user closes it
			progressDialogStage.show();
			// calls to process data
			controller.processData();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method that displays the Time series 15 minute interval Panel
	 */
	public void showChartPanel() {

		SwingNode heatMapConsumtionNode = new SwingNode();
		SwingNode heatMapGenerationNode = new SwingNode();

		chartPane.getChildren().add(chartNode);
		heatMapConsumption.getChildren().add(heatMapConsumtionNode);
		heatMapGeneration.getChildren().add(heatMapGenerationNode);
		createSwingContent(heatMapConsumtionNode, heatMapGenerationNode);
	}

	/**
	 * This method creates a swing node that outputs the chart
	 * 
	 * @param chartNode
	 */
	private void createSwingContent(SwingNode heatMapConsumtionNode, SwingNode heatMapGenerationNode) {

		try {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {

					intervalGraphPanel = new IntervalGraphPanel();
					consumptionHeatMap = new HeatMapPanel();
					generationHeatMap = new HeatMapPanel();
					barChartPanel = new MonthlyBarChartPanel();
					dailyDayTypeChartPanel = new DailyDayTypeChartPanel();

					chartNode.setContent(intervalGraphPanel);
					heatMapConsumtionNode.setContent(consumptionHeatMap);
					heatMapGenerationNode.setContent(generationHeatMap);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This handles the deletion of a meter or multiple meters
	 */
	@FXML
	private void handleDeleteMeter() {
		int selectedIndex = meterTable.getSelectionModel().getSelectedIndex();
		consumptionHeatMap.removeHeatMap();
		generationHeatMap.removeHeatMap();
		// if something is selected
		if (selectedIndex >= 0) {
			meterTable.getItems().remove(selectedIndex);
			meterTable.getSelectionModel().clearSelection(selectedIndex);

		} else {
			// nothing is selected
			Dialogs.create().title("No selection").masthead("No meter selected")
					.message("Please select a meter in the table").showError();
		}

		// TODO: handles whenever there are no meters left, there is a glitch
		// with the columns being fixed

		intervalGraphPanel.removeMeter(selectedIndex);
		barChartPanel.removeMeter(selectedIndex);
	}

	/**
	 * This is called when the processor has finished doing all the work then
	 * starts putting the charting effect Adds a new meter to the database
	 * 
	 * @param event
	 */
	@Override
	public void processedIntervalDataEmitted(DataProcessedEvent event) {
		progressDialogStage.close();
		intervalDataList = event.getIntervalList();
		BuildDayTimeDataTask dayArrayTask = new BuildDayTimeDataTask(intervalDataList);
		ExecutorService executor = Executors.newSingleThreadExecutor();

		dayArrayTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				ObservableList<DailyData> dayTimeInterval = null;
				try {
					dayTimeInterval = dayArrayTask.get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				dailyIntervalData = FXCollections.observableArrayList(dayTimeInterval);
				
				Meter meter = new Meter(intervalDataList);
				database.addMeter(meter);
				meterTable.setItems(database.getMeters());
				intervalGraphPanel.addNewSeries(meter);
				dailyDayTypeChartPanel.addNewSeries(dayTimeInterval);
				barChartPanel.addNewMonthlySeries(dailyIntervalData);
				// we set the variable dayTimeInterval in the controller so it
				// can be called from outside whenever necessary
				XYZDataset dataset = new XYZConsumptionArrayDataset("consumption", dayTimeInterval);
				consumptionHeatMap.addHeatMapConsumption(dataset, "consumption",
						Assistant.getMaxValueConsumpion(intervalDataList), 0, meter);
				XYZDataset genDataset = new XYZGenerationArrayDataset("generation", dayTimeInterval);
				generationHeatMap.addHeatMapGeneration(genDataset, "generation",
						Assistant.getMaxValueGeneration(intervalDataList), 0, meter);
				populateDayTypeTable(dayTimeInterval);
			}

		});
		executor.execute(dayArrayTask);

	}

	/**
	 * This method populates the Date table with the daytype combobox
	 * 
	 * @param dailyIntervalData
	 * @param meter
	 */
	private void populateDayTypeTable(ObservableList<DailyData> dailyIntervalData) {

		dateTable.setItems(dailyIntervalData);
	}

	/**
	 * This method handles the change in color of a specific series
	 * 
	 * @param selectedIndex
	 * @param Color
	 */
	public void changeSeriesColor(int selectedIndex, java.awt.Color color) {
		intervalGraphPanel.updateColors(selectedIndex, color);
	}

	public Database getDatabase() {
		return database;
	}

	/**
	 * This method handles the checkboxes in the table
	 * 
	 * @param selectedIndex
	 * @param meter
	 */
	public void activateSeries(int selectedIndex, Meter meter) {
		intervalGraphPanel.activateSeries(selectedIndex, meter);
	}

	/**
	 * This is a method in which we get the interval data, at the moment it is
	 * called from the RootLayoutController to generate the heat maps as comma
	 * delimited textFiles.
	 * 
	 * @return daily interval data
	 */
	public ObservableList<DailyData> getDailyIntervalData() {
		return dailyIntervalData;
	}

	/**
	 * This method sets the interval data in the variables of the controller, so
	 * it can be later called from other classes.
	 * 
	 * @param dailyIntervalData
	 */
	public void setDailyIntervalData(ObservableList<DailyData> dailyIntervalData) {
		this.dailyIntervalData = dailyIntervalData;

	}

	/**
	 * This event is called when the data has finished being processed. Once it
	 * is it passes the processed data to this main controller the event is
	 * passed from WeatherDataProgressDialogClass
	 */
	@Override
	public void processedWeatherDataEmitted(WeatherDataProcessedEvent event) {
		ObservableList<WeatherData> weatherDataList = event.getWeatherDataList();
		// TODO build the daily matrix
		// TODO setOnSucceeded the matrix task

		TimeSeries weatherSeries = ChartComponentsFactory.createWeatherTimeSeries("NOAA", weatherDataList);
		intervalGraphPanel.addTemperatureSeries(weatherSeries);

		BuildWeatherMatrixTask weatherMatrixTask = new BuildWeatherMatrixTask(weatherDataList);
		ExecutorService executor = Executors.newSingleThreadExecutor();

		weatherMatrixTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				ObservableList<DailyWeatherData> dayTimeWeatherData = weatherMatrixTask.getDailyWeatherDataList();
				dailyWeatherData = dayTimeWeatherData;
				// we set the variable dayTimeInterval in the controller so it
				// can be called from outside whenever necessary
			}

		});
		executor.execute(weatherMatrixTask);
	}

	/**
	 * This method is called in order to get the matrix it is called from the
	 * export weather data menu in the rootLayoutController
	 * 
	 * @return dailyWeatherData matrix
	 */
	public ObservableList<DailyWeatherData> getDailyWeatherData() {
		return dailyWeatherData;
	}

	/**
	 * This method is called mainly to expor the interval data The menu item
	 * activates it and this list is used to print it as a Coma delimmited text
	 * file
	 * 
	 * @return intervalDataList
	 */
	public ObservableList<IntervalData> getIntervalDataList() {
		return intervalDataList;
	}

	@Override
	public void chartMouseClicked(ChartMouseEvent event) {
		// TODO Auto-generated method stub

	}

	/**
	 * This allows the user to see the consumption from the heat map when the
	 * mouse is being hover over it
	 */
	@Override
	public void chartMouseMoved(ChartMouseEvent event) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				XYItemEntity xyItem = (XYItemEntity) event.getEntity();
				if (xyItem != null) {
					XYZDataset dataset = (XYZDataset) xyItem.getDataset();
					double power = dataset.getZValue(xyItem.getSeriesIndex(), xyItem.getItem());
					double energy = dataset.getZValue(xyItem.getSeriesIndex(), xyItem.getItem()) / 4;
					double date = dataset.getXValue(xyItem.getSeriesIndex(), xyItem.getItem());
					double time = dataset.getYValue(xyItem.getSeriesIndex(), xyItem.getItem());
					DecimalFormat df = new DecimalFormat("###0.00");
					DateNumberFormat dnf = new DateNumberFormat();
					dnf.setInitDateValue(database.getMeters().get(0).getStartDate().getTime());
					TimeNumberFormat tnf = new TimeNumberFormat();
					powerLabel.setText(df.format(power));
					energyLabel.setText(df.format(energy));
					dateLabel.setText(dnf.format(date));
					timeLabel.setText(tnf.format(time));
				}

			}
		});

	}

	/**
	 * This method is called when the radio monthly button is pressed It makes
	 * the time series chart to become a regular monthly bar chart
	 */
	public void changeToMonthlyBarChart() {
		chartNode.setContent(barChartPanel);
		updateEnabledButtons();
	}

	/**
	 * This method switched the node content to the regular time series chart.
	 * It displays a regular time series of meters
	 */
	public void changeToFifteenMinuteIntervalChart() {
		chartNode.setContent(intervalGraphPanel);
		updateEnabledButtons();
	}

	/**
	 * This method switches the chart into hourly values
	 */
	public void changeToHourlyIntervalChart() {
		// TODO still need to develop the code for this
		updateEnabledButtons();
	}

	/**
	 * This method switches the chart into daily values
	 */
	public void changeToDailyIntervalChart() {
		// TODO still need to develop the code for this
		updateEnabledButtons();
	}
	
	/*
	 * This method swiches the chart to scatter plot and plots the consumption vs temperature
	 */
	public void changeToScatterPlotChart(){
		//TODO Change the chart to scatter plot chart
		updateEnabledButtons();
	}
	
	/*
	 * This method changes the daily charts by the Type of Day
	 * enabling the buttons that repaint the chart by the type of day 
	 */
	public void changeToDailyPlotByDayType(){
		chartNode.setContent(dailyDayTypeChartPanel);
		updateEnabledButtons();
	}
	
	/**
	 * Method that filters all of the schooldays
	 */
	public void filterSchoolDays(){
		dailyDayTypeChartPanel.filterSchoolDays();
	}
	
	/*
	 * Method that filters weekends
	 */
	public void filtersWeekends(){
		dailyDayTypeChartPanel.filterWeekends();
	}
	
	/*
	 * Method that filters holidays
	 */
	public void filtersHolidays(){
		dailyDayTypeChartPanel.filterHolidays();
	}
	
	/*
	 * Method that filters summer days
	 */
	public void filtersSummerDays(){
		dailyDayTypeChartPanel.filterSummerDays();
	}
	
	public void includeAllDays(){
		dailyDayTypeChartPanel.includeAllDays();
	}
	
	/**
	 * This is a private method that is called whenever a button is pressed, enabling and disabling 
	 * the buttons
	 */
	private void updateEnabledButtons(){
		if(regularTimeSeriesRadioButton.isSelected()){
			monthlyRadioButton.setDisable(false);
			dailyRadioButton.setDisable(false);
			hourlyRadioButton.setDisable(false);
			fifteenMinRadioButton.setDisable(false);
			weekendRadioButton.setDisable(true);
			schoolDayRadioButton.setDisable(true);
			summerDayRadioButton.setDisable(true);
			holidayRadioButton.setDisable(true);
			everyDayRadioButton.setDisable(true);
			schoolDayColorPicker.setDisable(true);
			weekendColorPicker.setDisable(true);
			summerDayColorPicker.setDisable(true);
			holidayColorPicker.setDisable(true);
		} else if (dailyTypeOfDayRadioButton.isSelected()){
			monthlyRadioButton.setDisable(true);
			dailyRadioButton.setDisable(true);
			hourlyRadioButton.setDisable(true);
			fifteenMinRadioButton.setDisable(true);
			weekendRadioButton.setDisable(false);
			schoolDayRadioButton.setDisable(false);
			summerDayRadioButton.setDisable(false);
			holidayRadioButton.setDisable(false);
			everyDayRadioButton.setDisable(false);
			schoolDayColorPicker.setDisable(false);
			weekendColorPicker.setDisable(false);
			summerDayColorPicker.setDisable(false);
			holidayColorPicker.setDisable(false);
		}
	}
}
