/************************************************************************************************************
* Created by: Ryan Pickering
* Last updated: 02/17/2016 | 7:53 pm
*
* Finds the set of units U(n) given N, calculates the order of the set and the order of each element.
************************************************************************************************************/
import javax.swing.text.DefaultCaret;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.lang.NumberFormatException;
import java.util.*;

public class findUnitsofN extends JFrame{

	private JLabel _label;
	private JTextField _textField;
	private JTextArea _textArea;
	private JButton	_button;
	private JPanel _panel = new JPanel();

	public findUnitsofN(){

		super("findUnitsofN");

		setSize(500,350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		_label = new JLabel("Enter a value of n > 2 and press 'Generate'");
		_textField = new JTextField(40);
		_textArea = new JTextArea(15,40);
		_textArea.setEditable(false);


		_button = new JButton("Generate");
		_button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e){

				_textArea.setText("");

				String input = _textField.getText();
				try{
					//check input is integer
					Integer div = Integer.parseInt(input);

					// if n = 1 then the only unit is 1 
					if (div < 2){
						_textArea.append("Please enter a positive integer greater than 2");
					}
					else {
						//calculate units of the group Z mod n
						setOfUnits set = new setOfUnits(div.intValue());

						_textArea.append("U(" + div + ") = " + set.getIdSet() + "\n");				
						_textArea.append("U(" + div + ") has order " + set.getSetofUnits().size() + "\n");

						for (Element element : set.getSetofUnits()){ 
							_textArea.append("ord(" + element.getId() + ") is: " + element.getOrder() + "\n");
						}	
					}
				}
				catch (NumberFormatException i){
					_textArea.append("Error: invalid argument. Please try again.");
				}

				_textField.setText("");		
			}
		});

		_panel.add(_label);
		_panel.add(_button);

		add(new JScrollPane(_textArea), BorderLayout.CENTER);
		DefaultCaret caret = (DefaultCaret)_textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		add(_panel, BorderLayout.NORTH);
		add(_textField, BorderLayout.SOUTH);

		setVisible(true);

	}
	/********************************************************************************************************
	* public method 'getN' gets positive integer 'n' from user
	*
	* @param	none	(input from user)
	* @return			a valid N from the user
	* 
	* method used when using command line, taken out when using swing
	********************************************************************************************************/
	/*public int getN(){
		Scanner sc = new Scanner(System.in);
		int n = 0;
		do {
			System.out.print("\n" + "Please enter a positive integer: ");
			while (!sc.hasNextInt()){
				System.out.print("Please enter a positive integer: ");
				sc.next();
			}
			n = sc.nextInt();
		} while (n <= 0);
		return n;
	}*/

	//main
	public static void main(String[] args){
		new findUnitsofN();
	}
}


/************************************************************************************************************
* 'setOfUnits' can be used to create an instance of class 'setOfUnits', which illustrates a set of 'element' objects 
* to represent the set of units U(n) for a given positive integer n
*
* Each instance has the following attributes:
*		1. _set (containing all of its 'element' objects)
*
* Each instance has the following methods:
*		1. public setOfUnits(int n) - the constructor
*		2. private ArrayList<Element> createSetOfUnits(int n)
*		3. private int GCD(int a, int b)
*		4. public ArrayList<Element> getSetofUnits()
*		5. private ArrayList<Integer> createIdSet()
*		6. public ArrayList<Integer> getIdSet()
************************************************************************************************************/
class setOfUnits{

	private ArrayList<Element> _set = new ArrayList<Element>();

	public setOfUnits(int n){
		_set = createSetOfUnits(n);
	}

	/********************************************************************************************************
	* private method 'createSetOfUnits' finds the elements of U(N)
	*
	* @param	N    a positive integer
	* @return		 arraylist of integers (units) of U(N)
	********************************************************************************************************/
	private ArrayList<Element> createSetOfUnits(int n){
		ArrayList<Element> set = new ArrayList<Element>();
		for (int i = 1; i <= n; i++){
			if (GCD(i,n) == 1){
				set.add(new Element(i,n)); 
			}
		}
		return set;
	}

	/********************************************************************************************************
	* private method 'GCD' calculates the greatest common divisor (GCD)
	*
	* @param	a    integer less than or equal to b
	*			b    integer greater than or equal to a  		
	* @return		 positive integer (GCD)
	********************************************************************************************************/
	private int GCD(int a, int b){
		int r;
		int rem = a;
		int num = b;

		while (rem != 0){
			r = rem;
			rem = num % rem;
			num = r;
		} 
		return num;
	}

	/********************************************************************************************************
	* public method 'getSetofUnits' returns the set of units of U(n)
	*
	* @param		none
	* @return		_set	an arraylist of elements
	********************************************************************************************************/
	public ArrayList<Element> getSetofUnits(){
		return _set;
	}

	/********************************************************************************************************
	* private method 'createElementIdSet' creates a set of all the _id's for every element
	*
	* @param	a set of elements
	* @return	a set of _id's	
	********************************************************************************************************/
	private ArrayList<Integer> createIdSet(){

		ArrayList<Integer> id_set = new ArrayList<Integer>();

		for (Element el : getSetofUnits()){
			id_set.add(el.getId());
		}
		return id_set;
	}

	/********************************************************************************************************
	* public method 'getIdSet' gets the set of _id's for of all elements
	*
	* @param	none
	* @return	a set of element _id's
	********************************************************************************************************/
	public ArrayList<Integer> getIdSet(){
		return createIdSet();
	}
}

/************************************************************************************************************
* class 'Element' can be used to create 'element' objects for the set U(n)
* 
* Each instance contains the following attributes:
*		1. _id (the numerical representation of the element)
*		2. _generator (the integer 'n' used to generate the element)
*
* Each instance has the following methods:
*		1. public Element(int id, int generator) - the constructor
*		2. public int getId()
*		3. private int order(int num1, int num2)	
*		4. public int getOrder(_generator, _id)
************************************************************************************************************/
class Element{
	private int _id;
	private int _generator;
	
	public Element(int id, int generator){
		_id = id;
		_generator = generator;
	}

	/********************************************************************************************************
	* public method 'getId' gets the _id for an element
	*
	* @param	none
	* @return	_id		an int representation of the element
	********************************************************************************************************/
	public int getId(){
		return _id;
	}

	/********************************************************************************************************
	* private method 'order' calculates the order of an element in U(N)
	*
	* @param	num1    the sam number as N
	*			num2    an element of U(N)
	* @return		    the order of the element	        
	********************************************************************************************************/	
	private int order(int num1, int num2){
		int n = num1;
		int nmb = num2;
		int rem = nmb;
		int ctr = 1;
		while (rem != 1){
			rem = (rem * nmb) % n;
			ctr++;
		}
		return ctr;
	}

	/********************************************************************************************************
	* public method 'getOrder' gets the order of an element
	*
	* @param	none
	* @return	the (int) representation of the order 
	*********************************************************************************************************/
	public int getOrder(){
		return order(_generator, _id);
	}
}

