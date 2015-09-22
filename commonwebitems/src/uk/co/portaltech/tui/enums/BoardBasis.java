/**
 *
 */
package uk.co.portaltech.tui.enums;

/**
 *
 */
public enum BoardBasis
{

   HB("Half Board"), FB("Full Board"), AI("All Inclusive"), SC("Self Catering"), BB(
      "Bed and Breakfast"), RO("Room Only"), AB("As per Brochure");

   private String value;

   private BoardBasis(final String value)
   {
      this.value = value;
   }

   /**
    * @return the value
    */
   public String getValue()
   {
      return value;
   }

   /**
    * @param value the value to set
    */
   public void setValue(final String value)
   {
      this.value = value;
   }
}
