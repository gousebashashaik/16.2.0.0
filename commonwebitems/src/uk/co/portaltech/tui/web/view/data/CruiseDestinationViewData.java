/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

/**
 * @author EXTLP1
 *
 */
public class CruiseDestinationViewData
{
   private String title;

   private String linktitle;

   private String linkUrl;

   private List<CRdestinationViewData> destViewDatas;

   private String nileCruise;

   private String nileCruiseUrl;

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

   /**
    * @return the destViewDatas
    */
   public List<CRdestinationViewData> getDestViewDatas()
   {
      return destViewDatas;
   }

   /**
    * @param destViewDatas the destViewDatas to set
    */
   public void setDestViewDatas(final List<CRdestinationViewData> destViewDatas)
   {
      this.destViewDatas = destViewDatas;
   }

   public String getNileCruise()
   {
      return nileCruise;
   }

   public void setNileCruise(final String nileCruise)
   {
      this.nileCruise = nileCruise;
   }

   public String getNileCruiseUrl()
   {
      return nileCruiseUrl;
   }

   public void setNileCruiseUrl(final String nileCruiseUrl)
   {
      this.nileCruiseUrl = nileCruiseUrl;
   }

}
