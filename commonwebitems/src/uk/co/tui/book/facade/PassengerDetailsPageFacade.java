/**
 *
 */
package uk.co.tui.book.facade;

import java.util.List;

import uk.co.portaltech.tui.exception.TUIBusinessException;
import uk.co.tui.book.comparator.feedback.Feedback;
import uk.co.tui.book.data.CommunicationPreferencesCriteria;
import uk.co.tui.book.domain.lite.BasePackage;
import uk.co.tui.book.exception.FlightSoldOutException;
import uk.co.tui.book.view.data.AlertViewData;
import uk.co.tui.book.view.data.CommunicationPreferencesViewData;
import uk.co.tui.book.view.data.InfantNotYetBornViewData;
import uk.co.tui.book.view.data.PackageViewData;
import uk.co.tui.book.view.data.PassengerDetailsViewData;
import uk.co.tui.book.view.data.PassengerInsuranceViewData;

/**
 * The Interface PassengerDetailsPageFacade.
 *
 * @author pushparaja.g
 */
public interface PassengerDetailsPageFacade
{

   /**
    * Render package view data.
    *
    */
   PackageViewData renderPackageViewData(final PassengerDetailsViewData paxViewData);

   /**
    * This will provide the detail to be filled for infant not yet born case 1.firstName 2.Surname
    * 3.date of birth
    *
    * @param alertInfantNotBornMessages
    */
   InfantNotYetBornViewData updateInfantNotYetBornViewData(String alertInfantNotBornMessages);

   /**
    * Gets the passenger details.
    *
    * @return the passenger details
    */
   PassengerInsuranceViewData getPassengerDetails();

   /**
    * Party age val check.
    *
    * @param id int id
    * @param dateofbirth String date of birth
    * @param paxviewdata PassengerInsuranceViewData
    * @return List AlertViewData list
    * @throws TUIBusinessException the tUI business exception
    */
   List<AlertViewData> partyAgeValCheck(int id, String dateofbirth,
      PassengerDetailsViewData paxviewdata) throws TUIBusinessException;

   /**
    * Populate passenger static content view data.
    *
    * @param viewData the view data
    */
   void populatePassengerStaticContentViewData(PassengerDetailsViewData viewData);

   /**
    * Populate data protection content.
    *
    * @param viewData the view data
    */
   void populateDataProtectionContent(PassengerDetailsViewData viewData);

   /**
    * To get communication preferences from packageModel
    *
    * @return communicationPreferencesData
    */

   CommunicationPreferencesViewData getCommunicationPreferences();

   /**
    * To update communication preferences in packageModel
    *
    * @param communicationPreferencesCriteria
    */
   void updateCommunicationPreferences(
      CommunicationPreferencesCriteria communicationPreferencesCriteria);

   /**
    * Creates the communication preferences.
    */
   void createCommunicationPreferences();

   /**
    * Perform info book.
    *
    * @param pkg the pkg
    * @return the list
    * @throws TUIBusinessException the tUI business exception
    * @throws FlightSoldOutException
    */
   List<Feedback> performInfoBook(BasePackage pkg) throws TUIBusinessException,
      FlightSoldOutException;

   String getUpdatedAge(String dateofbirth);

}
