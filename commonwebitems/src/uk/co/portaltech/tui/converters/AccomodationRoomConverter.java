/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import uk.co.portaltech.travel.model.RoomModel;
import uk.co.portaltech.tui.web.view.data.AccommodationRoomViewData;

/**
 * @author gagan
 *
 */
public class AccomodationRoomConverter extends
   AbstractPopulatingConverter<RoomModel, AccommodationRoomViewData>
{

   @Override
   public AccommodationRoomViewData createTarget()
   {
      return new AccommodationRoomViewData();
   }

}
