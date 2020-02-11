/* A frame used for player setup
 * This frame is used in cunjunction with GameFrame:
 * This frame sets the units' type;
 * GameFrame places them where they belong.
 */
 
 //constructor - line 31;
 //method buttonClicked - line 69;
 //method makeReady - line 83;
 
import java.awt.Frame;
import java.awt.Button;
import java.awt.Label;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

class PlacementBox extends Frame {
	GameFrame caller;
	private boolean ready = true;
	
	Label[] labels = new Label[13];
	Button[] buttons = new Button[13];
	
	// remainder tells how many of each piece the player has yet to put on the board
	private int[] remainder = {1, 1, 1, 2, 3, 4, 4, 4, 5, 8, 1, 6, 1};
	
	//current tells what piece will be put on the board
	int current = 0;
	
	public PlacementBox(GameFrame caller) {
		this.caller = caller;
				
		// create PlacementBox - inner class will call buttonClicked
		setLayout(new GridLayout(2,13));
		for (int i = 0; i <= 12; i++) {
			labels[i] = new Label();
			labels[i].setText("" + remainder[i]);
			add(labels[i]);
		}
		for (int i = 0; i <= 12; i++) {
			final int I = i;
			buttons[i] = new Button();
			switch(i) {
				case 0:
				buttons[i].setLabel("f");
				break;
				case 10:
				buttons[i].setLabel("s");
				break;
				case 11:
				buttons[i].setLabel("b");
				break;
				case 12:
				buttons[i].setLabel("U");
				break;
				default:
				buttons[i].setLabel("" + i);
				break;
			}
			buttons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					buttonClicked(I);
				}
			});
			add(buttons[i]);
		}
		pack();
		setLocation(310,520);
		setVisible(true);
	}
	protected void buttonClicked(int i) {
		if(ready) {
			if (remainder[i] > 0) {
				current = i;
    			labels[i].setText("" + --remainder[i]);
    			caller.setReady(i, true);
    			ready = false;
			}
		} else {
			labels[current].setText("" + ++remainder[current]);
			caller.setReady(0, false);
			ready = true;
		}
	}
	public void makeReady() {
		ready = true;
	}
	public void Undo(int i) {
		labels[i].setText(""+ ++remainder[i]);
		labels[12].setText(""+ ++remainder[12]);
	}
}
