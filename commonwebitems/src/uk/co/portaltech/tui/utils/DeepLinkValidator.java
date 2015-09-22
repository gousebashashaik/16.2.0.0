/**
 *
 */
package uk.co.portaltech.tui.utils;

import static uk.co.portaltech.commons.DateUtils.isValidDate;
import static uk.co.portaltech.commons.DateUtils.isDateGreaterThanToday;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;


import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.tui.web.view.data.DeepLinkRequestData;
import uk.co.portaltech.tui.web.view.data.DeepLinkRequestViewData;
import uk.co.portaltech.tui.web.view.data.ErrorData;



/**
 * @author laxmibai.p
 *
 */
public class DeepLinkValidator {



    /**
     *
     */
    private static final int ONE = 1;
    private boolean requiredFieldSet;


    /**
     * @param deepLinkRequestData
     * @param errors
     * @param searchPanelComponentModel
     */
    public void validate(DeepLinkRequestData deepLinkRequestData,
            List<ErrorData> errors,
            SearchPanelComponentModel searchPanelComponentModel) {

        if (deepLinkRequestData == null) {
            addToErrors(ValidationErrorMessage.INVALID_INPUT, errors);
            requiredFieldSet = true;
        }
        airportUnitValidation(deepLinkRequestData, errors);
        validateWhen(deepLinkRequestData, errors);
        validatePartyComposition(deepLinkRequestData, errors,
                searchPanelComponentModel);

    }


    /**
     * @param deepLinkRequestData
     * @param errors
     */
    private void airportUnitValidation(DeepLinkRequestData deepLinkRequestData,
            List<ErrorData> errors) {
        if (CollectionUtils.isEmpty(deepLinkRequestData.getAirports())
                && (CollectionUtils.isEmpty(deepLinkRequestData.getUnits()))) {
            addToErrors(ValidationErrorMessage.INVALID_AIRPORT_UNITS, errors);
            requiredFieldSet = true;
        }
    }

    /**
     * @param deepLinkRequestData
     * @param errors
     */
    private void validateWhen(DeepLinkRequestData deepLinkRequestData,
            List<ErrorData> errors) {
        if (StringUtils.isEmpty(deepLinkRequestData.getWhen())) {
            addToErrors(ValidationErrorMessage.INVALID_WHEN, errors);
            requiredFieldSet = true;
        } else if (!isValidDate(deepLinkRequestData.getWhen())) {
            addToErrors(ValidationErrorMessage.INVALID_WHEN_FORMAT, errors);
            requiredFieldSet = true;
        } else if (!isDateGreaterThanToday(deepLinkRequestData.getWhen())) {
            addToErrors(ValidationErrorMessage.INVALID_WHEN_DATE, errors);
            requiredFieldSet = true;
        }
    }



    /**
     * @param deepLinkRequestData
     * @param errors
     * @param searchPanelComponentModel
     */
    private void validatePartyComposition(
            DeepLinkRequestData deepLinkRequestData, List<ErrorData> errors,
            SearchPanelComponentModel searchPanelComponentModel) {

        int adults = deepLinkRequestData.getNoOfAdults()
                + deepLinkRequestData.getNoOfSeniors();

        int noOfPassenger = deepLinkRequestData.getNoOfChildren() + adults;
        int totalvalidPartySize = searchPanelComponentModel.getMaxNoOfChild().intValue()
                + (searchPanelComponentModel.getMaxNoOfAdult().intValue());
        int minPartySize = searchPanelComponentModel.getMinNoOfAdult().intValue()
                + searchPanelComponentModel.getMinNoOfChild().intValue();

        if (minPartySize > noOfPassenger) {
            addToErrors(ValidationErrorMessage.INVALID_PARTY_COMPOSITION,
                    errors);
        } else if (totalvalidPartySize < noOfPassenger) {
            addToErrors(ValidationErrorMessage.INVALID_PARTY_COMPOSITION_MAX,
                    errors);
        } else {
            validateAdults(adults, searchPanelComponentModel, errors);
            validateChildren(deepLinkRequestData, searchPanelComponentModel,
                    errors);
        }
    }

    /**
     * @param deepLinkRequestData
     * @param searchPanelComponentModel
     * @param errors
     */
    private void validateChildren(DeepLinkRequestData deepLinkRequestData,
            SearchPanelComponentModel searchPanelComponentModel,
            List<ErrorData> errors) {

        int minNoOfChild = searchPanelComponentModel.getMinNoOfChild().intValue();
        int maxNoOfChild = searchPanelComponentModel.getMaxNoOfChild().intValue();
        int infantCount = getInfantCount(deepLinkRequestData.getChildrenAge(),
                searchPanelComponentModel.getInfantAge().intValue());

        if (deepLinkRequestData.getNoOfChildren() < minNoOfChild) {
            addToErrors(
                    ValidationErrorMessage.INVALID_PARTY_COMPOSITION_MIN_CHILD,
                    errors);
            requiredFieldSet = true;
        } else if (deepLinkRequestData.getNoOfChildren() > maxNoOfChild) {
            addToErrors(
                    ValidationErrorMessage.INVALID_PARTY_COMPOSITION_MAX_CHILD,
                    errors);
            requiredFieldSet = true;
        } else if (infantCount > (deepLinkRequestData.getNoOfAdults() + deepLinkRequestData
                .getNoOfSeniors())) {
            addToErrors(
                    ValidationErrorMessage.INVALID_PARTY_COMPOSITION_INFANT,
                    errors);
            requiredFieldSet = true;
        } else if (deepLinkRequestData.getNoOfChildren() >0
                && deepLinkRequestData.getNoOfAdults() == 0) {
            addToErrors(ValidationErrorMessage.INVALID_ADULTS_FOR_CHILDREN,
                    errors);
            requiredFieldSet = true;

        }
    }

    /**
     * @param adults
     * @param searchPanelComponentModel
     * @param errors
     */
    private void validateAdults(int adults,
            SearchPanelComponentModel searchPanelComponentModel,
            List<ErrorData> errors) {

        int maxNoofAdults = searchPanelComponentModel.getMaxNoOfAdult().intValue();

        // Its been agreed by the business that the value ONE will be hardcoded
        // here and also its a valid search if its 1 adult.
        if (adults < ONE) {
            addToErrors(
                    ValidationErrorMessage.INVALID_PARTY_COMPOSITION_MIN_ADULT,
                    errors);
            requiredFieldSet = true;
        } else if (adults > maxNoofAdults) {
            addToErrors(
                    ValidationErrorMessage.INVALID_PARTY_COMPOSITION_MAX_ADULT,
                    errors);
            requiredFieldSet = true;
        }
    }

    /**
     * @param childrenAge
     * @param infantAge
     * @return
     */
    private int getInfantCount(List<Integer> childrenAge, int infantAge) {

        int infantCount = 0;
        if(CollectionUtils.isNotEmpty(childrenAge))
        {
        for (int age: childrenAge) {
            if (age <= infantAge) {
                infantCount++;
            }
        }
        }
        return infantCount;
    }

    /**
     * @param errorType
     * @param errors
     */
    public void addToErrors(ValidationErrorMessage errorType,
            List<ErrorData> errors) {
        ErrorData errorData = new ErrorData();
        errorData.setCode(errorType.getCode());
        errorData.setDescription(errorType.getDescription());
        errors.add(errorData);
    }

    /**
     * @param requestViewData
     * @return
     */
    public boolean hasError(DeepLinkRequestViewData requestViewData) {
        if ((requestViewData.getErrors().size()) > 0) {
            return true;
        }
        return false;
    }

    /**
     * @return the requiredFieldSet
     */
    public  boolean isRequiredFieldSet() {
        return requiredFieldSet;
    }

    /**
     * @param requiredFieldSet the requiredFieldSet to set
     */
    public  void setRequiredFieldSet(boolean requiredFieldSet) {
        this.requiredFieldSet = requiredFieldSet;
    }




}
