package com.ciandt.pms.business.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ControleReajuste;
import com.ciandt.pms.model.ControleReajusteAud;
import com.ciandt.pms.model.DocumentoLegal;
import com.ciandt.pms.model.FichaReajuste;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.VwPmsContratoProfitCenter;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 11/12/2013
 */
@Service
public interface IReportService {

	List<VwPmsContratoProfitCenter> getRevenueForecast(Date dtValor);
	void runProcPlannedCost(Long codigoContratoPratica);
}