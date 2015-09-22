/**
 *
 */
package uk.co.tui.cr.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;

import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.Stay;
import uk.co.tui.book.domain.lite.StayType;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.cr.book.view.data.PackageViewData;

/**
 * @author ramkishore.p
 *
 */
public class AccommodationViewDataPopulator implements Populator<BasePackage, PackageViewData>
{
   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   /** The CruiseAccommodationViewDataPopulator. */
   @Resource
   private Populator<BasePackage, PackageViewData> crCruiseAccommodationViewDataPopulator;

   /** The HotelAccommodationViewDataPopulator. */
   @Resource
   private Populator<BasePackage, PackageViewData> crHotelAccommodationViewDataPopulator;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final BasePackage source, final PackageViewData target)
      throws ConversionException
   {
      boolean isCruiseDataPopulated = false;

      for (final Stay stay : packageComponentService.getAllStays(source))
      {
         if (StayType.SHIP == stay.getType())
         {
            if (!isCruiseDataPopulated)
            {
               crCruiseAccommodationViewDataPopulator.populate(source, target);
               // set the flag to ensure that for Back2Back cruise, CruiseViewData is
               // not populated for the second cruise itinerary
               isCruiseDataPopulated = true;
            }
         }
         else
         {
            crHotelAccommodationViewDataPopulator.populate(source, target);
         }
      }
   }
}
