/**
 *
 */
package uk.co.portaltech.tui.services.common;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.FeatureDescriptorModel;
import uk.co.portaltech.travel.model.FeatureValueModel;
import uk.co.portaltech.travel.model.FeatureValueSetModel;
import uk.co.portaltech.travel.model.TripAdvisorUserReviewsModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.populators.TripAdvisorPopulator;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.JsonadvisorUserReviewData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.feeds.util.FeedsCatalogUtil;

/**
 * @author omonikhide
 *
 */
public class TripAdvisorFeedProcessor extends AbstractFeedProcessor
{

   private static final String GET_ACCOMMODATION_QUERY =
      "SELECT {accomm.PK} FROM {Accommodation AS accomm JOIN CatalogVersion AS catVersion ON "
         + "{accomm.catalogVersion} = {catVersion.PK} JOIN Catalog AS catalog ON {catalog.PK} = {catVersion.catalog}}"
         + " WHERE {catVersion.version} = 'Online' AND {catalog.id} = ?catalogId";

   private static final String ALL_REVIEWS = "select {pk} from {TripAdvisorUserReviews}";

   private static final TUILogUtils LOGGER = new TUILogUtils("TripAdvisorFeedProcessor");

   private FlexibleSearchService flexibleSearchService;

   private ModelService modelService;

   @Resource
   private CatalogVersionService catalogVersionService;

   @Resource
   private TripAdvisorPopulator tripAdvisorPopulator;

   @Resource
   private SessionService sessionService;

   private static final int FOUR = 4;

   @Required
   public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
   {
      this.flexibleSearchService = flexibleSearchService;
   }

   @Required
   public void setModelService(final ModelService modelService)
   {
      this.modelService = modelService;
   }

   @Override
   public void processData(final String rootDir, final String outputDir, final String fileName)
   {
      LOGGER.debug("Starting import of tripadvisor data");
      logToFile = getCustomLog(outputDir, "trip");

      final FeatureService featureService =
         Registry.getApplicationContext().getBean(FeatureService.class);
      final FeedsCatalogUtil feedsCatalogUtil = new FeedsCatalogUtil();

      int count = 0;

      String currCode = "";
      try
      {
         final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(GET_ACCOMMODATION_QUERY);
         searchQuery.addQueryParameter("catalogId", feedsCatalogUtil.getActiveProductCatalog()
            .getId());
         searchQuery.addQueryParameter("catalogId", feedsCatalogUtil.getActiveProductCatalog()
            .getId());
         final SearchResult<AccommodationModel> searchResult =
            flexibleSearchService.<AccommodationModel> search(searchQuery);
         logToFile.info("Total number of records to be processed: " + searchResult.getCount());
         LOGGER.info("Total number of records to be processed:  " + searchResult.getCount());

         final List<AccommodationModel> accommodationModels = searchResult.getResult();
         FeatureValueSetModel featureValueSet = null;
         FeatureValueModel featureValue = null;
         final List<TripAdvisorUserReviewsModel> reviewsList =
            new ArrayList<TripAdvisorUserReviewsModel>();
         for (AccommodationModel accommodationModel : accommodationModels)
         {
            currCode = accommodationModel.getCode();
            if ("TH_FC"
               .equalsIgnoreCase(accommodationModel.getBrands().get(0).getCode().toString()))
            {
               sessionService.setAttribute("currentSite",
                  new FeedsCatalogUtil().getCMSSiteForId("TH"));

            }

            final AccommodationViewData tripAdvisorSummaryData =
               tripAdvisorPopulator.getTripAdvisorData(accommodationModel.getCode(),
                  Integer.valueOf(0));
            // Defcet115590 Trip advisor rating should not be -ve
            if (tripAdvisorSummaryData.getTripadvisorData() != null
               && tripAdvisorSummaryData.getTripadvisorData().getAverageRating() != null
               && tripAdvisorSummaryData.getTripadvisorData().getAverageRating().doubleValue() > 0.0)
            {
               accommodationModel.setReviewRating(tripAdvisorSummaryData.getTripadvisorData()
                  .getAverageRating());
               accommodationModel.setReviewsCount(Integer.valueOf(tripAdvisorSummaryData
                  .getTripadvisorData().getReviewsCount()));
               accommodationModel.setRatingsBar(tripAdvisorSummaryData.getTripadvisorData()
                  .getRatingBar());
               accommodationModel.setReviewRatingUrl(tripAdvisorSummaryData.getTripadvisorData()
                  .getRatingReviewsUrl());
               FeatureDescriptorModel featureDescriptor = new FeatureDescriptorModel();
               featureDescriptor.setCatalogVersion(getActiveCatalogversion());
               featureDescriptor.setCode("averagetripAdvisorRating");
               try
               {
                  featureDescriptor = flexibleSearchService.getModelByExample(featureDescriptor);
               }
               catch (final ModelNotFoundException e)
               {
                  LOGGER.error(
                     "Unable to find an existing FeatureDescriptor with code '"
                        + featureDescriptor.getCode() + "' and CatalogVersion '"
                        + featureDescriptor.getCatalogVersion(), e);
                  continue;
               }
               catch (final AmbiguousIdentifierException e)
               {
                  LOGGER.error(
                     "More than one FeatureDescriptor was found with code '"
                        + featureDescriptor.getCode() + "' and CatalogVersion '"
                        + featureDescriptor.getCatalogVersion(), e);
                  continue;
               }

               featureValueSet = featureService.newFeatureValueSet(getActiveCatalogversion());
               featureValueSet.setFeatureDescriptor(featureDescriptor);

               featureValue = featureService.newFeatureValue(getActiveCatalogversion());
               featureValue
                  .setValue(tripAdvisorSummaryData.getTripadvisorData().getAverageRating());
               modelService.attach(featureValueSet);
               modelService.attach(featureValue);
               featureValueSet =
                  featureService.addFeatureValueToFeatureValueSet(featureValue, featureValueSet);

               accommodationModel =
                  (AccommodationModel) featureService.addFeatureValueSetToItem(featureValueSet,
                     accommodationModel);

               getReviews(currCode, accommodationModel, reviewsList);
            }
            modelService.save(accommodationModel);

            count++;
         }

         if (CollectionUtils.isNotEmpty(reviewsList))
         {
            final FlexibleSearchQuery reviewsQuery = new FlexibleSearchQuery(ALL_REVIEWS);
            final SearchResult<TripAdvisorUserReviewsModel> result =
               flexibleSearchService.<TripAdvisorUserReviewsModel> search(reviewsQuery);
            logToFile.info("Total number of reviews to be removed: " + result.getCount());
            LOGGER.info("Total number of reviews to be removed:  " + result.getCount());

            final List<TripAdvisorUserReviewsModel> reviewModels = result.getResult();
            modelService.removeAll(reviewModels);
            logToFile.info("Total number of reviews to be persisted: " + reviewsList.size());
            LOGGER.info("Total number of reviews to be presisted:  " + reviewsList.size());
            modelService.saveAll(reviewsList);
         }
         logToFile.info("Total number of records successfully obtained: " + count);
         LOGGER.debug("Total number of records successfully obtained: " + count);

      }
      catch (final ModelSavingException modelSavingException)
      {
         LOGGER.error("Error saving accommodation", modelSavingException);
         logToFile.error("Error saving accommodation with code '" + currCode);
      }
      catch (final ModelNotFoundException e)
      {
         LOGGER.warn("Accommodations not found", e);
         logToFile.info("Accommodation with code '" + currCode);
      }
      catch (final Exception ex)
      {
         LOGGER.error("Failed to run tripadvisor rating update", ex);
         logToFile.info("Failed to run tripadvisor rating update");
      }

      LOGGER.debug("Ended import of tripadvisor data");
      logToFile.info("Ended import of tripadvisor data");

   }

   /**
    * @param currCode
    * @param accommodationModel
    */
   private void getReviews(final String currCode, final AccommodationModel accommodationModel,
      final List<TripAdvisorUserReviewsModel> reviewsList)
   {
      final List<JsonadvisorUserReviewData> userReviews =
         tripAdvisorPopulator.getJSONUserReviews(currCode, Integer.valueOf(FOUR));

      if (userReviews != null)
      {
         TripAdvisorUserReviewsModel userReview = null;

         final KeyGenerator taReviewCodeGenerator =
            (KeyGenerator) Registry.getApplicationContext().getBean("taReviewCodeGenerator");

         for (int x = 0; x < userReviews.size(); x++)
         {
            userReview = modelService.create(TripAdvisorUserReviewsModel.class);
            userReview.setAccommodationCode(currCode);
            userReview.setCode(taReviewCodeGenerator.generate().toString());
            userReview.setReviewsUrl(userReviews.get(x).getReviewsUrl());
            userReview.setAuthor(userReviews.get(x).getAuthor());
            userReview.setAuthorLocation(userReviews.get(x).getAuthorLocation());
            userReview.setContent(userReviews.get(x).getContent());
            userReview.setContentSummary(userReviews.get(x).getContentSummary());
            userReview.setRatingImage(userReviews.get(x).getRatingImage());
            userReview.setTitle(userReviews.get(x).getTitle());
            userReview.setAverageRating(String.valueOf(accommodationModel.getReviewRating()));
            userReview.setTotalReviewCount(String.valueOf(accommodationModel.getReviewsCount()));
            userReview.setPublishedDate(new Date(userReviews.get(x).getPublishedDate()));
            reviewsList.add(userReview);

         }
      }
   }

   /**
    * This method returns active Catalogversion
    *
    * @return CatalogVersionModel
    */
   private CatalogVersionModel getActiveCatalogversion()
   {
      final String catalog = new FeedsCatalogUtil().getActiveProductCatalog().getId();
      return catalogVersionService.getCatalogVersion(catalog, "Online");
   }

   @Override
   protected Map<String, Integer> getPositionMap()
   {
      return null;
   }

}
