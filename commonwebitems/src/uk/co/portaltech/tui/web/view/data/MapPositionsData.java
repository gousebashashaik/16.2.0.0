/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.Map;

/**
 * Map positions data required for mobile website.
 *
 * @author niranjankumar.d
 *
 */
public class MapPositionsData
{

   private String id;

   private String lat;

   private String lng;

   private String name;

   private String url;

   private Map<String, String> info;

   private String type;

   private boolean selected;

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
    * @return the lat
    */
   public String getLat()
   {
      return lat;
   }

   /**
    * @param lat the lat to set
    */
   public void setLat(final String lat)
   {
      this.lat = lat;
   }

   /**
    * @return the lng
    */
   public String getLng()
   {
      return lng;
   }

   /**
    * @param lng the lng to set
    */
   public void setLng(final String lng)
   {
      this.lng = lng;
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
    * @return the info
    */
   public Map<String, String> getInfo()
   {
      return info;
   }

   /**
    * @param info the info to set
    */
   public void setInfo(final Map<String, String> info)
   {
      this.info = info;
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

}
