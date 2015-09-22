/**
 *
 */
package uk.co.portaltech.tui.populators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.thirdparty.endeca.BoardBasisDataResponse;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.BoardBasisType;
import uk.co.portaltech.tui.web.view.data.HolidayViewData;
import uk.co.portaltech.tui.web.view.data.SearchResultViewData;

/**
 *
 */
public class RecommendationsPopulator
{

   /**
    * @param recommAccomViewData
    * @param endecaAccomViewData
    * @return List<AccommodationViewData>.
    */
   public List<AccommodationViewData> getBrowseRecommendedAccommodationsViewData(
      final List<AccommodationViewData> recommAccomViewData,
      final List<AccommodationViewData> endecaAccomViewData)
   {
      final List<AccommodationViewData> finalAccommData = new ArrayList<AccommodationViewData>();
      final Map<String, AccommodationViewData> endecaValues =
         new HashMap<String, AccommodationViewData>();

      for (final AccommodationViewData viewData : endecaAccomViewData)
      {
         final AccommodationViewData aviewData = new AccommodationViewData();

         aviewData.setPriceFrom(formatPrice(viewData.getPriceFrom()));

         aviewData.setCommercialPriority(viewData.getCommercialPriority());
         aviewData.setComPriority(Integer.parseInt(viewData.getCommercialPriority()));
         aviewData.setBoardBasis(viewData.getBoardBasis());
         endecaValues.put(viewData.getCode(), aviewData);
      }
      if (CollectionUtils.isNotEmpty(recommAccomViewData) && recommAccomViewData != null)
      {
         for (final AccommodationViewData accomRec : recommAccomViewData)
         {
            if (endecaValues.containsKey(accomRec.getCode()))
            {
               final AccommodationViewData accomData = new AccommodationViewData();
               accomData.setBrand(accomRec.getBrand());
               accomData.setName(accomRec.getName());
               accomData.setDestinationName(accomRec.getDestinationName());
               accomData.setResortName(accomRec.getResortName());
               accomData.setUrl(accomRec.getUrl());
               accomData.setCode(accomRec.getCode());
               accomData.setGalleryImages(accomRec.getGalleryImages());
               accomData.setDeepLinkUrl(accomRec.getUrl());
               accomData.setProductRanges(accomRec.getProductRanges());
               accomData.setFeaturedImgUrl(accomRec.getFeaturedImgUrl());
               // don't comment this, its required for UI
               accomData.putFeatureCodesAndValues(accomRec.getFeatureCodesAndValues());
               accomData.setDestinationName(accomRec.getDestinationName());
               accomData.setBaynoteTrackingMap(accomRec.getBaynoteTrackingMap());
               accomData.setAccommodationType(accomRec.getAccommodationType());
               accomData.setReviewRating(accomRec.getReviewRating());
               accomData.setProductRanges(accomRec.getProductRanges());
               accomData.settRating(accomRec.gettRating());
               accomData.setCountryName(accomRec.getCountryName());
               final AccommodationViewData accomFromEndeca = endecaValues.get(accomRec.getCode());
               accomData.setPriceFrom(formatPrice(accomFromEndeca.getPriceFrom()));
               accomData.setComPriority(accomFromEndeca.getComPriority());
               formatBoardBasisPrice(accomFromEndeca);
               accomData.setBoardBasis(accomFromEndeca.getBoardBasis());
               accomData.setBaynoteTrackingData(accomRec.getBaynoteTrackingData());
               accomData.setBaynoteTrackingMap(accomRec.getBaynoteTrackingMap());
               finalAccommData.add(accomData);
            }
         }
      }

      return finalAccommData;
   }

   private String formatPrice(final String price)
   {
      final List<String> str = Arrays.asList(price.split("\\."));
      if (str.size() > 1)
      {
         final String decinmal = str.get(1);
         if (decinmal.startsWith("0"))
         {
            return str.get(0);
         }
         else
         {
            return price;
         }
      }
      else
      {
         return price;
      }
   }

   /**
    * @param accomViewDatas
    * @param viewData
    * @return List<AccommodationViewData>
    */
   public List<AccommodationViewData> getBookflowRecommendedAccommodationsViewData(
      final List<AccommodationViewData> accomViewDatas, final HolidayViewData viewData)
   {
      final List<AccommodationViewData> finalbookflowAccomData =
         new ArrayList<AccommodationViewData>();
      final List<SearchResultViewData> searchDataFromEndeca =
         viewData.getSearchResult().getHolidays();
      final Map<String, AccommodationViewData> searchDataMap =
         new HashMap<String, AccommodationViewData>();

      for (final SearchResultViewData data : searchDataFromEndeca)
      {
         final AccommodationViewData accomData = new AccommodationViewData();

         accomData.setDeparturePoint(data.getItinerary().getDepartureAirport());
         popBoardBasis(data, accomData);
         accomData.setComPriority(data.getAccommodation().getCommercialPriority());
         accomData.setDepartureDate(data.getItinerary().getDepartureDate());
         accomData.setRecBookUrl(data.getAccommodation().getUrl());
         accomData.setDuration(data.getDuration());
         accomData.setPriceFrom(formatPrice(data.getPrice().getPerPerson()));

         // populate board basis.
         populateAccomBoardBasis(data, accomData);

         searchDataMap.put(data.getAccommodation().getCode(), accomData);
      }

      if (CollectionUtils.isNotEmpty(accomViewDatas) && accomViewDatas != null)
      {
         for (final AccommodationViewData accom : accomViewDatas)
         {

            if (searchDataMap.containsKey(accom.getCode()))
            {
               final AccommodationViewData finalAcomData = new AccommodationViewData();
               finalAcomData.setBrand(accom.getBrand());
               finalAcomData.setName(accom.getName());
               finalAcomData.setDestinationName(accom.getDestinationName());
               finalAcomData.setResortName(accom.getResortName());
               finalAcomData.setUrl(accom.getUrl());
               finalAcomData.setDeepLinkUrl(accom.getUrl());
               finalAcomData.setCode(accom.getCode());
               finalAcomData.setGalleryImages(accom.getGalleryImages());
               finalAcomData.setProductRanges(accom.getProductRanges());
               finalAcomData.setCountryName(accom.getCountryName());
               finalAcomData.setAccommodationType(accom.getAccommodationType());
               finalAcomData.setReviewRating(accom.getReviewRating());
               finalAcomData.setProductRanges(accom.getProductRanges());
               finalAcomData.settRating(accom.gettRating());
               finalAcomData.setCountryName(accom.getCountryName());
               finalAcomData.setFeaturedImgUrl(accom.getFeaturedImgUrl());
               finalAcomData.putFeatureCodesAndValues(accom.getFeatureCodesAndValues());

               finalAcomData.setBaynoteTrackingData(accom.getBaynoteTrackingData());
               final AccommodationViewData endecaData = searchDataMap.get(accom.getCode());

               finalAcomData.setDeparturePoint(endecaData.getDeparturePoint());
               finalAcomData.setDuration(endecaData.getDuration());
               finalAcomData.setDepartureDate(endecaData.getDepartureDate());
               finalAcomData.setComPriority(endecaData.getComPriority());
               finalAcomData.setPriceFrom(formatPrice(endecaData.getPriceFrom()));

               formatBoardBasisPrice(endecaData);

               finalAcomData.setRecBoardBasis(endecaData.getRecBoardBasis());
               finalAcomData.setRecBookUrl(endecaData.getRecBookUrl());
               // populated board basis
               finalAcomData.setBoardBasis(endecaData.getBoardBasis());

               finalbookflowAccomData.add(finalAcomData);
            }
         }
      }

      return finalbookflowAccomData;
   }

   /**
    * @param data
    * @param accomData
    */
   private void popBoardBasis(final SearchResultViewData data, final AccommodationViewData accomData)
   {
      if (StringUtils.isNotBlank(data.getAccommodation().getRooms().get(0).getBoardType()))
      {
         accomData.setRecBoardBasis(data.getAccommodation().getRooms().get(0).getBoardType());
      }
   }

   /**
    * @param endecaData
    */
   private void formatBoardBasisPrice(final AccommodationViewData endecaData)
   {
      for (final BoardBasisDataResponse boardBasis : endecaData.getBoardBasis())
      {
         final String formatPrice = boardBasis.getPrice();
         final String formettedPrice = formatPrice(formatPrice);
         boardBasis.setPrice(formettedPrice);
      }
   }

   /**
    * @param boadBasisList
    * @param data
    * @param accomData
    */
   private void populateAccomBoardBasis(final SearchResultViewData data,
      final AccommodationViewData accomData)
   {
      final List<BoardBasisDataResponse> boadBasisList = new ArrayList<BoardBasisDataResponse>();

      for (final BoardBasisType boardBasisType : data.getAlternateBoard())
      {
         final BoardBasisDataResponse boadBasis = new BoardBasisDataResponse();
         boadBasis.setBoardbasisCode(boardBasisType.getBoardbasisCode());
         boadBasis.setDefaultBoardBasis(boardBasisType.isDefaultBoardBasis());
         boadBasis.setName(boardBasisType.getName());
         boadBasis.setPrice(boardBasisType.getTotalPricePP());
         boadBasisList.add(boadBasis);
      }
      accomData.setBoardBasis(boadBasisList);
   }

}
