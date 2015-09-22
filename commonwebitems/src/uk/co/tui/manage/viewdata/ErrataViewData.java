/**
 *
 */
package uk.co.tui.manage.viewdata;

import uk.co.tui.book.domain.lite.MemoType;


/**
 * @author gopinath.n
 *
 */
public class ErrataViewData
{
    private String name;

    private Object description;

    private String code;

    private MemoType memoType;

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *           the name to set
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * @return the description
     */
    public Object getDescription()
    {
        return description;
    }

    /**
     * @param description
     *           the description to set
     */
    public void setDescription(final Object description)
    {
        this.description = description;
    }

    /**
     * @return the code
     */
    public String getCode()
    {
        return code;
    }

    /**
     * @param code
     *           the code to set
     */
    public void setCode(final String code)
    {
        this.code = code;
    }

    /**
     * @return the memoType
     */
    public MemoType getMemoType()
    {
        return memoType;
    }

    /**
     * @param memoType
     *           the memoType to set
     */
    public void setMemoType(final MemoType memoType)
    {
        this.memoType = memoType;
    }
}
