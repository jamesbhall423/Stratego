/**
 *This is the class that calls the primary game engine
 */
 
 // method main line 16;
 
class Run {
	
	/**
	 * Method main
	 *
	 *
	 * @param args
	 *
	 */
	public static void main(String[] args) throws Exception {
		InfoBox box = new InfoBox();
	}
	public static void test() {
		Mapboard board = new Mapboard();
		for (int i = 0; i < 10; i++) {
			for (int y = 0; y < 4; y++) {
				board.get(i,y).setContents(24);
			}
			for (int y = 6; y < 10; y++) {
				board.get(i,y).setContents(0);
			}
		}
		ProbabilityMap map = new ProbabilityMap(board);
		map.get(6,9).SetKnownContents(-1);
		map.removePiece(2);
		map.setProbabilities();
		for (int y = 0; y < 10;y++) {
			for (int x = 0; x < 10; x++) {
				int c = map.get(x,y).getContents();
				if (c>=0&&c<12) for (int probs =0; probs < 12;probs++) System.out.print(map.get(x,y).getProbabillities()[probs]+" ");
				System.out.print("- ");
				if (c>=24&&c<36) for (int probs =24; probs < 36;probs++) System.out.print(map.get(x,y).getProbabillities()[probs]+" ");
				System.out.println();
			}
			System.out.println("--");
		}
	}
}
