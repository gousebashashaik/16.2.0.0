/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import uk.co.portaltech.travel.model.ProductRangeCategoryModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeCategoryViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeCollectionViewData;

/**
 * @author pts
 *
 */
public class ProductRangeCollectionConverter
   extends
   AbstractPopulatingConverter<Collection<CategoryModel>, Map<String, ProductRangeCategoryViewData>>
{

   private static final String SELECT_ALL_SUBCATEGORIES =
      " SELECT tab.PK "
         + " FROM ( "
         + "   {{ SELECT {cat.pk} AS PK, {cat.commercialPriority} AS earlySalesComm "
         + "   {{ SELECT {cat.pk} AS PK "
         + "      FROM {CategoryCategoryRelation AS catRel JOIN ProductRange AS cat ON {catRel.target} = {cat.pk}} "
         + "      WHERE {catRel.source} = ?categoryPk }} "
         + "   UNION "
         + "   {{ SELECT {cat.pk} AS PK, {cat.commercialPriority} AS earlySalesComm "
         + "   {{ SELECT {cat.pk} AS PK "
         + "      FROM {CategoryCategoryRelation AS catRel JOIN Location AS cat ON {catRel.target} = {cat.pk}} "
         + "      WHERE {catRel.source}= ?categoryPk }}" + " ) AS tab"
         + " ORDER BY tab.earlySalesComm ";

   @Resource
   private CategoryService categoryService;

   @Resource
   private FlexibleSearchService flexibleSearchService;

   @Resource
   private TUIUrlResolver<CategoryModel> tuiCategoryModelUrlResolver;

   private Populator<Collection<MediaContainerModel>, List<ImageData>> productRangeImageDataPopulator;

   private Populator<MediaModel, MediaViewData> mediaViewDataPopulator;

   private Converter<MediaModel, MediaViewData> mediaModelConverter;

   @Override
   public Map<String, ProductRangeCategoryViewData> createTarget()
   {
      return new HashMap<String, ProductRangeCategoryViewData>();
   }

   @Override
   public void populate(final Collection<CategoryModel> source,
      final Map<String, ProductRangeCategoryViewData> target)
   {
      for (final CategoryModel categoryModel : source)
      {

         final ProductRangeCategoryViewData prCatViewData = new ProductRangeCategoryViewData();
         prCatViewData.setPrCategoryName(categoryModel.getName());
         prCatViewData.setPrCategoryDescription(categoryModel.getDescription());
         prCatViewData
            .setPrCollection(addAllProductRangesForCategory(getAllSubcategories(categoryModel)));

         target.put(categoryModel.getCode(), prCatViewData);
      }
   }

   /**
    * @param allSubcategories
    * @return List<ProductRangeCollectionViewData>
    */
   private List<ProductRangeCollectionViewData> addAllProductRangesForCategory(
      final Collection<CategoryModel> allSubcategories)
   {
      final List<ProductRangeCollectionViewData> prRangeLst =
         new ArrayList<ProductRangeCollectionViewData>();
      for (final CategoryModel categoryModel : allSubcategories)
      {
         if ("ProductRange".equals(categoryModel.getItemtype()))
         {
            final ProductRangeModel prModel = (ProductRangeModel) categoryModel;
            final ProductRangeCollectionViewData prCollViewData =
               new ProductRangeCollectionViewData();
            prCollViewData.setProductName(prModel.getName());
            prCollViewData.setProductDescription(prModel.getDescription());
            this.setImageData(prModel, prCollViewData);
            prCollViewData.setProductURL(tuiCategoryModelUrlResolver.resolve(prModel));
            prRangeLst.add(prCollViewData);
         }
      }
      return prRangeLst;
   }

   private void setImageData(final ProductRangeModel productRangeModel,
      final ProductRangeCollectionViewData prCollViewData)
   {
      final Collection<MediaContainerModel> mediaContainers = productRangeModel.getGalleryImages();
      if (mediaContainers != null && !mediaContainers.isEmpty())
      {
         final List<ImageData> imageDataList = new ArrayList<ImageData>();
         productRangeImageDataPopulator.populate(mediaContainers, imageDataList);
         prCollViewData.setImageDataList(imageDataList);
      }

      final Collection<MediaModel> primaryImages = productRangeModel.getMedias();
      if (primaryImages != null && !primaryImages.isEmpty())
      {
         final List<MediaViewData> primaryImageViewDataList = new ArrayList<MediaViewData>();
         for (final MediaModel primaryImage : primaryImages)
         {
            final MediaViewData primaryImageViewData = mediaModelConverter.convert(primaryImage);
            mediaViewDataPopulator.populate(primaryImage, primaryImageViewData);
            primaryImageViewDataList.add(primaryImageViewData);
         }
         prCollViewData.setGalleryImages(primaryImageViewDataList);
      }
   }

   private List<CategoryModel> getAllSubcategories(final String categoryPk)
   {
      final FlexibleSearchQuery flexibleSearchQuery =
         new FlexibleSearchQuery(SELECT_ALL_SUBCATEGORIES);
      flexibleSearchQuery.addQueryParameter("categoryPk", categoryPk);
      final SearchResult<CategoryModel> searchResult =
         flexibleSearchService.<CategoryModel> search(flexibleSearchQuery);
      return searchResult.getResult();
   }

   private Collection<CategoryModel> getAllSubcategories(final CategoryModel categoryModel)
   {
      if (categoryModel instanceof ProductRangeCategoryModel)
      {
         return categoryService.getAllSubcategoriesForCategory(categoryModel);
      }
      else
      {
         return getAllSubcategories(categoryModel.getPk().toString());
      }

   }

   /**
    * @param productRangeImageDataPopulator the productRangeImageDataPopulator to set
    */
   public void setProductRangeImageDataPopulator(
      final Populator<Collection<MediaContainerModel>, List<ImageData>> productRangeImageDataPopulator)
   {
      this.productRangeImageDataPopulator = productRangeImageDataPopulator;
   }

   /**
    * @param mediaViewDataPopulator the mediaViewDataPopulator to set
    */
   public void setMediaViewDataPopulator(
      final Populator<MediaModel, MediaViewData> mediaViewDataPopulator)
   {
      this.mediaViewDataPopulator = mediaViewDataPopulator;
   }

   /**
    * @param mediaModelConverter the mediaModelConverter to set
    */
   public void setMediaModelConverter(final Converter<MediaModel, MediaViewData> mediaModelConverter)
   {
      this.mediaModelConverter = mediaModelConverter;
   }

}
