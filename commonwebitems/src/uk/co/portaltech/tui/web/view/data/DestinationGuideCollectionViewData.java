/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

import uk.co.tui.book.domain.lite.Hotel;

/**
 * @author vinodkumar.g
 *
 */
public class DestinationGuideCollectionViewData
{

   private String name;

   private String id;

   private String type;

   private String inspireText;

   private String collectionImage;

   private String tRating;

   private boolean multiSelect;

   private List<Hotel> hotels;

   private boolean fewDaysFlag;

   private boolean available;

   private List<BestForViewData> bestFor;

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
    * @return the collectionImage
    */
   public String getCollectionImage()
   {
      return collectionImage;
   }

   /**
    * @param collectionImage the collectionImage to set
    */
   public void setCollectionImage(final String collectionImage)
   {
      this.collectionImage = collectionImage;
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
    * @return the hotels
    */
   public List<Hotel> getHotels()
   {
      return hotels;
   }

   /**
    * @param hotels the hotels to set
    */
   public void setHotels(final List<Hotel> hotels)
   {
      this.hotels = hotels;
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
    * @return the bestFor
    */
   public List<BestForViewData> getBestFor()
   {
      return bestFor;
   }

   /**
    * @param bestFor the bestFor to set
    */
   public void setBestFor(final List<BestForViewData> bestFor)
   {
      this.bestFor = bestFor;
   }

}
