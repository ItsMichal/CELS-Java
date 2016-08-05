package tsa;

import java.util.ArrayList;

import processing.core.PVector;
import sun.nio.cs.ext.Big5;

/**
 * 
 * @author Gunnar & Michal
 *
 */
class Cell {

	private final Simulation tsa;
	int re = 20;
	boolean dead = false;
	int rod = 60;
	Faction faction;
	PVector location;
	PVector velocity;
	PVector acceleration;
	float r;
	float maxforce; // Maximum steering force
	float maxspeed; // Maximum speed
	PVector colord = new PVector(0, 0, 0);
	// eating system
	// Boolean eatingb = false;
	int am = 20;
	// Food targets;
	String tag;
	int x = 0;
	int y = 0;
	int index = 0;
	int oc = 0;
	int maxdist = 50;
	Cell mother = null;
	float turningnum = 0.8f;

	/**
	 * <p>
	 * cell constructor giving it a cell to copy its value off of.
	 * 
	 * @param tsa
	 * @param x
	 * @param y
	 * @param faction
	 * @param ama
	 * @param copy
	 */
	Cell(Simulation tsa, float x, float y, Faction faction, int ama, Cell copy) {
		this.tsa = tsa;
		// Sets faction then joins it
		this.faction = faction;
		this.faction.addMember(this);
		this.am = ama;
		acceleration = new PVector(0, 0);
		// This is a new PVector method not yet implemented in JS
		// velocity = PVector.random2D();

		// Leaving the code temporarily this way so that this example runs
		// in JS
		float angle = this.tsa.random(Simulation.TWO_PI);
		velocity = new PVector(Simulation.cos(angle), Simulation.sin(angle));

		location = new PVector(x, y);
		r = 2.0f;
		maxspeed = 3.3f;
		maxforce = 1.03f;
		maxspeed = copy.maxspeed;
		this.re = copy.re;
		this.rod = copy.rod;
		this.maxdist = copy.maxdist;
		this.mother = copy;

	}

	/**
	 * <p>
	 * Cell Constructor that will set the values to parameters given for object
	 * Mutation.
	 * 
	 * @param tsa
	 * @param x
	 * @param y
	 * @param faction
	 * @param speed
	 * @param re
	 * @param rod
	 * @param dis
	 */
	Cell(Simulation tsa, float x, float y, Faction faction, float speed, int re, int rod, int dis) {
		this.tsa = tsa;
		this.rod = rod;
		// Sets faction then joins it
		this.faction = faction;
		this.faction.addMember(this);
		acceleration = new PVector(0, 0);

		// This is a new PVector method not yet implemented in JS
		// velocity = PVector.random2D();

		// Leaving the code temporarily this way so that this example runs
		// in JS
		float angle = this.tsa.random(Simulation.TWO_PI);
		velocity = new PVector(Simulation.cos(angle), Simulation.sin(angle));

		location = new PVector(x, y);
		r = 2.0f;
		// add genetics for a cell
		maxspeed = speed;
		this.re = re;
		this.maxdist = dis;

	}

	/**
	 * <p>
	 * Cell constructor that will accept an int array instead of parameters for
	 * Mutation.
	 * 
	 * @param tsa
	 * @param x
	 * @param y
	 * @param faction
	 * @param s
	 */
	Cell(Simulation tsa, float x, float y, Faction faction, int[] s) {
		this.tsa = tsa;
		// Sets faction then joins it
		this.faction = faction;
		this.faction.addMember(this);
		acceleration = new PVector(0, 0);

		float angle = this.tsa.random(Simulation.TWO_PI);
		velocity = new PVector(Simulation.cos(angle), Simulation.sin(angle));

		location = new PVector(x, y);
		r = 2.0f;
		// add genetics for a cell
		maxspeed = s[0];
		// System.out.println(faction.name + ": " + this.getspeed());
		this.re = s[1];
		this.rod = s[2];
		this.maxdist = (int) tsa.map(s[3], 0, 200, 0, 100);
		this.turningnum = (float) (s[3] / 10.0);

	}

	/**
	 * @return replication level
	 */
	public int getRe() {
		return this.re;
	}

	/**
	 * <p>
	 * connects all of the pieces to run Boids.
	 * 
	 * @param foods
	 */
	public void run(ArrayList<Food> foods) {
		flock(foods);

		update();

		borders();
		render();

	}

	/**
	 * <p>
	 * add a PVector to a different PVector.
	 * 
	 * @param force
	 */
	public void applyForce(PVector force) {
		// We could add mass here if we want A = F / M
		force.mult(turningnum);
		acceleration.add(force);
	}

	/**
	 * <p>
	 * Generates force values for the three rules of Boids.
	 * 
	 * @param foods
	 */
	public void flock(ArrayList<Food> foods) {
		PVector sep = separate(foods); // Separation
		PVector ali = align(foods); // Alignment
		PVector coh = cohesion(foods); // Cohesion
		// Arbitrarily weight these forces
		sep.mult(1.5f);
		ali.mult(1.0f);
		coh.mult(1.0f);
		// Add the force vectors to acceleration

		applyForce(sep);
		applyForce(ali);
		applyForce(coh);

	}

	/**
	 * <p>
	 * updates the values of the class and factions.
	 */
	public void update() {
		if (this.am > this.faction.big.am + 8) {
			this.faction.big = this;
		}
		// System.out.println(am);
		if (this.tsa.millis() % 60 == 0 && am > 0 && this.tsa.millis() != oc) {
			am--;
			oc = this.tsa.millis();

		}
		// Update velocity
		velocity.add(acceleration);
		// Limit speed
		velocity.limit(maxspeed);
		location.add(velocity);
		// Reset accelertion to 0 each cycle
		acceleration.mult(0);
	}

	/**
	 * <p>
	 * A method that calculates and applies a steering force towards a specific
	 * target.
	 *
	 * @parm target
	 */

	public PVector seek(PVector target) {
		PVector desired = PVector.sub(target, location); // A vector
															// pointing from
		// System.out.println(PVector.dist(desired, location)); // the location
		// to the target
		// Scale to maximum speed
		desired.normalize();
		desired.mult(maxspeed);
		// System.out.println(this.faction.name + "'s Speed: " + maxspeed);

		// Steering = Desired minus Velocity
		PVector steer = PVector.sub(desired, velocity);
		steer.limit(0.03f); // Limit to maximum steering force
		return steer;
	}

	/**
	 * <p>
	 * generate the object to be put on the canvas.
	 */
	public void render() {

		this.tsa.pushMatrix();
		this.tsa.scale(this.tsa.zooms);
		this.tsa.translate(this.tsa.tranX, this.tsa.tranY);
		this.tsa.stroke(this.tsa.color(0, 25));
		if (this.tsa.winner != null && this.tsa.winner.fid == faction.fid) {
			if (mother != null && !mother.dead) {
				//this.tsa.strokeWeight(100);
				this.tsa.line(location.x, location.y, mother.location.x, mother.location.y);
				// this.tsa.lerp(location.y, mother.location.y, (float)
				// (this.tsa.millis()%1000f)/1000f);
				this.tsa.fill(this.tsa.color(255 - faction.RGB[0], 255 - faction.RGB[1], 255 - faction.RGB[2]));
				this.tsa.stroke(0);
				//this.tsa.strokeWeight(100);
//				this.tsa.ellipse(
//						this.tsa.lerp(location.x, mother.location.x, (float) (this.tsa.millis() % 2000f) / 2000f),
//						this.tsa.lerp(location.y, mother.location.y, (float) (this.tsa.millis() % 3000f) / 3000f),
//						am / 4, am / 4);
//				this.tsa.ellipse(
//						this.tsa.lerp(location.x, mother.location.x, (float) (this.tsa.millis() % 2000f) / 2000f),
//						this.tsa.lerp(location.y, mother.location.y, (float) (this.tsa.millis() % 5000f) / 5000f),
//						am / 4, am / 4);
//				this.tsa.ellipse(
//						this.tsa.lerp(location.x, mother.location.x, (float) (this.tsa.millis() % 2000f) / 2000f),
//						this.tsa.lerp(location.y, mother.location.y, (float) (this.tsa.millis() % 2000f) / 2000f),
//						am / 4, am / 4);

			} else {
				this.tsa.line(location.x, location.y, faction.big.location.x,
						faction.big.location.y);
				this.tsa.fill(this.tsa.color(255 - faction.RGB[0], 255 - faction.RGB[1], 255 - faction.RGB[2]));
				this.tsa.stroke(0);
				/*
				 * this.tsa.ellipse(this.tsa.lerp(location.x,
				 * faction.members.get(0).location.x, (float)
				 * (this.tsa.millis()%2000f)/2000f),this.tsa.lerp(location.y,
				 * faction.members.get(0).location.y, (float)
				 * (this.tsa.millis()%3000f)/3000f) ,am/4, am/4);
				 * this.tsa.ellipse(this.tsa.lerp(location.x,
				 * faction.members.get(0).location.x, (float)
				 * (this.tsa.millis()%2000f)/2000f),this.tsa.lerp(location.y,
				 * faction.members.get(0).location.y, (float)
				 * (this.tsa.millis()%5000f)/5000f) ,am/4, am/4);
				 * this.tsa.ellipse(this.tsa.lerp(location.x,
				 * faction.members.get(0).location.x, (float)
				 * (this.tsa.millis()%2000f)/2000f),this.tsa.lerp(location.y,
				 * faction.members.get(0).location.y, (float)
				 * (this.tsa.millis()%2000f)/2000f) ,am/4, am/4);
				 */
			}
		}

		this.tsa.noStroke();
		this.tsa.popMatrix();
		float theta = velocity.heading() + Simulation.radians(90);
		this.tsa.pushMatrix();
		this.tsa.scale(this.tsa.zooms);
		this.tsa.translate(location.x + this.tsa.tranX, location.y + this.tsa.tranY);
		this.tsa.rotate(theta);
		if (this.equals(this.faction.big)) {
			this.tsa.fill(this.tsa.color(100, 100));
			this.tsa.ellipse(0, 0, am * 1.25f + 5, am * 1.5f + 7);
			this.tsa.fill(this.tsa.color(255 - this.faction.RGB[0], 255 - this.faction.RGB[1],
					255 - this.faction.RGB[2], 100));
			this.tsa.ellipse(0, 0, am * 1.25f + 3, am * 1.5f + 7);

		} else {
			this.tsa.fill(100);
			this.tsa.ellipse(0, 0, am + 3, am * 1.25f + 3);
		}
		this.tsa.ellipse(0, 0, am + 3, am * 1.25f + 3);

		// ellipse(0, 0, 20 +
		// (am) +
		// 5, 20 + (am * 1.5f) + 5);
		// this.tsa.textSize(25);
		this.tsa.popMatrix();
		// Draw a triangle rotated in the direction of velocity

		// heading2D() above is now heading() but leaving old syntax until
		// Processing.js catches up
		this.tsa.pushMatrix();
		this.tsa.scale(this.tsa.zooms);
		this.tsa.translate(location.x + this.tsa.tranX, location.y + this.tsa.tranY);
		this.tsa.rotate(theta);
		// this.tsa.stroke(255);

		// System.out.println(this.faction.RGB);
		this.tsa.fill(this.tsa.color(faction.RGB[0], faction.RGB[1],
				faction.RGB[2])); /* color(am, colord.y, am) */
		this.tsa.noStroke();
		// strokeWeight(1);

		this.tsa.ellipse(0, 0, am, am * 1.25f);// ellipse(0, 0, 20 + (am * 2),
												// 20 +

		// (am * 3));

		this.tsa.popMatrix();
		this.tsa.pushMatrix();
		this.tsa.scale(this.tsa.zooms);
		this.tsa.translate(location.x + this.tsa.tranX, location.y + this.tsa.tranY);
		this.tsa.fill(this.tsa.color(0));// (255-faction.RGB[0],
											// 255-faction.RGB[1],
											// 255-faction.RGB[2]));

		if (faction.big.equals(this)) {
			this.tsa.textSize((float) (am / 2));
			this.tsa.text(faction.name + " Leader", am, am);
		} else if (faction.members.get(0).equals(this)) {
			this.tsa.textSize((float) (am / 1.5));
			this.tsa.text(faction.name + " Elder", am, am);
		} else if (this.tag != null) {
			this.tsa.textSize(am);
			this.tsa.text(tag, 0, 0);
		} else {
			this.tsa.textSize(am / 3);
			this.tsa.text(faction.name, am, am);
		}
		this.tsa.popMatrix();

	}

	/**
	 * <p>
	 * Sets up walls that keeps the Cell from leaving the Canvas.
	 */
	public void borders() {

		if (location.x < 0) {
			velocity.x = -velocity.x;
			location.x = 1;
		}
		// location.x = width;
		if (location.y < 0) {
			velocity.y = -velocity.y;
			location.y = 1;
		}
		if (location.x > this.tsa.width) {
			velocity.x = -velocity.x;
			location.x = this.tsa.width - 1;
		}
		// location.x = 0;
		if (location.y > this.tsa.height) {
			velocity.y = -velocity.y;
			location.y = this.tsa.height - 1;
		}
	}

	/**
	 * <p>
	 * Looks at the boids around the Cell and stears away.
	 * 
	 * @param foods
	 * @return calc separation
	 */
	public PVector separate(ArrayList<Food> foods) {
		float desiredseparation = 25.0f;
		PVector steer = new PVector(0, 0, 0);
		int count = 0;
		for (Cell other : this.faction.members) {
			float d = PVector.dist(location, other.location);
			// If the distance is greater than 0 and less than an
			// arbitrary
			// amount (0 when you are yourself)
			if ((d > 0) && (d < desiredseparation)) {

				PVector diff = PVector.sub(location, other.location);
				diff.normalize();
				diff.div(d);
				steer.add(diff);
				count++;
			}
		}

		// Average -- divide by how many
		if (count > 0) {
			steer.div((float) count);
		}

		// As long as the vector is greater than 0
		if (steer.mag() > 0) {
			// First two lines of code below could be condensed with new
			// PVector setMag() method
			// Not using this method until Processing.js catches up
			// steer.setMag(maxspeed);

			// Implement Reynolds: Steering = Desired - Velocity
			steer.normalize();
			steer.mult(maxspeed);
			steer.sub(velocity);
			steer.limit(maxforce);
		}
		return steer;
	}

	/**
	 * <p>
	 * For every nearby boid, calculate the average velocity
	 * 
	 * @param foods
	 * @return calculated PVector
	 */

	public PVector align(ArrayList<Food> foods) {
		float neighbordist = 50;
		PVector sum = new PVector(0, 0);
		int count = 0;
		for (Cell other : this.faction.members) {
			float d = PVector.dist(location, other.location);
			if ((d > 0) && (d < neighbordist)) {
				sum.add(other.velocity);
				count++;
			}
		}
		for (int enid : this.faction.getEnemy()) {
			if (enid != 0) {
				for (Cell other : this.tsa.factions[enid - 1].members) {
					float d = PVector.dist(location, other.location);
					if ((d > 0) && (d < neighbordist) && (this.am > other.am)) {
						sum.add(other.velocity);
						count++;
					}
				}
			}
		}

		if (count > 0) {
			sum.div((float) count);
			// First two lines of code below could be condensed with new
			// PVector setMag() method
			// Not using this method until Processing.js catches up
			// sum.setMag(maxspeed);

			// Implement Reynolds: Steering = Desired - Velocity
			sum.normalize();
			sum.mult(maxspeed);
			PVector steer = PVector.sub(sum, velocity);
			steer.limit(maxforce);
			return steer;
		} else {
			return new PVector(0, 0);
		}
	}

	/**
	 * <p>
	 * For the average location (i.e. center) of all nearby boids, calculate
	 * steering vector towards that location
	 * 
	 * @param foods
	 * @return calculated Steering
	 */

	public PVector cohesion(ArrayList<Food> foods) {

		PVector sum = new PVector(0, 0); // Start with empty vector to
											// accumulate all locations
		int count = 0;

		for (Food other : foods) {
			float d = PVector.dist(location, other.location);
			// this.tsa.line(location.x, location.y, other.location.x,
			// other.location.y);
			if ((d > 0) && (d < maxdist)) {
				sum.add(other.location); // Add location
				count++;

			}
		}

		for (int enid : this.faction.getEnemy()) {
			if (enid != 0) {
				for (Cell other : this.tsa.factions[enid - 1].members) {

					float d = PVector.dist(location, other.location);

					if ((d > 0) && (d < maxdist) && (other.am < this.am)) {
						sum.add(other.location);
						count++;
					}
				}
			}
		}

		if (count > 0) {
			sum.div(count);
			// this.tsa.stroke(this.tsa.color(colord.x, colord.y, colord.z));

			return seek(sum); // Steer towards the location
		} else {
			return new PVector(0, 0);
		}
	}

	/**
	 * @return Max speed of Boid
	 */
	public float getspeed() {
		return maxspeed;
	}

	public boolean ifOver(float mX, float mY) {
		float h = (location.x + this.tsa.tranX) * this.tsa.zooms;
		float k = (location.y + this.tsa.tranY) * this.tsa.zooms;
		float rx = (float) ((am) / 2) * this.tsa.zooms;
		float ry = (float) ((am) / 2) * this.tsa.zooms;
		float theta = velocity.heading() + Simulation.radians(90);
		/*
		 * this.tsa.pushMatrix(); this.tsa.fill(100,200,0);
		 * this.tsa.stroke(200,100,0); //this.tsa.rotate(theta);
		 * this.tsa.ellipse(h, k, rx*2,ry*2); this.tsa.noStroke();
		 * this.tsa.popMatrix();
		 */
		/*
		 * a = theta
		 */

		// System.out.println(
		// (((Math.pow(((mX - h) * this.tsa.cos(theta) + (mY - k) *
		// this.tsa.sin(theta)), 2)) / Math.pow(ry, 2))
		// + ((Math.pow(((mX - h) * this.tsa.cos(theta) + (mY - k) *
		// this.tsa.sin(theta)), 2))
		// / Math.pow(rx, 2))) <= 1);

		// COMPLEX: BROKEN???
		// return (((Math.pow(( ( mX-h )* this.tsa.cos(theta) +( mY - k
		// )*this.tsa.sin(theta) ), 2))/Math.pow(ry, 2))+((Math.pow(( ( mX-h
		// )*this.tsa.cos(theta)+( mY - k )*this.tsa.sin(theta) ),
		// 2))/Math.pow(rx, 2))) <= 1;

		// SIMPLIFIED
		return (((Math.pow(mX - h, 2)) / Math.pow(rx, 2)) + ((Math.pow(mY - k, 2)) / Math.pow(ry, 2))) <= 1;
		// if()
		// return true;
	}

	public void setTracking() {
		this.tsa.tracking = this;

	}

}