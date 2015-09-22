/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

import uk.co.portaltech.travel.enums.LocationType;

/**
 * @author sunilkumar.sahu
 *
 */
public class CountryViewData
{
   private String id;

   private String name;

   private LocationType type;

   private List<DestinationData> children;

   private boolean available;

   private String url;

   private String imageurl;

   private boolean multiSelect = true;

   private boolean fewDaysFlag;

   public CountryViewData()
   {

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
   public LocationType getType()
   {
      return type;
   }

   /**
    * @param type the type to set
    */
   public void setType(final LocationType type)
   {
      this.type = type;
   }

   /**
    * @return the children
    */
   public List<DestinationData> getChildren()
   {
      return children;
   }

   /**
    * @param children the children to set
    */
   public void setChildren(final List<DestinationData> children)
   {
      this.children = children;
   }

   /**
    * @return the available
    */
   public boolean isAvailable()
   {
      return available;
   }

   /**
    * @param available the available to set
    */
   public void setAvailable(final boolean available)
   {
      this.available = available;
   }

   /**
    * @return the url
    */
   public String getUrl()
   {
      return url;
   }

   /**
    * @param url the url to set
    */
   public void setUrl(final String url)
   {
      this.url = url;
   }

   /**
    * @return the imageurl
    */
   public String getImageurl()
   {
      return imageurl;
   }

   /**
    * @param imageurl the imageurl to set
    */
   public void setImageurl(final String imageurl)
   {
      this.imageurl = imageurl;
   }

}
