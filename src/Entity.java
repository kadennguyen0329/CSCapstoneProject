import javax.swing.ImageIcon;
public class Entity
{
   private ImageIcon frame;
   private int x;
   private int y;
   private int height;
   private int width;
   
   public Entity(int xCoord, int yCoord, int w, int h, String imageFileName)
   {
      x = xCoord;
      y = yCoord;
      height = h;
      width = w;
      frame = new ImageIcon(imageFileName);
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
   
   public String toString()
   {
      return x + " " + y + " " + width + " " + height;
   }
}

