/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.ProductUspModel;
import uk.co.portaltech.tui.web.view.data.ProductRangeViewData;
import uk.co.portaltech.tui.web.view.data.ProductUspViewData;


/**
 * @author s.consolino
 *
 */
public class ProductRangeUspsPopulator implements Populator<List<ProductUspModel>, ProductRangeViewData>
{

   private Populator<Collection<MediaContainerModel>, List<ImageData>> productUspImageDataPopulator;

   @Override
   public void populate(final List<ProductUspModel> source, final ProductRangeViewData target) throws ConversionException
   {
      Assert.notNull(source, "Converter source must not be null");
      Assert.notNull(target, "Converter target must not be null");
      final List<ProductUspViewData> productUsps = new ArrayList<ProductUspViewData>();

      // Condition added to remove the PLT villa usp for Falcon Ref. Bugzilla Observation 140735
      if (StringUtils.equalsIgnoreCase(target.getBrandType(), "TI") && StringUtils.equalsIgnoreCase(target.getCode(), "PLT"))
      {
         filterPlatinumUspsForFalcon(source, productUsps);
      }
      else
      {
         addProductRangeUsps(source, productUsps);
      }

      target.setProductUsps(productUsps);
   }

   /**
    * @param source
    * @param productUsps
    */
   private void addProductRangeUsps(final List<ProductUspModel> source, final List<ProductUspViewData> productUsps)
   {
      for (final ProductUspModel productUspModel : source)
      {
         final ProductUspViewData productUspViewData = new ProductUspViewData();
         productUspViewData.setCode(productUspModel.getCode());
         productUspViewData.setName(productUspModel.getName());
         productUsps.add(productUspViewData);
      }
   }

   /**
    * @param source
    * @param productUsps
    */
   private void filterPlatinumUspsForFalcon(final List<ProductUspModel> source, final List<ProductUspViewData> productUsps)
   {
      for (final ProductUspModel productUspModel : source)
      {
         if (!StringUtils.equalsIgnoreCase(productUspModel.getCode(), "l31352_plt_usp3")
               && !StringUtils.equalsIgnoreCase(productUspModel.getCode(), "l31352_plt_usp4"))
         {
            final ProductUspViewData productUspViewData = new ProductUspViewData();
            productUspViewData.setCode(productUspModel.getCode());
            productUspViewData.setName(productUspModel.getName());
            productUsps.add(productUspViewData);
         }
      }
   }

   /**
    * @return the productUspImageDataPopulator
    */
   public Populator<Collection<MediaContainerModel>, List<ImageData>> getProductUspImageDataPopulator()
   {
      return productUspImageDataPopulator;
   }

   /**
    * @param productUspImageDataPopulator
    *           the productUspImageDataPopulator to set
    */
   public void setProductUspImageDataPopulator(
         final Populator<Collection<MediaContainerModel>, List<ImageData>> productUspImageDataPopulator)
   {
      this.productUspImageDataPopulator = productUspImageDataPopulator;
   }
}
