package tsa;
/**
 * 
 * @author Michal + Gunnur
 *	
 */
class DPoint {
	int[] facount;
	Boolean en = false;
	/**
	 * <p>
	 * Constructor for the DataPoint class.
	 * This excepts data about the time moment. 
	 * @param fac
	 */
	public DPoint(int[] fac) {
		facount = fac;

	}
	
	/**
	 * <p>
	 * End data point constructor that will tell the graph when to mark a section as the end
	 * of a round.
	 * @param end
	 */
	public DPoint(Boolean end){
		en = end;
		facount = new int[9];
	}
	
	/**
	 * <p>
	 * getter for the values.
	 * @return facount
	 */
	public int[] getmarks() {
		
		return facount;
	}
	
}