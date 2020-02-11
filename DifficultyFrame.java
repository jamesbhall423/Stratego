import java.awt.Frame;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//allows user to set difficulty

//constructor - line 19
//method WindowClosed - line 41
//method ButtonClicked - line 45

class DifficultyFrame extends Frame {
	GameFrame caller;
	
	//constructor
	public DifficultyFrame(GameFrame caller) {
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				WindowClosed();
			}
		});
		setLayout(new FlowLayout());
		this.caller = caller;
		Button[] difficulty = new Button[6];
		for(int i = 1; i <= 5; i++) {
			difficulty[i] = new Button("Difficulty" + i);
			final int I = i;
			difficulty[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ButtonClicked(I);
				}
			});
			add(difficulty[i]);
		}
		pack();
	}
	protected void WindowClosed() {
		setVisible(false);
	}
	//method called when one of the buttons is clicked
	public void ButtonClicked(int I) {
		//Sets Difficulty
		caller.setDifficulty(I);
		setVisible(false);
	}
}
