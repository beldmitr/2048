package Game;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Bone {

	private double marginFontX;
	private double marginFontY;
	private final int FONT_SIZE = 46;

	private int x;
	private int y;
	private int width;
	private int height;
	private int number;
	private Pane gamePane;
	private Rectangle rectangle;
	private Text text;

	public Bone(int x, int y, int width, int height, int number, Pane gamePane) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.number = number;
		this.gamePane = gamePane;

		rectangle = new Rectangle(x * width + 8, y * height + 8, width - 16, height - 16);
		DropShadow shadow = new DropShadow();
		rectangle.setEffect(shadow);

		setFontMargin(number);
		text = new Text((x + 0.5) * width + marginFontX, (y + 0.5) * height + marginFontY, Integer.toString(number));
		text.setFont(Font.font(FONT_SIZE));
		setColors(number);

		gamePane.getChildren().add(rectangle);
		gamePane.getChildren().add(text);
	}

	// Different bones has got different background and font color
	private void setColors(int number) {
		switch (number) {
		case 2:
			rectangle.setFill(Color.rgb(0xEE, 0xE4, 0xDA));
			text.setFill(Color.rgb(0x77, 0x6E, 0x65));
			break;
		case 4:
			rectangle.setFill(Color.rgb(0xED, 0xE0, 0xC8));
			text.setFill(Color.rgb(0x77, 0x6E, 0x65));
			break;
		case 8:
			rectangle.setFill(Color.rgb(0xF2, 0xB1, 0x79));
			text.setFill(Color.rgb(0xF9, 0xF6, 0xF2));
			break;
		case 16:
			rectangle.setFill(Color.rgb(0xF5, 0x95, 0x63));

			text.setFill(Color.rgb(0xF9, 0xF6, 0xF2));
			break;
		case 32:
			rectangle.setFill(Color.rgb(0xF6, 0x7C, 0x5F));
			text.setFill(Color.rgb(0xF9, 0xF6, 0xF2));
			break;
		case 64:
			rectangle.setFill(Color.rgb(0xF6, 0x5E, 0x3B));
			text.setFill(Color.rgb(0xF9, 0xF6, 0xF2));
			break;
		case 128:
			rectangle.setFill(Color.rgb(0xED, 0xCF, 0x72));
			text.setFill(Color.rgb(0xF9, 0xF6, 0xF2));
			break;
		case 256:
			rectangle.setFill(Color.rgb(0xED, 0xCC, 0x61));
			text.setFill(Color.rgb(0xF9, 0xF6, 0xF2));
			break;
		case 512:
			rectangle.setFill(Color.rgb(0xED, 0xC8, 0x50));
			text.setFill(Color.rgb(0xF9, 0xF6, 0xF2));
			break;
		case 1024:
			rectangle.setFill(Color.rgb(0xED, 0xC5, 0x3F));
			text.setFill(Color.rgb(0xF9, 0xF6, 0xF2));
			break;
		case 2048:
			rectangle.setFill(Color.rgb(0xED, 0xC2, 0x2E));
			text.setFill(Color.rgb(0xF9, 0xF6, 0xF2));
			break;
		default:
			rectangle.setFill(Color.rgb(0x3C, 0x3A, 0x32));
			text.setFill(Color.rgb(0xF9, 0xF6, 0xF2));
			break;
		}
	}

	/*
	 * Merges are used to set a text (value of a bone) to center position.
	 * Merges depends on text size
	 */
	private void setFontMargin(int number) {
		switch (Integer.toString(number).length()) {
		case 1:
			marginFontX = -12;
			marginFontY = 14;
			break;

		case 2:
			marginFontX = -24;
			marginFontY = 14;
			break;

		case 3:
			marginFontX = -37;
			marginFontY = 14;
			break;

		case 4:
			marginFontX = -50;
			marginFontY = 14;
			break;

		default:
			marginFontX = -0.5 * width; // Write from the beginning of a square
			marginFontY = 14;
			break;
		}
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
		/*
		 * Remove old text from a gamePane and below new text with actual merges
		 * and actual color will be added
		 */

		gamePane.getChildren().remove(text);

		setFontMargin(number);
		text = new Text((x + 0.5) * width + marginFontX, (y + 0.5) * height + marginFontY, Integer.toString(number));
		text.setFont(Font.font(FONT_SIZE));
		setColors(number);

		// Add actual text
		gamePane.getChildren().add(text);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void moveTop() {
		y -= 1;
		rectangle.setY(y * height + 8);
		setFontMargin(number);
		text.setY((y + 0.5) * height + marginFontY);
	}

	public void moveRight() {
		x += 1;
		rectangle.setX(x * width + 8);
		setFontMargin(number);
		text.setX((x + 0.5) * width + marginFontX);
	}

	public void moveBottom() {
		y += 1;
		rectangle.setY(y * height + 8);
		setFontMargin(number);
		text.setY((y + 0.5) * height + marginFontY);
	}

	public void moveLeft() {
		x -= 1;
		rectangle.setX(x * width + 8);
		setFontMargin(number);
		text.setX((x + 0.5) * width + marginFontX);
	}

	private void removeFromPane() {
		gamePane.getChildren().remove(text);
		gamePane.getChildren().remove(rectangle);
	}

	public void join(Bone bone) {
		number *= 2;
		setNumber(number);
		bone.removeFromPane();
	}

}
