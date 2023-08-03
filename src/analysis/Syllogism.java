/*      
 * Filename: Syllogism.java
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Syllogism extends Sentence {
	private int figureIndex = 0;

	public Syllogism(int[] senses) {
		this(new String[] { "S", "P", "M" }, senses, 0);
	}

	public Syllogism(int[] senses, int figure) {
		this(new String[] { "S", "P", "M" }, senses, figure);
	}

	public Syllogism(String[] namesOfSets, int[] senses, int figureIndex) {
		super(3, 3, 17);
		this.namesOfSets = namesOfSets;
		this.senses = new int[3];
		this.complements = new int[][] { { 0, 0 }, { 0, 0 }, { 0, 0 } };
		for (int i = 0; i < senses.length; i++) {
			this.senses[i] = senses[i];
		}
		this.figureIndex = figureIndex;
	}

	public int getFigureIndex() {
		return figureIndex;
	}

	@Override
	public boolean checkValidity(boolean userResponse) {
		boolean isValid = false;

		int[] premises = computeSolution();
		int[] conclusion = computeConclusion();

		Set<Integer> premisesNone = new HashSet<Integer>();
		Set<Integer> premisesSome = new HashSet<Integer>();
		Set<Integer> conclusionNone = new HashSet<Integer>();
		Set<Integer> conclusionSome = new HashSet<Integer>();

		for (int i = 0; i < premises.length; i++) {
			if (premises[i] < 0) {
				premisesNone.add(premises[i]);
			} else {
				premisesSome.add(premises[i]);
			}
		}

		for (int i = 0; i < conclusion.length; i++) {
			if (conclusion[i] < 0) {
				conclusionNone.add(conclusion[i]);
			} else {
				Set<Integer> singleDigits = toSingleDigits(conclusion[i]);
				conclusionSome.addAll(singleDigits);
			}
		}

		if (senses[2] == 'A' || senses[2] == 'E') {

			// SEE IF CONTRADICTS WITH PREM SOME
			for (Integer i : conclusionNone) {
				if (premisesSome.contains(-i)) {
					return userResponse == isValid;
				}
			}

			// SEE IF SUBSET OF PREM NONE
			conclusionNone.removeAll(premisesNone);
			if (!conclusionNone.isEmpty()) {
				return userResponse == isValid;
			}

			// IF PASSES BOTH ABOVE TESTS, THEN TRUE
			isValid = true;
			return userResponse == isValid;
		} else {
			// WE KNOW CONCLU IS EITHER I OR O

			// SEE IF CONTRADICTS WITH PREMISE NONE
			if (!premisesNone.isEmpty()) {
				for (Integer i : conclusionSome) {
					if (premisesNone.contains(-i)) {
						return userResponse == isValid;
					}
				}
			}

			// SEE IF THERE IS INTERSECTION BETWEEN AT LEAST ONE CONCLU SOME AND
			// A PREM SOME
			if (!premisesSome.isEmpty()) {
				for (Integer i : conclusionSome) {
					if (premisesSome.contains(i)) {
						isValid = true;
						return userResponse == isValid;
					}
				}
				return userResponse == isValid;
			} else {
				if (!conclusionSome.isEmpty()) {
					return userResponse == isValid;
				}
			}

			// IF PASSES BOTH ABOVE TESTS, THEN TRUE
			isValid = true;
			return userResponse == isValid;
		}
	}

	@Override
	public int[] computeConclusion() {
		// get the required data
		int[] data = compute(2);

		// uses a set to sort the data into ascending order
		SortedSet<Integer> set = new TreeSet<Integer>();

		for (int i = 0; i < data.length; i++) {
			set.add(data[i]);
		}

		data[0] = set.first();
		data[1] = set.last();

		return data;
	}

	public Set<Integer> toSingleDigits(int doubleDigitNo) {
		String s = Integer.toString(doubleDigitNo);
		Set<Integer> singleDigits = new HashSet<Integer>();
		singleDigits.add(Integer.parseInt(s.substring(0, 1)));
		singleDigits.add(Integer.parseInt(s.substring(s.length() - 1, s
				.length())));
		return singleDigits;
	}

	public boolean containsOnlyAorE(List<Integer> sensesList) {
		List<Integer> tester = new ArrayList<Integer>();
		tester.add(new Integer('A'));
		tester.add(new Integer('A'));
		tester.add(new Integer('E'));
		tester.add(new Integer('E'));
		List<Integer> newList = new ArrayList<Integer>(sensesList);
		newList.removeAll(tester);
		if (newList.isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean containsOnlyIorO(List<Integer> sensesList) {
		List<Integer> tester = new ArrayList<Integer>();
		tester.add(new Integer('I'));
		tester.add(new Integer('I'));
		tester.add(new Integer('O'));
		tester.add(new Integer('O'));
		List<Integer> newList = new ArrayList<Integer>(sensesList);
		newList.removeAll(tester);
		if (newList.isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	public int[] computeSolution() {
		// get the required data
		int[] premise0 = compute(0);
		int[] premise1 = compute(1);

		// uses these sets to sort the data into ascending order
		SortedSet<Integer> p0 = new TreeSet<Integer>();
		SortedSet<Integer> p1 = new TreeSet<Integer>();

		for (int i = 0; i < premise0.length; i++) {
			p0.add(premise0[i]);
		}

		for (int i = 0; i < premise1.length; i++) {
			p1.add(premise1[i]);
		}

		premise0[0] = p0.first();
		premise0[1] = p0.last();
		premise1[0] = p1.first();
		premise1[1] = p1.last();
		/*
		 * for (int i = 0; i < premise0.length; i++) { premise0[i] = p0. }
		 * 
		 * for (int i = 0; i < premise1.length; i++) { premise1[i] =
		 * p1.iterator().next(); }
		 */

		Set<Integer> solution = new HashSet<Integer>();

		/*
		 * there are three possible situations: case 1: when the premises are
		 * some combination of A and E (i.e. AA, EE, AE, EA) case 2: when one of
		 * the premise is A or E, and the other is I or O. case 3: when both of
		 * the premises are I or O. The type of the markings on the diagram will
		 * be different in each situation.
		 * 
		 */
		int situation = 0;

		List<Integer> sensesList = new ArrayList<Integer>();

		sensesList.add(senses[0]);
		sensesList.add(senses[1]);

		if (containsOnlyAorE(sensesList)) {
			situation = 1;
		} else if (containsOnlyIorO(sensesList)) {
			situation = 3;
		} else {
			situation = 2;
		}

		// System.out.println("Situation: " + situation);

		switch (situation) {
		case 1:
			solution.add(premise0[0]);
			solution.add(premise0[1]);
			solution.add(premise1[0]);
			solution.add(premise1[1]);
			// System.out.println("Solution: " + solution);
			break;
		case 2:
			Set<Integer> prem0 = new HashSet<Integer>();
			Set<Integer> prem1 = new HashSet<Integer>();
			prem0.add(premise0[0]);
			prem0.add(premise0[1]);
			prem1.add(premise1[0]);
			prem1.add(premise1[1]);

			if (senses[0] == 'A' || senses[0] == 'E') {
				Set<Integer> intersection = new HashSet<Integer>();
				intersection.add(-premise0[0]);
				intersection.add(-premise0[1]);
				intersection.retainAll(prem1);
				if (intersection.isEmpty()) {
					solution.add(premise0[0]);
					solution.add(premise0[1]);
					StringBuffer s = new StringBuffer();
					s.append(premise1[0]);
					s.append(premise1[1]);
					solution.add(Integer.parseInt(s.toString()));
				} else {
					solution.addAll(prem1);
					solution.removeAll(intersection);
					solution.addAll(prem0);
				}
			} else if ((senses[1] == 'A' || senses[1] == 'E')) {
				Set<Integer> intersection = new HashSet<Integer>();
				intersection.add(-premise1[0]);
				intersection.add(-premise1[1]);
				intersection.retainAll(prem0);
				if (intersection.isEmpty()) {
					solution.add(premise1[0]);
					solution.add(premise1[1]);
					StringBuffer s = new StringBuffer();
					s.append(premise0[0]);
					s.append(premise0[1]);
					solution.add(Integer.parseInt(s.toString()));
				} else {
					solution.addAll(prem0);
					solution.removeAll(intersection);
					solution.addAll(prem1);
				}
			}
			break;
		case 3:
			StringBuffer s0 = new StringBuffer();
			StringBuffer s1 = new StringBuffer();

			s0.append(premise0[0]);
			s0.append(premise0[1]);
			s1.append(premise1[0]);
			s1.append(premise1[1]);

			solution.add(Integer.parseInt(s0.toString()));
			solution.add(Integer.parseInt(s1.toString()));
			break;
		}

		List<Integer> list = new ArrayList<Integer>(solution);
		int[] array = new int[list.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = list.remove(0);
		}

		return array;
	}

	public int[] compute(int sentIndex) {
		Set<Integer> setX = new HashSet<Integer>();
		Set<Integer> setY = new HashSet<Integer>();
		Set<Integer> result = new HashSet<Integer>();

		switch (sentIndex) {
		case 0:
			switch (figureIndex) {
			case 0: // setX is M and setY is P
				setX.add(3);
				setX.add(5);
				setX.add(6);
				setX.add(7);
				setY.add(2);
				setY.add(4);
				setY.add(5);
				setY.add(7);
				break;

			case 1: // setX is P and setY is M
				setX.add(2);
				setX.add(4);
				setX.add(5);
				setX.add(7);
				setY.add(3);
				setY.add(5);
				setY.add(6);
				setY.add(7);
				break;

			case 2: // setX is M and setY is P
				setX.add(3);
				setX.add(5);
				setX.add(6);
				setX.add(7);
				setY.add(2);
				setY.add(4);
				setY.add(5);
				setY.add(7);
				break;

			case 3: // setX is P and setY is M
				setX.add(2);
				setX.add(4);
				setX.add(5);
				setX.add(7);
				setY.add(3);
				setY.add(5);
				setY.add(6);
				setY.add(7);
				break;
			}
			break;

		case 1:
			switch (figureIndex) {
			case 0: // setX is S and setY is M
				setX.add(1);
				setX.add(4);
				setX.add(6);
				setX.add(7);
				setY.add(3);
				setY.add(5);
				setY.add(6);
				setY.add(7);
				break;

			case 1: // setX is S and setY is M
				setX.add(1);
				setX.add(4);
				setX.add(6);
				setX.add(7);
				setY.add(3);
				setY.add(5);
				setY.add(6);
				setY.add(7);
				break;

			case 2: // setX is M and setY is S
				setX.add(3);
				setX.add(5);
				setX.add(6);
				setX.add(7);
				setY.add(1);
				setY.add(4);
				setY.add(6);
				setY.add(7);
				break;

			case 3: // setX is M and setY is S
				setX.add(3);
				setX.add(5);
				setX.add(6);
				setX.add(7);
				setY.add(1);
				setY.add(4);
				setY.add(6);
				setY.add(7);
				break;
			}
			break;

		case 2: // setX is S and setY is P
			setX.add(1);
			setX.add(4);
			setX.add(6);
			setX.add(7);
			setY.add(2);
			setY.add(4);
			setY.add(5);
			setY.add(7);
			break;
		}

		switch (senses[sentIndex]) {
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

	@Override
	public int[][] getComplements() {
		return this.complements;
	}

	@Override
	public String[] getNamesOfSets() {
		return this.namesOfSets;
	}

	@Override
	public int getNoOfRegions() {
		return this.noOfRegions;
	}

	@Override
	public int getNoOfSenses() {
		return this.noOfSentences;
	}

	@Override
	public int getNoOfSets() {
		return this.noOfSets;
	}

	@Override
	public int[] getSenses() {
		return this.senses;
	}

	@Override
	public boolean isConclSwapped() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setComplements(int[][] complements) {
		this.complements = complements;

	}

	@Override
	public void setSenses(int[] newSenses) {
		this.senses = newSenses;

	}

	@Override
	public void setSwapConcl(boolean swapConcl) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFigureIndex(int figure) {
		this.figureIndex = figure;
	}

	public static void main(String[] args) {

		final int[] form = new int[] { 'A', 'E', 'I', 'O' };
		int count = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 4; k++) {
					for (int l = 0; l < 4; l++) {
						Syllogism s = new Syllogism(new String[] { "X", "Y",
								"Z" }, new int[] { form[l], form[j], form[k] },
								i);

						if (s.checkValidity(true)) {
							System.out.println((char) form[l] + " "
									+ (char) form[j] + " " + (char) form[k]
									+ " " + "FIGURE: " + i);
							count++;
						}
					}
				}
			}
		}

		System.out.println(count);

		/*
		 * Syllogism s = new Syllogism(new String[] { "X", "Y", "Z" }, new int[] {
		 * 'I', 'O', 'A' }, 2); int[] arr = s.computeSolution();
		 * System.out.println("ARR LENGTH: " + arr.length); for (int i = 0; i <
		 * arr.length; i++) { // System.out.println(arr[i]); }
		 *///
		// System.out.println(s.checkValidity(true));
	}
}