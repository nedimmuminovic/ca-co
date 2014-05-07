package com.nedim.calculator;

import java.util.*;
import android.content.Context;
import android.widget.Toast;

/**
 * Class calculator performs basic arithmetic operations: addition,
 * multiplication, substraction and division. Calculations are performed using
 * recursive-descent parsing. Grammar: <expression> --> [ - ] <term> [(+|-)
 * <term>] <term> --> <factor> [( * | /) <factor> ] <factor> --> <number> |
 * (<expression>) <number> --> <digit> [ (. <number> | . <digit>)] <digit> -->
 * 0|1|2|3|4|5|6|7|8|9
 * 
 * @author Nedim Muminovic
 * @version 0.2.5
 */

public class Calculator {

	protected Queue<String> lexems;
	private int expressionIndex;
	private String expression;
	private Context context;

	public Calculator(String expression, Context context) {

		// initialise instance variables
		expressionIndex = 0;
		this.expression = expression;
		// holds list of lexems
		lexems = new LinkedList<String>();
		// used to show message to the user when error occurs while parsing
		// expression
		this.context = context;
	}

	/*
	 * Checks are there lexems left to store it in lexems linked list
	 * 
	 * @return Is there more lexems to be parsed
	 */

	public boolean hasMoreLexems() {
		return expressionIndex < expression.length();
	}

	/*
	 * Checks is a current lexem operator
	 * 
	 * @return True if lexem is operator else False
	 */
	public boolean isOperator(char lexem) {

		return lexem == '+' || lexem == '-' || lexem == '*' || lexem == '/';
	}

	/*
	 * Takes next lexem
	 */
	public String nextLexem() {

		int currentIndex = expressionIndex;
		char currentCharacter = expression.charAt(expressionIndex);
		if (currentCharacter == '(' || currentCharacter == ')'
				|| isOperator(currentCharacter)) {

			expressionIndex++;
			return expression.substring(currentIndex, expressionIndex);
		} else {
			// extracts multiple digit number
			while (hasMoreLexems()
					&& (Character.isDigit(expression.charAt(expressionIndex)) || expression
							.charAt(expressionIndex) == '.')) {
				expressionIndex++;
			}
		}
		return expression.substring(currentIndex, expressionIndex);
	}

	/*
	 * Performs addition and substraction of two numbers
	 * 
	 * @return The result of evaluating the whole expression by recurisvely
	 * calling other functions
	 * 
	 * @throws IllegalArgumentException when the input expression is not well
	 * formed
	 */
	public Double expression() throws IllegalArgumentException {

		boolean negativeValue = false;
		// char operator;
		if (lexems.peek().equals("-")) {
			lexems.remove();
			negativeValue = true;
		}
		Double result = term();
		if (negativeValue)
			result = (-1) * result;
		while ((lexems.peek() != null)
				&& ((lexems.peek().equals("+") || lexems.peek().equals("-")))) {

			String operator = lexems.remove();

			Double nextValue = term();

			if (nextValue == null) {
				Toast.makeText(context, "Operand is missing!",
						Toast.LENGTH_LONG).show();
				throw new IllegalArgumentException();
			}
			if (operator.equals("+")) {
				result = result + nextValue;
			} else {
				result = result - nextValue;
			}

		}

		return result;
	}

	/*
	 * Performs multiplication and division of two numbers
	 * 
	 * @ return Result of multiplication or division
	 * 
	 * @throws IllegalArgumentException if expression provided by user is not
	 * well formed
	 */
	public Double term() throws IllegalArgumentException {

		Double value = factor();
		Double nextValue = null;

		while ((lexems.peek() != null)
				&& (lexems.peek().equals("*") || lexems.peek().equals("/"))) {

			String operator = lexems.remove();
			nextValue = factor();

			if (nextValue == null) {
				Toast.makeText(context, "Operand is missing!",
						Toast.LENGTH_LONG).show();
				throw new IllegalArgumentException();

			}

			if (operator.equals("*"))
				value = value * nextValue;
			else
				value = value / nextValue;
		}

		return value;

	}

	/*
	 * Converts the number found in Queue of lexems to the double.
	 * 
	 * @throws IllegalArgumentException when expression is not well written by
	 * user
	 * 
	 * @return number
	 */
	public Double factor() throws IllegalArgumentException {

		Double value = 0.0;
		String lexem = lexems.poll();

		if (lexem == null)
			return null;

		if (Character.isDigit(lexem.charAt(0)))
			return Double.parseDouble(lexem);

		else if (lexem.equals("(")) {

			value = expression();
			lexems.remove();
			return value;
		}
		return value;
		
	}
	
}
