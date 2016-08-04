//*************************-=-=-=-=-=-=-=-=-=-=-=-=-**************************// 
//********************<         JAVA FX CALCULATOR        >*******************//
//*************************-=-=-=-=  V 2.2  -=-=-==-**************************//
//**************                     AUTHOR                     **************//
//---------------<_>------->>>   ALISTAIR COOPER   <<<-------<_>--------------//
//*****************<_>         CREATED: 07/05/2016          <_>***************//
//**************************-=-=-=-=-=-=-=-=-=-=-=-=-*************************//
//                                                                            //

package JavaFXCalculator_PKG;

import java.util.Random;
import org.apache.commons.math3.special.Gamma;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CalculatorMainClass extends Application {

	private String current = "0";
	private String stored = "0";
	private String binaryOp = "";
	private Label display = new Label(current);
	private Pane pane = new Pane(); // main pane for calculator
	private boolean clearOnNext = false;
	private double memory = 0.0; // value stored in calc memory
	
	// Order of operation
	//----------------------------------------//
	// first order = ( and )
	// second order = x\u005Ey and x\u221Ay (x to the power y and y root x)
	// third order = × and ÷
	// forth order = + and -
	//----------------------------------------//
	
	private double forthOrderOpValue = 0.0; // the result of prior 4th order ops
	private String forthOrderOp = ""; // + or - operator used before higher order ops
	private double thirdOrderOpValue = 0.0; // the result of prior 3rd order ops
	private String thirdOrderOp = ""; // × or ÷ operator used before higher order ops

	
	// Calculator modes
	private boolean exponentMode = false; // mode for entering exponents
	private boolean degMode = true; // Deg mode if true. Rad mode if false

	// Set the width and height of each cell in the grid.
	private int x_size = 60;
	private int y_size = 60;

	// Set the width and height between cells in the grid.
	private int x_spacer = 1;
	private int y_spacer = 1;

	// Set the x and y coordinates of the top left of the grid.
	private int x_orig = x_spacer;
	private int y_orig = y_spacer;

	// Set the style for the pane, display, and buttons.
	private String numberBtn_style = "-fx-border-color: grey; -fx-base: #D3D3D3; " + "-fx-font: 21 arial;";
	private String binaryStdBtn_style = "-fx-border-color: grey; -fx-base: #FFA500; "
			+ "-fx-font: 21 arial; -fx-text-fill: white;";
	private String otherBtn_style = "-fx-border-color: grey; -fx-base: #bdbdbd; " + "-fx-font: 15 arial;";
	private String display_style = "-fx-border-color: grey; -fx-background-color: #808080;"
			+ "-fx-text-fill: white; -fx-font: 28 arial;";
	private String pane_style = "-fx-border-color: black; -fx-background-color: #DCDCDC;";

	@Override // Override the start method in the Application class.
	public void start(Stage primaryStage) throws Exception {

		// Set the style for the main calculator pane
		pane.setStyle(pane_style);

		// Display: Row 0, Col 0/1/2/3
		display.setStyle(display_style);
		display.setAlignment(Pos.BASELINE_RIGHT);
		display.setLayoutX(x_orig + 0 * (x_size + x_spacer));
		display.setLayoutY(y_orig + 0 * (y_size + y_spacer));
		display.setPrefSize(x_size * 10 + x_spacer * 9, y_size);
		pane.getChildren().add(display);

		// 0 Button: Row 5, Col 6, is not double wide
		makeNumberButton("0", 5, 6, true);

		// 1 Button: Row 4, Col 6, is not double wide
		makeNumberButton("1", 4, 6, false);

		// 2 Button: Row 4, Col 7, is not double wide
		makeNumberButton("2", 4, 7, false);

		// 3 Button: Row 4, Col 8, is not double wide
		makeNumberButton("3", 4, 8, false);

		// 4 Button: Row 3, Col 6, is not double wide
		makeNumberButton("4", 3, 6, false);

		// 5 Button: Row 3, Col 7, is not double wide
		makeNumberButton("5", 3, 7, false);

		// 6 Button: Row 3, Col 8, is not double wide
		makeNumberButton("6", 3, 8, false);

		// 7 Button: Row 2, Col 6, is not double wide
		makeNumberButton("7", 2, 6, false);

		// 8 Button: Row 2, Col 7, is not double wide
		makeNumberButton("8", 2, 7, false);

		// 9 Button: Row 2, Col 8, is not double wide
		makeNumberButton("9", 2, 8, false);

		// . Button: Row 5, Col 8
		makeDecimalButton(5, 8);

		// + Button: Row 4, Col 9
		makeBinaryOpBtn("+", 4, 9);

		// - Button: Row 3, Col 9
		makeBinaryOpBtn("-", 3, 9);

		// × Button: Row 2, Col 9
		makeBinaryOpBtn("×", 2, 9);

		// ÷ Button: Row 1, Col 9
		makeBinaryOpBtn("÷", 1, 9);

		// % Button: Row 1, Col 8
		makePercentageBtn("%", 1, 8);

		// plus minus Button: Row 1, Col 7
		makeUnaryOpBtn("\u00B1", 1, 7);

		// C Button: Row 1, Col 6
		makeClearBtn(1, 6);

		// mc Button: Row 1, Col 2
		makeMemoryButton("mc", 1, 2);

		// m+ Button: Row 1, Col 3
		makeMemoryButton("m+", 1, 3);

		// m- Button: Row 1, Col 4
		makeMemoryButton("m-", 1, 4);

		// mr Button: Row 1, Col 5
		makeMemoryButton("mr", 1, 5);

		// x squared Button: Row 2, Col 1
		makeUnaryOpBtn("x\u00B2", 2, 1);

		// x cubed Button: Row 2, Col 2
		makeUnaryOpBtn("x\u00B3", 2, 2);
		
		// x to the power y Button: Row 2, Col 3
		makeBinaryOpBtn("x\u005Ey", 2, 3);

		// e to the power x Button: Row 2, Col 4
		makeUnaryOpBtn("e\u005Ex", 2, 4);

		// 10 to the power x Button: Row 2, Col 5
		makeUnaryOpBtn("10\u005Ex", 2, 5);

		// 1/x Button: Row 3, Col 0
		makeUnaryOpBtn("1/x", 3, 0);

		// square root Button: Row 3, Col 1
		makeUnaryOpBtn("\u221A", 3, 1);

		// cube root Button: Row 3, Col 2
		makeUnaryOpBtn("\u221B", 3, 2);
		
		// y to the root x Button: Row 3, Col 3
		makeBinaryOpBtn("y\u221Ax", 3, 3);

		// ln Button: Row 3, Col 4
		makeUnaryOpBtn("ln", 3, 4);

		// log10 Button: Row 3, Col 5
		makeUnaryOpBtn("log10", 3, 5);

		// x! Button: Row 4, Col 0
		makeUnaryOpBtn("x!", 4, 0);

		// sin Button: Row 4, Col 1
		makeUnaryOpBtn("sin", 4, 1);

		// cos Button: Row 4, Col 2
		makeUnaryOpBtn("cos", 4, 2);

		// tan Button: Row 4, Col 3
		makeUnaryOpBtn("tan", 4, 3);

		// e Button: Row 4, Col 4
		makeValueButton("e", 4, 4);

		// EE Button: Row 4, Col 5
		makeEEButton("EE", 4, 5);

		// DegRad Button: Row 5, Col 0
		makeDegRadButton("Rad", 5, 0);

		// sinh Button: Row 5, Col 1
		makeUnaryOpBtn("sinh", 5, 1);

		// cosh Button: Row 5, Col 2
		makeUnaryOpBtn("cosh", 5, 2);

		// tanh Button: Row 5, Col 3
		makeUnaryOpBtn("tanh", 5, 3);

		// pi  Button: Row 5, Col 4
		makeValueButton("\u03C0 ", 5, 4);

		// Rand Button: Row 5, Col 5
		makeValueButton("Rand", 5, 5);

		// = Button: Row 5, Col 9
		makeEqualsButton(5, 9);
		

		// Set the scene and the primary stage.
		Scene scene = new Scene(pane, x_size * 10 + x_spacer * 11, y_size * 6 + y_spacer * 7);
		primaryStage.setTitle("Calculator");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	/**
	 * makeNumberButton method make the number buttons
	 * @param number
	 * @param row
	 * @param column
	 * @param isDoubleWide
	 */
	private void makeNumberButton(String number, int row, int column, boolean isDoubleWide) {

		Button button = new Button(number);
		button.setStyle(numberBtn_style);
		button.setLayoutX(x_orig + column * (x_size + x_spacer));
		button.setLayoutY(y_orig + row * (y_size + y_spacer));

		if (isDoubleWide) {
			// for number zero
			button.setPrefSize(x_size * 2 + x_spacer, y_size); 
			
		} else {
			button.setPrefSize(x_size, y_size);
		}

		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				String newDisplay = ""; // string to update display

				// check if exponent mode is on
				if (exponentMode) {
					if (display.getText().contains("E 0")) {
						// if exponent hasn't been entered yet
						newDisplay = display.getText();
						display.setText(newDisplay.replace("0", number));
					} else {
						// else exponent has begun to be entered
						newDisplay = display.getText();
						display.setText(newDisplay += number);
					}
				} else {
					// exponent mode is not on
					if (clearOnNext == true) {
						current = number;
						clearOnNext = false;
					} else if (current.equals("0")) {
						current = number;
					} else {
						current += number;
					}
					display.setText(current);
				}
			}
		});
		pane.getChildren().add(button);
	}

	/**
	 * makeMemoryButton method makes buttons for "mc" "m+" m-" and "mr"
	 * 
	 * @param symbol
	 * @param row
	 * @param column
	 */
	private void makeMemoryButton(String symbol, int row, int column) {

		Button button = new Button(symbol);
		button.setStyle(numberBtn_style);
		button.setLayoutX(x_orig + column * (x_size + x_spacer));
		button.setLayoutY(y_orig + row * (y_size + y_spacer));
		button.setPrefSize(x_size, y_size);

		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				// if exponent mode is on update current value
				if (exponentMode) {
					convertExponent();
					exponentMode = false;
				}

				switch (symbol) {
				case "mc":
					memory = 0.0;
					break;
				case "m+":
					memory = memory + Double.valueOf(current);
					break;
				case "m-":
					memory = memory - Double.valueOf(current);
					break;
				case "mr":
					current = String.valueOf(memory);
					display.setText(formatOutput(memory));
					break;
				default:
					break;
				}
				clearOnNext = true;
			}
		});
		pane.getChildren().add(button);
	}

	/**
	 * makeValueButton method make buttons for "e" "pi" and "Rand"
	 * 
	 * @param symbol
	 * @param row
	 * @param column
	 */
	private void makeValueButton(String symbol, int row, int column) {

		Button button = new Button(symbol);
		button.setStyle(otherBtn_style);
		button.setLayoutX(x_orig + column * (x_size + x_spacer));
		button.setLayoutY(y_orig + row * (y_size + y_spacer));
		button.setPrefSize(x_size, y_size);

		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				// cancel exponent mode
				exponentMode = false;
				
				switch (symbol) {
				case "e":
					current = String.valueOf(2.718281828459045);
					break;
				case "\u03C0 ": // pi
					current = String.valueOf(3.141592653589793);
					break;
				case "Rand":
					Random rand = new Random();
					current = String.valueOf(rand.nextDouble());
					break;
				default:
					current = "";
					break;
				}

				clearOnNext = true;
				display.setText(current);
			}
		});
		pane.getChildren().add(button);
	}
	
	/**
	 * makeEEButton method makes exponent button
	 * @param symbol
	 * @param row
	 * @param column
	 */
	private void makeEEButton(String symbol, int row, int column) {

		Button button = new Button(symbol);
		button.setStyle(otherBtn_style);
		button.setLayoutX(x_orig + column * (x_size + x_spacer));
		button.setLayoutY(y_orig + row * (y_size + y_spacer));
		button.setPrefSize(x_size, y_size);

		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				exponentMode = true; // now next number entered becomes exponent

				display.setText(current + " E" + " 0");
				clearOnNext = false;
			}
		});
		pane.getChildren().add(button);
	}
	
	/**
	 * makeDecimalButton method makes the decimal point button
	 * @param row
	 * @param column
	 */
	private void makeDecimalButton(int row, int column) {

		Button button = new Button(".");
		button.setStyle(numberBtn_style);
		button.setLayoutX(x_orig + column * (x_size + x_spacer));
		button.setLayoutY(y_orig + row * (y_size + y_spacer));
		button.setPrefSize(x_size, y_size);

		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				// do nothing if exponent mode is on
				if (!exponentMode) {
					if (clearOnNext == true) {
						current = ".";
						clearOnNext = false;
					} else if (current.equals("0")) {
						current = ".";
					} else if (!current.contains(".")) {
						current += "."; // makes sure there's only 1 decimal point
					}
					display.setText(current);
				} 
				
			}
		});
		pane.getChildren().add(button);

	}
	
	/**
	 * makeBinaryOpBtn method makes binary operation buttons
	 * @param operator
	 * @param row
	 * @param column
	 */
	private void makeBinaryOpBtn(String operator, int row, int column) {

		Button button = new Button(operator);
		button.setStyle(binaryStdBtn_style);
		button.setLayoutX(x_orig + column * (x_size + x_spacer));
		button.setLayoutY(y_orig + row * (y_size + y_spacer));
		button.setPrefSize(x_size, y_size);
		
		// change style for these buttons 
		if (operator.equals("x\u005Ey") || operator.equals("y\u221Ax") ) {
			button.setStyle(otherBtn_style);
		}
		
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				double result = 0.0;
				double lowerOrderOpResult = 0.0; // the result of potential lower order ops
				double value1 = 0.0;
				double value2 = 0.0;

				String previousOp = binaryOp;
				String currentOp = operator;
				
				// if exponent mode is on update current value
				if (exponentMode) {
					convertExponent();
					exponentMode = false;
				}
				
				// set values
				value1 = Double.parseDouble(stored);
				value2 = Double.parseDouble(current); 
				
				// conditional statements to deal with order of ops and chaining
				if (currentOp.equals("x\u005Ey") || currentOp.equals("y\u221Ax") ) {
					
					if (previousOp.equals("")) {
						// if there wasn't previously an operator set
						// sets the stored to current
						stored = current;

					} else if (previousOp.equals("×") || previousOp.equals("÷")) {
						// store the op and value in case of higher order chaining
						thirdOrderOpValue = Double.parseDouble(stored);
						thirdOrderOp = previousOp;
						System.out.println("Stored thirdOrderOpValue: " + thirdOrderOpValue);
						System.out.println("Stored thirdOrderOp: " + thirdOrderOp);
						stored = current;

					} else if (previousOp.equals("+") || previousOp.equals("-")) {
						// store the op and value in case of higher order chaining
						forthOrderOpValue = Double.parseDouble(stored);
						forthOrderOp = previousOp;
						System.out.println("Stored forthOrderOpValue: " + forthOrderOpValue);
						System.out.println("Stored forthOrderOp: " + forthOrderOp);
						stored = current;

					} else {
						// the previous operator is x to the power y or y root x
						result = doCalc(previousOp, value1, value2);
						// stored becomes the result value
						stored = String.valueOf(result); 
					}
					
				} else if (currentOp.equals("×") || currentOp.equals("÷")) {
					if (previousOp.equals("")) {
						// if there wasn't previously an operator set
						// sets the stored to current
						stored = current;

					} else if (previousOp.equals("x\u005Ey") || previousOp.equals("y\u221A×")) {
						// evaluate the previous chain 
						result = doCalc(previousOp, value1, value2);

						if (!thirdOrderOp.equals("")) {
							// calculate prior third order chain
							lowerOrderOpResult = doCalc(thirdOrderOp, thirdOrderOpValue, result);
							thirdOrderOp = ""; // clears the thirdOrderOp operation
							thirdOrderOpValue = 0.0; // clears the thirdOrderOpValue
							
							// stored becomes the lower order op result
							stored = String.valueOf(lowerOrderOpResult);
							
							// calculate prior forth order chain
							if (!forthOrderOp.equals("")) {
								// calculate prior forth order chain
								double forthOrderOpResult = doCalc(forthOrderOp, forthOrderOpValue, lowerOrderOpResult);
								forthOrderOp = ""; // clears the forthOrderOp operation
								forthOrderOpValue = 0.0; // clears the forthOrderOpValue
								
								// stored becomes the lower order op result
								stored = String.valueOf(forthOrderOpResult);
								
							}
							
						} else {
							// stored becomes the result
							stored = String.valueOf(result); 
							System.out.println("Stored = " + stored);
						}

					} else if (previousOp.equals("+") || previousOp.equals("-")) {
						// set forth order op and value
						forthOrderOpValue = Double.parseDouble(stored);
						forthOrderOp = previousOp;
						System.out.println("Stored forthOrderOpValue: " + forthOrderOpValue);
						System.out.println("Stored forthOrderOp: " + forthOrderOp);
						
						stored = current;

					} else {
						// the previous operator is × or ÷
						result = doCalc(previousOp, value1, value2);
						// stored becomes the result
						stored = String.valueOf(result); 

					}

				} else {
					// current operator is + or -
					if (previousOp.equals("")) {
						// if there wasn't previously an operator set
						// sets the stored to current
						stored = current;
						

					} else if (previousOp.equals("x\u005Ey") || previousOp.equals("y\u221Ax")) {
						// evaluate the previous chain 
						result = doCalc(previousOp, value1, value2);

						if (!thirdOrderOp.equals("")) {
							// calculate prior third order chain
							lowerOrderOpResult = doCalc(thirdOrderOp, thirdOrderOpValue, result);
							thirdOrderOp = ""; // clears the forthOrderOp operation
							thirdOrderOpValue = 0.0; // clears the forthOrderOpValue
							
							// stored becomes the lower order op result
							stored = String.valueOf(lowerOrderOpResult); 
							
							if (!forthOrderOp.equals("")) {
								// calculate prior forth order chain
								double forthOrderOpResult = doCalc(forthOrderOp, forthOrderOpValue, lowerOrderOpResult);
								
								// set forth order op and value
								forthOrderOpValue = forthOrderOpResult;
								forthOrderOp = currentOp;
								System.out.println("Stored forthOrderOpValue: " + forthOrderOpValue);
								System.out.println("Stored forthOrderOp: " + forthOrderOp);
								
								// stored becomes the lower order op result
								stored = String.valueOf(forthOrderOpResult);
								
							}
						} else {
							// stored becomes the result
							stored = String.valueOf(result); 
							System.out.println("Stored = " + stored);
						}

					} else if (previousOp.equals("×") || previousOp.equals("÷")) {
						
						// evaluate previous chain
						result = doCalc(previousOp, value1, value2);

						if (!forthOrderOp.equals("")) {
							// calculate prior forth order chain
							lowerOrderOpResult = doCalc(forthOrderOp, forthOrderOpValue, result);
							forthOrderOp = ""; // clears the forthOrderOp operation
							forthOrderOpValue = 0.0; // clears the forthOrderOp value
							
							// stored becomes the lower order op result
							stored = String.valueOf(lowerOrderOpResult); 
						} else {
							// stored becomes result
							stored = String.valueOf(result); 
							
							System.out.println("Stored = " + stored);
						}
					} else {
						// the previous operator is + or -
						result = doCalc(previousOp, value1, value2);
						// stored becomes result
						stored = String.valueOf(result); 
					} 
				}

				binaryOp = operator;
				clearOnNext = true;
			}
		});
		pane.getChildren().add(button);
	}

	/**
	 * makeDegRadButton method make button to change between deg and rad mode
	 * 
	 * @param symbol
	 * @param row
	 * @param column
	 */
	private void makeDegRadButton(String symbol, int row, int column) {

		Button button = new Button(symbol);
		button.setStyle(otherBtn_style);
		button.setLayoutX(x_orig + column * (x_size + x_spacer));
		button.setLayoutY(y_orig + row * (y_size + y_spacer));
		button.setPrefSize(x_size, y_size);

		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				if (button.getText().equals("Rad")) {
					button.setText("Deg");
					degMode = false; // now in Radians mode
				} else {
					button.setText("Rad");
					degMode = true; // now in Degrees mode
				}

				clearOnNext = false;

			}
		});
		pane.getChildren().add(button);
	}
	
	/**
	 * makePercentageBtn method makes the percentage button
	 * @param operator
	 * @param row
	 * @param column
	 */
	private void makePercentageBtn(String operator, int row, int column) {

		Button button = new Button(operator);
		button.setStyle(otherBtn_style);
		button.setLayoutX(x_orig + column * (x_size + x_spacer));
		button.setLayoutY(y_orig + row * (y_size + y_spacer));
		button.setPrefSize(x_size, y_size);
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				double value = 0.0;
				double result = 0.0;
				
				// if exponent mode is on update current value
				if (exponentMode) {
					convertExponent();
					exponentMode = false;
				}
				
				value = Double.parseDouble(current);
				
				if (binaryOp.equals("")) {
					result = value / 100;
					// assigns result to current
					current = String.valueOf(result); 
														
					display.setText(String.format("%12f", result));
				} else {
					result = (Double.parseDouble(stored) * value) / 100;
					// the chosen % of the original value
					current = String.valueOf(result); 
														
					display.setText(current);
				}

				clearOnNext = true;
			}
		});
		pane.getChildren().add(button);
	}
	
	/**
	 * makeUnaryOpBtn method makes all unary op buttons
	 * @param operator
	 * @param row
	 * @param column
	 */
	private void makeUnaryOpBtn(String operator, int row, int column) {

		Button button = new Button(operator);
		button.setStyle(otherBtn_style);
		button.setLayoutX(x_orig + column * (x_size + x_spacer));
		button.setLayoutY(y_orig + row * (y_size + y_spacer));
		button.setPrefSize(x_size, y_size);
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				double value = 0.0; 
				
				// if exponent mode is on update current value
				if (exponentMode) {
					convertExponent();
					exponentMode = false;
				}
				
				// set value to current
				value = Double.parseDouble(current);

				switch (operator) {
				case "\u00B1": // plus minus sign
					value *= -1; // inverts positive / negative
					break;
				case "x\u00B2": // x squared
					value = Math.pow(value, 2);
					break;
				case "x\u00B3": // x cubed
					value = Math.pow(value, 3);
					break;
				case "e\u005Ex": // e to the power x
					value = Math.exp(value);
					break;
				case "10\u005Ex": // 10 to the power x
					value = Math.pow(10, value);
					break;
				case "1/x":
					value = 1 / value;
					break;
				case "\u221A": // square root
					value = Math.sqrt(value);
					break;
				case "\u221B": // cube root
					value = Math.cbrt(value);
					break;
				case "ln":
					value = Math.log(value);
					break;
				case "log10":
					value = Math.log10(value);
					break;
				case "x!":
					// works with factorials of decimal values
					value = Gamma.gamma(value + 1);
					break;
				case "sin":
					if (degMode) {
						value = Math.sin(Math.toRadians(value));
					} else {
						value = Math.sin(value);
					}
					break;
				case "cos":
					if (degMode) {
						value = Math.cos(Math.toRadians(value));
					} else {
						value = Math.cos(value);
					}
					break;
				case "tan":
					if (degMode) {
						value = Math.tan(Math.toRadians(value));
					} else {
						value = Math.tan(value);
					}
					break;
				case "sinh":
					value = Math.sinh(value);
					break;
				case "cosh":
					value = Math.cosh(value);
					break;
				case "tanh":
					value = Math.tanh(value);
					break;
				}

				current = String.valueOf(value);
				display.setText(formatOutput(value));
				clearOnNext = true;
			}
		});
		pane.getChildren().add(button);
	}
	
	/**
	 * makeClearButton method makes the clear button
	 * @param row
	 * @param column
	 */
	private void makeClearBtn(int row, int column) {

		Button button = new Button("AC");
		button.setStyle(otherBtn_style);
		button.setLayoutX(x_orig + column * (x_size + x_spacer));
		button.setLayoutY(y_orig + row * (y_size + y_spacer));
		button.setPrefSize(x_size, y_size);
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				current = "0";
				stored = "0";
				binaryOp = "";
				thirdOrderOp = "";
				thirdOrderOpValue = 0.0;
				forthOrderOp = "";
				forthOrderOpValue = 0.0;

				display.setText(String.valueOf(current));
				clearOnNext = true;
			}
		});
		pane.getChildren().add(button);
	}

	/**
	 * convertExponent method updates the "current" value to the displayed
	 * exponent value
	 * 
	 */
	private void convertExponent() {

		double value = 0.0;
		double exponent = 0.0;
		double output = 0.0;
		String s = ""; // to hold display text
		
		s = display.getText();
		
		// get index position of E
		int indexOfE = s.indexOf("E");
		
		value = Double.valueOf(s.substring(0, (indexOfE - 1) ));
		exponent = Double.valueOf(s.substring(s.lastIndexOf('E') + 1));
		
		output = value * Math.pow(10, exponent); // do exponent calculation
		
		current = String.valueOf(output); // update current value
		
		clearOnNext = true;
	}
	
	/**
	 * makeEqualsButton method makes the equals button
	 * @param row
	 * @param column
	 */
	private void makeEqualsButton(int row, int column) {

		Button button_eql = new Button("=");
		button_eql.setStyle(binaryStdBtn_style);
		button_eql.setLayoutX(x_orig + column * (x_size + x_spacer));
		button_eql.setLayoutY(y_orig + row * (y_size + y_spacer));
		button_eql.setPrefSize(x_size, y_size);
		button_eql.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				double value1 = 0.0; 
				double value2 = 0.0; 
				double result = 0.0;
				// result of lower order operations
				double chainResult = 0.0; 
				
				// if exponent mode is on update current value
				if (exponentMode) {
					convertExponent();
					display.setText(formatOutput(Double.valueOf(current)));
					exponentMode = false;
				}
				
				// set the stored and current values
				value1 = Double.parseDouble(stored);
				value2 = Double.parseDouble(current);

				if (!binaryOp.equals("")) {
					result = doCalc(binaryOp, value1, value2);
					binaryOp = ""; // clear the operation
					
					// the current becomes the result
					current = String.valueOf(result); 
					
					// calculate previous chain of third order ops
					if (!thirdOrderOp.equals("")) {
						chainResult = doCalc(thirdOrderOp, thirdOrderOpValue, result);
						// current becomes chain result
						current = String.valueOf(chainResult); 
						
						// if also forth order op chain
						if (!forthOrderOp.equals("")) {
							value2 = chainResult;
							chainResult = doCalc(forthOrderOp, forthOrderOpValue, value2);
							// current becomes chain result
							current = String.valueOf(chainResult); 
							
							forthOrderOp = ""; // clears the forthOrderOp
						}
						
					}
					
					// calculate previous chain of forth order ops
					if (!forthOrderOp.equals("")) {
						chainResult = doCalc(forthOrderOp, forthOrderOpValue, result);
						// current becomes chain result
						current = String.valueOf(chainResult); 
					}
				}
				
				thirdOrderOp = ""; // clear the thirdOrderOp
				thirdOrderOpValue = 0.0; // clears thirdOrderOpValue
				forthOrderOp = ""; // clears the forthOrderOp
				forthOrderOpValue = 0.0; // clears forthOrderOpValue
			}
		});
		pane.getChildren().add(button_eql);

	}

	/**
	 * doCalc method performs binary calculations
	 * 
	 * @param operator
	 * @param value1
	 * @param value2
	 * @return
	 */
	private double doCalc(String operator, double value1, double value2) {
		
		double result = 0.0;

		switch (operator) {
		case "+":
			result = value1 + value2;
			break;
		case "-":
			result = value1 - value2;
			break;
		case "×":
			result = value1 * value2;
			break;
		case "÷":
			result = value1 / value2;
			break;
		case "x\u005Ey":
			result = Math.pow(value1, value2);
			break;
		case "y\u221Ax":
			result = Math.pow(Math.E, Math.log(value1)/value2);
			break;
		default:
			result = 0.0;
			break;
		}
		System.out.println("value1 = " + value1 + ", value2 = " + value2 + ", result = " + result);
		display.setText(formatOutput(result));
		stored = String.valueOf(result); // the stored becomes the result
		clearOnNext = true;

		return result;
	}

	/**
	 * formatOutput method to format the output string
	 * 
	 * @param result
	 * @return
	 */
	private String formatOutput(double result) {

		String output;
		if (result % 1 == 0) {
			output = String.format("%.0f", result);
		} else {
			// set number of decimal places
			output = String.format("%.15f", result);
			// remove trailing zeros
			output = output.indexOf(".") < 0 ? output : output.replaceAll("0*$", "").replaceAll("\\.$", "");
		}
		return output;

	}

	public static void main(String[] args) {
		launch(args);
	}

}
