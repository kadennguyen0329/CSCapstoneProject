public class Enemy extends Character
{
   public boolean isFollowing;
   
   public Enemy(int xCoord, int yCoord, int w, int h, String imageFileName, int health1,int strength1 , int speed1, String name1, int location1)
   {
      super(xCoord, yCoord, w, h, imageFileName, speed1, strength1, health1, name1, location1);
      previousX = xCoord;
      previousY = yCoord;      
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