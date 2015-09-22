/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.ContentValueModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.model.ProductUspModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.travel.services.GenericContentService;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.GenericContentViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;
import uk.co.travel.daos.util.BrandUtils;


/**
 * @author gagan
 *
 */
public class AccommodationProductRangePopulator implements Populator<AccommodationModel, AccommodationViewData>
{

   @Resource
   private Converter<ProductRangeModel, ProductRangeViewData> productRangeConverter;

   @Resource
   private TUIUrlResolver<CategoryModel> tuiCategoryModelUrlResolver;

   private Populator<Collection<MediaContainerModel>, List<ImageData>> productRangeImageDataPopulator;

   private Populator<List<ProductUspModel>, ProductRangeViewData> productRangeUspsPopulator;

   @Resource
   private Converter<MediaModel, MediaViewData> mediaModelConverter;

   private Populator<MediaModel, MediaViewData> mediaViewDataPopulator;

   @Resource
   private FeatureService featureService;

   @Resource
   private TuiUtilityService tuiUtilityService;

   @Resource
   private BrandUtils brandUtils;

   @Resource
   private Populator<ContentValueModel, GenericContentViewData> genericContentPopulator;

   @Resource
   private GenericContentService genericContentService;

   private static final String[] NAME_SEARCH_ITEM =
   { "name_search_item" };

   /*
    * (non-Javadoc)
    *
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
    */
   @Override
   public void populate(final AccommodationModel source, final AccommodationViewData target) throws ConversionException
   {
      Collection<CategoryModel> supercategories = source.getSupercategories();
      if (supercategories != null && !supercategories.isEmpty())
      {
         if (StringUtils.equalsIgnoreCase("FJ", tuiUtilityService.getSiteBrand()))
         {
            supercategories = removeNotRequiredSuperCategoriesForFalcon(supercategories, target);
         }
         else
         {
            supercategories = removeNotRequiredSuperCategories(supercategories, tuiUtilityService.getSiteBrand());
         }

         final List<ProductRangeViewData> productRanges = new ArrayList<ProductRangeViewData>();
         for (final CategoryModel categoryModel : supercategories)
         {
            // get Product Usp associated to product range
            final ProductRangeModel productRangeModel = (ProductRangeModel) categoryModel;

            if (!("Yes".equalsIgnoreCase(getNameSearchItemValue(productRangeModel.getCode()))))
            {
               final ProductRangeViewData productRangeViewData = productRangeConverter.convert(productRangeModel);
               final String brandType = brandUtils.getFeatureServiceBrand(productRangeModel.getBrands());
               productRangeViewData.setCode(productRangeModel.getCode());
               productRangeViewData.setBrandType(brandType);
               productRangeUspsPopulator.populate(productRangeModel.getProductUsps(), productRangeViewData);
               final String targetDataURL = tuiCategoryModelUrlResolver.resolve(productRangeModel);
               productRangeViewData.setUrl(targetDataURL);
               productRangeViewData.putFeatureCodesAndValues(featureService.getOptimizedValuesForFeatures(
                     Arrays.asList(new String[]
                     { "strapline", "name", "mini_strapline" }), productRangeModel, new Date(), null));
               productRanges.add(productRangeViewData);
               setImageData(productRangeModel, productRangeViewData);
            }
         }
         target.setProductRanges(productRanges);
      }

   }

   /**
    * @param productRangeModel
    * @param productRangeViewData
    */
   private void setImageData(final ProductRangeModel productRangeModel, final ProductRangeViewData productRangeViewData)
   {
      final Collection<MediaContainerModel> mediaContainers = productRangeModel.getGalleryImages();
      if (CollectionUtils.isNotEmpty(mediaContainers))
      {
         final List<ImageData> imageDataList = new ArrayList<ImageData>();
         productRangeImageDataPopulator.populate(mediaContainers, imageDataList);
         productRangeViewData.setImageDataList(imageDataList);
      }

      final Collection<MediaModel> primaryImages = productRangeModel.getMedias();
      if (CollectionUtils.isNotEmpty(primaryImages))
      {
         final List<MediaViewData> primaryImageViewDataList = new ArrayList<MediaViewData>();
         for (final MediaModel primaryImage : primaryImages)
         {
            final MediaViewData primaryImageViewData = mediaModelConverter.convert(primaryImage);
            mediaViewDataPopulator.populate(primaryImage, primaryImageViewData);
            primaryImageViewDataList.add(primaryImageViewData);
         }
         productRangeViewData.setMediaData(primaryImageViewDataList);
      }

   }

   /**
    * @param supercategories
    */
   private List<CategoryModel> removeNotRequiredSuperCategoriesForFalcon(final Collection<CategoryModel> supercategories,
         final AccommodationViewData target)
   {
      final List<CategoryModel> productRangeModels = new ArrayList<CategoryModel>();

      final String featureValue = target.getFeatureValue("falcon_product_override");
      if (!StringUtils.equalsIgnoreCase(featureValue, "ZZZZZZ"))
      {
         final Iterator<CategoryModel> itr = supercategories.iterator();
         while (itr.hasNext())
         {
            final CategoryModel categoryModel = itr.next();
            if (categoryModel instanceof ProductRangeModel
                  && BrandUtils.isValidBrandCode(categoryModel.getBrands(), target.getBrand()))
            {
               productRangeModels.add(categoryModel);
            }
         }
      }
      return productRangeModels;
   }

   /**
    * @param supercategories
    */
   private List<CategoryModel> removeNotRequiredSuperCategories(final Collection<CategoryModel> supercategories,
         final String brandType)
   {
      String cpy = brandType;
      if (brandType.equalsIgnoreCase(BrandType.CR.toString()))
      {
         cpy = BrandType.TH.toString();
      }
      final List<CategoryModel> productRangeModels = new ArrayList<CategoryModel>();
      final Iterator<CategoryModel> itr = supercategories.iterator();
      while (itr.hasNext())
      {
         final CategoryModel categoryModel = itr.next();
         if (categoryModel instanceof ProductRangeModel && BrandUtils.isValidBrandCode(categoryModel.getBrands(), cpy))
         {
            productRangeModels.add(categoryModel);
            // commented break so as to add remaining product ranges.
            // For ex: nile legacy falls under two product ranges NCR and COU. where NCR is not
            // exactly productRange.
            // It has a different meaning. So, Nile legacy requires COU too.

            /*
             * this loop breaks as soon as first product range is found associated with current accommodation, in future
             * if we need to display more than one product ranges associated with current accommodation, then just
             * delete the break statement, corresponding JSP already handles it.
             */
         }
      }
      return productRangeModels;
   }

   /**
    * @return the productRangeImageDataPopulator
    */
   public Populator<Collection<MediaContainerModel>, List<ImageData>> getProductRangeImageDataPopulator()
   {
      return productRangeImageDataPopulator;
   }

   /**
    * @param productRangeImageDataPopulator
    *           the productRangeImageDataPopulator to set
    */
   public void setProductRangeImageDataPopulator(
         final Populator<Collection<MediaContainerModel>, List<ImageData>> productRangeImageDataPopulator)
   {
      this.productRangeImageDataPopulator = productRangeImageDataPopulator;
   }

   /**
    * @return the mediaViewDataPopulator
    */
   public Populator<MediaModel, MediaViewData> getMediaViewDataPopulator()
   {
      return mediaViewDataPopulator;
   }

   /**
    * @param mediaViewDataPopulator
    *           the mediaViewDataPopulator to set
    */
   public void setMediaViewDataPopulator(final Populator<MediaModel, MediaViewData> mediaViewDataPopulator)
   {
      this.mediaViewDataPopulator = mediaViewDataPopulator;
   }

   public void setProductRangeUspsPopulator(final Populator<List<ProductUspModel>, ProductRangeViewData> productRangeUspsPopulator)
   {
      this.productRangeUspsPopulator = productRangeUspsPopulator;
   }

   private String getNameSearchItemValue(final String code)
   {

      List<ContentValueModel> contentValueModels = null;
      GenericContentViewData contentViewData = null;
      String isNamesearchitem = "";
      contentValueModels = genericContentService.getContentValue(code, Arrays.asList(NAME_SEARCH_ITEM));
      if (contentValueModels != null && CollectionUtils.isNotEmpty(contentValueModels))
      {
         contentViewData = new GenericContentViewData();
         // populate ContentValueModel's value.
         genericContentPopulator.populate(contentValueModels.get(0), contentViewData);
         isNamesearchitem = contentViewData.getContent();
      }

      return isNamesearchitem;
   }

}
