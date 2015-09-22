<script type="text/javascript">
    dojoConfig.addModuleName("tui/widget/taggable/PassportAndVisaEditorial");
</script>

<%-- Passports and Visas Editorial component (WF_COM_064) --%>
<div class="${componentStyle}" data-dojo-type="tui.widget.taggable.PassportAndVisaEditorial">
    <h2 class="underline">Passports and Visas</h2>
    <p>${viewData.featureCodesAndValues['passportAndVisaInfo'][0]}</p>
    <p>For more information visit the Foreign Office website - <a href="http://www.fco.gov.uk/en/" class="ensLinkTrack" data-componentId="${componentUid}" target="_blank">www.fco.gov.uk/en/</a></p>
</div>