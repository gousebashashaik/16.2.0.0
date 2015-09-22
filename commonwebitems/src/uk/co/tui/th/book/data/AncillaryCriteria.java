/*
 * Copyright (C)2006 TUI UK Ltd
 *
 * TUI UK Ltd,
 * Columbus House,
 * Westwood Way,
 * Westwood Business Park,
 * Coventry,
 * United Kingdom
 * CV4 8TT
 *
 * Telephone - (024)76282828
 *
 * All rights reserved - The copyright notice above does not evidence
 * any actual or intended publication of this source code.
 *
 * $RCSfile:   AncillaryCriteria.java$
 *
 * $Revision:   $
 *
 * $Date:   Nov 12, 2013$
 *
 * Author: madhumathi.m
 *
 *
 * $Log:   $
 */
package uk.co.tui.th.book.data;

/**
 * @author madhumathi.m
 *
 */
public class AncillaryCriteria {
    /**
     * The ExtraFacility Code.
     */
    private String extraCode;

    /**
     * The selected extra Quantity.
     */
    private int quantity;

    /**
     * The Category code.
     */
    private String categoryCode;

    /**
     * The Category code.
     */
    private String aliasSuperCategoryCode;

    public String getAliasSuperCategoryCode() {
        return aliasSuperCategoryCode;
    }

    public void setAliasSuperCategoryCode(String aliasSuperCategoryCode) {
        this.aliasSuperCategoryCode = aliasSuperCategoryCode;
    }

    /**
     * @return the extraCode
     */
    public String getExtraCode() {
        return extraCode;
    }

    /**
     * @param extraCode
     *            the extraCode to set
     */
    public void setExtraCode(String extraCode) {
        this.extraCode = extraCode;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity
     *            the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the categoryCode
     */
    public String getCategoryCode() {
        return categoryCode;
    }

    /**
     * @param categoryCode
     *            the categoryCode to set
     */
    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

}
