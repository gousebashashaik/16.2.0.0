/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author manju.ts
 *
 */
public class CommonFilterData
{

   private String name;

   private String id;

   private String filterType;

   private boolean disabled;

   private String type;

   private List<?> values;

   /**
    * For displaying the top 10 prioritised feature
    */
   private List<String> featurePriorityList;

   /**
    * @return the featurePriorityList
    */
   public List<String> getFeaturePriorityList()
   {
      return featurePriorityList;
   }

   /**
    * @param featurePriorityList the featurePriorityList to set
    */
   public void setFeaturePriorityList(final List<String> featurePriorityList)
   {
      this.featurePriorityList = featurePriorityList;
   }

   /**
    * @return the filterType
    */
   public String getFilterType()
   {
      return filterType;
   }

   /**
    * @param filterType the filterType to set
    */
   public void setFilterType(final String filterType)
   {
      this.filterType = filterType;
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
    * @return the values
    */
   public List<?> getValues()
   {
      return values;
   }

   /**
    * @param values the values to set
    */
   public void setValues(final List<?> values)
   {
      this.values = values;
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
    * @return the disabled
    */
   public boolean isDisabled()
   {
      return disabled;
   }

   /**
    * @param disabled the disabled to set
    */
   public void setDisabled(final boolean disabled)
   {
      this.disabled = disabled;
   }
}
