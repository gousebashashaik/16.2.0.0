/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import uk.co.portaltech.thirdparty.tripadvisor.client.TripadvisorServiceImpl;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.TripAdvisorUserReviewsModel;
import uk.co.portaltech.travel.services.AccommodationService;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.JsonTripadvisorFetchResults;
import uk.co.portaltech.tui.web.view.data.JsonadvisorUserRatingData;
import uk.co.portaltech.tui.web.view.data.JsonadvisorUserReviewData;
import uk.co.portaltech.tui.web.view.data.TripadvisorData;
import uk.co.portaltech.tui.web.view.data.UserReview;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.feeds.util.FeedsCatalogUtil;

import com.googlecode.ehcache.annotations.Cacheable;

/**
 * @author omonikhide
 *
 */
public class TripAdvisorPopulator
{
   @Resource
   private TripadvisorServiceImpl tripadvisorServiceImpl;

   @Resource
   private ViewSelector viewSelector;

   @Resource
   private AccommodationService accommodationService;

   @Resource
   private CatalogVersionService catalogVersionService;

   @Resource
   private BrandUtils brandUtils;

   private static final String TRIPADVISOR_ID = "tripadvisor_id";

   @Resource
   private AccommodationEditorialIntroductionPopulator accommodationEditorialIntroductionPopulator;

   @Resource
   private JsonTripadvisorFetchResults jsonTripadvisorResults;

   @Resource
   private FeatureService featureService;

   private final TUILogUtils logger = new TUILogUtils("TripAdvisorPopulator");

   // commented for performance fix
   public AccommodationViewData getTripAdvisorSummaryData(final String accommodationCode,
      final Integer maxUserReviews)
   {
      final AccommodationModel accomodationmodel = getAccommodation(accommodationCode);
      final AccommodationViewData accommodationData = new AccommodationViewData();
      accommodationEditorialIntroductionPopulator.populate(accomodationmodel, accommodationData);
      buildTripAdvisorDataFromAccomModel(accomodationmodel, accommodationData);

      return accommodationData;
   }

   /**
    * @param accomodationmodel
    * @param accommodationData
    */
   private void buildTripAdvisorDataFromAccomModel(final AccommodationModel accomodationmodel,
      final AccommodationViewData accommodationData)
   {
      final TripadvisorData tripadvisorData = new TripadvisorData();
      tripadvisorData.setRatingBar(accomodationmodel.getRatingsBar());
      tripadvisorData.setAverageRating(accomodationmodel.getAverageRating());
      final Integer reviewCount = accomodationmodel.getReviewsCount();
      if (reviewCount != null)
      {
         tripadvisorData.setReviewsCount(reviewCount.intValue());
      }
      tripadvisorData.setRatingReviewsUrl(accomodationmodel.getReviewRatingUrl());
      accommodationData.setTripadvisorData(tripadvisorData);
   }

   public AccommodationViewData getTripAdvisorData(final String accommodationCode,
      final Integer maxUserReviews)
   {
      final AccommodationViewData accommodationData = new AccommodationViewData();

      final AccommodationModel accomodationmodel = getAccommodation(accommodationCode);
      final List<Object> tripAdviserFeatureValues =
         featureService.getFeatureValues(TRIPADVISOR_ID, accomodationmodel, new Date(),
            brandUtils.getFeatureServiceBrand(accomodationmodel.getBrands()));

      if (CollectionUtils.isNotEmpty(tripAdviserFeatureValues))
      {
         final String jsontripreviewsid =
            getJsonTripReviewId(accommodationCode, tripAdviserFeatureValues);
         fetchAndProcessTripAdvisorData(maxUserReviews, accommodationData, jsontripreviewsid);
      }
      else
      {
         logger.error("couldn't find tripadvisor id for accommodation code " + accommodationCode);
      }

      return accommodationData;
   }

   /**
    * @param maxUserReviews
    * @param accommodationData
    * @param jsontripreviewsid
    */
   private void fetchAndProcessTripAdvisorData(final Integer maxUserReviews,
      final AccommodationViewData accommodationData, final String jsontripreviewsid)
   {
      if (jsontripreviewsid != null)
      {
         final JsonadvisorUserRatingData jsondata =
            getJsonTripAdvisorData(maxUserReviews, jsontripreviewsid);

         buildTripAdvisorDataFromJsonResults(accommodationData, jsondata);
      }
   }

   /**
    * @param maxUserReviews
    * @param jsontripreviewsid
    * @return
    */
   private JsonadvisorUserRatingData getJsonTripAdvisorData(final Integer maxUserReviews,
      final String jsontripreviewsid)
   {

      return (JsonadvisorUserRatingData) jsonTripadvisorResults.getTripAdvisorRating(
         jsontripreviewsid, maxUserReviews.intValue());
   }

   /**
    * @param accommodationData
    * @param jsondata
    */
   private void buildTripAdvisorDataFromJsonResults(final AccommodationViewData accommodationData,
      final JsonadvisorUserRatingData jsondata)
   {
      if (jsondata != null)
      {
         final TripadvisorData tripadvisorData = new TripadvisorData();
         tripadvisorData.setRatingBar(jsondata.getRatingBar());
         tripadvisorData.setAverageRating(jsondata.getAverageRating());
         tripadvisorData.setReviewsCount(jsondata.getReviewsCount());
         tripadvisorData.setRatingReviewsUrl(jsondata.getRatingReviewsUrl());
         accommodationData.setTripadvisorData(tripadvisorData);
      }
   }

   /**
    * @param accommodationCode
    * @param tripAdviserFeatureValues
    * @return
    */
   private String getJsonTripReviewId(final String accommodationCode,
      final List<Object> tripAdviserFeatureValues)
   {
      final String tripadvisorid = tripAdviserFeatureValues.get(0).toString();
      logger.debug("TRIPADVISOR_ID  " + tripadvisorid + "for accommodation code "
         + accommodationCode);
      final String jsontripid = tripadvisorid.replace('[', ' ');
      final String jsontripreviewsid = jsontripid.replace(']', ' ');
      return jsontripreviewsid.trim();
   }

   @Cacheable(cacheName = "tripAdvisorReviewsCache")
   public AccommodationViewData getTripAdvisorReviews(final String accommodationCode,
      final Integer maxUserReviews)
   {
      final List<TripAdvisorUserReviewsModel> userReviews =
         tripadvisorServiceImpl.getUserReviews(accommodationCode);
      final AccommodationViewData accommodationData = new AccommodationViewData();
      final TripadvisorData tripadvisorData = new TripadvisorData();
      final List<UserReview> reviews = new ArrayList<UserReview>();
      UserReview userReview = null;

      if (CollectionUtils.isNotEmpty(userReviews))
      {
         if (viewSelector.checkIsMobile())
         {
            tripadvisorData.setRatingBar(userReviews.get(0).getRatingImage());
            final String avgRating = userReviews.get(0).getAverageRating();
            if (avgRating != null)
            {
               tripadvisorData.setAverageRating(Double.valueOf(avgRating));
            }
            final String totalReviewCount = userReviews.get(0).getTotalReviewCount();
            if (totalReviewCount != null)
            {
               tripadvisorData.setReviewsCount(Integer.parseInt(totalReviewCount));
            }
         }

         tripadvisorData.setReviewsUrl(userReviews.get(0).getReviewsUrl());

         for (int i = 0; i < maxUserReviews.intValue(); i++)
         {
            userReview = new UserReview();
            userReview.setAuthor(userReviews.get(i).getAuthor());
            userReview.setAuthorLocation(userReviews.get(i).getAuthorLocation());
            userReview.setContent(userReviews.get(i).getContent());
            userReview.setContentSummary(userReviews.get(i).getContentSummary());
            userReview.setRatingImage(userReviews.get(i).getRatingImage());
            userReview.setTitle(userReviews.get(i).getTitle());
            userReview.setPublishedDate(convertDate(userReviews.get(i).getPublishedDate()
               .toString()));
            reviews.add(userReview);

         }
         tripadvisorData.setUserReviewsCount(Integer.parseInt(userReviews.get(0)
            .getTotalReviewCount()));
      }
      tripadvisorData.setUserReviews(reviews);

      accommodationData.setTripadvisorData(tripadvisorData);
      return accommodationData;
   }

   public List<JsonadvisorUserReviewData> getJSONUserReviews(final String accommodationCode,
      final Integer maxUserReviews)
   {

      final AccommodationModel accomodationmodel = getAccommodationForCode(accommodationCode);
      JsonadvisorUserRatingData jsondata = new JsonadvisorUserRatingData();

      final List<Object> tripAdviserFeatureValues =
         featureService.getFeatureValues(TRIPADVISOR_ID, accomodationmodel, new Date(),
            brandUtils.getFeatureServiceBrand(accomodationmodel.getBrands()));

      if (CollectionUtils.isNotEmpty(tripAdviserFeatureValues))
      {

         final String tripadvisorid = tripAdviserFeatureValues.get(0).toString();
         final String jsontripid = tripadvisorid.replace('[', ' ');
         final String jsontripreviewsid = jsontripid.replace(']', ' ');
         if (jsontripreviewsid != null)
         {
            jsondata =
               (JsonadvisorUserRatingData) jsonTripadvisorResults.getTripAdvisorreviews(
                  jsontripreviewsid.trim(), maxUserReviews);
         }
      }

      List<JsonadvisorUserReviewData> jsonuserReviews = null;

      jsonuserReviews = jsondata.getUserReviews();

      return jsonuserReviews;
   }

   private AccommodationModel getAccommodationForCode(final String accommodationCode)
   {
      return accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode,
         getActiveCatalogversion(), null);
   }

   private AccommodationModel getAccommodation(final String accommodationCode)
   {
      return accommodationService.getAccomodationByCodeAndCatalogVersion(accommodationCode,
         getActiveCatalogversion(), null);
   }

   /**
    * This method returns active Catalogversion
    *
    * @return CatalogVersionModel
    */
   private CatalogVersionModel getActiveCatalogversion()
   {
      final String catalog = new FeedsCatalogUtil().getActiveProductCatalog().getId();
      logger.info("Catalog  " + new FeedsCatalogUtil().getActiveProductCatalog().getId());
      return catalogVersionService.getCatalogVersion(catalog, "Online");
   }

   private String convertDate(final String originalDate)
   {
      String dayStr = null;
      Date date;
      try
      {
         date =
            new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH).parse(originalDate);

         final SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
         final SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
         final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
         dayStr = dayFormat.format(date)

         + " " + monthFormat.format(date) + " , " + yearFormat.format(date);

      }
      catch (final ParseException e)
      {
         logger.error("Ops!", e);
      }
      return dayStr;
   }
}
