/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.BaynoteRecommendationFacade;
import uk.co.portaltech.tui.facades.ComponentFacade;
import uk.co.portaltech.tui.populators.RecommendationsPopulator;
import uk.co.portaltech.tui.services.RecommendationsService;
import uk.co.portaltech.tui.utils.CSPSorting;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.HolidayViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.RecommendationsData;
import uk.co.portaltech.tui.web.view.data.SearchResultsRequestData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.exception.SearchResultsBusinessException;

/**
 * @author veena.pn
 *
 */
public class BaynoteRecommendationFacadeImpl implements BaynoteRecommendationFacade
{

   /**
    *
    */
   private static final String ON = "on";

   /**
    *
    */
   private static final String HOME_REC = "HomeRec";

   /**
    *
    */
   private static final String ACCOMODATION_REC = "AccomodationRec";

   /**
    *
    */
   private static final String LATEST_CRITERIA = "latestCriteria";

   /**
    * number 1
    */
   private static final int NUMBER_ONE = 1;

   private static final TUILogUtils LOGGER = new TUILogUtils("RecommendationsFacadeImpl");

   @Resource
   private RecommendationsService recommendationsService;

   @Resource
   private AccommodationFacade accomodationFacade;

   @Resource
   private ComponentFacade componentFacade;

   @Resource
   private RecommendationsPopulator recommendationsPopulator;

   @Resource
   private SessionService sessionService;

   /** The configuration service. */
   @Resource
   private ConfigurationService configurationService;

   /** The product service. */
   @Resource
   private ProductService productService;

   /** The country code. */
   private String countryName = StringUtils.EMPTY;

   /**
    * @return the configurationService
    */
   public ConfigurationService getConfigurationService()
   {
      return configurationService;
   }

   /**
    * @param configurationService the configurationService to set
    */
   public void setConfigurationService(final ConfigurationService configurationService)
   {
      this.configurationService = configurationService;
   }

   /**
    * Clone search criteria.
    *
    * @param searchParameter the search parameter
    * @return the search results request data
    */
   protected SearchResultsRequestData cloneSearchCriteria(
      final SearchResultsRequestData searchParameter)
   {
      SearchResultsRequestData searchCriteriaClone = new SearchResultsRequestData();
      try
      {
         searchCriteriaClone = (SearchResultsRequestData) BeanUtils.cloneBean(searchParameter);
      }
      catch (final Exception e)
      {
         Log.error("Unable to Clone Search Criteria" + e.getMessage());
      }
      return searchCriteriaClone;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.facades.RecommendationsFacade#getProductProductRanges(java.util.ArrayList
    * )
    */
   @Override
   public RecommendationsData getProductProductRanges(final List<String> productCodes,
      final String siteBrand, final RecommendationsData recommendationsData)
   {
      LOGGER.debug("getProductProductRanges is calling");

      final List<AccommodationViewData> accomViewDataList = new ArrayList<AccommodationViewData>();

      if (ON.equalsIgnoreCase(baynoteStubStatus()))
      {
         recommendationsService.getRecommendations(HOME_REC, siteBrand);
      }
      else
      {
         handleAccomProductRanges(productCodes, recommendationsData, accomViewDataList);
      }
      return recommendationsData;

   }

   /**
    * Gets the product ranges for the recommended accom
    *
    * @param productCodes
    * @param recommendationsData
    * @param accomViewDataList
    */
   private void handleAccomProductRanges(final List<String> productCodes,
      final RecommendationsData recommendationsData,
      final List<AccommodationViewData> accomViewDataList)
   {
      try
      {
         for (final String productCode : productCodes)
         {
            AccommodationViewData accomViewData = null;
            accomViewData = accomodationFacade.getAccommodationProductRanges(productCode);
            accomViewData.setDeepLinkUrl(accomViewData.getUrl());
            final AccommodationModel accommodationModel = getAccommodationModel(productCode);
            populateMediaModel(accomViewData, accommodationModel);

            final List<CategoryModel> categories =
               new ArrayList<CategoryModel>(accommodationModel.getSupercategories());
            if (populateTRating(accomViewData) != null)
            {
               accomViewData.settRating(populateTRating(accomViewData));
            }
            populateAccom(accomViewData, accommodationModel, categories);
            addToAccomList(accomViewDataList, accomViewData);

         }
         populateBaynoteData(accomViewDataList, recommendationsData);
         recommendationsData.setAccomodationDatas(accomViewDataList);
         LOGGER.debug("Recommendations Size: " + recommendationsData.getAccomodationDatas().size());
      }
      catch (final Exception exp)
      {
         LOGGER.info("Exception while getting AccommodationProductRanges: " + exp.getMessage());
      }
   }

   /**
    * @param accomViewData
    * @param accommodationModel
    * @throws ConversionException
    */
   private void populateMediaModel(final AccommodationViewData accomViewData,
      final AccommodationModel accommodationModel) throws ConversionException
   {
      for (final MediaContainerModel mediaContainerModel : accommodationModel.getGalleryImages())
      {
         for (final MediaModel media : mediaContainerModel.getMedias())
         {
            accomViewData.getGalleryImages().add(getMediaData(media));
            break;
         }
      }
   }

   /**
    * Populate media data.
    *
    * @param sourceModel the source model
    * @throws ConversionException the conversion exception
    */
   private MediaViewData getMediaData(final MediaModel sourceModel) throws ConversionException
   {
      final MediaViewData mediaData = new MediaViewData();
      Assert.notNull(sourceModel, "Converter source must not be null");
      mediaData.setCode(sourceModel.getCode());
      mediaData.setAltText(sourceModel.getAltText());
      mediaData.setDescription(sourceModel.getDescription());
      mediaData.setMime(sourceModel.getMime());
      mediaData.setMainSrc(sourceModel.getURL());
      mediaData.setSize(sourceModel.getMediaFormat().getName());
      return mediaData;
   }

   /**
    * populataes baymote data
    *
    * @param accomViewDataList
    * @param recommendationsData
    */
   private void populateBaynoteData(final List<AccommodationViewData> accomViewDataList,
      final RecommendationsData recommendationsData)
   {

      for (final AccommodationViewData accom : recommendationsData.getAccomodationDatas())
      {
         for (final AccommodationViewData accomData : accomViewDataList)
         {
            if (accomData.getCode().equalsIgnoreCase(accom.getCode()))
            {
               accomData.setBaynoteTrackingData(accom.getBaynoteTrackingData());
               accomData.setBaynoteTrackingMap(accom.getBaynoteTrackingMap());
               accomData.setFeaturedImgUrl(accom.getFeaturedImgUrl());
               break;
            }
         }
      }
   }

   /**
    * Adding accom to list
    *
    * @param accomViewDataList
    * @param accomViewData
    */
   private void addToAccomList(final List<AccommodationViewData> accomViewDataList,
      final AccommodationViewData accomViewData)
   {
      if (accomViewData != null)
      {
         accomViewDataList.add(accomViewData);
      }
   }

   /**
    * Populate T rating
    *
    * @param accommodationViewData
    * @return
    */
   private String populateTRating(final AccommodationViewData accommodationViewData)
   {
      String tRating = null;
      if (accommodationViewData.getFeatureCodesAndValues().containsKey("tRating"))
      {
         tRating = accommodationViewData.getFeatureCodesAndValues().get("tRating").toString();
      }
      return tRating;
   }

   /**
    * populate acoom details
    *
    * @param accommodation
    * @param accommodationModel
    */
   private void populateAccom(final AccommodationViewData accommodation,
      final AccommodationModel accommodationModel, final List<CategoryModel> categories)
   {
      boolean flag = false;
      for (final CategoryModel category : categories)
      {
         if (category.getClass() == LocationModel.class)
         {
            final LocationModel location = (LocationModel) category;
            updateResortName(accommodation, accommodationModel, location);
            if (!flag)
            {
               flag = updateDestination(accommodation, location);
            }
         }

      }
      accommodation.setCountryName(getCountryName(categories));
   }

   /**
    * populate resort name
    *
    * @param accommodation
    * @param accommodationModel
    * @param location
    */
   private void updateResortName(final AccommodationViewData accommodation,
      final AccommodationModel accommodationModel, final LocationModel location)
   {
      if (LocationType.RESORT.equals(location.getType()))
      {
         accommodation.setResortName(location.getName());
         final List<CategoryModel> subCategories = location.getSupercategories();
         populateAccom(accommodation, accommodationModel, subCategories);
      }
   }

   /**
    * Gets country name
    *
    * @param categories
    * @return
    */
   private String getCountryName(final List<CategoryModel> categories)
   {
      if (CollectionUtils.isNotEmpty(categories))
      {
         for (final CategoryModel category : categories)
         {
            if (category.getClass() == LocationModel.class)
            {
               countryName = setCountryName(category);
            }
         }
      }
      return countryName;
   }

   /**
    * Sets country name
    *
    * @param category
    */
   private String setCountryName(final CategoryModel category)
   {
      final LocationModel location = (LocationModel) category;
      if (LocationType.COUNTRY == location.getType())
      {
         countryName = location.getName();
         return countryName;
      }
      else
      {
         getCountryName(location.getSupercategories());
      }
      return countryName;
   }

   /**
    * Populates destination name
    *
    * @param accommodation
    * @param location
    */
   private boolean updateDestination(final AccommodationViewData accommodation,
      final LocationModel location)
   {
      if (LocationType.COUNTRY == location.getType())
      {
         accommodation.setDestinationName(location.getName());
         return true;
      }
      if (LocationType.DESTINATION == location.getType())
      {
         accommodation.setDestinationName(location.getName());
         return true;
      }
      if (LocationType.REGION == location.getType())
      {
         accommodation.setDestinationName(location.getName());
         return true;
      }
      return false;
   }

   /**
    * Method to return AccommodationModel from accommodation Code from PIM.
    *
    * @param accomCode
    * @return AccommodationModel
    *
    */
   private AccommodationModel getAccommodationModel(final String accomCode)
   {
      return (AccommodationModel) productService.getProductForCode(accomCode);
   }

   @Override
   public RecommendationsData getRecommendedAccomPriceInfoBrowse(final List<String> productCodes,
      final Boolean cspSorting, final String siteBrand,
      final RecommendationsData recommendationsData)
   {
      LOGGER.debug("getRecommendedAccomPriceInfoBrowse");
      List<AccommodationViewData> endecaAccomViewData;
      RecommendationsData browseFlowRecomData = new RecommendationsData();

      if (ON.equalsIgnoreCase(baynoteStubStatus()))
      {
         browseFlowRecomData =
            recommendationsService.getRecommendations(ACCOMODATION_REC, siteBrand);
      }
      else
      {
         endecaAccomViewData =
            componentFacade.getRecommendedAccomPriceInfo(productCodes, null, null);

         endecaAccomViewData =
            handleAccomPriceInfoBrowse(productCodes, cspSorting, siteBrand, endecaAccomViewData,
               recommendationsData);

         browseFlowRecomData.setAccomodationDatas(endecaAccomViewData);

         LOGGER.debug("Recommendations Size: " + browseFlowRecomData.getAccomodationDatas().size());
      }
      return browseFlowRecomData;
   }

   /**
    * Populating accom price for browse flow
    *
    * @param productCodes
    * @param cspSorting
    * @param siteBrand
    * @param endecaAccomViewData
    * @return
    */
   @SuppressWarnings("javadoc")
   private List<AccommodationViewData> handleAccomPriceInfoBrowse(final List<String> productCodes,
      final Boolean cspSorting, final String siteBrand,
      final List<AccommodationViewData> endecaAccomViewData,
      final RecommendationsData recommendationsData)
   {
      List<AccommodationViewData> finalAccomList = new ArrayList<AccommodationViewData>();
      getProductProductRanges(productCodes, siteBrand, recommendationsData);
      if (CollectionUtils.isNotEmpty(recommendationsData.getAccomodationDatas()))
      {
         finalAccomList =
            recommendationsPopulator.getBrowseRecommendedAccommodationsViewData(
               recommendationsData.getAccomodationDatas(), endecaAccomViewData);

      }

      cspSortingPopulation(cspSorting, finalAccomList);
      return finalAccomList;
   }

   // Populate recommendation data for book
   @Override
   public RecommendationsData getReccommendedHolidayPackageDataBook(final String siteBrand,
      final List<String> productCodes, final Boolean cspSorting,
      final RecommendationsData recommendationsData)
   {
      LOGGER.debug("getReccommendedHolidayPackageDataBook");

      final Map<String, String> accomNameCode = new HashMap<String, String>();
      RecommendationsData bookRecData = new RecommendationsData();

      if (ON.equalsIgnoreCase(baynoteStubStatus()))
      {
         bookRecData = recommendationsService.getRecommendations(ACCOMODATION_REC, siteBrand);
      }
      else
      {
         getProductProductRanges(productCodes, siteBrand, recommendationsData);

         final SearchResultsRequestData searchParameterInSession =
            cloneSearchCriteria((SearchResultsRequestData) sessionService
               .getAttribute(LATEST_CRITERIA));

         bookRecData =
            populateAccomData(siteBrand, cspSorting, accomNameCode, bookRecData,
               recommendationsData, searchParameterInSession);
      }

      return bookRecData;
   }

   /**
    *
    * Populates accom data
    *
    * @param siteBrand
    * @param cspSorting
    * @param accomNameCode
    * @param endecaHolidayViewData
    * @param bookRecData
    * @param recommendationsData
    * @param searchParameterInSession
    */
   private RecommendationsData populateAccomData(final String siteBrand, final Boolean cspSorting,
      final Map<String, String> accomNameCode, final RecommendationsData bookRecData,
      final RecommendationsData recommendationsData,
      final SearchResultsRequestData searchParameterInSession)
   {
      List<AccommodationViewData> bookFlowAccomData;
      HolidayViewData endecaHolidayViewData = new HolidayViewData();
      if (CollectionUtils.isNotEmpty(recommendationsData.getAccomodationDatas())
         && recommendationsData.getAccomodationDatas() != null)
      {
         for (final AccommodationViewData accomData : recommendationsData.getAccomodationDatas())
         {
            accomNameCode.put(accomData.getCode(), accomData.getName());
         }
         searchParameterInSession.setRecomData(accomNameCode);
         searchParameterInSession.setFirst(NUMBER_ONE);
         searchParameterInSession.setOffset(accomNameCode.size());

         endecaHolidayViewData = populateEndecaViewData(siteBrand, searchParameterInSession);

         bookFlowAccomData =
            recommendationsPopulator.getBookflowRecommendedAccommodationsViewData(
               recommendationsData.getAccomodationDatas(), endecaHolidayViewData);

         /* CSPSorting by DESCENDING Order */

         bookFlowAccomData = cspSortingPopulation(cspSorting, bookFlowAccomData);
         bookRecData.setAccomodationDatas(bookFlowAccomData);
         LOGGER.debug("Recommendations Size: " + bookRecData.getAccomodationDatas().size());
      }
      return bookRecData;
   }

   /**
    * Populate endeca data
    *
    * @param siteBrand
    * @param searchParameterInSession
    * @param endecaHolidayViewData
    * @return
    */
   private HolidayViewData populateEndecaViewData(final String siteBrand,
      final SearchResultsRequestData searchParameterInSession)
   {
      HolidayViewData endecaHolidayViewData = new HolidayViewData();

      try
      {
         endecaHolidayViewData =
            componentFacade.getReccommendedHolidayPackageData(searchParameterInSession, siteBrand);
      }
      catch (final SearchResultsBusinessException e)
      {
         LOGGER.error("Error While caling getReccommendedHolidayPackageData : " + e.getMessage());
      }
      return endecaHolidayViewData;
   }

   /**
    * sort according to csp sort
    *
    * @param cspSorting
    * @param endecaAccomViewData
    * @param bookFlowAccomData
    * @return
    */

   private List<AccommodationViewData> cspSortingPopulation(final Boolean cspSorting,
      final List<AccommodationViewData> endecaAccomViewData)
   {
      if (cspSorting != null && Boolean.TRUE.equals(cspSorting))
      {
         CSPSorting.getCSPSortingOrder(endecaAccomViewData);
      }
      return endecaAccomViewData;
   }

   @Override
   public String baynoteStubStatus()
   {
      String baynoteStubCheck = null;
      baynoteStubCheck =
         configurationService.getConfiguration().getString("recommendations.baynote.stubs");
      return baynoteStubCheck;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.facades.BaynoteRecommendationFacade#getProductProductRanges(java.util
    * .List, java.lang.String)
    */
   @Override
   public RecommendationsData getProductProductRanges(final List<String> productCodes,
      final String siteBrand)
   {
      // YTODO Auto-generated method stub
      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.facades.BaynoteRecommendationFacade#getRecommendedAccomPriceInfoBrowse
    * (java.util.List, java.lang.Boolean, java.lang.String)
    */
   @Override
   public RecommendationsData getRecommendedAccomPriceInfoBrowse(final List<String> productCodes,
      final Boolean cspSorting, final String siteBrand)
   {
      // YTODO Auto-generated method stub
      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.facades.BaynoteRecommendationFacade#getReccommendedHolidayPackageDataBook
    * (java.lang.String, java.util.List, java.lang.Boolean)
    */
   @Override
   public RecommendationsData getReccommendedHolidayPackageDataBook(final String brandtype,
      final List<String> productCodes, final Boolean cspSorting)
   {
      // YTODO Auto-generated method stub
      return null;
   }
}
