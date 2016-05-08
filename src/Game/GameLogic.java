package Game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class GameLogic {

	// All graphics will be drawn to this pane
	private Pane gamePane;

	private Field field;

	private Timeline timeline;
	private KeyFrame update;
	private final int TIME_FIGURE_CREATION = 150;

	private int size;

	private boolean wasChanges = false;

	public GameLogic(Pane gamePane, int size) {
		super();

		this.gamePane = gamePane;
		this.size = size;
	}

	public void start() {
		gamePane.getChildren().clear();

		field = new Field(gamePane, size, size);

		field.createRandomFigure();
		field.createRandomFigure();

		timeline = new Timeline();
		update = new KeyFrame(Duration.millis(TIME_FIGURE_CREATION), e -> {
			if (wasChanges) {
				field.createRandomFigure();
				wasChanges = false;
			}
		});

		timeline.getKeyFrames().add(update);
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();

	}

	public void dispatcherKeys(KeyEvent e) {
		if (!wasChanges) {
			switch (e.getCode()) {
			case UP:
				if (field.pressedTop()) {
					wasChanges = true;
				}
				break;
			case RIGHT:
				if (field.pressedRight()) {
					field.createRandomFigure();
				}
				break;
			case DOWN:
				if (field.pressedBottom()) {
					wasChanges = true;
				}
				break;
			case LEFT:
				if (field.pressedLeft()) {
					wasChanges = true;
				}
				break;
			default:
				break;
			}
		}
	}

}
