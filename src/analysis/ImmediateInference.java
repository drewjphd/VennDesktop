/*      
 * Filename: ImmediateInference.java
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

import java.util.HashSet;
import java.util.Set;

public class ImmediateInference extends Sentence {
	private boolean swapConcl = false;
	
	public ImmediateInference(int[] senses) {
		this(new String[] { "X", "Y" }, senses);
	}

	public ImmediateInference(String[] namesOfSets, int[] senses) {
		this(namesOfSets, senses, new int[][] { { 0, 0 }, { 0, 0 } }, false);
	}

	public ImmediateInference(int[] senses, int[][] complements) {
		this(new String[] { "X", "Y" }, senses, complements, false);
	}

	public ImmediateInference(String[] namesOfSets, int[] charModes,
			int[][] complements, boolean swapConcl) {
		super(2, 2);
		this.namesOfSets = namesOfSets;
		this.complements = complements;
		this.senses = new int[2];
		for (int i = 0; i < charModes.length; i++) {
			this.senses[i] = charModes[i];
		}
		this.swapConcl = swapConcl;
	}

	public int[] computeSolution() {
		return computePremise();
	}

	// Method that computes the premise.
	// Returns the region(s) that should be deleted and/or shaded.
	public int[] computePremise() {
		Set<Integer> setX = new HashSet<Integer>();
		Set<Integer> setY = new HashSet<Integer>();

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

		return compute(setX, setY, senses[0]);
	}

	public int[] computeConclusion() {
		Set<Integer> setX = new HashSet<Integer>();
		Set<Integer> setY = new HashSet<Integer>();

		if (complements[1][0] == 1) {
			setX.add(swapConcl ? 1 : 2);
			setX.add(4);
		} else {
			setX.add(swapConcl ? 2 : 1);
			setX.add(3);
		}

		if (complements[1][1] == 1) {
			setY.add(swapConcl == true ? 2 : 1);
			setY.add(4);
		} else {
			setY.add(swapConcl == true ? 1 : 2);
			setY.add(3);
		}

		return compute(setX, setY, senses[1]);
	}

	public int[] compute(Set<Integer> setX, Set<Integer> setY, int mode) {
		Set<Integer> result = new HashSet<Integer>();

		switch (mode) {
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

		int[] array = new int[result.size()];
		int count = 0;
		for (Integer integer : result) {
			array[count] = integer;
			count++;
		}
		return array;

	}

	/**
	 * Returns <code>true</code> if validity of conclusion and
	 * <code>isValid</code> matches, <code>false</code> otherwise.
	 */
	public boolean checkValidity(boolean isValid) {
		Set<Integer> premise = new HashSet<Integer>();
		Set<Integer> conclusion = new HashSet<Integer>();

		int[] premiseResults = computePremise();
		int[] conclusionResults = computeConclusion();

		for (int i : premiseResults) {
			premise.add(i);
		}

		for (int i : conclusionResults) {
			conclusion.add(i);
		}

		conclusion.removeAll(premise);

		boolean validity = conclusion.isEmpty() ? true : false;

		return validity == isValid;
	}
	
	public boolean isConclSwapped() {
		return swapConcl;
	}
	
	public void setSwapConcl(boolean swapConcl) {
		this.swapConcl = swapConcl;
	}
}