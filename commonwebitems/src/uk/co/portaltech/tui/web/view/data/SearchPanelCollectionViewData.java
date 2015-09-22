/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author vinodkumar.g
 *
 */
public class SearchPanelCollectionViewData
{
   private String id;

   private String name;

   private String type;

   private String url;

   private String inspireURL;

   private String inspireImage;

   private String inspireText;

   private List<DestinationGuideCollectionViewData> children;

   /**
    * @return the id
    */
   public String getId()
   {
      return id;
   }

   /**
    * @return the children
    */
   public List<DestinationGuideCollectionViewData> getChildren()
   {
      return children;
   }

   /**
    * @param children the children to set
    */
   public void setChildren(final List<DestinationGuideCollectionViewData> children)
   {
      this.children = children;
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
    * @return the inspireURL
    */
   public String getInspireURL()
   {
      return inspireURL;
   }

   /**
    * @param inspireURL the inspireURL to set
    */
   public void setInspireURL(final String inspireURL)
   {
      this.inspireURL = inspireURL;
   }

   /**
    * @return the inspireImage
    */
   public String getInspireImage()
   {
      return inspireImage;
   }

   /**
    * @param inspireImage the inspireImage to set
    */
   public void setInspireImage(final String inspireImage)
   {
      this.inspireImage = inspireImage;
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

}
