/**
 *
 */
package uk.co.tui.shortlist.view.data;

import org.apache.commons.lang.StringUtils;

/**
 * @author Sravani
 *
 */
public class SavedHolidayPassengerViewData
{

   /**
    * Represents id of the passenger.
    */
   private int identifier;

   /**
    * Represents the type of the passenger like adult/child.
    */
   private String type = StringUtils.EMPTY;

   /**
    * Represents the age of passenger.
    */
   private int age;

   /**
    * Represents the age of passenger.
    */
   private int passengerCount;

   /**
    * Represents the Meal extra selected byt the passenger.
    */
   private SavedHolidayExtraViewData selectedMealOption;

   /**
    * Represents the baggage extra Selected by the passenger.
    */
   private SavedHolidayExtraViewData selectedBaggageOption;

   /**
    * Represents the baggage extra Selected by the passenger.
    */
   private boolean swimOrStageExtraSelected;

   /**
    * Represents the label for passenger e.g. Adult/Child.
    */
   private String passengerLabel;

   /**
    * @return the type
    */
   public String getType()
   {
      return type;
   }

   /**
    * @param type the type to set
    */
   public void setType(final String type)
   {
      this.type = type;
   }

   /**
    * @return the age
    */
   public int getAge()
   {
      return age;
   }

   /**
    * @param age the age to set
    */
   public void setAge(final int age)
   {
      this.age = age;
   }

   /**
    * @return the identifier
    */
   public int getIdentifier()
   {
      return identifier;
   }

   /**
    * @param identifier the identifier to set
    */
   public void setIdentifier(final int identifier)
   {
      this.identifier = identifier;
   }

   /**
    * @return the selectedMealOption
    */
   public SavedHolidayExtraViewData getSelectedMealOption()
   {
      return this.selectedMealOption;
   }

   /**
    * @param selectedMealOption the selectedMealOption to set
    */
   public void setSelectedMealOption(final SavedHolidayExtraViewData selectedMealOption)
   {
      this.selectedMealOption = selectedMealOption;
   }

   /**
    * @return the selectedBaggageOption
    */
   public SavedHolidayExtraViewData getSelectedBaggageOption()
   {
      return this.selectedBaggageOption;
   }

   /**
    * @param selectedBaggageOption the selectedBaggageOption to set
    */
   public void setSelectedBaggageOption(final SavedHolidayExtraViewData selectedBaggageOption)
   {
      this.selectedBaggageOption = selectedBaggageOption;
   }

   /**
    * @return the swimOrStageExtraSelected
    */
   public boolean isSwimOrStageExtraSelected()
   {
      return swimOrStageExtraSelected;
   }

   /**
    * @param swimOrStageExtraSelected the swimOrStageExtraSelected to set
    */
   public void setSwimOrStageExtraSelected(final boolean swimOrStageExtraSelected)
   {
      this.swimOrStageExtraSelected = swimOrStageExtraSelected;
   }

   /**
    * @return the passengerCount
    */
   public int getPassengerCount()
   {
      return passengerCount;
   }

   /**
    * @param passengerCount the passengerCount to set
    */
   public void setPassengerCount(final int passengerCount)
   {
      this.passengerCount = passengerCount;
   }

   /**
    * @return the passengerLabel
    */
   public String getPassengerLabel()
   {
      return passengerLabel;
   }

   /**
    * @param passengerLabel the passengerLabel to set
    */
   public void setPassengerLabel(final String passengerLabel)
   {
      this.passengerLabel = passengerLabel;
   }

}
