public class Player extends Character
{
   private boolean isHiding;
   public Player(int xCoord, int yCoord, int w, int h, String imageFileName, int health1,int strength1 , int speed1, String name1)
   {
      super(xCoord, yCoord, w, h, imageFileName, speed1, strength1, health1, name1);
      isHiding = false;
      
   }
   
   public boolean getIsHiding()
   {
      return isHiding;
   }
   
   public void switchIsHiding()
   {
      isHiding = !isHiding;
   }
   
}