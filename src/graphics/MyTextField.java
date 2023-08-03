/*      
 * Filename: MyTextField.java
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

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * TextField that accepts only a certain number of characters.
 * <p>
 * This reduces the validation required on the field after entry, and gives a
 * nicer entry field to the end user - if they cannot even enter invalid values,
 * they will not be plagued by entering data only to have it over-ruled by the
 * validating methods.
 * 
 * @author Dhrubo Jyoti
 * @version 1.0 25-Jun-2008
 */
public class MyTextField extends JTextField {
	private static final long serialVersionUID = 1L;
	/**
	 * The maximum number of characters that can be entered into this field.
	 */
	private int maxCapacity = 12;

	/**
	 * Constructs a new empty MyTextField with the specified number of columns
	 * and specified maximum capacity .
	 * 
	 * @param columns
	 *            the number of columns to use to calculate the preferred width;
	 *            if columns is set to zero, the preferred width will be
	 *            whatever naturally results from the component implementation
	 */
	public MyTextField(int columns, int maxCapacity) {
		super(columns);
		this.maxCapacity = maxCapacity;
	}

	public MyTextField(String string) {
		if (string.length() > 12) {
			this.setText(string.substring(0, 12));
		} else {
			this.setText(string);
		}
	}

	/**
	 * Creates the default implementation of the model to be used at
	 * construction. An instance of TextDocument is returned.
	 * 
	 * @return TextDocument a document which only allows positive integers
	 */
	protected Document createDefaultModel() {
		return new TextDocument();
	}

	/**
	 * A document that only allows positive integer numbers to be entered.
	 * <p>
	 */
	class TextDocument extends PlainDocument {
		private static final long serialVersionUID = 1L;

		/**
		 * {@inheritDoc}
		 */
		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {
			if (str == null) {
				return;
			}
			
			if (MyTextField.this.getText().length() < maxCapacity) {
				super.insertString(offs, str, a);
			}
		}
	}
}
