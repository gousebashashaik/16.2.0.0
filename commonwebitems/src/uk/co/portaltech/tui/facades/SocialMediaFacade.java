/**
 *
 */
package uk.co.portaltech.tui.facades;

import uk.co.portaltech.tui.components.model.SocialMediaLinksComponentModel;
import uk.co.portaltech.tui.web.view.data.SocialMediaLinksComponentData;

/**
 * @author arun.y
 *
 */
public interface SocialMediaFacade
{

   SocialMediaLinksComponentData getSocialMedia(
      SocialMediaLinksComponentModel socialMediaLinksComponentModel, String relativeurl);

   /**
    * @param socialMediaLinksComponentModel
    * @param relativeurl
    * @param productCode
    * @param pageType
    * @return
    */
   SocialMediaLinksComponentData getSocialMedia(
      SocialMediaLinksComponentModel socialMediaLinksComponentModel, String relativeurl,
      String productCode, String pageType);

}
