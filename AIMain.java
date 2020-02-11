
interface AIMain {
	
	/**
	 * Method nextMove
	 *
	 *
	 * @param map
	 *
	 * @return
	 *
	 */
	public Movement nextMove(ProbabilityMap map);
	public void ChangeProbs(ProbabilityMap map, Movement move);
}
