/*      
 * Fileame: VennDisplay.java
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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.RepaintManager;

import analysis.LogicalArgument;
import analysis.Sentence;

public class VennDisplay extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private static Cursor someCursor = Cursor
			.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	private static Cursor noneCursor = Cursor
			.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
	private static Cursor unknownCursor = Cursor
			.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

	public static void clearCheckDiag() {
		checkDiagLabel.setText("          ");
		checkDiagLabel2.setText("           ");
	}
	
	// radio buttons
	private JRadioButton noneButton = new JRadioButton("   \u25a7  (None)");
	private JRadioButton someButton = new JRadioButton("   \u2733  (Some)");
	private JRadioButton unknownButton = new JRadioButton(
			"   \u25cb  (Erase Area)");
	private ButtonGroup buttonGroup;

	// JButtons
	private JButton clearDiagButton = new JButton("Clear Diagram");
	JButton checkDiagButton = new JButton("Check Diagram");
	JButton showSolButton = new JButton("Show Solution");
	private JButton nextProbButton = new JButton("Next Problem");

	// JLabel that shows whether user's diagram is correct
	private static JLabel checkDiagLabel = new JLabel("         ");

	// ` second JLabel for the 2nd diagram in immInfer.
	private static JLabel checkDiagLabel2 = new JLabel("         ");

	// the tWo possible diagrams at any one time + the buttons
	VennDiagram userInteraction = null;
	VennDiagram solution = null;
	private JPanel buttonPanel = new JPanel(new GridBagLayout());
	JPanel userIntPanel = null;
	JPanel solutionPanel = null;

	// for IMM Infer
	private VennDiagram userInteraction2 = null;
	private VennDiagram solution2 = null;

	public VennDisplay() {
		Font times = new Font("Arial Unicode MS", Font.PLAIN, 15);
		noneButton.setFont(times);
		someButton.setFont(times);
		unknownButton.setFont(times);

		// ImageIcon i = new ImageIcon(MainWindow.noneImage);
		// noneButton.setIcon(i);
		noneCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				MainWindow.noneImage, new Point(16, 16), "");

		someCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				MainWindow.someImage, new Point(16, 16), "");

		unknownCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				MainWindow.unknownImage, new Point(16, 16), "");

		buttonGroup = new ButtonGroup();
		buttonGroup.add(noneButton);
		buttonGroup.add(someButton);
		buttonGroup.add(unknownButton);
		someButton.setSelected(true);

		// set the action commands
		noneButton.setActionCommand("none");
		unknownButton.setActionCommand("unknown");
		someButton.setActionCommand("some");
		clearDiagButton.setActionCommand("clearDiag");
		checkDiagButton.setActionCommand("checkDiag");
		showSolButton.setActionCommand("showSol");
		nextProbButton.setActionCommand("nextProb");

		// set the action listener
		noneButton.addActionListener(this);
		someButton.addActionListener(this);
		unknownButton.addActionListener(this);
		clearDiagButton.addActionListener(this);
		checkDiagButton.addActionListener(this);
		showSolButton.addActionListener(this);
		nextProbButton.addActionListener(this);

		// set the button size
		clearDiagButton.setPreferredSize(new Dimension(150, 25));
		showSolButton.setPreferredSize(new Dimension(150, 25));
		checkDiagButton.setPreferredSize(new Dimension(150, 25));

		// set up the button panel
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridwidth = 1;
		constraints.gridx = GridBagConstraints.REMAINDER;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.gridheight = 1;
		constraints.insets = new Insets(5, 5, 5, 0);

		buttonPanel.add(noneButton, constraints);
		buttonPanel.add(someButton, constraints);
		buttonPanel.add(unknownButton, constraints);
		buttonPanel.add(clearDiagButton, constraints);
		buttonPanel.add(checkDiagButton, constraints);

		// constraints.insets = new Insets(2, 2, 2, 0);
		buttonPanel.add(checkDiagLabel, constraints);
		buttonPanel.add(checkDiagLabel2, constraints);
		buttonPanel.add(showSolButton, constraints);
		buttonPanel.setPreferredSize(new Dimension(175, 345));

		RepaintManager currentManager = RepaintManager.currentManager(this);
		currentManager.setDoubleBufferingEnabled(false);

		refreshDisplay();
	}

	public void refreshDisplay() {
		noneButton.setPreferredSize(new Dimension(150, 50));
		someButton.setPreferredSize(new Dimension(150, 50));
		unknownButton.setPreferredSize(new Dimension(150, 50));

		this.removeAll();
		int[] zeroes = new int[MainWindow.arg.getNoOfRegions()];
		for (int i : zeroes) {
			i = 0;
		}

		userIntPanel = new JPanel();
		solutionPanel = new JPanel();
		userInteraction = null;
		userInteraction2 = null;
		solution = null;
		solution2 = null;

		userIntPanel.setLayout(new BoxLayout(userIntPanel, BoxLayout.Y_AXIS));
		solutionPanel.setLayout(new BoxLayout(solutionPanel, BoxLayout.Y_AXIS));

		// prepare the diagrams
		int paneNo = MainWindow.arg.getNoOfSenses();
		switch (paneNo) {
		case 1:
			userInteraction = new TwoSetsVennDiagram(MainWindow.arg
					.getNamesOfSets(), zeroes, "Sentence");
			solution = new TwoSetsVennDiagram(MainWindow.arg.getNamesOfSets(),
					MainWindow.arg.computeSolution(), "Solution", false);
			break;

		case 2:
			userInteraction = new TwoSetsVennDiagram(MainWindow.arg
					.getNamesOfSets(), zeroes, "Premise");
			solution = new TwoSetsVennDiagram(MainWindow.arg.getNamesOfSets(),
					MainWindow.arg.computeSolution(), "Solution - Premise",
					false);
			userInteraction2 = new TwoSetsVennDiagram(MainWindow.arg
					.getNamesOfSets(), zeroes, "Conclusion");
			solution2 = new TwoSetsVennDiagram(MainWindow.arg.getNamesOfSets(),
					MainWindow.arg.computeConclusion(),
					"Solution - Conclusion", false);
			break;

		case 3:
			userInteraction = new ThreeSetsVennDiagram(MainWindow.arg
					.getNamesOfSets(), zeroes, "Premises");

			solution = new ThreeSetsVennDiagram(
					MainWindow.arg.getNamesOfSets(), MainWindow.arg
							.computeSolution(), "Solution - Premises", false);

			String[] conclusionClasses = new String[2];
			conclusionClasses[0] = MainWindow.arg.getNamesOfSets()[0];
			conclusionClasses[1] = MainWindow.arg.getNamesOfSets()[1];
			LogicalArgument conclusionArg = new Sentence(conclusionClasses,
					MainWindow.arg.getSenses()[2]);
			userInteraction2 = new TwoSetsVennDiagram(conclusionArg
					.getNamesOfSets(), zeroes, "Conclusion");
			solution2 = new TwoSetsVennDiagram(conclusionArg.getNamesOfSets(),
					conclusionArg.computeSolution(), "Solution - Conclusion", false);
		}

		// initialize the cursor
		if (userInteraction.getSelectionMode() == -1) {
			userInteraction.setCursor(noneCursor);
			if (userInteraction2 != null) {
				userInteraction2.setCursor(noneCursor);
			}
		} else if (userInteraction.getSelectionMode() == 1) {
			userInteraction.setCursor(someCursor);
			if (userInteraction2 != null) {
				userInteraction2.setCursor(someCursor);
			}
		} else if (userInteraction.getSelectionMode() == 0) {
			userInteraction.setCursor(unknownCursor);
			if (userInteraction2 != null) {
				userInteraction2.setCursor(someCursor);
			}
		}

		userIntPanel.add(userInteraction);
		solutionPanel.add(solution);
		if (paneNo != 0 && paneNo != 1) {
			userIntPanel.add(userInteraction2);
			solutionPanel.add(solution2);
		}

		// now add the components
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(buttonPanel);
		this.add(userIntPanel);
		this.add(solutionPanel);
		checkDiagLabel.setText(" ");
		checkDiagLabel2.setText("         ");
		showSolButton.setText("Show Solution");
		solution.setVisible(false);
		if (solution2 != null) {
			solution2.setVisible(false); // disable solution for now
		}

		this.setPreferredSize(new Dimension(710, 600));
		userIntPanel.setPreferredSize(new Dimension(255, 600));
		solutionPanel.setPreferredSize(new Dimension(255, 600));

	}

	// more efficient method than refresh. used only to reflect changes in
	// complements/form/figure
	public void updateDisplay() {
		// clear the user interaction diagram
		userInteraction.clearDiagram();
		if (userInteraction2 != null) {
			userInteraction2.clearDiagram();
			checkDiagLabel2.setText("     ");
		}
		checkDiagLabel.setText("     ");

		// update the solution diagram(s)
		solutionPanel.remove(solution);
		if (solution2 != null) {
			solutionPanel.remove(solution2);
		}
		solutionPanel.repaint();
		if (MainWindow.arg.getNoOfSenses() == 1) {
			solution = new TwoSetsVennDiagram(MainWindow.arg.getNamesOfSets(),
					MainWindow.arg.computeSolution(), "Solution", false);
		} else if (MainWindow.arg.getNoOfSenses() == 2) {
			solution = new TwoSetsVennDiagram(MainWindow.arg.getNamesOfSets(),
					MainWindow.arg.computeSolution(), "Solution - Premise",
					false);
		} else if (MainWindow.arg.getNoOfSenses() == 3) {
			solution = new ThreeSetsVennDiagram(
					MainWindow.arg.getNamesOfSets(), MainWindow.arg
							.computeSolution(), "Solution - Premises", false);
		}

		solutionPanel.add(solution);
		solution.setVisible(false);
		showSolButton.setText("Show Solution");

		// for immediate inference
		if (MainWindow.arg.getNoOfSenses() == 2) {
			solution2 = new TwoSetsVennDiagram(MainWindow.arg.getNamesOfSets(),
					MainWindow.arg.computeConclusion(),
					"Solution - Conclusion", false);
			solutionPanel.add(solution2);
			solution2.setVisible(false);
		}
		// for syllogism
		else if (MainWindow.arg.getNoOfSenses() == 3) {
			String[] conclusionClasses = new String[2];
			conclusionClasses[0] = MainWindow.arg.getNamesOfSets()[0];
			conclusionClasses[1] = MainWindow.arg.getNamesOfSets()[1];
			LogicalArgument conclusionArg = new Sentence(conclusionClasses,
					MainWindow.arg.getSenses()[2]);
			solution2 = new TwoSetsVennDiagram(conclusionArg.getNamesOfSets(),
					conclusionArg.computeSolution(), "Solution - Conclusion", false);
			solutionPanel.add(solution2);
			solution2.setVisible(false);
		}
	}

	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		// System.out.println(s);
		if (s.equals("none")) {
			userInteraction.setSelectionMode(-1);
			userInteraction.setCursor(noneCursor);
			if (userInteraction2 != null) {
				userInteraction2.setSelectionMode(-1);
				userInteraction2.setCursor(noneCursor);
			}
		} else if (s.equals("some")) {
			userInteraction.setSelectionMode(1);
			userInteraction.setCursor(someCursor);
			if (userInteraction2 != null) {
				userInteraction2.setSelectionMode(1);
				userInteraction2.setCursor(someCursor);
			}
		} else if (s.equals("unknown")) {
			userInteraction.setSelectionMode(0);
			userInteraction.setCursor(unknownCursor);
			if (userInteraction2 != null) {
				userInteraction2.setSelectionMode(0);
				userInteraction2.setCursor(unknownCursor);
			}
		} else if (s.equals("clearDiag")) {
			userInteraction.clearDiagram();
			if (userInteraction2 != null) {
				userInteraction2.clearDiagram();
				checkDiagLabel2.setText("     ");
			}
			checkDiagLabel.setText("     ");
		} else if (s.equals("checkDiag")) {
			Map<Integer, Integer> userResponse = userInteraction
					.getRegionStatus();
			Map<Integer, Integer> correctSolution = solution.getRegionStatus();

			Map<Integer, Integer> userResponse2 = null;
			Map<Integer, Integer> correctSolution2 = null;

			if (userInteraction2 != null) {
				userResponse2 = userInteraction2.getRegionStatus();
				correctSolution2 = solution2.getRegionStatus();
				correctSolution2.remove(4); // fixes a bug. don't know why it
				// happens
				//System.out.println(userResponse2);
				//System.out.println(correctSolution2);

			}

			String argumentType = MainWindow.arg.getNoOfSenses() != 1 ? "Premise:  "
					: "";
			if (userResponse.equals(correctSolution)) {
				checkDiagLabel.setText(argumentType + "Correct!");
			} else {
				checkDiagLabel.setText(argumentType + "Incorrect");
			}

			if (MainWindow.arg.getNoOfSenses() != 1) {
				if (userResponse2.equals(correctSolution2)) {
					checkDiagLabel2.setText("Conclusion:  Correct!");
				} else {
					checkDiagLabel2.setText("Conclusion: Incorrect");
				}
			}
		} else if (s.equals("showSol")) {
			if (solution.isVisible() == false) {
				solution.setVisible(true);
				if (solution2 != null) {
					solution2.setVisible(true);
				}
				showSolButton.setText("Hide Solution");
			} else {
				solution.setVisible(false);
				if (solution2 != null) {
					solution2.setVisible(false);
				}

				showSolButton.setText("Show Solution");

			}
		}
	}

	public void showSolution() {
		if (solution.isVisible() == false) {
			solution.setVisible(true);
			if (solution2 != null) {
				solution2.setVisible(true);
			}
			showSolButton.setText("Hide Solution");
		}
	}

	public boolean checkDiagram() {
		boolean isValid = false;
		Map<Integer, Integer> userResponse = userInteraction.getRegionStatus();
		Map<Integer, Integer> correctSolution = solution.getRegionStatus();

		Map<Integer, Integer> userResponse2 = null;
		Map<Integer, Integer> correctSolution2 = null;

		if (userInteraction2 != null) {
			userResponse2 = userInteraction2.getRegionStatus();
			correctSolution2 = solution2.getRegionStatus();

		}

		if (userResponse.equals(correctSolution)) {
			isValid = true;
		}

		if (MainWindow.arg.getNoOfSenses() == 2) {
			if (userResponse2.equals(correctSolution2)) {
				isValid = isValid && true;
			} else {
				isValid = isValid && false;
			}
		}
		return isValid;
	}
}
