/**
 *
 */
package uk.co.portaltech.tui.enums;

import org.apache.commons.lang.StringUtils;

/**
 * Enum class for holding flight names for flight option page of Thomson Temp fix
 *
 *
 */
public enum CarrierName
{
   ABR("Europe Air Post"), AEA("Air Europa"), BA("British Airways"), BE("FlyBe"), BGH(
      "Balkan Air Holidays"), BJ("Nouvelair"), DY("Norwegian"), EI("Aer Lingus"), IE("Aer Lingus"),
      EZY("Easyjet"), JAF("Jet Air Fly"), KM("Air Malta"), LS("Jet2"), OHY("Onur Air"), TCX(
         "Thomas Cook"), VOE("Volotea"), X3("TUIfly"), ZB("Monarch"), TOM("Thomson Airways"), ST(
         "Germania");
   
   private String value;
   
   private CarrierName(final String value)
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
   
   public static String findByCode(final String code)
   {
      for (final CarrierName carrier : values())
      {
         if (StringUtils.equals(code, carrier.toString()))
         {
            return carrier.getValue();
         }
      }
      return "Other Carrier";
   }
   
}
