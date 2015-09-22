/**
 *
 */
package uk.co.portaltech.tui.populators;

import static uk.co.portaltech.commons.DateUtils.format;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.results.DealsCollectionEndecaResults;
import uk.co.portaltech.travel.model.results.DealsPackageInfo;
import uk.co.portaltech.travel.model.results.Leg;
import uk.co.portaltech.travel.model.results.MerchandisedHoliday;
import uk.co.portaltech.travel.model.results.MerchandisedResult;
import uk.co.portaltech.travel.model.results.RoomDetails;
import uk.co.portaltech.travel.model.results.TabbedDealsResult;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.enums.BoardBasis;
import uk.co.portaltech.tui.helper.Pagination;
import uk.co.portaltech.tui.utils.PackageIdGenerator;
import uk.co.portaltech.tui.web.url.builders.BookFlowAccomPageUrlBuilder;
import uk.co.portaltech.tui.web.view.data.DealsCollectionResult;
import uk.co.portaltech.tui.web.view.data.DealsTabResult;
import uk.co.portaltech.tui.web.view.data.MerchandiserViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultAccomodationViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultFlightViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultPriceViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultRoomsData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.async.logging.TUILogUtils;

public class DealsCollectionMerchandiserResultsPopulator implements
   Populator<DealsCollectionEndecaResults, DealsCollectionResult>
{

   @Resource
   private BrandUtils brandUtils;

   @Resource
   private SearchResultAccomPopulator searchResultsAccomPopulator;

   @Resource
   private HolidaySearchResultsPopulator holidaySearchResultsPopulator;

   @Resource
   private BookFlowAccomPageUrlBuilder bookFlowAccomPageUrlBuilder;

   @Resource
   private ProductService productService;

   @Resource
   private Pagination pagination;

   @Resource
   private FeatureService featureService;

   private static final String FALCON_ACCOM_USPS = "falcon_usp_override";

   private static final String FALCON_PRODUCT_OVERRIDE = "falcon_product_override";

   private static final String USPS = "usps";

   private final List<String> featureDescriptorList = new ArrayList(Arrays.asList(new String[] {
      USPS, "tRating", "name", "strapline", FALCON_ACCOM_USPS, FALCON_PRODUCT_OVERRIDE }));

   private static final TUILogUtils LOG = new TUILogUtils(
      "DealsCollectionMerchandiserResultsPopulator");

   /**
    * Deals Populate method
    *
    * @param dealsCollectionEndecaResults
    * @param viewData
    */
   @Override
   public void populate(final DealsCollectionEndecaResults dealsCollectionEndecaResults,
      final DealsCollectionResult viewData)
   {

      if (dealsCollectionEndecaResults != null)
      {

         if (dealsCollectionEndecaResults.getDealsCollection().size() > 1)
         {
            viewData.setCategorizedPage(true);
         }

         final List<DealsTabResult> dealsTabList = viewData.getTabbedDealsList();
         // populate uncategorized deal
         if (!viewData.isCategorizedPage()
            && (CollectionUtils.isNotEmpty(dealsCollectionEndecaResults.getDealsCollection())))
         {

            final List<MerchandisedHoliday> paginatedHolidays =
               pagination.paginateMerchandiserResults(dealsCollectionEndecaResults
                  .getDealsCollection().get(0).getMerchandisedResult(),
                  viewData.getMerchandiserRequest());

            final DealsTabResult dealsTabResult = new DealsTabResult();
            dealsTabResult.setDealsCategoryname(dealsCollectionEndecaResults.getDealsCollection()
               .get(0).getDealsCategoryname());
            dealsTabResult.setDealsCategorytitle(dealsCollectionEndecaResults.getDealsCollection()
               .get(0).getDealsCategorytitle());

            if (CollectionUtils.isNotEmpty(dealsCollectionEndecaResults.getDealsCollection().get(0)
               .getMerchandisedResult().getHolidays()))
            {
               dealsTabResult.getMerchandiserResult().setEndecaResultsCount(
                  (dealsCollectionEndecaResults.getDealsCollection().get(0).getMerchandisedResult()
                     .getHolidays()).size());
            }
            populatepax(dealsCollectionEndecaResults.getDealsCollection().get(0)
               .getMerchandisedResult(), dealsTabResult.getMerchandiserResult());
            populatePaginatedHoliday(paginatedHolidays, dealsTabResult.getMerchandiserResult());
            dealsTabList.add(dealsTabResult);
         }
         // populate Categorized deal
         else
         {
            for (final TabbedDealsResult tabbedDealsList : dealsCollectionEndecaResults
               .getDealsCollection())
            {
               final DealsTabResult dealsTabResult = new DealsTabResult();
               dealsTabResult.setDealsCategoryname(tabbedDealsList.getDealsCategoryname());
               dealsTabResult.setDealsCategorytitle(tabbedDealsList.getDealsCategorytitle());
               if (CollectionUtils
                  .isNotEmpty(tabbedDealsList.getMerchandisedResult().getHolidays()))
               {
                  dealsTabResult.getMerchandiserResult().setEndecaResultsCount(
                     (tabbedDealsList.getMerchandisedResult().getHolidays()).size());
               }
               populatepax(tabbedDealsList.getMerchandisedResult(),
                  dealsTabResult.getMerchandiserResult());

               populatePaginatedHoliday(tabbedDealsList.getMerchandisedResult().getHolidays(),
                  dealsTabResult.getMerchandiserResult());
               dealsTabList.add(dealsTabResult);
            }
         }
      }
   }

   /**
    * Populate pax config from endeca
    */
   private void populatepax(final MerchandisedResult source, final MerchandiserViewData destinations)
   {
      destinations.setChildrenAge(source.getChildrenAge());
      destinations.setNoOfAdults(source.getNoOfAdults());
      destinations.setNoOfChildren(source.getNoOfChildren());
      destinations.setNoOfSeniors(source.getNoOfSeniors());

   }

   /**
    * Population of paginated holiday objected
    *
    * @param holidays
    * @param merchandiserViewData
    * @throws ConversionException
    */
   @SuppressWarnings("boxing")
   public void populatePaginatedHoliday(final List<MerchandisedHoliday> holidays,
      final MerchandiserViewData merchandiserViewData) throws ConversionException
   {

      if (CollectionUtils.isNotEmpty(holidays))
      {
         AccommodationModel accommodationModel = null;

         for (final MerchandisedHoliday holiday : holidays)
         {

            accommodationModel = (AccommodationModel) fetchProductModel(holiday);

            if (accommodationModel != null)
            {
               final SearchResultViewData resultsview =
                  new SearchResultViewData(holiday.getIndex());
               populateDealsPackageInfo(holiday.getDealsPackageInfo(), resultsview);

               populateFeaturesForAccom(accommodationModel, resultsview);
               searchResultsAccomPopulator.populate(accommodationModel, resultsview);
               populateMerchandisedHolidayViewData(holiday, resultsview);
               resultsview.setWishListId(PackageIdGenerator.generateIscapePackageId(resultsview));
               bookFlowAccomPageUrlBuilder.build(holiday, accommodationModel, resultsview,
                  merchandiserViewData);
               merchandiserViewData.getHolidays().add(resultsview);
            }
         }

      }
   }

   /**
    * Only required data of deals display is populated . populated details are Flight info , selling
    * code, package id Room data -- only first room data is populated as board basis data of first
    * room is required for display
    */

   public void populateDealsPackageInfo(final DealsPackageInfo dealsPackageInfo,
      final SearchResultViewData resultsview)
   {

      resultsview.setPackageId(dealsPackageInfo.getPackageId());
      resultsview.setTracsUnitCode(dealsPackageInfo.getTracs());
      resultsview.setSellingCode(dealsPackageInfo.getTracsDetails().getSellingCode());
      resultsview.setTracsPackageId(dealsPackageInfo.getTracsDetails().getTracsPackageId());
      resultsview.setProductCode(dealsPackageInfo.getTracsDetails().getProductCode());
      resultsview.setSubProductCode(dealsPackageInfo.getTracsDetails().getSubProductCode());
      if (StringUtils.isNotEmpty(dealsPackageInfo.getBrand()))
      {
         resultsview.setBrandType(dealsPackageInfo.getBrand());
      }

      populateFlightData(dealsPackageInfo, resultsview);

      if (CollectionUtils.isNotEmpty(dealsPackageInfo.getAccomodation().getRoomDetails()))
      {
         final RoomDetails rooms = dealsPackageInfo.getAccomodation().getRoomDetails().get(0);
         final SearchResultRoomsData searchResultRoomsData = new SearchResultRoomsData();
         searchResultRoomsData.setRoomCode(rooms.getRoomCode());
         searchResultRoomsData.setSellingout(rooms.getNoOfRooms());
         if (StringUtils.isNotBlank(rooms.getBoardBasisCode()))
         {
            final String boardCode = rooms.getBoardBasisCode();
            searchResultRoomsData.setBoardType(BoardBasis
               .valueOf(StringUtils.chomp(boardCode, "+")).getValue());
            searchResultRoomsData.setBoardBasisCode(boardCode);
         }
         resultsview.getAccommodation().getRooms().add(searchResultRoomsData);
      }

   }

   /**
    * Method to populate Flight related data
    *
    * @param dealsPackageInfo
    * @param resultsview
    */
   public void populateFlightData(final DealsPackageInfo dealsPackageInfo,
      final SearchResultViewData resultsview)
   {

      final SearchResultFlightViewData flight = resultsview.getItinerary();
      final Leg outbound = dealsPackageInfo.getItinerary().getOutbound().get(0);

      if (outbound.getSchedule().getDepartureDate() != null)
      {
         flight.setDepartureDate(format(outbound.getSchedule().getDepartureDate()));
      }
      if (outbound.getDeparture().getCode() != null)
      {
         flight.setDepartureAirport(holidaySearchResultsPopulator.getAirportName(outbound
            .getDeparture().getCode()));
      }

      flight.getOutbounds().addAll(
         holidaySearchResultsPopulator.populateLegInfo(dealsPackageInfo.getItinerary()
            .getOutbound(), null, dealsPackageInfo.getItinerary().getInbound().size()
            + dealsPackageInfo.getItinerary().getOutbound().size(), false));

      flight.getInbounds().addAll(
         holidaySearchResultsPopulator.populateLegInfo(
            dealsPackageInfo.getItinerary().getInbound(), flight.getOutbounds(), dealsPackageInfo
               .getItinerary().getInbound().size()
               + dealsPackageInfo.getItinerary().getOutbound().size(), true));

      flight.setDreamlinerLogo(holidaySearchResultsPopulator.isDreamlinerPackage(flight));
   }

   public void populateMerchandisedHolidayViewData(final MerchandisedHoliday holiday,
      final SearchResultViewData target) throws ConversionException
   {

      target.setDuration(Integer.parseInt(holiday.getStayPeriod()));
      target.getAccommodation().getRatings().setTripAdvisorRating(holiday.getTripAdvisorRating());
      populatePriceData(holiday, target);
      populateAccommodationData(holiday, target);
      target.setAvailbleFrom(holiday.getAvailableFrom());
   }

   /**
    * Method to populate Accommodation Data
    *
    * @param holiday
    * @param resultsview
    */
   private void populateAccommodationData(final MerchandisedHoliday holiday,
      final SearchResultViewData resultsview)
   {

      final SearchResultAccomodationViewData accommodation = resultsview.getAccommodation();
      accommodation.setCode(holiday.getCode());

      if (StringUtils.isBlank(accommodation.getRatings().getOfficialRating()))
      {
         accommodation.getRatings().setOfficialRating(holiday.gettRating());
      }
   }

   /**
    * Method to populate Holiday Price * @param holiday
    *
    * @param resultsview
    */
   private void populatePriceData(final MerchandisedHoliday holiday,
      final SearchResultViewData resultsview)
   {

      final SearchResultPriceViewData price = resultsview.getPrice();
      price.setPerPerson(holiday.getPriceFrom());
   }

   /**
    * @param holiday
    * @return product
    */
   private ProductModel fetchProductModel(final MerchandisedHoliday holiday)
   {
      ProductModel product = null;
      try
      {
         product = productService.getProductForCode(holiday.getCode());
         Validate.notNull(product);
      }
      catch (final ModelNotFoundException exc)
      {
         LOG.error("THe Product model with code " + holiday.getCode() + " does not exist in PIM.",
            exc);
      }
      catch (final UnknownIdentifierException e)
      {
         LOG.error("Endeca has product code and PIM doesnt have" + holiday.getCode()
            + " does not exist in PIM.", e);
      }
      return product;
   }

   /**
    * @param accommodationModel
    * @param resultsview
    */
   private void populateFeaturesForAccom(final AccommodationModel accommodationModel,
      final SearchResultViewData resultsview)
   {
      final Map<String, List<Object>> valuesForFeatures =
         featureService.getValuesForFeatures(featureDescriptorList, accommodationModel, new Date(),
            brandUtils.getFeatureServiceBrand(accommodationModel.getBrands()));
      final List<Object> featureValues = valuesForFeatures.get(FALCON_ACCOM_USPS);
      if (CollectionUtils.isNotEmpty(featureValues))
      {
         valuesForFeatures.put(USPS, featureValues);
      }
      resultsview.getAccommodation().putFeatureCodesAndValues(valuesForFeatures);
   }
}
