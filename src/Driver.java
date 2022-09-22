import javax.swing.JFrame;

public class Driver
{

   public static Panel screen;

   public static void main(String[] args)
   {
      screen = new Panel();
      JFrame frame = new JFrame("test");
      frame.setSize(Panel.XSIZE, Panel.YSIZE);
      frame.setLocation(0, 0);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(screen);		
      frame.setVisible(true);
      frame.addKeyListener(screen);
   }     
}