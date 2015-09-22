/**
 *
 */
package uk.co.portaltech.tui.facades;

import uk.co.portaltech.tui.web.view.data.GlobalNavigationComponentViewData;
import uk.co.tui.cr.components.model.GlobalNavigationComponentModel;

/**
 * @author EXTLP1
 * 
 */
public interface GlobalNavigationFacade
{

   /**
    * 
    * @param globalNavigationComponent
    * @param pageId
    * @return GlobalNavigationComponentViewData
    * @description This method website specific global navigation component instance and pageId as
    *              input. This method returns GlobalNavigationComponentViewData as output. This view
    *              data have header info for current and all other website.
    */
   GlobalNavigationComponentViewData getGlobalNavigationViewData(
      GlobalNavigationComponentModel globalNavigationComponent, String pageId);

}
