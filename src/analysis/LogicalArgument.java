/*      
 * Filename: LogicalArgument.java
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


public interface LogicalArgument {
	public int value = 0;

	public int getNoOfSets();

	public String[] getNamesOfSets();

	public int[] computeSolution();

	public int getNoOfRegions();

	public int[] getSenses();

	public int[][] getComplements();

	public void setSenses(int[] newSenses);
	
	public void setComplements(int [] [] complements);
	
	public int getNoOfSenses();
	
	public int getFigureIndex();
	
	public void setFigureIndex(int figure);
	
	public boolean checkValidity(boolean isValid);
	
	public boolean isConclSwapped();
	
	public void setSwapConcl(boolean swapConcl);

	public int[] computeConclusion();
	
	public String [] getSetNames();
	
	public void setSetNames(String [] names);
}