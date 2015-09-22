/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

import uk.co.portaltech.tui.web.view.data.wrapper.UnitData;

/**
 * @author sunilkumar.sahu
 *
 */
public class DestinationData
{

   private String id;

   private String name;

   private String type;

   private List<DestinationData> children;

   private List<UnitData> hotels;

   private boolean multiSelect = true;

   private String tRating;

   private String inspireText;

   private boolean fewDaysFlag;

   private String url;

   private boolean available;

   public DestinationData()
   {

   }

   /**
    * @return the url
    */
   public final String getUrl()
   {
      return url;
   }

   /**
    * @param url the url to set
    */
   public final void setUrl(final String url)
   {
      this.url = url;
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
    * @return the inspireText
    */
   public String getInspireText()
   {
      return inspireText;
   }

   /**
    * @param inspireText the inspireText to set
    */
   public void setInspireText(final String inspireText)
   {
      this.inspireText = inspireText;
   }

   /**
    * @return the hotels
    */
   public List<UnitData> getHotels()
   {
      return hotels;
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
    * @param hotels the hotels to set
    */
   public void setHotels(final List<UnitData> hotels)
   {
      this.hotels = hotels;
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
    * @return the tRating
    */
   public String gettRating()
   {
      return tRating;
   }

   /**
    * @param tRating the tRating to set
    */
   public void settRating(final String tRating)
   {
      this.tRating = tRating;
   }

}
