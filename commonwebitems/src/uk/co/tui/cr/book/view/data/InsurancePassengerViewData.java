/**
 *
 */
package uk.co.tui.cr.book.view.data;

import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author akshay.an
 *
 */
public class InsurancePassengerViewData {

    /** The selected. */
    private boolean selected;

    /** The name. */
    private String name;

    /** The id. */
    private String id;

    /** The age profile. */
    private List<AgeProfile> ageProfile;

    /** The child. */
    private boolean child;

    /** The age. */
    private String age;

    /** The price. */
    private String price = StringUtils.EMPTY;

    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected
     *            the selected to set
     */
    public void setSelected(final boolean selected) {
        this.selected = selected;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * @return the ageProfile
     */
    public List<AgeProfile> getAgeProfile() {
        return ageProfile;
    }

    /**
     * @param ageProfile
     *            the ageProfile to set
     */
    public void setAgeProfile(final List<AgeProfile> ageProfile) {
        this.ageProfile = ageProfile;
    }

    /**
     * @return the child
     */
    public boolean isChild() {
        return child;
    }

    /**
     * @param child
     *            the child to set
     */
    public void setChild(final boolean child) {
        this.child = child;
    }

    /**
     * @return the age
     */
    public String getAge() {
        return age;
    }

    /**
     * @param age
     *            the age to set
     */
    public void setAge(final String age) {
        this.age = age;
    }

    /**
     * @return the price
     */
    public String getPrice() {
        return price;
    }

    /**
     * @param price
     *            the price to set
     */
    public void setPrice(final String price) {
        this.price = price;
    }
}
