/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

/**
 * @author extcs5
 *
 */
public class AccommodationCommonData
{

   private int noAccommodations;

   private String id;

   private String name;

   private boolean selected;

   private String parentCode;

   /**
    * @return the noAccommodations
    */
   public int getNoAccommodations()
   {
      return noAccommodations;
   }

   /**
    * @param noAccommodations the noAccommodations to set
    */
   public void setNoAccommodations(final int noAccommodations)
   {
      this.noAccommodations = noAccommodations;
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
    * @return the selected
    */
   public boolean isSelected()
   {
      return selected;
   }

   /**
    * @param selected the selected to set
    */
   public void setSelected(final boolean selected)
   {
      this.selected = selected;
   }

   /**
    * @return the parentCode
    */
   public String getParentCode()
   {
      return parentCode;
   }

   /**
    * @param parentCode the parentCode to set
    */
   public void setParentCode(final String parentCode)
   {
      this.parentCode = parentCode;
   }
}
