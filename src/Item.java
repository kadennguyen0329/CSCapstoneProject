public abstract class Item extends Entity
{
   public Item(int xCoord, int yCoord, int w, int h, String[][] imageFileName)
   {
      super(xCoord, yCoord, w, h, 0, imageFileName);
   }
   
   public abstract void use(Character target);
}