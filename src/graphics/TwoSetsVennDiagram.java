/*      
 * Filename: TwoSetsVennDiagram.java
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
import java.awt.geom.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.border.LineBorder;

public class TwoSetsVennDiagram extends VennDiagram {
	private static final long serialVersionUID = 1L;

	// diagram info
	public static final int NO_OF_SETS = 2;
	public static final int NO_OF_REGIONS = NO_OF_SETS + 1 + 1;
	private String[] nameOfSet = new String[NO_OF_SETS];
	private String diagramTitle;
	private Area[] set = new Area[NO_OF_SETS];
	private Area intersection;
	private Area remainder;

	// boolean and int that tells us if and which region to update after mouse
	// clicks
	boolean isInitialized = false;
	boolean needUpdate = false;
	int regionToUpdate = 0;

	// all the regions mapped by region number
	private Map<Integer, Area> regions = new HashMap<Integer, Area>();;
	// the status for all regions. 0 if default, 1 if selected, -1 if excluded.
	private Map<Integer, Integer> regionStatus = new HashMap<Integer, Integer>();

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

	public boolean isOptimizedDrawingEnabled() {
		return true;
	}

	/**
	 * This constructor is a convenience for creating user interaction diagrams
	 */
	public TwoSetsVennDiagram(String[] nameOfSet, int[] regionStatusInfo,
			String diagramTitle) {
		this(nameOfSet, regionStatusInfo, diagramTitle, true);
	}

	/**
	 * Creates a Venn Diagram using the names of sets and status of each region
	 */
	public TwoSetsVennDiagram(String[] nameOfSet, int[] regionStatusInfo,
			String diagramTitle, boolean isInteractive) {
		this.setBorder(new LineBorder(Color.BLACK));
		this.setBackground(new Color(0, 0, 0, 0));
		this.nameOfSet = new String[nameOfSet.length];
		this.setNamesOfSets(nameOfSet);
		this.diagramTitle = diagramTitle;
		this.setOpaque(true);

		if (isInteractive) {
			this.addMouseListener(this);
		}

		// initialize region status to get "empty" diagram
		for (int i = 0; i < NO_OF_REGIONS; i++) {
			this.regionStatus.put(i, 0);
		}

		// now update the diagram with regions that are "marked"
		for (int i = 0; i < regionStatusInfo.length; i++) {
			if (regionStatusInfo[i] == 0) {
				continue;
			}
			int regionNo = Math.abs(regionStatusInfo[i]) - 1;
			this.regionStatus
					.put(regionNo, Integer.signum(regionStatusInfo[i]));
		}

		// RepaintManager currentManager = RepaintManager.currentManager(this);
		// currentManager.setDoubleBufferingEnabled(false);
	}

	public void repaintIt(Graphics2D graphics) {
		int status = regionStatus.get(regionToUpdate);
		Area area = regions.get(regionToUpdate);
		if (status == 0) {
			graphics.setColor(DEFAULT_COLOR);
			graphics.fill(area);
		} else if (status == -1) {
			graphics.setColor(EXCLUSION_COLOR);
			graphics.fill(area);
		} else if (status == 1) {
			graphics.setColor(DEFAULT_COLOR);
			graphics.fill(area);
			graphics.setColor(BORDER_COLOR);
			int x = (int) area.getBounds().getCenterX();
			int y = (int) area.getBounds().getCenterY() + 5;
			switch (regionToUpdate) {
			case 0:
				graphics.drawString("\u2733", x - 10, y);
				break;
			case 1:
				graphics.drawString("\u2733", x + 5, y);
				break;
			case 2:
				graphics.drawString("\u2733", x - 5, y);
				break;
			case 3:
				graphics.drawString("\u2733", 10, 180);
				break;
			}
		}

		graphics.setColor(BORDER_COLOR);
		graphics.draw(set[0]);
		graphics.draw(set[1]);
		graphics.draw(remainder);
		// graphics.dispose();

		regionToUpdate = 0;
		needUpdate = false;

	}

	public void paint(Graphics g) {
		Graphics2D graphics = (Graphics2D) g.create();
		graphics.addRenderingHints(hints2);
		graphics.setStroke(STROKE);

		// this if block is used for user mouse clicks after initialization
		if (isInitialized && needUpdate) {
			repaintIt(graphics);
			return;
		}

		graphics.setStroke(STROKE);
		universeShape.setFrame(0, 40, 250, 150);
		remainder = new Area(universeShape);
		graphics.draw(universeShape);

		// draw title
		int centerX = 125 - (int) ((diagramTitle.length() / 2) * 6);
		graphics.drawChars(diagramTitle.toCharArray(), 0,
				diagramTitle.length(), centerX, 20);

		// draw set names
		graphics.drawChars(nameOfSet[0].toCharArray(), 0,
				nameOfSet[0].length(), 0, 35);
		/*
		 * for (int i = nameOfSet[1].length() - 1; i > 0; i--) {
		 * graphics.drawChars(new char[] { nameOfSet[1].toCharArray()[i] }, 0,
		 * 0, 250 - (7 * (nameOfSet[1].length() - i + 1)), 35);
		 * System.out.println(nameOfSet[1].toCharArray()[i]); }
		 */

		graphics.drawChars(nameOfSet[1].toCharArray(), 0,
				nameOfSet[1].length(), 150, 35);

		// Draw the first set
		setShape.setFrame(5, 45, 140, 140);
		set[0] = new Area(setShape);

		// Draw the second set
		setShape.setFrame(105, 45, 140, 140);
		set[1] = new Area(setShape);

		// Divide everything into 4 regions
		remainder.subtract(set[0]);
		remainder.subtract(set[1]);
		intersection = new Area(set[0]);
		intersection.intersect(set[1]);
		set[0].subtract(intersection);
		set[1].subtract(intersection);

		regions.put(0, set[0]);
		regions.put(1, set[1]);
		regions.put(2, intersection);
		regions.put(3, remainder);

		graphics.setColor(DEFAULT_COLOR);
		graphics.fill(set[0]);
		graphics.fill(set[1]);
		graphics.fill(intersection);
		graphics.fill(remainder);

		for (int k = 0; k < 4; k++) {
			int status = regionStatus.get(k);
			Area area = regions.get(k);
			if (status == 0) {
				graphics.setColor(DEFAULT_COLOR);
				graphics.fill(area);
			} else if (status == -1) {
				graphics.setColor(EXCLUSION_COLOR);
				graphics.fill(area);
			} else if (status == 1) {
				graphics.setColor(DEFAULT_COLOR);
				graphics.fill(area);
				graphics.setColor(BORDER_COLOR);
				int x = (int) area.getBounds().getCenterX();
				int y = (int) area.getBounds().getCenterY() + 5;
				switch (k) {
				case 0:
					graphics.drawString("\u2733", x - 10, y);
					break;
				case 1:
					graphics.drawString("\u2733", x + 5, y);
					break;
				case 2:
					graphics.drawString("\u2733", x - 5, y);
					break;
				case 3:
					graphics.drawString("\u2733", 10, 180);
					break;
				}
			}
		}

		graphics.setColor(BORDER_COLOR);
		graphics.draw(set[0]);
		graphics.draw(set[1]);
		graphics.draw(remainder);

		isInitialized = true;

	}

	public void mouseClicked(MouseEvent e) {
		double x = e.getX();
		double y = e.getY();

		for (int i = 0; i < regions.size(); i++) {
			Area a = regions.get(i);
			if (a.contains(x, y)) {
				regionStatus.put(i, mode);
				regionToUpdate = i;
				needUpdate = true;
				VennDisplay.clearCheckDiag();
				this.repaint();
			}
		}
	}

	public void clearDiagram() {
		for (int i = 0; i < regionStatus.size(); i++) {
			this.regionStatus.put(i, 0);
		}
		isInitialized = false;
		this.repaint();
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void setRegionStatus(Map<Integer, Integer> status) {
		this.regionStatus = status;
	}

	@Override
	public void setInitialized(boolean value) {
		isInitialized = value;

	}

}