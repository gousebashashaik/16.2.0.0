package uk.co.portaltech.tui.converters;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.StopWatch;

import uk.co.portaltech.tui.constants.BaynoteConstants;
import uk.co.portaltech.tui.thirdparty.baynote.client.RecResult;
import uk.co.portaltech.tui.thirdparty.baynote.client.SlotResults;
import uk.co.portaltech.tui.thirdparty.baynote.client.WidgetResults;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.BayNoteTrackingData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;
import uk.co.portaltech.tui.web.view.data.RecommendationsData;
import uk.co.tui.async.logging.TUILogUtils;

public class BaynoteRESTConverter
{

   /**
    * Constant to define Log.
    */
   private static final TUILogUtils LOG = new TUILogUtils("BaynoteRESTConverter");

   /**
    * Instantiates a Jaxb2Marshaller.
    */
   private Jaxb2Marshaller baynoteMarshallerBrowse;

   /**
    * @param baynoteMarshallerBrowse the baynoteMarshallerBrowse to set
    */
   public void setBaynoteMarshallerBrowse(final Jaxb2Marshaller baynoteMarshallerBrowse)
   {
      this.baynoteMarshallerBrowse = baynoteMarshallerBrowse;
   }

   /**
    * Method to convert baynote Response to RecommendationData baynoteMarshallerBrowse unmarshal the
    * baybote response and gives unmarshalled data containign RecResults WidgetResults SlotRec And
    * then populatates all the productId and featured image to recommendation data
    *
    * @param source
    * @param pageType
    * @return
    */
   public RecommendationsData convert(final String source, final String pageType)
   {
      final StopWatch bnProcessTimeout = new StopWatch();
      bnProcessTimeout.start();
      final RecommendationsData recommendationsData = new RecommendationsData();
      final List<AccommodationViewData> avdList = new ArrayList<AccommodationViewData>();
      final List<MediaViewData> imgdata = new ArrayList<MediaViewData>();

      try
      {

         RecResult recResult;
         List<WidgetResults> widgetRes;
         List<SlotResults> slotResList = null;
         String widgetID = null;
         final List<String> productList = new ArrayList<String>();

         if (source != null)
         {

            recResult =
               (RecResult) baynoteMarshallerBrowse.unmarshal(new StreamSource(new StringReader(
                  source)));

            widgetRes = recResult.getWidgetResults();
            widgetID = widgetRes.get(0).getId();

            if (CollectionUtils.isNotEmpty(widgetRes))
            {

               slotResList = widgetRes.get(0).getListOfslotResults();

            }
         }

         populateReccomendationData(pageType, recommendationsData, avdList, imgdata, slotResList,
            widgetID, productList);

         recommendationsData.setAccomodationDatas(avdList);
         recommendationsData.setProductCodes(productList);

      }
      catch (final XmlMappingException ex)
      {
         LOG.error("Error parsing baynote XML data " + ex.getMessage());
      }

      bnProcessTimeout.stop();

      LOG.info("Time took to process baynote response: " + bnProcessTimeout.getTotalTimeMillis()
         + " ms");

      return recommendationsData;
   }

   /**
    * This method populates the baynoteTrackingData and product id details to recommendationData
    *
    * @param pageType
    * @param recommendationsData
    * @param avdList
    * @param imgdata
    * @param slotResList
    * @param widgetID
    * @param productList
    */
   private void populateReccomendationData(final String pageType,
      final RecommendationsData recommendationsData, final List<AccommodationViewData> avdList,
      final List<MediaViewData> imgdata, final List<SlotResults> slotResList,
      final String widgetID, final List<String> productList)
   {
      if (slotResList != null)
      {
         for (final SlotResults res : slotResList)
         {
            final Map<String, String> baynoteTrackingMap = new HashMap<String, String>();

            final AccommodationViewData avd = new AccommodationViewData();
            final BayNoteTrackingData bayNoteTrackingData = new BayNoteTrackingData();
            bayNoteTrackingData.setBaynoteSlot(res.getSlot());
            bayNoteTrackingData.setBaynoteWidget(widgetID);

            baynoteTrackingMap.put("baynote-slot", res.getSlot());
            baynoteTrackingMap.put("baynote-widget", widgetID);
            for (final uk.co.portaltech.tui.thirdparty.baynote.client.Attribute attr : res
               .getListOfattrs())
            {

               populateAccomData(pageType, recommendationsData, imgdata, productList,
                  baynoteTrackingMap, avd, attr, bayNoteTrackingData);
            }
            avd.setBaynoteTrackingData(bayNoteTrackingData);
            avd.setBaynoteTrackingMap(baynoteTrackingMap);

            avdList.add(avd);
         }

      }
   }

   /**
    * Populates the prod id and featured
    *
    * @param pageType
    * @param recommendationsData
    * @param imgdata
    * @param productList
    * @param baynoteTrackingMap
    * @param avd
    * @param attr
    */
   private void populateAccomData(final String pageType,
      final RecommendationsData recommendationsData, final List<MediaViewData> imgdata,
      final List<String> productList, final Map<String, String> baynoteTrackingMap,
      final AccommodationViewData avd,
      final uk.co.portaltech.tui.thirdparty.baynote.client.Attribute attr,
      final BayNoteTrackingData bayNoteTrackingData)
   {
      populateDatafromAttr(recommendationsData, productList, baynoteTrackingMap, attr, avd,
         bayNoteTrackingData);

      if (checkForFeaturedImg(attr))
      {

         String url = attr.getValues();

         if ("HomeRec".equalsIgnoreCase(pageType))
         {

            for (final String u : url.split("/"))
            {
               url = setImgUrl(avd, url, u);
            }
         }

         final MediaViewData mvd = new MediaViewData();
         mvd.setMainSrc(url);
         imgdata.add(mvd);
      }
      avd.setGalleryImages(imgdata);

   }

   private void populateDatafromAttr(final RecommendationsData recommendationsData,
      final List<String> productList, final Map<String, String> baynoteTrackingMap,
      final uk.co.portaltech.tui.thirdparty.baynote.client.Attribute attr,
      final AccommodationViewData avd, final BayNoteTrackingData bayNoteTrackingData)
   {
      if (BaynoteConstants.PROD_ID.equalsIgnoreCase(attr.getName()))
      {
         populateProdId(productList, baynoteTrackingMap, attr, avd, bayNoteTrackingData);

      }
      else if (BaynoteConstants.FEATUREDIMAGE.equalsIgnoreCase(attr.getName()))
      {
         populatefeaturedImage(recommendationsData, attr, avd);

      }
   }

   /**
    * @param attr
    * @return
    */
   private boolean checkForFeaturedImg(
      final uk.co.portaltech.tui.thirdparty.baynote.client.Attribute attr)
   {
      return attr.getName() != null
         && BaynoteConstants.FEATUREDIMAGE.equalsIgnoreCase(attr.getName());
   }

   /**
    * @param avd
    * @param url
    * @param u
    * @return
    */

   private String setImgUrl(final AccommodationViewData avd, String url, final String u)
   {
      if (u.contains(BaynoteConstants.TWOTHREETWOPIXEL))
      {
         url =
            url.replaceFirst(BaynoteConstants.TWOTHREETWOPIXEL,
               BaynoteConstants.FOUREIGHTEIGHTPIXEL);
         avd.setRecommendationImgUrl(url);
      }
      return url;
   }

   /**
    * @param productList
    * @param baynoteTrackingMap
    * @param attr
    */
   private void populateProdId(final List<String> productList,
      final Map<String, String> baynoteTrackingMap,
      final uk.co.portaltech.tui.thirdparty.baynote.client.Attribute attr,
      final AccommodationViewData avd, final BayNoteTrackingData bayNoteTrackingData)
   {
      if (BaynoteConstants.PROD_ID.equalsIgnoreCase(attr.getName()))
      {
         baynoteTrackingMap.put("baynote-pid", attr.getValues());
         productList.add(attr.getValues());
         avd.setCode(attr.getValues());
         bayNoteTrackingData.setBaynotePid(attr.getValues());

      }
   }

   /**
    * @param recommendationsData
    * @param attr
    */

   private void populatefeaturedImage(final RecommendationsData recommendationsData,
      final uk.co.portaltech.tui.thirdparty.baynote.client.Attribute attr,
      final AccommodationViewData avd)
   {
      if (BaynoteConstants.FEATUREDIMAGE.equalsIgnoreCase(attr.getName()))
      {

         recommendationsData.setFeaturedImage(attr.getValues());
         avd.setFeaturedImgUrl(attr.getValues());
      }
   }

}
