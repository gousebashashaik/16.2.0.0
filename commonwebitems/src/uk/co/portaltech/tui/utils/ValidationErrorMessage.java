/**
 *
 */

package uk.co.portaltech.tui.utils;

/**
 *
 */
public enum ValidationErrorMessage
{




    INVALID_INPUT("INVALID_INPUT", "Search Critirea cannot be null"), INVALID_AIRPORT("INVALID_AIRPORT",
            "Airports should belong to UK"), INVALID_AIRPORT_UNITS("INVALID_AIRPORT_UNITS",
            "Departure Aiport or Destination cannot be null"), INVALID_WHEN("INVALID_WHEN", "Date cannot be null"), INVALID_WHEN_FORMAT(
            "INVALID_WHEN_FORMAT", "Invalid Date Format"), INVALID_WHEN_DATE("INVALID_WHEN_DATE",
            "Date should be greater than todays date"), INVALID_PARTY_COMPOSITION("INVALID_PARTY_COMPOSITION",
            "Invalid party composition"), INVALID_PARTY_COMPOSITION_MAX("INVALID_PARTY_COMPOSITION_MAX",
            "Total Party Size should not exceed Max party size allowed"), INVALID_PARTY_COMPOSITION_MIN_ADULT(
            "INVALID_PARTY_COMPOSITION_MIN_ADULT", "Number of Adults should be greater than or equal to Min Adult size allowed "), INVALID_PARTY_COMPOSITION_MAX_ADULT(
            "INVALID_PARTY_COMPOSITION_MAX_ADULT", "Number of adults should not exceed  Max aduluts size allowed"), INVALID_PARTY_COMPOSITION_MIN_CHILD(
            "INVALID_PARTY_COMPOSITION_MIN_CHILD", "Number of children should be greater than or equal Min child size allowed"), INVALID_PARTY_COMPOSITION_MAX_CHILD(
            "INVALID_PARTY_COMPOSITION_MAX_CHILD", "Number of children should not exceed the Max children size allowed"), INVALID_PARTY_COMPOSITION_INFANT(
            "INVALID_PARTY_COMPOSITION_INFANT",
            "Number of infants needs to be the same as or less than the number of Adults or Senior Citizens"), INVALID_ADULTS_FOR_CHILDREN(
            "INVALID_ADULTS_FOR_CHILDREN", "Atleast one adult or senior citizen should be there"), INVALID_UK_AIRPORT(
            "INVALID_UK_AIRPORT", "Only UK airports allowed for where from"), NO_ROUTE_FOUND_ON_FROM_AIRPORT(
            "NO_ROUTE_FOUND_ON_FROM_AIRPORT", "No Route Found for selected Dates from airport selected"), NO_ROUTE_FOUND_TO_FROM_AIRPORT(
            "NO_ROUTE_FOUND_TO_FROM_AIRPORT", "no route found to selected destination from selected airport"), NO_ROUTE_FOUND_ON_FROM_UNIT(
            "NO_ROUTE_FOUND_ON_FROM_UNIT", "No Route Found for selected Dates from unit selected"), NO_ROUTE_FOUND_TO_FROM_UNIT(
            "NO_ROUTE_FOUND_TO_FROM_UNIT", "no route found to selected destination from selected unit"), TAMPER_DATA_FOUND(
            "TAMPER_DATA_FOUND", "Tamper data found in a form field"), EMPTY_AIRPORT_OR_UNIT("EMPTY_AIRPORT_OR_UNIT",
            "Airports OR Unit should not be empty"),
    //For customer account
    EMAIL("EMAIL","Sorry, we can’t find an account that matches this email address."),
    ACCOUNTEXISTWITHEMAILADDRESS("ACCOUNTEXISTWITHEMAILADDRESS","We've already got an account with this email address."),
    FIRSTNAME("FIRSTNAME","We don’t have an account with this name. Please check and try again."),
    SURNAME("SURNAME","We don’t have an account with this surname. Please check and try again."),
    PWD("PWD","Sorry, this isn’t a valid password. Please check you’ve got the right sign-in details."),
    INACTIVE("INACTIVE", "inactive"),
    CLOSED("CLOSED","closed"),
    REGISTERED("REGISTERED", "Registered"),
    FAILURE("FAILURE", "Your personal details not updated"),
    SUSPENDED("SUSPENDED", "suspended"),
    SUCCESS("SUCCESS", "Your personal details have been updated"),
    ACCOUNTALREADYEXIST("ACCOUNTALREADYEXIST","Enter another email address as this email address already has an account" ),
    ACTIVE("ACTIVE", "Active");





    private final String code;

    /**
     * @return the code
     */



    private final String description;




    private ValidationErrorMessage(final String code, final String description)
    {

        this.code = code;

        this.description = description;

    }



    public String getDescription()
    {

        return description;

    }

    public String getCode()
    {
        return code;
    }

    @Override
    public String toString()
    {
        return code + ": " + description;
    }




}
