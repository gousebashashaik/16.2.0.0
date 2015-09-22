/**
 *
 */
package uk.co.portaltech.tui.populators;

import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.media.services.RoomMediaService;
import uk.co.portaltech.travel.media.services.domain.Media;
import uk.co.portaltech.travel.model.AccommodationModel;
import uk.co.portaltech.travel.model.RoomModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.resolvers.TUIUrlResolver;
import uk.co.portaltech.tui.web.view.data.AccommodationRoomViewData;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;
import uk.co.portaltech.tui.web.view.data.MediaViewData;

/**
 * @author gagan
 *
 */
public class AccomodationRoomPopulator implements
   Populator<AccommodationModel, AccommodationViewData>
{

   /**
     *
     */
   private static final String UPGRADE = "upgrade";

   /**
     *
     */
   private static final String USPS2 = "usps";

   /**
     *
     */
   private static final String NOMINAL_OCCUPANCY = "nominalOccupancy";

   /**
     *
     */
   private static final String ROOM_TITLE = "roomTitle";

   /**
     *
     */
   private static final String DESCRIPTION = "shortDescription";

   @Resource
   private FeatureService featureService;

   @Resource
   private MediaPopulatorLite mediaPopulatorLite;

   @Resource
   private TUIUrlResolver<AccommodationModel> tuiProductUrlResolver;

   // @Resource

   @Resource
   private RoomMediaService roomMediaService;

   private static final String MIN_OCCUPANCY = "minOccupancy";

   private static final String MAX_OCCUPANCY = "maxOccupancy";

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.Populator#populate(java .lang.Object,
    * java.lang.Object)
    */
   @Override
   public void populate(final AccommodationModel sourceModel, final AccommodationViewData targetData)
      throws ConversionException
   {
      Assert.notNull(sourceModel, "Converter source must not be null");
      Assert.notNull(targetData, "Converter target must not be null");
      final Collection<RoomModel> rooms = sourceModel.getRooms();

      final List<AccommodationRoomViewData> roomsData = new ArrayList<AccommodationRoomViewData>();

      for (final RoomModel roomModel : rooms)
      {
         final AccommodationRoomViewData roomData = new AccommodationRoomViewData();
         roomsData.add(populateDataObject(roomModel, roomData));
      }
      // This method groups all the available rooms into their individual
      // TypeGroups and populates the roomTypeMap
      // which is a global variable

      targetData.setRoomsData(roomsData);

   }

   public AccommodationRoomViewData populateDataObject(final RoomModel sourceModel,
      final AccommodationRoomViewData targetData) throws ConversionException
   {

      final List<String> roomFeaturesList =
         Arrays.asList(new String[] { MIN_OCCUPANCY, MAX_OCCUPANCY, DESCRIPTION, ROOM_TITLE,
            NOMINAL_OCCUPANCY, USPS2, UPGRADE });
      final Map<String, List<Object>> roomfeatures =
         featureService.getOptimizedValuesForFeatures(roomFeaturesList, sourceModel, new Date(),
            null);

      Assert.notNull(sourceModel, "Converter source must not be null");
      Assert.notNull(targetData, "Converter target must not be null");

      if (roomfeatures.get(DESCRIPTION) != null && !roomfeatures.get(DESCRIPTION).isEmpty())
      {
         targetData.setDescription(roomfeatures.get(DESCRIPTION).get(0).toString());
      }
      targetData.setRoomtypeCode(sourceModel.getRoomTypeCode());
      // Setting roomTypeCode for Hybris Content Services

      targetData.setRoomType(sourceModel.getRoomType().toString());

      if (roomfeatures.get(ROOM_TITLE) != null)
      {
         if (roomfeatures.get(ROOM_TITLE) != null && !roomfeatures.get(ROOM_TITLE).isEmpty())
         {
            targetData.setRoomTitle(roomfeatures.get(ROOM_TITLE).get(0).toString().toLowerCase());
         }
      }
      else
      {

         targetData.setRoomTitle(sourceModel.getRoomTypeCode());
      }

      final Map<String, Integer> occuMap = new HashMap<String, Integer>();
      if (roomfeatures.get(MIN_OCCUPANCY) != null && !roomfeatures.get(MIN_OCCUPANCY).isEmpty())
      {
         occuMap.put(MIN_OCCUPANCY,
            Integer.valueOf(roomfeatures.get(MIN_OCCUPANCY).get(0).toString()));
      }

      if (roomfeatures.get(NOMINAL_OCCUPANCY) != null
         && !roomfeatures.get(NOMINAL_OCCUPANCY).isEmpty())
      {
         occuMap.put(NOMINAL_OCCUPANCY,
            Integer.valueOf(roomfeatures.get(NOMINAL_OCCUPANCY).get(0).toString()));
      }

      if (roomfeatures.get(MAX_OCCUPANCY) != null && !roomfeatures.get(MAX_OCCUPANCY).isEmpty())
      {
         occuMap.put(MAX_OCCUPANCY,
            Integer.valueOf(roomfeatures.get(MAX_OCCUPANCY).get(0).toString()));
      }
      targetData.setOccupancy(occuMap);

      final List<Object> usps = roomfeatures.get(USPS2);
      if (usps != null && !usps.isEmpty())
      {
         targetData.setUsps(usps.toArray(new String[usps.size()]));
         targetData.setSharedUsps(Arrays.asList(targetData.getUsps()));
      }

      final List<Object> upgradeLst = roomfeatures.get(UPGRADE);
      if (upgradeLst != null && !upgradeLst.isEmpty())
      {
         targetData.setUpgrade(upgradeLst.toArray(new String[upgradeLst.size()]));
      }

      final List<Media> mediaLists = roomMediaService.getImageMedias(sourceModel);
      if (CollectionUtils.isNotEmpty(mediaLists))
      {
         targetData.setGalleryImages(new ArrayList<MediaViewData>());
         final List<MediaViewData> mediaViewDatas = new ArrayList<MediaViewData>();
         mediaPopulatorLite.populate(mediaLists, mediaViewDatas);
         for (final MediaViewData mediaViewData : mediaViewDatas)
         {
            if (null == mediaViewData.getRoomPlanImages())
            {
               targetData.getGalleryImages().add(mediaViewData);
            }
            else
            {
               targetData.setRoomPlanImage(mediaViewData.getRoomPlanImages());
            }
         }
      }

      tuiProductUrlResolver.setOverrideSubPageType("rooms");
      final String roomsUrl = tuiProductUrlResolver.resolve(sourceModel.getAccommodation());
      targetData.setAccommodationRoomsUrl(roomsUrl);
      return targetData;
   }

}
