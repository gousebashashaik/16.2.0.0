/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import java.util.List;
import java.util.Map;
import uk.co.portaltech.tui.web.view.data.BoardBasisData;

import org.apache.commons.lang.StringUtils;

/**
 * @author sunilkumar.sahu
 *
 */
public class PriceOverlayViewData {

    private String                          code;
    private String                          accomName;
    private String                          tRating;
    private String                          tRatingCss;
    private Map<String, String>             locationMap;
private List<BoardBasisData> boardBasisDatas;
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
     * @return the accomName
     */
    public String getAccomName() {
        return accomName;
    }
    /**
     * @param accomName the accomName to set
     */
    public void setAccomName(String accomName) {
        this.accomName = accomName;
    }

    /**
     * @return the boardBasisDatas
     */
    public List<BoardBasisData> getBoardBasisDatas() {
        return boardBasisDatas;
    }

    /**
     * @param boardBasisDatas the boardBasisDatas to set
     */
    public void setBoardBasisDatas(List<BoardBasisData> boardBasisDatas) {
        this.boardBasisDatas = boardBasisDatas;
    }

    /**
     * @return the tRating
     */
    public String gettRating() {

        if (tRating != null) {
            if (StringUtils.isNumeric(tRating)) {
                return tRating;
            } else {
                return tRating.replaceAll("\\s", "");
            }
        }
        return tRating;
    }
    /**
     * @param tRating the tRating to set
     */
    public void settRating(String tRating) {
        this.tRating = tRating;
    }
    /**
     * @return the locationMap
     */
    public Map<String, String> getLocationMap() {
        return locationMap;
    }
    /**
     * @param locationMap the locationMap to set
     */
    public void setLocationMap(Map<String, String> locationMap) {
        this.locationMap = locationMap;
    }

    /**
     * @return the tRatingCss
     */
    public String gettRatingCss()
    {
        return tRatingCss;
    }

    /**
     * @param tRatingCss the tRatingCss to set
     */
    public void settRatingCss(String tRatingCss)
    {
        this.tRatingCss = tRatingCss;
    }

}
