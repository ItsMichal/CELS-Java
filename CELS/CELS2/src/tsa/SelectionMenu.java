package tsa;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

import java.awt.Component;
import java.awt.Frame;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controlP5.*;

public class SelectionMenu extends PApplet {

	ArrayList<FactionMenu> factions;
	ControlP5 cp5;
	ScrollableList facsel;
	Button addfac;
	Button remfac;
	Button start;
	Button mute;
	Button song;
	PFont mono = createFont("monofonto.ttf", 32);
	PFont monos = createFont("monofonto.ttf", 12);
	PFont monom = createFont("monofonto.ttf", 24);
	PFont monol = createFont("monofonto.ttf", 72);
	// background color themes
	int bgColor = color(255);
	int c1 = color(255, 50, 50), c2 = color(50, 255, 50), c3 = color(50, 50, 255);
	float n, n1;
	Textlabel tooltip;
	Textlabel tooltip2;
	FactionMenu active;

	Faction[] fac;

	CColor cur = new CColor();

	Slider roundtimer;
	Textlabel roundtassist;
	Textlabel roundt2;
	Textlabel facselh;
	Frame frame;
	FlockShow f;

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
		frame.setTitle("CELS: Cell Evolution and Life Simulator (Setup)");
		factions = new ArrayList<FactionMenu>();

		cur.setCaptionLabel(color(0));
		cur.setActive(color(100, 100));
		cur.setBackground(bgColor);
		size(1200, 700);
		background(color(255, 102, 153));

		// create GUI
		cp5 = new ControlP5(this);
		// set default font to mono

		// List l = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");
		cp5.addButton("facselt").setPosition(width - 800 + 5, 0).setSize(395, 73).setColorActive(color(0, 0))
				.setColorBackground(color(0, 0)).setColorForeground(color(0, 0)).setColorLabel(color(0, 0));
		facsel = cp5.addScrollableList("facsel").setPosition(width - 800 + 5, 0).setSize(395, height - 70)
				.setBarHeight(68).setItemHeight(40).setBackgroundColor(bgColor).setColorBackground(bgColor)
				.setColorForeground(lerpColor(bgColor, color(100), 0.5f)).setColorCaptionLabel(color(255))
				.setBroadcast(true)
				// .setType(ScrollableList.DROPDOWN)

		;

		cv = cp5.addBackground("cv").setPosition(0, 73).setSize(width, height - 73 * 2).setBackgroundColor(color(0, 1f))
				.setVisible(false);

		facsel.getValueLabel().setFont(mono).getStyle().setPaddingTop(10);
		facsel.getCaptionLabel().setFont(mono).set("Press Add to Start").getStyle().setPaddingTop(13);// .setPaddingLeft(200);
		facsel.getCaptionLabel().align(CENTER, CENTER);

		roundtimer = cp5.addSlider("roundtime").setPosition(width - 400 + 5, 0).setSize(395, 68).setColorBackground(0)
				.setColorActive(lerpColor(bgColor, color(0), 0.95f))
				.setColorForeground(lerpColor(bgColor, color(0), 0.9f)).setMin(10).setMax(600).setNumberOfTickMarks(591)
				.setColorCaptionLabel(color(0)).showTickMarks(false);
		roundtimer.getValueLabel().setColor(color(0));
		roundtimer.getValueLabel().getStyle().setMarginLeft(1000);
		roundtassist = cp5.addTextlabel("roundtassist").setPosition(width - 400 + 5, 0).setSize(395, 73)
				.setColor(color(255)).setFont(mono);
		roundtassist.getStyle().setMarginTop(15);
		roundtassist.getCaptionLabel().align(Textlabel.CENTER, Textlabel.CENTER);
		roundt2 = cp5.addTextlabel("round").setPosition(width - 400 + 5, 32).setSize(395, 36).setColor(color(255))
				.setFont(monos).setText("Click and Drag Left and Right Here to Change Round Time").setColor(color(150));
		roundt2.getStyle().setMarginLeft(1).setMarginTop(15);
		addfac = cp5.addButton("addfac").setPosition(width - 800 - 68, 0).setSize(68, 32).setLabel("+");
		addfac.getCaptionLabel().setFont(mono).getStyle().setPaddingTop(5);
		remfac = cp5.addButton("remfac").setPosition(width - 800 - 68, 36).setSize(68, 32).setLabel("-");
		remfac.getCaptionLabel().setFont(mono).getStyle().setPaddingTop(-3);

		tooltip2 = cp5.addLabel("tooltip2").setPosition(width - 800 - 68, 0).setSize(68, 68).setFont(monos)
				.setText("Rem Faction").setColor(color(150));
		tooltip2.getStyle().setPaddingLeft(-3).setPaddingTop(54);

		tooltip = cp5.addLabel("tooltip").setPosition(width - 800 - 68, 0).setSize(68, 68).setFont(monos)
				.setText("Add Faction").setColor(color(150));
		tooltip.getStyle().setPaddingLeft(-3).setPaddingTop(-3);
		start = cp5.addButton("start").setPosition(0, height - 67).setSize(width / 2, 68).setLabel("START!");
		start.getCaptionLabel().setFont(mono);
		mute = cp5.addButton("mute").setPosition(width / 2, height - 67).setSize(width / 4, 68).setLabel("Music: Off");
		mute.getCaptionLabel().setFont(mono);
		//DEPRECATED
		song = cp5.addButton("song").setPosition(width / 4 * 3, height - 67).setSize(width / 4, 68)
				.setLabel("Song: None");
		song.getCaptionLabel().setFont(mono);
		facselh = cp5.addTextlabel("facselh").setPosition(width - 800 + 5, 32).setSize(395, 41).setColor(color(150))
				.setText("Press to pull down a list of Factions, then click on a Faction.").setFont(monos);
		facselh.getStyle().setPaddingTop(15);
		defcolor = remfac.getColor().getActive();
		// int j = 1;
		addfac(0);
	}

	boolean transitioning = false;
	boolean goback = false;
	int defcolor;

	public void draw() {
		
		background(bgColor);
		pushMatrix();
		noStroke();
		noFill();
		strokeWeight(1);
		if (millis() % 500 == 0) {
			// System.out.print("Triggered.");
			if(f.h.size() < 50){
				f.addBoid(new FoodShow(this, width, (int) random(height), 2));
				f.addBoid(new FoodShow(this, (int) random(width), height, 2));
			}
			if(f.killer.size() < 10){
				f.addbea(new CellShow(this, (int) random(width), random(height)));
			}
			
		}
		fill(lerpColor(bgColor, color(0), 0.5f));
		f.run();
		f.eat();
		f.reproduce();
		popMatrix();
		facsel.bringToFront();
		if (active != null) {
			active.loop();
			if (facsel.isOpen()) {
				if (!goback && !transitioning)
					transitionstart(500, false);
				goback = true;
			} else if (goback) {
				if (goback && !transitioning)
					transitionstart(500, true);
				goback = false;
			}
		}

		if (transitioning) {
			transitionrun();
		}
		if (muteval){
			song.setLabel("Song: None");
		}else{	
			songindex = (songindex == -1) ? 0 : songindex;
			song.setLabel("Song: " + songlist[songindex]);
		}
		if (active != null && active.fenemys != null && !goback) {

			// for(int i = 0; i < factions.size(); i++)
			// System.out.print(factions.get(i).fname + " ");
			// System.out.println();

			// for(int i :active.fenemys)
			// System.out.print(factions.get(i).fname + " ");
			// System.out.println();

			for (int i = 0; i < active.fenemys.length; i++) {
				// System.out.println(((i * 600/fenemys.length) + 600) + " " +
				// (600/fenemys.length) );
				// float[] colors = factions.get(i).fcolors;
				noStroke();
				fill(factions.get(active.fenemys[i]).r.getValue(), factions.get(active.fenemys[i]).g.getValue(),
						factions.get(active.fenemys[i]).b.getValue());
				rect((float)(i * (480.0 / (active.fenemys.length))) + 720, 135, (float)(480.0 / (active.fenemys.length)), 122);
				fill(255);

			}
		}

		roundtassist.setText("Round Time: " + roundtimer.getValueLabel().getText() + " secs");

		facsel.setBackgroundColor(lerpColor(bgColor, color(0), 0.6f)).setColorBackground(color(0, 100))
				.setColorForeground(color(0, 200));// lerpColor(bgColor,
													// color(0), 0.8f)
		roundtimer.setColorBackground(0).setColorActive(lerpColor(bgColor, color(0), 0.4f))
				.setColorForeground(lerpColor(bgColor, color(0), 0.3f)).getValueLabel().setText(""+(float)Math.round(roundtimer.getValue())+"0");;

		if (factions.size() <= 13) {
			tooltip.setText("Add Faction");
			addfac.setColorBackground(color(0)).setColorForeground(lerpColor(bgColor, color(0), 0.95f))
					.setColorActive(defcolor);
		} else {
			tooltip.setText("Can't   Add");
			addfac.setColorBackground(color(200, 50, 50)).setColorForeground(color(200, 50, 50))
					.setColorActive(color(200, 50, 50));
		}
		if (factions.size() >= 2) {
			tooltip2.setText("Remove  Fac");
			remfac.setColorBackground(color(0)).setColorForeground(lerpColor(bgColor, color(0), 0.95f));
		} else {
			tooltip2.setText("Can't   Rem");
			remfac.setColorBackground(color(200, 50, 50)).setColorForeground(color(200, 50, 50))
					.setColorActive(color(200, 50, 50));
		}

		start.setColorBackground(color(0)).setColorForeground(lerpColor(bgColor, color(0), 0.95f));
		mute.setColorBackground(color(0)).setColorForeground(lerpColor(bgColor, color(0), 0.95f));
		song.setColorBackground(color(0)).setColorForeground(lerpColor(bgColor, color(0), 0.95f));
		if (n < 1) {
			bgColor = lerpColor(c1, c2, n);
		} else if (n < 2) {
			bgColor = lerpColor(c2, c3, n - 1);
		} else if (n < 3) {
			bgColor = lerpColor(c3, c1, n - 2);
		} else {
			n = 0;
		}
		n += 0.0005;
		// cur.setBackground(bgColor);

		// facsel.setColor(cur);
		pushMatrix();
		noStroke();
		line(0, height - 70, width, height - 70);

		fill(0);
		rect(0, 0, width, 73);
		rect(0, height - 73, width, 73);
		noFill();
		popMatrix();
		textSize(50);
		textFont(mono);
		fill(lerpColor(bgColor, color(255), 0.2f));
		text("Simulation Setup", 33, 45);
		stroke(255);
		strokeWeight(5);
		line(0, 70, width, 70);
		line(0, height - 70, width, height - 70);
		strokeWeight(10);
		line(width - 800 - 68, 0, width - 800 - 68, 67);
		line(width - 800, 0, width - 800, 67);
		line(width - 400, 0, width - 400, 67);
		strokeCap(SQUARE);
		line(width - 800 - 68, 32, width - 800, 32);
		strokeCap(ROUND);
		// line()
		// strokeWeight(5);

		/*
		 * textSize(20); text("Select Faction -->",400,60);
		 */
		facselh.bringToFront();
		facsel.getCaptionLabel().set((facsel.getCaptionLabel().getText().length() > 23)
				? facsel.getCaptionLabel().getText().substring(0, 20) + "..." : facsel.getCaptionLabel().getText());
		

	}

	public void mousePressed() {

		if (active != null && (mouseX >= 0 && mouseX <= 600)
				&& (mouseY >= (73 + 184 * 2 + 40) && mouseY <= ((73 + 184 * 2 + 40) + 147)) && !goback) {

			System.out.println(mouseX + " " + mouseY);
			active.facPos[0] = map(mouseX, 0, 600, 0, width);
			active.facPos[1] = map(mouseY, (73 + 184 * 2 + 40), ((73 + 184 * 2 + 40) + 147), 0, height);
		}

	}

	public void mouseDragged() {

		if (active != null && (mouseX >= 0 && mouseX <= 600)
				&& (mouseY >= (73 + 184 * 2 + 40) && mouseY <= ((73 + 184 * 2 + 40) + 147)) && !goback) {

			System.out.println(mouseX + " " + mouseY);
			active.facPos[0] = map(mouseX, 0, 600, 0, width);
			active.facPos[1] = map(mouseY, (73 + 184 * 2 + 40), ((73 + 184 * 2 + 40) + 147), 0, height);
		}

	}

	int startmil = 0;
	int length = 0;
	int endmil = 0;
	boolean notdone;
	boolean on;
	boolean midway;

	/**
	 * Starts a transition of the screen for graphical fidelity
	 * 
	 * @param length
	 *            Length of the total transition in milliseconds
	 */
	private void transitionstart(int length, boolean on) {
		transitioning = true;
		startmil = millis();
		this.length = length;
		notdone = true;
		this.on = on;
		endmil = startmil + length;
	}

	Background cv;

	private void transitionrun() {
		int point = millis() - startmil;
		int half = length / 2;
		cv.bringToFront();
		cv.setVisible(true);
		if (point <= half) {
			cv.setBackgroundColor(lerpColor(color(0, 1f), bgColor, ((float) point) / (float) half));
		} else if (point <= half * 2) {
			if (notdone) {
				if (!on)
					active.off();
				else
					active.on();
				notdone = false;
				
			}
			cv.setBackgroundColor(lerpColor(bgColor, color(0, 1f), (float) (point - half) / (float) half));
		} else {
			transitioning = false;
			cv.setVisible(false);
		}

	}

	public void controlEvent(ControlEvent e) {

		if (e.isController()) {
			println(e.getController().getValue() + " from " + e.getController());

			if (active != null && (e.getController().getAddress().equals("/" + active.fid + "-enemyList"))) {
				active.addEnemy(((FactionMenu) ((HashMap) ((ScrollableList) e.getController())
						.getItem((int) e.getController().getValue())).get("value")).fid);

				System.out.println();
			}

			if (e.getController().getName().equals("facsel")) {
				active.off();
				factions.get((int) e.getController().getValue()).on();
				active = factions.get((int) e.getController().getValue());
				active.on();
				facsel.close();
				System.out.println(active.fname);
			}

			if (active != null && e.isController() && e.getController().getName().equals(active.fid + "-Add")) {
				// System.out.println(active.fid);
				active.addAll();
			}
		}
		// if (e.isGroup()) {
		// // check if the Event was triggered from a ControlGroup
		// System.out.println(e.getGroup() + "HI");
		// if (e.getGroup().getName().equals("facsel")) {
		// Map<String, Object> fm = facsel.getItem((int) e.getValue());
		// System.out.println(fm);
		//
	}
	// }

	public void addfac(int val) {
		if (factions.size() < 14) {

			tooltip.setText("Add Faction");
			FactionMenu x = new FactionMenu(this, "Faction " + (factions.size() + 1), factions.size());

			start.getCaptionLabel().setText("START!");
			factions.add(x);
			facsel.addItem("Faction " + factions.size(), x.fid);
			// x.off();
			if (active == null) {
				x.on();

				active = x;
				facsel.getCaptionLabel().setText(x.fname);
				System.out.println(active.fname);
			} else {
				active.off();
				facsel.getCaptionLabel().setText(x.fname);
				active = x;
				x.on();
			}

			for (int i = 0; i < factions.size(); i++)
				System.out.println(factions.get(i).fid);

			facsel.close();
		} else {
			tooltip.setText("Can't  Add");
			addfac.setColorBackground(color(200, 0, 0)).setColorForeground(color(200, 0, 0));
		}
	}

	public void remfac(int val) {
		if (factions.size() > 1) {
			active.off();
			factions.remove(active);
			facsel.removeItem(active.fname);
			active = factions.get(active.fid - 1);
			facsel.getCaptionLabel().setText(active.fname);
			active.on();

		} else {
			tooltip.setText("Can't  Add");
			addfac.setColorBackground(color(200, 0, 0)).setColorForeground(color(200, 0, 0));
		}
	}

	public void facselt(int val) {

		if (facsel.isOpen()) {
			System.out.println("CALLER1");
			facsel.close();
		} else {
			System.out.println("CALLER2");
			facsel.open();
		}
	}

	boolean muteval = true;

	public void mute(int val) {
		muteval = !muteval;
		if (muteval){
			mute.setLabel("Music: Off");
			song.setLabel("Song: None");
		}else{
			mute.setLabel("Music: On");
			songindex = (songindex == -1)  ? 0 : songindex;
			song.setLabel("Song: " + songlist[songindex]);
		}
	}

	boolean videoval = false;
	
	String[] songlist = {"Spring", "Rhapsody", "Bumblebee", "Night", "Symphony", "Swift", "Wave", "Interim"};	
	int songindex = -1;
	
	public void song(int val) {
		System.out.println(muteval);
		if(!muteval){
			songindex++;
			if(songindex >= songlist.length){
				songindex = 0;
			}
			song.getCaptionLabel().setText("Song: " + songlist[songindex]);
		}
	}

	public void start(int val) {
		if (moveOn()) {
			destroy();
			frame.setVisible(false);
		} else {
			start.getCaptionLabel().setText("START - No Factions, Can't Start");
		}
	}

	public boolean moveOn() {
		if (factions.size() < 1) {
			return false;
		} else {
			float[][] fcolors = new float[factions.size()][3];
			int[][] fenemies = new int[factions.size()][];
			int[][] facst = new int[factions.size()][4];
			String[] fname = new String[factions.size()];
			float[][] facpos = new float[factions.size()][2];
			int[] facBNum = new int[factions.size()];
			for (int i = 0; i < factions.size(); i++) {
				factions.get(i).commit();
				fcolors[i] = factions.get(i).fcolors;
				fenemies[i] = factions.get(i).fenemys;
				facst[i] = factions.get(i).facst;
				fname[i] = factions.get(i).fname;
				facpos[i] = factions.get(i).facPos;
				facBNum[i] = factions.get(i).facBNum;

			}

			for (int i = 0; i < fenemies.length; i++)
				for (int t = 0; t < fenemies[i].length; t++)
					fenemies[i][t]++;

			for (int[] i : fenemies) {
				for (int j : i) {
					System.out.print(j + ",");
				}
				System.out.println("");
			}
			System.out.println(factions.size() + "," + fcolors.toString() + "," + fenemies.toString() + ","
					+ facst.toString() + "," + fname.toString() + "," + (int) roundtimer.getValue() + ","
					+ facpos.toString() + "," + facBNum.toString());
			runSketch(new String[] { "tsa.Simulation" }, new Simulation(factions.size(), fcolors, fenemies, facst,
					fname, (int) roundtimer.getValue(), facpos, facBNum, !muteval, songindex));
			destroy();
			frame.setVisible(false);
			// hahaha this is unreachable code but it doesn't know it :D
			return true;
		}
	}
}
