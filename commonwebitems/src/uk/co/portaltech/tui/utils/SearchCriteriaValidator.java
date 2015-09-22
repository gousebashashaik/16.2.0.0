/**
 *
 */
package uk.co.portaltech.tui.utils;

import static uk.co.portaltech.commons.DateUtils.isDateGreaterThanToday;
import static uk.co.portaltech.commons.DateUtils.isValidDate;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

import uk.co.portaltech.travel.model.HolidayFinderComponentModel;
import uk.co.portaltech.travel.model.NewSearchPanelComponentModel;
import uk.co.portaltech.travel.model.SearchPanelComponentModel;
import uk.co.portaltech.tui.web.view.data.DeepLinkRequestData;
import uk.co.portaltech.tui.web.view.data.DeepLinkRequestViewData;
import uk.co.portaltech.tui.web.view.data.ErrorData;
import uk.co.tui.async.logging.TUILogUtils;
import uk.co.tui.web.common.jalo.CommonwebitemsManager;



/**
 * @author laxmibai.p
 *
 */
public class SearchCriteriaValidator
{



    /**
     *
     */
    private static final int ONE = 1;
    private boolean requiredFieldSet;
    private static final int ZERO = 0;

    private static Policy policy;


    private static final TUILogUtils LOGGER = new TUILogUtils("SearchCriteriaValidator");

    //Static block to create policy instance. To perform URL validations.
    static
    {
        try
        {
            final InputStream is = CommonwebitemsManager.class.getResourceAsStream("/tui/antisamy-esapi.xml");
            policy = Policy.getInstance(is);
        }
        catch (final PolicyException pe)
        {
            LOGGER.error("PolicyException: ", pe);
            LOGGER.info("There was a problem in the " + "esapi antisamy policy, hence resetting the benefit " + "content to empty");
        }
    }

    /**
     * @param deepLinkRequestData
     * @param errors
     * @param searchPanelComponentModel
     */
    public void validate(final DeepLinkRequestData deepLinkRequestData, final List<ErrorData> errors,
            final SearchPanelComponentModel searchPanelComponentModel)
    {

        if (deepLinkRequestData == null)
        {
            addToErrors(ValidationErrorMessage.INVALID_INPUT, errors);
            requiredFieldSet = true;
        }

        validateDataTamper(deepLinkRequestData, errors);
        airportUnitValidation(deepLinkRequestData, errors);
        validateWhen(deepLinkRequestData, errors);
        validatePartyComposition(deepLinkRequestData, errors, searchPanelComponentModel);


    }

    /**
     * @param deepLinkRequestData
     * @param errors
     * @param searchPanelComponentModel
     */
    public void validateForNewSearch(final DeepLinkRequestData deepLinkRequestData, final List<ErrorData> errors,
            final NewSearchPanelComponentModel newsearchPanelComponentModel)
    {

        if (deepLinkRequestData == null)
        {
            addToErrors(ValidationErrorMessage.INVALID_INPUT, errors);
            requiredFieldSet = true;
        }

        validateDataTamper(deepLinkRequestData, errors);
        airportUnitValidation(deepLinkRequestData, errors);
        validateWhen(deepLinkRequestData, errors);
        validatePartyCompositionForNewSearch(deepLinkRequestData, errors, newsearchPanelComponentModel);


    }


    /**
     * @param deepLinkRequestData
     * @param errors
     * @param searchPanelComponentModel
     */
    public void validateForSmerch(final DeepLinkRequestData deepLinkRequestData, final List<ErrorData> errors,
            final SearchPanelComponentModel searchPanelComponentModel)
    {

        if (deepLinkRequestData == null)
        {
            addToErrors(ValidationErrorMessage.INVALID_INPUT, errors);
            requiredFieldSet = true;
        }

        validateDataTamper(deepLinkRequestData, errors);
        airportUnitValidationForSmerch(deepLinkRequestData, errors);
        validateWhen(deepLinkRequestData, errors);
        validatePartyComposition(deepLinkRequestData, errors, searchPanelComponentModel);

    }


    /**
     * These are the String fields which might hold tampered data. So validating them.
     *
     * @param deepLinkRequestData
     */
    private void validateDataTamper(final DeepLinkRequestData deepLinkRequestData, final List<ErrorData> errors)
    {

        if (!(validateSafeHTML(deepLinkRequestData.getUntil()) && validateSafeHTML(deepLinkRequestData.getWhen())
        //&&
        //validateSafeHTML(deepLinkRequestData.getPackageId()) &&
        //validateSafeHTML(deepLinkRequestData.getSearchRequestType())
        ))
        {
            addToErrors(ValidationErrorMessage.TAMPER_DATA_FOUND, errors);
            requiredFieldSet = true;
        }
    }


    /**
     * @param deepLinkRequestData
     * @param errors
     */
    private void airportUnitValidation(final DeepLinkRequestData deepLinkRequestData, final List<ErrorData> errors)
    {
        if (CollectionUtils.isEmpty(deepLinkRequestData.getAirports()) && (CollectionUtils.isEmpty(deepLinkRequestData.getUnits())))
        {
            addToErrors(ValidationErrorMessage.INVALID_AIRPORT_UNITS, errors);
            requiredFieldSet = true;
        }
    }

    /**
     * @param deepLinkRequestData
     * @param errors
     */
    private void airportUnitValidationForSmerch(final DeepLinkRequestData deepLinkRequestData, final List<ErrorData> errors)
    {
        if (CollectionUtils.isEmpty(deepLinkRequestData.getAirports()) || (CollectionUtils.isEmpty(deepLinkRequestData.getUnits())))
        {
            addToErrors(ValidationErrorMessage.INVALID_AIRPORT_UNITS, errors);
            requiredFieldSet = true;
        }
    }

    /**
     * @param deepLinkRequestData
     * @param errors
     */
    private void validateWhen(final DeepLinkRequestData deepLinkRequestData, final List<ErrorData> errors)
    {
        if (StringUtils.isEmpty(deepLinkRequestData.getWhen()))
        {
            addToErrors(ValidationErrorMessage.INVALID_WHEN, errors);
            requiredFieldSet = true;
        }
        else if (!isValidDate(deepLinkRequestData.getWhen()))
        {
            addToErrors(ValidationErrorMessage.INVALID_WHEN_FORMAT, errors);
            requiredFieldSet = true;
        }
        else if (!isDateGreaterThanToday(deepLinkRequestData.getWhen()))
        {
            addToErrors(ValidationErrorMessage.INVALID_WHEN_DATE, errors);
            requiredFieldSet = true;
        }
    }



    /**
     * @param deepLinkRequestData
     * @param errors
     * @param searchPanelComponentModel
     */
    private void validatePartyComposition(final DeepLinkRequestData deepLinkRequestData, final List<ErrorData> errors,
            final SearchPanelComponentModel searchPanelComponentModel)
    {

        final int adults = deepLinkRequestData.getNoOfAdults() + deepLinkRequestData.getNoOfSeniors();

        final int noOfPassenger = deepLinkRequestData.getNoOfChildren() + adults;
        final int totalvalidPartySize = searchPanelComponentModel.getMaxPaxConfig().intValue();


        if (noOfPassenger <= ZERO)
        {
            addToErrors(ValidationErrorMessage.INVALID_PARTY_COMPOSITION, errors);
        }
        else if (totalvalidPartySize < noOfPassenger)
        {
            addToErrors(ValidationErrorMessage.INVALID_PARTY_COMPOSITION_MAX, errors);
        }
        else
        {
            validateAdults(adults, searchPanelComponentModel, errors);
            validateChildren(deepLinkRequestData, searchPanelComponentModel, errors);
        }
    }

    /**
     * @param deepLinkRequestData
     * @param errors
     * @param searchPanelComponentModel
     */
    private void validatePartyCompositionForNewSearch(final DeepLinkRequestData deepLinkRequestData, final List<ErrorData> errors,
            final NewSearchPanelComponentModel newsearchPanelComponentModel)
    {

        final int adults = deepLinkRequestData.getNoOfAdults() + deepLinkRequestData.getNoOfSeniors();

        final int noOfPassenger = deepLinkRequestData.getNoOfChildren() + adults;
        final int totalvalidPartySize = newsearchPanelComponentModel.getMaxPaxConfig().intValue();


        if (noOfPassenger <= ZERO)
        {
            addToErrors(ValidationErrorMessage.INVALID_PARTY_COMPOSITION, errors);
        }
        else if (totalvalidPartySize < noOfPassenger)
        {
            addToErrors(ValidationErrorMessage.INVALID_PARTY_COMPOSITION_MAX, errors);
        }
        else
        {
            validateAdultsForNewSearch(adults, newsearchPanelComponentModel, errors);
            validateChildrenForNewSearch(deepLinkRequestData, newsearchPanelComponentModel, errors);
        }
    }

    /**
     * @param deepLinkRequestData
     * @param newsearchPanelComponentModel
     * @param errors
     */
    private void validateChildrenForNewSearch(final DeepLinkRequestData deepLinkRequestData,
            final NewSearchPanelComponentModel newsearchPanelComponentModel, final List<ErrorData> errors)
    {
        final int minNoOfChild = newsearchPanelComponentModel.getMinNoOfChild().intValue();
        final int maxNoOfChild = newsearchPanelComponentModel.getMaxNoOfChild().intValue();
        final int adults = deepLinkRequestData.getNoOfAdults() + deepLinkRequestData.getNoOfSeniors();
        final int infantCount = getInfantCount(deepLinkRequestData.getChildrenAge(), newsearchPanelComponentModel.getInfantAge()
                .intValue());

        if (deepLinkRequestData.getNoOfChildren() < minNoOfChild)
        {
            addToErrors(ValidationErrorMessage.INVALID_PARTY_COMPOSITION_MIN_CHILD, errors);
            requiredFieldSet = true;
        }
        else if (deepLinkRequestData.getNoOfChildren() > maxNoOfChild)
        {
            addToErrors(ValidationErrorMessage.INVALID_PARTY_COMPOSITION_MAX_CHILD, errors);
            requiredFieldSet = true;
        }
        else if (infantCount > adults)
        {
            addToErrors(ValidationErrorMessage.INVALID_PARTY_COMPOSITION_INFANT, errors);
            requiredFieldSet = true;
        }
        else if ((deepLinkRequestData.getNoOfChildren() > ZERO) && (adults == ZERO))
        {
            addToErrors(ValidationErrorMessage.INVALID_ADULTS_FOR_CHILDREN, errors);
            requiredFieldSet = true;

        }
    }

    /**
     * @param deepLinkRequestData
     * @param searchPanelComponentModel
     * @param errors
     */
    private void validateChildren(final DeepLinkRequestData deepLinkRequestData,
            final SearchPanelComponentModel searchPanelComponentModel, final List<ErrorData> errors)
    {

        final int minNoOfChild = searchPanelComponentModel.getMinNoOfChild().intValue();
        final int maxNoOfChild = searchPanelComponentModel.getMaxNoOfChild().intValue();
        final int adults = deepLinkRequestData.getNoOfAdults() + deepLinkRequestData.getNoOfSeniors();
        final int infantCount = getInfantCount(deepLinkRequestData.getChildrenAge(), searchPanelComponentModel.getInfantAge()
                .intValue());

        if (deepLinkRequestData.getNoOfChildren() < minNoOfChild)
        {
            addToErrors(ValidationErrorMessage.INVALID_PARTY_COMPOSITION_MIN_CHILD, errors);
            requiredFieldSet = true;
        }
        else if (deepLinkRequestData.getNoOfChildren() > maxNoOfChild)
        {
            addToErrors(ValidationErrorMessage.INVALID_PARTY_COMPOSITION_MAX_CHILD, errors);
            requiredFieldSet = true;
        }
        else if (infantCount > adults)
        {
            addToErrors(ValidationErrorMessage.INVALID_PARTY_COMPOSITION_INFANT, errors);
            requiredFieldSet = true;
        }
        else if ((deepLinkRequestData.getNoOfChildren() > ZERO) && (adults == ZERO))
        {
            addToErrors(ValidationErrorMessage.INVALID_ADULTS_FOR_CHILDREN, errors);
            requiredFieldSet = true;

        }
    }

    /**
     * @param adults
     * @param searchPanelComponentModel
     * @param errors
     */
    private void validateAdults(final int adults, final SearchPanelComponentModel searchPanelComponentModel,
            final List<ErrorData> errors)
    {

        final int maxNoofAdults = searchPanelComponentModel.getMaxNoOfAdult().intValue();

        // Its been agreed by the business that the value ONE will be hardcoded
        // here and also its a valid search if its 1 adult.
        if (adults < ONE)
        {
            addToErrors(ValidationErrorMessage.INVALID_PARTY_COMPOSITION_MIN_ADULT, errors);
            requiredFieldSet = true;
        }
        else if (adults > maxNoofAdults)
        {
            addToErrors(ValidationErrorMessage.INVALID_PARTY_COMPOSITION_MAX_ADULT, errors);
            requiredFieldSet = true;
        }
    }

    /**
     * @param adults
     * @param searchPanelComponentModel
     * @param errors
     */
    private void validateAdultsForNewSearch(final int adults, final NewSearchPanelComponentModel newsearchPanelComponentModel,
            final List<ErrorData> errors)
    {

        final int maxNoofAdults = newsearchPanelComponentModel.getMaxNoOfAdult().intValue();

        // Its been agreed by the business that the value ONE will be hardcoded
        // here and also its a valid search if its 1 adult.
        if (adults < ONE)
        {
            addToErrors(ValidationErrorMessage.INVALID_PARTY_COMPOSITION_MIN_ADULT, errors);
            requiredFieldSet = true;
        }
        else if (adults > maxNoofAdults)
        {
            addToErrors(ValidationErrorMessage.INVALID_PARTY_COMPOSITION_MAX_ADULT, errors);
            requiredFieldSet = true;
        }
    }

    /**
     * @param childrenAge
     * @param infantAge
     * @return
     */
    private int getInfantCount(final List<Integer> childrenAge, final int infantAge)
    {

        int infantCount = 0;
        if (CollectionUtils.isNotEmpty(childrenAge))
        {
            for (final int age : childrenAge)
            {
                if (age < infantAge)
                {
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
    public void addToErrors(final ValidationErrorMessage errorType, final List<ErrorData> errors)
    {
        final ErrorData errorData = new ErrorData();
        errorData.setCode(errorType.getCode());
        errorData.setDescription(errorType.getDescription());
        errors.add(errorData);
    }

    /**
     * @param requestViewData
     * @return
     */
    public boolean hasError(final DeepLinkRequestViewData requestViewData)
    {
        if ((requestViewData.getErrors().size()) > 0)
        {
            return true;
        }
        return false;
    }

    /**
     * @return the requiredFieldSet
     */
    public boolean isRequiredFieldSet()
    {
        return requiredFieldSet;
    }

    /**
     * @param requiredFieldSet
     *           the requiredFieldSet to set
     */
    public void setRequiredFieldSet(final boolean requiredFieldSet)
    {
        this.requiredFieldSet = requiredFieldSet;
    }

    /**
     * This method shall make use of antisamy-esapi.xml to determine if the content passed is safe. any change in the
     * validation schemes need to be done in the antisamy-esapi.xml.
     *
     * @param contentToBeValidated
     *           the external content to be hosted by cps.
     *
     * @return isSafe the flag to denote if the content is safe or not.
     */
    private static boolean validateSafeHTML(final String contentToBeValidated)
    {
        boolean isSafe = false;
        final AntiSamy antiSamy = new AntiSamy();
        try
        {
            if (null != policy)
            {
                final CleanResults cleanResults = antiSamy.scan(contentToBeValidated, policy);
                isSafe = cleanResults.getNumberOfErrors() == 0;
                if (!isSafe)
                {
                    LOGGER.info("The content sent is deemed unsafe as it" + " is suspected to contain malicious/unsafe code");
                }
            }
        }
        catch (final ScanException e)
        {
            LOGGER.info("There was a problem while " + "scanning the content using esapi antisamy policy",e);
        }
        catch (final PolicyException e)
        {
            LOGGER.info("There was a problem in the " + "esapi antisamy policy",e);
        }
        return isSafe;
    }


    /**
     * @param deepLinkRequestData
     * @param errors
     * @param holidayFinderComp
     */
    public void validateForSmerchMobile(final DeepLinkRequestData deepLinkRequestData, final List<ErrorData> errors,
            final HolidayFinderComponentModel holidayFinderComp)
    {

        if (deepLinkRequestData == null)
        {
            addToErrors(ValidationErrorMessage.INVALID_INPUT, errors);
            requiredFieldSet = true;
        }
        validateDataTamper(deepLinkRequestData, errors);
        airportUnitValidationForSmerch(deepLinkRequestData, errors);
        validateWhen(deepLinkRequestData, errors);
        validatePartyCompositionForHolidayFinder(deepLinkRequestData, errors, holidayFinderComp);

    }

    /**
     * @param deepLinkRequestData
     * @param errors
     * @param holidayFinderComp
     */
    public void validateForHolidayFinder(final DeepLinkRequestData deepLinkRequestData, final List<ErrorData> errors,
            final HolidayFinderComponentModel holidayFinderComp)
    {



        if (deepLinkRequestData == null)
        {
            addToErrors(ValidationErrorMessage.INVALID_INPUT, errors);
            requiredFieldSet = true;
        }
        validateDataTamper(deepLinkRequestData, errors);
        airportUnitValidation(deepLinkRequestData, errors);
        validateWhen(deepLinkRequestData, errors);
        validatePartyCompositionForHolidayFinder(deepLinkRequestData, errors, holidayFinderComp);


    }


    /**
     * @param deepLinkRequestData
     * @param errors
     * @param holidayFinderComp
     */
    private void validatePartyCompositionForHolidayFinder(final DeepLinkRequestData deepLinkRequestData,
            final List<ErrorData> errors, final HolidayFinderComponentModel holidayFinderComp)
    {


        final int adults = deepLinkRequestData.getNoOfAdults() + deepLinkRequestData.getNoOfSeniors();

        final int noOfPassenger = deepLinkRequestData.getNoOfChildren() + adults;
        int totalvalidPartySize = 0;

        if (holidayFinderComp != null && holidayFinderComp.getMaxPaxConfig() != null)
        {
            totalvalidPartySize = holidayFinderComp.getMaxPaxConfig().intValue();
        }

        if (noOfPassenger <= ZERO)
        {
            addToErrors(ValidationErrorMessage.INVALID_PARTY_COMPOSITION, errors);
        }
        else if (totalvalidPartySize < noOfPassenger)
        {
            addToErrors(ValidationErrorMessage.INVALID_PARTY_COMPOSITION_MAX, errors);
        }
        else
        {
            validateAdultsForHolidayFinder(adults, holidayFinderComp, errors);
            validateChildrenForHolidayFinder(deepLinkRequestData, holidayFinderComp, errors);
        }


    }


    /**
     * @param deepLinkRequestData
     * @param holidayFinderComp
     * @param errors
     */
    private void validateChildrenForHolidayFinder(final DeepLinkRequestData deepLinkRequestData,
            final HolidayFinderComponentModel holidayFinderComp, final List<ErrorData> errors)
    {


        final int minNoOfChild = holidayFinderComp.getMinNoOfChild().intValue();
        final int maxNoOfChild = holidayFinderComp.getMaxNoOfChild().intValue();
        final int adults = deepLinkRequestData.getNoOfAdults() + deepLinkRequestData.getNoOfSeniors();
        final int infantCount = getInfantCount(deepLinkRequestData.getChildrenAge(), holidayFinderComp.getInfantAge().intValue());

        if (deepLinkRequestData.getNoOfChildren() < minNoOfChild)
        {
            addToErrors(ValidationErrorMessage.INVALID_PARTY_COMPOSITION_MIN_CHILD, errors);
            requiredFieldSet = true;
        }
        else if (deepLinkRequestData.getNoOfChildren() > maxNoOfChild)
        {
            addToErrors(ValidationErrorMessage.INVALID_PARTY_COMPOSITION_MAX_CHILD, errors);
            requiredFieldSet = true;
        }
        else if (infantCount > adults)
        {
            addToErrors(ValidationErrorMessage.INVALID_PARTY_COMPOSITION_INFANT, errors);
            requiredFieldSet = true;
        }
        else if ((deepLinkRequestData.getNoOfChildren() > ZERO) && (adults == ZERO))
        {
            addToErrors(ValidationErrorMessage.INVALID_ADULTS_FOR_CHILDREN, errors);
            requiredFieldSet = true;

        }


    }


    /**
     * @param adults
     * @param holidayFinderComp
     * @param errors
     */
    private void validateAdultsForHolidayFinder(final int adults, final HolidayFinderComponentModel holidayFinderComp,
            final List<ErrorData> errors)
    {


        final int maxNoofAdults = holidayFinderComp.getMaxNoOfAdult().intValue();

        // Its been agreed by the business that the value ONE will be hardcoded
        // here and also its a valid search if its 1 adult.
        if (adults < ONE)
        {
            addToErrors(ValidationErrorMessage.INVALID_PARTY_COMPOSITION_MIN_ADULT, errors);
            requiredFieldSet = true;
        }
        else if (adults > maxNoofAdults)
        {
            addToErrors(ValidationErrorMessage.INVALID_PARTY_COMPOSITION_MAX_ADULT, errors);
            requiredFieldSet = true;
        }

    }





}
