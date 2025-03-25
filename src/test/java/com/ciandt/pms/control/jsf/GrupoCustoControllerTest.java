import com.ciandt.pms.business.service.impl.AreaService;
import com.ciandt.pms.business.service.impl.CostCenterHierarchyService;
import com.ciandt.pms.business.service.impl.GrupoCustoService;
import com.ciandt.pms.business.service.impl.PessoaService;
import com.ciandt.pms.control.jsf.GrupoCustoController;
import com.ciandt.pms.control.jsf.bean.GrupoCustoBean;
import com.ciandt.pms.model.Area;
import com.ciandt.pms.model.CostCenterHierarchy;
import com.ciandt.pms.model.Pessoa;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GrupoCustoControllerTest {

    @InjectMocks
    private GrupoCustoController grupoCustoController;

    @Mock
    private GrupoCustoService grupoCustoService;

    @Mock
    private AreaService areaService;

    @Mock
    private PessoaService pessoaService;

    @Mock
    private CostCenterHierarchyService costCenterHierarchyService;

    @Mock
    private GrupoCustoBean bean;

    private static final String OUTCOME_COST_CENTER_ADD = "costCenter_add";

    private static final String OUTCOME_COST_CENTER_EDIT = "costCenter_edit";

    private static final String OUTCOME_COST_CENTER_SEARCH = "costCenter_search";

    private static final String OUTCOME_COST_CENTER_VIEW = "costCenter_view";


    @Test
    public void whenPrepareCreate_shouldReturnAddOutcome() {
        when(pessoaService.findAllForComboBox()).thenReturn(Arrays.asList(mockPessoa(),mockPessoa()));
        when(pessoaService.findAllManagerForComboBox()).thenReturn(Arrays.asList(mockPessoa(),mockPessoa()));

        when(areaService.findAreaAllActive()).thenReturn(Arrays.asList(mockArea(),mockArea()));

        when(costCenterHierarchyService.findAllActive()).thenReturn(new ArrayList<>(Arrays.asList(mockCostCenterHierarchy(), mockCostCenterHierarchy())));

        String outcome = grupoCustoController.prepareCreate();

        verify(bean).reset();

        verify(bean).setApproverCombo(any());
        verify(bean).setManagerApproverCombo(any());

        verify(bean).setAccountTypeSelect(any());

        verify(bean).setAreaCombo(any());

        verify(bean).setCostCenterHierarchySelect(any());
        assertEquals(OUTCOME_COST_CENTER_ADD, outcome);
    }

    private Pessoa mockPessoa() {
        Pessoa pessoa = new Pessoa();
        pessoa.setCodigoPessoa(1L);
        pessoa.setNomePessoa("PessoaTeste");
        return pessoa;
    }

    private Area mockArea() {
        Area area = new Area();
        area.setCodigoArea(1L);
        area.setNomeArea("AreaTeste");
        return area;
    }

    private CostCenterHierarchy mockCostCenterHierarchy() {
        CostCenterHierarchy costCenterHierarchy = new CostCenterHierarchy();
        costCenterHierarchy.setCode(1L);
        costCenterHierarchy.setName("CostCenterHierarchyTest");
        costCenterHierarchy.setOracleCode("OracleCodeTest");
        return costCenterHierarchy;

    }

}