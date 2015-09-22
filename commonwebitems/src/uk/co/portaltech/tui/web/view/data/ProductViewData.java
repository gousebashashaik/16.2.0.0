/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

/**
 * @author extcs5
 *
 */
public class ProductViewData
{

   private String type;

   private String name;

   private String code;

   private String multiSelect;

   private String brand;

   private boolean fewDaysFlag;

   /**
    * @return the fewDaysFlag
    */
   public boolean isFewDaysFlag()
   {
      return fewDaysFlag;
   }

   /**
    * @param fewDaysFlag the fewDaysFlag to set
    */
   public void setFewDaysFlag(final boolean fewDaysFlag)
   {
      this.fewDaysFlag = fewDaysFlag;
   }

   /**
    * @return the brand
    */
   public String getBrand()
   {
      return brand;
   }

   /**
    * @param brand the brand to set
    */
   public void setBrand(final String brand)
   {
      this.brand = brand;
   }

   /**
    * @return the multiSelect
    */
   public String getMultiSelect()
   {
      return multiSelect;
   }

   /**
    * @param multiSelect the multiSelect to set
    */
   public void setMultiSelect(final String multiSelect)
   {
      this.multiSelect = multiSelect;
   }

   /**
    * @return the code
    */
   public String getCode()
   {
      return code;
   }

   /**
    * @return the name
    */
   public String getName()
   {
      return name;
   }

   /**
    * @return the type
    */
   public String getType()
   {
      return type;
   }

   /**
    * @param code the code to set
    */
   public void setCode(final String code)
   {
      this.code = code;
   }

   /**
    * @param name the name to set
    */
   public void setName(final String name)
   {
      this.name = name;
   }

   /**
    * @param type the type to set
    */
   public void setType(final String type)
   {
      this.type = type;
   }

}
