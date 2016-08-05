package tsa;

import java.util.ArrayList;

import processing.core.PVector;

/**
 * 
 * @author Gunnar
 *
 */
class Flock {
	
	private  Simulation tsa;
	public ArrayList<Food> h;
	public ArrayList<Cell> killer;
	int high = 0;
	// An ArrayList for all the boids
	PVector g = new PVector(0, 0, 0);
	
	/**
	 * <p>
	 * Constructor that will link the Simulation object to the flock 
	 * along with a gobal color.  
	 * @param tsa
	 * @param colors
	 */
	Flock(Simulation tsa, PVector colors) {
		this.tsa = tsa;
		h = new ArrayList<Food>();
		
		killer = new ArrayList<Cell>();
		// Initialize the ArrayList
		g = colors;
	}

	/**
	 * @return amount of food objects
	 */
	public int getAmBac() {
		return h.size();
	}

	/**
	 * 
	 * @return amount of Cells.
	 */
	public int getAmKiller() {
		return killer.size();
	}
	
	
	
	
	/**
	 * <p>
	 * This method will run every Object. It will tell
	 * each one to process it step for that play. 
	 */
	public void run() {

		for (Food b : h) {
			b.run(h);
			b.lifetime++;

		}
		
		for (Cell k : killer) {
			k.run(h);

		}

	}

	
	/**
	 * <p>
	 * Mutator adding a new food object to the simulation.  
	 * @param b
	 */
	public void addBoid(Food b) {
		h.add(b);
		b.colord = g;
	}
	
	/**
	 * <p>
	 * Mutator that will add more Hostile cells to the simulation.
	 * @param b
	 */
	public void addbea(Cell b) {
		killer.add(b);
	}
	
	/**
	 * <p>
	 * 
	 * See if any objects are in the edible range of an object.
	 * If an object is then it will remove that object and reward the 
	 * other object points.
	 */
	public void eat() {
		for (int f = 0; f < killer.size(); f++) {
			Cell j = killer.get(f);
			for (int i = 0; i < killer.size(); i++) {
				Cell otherhc = killer.get(i);
				if (j.faction.isEnemyFromFID(otherhc.faction.getFid())) {
					if (j.am > otherhc.am) {
						float k = PVector.dist(j.location, otherhc.location);
						if (k <= j.am) {
							j.am += otherhc.am;
							reproduce();
							if(j.am > j.faction.big.am + 8){
								j.faction.big = j;
							}
							Cell xt= killer.get(i);
							xt.dead = true;
							xt.faction.members.remove(xt);
							killer.remove(i);
					  	}
					}
				}
			}
			//BUG HERE
			for (int s = 0; s < h.size(); s++) {
				float k = PVector.dist(j.location, h.get(s).location);
				if (k <= j.am - 3) {
					h.remove(s);
					j.am+=2;
					if(j.am > j.faction.big.am + 8){
						j.faction.big = j;
					}
				}

			}

		}

	}

	/**
	 * <p>
	 * This method will calculate if a cell can reproduce or not based 
	 * on its food level. Some objects have different restrictions then others
	 * objects.
	 */
	public void reproduce() {
		for (int k = 0; k < killer.size(); k++) {
			Cell j = killer.get(k);
			// welcome to the space jam
			
				if (j.am > j.getRe()) {
					// System.out.println((j.am - (j.am%10))/10);
						Cell favoritechild = new Cell(this.tsa, j.location.x + (float)(((Math.random()/4)-0.125) * (j.am/2 + 3)), j.location.y + (float)(((Math.random()/4)-0.125)*(j.am * 1.25f + 3)), j.faction, j.am/2, (j.mother != null) ? j.mother : j);
						if(this.tsa.tracking == j){
							addbea(favoritechild);
							favoritechild.setTracking();
						}else{
							addbea(favoritechild);
						}
						//RIP not favorite child
						addbea(new Cell(this.tsa, j.location.x + (float)(((Math.random()/4)-0.125) * (j.am/2 + 3)), j.location.y + (float)(((Math.random()/4)-0.125)*(j.am * 1.25f + 3)), j.faction, j.am/2, favoritechild));
						j.am = 0;
						
				}
				//j.repcool = 2;
			
			if (j.am == 0) {
				j.dead = true;
				j.faction.members.remove(j);
				killer.remove(j);
			}
			//j.repcool--;
		}
		for (int y = 0; y < 3; y++) {
			int select = Simulation.round(this.tsa.random(h.size() - 1));
			if (h.size() != 0) {
				if ((int) this.tsa.random(h.get(select).ratio) == 1 && this.h.size() < 300)
					addBoid(new Food(this.tsa, h.get(select).location.x, h.get(select).location.y,
							h.get(select).ratio + (int) this.tsa.random(-5, 5)));
			}
		}

	}
	

}