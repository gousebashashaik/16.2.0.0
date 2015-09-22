/**
 *
 */
package uk.co.portaltech.tui.breadcrumb;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * @author Abi
 *
 */
public class BreadCrumb
{
   private String url;

   private String name;

   private String linkClass;

   private boolean active;

   private boolean completed;

   public static final int PRIME = 31;

   /**
    * @param url
    * @param name
    * @param linkClass
    */
   public BreadCrumb(final String url, final String name, final String linkClass)
   {
      this.url = url;
      this.name = name;
      this.linkClass = linkClass;
   }

   public BreadCrumb(final String url, final String name, final String linkClass,
                     final boolean active, final boolean completed)
   {
      this.url = url;
      this.name = name;
      this.linkClass = linkClass;
      this.active = active;
      this.completed = completed;
   }

   /**
    * @param active the active to set
    */
   public void setActive(final boolean active)
   {
      this.active = active;
   }

   public String getUrl()
   {
      return url;
   }

   public void setUrl(final String url)
   {
      this.url = url;
   }

   public String getName()
   {
      return name;
   }

   public void setName(final String name)
   {
      this.name = name;
   }

   /**
    * @return the completed
    */
   public boolean isCompleted()
   {
      return completed;
   }

   /**
    * @param completed the completed to set
    */
   public void setCompleted(final boolean completed)
   {
      this.completed = completed;
   }

   /**
    * @return the active
    */
   public boolean isActive()
   {
      return active;
   }

   /**
    * @return the linkClass
    */
   public String getLinkClass()
   {
      return linkClass;
   }

   /**
    * @param linkClass the linkClass to set
    */
   public void setLinkClass(final String linkClass)
   {
      this.linkClass = linkClass;
   }

   @Override
   public int hashCode()
   {

      int result = 1;
      result = PRIME * result + ((linkClass == null) ? 0 : linkClass.hashCode());
      result = PRIME * result + ((name == null) ? 0 : name.hashCode());
      result = PRIME * result + ((url == null) ? 0 : url.hashCode());
      return result;
   }

   /*
    * Overridden equals method of the Breadcrumb instance using EqualsBuilder.
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(final Object obj)
   {
      if (obj instanceof BreadCrumb)
      {
         final BreadCrumb other = (BreadCrumb) obj;
         return new EqualsBuilder().append(name, other.name).append(url, other.url)
            .append(linkClass, other.linkClass).append(active, other.active)
            .append(completed, other.completed).isEquals();
      }
      else
      {
         return false;
      }
   }
}
