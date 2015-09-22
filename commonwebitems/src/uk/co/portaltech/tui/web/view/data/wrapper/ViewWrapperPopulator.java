/**
 *
 */
package uk.co.portaltech.tui.web.view.data.wrapper;

import java.util.ArrayList;
import java.util.List;

import uk.co.portaltech.travel.thirdparty.endeca.FacetOption;
import uk.co.portaltech.travel.thirdparty.endeca.FacetValue;
import uk.co.portaltech.travel.thirdparty.endeca.ResultData;
import uk.co.portaltech.tui.web.view.data.AccommodationViewData;

/**
 * @author gagan
 *
 */
public class ViewWrapperPopulator {

    public List<AccommodationViewData> getAccommodationList(List<ResultData> results) {
        List<AccommodationViewData> accommodationViewDataList = null;
        if(results != null && !results.isEmpty()) {
            accommodationViewDataList = new ArrayList<AccommodationViewData>();
            for (ResultData resultData : results) {

                AccommodationViewData accommodationViewData = new AccommodationViewData();
                accommodationViewData.setCode(resultData.getCode());
                accommodationViewData.setName(resultData.getName());
                accommodationViewData.setPriceFrom(resultData.getPriceFrom());
                accommodationViewData.setAvailableFrom(resultData.getAvailableFrom());
                accommodationViewData.settRating(resultData.gettRating());
                accommodationViewData.setStayPeriod(resultData.getStayPeriod());
                accommodationViewData.setCommercialPriority(resultData.getCommercialPriority());
                accommodationViewData.setAccommodationType(resultData.getAccommodationType());
                accommodationViewData.setDeparturePoint(resultData.getDeparturePoint());
                accommodationViewDataList.add(accommodationViewData);
            }
        }

        return accommodationViewDataList;
    }

    public List<FacetOption> getFacetOptions(List<ResultData> results) {
        List<FacetOption> facetOptions = null;
        if(results != null && !results.isEmpty()) {
            facetOptions = new ArrayList<FacetOption>();
            verifyFacet(results, facetOptions);
        }
        return facetOptions;
    }

    /**
     * @param results
     * @param facetOptions
     */
    private void verifyFacet(List<ResultData> results, List<FacetOption> facetOptions)
    {
        for (ResultData resultData : results) {
            FacetOption facetOption = resultData.getFacetOption();
            if(facetOption != null) {
                updateFacetValues(facetOption);
                facetOptions.add(facetOption);
            }

        }
    }


    public List<FacetOption> getSelectedFacetOptions(List<ResultData> results) {
        List<FacetOption> selectedFacetOptions = null;
        if(results != null && !results.isEmpty()) {
            selectedFacetOptions = new ArrayList<FacetOption>();
            selectFacet(results, selectedFacetOptions);
        }
        return selectedFacetOptions;
    }

    /**
     * @param results
     * @param selectedFacetOptions
     */
    private void selectFacet(List<ResultData> results, List<FacetOption> selectedFacetOptions)
    {
        for (ResultData resultData : results) {
            FacetOption selectedFacetOption = resultData.getSelectedFacetOption();
            if(selectedFacetOption != null) {
                updateFacetValues(selectedFacetOption);
                selectedFacetOptions.add(selectedFacetOption);
            }
        }
    }

    /**
     * @param selectedFacetOption
     */
    private void updateFacetValues(FacetOption selectedFacetOption) {
        List<FacetValue> selectedFacetValues = selectedFacetOption.getFacetValues();
        if(selectedFacetValues != null && !selectedFacetValues.isEmpty()) {
            for (FacetValue selectedFacetValue : selectedFacetValues) {
                selectedFacetValue.setValue(selectedFacetValue.getLabel());
            }
        }
    }

}
