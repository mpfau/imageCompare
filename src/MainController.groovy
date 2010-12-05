import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import groovy.lang.Closure;
import groovy.swing.SwingBuilder;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.WindowConstants;
import javax.swing.TransferHandler.TransferSupport;


class MainController {
	
	JFrame frame1;
	JFrame frame2;
	SwingBuilder swing = new SwingBuilder()
	List<File> files = []
	List<Comparison> comparisons = []
	ImageCache cache = new ImageCache()                                
	
	// new Items have been dropped at the Panel, the comparison will be initialized
	def newState = {
		Comparison comparison = new Comparison()
		comparison.files.addAll files[0..1]
		files = files - files[0..1]
		if (files.isEmpty()) {
			currentState = endState
		} else {
			currentState = comparisonState
		}
		
		comparisons << comparison
		compare comparison
	} as State
	// The comparison is on the way, handle events and process further
	def comparisonState = {
		Comparison comparison = new Comparison()
		comparison.files << comparisons.last().choosen
		comparison.files << files[0]
		files = files - files[0] 
		if (files.isEmpty()) {
			currentState = endState
		}
		
		comparisons << comparison
		compare comparison
	} as State
	// No more Images to compare, display dialogue
	def endState = {
		JOptionPane.showMessageDialog frame1, "You took your decision for: ${comparisons.last().choosen}"
	} as State
	def currentState = newState

	TransferHandler transferHandler
	
	ImagePanel imagePanel1 = new ImagePanel()
	ImagePanel imagePanel2 = new ImagePanel()
	                    
	public MainController() {
		transferHandler = new FileTransferHandler(controller: this)
		imagePanel1.setTransferHandler transferHandler
		imagePanel2.setTransferHandler transferHandler
	}
	                   
   def right = swing.action(	
			name:			'choose One',
			closure:    	this.&chooseOne,
			accelerator: 	'1'
		)
	
   def left = swing.action(	
			name:			'choose Two',
			closure:    	this.&chooseTwo,
			accelerator: 	'2'
		)
		
	public JFrame getFrame(int number, def imagePanel) {
		swing.frame (
				title: "$number: ImageCompare", defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE,
				size: [800, 600], show: true, locationRelativeTo: null, mousePressed: {setOnTop()}, 
				componentResized: {resize(it)}, componentMoved: {move(it)}) {
				menuBar() {
					menu('Action') {
						menuItem(action: right)
						menuItem(action: left)
					}
				}
				container(imagePanel)
			}
	}
	
	void chooseOne(def event) {
		comparisons.last().choosen = comparisons.last().files[0]
		cache.removeFromCache comparisons.last().files[1]
		currentState.compareImages()
	}
	
	void chooseTwo(def event) {
		comparisons.last().choosen = comparisons.last().files[1]
		cache.removeFromCache comparisons.last().files[0]
		currentState.compareImages()
	}
	
	void setOnTop() {
		frame1.setAlwaysOnTop true
		frame1.setAlwaysOnTop false
		frame2.setAlwaysOnTop true
		frame2.setAlwaysOnTop false
	}
	
	void resize(ComponentEvent e) {
		frame1.setSize(e.source.width, e.source.height)
		frame2.setSize(e.source.width, e.source.height)
	}
	
	void move(ComponentEvent e) {
		frame1.setLocation new Point((int) frame1.location.x, (int) e.source.location.y)
		frame2.setLocation new Point((int) frame2.location.x, (int) e.source.location.y)
	}

	void run() {
		frame1 = getFrame(1, imagePanel1)
		frame2 = getFrame(2, imagePanel2)
	}
	
	private void startComparison(List<File> files) {
		currentState = newState
		this.files = files
		cache.setFiles files
        if (files.size() < 2) {
        	JOptionPane.showMessageDialog frame1, "You have to drop at least two pictures on one frame!"
        	return
        }
		currentState.compareImages()
	}
	
	private void compare(Comparison comparison) {
		BufferedImage image1 = cache.getImage(comparison.files[0]).image
		BufferedImage image2 = cache.getImage(comparison.files[1]).image
        imagePanel1.image = image1
        imagePanel2.image = image2
        imagePanel1.repaint()
        imagePanel2.repaint()
	}
	
	
	static main(def args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");

		new MainController().run()
	}

}

interface State {
	void compareImages()
}