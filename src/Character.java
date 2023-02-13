public class Character extends Entity
{
   public int previousX;
   public int previousY;
   private int health;
   private int strength;
   private int location;
   private String name;
   
   public Character(int xCoord, int yCoord, int w, int h, String imageFileName, int health1, int speed1, int strength1, String name1, int location1)
   {
      super(xCoord, yCoord, w, h, speed1, imageFileName);
      name = name1;
      health = health1;
      strength = strength1;
      location = location1;
   
   }
   
   public void damage(int num)
   {
      health += -num;
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
   
   public int getHealth(){
      return health;
   }
   
   public int getStrength(){
      return strength;
   }
   
   public String getName(){
      return name;
   }
   
   public int getLocation()
   {
      return location;
   }
   
   public void setLocation(int l)
   {
      location = l;
   }
   
}