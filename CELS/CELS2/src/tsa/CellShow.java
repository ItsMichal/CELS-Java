package tsa;
import java.util.ArrayList;

import processing.core.*;



public class CellShow{
		PApplet sm;
		int lifetime = 500;
		/*
		 * The bellow PVectors act like the point class, they contain importent
		 * information about the Hostile object.
		 */
		Faction faction;
		PVector location;
		PVector velocity;
		PVector acceleration;
		float r;
		float maxforce; // Maximum steering force
		float maxspeed; // Maximum speed
		PVector colord = new PVector(0, 0, 0);
		// eating system
		Boolean eatingb = false;
		int am = 20;
		Cell targets;
		int x = 0;
		int y = 0;
		int index = 0;
		int oc = 0;
		float n = 0;
		
		public CellShow(PApplet sim, float x, float y) {
			// Sets faction then joins it
			sm = sim;
			acceleration = new PVector(0, 0);

			// velocity = PVector.random2D();

			
			float angle = sm.random(sim.TWO_PI);
			velocity = new PVector(sm.cos(angle), sm.sin(angle));

			location = new PVector(x, y);
			r = 2.0f;
			maxspeed = 3.3f;
			maxforce = 1.03f;

			c1 = sm.color(255,50,50);
			c2 = sm.color(50,255,50);
			c3 = sm.color (50,50,255);
			
		}

		// run everything
		public void run(ArrayList<FoodShow> cells) {
			flock(cells);

			update();

			borders();
			render();

		}

		public void applyForce(PVector force) {
			// We could add mass here if we want A = F / M
			acceleration.add(force);
		}

		// We accumulate a new acceleration each time based on three rules
		public void flock(ArrayList<FoodShow> cells) {
			PVector sep = separate(cells); // Separation
			PVector ali = align(cells); // Alignment
			PVector coh = cohesion(cells); // Cohesion
			// Arbitrarily weight these forces
			sep.mult(1.5f);
			ali.mult(1.0f);
			coh.mult(1.0f);
			// Add the force vectors to acceleration
			if (eatingb == false) {
				applyForce(sep);
				applyForce(ali);
				applyForce(coh);
			}
		}

		// Method to update location
		public void update() {
			// System.out.println(am);
			if (sm.millis() % 50 == 0 && am > 0 /* && millis() != oc */) {
				am--;
				oc = sm.millis();
			}
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
			// Not using this method until Processing.js catches up
			// desired.setMag(maxspeed);

			// Steering = Desired minus Velocity
			PVector steer = PVector.sub(desired, velocity);
			steer.limit(maxforce); // Limit to maximum steering force
			return steer;
		}
		
		private int cColor = 0;
		private int c1 = 0, c2 = 0,c3 = 0;
		public void render() {
			float theta = velocity.heading() + sm.radians(90);
			
			sm.pushMatrix();
			sm.fill(cColor);
			sm.translate(location.x, location.y);
			sm.rotate(theta);
			sm.ellipse(0, 0, am + 3, am * 1.25f + 3);// ellipse(0, 0, 20 + (am) +
													// 5, 20 + (am * 1.5f) + 5);
			sm.popMatrix();
			// Draw a triangle rotated in the direction of velocity
			
			if (n < 1) {
				cColor = sm.lerpColor(c1, c2, n);
			} else if (n < 2) {
				cColor = sm.lerpColor(c2, c3, n - 1);
			} else if (n < 3) {
				cColor = sm.lerpColor(c3, c1, n - 2);
			} else{
				n = 0;
			}
			n+=0.001;
			
			
			sm.pushMatrix();
			sm.translate(location.x, location.y);
			sm.rotate(theta);
			//System.out.println(this.faction.RGB);
			sm.fill(cColor); /* color(am, colord.y, am) */
			sm.noStroke();
			// strokeWeight(1);
			
			sm.ellipse(0, 0, am, am * 1.25f);// ellipse(0, 0, 20 + (am * 2), 20 +
											// (am * 3));

			sm.popMatrix();
		}

		// Wraparound
		public void borders() {
			/*if (location.x < 0)
				location.x = width;
			if (location.y < 0)
				location.y = height;
			if (location.x > width)
				location.x = 0;
			if (location.y > height)
				location.y = 0;*/
			if (location.x < 0)
				location.x = this.sm.width;
			if (location.y < 0)
				location.y = this.sm.height;
			if (location.x > this.sm.width)
				location.x = 0;
			if (location.y > this.sm.height)
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
			if (location.x > this.sm.width) {
				velocity.x = -velocity.x;
				location.x = this.sm.width - 1;
			}
			// location.x = 0;
			if (location.y > this.sm.height) {
				velocity.y = -velocity.y;
				location.y = this.sm.height - 1;
			}
		}

		// eating method

		// Separation
		// Method checks for nearby boids and steers away
		public PVector separate(ArrayList<FoodShow> cells) {
			float desiredseparation = 0.0f;
			PVector steer = new PVector(0, 0, 0);
			int count = 0;
			// For every boid in the system, check if it's too close
			for (FoodShow other : cells) {
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
		public PVector align(ArrayList<FoodShow> cells) {
			float neighbordist = 500;
			PVector sum = new PVector(0, 0);
			int count = 0;
			for (FoodShow other : cells) {
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
		public PVector cohesion(ArrayList<FoodShow> cells) {
			float neighbordist = 500;
			float enemydist = 50;
			PVector sum = new PVector(0, 0); // Start with empty vector to
												// accumulate all locations
			int count = 0;
			
			for (FoodShow other : cells) {
				float d = PVector.dist(location, other.location);
				sm.line(location.x, location.y, other.location.x, other.location.y);
				if ((d > 0) && (d < neighbordist)) {
					sum.add(other.location); // Add location
					count++;

				}
			}
			
			
			
			
			if (count > 0) {
				sum.div(count);
				sm.stroke(sm.color(colord.x, colord.y, colord.z));

				return seek(sum); // Steer towards the location
			} else {
				return new PVector(0, 0);
			}
		}

	}
