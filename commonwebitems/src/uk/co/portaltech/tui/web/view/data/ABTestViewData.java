/**
 *
 */
package uk.co.portaltech.tui.web.view.data;

/**
 * @author s.consolino
 *
 */
public class ABTestViewData extends ViewData
{
    private String abTestComponentUid;
    private String abTestComponentName;
    private String abTestComponentPk;
    private String abTestComponentScope;
    private String cmsComponentName;
    private String cmsComponentPk;
    private String cmsComponentUid;
    private String cmsComponentItemtype;
    private String variantCode;
    private String testName;

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        final StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("\n");
        stringBuffer.append("abTestComponentUid: ");
        stringBuffer.append(abTestComponentUid);
        stringBuffer.append("\n");
        stringBuffer.append("abTestComponentName: ");
        stringBuffer.append(abTestComponentName);
        stringBuffer.append("\n");
        stringBuffer.append("abTestComponentPk: ");
        stringBuffer.append(abTestComponentPk);
        stringBuffer.append("\n");
        stringBuffer.append("abTestComponentScope: ");
        stringBuffer.append(abTestComponentScope);
        stringBuffer.append("\n");
        stringBuffer.append("cmsComponentName: ");
        stringBuffer.append(cmsComponentName);
        stringBuffer.append("\n");
        stringBuffer.append("cmsComponentPk: ");
        stringBuffer.append(cmsComponentPk);
        stringBuffer.append("\n");
        stringBuffer.append("cmsComponentUid: ");
        stringBuffer.append(cmsComponentUid);
        stringBuffer.append("\n");
        stringBuffer.append("cmsComponentItemtype: ");
        stringBuffer.append(cmsComponentItemtype);
        stringBuffer.append("variantCode: ");
        stringBuffer.append(variantCode);
        stringBuffer.append("testName: ");
        stringBuffer.append(testName);
        return stringBuffer.toString();
    }

    /**
     * @return the abTestComponentUid
     */
    public String getAbTestComponentUid()
    {
        return abTestComponentUid;
    }

    /**
     * @param abTestComponentUid
     *           the abTestComponentUid to set
     */
    public void setAbTestComponentUid(final String abTestComponentUid)
    {
        this.abTestComponentUid = abTestComponentUid;
    }

    /**
     * @return the abTestComponentName
     */
    public String getAbTestComponentName()
    {
        return abTestComponentName;
    }

    /**
     * @param abTestComponentName
     *           the abTestComponentName to set
     */
    public void setAbTestComponentName(final String abTestComponentName)
    {
        this.abTestComponentName = abTestComponentName;
    }

    /**
     * @return the abTestComponentPk
     */
    public String getAbTestComponentPk()
    {
        return abTestComponentPk;
    }

    /**
     * @param abTestComponentPk
     *           the abTestComponentPk to set
     */
    public void setAbTestComponentPk(final String abTestComponentPk)
    {
        this.abTestComponentPk = abTestComponentPk;
    }

    /**
     * @return the abTestComponentScope
     */
    public String getAbTestComponentScope()
    {
        return abTestComponentScope;
    }

    /**
     * @param abTestComponentScope
     *           the abTestComponentScope to set
     */
    public void setAbTestComponentScope(final String abTestComponentScope)
    {
        this.abTestComponentScope = abTestComponentScope;
    }

    /**
     * @return the cmsComponentName
     */
    public String getCmsComponentName()
    {
        return cmsComponentName;
    }

    /**
     * @param cmsComponentName
     *           the cmsComponentName to set
     */
    public void setCmsComponentName(final String cmsComponentName)
    {
        this.cmsComponentName = cmsComponentName;
    }

    /**
     * @return the cmsComponentPk
     */
    public String getCmsComponentPk()
    {
        return cmsComponentPk;
    }

    /**
     * @param cmsComponentPk
     *           the cmsComponentPk to set
     */
    public void setCmsComponentPk(final String cmsComponentPk)
    {
        this.cmsComponentPk = cmsComponentPk;
    }

    /**
     * @return the cmsComponentUid
     */
    public String getCmsComponentUid()
    {
        return cmsComponentUid;
    }

    /**
     * @param cmsComponentUid
     *           the cmsComponentUid to set
     */
    public void setCmsComponentUid(final String cmsComponentUid)
    {
        this.cmsComponentUid = cmsComponentUid;
    }

    /**
     * @return the cmsComponentItemtype
     */
    public String getCmsComponentItemtype()
    {
        return cmsComponentItemtype;
    }

    /**
     * @param cmsComponentItemtype
     *           the cmsComponentItemtype to set
     */
    public void setCmsComponentItemtype(final String cmsComponentItemtype)
    {
        this.cmsComponentItemtype = cmsComponentItemtype;
    }

    /**
     * @return the variantName
     */
    public String getVariantCode()
    {
        return variantCode;
    }

    /**
     * @param variantCode
     *           the variantName to set
     */
    public void setVariantCode(final String variantCode)
    {
        this.variantCode = variantCode;
    }

    /**
     * @return the testName
     */
    public String getTestName()
    {
        return testName;
    }

    /**
     * @param testName
     *           the testName to set
     */
    public void setTestName(final String testName)
    {
        this.testName = testName;
    }


}
