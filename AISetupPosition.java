// generates the setup position of the computer

//constructor - line 16;
//method place - line 363;

import java.util.Random;

class AISetupPosition {
	// represents the setup position of the computer
	int[] position = new int[40];
	
	//the number of each piece that has yet to be placed on the board
	private int number[] = new int[12];
	
	//constructor
	public AISetupPosition() {
	    number[0] = 1;
	    number[1] = 1;
	    number[2] = 1;
	    number[3] = 2;
	    number[4] = 3;
	    number[5] = 4;
	    number[6] = 4;
    	number[7] = 4;
    	number[8] = 5;
    	number[9] = 8;
    	number[10] = 1;
	    number[11] = 6;
	    
	    //determines basic setup for bombs and flag
		int baseSetup = new Random().nextInt(4);
		int placeSetup;
		switch(baseSetup) {
			
			//twin fortress
			/*bomb								  bomb
			 *seven bomb					bomb seven
			 *flag seven bomb		  bomb seven decoy
			 **/
			case 0:
			number[11] -= 6;
			number[0] -= 1;
			number[7] -= 4;
			
			//placeSetup is effectively a subset of base setup
			//Here it decides--
			/*bomb								  bomb
			 *seven bomb					bomb seven
			 *flag seven bomb		  bomb seven decoy
			 **/
			 //vs
			 /*bomb								  bomb
			 *seven bomb					bomb seven
			 *decoy seven bomb		   bomb seven flag
			 **/
			placeSetup = new Random().nextInt(2);
			for (int i = 0; i <= 39; i++) {
				if(i < 10 || i < 19 && i != 10 || i < 28 && i > 21 || i < 37 && i > 32) {
					place(i);
				} else if(i == 10 || i == 19 || i == 21 || i == 28 || i == 32 || i == 37) {
					position[i] = 35;
				} else if(i != 30 && i != 39) {
					position[i] = 31;
				}
			}
			if (placeSetup == 0) {
				position[30] = 24;
				place(39);
			} else {
				place(30);
				position[39] = 24;
			}
			break;
			
			//small corners
			/*bomb					bomb
			 *flag bomb		  bomb seven
			 **/
			case 1:
			number[11] -= 4;
			number[0] -= 1;
			number[7] -= 1;
			
			placeSetup = new Random().nextInt(2);
			for (int i = 0; i <= 39; i++) {
				if(i == 20 || i == 29 || i == 31 || i == 38) {
					position[i] = 35;
				} else if(i != 30 && i != 39) {
					place(i);
				}
			}
			if (placeSetup == 0) {
				position[30] = 24;
				position[39] = 31;
			} else {
				position[30] = 31;
				position[39] = 24;
			}
			break;
			
			//single major fortress
			/*bomb
			 *seven bomb
			 *bomb seven bomb
			 *flag bomb seven bomb
			 **/
			case 2:
			number[11] -= 6;
			number[7] -= 3;
			number[0] -= 1;
			
			placeSetup = new Random().nextInt(2);
			if (placeSetup == 0) {
				position[0] = 35;
				position[11] = 35;
				position[22] = 35;
				position[33] = 35;
				position[20] = 35;
				position[31] = 35;
				position[10] = 31;
				position[21] = 31;
				position[32] = 31;
				position[30] = 24;
				for (int i = 1; i <= 9; i++) {
					place(i);
				}
				for (int i = 12; i <= 19; i++) {
					place(i);
				}
				for (int i = 23; i <= 29; i++) {
					place(i);
				}
				for (int i = 34; i <= 39; i++) {
					place(i);
				}
			} else {
				position[9] = 35;
				position[18] = 35;
				position[27] = 35;
				position[36] = 35;
				position[29] = 35;
				position[38] = 35;
				position[19] = 31;
				position[28] = 31;
				position[37] = 31;
				position[39] = 24;
				for (int i = 0; i <= 8; i++) {
					place(i);
				}
				for (int i = 10; i <= 17; i++) {
					place(i);
				}
				for (int i = 20; i <= 26; i++) {
					place(i);
				}
				for (int i = 30; i <= 35; i++) {
					place(i);
				}
			}
			break;
			
			//single-line-edge
			/*
			 *		  bomb 		 bomb
			 *	 bomb seven bomb flag bomb
			 **/
			case 3:
			placeSetup = new Random().nextInt(6);
			number[0] -= 1;
			if (placeSetup == 0 || placeSetup == 5) {
				number[11] -= 6;
				number[7] -= 2;
			} else {
				number[11] -= 5;
				number[7] -= 1;
			}
			for (int i = 0; i <= 19; i++) {
				place(i);
			}
			switch(placeSetup) {
				case 0:
				position[20] = 35;
				place(21);
				position[22] = 35;
				place(23);
				position[24] = 35;
				place(25);
				place(26);
				place(27);
				place(28);
				place(29);
				position[31] = 35;
				position[33] = 35;
				position[35] = 35;
				place(36);
				place(37);
				place(38);
				place(39);
				if (new Random().nextInt(3) == 0) {
					position[30] = 24;
					position[32] = 31;
					position[34] = 31;
				} else if(new Random().nextInt(2) == 0) {
					position[30] = 31;
					position[32] = 24;
					position[34] = 31;
				} else {
				    position[30] = 31;
					position[32] = 31;
					position[34] = 24;
				}
				break;
				
				case 1:
				position[21] = 35;
				place(20);
				position[23] = 35;
				place(22);
				place(24);
				place(25);
				place(26);
				place(27);
				place(28);
				place(29);
				position[30] = 35;
				position[32] = 35;
				position[34] = 35;
			    place(35);
				place(36);
				place(37);
				place(38);
				place(39);
			    if(new Random().nextInt(2) == 0) {
					position[31] = 24;
					position[33] = 31;
				} else {
					position[31] = 31;
					position[33] = 24;
				}
				break;
				
				case 2:
				place(20);
				place(21);
				position[22] = 35;
				place(23);
				position[24] = 35;
				place(25);
				place(26);
				place(27);
				place(28);
				place(29);
				place(30);
				position[31] = 35;
				position[33] = 35;
				position[35] = 35;
				place(36);
				place(37);
				place(38);
				place(39);
				if(new Random().nextInt(2) == 0) {
					position[32] = 24;
					position[34] = 31;
				} else {
					position[32] = 31;
					position[34] = 24;
				}
				break;
				
				case 3:
				place(29);
				place(28);
				position[27] = 35;
				place(26);
				position[25] = 35;
				place(24);
				place(23);
				place(22);
				place(21);
				place(20);
				place(39);
				position[38] = 35;
				position[36] = 35;
				position[34] = 35;
				place(33);
				place(32);
				place(31);
				place(30);
				if(new Random().nextInt(2) == 0) {
					position[37] = 24;
					position[35] = 31;
				} else {
					position[37] = 31;
					position[35] = 24;
				}
				break;
				
				case 4:
				position[28] = 35;
				place(29);
				position[26] = 35;
				place(27);
				place(25);
				place(24);
				place(23);
				place(22);
				place(21);
				place(20);
				position[39] = 35;
				position[37] = 35;
				position[35] = 35;
			    place(34);
				place(33);
				place(32);
				place(31);
				place(30);
			    if(new Random().nextInt(2) == 0) {
					position[38] = 24;
					position[36] = 31;
				} else {
					position[38] = 31;
					position[36] = 24;
				}
				break;
				
				case 5:
				position[29] = 35;
				place(28);
				position[27] = 35;
				place(26);
				position[25] = 35;
				place(24);
				place(23);
				place(22);
				place(21);
				place(20);
				position[38] = 35;
				position[36] = 35;
				position[34] = 35;
				place(33);
				place(32);
				place(31);
				place(30);
				if (new Random().nextInt(3) == 0) {
					position[39] = 24;
					position[37] = 31;
					position[35] = 31;
				} else if(new Random().nextInt(2) == 0) {
					position[39] = 31;
					position[37] = 24;
					position[35] = 31;
				} else {
				    position[39] = 31;
					position[37] = 31;
					position[35] = 24;
				}
				break;
			}
		}
	}
	
	//randomly determines the peice for the entered square
	private void place(int square) {
		if(square < 20) {
			number[0] -= 1;
		}
		if(square < 10) {
			boolean continuation = true;
			int i = 1;
			int BaseNumber = new Random().nextInt(number[1] + number[2] + number[3] + number[4] + number[5] + number[6] + number[7] + number[9] + number[11]) + 1;
			while(continuation && i <= 7) {
				if (BaseNumber <= number[i]) {
					continuation = false;
					position[square] = i + 24;
					number[i]--;
				}
				BaseNumber -= number[i];
				i++;
			}
			if(continuation) {
				if (BaseNumber <= number[9]) {
					position[square] = 33;
					number[9]--;
				} else {
					position[square] = 35;
					number[11]--;
				}
			}
		} else {
			boolean continuation = true;
			int total = 0;
			for (int i = 0; i <= 11; i++) {
				total += number[i];
			}
			int i = 0;
			int BaseNumber = new Random().nextInt(total) + 1;
			while(continuation) {
				if (BaseNumber <= number[i]) {
					continuation = false;
					position[square] = i + 24;
					number[i]--;
				}
				BaseNumber -= number[i];
				i++;
			}
		}
		if(square < 20) {
			number[0] += 1;
		}
	}
}
