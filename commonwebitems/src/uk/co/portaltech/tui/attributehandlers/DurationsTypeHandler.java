/**
 *
 */
package uk.co.portaltech.tui.attributehandlers;

import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

import uk.co.portaltech.travel.model.HowLongDurationModel;

/**
 * @author omonikhide
 *
 */
public class DurationsTypeHandler implements DynamicAttributeHandler<String, HowLongDurationModel>
{

   /*
    * (non-Javadoc)
    * 
    * @see
    * de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler#get(de.hybris.platform
    * .servicelayer.model.AbstractItemModel)
    */
   @Override
   public String get(final HowLongDurationModel model)
   {
      return model.getDurationCode();
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler#set(de.hybris.platform
    * .servicelayer.model.AbstractItemModel, java.lang.Object)
    */
   @Override
   public void set(final HowLongDurationModel model, final String value)
   {

      throw new UnsupportedOperationException();
   }

}
