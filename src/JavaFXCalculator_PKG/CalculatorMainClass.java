//*************************-=-=-=-=-=-=-=-=-=-=-=-=-**************************// 
//********************<         JAVA FX CALCULATOR        >*******************//
//*************************-=-=-=-=  V 1.0  -=-=-==-**************************//
//**************                     AUTHOR                     **************//
//---------------<_>------->>>   ALISTAIR COOPER   <<<-------<_>--------------//
//*****************<_>         CREATED: 07/05/2016          <_>***************//
//**************************-=-=-=-=-=-=-=-=-=-=-=-=-*************************//
//                                                                            //


package JavaFXCalculator_PKG;

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
	private String unaryOp = "";  // currently unused
	private Label display = new Label(current);
	private Pane pane = new Pane(); // main pane for calculator
	private boolean clearOnNext = false;

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
	private String numberBtn_style = "-fx-border-color: grey; -fx-base: #D3D3D3; " 
							  		 + "-fx-font: 21 arial;";
	private String binaryBtn_style = "-fx-border-color: grey; -fx-base: #FFA500; " 
							  		 + "-fx-font: 21 arial; -fx-text-fill: white;";
	private String display_style = "-fx-border-color: grey; -fx-background-color: #808080;" 
								   + "-fx-text-fill: white; -fx-font: 28 arial;";
	private String pane_style = "-fx-border-color: black; -fx-background-color: #DCDCDC;";

	@Override // Override the start method in the Application class.
	public void start(Stage primaryStage) throws Exception {
		// Create the layout of a calculator
		// in a grid, 6 rows by 4 columns, as shown here:
		//     C0  C1  C2  C3
		// R0 | |
		// R1 | C |+/-| % | ÷ |
		// R2 | 7 | 8 | 9 | × |
		// R3 | 4 | 5 | 6 | - |
		// R4 | 1 | 2 | 3 | + |
		// R5 | - 0 - | . | = |

		// Set the style for the main calculator pane
		pane.setStyle(pane_style);

		// Display: Row 0, Col 0/1/2/3
		display.setStyle(display_style);
		display.setAlignment(Pos.BASELINE_RIGHT);
		display.setLayoutX(x_orig + 0 * (x_size + x_spacer));
		display.setLayoutY(y_orig + 0 * (y_size + y_spacer));
		display.setPrefSize(x_size * 4 + x_spacer * 3, y_size);
		pane.getChildren().add(display);

		// 0 Button: Row 5, Col 0, is not double wide
		makeNumberButton("0", 5, 0, true);

		// 1 Button: Row 4, Col 0, is not double wide
		makeNumberButton("1", 4, 0, false);

		// 2 Button: Row 4, Col 1, is not double wide
		makeNumberButton("2", 4, 1, false);

		// 3 Button: Row 4, Col 2, is not double wide
		makeNumberButton("3", 4, 2, false);

		// 4 Button: Row 3, Col 0, is not double wide
		makeNumberButton("4", 3, 0, false);

		// 5 Button: Row 3, Col 1, is not double wide
		makeNumberButton("5", 3, 1, false);

		// 6 Button: Row 3, Col 2, is not double wide
		makeNumberButton("6", 3, 2, false);

		// 7 Button: Row 2, Col 0, is not double wide
		makeNumberButton("7", 2, 0, false);

		// 8 Button: Row 2, Col 1, is not double wide
		makeNumberButton("8", 2, 1, false);

		// 9 Button: Row 2, Col 2, is not double wide
		makeNumberButton("9", 2, 2, false);

		// . Button: Row 5, Col 2
		makeDecimalButton(5, 2);

		// + Button: Row 4, Col 3
		makeBinaryOpBtn("+", 4, 3);

		// - Button: Row 3, Col 3
		makeBinaryOpBtn("-", 3, 3);

		// × Button: Row 2, Col 3
		makeBinaryOpBtn("×", 2, 3);

		// ÷ Button: Row 1, Col 3
		makeBinaryOpBtn("÷", 1, 3);

		// % Button: Row 1, Col 2
		makePercentageBtn("%", 1, 2);

		// ± Button: Row 1, Col 1
		makePlusMinusBtn(1, 1);

		// C Button: Row 1, Col 0
		makeClearBtn(1, 0);

		// = Button: Row 5, Col 3
		Button button_eql = new Button("=");
		button_eql.setStyle(binaryBtn_style);
		button_eql.setLayoutX(x_orig + 3 * (x_size + x_spacer));
		button_eql.setLayoutY(y_orig + 5 * (y_size + y_spacer));
		button_eql.setPrefSize(x_size, y_size);
		button_eql.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				double result = 0.0;
				
				if (!binaryOp.equals("")) {
					result = doCalc();
				    binaryOp = "";    // clear the operation
				    
				    current = String.valueOf(result);   // the result is now the current
				}
			}
		});
		pane.getChildren().add(button_eql);

		// Set the scene and the primary stage.
		Scene scene = new Scene(pane, x_size * 4 + x_spacer * 5, y_size * 6 + y_spacer * 7);
		primaryStage.setTitle("Calculator");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	private void makeNumberButton(String number, int row, int column, boolean isDoubleWide) {

		Button button = new Button(number);
		button.setStyle(numberBtn_style);
		button.setLayoutX(x_orig + column * (x_size + x_spacer));
		button.setLayoutY(y_orig + row * (y_size + y_spacer));

		if (isDoubleWide) {
			button.setPrefSize(x_size * 2 + x_spacer, y_size); // for number
																// zero
		} else {
			button.setPrefSize(x_size, y_size);
		}

		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
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
		});
		pane.getChildren().add(button);

	}

	private void makeDecimalButton(int row, int column) {

		Button button = new Button(".");
		button.setStyle(numberBtn_style);
		button.setLayoutX(x_orig + column * (x_size + x_spacer));
		button.setLayoutY(y_orig + row * (y_size + y_spacer));
		button.setPrefSize(x_size, y_size);

		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
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
		});
		pane.getChildren().add(button);

	}

	private void makeBinaryOpBtn(String operator, int row, int column) {

		Button button = new Button(operator);
		button.setStyle(binaryBtn_style);
		button.setLayoutX(x_orig + column * (x_size + x_spacer));
		button.setLayoutY(y_orig + row * (y_size + y_spacer));
		button.setPrefSize(x_size, y_size);
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				double result = Double.parseDouble(stored);
				
				if (binaryOp.equals("")) {
					// if there wasn't previously an operator set 
					// sets the stored to current
					stored = current;
					
				} else {
					// there was previously an operator set 
					// evaluate the previous calculation
					result = doCalc();
					stored = String.valueOf(result);
					
				}
					
				binaryOp = operator;
				clearOnNext = true;
			}
		});
		pane.getChildren().add(button);
	}

	private void makePercentageBtn(String operator, int row, int column) {

		Button button = new Button(operator);
		button.setStyle(numberBtn_style);
		button.setLayoutX(x_orig + column * (x_size + x_spacer));
		button.setLayoutY(y_orig + row * (y_size + y_spacer));
		button.setPrefSize(x_size, y_size);
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
					
				double value = Double.parseDouble(current);
				double result = 0.0;
				
				if(binaryOp.equals("")) {
					result = value / 100;
					current = String.valueOf(result);  // assigns result to current
					display.setText(String.format("%12f", result));
				} else {
					result = (Double.parseDouble(stored) * value) / 100;
					current = String.valueOf(result);   // the chosen % of the original value
					display.setText(current);
				}
				
				clearOnNext = true;
			}
		});
		pane.getChildren().add(button);
	}

	private void makePlusMinusBtn(int row, int column) {

		Button button = new Button("±");
		button.setStyle(numberBtn_style);
		button.setLayoutX(x_orig + column * (x_size + x_spacer));
		button.setLayoutY(y_orig + row * (y_size + y_spacer));
		button.setPrefSize(x_size, y_size);
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				double value = Double.parseDouble(current);
				value *= -1;  // inverts positive / negative
				current = String.valueOf(value);
				display.setText(String.format("%.2f", value));
				clearOnNext = true;
			}
		});
		pane.getChildren().add(button);
	}
	
	private void makeClearBtn(int row, int column) {

		Button button = new Button("AC");
		button.setStyle(numberBtn_style);
		button.setLayoutX(x_orig + column * (x_size + x_spacer));
		button.setLayoutY(y_orig + row * (y_size + y_spacer));
		button.setPrefSize(x_size, y_size);
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				current = "0";
				stored = "0";
				binaryOp = "";
				unaryOp = "";
				
				display.setText(String.valueOf(current));
				clearOnNext = true;
			}
		});
		pane.getChildren().add(button);
	}

	private double doCalc() {

		double value1 = Double.parseDouble(stored);
		double value2 = Double.parseDouble(current);
		double result = 0.0;
		switch (binaryOp) {
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
		default:
			result = 0.0;
			break;
		}
		System.out.println("value1 = " + value1 + ", value2 = " + value2 + ", result = " + result);
		display.setText(String.format("%.2f", result));
		stored = String.valueOf(result); // the stored becomes the result
		clearOnNext = true;
		
		return result;   // used to chain operations
	}

	public static void main(String[] args) {
		launch(args);
	}

}
