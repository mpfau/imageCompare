import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private static final long serialVersionUID = 23491796644908811L;
	BufferedImage image;

	double scale = 1;
    
    ImagePanel() {
    	this.setBackground(Color.black);
    }
    
    public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);
        if (image == null) {
        	return;
        }
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        double w = getWidth();
        double h = getHeight();
        double iw = image.getWidth();
        double ih = image.getHeight();
        double heightScale = h / ih;
        double widthScale = w / iw;
        scale = heightScale < widthScale ? heightScale : widthScale;
        double x = (w - scale*iw)/2;
        double y = (h - scale*ih)/2;
//        System.out.println("x:" + x +";y:" +y + ";scale:" + scale + ";hs:" + heightScale + ";ws:" + widthScale);
        AffineTransform at = AffineTransform.getTranslateInstance(x, y);
        at.scale(scale, scale);
        g2.drawRenderedImage(image, at);
    }
    
    

//    public static void main(String[] args) throws IOException {
//        String path = "/Users/matthias/Desktop/Hochzeit.jpg";
//        BufferedImage image = ImageIO.read(new File(path));
//        ImagePanel test = new ImagePanel(image);
//
//        JFrame f = new JFrame();
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.getContentPane().add(test);
//        f.setSize(500,500);
//        f.setLocation(200,200);
//        f.setVisible(true);
//    }
}