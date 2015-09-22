/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2012 hybris AG All rights reserved.
 * 
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with hybris.
 */
package uk.co.portaltech.tui.resolvers;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commerceservices.url.impl.AbstractUrlResolver;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.tui.components.model.AccommodationPageModel;
import uk.co.portaltech.tui.components.model.LocationPageModel;
import uk.co.portaltech.tui.components.model.ProductRangeCategoryPageModel;
import uk.co.portaltech.tui.components.model.ProductRangePageModel;
import uk.co.tui.web.common.enums.CategoryType;

/**
 *
 */
public class TuiPageModelUrlResolver extends AbstractUrlResolver<AbstractPageModel>
{

   private static final String DEFAULT_COUNTRY_CODE = "ESP";

   private static final String DEFAULT_REGION_CODE = "002501";

   private static final String DEFAULT_DESTINATION_CODE = "000347";

   private static final String DEFAULT_RESORT_CODE = "000349";

   private static final String DEFAULT_ACCOMMODATION_CODE = "013499";

   private static final String DEFAULT_ATTRACTION_CODE = "980353";

   private static final String DEFAULT_PRODUCTRANGECATEGORY_CODE = "familyholidays";

   private static final String DEFAULT_PRODUCTRANGE_CODE = "FHV";

   private static final String SOME_SEO_STUFF = "/someSEOstuff-";

   /*
    * I believe this class may now be defunct. It looks like it is a remnant of a test harness from
    * an early development phase.
    */
   @Override
   public String resolve(final AbstractPageModel source)
   {
      final String itemType = source.getItemtype();
      if (("LocationPage").equals(itemType))
      {
         return resolveLocationPage(source);
      }
      else if (("AccommodationPage").equals(itemType))
      {
         return "/accommodation/" + ((AccommodationPageModel) source).getPageType().getCode()
            + SOME_SEO_STUFF + DEFAULT_ACCOMMODATION_CODE;
      }
      else if (("AttractionPage").equals(itemType))
      {
         return "/holiday/attraction/someSEOstuff-" + DEFAULT_ATTRACTION_CODE;
      }
      else if (("ContentPage").equals(itemType))
      {
         return "/content/" + ((ContentPageModel) source).getLabel();
      }
      else if (("ProductRangeCategoryPage").equals(itemType))
      {
         return "/location/" + ((ProductRangeCategoryPageModel) source).getPageType().getCode()
            + SOME_SEO_STUFF + DEFAULT_PRODUCTRANGECATEGORY_CODE;
      }
      else if (("ProductRangePage").equals(itemType))
      {
         return "/location/" + ((ProductRangePageModel) source).getPageType().getCode()
            + SOME_SEO_STUFF + DEFAULT_PRODUCTRANGE_CODE;
      }
      return null;
   }

   /**
    * @param sourceModel
    * @return preview Url.
    */
   private String resolveLocationPage(final AbstractPageModel sourceModel)
   {
      final AbstractPageModel source = sourceModel;
      final LocationPageModel locationPage = (LocationPageModel) source;
      String defaultCode = StringUtils.EMPTY;
      if (locationPage.getCategoryType().equals(CategoryType.COUNTRY))
      {
         defaultCode = DEFAULT_COUNTRY_CODE;
      }
      else if (locationPage.getCategoryType().equals(CategoryType.REGION))
      {
         defaultCode = DEFAULT_REGION_CODE;
      }
      else if (locationPage.getCategoryType().equals(CategoryType.DESTINATION))
      {
         defaultCode = DEFAULT_DESTINATION_CODE;
      }
      else if (locationPage.getCategoryType().equals(CategoryType.RESORT))
      {
         defaultCode = DEFAULT_RESORT_CODE;
      }

      return "/location/" + locationPage.getPageType().getCode() + SOME_SEO_STUFF + defaultCode;
   }

}
