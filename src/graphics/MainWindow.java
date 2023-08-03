/*      
 * Filename: MainWindow.java
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;

import analysis.LogicalArgument;
import analysis.ProblemBank;

/**
 * The main class of the application. Encapsulates the different components of
 * the GUI.
 * 
 * @author Dhrubo Jyoti
 * @version 5.0 09-Jun-2008
 */
public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	static final ProblemBank problemBank = new ProblemBank();
	static VennDisplay vennDisp = null;
	static ArgDisplay argDisp = null;

	// the current problem
	public static LogicalArgument arg = problemBank.nextSentence();

	public static Color BACKGROUND_COLOR = null;

	static final JMenuBar menuBar = new JMenuBar();
	static final JMenu helpMenu = new JMenu("Help");

	static Image noneImage = null;
	static Image someImage = null;
	static Image unknownImage = null;
	static Image iconImage = null;

	protected JFrame aboutBox;

	// Check that we are on Mac OS X. This is crucial to loading and using the
	// OSXAdapter class.
	public static final boolean MAC_OS_X = (System.getProperty("os.name")
			.toLowerCase().startsWith("mac"));

	// Ask AWT which menu modifier we should be using.
	final static int MENU_MASK = Toolkit.getDefaultToolkit()
			.getMenuShortcutKeyMask();

	public MainWindow() throws Exception {
		// set look and feel

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException uex) {
		} catch (ClassNotFoundException cex) {
		} catch (InstantiationException iex) {
		} catch (IllegalAccessException iaex) {
		}

		// get the image files
		try {
			URL noneUrl = this.getClass().getResource("none.gif");
			noneImage = Toolkit.getDefaultToolkit().getImage(noneUrl);

			URL someUrl = this.getClass().getResource("some.gif");
			someImage = Toolkit.getDefaultToolkit().getImage(someUrl);

			URL unknownUrl = this.getClass().getResource("unknown.gif");
			unknownImage = Toolkit.getDefaultToolkit().getImage(unknownUrl);

			URL iconURL = this.getClass().getResource("thinker.png");
			iconImage = Toolkit.getDefaultToolkit().getImage(iconURL);
		} catch (Exception e) {
			System.err.println("Can't file cursor images.");
		}
		if (!MAC_OS_X) {
			this.setIconImage(iconImage);
		}
		this.setTitle("Venn");
		this.setLayout(new BorderLayout());
		argDisp = new ArgDisplay(this);
		vennDisp = new VennDisplay();
		this.add(argDisp, BorderLayout.NORTH);
		this.add(vennDisp, BorderLayout.CENTER);

		this.addMenus();
		JLabel copyrightLabel = new JLabel(
				"Copyright © 1984-2008, Trustees of Dartmouth College. All rights reserved.");
		// copyrightLabel.setPreferredSize(new Dimension(700, 20));
		this.add(copyrightLabel, BorderLayout.SOUTH);
		BACKGROUND_COLOR = this.getBackground();
		this.pack();
		this.setPreferredSize(new Dimension(950, 650));
		this.setResizable(true);
		// useFullScreen(this);
		centerOnScreen(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

	}

	public void addMenus() {
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		JMenuItem print = new JMenuItem("Print");
		print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, MENU_MASK));
		print.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PrintUtilities.printComponent(MainWindow.this);
			}
		});
		fileMenu.add(print);
		// Quit menu items are provided on Mac OS X; only add your own on
		// other platforms
		if (!MAC_OS_X) {
			fileMenu.addSeparator();
			JMenuItem quit = new JMenuItem("Exit");
			quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,
					MENU_MASK));
			quit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (quit()) {
						System.exit(0);
					}
				}
			});
			fileMenu.add(quit);
		}

		// then the edit menu
		JMenu edit = new JMenu("Edit");
		JMenuItem changeNames = new ChangeClassNamesButton("Change Class Names");
		edit.add(changeNames);
		changeNames.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));

		// then add help menu
		createHelpMenuItems();
		if (!MAC_OS_X) {
			helpMenu.addSeparator();
			this.setUpHelpMenuItem("About Venn");
		} else {
			// Set up our application to respond to the Mac OS X application
			// menu
			registerForMacOSXEvents();
		}

		// now add file and edit to menu bar
		menuBar.add(fileMenu);
		menuBar.add(edit);
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);
	}

	// Generic registration with the Mac OS X application menu
	// Checks the platform, then attempts to register with the Apple EAWT
	// See OSXAdapter.java to see how this is done without directly referencing
	// any Apple APIs
	public void registerForMacOSXEvents() {
		if (MAC_OS_X) {
			try {
				// Generate and register the OSXAdapter, passing it a hash of
				// all the methods we wish to
				// use as delegates for various
				// com.apple.eawt.ApplicationListener methods
				OSXAdapter.setQuitHandler(this, getClass().getDeclaredMethod(
						"quit", (Class[]) null));
				OSXAdapter.setAboutHandler(this, getClass().getDeclaredMethod(
						"about", (Class[]) null));
			} catch (Exception e) {
				System.err.println("Error while loading the OSXAdapter:");
				e.printStackTrace();
			}
		}
	}

	// General info dialog; fed to the OSXAdapter as the method to call when
	// "About OSXAdapter" is selected from the application menu
	public void about() {
		aboutBox = new JFrame("About Venn");
		URL url = MainWindow.this.getClass().getResource("About Venn.htm");
		aboutBox.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JTextPane text = new JTextPane();
		try {
			text.setPage(url);
		} catch (IOException e) {
			System.out.println("Error loading URL: " + url);
		}
		text.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(text);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(500, 300));
		scrollPane.setViewportBorder(new LineBorder(Color.WHITE, 10));
		aboutBox.setLayout(new FlowLayout(FlowLayout.LEFT));
		aboutBox.add(new JLabel(new ImageIcon(iconImage)));
		aboutBox.add(scrollPane);
		aboutBox.pack();
		centerOnScreen(aboutBox);
		aboutBox.setResizable(true);
		aboutBox.setVisible(true);
	}

	public static void centerOnScreen(Component c) {
		// Center on screen
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((d.getWidth() - c.getWidth()) / 2);
		int y = (int) ((d.getHeight() - c.getHeight()) / 2);
		c.setLocation(x, y);
	}

	public void createHelpMenuItems() {
		setUpHelpMenuItem("Introduction");
		setUpHelpMenuItem("Overview");
		setUpHelpMenuItem("Sentence Form (AEIO)");
		setUpHelpMenuItem("Complement");
		setUpHelpMenuItem("Figure and Mood");
		setUpHelpMenuItem("Marking Venn Diagrams");
		setUpHelpMenuItem("None");
		setUpHelpMenuItem("Some");
		setUpHelpMenuItem("Correction");
		setUpHelpMenuItem("Using Venn Diagrams");
		setUpHelpMenuItem("Valid-Invalid");
		setUpHelpMenuItem("Getting New Problems");
	}

	public void setUpHelpMenuItem(final String fileName) {
		JMenuItem item = new JMenuItem(fileName);

		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				URL url = MainWindow.this.getClass().getResource(
						fileName + ".htm");
				JFrame popup = new JFrame("Venn - Help - " + fileName);
				popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				JTextPane text = new JTextPane();
				try {
					text.setPage(url);
				} catch (IOException e) {
					System.out.println("Error loading URL: " + url);
				}
				text.setEditable(false);
				JScrollPane scrollPane = new JScrollPane(text);
				scrollPane
						.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane
						.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				scrollPane.setPreferredSize(new Dimension(550, 400));
				scrollPane.setViewportBorder(new LineBorder(Color.WHITE, 10));
				if (!MAC_OS_X) {
					popup.setIconImage(iconImage);
				}
				// popup.setPreferredSize(new Dimension(600, 400));
				popup.setLayout(new FlowLayout(FlowLayout.LEFT));
				if (fileName.equals("About Venn")) {
					popup.add(new JLabel(new ImageIcon(iconImage)));
				}
				popup.add(scrollPane);
				popup.pack();
				centerOnScreen(popup);
				popup.setResizable(true);
				popup.setVisible(true);
			}
		});

		if (fileName.equals("Unknown")) {
			item.setIcon(new ImageIcon(unknownImage));
		} else if (fileName.equals("Some")) {
			item.setIcon(new ImageIcon(someImage));
		} else if (fileName.equals("None")) {
			item.setIcon(new ImageIcon(noneImage));
		}

		helpMenu.add(item);
	}

	public boolean quit() {
		int option = JOptionPane.showConfirmDialog(this,
				"Are you sure you want to quit?", "Quit?",
				JOptionPane.YES_NO_OPTION);
		return (option == JOptionPane.YES_OPTION);
	}

	public static void loadNextSent() {
		arg = problemBank.nextSentence();
		argDisp.refreshDisplay(0);
		vennDisp.refreshDisplay();
	}

	public static void loadNextSentWComp() {
		arg = problemBank.nextSentWComp();
		argDisp.refreshDisplay(1);
		vennDisp.refreshDisplay();
	}

	public static void loadNextImmInfer() {
		arg = problemBank.nextImmInfer();
		argDisp.refreshDisplay(2);
		vennDisp.refreshDisplay();
	}

	public static void loadNextImmInferWComp() {
		arg = problemBank.nextImmInferWComp();
		argDisp.refreshDisplay(3);
		vennDisp.refreshDisplay();
	}

	public static void loadNextSyllogism() {
		arg = problemBank.nextSyllogism();
		argDisp.refreshDisplay(4);
		vennDisp.refreshDisplay();
	}

	public static void refreshVenn() {
		vennDisp.refreshDisplay();
	}

	public class ChangeClassNamesButton extends JMenuItem implements
			ActionListener, KeyListener {
		private static final long serialVersionUID = 1L;

		MyTextField[] textFields = null;
		JButton ok = null;

		public ChangeClassNamesButton(String s) {
			this.addActionListener(this);
			this.setText(s);
		}

		public void actionPerformed(ActionEvent arg0) {
			int noOfFields = MainWindow.arg.getNoOfSenses() == 3 ? 3 : 2;
			textFields = new MyTextField[noOfFields];
			JLabel[] labels = new JLabel[noOfFields];
			String[] textFieldsInitVal = MainWindow.arg.getNamesOfSets();

			if (noOfFields == 3) {
				labels[0] = new JLabel("Set S: ");
				labels[1] = new JLabel("Set P: ");
				labels[2] = new JLabel("Set M: ");
			} else {
				labels[0] = new JLabel("Set 1: ");
				labels[1] = new JLabel("Set 2: ");
			}
			final int paneIndex = ArgDisplay.paneIndex;
			final JDialog dialog = new JDialog(MainWindow.this, "Change Names",
					true);
			JPanel textBoxesPanel = new JPanel();
			textBoxesPanel.setLayout(new BoxLayout(textBoxesPanel,
					BoxLayout.Y_AXIS));
			for (int i = 0; i < textFields.length; i++) {
				JPanel textBoxPanel = new JPanel(new FlowLayout(
						FlowLayout.LEFT, 20, 20));
				textBoxPanel.add(labels[i]);
				labels[i].setFocusable(false);
				textFields[i] = new MyTextField(textFieldsInitVal[i]);
				textFields[i].setColumns(20);
				textFields[i].setSelectionStart(0);
				textFields[i].setSelectionEnd(textFields[i].getColumns());
				textFields[i]
						.setToolTipText("Use the Tab key to traverse between text fields.");
				textFields[i].addKeyListener(this);
				textBoxPanel.add(textFields[i]);
				textBoxesPanel.add(textBoxPanel);
			}
			dialog.add(textBoxesPanel, BorderLayout.CENTER);
			ok = new JButton("OK");
			JButton cancel = new JButton("Cancel");
			ok.setPreferredSize(new Dimension(150, 30));
			cancel.setPreferredSize(new Dimension(150, 30));

			ok.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String[] newValues = MainWindow.arg.getNamesOfSets();
					for (int i = 0; i < newValues.length; i++) {
						String s = textFields[i].getText();
						newValues[i] = s != null && !s.equals("") ? s
								: newValues[i];
					}
					MainWindow.arg.setSetNames(newValues);
					MainWindow.argDisp.refreshDisplay(paneIndex);
					MainWindow.vennDisp.refreshDisplay();
					dialog.dispose();
				}
			});

			cancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dialog.dispose();
				}
			});

			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			buttonPanel.add(ok);
			buttonPanel.add(cancel);

			dialog.add(buttonPanel, BorderLayout.SOUTH);
			textFields[0].requestFocus();
			dialog.setFocusCycleRoot(true);
			dialog.setSize(new Dimension(350, 100 + 50 * noOfFields));
			dialog.setResizable(false);

			// Center on screen
			Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
			int x = (int) ((d.getWidth() - dialog.getWidth()) / 2);
			int y = (int) ((d.getHeight() - dialog.getHeight()) / 2);
			dialog.setLocation(x, y);
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
		}

		public void keyPressed(KeyEvent arg0) {
		}

		public void keyReleased(KeyEvent arg0) {
			for (int i = 0; i < textFields.length; i++) {
				String s = textFields[i].getText();
				s.trim();
				if (s == null || s.length() == 0 || s.startsWith(" ")
						|| s.startsWith(" ")) {
					ok.setEnabled(false);
					return;
				}
			}
			ok.setEnabled(true);
			return;
		}

		public void keyTyped(KeyEvent arg0) {
		}
	}

	public static void main(String[] args) throws Exception {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					// take the menu bar off the jframe
					System.setProperty("apple.laf.useScreenMenuBar", "true");
					// set the name of the application menu item
					System.setProperty(
							"com.apple.mrj.application.apple.menu.about.name",
							"Venn");
					new MainWindow();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(e);
				}
			}
		});
	}

}