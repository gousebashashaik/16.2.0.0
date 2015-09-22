/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import uk.co.portaltech.tui.components.model.PromoComponentModel;
import uk.co.portaltech.tui.web.view.data.PromoComponentData;

/**
 * @author pts
 *
 */
public class PromoComponentConverter extends
   AbstractPopulatingConverter<PromoComponentModel, PromoComponentData>
{
   @Override
   public PromoComponentData createTarget()
   {
      return new PromoComponentData();
   }

   @Override
   public void populate(final PromoComponentModel source, final PromoComponentData target)
   {
      target.setComponentID(source.getUid());
      target.setPromoText(source.getPromoText());
      if (source.getMediaClip() != null)
      {
         target.setUrl(source.getMediaClip().getURL());
         target.setMediaFormat(source.getMediaClip().getMime());
      }
   }
}
