package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.bean.TiRecursoBean;
import com.ciandt.pms.model.TiRecurso;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import javax.faces.component.html.HtmlMessage;
import javax.faces.event.ValueChangeEvent;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TiRecursoControllerTest extends LicensaSoftwareControllerTest{

    @InjectMocks
    TiRecursoController controller;

    @Test
    public void prepareCompanyList() {

        //GIVEN New Bean
        controller.setBean(new TiRecursoBean());

        //AND Mocks
        companyMocks();

        //WHEN the method is Called
        controller.prepareCompanyList();

        //THEN The size of company collection must be 2.
        assertEquals(2, controller.getBean().getCompanySelect().list().size());
    }

    @Test
    public void getTipoAlocacaoList_filter() {

        //WHEN the method is Called WITH Company Null And Param Filter
        List<String> tipoAlocacaoList = controller.getTipoAlocacaoList(null, Boolean.TRUE);

        //THEN The size of type collection must be 4.
        assertEquals(4, tipoAlocacaoList.size());
    }

    @Test
    public void getTipoAlocacaoList_noFilter() {

        //WHEN the method is Called WITH Company Null And Param Filter is False
        List<String> tipoAlocacaoList = controller.getTipoAlocacaoList(null, Boolean.FALSE);

        //THEN The size of type collection must be 3.
        assertEquals(3, tipoAlocacaoList.size());
    }

    @Test
    public void getTipoAlocacaoList_companyAndFilter() {

        //WHEN the method is Called WITH Company Default Null And Param Filter
        List<String> tipoAlocacaoList = controller.getTipoAlocacaoList(CIT_CODE, Boolean.TRUE);

        //THEN The size of type collection must be 3.
        assertEquals(4, tipoAlocacaoList.size());
    }

    @Test
    public void getTipoAlocacaoList_companyIncAndFilter() {

        //WHEN the method is Called WITH Company INC Null And Param Filter
        List<String> tipoAlocacaoList = controller.getTipoAlocacaoList(INC_CODE, Boolean.TRUE);

        //THEN The size of type collection must be 1.
        assertEquals(1, tipoAlocacaoList.size());
    }

    @Test
    public void getTipoAlocacaoList_companyIncAndNoFilter() {

        //WHEN the method is Called WITH Company INC Null And Param Filter False
        List<String> tipoAlocacaoList = controller.getTipoAlocacaoList(INC_CODE, Boolean.FALSE);

        //THEN The size of type collection must be 1.
        assertEquals(1, tipoAlocacaoList.size());
    }

    @Test
    public void prepareTipoAlocacaoList_companyDefaultToInc(){

        //GIVEN Event FROM CIT Software S.A. - TO CIT INC - EUA
        ValueChangeEvent e = new ValueChangeEvent(new HtmlMessage(), "CIT Software S.A.", "CIT INC - EUA");

        //THEN
        prepareTipoAlocacaoList(e, 1, Constants.TI_RECURSO_TYPE_SOFTWARE_USER, Boolean.FALSE);

    }

    @Test
    public void prepareTipoAlocacaoList_companyIncToDefault(){

        //GIVEN Event FROM CIT INC - EUA - TO CIT Software S.A.
        ValueChangeEvent e = new ValueChangeEvent(new HtmlMessage(), "CIT INC - EUA", "CIT Software S.A.");

        //THEN
        prepareTipoAlocacaoList(e, 3, Constants.TI_RECURSO_TYPE_SOFTWARE_USER, Boolean.FALSE);
    }

    @Test
    public void prepareTipoAlocacaoList_companyIncToNull(){

        //GIVEN Event FROM CIT INC - EUA - TO NULL
        ValueChangeEvent e = new ValueChangeEvent(new HtmlMessage(), "CIT INC - EUA", null);

        //THEN
        prepareTipoAlocacaoList(e, 3, Constants.TI_RECURSO_TYPE_CONTRCT_SERVICE, Boolean.FALSE);
    }

    @Test
    public void prepareTipoAlocacaoListResearch_companyDefaultToInc(){

        //GIVEN Event FROM CIT Software S.A. - TO CIT INC - EUA
        ValueChangeEvent e = new ValueChangeEvent(new HtmlMessage(), "CIT Software S.A.", "CIT INC - EUA");

        //THEN
        prepareTipoAlocacaoList(e, 1, Constants.TI_RECURSO_TYPE_SOFTWARE_USER, Boolean.TRUE);

    }

    @Test
    public void prepareTipoAlocacaoListResearch_companyIncToDefault(){

        //GIVEN Event FROM CIT INC - EUA - TO CIT Software S.A.
        ValueChangeEvent e = new ValueChangeEvent(new HtmlMessage(), "CIT INC - EUA", "CIT Software S.A.");

        //THEN
        prepareTipoAlocacaoList(e, 4, null, Boolean.TRUE);
    }

    @Test
    public void prepareTipoAlocacaoListResearch_companyIncToNull(){

        //GIVEN Event FROM CIT INC - EUA - TO NULL
        ValueChangeEvent e = new ValueChangeEvent(new HtmlMessage(), "CIT INC - EUA", null);

        //THEN
        prepareTipoAlocacaoList(e, 4, Constants.ALL, Boolean.TRUE);
    }

    /**
     * @param e - Event
     * @param sizeExpected - Size to Compare result
     * @param indicadorExpected - String to Compare Result
     * @param isResearch - Indicates it is a Research Behavior
     */
    private void prepareTipoAlocacaoList(ValueChangeEvent e, int sizeExpected, String indicadorExpected, boolean isResearch){

        //WITH BEAN
        initBean();

        //AND Mocks
        companyMocks();
        controller.prepareCompanyList();

        //WHEN The method is Called
        TiRecurso tiRecurso;
        if(isResearch) {
            controller.prepareTipoAlocacaoListResearch(e);
            tiRecurso = controller.getBean().getFilter();
        }else{
            controller.prepareTipoAlocacaoList(e);
            tiRecurso = controller.getBean().getTo();
        }

        //THEN
        assertEquals(sizeExpected, controller.getBean().getTipoAlocacaoList().size());
        assertEquals(indicadorExpected, tiRecurso.getIndicadorTipoAlocacao());
    }

    /**
     *
     */
    private void initBean(){
        TiRecursoBean bean = new TiRecursoBean();
        bean.reset();
        controller.setBean(bean);
    }
}