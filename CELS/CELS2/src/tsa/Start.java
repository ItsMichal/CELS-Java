package tsa;

//yolo
import java.util.ArrayList;

import javax.print.DocFlavor.URL;
import javax.swing.ImageIcon;

import processing.core.*;

import java.awt.Component;
import java.awt.Frame;
import java.util.*;

/**
 * 
 * @author Michal.B & Gunnar
 * 
 */
public class Start extends PApplet {
	

	int bgColor = color(255);

	int c1 = color(255, 50, 50), c2 = color(50, 255, 50), c3 = color(50, 50, 255);

	float n, n1;

	PFont tnof;
	Frame frame;

	int sLength = 9;
	int space = 1;

	float[][] change;

	public void setup() {
		PVector first = new PVector(random(255), (255), random(255));
		f = new FlockShow(this, first);
		// assign frame
		Object ftemp = this;
		while (ftemp != null && !(ftemp instanceof Frame)) {
			ftemp = ((Component) ftemp).getParent();
		}
		if (ftemp != null) {
			frame = (Frame) ftemp;
		}
		String[] directory = { "tsa.Start", "tsa.Simulation" };
		
		size(1000, 700);
		noStroke();
		tnof = createFont("monofonto.ttf", 32);
		frame.setResizable(false);
		frame.setTitle("CELS: Cell Evolution and Life Simulator (Simulation)");
		// pattern in the background
		change = new float[(700 / (sLength + space)) + 2][(1000 / (sLength + space)) + 2];

		for (int i = 0; i < change.length; i++)
			for (int u = 0; u < change[i].length; u++)
				change[i][u] = random(-10, 10);

	}
	FlockShow f;
	float transp = 255;
	float z = 0;
	public void draw() {
		
		background(bgColor);
		if (n < 1) {
			bgColor = lerpColor(c1, c2, n);
			
		} else if (n < 2) {
			bgColor = lerpColor(c2, c3, n - 1);
		} else if (n < 3) {
			bgColor = lerpColor(c3, c1, n - 2);
		} else {
			n = 0;
		}
		n += 0.001;
		if (f.h.size() < 50 && millis() % 500 == 0) {
			// System.out.print("Triggered.");
			f.addBoid(new FoodShow(this, width, (int) random(height), 2));
			f.addBoid(new FoodShow(this, (int) random(width), height, 2));
			
		}
		
		if(millis() % 500 == 0){
			f.addbea(new CellShow(this,(int) random(width), random(height)));
		}
		
		if(z < 1){
			transp-=map(0.01f, 0, 1, 0, 255);
			z+=0.01f;
		}else if(z < 2){
			transp+=map(0.01f, 0, 1, 0, 255);
			z+=0.01f;
		}else{
			z = 0;
		}
		if(transp < 2){
			transp=2;
		}else if(transp > 254){
			transp=254;
		}
		noStroke();
		  
		  
		  int x = space;
		  int y = space;
		  
		  for(int i = 0; i < (700/(sLength + space)) + 2; i++){
		     for(int t = 0; t < (1000/(sLength + space))+2; t++){
		      int fx=(int) map(noise(i + change[i][t],t + change[i][t]),0,1,red(bgColor)/2,red(bgColor)); 
		      int fx2=(int) map(noise(i + change[i][t],t + change[i][t]),0,1,green(bgColor)/2,green(bgColor)); 
		      int fx3=(int) map(noise(i + change[i][t],t + change[i][t]),0,1,blue(bgColor)/2,blue(bgColor)); 
		       //if(noise(i,t) == noise(i + change[i][t],t + change[i][t])
		       fill(color(fx,fx2,fx3), 100);
		       rect(x,y,sLength,sLength);
		       
		       
		       if((int)random(0,5) == 1)
		       change[i][t] += random((float)-0.1,(float)0.1 );
		       
		       
		       
		       x+= (sLength + space);
		     }
		     x = space;
		     y+= (sLength + space);
		  }
		
		  f.run();
		  f.eat();
		  f.reproduce();
		System.out.println(transp);
		textFont(tnof);
		fill(color(0,150));
		textSize(300);
		text("CELS", 0+width/4-60, 90+height/4);
		textSize(50);
		fill(color(0, map(transp, 0, 255, 0, 150)));
		text("Click Anywhere to Start", width / 2 - 298, height / 2 + 2);
		fill(color(0,90));
		rect(50,height/2+50, width-100,180);
		rect(50,height/2+300, width-100,180);
		fill(255);
		textSize(300);
		text("CELS", 0+width/4-65, 85+height/4);
		textSize(50);
		fill(255, transp);
		text("Click Anywhere to Start", width / 2 - 300, height / 2);
		fill(255);
		textSize(14);
		String s = "     This is Cell Evolution/life Simulator (CELS), a simulation of general life/evolution principles. When you start the program you will see a species builder as the first window. In this you will build the simulation of your desire. Once you click start each species will try to stay alive by eating food (the colorful triangles) and other species marked as enemies. Once the timer has cycled through the specified time the simulation will be over and a winner will be chosen, based off the amount of living cells a species has. Afterwards, a graph will be displayed showing dominance throughout the cycle, as well as a few further options, such as to Start Over from scratch. If you click Evolve, the other losing species will be removed and replaced with random mutations of the winning species, and the cycle starts again, simulating survival of the fittest. Run Forever will keep evolving species until the stop button is pressed. Thank you for using CELS. Have fun!";
		text(s, 50, height / 2 + 50, width - 100, height);
		String s2 = "Song Credit: Flight of the Bumblebee - US Army Band, Hungarian Rhapsody No. 2 - US Navy Band, Moonlight Sonata - MIDI Rendition, Vivaldi Four Seasons Spring Mvt. 1 - John Harrison, Symphony No. 5 in C Minor - Musopen, All other songs are original. All free.";
		text(s2, 50, height / 2 + 300, width - 100, height);
		// System.out.println(n);
	}

	/**
	 * <p>
	 * This method is what talks to the handler to change screens
	 * 
	 */
	public void starto() {
		// TODO: Replace useless handler with simple runSketch() call, import
		// main method here
		runSketch(new String[] { "tsa.SelectionMenu" }, new SelectionMenu());
		destroy();
		frame.setVisible(false);
		// frame.setVisible(false);
	}

	public void mousePressed() {
		starto();
	}
	static public void main(String[] passedArgs) {
		PApplet.main("tsa.Start");
	}

}