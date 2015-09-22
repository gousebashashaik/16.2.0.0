/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ImageData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.portaltech.travel.thirdparty.endeca.ResultData;

/**
 * @author omonikhide
 *
 */
public class ProductRangeViewData extends CategoryData implements HasFeatures
{

   private List<BenefitViewData> benefits;

   private List<String> usps;

   private List<ImageData> imageDataList;

   private List<MediaViewData> mediaData;

   private final Map<String, List<Object>> featureCodesAndValues;

   private List<ProductUspViewData> productUsps;

   private List<ResultData> endecaAccommodationFilterList;

   private MediaViewData picture;

   private String pictureUrl;

   private List<MediaViewData> galleryImages;

   private String brandType;

   public ProductRangeViewData()
   {
      this.featureCodesAndValues = new HashMap<String, List<Object>>();
      this.galleryImages = new ArrayList<MediaViewData>();
   }

   /**
    * @return the brandType
    */
   public String getBrandType()
   {
      return brandType;
   }

   /**
    * @param brandType the brandType to set
    */
   public void setBrandType(final String brandType)
   {
      this.brandType = brandType;
   }

   /**
    * @return the endecaAccommodationFilterList
    */
   public List<ResultData> getEndecaAccommodationFilterList()
   {
      return endecaAccommodationFilterList;
   }

   /**
    * @param endecaAccommodationFilterList the endecaAccommodationFilterList to set
    */
   public void setEndecaAccommodationFilterList(final List<ResultData> endecaAccommodationFilterList)
   {
      this.endecaAccommodationFilterList = endecaAccommodationFilterList;
   }

   /**
    * @return the usps
    */
   public List<String> getUsps()
   {
      return usps;
   }

   /**
    * @param usps the usps to set
    */
   public void setUsps(final List<String> usps)
   {
      this.usps = usps;
   }

   public List<BenefitViewData> getBenefits()
   {
      return benefits;
   }

   public void setBenefits(final List<BenefitViewData> benefits)
   {
      this.benefits = benefits;
   }

   /**
    * @return the imageDataList
    */
   public List<ImageData> getImageDataList()
   {
      return imageDataList;
   }

   /**
    * @param imageDataList the imageDataList to set
    */
   public void setImageDataList(final List<ImageData> imageDataList)
   {
      this.imageDataList = imageDataList;
   }

   /**
    * @return the mediaData
    */
   public List<MediaViewData> getMediaData()
   {
      return mediaData;
   }

   /**
    * @param mediaData the mediaData to set
    */
   public void setMediaData(final List<MediaViewData> mediaData)
   {
      this.mediaData = mediaData;
   }

   @Override
   public Map<String, List<Object>> getFeatureCodesAndValues()
   {
      return featureCodesAndValues;
   }

   @Override
   public void putFeatureCodesAndValues(final Map<String, List<Object>> featureCodeAndValues)
   {
      this.featureCodesAndValues.putAll(featureCodeAndValues);

   }

   @Override
   public void putFeatureValue(final String featureCode, final List<Object> featureValues)
   {
      featureCodesAndValues.put(featureCode, featureValues);
   }

   @Override
   public List<Object> getFeatureValues(final String featureCode)
   {
      return featureCodesAndValues.get(featureCode);
   }

   /**
    * @return the productUsps
    */
   public List<ProductUspViewData> getProductUsps()
   {
      return productUsps;
   }

   /**
    * @param productUsps the productUsps to set
    */
   public void setProductUsps(final List<ProductUspViewData> productUsps)
   {
      this.productUsps = productUsps;
   }

   /**
    * @return the pictureUrl
    */
   public String getPictureUrl()
   {
      return pictureUrl;
   }

   /**
    * @param pictureUrl the pictureUrl to set
    */
   public void setPictureUrl(final String pictureUrl)
   {
      this.pictureUrl = pictureUrl;
   }

   public MediaViewData getPicture()
   {
      return picture;
   }

   public void setPicture(final MediaViewData picture)
   {
      this.picture = picture;
   }

   public List<MediaViewData> getGalleryImages()
   {
      return galleryImages;
   }

   public void setGalleryImages(final List<MediaViewData> galleryImages)
   {
      this.galleryImages = galleryImages;
   }

}
