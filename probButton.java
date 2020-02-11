// a square involving probabillities

//constructor line 28;
//method getContents line 56;
//method getProbContents line 59;
//method getX line 62;
//method getY line 65;
//method SetKnownContents line 68;
//method SetProbContents line 74;
//method setProbabillities line 78;
//method getProbabillities line 81;
//method getRemainder line 84;
//method copy line 91;
//method value line 99;
//method netChange line 111;
//method getQuadrant line 171;
//method isKnown line 174;
class probButton {
	
	private int Contents;
	
	//probabillity the contents will be a certain thing if contents is not known
	private int[] probContents = new int[48];
	private double[] probabillities = null;
	
	private int X;
	private int Y;
	probButton(int contents, int x, int y) {
		Contents = contents;
		X = x;
		Y = y;
		if (Contents>=0&&Contents<12) {
			if (Y < 8) {
				probContents[0] = 1;
				probContents[11] = 10000;
			} else {
				probContents[0] = 20000;
				probContents[11] = 20000;
			}
			for (int i = 1; i <= 10; i++) {
				probContents[i] = 20000;
			}
		} else if (Contents>=24&&Contents<36) {
			if (Y >= 2) {
				probContents[24] = 1;
				probContents[35] = 10000;
			} else {
				probContents[24] = 20000;
				probContents[35] = 20000;
			}
			for (int i = 25; i <= 34; i++) {
				probContents[i] = 20000;
			}
		} else probContents=null;
	}
	public int getContents() {
		return Contents;
	}
	public int[] getProbContents() {
		return probContents;
	}
	public int getX() {
		return X;
	}
	public int getY() {
		return Y;
	}
	public void SetKnownContents(int Contents) {
		this.Contents=Contents;
		probContents=null;
	}
	public void SetProbContents(int Contents, int[] probContents) {
		this.Contents=Contents;
		this.probContents=probContents;
	}
	public void setProbabillities(double[] probabillities) {
		this.probabillities=probabillities;
	}
	public double[] getProbabillities() {
		return probabillities;
	}
	public int getRemainder() {
	 	if (Contents >= 0) {
	 		return Contents - (Contents / 12) * 12;
	 	} else {
	 		return Contents;
	 	}
	}
	public probButton copy() {
		probButton out = new probButton(Contents, X, Y);
		if (probContents!=null) out.SetProbContents(Contents, probContents.clone());
		else out.SetKnownContents(Contents);
		if (probabillities!=null) {
			out.setProbabillities(probabillities.clone());
		}
		return out;
	}
	public int getQuadrant() {
		return Contents/4;
	}
	public boolean isKnown() {
		return Contents>=12||Contents<0;
	}
}
