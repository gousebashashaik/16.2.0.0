/**
 *
 */
package uk.co.tui.cr.book.populators;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.results.InventoryDetails;
import uk.co.portaltech.travel.services.AccommodationService;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.thirdparty.cruise.endeca.pojo.MultiCenterUnits;
import uk.co.portaltech.travel.thirdparty.cruise.endeca.pojo.PackageItemValue;
import uk.co.portaltech.travel.thirdparty.cruise.endeca.pojo.PackageSailingInfo;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.book.domain.lite.Cruise;
import uk.co.tui.book.domain.lite.Money;
import uk.co.tui.book.domain.lite.Price;
import uk.co.tui.book.domain.lite.Rating;
import uk.co.tui.book.domain.lite.Stay;
import uk.co.tui.book.domain.lite.StayType;
import uk.co.tui.book.domain.lite.SupplierProductDetails;
import uk.co.tui.book.services.CurrencyResolver;

/**
 * @author ramkishore.p
 *
 */
public class CruisePopulator implements Populator<PackageItemValue, Stay>
{

   /** The accommodation service. */
   @Resource
   private AccommodationService accommodationService;

   /** The cms site service. */
   @Resource
   private CMSSiteService cmsSiteService;

   /** The Feature Service . */
   @Resource
   private FeatureService featureService;

   /** The currency resolver. */
   @Resource
   private CurrencyResolver currencyResolver;

   /** The brand utils. */
   @Resource
   private BrandUtils brandUtils;

   /** The Constant CRUISE_STAY. */
   private static final String CRUISE_STAY = "CRZ";

   /** The t Rating of the accommodation. */
   private static final String T_RATING = "tRating";

   /** The Constant INDEX_ZERO. */
   private static final int INDEX_ZERO = 0;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final PackageItemValue packageItemValue, final Stay stay)
      throws ConversionException
   {
      populateFromShipModel(packageItemValue, stay);
      stay.setCode(packageItemValue.getPckShip().getCode());
      final Price price = new Price();
      final PackageSailingInfo packageSailingInfo = packageItemValue.getPckSailingInfo().get(0);
      final MultiCenterUnits sourceAccom =
         getAccom(packageSailingInfo.getMulitCenterUnits(), CRUISE_STAY);
      price.setAmount(convertBigDecimalToMoneyLite(new BigDecimal(packageSailingInfo
         .getPackagePrice().getTotalPrice())));
      stay.setPrice(price);
      ((Cruise) stay).setJourneyCode(sourceAccom.getId());
      ((Cruise) stay)
         .setJourneyName(packageItemValue.getPckItineraries().get(INDEX_ZERO).getName());
      stay.setStartDate(sourceAccom.getStartDate().toDate());
      stay.setEndDate(populateAccomEndDate(sourceAccom));
      stay.setDuration(sourceAccom.getStay());
      stay.setType(StayType.SHIP);
      populateInventoryDetails(
         getAccom(packageItemValue.getPckSailingInfo().get(0).getMulitCenterUnits(), CRUISE_STAY)
            .getInventoryDetails(), stay);
   }

   /**
    * @param multiCentreData
    * @return Accom
    */
   private MultiCenterUnits getAccom(final List<MultiCenterUnits> multiCentreData,
      final String stayType)
   {
      MultiCenterUnits sourceAccom = null;
      for (final MultiCenterUnits multiCentreDatum : multiCentreData)
      {
         if (StringUtils.equalsIgnoreCase(stayType, multiCentreDatum.getStayType()))
         {
            sourceAccom = multiCentreDatum;
            break;
         }
      }
      return sourceAccom;
   }

   /**
    *
    * This method takes the value as BigDecimal and returns the MoneyModel object.
    *
    * @param value the value as BigDecimal.
    * @return MoneyModel
    */
   private Money convertBigDecimalToMoneyLite(final BigDecimal value)
   {
      final Money money = new Money();
      money.setAmount(value);
      money.setCurrency(Currency.getInstance(currencyResolver.getSiteCurrency()));
      return money;
   }

   /**
    * This method adds duration with destinations arrival date.
    *
    * @param sourceAccom the package sailing info
    * @return the date
    */
   private Date populateAccomEndDate(final MultiCenterUnits sourceAccom)
   {
      return sourceAccom.getStartDate().plusDays(sourceAccom.getStay()).toDate();
   }

   /**
    * @param packageItemValue
    * @param stay
    */
   private void populateFromShipModel(final PackageItemValue packageItemValue, final Stay stay)
   {
      final AccommodationModel shipModel =
         accommodationService.getAccomodationByCodeAndCatalogVersion(packageItemValue.getPckShip()
            .getCode(), cmsSiteService.getCurrentCatalogVersion());
      stay.setType(StayType.valueOf(shipModel.getType().toString()));
      populateRatings(getFeatureValueMap(shipModel).get(T_RATING), stay);
   }

   /**
    * For populating the T rating of the accommodation.
    *
    * @param officialRating the source
    * @param target the target
    */
   private void populateRatings(final String officialRating, final Stay target)
   {
      final Rating ratingModel = new Rating();
      ratingModel.setValue(StringUtils.EMPTY);
      if (StringUtils.isNotEmpty(officialRating))
      {
         ratingModel.setValue(officialRating);
         target.setOfficialRating(ratingModel);
      }
   }

   /**
    * Gets the feature value map.
    *
    * @param shipModel the ship model
    * @return the feature value map
    */
   private Map<String, String> getFeatureValueMap(final AccommodationModel shipModel)
   {
      final List<String> featureCodes = new ArrayList<String>();
      featureCodes.add("holiday_type");
      featureCodes.add(T_RATING);
      return featureService.getFirstValueForFeaturesAsStrings(featureCodes, shipModel, new Date(),
         brandUtils.getFeatureServiceBrand(shipModel.getBrands()));
   }

   /**
    * To set the atcom details.
    *
    * @param inventoryDetails the package item value
    * @param target the target
    */
   private void populateInventoryDetails(final InventoryDetails inventoryDetails, final Stay target)
   {
      final SupplierProductDetails supplierDetails = new SupplierProductDetails();
      supplierDetails.setPromoCode(inventoryDetails.getProm());
      supplierDetails.setProductCode(inventoryDetails.getAtcomId());
      supplierDetails.setSellingCode(inventoryDetails.getSellingCode());
      supplierDetails.setSupplierNumber(null);
      target.setSupplierProductDetails(supplierDetails);
   }

}
