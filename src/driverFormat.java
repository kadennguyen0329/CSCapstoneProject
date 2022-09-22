import javax.swing.JFrame;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class driverFormat
{

   public static panelFormat screen;

   public static void main(String[] args)
   {
      screen = new panelFormat();
      JFrame frame = new JFrame("test");
      frame.setSize(panelFormat.XSIZE, panelFormat.YSIZE);
      frame.setLocation(0, 0);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(screen);		
      frame.setVisible(true);
      frame.addKeyListener(screen);
   }     
}