/**
 * Graphical is the startup class and controller for 
 * the java swing version of Life.
 * @author Tyler Baldwin
 * @date May 2018
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


public class Graphical extends JPanel implements ActionListener, ChangeListener {
	private static Graphical sharedApp = null;
	private static final long serialVersionUID = 1L;
	private static final int squareSize = 5;
	private static final int borderSize = 5;

	private static final String runButtonText = "Run";
	private static final String birthMinSliderName = "Birth Minimum";
	private static final String birthMaxSliderName = "Birth Maximum";
	private static final String liveMinSliderName = "Live Minimum";
	private static final String liveMaxSliderName = "Live Maximum";

	private Timer myTimer;
	private JFrame theFrame;
	private Life myLife;
	private int birthMin, birthMax, liveMin, liveMax;

	/**
	 * Constructs the object, creating the model and view (with the
	 * frames and widgets) and starts the timer.
	 */
	private Graphical() {
		//Set the look and feel (for Macs too).
		if (System.getProperty("mrj.version") != null) {
			System.setProperty("apple.laf.useScreenMenuBar","true");
		}
		JFrame.setDefaultLookAndFeelDecorated(true);

		myLife = null;
		birthMin = 4;
		birthMax = 6;
		liveMin = 3;
		liveMax = 5;

		theFrame = new JFrame("Life");
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel widgetPanel = setupLayout();
		theFrame.getContentPane().add(widgetPanel, BorderLayout.CENTER);
		//createTextItems(widgetPanel);
		createButtons(widgetPanel);

		theFrame.pack();
		theFrame.setVisible(true);
		
		myTimer = new Timer(1000, this);
		myTimer.start();

	}

	/**
	 * Set up the layout for the interface
	 * @return returns the JPanel that will hold the widgets and this UI
	 */
	private JPanel setupLayout() {
		JPanel widgetPanel = new JPanel();
		widgetPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		widgetPanel.setLayout(new BoxLayout(widgetPanel, BoxLayout.PAGE_AXIS));
		widgetPanel.add(this);

		Dimension puzzleDrawingSize = new Dimension(100*squareSize + 2*borderSize, 100*squareSize + 2*borderSize);
		setMinimumSize(puzzleDrawingSize);
		setPreferredSize(puzzleDrawingSize);
		setMaximumSize(puzzleDrawingSize);
		setBorder(BorderFactory.createLineBorder(Color.lightGray, 2));
		return widgetPanel;
	}

	/**
	 * Creates the buttons to control action.
	 * @param widgetPanel is the JPanel that will hold the buttons.
	 */
	private void createButtons(JPanel widgetPanel) {
		JButton runButton = new JButton(runButtonText);
		runButton.addActionListener(this);
		widgetPanel.add(runButton);
		runButton.setAlignmentX(CENTER_ALIGNMENT);
		createSlider(widgetPanel, birthMinSliderName, birthMin);
		createSlider(widgetPanel, birthMaxSliderName, birthMax);
		createSlider(widgetPanel, liveMinSliderName, liveMin);
		createSlider(widgetPanel, liveMaxSliderName, liveMax);
	}
	
	/**
	 * Creates a slider and adds it to the widgetPanel with a label
	 * @param widgetPanel panel that will contain the slider
	 * @param name of the slider
	 * @param initialValue of the slider
	 */
	private void createSlider(JPanel widgetPanel, String name, int initialValue) {
		JPanel p = new JPanel();
		p.setAlignmentX(CENTER_ALIGNMENT);
		p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
		widgetPanel.add(p);
		
		JLabel inputLabel = new JLabel();
		p.add(inputLabel);
		inputLabel.setText(name);
		inputLabel.setAlignmentX(RIGHT_ALIGNMENT);
		
		JSlider js = new JSlider(JSlider.HORIZONTAL, 0, 9, initialValue);
		js.setName(name);
		js.addChangeListener(this);
		js.setMajorTickSpacing(1);
		js.setMinorTickSpacing(1);
		js.setPaintTicks(true);
		js.setPaintLabels(true);
		js.setSnapToTicks(true);
		p.add(js);
	}

	/**
	 * Sets the sizes based on the current world.
	 * @parm boolean [][] world is the description of the world to be displayed
	 */
	public void setSizes(boolean [][] world) {
		Dimension puzzleDrawingSize = new Dimension (world[0].length*squareSize + borderSize*2,
				world.length*squareSize + borderSize*2);
		setMinimumSize(puzzleDrawingSize);
		setPreferredSize(puzzleDrawingSize);
		setMaximumSize(puzzleDrawingSize);
		revalidate();
		theFrame.pack();
	}


	/**
	 * Draws the painted portions when requested.
	 * @param gc the graphics context in which to draw
	 */
	public void paintComponent(Graphics gc) { 
		if (isOpaque()) { //paint background
			gc.setColor(getBackground());
			gc.fillRect(0, 0, getWidth(), getHeight());
		}
		if (myLife == null) {
			return;
		}

		gc.translate(borderSize, borderSize);
		boolean [][] world = myLife.world();
		Color liveColor = new Color(255, 44, 40);
		Color deadColor = new Color(240, 240, 240);
		for (int i = 0; i < world.length; i++) {
			for (int j = 0; j < world[0].length; j++) {
				if (world[i][j]) {
					gc.setColor(liveColor);	
				} else {
					gc.setColor(deadColor); 	
				}
				gc.fillRect(j*squareSize, i*squareSize, squareSize, squareSize);
			}
		}
		gc.translate(-borderSize, -borderSize);
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent ce) {
	    JSlider source = (JSlider)ce.getSource();
	    if (!source.getValueIsAdjusting()) {
	        int value = (int)source.getValue();
	        if (source.getName().equals(birthMinSliderName)) {
	        	birthMin = value;
	        } else if (source.getName().equals(birthMaxSliderName)) {
	        	birthMax = value;
	        } else if (source.getName().equals(liveMinSliderName)) {
	        	liveMin = value;
	        } else if (source.getName().equals(liveMaxSliderName)) {
	        	liveMax = value;
	        } else {
	        	System.out.println("Unknown stateChange source");
	        }
	    }
	}

	/**
	 * Perform actions depending which widget was selected.
	 * Determines which sort of widget was selected and bases action on its name.
	 * Currently has actions for the find word button and the open puzzle menu.
	 * @param se the selection event including the source of the event
	 */
	public void actionPerformed(ActionEvent se) {
		String command = se.getActionCommand();
		if (se.getSource() == myTimer) {
			if (myLife != null) {
				myLife.update();
				repaint();
			}
		} else if (command.equals(runButtonText)) {
			myLife = new Life(System.currentTimeMillis(), 100, 100, birthMin, birthMax, liveMin, liveMax);
			setSizes(myLife.world());
			repaint();
		} else {
			System.out.println("Unknown action: " + command);
		}
	}

	/**
	 * Creates (if necessary) and returns the singleton instance
	 * @return the singleton shared instance
	 */
	public static Graphical sharedInstance() {
		if (sharedApp == null) {
			sharedApp = new Graphical();
		}
		return sharedApp;
	}

	/**
	 * Starts the graphical interface
	 * @param args ignored
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						sharedInstance();
					}
				}
		);

	}

}
