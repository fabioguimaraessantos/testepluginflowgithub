package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ITiRecursoService;
import com.ciandt.pms.control.jsf.bean.IntegrarLicencaSoftwareBean;
import com.ciandt.pms.model.TiRecurso;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.faces.component.html.HtmlMessage;
import javax.faces.event.ValueChangeEvent;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class IntegrarLicencaSoftwareControllerTest extends LicensaSoftwareControllerTest{

    @InjectMocks
    private IntegrarLicencaSoftwareController controller;

    @Mock
    private ITiRecursoService tiRecursoService;

    @Test
    public void prepareCompanyList() {

        //GIVEN New Bean
        controller.setBean(new IntegrarLicencaSoftwareBean());

        //AND Mocks
        companyMocks();

        //WHEN the method is Called
        controller.prepareCompanyList();

        //THEN The size of company collection must be 2.
        assertEquals(2, controller.getBean().getCompanySelect().list().size());
    }

    @Test
    public void getTipoTiRecursos_filter() {

        //WHEN the method is Called
        List<String> tipoAlocacaoList = controller.getTipoTiRecursos();

        //THEN The size of type collection must be 4.
        assertEquals(2, tipoAlocacaoList.size());
    }

    @Test
    public void prepareTipoTiRecursoCombo_companyDefaultToInc(){

        //GIVEN Event FROM CIT Software S.A. - TO CIT INC - EUA
        ValueChangeEvent e = new ValueChangeEvent(new HtmlMessage(), "CIT Software S.A.", "CIT INC - EUA");

        //AND TI RECURSO MOCK
        tiRecursoMocks();


        //THEN
        prepareTipoTiRecursoCombo(e, 1, Constants.TI_RECURSO_TYPE_SOFTWARE_USER);
    }

    @Test
    public void prepareTipoTiRecursoCombo_companyIncToDefault(){

        //GIVEN Event FROM CIT INC - EUA - TO CIT Software S.A.
        ValueChangeEvent e = new ValueChangeEvent(new HtmlMessage(), "CIT INC - EUA", "CIT Software S.A.");

        //AND TI RECURSO MOCK
        tiRecursoMocks();

        //THEN
        prepareTipoTiRecursoCombo(e, 2, Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT);
    }

    @Test
    public void prepareTipoTiRecursoCombo_companyIncToNull(){

        //GIVEN Event FROM CIT INC - EUA - TO NULL
        ValueChangeEvent e = new ValueChangeEvent(new HtmlMessage(), "CIT INC - EUA", null);

        //AND TI RECURSO MOCK
        tiRecursoMocks();

        //THEN
        prepareTipoTiRecursoCombo(e, 2, Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT);
    }

    /**
     * @param e - Event
     * @param sizeExpected - Size to Compare result
     * @param indicadorExpected - String to Compare Result
     */
    private void prepareTipoTiRecursoCombo(ValueChangeEvent e, int sizeExpected, String indicadorExpected){

        //WITH BEAN
        initBean();

        //AND Mocks
        companyMocks();
        controller.prepareCompanyList();

        //WHEN The method is Called
        controller.prepareTipoTiRecursoCombo(e);

        //THEN
        assertEquals(sizeExpected, controller.getBean().getTipoTiRecursoList().size());
        assertEquals(indicadorExpected, controller.getBean().getTipoRecurso());
    }

    /**
     *
     */
    private void initBean(){
        IntegrarLicencaSoftwareBean bean = new IntegrarLicencaSoftwareBean();
        bean.reset();
        controller.setBean(bean);
    }

    /**
     * @return Collection of Ti Recursos.
     */
    protected List<TiRecurso> createListTiRecurso(){

        List<TiRecurso> recursos = new ArrayList<>();
        recursos.add(createTiRecurso(20L, "TI Recurso 1"));
        recursos.add(createTiRecurso(30L, "TI Recurso 2"));

        return recursos;
    }

    /**
     * @param id - TiRecurso ID
     * @param name - TiRecurso Name
     * @return - New TiRecurso
     */
    protected TiRecurso createTiRecurso(Long id, String name){
        TiRecurso tiRecurso = new TiRecurso();
        tiRecurso.setCodigoTiRecurso(id);
        tiRecurso.setNomeTiRecurso(name);
        return tiRecurso;
    }

    /**
     *
     */
    protected void tiRecursoMocks(){
        List<TiRecurso> recursos = createListTiRecurso();
        Mockito.when(tiRecursoService.findTiRecursoByType(Constants.TI_RECURSO_TYPE_SOFTWARE_USER)).thenReturn(recursos);
        Mockito.when(tiRecursoService.findTiRecursoByType(Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT)).thenReturn(recursos);
    }
}