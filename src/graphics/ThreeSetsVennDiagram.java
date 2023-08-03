/*      
 * Fileame: ThreeSetsVennDiagram.java
 *  
 * Author: Dhrubo Jyoti
 *         Class of 2011
 *         HB 2313, Dartmouth College
 *         Hanover, NH 03755, USA
 *         email: Dhrubo.Jyoti@Dartmouth.edu
 *
 * Date:   	June 9, 2008
 * Version:	5.0
 *
 * License:        BSD
 * This product includes software developed by Dartmouth College. 
 * Copyright (c) 2008, Trustees, Dartmouth College. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions 
 * are met:
 * - Redistributions of source code must retain the above copyright notice, this list of  
 * conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the distribution.
 * - Neither the name of Dartmouth College nor the names of its contributors may be  
 * used to endorse or promote products derived from this software 
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS 
 * FOR A PARTICULAR PURPOSE ARE EXPRESSLY DISCLAIMED. IN NO EVENT SHALL 
 * DARTMOUTH COLLEGE OR ITS EMPLOYEES BE LIABLE FOR DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS 
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED 
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT 
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF 
 * SUCH DAMAGE.
 */

package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.border.LineBorder;

public class ThreeSetsVennDiagram extends VennDiagram {
	private static final long serialVersionUID = 1L;

	// diagram info
	public static final int NO_OF_SETS = 3;
	public static final int NO_OF_INTERSECTIONS = 4;
	public static final int NO_OF_BASIC_REGIONS = NO_OF_SETS
			+ NO_OF_INTERSECTIONS + 1;
	public static final int NO_OF_THICK_BORDERS = 9;
	public static final int NO_OF_REGIONS = NO_OF_BASIC_REGIONS
			+ NO_OF_THICK_BORDERS;
	private String[] nameOfSet = new String[NO_OF_SETS];
	private String diagramTitle;

	// used for drawing only
	private Area[] set = new Area[NO_OF_SETS];
	private Area[] intersection = new Area[NO_OF_INTERSECTIONS];
	private Area remainder;

	// the three "pure" set regions minus thick borders
	private Area area1 = null;
	private Area area2 = null;
	private Area area3 = null;

	// used for thick borders only
	private Area area14 = null;
	private Area area16 = null;
	private Area area67 = null;
	private Area area47 = null;
	private Area area24 = null;
	private Area area25 = null;
	private Area area57 = null;
	private Area area36 = null;
	private Area area35 = null;
	private Area areaInt0 = null;
	private Area areaInt1 = null;
	private Area areaInt2 = null;
	private Area areaInt3 = null;

	private int WIDTH = 5;

	// all the regions mapped by region number
	private Map<Integer, Area> regions = new HashMap<Integer, Area>();
	// the status for all regions. 0 if default, 1 if selected, -1 if excluded.
	private Map<Integer, Integer> regionStatus = new HashMap<Integer, Integer>();
	// all the drawing regions mapped by region number
	private Map<Integer, Area> drawingRegions = new HashMap<Integer, Area>();

	// boolean and int that tells us if and which region to update after mouse
	// clicks
	boolean isInitialized = false;
	boolean needUpdate = false;
	int regionToUpdate = 0;

	// boolean that says whether to clear area before putting some
	boolean eraseAreaFirst = false;

	/**
	 * This constructor is a convenience for creating user interaction diagrams
	 */
	public ThreeSetsVennDiagram(String[] nameOfSet, int[] regionStatusInfo,
			String diagramTitle) {
		this(nameOfSet, regionStatusInfo, diagramTitle, true);
	}

	/**
	 * Creates a Venn Diagram using the names of sets and status of each region
	 */
	public ThreeSetsVennDiagram(String[] nameOfSet, int[] regionStatusInfo,
			String diagramTitle, boolean isInteractive) {
		this.setBorder(new LineBorder(Color.BLACK));
		this.setBackground(BACKGROUND_COLOR);
		this.nameOfSet = new String[nameOfSet.length];
		this.setNamesOfSets(nameOfSet);
		this.diagramTitle = diagramTitle;

		if (isInteractive) {
			this.addMouseListener(this);
		}

		// initialize region status to get "empty" diagram
		for (int i = 0; i < NO_OF_REGIONS; i++) {
			this.regionStatus.put(i, 0);
		}

		// now update the diagram with regions that are "marked"
		for (int i = 0; i < regionStatusInfo.length; i++) {
			int regionNo = 0;
			if (regionStatusInfo[i] > 10) {
				switch (regionStatusInfo[i]) {
				case 14:
					regionNo = 8;
					break;
				case 24:
					regionNo = 9;
					break;
				case 25:
					regionNo = 10;
					break;
				case 35:
					regionNo = 11;
					break;
				case 36:
					regionNo = 12;
					break;
				case 16:
					regionNo = 13;
					break;
				case 47:
					regionNo = 14;
					break;
				case 57:
					regionNo = 15;
					break;
				case 67:
					regionNo = 16;
					break;
				}
			} else {
				regionNo = Math.abs(regionStatusInfo[i]) - 1;
				if (regionNo < 0) {
					continue;
				}
			}
			this.regionStatus
					.put(regionNo, Integer.signum(regionStatusInfo[i]));
		}
	}

	public void paintStar(Area area, Graphics2D graphics, Color color) {
		int x = (int) area.getBounds().getCenterX();
		int y = (int) area.getBounds().getCenterY() + 5;
		if (eraseAreaFirst) {
			graphics.setColor(DEFAULT_COLOR);
			graphics.fill(area);
			eraseAreaFirst = false;
		}
		graphics.setColor(color);
		switch (regionToUpdate) {
		case 0:
			graphics.drawString("\u2733", x - 15, y - 10);
			break;
		case 1:
			graphics.drawString("\u2733", x + 5, y - 10);
			break;
		case 2:
			graphics.drawString("\u2733", x - 5, y + 2);
			break;
		case 3:
			graphics.drawString("\u2733", x - 5, y + 5);
			break;
		case 4:
			graphics.drawString("\u2733", x + 10, y + 8);
			break;
		case 5:
			graphics.drawString("\u2733", x - 18, y + 7);
			break;
		case 6:
			graphics.drawString("\u2733", x - 5, y - 3);
			break;
		case 7:
			graphics.drawString("\u2733", 10, 260);
			break;
		case 8:
			graphics.drawString("\u2733", x - 13, y + 1);
			break;
		case 9:
			graphics.drawString("\u2733", x + 4, y + 1);
			break;
		case 10:
			graphics.drawString("\u2733", x + 22, y - 3);
			break;
		case 11:
			graphics.drawString("\u2733", x + 7, y + 24);
			break;
		case 12:
			graphics.drawString("\u2733", x - 5, y + 7);
			break;
		case 13:
			graphics.drawString("\u2733", x - 31, y - 3);
			break;
		case 14:
			graphics.drawString("\u2733", x - 5, y - 2);
			break;
		case 15:
			graphics.drawString("\u2733", x - 1, y + 1);
			break;
		case 16:
			graphics.drawString("\u2733", x - 8, y + 1);
			break;
		}
	}

	public void repaint(Graphics2D graphics) {
		graphics.setClip(universeShape);
		int status = regionStatus.get(new Integer(regionToUpdate));
		Area area = drawingRegions.get(new Integer(regionToUpdate));
		if (status == 0 && regionToUpdate < 8) {
			graphics.setColor(DEFAULT_COLOR);
			graphics.fill(area);
		} else if (status == 0 && regionToUpdate >= 8) {
			paintStar(area, graphics, DEFAULT_COLOR);
			Set<Integer> rs = regionStatus.keySet();
			for (Integer i : rs) {
				if (regionStatus.get(i) == -1) {
					Area a = drawingRegions.get(i);
					graphics.setColor(EXCLUSION_COLOR);
					graphics.fill(a);
				}
			}
		} else if (status == -1 && regionToUpdate < 8) {
			graphics.setColor(EXCLUSION_COLOR);
			graphics.fill(area);
		} else if (status == 1) {
			paintStar(area, graphics, BORDER_COLOR);
			if (regionToUpdate < 8) {
				graphics.setColor(BORDER_COLOR);
				graphics.draw(area);
			}
		}

		// now repaint the "border some"-s
		graphics.setColor(BORDER_COLOR);
		for (int i = 8; i < 17; i++) {
			if (regionStatus.get(new Integer(i)) == 1) {
				Area a = drawingRegions.get(new Integer(i));
				regionToUpdate = i;
				paintStar(a, graphics, BORDER_COLOR);
			}
		}

		graphics.setColor(BORDER_COLOR);
		graphics.draw(set[0]);
		graphics.draw(set[1]);
		graphics.draw(set[2]);
		graphics.draw(intersection[3]);
		graphics.draw(universeShape);

		regionToUpdate = 0;
		needUpdate = false;
		return;
	}

	public void paint(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		graphics.addRenderingHints(hints2);
		graphics.setStroke(STROKE);

		// this if block is used for user mouse clicks after initialization
		if (isInitialized && needUpdate) {
			repaint(graphics);
			return;
		}

		universeShape.setFrame(0, 40, 240, 230);
		remainder = new Area(universeShape);
		graphics.draw(universeShape);

		// draw title
		int centerX = 120 - (int) ((diagramTitle.length() / 2) * 6);
		graphics.drawChars(diagramTitle.toCharArray(), 0,
				diagramTitle.length(), centerX, 20);

		// draw names of first two sets
		graphics.drawChars(nameOfSet[0].toCharArray(), 0,
				nameOfSet[0].length(), 0, 35);
		graphics.drawChars(nameOfSet[1].toCharArray(), 0,
				nameOfSet[1].length(), 150, 35);

		// Draw the first set
		setShape.setFrame(5, 45, 140, 140);
		set[0] = new Area(setShape);

		// Draw the second set
		setShape.setFrame(95, 45, 140, 140);
		set[1] = new Area(setShape);

		// Draw the third set
		setShape.setFrame(50, 125, 140, 140);
		set[2] = new Area(setShape);

		// draw name of the third set
		graphics.drawChars(nameOfSet[2].toCharArray(), 0,
				nameOfSet[2].length(), 125 - ((nameOfSet[2].length() * 7) / 2),
				290);

		// now "make the borders thick" by defining new areas
		// first for the first set
		setShape.setFrame(5 - WIDTH, 45 - WIDTH, 140 + 2 * WIDTH,
				140 + 2 * WIDTH);
		Area strip = new Area(setShape);
		setShape.setFrame(5 + WIDTH, 45 + WIDTH, 140 - 2 * WIDTH,
				140 - 2 * WIDTH);
		Area innerCircle = new Area(setShape);
		strip.subtract(innerCircle);

		area24 = new Area(strip);
		area24.intersect(set[1]);

		area36 = new Area(strip);
		area36.intersect(set[2]);

		area57 = new Area(area24);
		area57.intersect(area36);

		area24.subtract(area57);
		area36.subtract(area57);

		// similarly, for the second set
		setShape.setFrame(95 - WIDTH, 45 - WIDTH, 140 + 2 * WIDTH,
				140 + 2 * WIDTH);
		strip = new Area(setShape);
		setShape.setFrame(95 + WIDTH, 45 + WIDTH, 140 - 2 * WIDTH,
				140 - 2 * WIDTH);
		innerCircle = new Area(setShape);
		strip.subtract(innerCircle);

		area14 = new Area(strip);
		area14.intersect(set[0]);

		area35 = new Area(strip);
		area35.intersect(set[2]);

		area67 = new Area(area14);
		area67.intersect(area35);

		area14.subtract(area67);
		area35.subtract(area67);

		// again, for the 3rd set
		setShape.setFrame(50 - WIDTH, 125 - WIDTH, 140 + 2 * WIDTH,
				140 + 2 * WIDTH);
		strip = new Area(setShape);
		setShape.setFrame(50 + WIDTH, 125 + WIDTH, 140 - 2 * WIDTH,
				140 - 2 * WIDTH);
		innerCircle = new Area(setShape);
		strip.subtract(innerCircle);

		area16 = new Area(strip);
		area16.intersect(set[0]);

		area25 = new Area(strip);
		area25.intersect(set[1]);

		area47 = new Area(area16);
		area47.intersect(area25);

		area16.subtract(area47);
		area25.subtract(area47);

		// Divide everything into 8 regions
		remainder.subtract(set[0]);
		remainder.subtract(set[1]);
		remainder.subtract(set[2]);

		intersection[3] = new Area(set[0]);
		intersection[3].intersect(set[1]);
		intersection[3].intersect(set[2]);

		intersection[0] = new Area(set[0]);
		intersection[0].intersect(set[1]);
		intersection[0].subtract(intersection[3]);

		intersection[1] = new Area(set[1]);
		intersection[1].intersect(set[2]);
		intersection[1].subtract(intersection[3]);

		intersection[2] = new Area(set[2]);
		intersection[2].intersect(set[0]);
		intersection[2].subtract(intersection[3]);

		set[0].subtract(intersection[0]);
		set[0].subtract(intersection[2]);
		set[0].subtract(intersection[3]);

		set[1].subtract(intersection[0]);
		set[1].subtract(intersection[1]);
		set[1].subtract(intersection[3]);

		set[2].subtract(intersection[1]);
		set[2].subtract(intersection[2]);
		set[2].subtract(intersection[3]);

		// now find the "effective" pure regions, namely area1, 2 and 3
		area1 = new Area(set[0]);
		area1.subtract(area14);
		area1.subtract(area16);

		area2 = new Area(set[1]);
		area2.subtract(area24);
		area2.subtract(area25);

		area3 = new Area(set[2]);
		area3.subtract(area36);
		area3.subtract(area35);

		// get the intersections
		areaInt0 = new Area(intersection[0]);
		areaInt1 = new Area(intersection[1]);
		areaInt2 = new Area(intersection[2]);
		areaInt3 = new Area(intersection[3]);

		areaInt0.subtract(area14);
		areaInt0.subtract(area24);
		areaInt0.subtract(area47);

		areaInt1.subtract(area25);
		areaInt1.subtract(area35);
		areaInt1.subtract(area57);

		areaInt2.subtract(area36);
		areaInt2.subtract(area16);
		areaInt2.subtract(area67);

		areaInt3.subtract(area47);
		areaInt3.subtract(area57);
		areaInt3.subtract(area67);

		// add the "basic" regions
		regions.put(0, area1);
		regions.put(1, area2);
		regions.put(2, area3);
		regions.put(3, areaInt0);
		regions.put(4, areaInt1);
		regions.put(5, areaInt2);
		regions.put(6, areaInt3);
		regions.put(7, remainder);

		// now add the "thick border" regions
		regions.put(8, area14);
		regions.put(9, area24);
		regions.put(10, area25);
		regions.put(11, area35);
		regions.put(12, area36);
		regions.put(13, area16);
		regions.put(14, area47);
		regions.put(15, area57);
		regions.put(16, area67);

		// now add the drawing areas to the drawing regions
		drawingRegions.put(0, set[0]);
		drawingRegions.put(1, set[1]);
		drawingRegions.put(2, set[2]);
		drawingRegions.put(3, intersection[0]);
		drawingRegions.put(4, intersection[1]);
		drawingRegions.put(5, intersection[2]);
		drawingRegions.put(6, intersection[3]);
		drawingRegions.put(7, remainder);
		drawingRegions.put(8, area14);
		drawingRegions.put(9, area24);
		drawingRegions.put(10, area25);
		drawingRegions.put(11, area35);
		drawingRegions.put(12, area36);
		drawingRegions.put(13, area16);
		drawingRegions.put(14, area47);
		drawingRegions.put(15, area57);
		drawingRegions.put(16, area67);

		graphics.setColor(DEFAULT_COLOR);
		graphics.fill(set[0]);
		graphics.fill(set[1]);
		graphics.fill(set[2]);
		graphics.fill(intersection[0]);
		graphics.fill(intersection[1]);
		graphics.fill(intersection[2]);
		graphics.fill(intersection[3]);
		graphics.fill(remainder);

		for (int k = 0; k < NO_OF_REGIONS; k++) {
			int status = regionStatus.get(new Integer(k));
			Area area = drawingRegions.get(new Integer(k));
			if (status == -1) {
				graphics.setColor(EXCLUSION_COLOR);
				graphics.fill(area);
			} else if (status == 1) {
				graphics.setColor(BORDER_COLOR);
				int x = (int) area.getBounds().getCenterX();
				int y = (int) area.getBounds().getCenterY() + 5;
				switch (k) {
				case 0:
					graphics.drawString("\u2733", x - 15, y - 10);
					break;
				case 1:
					graphics.drawString("\u2733", x + 5, y - 10);
					break;
				case 2:
					graphics.drawString("\u2733", x - 5, y + 2);
					break;
				case 3:
					graphics.drawString("\u2733", x - 5, y + 5);
					break;
				case 4:
					graphics.drawString("\u2733", x + 10, y + 8);
					break;
				case 5:
					graphics.drawString("\u2733", x - 18, y + 7);
					break;
				case 6:
					graphics.drawString("\u2733", x - 5, y - 3);
					break;
				case 7:
					graphics.drawString("\u2733", 10, 260);
					break;
				case 8:
					graphics.drawString("\u2733", x - 13, y + 1);
					break;
				case 9:
					graphics.drawString("\u2733", x + 4, y + 1);
					break;
				case 10:
					graphics.drawString("\u2733", x + 22, y - 3);
					break;
				case 11:
					graphics.drawString("\u2733", x + 7, y + 24);
					break;
				case 12:
					graphics.drawString("\u2733", x - 5, y + 7);
					break;
				case 13:
					graphics.drawString("\u2733", x - 31, y - 3);
					break;
				case 14:
					graphics.drawString("\u2733", x - 5, y - 2);
					break;
				case 15:
					graphics.drawString("\u2733", x - 1, y + 1);
					break;
				case 16:
					graphics.drawString("\u2733", x - 8, y + 1);
					break;
				}
			}

			graphics.setColor(BORDER_COLOR);
			graphics.draw(set[0]);
			graphics.draw(set[1]);
			graphics.draw(set[2]);
			graphics.draw(intersection[0]);
			graphics.draw(intersection[1]);
			graphics.draw(intersection[2]);
			graphics.draw(intersection[3]);
			graphics.draw(remainder);
		}

		isInitialized = true;
	}

	// method that sets the selection mode upon button click
	public void setSelectionMode(int newMode) {
		mode = newMode;
	}

	// method that gets the selection mode
	public int getSelectionMode() {
		return mode;
	}

	// method that sets the names of Sets. uses first 15 chars only
	public void setNamesOfSets(String[] nameOfSet) {
		for (int i = 0; i < NO_OF_SETS; i++) {
			this.nameOfSet[i] = nameOfSet[i];
		}
	}

	// method that gets the names of Sets
	public String[] getNamesOfSets() {
		return this.nameOfSet;
	}

	public Map<Integer, Integer> getRegionStatus() {
		return regionStatus;
	}

	public void clearDiagram() {
		for (int i = 0; i < regionStatus.size(); i++) {
			this.regionStatus.put(i, 0);
		}
		isInitialized = false;
		this.repaint();
	}

	@Override
	public void setRegionStatus(Map<Integer, Integer> status) {
		this.regionStatus = status;
	}

	public void mouseClicked(MouseEvent e) {
		double x = e.getX();
		double y = e.getY();

		for (int i = 0; i < regions.size(); i++) {
			Area a = regions.get(i);
			if (a.contains(x, y) && i < 8) {
				if (regionStatus.get(i) == -1) {
					eraseAreaFirst = true;
				}
				regionStatus.put(i, mode);
				regionToUpdate = i;
				needUpdate = true;
				VennDisplay.clearCheckDiag();
				this.repaint();
			} else if (a.contains(x, y) && i >= 8 && mode == 1) {
				regionStatus.put(i, mode);
				regionToUpdate = i;
				needUpdate = true;
				VennDisplay.clearCheckDiag();
				this.repaint();
			} else if (a.contains(x, y) && i >= 8 && mode == 0) {
				regionStatus.put(i, mode);
				regionToUpdate = i;
				needUpdate = true;
				VennDisplay.clearCheckDiag();
				this.repaint();
			}
		}
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void setInitialized(boolean value) {
		isInitialized = value;
	}

}