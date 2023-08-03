/*      
 * Filename: ProblemBank.java
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
import java.util.List;

/**
 * Holds a collection of problems of each type
 */
public class ProblemBank {
	// collection of doubles and triples (groups of set names)
	private static final List<String[]> doubles = new ArrayList<String[]>();
	private static final List<String[]> triples = new ArrayList<String[]>();
	// the four possible forms
	public static final int[] possibleForms = { 'A', 'E', 'I', 'O' };

	private static int doubleNo = 0;
	private static int tripleNo = 0;

	static {
		doubles.add(new String[] { "Trucks", "Vehicles" });
		doubles.add(new String[] { "Polygons", "Squares" });
		doubles.add(new String[] { "Birds", "Vertebrates" });
		doubles.add(new String[] { "Computers", "Humans" });
		doubles.add(new String[] { "Buildings", "Houses" });
		doubles.add(new String[] { "Atoms", "Elements" });
		doubles.add(new String[] { "Boats", "Ships" });
		doubles.add(new String[] { "Runners", "Joggers" });
		doubles.add(new String[] { "Oaks", "Trees" });
		doubles.add(new String[] { "Amphibians", "Frogs" });
		doubles.add(new String[] { "Cars", "Trucks" });
		doubles.add(new String[] { "Isotopes", "Atoms" });
		doubles.add(new String[] { "Sloops", "Boats" });
		doubles.add(new String[] { "Egotists", "Paupers" });
		doubles.add(new String[] { "Animals", "Amphibians" });
		doubles.add(new String[] { "Wizards", "Warlocks" });
		doubles.add(new String[] { "Cities", "Villages" });
		doubles.add(new String[] { "Cities", "Towns" });
		doubles.add(new String[] { "Sparrows", "Vertebrates" });
		doubles.add(new String[] { "Humans", "Thinkers" });
		doubles.add(new String[] { "Hills", "Volcanoes" });
		doubles.add(new String[] { "Marathoners", "Runners" });
		doubles.add(new String[] { "Runners", "Joggers" });
		doubles.add(new String[] { "Smokers", "Athletes" });
		doubles.add(new String[] { "Mountains", "Volcanoes" });
		doubles.add(new String[] { "Doctors", "Athletes" });
		doubles.add(new String[] { "Houses", "Apartments" });
		doubles.add(new String[] { "Comets", "Meteors" });
		doubles.add(new String[] { "Egotists", "Artists" });
		doubles.add(new String[] { "Trees", "Elms" });
		doubles.add(new String[] { "Asteroids", "Comets" });
		doubles.add(new String[] { "Fanatics", "Idealists" });
		doubles.add(new String[] { "Reformers", "Fanatics" });
		doubles.add(new String[] { "Elms", "Oaks" });
		doubles.add(new String[] { "Lawyers", "Liars" });
		doubles.add(new String[] { "Greek", "Men" });
		doubles.add(new String[] { "Frogs", "Amphibians" });
		doubles.add(new String[] { "A", "B" });
		doubles.add(new String[] { "Squares", "Rectangles" });
		doubles.add(new String[] { "Politicians", "Lawyers" });
		doubles.add(new String[] { "Birds", "Sparrows" });
	}

	// add some triples for syllogism
	static {
		triples.add(new String[] { "Amphibians", "Frogs", "Animals" });
		triples.add(new String[] { "Houses", "Apartments", "Buildings" });
		triples.add(new String[] { "Boats", "Ships", "Sloops" });
		triples.add(new String[] { "A", "B", "C" });
		triples.add(new String[] { "Squares", "Rectangles", "Polygons" });
		triples.add(new String[] { "Smokers", "Doctors", "Athletes" });
		triples.add(new String[] { "Wizards", "Warlocks", "Witches" });
		triples.add(new String[] { "Vertebrates", "Birds", "Sparrows" });
		triples.add(new String[] { "Mountains", "Hills", "Volcanoes" });
		triples.add(new String[] { "Books", "Magazines", "Pamphlets" });
		triples.add(new String[] { "Men", "Greeks", "Athenians" });
		triples.add(new String[] { "Towns", "Cities", "Villages" });
	}

	public String[] nextNames(int noOfNames) {
		String[] nextNames = null;
		if (noOfNames == 3) {
			if (tripleNo == triples.size()) {
				tripleNo = 0;
			}
			nextNames = triples.get(tripleNo);
			tripleNo++;
		} else {
			if (doubleNo == doubles.size()) {
				doubleNo = 0;
			}
			nextNames = doubles.get(doubleNo);
			doubleNo++;
		}
		return nextNames;
	}

	public static int randomForm() {
		int randomIndex = ((int) (Math.random() * 100)) % 4;
		return possibleForms[randomIndex];
	}

	public static int[] randomForms(int noOfForms) {
		int[] forms = new int[noOfForms];
		for (int i = 0; i < forms.length; i++) {
			forms[i] = randomForm();
		}
		return forms;
	}

	public static int[] randomCompPair() {
		int[] result = new int[2];
		for (int i = 0; i < result.length; i++) {
			result[i] = ((int) (Math.random() * 100)) % 2;
		}
		return result;
	}

	public static int[][] randomCompPairs(int noOfPairs) {
		int[][] result = new int[noOfPairs][2];
		for (int i = 0; i < result.length; i++) {
			result[i] = randomCompPair();
		}
		return result;
	}

	public LogicalArgument nextSentence() {
		LogicalArgument arg = new Sentence(nextNames(2), randomForm(),
				new int[] { 0, 0 });
		return arg;
	}

	public LogicalArgument nextSentWComp() {
		LogicalArgument arg = new Sentence(nextNames(2), randomForm(),
				randomCompPair());

		return arg;

	}

	/*
	 * Used when loading immediate inference (and syllogism) problems. Decides
	 * whether to produce a valid or an invalid immediate inference argument (or
	 * valid syllogism). Roughly 50-50 chance of valid-invalid.
	 * 
	 * @return true if next argument should be valid, false if next argument
	 * should be invalid
	 */
	public boolean tossACoin() {
		int randomInteger = (int) (Math.random() * 1000);
		// chance of this "randomInteger" being divisible by even or odd is
		// 50-50
		boolean evenInteger = randomInteger % 2 == 0 ? true : false;
		if (evenInteger) {
			return true;
		} else {
			return false;
		}
	}

	public LogicalArgument nextImmInfer() {
		LogicalArgument arg;
		do {
			arg = new ImmediateInference(nextNames(2), randomForms(2),
					new int[][] { { 0, 0 }, { 0, 0 } },
					((int) (Math.random() * 100)) % 2 == 0 ? true : false);
		} while ((arg.getSenses()[0] == arg.getSenses()[1])
				&& (!arg.isConclSwapped())); // to make sure that both
		// sentences are not the same!
		return arg;
	}

	public LogicalArgument nextImmInferWComp() {
		LogicalArgument arg;
		boolean isNextProbValid = tossACoin();
		for (;;) {
			arg = new ImmediateInference(nextNames(2), randomForms(2),
					randomCompPairs(2),
					((int) (Math.random() * 100)) % 2 == 0 ? true : false);
			if (arg.checkValidity(isNextProbValid)) {
				break;
			}
		}
		return arg;

	}

	public LogicalArgument nextSyllogism() {
		LogicalArgument arg;
		boolean isNextProbValid = tossACoin();
		for (;;) {
			arg = new Syllogism(nextNames(3), randomForms(3),
					((int) (100 * Math.random())) % 4);
			if (arg.checkValidity(isNextProbValid)) {
				break;
			}
		}
		return arg;
	}
}