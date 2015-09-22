<%@ taglib prefix="cms" uri="/cms2lib/cmstags/cmstags.tld"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<cms:slot contentSlot="${cms_common_main}" var="comp">
    <cms:component component="${comp}" />
</cms:slot>