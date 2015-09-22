    /**
 *
 */
package uk.co.tui.fj.book.view.data;

import org.apache.commons.lang.StringUtils;


/**
 * @author madhumathi.m
 *
 */
public class PassengerViewData
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
   private ExtraFacilityViewData selectedMealOption ;

   /**
    * Represents the baggage extra Selected by the passenger.
    */
   private ExtraFacilityViewData selectedBaggageOption ;

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
   public void setType(String type)
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
   public void setAge(int age)
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
   public void setIdentifier(int identifier)
   {
      this.identifier = identifier;
   }

/**
 * @return the selectedMealOption
 */
public ExtraFacilityViewData getSelectedMealOption() {
    return this.selectedMealOption;
}

/**
 * @param selectedMealOption the selectedMealOption to set
 */
public void setSelectedMealOption(ExtraFacilityViewData selectedMealOption) {
    this.selectedMealOption = selectedMealOption;
}

/**
 * @return the selectedBaggageOption
 */
public ExtraFacilityViewData getSelectedBaggageOption() {
    return this.selectedBaggageOption;
}

/**
 * @param selectedBaggageOption the selectedBaggageOption to set
 */
public void setSelectedBaggageOption(ExtraFacilityViewData selectedBaggageOption) {
    this.selectedBaggageOption = selectedBaggageOption;
}

/**
 * @return the swimOrStageExtraSelected
 */
public boolean isSwimOrStageExtraSelected() {
    return swimOrStageExtraSelected;
}

/**
 * @param swimOrStageExtraSelected the swimOrStageExtraSelected to set
 */
public void setSwimOrStageExtraSelected(boolean swimOrStageExtraSelected) {
    this.swimOrStageExtraSelected = swimOrStageExtraSelected;
}

/**
 * @return the passengerCount
 */
public int getPassengerCount() {
    return passengerCount;
}

/**
 * @param passengerCount the passengerCount to set
 */
public void setPassengerCount(int passengerCount) {
    this.passengerCount = passengerCount;
}

/**
 * @return the passengerLabel
 */
public String getPassengerLabel() {
    return passengerLabel;
}

/**
 * @param passengerLabel the passengerLabel to set
 */
public void setPassengerLabel(String passengerLabel) {
    this.passengerLabel = passengerLabel;
}

 }
