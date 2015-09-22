/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import de.hybris.platform.acceleratorcms.enums.NavigationBarMenuLayout;

/**
 * @author gagan
 *
 */
public class NavigationBarComponentViewData
{

   private CMSLinkComponentViewData link;

   private String styleClass;

   private CMSNavigationNodeViewData navigationNode;

   private NavigationBarMenuLayout dropDownLayout;

   private Integer wrapAfter;

   private String name;

   private boolean currentPage;

   private String uid;

   /**
    * @return the uid
    */
   public String getUid()
   {
      return uid;
   }

   /**
    * @param uid the uid to set
    */
   public void setUid(final String uid)
   {
      this.uid = uid;
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
    * @return the link
    */
   public CMSLinkComponentViewData getLink()
   {
      return link;
   }

   /**
    * @param link the link to set
    */
   public void setLink(final CMSLinkComponentViewData link)
   {
      this.link = link;
   }

   /**
    * @return the styleClass
    */
   public String getStyleClass()
   {
      return styleClass;
   }

   /**
    * @param styleClass the styleClass to set
    */
   public void setStyleClass(final String styleClass)
   {
      this.styleClass = styleClass;
   }

   /**
    * @return the navigationNode
    */
   public CMSNavigationNodeViewData getNavigationNode()
   {
      return navigationNode;
   }

   /**
    * @param navigationNode the navigationNode to set
    */
   public void setNavigationNode(final CMSNavigationNodeViewData navigationNode)
   {
      this.navigationNode = navigationNode;
   }

   /**
    * @return the dropDownLayout
    */
   public NavigationBarMenuLayout getDropDownLayout()
   {
      return dropDownLayout;
   }

   /**
    * @param dropDownLayout the dropDownLayout to set
    */
   public void setDropDownLayout(final NavigationBarMenuLayout dropDownLayout)
   {
      this.dropDownLayout = dropDownLayout;
   }

   /**
    * @return the wrapAfter
    */
   public Integer getWrapAfter()
   {
      return wrapAfter;
   }

   /**
    * @param wrapAfter the wrapAfter to set
    */
   public void setWrapAfter(final Integer wrapAfter)
   {
      this.wrapAfter = wrapAfter;
   }

   /**
    * @return the currentPage
    */
   public boolean isCurrentPage()
   {
      return currentPage;
   }

   /**
    * @param currentPage the currentPage to set
    */
   public void setCurrentPage(final boolean currentPage)
   {
      this.currentPage = currentPage;
   }
}
