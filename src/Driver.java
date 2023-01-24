import javax.swing.JFrame;

public class Driver
{

   public static Panel2 screen;

   public static void main(String[] args)
   {
      screen = new Panel2();
      JFrame frame = new JFrame("test");
      frame.setSize(Panel2.XSIZE, Panel2.YSIZE);
      frame.setLocation(0, 0);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setContentPane(screen);		
      frame.setVisible(true);
      frame.addKeyListener(screen);
   }     
}
