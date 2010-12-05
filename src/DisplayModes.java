import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JWindow;

public class DisplayModes {

  public static void main(String args[]) throws IOException {
    GraphicsEnvironment graphicsEnvironment =
      GraphicsEnvironment.getLocalGraphicsEnvironment();
    GraphicsDevice graphicsDevice =
      graphicsEnvironment.getScreenDevices()[0];
    GraphicsDevice graphicsDevice2 =
        graphicsEnvironment.getScreenDevices()[1];
    DisplayMode displayModes[] = graphicsDevice.getDisplayModes();
    DisplayMode originalDisplayMode = graphicsDevice.getDisplayMode();
    JWindow window = new JWindow() {
//      public void paint(Graphics g) {
//        g.setColor(Color.blue);
//        g.drawString("Hello, World!", 50, 50);
//      }
    };
  String path = "/Users/matthias/Desktop/Hochzeit.jpg";
  BufferedImage image = ImageIO.read(new File(path));
    ImagePanel panel = new ImagePanel();
    panel.setImage(image);
	window.add(panel);
    JWindow window2 = new JWindow() {
        public void paint(Graphics g) {
          g.setColor(Color.blue);
          g.drawString("Hello, World!", 50, 50);
        }
      };
    try {
      if (graphicsDevice.isFullScreenSupported()) {
        graphicsDevice.setFullScreenWindow(window);
      }
      if (graphicsDevice2.isFullScreenSupported()) {
          graphicsDevice2.setFullScreenWindow(window2);
        }
//      Random random = new Random();
//      int mode = random.nextInt(displayModes.length);
//      DisplayMode displayMode = displayModes[mode];
//      System.out.println(displayMode.getWidth() + "x" + 
//        displayMode.getHeight() + " \t" + displayMode.getRefreshRate() + 
//        " / " + displayMode.getBitDepth());
//      if (graphicsDevice.isDisplayChangeSupported()) {
//        graphicsDevice.setDisplayMode(displayMode);
//      }
      Thread.sleep(10000);
    } catch (InterruptedException e) {
    } finally {
      graphicsDevice.setDisplayMode(originalDisplayMode);
      graphicsDevice.setFullScreenWindow(null);
    }
    System.exit(0);
  }
}