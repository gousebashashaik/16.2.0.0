/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.converter.Converters;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.tui.web.view.data.CMSLinkComponentViewData;

/**
 * @author gagan
 *
 */
public class CMSLinkComponentPopulator implements
   Populator<CMSLinkComponentModel, CMSLinkComponentViewData>
{

   @Resource
   private Converter<CategoryModel, CategoryData> categoryConverter;

   @Resource
   private Converter<ProductModel, ProductData> productConverter;

   private ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator;

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final CMSLinkComponentModel source, final CMSLinkComponentViewData target)
      throws ConversionException
   {

      final CategoryModel categoryModel = source.getCategory();
      if (categoryModel != null)
      {
         final CategoryData categoryData = getCategoryData(categoryModel);
         target.setCategory(categoryData);
      }

      final String languageCode = "en";
      Locale locale = null;
      if (StringUtils.isNotBlank(languageCode))
      {
         locale = new Locale(languageCode);
      }

      target.setExternal(source.isExternal());
      target.setLinkName(source.getLinkName(locale));

      final ProductModel productModel = source.getProduct();
      if (productModel != null)
      {
         final ProductData productData = getProductData(productModel);
         target.setProduct(productData);
      }

      target.setUrl(source.getUrl());
      target.setTarget(source.getTarget());
      target.setUid(source.getUid());

   }

   /**
    * @return the productConfiguredPopulator
    */
   public ConfigurablePopulator<ProductModel, ProductData, ProductOption> getProductConfiguredPopulator()
   {
      return productConfiguredPopulator;
   }

   /**
    * @param productConfiguredPopulator the productConfiguredPopulator to set
    */
   public void setProductConfiguredPopulator(
      final ConfigurablePopulator<ProductModel, ProductData, ProductOption> productConfiguredPopulator)
   {
      this.productConfiguredPopulator = productConfiguredPopulator;
   }

   /**
    * @param productModel
    * @return
    */
   private ProductData getProductData(final ProductModel productModel)
   {
      final ProductData productData = productConverter.convert(productModel);
      productConfiguredPopulator.populate(productModel, productData,
         Arrays.asList(ProductOption.BASIC));
      return productData;
   }

   /**
    * @param categoryModel
    */
   private CategoryData getCategoryData(final CategoryModel categoryModel)
   {
      final List<CategoryModel> categoryModelList = new ArrayList<CategoryModel>();
      categoryModelList.add(categoryModel);
      final List<CategoryData> categoryDataList =
         Converters.convertAll(categoryModelList, categoryConverter);
      return categoryDataList.get(0);

   }

}
