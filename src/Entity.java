import javax.swing.ImageIcon;
public abstract class Entity
{
   private ImageIcon frame;
   private int x;
   private int y;
   private int height;
   private int width;
   private int speed;
   protected String[][] imageFileNames;
   
   public Entity(int xCoord, int yCoord, int w, int h, int speed1, String[][] imageFileName)
   {
      x = xCoord;
      y = yCoord;
      height = h;
      width = w;
      speed = speed1;
      imageFileNames = imageFileName;
      frame = new ImageIcon(imageFileNames[0][0]);
   }
   
   public void setIdle()
   {
      frame = new ImageIcon(imageFileNames[0][0]);
   }
   
   public void setFrame(int x, int y)
   {
      frame = new ImageIcon(imageFileNames[x][y]);
   }
   
   public ImageIcon getFrame()
   {
      return frame;
   }
   
   public int getX()
   {
      return x;
   }
   
   public void setX(int x1)
   {
      x = x1;
   }
   
   public int getHeight()
   {
      return height;
   }
   
   public int getWidth()
   {
      return width;
   }
   
   public int getY()
   {
      return y;
   }
   
   public void setY(int y1)
   {
      y = y1;
   }
   
   public void moveX(int spaces)
   {
      x += spaces;
   }
   
   public void moveY(int spaces)
   {
      y += spaces;
   }
   
   public int getSpeed(){
      return speed;
   }
   
   public String toString()
   {
      return x + " " + y + " " + width + " " + height;
   }
}

