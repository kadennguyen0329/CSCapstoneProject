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

//    public static final int LOBBY = 1;
//    public static final int EHALL = 2;
//    public static final int FHALL = 3;
//    public static final int CAFEA = 4;
//    public static final int CAFEB = 5;
//    public static final int AHALL1 = 6;
//    public static final int AHALL2 = 7;
//    public static final int AHALL3 = 8;
//    public static final int BHALL = 9;
//    public static final int CHALL1 = 10;
//    public static final int CHALL2 = 11;
//    public static final int DHALL = 12;
//    public static final int FHALL2 = 13;
//    public static final int GHALL = 14;
//    public static final int KHALL = 15;
//    public static final int LHALL = 16;
//    public static final int END = 17;