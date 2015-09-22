/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

/**
 * @author Manju.ts
 *
 */
public class SliderRequest
{

   private String name;

   private String code;

   private String min;

   private String max;

   private boolean changed;

   private String maxValue;

   private String minValue;

   /**
    * @return the min
    */
   public String getMin()
   {
      return min;
   }

   /**
    * @param min the min to set
    */
   public void setMin(final String min)
   {
      this.min = min;
   }

   /**
    * @return the max
    */
   public String getMax()
   {
      return max;
   }

   /**
    * @param max the max to set
    */
   public void setMax(final String max)
   {
      this.max = max;
   }

   /**
    * @return the name
    */
   public String getName()
   {
      return name;
   }

   /**
    * @param name the name to set
    */
   public void setName(final String name)
   {
      this.name = name;
   }

   /**
    * @return the code
    */
   public String getCode()
   {
      return code;
   }

   /**
    * @param code the code to set
    */
   public void setCode(final String code)
   {
      this.code = code;
   }

   /**
    * @param changed the changed to set
    */
   public void setChanged(final boolean changed)
   {
      this.changed = changed;
   }

   /**
    * @return the changed
    */
   public boolean isChanged()
   {
      return changed;
   }

   /**
    * @return the maxValue
    */
   public String getMaxValue()
   {
      return maxValue;
   }

   /**
    * @param maxValue the maxValue to set
    */
   public void setMaxValue(final String maxValue)
   {
      this.maxValue = maxValue;
   }

   /**
    * @return the minValue
    */
   public String getMinValue()
   {
      return minValue;
   }

   /**
    * @param minValue the minValue to set
    */
   public void setMinValue(final String minValue)
   {
      this.minValue = minValue;
   }

}
