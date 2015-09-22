/**
 *
 */
package uk.co.tui.shortlist.data;

/**
 * @author akhileshvarma.d
 *
 */
public class ShortlistFlightData
{

   private String carrier;

   private String code;

   private boolean dreamLine;

   /**
    * @return the carrier
    */
   public String getCarrier()
   {
      return carrier;
   }

   /**
    * @param carrier the carrier to set
    */
   public void setCarrier(final String carrier)
   {
      this.carrier = carrier;
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
    * @return the dreamLine
    */
   public boolean isDreamLine()
   {
      return dreamLine;
   }

   /**
    * @param dreamLine the dreamLine to set
    */
   public void setDreamLine(final boolean dreamLine)
   {
      this.dreamLine = dreamLine;
   }

}
