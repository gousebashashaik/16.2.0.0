/**
 *
 */
package uk.co.tui.cr.book.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import javax.annotation.Resource;

import uk.co.portaltech.travel.thirdparty.cruise.endeca.pojo.PackageItemValue;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.Cruise;
import uk.co.tui.book.domain.lite.CruiseAndStayPackage;
import uk.co.tui.book.domain.lite.FlightItinerary;
import uk.co.tui.book.domain.lite.Hotel;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Stay;

/**
 * The Class CruiseNStayPackagePopulator.
 *
 * @author ramkishore.p
 */
public class CruiseNStayPackagePopulator implements Populator<Object, BasePackage>
{

   /** The cr itinerary populator. */
   @Resource
   private Populator<PackageItemValue, Itinerary> crItineraryPopulator;

   /** The cr passenger populator. */
   @Resource
   private Populator<PackageItemValue, BasePackage> crPassengerPopulator;

   /** The cr package element populator. */
   @Resource
   private Populator<PackageItemValue, BasePackage> crPackageElementPopulator;

   /** The cr cruise populator. */
   @Resource
   private Populator<PackageItemValue, Stay> crCruisePopulator;

   /** The cr hotel populator. */
   @Resource
   private Populator<PackageItemValue, Stay> crHotelPopulator;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final Object source, final BasePackage target) throws ConversionException
   {
      final PackageItemValue packageItemValue = (PackageItemValue) source;
      // Populate Itinerary details
      final Itinerary flightItinerary = new FlightItinerary();
      crItineraryPopulator.populate(packageItemValue, flightItinerary);
      ((CruiseAndStayPackage) target).setItinerary(flightItinerary);
      // Populate Cruise details
      final Stay cruiseStay = new Cruise();
      crCruisePopulator.populate(packageItemValue, cruiseStay);
      ((CruiseAndStayPackage) target).setCruise(cruiseStay);
      // Populate Hotel details
      final Stay hotelStay = new Hotel();
      crHotelPopulator.populate(packageItemValue, hotelStay);
      ((CruiseAndStayPackage) target).setHotel(hotelStay);
      // Populate Passenger details
      crPassengerPopulator.populate(packageItemValue, target);
      // Populate Other package details
      crPackageElementPopulator.populate(packageItemValue, target);
   }

}
