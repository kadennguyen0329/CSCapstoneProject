public class MeleeWeapon extends Item
{
   private int damage;
   private int range;
   
   public MeleeWeapon(int xCoord, int yCoord, int w, int h, String[][] imageFileName, int dmg, int rg, int cd)
   {
      super(xCoord, yCoord, w, h, imageFileName, cd);
      damage = dmg;
      range  = rg;
   }
   
   public int getRange()
   {
      return range;
   }
   
   public int getDamage()
   {
      return damage;
   }
   
   public void use(Character target)
   {
      if(Math.sqrt(Math.pow(this.getX()-target.getX(), 2) + Math.pow(this.getY()-target.getY(), 2)) < range)
         target.damage(damage);
   } 
}