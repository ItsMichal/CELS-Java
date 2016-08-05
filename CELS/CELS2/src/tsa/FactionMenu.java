package tsa;

import controlP5.*;
import processing.core.PFont;
import processing.core.PGraphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class FactionMenu {

	/*
	 * Bounds 0,73 width, height-73*2 554/3 h184 w600
	 */

	// int numOfFacs, float[][] fcolors, int[][] fenemies, int[][] facst,
	// String[] fname, int stoptime , float[][] facPos , int[] facBNum
	public float[] fcolors = { 0f, 0f, 0f }; // DONE
	public int[] fenemys; // BROKEN ATM
	public int[] facst; // DONE
	public String fname; // DONE
	public float[] facPos; // DONE
	public int facBNum; // DONE

	public int fid;

	Group allitems;

	PFont small;
	PFont large;

	SelectionMenu sm;
	ControlP5 cp5;
	// one
	Textlabel fct;
	Textlabel fctt;
	Background s1;
	Slider r;
	Slider g;
	Slider b;
	Background rgb;

	// three
	Background traits_bg;
	Textlabel four_exp;
	Slider speed;
	Slider maxsize;
	Slider metabolism;
	Slider finesse;
	Textlabel four_label;
	Textlabel speed_label;
	Textlabel maxsize_label;
	Textlabel metabolism_label;
	Textlabel finesse_label;
	Textlabel fitness_exp;

	// four
	Textlabel nmt;
	Textfield fnamei;
	Background name_bg;

	// two
	Textlabel enemys;
	ScrollableList enm;
	Button all;
	Background enemy_bg;
	Button add;

	// five
	Canvas posC;
	Background xy_bg;
	Textlabel five_label;
	Background posBack;
	//Background pos;
	
	// six
	Textlabel num;
	Slider facNum;
	Background num_bg;
	private Textlabel enemy_exp;
	private Textlabel one_exp;
	private Textlabel name_exp;
	private Textlabel xy_exp;
	private Textlabel num_exp;

	// String oldName;

	public FactionMenu(SelectionMenu x, String name, int fid) {
		sm = x;
		facPos = new float[2];
		facPos[0] = sm.width / 2;
		facPos[1] = sm.height / 2;
		cp5 = x.cp5;
		fname = name;
		this.fid = fid;
		small = sm.monos;
		large = sm.monol;
		create();
	}

	// 400,460,150,150
	//

	public void create() {
		allitems = cp5.addGroup(fname + "-allitems");
		s1 = cp5.addBackground(fname + "-ret").setSize(600, 184).setPosition(0, 73).setGroup(allitems);
		fct = cp5.addTextlabel(fname + "-cptl").setText("Color").setPosition(0, 73).setColorValue(sm.color(255))
				.setGroup(allitems);
		one_exp = cp5.addTextlabel(fid + "-one_exp").setText("Drag RGB Sliders to Select Color, Preview it below.").setPosition(85, 73+32-14)
				.setGroup(allitems);
		one_exp.setFont(sm.monos).setColor(sm.color(155));
		fct.setFont(sm.mono);
		r = cp5.addSlider(fname + "-r").setPosition(0, 109).setSize(600, 37).setColorBackground(sm.color(100, 0, 0))
				.setGroup(allitems).setColorForeground(sm.color(200, 0, 0)).setColorActive(sm.color(150, 0, 0))
				.setColorValue(sm.color(255)).setMax(255).setMin(0).setLabel("");
		r.getValueLabel().setFont(small).alignX(ControlP5.CENTER);
		g = cp5.addSlider(fname + "-g").setPosition(0, 146).setSize(600, 37).setColorBackground(sm.color(0, 100, 0))
				.setGroup(allitems).setColorForeground(sm.color(0, 200, 0)).setColorActive(sm.color(0, 150, 0))
				.setColorValue(sm.color(255)).setMax(255).setMin(0).setLabel("");
		g.getValueLabel().setFont(small).alignX(ControlP5.CENTER);
		b = cp5.addSlider(fname + "-b").setPosition(0, 183).setSize(600, 37).setColorBackground(sm.color(0, 0, 100))
				.setGroup(allitems).setColorForeground(sm.color(0, 0, 200)).setColorActive(sm.color(0, 0, 150))
				.setColorValue(sm.color(255)).setMax(255).setMin(0).setLabel("");
		b.getValueLabel().setFont(small).alignX(ControlP5.CENTER);
		rgb = cp5.addBackground(fname + "-rgb").setPosition(0, 220).setSize(600, 37).setGroup(allitems)
				.setBackgroundColor(sm.color(r.getValue(), g.getValue(), b.getValue()));
		rgb.getValueLabel().setFont(small).setText("FINAL COLOR").alignX(ControlP5.CENTER).getStyle().setPaddingTop(8);
		// block
		// two----------------------------------------------------------------------------
		
		enemy_bg = cp5.addBackground(fid + "-enemy_bg").setSize(600, 40).setPosition(600,73)
				.setGroup(allitems);
		enemy_exp = cp5.addTextlabel(fid + "-enemy_exp").setText("Drop Down to Select Enemy, or Add All").setPosition(600+115, 73+32-14)
				.setGroup(allitems);
		enemy_exp.setFont(sm.monos).setColor(sm.color(155));
		enemys = cp5.addTextlabel(fid + "-enemys").setText("Enemies").setPosition(600, 73).setGroup(allitems);

		enemys.setFont(sm.mono);

		enm = cp5.addScrollableList(fid + "-enemyList").setPosition(720, 109).setSize(480, 148).setBarHeight(34)
				.setGroup(allitems).setItemHeight(19);
		enm.getCaptionLabel().setFont(small).alignX(ControlP5.CENTER);
		enm.getValueLabel().setFont(small).getStyle().setPaddingTop(3);
		enm.getCaptionLabel().set("Click To Here Select Another Faction").setColor(sm.color(255)).getStyle()
				.setPaddingTop(3);
		enm.setBackgroundColor(sm.color(255, 50, 50)).setColorBackground(sm.color(255, 50, 50))
				.setColorForeground(sm.color(200, 20, 20)).setColorCaptionLabel(255);
		enm.close();
		all = cp5.addButton(fid + "-Add").setPosition(600, 109).setSize(120, 147)
				.setColorBackground(sm.color(200, 50, 50)).setGroup(allitems).setColorForeground(sm.color(150, 10, 10))
				.setColorCaptionLabel(255);
		all.getCaptionLabel().setText("ADD\nALL").setFont(sm.mono).setColor(255).getStyle().setPaddingTop(-13);

		// ALL
		sm.facsel.bringToFront();

		// three---------height 74 width 300
		
		
		traits_bg = cp5.addBackground(fid + "-traitss_bg").setSize(600, 184).setPosition(0, 72 + 184)
				.setGroup(allitems);
		four_label = cp5.addTextlabel(fid + "-four_label").setText("Traits").setPosition(0, 73 + 184)
				.setGroup(allitems);
		four_label.setFont(sm.mono);
		four_exp = cp5.addTextlabel(fid + "-four_exp").setText("Drag Sliders to Select Value").setPosition(100, 73+32+184-14)
				.setGroup(allitems);
		four_exp.setFont(sm.monos).setColor(sm.color(155));
		speed = cp5.addSlider(fid + "-speed").setPosition(0, 73 + 184 + 36).setSize(300, 74).setMin(1).setMax(20)
				.setValue(5).setNumberOfTickMarks(20).showTickMarks(false).setGroup(allitems);
		speed.getValueLabel().setFont(sm.monom).alignX(ControlP5.RIGHT);
		speed.getCaptionLabel().set("");
		speed_label = cp5.addTextlabel(fid + "-speed_label").setPosition(0, 73 + 184 + 36 + 20).setSize(300, 74 - 20)
				.setText("Speed").setFont(sm.monom).setGroup(allitems);
		speed_label.getValueLabel().setPaddingX(5);// .setPaddingX(110);

		maxsize = cp5.addSlider(fid + "-maxsize").setPosition(300, 73 + 184 + 36).setSize(300, 74).setMin(1).setMax(100)
				.setValue(30).setNumberOfTickMarks(100).showTickMarks(false).setGroup(allitems);
		maxsize.getValueLabel().setFont(sm.monom).alignX(ControlP5.RIGHT);
		maxsize.getCaptionLabel().set("");
		maxsize_label = cp5.addTextlabel(fid + "-maxsize_label").setPosition(300, 73 + 184 + 36 + 20)
				.setSize(300, 74 - 20).setText("Max. Size").setFont(sm.monom).setGroup(allitems);
		maxsize_label.getValueLabel().setPaddingX(5);// .setPaddingX(85);

		metabolism = cp5.addSlider(fid + "-metabolism").setPosition(0, 73 + 184 + 36 + 74).setSize(300, 74).setMin(1)
				.setMax(200).setValue(55).setNumberOfTickMarks(200).showTickMarks(false).setGroup(allitems);
		metabolism.getValueLabel().setFont(sm.monom).alignX(ControlP5.RIGHT);
		metabolism.getCaptionLabel().set("");
		metabolism_label = cp5.addTextlabel(fid + "-metabolism_label").setPosition(0, 73 + 184 + 36 + 20 + 74)
				.setSize(300, 74 - 20).setText("Hunger Rate").setFont(sm.monom).setGroup(allitems);
		metabolism_label.getValueLabel().setPaddingX(5);// .setPaddingX(75);

		finesse = cp5.addSlider(fid + "-finesse").setPosition(300, 73 + 184 + 36 + 74).setSize(300, 74).setMin(1)
				.setMax(200).setValue(55).setNumberOfTickMarks(200).showTickMarks(false).setGroup(allitems);
		finesse.getValueLabel().setFont(sm.monom).alignX(ControlP5.RIGHT);
		finesse.getCaptionLabel().set("");
		fitness_exp = cp5.addTextlabel(fid + "-fitness_exp").setPosition(300, 73 + 184 * 2 - 17).setSize(300, 74 - 20)
				.setText("Fitness is a combination of Sight and Finesse").setFont(sm.monos).setColor(sm.color(150))
				.setGroup(allitems);
		fitness_exp.getValueLabel().setPaddingX(5);
		finesse_label = cp5.addTextlabel(fid + "-finesse_label").setPosition(300, 73 + 184 + 36 + 20 + 74)
				.setSize(300, 74 - 20).setText("Fitness").setFont(sm.monom).setGroup(allitems);
		finesse_label.getValueLabel().setPaddingX(5);

		// four----------
		
		fnamei = cp5.addTextfield(fid + "name").setPosition(601, 293).setSize(600, 148).setGroup(allitems)
				.setColorBackground(sm.lerpColor(sm.bgColor, sm.color(0), 0.2f)).setColorForeground(sm.lerpColor(sm.bgColor, sm.color(0), 0.2f)).setLabel("")
				.setValueLabel("" + fid).setAutoClear(false);
		// fnamei.get
		fnamei.getValueLabel().setFont(large).alignY(Textfield.CENTER).alignX(Textfield.LEFT);
		fnamei.setText(fname);
		name_bg = cp5.addBackground(fid + "-name_bg").setPosition(600, 256).setSize(600, 38).setGroup(allitems);
		name_exp = cp5.addTextlabel(fid + "-name_exp").setText("Click to select, then type in a name below 24 characters.").setPosition(600+70, 73+32+184-14)
				.setGroup(allitems);
		name_exp.setFont(sm.monos).setColor(sm.color(155));
		nmt = cp5.addTextlabel(fname + "-nmttl").setText("Name").setPosition(600, 256).setColorValue(sm.color(255))
				.setGroup(allitems)

		;
		nmt.setFont(sm.mono);
		// five-------

		xy_bg = cp5.addBackground(fid + "-traits_bg").setSize(600, 41).setPosition(0, 73 + 184 * 2).setGroup(allitems);
		five_label = cp5.addTextlabel(fid + "-five_label").setPosition(0, 73 + 184 * 2).setGroup(allitems)
				.setText("Starting Position");
		xy_exp = cp5.addTextlabel(fid + "-xy_exp").setText("Drag the dot to select, other factions shown for help").setPosition(275, 73+32-14+184*2)
				.setGroup(allitems);
		xy_exp.setFont(sm.monos).setColor(sm.color(155));
	
		five_label.setFont(sm.mono);
		//posBack = cp5.addBackground(fid + "back").setPosition(0,(73+184*2+40)).setSize(600, 147).setGroup(allitems);
		//pos = cp5.addBackground(fid + "pos").setPosition(300,(73+184*2+40)/2).setSize(10, 10).setGroup(allitems);
		
		/*cp5.addCanvas(new Canvas() {

			@Override
			public void draw(PGraphics arg0) {
				sm.background(sm.color(0, 0));
				sm.fill(255);
				sm.rect(0, 445, 600, 177);
				sm.noStroke();
				for (int i = 0; i < sm.factions.size(); i++) {
					sm.fill(sm.factions.get(i).r.getValue(), sm.factions.get(i).g.getValue(),
							sm.factions.get(i).b.getValue());
					sm.ellipse(sm.map(sm.factions.get(i).facPos[0], 0, sm.width, 0, 600),
							(73 + 184 * 2 + 40) + sm.map(sm.factions.get(i).facPos[1], 0, sm.height, 0, 147), 10, 10);// TODO
																														// Auto-generated
																														// method
																														// stub

				}
			}
		});*/
		
		
		// six---------------------------------------------------------------------------------------------------------
		num_bg = cp5.addBackground(fname + "-num_bg").setSize(600, 184).setPosition(600, 441).setGroup(allitems);
		num = cp5.addTextlabel(fid + "-nusm").setGroup(allitems).setPosition(600, 441)
				.setText("Starting Amount of Cells").setColorValue(sm.color(255)).setSize(600, 184);
		num.setFont(sm.mono);
		
		facNum = cp5.addSlider(fname + "-facNum").setPosition(600, 481).setSize(600, 146).setNumberOfTickMarks(50).setMin(1).setMax(50).showTickMarks(false)
				.setGroup(allitems);

		facNum.getValueLabel().setFont(sm.monol).align(ControlP5.CENTER, ControlP5.CENTER);
		facNum.setValue(5);
		num_exp = cp5.addTextlabel(fid + "-num_exp").setText("Drag Slider to Select Value").setPosition(385+600, 73+32-14+184*2)
				.setGroup(allitems);
		num_exp.setFont(sm.monos).setColor(sm.color(155));
		//extra
		facNum.setColorBackground(sm.lerpColor(sm.bgColor, sm.color(0), 0.2f))
				.setColorActive(sm.lerpColor(sm.bgColor, sm.color(0), 0.7f))
				.setColorForeground(sm.lerpColor(sm.bgColor, sm.color(0), 0.6f));
		speed.setColorBackground(sm.lerpColor(sm.bgColor, sm.color(0), 0.2f))
				.setColorActive(sm.lerpColor(sm.bgColor, sm.color(0), 0.7f))
				.setColorForeground(sm.lerpColor(sm.bgColor, sm.color(0), 0.6f));
		maxsize.setColorBackground(sm.lerpColor(sm.bgColor, sm.color(0), 0.2f))
				.setColorActive(sm.lerpColor(sm.bgColor, sm.color(0), 0.7f))
				.setColorForeground(sm.lerpColor(sm.bgColor, sm.color(0), 0.6f));
		metabolism.setColorBackground(sm.lerpColor(sm.bgColor, sm.color(0), 0.2f))
				.setColorActive(sm.lerpColor(sm.bgColor, sm.color(0), 0.7f))
				.setColorForeground(sm.lerpColor(sm.bgColor, sm.color(0), 0.6f));
		finesse.setColorBackground(sm.lerpColor(sm.bgColor, sm.color(0), 0.2f))
				.setColorActive(sm.lerpColor(sm.bgColor, sm.color(0), 0.7f))
				.setColorForeground(sm.lerpColor(sm.bgColor, sm.color(0), 0.6f));
	}

	public void loop() {
		sm.facsel.bringToFront();
		/*
		 * xpos.setColorBackground(sm.lerpColor(sm.bgColor, sm.color(0), 0.2f))
		 * .setColorActive(sm.lerpColor(sm.bgColor, sm.color(0), 0.7f))
		 * .setColorForeground(sm.lerpColor(sm.bgColor, sm.color(0), 0.6f));
		 */
		/*
		 * ypos.setColorBackground(sm.lerpColor(sm.bgColor, sm.color(0), 0.2f))
		 * .setColorActive(sm.lerpColor(sm.bgColor, sm.color(0), 0.7f))
		 * .setColorForeground(sm.lerpColor(sm.bgColor, sm.color(0), 0.6f));
		 */
		fnamei.setColorBackground(sm.lerpColor(sm.bgColor, sm.color(0), 0.2f))
		.setColorActive(sm.lerpColor(sm.bgColor, sm.color(0), 0.2f))
		.setColorForeground(sm.lerpColor(sm.bgColor, sm.color(0), 0.2f));//.setColorValueLabel(sm.lerpColor(sm.bgColor, sm.color(0), 0.6f));
		facNum.setColorBackground(sm.lerpColor(sm.bgColor, sm.color(0), 0.2f))
				.setColorActive(sm.lerpColor(sm.bgColor, sm.color(0), 0.7f))
				.setColorForeground(sm.lerpColor(sm.bgColor, sm.color(0), 0.6f))
				.getValueLabel().setText(""+(float)Math.round(facNum.getValue())+"0");
		speed.setColorBackground(sm.lerpColor(sm.bgColor, sm.color(0), 0.2f))
				.setColorActive(sm.lerpColor(sm.bgColor, sm.color(0), 0.7f))
				.setColorForeground(sm.lerpColor(sm.bgColor, sm.color(0), 0.6f))
				.getValueLabel().setText(""+(float)Math.round(speed.getValue())+"0");
		maxsize.setColorBackground(sm.lerpColor(sm.bgColor, sm.color(0), 0.2f))
				.setColorActive(sm.lerpColor(sm.bgColor, sm.color(0), 0.7f))
				.setColorForeground(sm.lerpColor(sm.bgColor, sm.color(0), 0.6f))
			.getValueLabel().setText(""+(float)Math.round(maxsize.getValue())+"0");
		metabolism.setColorBackground(sm.lerpColor(sm.bgColor, sm.color(0), 0.2f))
				.setColorActive(sm.lerpColor(sm.bgColor, sm.color(0), 0.7f))
				.setColorForeground(sm.lerpColor(sm.bgColor, sm.color(0), 0.6f))
				.getValueLabel().setText(""+(float)Math.round(metabolism.getValue())+"0");
		finesse.setColorBackground(sm.lerpColor(sm.bgColor, sm.color(0), 0.2f))
				.setColorActive(sm.lerpColor(sm.bgColor, sm.color(0), 0.7f))
				.setColorForeground(sm.lerpColor(sm.bgColor, sm.color(0), 0.6f))
				.getValueLabel().setText(""+(float)Math.round(finesse.getValue())+"0");

		rgb.setBackgroundColor(sm.color(r.getValue(), g.getValue(), b.getValue()));
		rgb.getValueLabel().setColor(sm.color(255 - r.getValue(), 255 - g.getValue(), 255 - b.getValue()));
		// enm.setBackgroundColor(sm.bgColor).setColorBackground(sm.bgColor)
		// .setColorForeground(sm.lerpColor(sm.bgColor, 100,
		// 0.5f)).setColorCaptionLabel(255).clear();
		//fnamei.
		fnamei.setText((fnamei.getText().length() < 24) ? fnamei.getText() : fnamei.getText().substring(0, 24));
		if (!fname.equals(fnamei.getValueLabel().getText())) {
			// sm.facsel.getItem(fid - 1).;
			// fname = fnamei.getStringValue();
			((HashMap) sm.facsel.getItems().get(fid)).remove("name");
			((HashMap) sm.facsel.getItems().get(fid)).put("name", fnamei.getValueLabel().getText());
			((HashMap) sm.facsel.getItems().get(fid)).remove("text");
			((HashMap) sm.facsel.getItems().get(fid)).put("text", fnamei.getValueLabel().getText());
			sm.facsel.getCaptionLabel().setText(fnamei.getValueLabel().getText());
			fname = fnamei.getValueLabel().getText();
			System.out.println(((HashMap) sm.facsel.getItems().get(fid)));
		}
		for (int i = 0; i < sm.factions.size(); i++) {
			FactionMenu n = sm.factions.get(i);
			boolean f = true;
			for (int j = 0; j < enm.getItems().size(); j++) {
				// System.out.println(((HashMap) enm.getItems().get(j)));
				if (((HashMap) enm.getItems().get(j)).get("value") == n)
					f = false;
			}
			if (n != this && f) {

				enm.addItem(n.fname, n);
			}
		}

		/*
		 * if(fenemys != null) for(int s: fenemys) System.out.print(s + ",");
		 * 
		 * System.out.println();
		 */
		if(!sm.goback && ((sm.on) ? (!sm.notdone) : (sm.notdone) || !sm.transitioning)){
			sm.fill(sm.lerpColor(sm.bgColor, sm.color(0), 0.2f));
			sm.rect(600,110, 600, 147);	
			sm.rect(0,481,600,151);
			sm.noStroke();
		for(int i = 0; i < sm.factions.size(); i++){
			if(sm.factions.get(i) == this){
				sm.fill(sm.factions.get(i).r.getValue(),sm.factions.get(i).g.getValue(),sm.factions.get(i).b.getValue());	
			}else{
				sm.fill(sm.map(sm.factions.get(i).r.getValue(), 0, 255, 0, 175),sm.map(sm.factions.get(i).g.getValue(),0,255,0,175),sm.map(sm.factions.get(i).b.getValue(), 0, 255, 0,175));	
			}
			//sm.fill(sm.factions.get(i).r.getValue(),sm.factions.get(i).g.getValue(),sm.factions.get(i).b.getValue());	
			//posBack.draw(sm.getGraphics());
			sm.ellipse(sm.map(sm.factions.get(i).facPos[0],0,sm.width,0,600), (73+184*2+40) + sm.map(sm.factions.get(i).facPos[1],0,sm.height,0,147), 10, 10);
			
		}
		}
	}

	public void on() {
		/*
		 * fct.setVisible(true); fctt.setVisible(true); r.setVisible(true);
		 * g.setVisible(true); b.setVisible(true); rgb.setVisible(true);
		 * s1.setVisible(true); enemys.setVisible(true); enm.setVisible(true);
		 * nmt.setVisible(true); fnamei.setVisible(true);
		 */
		allitems.show();
		for (int i = 0; i < enm.getItems().size(); i++) {
			enm.removeItem((String) ((HashMap) enm.getItems().get(i)).get("name"));
		}

		for (int i = 0; i < sm.factions.size(); i++) {
			FactionMenu n = sm.factions.get(i);
			boolean f = true;
			for (int j = 0; j < enm.getItems().size(); j++) {
				System.out.println(((HashMap) enm.getItems().get(j)));
				if (((HashMap) enm.getItems().get(j)).get("value") == n)
					f = false;
			}
			if (n != this && f) {
				enm.addItem(n.fname, n);
			}
		}
	}

	public void off() {
		/*
		 * enemys.setVisible(false); fct.setVisible(false);
		 * fctt.setVisible(false); r.setVisible(false); g.setVisible(false);
		 * b.setVisible(false); rgb.setVisible(false); s1.setVisible(false);
		 * enm.setVisible(false); nmt.setVisible(false);
		 * fnamei.setVisible(false);
		 */

		allitems.hide();

	}

	public void addEnemy(int en) {

		System.out.println(sm.factions.get(en).fid);

		if (sm.factions.get(en).fid != this.fid) {

			if (fenemys != null) {

				for (int i : fenemys)
					System.out.print(sm.factions.get(i).fid + " ");
				System.out.println();

				boolean b = true;

				for (int e = 0; e < fenemys.length; e++)
					if (fenemys[e] == en)
						b = false;

				if (b) {
					int[] buf = fenemys;
					fenemys = new int[buf.length + 1];

					for (int i = 0; i < buf.length; i++)
						fenemys[i] = buf[i];

					fenemys[fenemys.length - 1] = en;
				}
			} else {

				fenemys = new int[1];
				fenemys[0] = en;

			}
		}

	}

	public void addAll() {
		// System.out.println(fname);
		for (int i = 0; i < sm.factions.size(); i++)
			addEnemy(i);

	}

	public void commit() {
		// TODO: commit all current values to fields
		fcolors = new float[] { r.getValue(), g.getValue(), b.getValue() };
		facst = new int[] { (int) speed.getValue(), (int) maxsize.getValue(), (int) metabolism.getValue(),
				(int) finesse.getValue() };
		// facPos = new float[] { xpos.getValue(), ypos.getValue() };
		facBNum = (int) facNum.getValue();
		if (fenemys == null) {
			fenemys = new int[] {};
		}
	}
}

