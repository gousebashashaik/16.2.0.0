/**
 *
 */
package uk.co.portaltech.tui.services.mediafinder;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.AttractionModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.AccommodationService;
import uk.co.portaltech.travel.services.AttractionService;
import uk.co.portaltech.tui.components.model.HeroCarouselComponentModel;
import uk.co.portaltech.tui.converters.LocationConverter;
import uk.co.portaltech.tui.populators.MediaDataPopulator;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.AttractionViewData;
import uk.co.portaltech.tui.web.view.data.HasFeatures;
import uk.co.portaltech.tui.web.view.data.LocationData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.SearchRequestData;
import uk.co.portaltech.tui.web.view.data.SearchResultData;
import uk.co.portaltech.util.CatalogUtil;
import uk.co.tui.async.logging.TUILogUtils;

/**
 * @author omonikhide
 *
 */
public class ExplicitMediaFinder implements MediaFinder
{

   @Resource
   private CMSSiteService cmsSiteService;

   @Resource
   private FlexibleSearchService flexibleSearchService;

   @Resource
   private CatalogVersionService catalogVersionService;

   @Resource
   private CategoryService categoryService;

   @Resource
   private AccommodationService accommodationService;

   @Resource
   private AttractionService attractionService;

   @Resource
   private MediaDataPopulator mediaDataPopulator;

   @Resource
   private Converter<AccommodationModel, AccommodationViewData> accommodationConverter;

   @Resource
   private Converter<AttractionModel, AttractionViewData> attractionConverter;

   @Resource
   private LocationConverter locationConverter;

   @Resource
   private CatalogUtil catalogUtil;

   private static final TUILogUtils LOG = new TUILogUtils("ExplicitMediaFinder");

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.services.mediafinder.MediaFinder#search(uk.co.portaltech
    * .tui.catalog.Data.SearchReques final
    */
   private static final String GET_HEROCAROUSEL_FOR_PK =
      "SELECT {m.PK} FROM {MediaForHeroCarouselComponent AS hcc}, {Media AS m},"
         + "{catalogVersion AS cv} WHERE {hcc:target} = {m:pk} AND {hcc:source} =?carouselPk AND {m:catalogVersion} "
         + "= {cv:pk} AND {cv:pk} = ?Catalogversion";

   @Override
   public SearchResultData<MediaModel> search(final SearchRequestData request)
   {
      LOG.debug("Searching explicit products media.");
      final SearchResultData<MediaModel> result = new SearchResultData<MediaModel>();
      final int fromIndex = request.getOffset();
      final int toIndex = request.getOffset() + request.getPageSize();
      final HeroCarouselComponentModel carouselModel =
         (HeroCarouselComponentModel) request.getRelevantItem();
      final List<MediaModel> mediaForHeroCarousel =
         getMediaForHeroCarousel(carouselModel.getPk().toString(), fromIndex, toIndex);
      result.setResults(mediaForHeroCarousel);
      return result;
   }

   private List<MediaModel> getMediaForHeroCarousel(final String carouselPk, final int fromIndex,
      final int toIndex)
   {
      // Query is been made using the flexible search query paging functionality
      final String queryString =
         "select {target} from {MediaForHeroCarouselComponent} where {source}= ?carouselPk";
      final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);
      query.addQueryParameter("carouselPk", carouselPk);
      query.setCount(toIndex);
      query.setNeedTotal(true);
      query.setStart(fromIndex);
      // For now we are not making a unique search so it returns both the copy
      // in staged and online.
      // Later on it would be replaced with a unique search that returns a
      // single unique media based on the catalog version passed in.
      return flexibleSearchService.<MediaModel> search(query).getResult();
   }

   @Override
   public HasFeatures searchAutomatic(final SearchRequestData request, final String code,
      final String type)
   {

      return null;
   }

   @Override
   public HasFeatures searchManual(final SearchRequestData request, final String code,
      final String type)
   {
      final int fromIndex = request.getOffset();
      final int toIndex = request.getOffset() + request.getPageSize();
      final HeroCarouselComponentModel carouselModel =
         (HeroCarouselComponentModel) request.getRelevantItem();
      final List<MediaModel> carouselMedias =
         getMediaModelForComponent(carouselModel.getPk().toString(), fromIndex, toIndex);

      final List<MediaViewData> imageDataList = new ArrayList<MediaViewData>();
      if (carouselMedias != null && !carouselMedias.isEmpty())
      {
         for (final MediaModel mediaModel : carouselMedias)
         {
            final MediaViewData targetData = new MediaViewData();
            mediaDataPopulator.populate(mediaModel, targetData);
            imageDataList.add(targetData);
         }
      }

      if ("accommodation".equalsIgnoreCase(type))
      {

         final AccommodationModel accommodationModel =
            accommodationService.getAccomodationByCodeAndCatalogVersion(code,
               cmsSiteService.getCurrentCatalogVersion(), null);
         final AccommodationViewData accommodationViewData =
            accommodationConverter.convert(accommodationModel);
         accommodationViewData.setGalleryImages(imageDataList);
         return accommodationViewData;
      }
      else if ("location".equalsIgnoreCase(type))
      {
         final LocationModel locationModel =
            (LocationModel) categoryService.getCategoryForCode(code);
         final LocationData locationData = locationConverter.convert(locationModel);
         locationData.setGalleryImages(imageDataList);
         return locationData;
      }
      else if ("attraction".equalsIgnoreCase(type))
      {
         final AttractionModel attractionModel =
            (AttractionModel) attractionService.getAttractionForCode(code,
               cmsSiteService.getCurrentCatalogVersion());
         final AttractionViewData attractionViewData = attractionConverter.convert(attractionModel);
         attractionViewData.setGalleryImages(imageDataList);
         return attractionViewData;
      }

      return null;
   }

   List<MediaModel> getMediaModelForComponent(final String carouselPk, final int fromIndex,
      final int toIndex)
   {
      final FlexibleSearchQuery query = new FlexibleSearchQuery(GET_HEROCAROUSEL_FOR_PK);
      query.addQueryParameter("carouselPk", carouselPk);
      query.addQueryParameter(
         "catalogVersion",
         catalogVersionService.getCatalogVersion(catalogUtil.getActiveProductCatalog().getId(),
            "Online").getPk());
      query.setCount(toIndex);
      query.setNeedTotal(true);
      query.setStart(fromIndex);
      return flexibleSearchService.<MediaModel> search(query).getResult();
   }

   @Override
   public HasFeatures searchAttractions(final SearchRequestData request, final String code)
   {

      return null;
   }

}
