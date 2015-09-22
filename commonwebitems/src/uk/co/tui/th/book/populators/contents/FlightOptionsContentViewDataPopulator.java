/**
 *
 */
package uk.co.tui.th.book.populators.contents;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.portaltech.travel.services.GenericContentService;
import uk.co.portaltech.tui.services.DurationHaulTypeService;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.constants.SessionObjectKeys;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.domain.lite.ExtraFacility;
import uk.co.tui.book.domain.lite.Itinerary;
import uk.co.tui.book.domain.lite.Leg;
import uk.co.tui.book.services.PackageComponentService;
import uk.co.tui.book.utils.PackageUtilityService;
import uk.co.tui.domain.model.DynamicContentConfigModel;
import uk.co.tui.th.book.store.FlightExtraFacilityStore;
import uk.co.tui.th.book.view.data.ContentViewData;

/**
 * @author amaresh.d
 *
 */
public class FlightOptionsContentViewDataPopulator implements Populator<Object, ContentViewData>
{

   /** The generic content service. */
   @Resource
   private GenericContentService genericContentService;

   /** The package cart service. */
   @Resource
   private PackageCartService packageCartService;

   /** The session service. */
   @Resource
   private SessionService sessionService;

   /** The duration hual service. */
   @Resource
   private DurationHaulTypeService durationHaulTypeService;

   /** The package component service. */
   @Resource
   private PackageComponentService packageComponentService;

   /**
    * Populates the flight extra content
    */
   @Override
   public void populate(final Object source, final ContentViewData target)
   {
      // REVISIT:Changed to BasePackage
      final BasePackage packageModel = packageCartService.getBasePackage();
      final FlightExtraFacilityStore flightExtraStore =
         sessionService.getAttribute(SessionObjectKeys.FLIGHT_EXTRA_FACILITY_STORE);
      String flightMealCode = StringUtils.EMPTY;
      if (flightExtraStore != null)
      {
         String inventoryCode = StringUtils.EMPTY;
         final Map<String, List<ExtraFacility>> validExtraFacilitiesMap =
            flightExtraStore.getExtraFacilityFromAllLegsBasedOnCabinClass(packageModel.getId(),
               PackageUtilityService.getCabinClass(packageModel));
         for (final List<ExtraFacility> eachEntry : validExtraFacilitiesMap.values())
         {
            inventoryCode = eachEntry.get(0).getExtraFacilityCategory().getInventoryCode();
            final String extraCode = eachEntry.get(0).getExtraFacilityCode();
            final String corporateCode = eachEntry.get(0).getCorporateCode();
            final List<DynamicContentConfigModel> dynamicContents =
               genericContentService.getDynamicContentConfig(inventoryCode, extraCode,
                  corporateCode);

            getDynamicContents(target, dynamicContents);
         }
         if (StringUtils.equalsIgnoreCase(inventoryCode, "FM"))
         {
            flightMealCode = inventoryCode;
         }

         final Leg leg = getFlightLeg(packageModel);
         populateContentForShortHaul(target, leg);
         populateMealContent(flightMealCode, target);
      }
   }

   /**
    * @param target
    * @param dynamicContents
    */
   private void getDynamicContents(final ContentViewData target,
      final List<DynamicContentConfigModel> dynamicContents)
   {
      for (final DynamicContentConfigModel dynamicContent : dynamicContents)
      {
         target.getContentMap().putAll(
            genericContentService.getGenericDynamicContentValue(dynamicContent));
      }
   }

   /**
    * @param packageModel
    * @return leg
    */
   private Leg getFlightLeg(final BasePackage packageModel)
   {
      Leg leg = null;
      final Itinerary flightItir = packageComponentService.getFlightItinerary(packageModel);

      if (CollectionUtils.isNotEmpty(flightItir.getInBound()))
      {
         leg = flightItir.getInBound().get(0);
      }
      else
      {
         leg = flightItir.getOutBound().get(0);
      }
      return leg;
   }

   /**
    * Populate meal content.
    *
    * @param target the target
    */
   private void populateMealContent(final String flightMealCode, final ContentViewData target)
   {
      if (StringUtils.isEmpty(flightMealCode))
      {
         final List<DynamicContentConfigModel> dynamicContents =
            genericContentService.getDynamicContentConfig("FM", StringUtils.EMPTY,
               StringUtils.EMPTY);
         getDynamicContents(target, dynamicContents);
      }
   }

   /**
    * Populate content for short haul.
    *
    * @param target the target
    * @param leg the leg
    */
   private void populateContentForShortHaul(final ContentViewData target, final Leg leg)
   {
      if (StringUtil.isNotEquals(durationHaulTypeService.findHaultypeForAirports(leg
         .getArrivalAirport().getCode(), leg.getDepartureAirport().getCode()), "LH"))
      {
         final List<DynamicContentConfigModel> dynamicContents =
            genericContentService.getDynamicContentConfig("FM", StringUtils.EMPTY,
               StringUtils.EMPTY);
         getDynamicContents(target, dynamicContents);
      }
   }
}
