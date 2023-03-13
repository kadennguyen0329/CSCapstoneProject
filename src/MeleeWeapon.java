public class MeleeWeapon extends Item
{
   private int damage;
   public MeleeWeapon(int xCoord, int yCoord, int w, int h, String[][] imageFileName, int dmg)
   {
      super(xCoord, yCoord, w, h, imageFileName);
      damage = dmg;
   }
}