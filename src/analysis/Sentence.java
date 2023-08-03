/*      
 * Filename: Sentence.java
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

package analysis;

import java.util.*;

public class Sentence implements LogicalArgument {
	private static final long serialVersionUID = 1L;

	// The UNICODE math symbols
	public static final char SUBSET_OF = '\u2282';
	public static final char SUPERSET_OF = '\u2283';
	public static final char INTERSECTION = '\u22C2';
	public static final char UNION = '\u222A';
	public static final char COMPLEMENT = '\'';

	// The information for this particular problem.
	public int noOfSentences = 1;
	public int noOfSets = 2;
	public int noOfRegions = 4;
	public String[] namesOfSets = null;
	public int[] senses; // any one of A, E, I or O
	public int[][] complements = null;

	// Method that computes the solution.
	// Returns the region(s) that should be deleted and/or shaded.
	public int[] computeSolution() {
		Set<Integer> setX = new HashSet<Integer>();
		Set<Integer> setY = new HashSet<Integer>();
		Set<Integer> result = new HashSet<Integer>();

		if (complements[0][0] == 1) {
			setX.add(2);
			setX.add(4);
		} else {
			setX.add(1);
			setX.add(3);
		}

		if (complements[0][1] == 1) {
			setY.add(1);
			setY.add(4);
		} else {
			setY.add(2);
			setY.add(3);
		}

		switch (senses[0]) {
		case 'A':
			Set<Integer> temp = new HashSet<Integer>(setX);
			setX.retainAll(setY);
			temp.removeAll(setX);
			for (Integer i : temp) {
				result.add(-i.intValue());
			}
			break;
		case 'E':
			setX.retainAll(setY);
			for (Integer i : setX) {
				result.add(-i.intValue());
			}
			break;
		case 'I':
			setX.retainAll(setY);
			result = setX;
			break;
		case 'O':
			setX.removeAll(setY);
			result = setX;
			break;
		}
		// System.out.println("Inside SGC compSol: " + result);

		int[] array = new int[result.size()];
		int count = 0;
		for (Integer integer : result) {
			array[count] = integer;
			count++;
		}
		return array;
	}

	// used by subclasses
	public Sentence(int noOfSentences, int noOfSets) {
		this.noOfSentences = noOfSentences;
		this.noOfSets = noOfSets;
	}

	public Sentence(int noOfSentences, int noOfSets, int noOfRegions) {
		this.noOfSentences = noOfSentences;
		this.noOfSets = noOfSets;
		this.noOfRegions = noOfRegions;
	}

	public Sentence(int mode) {
		this(new String[] { "X", "Y" }, mode);
	}

	public Sentence(String[] namesOfSets, int mode) {
		this(namesOfSets, mode, new int[] { 0, 0 });
	}

	public Sentence(int mode, int[] complements) {
		this(new String[] { "X", "Y" }, mode, complements);
	}

	public Sentence(String[] namesOfSets, int mode, int[] complements) {
		this.namesOfSets = namesOfSets;
		this.senses = new int[] { mode };
		this.complements = new int[1][2];
		this.complements[0] = complements;
	}

	public String[] getNamesOfSets() {
		return namesOfSets;
	}

	public int getNoOfSets() {
		return noOfSets;
	}

	public int getNoOfRegions() {
		return noOfRegions;
	}

	public int[] getSenses() {
		return senses;
	}

	public int[][] getComplements() {
		return complements;
	}

	public void setSenses(int[] newSenses) {
		this.senses = newSenses;

	}

	public void setComplements(int[][] complements) {
		this.complements = complements;
	}

	public int getNoOfSenses() {
		return noOfSentences;
	}

	public boolean checkValidity(boolean isValid) {
		return false;
	}

	public boolean isConclSwapped() {
		return false;
	}

	public void setSwapConcl(boolean swapConcl) {
	}

	public int[] computeConclusion() {
		return null;
	}

	
	public int getFigureIndex() {
		return 0;
	}

	
	public void setFigureIndex(int figure) {

	}

	
	public String[] getSetNames() {
		return this.namesOfSets;
	}

	
	public void setSetNames(String[] names) {
		if (this.noOfSets == names.length) {
			this.namesOfSets = names;
		} else {
			System.err.println("Can't change names: Incorrect no. of names.");
		}

	}
}