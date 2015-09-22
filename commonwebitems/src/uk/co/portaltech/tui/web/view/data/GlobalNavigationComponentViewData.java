/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author EXTLP1
 *
 */
public class GlobalNavigationComponentViewData
{
   // This view data is used in both TH & FC
   private TuiGlobalNavigationViewData tuiGlobalNavigationViewData =
      new TuiGlobalNavigationViewData();

   private CRGlobalNavigationViewData crGlobalNavigationViewData = new CRGlobalNavigationViewData();

   private DestGlobalNavigationViewData destGlobalNavigationViewData =
      new DestGlobalNavigationViewData();

   private Map<String, String> globalLinks;

   private static final Map<Boolean, String> STYLE_CLASS_NAME = new HashMap<Boolean, String>()
   {
      {
         put(Boolean.TRUE, "active");
         put(Boolean.FALSE, "");
      }
   };

   private String siteBrand;

   /**
    * @return the siteBrand
    */
   public String getSiteBrand()
   {
      return siteBrand;
   }

   /**
    * @param siteBrand the siteBrand to set
    */
   public void setSiteBrand(final String siteBrand)
   {
      this.siteBrand = siteBrand;
   }

   /**
    * @return the crGlobalNavigationViewData
    */
   public CRGlobalNavigationViewData getCrGlobalNavigationViewData()
   {
      return crGlobalNavigationViewData;
   }

   /**
    * @param crGlobalNavigationViewData the crGlobalNavigationViewData to set
    */
   public void setCrGlobalNavigationViewData(
      final CRGlobalNavigationViewData crGlobalNavigationViewData)
   {
      this.crGlobalNavigationViewData = crGlobalNavigationViewData;
   }

   /**
    * @param activeTab the activeTab to set
    */
   public void setActiveTabs(final String activeTab)
   {
      this.tuiGlobalNavigationViewData.setActive(activeTab);
      this.tuiGlobalNavigationViewData.setActiveStyle(STYLE_CLASS_NAME.get(Boolean
         .valueOf(tuiGlobalNavigationViewData.isActive())));

      this.crGlobalNavigationViewData.setActive(activeTab);
      this.crGlobalNavigationViewData.setActiveStyle(STYLE_CLASS_NAME.get(Boolean
         .valueOf(crGlobalNavigationViewData.isActive())));

      this.destGlobalNavigationViewData.setActive(activeTab);
      this.destGlobalNavigationViewData.setActiveStyle(STYLE_CLASS_NAME.get(Boolean
         .valueOf(destGlobalNavigationViewData.isActive())));
   }

   /**
    * @return the thGlobalNavigationViewData
    */
   public TuiGlobalNavigationViewData getTuiGlobalNavigationViewData()
   {
      return tuiGlobalNavigationViewData;
   }

   /**
    * @param thGlobalNavigationViewData the thGlobalNavigationViewData to set
    */
   public void setTuiGlobalNavigationViewData(
      final TuiGlobalNavigationViewData thGlobalNavigationViewData)
   {
      this.tuiGlobalNavigationViewData = thGlobalNavigationViewData;
   }

   /**
    * @return the destGlobalNavigationViewData
    */
   public DestGlobalNavigationViewData getDestGlobalNavigationViewData()
   {
      return destGlobalNavigationViewData;
   }

   /**
    * @param destGlobalNavigationViewData the destGlobalNavigationViewData to set
    */
   public void setDestGlobalNavigationViewData(
      final DestGlobalNavigationViewData destGlobalNavigationViewData)
   {
      this.destGlobalNavigationViewData = destGlobalNavigationViewData;
   }

   /**
    * @return the globalLinks
    */
   public Map<String, String> getGlobalLinks()
   {
      return globalLinks;
   }

   /**
    * @param globalLinks the globalLinks to set
    */
   public void setGlobalLinks(final Map<String, String> globalLinks)
   {
      this.globalLinks = globalLinks;
   }
}
