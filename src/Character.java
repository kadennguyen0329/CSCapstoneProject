public class Character extends Entity
{
   private int health;
   private int strength;
   private String name;
   
   public Character(int xCoord, int yCoord, int w, int h, String imageFileName, String name1, int health1, int speed1, int strength1)
   {
      super(xCoord, yCoord, w, h, speed1, imageFileName);
      name = name1;
      health = health1;
      strength = strength1;

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
   
  }

