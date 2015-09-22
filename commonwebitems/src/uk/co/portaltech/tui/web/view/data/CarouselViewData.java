/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

/**
 * @author Abi
 *
 */
public class CarouselViewData {
    private String name;
    private String code;
    private String lookUpTypeCode;
    private Integer visibleItems;





    public CarouselViewData(){
        super();
    }

    /**
     * @param name
     * @param code
     * @param visibleItems
     */
    public CarouselViewData(String name, String code, Integer visibleItems) {
        super();
        this.name = name;
        this.code = code;
        this.visibleItems = visibleItems;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }
    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * @return the lookUpTypeCode
     */
    public String getLookUpTypeCode() {
        return lookUpTypeCode;
    }
    /**
     * @param lookUpTypeCode the lookUpTypeCode to set
     */
    public void setLookUpTypeCode(String lookUpTypeCode) {
        this.lookUpTypeCode = lookUpTypeCode;
    }
    /**
     * @return the visibleItems
     */
    public Integer getVisibleItems() {
        return visibleItems;
    }
    /**
     * @param visibleItems the visibleItems to set
     */
    public void setVisibleItems(Integer visibleItems) {
        this.visibleItems = visibleItems;
    }

}
