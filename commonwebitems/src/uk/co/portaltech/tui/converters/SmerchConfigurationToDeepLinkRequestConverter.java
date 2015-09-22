/**
 *
 */
package uk.co.portaltech.tui.converters;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.commerceservices.converter.impl.AbstractPopulatingConverter;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.util.Assert;

import uk.co.portaltech.commons.DateUtils;
import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.travel.model.results.SmerchConfiguration;
import uk.co.portaltech.tui.helper.DeepLinkHelper;
import uk.co.portaltech.tui.web.view.data.DeepLinkRequestData;
import uk.co.tui.async.logging.TUILogUtils;

/**
 * @author narendra.bm
 *
 */
public class SmerchConfigurationToDeepLinkRequestConverter extends
   AbstractPopulatingConverter<SmerchConfiguration, DeepLinkRequestData>
{

   @Resource
   private CMSComponentService cmsComponentService;

   @Resource(name = "deepLinkHelper")
   private DeepLinkHelper deepLinkHelper;

   private static final String DATE_FORMAT = "dd-MM-yyyy";

   private static final String DATE_FORMAT_1 = "yyyy-MM-dd";

   private static final String COMPONENT_ID = "WF_COM_300";

   private static final TUILogUtils LOG = new TUILogUtils(
      "SmerchConfigurationToDeepLinkRequestConverter");

   /*
    * (non-Javadoc)
    * 
    * @see de.hybris.platform.commerceservices.converter.impl.AbstractConverter#createTarget()
    */
   @Override
   public DeepLinkRequestData createTarget()
   {

      return null;
   }

   @Override
   public void populate(final SmerchConfiguration source, final DeepLinkRequestData target)
   {
      Assert.notNull(source, "Parameter source cannot be null.");
      Assert.notNull(target, "Parameter target cannot be null.");

      String startDate = null;
      String endDate = null;

      final List<Integer> childernAges = deepLinkHelper.getChildernAge(source.getChildAges());

      target.setChildrenAge(childernAges);
      target.setInfantCount(getInfantCount(childernAges));

      startDate = calculateDate(source.getStartDate(), startDate);

      endDate = calculateDate(source.getEndDate(), endDate);
      target.setWhen(StringUtils.isNotEmpty(startDate) ? DateUtils.formatdate(startDate,
         DATE_FORMAT_1, DATE_FORMAT) : StringUtils.EMPTY);

      target.setUntil(StringUtils.isNotEmpty(endDate) ? DateUtils.formatdate(endDate,
         DATE_FORMAT_1, DATE_FORMAT) : StringUtils.EMPTY);

      target.setAirports(deepLinkHelper.getAirportsForInventory(source.getDepartureAirport()));

      target.setNoOfAdults(parseStringToInt(source.getNoOfAdults()));

      target.setChildCount(childernAges.size());

      target.setNoOfChildren(childernAges.size());

      target.setDuration(parseStringToInt(source.getDuration()));

      target.setAppRequest(true);

      target.setUnits(deepLinkHelper.getUnitDataListForSmerch(source.getDestinations()));

      try
      {
         target.setStartDate(DateUtils.getFormattedDateForSmerch(source.getStartDate()));

         target.setEnddate(DateUtils.getFormattedDateForSmerch(source.getEndDate()));

      }
      catch (final ParseException e)
      {
         LOG.error(" Error while parsing start date and end date for smerch");
      }

      target.setSmerchDuration(source.getDuration());

      target.setSelectedBoardBasis(source.getBoardBasis());
   }

   /**
    * @param date
    * @param startDate
    * @return String
    */
   private String calculateDate(final String date, final String startDate)
   {
      if (StringUtils.isNotEmpty(date))
      {
         // Check for date pattern yyyy-MM-dd
         if (DateUtils.verfiyDateFormat(date))
         {
            return date;
         }
         return calculateDateOnDays(date);

      }
      return startDate;
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

   /**
    * Add days to current date
    *
    * @param date
    * @return String
    */
   private String calculateDateOnDays(final String date)
   {
      final DateTime currentDate = new DateTime();
      if (date.contains("+"))
      {

         return DateUtils.addDaysToDate(currentDate, Integer.parseInt(date.substring(1))).toString(
            DATE_FORMAT_1);
      }

      return DateUtils.addDaysToDate(currentDate, Integer.parseInt(date)).toString(DATE_FORMAT_1);

   }

}
