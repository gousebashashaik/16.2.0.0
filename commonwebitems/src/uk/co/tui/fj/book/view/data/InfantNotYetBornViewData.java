/**
 *
 */
package uk.co.tui.fj.book.view.data;

import org.apache.commons.lang.StringUtils;

/**
 * This class holds the datas related to fill the firstname,surname and date of
 * birth
 *
 * @author sujith.ks
 *
 */
public class InfantNotYetBornViewData {
    /**
     * Surname
     */
    private String surName =StringUtils.EMPTY;
    /**
     * First name
     */
    private String firstName =StringUtils.EMPTY;
    /**
     * Day in Infant's default date of birth
     */
    private String day =StringUtils.EMPTY;
    /**
     * Month in Infant's default date of birth
     */
    private String month =StringUtils.EMPTY;
    /**
     * Year in Infant's default date of birth
     */
    private String year =StringUtils.EMPTY;

    /**
     * Year in Infant's not yet born alert data
     */
    private String alertInfantNotYetBorn =StringUtils.EMPTY;

    /**
     * Title
     */
    private String title =StringUtils.EMPTY;

    /**
     * Gender
     */
    private String gender =StringUtils.EMPTY;

    /**
     * @return the surName
     */
    public String getSurName() {
        return surName;
    }
    /**
     * @param surName the surName to set
     */
    public void setSurName(String surName) {
        this.surName = surName;
    }
    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }
    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    /**
     * @return the day
     */
    public String getDay() {
        return day;
    }
    /**
     * @param day the day to set
     */
    public void setDay(String day) {
        this.day = day;
    }
    /**
     * @return the month
     */
    public String getMonth() {
        return month;
    }
    /**
     * @param month the month to set
     */
    public void setMonth(String month) {
        this.month = month;
    }
    /**
     * @return the year
     */
    public String getYear() {
        return year;
    }
    /**
     * @param year the year to set
     */
    public void setYear(String year) {
        this.year = year;
    }


    /**
     * @return the alertInfantNotYetBorn
     */
    public String getAlertInfantNotYetBorn() {
        return alertInfantNotYetBorn;
    }
    /**
     * @param alertInfantNotYetBorn the alertInfantNotYetBorn to set
     */
    public void setAlertInfantNotYetBorn(String alertInfantNotYetBorn) {
        this.alertInfantNotYetBorn = alertInfantNotYetBorn;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

}
