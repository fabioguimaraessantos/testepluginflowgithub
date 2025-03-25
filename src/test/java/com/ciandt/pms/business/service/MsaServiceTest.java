package com.ciandt.pms.business.service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.impl.MsaContratoService;
import com.ciandt.pms.business.service.impl.MsaService;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.UploadMsaLegalDoc;
import com.ciandt.pms.persistence.dao.IMsaDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MsaServiceTest {

    @InjectMocks
    MsaService service;

    @Mock
    private IMsaDao dao;

    @Mock
    private MsaContratoService msaContratoService;

    @Test
    public void shouldFindAllMsasByIndustryTypeCode() throws BusinessException {

        //GIVEN industry type code
        Long industryTypeCode = 1L;

        //WITH mock
        List<Msa> entities = getEntities();

        //AND service find by code
        when(dao.findByIndustryType(industryTypeCode)).thenReturn(entities);

        //WHEN service is executed by code
        List<Msa> result = service.findMsaByIndustryType(industryTypeCode);

        //THEN result size should be 3
        assertEquals(3, result.size());
    }

    @Test(expected = BusinessException.class)
    public void shouldNotFindMsasAndThrowBusinessException() throws BusinessException {

        //GIVEN industry type code
        Long industryTypeCode = 1L;

        //AND service should thrown Business Exception
        when(dao.findByIndustryType(industryTypeCode)).thenReturn(null);

        //WHEN service is executed by code.
        service.findMsaByIndustryType(industryTypeCode);

        //EXPECTED BusinessException
    }

    @Test
    public void shouldFindAllMsasByBmdnCode() throws BusinessException {

        //GIVEN bmdn code
        Long bmdnCode = 1L;

        //WITH mock
        List<Msa> entities = getEntities();

        //AND service find by code
        when(dao.findByBmDn(bmdnCode)).thenReturn(entities);

        //WHEN service is executed by code
        List<Msa> result = service.findMsaByBmDn(bmdnCode);

        //THEN result size should be 3
        assertEquals(3, result.size());
    }

    @Test(expected = BusinessException.class)
    public void shouldNotFindMsasByBmdnAndThrowBusinessException() throws BusinessException {

        //GIVEN bmdn code
        Long bmdnCode = 1L;

        //AND service should thrown Business Exception
        when(dao.findByBmDn(bmdnCode)).thenReturn(null);

        //WHEN service is executed by bmdn code.
        service.findMsaByBmDn(bmdnCode);

        //EXPECTED BusinessException
    }

    @Test
    public void shouldReturnEmptyErrorMapWhenNoDocumentsProvided() throws Exception {

        List<UploadMsaLegalDoc> uploadMsaLegalDocs = new ArrayList<>();

        Map<String, List<String>> result = service.mapErrosUploadFile(uploadMsaLegalDocs);

        for (String key : Constants.ERROR_KEYS) {
            assertEquals(0, result.get(key).size());
        }
    }

    @Test
    public void shouldReturnErrorWhenRequiredFieldsAreEmpty() throws Exception {

        List<UploadMsaLegalDoc> uploadMsaLegalDocs = new ArrayList<>();
        UploadMsaLegalDoc doc = new UploadMsaLegalDoc();
        doc.setJiraCp("");
        doc.setProjectDescription("");
        doc.setMsa("");
        uploadMsaLegalDocs.add(doc);

        Map<String, List<String>> result = service.mapErrosUploadFile(uploadMsaLegalDocs);

        assertEquals(1, result.get(Constants.MSA_LEGAL_DOC_JIRA_CP_UPLOAD_ERROR).size());
        assertEquals(1, result.get(Constants.MSA_LEGAL_DOC_PROJECT_DESCRIPTION_UPLOAD_ERROR).size());
        assertEquals(1, result.get(Constants.MSA_LEGAL_DOC_MSA_UPLOAD_ERROR).size());
    }

    @Test
    public void shouldNotUploadWhenMsaDoesNotExist() throws Exception {

        List<UploadMsaLegalDoc> uploadMsaLegalDocs = new ArrayList<>();
        UploadMsaLegalDoc doc = new UploadMsaLegalDoc();
        doc.setMsa("NonExistentMsa");
        uploadMsaLegalDocs.add(doc);

        when(dao.findByName("NonExistentMsa")).thenReturn(null);

        Map<String, List<String>> result = service.mapErrosUploadFile(uploadMsaLegalDocs);

        assertEquals(1, result.get(Constants.MSA_LEGAL_DOC_MSA_UPLOAD_ERROR).size());
    }

    @Test
    public void shouldCheckForUniqueContract() throws Exception {

        List<UploadMsaLegalDoc> uploadMsaLegalDocs = new ArrayList<>();
        UploadMsaLegalDoc doc = new UploadMsaLegalDoc();
        doc.setMsa("ExistingMsa");
        doc.setJiraCp("jiraCP");
        doc.setStartDate("01/01/2024");
        uploadMsaLegalDocs.add(doc);

        when(dao.findByName("ExistingMsa")).thenReturn(new Msa());
        when(msaContratoService.validateUniqueContract(Mockito.any(), Mockito.any())).thenReturn(false);

        Map<String, List<String>> result = service.mapErrosUploadFile(uploadMsaLegalDocs);

        assertEquals(1, result.get(Constants.MSA_LEGAL_DOC_UPLOAD_ALREADY_EXIST_ERROR).size());
    }


    /**
     * @return List of MSA
     */
    private List<Msa> getEntities() {

        List<Msa> entities = new ArrayList<Msa>();
        entities.add(create(1L, "Test"));
        entities.add(create(2L, "Test 2"));
        entities.add(create(3L, "Test 3"));
        return entities;
    }

    /**
     * @param code MSA code
     * @param name MSA name
     * @return MSA - New Entity
     */
    private Msa create(Long code, String name) {
        Msa entity = new Msa();

        if (code != null)
            entity.setCodigoMsa(code);

        if (name != null)
            entity.setNomeMsa(name);

        return entity;
    }
}