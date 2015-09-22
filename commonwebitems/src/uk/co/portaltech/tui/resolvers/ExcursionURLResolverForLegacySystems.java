/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2012 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package uk.co.portaltech.tui.resolvers;

import de.hybris.platform.category.CategoryService;
import de.hybris.platform.commerceservices.url.impl.AbstractUrlResolver;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import uk.co.portaltech.travel.enums.LocationType;
import uk.co.portaltech.travel.model.ExcursionModel;
import uk.co.portaltech.travel.model.ExcursionPriceModel;
import uk.co.portaltech.travel.model.LocationModel;
import uk.co.portaltech.travel.services.LocationService;
import uk.co.portaltech.tui.brand.BrandDetails;
import uk.co.tui.web.common.constants.CommonwebitemsConstants;


/**
 * @author Vijay
 *
 */
public class ExcursionURLResolverForLegacySystems extends AbstractUrlResolver<ExcursionModel>
{

    private String pattern;
    private String suffixUrlString;


    @Resource
    private LocationService tuiLocationService;

    @Resource
    private CategoryService categoryService;



    @Resource
    private SessionService sessionService;

    @Override
    public String resolve(final ExcursionModel excursionModel)
    {

        String locCode = "";
        final Set<ExcursionPriceModel> excursionPrices = excursionModel.getExcursionPrices();
        if (excursionPrices != null && !excursionPrices.isEmpty())
        {
            locCode = excursionPrices.iterator().next().getLocation().getCode();
        }
        if (StringUtils.isEmpty(locCode))
        {
            return "";
        }
        final LocationModel location = (LocationModel) categoryService.getCategoryForCode(locCode);

        // Replace pattern values
        String url = "";
        url = getPattern();

        final BrandDetails brandDetails = sessionService.getAttribute(CommonwebitemsConstants.BRAND_DETAILS);
        final List<String> brandTypePKS = brandDetails.getRelevantBrands();

        final LocationModel countryLocation = tuiLocationService.getLocationForItem(location, LocationType.COUNTRY, brandTypePKS);
        url = url.replace("{domain-url}", getDomainURL(brandDetails.getSiteUid()));
        url = url.replace("{country-code}", countryLocation == null ? "X" : parseString(countryLocation.getCode()));
        url = url.replace("{country-name}", countryLocation == null ? "X" : parseString(countryLocation.getName()));
        final LocationModel resortLoc = tuiLocationService.getLocationForItem(location, LocationType.RESORT, brandTypePKS);
        url = url.replace("{resort-code}", resortLoc == null ? "X" : resortLoc.getCode());
        final LocationModel regionLoc = tuiLocationService.getLocationForItem(location, LocationType.REGION, brandTypePKS);
        url = url.replace("{region-name}", regionLoc == null ? "X" : parseString(regionLoc.getName()));
        url = url.replace("{excursion-code}", StringUtils.isEmpty(excursionModel.getCode()) ? "" : excursionModel.getCode());
        url = url.replace("{excursion-name}",
                StringUtils.isEmpty(excursionModel.getName()) ? "" : parseString(excursionModel.getName()));
        url = url.replace("{suffix-url}", suffixUrlString);
        return url;
    }


    /**
     *
     */
    private CharSequence parseString(final String string)
    {
        return string.replaceAll("/", " ").replaceAll("&", " ").replaceAll("-", " ").replaceAll("( )+", " ").replaceAll("\\s", "-");
    }


    /**
     *
     */
    private String getDomainURL(final String siteId)
    {
        // need to return the current domain url here

        final StringBuilder configString = new StringBuilder(siteId);
        configString.append(".");
        configString.append("excursion.baseURL");

        return Config.getString(configString.toString(), "");

    }

    /**
     * @param suffixUrlString
     *           the suffixUrlString to set
     */
    public void setSuffixUrlString(final String suffixUrlString)
    {
        this.suffixUrlString = suffixUrlString;
    }

    protected String getPattern()
    {
        return pattern;
    }

    @Required
    public void setPattern(final String pattern)
    {
        this.pattern = pattern;
    }

}
