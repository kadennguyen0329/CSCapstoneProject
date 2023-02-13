public class Enemy extends Character
{
   public boolean isFollowing;
      public int dps;

   
   public Enemy(int xCoord, int yCoord, int w, int h, String imageFileName, int health1,int strength1 , int speed1, String name1, int location1, int dmg)
   {
      super(xCoord, yCoord, w, h, imageFileName, speed1, strength1, health1, name1, location1);
      previousX = xCoord;
      previousY = yCoord;  
            dps = dmg;      
    
   }
   
      public int getDPS()
   {
      return dps;
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