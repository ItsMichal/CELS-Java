package tsa;

import java.util.ArrayList;

/**
 * @author Michal B. 
 *
 */
class Faction {
	// Some Parameters
	String name;
	int fid;
	float[] RGB = { 0, 0, 0 };
	int[] enemyfid;
	ArrayList<Cell> members = new ArrayList<Cell>();
	Cell big = null;
	

	/**
	 * <p>
	 * default constructor that will set the base faction.
	 */
	public Faction() {
		fid = -1;
		enemyfid = new int[1];
		name = "Cell Group #" + fid;
		
	}

	
    public int advSpe(){
    	int sum = 0;
    	for(Cell k:members){
    		sum += k.getspeed();
    	}
    	sum /= members.size();
    return sum;	
    }
    
    public int advRe(){
    	int sum = 0;
    	for(Cell k:members){
    		sum += k.getRe();
    	}
    	sum /= members.size();
    return sum;	
    }
    
	/**
	 * <p>
	 * Constructor that will create a new factions but give it an ID.  
	 * @param id
	 */
	public Faction(int id) {
		if (id != 0) {
			fid = id;
		} else {
			fid = -1;
			System.out.println("ID CANNOT BE ZERO, SET TO -1");
		}

		enemyfid = new int[1];
		name = "Cell Group #" + fid;
		
	}

	/**
	 * <p>
	 * Constructor that will give a faction ID and Color. 
	 * @param id
	 * @param color
	 */
	public Faction(int id, float color) {
		if (id != 0) {
			fid = id;
		} else {
			fid = -1;
			System.out.println("ID CANNOT BE ZERO, SET TO -1");
		}
		enemyfid = new int[1];
		name = "Cell Group #" + fid;
		
	}

	/**
	 * <p>
	 * 
	 * This constructor will create a faction that will
	 * have a custom ID, Color, and enemy list.
	 * @param id
	 * @param color
	 * @param enems
	 */
	public Faction(int id, float[] color, int[] enems) {
		if (id != 0) {
			fid = id;
		} else {
			fid = -1;
			System.out.println("ID CANNOT BE ZERO, SET TO -1");
		}

		RGB = color.clone();

		boolean temp = true;
		for (int i : enems)
			if (i == fid)
				temp = false;
		if (temp) {
			enemyfid = enems.clone();
		} else {
			enemyfid = new int[1];
			System.out.println("YOU CAN NOT BE YOUR OWN ENEMY.");
		}
		name = "Cell Group #" + fid;
	}

	/**
	 * <p>
	 * Constructor that will allow the custumization of 
	 * ID,color,enemy, and name.
	 *  
	 * @param id
	 * @param color
	 * @param enems
	 * @param name
	 */
	public Faction(int id, float[] color, int[] enems, String name) {
		if (id != 0) {
			fid = id;
		} else {
			fid = -1;
			System.out.println("ID CANNOT BE ZERO, SET TO -1");
		}

		RGB = color.clone();

		boolean temp = true;
		for (int i : enems)
			if (i == fid)
				temp = false;
		if (temp) {
			enemyfid = enems.clone();
		} else {
			enemyfid = new int[1];
			System.out.println("YOU CAN NOT BE YOUR OWN ENEMY.");
		}
		this.name = name;
	}
	
	/**
	 * <p>
	 * generate new member and sets the correct elder cell in the simulation to the 
	 * biggest cell.
	 * @param x
	 */
	public void addMember(Cell x) {
		if(big == null || x.am > big.am){
			big = x;
		}
		members.add(x);
	}
	
	/**
	 * <p>
	 * searches for a specific object to remove form the factions 
	 * array.
	 * @param x
	 */
	public void removeMember(Cell x) {
		if (members.indexOf(x) > -1) {
			members.remove(members.indexOf(x));
		} else {
			System.out.println("COULD NOT REMOVE. SORRY.");
		}

	}
	
	/**
	 * <p>
	 * Allows the factions to identify if a faction
	 * ID shows up in the enemy list.
	 * 
	 * @param id
	 * @return if ID is in enemys. 
	 */
	public boolean isEnemyFromFID(int id) {
		for (int id2 : enemyfid) {
			if (id2 == id) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @return id of Faction.
	 */
	public int getFid() {
		return fid;
	}
	
	/**
	 * <p>
	 * sets the ID for the object.
	 * 
	 * @param fid
	 */
	public void setFID(int fid) {
		this.fid = fid;
	}
	
	/**
	 * 
	 * @return Faction color in RGB array.
	 */
	public float[] getFactionColor() {
		return RGB;
	}
	
	/**
	 * <p>
	 * 
	 * sets the Faction colors using RGB values.
	 * 
	 * @param R
	 * @param G
	 * @param B
	 */
	public void setFactionColor(float R, float G, float B) {
		this.RGB[0] = R;
		this.RGB[1] = G;
		this.RGB[2] = B;
	}

	/**
	 * @return enemys ID list
	 */
	public int[] getEnemy() {
		return enemyfid;
	}
	
	/**
	 * <p>
	 * set enemys ID list
	 *  
	 * @param enemyfid
	 */
	public void setEnemy(int[] enemyfid) {
		this.enemyfid = enemyfid;
	}
	
	
	/**
	 * 
	 * @return faction size.
	 */
	public int getamount(){
		return members.size();
	}
	}

