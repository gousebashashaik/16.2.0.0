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

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.url.impl.AbstractUrlResolver;
import de.hybris.platform.util.Config;

import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import uk.co.portaltech.travel.model.ExcursionModel;
import uk.co.portaltech.travel.model.ExcursionPriceModel;
import uk.co.portaltech.travel.services.ExcursionPriceService;
import uk.co.portaltech.tui.services.TuiUtilityService;

/**
 * @author
 *
 */
public class ExcursionURLResolver extends AbstractUrlResolver<ExcursionModel> {
    /**
     *
     */
    private String pattern;

    @Resource
    private ExcursionPriceService excursionPriceService;

    @Resource
    private CMSSiteService cmsSiteService;

    @Resource
    private TuiUtilityService tuiUtilityService;

    private static final String GBP = "gbp";

    private static final String EUR = "eur";

    @Override
    public String resolve(final ExcursionModel excursionModel) {
        // Replace pattern values
        String url = "";
        url = getPattern();

        url = url.replace("{domain-url}",
                getDomainURL(tuiUtilityService.getSiteBrand()));
        url = url.replace("{currency}", getCurrency(excursionModel));
        url = url.replace("{atlas-id}", excursionModel.getAtlasId());
        url = url.replace("{excursion-name}", StringUtils
                .isEmpty(excursionModel.getName())
                ? "X"
                : parseString(excursionModel.getName()));

        return url;
    }

    /**
     * @param excursionModel
     */
    private String getCurrency(final ExcursionModel excursionModel) {

        final Set<ExcursionPriceModel> prices = excursionModel
                .getExcursionPrices();
        if (CollectionUtils.isNotEmpty(prices)) {
            final ExcursionPriceModel excursionPriceModel = excursionPriceService
                    .getExcursionPrice(((ExcursionPriceModel) CollectionUtils
                            .get(prices, 0)).getCode(), cmsSiteService
                            .getCurrentCatalogVersion());
            String code = excursionPriceModel.getCurrency().toLowerCase();
            if (!(StringUtils.equals(code, GBP))
                    && !(StringUtils.equals(code, EUR))) {
                code = GBP;
            }
            return code;

        }

        return StringUtils.EMPTY;
    }

    /**
     *
     */
    private CharSequence parseString(final String string) {
        return string.replaceAll("/", " ").replaceAll("&", " ")
                .replaceAll("-", " ").replaceAll("( )+", " ")
                .replaceAll("\\s", "_");
    }

    /**
     *
     */
    private String getDomainURL(final String siteId) {
        // need to return the current domain url here

        final StringBuilder configString = new StringBuilder(siteId);
        configString.append(".");
        configString.append("excursion.newBaseURL");

        return Config.getString(configString.toString(), "");

    }

    protected String getPattern() {
        return pattern;
    }

    @Required
    public void setPattern(final String pattern) {
        this.pattern = pattern;
    }

}
