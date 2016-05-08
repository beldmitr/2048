package Game;

import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Score {
	private int score = 0;

	private Text text;
	private final int FONT_SIZE = 18;

	public Score(Pane gamePane, int x, int y) {
		super();

		text = new Text(x, y, "Score : " + score);
		text.setFont(Font.font(FONT_SIZE));
		gamePane.getChildren().add(text);
	}

	public void addScore(int number) {
		score = score + number;
		text.setText("Score : " + score);
	}

	public void resetScore() {
		score = 0;
		text.setText("Score : " + score);
	}

}
