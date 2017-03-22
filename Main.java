import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/* Entry Class: Main */
public class Main extends Application{

	ArrayList<Long> times = new ArrayList<Long>();
	ArrayList<Long> acceptableTimes = new ArrayList<Long>();
	ArrayList<Double> x = new ArrayList<Double>();
	ArrayList<Double> y = new ArrayList<Double>();
	ArrayList<Double> distances = new ArrayList<Double>();
	ArrayList<Double> widths = new ArrayList<Double>();
	ArrayList<Double> indexOfDifficulty = new ArrayList<Double>();
	int failures = 0;
	double perceivedWidth = 0;
	int trials = 0;

	/* Main Method */
	public static void main(String[] args)  {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Button power = new Button("Push to Start");
		power.setLayoutX(500);
		power.setLayoutY(300);
		power.setPrefWidth(320);
		power.setPrefHeight(200);
		Pane pane = new Pane();
		pane.getChildren().add(power);

		System.out.println("here");
		Scene scene = new Scene(pane, 1500, 1000);
		scene.setOnMousePressed(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				System.out.println("Outside of button");
				failures++;
			}
		});

		primaryStage.setScene(scene);
		primaryStage.show();

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("How To");

		alert.setHeaderText(null);
		alert.setContentText("Click the 10 targets as fast as possible."
				+ " Your accuracy will be displayed at the end along with a graph. Have fun!");

		alert.showAndWait();

		power.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				if (trials != 10) {
					trials++;
					power.setText("Target " + trials);
					times.add(System.currentTimeMillis());

					if (times.size() > 1) {
						acceptableTimes.add(times.get(times.size()-1) 
								- times.get(times.size()-2));
					}

					x.add(event.getSceneX());
					y.add(event.getSceneY());
					power.setLayoutX(ThreadLocalRandom.current().nextInt(1, 800));
					power.setLayoutY(ThreadLocalRandom.current().nextInt(1, 630));
					power.setPrefHeight(50);
					pane.getChildren().remove(power);
					perceivedWidth = ThreadLocalRandom.current().nextInt(70, 200);
					System.out.println(perceivedWidth);
					power.setPrefWidth(perceivedWidth);
					widths.add(perceivedWidth);
					pane.getChildren().add(power);
				}

				else {
					times.add(System.currentTimeMillis());
					acceptableTimes.add(times.get(times.size()-1) 
							- times.get(times.size()-2));
					x.add(event.getSceneX());
					y.add(event.getSceneY());
					pane.getChildren().remove(power);
					for (int i = 1; i < x.size(); i++){
						distances.add(Math.sqrt(Math.pow(x.get(i)-x.get(i-1), 2) 
								+ Math.pow(y.get(i)-y.get(i-1), 2)));
					}
					for (int i=0; i<distances.size(); i++){
						indexOfDifficulty.add(Math.log(distances.get(i)/widths.get(i)+1)/Math.log(2));
						System.out.println(indexOfDifficulty.get(i));
					}

					NumberAxis xAxis = new NumberAxis();
					xAxis.setLabel("Index of Difficulty");
					NumberAxis yAxis = new NumberAxis();
					yAxis.setLabel("Time");

					LineChart<Number, Number> line = new LineChart<Number, Number> (xAxis, yAxis);
					line.setTitle("Line Chart");
					XYChart.Series<Number, Number> data = new XYChart.Series<>();
					for (int i = 0; i < acceptableTimes.size(); i++){
						data.getData().add(new XYChart.Data<Number, Number>(indexOfDifficulty.get(i),
								acceptableTimes.get(i)));
					}

					line.getData().add(data);
					primaryStage.setWidth(800);
					primaryStage.setHeight(600);
					pane.getChildren().add(line);
					primaryStage.setScene(scene);
					primaryStage.show();

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information Dialog");
					alert.setHeaderText(null);
					float percent = (float)10/(10+failures)*100;
					System.out.println(percent);
					alert.setContentText("Accuracy: " + percent + "%");

					alert.showAndWait();
				}
			}
		});
	}
}