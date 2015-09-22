package uk.co.tui.book.populators;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import uk.co.portaltech.commons.StringUtil;
import uk.co.tui.book.cart.services.PackageCartService;
import uk.co.tui.book.constants.SessionObjectKeys;
import uk.co.tui.book.utils.PropertyReader;
import uk.co.tui.book.view.data.SummaryPanelComponentViewData;
import uk.co.tui.book.view.data.SummaryPanelUrlsViewData;
import uk.co.tui.book.view.data.SummaryPanelViewData;

/**
 * * The Class SummaryPanelUrlPopulator. populator class used to populating the
 * summary view data from the session.
 *
 * @author madhumathi.m
 *
 */
public class SummaryPanelUrlPopulator
        implements
            Populator<String, SummaryPanelViewData> {

    /** The session service. */
    @Resource
    private SessionService sessionService;

    /** The book flow urls. */
    @Resource
    private PropertyReader bookFlowUrls;

    /** The package cart service. */
    @Resource
    private PackageCartService packageCartService;

    private static final String NAME = ".name";

    /** The cms site service. */
    @Resource
    private CMSSiteService cmsSiteService;

    /**
     * This method populates summary panel urls and order of display
     *
     * @param source
     *            - currentPage
     * @param target
     *            - SummaryPanelViewData object
     * @throws ConversionException
     *             the conversion exception
     */
    @Override
    public void populate(final String source, final SummaryPanelViewData target)
            throws ConversionException {
        target.setCurrentPage(source);
        final String brand = cmsSiteService.getCurrentSite().getUid();
        final String httpBaseURL = Config.getString(
                StringUtil.append(brand, ".web.http.base.url"),
                "http://www.firstchoice.co.uk");
        final List<String> pageNames = getPageNamesFromConfig(StringUtil
                .append(packageCartService.getBasePackage()
                        .getPackageType().toString(),
                        ".summary.componentOrder"));
        final Map<String, String> urlMap = new HashMap<String, String>();

        for (final String page : pageNames) {
            String url = StringUtil.append(httpBaseURL,
                    bookFlowUrls.getValue(brand + "." + page + ".url"));
            if ("HO".equals(page)) {
                url = StringUtil.append(httpBaseURL, (String) sessionService
                        .getAttribute(SessionObjectKeys.ACCOMOPTIONS_URL));
            }
            urlMap.put(bookFlowUrls.getValue(brand + "." + page + NAME), url);
        }
        target.setSummaryPanelComponentViewData(getSummaryPanelIndicators(
                source, urlMap));

    }

    /**
     * Gets the summary panel indicators.
     *
     * @param pageLabel
     *            the page label
     * @param urlMap
     *            the url map
     * @return the summary panel indicators
     */
    private List<SummaryPanelComponentViewData> getSummaryPanelIndicators(
            final String pageLabel, final Map<String, String> urlMap) {
        final List<SummaryPanelComponentViewData> summaryPanelComponentList = new ArrayList<SummaryPanelComponentViewData>();
        final List<String> pageNames = getPageNamesFromConfig(StringUtil
                .append(packageCartService.getBasePackage()
                        .getPackageType().toString(), ".pageOrder"));
        final String brand = cmsSiteService.getCurrentSite().getUid();
        populatePageNames(summaryPanelComponentList, pageNames, brand);
        boolean isBookComponent = true;
        for (final String page : pageNames) {
            if (StringUtils.equalsIgnoreCase(
                    bookFlowUrls.getValue(brand + "." + page + ".id"),
                    pageLabel)) {
                final int index = summaryPanelComponentList
                        .indexOf(new SummaryPanelComponentViewData(null,
                                bookFlowUrls
                                        .getValue(brand + "." + page + NAME),
                                null));
                summaryPanelComponentList.get(index).setExpanded("expand");
                summaryPanelComponentList.get(index).setName(
                        bookFlowUrls.getValue(brand + "." + page + NAME));
                populateSummaryPanelUrls(urlMap, index,
                        summaryPanelComponentList);
                isBookComponent = false;
            }
        }
        if (isBookComponent) {
            final int index = summaryPanelComponentList
                    .indexOf(new SummaryPanelComponentViewData(null,
                            bookFlowUrls.getValue(brand + ".BO.name"), null));
            final int index2 = index;
            summaryPanelComponentList.get(index).setExpanded("expand");
            summaryPanelComponentList.get(index).setName(
                    bookFlowUrls.getValue(brand + ".BO.name"));
            populateSummaryPanelUrls(urlMap, index, summaryPanelComponentList);
            populateExpandAttribute(summaryPanelComponentList, index2);

        }
        return summaryPanelComponentList;
    }

    /**
     * Populate expand attribute.
     *
     * @param summaryPanelComponentList
     *            the summary panel component list
     * @param indexValue
     *            the index
     */
    private void populateExpandAttribute(
            final List<SummaryPanelComponentViewData> summaryPanelComponentList,
            final int indexValue) {
        int index = indexValue;
        while (index != 0) {
            summaryPanelComponentList.get(index - 1).setExpanded("expand");
            index--;
        }
    }

    /**
     * Populate page names.
     *
     * @param summaryPanelComponentList
     *            the summary panel component list
     * @param pageNames
     *            the page names
     */
    private void populatePageNames(
            final List<SummaryPanelComponentViewData> summaryPanelComponentList,
            final List<String> pageNames, final String brand) {
        for (final String pageName : pageNames) {
            final SummaryPanelComponentViewData summaryComponent = new SummaryPanelComponentViewData(
                    null, bookFlowUrls.getValue(StringUtil.append(brand, ".",
                            pageName, NAME)), null);
            summaryPanelComponentList.add(summaryComponent);
        }
    }

    /**
     * Gets the page names from property file.
     *
     * @return the page names
     */
    private List<String> getPageNamesFromConfig(final String value) {
        final String pageOrder = bookFlowUrls.getValue(value);
        final List<String> pageNames = new ArrayList<String>();
        final String[] pages = StringUtils.split(pageOrder, ',');
        for (final String page : pages) {
            pageNames.add(page);
        }
        return pageNames;
    }

    /**
     * Populate summary panel urls.
     *
     * @param urlMap
     *
     * @param indexValue
     *            the index
     * @param summaryPanel
     *            the summary panel
     */
    private void populateSummaryPanelUrls(final Map<String, String> urlMap,
            final int indexValue,
            final List<SummaryPanelComponentViewData> summaryPanel) {
        int index = indexValue;
        while (index != 0) {
            final SummaryPanelUrlsViewData summaryPanelUrlViewData = new SummaryPanelUrlsViewData();
            summaryPanelUrlViewData.setDisplay("display");
            summaryPanelUrlViewData.setUrlName(summaryPanel.get(index - 1)
                    .getName());
            summaryPanelUrlViewData.setUrl(urlMap.get(summaryPanel.get(
                    index - 1).getName()));
            summaryPanel.get(index - 1).setSummaryPanelUrlsViewData(
                    summaryPanelUrlViewData);
            summaryPanel.set(index - 1, summaryPanel.get(index - 1));
            index--;
        }
    }
}
