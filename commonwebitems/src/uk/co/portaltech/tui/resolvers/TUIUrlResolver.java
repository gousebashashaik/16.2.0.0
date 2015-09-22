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

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.url.impl.AbstractUrlResolver;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import uk.co.portaltech.travel.enums.BrandType;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.FeatureService;
import uk.co.portaltech.tui.services.TuiUtilityService;
import uk.co.travel.daos.util.BrandUtils;
import uk.co.tui.web.common.enums.LocationPageType;

/**
 *
 */
public abstract class TUIUrlResolver<T> extends AbstractUrlResolver<T>
{
   protected String pattern;

   protected String defaultSubPageType;

   protected String overrideSubPageType;

   protected String context = "";

   private static final int TWO = 2;

   private FeatureService featureService;

   @Resource
   private ConfigurationService configurationService;

   @Resource
   private TuiUtilityService tuiUtilityService;

   @Resource
   private BrandUtils brandUtils;

   private static final String SUB_PAGE_TYPE = "{subPageType}";

   protected String buildPathString(final List<CategoryModel> path)
   {
      if (path == null || path.isEmpty())
      {
         // Default category part of path when missing category
         return "cc";
      }

      final StringBuilder result = new StringBuilder();

      for (int i = 0; i < path.size(); i++)
      {
         if (i != 0)
         {
            result.append('/');
         }

         result.append(urlSafe(getFeatureService().getFirstFeatureValueAsString("name",
            path.get(i), new Date(), brandUtils.getFeatureServiceBrand(path.get(i).getBrands()))));
      }

      return result.toString();
   }

   protected List<CategoryModel> getCategoryPath(final ProductModel source)
   {
      final List<CategoryModel> path = new ArrayList<CategoryModel>();
      for (final CategoryModel category : source.getSupercategories())
      {
         if (category instanceof LocationModel)
         {
            path.add(category);
         }
      }
      return path;
   }

   @Override
   public String urlSafe(final String url)
   {
      if (StringUtils.isEmpty(url))
      {
         return "";
      }
      else
      {
         // Replace all '&nbsp;' style elements with a hyphen.
         String theUrl = url.trim().replaceAll("&\\S+;", "-");
         // Replace any whitespace with a single hyphen.
         theUrl = theUrl.replaceAll("\\s+", "-");
         // Remove any other dodgy characters.
         theUrl = theUrl.replaceAll("[^a-zA-Z0-9-&]+", "");
         // Replace any resulting '--' multiple hyphens with a single hyphen.
         return theUrl.replaceAll("-{2,}", "-");
      }
   }

   public String getDefaultSubPageType()
   {
      return defaultSubPageType;
   }

   @Required
   public void setDefaultSubPageType(final String defaultSubPageType)
   {
      this.defaultSubPageType = defaultSubPageType;
   }

   @Required
   public void setPattern(final String pattern)
   {
      this.pattern = pattern;
   }

   public FeatureService getFeatureService()
   {
      if (featureService == null)
      {
         featureService = Registry.getApplicationContext().getBean(FeatureService.class);
      }
      return featureService;
   }

   public void setOverrideSubPageType(final String overrideSubPageType)
   {
      this.overrideSubPageType = overrideSubPageType;
   }

   public String getContext()
   {

      if (BrandType.TH.getCode().equals(tuiUtilityService.getSiteBrand()))
      {
         context = configurationService.getConfiguration().getString("thweb.webroot");
      }
      else if (BrandType.FJ.getCode().equals(tuiUtilityService.getSiteBrand()))
      {
         context = configurationService.getConfiguration().getString("fjweb.webroot");
      }
      else if (BrandType.FC.getCode().equals(tuiUtilityService.getSiteBrand()))
      {
         context = configurationService.getConfiguration().getString("tuiweb.webroot");
      }

      // Commented code for content services

      else if (BrandType.RT.getCode().equals(tuiUtilityService.getSiteBrand()))
      {
         context = configurationService.getConfiguration().getString("rtweb.webroot");
      }
      return context;
   }

   public String getContext(final String contextKey)
   {
      return configurationService.getConfiguration().getString(contextKey);
   }

   public String getTabUrl(final String sourceUrl, final String overrideSubPageType)
   {

      final StringBuilder targetURL = new StringBuilder();

      final String siteBrand = tuiUtilityService.getSiteBrand();
      if (BrandType.TH.getCode().equals(siteBrand))
      {
         final String[] htmlSplitArr = sourceUrl.split("\\.");
         final int urlsize = htmlSplitArr.length;
         targetURL.append(htmlSplitArr[urlsize - TWO]);
      }
      else
      {
         targetURL.append(sourceUrl);
      }

      if (LocationPageType.OVERVIEW.getCode().equalsIgnoreCase(overrideSubPageType))
      {
         if (BrandType.TH.getCode().equals(siteBrand))
         {
            targetURL.append(".html");
         }
         else
         {
            targetURL.append("");
         }
      }

      else
      {
         targetURL.append("/");
         targetURL.append(overrideSubPageType);
      }

      return targetURL.toString();
   }

   /**
    * @param source
    * @param categoryCode
    * @return
    */
   public String getFCCategoryUrl(final CategoryModel source)
   {
      String url;

      String categoryName =
         getFeatureService().getFirstFeatureValueAsString("name", source, new Date(), "FC");
      if (StringUtils.isBlank(categoryName))
      {
         categoryName = source.getName();
      }

      // Replace pattern values
      url = pattern.replace("{category-code}", source.getCode());

      if (StringUtils.isNotBlank(overrideSubPageType))
      {
         url = url.replace(SUB_PAGE_TYPE, overrideSubPageType);
      }
      else
      {
         url = url.replace(SUB_PAGE_TYPE, defaultSubPageType);
      }

      if (StringUtils.isNotBlank(categoryName))
      {
         url = url.replace("{category-name}", urlSafe(categoryName));
      }

      /*
       * We reset the overridePageSubType when finished. We do this to avoid having to make this
       * bean prototype scoped.
       */
      overrideSubPageType = null;

      url = getContext("tuiweb.webroot") + url;

      return url;
   }

   /**
    * @param source
    * @return
    */
   public String getFCProductUrl(final ProductModel source)
   {
      String url;

      String productName = source.getName();

      if (StringUtils.isBlank(productName))
      {
         productName =
            getFeatureService().getFirstFeatureValueAsString("name", source, new Date(), null);
      }

      url = pattern.replace("{category-path}", buildPathString(getCategoryPath(source)));
      if (StringUtils.isNotBlank(productName))
      {
         url = url.replace("{product-name}", urlSafe(productName));
      }
      url = url.replace("{product-code}", source.getCode());

      if (StringUtils.isNotBlank(overrideSubPageType))
      {
         url = url.replace(SUB_PAGE_TYPE, overrideSubPageType);
      }
      else
      {
         url = url.replace(SUB_PAGE_TYPE, defaultSubPageType);
      }

      /*
       * We reset the overridePageSubType when finished. We do this to avoid having to make this
       * bean prototype scoped.
       */
      overrideSubPageType = null;
      url = getContext("tuiweb.webroot") + url;
      return url;
   }
}
