// This class forms each square of the mapboard.

//constructor - line 31;
//method setContents - line 36;
//method getSquareValues - line 166;
//method getContents - line 169;
//method getRemainder - line 172;

import java.awt.Button;
import java.awt.Color;
class mapButton extends Button {
	/*Contents - what is in the square;
	 *Values of Contents:
	 * -2: Lake;
	 * -1: Open Space;
	 * 0: unknown red(player) flag;
	 * 1-9: unknown red 1-9 respectively;
	 * 10: unknown red spy;
	 * 11: unknown red bomb;
	 * 12-23: same as 0-11 respectively, except known;
	 * 24-47: same as 0-23 respectively, except blue or computer;
	 */
	 private int Contents;
	 
	 //position of the mapButton
	 private int X;
	 private int Y;
	 boolean set = false;
	 
	 //Constructor
	 public mapButton(int X, int Y) {
	 	this.X = X;
	 	this.Y = Y;
	 	setContents(-1);
	 }
	 public void setContents(int Contents) {
	 	this.Contents = Contents;
	 	if (Contents>=0) if (Contents>=24) setBackground(Color.red);
	 	else setBackground(Color.cyan);
	 	// setLabel to adjust for new Contents
	 	switch(Contents) {
	 		case -4:
	 		setBackground(Color.cyan);
	 		setLabel(" Win ");
	 		break;
	 		case -3:
	 		setBackground(Color.red);
	 		setLabel("Lose");
	 		break;
	 		case -2:
	 		setBackground(Color.blue);
	 		setLabel("         ");
	 		break;
	 		case -1:
	 		setBackground(Color.green);
	 		setLabel("         ");
	 		break;
	 		case 0:
	 		setLabel("   F   ");
	 		break;
	 		case 1:
	 		setLabel("   1   ");
	 		break;
	 		case 2:
	 		setLabel("   2   ");
	 		break;
	 		case 3:
	 		setLabel("   3   ");
	 		break;
	 		case 4:
	 		setLabel("   4   ");
	 		break;
	 		case 5:
	 		setLabel("   5   ");
	 		break;
	 		case 6:
	 		setLabel("   6   ");
	 		break;
	 		case 7:
	 		setLabel("   7   ");
	 		break;
	 		case 8:
	 		setLabel("   8   ");
	 		break;
	 		case 9:
	 		setLabel("   9   ");
	 		break;
	 		case 10:
	 		setLabel("   S   ");
	 		break;
	 		case 11:
	 		setLabel("   B   ");
	 		break;
	 		case 12:
	 		setLabel("  F  K");
	 		break;
	 		case 13:
	 		setLabel("  1  K");
	 		break;
	 		case 14:
	 		setLabel("  2  K");
	 		break;
	 		case 15:
	 		setLabel("  3  K");
	 		break;
	 		case 16:
	 		setLabel("  4  K");
	 		break;
	 		case 17:
	 		setLabel("  5  K");
	 		break;
	 		case 18:
	 		setLabel("  6  K");
	 		break;
	 		case 19:
	 		setLabel("  7  K");
	 		break;
	 		case 20:
	 		setLabel("  8  K");
	 		break;
	 		case 21:
	 		setLabel("  9  K");
	 		break;
	 		case 22:
	 		setLabel("  S  K");
	 		break;
	 		case 23:
	 		setLabel("  B  K");
	 		break;
	 		case 36:
	 		setLabel("   F   ");
	 		break;
	 		case 37:
	 		setLabel("   1   ");
	 		break;
	 		case 38:
	 		setLabel("   2   ");
	 		break;
	 		case 39:
	 		setLabel("   3   ");
	 		break;
	 		case 40:
	 		setLabel("   4   ");
	 		break;
	 		case 41:
	 		setLabel("   5   ");
	 		break;
	 		case 42:
	 		setLabel("   6   ");
	 		break;
	 		case 43:
	 		setLabel("   7   ");
	 		break;
	 		case 44:
	 		setLabel("   8   ");
	 		break;
	 		case 45:
	 		setLabel("   9   ");
	 		break;
	 		case 46:
	 		setLabel("   S   ");
	 		break;
	 		case 47:
	 		setLabel("   B   ");
	 		break;
	 		default:
	 		setLabel("        ");
	 		break; 		
	 	}
	 }
	 
	 public squareValues getSquareValues() {
	 	return new squareValues(X, Y, Contents);
	 }
	 int getContents() {
	 	return Contents;
	 }
	 int getRemainder() {
	 	if (Contents >= 0) {
	 		return Contents - (Contents / 12) * 12;
	 	} else {
	 		return Contents;
	 	}
	 }
}
