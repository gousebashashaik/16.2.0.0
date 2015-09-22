/**
 *
 */
package uk.co.portaltech.tui.enums;

/**
 * @author narendra.bm
 * 
 */
public enum MultiStayNames
{
   NILECRUISEANDSTAY("NILE CRUISE & STAY"), TOURANDSTAY("TOUR & STAY"),
      GULETCRUISE("GULET CRUISE"), NILECRUISE("NILE CRUISE"), TWIN_MULTICENTRE("TWIN MULTICENTRE"),
      DAYTRIP("Lapland Daytrips");
   private String value;

   /**
    * @param value
    */
   private MultiStayNames(final String value)
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
