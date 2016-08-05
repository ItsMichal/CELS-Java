package tsa;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

class FoodShow {
	/**
	 * 
	 */
	private PApplet tsa;
	int lifetime = 0;
	PVector location;
	PVector velocity;
	PVector acceleration;
	float r;
	float maxforce; // Maximum steering force
	float maxspeed; // Maximum speed
	PVector colord = new PVector(0, 0, 0);
	float n = 0;
	int ratio;

	FoodShow(PApplet tsa, float x, float y, int ratio) {
		this.tsa = tsa;
		acceleration = new PVector(0, 0);

		
		float angle = this.tsa.random(Simulation.TWO_PI);
		velocity = new PVector(Simulation.cos(angle), Simulation.sin(angle));

		location = new PVector(x, y);
		r = 2.0f;
		maxspeed = 0.8f;
		maxforce = 0.03f;
		this.ratio = ratio;
		c1 = this.tsa.color(255,50,50);
		c2 = this.tsa.color(50,255,50);
		c3 = this.tsa.color (50,50,255);
	}

	public void run(ArrayList<FoodShow> h) {
		flock(h);
		update();
		borders();
		render();
	}

	public void applyForce(PVector force) {
		// We could add mass here if we want A = F / M
		acceleration.add(force);
	}

	// We accumulate a new acceleration each time based on three rules
	public void flock(ArrayList<FoodShow> h) {
		PVector sep = separate(h); // Separation
		PVector ali = align(h); // Alignment
		PVector coh = cohesion(h); // Cohesion
		// Arbitrarily weight these forces
		sep.mult(1.5f);
		ali.mult(1.0f);
		coh.mult(1.0f);
		// Add the force vectors to acceleration
		applyForce(sep);
		applyForce(ali);
		applyForce(coh);
	}

	// Method to update location
	public void update() {
		// Update velocity
		velocity.add(acceleration);
		// Limit speed
		velocity.limit(maxspeed);
		location.add(velocity);
		// Reset accelertion to 0 each cycle
		acceleration.mult(0);
	}

	// A method that calculates and applies a steering force towards a
	// target
	// STEER = DESIRED MINUS VELOCITY
	public PVector seek(PVector target) {
		PVector desired = PVector.sub(target, location); // A vector
															// pointing from
															// the location
															// to the target
		// Scale to maximum speed
		desired.normalize();
		desired.mult(maxspeed);

		// Above two lines of code below could be condensed with new PVector
		// setMag() method
		
		// Steering = Desired minus Velocity
		PVector steer = PVector.sub(desired, velocity);
		steer.limit(maxforce); // Limit to maximum steering force
		return steer;
	}
	
	private int cColor = 0;
	private int c1 = 0, c2 = 0,c3 = 0;
	public void render() {
		
		// Draw a triangle rotated in the direction of velocity
		// drawGridLines(100, false);
		float theta = velocity.heading() + Simulation.radians(90);

		

		this.tsa.noStroke();
		this.tsa.fill(cColor);
		this.tsa.pushMatrix();
		this.tsa.translate(location.x,location.y);
		this.tsa.rotate(theta);
		if (n < 1) {
			cColor = this.tsa.lerpColor(c1, c2, n);
		} else if (n < 2) {
			cColor = this.tsa.lerpColor(c2, c3, n - 1);
		} else if (n < 3) {
			cColor = this.tsa.lerpColor(c3, c1, n - 2);
		} else{
			n = 0;
		}
		n+=0.001;
		//System.out.println("IMA KILL YOU" + this.tsa.n);
		// this.tsa.fill(this.tsa.color(this.tsa.lerpColor(c1, c2, amt)));
	   
		this.tsa.pushMatrix();
		// this.tsa.translate(location.x,location.y);
		//this.tsa.scale(this.tsa.zooms);
		this.tsa.stroke(0);
		this.tsa.triangle(3, 0, 6, 8, 0, 8);
		//this.tsa.rect(0, 0, 5,10);
		this.tsa.noStroke();
		this.tsa.popMatrix();
		this.tsa.popMatrix();

	}

	// Wraparound
	public void borders() {
		if (location.x < 0)
			location.x = this.tsa.width;
		if (location.y < 0)
			location.y = this.tsa.height;
		if (location.x > this.tsa.width)
			location.x = 0;
		if (location.y > this.tsa.height)
			location.y = 0;

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

	// Separation
	// Method checks for nearby boids and steers away
	public PVector separate(ArrayList<FoodShow> h) {
		float desiredseparation = 25.0f;
		PVector steer = new PVector(0, 0, 0);
		int count = 0;
		// For every boid in the system, check if it's too close
		for (FoodShow other : h) {
			float d = PVector.dist(location, other.location);
			// If the distance is greater than 0 and less than an arbitrary
			// amount (0 when you are yourself)
			if ((d > 0) && (d < desiredseparation)) {
				// Calculate vector pointing away from neighbor
				PVector diff = PVector.sub(location, other.location);
				diff.normalize();
				diff.div(d); // Weight by distance
				steer.add(diff);
				count++; // Keep track of how many
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

	// Alignment
	// For every nearby boid in the system, calculate the average velocity
	public PVector align(ArrayList<FoodShow> h) {
		float neighbordist = 50;
		PVector sum = new PVector(0, 0);
		int count = 0;
		for (FoodShow other : h) {
			float d = PVector.dist(location, other.location);
			if ((d > 0) && (d < neighbordist)) {
				sum.add(other.velocity);
				count++;
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

	// Cohesion
	// For the average location (i.e. center) of all nearby boids, calculate
	// steering vector towards that location
	public PVector cohesion(ArrayList<FoodShow> h) {
		float neighbordist = 50;
		PVector sum = new PVector(0, 0); // Start with empty vector to
											// accumulate all locations
		int count = 0;

		for (FoodShow other : h) {
			float d = PVector.dist(location, other.location);
			this.tsa.line(location.x, location.y, other.location.x, other.location.y);
			if ((d > 0) && (d < neighbordist)) {
				sum.add(other.location); // Add location
				count++;
			}
		}
		if (count > 0) {
			sum.div(count);
			this.tsa.stroke(this.tsa.color(colord.x, colord.y, colord.z));

			return seek(sum); // Steer towards the location
		} else {
			return new PVector(0, 0);
		}
	}
}