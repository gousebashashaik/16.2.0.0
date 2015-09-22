/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.HtmlUtils;

import uk.co.portaltech.travel.model.FacilityTypeModel;
import uk.co.portaltech.travel.model.results.MerchandisedResult;
import uk.co.portaltech.travel.services.destination.MainStreamTravelLocationService;
import uk.co.portaltech.travel.services.facility.FacilityTypeService;
import uk.co.portaltech.travel.thirdparty.endeca.FacetOption;
import uk.co.portaltech.travel.thirdparty.endeca.FacetValue;
import uk.co.portaltech.tui.constants.FilterConstants;
import uk.co.portaltech.tui.web.view.data.CommonData;
import uk.co.portaltech.tui.web.view.data.CommonFilterData;
import uk.co.portaltech.tui.web.view.data.DestinationMainData;
import uk.co.portaltech.tui.web.view.data.InventoryFilterResponse;
import uk.co.portaltech.tui.web.view.data.MerchandiserViewData;
import uk.co.portaltech.tui.web.view.data.SliderData;
import uk.co.tui.async.logging.TUILogUtils;

/**
 * @author EXTCS5
 *
 */
@SuppressWarnings("boxing")
public class InvGroupsFilterSelectorPopulator implements
   Populator<MerchandisedResult, MerchandiserViewData>
{

   private static final String REG_EXP = "\\|";

   private static final String DO = "DO_";

   private static final String DH = "DH_";

   private static final TUILogUtils LOG = new TUILogUtils("InvGroupsFilterSelectorPopulator");

   @Resource
   private FacilityTypeService facilityType;

   @Resource
   private CMSSiteService cmsSiteService;

   @Resource(name = "mainStreamTravelLocationService")
   private MainStreamTravelLocationService mstravelLocationService;

   @Resource
   private ConfigurationService configurationService;

   public void setData(final MerchandiserViewData target) throws ConversionException
   {

      final Map<String, Boolean> visibility = new HashMap<String, Boolean>();
      visibility.put(FilterConstants.HOLIDAYTYPEFILTER, Boolean.FALSE);
      visibility.put(FilterConstants.TRIPADVISORRATINGFILTER, Boolean.FALSE);
      visibility.put(FilterConstants.FCRATINGFILTER, Boolean.FALSE);
      visibility.put(FilterConstants.BESTFORFILTER, Boolean.FALSE);
      visibility.put(FilterConstants.FEATURESFILTER, Boolean.FALSE);
      visibility.put(FilterConstants.DESTOPTIONSFILTER, Boolean.FALSE);
      visibility.put(FilterConstants.HOLIDAYTYPEFILTER, Boolean.FALSE);

      final InventoryFilterResponse filterPanel = new InventoryFilterResponse();
      filterPanel.setFilterVisibility(visibility);
      target.setFilterPanel(filterPanel);

      final List<CommonData> featureFilters = new ArrayList<CommonData>();

      final CommonFilterData featureData = new CommonFilterData();
      featureData.setName(FilterConstants.FEATURES);
      featureData.setType(FilterConstants.CHECKBOX);
      featureData.setId(FilterConstants.FEATURES_ID);
      featureData.setFilterType("AND");
      featureData.setValues(featureFilters);
      filterPanel.setFeatures(featureData);

   }

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final MerchandisedResult source, final MerchandiserViewData target)
      throws ConversionException
   {
      setData(target);

      for (final FacetOption facet : source.getFacets())
      {
         if (facetValueNotEmpty(FilterConstants.TARATING_CODE, facet))
         {
            populateTripAdvisorRating(target.getFilterPanel(), facet, target.getFilterPanel()
               .getFilterVisibility());
         }
         else if (facetValueNotEmpty(FilterConstants.FCRATING_CODE, facet))
         {
            populateFCRating(target.getFilterPanel(), facet, target.getFilterPanel()
               .getFilterVisibility());

         }
         else if (facetValueNotEmpty(FilterConstants.DESTINATION_CODE, facet))
         {
            populateDestinationOption(target.getFilterPanel(), facet, target.getFilterPanel()
               .getFilterVisibility());

         }
         else if (facetValueNotEmpty(FilterConstants.BESTFOR_CODE, facet))
         {
            populateBestFor(target.getFilterPanel(), facet, target.getFilterPanel()
               .getFilterVisibility());
         }
         else if (facetValueNotEmpty(FilterConstants.COLLECTION_CODE, facet))
         {
            populateCollections(target.getFilterPanel(), facet, target.getFilterPanel()
               .getFilterVisibility());
         }
         else if (featureMatches(facet) && CollectionUtils.isNotEmpty(facet.getFacetValues()))
         {
            populateFeatures(target.getFilterPanel().getFeatures().getValues(), facet, target
               .getFilterPanel().getFilterVisibility());

         }
      }

   }

   private void populateTripAdvisorRating(final InventoryFilterResponse filterPanel,
      final FacetOption facet, final Map<String, Boolean> visibility)
   {

      final SliderData minimumTripAdvisor = new SliderData();

      minimumTripAdvisor.setId(FilterConstants.TARATINGID);
      minimumTripAdvisor.setName(FilterConstants.TARATINGNAME);
      minimumTripAdvisor.setType(FilterConstants.SLIDER);
      minimumTripAdvisor.setTrackType(FilterConstants.TRACKTYPE);
      minimumTripAdvisor.setCode(facet.getFacetCode());
      final List<Double> values = new ArrayList<Double>();
      final List<Double> limit = new ArrayList<Double>();

      Double min = null;
      Double max = null;
      min = Math.floor(Double.parseDouble(facet.getFacetValues().get(0).getMin()));
      max = Math.floor(Double.parseDouble(facet.getFacetValues().get(0).getMax()));
      // For single value slider only minimum value is required
      values.add(min);

      if (!min.equals(max))
      {
         limit.add(min);
         limit.add(max);
         minimumTripAdvisor.setLimit(limit);
         minimumTripAdvisor.setValues(values);
         minimumTripAdvisor.setSteps(FilterConstants.RATINGSTEPS);
         final List<Long> range1 = new ArrayList<Long>();
         range1.add(Long.valueOf(FilterConstants.MINRANGE));
         range1.add(Long.valueOf(FilterConstants.MAXRANGE));
         minimumTripAdvisor.setRange(range1);

         filterPanel.setTripAdivsorRating(minimumTripAdvisor);

         if (CollectionUtils.isNotEmpty(minimumTripAdvisor.getValues())
            && CollectionUtils.isNotEmpty(minimumTripAdvisor.getLimit()))
         {
            visibility.put(FilterConstants.TRIPADVISORRATINGFILTER, Boolean.TRUE);
         }
      }
      else
      {
         visibility.put(FilterConstants.TRIPADVISORRATINGFILTER, Boolean.FALSE);
      }

   }

   private void populateFCRating(final InventoryFilterResponse filterPanel,
      final FacetOption facet, final Map<String, Boolean> visibility)
   {

      final SliderData minimumFc = new SliderData();

      minimumFc.setId(FilterConstants.FCRATINGID);
      minimumFc.setName(FilterConstants.FCRATINGNAME);
      minimumFc.setType(FilterConstants.SLIDER);
      minimumFc.setTrackType(FilterConstants.TRACKTYPE);
      minimumFc.setCode(facet.getFacetCode());
      final List<Double> values = new ArrayList<Double>();
      final List<Double> limit = new ArrayList<Double>();
      Double min = null;
      Double max = null;

      min = Math.floor(Double.parseDouble(facet.getFacetValues().get(0).getMin()));
      max = Math.floor(Double.parseDouble(facet.getFacetValues().get(0).getMax()));

      // For single value slider only minimum value is required
      if (!min.equals(max))
      {
         values.add(min);
         limit.add(min);
         limit.add(max);
         minimumFc.setLimit(limit);
         minimumFc.setValues(values);
         minimumFc.setSteps(FilterConstants.RATINGSTEPS);

         final List<Long> range1 = new ArrayList<Long>();
         range1.add(Long.valueOf(FilterConstants.MINRANGE));
         range1.add(Long.valueOf(FilterConstants.MAXRANGE));
         minimumFc.setRange(range1);
         filterPanel.setFcRating(minimumFc);

         if (CollectionUtils.isNotEmpty(minimumFc.getValues())
            && CollectionUtils.isNotEmpty(minimumFc.getLimit()))
         {
            visibility.put(FilterConstants.FCRATINGFILTER, Boolean.TRUE);
         }
      }
      else
      {
         // this is set to false because data is not there
         visibility.put(FilterConstants.FCRATINGFILTER, Boolean.FALSE);
      }

   }

   private void populateBestFor(final InventoryFilterResponse filterPanel, final FacetOption facet,
      final Map<String, Boolean> visibility)
   {
      final List<CommonData> bestForFilters = new ArrayList<CommonData>();
      final CommonFilterData bestFor = new CommonFilterData();
      bestFor.setName(FilterConstants.BESTFOR_NAME);
      bestFor.setType(FilterConstants.CHECKBOX);
      bestFor.setId(FilterConstants.BESTFOR_ID);
      bestFor.setFilterType("AND");
      bestFor.setValues(bestForFilters);

      final Map bestForMap = getBestForMap();
      CommonData best = null;
      for (final FacetValue facetValue : facet.getFacetValues())
      {
         best = new CommonData();
         best.setId("BF_" + facetValue.getCode());
         best.setValue(facetValue.getCode());
         if (null != bestForMap.get(facetValue.getCode()))
         {
            best.setName(bestForMap.get(facetValue.getCode()).toString());
            best.setNoAccommodations(Integer.parseInt(facetValue.getCount()));
            bestForFilters.add(best);
         }
         else
         {
            LOG.error("Best for  is not present with code " + facetValue.getCode());
         }

      }

      filterPanel.setBestFor(bestFor);

      if (CollectionUtils.isNotEmpty(bestFor.getValues()))
      {
         visibility.put(FilterConstants.BESTFORFILTER, Boolean.TRUE);
      }
   }

   private void populateCollections(final InventoryFilterResponse filterPanel,
      final FacetOption facet, final Map<String, Boolean> visibility)
   {
      final List<CommonData> holidayFilters = new ArrayList<CommonData>();

      final CommonFilterData holidaytype = new CommonFilterData();
      holidaytype.setName(FilterConstants.HOLIDAYTYPENAME);
      holidaytype.setId(FilterConstants.HOLIDAYTYPEID);
      holidaytype.setValues(holidayFilters);
      holidaytype.setType(FilterConstants.CHECKBOX);
      holidaytype.setValues(holidayFilters);
      holidaytype.setFilterType("OR");

      CommonData filterValue = null;
      for (final FacetValue facetValue : facet.getFacetValues())
      {
         filterValue = new CommonData();
         filterValue.setName(HtmlUtils.htmlEscape(facetValue.getLabel()));
         filterValue.setId("HT_" + facetValue.getCode());
         filterValue.setValue(facetValue.getCode());
         filterValue.setNoAccommodations(Integer.parseInt(facetValue.getCount()));
         holidayFilters.add(filterValue);
      }

      filterPanel.setCollection(holidaytype);

      if (CollectionUtils.isNotEmpty(holidaytype.getValues()))
      {
         visibility.put(FilterConstants.HOLIDAYTYPEFILTER, Boolean.TRUE);
      }
   }

   /**
    * Get all features
    *
    * @param featureFilters
    * @param facet
    */
   private void populateFeatures(final List featureFilters, final FacetOption facet,
      final Map<String, Boolean> visibility)
   {

      CommonData feature = null;
      FacilityTypeModel facilityTypeModel = null;
      for (final FacetValue facetValue : facet.getFacetValues())
      {
         facilityTypeModel =
            facilityType.getFacilityTypeForCode(cmsSiteService.getCurrentCatalogVersion(),
               facetValue.getCode());
         feature = new CommonData();
         feature.setId("FT_" + facetValue.getCode());
         feature.setValue(facetValue.getCode());
         feature.setName(facilityTypeModel != null ? facilityTypeModel.getName() : facetValue
            .getCode());
         feature.setNoAccommodations(Integer.parseInt(facetValue.getCount()));
         feature.setCategoryCode(facet.getFacetCode());
         if (facilityTypeModel != null)
         {
            featureFilters.add(feature);
         }

      }

      if (CollectionUtils.isNotEmpty(featureFilters))
      {
         visibility.put(FilterConstants.FEATURESFILTER, Boolean.TRUE);
      }
   }

   /**
    * Get destination options
    *
    * @param filterPanel
    * @param facet
    */
   private void populateDestinationOption(final InventoryFilterResponse filterPanel,
      final FacetOption facet, final Map<String, Boolean> visibility)
   {
      final List<CommonFilterData> commonFilterList = new ArrayList<CommonFilterData>();

      final CommonFilterData commonFilterData = new CommonFilterData();
      commonFilterData.setName(FilterConstants.DESTINATIONS);
      commonFilterData.setId(FilterConstants.DESTINATION_ID);
      commonFilterData.setType(FilterConstants.CHECKBOX);
      commonFilterData.setFilterType("OR");
      commonFilterList.add(commonFilterData);
      final List<DestinationMainData> destMainList = createDestinationList(facet);
      commonFilterData.setValues(destMainList);

      filterPanel.setDestination(commonFilterData);

      if (CollectionUtils.isNotEmpty(commonFilterData.getValues()))
      {
         visibility.put(FilterConstants.DESTOPTIONSFILTER, Boolean.TRUE);
      }
   }

   /**
    * Creating destination list in hierarchical structure
    *
    * @param facet
    * @return destMainList
    */
   private List<DestinationMainData> createDestinationList(final FacetOption facet)
   {
      // Creating hierarchical destination option structure
      final Map<DestinationMainData, List<DestinationMainData>> destinationMap =
         new HashMap<DestinationMainData, List<DestinationMainData>>();
      DestinationMainData destination = null;
      DestinationMainData parent = null;
      String code = null;
      // incrementior i used to generate unique ids for UI
      int i = 0;
      if (CollectionUtils.isNotEmpty(facet.getFacetValues()))
      {
         for (final FacetValue facetValue : facet.getFacetValues())
         {
            parent = new DestinationMainData();
            parent.setId((facetValue.getParentCode() != null) ? DO + facetValue.getParentCode()
               : DO);
            parent.setValue(facetValue.getParentCode());

            parent.setName((facetValue.getParentCode() != null) ? mstravelLocationService
               .getDestinationName(facetValue.getParentCode()) : "test");

            if (destinationMap.containsKey(parent))
            {
               destination = new DestinationMainData();

               final List<DestinationMainData> entries = destinationMap.get(parent);
               destination.setNoAccommodations(facetValue.getCount());
               if (facetValue.getCode() != null)
               {
                  if (facetValue.getCode().contains("|"))
                  {
                     code = facetValue.getCode().split(REG_EXP)[1];
                     destination.setId(DH + i + code);
                     destination.setValue(facetValue.getCode());
                     destination.setName(HtmlUtils
                        .htmlEscape(facetValue.getLabel().split(REG_EXP)[1]));
                     i++;
                  }
                  else
                  {
                     code = facetValue.getCode();
                     destination.setId(DO + code);
                     destination.setValue(code);
                     destination.setName(HtmlUtils.htmlEscape(facetValue.getLabel()));
                  }
               }

               entries.add(destination);
               destinationMap.put(parent, entries);
            }
            else
            {
               final List<DestinationMainData> entries = new ArrayList<DestinationMainData>();
               destination = new DestinationMainData();
               destination.setNoAccommodations(facetValue.getCount());
               if (facetValue.getCode() != null)
               {
                  if (facetValue.getCode().contains("|"))
                  {
                     code = facetValue.getCode().split(REG_EXP)[1];
                     destination.setId(DH + i + code);
                     destination.setValue(facetValue.getCode());
                     destination.setName(HtmlUtils
                        .htmlEscape(facetValue.getLabel().split(REG_EXP)[1]));
                     i++;
                  }
                  else
                  {
                     code = facetValue.getCode();
                     destination.setId(DO + code);
                     destination.setValue(code);
                     destination.setName(HtmlUtils.htmlEscape(facetValue.getLabel()));
                  }
               }

               entries.add(destination);
               destinationMap.put(parent, entries);
            }
         }
      }

      final List<DestinationMainData> destMainList = new ArrayList<DestinationMainData>();
      final Iterator it = destinationMap.entrySet().iterator();
      while (it.hasNext())
      {
         final Map.Entry pairs = (Map.Entry) it.next();
         final DestinationMainData key = (DestinationMainData) pairs.getKey();
         destMainList.add(key);
         key.setChildren((List<DestinationMainData>) pairs.getValue());
      }
      return destMainList;
   }

   private boolean facetValueNotEmpty(final String facetName, final FacetOption facet)
   {
      return facetName.equalsIgnoreCase(facet.getFacetOptionName())
         && CollectionUtils.isNotEmpty(facet.getFacetValues());
   }

   /**
    * @param facet
    * @return true / false
    */
   private boolean featureMatches(final FacetOption facet)
   {
      return facetOptionCheck(facet)
         || StringUtils.equalsIgnoreCase(facet.getFacetOptionName(), "FT000S")
         || StringUtils.equalsIgnoreCase(facet.getFacetOptionName(), "FT000E");
   }

   private boolean facetOptionCheck(final FacetOption facet)
   {

      return StringUtils.equalsIgnoreCase(facet.getFacetOptionName(), "FT000H")
         || StringUtils.equalsIgnoreCase(facet.getFacetOptionName(), "FT000O")
         || StringUtils.equalsIgnoreCase(facet.getFacetOptionName(), "FT000P");
   }

   private Map getBestForMap()
   {
      final Map bestFor = new HashMap();
      final String bestForValuesStr =
         configurationService.getConfiguration().getString("holiday.search.bestfor.categories");
      if (StringUtils.isNotEmpty(bestForValuesStr))
      {
         final String[] categoryValuesArry = StringUtils.split(bestForValuesStr, ",");
         if (categoryValuesArry.length > 0)
         {
            for (final String bestForValues : categoryValuesArry)
            {

               bestFor.put(StringUtils.split(bestForValues, ':')[0],
                  StringUtils.split(bestForValues, ':')[1]);
            }
         }
      }
      return bestFor;

   }

}
