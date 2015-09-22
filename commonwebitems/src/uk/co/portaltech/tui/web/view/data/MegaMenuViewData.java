/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * @author sreenivas.v
 * */
public class MegaMenuViewData
{



    /** Holds the values for Mega menu components. */


    private String collectionCode1 = StringUtils.EMPTY;
    private String collectionCode2 = StringUtils.EMPTY;
    private String collectionCode3 = StringUtils.EMPTY;
    private String collectionCode4 = StringUtils.EMPTY;
    private String collectionCode5 = StringUtils.EMPTY;
    private String collectionCode6 = StringUtils.EMPTY;
    private String collectionLink1 = StringUtils.EMPTY;
    private String collectionLink2 = StringUtils.EMPTY;
    private String collectionLink3 = StringUtils.EMPTY;
    private String collectionLink4 = StringUtils.EMPTY;
    private String collectionLink5 = StringUtils.EMPTY;
    private String collectionLink6 = StringUtils.EMPTY;
    private String bestForLink1 = StringUtils.EMPTY;
    private String bestForLink2 = StringUtils.EMPTY;
    private String bestForLink3 = StringUtils.EMPTY;
    private String bestForLink4 = StringUtils.EMPTY;
    private String bestForLink5 = StringUtils.EMPTY;
    private String bestForLink6 = StringUtils.EMPTY;
    private String bestForLink7 = StringUtils.EMPTY;
    private String bestForLink8 = StringUtils.EMPTY;
    private String bestForLink9 = StringUtils.EMPTY;
    private String bestForLink10 = StringUtils.EMPTY;

    private String bestForTitle1 = StringUtils.EMPTY;
    private String bestForTitle2 = StringUtils.EMPTY;
    private String bestForTitle3 = StringUtils.EMPTY;
    private String bestForTitle4 = StringUtils.EMPTY;
    private String bestForTitle5 = StringUtils.EMPTY;
    private String bestForTitle6 = StringUtils.EMPTY;
    private String bestForTitle7 = StringUtils.EMPTY;
    private String bestForTitle8 = StringUtils.EMPTY;
    private String bestForTitle9 = StringUtils.EMPTY;
    private String bestForTitle10 = StringUtils.EMPTY;
    private String location1 = StringUtils.EMPTY;
    private String location2 = StringUtils.EMPTY;
    private String location3 = StringUtils.EMPTY;
    private String location4 = StringUtils.EMPTY;
    private String locationLink1 = StringUtils.EMPTY;
    private String locationLink2 = StringUtils.EMPTY;
    private String locationLink3 = StringUtils.EMPTY;
    private String locationLink4 = StringUtils.EMPTY;
    private String deals1 = StringUtils.EMPTY;
    private String deals2 = StringUtils.EMPTY;
    private String deals3 = StringUtils.EMPTY;
    private String dealsLink1 = StringUtils.EMPTY;
    private String dealsLink2 = StringUtils.EMPTY;
    private String dealsLink3 = StringUtils.EMPTY;

    /* For collection component common attributes */
    private String collectionHeading = StringUtils.EMPTY;
    private String collectionLinkTitle = StringUtils.EMPTY;
    private String collectionLink = StringUtils.EMPTY;
    private String collectionName = StringUtils.EMPTY;


    /* For Bestfor component common attributes */
    private String bestforHeading = StringUtils.EMPTY;
    private String bestforLinkTitle = StringUtils.EMPTY;
    private String bestforLink = StringUtils.EMPTY;
    private String bestforName = StringUtils.EMPTY;

    /* For Destination component common attributes */
    private String destinationHeading = StringUtils.EMPTY;
    private String destinationLinkTitle = StringUtils.EMPTY;
    private String destinationLink = StringUtils.EMPTY;
    private String destinationName = StringUtils.EMPTY;

    /* For Deals component common attributes */
    private String dealsHeading = StringUtils.EMPTY;
    private String dealsLinkTitle = StringUtils.EMPTY;
    private String dealsLink = StringUtils.EMPTY;
    private String dealsName = StringUtils.EMPTY;


    /* For collection small images */

    private List<String> imgUrl;





    /**
     * @return the imgUrl
     */
    public List<String> getImgUrl()
    {
        return imgUrl;
    }

    /**
     * @param imgUrl
     *           the imgUrl to set
     */
    public void setImgUrl(final List<String> imgUrl)
    {
        this.imgUrl = imgUrl;
    }

    /**
     * @return the collectionHeading
     */
    public String getCollectionHeading()
    {
        return collectionHeading;
    }

    /**
     * @param collectionHeading
     *           the collectionHeading to set
     */
    public void setCollectionHeading(final String collectionHeading)
    {
        this.collectionHeading = collectionHeading;
    }

    /**
     * @return the collectionLinkTitle
     */
    public String getCollectionLinkTitle()
    {
        return collectionLinkTitle;
    }

    /**
     * @param collectionLinkTitle
     *           the collectionLinkTitle to set
     */
    public void setCollectionLinkTitle(final String collectionLinkTitle)
    {
        this.collectionLinkTitle = collectionLinkTitle;
    }

    /**
     * @return the collectionLink
     */
    public String getCollectionLink()
    {
        return collectionLink;
    }

    /**
     * @param collectionLink
     *           the collectionLink to set
     */
    public void setCollectionLink(final String collectionLink)
    {
        this.collectionLink = collectionLink;
    }

    /**
     * @return the collectionName
     */
    public String getCollectionName()
    {
        return collectionName;
    }

    /**
     * @param collectionName
     *           the collectionName to set
     */
    public void setCollectionName(final String collectionName)
    {
        this.collectionName = collectionName;
    }

    /**
     * @return the bestforHeading
     */
    public String getBestforHeading()
    {
        return bestforHeading;
    }

    /**
     * @param bestforHeading
     *           the bestforHeading to set
     */
    public void setBestforHeading(final String bestforHeading)
    {
        this.bestforHeading = bestforHeading;
    }

    /**
     * @return the bestforLinkTitle
     */
    public String getBestforLinkTitle()
    {
        return bestforLinkTitle;
    }

    /**
     * @param bestforLinkTitle
     *           the bestforLinkTitle to set
     */
    public void setBestforLinkTitle(final String bestforLinkTitle)
    {
        this.bestforLinkTitle = bestforLinkTitle;
    }

    /**
     * @return the bestforLink
     */
    public String getBestforLink()
    {
        return bestforLink;
    }

    /**
     * @param bestforLink
     *           the bestforLink to set
     */
    public void setBestforLink(final String bestforLink)
    {
        this.bestforLink = bestforLink;
    }

    /**
     * @return the bestforName
     */
    public String getBestforName()
    {
        return bestforName;
    }

    /**
     * @param bestforName
     *           the bestforName to set
     */
    public void setBestforName(final String bestforName)
    {
        this.bestforName = bestforName;
    }

    /**
     * @return the destinationHeading
     */
    public String getDestinationHeading()
    {
        return destinationHeading;
    }

    /**
     * @param destinationHeading
     *           the destinationHeading to set
     */
    public void setDestinationHeading(final String destinationHeading)
    {
        this.destinationHeading = destinationHeading;
    }

    /**
     * @return the destinationLinkTitle
     */
    public String getDestinationLinkTitle()
    {
        return destinationLinkTitle;
    }

    /**
     * @param destinationLinkTitle
     *           the destinationLinkTitle to set
     */
    public void setDestinationLinkTitle(final String destinationLinkTitle)
    {
        this.destinationLinkTitle = destinationLinkTitle;
    }

    /**
     * @return the destinationLink
     */
    public String getDestinationLink()
    {
        return destinationLink;
    }

    /**
     * @param destinationLink
     *           the destinationLink to set
     */
    public void setDestinationLink(final String destinationLink)
    {
        this.destinationLink = destinationLink;
    }

    /**
     * @return the destinationName
     */
    public String getDestinationName()
    {
        return destinationName;
    }

    /**
     * @param destinationName
     *           the destinationName to set
     */
    public void setDestinationName(final String destinationName)
    {
        this.destinationName = destinationName;
    }

    /**
     * @return the dealsHeading
     */
    public String getDealsHeading()
    {
        return dealsHeading;
    }

    /**
     * @param dealsHeading
     *           the dealsHeading to set
     */
    public void setDealsHeading(final String dealsHeading)
    {
        this.dealsHeading = dealsHeading;
    }

    /**
     * @return the dealsLinkTitle
     */
    public String getDealsLinkTitle()
    {
        return dealsLinkTitle;
    }

    /**
     * @param dealsLinkTitle
     *           the dealsLinkTitle to set
     */
    public void setDealsLinkTitle(final String dealsLinkTitle)
    {
        this.dealsLinkTitle = dealsLinkTitle;
    }

    /**
     * @return the dealsLink
     */
    public String getDealsLink()
    {
        return dealsLink;
    }

    /**
     * @param dealsLink
     *           the dealsLink to set
     */
    public void setDealsLink(final String dealsLink)
    {
        this.dealsLink = dealsLink;
    }

    /**
     * @return the dealsName
     */
    public String getDealsName()
    {
        return dealsName;
    }

    /**
     * @param dealsName
     *           the dealsName to set
     */
    public void setDealsName(final String dealsName)
    {
        this.dealsName = dealsName;
    }

    /**
     * @return the collectionCode1
     */
    public String getCollectionCode1()
    {
        return collectionCode1;
    }

    /**
     * @param collectionCode1
     *           the collectionCode1 to set
     */
    public void setCollectionCode1(final String collectionCode1)
    {
        this.collectionCode1 = collectionCode1;
    }

    /**
     * @return the collectionCode2
     */
    public String getCollectionCode2()
    {
        return collectionCode2;
    }

    /**
     * @param collectionCode2
     *           the collectionCode2 to set
     */
    public void setCollectionCode2(final String collectionCode2)
    {
        this.collectionCode2 = collectionCode2;
    }

    /**
     * @return the collectionCode3
     */
    public String getCollectionCode3()
    {
        return collectionCode3;
    }

    /**
     * @param collectionCode3
     *           the collectionCode3 to set
     */
    public void setCollectionCode3(final String collectionCode3)
    {
        this.collectionCode3 = collectionCode3;
    }

    /**
     * @return the collectionCode4
     */
    public String getCollectionCode4()
    {
        return collectionCode4;
    }

    /**
     * @param collectionCode4
     *           the collectionCode4 to set
     */
    public void setCollectionCode4(final String collectionCode4)
    {
        this.collectionCode4 = collectionCode4;
    }

    /**
     * @return the collectionCode5
     */
    public String getCollectionCode5()
    {
        return collectionCode5;
    }

    /**
     * @param collectionCode5
     *           the collectionCode5 to set
     */
    public void setCollectionCode5(final String collectionCode5)
    {
        this.collectionCode5 = collectionCode5;
    }

    /**
     * @return the collectionCode6
     */
    public String getCollectionCode6()
    {
        return collectionCode6;
    }

    /**
     * @param collectionCode6
     *           the collectionCode6 to set
     */
    public void setCollectionCode6(final String collectionCode6)
    {
        this.collectionCode6 = collectionCode6;
    }

    /**
     * @return the collectionLink1
     */
    public String getCollectionLink1()
    {
        return collectionLink1;
    }

    /**
     * @param collectionLink1
     *           the collectionLink1 to set
     */
    public void setCollectionLink1(final String collectionLink1)
    {
        this.collectionLink1 = collectionLink1;
    }

    /**
     * @return the collectionLink2
     */
    public String getCollectionLink2()
    {
        return collectionLink2;
    }

    /**
     * @param collectionLink2
     *           the collectionLink2 to set
     */
    public void setCollectionLink2(final String collectionLink2)
    {
        this.collectionLink2 = collectionLink2;
    }

    /**
     * @return the collectionLink3
     */
    public String getCollectionLink3()
    {
        return collectionLink3;
    }

    /**
     * @param collectionLink3
     *           the collectionLink3 to set
     */
    public void setCollectionLink3(final String collectionLink3)
    {
        this.collectionLink3 = collectionLink3;
    }

    /**
     * @return the collectionLink4
     */
    public String getCollectionLink4()
    {
        return collectionLink4;
    }

    /**
     * @param collectionLink4
     *           the collectionLink4 to set
     */
    public void setCollectionLink4(final String collectionLink4)
    {
        this.collectionLink4 = collectionLink4;
    }

    /**
     * @return the collectionLink5
     */
    public String getCollectionLink5()
    {
        return collectionLink5;
    }

    /**
     * @param collectionLink5
     *           the collectionLink5 to set
     */
    public void setCollectionLink5(final String collectionLink5)
    {
        this.collectionLink5 = collectionLink5;
    }

    /**
     * @return the collectionLink6
     */
    public String getCollectionLink6()
    {
        return collectionLink6;
    }

    /**
     * @param collectionLink6
     *           the collectionLink6 to set
     */
    public void setCollectionLink6(final String collectionLink6)
    {
        this.collectionLink6 = collectionLink6;
    }

    /**
     * @return the bestForLink1
     */
    public String getBestForLink1()
    {
        return bestForLink1;
    }

    /**
     * @param bestForLink1
     *           the bestForLink1 to set
     */
    public void setBestForLink1(final String bestForLink1)
    {
        this.bestForLink1 = bestForLink1;
    }

    /**
     * @return the bestForLink2
     */
    public String getBestForLink2()
    {
        return bestForLink2;
    }

    /**
     * @param bestForLink2
     *           the bestForLink2 to set
     */
    public void setBestForLink2(final String bestForLink2)
    {
        this.bestForLink2 = bestForLink2;
    }

    /**
     * @return the bestForLink3
     */
    public String getBestForLink3()
    {
        return bestForLink3;
    }

    /**
     * @param bestForLink3
     *           the bestForLink3 to set
     */
    public void setBestForLink3(final String bestForLink3)
    {
        this.bestForLink3 = bestForLink3;
    }

    /**
     * @return the bestForLink4
     */
    public String getBestForLink4()
    {
        return bestForLink4;
    }

    /**
     * @param bestForLink4
     *           the bestForLink4 to set
     */
    public void setBestForLink4(final String bestForLink4)
    {
        this.bestForLink4 = bestForLink4;
    }

    /**
     * @return the bestForLink5
     */
    public String getBestForLink5()
    {
        return bestForLink5;
    }

    /**
     * @param bestForLink5
     *           the bestForLink5 to set
     */
    public void setBestForLink5(final String bestForLink5)
    {
        this.bestForLink5 = bestForLink5;
    }

    /**
     * @return the bestForLink6
     */
    public String getBestForLink6()
    {
        return bestForLink6;
    }

    /**
     * @param bestForLink6
     *           the bestForLink6 to set
     */
    public void setBestForLink6(final String bestForLink6)
    {
        this.bestForLink6 = bestForLink6;
    }

    /**
     * @return the bestForLink7
     */
    public String getBestForLink7()
    {
        return bestForLink7;
    }

    /**
     * @param bestForLink7
     *           the bestForLink7 to set
     */
    public void setBestForLink7(final String bestForLink7)
    {
        this.bestForLink7 = bestForLink7;
    }

    /**
     * @return the bestForLink8
     */
    public String getBestForLink8()
    {
        return bestForLink8;
    }

    /**
     * @param bestForLink8
     *           the bestForLink8 to set
     */
    public void setBestForLink8(final String bestForLink8)
    {
        this.bestForLink8 = bestForLink8;
    }

    /**
     * @return the bestForLink9
     */
    public String getBestForLink9()
    {
        return bestForLink9;
    }

    /**
     * @param bestForLink9
     *           the bestForLink9 to set
     */
    public void setBestForLink9(final String bestForLink9)
    {
        this.bestForLink9 = bestForLink9;
    }

    /**
     * @return the bestForLink10
     */
    public String getBestForLink10()
    {
        return bestForLink10;
    }

    /**
     * @param bestForLink10
     *           the bestForLink10 to set
     */
    public void setBestForLink10(final String bestForLink10)
    {
        this.bestForLink10 = bestForLink10;
    }

    /**
     * @return the bestForTitle1
     */
    public String getBestForTitle1()
    {
        return bestForTitle1;
    }

    /**
     * @param bestForTitle1
     *           the bestForTitle1 to set
     */
    public void setBestForTitle1(final String bestForTitle1)
    {
        this.bestForTitle1 = bestForTitle1;
    }

    /**
     * @return the bestForTitle2
     */
    public String getBestForTitle2()
    {
        return bestForTitle2;
    }

    /**
     * @param bestForTitle2
     *           the bestForTitle2 to set
     */
    public void setBestForTitle2(final String bestForTitle2)
    {
        this.bestForTitle2 = bestForTitle2;
    }

    /**
     * @return the bestForTitle3
     */
    public String getBestForTitle3()
    {
        return bestForTitle3;
    }

    /**
     * @param bestForTitle3
     *           the bestForTitle3 to set
     */
    public void setBestForTitle3(final String bestForTitle3)
    {
        this.bestForTitle3 = bestForTitle3;
    }

    /**
     * @return the bestForTitle4
     */
    public String getBestForTitle4()
    {
        return bestForTitle4;
    }

    /**
     * @param bestForTitle4
     *           the bestForTitle4 to set
     */
    public void setBestForTitle4(final String bestForTitle4)
    {
        this.bestForTitle4 = bestForTitle4;
    }

    /**
     * @return the bestForTitle5
     */
    public String getBestForTitle5()
    {
        return bestForTitle5;
    }

    /**
     * @param bestForTitle5
     *           the bestForTitle5 to set
     */
    public void setBestForTitle5(final String bestForTitle5)
    {
        this.bestForTitle5 = bestForTitle5;
    }

    /**
     * @return the bestForTitle6
     */
    public String getBestForTitle6()
    {
        return bestForTitle6;
    }

    /**
     * @param bestForTitle6
     *           the bestForTitle6 to set
     */
    public void setBestForTitle6(final String bestForTitle6)
    {
        this.bestForTitle6 = bestForTitle6;
    }

    /**
     * @return the bestForTitle7
     */
    public String getBestForTitle7()
    {
        return bestForTitle7;
    }

    /**
     * @param bestForTitle7
     *           the bestForTitle7 to set
     */
    public void setBestForTitle7(final String bestForTitle7)
    {
        this.bestForTitle7 = bestForTitle7;
    }

    /**
     * @return the bestForTitle8
     */
    public String getBestForTitle8()
    {
        return bestForTitle8;
    }

    /**
     * @param bestForTitle8
     *           the bestForTitle8 to set
     */
    public void setBestForTitle8(final String bestForTitle8)
    {
        this.bestForTitle8 = bestForTitle8;
    }

    /**
     * @return the bestForTitle9
     */
    public String getBestForTitle9()
    {
        return bestForTitle9;
    }

    /**
     * @param bestForTitle9
     *           the bestForTitle9 to set
     */
    public void setBestForTitle9(final String bestForTitle9)
    {
        this.bestForTitle9 = bestForTitle9;
    }

    /**
     * @return the bestForTitle10
     */
    public String getBestForTitle10()
    {
        return bestForTitle10;
    }

    /**
     * @param bestForTitle10
     *           the bestForTitle10 to set
     */
    public void setBestForTitle10(final String bestForTitle10)
    {
        this.bestForTitle10 = bestForTitle10;
    }

    /**
     * @return the location1
     */
    public String getLocation1()
    {
        return location1;
    }

    /**
     * @param location1
     *           the location1 to set
     */
    public void setLocation1(final String location1)
    {
        this.location1 = location1;
    }

    /**
     * @return the location2
     */
    public String getLocation2()
    {
        return location2;
    }

    /**
     * @param location2
     *           the location2 to set
     */
    public void setLocation2(final String location2)
    {
        this.location2 = location2;
    }

    /**
     * @return the location3
     */
    public String getLocation3()
    {
        return location3;
    }

    /**
     * @param location3
     *           the location3 to set
     */
    public void setLocation3(final String location3)
    {
        this.location3 = location3;
    }

    /**
     * @return the location4
     */
    public String getLocation4()
    {
        return location4;
    }

    /**
     * @param location4
     *           the location4 to set
     */
    public void setLocation4(final String location4)
    {
        this.location4 = location4;
    }

    /**
     * @return the locationLink1
     */
    public String getLocationLink1()
    {
        return locationLink1;
    }

    /**
     * @param locationLink1
     *           the locationLink1 to set
     */
    public void setLocationLink1(final String locationLink1)
    {
        this.locationLink1 = locationLink1;
    }

    /**
     * @return the locationLink2
     */
    public String getLocationLink2()
    {
        return locationLink2;
    }

    /**
     * @param locationLink2
     *           the locationLink2 to set
     */
    public void setLocationLink2(final String locationLink2)
    {
        this.locationLink2 = locationLink2;
    }

    /**
     * @return the locationLink3
     */
    public String getLocationLink3()
    {
        return locationLink3;
    }

    /**
     * @param locationLink3
     *           the locationLink3 to set
     */
    public void setLocationLink3(final String locationLink3)
    {
        this.locationLink3 = locationLink3;
    }

    /**
     * @return the locationLink4
     */
    public String getLocationLink4()
    {
        return locationLink4;
    }

    /**
     * @param locationLink4
     *           the locationLink4 to set
     */
    public void setLocationLink4(final String locationLink4)
    {
        this.locationLink4 = locationLink4;
    }

    /**
     * @return the deals1
     */
    public String getDeals1()
    {
        return deals1;
    }

    /**
     * @param deals1
     *           the deals1 to set
     */
    public void setDeals1(final String deals1)
    {
        this.deals1 = deals1;
    }

    /**
     * @return the deals2
     */
    public String getDeals2()
    {
        return deals2;
    }

    /**
     * @param deals2
     *           the deals2 to set
     */
    public void setDeals2(final String deals2)
    {
        this.deals2 = deals2;
    }

    /**
     * @return the deals3
     */
    public String getDeals3()
    {
        return deals3;
    }

    /**
     * @param deals3
     *           the deals3 to set
     */
    public void setDeals3(final String deals3)
    {
        this.deals3 = deals3;
    }

    /**
     * @return the dealsLink1
     */
    public String getDealsLink1()
    {
        return dealsLink1;
    }

    /**
     * @param dealsLink1
     *           the dealsLink1 to set
     */
    public void setDealsLink1(final String dealsLink1)
    {
        this.dealsLink1 = dealsLink1;
    }

    /**
     * @return the dealsLink2
     */
    public String getDealsLink2()
    {
        return dealsLink2;
    }

    /**
     * @param dealsLink2
     *           the dealsLink2 to set
     */
    public void setDealsLink2(final String dealsLink2)
    {
        this.dealsLink2 = dealsLink2;
    }

    /**
     * @return the dealsLink3
     */
    public String getDealsLink3()
    {
        return dealsLink3;
    }

    /**
     * @param dealsLink3
     *           the dealsLink3 to set
     */
    public void setDealsLink3(final String dealsLink3)
    {
        this.dealsLink3 = dealsLink3;
    }


}
