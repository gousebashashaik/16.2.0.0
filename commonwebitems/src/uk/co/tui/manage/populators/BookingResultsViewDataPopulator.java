/**
 *
 */
package uk.co.tui.manage.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.accommodation.MainstreamAccommodationService;
import uk.co.portaltech.tui.web.view.data.BookingAddressViewData;
import uk.co.portaltech.tui.web.view.data.BookingResultsViewData;
import uk.co.portaltech.tui.web.view.data.BookingSummaryViewData;
import uk.co.travel.domain.manage.response.BookingAddress;
import uk.co.travel.domain.manage.response.BookingSearchResponse;
import uk.co.travel.domain.manage.response.BookingSummary;

/**
 * @author veena.pn
 *
 */
public class BookingResultsViewDataPopulator implements
   Populator<BookingSearchResponse, BookingResultsViewData>
{
   private static final Logger LOGGER = Logger.getLogger(BookingResultsViewDataPopulator.class);

   @Resource
   private MainstreamAccommodationService accommodationService;

   @Resource
   private CMSSiteService cmsSiteService;

   static final int THREE = 3;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final BookingSearchResponse source, final BookingResultsViewData target)
      throws ConversionException
   {
      LOGGER.info("Entering Booking results poupulator");
      final List<BookingSummaryViewData> bookingSummaryViewDataList =
         new ArrayList<BookingSummaryViewData>();
      if (source.getBookingSummary() != null)
      {
         chcekTargetNotNull(target);
         for (final BookingSummary bookSummary : source.getBookingSummary())
         {
            final BookingSummaryViewData bookingSummaryViewData = new BookingSummaryViewData();

            bookingSummaryViewData.setBookingShopName(bookSummary.getAgentDetails());
            bookingSummaryViewData.setBookingReference(bookSummary.getBookingReference());
            // Adding seven 0's to booking reference Id
            // This will be removed when we integrate with correct booking reference Id
            checkBookingRefField(bookSummary, bookingSummaryViewData);
            bookingSummaryViewData.setLeadPassengerName(bookSummary.getLeadPaxName());
            checkBookingDeptDateNotNull(bookSummary, bookingSummaryViewData);
            bookingSummaryViewData.setBookingAddress(populateBookingAddress(bookSummary
               .getBookingAddress()));

            // final AccommodationModel accommodationModel =
            // accommodationService.getAccomodationByCodeAndCatalogVersion(

            // Need to be removed when actual prod code is from response
            final AccommodationModel accommodationModel =
               accommodationService.getAccomodationByCodeAndCatalogVersion("002766",
                  cmsSiteService.getCurrentCatalogVersion(), null);
            populateDetsinationName(accommodationModel, bookingSummaryViewData);
            bookingSummaryViewDataList.add(bookingSummaryViewData);
         }
         target.setBookingsSummary(bookingSummaryViewDataList);
      }
      else
      {
         final BookingResultsViewData bookingResults = new BookingResultsViewData();
         bookingResults.setResultsAvailable(false);

      }
   }

   /**
     *
     */
   private void populateDetsinationName(final AccommodationModel accommodationModel,
      final BookingSummaryViewData bookingSummaryViewData)
   {
      if (accommodationModel != null && accommodationModel.getSupercategories() != null)
      {
         for (final CategoryModel categorys : accommodationModel.getSupercategories())
         {
            if (categorys instanceof LocationModel)
            {
               for (final CategoryModel category : categorys.getAllSupercategories())
               {

                  if (category instanceof LocationModel
                     && ("DESTINATION").equalsIgnoreCase(((LocationModel) category).getType()
                        .toString()))

                  {
                     bookingSummaryViewData.setDestinationName(category.getName());

                  }

               }
            }
         }
      }
   }

   /**
    * @param bookSummary
    * @param bookingSummaryViewData
    */
   private void checkBookingDeptDateNotNull(final BookingSummary bookSummary,
      final BookingSummaryViewData bookingSummaryViewData)
   {
      if (bookSummary.getDepartureDate() != null)
      {
         bookingSummaryViewData.setDepartureDate(DateUtils.getDateInStringFormat(
            bookSummary.getDepartureDate(), "dd MMM yyyy"));
      }
   }

   /**
    * @param bookSummary
    * @param bookingSummaryViewData
    */
   private void checkBookingRefField(final BookingSummary bookSummary,
      final BookingSummaryViewData bookingSummaryViewData)
   {
      if (bookSummary.getBookingReference().length() <= THREE)
      {
         final StringBuilder sb = new StringBuilder("0000000");
         sb.append(bookSummary.getBookingReference());
         bookingSummaryViewData.setBookingReference(sb.toString());
      }
   }

   /**
    * @param target
    */
   private void chcekTargetNotNull(final BookingResultsViewData target)
   {
      if (target != null)
      {
         target.setResultsAvailable(true);
      }
   }

   /**
    * @param bookingAddress
    * @return
    */
   private BookingAddressViewData populateBookingAddress(final BookingAddress bookingAddress)
   {
      final BookingAddressViewData addessViewData = new BookingAddressViewData();
      addessViewData.setAddress(bookingAddress.getAddress());
      addessViewData.setContactNumber(bookingAddress.getContactNumber());
      addessViewData.setEmailAddress(bookingAddress.getEmail());

      return addessViewData;
   }
}
