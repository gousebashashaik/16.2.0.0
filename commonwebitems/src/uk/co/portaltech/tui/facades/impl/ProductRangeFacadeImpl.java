/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import uk.co.portaltech.travel.enums.SearchResultType;
import uk.co.portaltech.travel.media.services.ProductRangeMediaService;
import uk.co.portaltech.travel.model.BenefitModel;
import uk.co.portaltech.travel.model.ContentValueModel;
import uk.co.portaltech.travel.model.ProductRangeModel;
import uk.co.portaltech.travel.model.ProductUspModel;
import uk.co.portaltech.travel.services.GenericContentService;
import uk.co.portaltech.travel.services.productrange.MainstreamProductRangeService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.portaltech.tui.facades.CrdToUrlMappingFacade;
import uk.co.portaltech.tui.facades.ProductRangeFacade;
import uk.co.portaltech.tui.populators.MediaPopulatorLite;
import uk.co.portaltech.tui.web.view.data.BenefitViewData;
import uk.co.portaltech.tui.web.view.data.CollectionGroupViewData;
import uk.co.portaltech.tui.web.view.data.CollectionViewData;
import uk.co.portaltech.tui.web.view.data.GenericContentViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeCategoryViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeCollectionViewData;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;
import uk.co.tui.web.common.enums.ProductRangeForCrossSell;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.KeyGenerator;

/**
 * @author omonikhide
 *
 */
public class ProductRangeFacadeImpl implements ProductRangeFacade
{

   private static final String GENERIC_KEY_GENERATOR =
      "uk.co.portaltech.tui.utils.GenericKeyGenerator";

   @Resource
   private Converter<BenefitModel, BenefitViewData> defaultBenefitConverter;

   @Resource
   private Converter<Collection<CategoryModel>, Map<String, ProductRangeCategoryViewData>> defaultProductRangeCollectionConverter;

   @Resource
   private Converter<ProductRangeModel, ProductRangeCollectionViewData> defaultProductRangeProductConverter;

   @Resource
   private Converter<ProductRangeModel, ProductRangeViewData> productRangeConverter;

   private Populator<ProductRangeModel, ProductRangeViewData> productRangeBasicPopulator;

   private Populator<ProductRangeModel, ProductRangeViewData> productRangeHighlightsPopulator;

   private Populator<List<ProductUspModel>, ProductRangeViewData> productRangeUspsPopulator;

   @Resource
   private MainstreamProductRangeService productRangeService;

   @Resource
   private CategoryService categoryService;

   @Resource
   private CMSSiteService cmsSiteService;

   @Resource
   private SessionService sessionService;

   @Resource
   private ProductRangeMediaService productRangeMediaService;

   @Resource
   private MediaPopulatorLite mediaPopulatorLite;

   @Resource
   private Populator<ContentValueModel, GenericContentViewData> genericContentPopulator;

   @Resource
   private GenericContentService genericContentService;

   @Resource
   private CrdToUrlMappingFacade crdToUrlMapFacade;

   @Resource
   private TypeService typeService;

   private static final String[] NAME_SEARCH_ITEM = { "name_search_item" };

   @Required
   public void setProductRangeBasicPopulator(
      final Populator<ProductRangeModel, ProductRangeViewData> productRangeBasicPopulator)
   {
      this.productRangeBasicPopulator = productRangeBasicPopulator;
   }

   @Required
   public void setProductRangeHighlightsPopulator(
      final Populator<ProductRangeModel, ProductRangeViewData> productRangeHighlightsPopulator)
   {
      this.productRangeHighlightsPopulator = productRangeHighlightsPopulator;
   }

   @Required
   public void setProductRangeUspsPopulator(
      final Populator<List<ProductUspModel>, ProductRangeViewData> productRangeUspsPopulator)
   {
      this.productRangeUspsPopulator = productRangeUspsPopulator;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.facades.ProductRangeFacade#getProductRangeFacadeData(java.lang.String)
    */
   @Override
   public ProductRangeViewData getProductRangeBenefitsData(final String productRangeCode)
   {

      final BrandDetails brandDetails =
         sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
      final List<String> brandPKs = brandDetails.getRelevantBrands();
      final List<BenefitModel> productRangeBenefits =
         productRangeService.getProductRangeBenefits(productRangeCode,
            cmsSiteService.getCurrentCatalogVersion(), brandPKs);
      final List<BenefitViewData> benefitData = new ArrayList<BenefitViewData>();
      for (final BenefitModel benefitModel : productRangeBenefits)
      {
         benefitData.add(defaultBenefitConverter.convert(benefitModel));
      }
      final ProductRangeViewData productRangeData = new ProductRangeViewData();
      productRangeData.setBenefits(benefitData);

      return productRangeData;
   }

   /**
    * returns list of Product Ranges(Holiday Collections)
    */
   @Override
   public Map<String, ProductRangeCategoryViewData> getAllProductRanges()
   {

      final BrandDetails brandDetails =
         sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
      final List<String> brandPKs = brandDetails.getRelevantBrands();
      final Map<String, ProductRangeCategoryViewData> productRangeCollection =
         new HashMap<String, ProductRangeCategoryViewData>();
      final Collection<CategoryModel> allProductRangeCollection =
         productRangeService.getAllProductRangeCollection(brandPKs);
      defaultProductRangeCollectionConverter.convert(allProductRangeCollection,
         productRangeCollection);
      return productRangeCollection;
   }

   /*
    * This method returns the Product Range Category Data based on the passed value.
    */
   @Override
   public ProductRangeCategoryViewData getProductRange(final String productRangeCategoryCode)
   {
      ProductRangeCategoryViewData productRangeCategoryViewData = null;
      final Map<String, ProductRangeCategoryViewData> allProductRanges = this.getAllProductRanges();
      if (StringUtils.isNotBlank(productRangeCategoryCode) && allProductRanges != null)
      {
         productRangeCategoryViewData = allProductRanges.get(productRangeCategoryCode);
      }
      return productRangeCategoryViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.facades.ProductRangeFacade#getProductRangeHighlights(java.lang.String,
    * java.lang.Integer)
    */
   @Override
   public ProductRangeViewData getProductRangeHighlights(final String productRangeCode,
      final Integer highlightsNumber)
   {
      final BrandDetails brandDetails =
         sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
      final List<String> brandPKs = brandDetails.getRelevantBrands();
      ProductRangeModel productRangeModel =
         (ProductRangeModel) categoryService.getCategoryForCode(productRangeCode);
      productRangeModel = getProductRangeModelFilteredByBrand(productRangeModel, brandPKs);
      final ProductRangeViewData productRangeViewData =
         productRangeConverter.convert(productRangeModel);
      productRangeHighlightsPopulator.populate(productRangeModel, productRangeViewData);
      if (highlightsNumber != null)
      {
         final int highlightsSize = highlightsNumber.intValue();
         final List<Object> list = productRangeViewData.getFeatureValues("usps");
         if (list != null && list.size() > highlightsSize)
         {
            productRangeViewData.putFeatureValue("usps", list.subList(0, highlightsSize));
         }
      }
      return productRangeViewData;
   }

   @Override
   public ProductRangeCollectionViewData getProduct(final String productRangeCode)
   {

      if (StringUtils.isNotBlank(productRangeCode))
      {
         final BrandDetails brandDetails =
            sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
         final List<String> brandPKs = brandDetails.getRelevantBrands();
         final ProductRangeModel productForProductRange =
            productRangeService.getProductForProductRange(productRangeCode,
               cmsSiteService.getCurrentCatalogVersion(), brandPKs);
         final ProductRangeCollectionViewData viewData =
            defaultProductRangeProductConverter.convert(productForProductRange);

         viewData.getGalleryImages().addAll(getMediaViewDatas(productForProductRange));
         return viewData;
      }
      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.ProductRangeFacade#getProductRangeUsps(java.lang.String)
    */
   @Override
   public ProductRangeViewData getProductRangeUsps(final String productRangeCode)
   {

      final BrandDetails brandDetails =
         sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
      final List<String> brandPKs = brandDetails.getRelevantBrands();
      ProductRangeModel productRangeModel =
         (ProductRangeModel) categoryService.getCategoryForCode(productRangeCode);
      productRangeModel = getProductRangeModelFilteredByBrand(productRangeModel, brandPKs);
      final List<ProductUspModel> productUspList =
         productRangeService.getProductRangeUsps(productRangeCode,
            cmsSiteService.getCurrentCatalogVersion(), brandPKs);
      final ProductRangeViewData productRangeViewData =
         productRangeConverter.convert(productRangeModel);
      productRangeBasicPopulator.populate(productRangeModel, productRangeViewData);
      productRangeUspsPopulator.populate(productUspList, productRangeViewData);
      return productRangeViewData;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.ProductRangeFacade#getProductRangeByCode(java.lang.String)
    */
   @Override
   @Cacheable(cacheName = "productRangeByCodeCache", keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
   public ProductRangeViewData getProductRangeByCode(final String productRangeCode)
   {
      final BrandDetails brandDetails =
         sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
      final List<String> brandPKs = brandDetails.getRelevantBrands();
      ProductRangeModel productRangeModel =
         (ProductRangeModel) categoryService.getCategoryForCode(productRangeCode);
      productRangeModel = getProductRangeModelFilteredByBrand(productRangeModel, brandPKs);
      final ProductRangeViewData productRangeViewData =
         productRangeConverter.convert(productRangeModel);
      productRangeBasicPopulator.populate(productRangeModel, productRangeViewData);
      productRangeHighlightsPopulator.populate(productRangeModel, productRangeViewData);

      productRangeViewData.getGalleryImages().addAll(getMediaViewDatas(productRangeModel));

      return productRangeViewData;
   }

   @Override
   @Cacheable(cacheName = "productRangeByBrand", keyGenerator = @KeyGenerator(name = GENERIC_KEY_GENERATOR))
   public List<ProductRangeViewData> getProductRangesByBrand(final String brand)
   {
      final List<ProductRangeViewData> productRanges = new ArrayList<ProductRangeViewData>();
      final List<ProductRangeModel> listOfProdRanges =
         productRangeService.getAllProductRanges(cmsSiteService.getCurrentCatalogVersion(),
            Arrays.asList(brand));
      for (final ProductRangeModel productRange : listOfProdRanges)
      {
         if (!("Yes".equalsIgnoreCase(getNameSearchItemValue(productRange.getCode()))))
         {
            final ProductRangeViewData productRangeViewData =
               productRangeConverter.convert(productRange);
            productRangeBasicPopulator.populate(productRange, productRangeViewData);
            productRangeUspsPopulator.populate(productRange.getProductUsps(), productRangeViewData);

            // Fetching the product range URL from CRDToURLMap
            final String url =
               crdToUrlMapFacade.getUrlForCRD(productRange.getCode(), typeService
                  .getEnumerationValue("SearchResultType", SearchResultType.PRODUCTRANGE.getCode())
                  .getPk().toString(), typeService.getEnumerationValue("BrandType", brand).getPk()
                  .toString());

            productRangeViewData.setUrl(url);
            productRanges.add(productRangeViewData);
         }
      }
      return productRanges;
   }

   /**
    * @param productRangeModel
    * @return mediaDatalist
    */
   private List<MediaViewData> getMediaViewDatas(final ProductRangeModel productRangeModel)
   {
      final ArrayList<MediaViewData> mediaDatalist = new ArrayList<MediaViewData>();
      mediaPopulatorLite.populate(productRangeMediaService.getImageMedias(productRangeModel),
         mediaDatalist);
      return mediaDatalist;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.portaltech.tui.facades.ProductRangeFacade#getProductRanges(java.util.List)
    */
   @Override
   public List<ProductRangeViewData> getProductRanges(
      final List<ProductRangeForCrossSell> productRangeCodes)
   {

      final BrandDetails brandDetails =
         sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
      final List<String> brandPKs = brandDetails.getRelevantBrands();

      ProductRangeViewData productRangeViewData = null;
      final List<ProductRangeViewData> productRangeViewDataList =
         new ArrayList<ProductRangeViewData>();
      for (final ProductRangeForCrossSell productRangeForCrossSell : productRangeCodes)
      {

         final ProductRangeModel productRangeModel =
            (ProductRangeModel) categoryService.getCategoryForCode(productRangeForCrossSell
               .getCode());
         final List<ProductUspModel> productUspList =
            productRangeService.getProductRangeUsps(productRangeForCrossSell.getCode(),
               cmsSiteService.getCurrentCatalogVersion(), brandPKs);
         productRangeViewData = productRangeConverter.convert(productRangeModel);
         productRangeBasicPopulator.populate(productRangeModel, productRangeViewData);
         productRangeUspsPopulator.populate(productUspList, productRangeViewData);
         productRangeViewData.getGalleryImages().addAll(getMediaViewDatas(productRangeModel));
         productRangeViewDataList.add(productRangeViewData);
      }

      return productRangeViewDataList;

   }

   /*
    * Method is used to filter the ProductRangeModel by brand types
    */
   private ProductRangeModel getProductRangeModelFilteredByBrand(
      final ProductRangeModel productRangeModel, final List<String> brandTypePKs)
   {
      ProductRangeModel prodRangeModel = null;
      if ((CollectionUtils.isNotEmpty(brandTypePKs)) && (null != productRangeModel))
      {
         final List<ProductRangeModel> productRangeModels = new ArrayList<ProductRangeModel>();
         productRangeModels.add(productRangeModel);
         productRangeService.getProductRangeModelsFilteredByBrand(productRangeModels, brandTypePKs);
         prodRangeModel =
            CollectionUtils.isNotEmpty(productRangeModels) ? productRangeModels.get(0) : null;
      }
      return prodRangeModel;
   }

   /**
    * @param collectionList
    * @return list collection group data
    */
   @Override
   public List<CollectionViewData> getAllCollectionData(
      final List<CollectionGroupViewData> collectionList)
   {
      final List<CollectionViewData> allCollectionList = new ArrayList<CollectionViewData>();
      if (collectionList != null && CollectionUtils.isNotEmpty(collectionList))
      {
         for (final CollectionGroupViewData data : collectionList)
         {
            final List<CollectionViewData> collectionData = data.getChildren();
            getCollectionList(collectionData, allCollectionList);
         }
      }
      return allCollectionList;

   }

   /**
    * @param collectionData
    * @param allCollectionList
    */
   private void getCollectionList(final List<CollectionViewData> collectionData,
      final List<CollectionViewData> allCollectionList)
   {

      if (CollectionUtils.isNotEmpty(collectionData))
      {
         for (final CollectionViewData productdata : collectionData)
         {
            allCollectionList.add(productdata);

         }
      }
   }

   private String getNameSearchItemValue(final String code)
   {

      List<ContentValueModel> contentValueModels = null;
      GenericContentViewData contentViewData = null;
      String isNamesearchitem = "";
      contentValueModels =
         genericContentService.getContentValue(code, Arrays.asList(NAME_SEARCH_ITEM));
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
