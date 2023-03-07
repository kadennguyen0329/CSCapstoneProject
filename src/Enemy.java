public class Enemy extends Character
{
   public boolean isFollowing;
   public int dps;
   public int prevHall;

   
   public Enemy(int xCoord, int yCoord, int w, int h, String[][] imageFileName, int health1,int strength1 , int speed1, String name1, int location1, int dmg)
   {
      super(xCoord, yCoord, w, h, imageFileName, health1, strength1, speed1, name1, location1);
      previousX = xCoord;
      previousY = yCoord;  
      dps = dmg;      
      prevHall = 1;
    
   }
   
   public int getDPS()
   {
      return dps;
   }
   
   public int getPrevHall()
   {
      return prevHall;
   }
   
   public void setPrevHall(int s)
   {
      prevHall = s;
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