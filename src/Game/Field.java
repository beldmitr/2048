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

	// Bones on the table
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

		drawGrid();
		score = new Score(gamePane, fieldWidth + SCORE_MARGIN_X, SCORE_MARGIN_Y);
	}

	private void drawGrid() {
		for (int i = 0; i < NUMBER_OF_FIELD + 1; i++) {
			Line verticalLine = new Line(i * squareWidth, 0, i * squareWidth, fieldHeight);
			verticalLine.setStrokeWidth(LINE_WIDTH);

			Line horizontalLine = new Line(0, i * squareHeight, fieldWidth, i * squareHeight);
			horizontalLine.setStrokeWidth(LINE_WIDTH);

			gamePane.getChildren().add(verticalLine);
			gamePane.getChildren().add(horizontalLine);
		}
	}

	public void createRandomFigure() {
		Random random = new Random();
		Random randomNumber = new Random();

		getFreePlaces();

		// Choose some random position from a list of free cells
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

	// Rules for merging 2 cells to 1 cell with bigger value
	private boolean joinTop() {
		/*
		 * wasChanges is used to define that there was some changes. In this
		 * case, for example, that there was a merging. I use this result in
		 * GameLogic class in method dispatcherKeys
		 */

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
		/*
		 * wasChanges is used to define that there was some changes. In this
		 * case, for example, that there was a merging. I use this result in
		 * GameLogic class in method dispatcherKeys
		 */
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
		/*
		 * wasChanges is used to define that there was some changes. In this
		 * case, for example, that there was a merging. I use this result in
		 * GameLogic class in method dispatcherKeys
		 */
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
		/*
		 * wasChanges is used to define that there was some changes. In this
		 * case, for example, that there was a merging. I use this result in
		 * GameLogic class in method dispatcherKeys
		 */
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

	// Rules for moving

	private boolean moveBonesTop() {
		Bone[][] newBones = new Bone[NUMBER_OF_FIELD][NUMBER_OF_FIELD];
		/*
		 * wasChanges is used to define that there was some changes. In this
		 * case, for example, that there was a moving. I use this result in
		 * GameLogic class in method dispatcherKeys
		 */
		boolean wasChanges = false;

		for (int j = 1; j < NUMBER_OF_FIELD; j++) {
			for (int i = 0; i < NUMBER_OF_FIELD; i++) {
				if (bones[i][j] != null && bones[i][j - 1] == null) {
					bones[i][j].moveTop();
					wasChanges = true;
				}
			}
		}

		// Makes new actual matrix with bones
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
		/*
		 * wasChanges is used to define that there was some changes. In this
		 * case, for example, that there was a moving. I use this result in
		 * GameLogic class in method dispatcherKeys
		 */
		boolean wasChanges = false;

		for (int j = 0; j < NUMBER_OF_FIELD; j++) {
			for (int i = 0; i < NUMBER_OF_FIELD - 1; i++) {
				if (bones[i][j] != null && bones[i + 1][j] == null) {
					bones[i][j].moveRight();
					wasChanges = true;
				}
			}
		}

		// Makes new actual matrix with bones
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
		/*
		 * wasChanges is used to define that there was some changes. In this
		 * case, for example, that there was a moving. I use this result in
		 * GameLogic class in method dispatcherKeys
		 */
		boolean wasChanges = false;

		for (int j = 0; j < NUMBER_OF_FIELD - 1; j++) {
			for (int i = 0; i < NUMBER_OF_FIELD; i++) {
				if (bones[i][j] != null && bones[i][j + 1] == null) {
					bones[i][j].moveBottom();
					wasChanges = true;
				}
			}
		}

		// Makes new actual matrix with bones
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
		/*
		 * wasChanges is used to define that there was some changes. In this
		 * case, for example, that there was a moving. I use this result in
		 * GameLogic class in method dispatcherKeys
		 */
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

	// Action on key press

	/*
	 * On press button up: Move top Merge Move top
	 */

	public boolean pressedTop() {
		/*
		 * wasChanges is used to define that there was some changes. I use this
		 * result in GameLogic class in method dispatcherKeys
		 */
		boolean wasChanges = false;

		// Move top
		for (int i = 0; i < NUMBER_OF_FIELD; i++) {
			if (moveBonesTop()) {
				wasChanges = true;
			}
		}

		// Merge
		if (joinTop()) {
			wasChanges = true;
		}

		// Move top
		for (int i = 0; i < NUMBER_OF_FIELD; i++) {
			if (moveBonesTop()) {
				wasChanges = true;
			}
		}

		return wasChanges;
	}

	/*
	 * Move right Merge Move right
	 */
	public boolean pressedRight() {
		/*
		 * wasChanges is used to define that there was some changes. I use this
		 * result in GameLogic class in method dispatcherKeys
		 */
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

	/*
	 * Move down Merge Move down
	 */
	public boolean pressedBottom() {
		/*
		 * wasChanges is used to define that there was some changes. I use this
		 * result in GameLogic class in method dispatcherKeys
		 */
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

	/*
	 * Move left Merge Move left
	 */
	public boolean pressedLeft() {
		/*
		 * wasChanges is used to define that there was some changes. I use this
		 * result in GameLogic class in method dispatcherKeys
		 */
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
