/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import uk.co.portaltech.travel.model.AirportModel;
import uk.co.portaltech.travel.model.results.MerchandisedHoliday;
import uk.co.portaltech.travel.services.AirportService;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.utils.PackageIdGenerator;
import uk.co.portaltech.tui.web.view.data.MerchandiserViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultPriceViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.async.logging.TUILogUtils;

public class MerchandiserResultsPopulator implements
   Populator<List<MerchandisedHoliday>, MerchandiserViewData>
{

   @Resource
   private BrandUtils brandUtils;

   @Resource
   private SearchResultAccomPopulator searchResultsAccomPopulator;

   @Resource
   private AirportService airportService;

   @Resource
   private ProductService productService;

   @Resource
   private TUIUrlResolver<ProductModel> tuiProductUrlResolver;

   @Resource
   private FeatureService featureService;

   private static final String FALCON_ACCOM_USPS = "falcon_usp_override";

   private static final String USPS = "usps";

   private final List<String> featureDescriptorList = new ArrayList(Arrays.asList(new String[] {
      USPS, "tRating", "name", "strapline", FALCON_ACCOM_USPS }));

   private static final TUILogUtils LOG = new TUILogUtils("MerchandiserResultsPopulator");

   @SuppressWarnings("boxing")
   @Override
   public void populate(final List<MerchandisedHoliday> holidays,
      final MerchandiserViewData resultsViewData) throws ConversionException
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
               if (StringUtils.isNotBlank(holiday.getBrandType()))
               {
                  resultsview.setBrandType(holiday.getBrandType());
               }

               resultsview.setPageLabel(resultsViewData.getPageLabel());
               populateFeaturesForAccom(accommodationModel, resultsview);
               searchResultsAccomPopulator.populate(accommodationModel, resultsview);
               populateMerchandisedHolidayViewData(holiday, resultsview);
               resultsview.setWishListId(PackageIdGenerator.generateIscapePackageId(resultsview));

               resultsview.getAccommodation().setUrl(
                  tuiProductUrlResolver.resolve(accommodationModel));

               resultsview.setPackageId(holiday.getCode());
               resultsViewData.getHolidays().add(resultsview);
            }
         }

      }

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

   public void populateMerchandisedHolidayViewData(final MerchandisedHoliday holiday,
      final SearchResultViewData target) throws ConversionException
   {

      target.setDuration(Integer.parseInt(holiday.getStayPeriod()));
      target.getAccommodation().getRatings().setTripAdvisorRating(holiday.getTripAdvisorRating());
      populatePriceData(holiday, target);

      target.getAccommodation().setCode(holiday.getCode());
      target.getItinerary().setDepartureAirport(getDeparturePoint(holiday.getDeparturePoint()));
      target.setAvailbleFrom(holiday.getAvailableFrom());
   }

   /**
    * Method to populate Holiday Price
    *
    * @param holiday
    * @param resultsview
    */
   private void populatePriceData(final MerchandisedHoliday holiday,
      final SearchResultViewData resultsview)
   {

      final SearchResultPriceViewData price = resultsview.getPrice();
      price.setPerPerson(holiday.getPriceFrom());
   }

   private String getDeparturePoint(final String value)
   {
      String departurePoint = null;
      if (!StringUtils.isEmpty(value))
      {
         final AirportModel airportByCode = airportService.getAirportByCode(value);
         if (airportByCode != null)
         {
            departurePoint = airportByCode.getName();
         }
      }
      return departurePoint;
   }

   public String changeDateFormat(final String date)
   {
      try
      {
         final SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
         final SimpleDateFormat newFormat = new SimpleDateFormat("dd-MMM-yyyy");
         final Date newDate = format1.parse(date);
         return newFormat.format(newDate).replace("-", " ");
      }
      catch (final ParseException e)
      {
         LOG.error(e.getMessage(), e);
      }
      return null;
   }
}
