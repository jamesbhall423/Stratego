//A set of methods for helping the AI; form is not trusted

//method winner -- line 40;
//method value -- line 81;
//method pieceTaken -- line 94;
//method pieceRevealed -- line 107;
//method close -- line 120;
//method baseValues -- line 206;
//method valueChanges -- line 232;
//method numberGames -- line 262;

import java.util.Arrays;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.FileOutputStream;

class Rules {
	public static int[] numbers; //numbers of each piece each player starts with
	private static double[] basicValues = baseValues(); //basic values for each piece; length 24
	 //multiples for changing the value of each piece, based first off another type of piece
	 // and the how many of it are in the game.
	 // demensions:24x9x48;
	private static double[][][] changes;
	private static int num; //number of games that have been played using this
	private static double[] cbV; //a measure of how valuable each piece turns out to be
	private static double[][][] cc; //a measure of how valuable each piece turns out to be with given numbers of other pieces
	/**
	 * Method winner
	 * returns who will take the square
	 *
	 * @param attacker
	 * @param defender
	 *returns 1: attacker wins
	 * 2: defender wins
	 * 0: tie
	 */
	public static int winner(int attacker, int defender) {
		if (attacker<10&&defender<=10&&defender>0) {
			if (attacker<defender) {
				return 1;
			} else if (attacker>defender) {
				return 2;
			} else {
				return 0;
			}
		}
		if (defender==0) {
			return 1;
		}
		if (defender==11) {
			if (attacker==8) {
				return 1;
			}
			return 2;
		}
		if (defender==1) { //attacker==10
			return 1;
		}
		if (defender==10) {
			return 0;
		}
		return 2;
	}

	/**
	 * Method value
	 *
	 *
	 * @param attacker
	 * @param defender
	 * @param piece -- defender's
	 * @param side
	 *
	 * @return
	 *
	 */
	 //returns the value of the given piece in the given situation
	public static double value(int[] attacker, int[] defender, int piece) {
		double value = basicValues[piece];
		for (int i = 0; i < 24; i++) {
			value*=changes[i][defender[i]][piece];
		}
		for (int i = 0; i < 24; i++) {
			value*=changes[i][attacker[i]][piece+24];
		}
		return value;
	}

	
	 //records one piece taking another and the situation in which it occured.
	public static void pieceTaken(int taker, int taken,int[] attacker, int[] defender) {
		double valueTaken = value(attacker, defender, taken); //value of the taken piece
		//this gives taking piece more value
		cbV[taker]*=1+(valueTaken/(4*(3+num)));
		//and even more value in the given situation
		for (int i = 0; i< 24; i++) {
			cc[i][attacker[i]][taker]*=1+(valueTaken/(4*(3+num)));
		}
		for (int i = 0; i< 24; i++) {
			cc[i][defender[i]][taker+24]*=1+(valueTaken/(4*(3+num)));
		}
	}
	 //records one piece discovering the value of another and the situation in which it occured.
	public static void pieceRevealed(int discovering, int revealed,int[] attacker, int[] defender) {
		double valueRevealed = value(attacker, defender, revealed)-value(attacker, defender, revealed+12); //value of the revealed piece
		//this gives discovering piece more value
		cbV[discovering]*=1+(valueRevealed/(4*(3+num)));
		//and even more value in the given situation
		for (int i = 0; i< 24; i++) {
			cc[i][attacker[i]][discovering]*=1+(valueRevealed/(4*(3+num)));
		}
		for (int i = 0; i< 24; i++) {
			cc[i][defender[i]][discovering+24]*=1+(valueRevealed/(4*(3+num)));
		}
	}
	//writes the records to files
	public static void close(int[] loserRemaining) throws FileNotFoundException {
		double[] percents = new double[12]; //how much the loser has lost of each unit type
		for (int i = 0; i < 12; i++) {
			percents[i] = 1-((double) loserRemaining[i])/numbers[i];
		}
		for (int i = 0; i < 12; i++) {
			percents[i] = (percents[i]+(1-((double) loserRemaining[i+12])/numbers[i]))/2;
		}
		for (int i = 0; i < 12; i++) {
			if (percents[i]>.5) { //if losses are significant
				cbV[i]*=1+(1/(3+num));
				cbV[i+12]*=1+(1/(3+num));
				cc[i][loserRemaining[i]][i]*=1+(1/(2*(3+num)));
				cc[i+12][loserRemaining[i]][i]*=1+(1/(2*(3+num)));
				cc[i][loserRemaining[i]][i+12]*=1+(1/(2*(3+num)));
				cc[i+12][loserRemaining[i]][i+12]*=1+(1/(2*(3+num)));
				cc[i][loserRemaining[i]+1][i]*=1+(1/(5*(3+num)));
				cc[i+12][loserRemaining[i]+1][i]*=1+(1/(5*(3+num)));
				cc[i][loserRemaining[i]+1][i+12]*=1+(1/(5*(3+num)));
				cc[i+12][loserRemaining[i]+1][i+12]*=1+(1/(5*(3+num)));
			}
		}
		//make numbers  geometricly average one
		double bvRoot = 1.0;
		double cRoot = 1.0;
		for (int i = 0; i < 24; i++) {
			int add = 0;
			if (i>=12) {
				add-=12;
			}
			basicValues[i]*=cbV[i];
			bvRoot*=basicValues[i];
			for (int I = 0; I < numbers[i+add]; I++) {
				for (int t = 0; t < 48; t++) {
					changes[i][I][t]*=cc[i][I][t];
					cRoot*=changes[i][I][t];
				}
			}
		}
		bvRoot = Math.pow(bvRoot,1.0/24.0);
		cRoot = Math.pow(bvRoot,1.0/3840.0);
		for (int i = 0; i < 24; i++) {
			int add = 0;
			if (i>=12) {
				add-=12;
			}
			basicValues[i]/=bvRoot;
			for (int I = 0; I <= numbers[i+add]; I++) {
				for (int t = 0; t < 48; t++) {
					changes[i][I][t]/=cRoot;
				}
			}
		}
		//print basicValues
		PrintStream output= new PrintStream(new FileOutputStream("TextFiles/basicValues.txt"));
		for (int i = 0; i < 24; i++) {
			output.print(basicValues[i]+" ");
		}
		output.close();
		//print number of Games
		output = new PrintStream(new FileOutputStream("TextFiles/Games.txt"));
		output.print(num+1);
		output.close();
		//print changes
		output = new PrintStream(new FileOutputStream("TextFiles/changes.txt"));
		for (int i = 0; i < 12; i++) {
			for (int I = 0; I <= numbers[i]; I++) { //up to the max possible number of pieces of the given type
				for (int t = 0; t < 48; t++) {
					output.print(changes[i][I][t]+" ");
				}
				output.println();
			}
			output.println();
		}
		for (int i = 0; i < 12; i++) {
			for (int I = 0; I <= numbers[i]; I++) { //up to the max possible number of pieces of the given type
				for (int t = 0; t < 48; t++) {
					output.print(changes[i+12][I][t]+" ");
				}
				output.println();
			}
			output.println();
		}
		output.close();
	}
	//reads basicValues from file and does other initialization work;
	private static double[] baseValues() {
		cbV = new double[24];
		cc = new double[24][9][48];
		double[] out = new double[24];
		Scanner input=null;
		try {
			input = new Scanner(new FileInputStream("TextFiles/basicValues.txt"));
		} catch (FileNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}
		for (int i = 0; i < 24; i++) {
			out[i]=input.nextDouble();
		}
		input.close();
		int[] load = {1,1,1,2,3,4,4,4,5,8,1,6};
		numbers = load;
		//read changes from file
		changes=valueChanges();
		//read number of Games from file
		num=numberGames();
		//provides starting point for recording data
		Arrays.fill(cbV,1.0);
		for (int i = 0; i < 24; i++)  for (int I = 0; I < 9; I++) for (int t = 0; t < 48; t++) cc[i][I][t]=1.0;
		return out;
	}
	//reads valueChanges from file
	private static double[][][] valueChanges() {
		double[][][] out = new double[24][9][48];
		Scanner input=null;
		try {
			input = new Scanner(new FileInputStream("TextFiles/changes.txt"));
		} catch (FileNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}
		for (int i = 0; i < 12; i++) {
			for (int I = 0; I <= numbers[i]; I++) { //up to the max possible number of pieces of the given type
				for (int t = 0; t < 48; t++) {
					out[i][I][t]=input.nextDouble();
				}
				input.nextLine();
			}
			input.nextLine();
		}
		for (int i = 0; i < 12; i++) {
			for (int I = 0; I <= numbers[i]; I++) { //up to the max possible number of pieces of the given type
				for (int t = 0; t < 48; t++) {
					out[i+12][I][t]=input.nextDouble();
				}
				input.nextLine();
			}
			input.nextLine();
		}
		input.close();
		return out;
	}
	//reads number of Games from file
	private static int numberGames() {
		Scanner input=null;
		try {
			input = new Scanner(new FileInputStream("TextFiles/Games.txt"));
		} catch (FileNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}
		int out = input.nextInt();
		input.close();
		return out;
	}
}
