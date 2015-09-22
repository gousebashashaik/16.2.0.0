/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

/**
 * @author venkataharish.k
 *
 */
public class KidsClubComponentData
{
   private String featureTitle;

   private String name;

   private String imageUrl;

   private String description;

   private String intro;

   private String strapLine;

   private boolean isSubImage;

   /**
    * @return the strapLine
    */
   public String getStrapLine()
   {
      return strapLine;
   }

   /**
    * @param strapLine the strapLine to set
    */
   public void setStrapLine(final String strapLine)
   {
      this.strapLine = strapLine;
   }

   /**
    * @return the intro
    */
   public String getIntro()
   {
      return intro;
   }

   /**
    * @param intro the intro to set
    */
   public void setIntro(final String intro)
   {
      this.intro = intro;
   }

   /**
    * @return the featureTitle
    */
   public String getFeatureTitle()
   {
      return featureTitle;
   }

   /**
    * @param featureTitle the featureTitle to set
    */
   public void setFeatureTitle(final String featureTitle)
   {
      this.featureTitle = featureTitle;
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
    * @return the imageUrl
    */
   public String getImageUrl()
   {
      return imageUrl;
   }

   /**
    * @param imageUrl the imageUrl to set
    */
   public void setImageUrl(final String imageUrl)
   {
      this.imageUrl = imageUrl;
   }

   /**
    * @return the description
    */
   public String getDescription()
   {
      return description;
   }

   /**
    * @param description the description to set
    */
   public void setDescription(final String description)
   {
      this.description = description;
   }

   /**
    * @return the isSubImage
    */
   public boolean isSubImage()
   {
      return isSubImage;
   }

   /**
    * @param isSubImage the isSubImage to set
    */
   public void setSubImage(final boolean isSubImage)
   {
      this.isSubImage = isSubImage;
   }
}