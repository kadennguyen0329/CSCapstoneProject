public class Player extends Character
{
   private boolean isHiding;
   public Player(int xCoord, int yCoord, int w, int h, String[][] imageFileName, int health1 ,int strength1 , int speed1, String name1, int location1)
   {
      super(xCoord, yCoord, w, h, imageFileName, health1, strength1, speed1, name1, location1);
      isHiding = false;
      
   }
   
   
   public boolean isHiding()
   {
      return isHiding;
   }
   
   public void setIsHiding(boolean x)
   {
      isHiding = x;
   }
   
}