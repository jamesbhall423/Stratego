// Primary Game Engine

//Constructor line 47;
//method windowClosed line 87;
//method addSquareGroups line 95;
//method setDifficulty line 103;
//method setReady line 119;
//method buttonClicked line 124;
//method Place line 139;
//method PlayerSet line 152;
//method PlayerMove line 160;
//method ComputerTurn line 220;
//methods testMove line 282/355;
//methods testMoveTrue line 428/437;
//method winner line 446;
//method sideCanMove line 494;
//method computerMove line 514;

import java.awt.Frame;
import java.awt.event.*;
import java.awt.GridLayout;
import java.awt.Button;
import java.util.Random;

class GameFrame extends Frame {
	//Last piece clicked
	int x1;
	int y1;
	int contents1;
	//computer finished turn
	boolean Ready1 = true;
	//Player Ready to move
	boolean Ready2 = false;
	//whether in placing
	boolean placing = true;
	//whether player ready to place piece
	boolean SReady = false;
	
	int difficulty;
	int sum = 0;
	
	ProbabilityMap ComputerMap;
	PlacementBox placementBox;
	Mapboard mapboard = new Mapboard();
	AIMain computer;
	// Setup
	public GameFrame() {
		// Add window listener.
        addWindowListener
        (
            new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    GameFrame.this.windowClosed();
                }
            }
        ); 
        setLayout(new GridLayout(10,10));
        
        // create parts of map
        addSquareGroups(0, -1);
        addSquareGroups(2, -2);
        addSquareGroups(4, -1);
        addSquareGroups(6, -2);
        addSquareGroups(8, -1);
        
        //show map - makes buttons call buttonClicked
        for (int i = 0; i <= 9; i++) {
        	for (int I = 0; I <= 9; I++) {
        		final int x = I;
        		final int y = i;
        		mapButton b = mapboard.get(I, i);
        		b.addActionListener(new ActionListener(){
        			@Override
        			public void actionPerformed(ActionEvent e) {
        				buttonClicked(x, y);
        			}
        		});
        		add(b);
        	}
        }
        pack();
		// get dificulty
		DifficultyFrame difficultyFrame = new DifficultyFrame(this);
		difficultyFrame.setLocation(275,520);
		setLocation(250,250);
		difficultyFrame.setVisible(true);
	}
	protected void windowClosed() {
		// TODO: Check if it is safe to close the application
    	
        // Exit application.
        System.exit(0);
	}
	
	//used to create mapboard;
	private void addSquareGroups(int X, int contents) {
			mapboard.get(X, 4).setContents(contents);
			mapboard.get(X + 1, 4).setContents(contents);
			mapboard.get(X, 5).setContents(contents);
			mapboard.get(X + 1, 5).setContents(contents);
	}
	
	//called from difficultyFrame
	void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
		computer = new Dificulty1(); //Only AI developed so far
		// create cumputer's side of the map
		AISetupPosition computer = new AISetupPosition();
		int[] CSetup = computer.position;
		for (int i = 0; i <= 3; i++) {
			for (int I = 0; I <= 9; I++) {
				int n = 39 - (i * 10) - I;
				mapboard.get(I, i).setContents(CSetup[n]);
			}
		}
		// create PlacementBox
		placementBox = new PlacementBox(this);
	}
	//called by PlacementBox
	void setReady(int piece, boolean ready) {
		contents1 = piece;
		SReady = ready;
	}
	
	protected void buttonClicked(int x, int y) {
		if (placing) {
			if (SReady) {
				Place(x, y);
			}
		} else {
			if (Ready1) {
				if (Ready2) {
					PlayerMove(x, y);
				} else {
					PlayerSet(x, y); //select piece to move
				}
			}
		}
	}
	private void Place(int x, int y) {
		mapButton square = mapboard.get(x, y);
		if (y >= 6) {
			if (contents1==12) {
				if (square.set) {
					placementBox.Undo(square.getContents());
					square.setContents(-1);
					square.set=false;
					placementBox.makeReady();
					SReady = false;
					sum--;
				}
			} else if (!square.set) {
				square.setContents(contents1);
				square.set = true;
	    		placementBox.makeReady();
	    		SReady = false;
	    		if (++sum == 40) {
	    			placementBox.setVisible(false);
	    			placing = false;
        			ComputerMap = new ProbabilityMap(mapboard);
				}
	    	}
		}
	}
	private void PlayerSet(int x, int y) {
		int contents = mapboard.get(x, y).getContents();
		if (contents > 0 && contents < 24 && contents != 11 && contents != 23) {
			x1 = x;
			y1 = y;
			Ready2 = true;
		}
	}
	private void PlayerMove(int x, int y) {
		Ready2 = false;
		if (testMoveTrue(x1, y1, x, y, mapboard, true)) {
			//the computer sees your movement
			ComputerMap.move(new Movement(x1, y1, x, y, 0),computer);
		    Ready1 = false;
		    mapButton square1 = mapboard.get(x1, y1);
		    mapButton square2 = mapboard.get(x, y);
			if (y1!=y+1&&y1!=y-1&&x1!=x+1&&x1!=x-1) square1.setContents(21); //if moveing a nine more than one space
		    //Move	
		    int Remainder = square2.getRemainder();
		    if (square2.getContents() >= 24 && square2.getContents() < 36) { //if enemy unknown
		    	square2.setContents(square2.getContents() + 12); //enemy becomes known
		    	if (square1.getContents() < 12) { //if you are unknown
		    		square1.setContents(square1.getContents() + 12); //you become known
		    	}
		    	//if your piece beats computer's piece; pause and then move your piece where its was
		    	if (Remainder == 11 && square1.getRemainder() == 8 || Remainder > square1.getRemainder() && Remainder != 11 || Remainder == 1 && square1.getRemainder() == 10 || Remainder == 0) {
		    		try {
		    			Thread.sleep(3000);
		    		} catch (Exception e){
		    		}
		    		square2.setContents(square1.getContents());
		    		square1.setContents(-1);
		    		//if pieces are equal pause and then eliminate both
		    	} else if (Remainder == square1.getRemainder()) {
		    		try {
		    			Thread.sleep(3000);
		    		} catch (Exception e){
		    		}
		    		square2.setContents(-1);
		    		square1.setContents(-1);
		    		//if computer's piece beats your piece
		    	} else {
		    		square1.setContents(-1);
		    	}
		    } else {
		    	if (square1.getContents() < 12 && Remainder != -1) { //unknown piece attacking known enemy
		    		square1.setContents(square1.getContents() + 12); //becomes known
		    	}
		    	//if the piece can move, it will
		    	if (Remainder == -1 || Remainder == 11 && square1.getRemainder() == 8 || Remainder > square1.getRemainder() && Remainder != 11 || Remainder == 1 && square1.getRemainder() == 10) {
		    		square2.setContents(square1.getContents());
		    		square1.setContents(-1);
		    		//pieces are equal
		    	} else if (Remainder == square1.getRemainder()) {
		    		square2.setContents(-1);
		    		square1.setContents(-1);
		    		//computer's piece defeats your piece
		    	} else {
		    		square1.setContents(-1);
		    	}
		    }
		    for (int X = 0; X < 10; X++) {
		    	for (int Y = 0; Y < 10; Y++) {
		    		if (ComputerMap.get(X,Y).getContents()!=mapboard.get(X,Y).getContents()) {
		    			System.out.println(X);
		    			System.out.println(Y);
		    			System.out.println(ComputerMap.get(X,Y).getContents());
		    			throw new RuntimeException();
		    		}
		    	}
		    }
		    //if you take the flag or eliminate all options for the computer to move you win
		    if (Remainder == 0 || !sideCanMove(mapboard, false)) {
		    	winner(true);
		    }
		    ComputerTurn();
		    for (int X = 0; X < 10; X++) {
		    	for (int Y = 0; Y < 10; Y++) {
		    		if (ComputerMap.get(X,Y).getContents()!=mapboard.get(X,Y).getContents()) {
		    			System.out.println(X);
		    			System.out.println(Y);
		    			System.out.println(ComputerMap.get(X,Y).getContents());
		    			throw new RuntimeException();
		    		}
		    	}
		    }
		}
	}
	private void ComputerTurn() {
		//Conducts the computers turn
		Movement movement = ComputerMove();
		int x1 = movement.X1;
		int x2 = movement.X2;
		int y1 = movement.Y1;
		int y2 = movement.Y2;
		//the computer knows you see its movement
		ComputerMap.move(movement,computer);
	    mapButton square1 = mapboard.get(x1, y1);
	    mapButton square2 = mapboard.get(x2, y2);
	    //Move	
	    int Remainder = square2.getRemainder();
	    //pause to alow player to prepare for move
	   	try {
	   		Thread.sleep(1500);
	    } catch (Exception e){
	    }
	    //if computer attacks with unknown unit
	    if (Remainder!=-1&&square1.getContents() < 36) {
	    	square1.setContents(square1.getContents() + 12); //make unit known
	   	 	try { //pause to allow player to see attack
	   	 		Thread.sleep(4000);
	    	} catch (Exception e){
	    	}
	    }
		if (y1!=y2+1&&y1!=y2-1&&x1!=x2+1&&x1!=x2-1) square1.setContents(45); //if a piece moves more than one space in a turn you know its a nine
	    if (square2.getContents() >= 0 && square2.getContents() < 12) { //if attacking one of your unknown pieces
	    	square2.setContents(square2.getContents() + 12); //your piece becomes known
	    	//if the computer's piece beats yours it will move to your pieces spot
	    	if (Remainder == 11 && square1.getRemainder() == 8 || Remainder > square1.getRemainder() && Remainder != 11 || Remainder == 1 && square1.getRemainder() == 10 || Remainder == 0) {
	    		square2.setContents(square1.getContents());
	    		square1.setContents(-1);
	    		//if pieces are equal they will cancel
	    	} else if (Remainder == square1.getRemainder()) {
	    		square2.setContents(-1);
	    		square1.setContents(-1);
	    		//if your piece beats computer's then computer's piece disapears
	    	} else {
	    		square1.setContents(-1);
	    	}
	    } else {
	    	//if computer's piece can move to given spot without disapearing
	    	if (Remainder == -1 || Remainder == 11 && square1.getRemainder() == 8 || Remainder > square1.getRemainder() && Remainder != 11 || Remainder == 1 && square1.getRemainder() == 10) {
	    		square2.setContents(square1.getContents());
	    		square1.setContents(-1);
	    		//if pieces are equal
	    	} else if (Remainder == square1.getRemainder()) {
	    		square2.setContents(-1);
	    		square1.setContents(-1);
	    		//if defending piece beats computer's piece
	    	} else {
	    		square1.setContents(-1);
	    	}
	    }
	    //if computer can take your flag or eliminate all possibilities for movement
		if (Remainder == 0 || !sideCanMove(mapboard, true)) {
		   	winner(false);
		}
    	Ready1 = true;
	}
	//whether the given player can move the given piece to the given position, assuming that position does not contain a piece belonging to the player
	private static boolean testMove(int x1, int y1, int x2, int y2, Mapboard mapboard, boolean side) {
		boolean Return = false;
		mapButton square1 = mapboard.get(x1, y1); //move from
		mapButton square2 = mapboard.get(x2, y2); //move to
		int contents1 = square1.getContents();
		int contents2 = square2.getContents();
		int n = contents1 / 12;
		int remainder = contents1 - (n * 12);
		//if the given player can move the unit in general and the target square is not a lake
		if (contents2 != -2 && contents1 > 0 && remainder != 0 && remainder != 11 && (side && contents1 < 24 || !side && contents1 >= 24)) {
			if (remainder == 9) { //if scout
				if (x1 == x2 || y1 == y2) { //scouts can move any number of open squares up or down, left or right
					boolean b = true;
					if (x1 == x2) {
						int y0 = y1;
						if (y2 > y1) {
							while (b && y0 < 9) {
								y0++;
								if (y0 == y2) {
									Return = true;
									b = false;
								}
							    if (mapboard.get(x1, y0).getContents() != -1) {
									b = false;
								}
							}
						} else if (y2 != y1) {
							while (b && y0 > 0) {
								y0--;
								if (y0 == y2) {
									Return = true;
									b = false;
								}
							    if (mapboard.get(x1, y0).getContents() != -1) {
									b = false;
								}
						    }
						}
				    } else {
				    	int x0 = x1;
						if (x2 > x1) {
							while (b && x0 < 9) {
								x0++;
								if (x0 == x2) {
									Return = true;
									b = false;
								}
							    if (mapboard.get(x0, y1).getContents() != -1) {
									b = false;
								}
							}
						} else if (x2 != x1) {
							while (b && x0 > 0) {
								x0--;
								if (x0 == x2) {
									Return = true;
									b = false;
								}
							    if (mapboard.get(x0, y1).getContents() != -1) {
									b = false;
								}
						    }
						}
				    }
			    } //end scout or nine
			    //all moveable pieces can move to adjacent squares
			} else if (x2 == x1 && (y2 == y1 + 1 || y2 == y1 - 1) || y2 == y1 && (x2 == x1 + 1 || x2 == x1 - 1)) {
			    Return = true;	
			}
		}
		return Return;
	}
	//whether the given player can move the given piece to the given position, assuming that position does not contain a piece belonging to the player
	private static boolean testMove(int x1, int y1, int x2, int y2, ProbabilityMap mapboard, boolean side) {
		boolean Return = false;
		probButton square1 = mapboard.get(x1, y1); //move from
		probButton square2 = mapboard.get(x2, y2); //move to
		int contents1 = square1.getContents();
		int contents2 = square2.getContents();
		int n = contents1 / 12;
		int remainder = contents1 - (n * 12);
		//if the given player can move the unit in general and the target square is not a lake
		if (contents2 != -2 && contents1 > 0 && remainder != 0 && remainder != 11 && (side && contents1 < 24 || !side && contents1 >= 24)) {
			if (remainder == 9) { //if scout
				if (x1 == x2 || y1 == y2) { //scouts can move any number of open squares up or down, left or right
					boolean b = true;
					if (x1 == x2) {
						int y0 = y1;
						if (y2 > y1) {
							while (b && y0 < 9) {
								y0++;
								if (y0 == y2) {
									Return = true;
									b = false;
								}
							    if (mapboard.get(x1, y0).getContents() != -1) {
									b = false;
								}
							}
						} else if (y2 != y1) {
							while (b && y0 > 0) {
								y0--;
								if (y0 == y2) {
									Return = true;
									b = false;
								}
							    if (mapboard.get(x1, y0).getContents() != -1) {
									b = false;
								}
						    }
						}
				    } else {
				    	int x0 = x1;
						if (x2 > x1) {
							while (b && x0 < 9) {
								x0++;
								if (x0 == x2) {
									Return = true;
									b = false;
								}
							    if (mapboard.get(x0, y1).getContents() != -1) {
									b = false;
								}
							}
						} else if (x2 != x1) {
							while (b && x0 > 0) {
								x0--;
								if (x0 == x2) {
									Return = true;
									b = false;
								}
							    if (mapboard.get(x0, y1).getContents() != -1) {
									b = false;
								}
						    }
						}
				    }
			    } //end scout or nine
			    //all moveable pieces can move to adjacent squares
			} else if (x2 == x1 && (y2 == y1 + 1 || y2 == y1 - 1) || y2 == y1 && (x2 == x1 + 1 || x2 == x1 - 1)) {
			    Return = true;	
			}
		}
		return Return;
	}
	//whether a given player can move the given piece to the given location
	private static boolean testMoveTrue(int x1, int y1, int x2, int y2, Mapboard mapboard, boolean side) {
		int contents2 = mapboard.get(x2, y2).getContents();
		if ((contents2 == -1 || side && contents2 >= 24 || !side && contents2 < 24) && testMove(x1, y1, x2, y2, mapboard, side)) {
			return true;
		} else {
			return false;
		}
	}
	//whether a given player can move the given piece to the given location
	public static boolean testMoveTrue(int x1, int y1, int x2, int y2, ProbabilityMap mapboard, boolean side) {
		int contents2 = mapboard.get(x2, y2).getContents();
		if ((contents2 == -1 || side && contents2 >= 24 || !side && contents2 < 24) && testMove(x1, y1, x2, y2, mapboard, side)) {
			return true;
		} else {
			return false;
		}
	}
	//when somebody wins
	private void winner(boolean side) {
		//if you win
		if (side) {
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
			}
			mapboard.Victory();
			try {
				Thread.sleep(6000);
			} catch (Exception e) {
			}
			System.exit(0);
			//if computer wins
		} else {
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
			}
			mapboard.Defeat();
			try {
				Thread.sleep(4000);
			} catch (Exception e) {
			}
			System.exit(0);
		}
	}
	//whether the given side(true for your, false for computer) has any options to move
	private static boolean sideCanMove(Mapboard board, boolean side) {
		for (int x1 = 0; x1 < 10; x1++) for (int y1 = 0; y1 < 10; y1++) {
			if (board.get(x1, y1).getRemainder()==9) { //if the piece is a nine
				for (int x2 = 0; x2 < 10; x2++) { //any moves left or right
					if (testMoveTrue(x1,y1,x2,y1, board, side)) return true;
				}
				for (int y2 = 0; y2 < 10; y2++) { //any moves up or down
					if (testMoveTrue(x1,y1,x1,y2, board, side)) return true;
				}
			} else { //otherwise, see if the piece can move to any position adjacent to its current one
				if (x1>0&&testMoveTrue(x1,y1,x1-1,y1, board, side)) return true;
				if (x1<9&&testMoveTrue(x1,y1,x1+1,y1, board, side)) return true;
				if (y1>0&&testMoveTrue(x1,y1,x1,y1-1, board, side)) return true;
				if (y1<9&&testMoveTrue(x1,y1,x1,y1+1, board, side)) return true;
			}
		}
		//if no moves are availible
		return false;
	}
	//whether the given side(true for your, false for computer) has any options to move
	public static boolean sideCanMove(ProbabilityMap board, boolean side) {
		for (int x1 = 0; x1 < 10; x1++) for (int y1 = 0; y1 < 10; y1++) {
			if (board.get(x1, y1).getRemainder()==9) { //if the piece is a nine
				for (int x2 = 0; x2 < 10; x2++) { //any moves left or right
					if (testMoveTrue(x1,y1,x2,y1, board, side)) return true;
				}
				for (int y2 = 0; y2 < 10; y2++) { //any moves up or down
					if (testMoveTrue(x1,y1,x1,y2, board, side)) return true;
				}
			} else { //otherwise, see if the piece can move to any position adjacent to its current one
				if (x1>0&&testMoveTrue(x1,y1,x1-1,y1, board, side)) return true;
				if (x1<9&&testMoveTrue(x1,y1,x1+1,y1, board, side)) return true;
				if (y1>0&&testMoveTrue(x1,y1,x1,y1-1, board, side)) return true;
				if (y1<9&&testMoveTrue(x1,y1,x1,y1+1, board, side)) return true;
			}
		}
		//if no moves are availible
		return false;
	}
	//so far, a test computer
	private Movement ComputerMove() {
		if (computer!=null) return computer.nextMove(ComputerMap);
		Random random = new Random();
		if (difficulty==1) {
			while (true) {
				int x1 = random.nextInt(10);
				int y1 = random.nextInt(10);
				int x2 = random.nextInt(10);
				int y2 = random.nextInt(10);
				Movement next = new Movement(x1,y1,x2,y2,0);
				if (testMoveTrue(x1,y1,x2,y2,mapboard,false)) return next;
			}
		}
		return null;
	}
}
