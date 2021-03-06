/**
 *
 */
package uk.co.tui.fj.book.view.data;

import java.util.ArrayList;
import java.util.List;

import org.fest.util.Collections;

/**
 * This class holda all the summarypanel specific data used in all other pages.
 *
 * @author madhumathi.m
 *
 */
public class SummaryPanelViewData {
    /** The summary panel component view data. */
    private List<SummaryPanelComponentViewData> summaryPanelComponentViewData;

    /** The current page. */
    private String currentPage;

    /**
     * @return the summaryPanelComponentViewData
     */
    public List<SummaryPanelComponentViewData> getSummaryPanelComponentViewData() {
        if (Collections.isEmpty(this.summaryPanelComponentViewData)) {
            this.summaryPanelComponentViewData = new ArrayList<SummaryPanelComponentViewData>();
        }
        return this.summaryPanelComponentViewData;
    }

    /**
     * @param summaryPanelComponentViewData
     *            the summaryPanelComponentViewData to set
     */
    public void setSummaryPanelComponentViewData(
            List<SummaryPanelComponentViewData> summaryPanelComponentViewData) {
        this.summaryPanelComponentViewData = summaryPanelComponentViewData;
    }

    /**
     * @return the currentPage
     */
    public String getCurrentPage() {
        return currentPage;
    }

    /**
     * @param currentPage
     *            the currentPage to set
     */
    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

}
