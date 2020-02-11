import java.awt.Frame;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.awt.Label;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;

public class InfoBox extends Frame {
	
	/**
	 * Method InfoBox
	 *
	 *
	 */
	public InfoBox() {
		setLayout(new GridLayout(25,1));
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				GameFrame gameFrame = new GameFrame();
				gameFrame.setVisible(true);
				setVisible(false);
			}
		});
		setLocation(50,50);
		setTitle("Instructions");
		add(new Label("             The Pieces:         "));
		add(new Label("The symbols representing the peices are as follows- "));
		add(new Label("F: Flag, 1: Marshal, 2: General, 3: Colonel, 4: Major, 5: Captain, 6: Lieutenant, 7: Sergant, 8: Miner, 9: Scout, S: Spy, B: Bomb."));
		add(new Label("Except for Bombs and Flags, which cannot move at all, and the Scout, which can move any number of squares on the board up, down, left, or right,"));
		add(new Label("as long as the scout does not move through a piece, a lake, or into lake, all pieces may move one square up, down, left, or right,"));
		add(new Label("as long as they don't move off the board, into a lake, or into a piece of the same color."));
		add(new Label("Battles happen when one piece moves into the same location as an enemy. When there is a battle, both pieces are revealed, and  at most one can remain."));
		add(new Label("Pieces of lower numbers take pieces of higher numbers. The Flag may be taken by any piece, but only miners can take out a bomb."));
		add(new Label("The spy will take out the marshal so long as the spy attacks. If the pieces are equal, both will be eliminated."));
		add(new Label("Otherwise, only the attacking piece will be eliminated. A K will mark any of your units that have been discovered by the enemy."));
		add(new Label("The first person that takes the enemy's flag or prevents the enemy from moving, wins."));
		add(new Label("             Setup:              "));
		add(new Label("The setup phase is immediately after the dificulty has been selected. The lower box contains the units you have yet to place,"));
		add(new Label("and you may place them anywhere in the four lower rows of the board. To place a piece on the board, click on the symbol for the piece,"));
		add(new Label("and then click on the place you want the piece. Clicking on a piece and clicking that piece or another piece again undoes the effect"));
		add(new Label("of clicking on that piece. To move a piece back into your pile, click on the 'U' button and then click piece on the board you want to remove."));
		add(new Label("Numbers on top of the symbols in the lower box show you how many of each piece you have left to put down."));
		add(new Label("When the lower box disapears, setup phase is over."));
		add(new Label("             Playing:            "));
		add(new Label("The game involves turns between you and the computer, starting with you, wherein one move is made per turn."));
		add(new Label("To make a move, click on a piece, and then click on where you want it to go."));
		add(new Label("Clicking on a piece and then clicking on where it can't go undoes the effect of clicking on that piece."));
		add(new Label("Be sure to wait for the computer to make its move before trying to move again."));
		add(new Label("Color Scheme: you are light blue, lakes are dark blue, enemy is red, open space is green."));
		add(new Label("Close this to start the game."));
		pack();
		setVisible(true);
	}
}
