package Game;

import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;

	private static final int BUTTON_X = 520;
	private static final int BUTTON_Y = 420;

	public static void main(String[] args) {
		launch(args);
	}

	private GameLogic gameLogic;
	private Pane root;
	private Pane gamePane;
	private Button startButton;

	@Override
	public void start(Stage primaryStage) throws Exception {
		root = new Pane();
		gamePane = new Pane();
		gamePane.setLayoutX(20);
		gamePane.setLayoutY(20);
		root.getChildren().add(gamePane);

		gameLogic = new GameLogic(gamePane, HEIGHT - 40);
		gameLogic.start();

		startButton = new Button("New game");
		startButton.setCursor(Cursor.HAND);
		startButton.setLayoutX(BUTTON_X);
		startButton.setLayoutY(BUTTON_Y);

		startButton.setOnAction(e -> {
			gameLogic.start();
		});

		root.getChildren().add(startButton);

		Scene scene = new Scene(root, WIDTH, HEIGHT);
		scene.setOnKeyPressed(gameLogic::dispatcherKeys);

		primaryStage.setScene(scene);
		primaryStage.setTitle("2048");
		primaryStage.show();
	}

}
