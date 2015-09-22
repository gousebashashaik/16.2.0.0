/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.tui.helper.DeepLinkHelper;
import uk.co.portaltech.tui.web.view.data.DeepLinkRequestData;
import uk.co.portaltech.tui.web.view.data.RoomAllocation;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;

/**
 * @author laxmibai.p Class is to convert Http request object to deepLinkRequest Object
 *
 */
public class HttpRequestToDeepLinkRequestConverter extends
   AbstractPopulatingConverter<HttpServletRequest, DeepLinkRequestData>
{

   /**
     *
     */
   private static final String ISCAPE = "iscape";

   @Resource
   private CMSComponentService cmsComponentService;

   @Resource(name = "deepLinkHelper")
   private DeepLinkHelper deepLinkHelper;

   private static final String COMPONENT_ID = "WF_COM_300";

   private static final String FROM = "airports[]";

   private static final String WHERE_TO = "units[]";

   private static final String IS_FLEXIBLE = "flexibility";

   private static final String FLEXIBLE_DAYS = "flexibleDays";

   private static final String DURATION = "duration";

   private static final String FIRST = "first";

   private static final String LAST = "last";

   private static final String OFFSET = "offset";

   private static final String ADULTS = "noOfAdults";

   private static final String SENIORS = "noOfSeniors";

   private static final String CHILDREN = "noOfChildren";

   private static final String CHILDREN_AGE = "childrenAge";

   private static final String INFANT_COUNT = "infantCount";

   private static final String CHILD_COUNT = "childCount";

   private static final String UNTIL = "until";

   private static final String WHEN = "when";

   private static final String SORTBY = "sortBy";

   private static final String PACKAGE_ID = "packageId";

   private static final String ACCOM_CODE = "productCode";

   private static final String SEARCH_REQUEST_TYPE = "searchRequestType";

   private static final String SEARCH_PANEL = "sp";

   private static final String ISCAPE_REQUEST = "requestFrom";

   private static final String MULTI_SELECT = "multiSelect";

   private static final String FLIGHT_OPTIONS = "flightOptions";

   private static final String BOARD_BASIS = "boardBasisCode";

   private static final String BRAND_TYPE = "brandType";

   private static final String ROOMS = "room";

   private static final String MODIFIED_DURATION = "modifiedDuration";

   private static final String SEARCHTYPE = "searchType";

   private static final String PDD = "pdd";

   private static final TUILogUtils LOG = new TUILogUtils("HttpRequestToDeepLinkRequestConverter");

   @Override
   public DeepLinkRequestData createTarget()
   {
      return new DeepLinkRequestData();
   }

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter# createTarget()
    */

   @Override
   public void populate(final HttpServletRequest source, final DeepLinkRequestData target)
   {

      Assert.notNull(source, "Parameter source cannot be null.");
      Assert.notNull(target, "Parameter target cannot be null.");

      final List<Integer> childernAges =
         deepLinkHelper.getChildernAge(source.getParameter(CHILDREN_AGE));

      target.setBrandType(StringUtils.isNotBlank(source.getParameter(BRAND_TYPE)) ? source
         .getParameter(BRAND_TYPE) : StringUtils.EMPTY);
      target.setChildrenAge(childernAges);
      target.setInfantCount(getInfantCount(childernAges));
      target.setFirst(parseStringToInt(source.getParameter(FIRST)));
      target.setLast(parseStringToInt(source.getParameter(LAST)));

      target.setFlexibleDays(parseStringToInt(source.getParameter(FLEXIBLE_DAYS)));

      target.setNoOfAdults(parseStringToInt(source.getParameter(ADULTS)));
      target.setNoOfChildren(parseStringToInt(source.getParameter(CHILDREN)));
      target.setNoOfSeniors(parseStringToInt(source.getParameter(SENIORS)));

      target.setUntil(StringUtils.isNotEmpty(source.getParameter(UNTIL)) ? source
         .getParameter(UNTIL) : StringUtils.EMPTY);

      target.setWhen(StringUtils.isNotEmpty(source.getParameter(WHEN)) ? source.getParameter(WHEN)
         : StringUtils.EMPTY);

      target.setFlexibility(BooleanUtils.toBoolean(source.getParameter(IS_FLEXIBLE)));

      target.setUnits(deepLinkHelper.getUnits(source.getParameter(WHERE_TO),
         source.getParameter("multiSelect"), target.getBrandType()));
      target.setAirports(deepLinkHelper.getAirports(source.getParameter(FROM)));
      target.setDuration(parseStringToInt(source.getParameter(DURATION)));

      target.setSortBy(StringUtils.isNotEmpty(source.getParameter(SORTBY)) ? source
         .getParameter(SORTBY) : null);

      target.setInfantCount(parseStringToInt(source.getParameter(INFANT_COUNT)));
      target.setChildCount(parseStringToInt(source.getParameter(CHILD_COUNT)));
      target.setOffset(parseStringToInt(source.getParameter(OFFSET)));

      target.setPackageId(StringUtils.isNotEmpty(source.getParameter(PACKAGE_ID)) ? source
         .getParameter(PACKAGE_ID) : null);

      target.setProductCode(StringUtils.isNotEmpty(source.getParameter(ACCOM_CODE)) ? source
         .getParameter(ACCOM_CODE) : null);

      target.setSearchRequestType(StringUtils.isNotEmpty(source.getParameter(SEARCH_REQUEST_TYPE))
         ? source.getParameter(SEARCH_REQUEST_TYPE) : null);

      target.setMultiSelect(StringUtils.isNotEmpty(source.getParameter(MULTI_SELECT)) ? source
         .getParameter(MULTI_SELECT) : null);

      target
         .setAppRequest(StringUtils.isNotEmpty(source.getParameter(SEARCH_PANEL)) ? true : false);

      target.setIscapeRequest(isIscapeReqeust(source) ? true : false);

      target.setFlightOptions(isFlightOptionsRequest(source) ? true : false);

      target.setSelectedBoardBasis(StringUtils.isNotBlank(source.getParameter(BOARD_BASIS))
         ? source.getParameter(BOARD_BASIS) : StringUtils.EMPTY);
      target.setRooms((List<RoomAllocation>) ((StringUtils.isNotEmpty(source.getParameter(ROOMS))
         ? getRoomsData(source.getParameter(ROOMS)) : Collections.emptyList())));
      target.setModifiedDuration(parseStringToInt(source.getParameter(MODIFIED_DURATION)));

      target.setSearchType(source.getParameter(SEARCHTYPE));

      target.setPdd(StringUtils.isNotBlank(source.getParameter(PDD)) ? source.getParameter(PDD)
         : null);

   }

   /**
    * @param parameter
    * @return List
    */
   private List<RoomAllocation> getRoomsData(final String parameter)
   {

      final List<RoomAllocation> rooms = new ArrayList<RoomAllocation>();

      for (final String room : StringUtils.split(parameter, "-"))
      {
         if (StringUtils.isNotEmpty(room))
         {
            final String[] roomData = StringUtils.split(room, "|");
            if (roomData.length >= CommonwebitemsConstants.FIVE)
            {
               final RoomAllocation roomAllocation = new RoomAllocation();
               roomAllocation.setId(parseStringToInt(roomData[CommonwebitemsConstants.ZERO]));
               roomAllocation
                  .setNoOfAdults(parseStringToInt(roomData[CommonwebitemsConstants.ONE]));
               roomAllocation
                  .setNoOfSeniors(parseStringToInt(roomData[CommonwebitemsConstants.TWO]));
               roomAllocation
                  .setNoOfChildren(parseStringToInt(roomData[CommonwebitemsConstants.THREE]));
               roomAllocation
                  .setInfantCount(parseStringToInt(roomData[CommonwebitemsConstants.FOUR]));

               if (roomData.length > CommonwebitemsConstants.FIVE)
               {
                  roomAllocation.setChildrenAge(deepLinkHelper
                     .getChildernAge(roomData[CommonwebitemsConstants.FIVE]));
               }
               rooms.add(roomAllocation);
            }
         }
      }
      return rooms;
   }

   /**
    * @param source
    * @return boolean
    */
   private boolean isFlightOptionsRequest(final HttpServletRequest source)
   {
      return StringUtils.isNotBlank(source.getParameter(FLIGHT_OPTIONS))
         && StringUtils.equalsIgnoreCase(Boolean.TRUE.toString(),
            source.getParameter(FLIGHT_OPTIONS));
   }

   /**
    * @param source
    * @return boolean
    */
   private boolean isIscapeReqeust(final HttpServletRequest source)
   {
      return StringUtils.isNotEmpty(source.getParameter(ISCAPE_REQUEST))
         && StringUtils.equalsIgnoreCase(source.getParameter(ISCAPE_REQUEST), ISCAPE);
   }

   @SuppressWarnings("boxing")
   private int getInfantCount(final List<Integer> childrenAge)
   {
      SearchPanelComponentModel component = null;
      int infantCount = 0;

      try
      {
         component = cmsComponentService.getAbstractCMSComponent(COMPONENT_ID);
      }
      catch (final CMSItemNotFoundException e)
      {
         LOG.error("Failed to render search panel component", e);
      }

      if (CollectionUtils.isNotEmpty(childrenAge)
         && !(Collections.min(childrenAge) > component.getInfantAge()))
      {
         for (final int age : childrenAge)
         {
            if (age <= component.getInfantAge())
            {
               infantCount++;
            }
         }
      }

      return infantCount;
   }

   private int parseStringToInt(final String value)
   {
      if (StringUtils.isNumeric(value) && StringUtils.isNotEmpty(value))
      {
         return Integer.parseInt(value);
      }
      return 0;
   }
}