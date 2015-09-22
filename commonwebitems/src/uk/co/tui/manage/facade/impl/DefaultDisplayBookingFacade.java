/**
 *
 */
package uk.co.tui.manage.facade.impl;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import uk.co.travel.domain.manage.request.BookingSearchRequest;
import uk.co.travel.domain.manage.request.DisplayMemoRequest;
import uk.co.travel.domain.manage.response.DisplayMemoResponse;
import uk.co.travel.domain.manage.response.Memo;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.domain.lite.Address;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.Brand;
import uk.co.tui.book.domain.lite.PackageHoliday;
import uk.co.tui.book.domain.lite.PackageType;
import uk.co.tui.book.domain.lite.Passenger;
import uk.co.tui.book.page.enums.PageType;
import uk.co.tui.book.page.request.PageRequest;
import uk.co.tui.book.page.response.PageResponse;
import uk.co.tui.book.services.ISOCountryCodeService;
import uk.co.tui.book.services.NavigationPageResolver;
import uk.co.tui.book.utils.DefaultStaticContentWrapper;
import uk.co.tui.common.DateUtils;
import uk.co.tui.manage.criteria.BookingSearchCriteria;
import uk.co.tui.manage.facade.DisplayBookingFacade;
import uk.co.tui.manage.security.services.ManageSessionService;
import uk.co.tui.manage.services.exception.AmendNCancelServiceException;
import uk.co.tui.manage.services.exception.BookingNotFoundException;
import uk.co.tui.manage.services.exception.ChannelNotFoundException;
import uk.co.tui.manage.services.exception.TUIBusinessException;
import uk.co.tui.manage.services.inventory.DisplayBookingService;
import uk.co.tui.manage.services.inventory.ManageBookingMemoService;
import uk.co.tui.manage.viewdata.AvailableAccommodationViewData;
import uk.co.tui.manage.viewdata.ErrorViewData;
import uk.co.tui.manage.viewdata.ManageBookingSummaryPageStaticContentViewData;
import uk.co.tui.manage.viewdata.ManageHomePageStaticContentViewData;
import uk.co.tui.manage.viewdata.ManageHomePageViewData;
import uk.co.tui.manage.viewdata.MemoViewData;
import uk.co.tui.manage.viewdata.PackageViewData;
import uk.co.tui.manage.viewdata.PassengerAddressViewData;
import uk.co.tui.manage.viewdata.SecurityQuestionsResultViewData;
import uk.co.tui.manage.viewdata.bookingsummary.BookingSummaryPageViewData;

/**
 * @author veena.pn
 * 
 */
public class DefaultDisplayBookingFacade implements DisplayBookingFacade
{

   @Resource
   private DisplayBookingService displayBookingService;

   @Resource
   private ManageSessionService manageSessionService;

   @Resource
   private PackageCartService packageCartService;

   private static final Logger LOGGER = Logger.getLogger(DefaultDisplayBookingFacade.class);

   @Resource
   private Populator<PackageHoliday, PackageViewData> bookingSummaryPopulator;

   @Resource
   private Populator<PackageHoliday, PackageViewData> costofHolidaySummaryPopulator;

   @Resource
   private NavigationPageResolver defaultNavigationPageResolver;

   /** The static page content service. */
   @Resource
   private DefaultStaticContentWrapper staticContentServ;

   @Resource
   private ManageBookingMemoService manageBookingMemoService;

   @Resource
   private ISOCountryCodeService iSOCountryCodeService;

   /** The cms site service. */
   @Resource
   private CMSSiteService cmsSiteService;

   /*
    * @see uk.co.tui.manage.facade.DisplayBookingFacade#displayBooking(uk.co.tui.manage.criteria.
    * BookingSearchCriteria)
    */
   @Override
   public BookingSummaryPageViewData displayBooking(
      final BookingSearchCriteria bookingSearchCriteria, final String hostName)
   {
      LOGGER.info("Entering Default Display Booking Facade");
      final BookingSummaryPageViewData bookingSummaryPageViewData =
         new BookingSummaryPageViewData();
      final BookingSearchRequest bookingSearchRequest = new BookingSearchRequest();
      final String bookingRefId = bookingSearchCriteria.getBookingRefereneceId();
      bookingSearchRequest.setBookingReferenceNum(bookingRefId.trim());
      bookingSearchRequest.setDepartureDateFrom(bookingSearchCriteria.getDepartureDate());
      bookingSearchRequest.setPaxSurName(bookingSearchCriteria.getSecPassengerName());
      try
      {
         final PackageHoliday packageHoliday =
            displayBookingService.displayBooking(bookingSearchRequest);
         final PackageViewData packageViewData = new PackageViewData();
         packageViewData.settAndCDomainURL(hostName);
         bookingSummaryPopulator.populate(packageHoliday, packageViewData);
         costofHolidaySummaryPopulator.populate(packageHoliday, packageViewData);
         final DisplayMemoResponse displayMemoResponse =
            getBookingMemo(bookingSearchRequest, bookingSummaryPageViewData);
         final boolean isNotRekeydBooking = validateForRekeyedBooking(displayMemoResponse);
         LOGGER.info("is Rekeyed Booking=" + isNotRekeydBooking);

         populateDisplayMemoResponse(displayMemoResponse,
            packageViewData.getAvailableAccommodationViewData());
         bookingSummaryPageViewData.setPackageViewData(packageViewData);

      }
      catch (final AmendNCancelServiceException e)
      {

         LOGGER.info("Caught AmendNCancelServiceException ", e);
         final ErrorViewData errorData = new ErrorViewData();
         errorData.setError(Boolean.TRUE);
         errorData.setErrorCode(e.getErrorCode());

         final String errorMessage =
            "Sorry, we can't find a booking with these details. Please make sure you’ve entered all the information correctly. And have a read of the right-hand side of this page to confirm your booking can be managed online.";
         errorData.setErrorMessage(errorMessage);
         bookingSummaryPageViewData.setError(errorData);
         bookingSummaryPageViewData.setBookingSearchCriteria(bookingSearchCriteria);
      }
      catch (final ChannelNotFoundException e)
      {

         setErrorData(bookingSearchCriteria, bookingSummaryPageViewData, e);

      }

      catch (final BookingNotFoundException bnfEx)
      {
         setBookingNotFoundErrorData(bookingSearchCriteria, bookingSummaryPageViewData, bnfEx);

      }

      catch (final TUIBusinessException tuiBuzEx)
      {
         setBusinessErrorData(bookingSearchCriteria, bookingSummaryPageViewData, tuiBuzEx);

      }

      return bookingSummaryPageViewData;

   }

   @Override
   public BookingSummaryPageViewData displayBooking(final String hostName)
   {
      LOGGER.info("Entering Default Display Booking Facade");
      final BookingSummaryPageViewData bookingSummaryPageViewData =
         new BookingSummaryPageViewData();
      final BookingSearchRequest bookingSearchRequest = new BookingSearchRequest();

      try
      {

         final PackageHoliday packageHoliday = (PackageHoliday) packageCartService.getBasePackage();

         packageCartService.addToCart(packageHoliday);

         final PackageViewData packageViewData = new PackageViewData();
         packageViewData.settAndCDomainURL(hostName);
         bookingSummaryPopulator.populate(packageHoliday, packageViewData);
         costofHolidaySummaryPopulator.populate(packageHoliday, packageViewData);
         final DisplayMemoResponse displayMemoResponse =
            getBookingMemo(bookingSearchRequest, bookingSummaryPageViewData);

         populateDisplayMemoResponse(displayMemoResponse,
            packageViewData.getAvailableAccommodationViewData());
         bookingSummaryPageViewData.setPackageViewData(packageViewData);
      }
      catch (final AmendNCancelServiceException e)
      {

         LOGGER.info("Caught AmendNCancelServiceException ", e);
         final ErrorViewData errorData = new ErrorViewData();
         errorData.setError(Boolean.TRUE);
         errorData.setErrorCode(e.getErrorCode());

         bookingSummaryPageViewData.setError(errorData);
      }

      return bookingSummaryPageViewData;

   }

   /**
    * @param bookingSearchCriteria
    * @param bookingSummaryPageViewData
    * @param e
    */
   private void setErrorData(final BookingSearchCriteria bookingSearchCriteria,
      final BookingSummaryPageViewData bookingSummaryPageViewData, final ChannelNotFoundException e)
   {
      final ErrorViewData errorData = new ErrorViewData();
      errorData.setError(Boolean.TRUE.booleanValue());
      errorData.setErrorCode(e.getCode());
      errorData.setErrorMessage(e.getMessage());
      bookingSummaryPageViewData.setError(errorData);
      bookingSummaryPageViewData.setBookingSearchCriteria(bookingSearchCriteria);
   }

   @Override
   public DisplayMemoResponse getBookingMemo(final BookingSearchRequest bookingSearchRequest,
      final BookingSummaryPageViewData bookingSummaryPageViewData)
      throws AmendNCancelServiceException
   {

      final DisplayMemoRequest displayMemoRequest = new DisplayMemoRequest();
      DisplayMemoResponse displayMemoResponse = new DisplayMemoResponse();

      final String memoCodes = Config.getParameter("MEMOCODES");
      if (memoCodes != null)
      {
         displayMemoRequest.setMemoCodes(Arrays.asList(memoCodes.split(",")));
         displayMemoRequest.setBookingRefNumber(bookingSearchRequest.getBookingReferenceNum());
         try
         {
            displayMemoResponse = manageBookingMemoService.getBookingMemo(displayMemoRequest);
         }

         catch (final AmendNCancelServiceException ancEx)
         {

            throw new AmendNCancelServiceException(" Caught Error Code " + ancEx.getErrorCode(),
               null, ancEx);
         }

      }

      return displayMemoResponse;
   }

   boolean validateForRekeyedBooking(final DisplayMemoResponse displayMemoResponse)
      throws TUIBusinessException
   {
      return manageBookingMemoService.validateRekeyedBooking(displayMemoResponse);

   }

   private void setBusinessErrorData(final BookingSearchCriteria bookingSearchCriteria,
      final BookingSummaryPageViewData bookingSummaryPageViewData, final TUIBusinessException e)
   {
      final ErrorViewData errorData = new ErrorViewData();
      errorData.setError(Boolean.TRUE.booleanValue());
      errorData.setErrorCode(e.getCode());
      errorData.setErrorMessage(e.getMessage());
      bookingSummaryPageViewData.setError(errorData);
      bookingSummaryPageViewData.setBookingSearchCriteria(bookingSearchCriteria);
   }

   private void setBookingNotFoundErrorData(final BookingSearchCriteria bookingSearchCriteria,
      final BookingSummaryPageViewData bookingSummaryPageViewData, final BookingNotFoundException e)
   {
      final ErrorViewData errorData = new ErrorViewData();
      errorData.setError(Boolean.TRUE.booleanValue());
      errorData.setErrorCode(e.getCode());
      errorData.setErrorMessage(e.getMessage());
      bookingSummaryPageViewData.setError(errorData);
      bookingSummaryPageViewData.setBookingSearchCriteria(bookingSearchCriteria);
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.tui.manage.facade.DisplayBookingFacade#populateManageHomePageStaticContentViewData(uk
    * .co.tui.manage.viewdata .bookingsummary.BookingSummaryPageViewData)
    */
   @Override
   public void populateManageHomePageStaticContentViewData(
      final ManageHomePageViewData homePageViewData)
   {
      final ManageHomePageStaticContentViewData manageHomePageStaticContentViewData =
         new ManageHomePageStaticContentViewData();
      manageHomePageStaticContentViewData.setManageHomePageContentMap(staticContentServ
         .getManageHomePageContents());

      homePageViewData.setManageHomePageContentViewData(manageHomePageStaticContentViewData);
   }

   @Override
   public void populateBookingSummaryPageStaticContentViewData(
      final BookingSummaryPageViewData pageViewData)
   {
      final ManageBookingSummaryPageStaticContentViewData summaryPageStaticContentViewData =
         new ManageBookingSummaryPageStaticContentViewData();
      summaryPageStaticContentViewData.setManageBookingSummaryContentMap(staticContentServ
         .getBookingSummaryPageContents());

      pageViewData.setManageBookingSummaryContentViewData(summaryPageStaticContentViewData);
   }

   /*
    * To populate the special request
    */
   private void populateDisplayMemoResponse(final DisplayMemoResponse displayMemoResponse,
      final AvailableAccommodationViewData availableAccommodationViewData)
   {
      final List<MemoViewData> memoViewDatas = new ArrayList<MemoViewData>();
      if (displayMemoResponse != null && displayMemoResponse.getMemoList() != null)
      {
         for (final Memo memo : displayMemoResponse.getMemoList())
         {
            final MemoViewData viewData = new MemoViewData();
            viewData.setMemoCode(memo.getMemoCode());
            viewData.setMemoDescription(memo.getMemoDescription());
            memoViewDatas.add(viewData);
         }
      }
      availableAccommodationViewData.setSpecialReqest(memoViewDatas);

   }

   /**
    * Validate LeadPax Details
    * 
    * @param leadPaxShortAddress
    * @param contactNumber
    * @return passengerAddressViewData
    */
   @Override
   public SecurityQuestionsResultViewData validateLeadPaxDetails(final String leadPaxShortAddress,
      final String contactNumber)
   {
      final BasePackage packageHoliday = packageCartService.getBasePackage();
      final Address leadPaxAddress = getLeadPassengerAddress(packageHoliday.getPassengers());

      final SecurityQuestionsResultViewData viewData = new SecurityQuestionsResultViewData();

      if (StringUtils.equalsIgnoreCase(leadPaxShortAddress, leadPaxAddress.getHouseNumber())
         && StringUtils.equalsIgnoreCase(contactNumber, leadPaxAddress.getPhone1()))
      {
         final PassengerAddressViewData addressViewData = new PassengerAddressViewData();

         addressViewData.setFirstName(packageHoliday.getPassengers().get(0).getFirstname());
         addressViewData.setLastName(packageHoliday.getPassengers().get(0).getLastname());
         addressViewData.setGender(packageHoliday.getPassengers().get(0).getGender().getCode());

         if (packageHoliday.getPassengers().get(0).getDateOfBirth() != null)
         {
            addressViewData.setDateofbirth(DateUtils.amendnCancelDateFormat(new DateTime(
               packageHoliday.getPassengers().get(0).getDateOfBirth())));
         }

         addressViewData.setCity(leadPaxAddress.getTown());
         addCountry(leadPaxAddress, addressViewData);
         addressViewData.setEmail(leadPaxAddress.getEmail());
         addressViewData.setHouseNumber(leadPaxAddress.getHouseNumber());
         addressViewData.setPhone(leadPaxAddress.getPhone1());
         addressViewData.setPostalcode(leadPaxAddress.getPostalcode());
         addressViewData.setPostbox(leadPaxAddress.getPobox());
         addressViewData.setStreetName(leadPaxAddress.getStreetname());
         addressViewData.setRegion(leadPaxAddress.getCounty());

         manageSessionService.addBookingReferenceToSession(packageHoliday.getBookingRefNum());

         viewData.setAddress(addressViewData);
      }
      else
      {
         final ErrorViewData errorViewData = new ErrorViewData();
         errorViewData.setError(true);
         errorViewData.setErrorMessage("Invalid Contact Details");
         viewData.setError(errorViewData);
      }
      return viewData;
   }

   /**
    * @param leadPaxAddress
    * @param addressViewData
    */
   private void addCountry(final Address leadPaxAddress,
      final PassengerAddressViewData addressViewData)
   {
      if (iSOCountryCodeService.getCountryByAlpha2Code(leadPaxAddress.getCountry()) != null)
      {
         addressViewData.setCountry(iSOCountryCodeService.getCountryByAlpha2Code(
            leadPaxAddress.getCountry()).getShortName());
      }
   }

   /**
    * Gets the lead passenger Address
    * 
    * @param passengers
    * @return address
    */
   private Address getLeadPassengerAddress(final List<Passenger> passengers)
   {
      for (final Passenger passenger : passengers)
      {
         if (passenger.isLead())
         {
            return passenger.getAddresses().get(0);
         }
      }

      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.tui.manage.facade.DisplayBookingFacade#makeSecuredPage(uk.co.tui.manage.viewdata.
    * ManageHomePageViewData)
    */
   @Override
   public PageResponse makeSecuredPage(final String pageType)
   {
      final PageRequest pageRequest = new PageRequest();
      pageRequest.setBrand(Brand.valueOf(cmsSiteService.getCurrentSite().getUid()));
      if (pageType != null)
      {
         pageRequest.setCurrentPage(PageType.valueOf(pageType));
      }
      pageRequest.setPackageType(PackageType.INCLUSIVE);

      return defaultNavigationPageResolver.resolveNextPage(pageRequest);

   }

}
