/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author deepakkumar.k
 *
 */
public class FlightOptionRequestData {
    /***
     * boardBasisPackageNumber
     */
    private String boardBasisPackageNumber;
    /***
     * month and year
     */
    private String mthyr;
    /***
     * Duration type.
     */
    private String durT;
    /***
     * latesSearch
     */
    private Boolean ls;
    /***
     * Tui Destination code.
     */
    private String tuidesc;
    /***
     *  Day of month.
     */
    private String day;
    /***
     * max. party size.
     */
    private int mps;
    /***
     * Is departure Airport Selected
     */
    private String sda;
    /***
     * party configuration.
     */
    private  String  pconfig;
    /***
     * Total no. of children.
     */
    private int tchd;
    /***
     * official Rating.
     */
    private int rating;
    /***
     *  Is Destnation selected .
     */
    private String dess;
    /***
     * Accommodation type.
     */
    private int act;
    /***
     * Is java script is enabled .
     */
    private Boolean jsen;
    /***
     * Attribute as String.
     */
    private String attrstr;
    /***
     * Total no of infants.
     */
    private int tinf;
    /***
     * month of journey.
     */
    private String mnth;
    /***
     * Business context id.
     */
    private int bc;
    /***
     * Margin days.
     */
    private int margindt ;
    /***
     * Total adults.
     */
    private int tadt;
    /***
     * No of Rooms.
     */
    private String numr;
    /***
     * Departure Margin.
     */
    private int depm;
    /***
     *Duration .
     */
    private int dur;
    /***
     * DateXshell.
     */
    private int dtx;
    /***
     * Duration XShell.
     */
    private int dxsel;
    /***
     * Departure Airport code.
     */
     private String dac;
     /***
         * Location type.
         */
     private int loct;
     /***
         * Total siniors.
         */
     private int tsnr;
     /***
         * year of journey.
         */
     private String year;
     /***
         * Day trip Applicable .
         */
     private Boolean dta;
     /***
         * Iscape package id.
         */
     private String pid;
     /***
         * Selected Date.
         */
     private String sd;
    /**
     * @return the boardBasisPackageNumber
     */

     private String uc;

    public String getBoardBasisPackageNumber() {
        return boardBasisPackageNumber;
    }
    /**
     * @return the rating
     */

    /**
     * @return the rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * @param boardBasisPackageNumber the boardBasisPackageNumber to set
     */
    @Required
    public void setBoardBasisPackageNumber(String boardBasisPackageNumber) {
        this.boardBasisPackageNumber = boardBasisPackageNumber;
    }
    /**
     * @return the mthyr
     */

    public String getMthyr() {
        return mthyr;
    }
    /**
     * @param mthyr the mthyr to set
     */
    public void setMthyr(String mthyr) {
        this.mthyr = mthyr;
    }
    /**
     * @return the durT
     */
    public String getDurT() {
        return durT;
    }
    /**
     * @param durT the durT to set
     */
    @Required
    public void setDurT(String durT) {
        this.durT = durT;
    }
    /**
     * @return the ls
     */
    public Boolean getLs() {
        return ls;
    }
    /**
     * @param ls the ls to set
     */
    @Required
    public void setLs(Boolean ls) {
        this.ls = ls;
    }
    /**
     * @return the tuidesc
     */
    public String getTuidesc() {
        return tuidesc;
    }
    /**
     * @param tuidesc the tuidesc to set
     */
    @Required
    public void setTuidesc(String tuidesc) {
        this.tuidesc = tuidesc;
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
    @Required
    public void setDay(String day) {
        this.day = day;
    }
    /**
     * @return the mps
     */
    public int getMps() {
        return mps;
    }
    /**
     * @param mps the mps to set
     */
    @Required
    public void setMps(int mps) {
        this.mps = mps;
    }
    /**
     * @return the sda
     */
    public String getSda() {
        return sda;
    }
    /**
     * @param sda the sda to set
     */
    @Required
    public void setSda(String sda) {
        this.sda = sda;
    }
    /**
     * @return the pconfig
     */
    public String getPconfig() {
        return pconfig;
    }
    /**
     * @param pconfig the pconfig to set
     */
    @Required
    public void setPconfig(String pconfig) {
        this.pconfig = pconfig;
    }
    /**
     * @return the tchd
     */
    public int getTchd() {
        return tchd;
    }
    /**
     * @param tchd the tchd to set
     */
    @Required
    public void setTchd(int tchd) {
        this.tchd = tchd;
    }
    /**
     * @return the rating
     */

    /**
     * @return the dess
     */
    public String getDess() {
        return dess;
    }
    /**
     * @param dess the dess to set
     */
    @Required
    public void setDess(String dess) {
        this.dess = dess;
    }
    /**
     * @return the act
     */
    public int getAct() {
        return act;
    }
    /**
     * @param act the act to set
     */
    @Required
    public void setAct(int act) {
        this.act = act;
    }
    /**
     * @return the jsen
     */
    public Boolean getJsen() {
        return jsen;
    }
    /**
     * @param jsen the jsen to set
     */
    @Required
    public void setJsen(Boolean jsen) {
        this.jsen = jsen;
    }
    /**
     * @return the attrstr
     */
    public String getAttrstr() {
        return attrstr;
    }
    /**
     * @param attrstr the attrstr to set
     */

    public void setAttrstr(String attrstr) {
        this.attrstr = attrstr;
    }
    /**
     * @return the tinf
     */
    public int getTinf() {
        return tinf;
    }
    /**
     * @param tinf the tinf to set
     */
    @Required
    public void setTinf(int tinf) {
        this.tinf = tinf;
    }
    /**
     * @return the mnth
     */
    public String getMnth() {
        return mnth;
    }
    /**
     * @param mnth the mnth to set
     */
    @Required
    public void setMnth(String mnth) {
        this.mnth = mnth;
    }
    /**
     * @return the bc
     */
    public int getBc() {
        return bc;
    }
    /**
     * @param bc the bc to set
     */
    @Required
    public void setBc(int bc) {
        this.bc = bc;
    }
    /**
     * @return the margindt
     */
    public int getMargindt() {
        return margindt;
    }
    /**
     * @param margindt the margindt to set
     */
    @Required
    public void setMargindt(int margindt) {
        this.margindt = margindt;
    }
    /**
     * @return the tadt
     */
    public int getTadt() {
        return tadt;
    }
    /**
     * @param tadt the tadt to set
     */
    @Required
    public void setTadt(int tadt) {
        this.tadt = tadt;
    }
    /**
     * @return the numr
     */
    public String getNumr() {
        return numr;
    }
    /**
     * @param numr the numr to set
     */
    @Required
    public void setNumr(String numr) {
        this.numr = numr;
    }
    /**
     * @return the depm
     */
    public int getDepm() {
        return depm;
    }
    /**
     * @param depm the depm to set
     */

    public void setDepm(int depm) {
        this.depm = depm;
    }
    /**
     * @return the dur
     */
    public int getDur() {
        return dur;
    }
    /**
     * @param dur the dur to set
     */
    @Required
    public void setDur(int dur) {
        this.dur = dur;
    }
    /**
     * @return the dtx
     */
    public int getDtx() {
        return dtx;
    }
    /**
     * @param dtx the dtx to set
     */
    @Required
    public void setDtx(int dtx) {
        this.dtx = dtx;
    }
    /**
     * @return the dxsel
     */
    public int getDxsel() {
        return dxsel;
    }
    /**
     * @param dxsel the dxsel to set
     */
    @Required
    public void setDxsel(int dxsel) {
        this.dxsel = dxsel;
    }
    /**
     * @return the dac
     */
    public String getDac() {
        return dac;
    }
    /**
     * @param dac the dac to set
     */
    @Required
    public void setDac(String dac) {
        this.dac = dac;
    }
    /**
     * @return the loct
     */
    public int getLoct() {
        return loct;
    }
    /**
     * @param loct the loct to set
     */

    public void setLoct(int loct) {
        this.loct = loct;
    }
    /**
     * @return the tsnr
     */
    public int getTsnr() {
        return tsnr;
    }
    /**
     * @param tsnr the tsnr to set
     */
    @Required
    public void setTsnr(int tsnr) {
        this.tsnr = tsnr;
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
    @Required
    public void setYear(String year) {
        this.year = year;
    }
    /**
     * @return the dta
     */
    public Boolean getDta() {
        return dta;
    }
    /**
     * @param dta the dta to set
     */
    @Required
    public void setDta(Boolean dta) {
        this.dta = dta;
    }
    /**
     * @return the pid
     */
    public String getPid() {
        return pid;
    }
    /**
     * @param pid the pid to set
     */
    @Required
    public void setPid(String pid) {
        this.pid = pid;
    }
    /**
     * @return the sd
     */
    public String getSd() {
        return sd;
    }
    /**
     * @param sd the sd to set
     */

    public void setSd(String sd) {
        this.sd = sd;
    }

    /**
     * @return the uc
     */
    public String getUc() {
        return uc;
    }

    /**
     * @param uc the uc to set
     */
    public void setUc(String uc) {
        this.uc = uc;
    }




}
