/**
 *
 */
package uk.co.portaltech.tui.facades.impl;

import de.hybris.platform.commercefacades.product.data.ImageData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import uk.co.portaltech.tui.components.model.SocialMediaLinksComponentModel;
import uk.co.portaltech.tui.components.model.SocialMediaModel;
import uk.co.portaltech.tui.facades.AccommodationFacade;
import uk.co.portaltech.tui.facades.SocialMediaFacade;
import uk.co.portaltech.tui.utils.ViewSelector;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.SocialMediaLinksComponentData;
import uk.co.portaltech.tui.web.view.data.SocialMediaViewData;

/**
 * @author venkataharish.k
 *
 */
public class SocialMediaFacadeImpl implements SocialMediaFacade
{

   @Resource
   private ViewSelector viewSelector;

   @Resource
   private AccommodationFacade accomodationFacade;

   @Resource
   private DefaultCarouselFacade carouselFacade;

   @Override
   public SocialMediaLinksComponentData getSocialMedia(
      final SocialMediaLinksComponentModel socialMediaLinksComponentModel,
      final String relativeurl, final String productCode, final String pageType)
   {
      final SocialMediaLinksComponentData socialMedia = new SocialMediaLinksComponentData();
      final List<SocialMediaViewData> socialMediaViewDataList =
         new ArrayList<SocialMediaViewData>();
      final List<SocialMediaModel> socialMediaList = socialMediaLinksComponentModel.getMediaList();
      SocialMediaViewData view;
      for (final SocialMediaModel socialMediaModel : socialMediaList)
      {
         if (viewSelector.checkIsMobile() && !("true").equals(socialMediaModel.getAvailable()))
         {
            continue;
         }
         view = new SocialMediaViewData();
         view.setColor(socialMediaModel.getColor());
         view.setIconUrl(socialMediaModel.getIconURL());
         view.setMediaText(socialMediaModel.getMediaText());
         view.setOnHoverColor(socialMediaModel.getOnHoverColor());
         view.setOnHoverText(socialMediaModel.getOnHoverText());
         StringBuilder mediaurl = null;
         // disabling count access to avoid performance issues
         mediaurl = new StringBuilder(socialMediaModel.getMediaURL());
         view.setMediaUrlForMobile(mediaurl.toString());

         // disabling count access to avoid performance issues
         view.setCounts(0);
         setMediaUrlForDesktop(productCode, pageType, view, socialMediaModel, mediaurl);
         socialMediaViewDataList.add(view);
      }

      socialMedia.setSocialMediaViewDataList(socialMediaViewDataList);

      return socialMedia;

   }

   /**
    * @param productCode
    * @param pageType
    * @param view
    * @param socialMediaModel
    * @param mediaurl
    */
   private void setMediaUrlForDesktop(final String productCode, final String pageType,
      final SocialMediaViewData view, final SocialMediaModel socialMediaModel,
      final StringBuilder mediaurl)
   {
      if ("Pinterest".equalsIgnoreCase(socialMediaModel.getCode()))
      {
         view.setMediaUrl(mediaurl.toString() + setPinterestData(productCode, pageType));
      }
      else
      {
         view.setMediaUrl(mediaurl.toString());

      }
   }

   private String setPinterestData(final String productCode, final String pageType)
   {
      final AccommodationViewData accommodationData =
         carouselFacade.getHeroCarouselMediaDataByProductCode(productCode, 0, 0);

      final AccommodationViewData accommodationdata =
         accomodationFacade.getAccommodationEditorialInfo(productCode, pageType);

      StringBuilder mediaurl = null;
      final String imgUrl = getMetaImageURL(accommodationData.getImages());
      mediaurl =
         new StringBuilder("&media=" + imgUrl + "&description="
            + accommodationdata.getDescription());
      return mediaurl.toString();

   }

   private String getMetaImageURL(final Collection<ImageData> images)
   {
      String metaURL = "";
      if (!images.isEmpty())
      {
         final Iterator<ImageData> iterator = images.iterator();
         while (iterator.hasNext())
         {
            final ImageData imageData = iterator.next();
            metaURL = imageData.getUrl();

            break;
         }

      }
      return metaURL;

   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * uk.co.portaltech.tui.facades.SocialMediaFacade#getSocialMedia(uk.co.portaltech.tui.components
    * .model. SocialMediaLinksComponentModel, java.lang.String)
    */
   @Override
   public SocialMediaLinksComponentData getSocialMedia(
      final SocialMediaLinksComponentModel socialMediaLinksComponentModel, final String relativeurl)
   {
      // YTODO Auto-generated method stub
      return null;
   }

}
