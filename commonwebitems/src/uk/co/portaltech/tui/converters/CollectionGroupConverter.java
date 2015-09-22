/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import uk.co.portaltech.tui.model.CollectionGroupsModel;
import uk.co.portaltech.tui.web.view.data.CollectionGroupViewData;

/**
 * @author niranjani.r
 *
 */
public class CollectionGroupConverter extends
   AbstractPopulatingConverter<CollectionGroupsModel, CollectionGroupViewData>
{

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter# createTarget()
    */
   @Override
   public void populate(final CollectionGroupsModel source, final CollectionGroupViewData target)
   {
      target.setId(source.getCode());
      target.setName(source.getName());
      target.setType("Collection");
      super.populate(source, target);
   }

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter# createTarget()
    */
   @Override
   public CollectionGroupViewData createTarget()
   {
      return new CollectionGroupViewData();
   }

}
