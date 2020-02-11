// A map that uses probabilities to help the computer make decisions
// Completely Virtual

//Constructor(Mapboard) - line 21;
//Constructor(int[]) - line 63;
//method remove - line 66;
//method discover - line 69;
//method move - line 72;
//method moveAssuming - line 75;
//method setProbabillities - line 92;
//mehtod getChange - line 228;
//method get - line 232;
//method copy - line 235;

import java.util.ArrayList;
import java.util.Arrays;
class ProbabilityMap extends ArrayList<probButton[]> {
	//numbers of units
	int[] numbers = new int[48];
	int[] ComputerNumbers = new int[24];
	ProbabilityMap(Mapboard mapboard) {
		numbers[0] = 1;
		numbers[1] = 1;
		numbers[2] = 1;
		numbers[3] = 2;
		numbers[4] = 3;
		numbers[5] = 4;
		numbers[6] = 4;
		numbers[7] = 4;
		numbers[8] = 5;
		numbers[9] = 8;
		numbers[10] = 1;
		numbers[11] = 6;
		for (int i = 12; i < 24; i++) {
			numbers[i] = 0;
		}
		numbers[24] = 1;
		numbers[25] = 1;
		numbers[26] = 1;
		numbers[27] = 2;
		numbers[28] = 3;
		numbers[29] = 4;
		numbers[30] = 4;
		numbers[31] = 4;
		numbers[32] = 5;
		numbers[33] = 8;
		numbers[34] = 1;
		numbers[35] = 6;
		for (int i = 36; i < 48; i++) {
			numbers[i] = 0;
		}
		for (int i = 24; i < 48; i++) {
			ComputerNumbers[i-24]=numbers[i];
		}
		for (int y = 0; y <= 9; y++) {
			probButton[] row = new probButton[10];
			for (int x = 0; x <= 9; x++) {
				row[x] =  new probButton(mapboard.get(x, y).getContents(), x, y);
			}
			add(row);
		}
	}
	ProbabilityMap(int[] i) {
		numbers = i;
	}
	public void removePiece(int piece) {
		numbers[piece]--;
	}
	public void discover(int piece) {
		numbers[piece]--;
		numbers[piece+12]++;
	}
	public void move(Movement movement,AIMain computer) {
		if (computer!=null) computer.ChangeProbs(this,movement);
		move(movement);
	}
	public void move(Movement movement) {
		if (movement.X1>movement.X2+1||movement.X1<movement.X2-1||movement.Y1>movement.Y2+1||movement.Y1<movement.Y2-1) {
			probButton b = get(movement.X1,movement.Y1);
			if (b.getContents()==9) {
				b.SetKnownContents(21);
				numbers[9]--;
				numbers[21]++;
			} else if (b.getContents()==33) {
				b.SetKnownContents(45);
				numbers[33]--;
				numbers[45]++;
			}
		}
		int C1 = get(movement.X1,movement.Y1).getContents();
		int C2 = get(movement.X2,movement.Y2).getContents();
		if (C2==-1||C2%12<=0||C1%12==10&&C2%12==1||C1%12==8&&C2%12==11||C2%12!=11&&C1%12<C2%12) {
			probButton from=get(movement.X1,movement.Y1);
			if (C2>=0) numbers[C2]--;
			get(movement.X2,movement.Y2).SetProbContents(from.getContents(),from.getProbContents());
			from.SetKnownContents(-1);
			C1=get(movement.X2,movement.Y2).getContents();
			if (C2>=0&&(C1<12||C1>=24&&C1<36)) {
				get(movement.X2,movement.Y2).SetKnownContents(C1+12);
				numbers[C1]--;
				numbers[C1+12]++;
			}
		} else  {
			numbers[get(movement.X1,movement.Y1).getContents()]--;
			get(movement.X1,movement.Y1).SetKnownContents(-1);
			if (C1%12==C2%12) {
				numbers[get(movement.X2,movement.Y2).getContents()]--;
				get(movement.X2,movement.Y2).SetKnownContents(-1);
			} else if (C2<12||C2>=24&&C2<36) {
				numbers[get(movement.X2,movement.Y2).getContents()+12]++;
				numbers[get(movement.X2,movement.Y2).getContents()]--;
				get(movement.X2,movement.Y2).SetKnownContents(C2+12);
			}
		}
	}
	public void moveAssuming(Movement movement,char assumption) {
		if (movement.X1>movement.X2+1||movement.X1<movement.X2-1||movement.Y1>movement.Y2+1||movement.Y1<movement.Y2-1) {
			probButton b = get(movement.X1,movement.Y1);
			int n = b.getContents()/12;
			if (n==0) b.SetKnownContents(21);
			else if (n==2) b.SetKnownContents(45);
		}
		int C1 = get(movement.X1,movement.Y1).getContents()%12;
		int C2 = get(movement.X2,movement.Y2).getContents()%12;
		if (assumption=='V') {
			probButton from=get(movement.X1,movement.Y1);
			get(movement.X2,movement.Y2).SetProbContents(from.getContents(),from.getProbContents());
			from.SetKnownContents(-1);
		} else  {
			get(movement.X1,movement.Y1).SetKnownContents(-1);
			if (assumption=='E') get(movement.X2,movement.Y2).SetKnownContents(-1);
		}
	}
	//updates Probabilities
	/*	Probabilities are arrived at via the determination that the probabilitiy
	 *in an Unknown square of being an unknown unit is one
	 *as well as that fact the mean number of units per square equals
	 *the number of units of the given type
	 *divided by the number of possible squares.
	 *	To achieve this goal,
	 *adjustment numbers are selected by which we multiply every evidence number
	 *within either a unit type or a square,
	 *are selected and then adjusted
	 *by the square-root of where they would need to be
	 *in order to individualy fufill the above requirements.
	 *	This process is repeated twenty times.
	 **/ 
	public void setProbabilities() {
		//the positions of unknown blue and red-
		ArrayList<int[]> unknownBlue = new ArrayList<int[]>();
		ArrayList<int[]> unknownRed = new ArrayList<int[]>();
		for (int y = 0; y <= 9; y++) {
			probButton[] row = get(y);
			for (int x = 0; x <= 9; x++) {
				int contents = row[x].getContents();
				if (contents>=0&&contents<12) {
					int[] toSend={x,y};
					unknownRed.add(toSend);
				} else if (contents>=24&&contents<36) {
					int[] toSend={x,y};
					unknownBlue.add(toSend);
				}
			}
		}
		//sets first probabilities
		double[] numberValues = new double[48];
		double[] squareValuesB = new double[unknownBlue.size()];
		double[] squareValuesR = new double[unknownRed.size()];
		Arrays.fill(numberValues,0.0);
		Arrays.fill(squareValuesB,0.0);
		Arrays.fill(squareValuesR,0.0);
		for (int i = 0; i<unknownBlue.size(); i++) {
			int[] position = unknownBlue.get(i);
			probButton square=get(position[1])[position[0]];
			int[] probContents = square.getProbContents();
			for (int contents=24; contents<36; contents++) {
				numberValues[contents]+=probContents[contents];
				squareValuesB[i]+=probContents[contents];
			}
		}
		for (int i = 0; i<unknownRed.size(); i++) {
			int[] position = unknownRed.get(i);
			probButton square=get(position[1])[position[0]];
			int[] probContents = square.getProbContents();
			for (int contents=0; contents<12; contents++) {
				numberValues[contents]+=probContents[contents];
				squareValuesR[i]+=probContents[contents];
			}
		}
		for (int i = 0; i < 12; i++) {
			if (!(numberValues[i]<Double.MAX_VALUE)) throw new RuntimeException();
			if (numbers[i]!=0&&numberValues[i]==0.0) throw new RuntimeException();
			double nv = numberValues[i];
			if (numbers[i]!=0) numberValues[i]=Math.sqrt(numbers[i])/Math.sqrt(numberValues[i]);
			if (!(numberValues[i]<Double.MAX_VALUE)) {
				System.out.println(i);
				System.out.println(numberValues[i]);
				System.out.println(nv);
				System.out.println(numbers[i]);
				throw new RuntimeException();
			}
		}
		for (int i = 24; i < 36; i++) {
			if (!(numberValues[i]<Double.MAX_VALUE)) throw new RuntimeException();
			if (numbers[i]!=0&&numberValues[i]==0.0) throw new RuntimeException();
			double nv = numberValues[i];
			if (numbers[i]!=0) numberValues[i]=Math.sqrt(numbers[i])/Math.sqrt(numberValues[i]);
			if (!(numberValues[i]<Double.MAX_VALUE)) {
				System.out.println(i);
				System.out.println(numberValues[i]);
				System.out.println(nv);
				System.out.println(numbers[i]);
				throw new RuntimeException();
			}
		}
		for (int i = 0; i<squareValuesB.length; i++) {
			if (!(squareValuesB[i]<Double.MAX_VALUE)) throw new RuntimeException();
			if (squareValuesB[i]==0.0) throw new RuntimeException();
			squareValuesB[i]=1/Math.sqrt(squareValuesB[i]);
		}
		for (int i = 0; i<squareValuesR.length; i++) {
			if (!(squareValuesR[i]<Double.MAX_VALUE)) throw new RuntimeException();
			if (squareValuesR[i]==0.0) throw new RuntimeException();
			squareValuesR[i]=1/Math.sqrt(squareValuesR[i]);
		}
		double[] numberValuesD = new double[48]; //Dividers;
		double[] squareValuesBD = new double[unknownBlue.size()];
		double[] squareValuesRD = new double[unknownRed.size()];
		//moves probabilities close to target
		for (int I = 0; I < 20; I++) {
			for (int i = 0; i< unknownBlue.size(); i++) {
				int[] position = unknownBlue.get(i);
				probButton square=get(position[1])[position[0]];
				int[] probContents = square.getProbContents();
				double[] probs = new double[48];
				for (int count = 24; count<36; count++) {
					if (!(numberValues[count]<Double.MAX_VALUE)) {
						System.out.println(I);
						System.out.println(i);
						System.out.println(position[1]);
						System.out.println(position[0]);
						System.out.println(count);
						System.out.println(numberValues[count]);
						throw new RuntimeException();
					}
					if (!(squareValuesB[i]<Double.MAX_VALUE)) throw new RuntimeException();
					probs[count] = probContents[count]*numberValues[count]*squareValuesB[i];
				}
				square.setProbabillities(probs);
			}
			for (int i = 0; i< unknownRed.size(); i++) {
				int[] position = unknownRed.get(i);
				probButton square=get(position[1])[position[0]];
				int[] probContents = square.getProbContents();
				double[] probs = new double[48];
				for (int count = 0; count<12; count++) {
					if (!(numberValues[count]<Double.MAX_VALUE)) {
						System.out.println(I);
						System.out.println(i);
						System.out.println(position[1]);
						System.out.println(position[0]);
						System.out.println(count);
						System.out.println(numberValues[count]);
						throw new RuntimeException();
					}
					if (!(squareValuesR[i]<Double.MAX_VALUE)) throw new RuntimeException();
					probs[count] = probContents[count]*numberValues[count]*squareValuesR[i];
				}
				square.setProbabillities(probs);
			}
			Arrays.fill(numberValuesD,0.0);
			Arrays.fill(squareValuesBD,0.0);
			Arrays.fill(squareValuesRD,0.0);
			for (int i = 0; i<unknownBlue.size(); i++) {
				int[] position = unknownBlue.get(i);
				probButton square=get(position[1])[position[0]];
				double[] probContents = square.getProbabillities();
				for (int contents=24; contents<36; contents++) {
					if (!(probContents[contents]<Double.MAX_VALUE)) throw new RuntimeException();
					numberValuesD[contents]+=probContents[contents];
					squareValuesBD[i]+=probContents[contents];
				}
			}
			for (int i = 0; i<unknownRed.size(); i++) {
				long l = System.nanoTime();
				int[] position = unknownRed.get(i);
				probButton square=get(position[1])[position[0]];
				double[] probContents = square.getProbabillities();
				for (int contents=0; contents<12; contents++) {
					if (!(probContents[contents]<Double.MAX_VALUE)) throw new RuntimeException();
					numberValuesD[contents]+=probContents[contents];
					squareValuesRD[i]+=probContents[contents];
				}
			}
			for (int i = 0; i < 12; i++) {
				if (!(numberValuesD[i]<Double.MAX_VALUE)) throw new RuntimeException();
				if (numbers[i]!=0&&numberValuesD[i]==0.0) {
					System.out.println(I);
					System.out.println(i);
					System.out.println(squareValuesB.length);
					System.out.println(squareValuesR.length);
					throw new RuntimeException();
				}
				if (numbers[i]!=0) numberValues[i]/=Math.sqrt(numberValuesD[i])/Math.sqrt(numbers[i]);
			}
			for (int i = 24; i < 36; i++) {
				if (!(numberValuesD[i]<Double.MAX_VALUE)) throw new RuntimeException();
				if (numbers[i]!=0&&numberValuesD[i]==0.0) {
					System.out.println(I);
					System.out.println(i);
					System.out.println(squareValuesB.length);
					System.out.println(squareValuesR.length);
					throw new RuntimeException();
				}
				if (numbers[i]!=0) numberValues[i]/=Math.sqrt(numberValuesD[i])/Math.sqrt(numbers[i]);
			}
			for (int i = 0; i<squareValuesB.length; i++) {
				if (!(squareValuesBD[i]<Double.MAX_VALUE)) throw new RuntimeException();
				if (squareValuesBD[i]==0.0) throw new RuntimeException();
				squareValuesB[i]/=Math.sqrt(squareValuesBD[i]);
			}
			for (int i = 0; i<squareValuesR.length; i++) {
				if (!(squareValuesRD[i]<Double.MAX_VALUE)) throw new RuntimeException();
				if (squareValuesRD[i]==0.0) throw new RuntimeException();
				squareValuesR[i]/=Math.sqrt(squareValuesRD[i]);
			}
		}
		//finish update
		for (int i = 0; i< unknownBlue.size(); i++) {
			int[] position = unknownBlue.get(i);
			probButton square=get(position[1])[position[0]];
			int[] probContents = square.getProbContents();
			double[] probs = new double[48];
			for (int count = 24; count<36; count++) {
				if (!(numberValues[count]<Double.MAX_VALUE&&squareValuesB[i]<Double.MAX_VALUE)) {
					System.out.println(count);
					System.out.println(i);
					System.out.println(numberValues[count]);
					System.out.println(squareValuesB[i]);
					throw new RuntimeException();
				}
				probs[count] = probContents[count]*numberValues[count]*squareValuesB[i];
			}
			square.setProbabillities(probs);
		}
		for (int i = 0; i< unknownRed.size(); i++) {
			int[] position = unknownRed.get(i);
			probButton square=get(position[1])[position[0]];
			int[] probContents = square.getProbContents();
			double[] probs = new double[48];
			for (int count = 0; count<12; count++) {
				if (!(numberValues[count]<Double.MAX_VALUE&&squareValuesR[i]<Double.MAX_VALUE)) {
					System.out.println(count);
					System.out.println(i);
					System.out.println(numberValues[count]);
					System.out.println(squareValuesR[i]);
					throw new RuntimeException();
				}
				probs[count] = probContents[count]*numberValues[count]*squareValuesR[i];
			}
			square.setProbabillities(probs);
		}
	}
	public int getChange(Movement movement, int x, int y, int piece) {
		//For now:
		return 0;
	}
	public probButton get(int x, int y) {
		return get(y)[x];
	}
	public ProbabilityMap copy() {
		ProbabilityMap out = new ProbabilityMap(numbers.clone());
		for (int y = 0; y < 10; y++) {
			probButton[] toAdd = new probButton[10];
			probButton[] toCopy = get(y);
			for (int x = 0; x <10; x++) {
				toAdd[x] = toCopy[x].copy();
			}
			out.add(toAdd);
		}
		return out;
	}
}
