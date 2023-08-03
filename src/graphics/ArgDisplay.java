/*      
 * Filename: ArgDisplay.java
 * 
 * Author: Dhrubo Jyoti
 *         Class of 2011
 *         HB 2313, Dartmouth College
 *         Hanover, NH 03755, USA
 *         email: Dhrubo.Jyoti@Dartmouth.edu
 *
 * Date:   	June 25, 2008
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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.RepaintManager;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.font.TextAttribute;

public class ArgDisplay extends JTabbedPane implements ChangeListener {
	private static final long serialVersionUID = 1L;

	// info of currently displayed problem
	int[] senses;
	String[] namesOfSets;
	int[][] complements;
	int noOfSenses;

	// the sentence panel
	Pane sentence = new Pane(0);
	Pane sentWComp = new Pane(1);
	Pane immInfer = new Pane(2);
	Pane immInferWComp = new Pane(3);
	Pane syllogism = new Pane(4);

	// the parent
	static JFrame parent = null;
	static int paneIndex = 0;

	public ArgDisplay(JFrame parent) {
		ArgDisplay.parent = parent;
		this.add(sentence, "Sentence");
		this.add(sentWComp, "Sentence With Complements");
		this.add(immInfer, "Immediate Inference");
		this.add(immInferWComp, "Immediate Inference With Complements");
		this.add(syllogism, "Syllogism");
		this.addChangeListener(this);
		refreshDisplay(0); // show sentence on start-up
		RepaintManager currentManager = RepaintManager.currentManager(this);
		currentManager.setDoubleBufferingEnabled(false);
	}

	public void refreshDisplay(int index) {
		paneIndex = index;
		senses = MainWindow.arg.getSenses();
		namesOfSets = MainWindow.arg.getNamesOfSets();
		complements = MainWindow.arg.getComplements();
		noOfSenses = MainWindow.arg.getNoOfSenses();

		switch (index) {
		case 0:
			sentence.refresh();
			break;
		case 1:
			sentWComp.refresh();
			break;

		case 2:
			immInfer.refresh();
			break;
		case 3:
			immInferWComp.refresh();
			break;
		case 4:
			syllogism.refresh();
			break;
		}
	}

	public class Pane extends JPanel implements ActionListener {
		private static final long serialVersionUID = 1L;

		private FlowLayout leftLayout = new FlowLayout(FlowLayout.LEFT, 0, 0);
		private FlowLayout rightLayout = new FlowLayout(FlowLayout.RIGHT, 10,
				10);
		private int levelIndex = 0;
		private JPanel properties = null;
		private JPanel sentences = null;
		private JPanel nextProbPanel = null;
		private JButton nextProbButton = null;

		public Pane(int levelIndex) {
			this.levelIndex = levelIndex;
			this.setLayout(leftLayout);
			RepaintManager currentManager = RepaintManager.currentManager(this);
			currentManager.setDoubleBufferingEnabled(false);
		}

		public void refresh() {
			this.removeAll();
			properties = new JPanel(rightLayout);
			sentences = new JPanel();
			nextProbPanel = new JPanel();
			nextProbButton = new JButton("Next");
			nextProbButton.addActionListener(this);

			this.properties.setPreferredSize(new Dimension(310, 135));
			this.sentences.setPreferredSize(new Dimension(410, 135));
			this.nextProbPanel.setPreferredSize(new Dimension(95, 135));
			this.setPreferredSize(new Dimension(840, 135));

			this.properties.setBorder(new LineBorder(Color.BLACK));
			this.sentences.setBorder(new LineBorder(Color.BLACK));
			this.nextProbPanel.setBorder(new LineBorder(Color.BLACK));
			this.add(properties);
			this.add(sentences);

			sentences.setLayout(new BoxLayout(sentences, BoxLayout.Y_AXIS));

			nextProbPanel.setLayout(new BoxLayout(nextProbPanel,
					BoxLayout.Y_AXIS));

			switch (levelIndex) {
			case 0: // sentence
				properties.add(new Form("Form"));
				break;
			case 1: // sentence with complements
				properties.add(new Complements());
				properties.add(new Form("Form"));
				break;
			case 2: // immediate inference
				properties.add(new Form("Forms"));
				nextProbPanel.add(new CheckValidityPanel());
				break;
			case 3: // imm infer w complements
				properties.add(new Complements());
				properties.add(new Form("Forms"));
				nextProbPanel.add(new CheckValidityPanel());
				break;
			case 4: // syllogism
				properties.add(new Figure());
				properties.add(new Form("Mood"));
				nextProbPanel.add(new CheckValidityPanel());
				break;
			case 5: // syllogism w comp
				properties.add(new Figure());
				properties.add(new Complements());
				properties.add(new Form("Mood"));
				nextProbPanel.add(new CheckValidityPanel());
				break;
			}

			nextProbPanel.add(nextProbButton);
			this.add(nextProbPanel);

			Font sentenceFont = new Font("Arial Unicode", Font.PLAIN, 14);
			Font spacingFont = new Font("Arial", Font.PLAIN, 5);
			Font underlineFont = new Font("Arial Bold", Font.BOLD, 8);
			for (int i = 0; i < 5; i++) {
				JLabel spacing = new JLabel(" ");
				spacing.setFont(spacingFont);
				sentences.add(spacing);
			}

			// make underlined font for imm infer and syllo
			Map<TextAttribute, Object> map = new HashMap<TextAttribute, Object>();
			map.put(TextAttribute.UNDERLINE, new Integer(0));

			map.put(TextAttribute.SIZE, new Float(14));

			for (int i = 0; i < noOfSenses; i++) {
				JLabel sentenceLabel = new JLabel();
				sentenceLabel.setFont(sentenceFont);

				String s = "";
				switch (senses[i]) {
				case 'A':
					s = "All ";
					break;
				case 'E':
					s = "No ";
					break;
				case 'I':
					s = "Some ";
					break;
				case 'O':
					s = "Some ";
					break;
				}

				String subject = "";
				if (levelIndex == 4 || levelIndex == 5) {
					int figure = MainWindow.arg.getFigureIndex();
					switch (i) {
					case 0:
						if (figure == 0 || figure == 2) {
							subject = namesOfSets[2];
						} else {
							subject = namesOfSets[1];
						}
						break;
					case 1:
						if (figure == 0 || figure == 1) {
							subject = namesOfSets[0];
						} else {
							subject = namesOfSets[2];
						}
						break;
					case 2:
						subject = namesOfSets[0];
						break;
					}

				} else {
					subject = MainWindow.arg.isConclSwapped() && i == 1 ? namesOfSets[1]
							: namesOfSets[0];
				}

				if (complements[i][0] == 0) {
					s = s + subject + " are ";
				} else if (complements[i][0] == 1) {
					s = s + " non-" + subject + " are ";
				}

				if (senses[i] == 'O') {
					s = s + "not ";
				}

				String predicate = "";
				if (levelIndex == 4 || levelIndex == 5) {
					int figure = MainWindow.arg.getFigureIndex();
					switch (i) {
					case 0:
						if (figure == 0 || figure == 2) {
							predicate = namesOfSets[1];
						} else {
							predicate = namesOfSets[2];
						}
						break;
					case 1:
						if (figure == 0 || figure == 1) {
							predicate = namesOfSets[2];
						} else {
							predicate = namesOfSets[0];
						}
						break;
					case 2:
						predicate = namesOfSets[1];
						break;
					}

				} else {
					predicate = MainWindow.arg.isConclSwapped() && i == 1 ? namesOfSets[0]
							: namesOfSets[1];
				}

				if (complements[i][1] == 0) {
					s = s + predicate + ".";
				} else if (complements[i][1] == 1) {
					s = s + "non-" + predicate + ".";
				}

				sentenceLabel.setHorizontalAlignment(SwingConstants.LEFT);
				if (levelIndex == 0 || levelIndex == 1) {
					sentenceLabel.setText(" " + s);
				} else if ((levelIndex == 2 || levelIndex == 3) && i == 0) {
					sentenceLabel.setText(" " + s);
				} else if ((levelIndex == 2 || levelIndex == 3) && i == 1) {
					sentenceLabel.setText(" " + "\u2234 " + s);
				} else if ((levelIndex == 4 || levelIndex == 5) && (i == 0)) {
					sentenceLabel.setText(" " + s);
				} else if ((levelIndex == 4 || levelIndex == 5) && (i == 2)) {
					sentenceLabel.setText(" " + "\u2234 " + s);
				} else if ((levelIndex == 4 || levelIndex == 5) && (i == 1)) {
					sentenceLabel.setText(" " + s);
				}
				sentences.add(sentenceLabel);
				// add spacing or underline
				if (levelIndex != 0 && levelIndex != 1) {
					if (((levelIndex == 2 || levelIndex == 3) && i == 0)
							|| (levelIndex == 4 && i == 1)) {

						JLabel spacing = new JLabel(
								"    -----------------------"
										+ "----------------------------"
										+ "----------");
						spacing.setFont(underlineFont);
						sentences.add(spacing);
					} else {
						JLabel spacing = new JLabel(" ");
						spacing.setFont(spacingFont);
						sentences.add(spacing);
					}

				} else {
					JLabel spacing = new JLabel(" ");
					spacing.setFont(spacingFont);
					sentences.add(spacing);
				}
			}
		}

		public void actionPerformed(ActionEvent arg0) {
			switch (levelIndex) {
			case 0:
				MainWindow.loadNextSent();
				break;
			case 1:
				MainWindow.loadNextSentWComp();
				break;
			case 2:
				MainWindow.loadNextImmInfer();
				break;
			case 3:
				MainWindow.loadNextImmInferWComp();
				break;
			case 4:
				MainWindow.loadNextSyllogism();
				break;
			}
		}
	}

	/**
	 * Class that handles the "Form" of the argument, which is made up of 1-3
	 * "senses".
	 */
	public class Form extends JPanel implements ListSelectionListener {
		private static final long serialVersionUID = 1L;

		JList[] list = null;
		final String[] POSSIBLE_SENSES = new String[] { "A", "E", "I", "O" };
		boolean[] isInitialized = new boolean[noOfSenses];

		public Form(String title) {
			this.setBorder(new TitledBorder(null, title));
			list = new JList[noOfSenses];
			for (int i = 0; i < noOfSenses; i++) {
				list[i] = new JList(POSSIBLE_SENSES);
			}
			for (int i = 0; i < noOfSenses; i++) {
				list[i]
						.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				list[i].setLayoutOrientation(JList.HORIZONTAL_WRAP);
				list[i].setVisibleRowCount(1);
				list[i].setCellRenderer(new CellRenderer());
				list[i].addListSelectionListener(this);

				// determine sense
				switch (senses[i]) {
				case 'A':
					list[i].setSelectedIndex(0);
					break;
				case 'E':
					list[i].setSelectedIndex(1);
					break;
				case 'I':
					list[i].setSelectedIndex(2);
					break;
				case 'O':
					list[i].setSelectedIndex(3);
					break;
				}
				this.add(list[i]);
				isInitialized[i] = true;
			}
			this.setPreferredSize(new Dimension(100, 20 + (32 * noOfSenses)));
		}

		class CellRenderer extends JLabel implements ListCellRenderer {
			private static final long serialVersionUID = 1L;

			public CellRenderer() {
				setOpaque(true);
			}

			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {

				String str = (String) value;
				this.setText(" " + str + " ");
				this.setHorizontalTextPosition(SwingConstants.CENTER);
				this.setVerticalTextPosition(SwingConstants.CENTER);
				this.setBorder(new LineBorder(Color.BLACK));
				this.setFont(new Font("Arial", Font.BOLD, 16));
				this.setSize(new Dimension(140, 28));

				Color background;
				Color foreground;

				// check if this cell represents the current DnD drop location
				DropTarget dropLocation = list.getDropTarget();
				if (dropLocation != null && !dropLocation.isActive()
				/* && dropLocation.g == index */) {

					background = Color.BLUE;
					foreground = Color.WHITE;

					// check if this cell is selected
				} else if (isSelected) {
					background = Color.BLACK;
					foreground = Color.WHITE;

					// unselected, and not the DnD drop location
				} else {
					background = Color.WHITE;
					foreground = Color.BLACK;
				}

				setBackground(background);
				setForeground(foreground);

				return this;
			}
		}

		public void valueChanged(ListSelectionEvent e) {
			int[] currentSenses = MainWindow.arg.getSenses();
			boolean ready = isInitialized[0];
			for (int i = 0; i < isInitialized.length; i++) {
				ready = ready && isInitialized[i];
			}
			if (ready) {
				for (int i = 0; i < noOfSenses; i++) {
					int newSenseIndex = list[i].getSelectedIndex();
					switch (newSenseIndex) {
					case 0:
						currentSenses[i] = 'A';
						break;
					case 1:
						currentSenses[i] = 'E';
						break;
					case 2:
						currentSenses[i] = 'I';
						break;
					case 3:
						currentSenses[i] = 'O';
						break;
					}

					MainWindow.arg.setSenses(currentSenses);
					MainWindow.vennDisp.updateDisplay();
					ArgDisplay.this.refreshDisplay(getSelectedIndex());
				}
			}
		}
	}

	public class Complements extends JPanel implements ActionListener {
		private static final long serialVersionUID = 1L;

		JCheckBox[][] checkBox = new JCheckBox[noOfSenses][2];
		boolean[][] isInitialized = new boolean[noOfSenses][2];

		public Complements() {
			this.setLayout(new GridLayout(noOfSenses, 2, 0, 0));
			this.setBorder(new TitledBorder(null, "Complements"));
			this.setPreferredSize(new Dimension(180, 20 + (32 * noOfSenses)));
			for (int i = 0; i < noOfSenses; i++) {
				for (int j = 0; j < 2; j++) {
					checkBox[i][j] = new JCheckBox(j == 0 ? "Subject"
							: "Predicate", complements[i][j] == 0 ? false
							: true);
					checkBox[i][j].addActionListener(this);
					this.add(checkBox[i][j]);
				}
			}
		}

		public void actionPerformed(ActionEvent e) {
			JCheckBox changedCheckBox = (JCheckBox) e.getSource();
			outer: for (int i = 0; i < noOfSenses; i++) {
				for (int j = 0; j < 2; j++) {
					if (checkBox[i][j] == changedCheckBox) {
						complements[i][j] = changedCheckBox.isSelected() ? 1
								: 0;
						MainWindow.arg.setComplements(complements);
						MainWindow.vennDisp.updateDisplay();
						ArgDisplay.this.refreshDisplay(ArgDisplay.this
								.getSelectedIndex());
						break outer;
					}
				}
			}

		}
	}

	public class Figure extends JPanel implements ListSelectionListener {
		private static final long serialVersionUID = 1L;

		JList list = null;
		boolean isInitialized = false;

		public Figure() {
			this.setBorder(new TitledBorder(null, "Figure"));
			list = new JList(new String[] { " 1. M P\n     S M\n     S P",
					" 2. P M\n     S M\n     S P",
					" 3. M P\n     M S\n     S P",
					" 4. P M\n     M S\n     S P" });
			list.setAlignmentX(0);
			for (int i = 0; i < noOfSenses; i++) {
				list
						.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
				list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
				list.setVisibleRowCount(2);
				list.setCellRenderer(new CellRenderer());
				list.addListSelectionListener(this);

				// determine figure
				switch (MainWindow.arg.getFigureIndex()) {
				case 0:
					list.setSelectedIndex(0);
					break;
				case 1:
					list.setSelectedIndex(1);
					break;
				case 2:
					list.setSelectedIndex(2);
					break;
				case 3:
					list.setSelectedIndex(3);
					break;
				}
				this.add(list);

			}
			this.setPreferredSize(new Dimension(100, 20 + (32 * noOfSenses)));
			isInitialized = true;
		}

		class CellRenderer extends JLabel implements ListCellRenderer {
			private static final long serialVersionUID = 1L;

			public CellRenderer() {
				setOpaque(true);
			}

			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {

				this.setUI(new MultiLineLabelUI());
				String str = (String) value;
				this.setText(str);
				this.setHorizontalAlignment(SwingConstants.LEFT);
				this.setVerticalAlignment(SwingConstants.CENTER);

				this.setHorizontalTextPosition(SwingConstants.LEFT);
				this.setVerticalTextPosition(SwingConstants.CENTER);
				this.setBorder(new LineBorder(Color.BLACK));
				this.setFont(new Font("Arial", Font.BOLD, 10));
				this.setPreferredSize(new Dimension(40, 40));

				Color background;
				Color foreground;

				// check if this cell represents the current DnD drop location
				DropTarget dropLocation = list.getDropTarget();
				if (dropLocation != null && !dropLocation.isActive()
				/* && dropLocation.g == index */) {

					background = Color.BLUE;
					foreground = Color.WHITE;

					// check if this cell is selected
				} else if (isSelected) {
					background = Color.BLACK;
					foreground = Color.WHITE;

					// unselected, and not the DnD drop location
				} else {
					background = Color.WHITE;
					foreground = Color.BLACK;
				}

				setBackground(background);
				setForeground(foreground);

				return this;
			}
		}

		public void valueChanged(ListSelectionEvent e) {
			if (isInitialized) {
				MainWindow.arg.setFigureIndex(list.getSelectedIndex());
				MainWindow.vennDisp.updateDisplay();
				ArgDisplay.this.refreshDisplay(getSelectedIndex());
			}
		}
	}

	public class CheckValidityPanel extends JPanel implements ActionListener {
		private static final long serialVersionUID = 1L;

		private JRadioButton validButton = new JRadioButton("  Valid");
		private JRadioButton invalidButton = new JRadioButton("  Invalid");
		private ButtonGroup buttonGroup = new ButtonGroup();
		private JLabel correctLabel = new JLabel(" ");

		public CheckValidityPanel() {
			this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			buttonGroup.add(validButton);
			buttonGroup.add(invalidButton);
			this.add(validButton);
			this.add(invalidButton);
			validButton.addActionListener(this);
			invalidButton.addActionListener(this);
			this.add(correctLabel);
			this.add(new JLabel(" "));
			// validButton.setSelected(false);
			// invalidButton.setSelected(false);
		}

		public void actionPerformed(ActionEvent e) {
			if (validButton.isSelected()) {
				correctLabel
						.setText(MainWindow.arg.checkValidity(true) ? " Correct!"
								: " Incorrect.");

			} else if (invalidButton.isSelected()) {
				correctLabel
						.setText(MainWindow.arg.checkValidity(false) ? " Correct!"
								: " Incorrect.");
			}

			if (!MainWindow.vennDisp.checkDiagram()) {
				MainWindow.vennDisp.showSolution();
			}
		}

	}

	public void stateChanged(ChangeEvent arg0) {
		switch (this.getSelectedIndex()) {
		case 0:
			MainWindow.loadNextSent();
			break;
		case 1:
			MainWindow.loadNextSentWComp();
			break;
		case 2:
			MainWindow.loadNextImmInfer();
			break;
		case 3:
			MainWindow.loadNextImmInferWComp();
			break;
		case 4:
			MainWindow.loadNextSyllogism();
			break;
		}
	}
}