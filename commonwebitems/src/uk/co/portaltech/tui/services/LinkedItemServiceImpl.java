/**
 *
 */
package uk.co.portaltech.tui.services;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.enums.AccommodationType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.model.ItineraryLegItemModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.AccommodationService;
import uk.co.portaltech.travel.services.AttractionService;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.model.ItineraryLeg;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.ItineraryViewData;
import uk.co.portaltech.tui.web.view.data.MultiCentreData;
import uk.co.portaltech.tui.web.view.data.wrapper.TwoCentreSelectorViewData;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.async.logging.TUILogUtils;

/**
 * @author
 *
 */
public class LinkedItemServiceImpl<T> implements LinkedItemService
{
   private static final String HOTEL_OVERVIEW_STAY = "/hotel_overview-STAY";

   private static final String HTML = ".html";

   private static final String STAY = "STAY";

   private static final String CRUISE = "CRUISE";

   private static final String SAFARI = "SAFARI";

   private static final String TOUR = "TOUR";

   private static final String HYPHEN = "-";

   private static final String SPACE = " ";

   private static final String ATTACTION_ITEM_TYPE = "attraction";

   private static final String EXCURSION_ITEM_TYPE = "excursion";

   private static final Set<String> MULTICENTREHOLIDAYTYPES = new HashSet<String>();

   private static final int TWO = 2;

   @Resource
   private AccommodationService accommodationService;

   @Resource
   private CMSSiteService cmsSiteService;

   @Resource
   private TuiUtilityService tuiUtilityService;

   @Resource
   private Converter<ItemModel, ItineraryLeg> itineraryConverter;

   @Resource
   private Populator<ItemModel, ItineraryLeg> itineraryPopulator;

   @Resource
   private TUIUrlResolver<ProductModel> tuiProductUrlResolver;

   @Resource
   private FeatureService featureService;

   @Resource
   private AttractionService attractionService;

   @Resource
   private FlexibleSearchService flexibleSearchService;

   @Resource
   private BrandUtils brandUtils;

   private static final String ITINERARY_LEG_QUERY =
      "SELECT {iLeg.pk} FROM {ItineraryLeg AS iLeg} WHERE {iLeg.itineraryLegCode} = ?itineraryLegCode AND {catalogVersion} = ?catalogVersionPK";

   private static final String MEDIUM = "medium";

   private static final TUILogUtils LOG = new TUILogUtils("LinkedItemServiceImpl");

   static
   {
      MULTICENTREHOLIDAYTYPES.add("Twin_MultiCentre");
      MULTICENTREHOLIDAYTYPES.add("NileCruiseandStay");
      MULTICENTREHOLIDAYTYPES.add("SafariandStay");
      MULTICENTREHOLIDAYTYPES.add("TourandStay");
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.services.LinkedItemService#getLinkedItemsForType (java.lang.String,
    * java.lang.String)
    */
   @Override
   public List<AccommodationModel> getLinkedItemsOfAccommodationType(final String code)
   {

      final List<AccommodationModel> linkedItemsOfAccommodationType =
         new LinkedList<AccommodationModel>();

      final Set<? extends ItemModel> linkedItems = getLinkedItems(code);

      if (CollectionUtils.isNotEmpty(linkedItems))
      {
         for (final ItemModel itemModel : linkedItems)
         {
            if (itemModel instanceof AccommodationModel)
            {
               final AccommodationModel accommodationModel = (AccommodationModel) itemModel;

               if (StringUtils.equalsIgnoreCase(AccommodationType.SHIP.getCode(),
                  accommodationModel.getType().getCode()))
               {
                  linkedItemsOfAccommodationType.add(0, accommodationModel);
               }
               else if (StringUtils.equalsIgnoreCase(AccommodationType.SAFARI.getCode(),
                  accommodationModel.getType().getCode()))
               {
                  linkedItemsOfAccommodationType.add(0, accommodationModel);
               }
               else if (StringUtils.equalsIgnoreCase(AccommodationType.TOUR.getCode(),
                  accommodationModel.getType().getCode()))
               {
                  linkedItemsOfAccommodationType.add(0, accommodationModel);
               }
               else
               {
                  linkedItemsOfAccommodationType.add(accommodationModel);
               }

            }
         }

         return linkedItemsOfAccommodationType;
      }

      return Collections.emptyList();
   }

   @Override
   public List<AttractionModel> getLinkedItemsOfAttractionModel(final String multiCentrecode)
   {

      final List<AttractionModel> attractionModels = new LinkedList<AttractionModel>();

      final Set<? extends ItemModel> linkedItems = getLinkedItems(multiCentrecode);

      if (CollectionUtils.isNotEmpty(linkedItems))
      {
         for (final ItemModel itemModel : linkedItems)
         {
            if (itemModel instanceof AttractionModel)
            {
               attractionModels.add((AttractionModel) itemModel);
            }
         }

         return attractionModels;
      }

      return Collections.emptyList();

   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.services.LinkedItemService#getItinerary(java.lang .String)
    */
   @Override
   public ItineraryViewData getItinerary(final String code)
   {

      ItineraryViewData itinerary = new ItineraryViewData();
      Map<Integer, List<ItineraryLeg>> itineraryMap = null;
      if (StringUtils.isNotBlank(code))
      {
         final List<ItineraryLegItemModel> listItineraryLegModel = getItineraryLegs(code);

         if (CollectionUtils.isNotEmpty(listItineraryLegModel))
         {
            itinerary = new ItineraryViewData();
            itineraryMap = new TreeMap<Integer, List<ItineraryLeg>>();

            for (final ItineraryLegItemModel itineraryLegItemModel : listItineraryLegModel)
            {
               final ItemModel itemModel =
                  getItemModel(itineraryLegItemModel.getItineraryLegCode());

               if (itemModel != null)
               {
                  populateItinerary(itineraryMap, itineraryLegItemModel, itemModel);
               }
            }

            itinerary.setItinerary(itineraryMap);
         }

      }

      return itinerary;
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.services.LinkedItemService#getDetailedItinerary( java.lang.String)
    */
   @SuppressWarnings("boxing")
   @Override
   public ItineraryViewData getDetailedItinerary(final String multiCentreCode)
   {

      final ItineraryViewData itineraryViewData = getItinerary(multiCentreCode);
      ItineraryViewData subItinerary = null;
      final Set<String> subItineraryCodes = new HashSet<String>();

      if (StringUtils.isNotBlank(multiCentreCode))
      {
         final List<ItineraryLegItemModel> listItineraryLegModel =
            getItineraryLegs(multiCentreCode);
         final List<ItineraryLegItemModel> listOfSubItineraryLegModels =
            new LinkedList<ItineraryLegItemModel>();

         if (CollectionUtils.isNotEmpty(listItineraryLegModel))
         {
            for (final ItineraryLegItemModel itineraryLegModel : listItineraryLegModel)
            {
               final ItemModel itemModel = getItemModel(itineraryLegModel.getItineraryLegCode());

               if (itemModel != null && "Accommodation".equalsIgnoreCase(itemModel.getItemtype()))
               {
                  final List<ItineraryLegItemModel> itinerarySubList =
                     getItineraryLegs(itineraryLegModel.getItineraryLegCode());
                  if (CollectionUtils.isNotEmpty(itinerarySubList))
                  {
                     subItineraryCodes.add(itineraryLegModel.getItineraryLegCode());
                     listOfSubItineraryLegModels.addAll(itinerarySubList);
                  }
               }
            }
            if (CollectionUtils.isNotEmpty(listOfSubItineraryLegModels))
            {
               subItinerary = new ItineraryViewData();
               final Map<Integer, List<ItineraryLeg>> subItineraryMap =
                  new TreeMap<Integer, List<ItineraryLeg>>();

               for (final ItineraryLegItemModel itineraryLegModel : listOfSubItineraryLegModels)
               {
                  final ItemModel itemModel = getItemModel(itineraryLegModel.getItineraryLegCode());

                  if (itemModel != null)
                  {
                     populateItinerary(subItineraryMap, itineraryLegModel, itemModel);
                  }
               }
               subItinerary.setItinerary(subItineraryMap);
            }
         }
      }
      final Set<Integer> removeKeySet = new LinkedHashSet<Integer>();
      if (itineraryViewData != null && subItinerary != null
         && MapUtils.isNotEmpty(itineraryViewData.getItinerary())
         && MapUtils.isNotEmpty(subItinerary.getItinerary()))
      {
         final Map<Integer, List<ItineraryLeg>> subItineraryMap =
            new LinkedHashMap<Integer, List<ItineraryLeg>>();
         for (final Entry<Integer, List<ItineraryLeg>> mapEntry : itineraryViewData.getItinerary()
            .entrySet())
         {
            final int key = mapEntry.getKey();
            final List<ItineraryLeg> value = mapEntry.getValue();

            for (final ItineraryLeg leg : value)
            {
               if (subItineraryCodes.contains(leg.getCode()))
               {
                  removeKeySet.add(key);
                  for (final Entry<Integer, List<ItineraryLeg>> subMapEntry : subItinerary
                     .getItinerary().entrySet())
                  {
                     final int subKey = subMapEntry.getKey();
                     final List<ItineraryLeg> subValue = subMapEntry.getValue();
                     for (final ItineraryLeg subLegs : subValue)
                     {
                        final String subArrivalDayString =
                           StringUtils.substringBefore(subLegs.getArrivalDay(), " ");
                        final int subArrivalDay = Integer.parseInt(subArrivalDayString) + key - 1;

                        final String[] days = subLegs.getDays().split("-");
                        final String subDays =
                           days.length == 1 ? Integer.toString(Integer.parseInt(days[0]) + key - 1)
                              : (Integer.parseInt(days[0]) + key - 1) + "-"
                                 + (Integer.parseInt(days[1]) + key - 1);

                        // int subDays =

                        subLegs.setArrivalDay(Integer.toString(subArrivalDay));
                        subLegs.setDays(subDays);
                     }
                     subItineraryMap.put(key + subKey - 1, subValue);
                  }

               }
            }
         }
         for (final Integer key : removeKeySet)
         {
            itineraryViewData.getItinerary().remove(key);
         }

         final Set<Integer> intersectionKeySet =
            new LinkedHashSet<Integer>(itineraryViewData.getItinerary().keySet());
         intersectionKeySet.retainAll(subItineraryMap.keySet());

         final Map<Integer, List<ItineraryLeg>> finalItineraryMap =
            new LinkedHashMap<Integer, List<ItineraryLeg>>();
         final Map<Integer, List<ItineraryLeg>> intersectionItineraryMap =
            new LinkedHashMap<Integer, List<ItineraryLeg>>();
         for (final Integer key : intersectionKeySet)
         {
            intersectionItineraryMap.put(key + 1, itineraryViewData.getItinerary().get(key));
            itineraryViewData.getItinerary().remove(key);
         }
         finalItineraryMap.putAll(itineraryViewData.getItinerary());
         finalItineraryMap.putAll(subItineraryMap);
         finalItineraryMap.putAll(intersectionItineraryMap);
         itineraryViewData.setItinerary(finalItineraryMap);

      }

      return itineraryViewData;
   }

   @Override
   public ItineraryViewData getItineraryDataForAttrationandExcursion(final String code)
   {

      ItineraryViewData itinerary = new ItineraryViewData();
      Map<Integer, List<ItineraryLeg>> itineraryMap = Collections.emptyMap();
      if (StringUtils.isNotBlank(code))
      {
         final List<ItineraryLegItemModel> listItineraryLegModel = getItineraryLegs(code);

         if (CollectionUtils.isNotEmpty(listItineraryLegModel))
         {
            itinerary = new ItineraryViewData();
            itineraryMap = new TreeMap<Integer, List<ItineraryLeg>>();

            for (final ItineraryLegItemModel itineraryLegItemModel : listItineraryLegModel)
            {

               final ItemModel itemModel =
                  getItemModelForAttraction(itineraryLegItemModel.getItineraryLegCode());

               if (itemModel != null
                  && (itemModel.getItemtype().equalsIgnoreCase(ATTACTION_ITEM_TYPE) || itemModel
                     .getItemtype().equalsIgnoreCase(EXCURSION_ITEM_TYPE)))
               {

                  populateItinerary(itineraryMap, itineraryLegItemModel, itemModel);
               }
            }
         }

         itinerary.setItinerary(itineraryMap);
      }

      return itinerary;
   }

   @Override
   public ItineraryViewData getItineraryDataForLocation(final LocationModel locationModel,
      final String fdCode)
   {
      List<AttractionModel> attractionModels = null;
      ItineraryViewData itinerary = new ItineraryViewData();

      attractionModels =
         attractionService.getNonBookableAttractionsForLocationCodeAndFDCode(locationModel, fdCode);
      if (CollectionUtils.isNotEmpty(attractionModels))
      {
         itinerary = new ItineraryViewData();
         final Map<Integer, List<ItineraryLeg>> itineraryMap =
            new TreeMap<Integer, List<ItineraryLeg>>();
         for (final AttractionModel attractionModel : attractionModels)
         {
            ItineraryLegItemModel itineraryLegItemModel = null;
            itineraryLegItemModel = getItineraryLeg(attractionModel.getCode());
            if (itineraryLegItemModel != null)
            {
               populateItinerary(itineraryMap, itineraryLegItemModel, attractionModel);
            }
         }
         itinerary.setItinerary(itineraryMap);
      }
      return itinerary;
   }

   /**
    * @param code
    * @return
    */
   private ItineraryLegItemModel getItineraryLeg(final String itineraryLegCode)
   {
      ItineraryLegItemModel itineraryLeg = null;
      final FlexibleSearchQuery query = new FlexibleSearchQuery(ITINERARY_LEG_QUERY);
      query.addQueryParameter("itineraryLegCode", itineraryLegCode);
      query.addQueryParameter("catalogVersionPK", cmsSiteService.getCurrentCatalogVersion());
      try
      {
         itineraryLeg = flexibleSearchService.<ItineraryLegItemModel> searchUnique(query);
      }
      catch (final AmbiguousIdentifierException e)
      {
         LOG.error("More than one Accommodafinal or code '" + itineraryLegCode + "'.", e);
         return null;
      }
      catch (final ModelNotFoundException e)
      {
         LOG.error("Could not find Accommodation for code '" + itineraryLegCode + "'.", e);
         return null;
      }

      return itineraryLeg;
   }

   @Override
   public TwoCentreSelectorViewData getTwoCentreSelectorData(final String code)
   {

      final AccommodationModel multiCentreModel =
         accommodationService.getAccomodationByCodeAndCatalogVersion(code,
            cmsSiteService.getCurrentCatalogVersion());
      TwoCentreSelectorViewData viewData = null;
      final List<ItineraryLeg> itineraryLegs = new LinkedList<ItineraryLeg>();
      populateItinerayForTwoSelector(code, itineraryLegs, multiCentreModel);
      if (CollectionUtils.isNotEmpty(itineraryLegs))
      {
         viewData = new TwoCentreSelectorViewData();
         viewData.setItineraryLegs(itineraryLegs);
      }
      return viewData;
   }

   @Override
   public List<MultiCentreData> getMultiCentreData(final String code)
   {
      MultiCentreData multiCentreData = null;
      final List<MultiCentreData> multiCentreDatas = new LinkedList<MultiCentreData>();
      final Map<String, String> durationsMap = getDurationForLinkedItems(code);

      if (MapUtils.isNotEmpty(durationsMap))
      {
         final List<AccommodationModel> accommodationModels =
            getLinkedItemsOfAccommodationType(code);
         if (CollectionUtils.isNotEmpty(accommodationModels))
         {
            for (final AccommodationModel accommodationModel : accommodationModels)
            {
               multiCentreData = new MultiCentreData();
               final Map locations = new LinkedHashMap<String, String>();
               populateBasicMultiCentreData(multiCentreData, durationsMap, accommodationModel);
               populateTRatingData(accommodationModel, multiCentreData);
               populateLocationData(accommodationModel, locations);
               multiCentreData.setLocations(locations);
               multiCentreData.setType(accommodationModel.getType().getCode());
               multiCentreDatas.add(multiCentreData);
            }
         }
      }

      return multiCentreDatas;
   }

   /**
    * @param accommodationModel
    * @param multiCentreData
    */
   private void populateTRatingData(final AccommodationModel accommodationModel,
      final MultiCentreData multiCentreData)
   {
      final List<Object> featureValue =
         featureService.getFeatureValues("tRating", accommodationModel, new Date(),
            tuiUtilityService.getSiteBrand());
      if (CollectionUtils.isNotEmpty(featureValue))
      {
         multiCentreData.settRating(featureValue.get(0).toString());
      }
   }

   /**
    * @param locations
    * @param accommodationModel
    *
    */
   private void populateLocationData(final AccommodationModel sourceModel, final Map locations)
   {
      Collection<CategoryModel> productCategories = sourceModel.getSupercategories();
      while (CollectionUtils.isNotEmpty(productCategories) && locations.size() < TWO)
      {
         for (final CategoryModel category : productCategories)
         {
            if (category instanceof LocationModel && locations.size() < TWO)
            {
               final LocationModel locModel = (LocationModel) category;
               locations.put(locModel.getType().getCode(), locModel.getName());
               productCategories = locModel.getSupercategories();
               break;
            }
         }
      }
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.services.LinkedItemService#
    * getAccommodationCodeOfTypeShip(java.lang.String)
    */
   @Override
   public AccommodationModel getAccommodationOfTypeShip(final String multiCentreCode)
   {

      final List<AccommodationModel> accommodationModels =
         getLinkedItemsOfAccommodationType(multiCentreCode);
      if (CollectionUtils.isNotEmpty(accommodationModels))
      {
         for (final AccommodationModel accommodationModel : accommodationModels)
         {
            if (StringUtils.equalsIgnoreCase(AccommodationType.SHIP.getCode(), accommodationModel
               .getType().getCode()))
            {
               return accommodationModel;
            }
         }
         return accommodationModels.get(0);
      }

      return null;

   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.services.LinkedItemService#isMultiCentre(java.lang .String)
    */
   @Override
   public boolean isMultiCentre(final AccommodationModel accommodationModel)
   {

      final List<Object> featureValue =
         featureService.getFeatureValues("holiday_type", accommodationModel, new Date(),
            brandUtils.getFeatureServiceBrand(accommodationModel.getBrands()));
      String holidayType = StringUtils.EMPTY;
      if (CollectionUtils.isNotEmpty(featureValue))
      {
         holidayType = featureValue.get(0).toString();
         if (MULTICENTREHOLIDAYTYPES.contains(holidayType))
         {
            return true;
         }
      }

      return false;
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.portaltech.tui.services.LinkedItemService#getLinkedAccomCodeByTab
    * (java.lang.String, java.lang.String)
    */
   @Override
   public String getLinkedAccomCodeByTab(final String multiCentreCode, final String tab)
   {

      final Integer tabKey = Integer.valueOf(tab);

      final Map<Integer, AccommodationModel> linkedAccomMap =
         new LinkedHashMap<Integer, AccommodationModel>();

      final List<AccommodationModel> linkedAccoms =
         getLinkedItemsOfAccommodationType(multiCentreCode);

      if (CollectionUtils.isNotEmpty(linkedAccoms))
      {
         int i = 0;
         for (final AccommodationModel linkedAccom : linkedAccoms)
         {
            linkedAccomMap.put(Integer.valueOf(++i), linkedAccom);
         }
      }

      if (linkedAccomMap.containsKey(tabKey))
      {
         return linkedAccomMap.get(tabKey).getCode();
      }

      return StringUtils.EMPTY;
   }

   private Set<? extends ItemModel> getLinkedItems(final String code)
   {

      final Set<ItemModel> linkedItems = new LinkedHashSet<ItemModel>();

      final List<ItineraryLegItemModel> itineraryLegModels = getItineraryLegs(code);

      if (CollectionUtils.isNotEmpty(itineraryLegModels))
      {
         for (final ItineraryLegItemModel itineraryLeg : itineraryLegModels)
         {
            linkedItems.add(getItemModel(itineraryLeg.getItineraryLegCode()));
         }
         return linkedItems;
      }

      return Collections.emptySet();
   }

   private List<ItineraryLegItemModel> getItineraryLegs(final String code)
   {
      final AccommodationModel accommodationModel =
         accommodationService.getAccomodationByCodeAndCatalogVersion(code,
            cmsSiteService.getCurrentCatalogVersion(), null);
      if (accommodationModel != null && accommodationModel.getItinerary() != null)
      {
         final List<ItineraryLegItemModel> itinearyLegItemList =
            accommodationModel.getItinerary().getItineraryLegs();

         final List<ItineraryLegItemModel> sortedList =
            new ArrayList<ItineraryLegItemModel>(itinearyLegItemList);

         Collections.sort(sortedList, new Comparator<ItineraryLegItemModel>()
         {
            @Override
            public int compare(final ItineraryLegItemModel itineraryLeg1,
               final ItineraryLegItemModel itineraryLeg2)
            {
               final Integer seq1 = Integer.valueOf(itineraryLeg1.getSeq());
               final Integer seq2 = Integer.valueOf(itineraryLeg2.getSeq());
               return seq1.compareTo(seq2);
            }
         });
         return sortedList;
      }
      return Collections.emptyList();
   }

   /**
    * @param code
    */
   private ItemModel getItemModel(final String code)
   {
      return accommodationService.getAccommodationAttractionExcursionbyCode(code,
         cmsSiteService.getCurrentCatalogVersion());
   }

   /**
    * @param code
    */
   private ItemModel getItemModelForAttraction(final String code)
   {
      return attractionService
         .getAttractionForCode(code, cmsSiteService.getCurrentCatalogVersion());
   }

   // below are population logic, we need to move it to a populator

   /**
    * @param itineraryMap
    * @param ItineraryLegItemModel
    * @param itemModel
    */
   @SuppressWarnings("boxing")
   private void populateItinerary(final Map<Integer, List<ItineraryLeg>> itineraryMap,
      final ItineraryLegItemModel itineraryLegItemModel, final ItemModel itemModel)
   {
      ItineraryLeg itineraryLeg;

      itineraryLeg = itineraryConverter.convert(itemModel);
      populateItineraryLeg(itineraryLegItemModel, itemModel, itineraryLeg);

      final String arrivalDayInString = itineraryLegItemModel.getArrivalDay();

      if (StringUtils.isNotBlank(arrivalDayInString))
      {
         final int arrivalDay = Integer.parseInt(itineraryLegItemModel.getArrivalDay());
         if (itineraryMap.containsKey(arrivalDay))
         {
            if (itineraryLeg.isNonBookable())
            {
               itineraryMap.get(arrivalDay).add(0, itineraryLeg);
            }
            else
            {
               itineraryMap.get(arrivalDay).add(itineraryLeg);
            }
         }
         else
         {
            final List<ItineraryLeg> itinerayLegs = new LinkedList<ItineraryLeg>();
            if (itineraryLeg.isNonBookable())
            {
               itinerayLegs.add(0, itineraryLeg);
            }
            else
            {
               itinerayLegs.add(itineraryLeg);
            }
            itineraryMap.put(arrivalDay, itinerayLegs);
         }
      }

   }

   /**
    * @param durationsMap
    * @param itineraryLeg
    * @param duration
    */
   private void populateDuration(final Map<String, String> durationsMap,
      final ItineraryLegItemModel itineraryLeg, final String duration)
   {
      final String itemCode = itineraryLeg.getItineraryLegCode();
      if (durationsMap.containsKey(itemCode))
      {
         durationsMap.put(
            itemCode,
            Integer.toString(Integer.parseInt(durationsMap.get(itemCode))
               + Integer.parseInt(StringUtils.substringBefore(duration, SPACE))));
      }
      else
      {
         durationsMap.put(itemCode, StringUtils.substringBefore(duration, SPACE));
      }
   }

   /**
    * @param code
    * @param itineraryLegs
    */
   private void populateItinerayForTwoSelector(final String code,
      final List<ItineraryLeg> itineraryLegs, final AccommodationModel multiCentreModel)
   {
      ItineraryLeg itineraryLeg = null;
      final Map<String, String> durationsMap = getDurationForLinkedItems(code);

      if (MapUtils.isNotEmpty(durationsMap))
      {
         final List<AccommodationModel> accommodationModels =
            getLinkedItemsOfAccommodationType(code);
         if (CollectionUtils.isNotEmpty(accommodationModels))
         {
            int tabCount = 0;
            for (final AccommodationModel accommodationModel : accommodationModels)
            {
               tabCount++;
               itineraryLeg = new ItineraryLeg();
               populateBasicItineraryLeg(itineraryLeg, durationsMap, accommodationModel,
                  multiCentreModel);
               populateShipFirst((List<T>) itineraryLegs, itineraryLeg, accommodationModel
                  .getType().getCode(), Integer.valueOf(tabCount));
            }
         }
      }

   }

   private Map<String, String> getDurationForLinkedItems(final String code)
   {
      // key of the map is itinerary item code and value is duration
      final List<ItineraryLegItemModel> itineraryLegs = getItineraryLegs(code);
      final Map<String, String> durationsMap = new HashMap<String, String>();
      if (CollectionUtils.isNotEmpty(itineraryLegs))
      {
         for (final ItineraryLegItemModel itineraryLeg : itineraryLegs)
         {
            final String duration = itineraryLeg.getDuration();
            if (StringUtils.isNotBlank(duration))
            {
               populateDuration(durationsMap, itineraryLeg, duration);
            }
         }
      }

      return durationsMap;
   }

   /**
    * @param itineraryLegs
    * @param itineraryLeg
    * @param code
    */
   private void populateShipFirst(final List<T> itineraryLegs, final ItineraryLeg itineraryLeg,
      final String code, final Integer tabCount)
   {
      if (StringUtils.equalsIgnoreCase(AccommodationType.SHIP.getCode(), code))
      {
         itineraryLeg.setLegType(CRUISE);
         itineraryLegs.add(0, (T) itineraryLeg);
      }
      else if (StringUtils.equalsIgnoreCase(AccommodationType.SAFARI.getCode(), code))
      {
         itineraryLeg.setLegType(SAFARI);
         itineraryLegs.add(0, (T) itineraryLeg);
      }
      else if (StringUtils.equalsIgnoreCase(AccommodationType.TOUR.getCode(), code))
      {
         itineraryLeg.setLegType(TOUR);
         itineraryLegs.add(0, (T) itineraryLeg);
      }
      else
      {
         itineraryLeg.setLegType(STAY);
         itineraryLeg.setUrl(itineraryLeg.getUrl().concat(tabCount.toString()));
         itineraryLegs.add((T) itineraryLeg);
      }
   }

   /**
    * @param itineraryLeg
    * @param durationsMap
    * @param accommodationModel
    */
   private void populateBasicItineraryLeg(final ItineraryLeg itineraryLeg,
      final Map<String, String> durationsMap, final AccommodationModel accommodationModel,
      final AccommodationModel multiCentreModel)
   {
      itineraryLeg.setCode(accommodationModel.getCode());
      itineraryLeg.setName(accommodationModel.getName());
      final String url = tuiProductUrlResolver.resolve(multiCentreModel);
      if (StringUtils.equalsIgnoreCase(accommodationModel.getType().getCode(),
         AccommodationType.SHIP.getCode()))
      {
         itineraryLeg.setUrl(url);
      }
      else if (StringUtils.equalsIgnoreCase(accommodationModel.getType().getCode(),
         AccommodationType.TOUR.getCode()))
      {
         itineraryLeg.setUrl(url);
      }
      else
      {
         itineraryLeg.setUrl(StringUtils.replaceOnce(url, HTML, HOTEL_OVERVIEW_STAY));
      }

      itineraryLeg.setDuration(durationsMap.get(accommodationModel.getCode()));
   }

   /**
    * @param multiCentreData
    * @param durationsMap
    * @param accommodationModel
    */
   private void populateBasicMultiCentreData(final MultiCentreData multiCentreData,
      final Map<String, String> durationsMap, final AccommodationModel accommodationModel)
   {
      multiCentreData.setName(accommodationModel.getName());
      multiCentreData.setDuration(durationsMap.get(accommodationModel.getCode()));
      if (accommodationModel.getGalleryImages() != null)
      {
         for (final MediaContainerModel mediaContainerModel : accommodationModel.getGalleryImages())
         {
            if (mediaContainerModel != null)
            {
               for (final MediaModel media : mediaContainerModel.getMedias())
               {
                  if (media.getMediaFormat().getName() != null
                     && MEDIUM.equalsIgnoreCase(media.getMediaFormat().getName()))
                  {
                     multiCentreData.setImageUrl(media.getURL());
                     break;
                  }
               }
            }
            break;
         }
      }
   }

   /**
    * @param ItineraryLegItemModel
    * @param itemModel
    * @param itineraryLeg
    */
   private void populateItineraryLeg(final ItineraryLegItemModel itineraryLegItemModel,
      final ItemModel itemModel, final ItineraryLeg itineraryLeg)
   {

      itineraryPopulator.populate(itemModel, itineraryLeg);
      itineraryLeg.setCode(itineraryLegItemModel.getItineraryLegCode());
      itineraryLeg.setArrivalDay(itineraryLegItemModel.getArrivalDay());
      itineraryLeg.setDuration(itineraryLegItemModel.getDuration());
      itineraryLeg.setSeq(itineraryLegItemModel.getSeq());
      itineraryLeg.setArrivalTime(itineraryLegItemModel.getArrivalTime());
      final String duration =
         StringUtils.substringBefore(itineraryLegItemModel.getDuration(), SPACE);
      if (StringUtils.isNotBlank(duration) && Integer.parseInt(duration) > 1)
      {
         final int toDay =
            Integer.parseInt(itineraryLegItemModel.getArrivalDay()) + Integer.parseInt(duration)
               - 1;
         itineraryLeg.setDays(itineraryLegItemModel.getArrivalDay().concat(HYPHEN)
            .concat(Integer.toString(toDay)));
      }
      else
      {
         itineraryLeg.setDays(itineraryLegItemModel.getArrivalDay());
      }
   }

}
