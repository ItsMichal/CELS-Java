//PACKAGE
package tsa;

//IMPORTS
import java.util.ArrayList;

import codeanticode.gsvideo.GSCapture;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.*;
import processing.event.MouseEvent;
import processing.video.Capture;

import java.util.*;
//import controlP5.*;
import java.awt.Component;
import java.awt.Frame;

public class Simulation extends PApplet {
	// SOUNDS
	Minim m = new Minim(this);
	int songid;

	AudioPlayer Rhapsody = m.loadFile("Hungarian.mp3");
	AudioPlayer Wave = m.loadFile("Wave.mp3");
	AudioPlayer Swift = m.loadFile("Swift.mp3");
	AudioPlayer Silent = m.loadFile("Silent.mp3");
	AudioPlayer Night = m.loadFile("Night.mp3");
	AudioPlayer Symphony = m.loadFile("Symphony.mp3");
	AudioPlayer Spring = m.loadFile("Spring.mp3");
	AudioPlayer Bumblebee = m.loadFile("Bumblebee.mp3");

	// VIDEO/WEBCAM
	Capture wc;

	private static final long serialVersionUID = 1L;
	// VARIABLES
	boolean runforever = false;
	// Is GUI Menu
	boolean isGUIEnabled_Start = false;
	boolean isGUIEnabled_Params = false;
	boolean stop = true;
	// The flock is a handler of every living thing
	Flock flock;
	PFont mono = createFont("monofonto.ttf", 32);

	// Cool colors
	int sfgColor = 0;
	int sbgColor = 255;
	int fgColor = 0;
	int bgColor = 255;
	float lerpr = 0;

	// Whether thing is being reversed
	boolean rev = false;

	// List of factions
	int numOfFacs;
	Faction[] factions;
	float[][] fcolors; /*
						 * = { { 255, 0, 0 }, { 0, 255, 0 }, { 0, 0, 255 }, {
						 * 255, 255, 0 }, { 0, 255, 255 }, { 255, 0, 255 }, { 0,
						 * 0, 0 }, { 255, 255, 180 } };
						 */
	// Fenemies sets up a team system
	int[][] fenemies; /*
						 * = { { 2, 3, 4, 5, 6, 7, 8 }, { 1, 3, 4, 5, 6, 7, 8 },
						 * { 1, 2, 4, 5, 6, 7, 8 }, { 1, 2, 3, 5, 6, 7, 8 }, {
						 * 1, 2, 3, 4, 6, 7, 8 }, { 1, 2, 3, 4, 5, 7, 8 }, { 1,
						 * 2, 3, 4, 5, 6, 8 }, { 1, 2, 3, 4, 5, 6, 7 } };
						 */
	int life = 20;
	/*
	 * FORMAT 0: Maximum Speed Value 1: Max Size (Before Split) 2: Metabolism 3:
	 * Maximum Targeting Distance / Sight (0 - blind, 600 - can see everything
	 * on the board, from any position) 4: Finesse (0 - no finesse, can't turn,
	 * 2 - too jittery, 1 - normal turning)
	 */

	int[][] facst; /*
					 * = { { (int) random(0, 5), (int) random(life, 40), (int)
					 * random(50, 60), (int) random(30, 60) }, { (int) random(1,
					 * 3), (int) random(life, 40), (int) random(50, 60), (int)
					 * random(30, 60) }, { (int) random(1, 3), (int)
					 * random(life, 40), (int) random(50, 60), (int) random(30,
					 * 60) }, { (int) random(1, 3), (int) random(life, 40),
					 * (int) random(50, 60), (int) random(30, 60) }, { (int)
					 * random(1, 3), (int) random(life, 40), (int) random(50,
					 * 60), (int) random(30, 60) }, { (int) random(1, 3), (int)
					 * random(life, 40), (int) random(50, 60), (int) random(30,
					 * 60) }, { (int) random(1, 3), (int) random(life, 40),
					 * (int) random(50, 60), (int) random(30, 60) }, { (int)
					 * random(1, 3), (int) random(life, 40), (int) random(50,
					 * 60), (int) random(30, 60) } };
					 */
	String[] fname; /*
					 * = { "Alpha", "Bravo", "Charlie", "Delta", "Echo",
					 * "Foxtrot", "Golf", "Hotel" };
					 */
	float[][] facPos;
	int[] facBNum;

	Faction winningFac;
	Faction winner = null;

	ArrayList<Faction> winners = new ArrayList<Faction>();
	int winIndex = 0;
	// The time left in the simulation
	int clock = 0;
	int time;
	// STOP

	// STOP
	// time to stop sim
	int stoptime;
	// STOP

	// STOP
	int gen = 1;
	int countdown;

	// ArrayList containing data for grap.
	ArrayList<DPoint> data = new ArrayList<DPoint>();

	// Status
	/*
	 * 0: Start Menu (Until Button is Clicked) 1: GUI Preparation (Until
	 * Everything is set) 2: Sim Prep (1 Frame) 2: Active Simulation (Until time
	 * ends) 3: Graphs (Until Button is clicked)
	 * 
	 * NOT IMPLEMENTED: 4: Legacy Simulation 5: Experimental Simulation
	 */
	int step = 0;

	// zoomscale
	float minZoom = 1;
	public float zooms = minZoom;
	// global position
	float tranX = 0, tranY = 0;
	// zoom MAX
	float maxZoom = 5;
	float zscale = 0.05f;

	// tracking
	Cell tracking = null;
	boolean istracking = false;

	boolean music;

	/**
	 * @param numOfFacs
	 * @param fcolors
	 * @param fenemies
	 * @param facst
	 * @param fname
	 * @param stoptime
	 */
	public Simulation(int numOfFacs, float[][] fcolors, int[][] fenemies, int[][] facst, String[] fname, int stoptime,
			float[][] facPos, int[] facBNum, boolean music, int song) {
		super();
		this.music = music;
		this.numOfFacs = numOfFacs;
		this.fcolors = fcolors.clone();
		this.fenemies = fenemies.clone();
		this.facst = facst.clone();
		this.fname = fname.clone();
		this.stoptime = stoptime;
		this.facPos = facPos.clone();
		this.facBNum = facBNum.clone();
		this.songid = song;
		countdown = stoptime;
	}
	Frame frame;

	public void setup() {
		// p5 = new ControlP5(this);
		System.out.println(zooms);
		// Sets the size of the sketch (if not fullscreen.
		size(1200, 700);
		// Populates faction list
		// frame.setResizable(false);
		Object ftemp = this;
		while (ftemp != null && !(ftemp instanceof Frame)) {
			ftemp = ((Component) ftemp).getParent();
		}
		if (ftemp != null) {
			frame = (Frame) ftemp;
		}
		frame.setTitle("CELS: Cell Evolution and Life Simulator (Start)");
		generate();
		// String[] songlist = {"Spring", "Rhapsody", "Bumblebee", "Night",
		// "Symphony", "Swift"};
		switch (songid) {
		case 0:
			playLoop(Spring);
			break;
		case 1:
			playLoop(Rhapsody);
			break;
		case 2:
			playLoop(Bumblebee);
			break;
		case 3:
			playLoop(Night);
			break;
		case 4:
			playLoop(Symphony);
			break;
		case 5:
			playLoop(Swift);
			break;
		case 6:
			playLoop(Wave);
			break;
		case 7:
			playLoop(Silent);
			break;

		}
	}

	/**
	 * <p>
	 * This method will start a new generation of the simulator and reset all
	 * the settings to start another simulation.
	 * 
	 */
	public void generate() {
		factions = new Faction[numOfFacs];
		for (int i = 0; i < factions.length; i++) {
			factions[i] = new Faction(i + 1, fcolors[i], fenemies[i], fname[i]);
		}

		// The initial point for the flock that is set to be random
		PVector first = new PVector(random(255), (255), random(255));

		// Instantiate a flock of
		flock = new Flock(this, first);
		// Add an initial set of boids into the system
		for (int i = 0; i < 69; i++) {
			flock.addBoid(new Food(this, width / 2, height / 2, 20));
		}

		for (int i = 0; i < numOfFacs; i++) {
			// adds the cells with all of their values.
			for (int j = 0; j < facBNum[i]; j++)
				flock.addbea(new Cell(this, facPos[i][0], facPos[i][1], factions[i], facst[i]));

		}

		time = millis();

	}

	boolean test = true;
	float n = 0;

	public void draw() {

		if (stop) {
			if(winner != null && winner.members.size() == 0){
				stop = false;
			}
			if (n > 3) {
				n = 0;
			}
			n += 0.001;

			background(bgColor);

			flock.run();
			// flock.removes();

			flock.eat();
			flock.reproduce();
			// status
			for (int i = 0; i < factions.length; i++) {
				for (int j = 0; j < factions[i].members.size(); j++) {
					if (factions[i].members.get(j).ifOver(mouseX, mouseY)) {

					}
				}

			}
			status();
			timer();
			tracking();
			translate(40, 40);

			if (flock.h.size() < 200 && millis() % 40 == 0) {
				// System.out.print("Triggered.");
				flock.addBoid(new Food(this, width, (int) random(height), 2));
				flock.addBoid(new Food(this, (int) random(width), height, 2));
				flock.addBoid(new Food(this, width, (int) random(height), 2));
				flock.addBoid(new Food(this, (int) random(width), height, 2));
				flock.addBoid(new Food(this, width, (int) random(height), 2));
				flock.addBoid(new Food(this, (int) random(width), height, 2));
				flock.addBoid(new Food(this, width, (int) random(height), 2));
				flock.addBoid(new Food(this, (int) random(width), height, 2));
				flock.addBoid(new Food(this, width, (int) random(height), 2));
				flock.addBoid(new Food(this, (int) random(width), height, 2));
				flock.addBoid(new Food(this, width, (int) random(height), 2));
				flock.addBoid(new Food(this, (int) random(width), height, 2));
				flock.addBoid(new Food(this, width, (int) random(height), 2));
				flock.addBoid(new Food(this, (int) random(width), height, 2));
				flock.addBoid(new Food(this, width, (int) random(height), 2));
				flock.addBoid(new Food(this, (int) random(width), height, 2));
				flock.addBoid(new Food(this, width, (int) random(height), 2));
				flock.addBoid(new Food(this, (int) random(width), height, 2));
				flock.addBoid(new Food(this, width, (int) random(height), 2));
				flock.addBoid(new Food(this, (int) random(width), height, 2));
				flock.addBoid(new Food(this, width, (int) random(height), 2));
				flock.addBoid(new Food(this, (int) random(width), height, 2));
				flock.addBoid(new Food(this, width, (int) random(height), 2));
				flock.addBoid(new Food(this, (int) random(width), height, 2));
			}
			/*
			 * if (millis() % 1000 == 0) { flock.addbea(new HostileCell(width /
			 * 4, height / 4, factions[0])); flock.addbea(new HostileCell(width
			 * - width / 4, height - height / 4, factions[1])); flock.addbea(new
			 * HostileCell(width / 4, height - height / 4, factions[2]));
			 * flock.addbea(new HostileCell(width - width / 4, height / 4,
			 * factions[3])); flock.addbea(new HostileCell(width / 2, height -
			 * height / 4, factions[4])); flock.addbea(new HostileCell(width /
			 * 2, height / 4, factions[5])); flock.addbea(new HostileCell(width
			 * - width / 4, height / 2, factions[6])); flock.addbea(new
			 * HostileCell(width / 4, height / 2, factions[7]));
			 * flock.addbea(new HostileCell(width / 2, height / 2,
			 * factions[8])); } // flock.addBoid(new Cell(width / 2, height / 2,
			 * 20)); // drawGridLines(40, true); // drawGridLines(100, false); }
			 * else { graph(); }
			 */
			if (rev) {
				if (millis() % 1 == 0) {
					lerpr += 0.01;
				}
			} else {
				if (millis() % 1 == 0)
					lerpr -= 0.01;
			}
			if (lerpr > 1) {
				lerpr = 1;
				rev = false;
			}
			if (lerpr < 0) {
				lerpr = 0;
				rev = true;
			}
			if (runforever) {
				pushMatrix();
				// GUI ELEMEENT scale(zooms);
				fill(color(255, 0, 0, 150));
				rect(width - 100, 0, 100, 50);
				fill(255);
				text("stop", width - 85, 37);
				popMatrix();
			}
		} else {
			if (test)
				for (int i = 0; i < factions.length; i++) {
					if (i == winIndex) {
						System.out.println("Hooray! " + factions[i].name + " is the winner with color R"
								+ factions[i].RGB[0] + " G:" + factions[i].RGB[1] + " B:" + factions[i].RGB[2]
								+ " | am: " + factions[i].getamount());
					} else {
						System.out.println("Boo... " + factions[i].name + " is the loser with color R"
								+ factions[i].RGB[0] + " G:" + factions[i].RGB[1] + " B:" + factions[i].RGB[2]
								+ " | am: " + factions[i].getamount());
					}

				}
			graph();
			test = false;
			if (runforever) {
				but();
			}
		}
		noStroke();
	}

	/**
	 * <p>
	 * The timer method keeps track of where the simulation is in a time stand
	 * point. The method also logs data on every 10 sec, it saves that data in a
	 * encapsulated class called DPoint. It saves up to 6 data points.
	 * 
	 */
	public void timer() {

		translate(0, 0);

		fill(50, 50, 255);
		if (millis() - time >= 1000) {
			clock++;
			time = millis();

		}
		if (clock % 5 == 0 && clock != stoptime) {
			// added------------------------------------------------------------
			int[] fact = new int[factions.length];

			for (int k = 0; k < fact.length; k++) {

				fact[k] = factions[k].getamount();

			}

			data.add(new DPoint(fact));
		}
		// added------------------------------------------------------------
		if (clock == stoptime) {
			clock = 0;
			stop = false;
			winners.add(winner);
			data.add(new DPoint(true));
		}
		// System.out.println("TC: " + clock);

		stroke(fgColor);
		noFill();
		arc(100, 100, 100, 100, radians(0), radians(360));
		noStroke();
		// fill(fgColor, fgColor, fgColor, 0.8f);
		// arc(100, 100, 100, 100, radians(0), radians(map(clock, 0, stoptime,
		// 0, 360)));
		fill(0);
		int value = countdown - clock;
		int minutes = value / 60;

		String min = "";
		String sec = "";
		if (value % 60 <= 9)
			sec = "0" + value % 60;
		else
			sec = "" + value % 60;
		if (value < 600)
			min = "0" + minutes;
		else
			min = minutes + "";
		textSize(14);
		text("Timer:", 50, 165);
		textSize(24);
		text(min + ":" + sec, 95, 169);
	}

	/**
	 * <p>
	 * Status method will be talking to the Flock class to obtain information
	 * that can be used by the user. This calls will layout the text that shows
	 * during simulation.
	 */

	public void status() {

		fill(0);

		translate(-40, -40);
		noFill();
		pushMatrix();

		// Setting values for the status bar so not affect from other external
		// values.
		fill(color(255, 200));
		stroke(color(100, 200));
		strokeWeight(1);
		rect(45, 45, 250, 150);
		noStroke();

		popMatrix();

		if (winner != null)
			fill(color(winner.RGB[0], winner.RGB[1], winner.RGB[2], 220));
		rect(210, 130, 80, 40);
		stroke(255);
		// displ generations
		if (winner != null) {
			float common = (winner.RGB[0] + winner.RGB[1] + winner.RGB[2]) / 3;
			if (common > 127) {
				fill(color(0));
			} else {
				fill(color(255));
			}

		}
		textSize(32);
		if (gen >= 10) {
			text("G:" + gen, 220, 165);
		} else {
			text("G:0" + gen, 220, 165);
		}

		textSize(14);
		textFont(mono);
		fill(0, 0.2f);
		// rect(100, 100, 400, 300);
		/*
		 * fill(0, 0, flock.getAmBac() * 2); rect(200, 100, flock.getAmBac() *
		 * 2, 20); fill(0, flock.getAmKiller() * 2, flock.getAmKiller() * 2);
		 * rect(200, 120, flock.getAmKiller() * 2, 20);
		 */

		// Count faction winner
		textSize(14);
		int total = 0;
		int cnt = 0;

		for (int i = 0; i < factions.length; i++) {
			if (factions[i].members.size() > cnt) {
				winner = factions[i];
				cnt = factions[i].members.size();
			}
			total += factions[i].members.size();
		}
		// System.out.println(total);
		float totalarc = 0;
		float totalpt = 0;
		for (int i = 0; i < factions.length; i++) {
			fill(color(factions[i].RGB[0], factions[i].RGB[1], factions[i].RGB[2], 220));
			// System.out.println(factions[i].name + ": " + (float)
			// factions[i].members.size() / (float) total);
			arc(100, 100, 100, 100, radians(totalarc),
					radians(totalarc + map((float) factions[i].members.size() / (float) total, 0, 1, 0, 360)));
			totalarc += map((float) factions[i].members.size() / (float) total, 0, 1, 0, 360);
			noStroke();
			// totalpt += map((float)factions[i].members.size()/(float)total,
			// 0,1,0,360);
		}
		// System.out.println(totalpt);

		fill(color(winner.RGB[0], winner.RGB[1], winner.RGB[2], 220));
		rect(50, 170, 240, 20);
		if (winner != null) {
			float common = (winner.RGB[0] + winner.RGB[1] + winner.RGB[2]) / 3;
			if (common > 127) {
				fill(color(255));
			} else {
				fill(color(0));
			}

		}
		text(" Winning Cell Group: " + ((winner.name.length() < 9) ? winner.name : winner.name.substring(0, 7) + "..."), 71, 185);
		// fill(color(255 - winner.RGB[0], 255 - winner.RGB[1], 255 -
		// winner.RGB[2]));
		if (winner != null) {
			float common = (winner.RGB[0] + winner.RGB[1] + winner.RGB[2]) / 3;
			if (common > 127) {
				fill(color(0));
			} else {
				fill(color(255));
			}

		}

		text(" Winning Cell Group: " + ((winner.name.length() < 9) ? winner.name : winner.name.substring(0, 7) + "..."), 70, 185);

		// show fraction amount

		int indx = 0;
		int row = factions.length;
		int j = 0;
		int ro = 0;
		for (int ros = 0; ros < row; ros++) {
			if (ro % 3 == 0) {
				j--;
				ro = 0;
			}

			fill(color(factions[indx].RGB[0], factions[indx].RGB[1], factions[indx].RGB[2]), 150);
			if (factions[indx] == winner) {
				fill(color(factions[indx].RGB[0], factions[indx].RGB[1], factions[indx].RGB[2], 220));
				winIndex = indx;
			}

			float common = (factions[indx].RGB[0] + factions[indx].RGB[1] + factions[indx].RGB[2]) / 3;
			stroke(100);
			strokeWeight(0.5f);
			rect(ro * 40 + 170, 100 + (j * 20 + 70), 40, 20);
			noStroke();
			if (common > 127) {
				fill(color(0));
			} else {
				fill(color(255));
			}

			// fill(color(100 - factions[indx].RGB[0], 100 -
			// factions[indx].RGB[0], 100 - factions[indx].RGB[0]));
			text(factions[indx].members.size(), ro * 40 + 177, 100 + (j * 20 + 85));
			indx++;
			if (indx >= 3)
				ro++;
		}

	}

	/**
	 * <p>
	 * This method shows data that has been collected by the Timer class. It
	 * will display a matrix of lines. This method will ask for input form the
	 * Flock class.
	 * 
	 */
	public void graph() {
		int plx = 1100;
		background(50);

		// title
		noStroke();
		fill(255);
		textSize(72);
		text("RESULTS", 70, 100);
		textSize(24);
		fill(200);
		text("Simulation #" + gen, 325, 100);

		// scale(100,100);

		noStroke();
		textSize(18);
		stroke(255);
		fill(color(255, 100, 100));
		rect(70, 565, 150, 50);
		fill(50);
		text("Start Over", 97, 595);
		fill(color(100, 255, 100));
		rect(245, 565, 150, 50);
		fill(50);
		text("Evolve", 292, 595);
		fill(color(100, 255, 100));
		rect(420, 565, 150, 50);
		fill(50);
		text("Run Forever", 445, 595);

		translate(70, 550);

		// textSize(50);
		// text("Graph",0,0);

		stroke(100);
		strokeWeight(4);
		for (int h = 0; h <= 20; h++)
			line(0, -h * 20, plx, -h * 20);
		textSize(20);
		fill(255);
		for (int tex = 0; tex <= 20; tex++) {

			text(tex * 5, -10 * (new String(tex * 5 + "").length()) + -20, -tex * 20);

		}

		int siz = data.size() - 1;
		int amEnd = 0;
		for (int g = 0; g < data.size(); g++)
			if (data.get(g).en)
				amEnd++;

		if (amEnd > 2) {

			for (int h = 0; h < data.size(); h++) {

				if (data.get(0).en) {
					data.remove(0);
					break;
				}
				data.remove(0);
			}

			siz = data.size() - 1;

		}
		// puts data on graph
		if (data.size() > plx) {
			siz = plx;
		}

		for (int points = 0; points < siz - 1; points++) {

			int[] s = data.get(points).getmarks();
			int[] f = data.get(points + 1).getmarks();
			stroke(color(0, 102, 255));

			strokeWeight(5);

			for (int h = 0; h < s.length; h++) {
				if (s[h] != 0) {
					float[] colo = factions[h].getFactionColor();
					stroke(color(colo[0], colo[1], colo[2]));

					line(points * (plx / (float) siz), -map(s[h], 0, 400, 0, plx), (points + 1) * (plx / (float) siz),
							-map(f[h], 0, 400, 0, plx));
				}

			}
			if (data.get(points).en) {
				stroke(100);
				strokeWeight(5);
				line((points * (plx / (float) siz)) + 5, 0, (points * (plx / (float) siz)) + 5, -400);
			}
			stroke(255);
			strokeWeight(4);

			for (int k = 0; k < winners.size(); k++) {
				// System.out.println("Winners Size: " + winners.size());
				float[] co = winners.get(k).RGB;
				fill(color(co[0], co[1], co[2]));

				rect(80 * (k) + 540, 30, 80, 30);
				fill(color(255 - co[0], 255 - co[1], 255 - co[2]));
				String modi = (winners.get(k).name.length() < 7) ? winners.get(k).name
						: winners.get(k).name.substring(0, 4) + "...";
				text(modi, 80 * (k) + 548, 55);
				if (k != 0 && k % 7 == 0) {
					winners.remove(0);
					System.out.println("Winners Removed");
				}

			}
			/*
			 * if(points % 10 == 0){ text(s[0],points * (1500 / data.size()),
			 * map(s[0] - 5,0,high,0,size)); text(s[1],points * (1500 /
			 * data.size()), map(s[1] - 5,0,high,0,size)); text(s[3],points *
			 * (1500 / data.size()), map(s[3] - 5,0,high,0,size)); }
			 */
			noStroke();
			strokeWeight(0);
			// scale(0f);
		}

	}

	/**
	 * <p>
	 * This method is the main controller of the evolution piece for our
	 * program. It will control the mutations or rest the entire model based on
	 * what the mouse clicked returns.
	 */
	public void but() {
		// System.out.println((mouseX >= 275 && mouseX <= 425) && (mouseY >= 565
		// && mouseY <= 615));
		if ((mouseX >= 245 && mouseX <= 395) && (mouseY >= 565 && mouseY <= 615) && runforever == false) {
			generate();
			stop = true;
			for (int h = 0; h < facst.length; h++) {
				// System.out.print(h);
				// System.out.println(factions[winIndex].name + " is winner with
				// color " + factions[winIndex].RGB[0] + " "
				// +factions[winIndex].RGB[1]+" " + factions[winIndex].RGB[2] );

				if (facst.length == 1||h != winIndex) {
					// facst 0:
					facst[h][0] = facst[winIndex][0] + (int) random(-2, 2);
					facst[h][1] = facst[winIndex][1] + (int) random(-5, 5);
					if (facst[h][1] < 20) {
						facst[h][1] = 20;
					}
					facst[h][2] = facst[winIndex][2] + (int) random(-5, 5);
					facst[h][3] = facst[winIndex][3] + (int) random(-5, 5);
					if (facst[h][3] < 0) {
						facst[h][3] = 0;
					}
					// Variance value to ensure different colors
					int variance = 50;

					// cap of random iterations
					int cap = 500;

					// counts how many iterations
					int capr = 0;
					// Value of RED
					float rvar = ((int) (random(-13, 10) + 2) * 14 + 1) + factions[winIndex].RGB[0];
					for (int i = 0; i < factions.length; i++) {
						if (i != h) {
							int magnitude = 14;
							boolean ran = false;
							capr++;
							if (capr > cap)
								break;
							System.out.println("RED i:" + i + " | " + rvar + " <= " + factions[i].RGB[0] + "+"
									+ variance + factions[h].name + " "
									+ (rvar <= factions[i].RGB[0] + variance && rvar >= factions[i].RGB[0] - variance));
							while ((rvar <= factions[i].RGB[0] + variance && rvar >= factions[i].RGB[0] - variance)
									|| rvar < 0 || rvar > 255) {
								System.out.println(rvar + " " + i + " " + factions[h].name);
								rvar += ((int) (random(-13, 10) + 2) * magnitude + 1);
								capr++;
								while (rvar < 0) {
									System.out.println("NEGATIVE");
									rvar += ((int) (random(1, 10) + 2) * magnitude + 1);
									capr++;
								}
								while (rvar > 255) {
									System.out.println("POSITIVE");
									rvar += ((int) (random(-13, -1) + 2) * magnitude + 1);
									capr++;
									if (capr > cap)
										break;
								}
								magnitude++;

								if (capr > cap)
									break;
								ran = true;
							}
							if (ran) {
								i = -1;
							}
						}

					}
					capr = 0;
					float gvar = ((int) (random(-13, 10) + 2) * 14 + 1) + factions[winIndex].RGB[1];
					for (int i = 0; i < factions.length; i++) {
						if (i != h) {
							capr++;
							if (capr > cap)
								break;
							System.out.println("GREEN i:" + i + " | " + gvar + " <= " + factions[i].RGB[0] + "+"
									+ variance + factions[h].name + " "
									+ (rvar <= factions[i].RGB[0] + variance && rvar >= factions[i].RGB[0] - variance));
							int magnitude = 14;
							boolean ran = false;
							while ((gvar <= factions[i].RGB[1] + variance && gvar >= factions[i].RGB[1] - variance)
									|| rvar < 0 || rvar > 255) {
								gvar = ((int) (random(-13, 10) + 2) * magnitude + 1) + factions[winIndex].RGB[1];
								while (gvar < 0) {
									gvar = ((int) (random(1, 10) + 2) * magnitude + 1) + factions[winIndex].RGB[1];
									capr++;
									if (capr > cap)
										break;
								}
								while (gvar > 255) {
									gvar = ((int) (random(-13, -1) + 2) * magnitude + 1) + factions[winIndex].RGB[1];
									capr++;
									if (capr > cap)
										break;
								}
								magnitude++;
								capr++;
								if (capr > cap)
									break;
								ran = true;
							}
							if (ran) {
								i = -1;
							}
						}
					}
					capr = 0;
					float bvar = ((int) (random(-13, 10) + 2) * 14 + 1) + factions[winIndex].RGB[2];
					for (int i = 0; i < factions.length; i++) {
						if (i != h) {
							int magnitude = 14;
							boolean ran = false;
							capr++;
							if (capr > cap)
								break;
							System.out.println(capr + " capr BLUE i:" + i + " | " + bvar + " <= " + factions[i].RGB[2]
									+ "+" + variance + factions[h].name + " "
									+ (bvar <= factions[i].RGB[2] + variance && bvar >= factions[i].RGB[2] - variance));
							while ((bvar <= factions[i].RGB[2] + variance && bvar >= factions[i].RGB[2] - variance)
									|| bvar < 0 || bvar > 255) {
								bvar = ((int) (random(-13, 10) + 2) * magnitude + 1) + factions[winIndex].RGB[2];

								while (bvar < 0) {
									bvar = ((int) (random(1, 10) + 2) * magnitude + 1) + factions[winIndex].RGB[2];
									capr++;
									if (capr > cap)
										break;
								}
								while (bvar > 255) {

									bvar = ((int) (random(-13, -1) + 2) * magnitude + 1) + factions[winIndex].RGB[2];
									capr++;
									if (capr > cap)
										break;
								}

								magnitude++;
								ran = true;
								capr++;
								if (capr > cap)
									break;
							}

							if (ran) {
								i = -1;
							}
						}
					}
					if (rvar > 255) {
						rvar = 255;
						rvar += random(-11, -1) * 14;
					} else if (rvar < 0) {
						rvar = 0;
						rvar += random(11, 1) * 14;
					}
					if (gvar > 255) {
						gvar = 255;
						gvar += random(-11, -1) * 14;
					} else if (gvar < 0) {
						gvar += random(11, 1) * 14;
						gvar = 0;
					}
					if (bvar > 255) {
						bvar = 255;
						bvar += random(-11, -1) * 14;
					} else if (bvar < 0) {
						bvar = 0;
						bvar += random(11, 1) * 14;
					}
					if (factions[h].RGB == null) {
						System.out.println("IAMBULL");
					}

					factions[h].RGB[0] = rvar;
					factions[h].RGB[1] = gvar;
					factions[h].RGB[2] = bvar;
					fcolors[h][0] = rvar;
					fcolors[h][1] = gvar;
					fcolors[h][2] = bvar;
					// System.out.println(rvar);
					// System.out.print(factions[h].RGB[0]);

				}
			}
			System.out.println("HERE");

			test = true;
			noStroke();
			// data.clear();
			gen++;
			// System.out.println("gen pp called at 631");
		}
		if ((mouseX >= 100 && mouseX <= 250) && (mouseY >= 565 && mouseY <= 615)) {
			noStroke();
			winners.clear();
			generate();
			stop = true;
			data.clear();
			gen = 0;
			// System.out.println("gen pp called at 640");
		}

		if ((mouseX >= 450 && mouseX <= 600) && (mouseY >= 565 && mouseY <= 615) || runforever) {
			runforever = true;
			generate();
			for (int h = 0; h < facst.length; h++) {
				// System.out.print(h);
				// System.out.println(factions[winIndex].name + " is winner with
				// color " + factions[winIndex].RGB[0] + " "
				// +factions[winIndex].RGB[1]+" " + factions[winIndex].RGB[2] );

				if (h != winIndex) {
					facst[h][0] = facst[winIndex][0] + (int) random(-2, 2);
					facst[h][1] = facst[winIndex][1] + (int) random(-5, 5);
					if (facst[h][1] < 20) {
						facst[h][1] = 20;
					}
					facst[h][2] = facst[winIndex][2] + (int) random(-5, 5);
					facst[h][3] = facst[winIndex][3] + (int) random(-5, 5);
					if (facst[h][3] < 0) {
						facst[h][3] = 0;
					}
					float rvar = ((int) (random(-13, 14) + 2) * 10 + 1) + factions[winIndex].RGB[0];
					float gvar = ((int) (random(-13, 14) + 2) * 10 + 1) + factions[winIndex].RGB[1];
					float bvar = ((int) (random(-13, 14) + 2) * 10 + 1) + factions[winIndex].RGB[2];
					if (rvar > 255) {
						rvar = 255;
						rvar += random(-11, -1) * 14;
					} else if (rvar < 0) {
						rvar = 0;
						rvar += random(11, 1) * 14;
					}
					if (gvar > 255) {
						gvar = 255;
						gvar += random(-11, -1) * 14;
					} else if (gvar < 0) {
						gvar += random(11, 1) * 14;
						gvar = 0;
					}
					if (bvar > 255) {
						bvar = 255;
						bvar += random(-11, -1) * 14;
					} else if (bvar < 0) {
						bvar = 0;
						bvar += random(11, 1) * 14;
					}
					if (factions[h].RGB == null) {
						// System.out.println("IAMBULL");
					}

					factions[h].RGB[0] = rvar;
					factions[h].RGB[1] = gvar;
					factions[h].RGB[2] = bvar;
					fcolors[h][0] = rvar;
					fcolors[h][1] = gvar;
					fcolors[h][2] = bvar;
					// System.out.println(rvar);
					// System.out.print(factions[h].RGB[0]);

				}
			}

			stop = true;
			test = true;
			noStroke();
			gen++;
		}

		// System.out.println("gen pp called at 710");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see processing.core.PApplet#mousePressed()
	 * 
	 */
	public void mousePressed() {

		if (!stop) {
			but();

		} else {
			if (runforever)
				if ((mouseX >= width - 100 && mouseX <= width) && (mouseY >= 0 && mouseY <= 50)) {
					runforever = false;
					stop = false;
				}

			if (istracking) {
				istracking = false;
			}
			for (int i = 0; i < factions.length; i++) {
				for (int j = 0; j < factions[i].members.size(); j++) {
					if (factions[i].members.get(j).ifOver(mouseX, mouseY)) {
						tracking = factions[i].members.get(j);
						istracking = true;
						break;

					}
				}

			}

		}
	}

	public void mouseWheel(MouseEvent e) {

		if (stop) {
			double lboundX = width * (1.0 / zooms);
			double lboundY = height * (1.0 / zooms);
			System.out.println(e.toString());

			float delta = 1.0f;
			if (e.getCount() < 0) {
				zooms += zscale;

			} else if (e.getCount() > 0) {
				// make sure zoom is not going to be below zero
				// if (!(zooms - zscale < 1)) {
				zooms -= zscale;

				// }
			}

			/*
			 * tranX -= mouseX; tranY -= mouseY; float delta = (float)
			 * (e.getCount() > 0 ? 1.05 : e.getCount() < 0 ? 1.0/1.05 : 1.0);
			 * zooms *= delta; tranX *= delta; tranY *= delta; tranX += mouseX;
			 * tranY += mouseY;
			 */
			if (zooms > maxZoom) {
				zooms = maxZoom;
			} else if (zooms < 1) {
				zooms = 1;
			}

			// if(e.getCount() < 0){
			// tranX += (int) -(mouseX * 0.01);
			// tranY += (int) -(mouseY * 0.01);
			// }else if(e.getCount() > 0){
			// tranX = (int) -(mouseX * -0.01);
			// tranY = (int) -(mouseY * -0.01);
			// }
			double boundX = width * (1.0 / zooms);
			double boundY = height * (1.0 / zooms);

			tranX += (float) -((lboundX - boundX) / 2);
			tranY += (float) -((lboundY - boundY) / 2);

			// System.out
			if ((-tranX + boundX) > width) {
				tranX = (int) (-width + (boundX));
			}
			if (tranX > 0) {
				tranX = 0;
			}
			if ((-tranY + boundY) > height) {
				tranY = (int) (-height + (boundY));
			}
			if (tranY > 0) {
				tranY = 0;
			}
			// System.out.println("mouseX: " + mouseX + " mouseY: " + mouseY);
			System.out.println(height + " : " + width + " : " + boundX + " : " + boundY + " : " + zooms + " : " + tranX
					+ " : " + tranY);
		}

	}

	public void mouseDragged(MouseEvent e) {
		if (stop && !istracking) {
			double boundX = width * (1.0 / zooms);
			double boundY = height * (1.0 / zooms);
			tranX += mouseX - pmouseX;
			tranY += mouseY - pmouseY;
			if ((-tranX + boundX) > width) {
				tranX = (int) (-width + (boundX));
			}
			if (tranX > 0) {
				tranX = 0;
			}
			if ((-tranY + boundY) > height) {
				tranY = (int) (-height + (boundY));
			}
			if (tranY > 0) {
				tranY = 0;
			}
		}
	}

	PVector plocation = null;

	public void tracking() {

		if (tracking != null && istracking) {
			if (plocation == null) {
				plocation = tracking.location;
				System.out.println("PLCOATION SET");
			}

			zooms = 1.75f;
			double boundX = width * (1.0 / zooms);
			double boundY = height * (1.0 / zooms);
			// tranX += (float) -((lboundX - boundX) / 2);
			// tranY += (float) -((lboundY - boundY) / 2);
			tranX = (float) (-tracking.location.x + boundX / 2);
			tranY = (float) (-tracking.location.y + boundY / 2);
			if ((-tranX + boundX) > width) {
				tranX = (int) (-width + (boundX));
			}
			if (tranX > 0) {
				tranX = 0;
			}
			if ((-tranY + boundY) > height) {
				tranY = (int) (-height + (boundY));
			}
			if (tranY > 0) {
				tranY = 0;
			}
			plocation = tracking.location;
		} else {
			plocation = null;
			// System.out.println("NOTTRACKING");
		}

	}

	public void playLoop(AudioPlayer f) {
		f.loop();
		//f.play();
	}
}
