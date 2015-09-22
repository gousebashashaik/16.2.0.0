/**
 *
 */
package uk.co.portaltech.tui.web.view.data.wrapper;

import java.util.List;

import uk.co.portaltech.travel.thirdparty.endeca.FacetOption;
import uk.co.portaltech.travel.thirdparty.endeca.PaginationData;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;

/**
 * @author gagan
 *
 */
public class AccommodationFilterDataWrapper {

    private List<AccommodationViewData> accommodations;

    private PaginationData paginationData;

    private List<FacetOption> facetOptions;

    private List<FacetOption> selectedFacetOptions;

    /**
     * @return the accommodations
     */
    public List<AccommodationViewData> getAccommodations() {
        return accommodations;
    }

    /**
     * @param accommodations the accommodations to set
     */
    public void setAccommodations(List<AccommodationViewData> accommodations) {
        this.accommodations = accommodations;
    }



    /**
     * @return the facetOptions
     */
    public List<FacetOption> getFacetOptions() {
        return facetOptions;
    }

    /**
     * @param facetOptions the facetOptions to set
     */
    public void setFacetOptions(List<FacetOption> facetOptions) {
        this.facetOptions = facetOptions;
    }

    /**
     * @return the paginationData
     */
    public PaginationData getPaginationData() {
        return paginationData;
    }

    /**
     * @param paginationData the paginationData to set
     */
    public void setPaginationData(PaginationData paginationData) {
        this.paginationData = paginationData;
    }

    /**
     * @return the selectedFacetOptions
     */
    public List<FacetOption> getSelectedFacetOptions() {
        return selectedFacetOptions;
    }

    /**
     * @param selectedFacetOptions the selectedFacetOptions to set
     */
    public void setSelectedFacetOptions(List<FacetOption> selectedFacetOptions) {
        this.selectedFacetOptions = selectedFacetOptions;
    }


}
