package tsa;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * 
 * @author Michal
 *
 */
class FlockShow {
	
	private PApplet tsa;
	public ArrayList<FoodShow> h;
	public ArrayList<CellShow> killer;
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
	FlockShow(PApplet tsa, PVector colors) {
		this.tsa = tsa;
		h = new ArrayList<FoodShow>();
		
		killer = new ArrayList<CellShow>();
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
	 * @return amount of CellShows.
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

		for (FoodShow b : h) {
			b.run(h);
			b.lifetime++;
			
		}
		//for(int i = 0; i < h..size(); i++)
		
		for (CellShow k : killer) {
			k.run(h);

		}

	}

	
	/**
	 * <p>
	 * Mutator adding a new food object to the simulation.  
	 * @param foodShow
	 */
	public void addBoid(FoodShow foodShow) {
		h.add(foodShow);
		foodShow.colord = g;
	}
	
	/**
	 * <p>
	 * Mutator that will add more Hostile CellShows to the simulation.
	 * @param b
	 */
	public void addbea(CellShow b) {
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
			CellShow j = killer.get(f);
			
			//BUG HERE
			for (int s = 0; s < h.size(); s++) {
				float k = PVector.dist(j.location, h.get(s).location);
				if (k <= j.am - 3) {
					h.remove(s);
					j.am+=2;
					
				}

			}

		}

	}

	/**
	 * <p>
	 * This method will calculate if a CellShow can reproduce or not based 
	 * on its food level. Some objects have different restrictions then others
	 * objects.
	 */
	public void reproduce() {
		for (int k = 0; k < killer.size(); k++) {
			CellShow j = killer.get(k);
			// welcome to the space jam
			
				if (j.am > 50) {
					// System.out.println((j.am - (j.am%10))/10);
						CellShow favoritechild = new CellShow(this.tsa, j.location.x + (float)(((Math.random()/4)-0.125) * (j.am/2 + 3)), j.location.y + (float)(((Math.random()/4)-0.125)*(j.am * 1.25f + 3)));
						
						addbea(favoritechild);
						
						//RIP not favorite child
						addbea(new CellShow(this.tsa, j.location.x + (float)(((Math.random()/4)-0.125) * (j.am/2 + 3)), j.location.y + (float)(((Math.random()/4)-0.125)*(j.am * 1.25f + 3))));
						j.am = 0;
						
				}
				//j.repcool = 2;
			
			if (j.am == 0) {
				
				
				killer.remove(j);
			}
			//j.repcool--;
		}
		for (int y = 0; y < 3; y++) {
			int select = Simulation.round(this.tsa.random(h.size() - 1));
			if (h.size() != 0) {
				if ((int) this.tsa.random(h.get(select).ratio) == 1 && this.h.size() < 300)
					addBoid(new FoodShow(this.tsa, h.get(select).location.x, h.get(select).location.y,
							h.get(select).ratio + (int) this.tsa.random(-5, 5)));
			}
		}

	}
	

}