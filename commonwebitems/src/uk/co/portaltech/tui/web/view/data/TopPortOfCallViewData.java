package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * Data object for {@link TopPortOfCallViewData}
 */
public class TopPortOfCallViewData
{
   private String title;

   private String linktitle;

   private String linkUrl;

   private List<PortsOfCallViewData> pocViewDatas;

   public String getTitle()
   {
      return title;
   }

   public void setTitle(final String title)
   {
      this.title = title;
   }

   public String getLinktitle()
   {
      return linktitle;
   }

   public void setLinktitle(final String linktitle)
   {
      this.linktitle = linktitle;
   }

   public String getLinkUrl()
   {
      return linkUrl;
   }

   public void setLinkUrl(final String linkUrl)
   {
      this.linkUrl = linkUrl;
   }

   public List<PortsOfCallViewData> getPocViewDatas()
   {
      return pocViewDatas;
   }

   public void setPocViewDatas(final List<PortsOfCallViewData> pocViewDatas)
   {
      this.pocViewDatas = pocViewDatas;
   }

}
