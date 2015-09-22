/**
 *
 */
package uk.co.tui.th.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.RoundingMode;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.tui.utils.CurrencyUtils;
import uk.co.tui.book.domain.AltRoom;
import uk.co.tui.book.domain.RoomDetails;
import uk.co.tui.book.services.CurrencyResolver;
import uk.co.tui.th.book.view.data.RoomViewData;

/**
 * The Class RoomViewDataPopulator.
 *
 * @author pradeep.as
 */

public class RoomViewDataPopulator implements Populator<AltRoom, RoomViewData>
{

   @Resource
   private CurrencyResolver currencyResolver;

   private static final int TWO = 2;

   /**
    * Populates the roomviewdata.
    *
    * @param room the room
    * @param roomViewData the room view data
    * @throws ConversionException the conversion exception
    */
   @Override
   public void populate(final AltRoom room, final RoomViewData roomViewData)
      throws ConversionException
   {
      if (StringUtils.isNotEmpty(room.getTitle()))
      {
         roomViewData.setRoomTitle(room.getTitle());
      }
      populateAdditionalRooms(room, roomViewData);
      populateRoomDetails(room, roomViewData);
   }

   /**
    * Populate room data.
    *
    * @param room the room
    * @param roomViewData the room view data
    */
   public void populateAdditionalRooms(final AltRoom room, final RoomViewData roomViewData)
   {

      // revisit
      roomViewData.setQuantity(room.getQtyAvailable());
      roomViewData.setSelected(room.isSelected());
      roomViewData.setDefaultRoom(room.isDefault());
      roomViewData.setCurrencyAppendedPrice(CurrencyUtils.getCurrencySymbol(currencyResolver
         .getSiteCurrency()) + room.getTotalPrice().setScale(TWO, RoundingMode.HALF_UP));
      roomViewData.setPrice(room.getTotalPrice());
   }

   /**
    * Populate room details.
    *
    * @param room the room
    * @param roomViewData the room view data
    */
   private void populateRoomDetails(final AltRoom room, final RoomViewData roomViewData)
   {
      final RoomDetails roomDetails = room.getRoomDetails();
      roomViewData.setRoomCode(roomDetails.getRoomCode());
      roomViewData.setSellingCode(roomDetails.getSellingCode());
      roomViewData.setPackageId(roomDetails.getPackageId());
   }

}
