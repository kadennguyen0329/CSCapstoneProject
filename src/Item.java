public abstract class Item extends Entity
{
   private int coolDown;
   private boolean onCooldown;
   public Item(int xCoord, int yCoord, int w, int h, String[][] imageFileName, int cd)
   {
      super(xCoord, yCoord, w, h, 0, imageFileName);
      coolDown = cd;
   }
   
   public void setOnCoolDown(boolean cd)
   {
      onCooldown = cd;
   }
   
   public boolean onCoolDown()
   {
      return onCooldown;
   }
   
      public int getCoolDown()
      {
         return coolDown;
      }

   
   public abstract void use(Character target);
}