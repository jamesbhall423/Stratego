//Basic AI engine

//method nextMove line 22;
//method possibleCMoves line 47;
//methods changeInDistance lines 67/72/103;
//method estimateDistance line 173;
//method probLoss line 221;
//method value line 240;
//method ChangeProbs line 304;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
class Dificulty1 implements AIMain {
	private final double[] baseValues = {5.0,1.0,0.7,0.5,0.4,0.32,0.25,0.20,0.3,0.12,0.3,0.2};
	private boolean[][] AWB;
	public boolean[][] bombs;
	private Random random = new Random();
	/**
	 * Method nextMove
	 *
	 *
	 * @param map
	 *
	 * @return
	 *
	 */
	public Dificulty1() {
		AWB=new boolean[10][10];
		bombs=new boolean[10][10];
		for (int x = 0; x < 10; x++) for (int y = 0; y < 10; y++) bombs[x][y]=false;
		for (int x = 0; x < 10; x++) for (int y = 0; y < 10; y++) AWB[x][y]=(y>6);
	}
	public Movement nextMove(ProbabilityMap map) {
		ArrayList<Movement> possibleMoves = possibleCMoves(map);
		map.setProbabilities();
		Movement moveHolder = null;
		for (Movement next: possibleMoves) {
			int c1 = map.get(next.X1,next.Y1).getContents();
			int c2 = map.get(next.X2,next.Y2).getContents();
			for (int X1 = 0; X1 < 10; X1++) for (int Y1 = 0; Y1 < 10; Y1++) {
				int C1 = map.get(X1,Y1).getContents();
				if (C1>12&&(C1%12<=4&&C1%12!=0||C1%12==10||C1%12==8||X1<next.X1+3&&Y1<next.Y1+3&&X1>next.X1-3&&Y1>next.Y1-3||X1<next.X2+3&&Y1<next.Y2+3&&X1>next.X2-3&&Y1>next.Y2-3)) for (int X2 = 0; X2 < 10; X2++) for (int Y2 = 0; Y2 < 10; Y2++) {
					int C2 = map.get(X2,Y2).getContents();
					if (C2>=0&&(C1>=24&&C2<24||C2>=24&&C1<24)) if (next.X1==X1&&next.Y1==Y1&&next.X2==X2&&next.Y2==Y2) {
						double d = value(C1,C2,false,map.get(X2,Y2).getProbabillities());
						if (!(d<Double.MAX_VALUE)) throw new RuntimeException();
						next.Value+=d;
					} else if (C2<=12) {
						double d1;
						try {
							d1 = value(C1,C2,true,map.get(X2,Y2).getProbabillities());
						} catch (RuntimeException e) {
							System.out.println(X2);
							System.out.println(Y2);
							throw(e);
						}
						double d2 = changeInDistance(map,next.X1,next.Y1,next.X2,next.Y2,X1,Y1,X2,Y2);
						if (!(d1<Double.MAX_VALUE)) throw new RuntimeException();
						if (!(d2<Double.MAX_VALUE)) throw new RuntimeException();
						next.Value-=d1*d2/160;
					} else if ((next.X1==X1&&next.Y1==Y1||next.X2==X1&&next.Y2==Y1)&&(C2%12<=4||C2%12>=10||C2%12==8)||(next.X1==X2&&next.Y1==Y2||next.X2==X2&&next.Y2==Y2)&&(C1%12<=4||C1%12>=10||C1%12==8)||(C2%12<=4||C2%12>=10||C2%12==8)&&(C1%12<=4||C1%12>=10||C1%12==8)||X2<next.X1+3&&X2>next.X1-3&&Y2<next.Y1+3&&Y2>next.Y1-3||X2<next.X2+4&&X2>next.X2-3&&Y2<next.Y2+3&&Y2>next.Y2-3) {
						if (!(C1%12==10&&C2%12==1&&(next.X1==X1&&next.Y1==Y1)&&(Math.abs(next.X2-X2)+Math.abs(next.Y2-Y2))==1||C2%12==10&&C1%12==1&&(next.X1==X2&&next.Y1==Y2)&&(Math.abs(next.X2-X1)+Math.abs(next.Y2-Y1))==1)) {
							double d1 = value(C1,C2,true,null);
							double d2 = changeInDistance(map,next.X1,next.Y1,next.X2,next.Y2,X1,Y1,X2,Y2);
							if (!(d1<Double.MAX_VALUE)) throw new RuntimeException();
							if (!(d2<Double.MAX_VALUE)) throw new RuntimeException();
							next.Value-=d1*d2/80;
						}
					}
				}
			}
			if (!(next.Value<Double.MAX_VALUE)) throw new RuntimeException();
			if (moveHolder==null||next.Value>moveHolder.Value) moveHolder=next;
		}
		return moveHolder;
	}
	private ArrayList<Movement> possibleCMoves(ProbabilityMap map) {
		ArrayList<Movement> out = new ArrayList<Movement>();
		for (int x1 = 0; x1 < 10; x1++) for (int y1 = 0; y1 < 10; y1++) {
			int contents = map.get(x1,y1).getContents();
			if (contents>24&&contents<48) {
				if (contents%12==9) {
					for (int x2 = 0; x2 < 10; x2++) {
						if (x2!=x1&&GameFrame.testMoveTrue(x1,y1,x2,y1,map,false)) out.add(new Movement(x1,y1,x2,y1,0));
					}
					for (int y2 = 0; y2 < 10; y2++) {
						if (y2!=y1&&GameFrame.testMoveTrue(x1,y1,x1,y2,map,false)) out.add(new Movement(x1,y1,x1,y2,0));
					}
				} else {
					if (x1<9&&GameFrame.testMoveTrue(x1,y1,x1+1,y1,map,false)) out.add(new Movement(x1,y1,x1+1,y1,0));
					if (x1>0&&GameFrame.testMoveTrue(x1,y1,x1-1,y1,map,false)) out.add(new Movement(x1,y1,x1-1,y1,0));
					if (y1<9&&GameFrame.testMoveTrue(x1,y1,x1,y1+1,map,false)) out.add(new Movement(x1,y1,x1,y1+1,0));
					if (y1>0&&GameFrame.testMoveTrue(x1,y1,x1,y1-1,map,false)) out.add(new Movement(x1,y1,x1,y1-1,0));
				}
			}
		}
		return out;
	}
	private double[][][][] changeInDistance(ProbabilityMap map,int x1, int y1, int x2, int y2) {
		double[][][][] out = new double[10][10][10][10];
		for (int X1 = 0; X1 < 10; X1++) for (int Y1 = 0; Y1 < 10; Y1++) for (int X2 = 0; X2 < 10; X2++) for (int Y2 = 0; Y2 < 10; Y2++) out[X1][Y1][X2][Y2]=changeInDistance(map,x1,y1,x2,y2,X1,Y1,X2,Y2);
		return out;
	}
	private double changeInDistance(ProbabilityMap map,int x1, int y1, int x2, int y2, int X1, int Y1, int X2, int Y2) { //smaller changes at greater distance
		int C1 = map.get(X1,Y1).getContents();
		int C2 = map.get(X2,Y2).getContents();
		if (C1<0||C2<0||X1==X2&&Y1==Y2) return 0.0; //eliminate unneccesary connections
		if (x2==X2&&y2==Y2) { //move to target spot
			return changeInDistance(map,x1,y1,x2,y2,X1,Y1, false,false);
		}
		if (x2==X1&&y2==Y1) {
			return changeInDistance(map,x1,y1,x2,y2,X2,Y2, true,false); //reverse points
		}
		if (x1==X1&&y1==Y1) {
			return changeInDistance(map,x1,y1,x2,y2,X2,Y2,true,true);
		}
		if (x1==X2&&y1==Y2) {
			return changeInDistance(map,x1,y1,x2,y2,X1,Y1,false,true);
		}
		int C4 = map.get(x2,y2).getContents();
		int R3 = map.get(x1,y1).getRemainder();
		double baseDistance=Math.pow(Math.pow(X2-X1,2)+Math.pow(Y2-Y1,2),0.25);
		if (C4>=0) {
			double probSame;
			if (C4<12) probSame=map.get(x2,y2).getProbabillities()[R3];
			else if (R3==C4-12) probSame=1.0;
			else probSame=0.0;
			if ((Y2>=Y1)==(X2>=X1)) return -probSame/(baseDistance*Math.pow(Math.pow((X2-x2)*(y2-Y1)+(x2-X1)*(Y2-y2),2)+10,0.5))-1/(baseDistance*Math.pow(Math.pow((X2-x1)*(y1-Y1)+(x1-X1)*(Y2-y1),2)+10,0.5));
			else return -probSame/(baseDistance*Math.pow(Math.pow((x2-X1)*(y2-Y1)+(X2-x2)*(Y2-y2),2)+10,0.5))-1/(baseDistance*Math.pow(Math.pow((x1-X1)*(y1-Y1)+(X2-x1)*(Y2-y1),2)+10,0.5));
		}
		if ((Y2>=Y1)==(X2>=X1)) return 1/(baseDistance*Math.pow(Math.pow((X2-x2)*(y2-Y1)+(x2-X1)*(Y2-y2),2)+10,0.5))-1/(baseDistance*Math.pow(Math.pow((X2-x1)*(y1-Y1)+(x1-X1)*(Y2-y1),2)+10,0.5));
		else return 1/(baseDistance*Math.pow(Math.pow((x2-X1)*(y2-Y1)+(X2-x2)*(Y2-y2),2)+10,0.5))-1/(baseDistance*Math.pow(Math.pow((x1-X1)*(y1-Y1)+(X2-x1)*(Y2-y1),2)+10,0.5)); 
	}
	private double changeInDistance(ProbabilityMap map,int x1, int y1, int x2, int y2, int X1, int Y1, boolean switch1,boolean switch2) { //X2=x2;Y2=y2;, smaller changes at greater distance
		int X2;
		int Y2;
		if (switch1) {
			X2=X1;
			Y2=Y1;
			if (switch2) {
				X1=x1;
				Y1=y1;
			} else {
				X1=x2;
				Y1=y2;
			}
		} else {
			if (switch2) {
				X2=x1;
				Y2=y1;
			} else {
				X2=x2;
				Y2=y2;
			}
		}
		if (!switch2&&(x1==X1&&y1==Y1||x1==X2&&y1==Y2)) return -1.0; //falls from one to zero
		int C1 = map.get(x1,y1).getContents(); //always computer
		int C2 = map.get(x2,y2).getContents(); //always player
		boolean cut=false;
		if (C2<12&&C2>=0) {
			if (switch2) {
				double[] probLoss=probLoss(map,x1,y1,x2,y2);
				double cDistance=probLoss[0]*estimateDistance(map,X1,Y1,X2,Y2);
				ProbabilityMap mapC = map.copy();
				mapC.moveAssuming(new Movement(x1,x2,y1,y2,0),'V');
				if (switch1) cDistance+=(1-probLoss[0])*estimateDistance(mapC,x2,y2,X2,Y2)-estimateDistance(map,X1,Y1,X2,Y2);
				else cDistance+=(1-probLoss[0])*estimateDistance(mapC,X1,Y1,x2,y2)-estimateDistance(map,X1,Y1,X2,Y2);
				return cDistance;
			} else return 0.0; //target end unknown
		}
		int R1 = C1%4;
		int R2 = C2%4;
		if (C2!=-1&&switch2!=(R2==0||R2==11&&R1==8||R2==1&&R1==10||R2!=11&&R1<R2)||R1==R2) { //end eliminated
			return estimateDistance(map,X1,Y1,X2,Y2)-1;
		} else if (!switch2) { //peice eliminated
			int XX1;
			int XX2;
			int YY1;
			int YY2;
			if (X1<=X2) {
				XX1=X1;
				XX2=X2;
			} else {
				XX1=X2;
				XX2=X1;
			}
			if (Y1<=Y2) {
				YY1=Y1;
				YY2=Y2;
			} else {
				YY1=Y2;
				YY2=Y1;
			}
			double baseDistance=Math.pow(Math.pow(XX2-XX1,2)+Math.pow(YY2-YY1,2),0.25);
			if ((X1<=X2)==(Y1<=Y2)) return -1/(baseDistance*Math.pow(Math.pow((X2-x1)*(y1-Y1)+(x1-X1)*(Y2-y1),2)+10,0.5));
			else return -1/(baseDistance*Math.pow(Math.pow((x1-X1)*(y1-Y1)+(X2-x1)*(Y2-y1),2)+1,0.5));
		} else { //end moved
			ProbabilityMap mapC = map.copy();
			mapC.move(new Movement(x1,y1,x2,y2,0));
			if (switch1) return estimateDistance(mapC,x2,y2,X2,Y2)-estimateDistance(map,X1,Y1,X2,Y2);
			else return estimateDistance(mapC,X1,Y1,x2,y2)-estimateDistance(map,X1,Y1,X2,Y2);
		}
	}
	private double estimateDistance(ProbabilityMap map, int X1, int Y1, int X2, int Y2) {
		if (!map.get(X1,Y1).isKnown()) return 0; //Computer cannot spy on player.
		int XX1;
		int XX2;
		int YY1;
		int YY2;
		if (X1<=X2) {
			XX1=X1;
			XX2=X2;
		} else {
			XX1=X2;
			XX2=X1;
		}
		if (Y1<=Y2) {
			YY1=Y1;
			YY2=Y2;
		} else {
			YY1=Y2;
			YY2=Y1;
		}
		int R3=map.get(X1,Y1).getRemainder();
		double baseDistance;
		double multiplier = 1;
		if (R3==9) {
			baseDistance = Math.pow(Math.pow(XX2-XX1,2)+Math.pow(YY2-YY1,2),0.125);
			multiplier=baseDistance;
		} else baseDistance = Math.pow(Math.pow(XX2-XX1,2)+Math.pow(YY2-YY1,2),0.25);
		double distance=baseDistance;
		int XX1s = XX1;
		if (XX1>0) XX1s--;
		int YY1s = YY1;
		if (YY1>0) YY1s--;
		int XX2s = XX2;
		if (XX2<9) XX2s++;
		int YY2s = YY2;
		if (YY2<9) YY2s++;
		for (int XX = XX1s; XX <= XX2s; XX++) for (int YY = YY1s; YY <= YY2s; YY++) if ((XX!=XX1||YY!=YY1)&&(XX!=XX2||YY!=YY2)) {
			int contents = map.get(XX,YY).getContents();
			if (contents==-2||contents==35||contents==47||contents==24) {
				if ((X1<=X2)==(Y1<=Y2)) distance+=2/(multiplier*baseDistance*Math.pow(Math.pow((X2-XX)*(YY-Y1)+(XX-X1)*(Y2-YY),2)+10,0.5)); //pieces upper-left-hand, lower-right-hand
				else distance+=2/(multiplier*baseDistance*Math.pow(Math.pow((XX-X2)*(YY-Y1)+(X1-XX)*(Y2-YY),2)+10,0.5));
			} else if (contents>24) {
				if ((X1<=X2)==(Y1<=Y2)) distance+=1/(multiplier*baseDistance*Math.pow(Math.pow((X2-XX)*(YY-Y1)+(XX-X1)*(Y2-YY),2)+10,0.5)); //pieces upper-left-hand, lower-right-hand
				else distance+=1/(multiplier*baseDistance*Math.pow(Math.pow((XX-X2)*(YY-Y1)+(X1-XX)*(Y2-YY),2)+10,0.5));
			}
		}
		return distance;
	}
	private double[] probLoss(ProbabilityMap map, int x1, int y1, int x2, int y2) {//1computer,2player
		double[] out = new double[2];
		int R1=map.get(x1,y1).getRemainder();
		int C2=map.get(x2,y2).getContents();
		if (C2==-1) throw new RuntimeException();
		if (C2<12) {
			double[] probs = map.get(x2,y2).getProbabillities();
			for (int i = 0; i < 12; i++) {
				if (!(i==0||R1==8&&i==11||R1==10&&i==1||i!=11&&i>R1)) out[0]+=probs[i];
				if (i==0||R1==8&&i==11||R1==10&&i==1||i!=11&&i>=R1) out[1]+=probs[i];
			}
		} else {
			int R2=C2%4;
			if (R2==0||R1==8&&R2==11||R1==10&&R2==1||R2!=11&&R2>R1) out[0]=0.0;
			else out[0]=1.0;
			if (R2==0||R1==8&&R2==11||R1==10&&R2==1||R2!=11&&R2>=R1) out[1]=1.0;
			else out[1]=0.0;
		}
		return out;
	}
	private double value(int C1, int C2, boolean nonDirect,double[] probs2) {
		if (C2==-1) throw new RuntimeException();
		if (nonDirect) {
			if (C2==23&&C1!=32&&C1!=44) return 0;
			if (C1==34&&C2==13) return 0.6;
		}
		if (C1>=12&&C2>=12) {
			if (C1>=24) {
				if (C1>=36||nonDirect) {
					C1=C1%12;
					C2-=12;
					if (C2==0||C1==10&&C2==1||C1==8&&C2==11||C2!=11&&C1<C2) return baseValues[C2];
					else if (C1!=C2) return -baseValues[C1];
					else return 0;
				} else {
					C1-=24;
					C2-=12;
					if (C2==0||C1==10&&C2==1||C1==8&&C2==11||C2!=11&&C1<C2) return baseValues[C2]-(0.5*baseValues[C1]);
					else if (C1!=C2) return -1.5*baseValues[C1];
					else return -0.5*baseValues[C1];
				}
			} else {
				if (C2>=36||nonDirect) {
					C2=C2%12;
					C1-=12;
					if (C2==0||C1==10&&C2==1||C1==8&&C2==11||C2!=11&&C1<C2) return -baseValues[C2];
					else if (C1!=C2) return baseValues[C1];
					else return 0;
				} else {
					C2-=24;
					C1-=12;
					if (C2==0||C1==10&&C2==1||C1==8&&C2==11||C2!=11&&C1<C2) return -1.5*baseValues[C2];
					else if (C1!=C2) return baseValues[C1]-0.5*baseValues[C2];
					else return -0.5*baseValues[C2];
				}
			}
		} else if (C2<=12) {
			if (!nonDirect) {
				double out = 0.0;
				int R = C1%12;
				for (int i = 0; i < 12; i++) {
					if (!(probs2[i]<Double.MAX_VALUE)) {
						System.out.println(i);
						System.out.println(probs2[i]);
						throw new RuntimeException();
					}
					if (C1>=36) {
						if (i==0||R==10&&i==1||R==8&&i==11||i!=11&&R<i) out+=probs2[i]*1.5*baseValues[i];
						else if (R!=i) out-=probs2[i]*(baseValues[R]-0.5*baseValues[i]);
						else out+=probs2[i]*0.5*baseValues[i];
					} else {
						if (i==0||R==10&&i==1||R==8&&i==11||i!=11&&R<i) out+=probs2[i]*(1.5*baseValues[i]-(0.5*baseValues[R]));
						else if (R!=i) out-=probs2[i]*(1.5*baseValues[R]+0.5*baseValues[i]);
					}
				}
				return out;
			} else {
				double out = 0.0;
				int R=C1%12;
				for (int i = 0; i < 12; i++) {
					if (!(probs2[i]<Double.MAX_VALUE)) {
						System.out.println(i);
						System.out.println(probs2[i]);
						throw new RuntimeException();
					}
					if (i==11) if (R==8) out+=probs2[i]*baseValues[11]; else ;
					else if (i==1&&R==10) out+=0.6*probs2[i];
					else if (i==10&&R==1) out-=0.6*probs2[i];
					else if (i==0||R<i) out+=probs2[i]*baseValues[i];
					else if (R!=i) out-=probs2[i]*baseValues[R];
				}
				return out;
			}
		} else return 0.0;
	}
	public void ChangeProbs(ProbabilityMap map, Movement move) {
		int c1 = map.get(move.X1,move.Y1).getContents();
		int c2 = map.get(move.X2,move.Y2).getContents();
		if (!(AWB[move.X1][move.Y1]&&AWB[move.X2][move.Y2])&&!(bombs[move.X1][move.Y1]||bombs[move.X2][move.Y2])) {
			if (map.get(move.X2,move.Y2).getProbContents()!=null&&map.get(move.X2,move.Y2).getProbContents()[0]>1) map.get(move.X2,move.Y2).getProbContents()[0]=1;
			if (move.X1>0&&map.get(move.X1-1,move.Y1).getProbContents()!=null&&map.get(move.X1-1,move.Y1).getProbContents()[0]>1) map.get(move.X1-1,move.Y1).getProbContents()[0]=1;
			if (move.X1<9&&map.get(move.X1+1,move.Y1).getProbContents()!=null&&map.get(move.X1+1,move.Y1).getProbContents()[0]>1) map.get(move.X1+1,move.Y1).getProbContents()[0]=1;
			if (move.Y1>0&&map.get(move.X1,move.Y1-1).getProbContents()!=null&&map.get(move.X1,move.Y1-1).getProbContents()[0]>1) map.get(move.X1,move.Y1-1).getProbContents()[0]=1;
			if (move.Y1<9&&map.get(move.X1,move.Y1+1).getProbContents()!=null&&map.get(move.X1,move.Y1+1).getProbContents()[0]>1) map.get(move.X1,move.Y1+1).getProbContents()[0]=1;
			if (move.X2>0&&map.get(move.X2-1,move.Y2).getProbContents()!=null&&map.get(move.X2-1,move.Y2).getProbContents()[0]>1) map.get(move.X2-1,move.Y2).getProbContents()[0]=1;
			if (move.X2<9&&map.get(move.X2+1,move.Y2).getProbContents()!=null&&map.get(move.X2+1,move.Y2).getProbContents()[0]>1) map.get(move.X2+1,move.Y2).getProbContents()[0]=1;
			if (move.Y2>0&&map.get(move.X2,move.Y2-1).getProbContents()!=null&&map.get(move.X2,move.Y2-1).getProbContents()[0]>1) map.get(move.X2,move.Y2-1).getProbContents()[0]=1;
			if (move.Y2<9&&map.get(move.X2,move.Y2+1).getProbContents()!=null&&map.get(move.X2,move.Y2+1).getProbContents()[0]>1) map.get(move.X2,move.Y2+1).getProbContents()[0]=1;
			AWB[move.X1][move.Y1]=false;
			AWB[move.X2][move.Y2]=false;
			if (move.X1>0) AWB[move.X1-1][move.Y1]=false;
			if (move.X1<9) AWB[move.X1+1][move.Y1]=false;
			if (move.Y1>0) AWB[move.X1][move.Y1-1]=false;
			if (move.Y1<9) AWB[move.X1][move.Y1+1]=false;
			if (move.X2>0) AWB[move.X2-1][move.Y2]=false;
			if (move.X2<9) AWB[move.X2+1][move.Y2]=false;
			if (move.Y2>0) AWB[move.X2][move.Y2-1]=false;
			if (move.Y2<9) AWB[move.X2][move.Y2+1]=false;
		}
		if (c1<24) {
			if (c1<12) {
				int[] pc1 = map.get(move.X1,move.Y1).getProbContents();
				pc1[0]=0;
				pc1[11]=0;
				if (c2==-1) {
					for (int x = 0; x < 10; x++) for (int y = 0; y < 10; y++) if (map.get(x,y).getContents()>=36) {
						double cDistance = (Math.abs(move.X2-x)+Math.abs(move.Y2-y)-Math.abs(move.X1-x)-Math.abs(move.Y1-y))/(Math.abs(move.X1-x)+Math.abs(move.Y1-y));
						int[] probContents = map.get(move.X1,move.Y1).getProbContents();
						for (int i = 0; i < 12; i++) {
							if (value(i+12,map.get(x,y).getContents(),true,null)<0.0) probContents[i]+=(int) -1000*cDistance*value(i+12,map.get(x,y).getContents(),true,null);
						}
					}
				}
			}
		} else if (c2==11) {
			 bombs[move.X2][move.Y2]=true;
		}
	}
}
