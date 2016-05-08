package Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class Field {
	private int squareWidth;
	private int squareHeight;
	private int fieldWidth;
	private int fieldHeight;

	private final int NUMBER_OF_FIELD = 4;
	private final double LINE_WIDTH = 4.0;

	private final int SCORE_MARGIN_X = 10;
	private final int SCORE_MARGIN_Y = 15;

	private List<Coordinate> freePlaces = new ArrayList<>();
	private Bone[][] bones = new Bone[NUMBER_OF_FIELD][NUMBER_OF_FIELD];
	private Pane gamePane;
	private Score score;

	public Field(Pane gamePane, int fieldWidth, int fieldHeight) {
		super();

		this.gamePane = gamePane;
		this.fieldWidth = fieldWidth;
		this.fieldHeight = fieldHeight;

		squareWidth = fieldWidth / NUMBER_OF_FIELD;
		squareHeight = fieldHeight / NUMBER_OF_FIELD;

		drawLines();
		score = new Score(gamePane, fieldWidth + SCORE_MARGIN_X, SCORE_MARGIN_Y);
	}

	private void drawLines() {
		for (int i = 0; i < NUMBER_OF_FIELD + 1; i++) {
			Line verticalLine = new Line(i * squareWidth, 0, i * squareWidth, fieldHeight);
			verticalLine.setStrokeWidth(LINE_WIDTH);

			Line horizontalLine = new Line(0, i * squareHeight, fieldWidth, i * squareHeight);
			horizontalLine.setStrokeWidth(LINE_WIDTH);

			gamePane.getChildren().add(verticalLine);
			gamePane.getChildren().add(horizontalLine);
		}

		for (int j = 0; j < NUMBER_OF_FIELD; j++) {
			for (int i = 0; i < NUMBER_OF_FIELD; i++) {
				bones[i][j] = null;
			}
		}
	}

	public void createRandomFigure() {
		Random random = new Random();
		Random randomNumber = new Random();

		getFreePlaces();

		Coordinate c = freePlaces.get(random.nextInt(freePlaces.size()));

		int number = 2;

		if (randomNumber.nextInt(5) > 3) { // 3/5 chance that it will be 2
			number = 4;
		}

		bones[c.x][c.y] = new Bone(c.x, c.y, squareWidth, squareHeight, number, gamePane);
	}

	public void createFigure(int x, int y, int number) {
		bones[x][y] = new Bone(x, y, squareWidth, squareHeight, number, gamePane);
	}

	private void getFreePlaces() {
		freePlaces.clear();
		for (int j = 0; j < NUMBER_OF_FIELD; j++) {
			for (int i = 0; i < NUMBER_OF_FIELD; i++) {
				if (bones[i][j] == null) {
					freePlaces.add(new Coordinate(i, j));
				}
			}
		}
	}

	// joins

	private boolean joinTop() {
		boolean wasChanges = false;
		for (int j = 1; j < NUMBER_OF_FIELD; j++) {
			for (int i = 0; i < NUMBER_OF_FIELD; i++) {
				if (bones[i][j] == null || bones[i][j - 1] == null) {
					continue;
				}

				Bone top = bones[i][j - 1];
				Bone bottom = bones[i][j];

				if (top.getNumber() == bottom.getNumber()) {
					top.join(bottom);
					score.addScore(top.getNumber());
					bones[i][j] = null;
					wasChanges = true;
				}
			}
		}
		return wasChanges;
	}

	private boolean joinRight() {
		boolean wasChanges = false;
		for (int j = 0; j < NUMBER_OF_FIELD; j++) {
			for (int i = NUMBER_OF_FIELD - 2; i >= 0; i--) {
				if (bones[i][j] == null || bones[i + 1][j] == null) {
					continue;
				}

				Bone left = bones[i][j];
				Bone right = bones[i + 1][j];

				if (left.getNumber() == right.getNumber()) {
					right.join(left);
					score.addScore(right.getNumber());
					bones[i][j] = null;
					wasChanges = true;
				}

			}
		}
		return wasChanges;
	}

	private boolean joinBottom() {
		boolean wasChanges = false;
		for (int j = NUMBER_OF_FIELD - 2; j >= 0; j--) {
			for (int i = 0; i < NUMBER_OF_FIELD; i++) {
				if (bones[i][j] == null || bones[i][j + 1] == null) {
					continue;
				}

				Bone bottom = bones[i][j + 1];
				Bone top = bones[i][j];

				if (top.getNumber() == bottom.getNumber()) {
					bottom.join(top);
					score.addScore(bottom.getNumber());
					bones[i][j] = null;
					wasChanges = true;
				}
			}
		}
		return wasChanges;
	}

	private boolean joinLeft() {
		boolean wasChanges = false;
		for (int j = 0; j < NUMBER_OF_FIELD; j++) {
			for (int i = 1; i < NUMBER_OF_FIELD; i++) {
				if (bones[i][j] == null || bones[i - 1][j] == null) {
					continue;
				}

				Bone left = bones[i - 1][j];
				Bone right = bones[i][j];

				if (left.getNumber() == right.getNumber()) {
					left.join(right);
					score.addScore(left.getNumber());
					bones[i][j] = null;
					wasChanges = true;
				}
			}
		}
		return wasChanges;
	}

	// moves

	private boolean moveBonesTop() {
		Bone[][] newBones = new Bone[NUMBER_OF_FIELD][NUMBER_OF_FIELD];
		boolean wasChanges = false;

		for (int j = 1; j < NUMBER_OF_FIELD; j++) {
			for (int i = 0; i < NUMBER_OF_FIELD; i++) {
				if (bones[i][j] != null && bones[i][j - 1] == null) {
					bones[i][j].moveTop();
					wasChanges = true;
				}
			}
		}

		for (int j = 0; j < NUMBER_OF_FIELD; j++) {
			for (int i = 0; i < NUMBER_OF_FIELD; i++) {
				if (bones[i][j] != null) {
					Bone bone = bones[i][j];
					newBones[bone.getX()][bone.getY()] = bone;
				}
			}
		}

		bones = newBones;
		return wasChanges;
	}

	private boolean moveBonesRight() {
		Bone[][] newBones = new Bone[NUMBER_OF_FIELD][NUMBER_OF_FIELD];
		boolean wasChanges = false;

		for (int j = 0; j < NUMBER_OF_FIELD; j++) {
			for (int i = 0; i < NUMBER_OF_FIELD - 1; i++) {
				if (bones[i][j] != null && bones[i + 1][j] == null) {
					bones[i][j].moveRight();
					wasChanges = true;
				}
			}
		}

		for (int j = 0; j < NUMBER_OF_FIELD; j++) {
			for (int i = 0; i < NUMBER_OF_FIELD; i++) {
				if (bones[i][j] != null) {
					Bone bone = bones[i][j];
					newBones[bone.getX()][bone.getY()] = bone;
				}
			}
		}

		bones = newBones;
		return wasChanges;
	}

	private boolean moveBonesBottom() {
		Bone[][] newBones = new Bone[NUMBER_OF_FIELD][NUMBER_OF_FIELD];
		boolean wasChanges = false;

		for (int j = 0; j < NUMBER_OF_FIELD - 1; j++) {
			for (int i = 0; i < NUMBER_OF_FIELD; i++) {
				if (bones[i][j] != null && bones[i][j + 1] == null) {
					bones[i][j].moveBottom();
					wasChanges = true;
				}
			}
		}

		for (int j = 0; j < NUMBER_OF_FIELD; j++) {
			for (int i = 0; i < NUMBER_OF_FIELD; i++) {
				if (bones[i][j] != null) {
					Bone bone = bones[i][j];
					newBones[bone.getX()][bone.getY()] = bone;
				}
			}
		}

		bones = newBones;
		return wasChanges;
	}

	private boolean moveBonesLeft() {
		Bone[][] newBones = new Bone[NUMBER_OF_FIELD][NUMBER_OF_FIELD];
		boolean wasChanges = false;

		for (int j = 0; j < NUMBER_OF_FIELD; j++) {
			for (int i = 1; i < NUMBER_OF_FIELD; i++) {
				if (bones[i][j] != null && bones[i - 1][j] == null) {
					bones[i][j].moveLeft();
					wasChanges = true;
				}
			}
		}

		for (int j = 0; j < NUMBER_OF_FIELD; j++) {
			for (int i = 0; i < NUMBER_OF_FIELD; i++) {
				if (bones[i][j] != null) {
					Bone bone = bones[i][j];
					newBones[bone.getX()][bone.getY()] = bone;
				}
			}
		}

		bones = newBones;
		return wasChanges;
	}

	// MOVES

	public boolean pressedTop() {
		boolean wasChanges = false;

		for (int i = 0; i < NUMBER_OF_FIELD; i++) {
			if (moveBonesTop()) {
				wasChanges = true;
			}
		}

		if (joinTop()) {
			wasChanges = true;
		}

		for (int i = 0; i < NUMBER_OF_FIELD; i++) {
			if (moveBonesTop()) {
				wasChanges = true;
			}
		}

		return wasChanges;
	}

	public boolean pressedRight() {
		boolean wasChanges = false;

		for (int i = 0; i < NUMBER_OF_FIELD; i++) {
			if (moveBonesRight()) {
				wasChanges = true;
			}
		}

		if (joinRight()) {
			wasChanges = true;
		}

		for (int i = 0; i < NUMBER_OF_FIELD; i++) {
			if (moveBonesRight()) {
				wasChanges = true;
			}
		}

		return wasChanges;
	}

	public boolean pressedBottom() {
		boolean wasChanges = false;

		for (int i = 0; i < NUMBER_OF_FIELD; i++) {
			if (moveBonesBottom()) {
				wasChanges = true;
			}
		}

		if (joinBottom()) {
			wasChanges = true;
		}

		for (int i = 0; i < NUMBER_OF_FIELD; i++) {
			if (moveBonesBottom()) {
				wasChanges = true;
			}
		}

		return wasChanges;
	}

	public boolean pressedLeft() {
		boolean wasChanges = false;

		for (int i = 0; i < NUMBER_OF_FIELD; i++) {
			if (moveBonesLeft()) {
				wasChanges = true;
			}
		}

		if (joinLeft()) {
			wasChanges = true;
		}

		for (int i = 0; i < NUMBER_OF_FIELD; i++) {
			if (moveBonesLeft()) {
				wasChanges = true;
			}
		}

		return wasChanges;
	}

	private class Coordinate {
		private int x;
		private int y;

		public Coordinate(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

	}

}
