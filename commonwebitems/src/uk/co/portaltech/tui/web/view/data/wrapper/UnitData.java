/**
 *
 */
package uk.co.portaltech.tui.web.view.data.wrapper;

import uk.co.portaltech.travel.model.unit.Unit;

public class UnitData
{

   private String id;

   private String name;

   private String type;

   private String synonym;

   private String parent;

   private boolean available;

   private String productRangeType;

   private String parentName;

   private boolean multiSelect = true;

   private boolean nonCoreProduct;

   private boolean fewDaysFlag;

   /**
    * Default constructor needed for Jackson
    */
   public UnitData()
   {

   }

   public UnitData(final String id, final String name, final String type)
   {
      this.id = id;
      this.name = name;
      this.type = type;
   }

   public UnitData(final String id, final String name, final String type, final String synonym,
                   final String parent, final String productRangeType, final boolean multiSelect,
                   final boolean nonCoreProduct)
   {
      this.id = id;
      this.name = name;
      this.type = type;
      this.synonym = synonym;
      this.parent = parent;
      this.productRangeType = productRangeType;
      this.multiSelect = multiSelect;
      this.nonCoreProduct = nonCoreProduct;
   }

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

   @Override
   public boolean equals(final Object rhs)
   {
      if (this == rhs)
      {
         return true;
      }
      if (rhs == null || getClass() != rhs.getClass())
      {
         return false;
      }

      final UnitData airport = (UnitData) rhs;

      return this.id.equals(airport.id);
   }

   @Override
   public int hashCode()
   {
      return id.hashCode();
   }

   @SuppressWarnings("boxing")
   public static UnitData toUnitData(final Unit input)
   {
      if (input.getParent() != null)
      {
         return new UnitData(input.id(), input.name(), input.type(), input.matchedSynonym(), input
            .getParent().id(), input.getProductRangeType(),
            Boolean.valueOf(input.getMultiSelect()), Boolean.valueOf(input.getNonCoreProduct()));
      }
      else
      {
         return new UnitData(input.id(), input.name(), input.type(), input.matchedSynonym(),
            "null", input.getProductRangeType(), Boolean.valueOf(input.getMultiSelect()),
            Boolean.valueOf(input.getNonCoreProduct()));
      }
   }

   /**
    * @return the parentName
    */
   public String getParentName()
   {
      return parentName;
   }

   /**
    * @param parentName the parentName to set
    */
   public void setParentName(final String parentName)
   {
      this.parentName = parentName;
   }

   /**
    * @return the code
    */
   public String getId()
   {
      return id;
   }

   /**
    * @param code the code to set
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

   /**
    * @return the synonym
    */
   public String getSynonym()
   {
      return synonym;
   }

   /**
    * @param synonym the synonym to set
    */
   public void setSynonym(final String synonym)
   {
      this.synonym = synonym;
   }

   public boolean isAvailable()
   {
      return available;
   }

   public void setAvailable(final boolean available)
   {
      this.available = available;
   }

   /**
    * @return the parent
    */
   public String getParent()
   {
      return parent;
   }

   /**
    * @param parent the parent to set
    */
   public void setParent(final String parent)
   {
      this.parent = parent;
   }

   /**
    * @return the productRangeType
    */
   public String getProductRangeType()
   {
      return productRangeType;
   }

   /**
    * @param productRangeType the productRangeType to set
    */
   public void setProductRangeType(final String productRangeType)
   {
      this.productRangeType = productRangeType;
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
