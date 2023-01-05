public class Enemy extends Character
{
   public int previousX;
   public int previousY;
   public boolean isFollowing;
   
   public Enemy(int xCoord, int yCoord, int w, int h, String imageFileName, int health1,int strength1 , int speed1, String name1)
   {
      super(xCoord, yCoord, w, h, imageFileName, speed1, strength1, health1, name1);
      previousX = xCoord;
      previousY = yCoord;      
   }
   
   public int getPreviousX()
   {
      return previousX;
   }
   
   public int getPreviousY()
   {
      return previousY;
   }
   
   public void setPreviousX(int x)
   {
      previousX = x;
   }
   
   public void setPreviousY(int y)
   {
      previousY = y;
   }
   
   public boolean getIsFollowing()
   {
      return isFollowing;
   }
   
   public void setIsFollowing(boolean x)
   {
      isFollowing = x;
   }
}