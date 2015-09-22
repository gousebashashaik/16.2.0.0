/**
 *
 */
package uk.co.portaltech.tui.web.view.data.wrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sunilkumar.sahu
 *
 */
public class ProductData
{

   private String code;

   private List<UnitData> hotels = new ArrayList<UnitData>();

   private String id;

   private String name;

   private String type;

   private boolean multiSelect = true;

   private boolean nonCoreProduct;

   public ProductData()
   {

   }

   private ProductData(final UnitData hotels, final String code, final String id,
                       final String name, final String type, final boolean multiSelect,
                       final boolean nonCoreProduct)
   {
      this.hotels.add(hotels);
      this.code = code;
      this.id = id;
      this.name = name;
      this.type = type;
      this.multiSelect = multiSelect;
      this.nonCoreProduct = nonCoreProduct;
   }

   /**
    * @return the multiSelect
    */
   public boolean isMultiSelect()
   {
      return multiSelect;
   }

   /**
    * @param multiSelect the multiSelect to set
    */
   public void setMultiSelect(final boolean multiSelect)
   {
      this.multiSelect = multiSelect;
   }

   /**
    * @return the id
    */
   public String getId()
   {
      return id;
   }

   /**
    * @param id the id to set
    */
   public void setId(final String id)
   {
      this.id = id;
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
    * @return the type
    */
   public String getType()
   {
      return type;
   }

   /**
    * @param type the type to set
    */
   public void setType(final String type)
   {
      this.type = type;
   }

   public static ProductData toProductData(final UnitData input)
   {
      if ("productrange".equalsIgnoreCase(input.getType())
         || ((input.getProductRangeType() != null) && (!"".equalsIgnoreCase(input
            .getProductRangeType()))))
      {
         return new ProductData(input, input.getProductRangeType(), input.getId(), input.getName(),
            input.getType(), input.isMultiSelect(), input.isNonCoreProduct());
      }
      else
      {
         return null;
      }
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
    * @return the hotels
    */
   public List<UnitData> getHotels()
   {
      return hotels;
   }

   /**
    * @param hotels the hotels to set
    */
   public void setHotels(final List<UnitData> hotels)
   {
      this.hotels = hotels;
   }

   /**
    * @return the nonCoreProduct
    */
   public boolean isNonCoreProduct()
   {
      return nonCoreProduct;
   }

   /**
    * @param nonCoreProduct the nonCoreProduct to set
    */
   public void setNonCoreProduct(final boolean nonCoreProduct)
   {
      this.nonCoreProduct = nonCoreProduct;
   }

}
