package com.ciandt.pms.business.service;

import com.ciandt.pms.model.LicencaSwProjetoParcela;
import com.ciandt.pms.model.vo.LicencaSwDetail;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ILicencaSwDetailService {

    List<LicencaSwDetail> convertLicencaSwParcela(List<LicencaSwProjetoParcela> licencaSwProjetoParcelas);

    Map<String, List<LicencaSwDetail>> createMapFromAccounting(List<LicencaSwDetail> licencaSwDetails) throws Exception;

    String getValorTotal(List<LicencaSwDetail> licencaSwDetails);

    List<String> validateCostCenterAndProjectActive(List<LicencaSwDetail> licencaSwDetailList) throws Exception;
}
