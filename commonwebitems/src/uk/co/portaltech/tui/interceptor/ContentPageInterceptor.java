/**
 *
 */
package uk.co.portaltech.tui.interceptor;

import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.ListUtils;
import org.jfree.util.Log;
import org.zkoss.zhtml.Messagebox;

/**
 * @author l.furrer
 *
 */
public class ContentPageInterceptor implements ValidateInterceptor {

    private static final String   META_KEYWORDS                             = "metaKeywords";
    private static final String   META_TITLE                                = "metaTitle";
    private static final String   META_DESCRIPTION                          = "metaDescription";

    private static final String   GET_ALL_CONTENT_PAGES_FOR_CATALOG_VERSION = "SELECT {pk},{metaKeywords},{metaTitle},{metaDescription} " +
                                                                                    "FROM {ContentPage} " +
                                                                                    "WHERE {catalogVersion}=?catalogVersion";

    @Resource
    private FlexibleSearchService flexibleSearchService;

    @Override
    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException {

        ContentPageModel contentPage = (ContentPageModel) model;
        FlexibleSearchQuery getAllContentPagesForCatalogVersion = new FlexibleSearchQuery(GET_ALL_CONTENT_PAGES_FOR_CATALOG_VERSION);
        getAllContentPagesForCatalogVersion.addQueryParameter("catalogVersion", contentPage.getCatalogVersion().getPk());
        SearchResult<ContentPageModel> result = flexibleSearchService.<ContentPageModel> search(getAllContentPagesForCatalogVersion);
        List<ContentPageModel> allContentPages = result.getResult();

        List<ContentPageModel> keywordDuplicates = searchForDuplicates(contentPage, META_KEYWORDS, allContentPages);
        List<ContentPageModel> titleDuplicates = searchForDuplicates(contentPage, META_TITLE, allContentPages);
        List<ContentPageModel> descriptionDuplicates = searchForDuplicates(contentPage, META_DESCRIPTION, allContentPages);

        StringBuilder warningMessage = new StringBuilder();
        if (!keywordDuplicates.isEmpty()) {
            warningMessage.append("Warning, the following pages have the same meta keywords as the one that is being created.\n");
            warningMessage = buildWarningMessage(warningMessage, keywordDuplicates);
            warningMessage.append("\n");
        }

        if (!titleDuplicates.isEmpty()) {
            warningMessage.append("Warning, the following pages have the same meta title as the one that is being created.\n");
            warningMessage = buildWarningMessage(warningMessage, titleDuplicates);
            warningMessage.append("\n");
        }

        if (!descriptionDuplicates.isEmpty()) {
            warningMessage.append("Warning, the following pages have the same meta description as the one that is being created.\n");
            warningMessage = buildWarningMessage(warningMessage, descriptionDuplicates);
            warningMessage.append("\n");
        }

        if (!warningMessage.toString().isEmpty()) {
            try {
                Messagebox.show(warningMessage.toString(), "Warning!", Messagebox.OK, Messagebox.EXCLAMATION);
            } catch (InterruptedException e) {
               Log.error("exception caused while validating", e);
            }
        }
    }

    private List<ContentPageModel> searchForDuplicates(ContentPageModel contentPage, String attribute, Collection<ContentPageModel> allContentPages) {
        if (attribute.equals(META_KEYWORDS)) {
            return searchKeywordsDuplicates(contentPage, allContentPages);
        }

        if (attribute.equals(META_TITLE)) {
            return searchTitleDuplicates(contentPage, allContentPages);
        }

        if (attribute.equals(META_DESCRIPTION)) {
            return searchDescriptionDuplicates(contentPage, allContentPages);
        }

        return ListUtils.EMPTY_LIST;
    }

    private List<ContentPageModel> searchKeywordsDuplicates(ContentPageModel contentPage, Collection<ContentPageModel> allContentPages) {
        if (contentPage.getMetaKeywords() == null || contentPage.getMetaKeywords().isEmpty()) {
            return ListUtils.EMPTY_LIST;
        }

        List<ContentPageModel> duplicates = new ArrayList<ContentPageModel>();
        Iterator<ContentPageModel> allContentPagesIterator = allContentPages.iterator();
        while (allContentPagesIterator.hasNext()) {
            ContentPageModel currentContentPage = allContentPagesIterator.next();
            if (currentContentPage.getMetaKeywords() != null
                    && currentContentPage.getMetaKeywords().equals(contentPage.getMetaKeywords())
                    && !currentContentPage.equals(contentPage)) {
                duplicates.add(currentContentPage);
            }
        }
        return duplicates;
    }

    private List<ContentPageModel> searchTitleDuplicates(ContentPageModel contentPage, Collection<ContentPageModel> allContentPages) {
        if (contentPage.getMetaTitle() == null || contentPage.getMetaTitle().isEmpty()) {
            return ListUtils.EMPTY_LIST;
        }

        List<ContentPageModel> duplicates = new ArrayList<ContentPageModel>();
        Iterator<ContentPageModel> allContentPagesIterator = allContentPages.iterator();
        while (allContentPagesIterator.hasNext()) {
            ContentPageModel currentContentPage = allContentPagesIterator.next();
            if (currentContentPage.getMetaTitle() != null
                    && currentContentPage.getMetaTitle().equals(contentPage.getMetaTitle())
                    && !currentContentPage.equals(contentPage)) {
                duplicates.add(currentContentPage);
            }
        }
        return duplicates;
    }

    private List<ContentPageModel> searchDescriptionDuplicates(ContentPageModel contentPage, Collection<ContentPageModel> allContentPages) {
        if (contentPage.getMetaDescription() == null || contentPage.getMetaDescription().isEmpty()) {
            return ListUtils.EMPTY_LIST;
        }

        List<ContentPageModel> duplicates = new ArrayList<ContentPageModel>();
        Iterator<ContentPageModel> allContentPagesIterator = allContentPages.iterator();
        while (allContentPagesIterator.hasNext()) {
            ContentPageModel currentContentPage = allContentPagesIterator.next();
            if (currentContentPage.getMetaDescription() != null
                    && currentContentPage.getMetaDescription().equals(contentPage.getMetaDescription())
                    && !currentContentPage.equals(contentPage)) {
                duplicates.add(currentContentPage);
            }
        }
        return duplicates;
    }

    private StringBuilder buildWarningMessage(StringBuilder initialMessages, List<ContentPageModel> duplicates) {
        StringBuilder initialMessage= initialMessages;
        initialMessage = composeMessageString(duplicates, initialMessage);
        return initialMessage;
    }

    private StringBuilder composeMessageString(List<ContentPageModel> duplicates, StringBuilder message) {
        for (ContentPageModel duplicatePage : duplicates) {
            message.append(" --> ");
            message.append(duplicatePage.getName());
            message.append("\n");
        }
        return message;
    }
}
