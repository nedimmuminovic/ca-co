package com.nedim.calculator;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	// controls enable/disable of dot button
	static boolean enableButton;
	
	// counts how many times left parenthesis is pressed 
	int leftCounter;
	int rightCounter;
	
	//TextView result;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		
		enableButton = true;
		leftCounter = 0;
		rightCounter = 0;
		//result = (TextView) findViewById(R.id.textView1);
		//
		
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

	}

	public void buttonClicked(View v) {

		Button b = (Button) v;
		String buttonText = b.getText().toString();

		TextView result = (TextView) findViewById(R.id.textView1);
		result.append(buttonText);

		Button btn = (Button) findViewById(R.id.button19);

		if (enableButton)

			btn.setEnabled(true);
		enableDisableOperator(true);

	}

	public void handleDot(View v) {
		// put dot and disable button

		// set the text of button into a text edit

		// if there is a number on left put it in textbox

		// Button b = (Button) v;
		TextView result = (TextView) findViewById(R.id.textView1);

		String userExpression = result.getText().toString();
		int lastIndex = userExpression.length() - 1;
		char lastCharacter = userExpression.charAt(lastIndex);

		// fix this line of code
		// enable button must be updated to true
		// from other method
		if (Character.isDigit(lastCharacter) && enableButton) {

			// disable button
			Button btn = (Button) findViewById(R.id.button19);
			btn.setEnabled(false);

			result.append(".");

			enableButton = false;

		}
		// String buttonText = b.getText().toString();

	}

	// Enables or disables operator buttons
	public void enableDisableOperator (boolean enabled){
		
		final int[] BUTTONS = {
			    R.id.button13,
			    R.id.button14,
			    R.id.button15,
			    R.id.button16
			};
			
			
			for (int i=0; i<BUTTONS.length; i++) {
				
			Button btn = (Button) findViewById(BUTTONS[i]);
			btn.setEnabled(enabled);
			}
			
		
	}
	public void handleOperator(View v) {

		buttonClicked(v);
		enableButton = true;
		enableDisableOperator(false);

	}
	
	public void handleParentheses(View v) {
		
		TextView result = (TextView) findViewById(R.id.textView1);
		
		if (v.getId() == R.id.button12) {
			
			result.append(")");
			leftCounter++;
		}
		else {
			result.append("(");
			rightCounter++;
		}
		
		
	}

	public void calculate(View v) throws IllegalArgumentException {

		if (leftCounter != rightCounter) {
			Toast.makeText(getBaseContext(), "Check your parentheses!", Toast.LENGTH_LONG).show();
			return;
		}
		TextView result = (TextView) findViewById(R.id.textView1);

		enableButton = true;

		if (!result.getText().toString().equals("")) {

			Calculator calc = new Calculator(result.getText().toString(),
					this.getApplicationContext());

			while (calc.hasMoreLexems()) {
				calc.lexems.add(calc.nextLexem());
			}

			// show result to the user
			try {

				Double myResult = calc.expression();
				result.setText(String.valueOf(myResult));

			} catch (IllegalArgumentException e) {

				// Toast.makeText(getBaseContext(), "Check your expression",
				// Toast.LENGTH_LONG);

			}

		} else
			Toast.makeText(this, "Please enter an expression",
					Toast.LENGTH_LONG).show();
	}

	public void clearCalculation(View v) {
		
		TextView result = (TextView) findViewById(R.id.textView1);
		Button minusOperator = (Button) findViewById(R.id.button14);
		Button dot = (Button) findViewById(R.id.button19);
		enableDisableOperator(false);
		
		
		// FIX: enables and disables minus too many times
		minusOperator.setEnabled(true);
		dot.setEnabled(true);
		
		// controls 
		enableButton = true;
		
		result.setText("");
		
		
	}
	
	public void converter(View v) {
		
		Intent converterIntent = new Intent(this,ConverterIntent.class);
		startActivity(converterIntent);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);

			return rootView;
		}
	}

}
