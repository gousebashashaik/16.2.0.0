/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeCollectionViewData;

/**
 * @author pts
 *
 */
public class ProductRangeProductConverter extends
   AbstractPopulatingConverter<ProductRangeModel, ProductRangeCollectionViewData>
{

   @Resource
   private TUIUrlResolver<CategoryModel> tuiCategoryModelUrlResolver;

   private Populator<Collection<MediaContainerModel>, List<ImageData>> productRangeImageDataPopulator;

   private Populator<MediaModel, MediaViewData> mediaViewDataPopulator;

   private Converter<MediaModel, MediaViewData> mediaModelConverter;

   @Override
   public ProductRangeCollectionViewData createTarget()
   {
      return new ProductRangeCollectionViewData();
   }

   @Override
   public void populate(final ProductRangeModel source, final ProductRangeCollectionViewData target)
   {

      target.setProductName(source.getName());
      target.setProductDescription(source.getDescription());
      this.setImageData(source, target);
      target.setProductURL(tuiCategoryModelUrlResolver.resolve(source));

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
