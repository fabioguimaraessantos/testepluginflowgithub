package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IGrupoCustoPeriodoService;
import com.ciandt.pms.business.service.INaturezaCentroLucroService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.*;
import com.ciandt.pms.persistence.dao.IGrupoCustoPeriodoDao;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * A classe GrupoCustoPeriodoService proporciona as funcionalidades da camada de
 * servi�o referente a entidade GrupoCustoPeriodo.
 * 
 * @since 15/05/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class GrupoCustoPeriodoService implements IGrupoCustoPeriodoService {

    /** Instancia do GrupoCustoPeriodoDao. */
    @Autowired
    private IGrupoCustoPeriodoDao grupoCustoPeriodoDao;

    /** Instancia do NaturezaCentroLucroService. */
    @Autowired
    private INaturezaCentroLucroService naturezaService;

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que ser� apagada.
     * 
     * @return retorna true se a data inicio da vigencia, � posterior a maior
     *         data existente no banco de dados. Caso contrario retorna false
     */
    public Boolean createGrupoCustoPeriodo(final GrupoCustoPeriodo entity) {
        GrupoCustoPeriodo maxGCP = grupoCustoPeriodoDao
                .findMaxStartDateByGrupoCusto(entity.getGrupoCusto());
        // verifica se a data de vigencia � a maior
        if (maxGCP != null) {
            if (entity.getDataInicio().after(maxGCP.getDataInicio())) {
                maxGCP.setDataFim(DateUtils.addMonths(entity.getDataInicio(),
                        -1));
                grupoCustoPeriodoDao.update(maxGCP);
            } else {
                Messages.showError("createGrupoCustoPeriodo",
                        Constants.MSG_ERROR_ADD_GRUPO_CUSTO_PERIODO);
                return Boolean.valueOf(false);
            }
        }

        // recupera a lista de GrupoCustoCentroLucro do GrupoCustoPeriodo
        // corrente
        List<GrupoCustoCentroLucro> grupoCustoCentroLucroList = entity
                .getGrupoCustoCentroLucros();

        List<NaturezaCentroLucro> naturezaList = naturezaService
                .findNaturezaCentroLucroAll();
        // itera a lista de NaturezaCentroLucro que serao relacionadas com o
        // GrupoCustoCentroLucro
        for (NaturezaCentroLucro natureza : naturezaList) {
            List<CentroLucro> activesOnly = new ArrayList<CentroLucro>();
            for (CentroLucro centroLucro : natureza.getCentroLucros()) {
                if (centroLucro.getIndicadorAtivo().equalsIgnoreCase(Constants.ACTIVE)) {
                    activesOnly.add(centroLucro);
                }
            }
            natureza.getCentroLucros().clear();
            natureza.setCentroLucros(activesOnly);

            GrupoCustoCentroLucro gccl = new GrupoCustoCentroLucro();
            gccl.setGrupoCustoPeriodo(entity);
            gccl.setNaturezaCentroLucro(natureza);

            // adiciona o GrupoCustoCentroLucro correnta na lista
            grupoCustoCentroLucroList.add(gccl);
        }

        entity.setGrupoCustoCentroLucros(grupoCustoCentroLucroList);
        grupoCustoPeriodoDao.create(entity);

        return Boolean.valueOf(true);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que ser� apagada.
     * 
     * @return true se foi removido com sucesso, false caso contrario
     * 
     */
    public Boolean removeGrupoCustoPeriodo(final GrupoCustoPeriodo entity) {
        GrupoCustoPeriodo maxGCP = findMaxStartDateByGrupoCusto(entity
                .getGrupoCusto());

        // verifica se o maior GrupoCustoPeriodo � o que esta sendo deletado
        if (maxGCP.getCodigoGcPeriodo().compareTo(entity.getCodigoGcPeriodo()) == 0) {

            grupoCustoPeriodoDao.remove(entity);

            // pega o proximo maior, para setar a data final como null
            maxGCP = findMaxStartDateByGrupoCusto(entity.getGrupoCusto());
            if (maxGCP != null) {
                maxGCP.setDataFim(null);
                grupoCustoPeriodoDao.update(maxGCP);
            }

            return Boolean.valueOf(true);
        } else {
            Messages.showError("removeGrupoCustoPeriodo",
                    Constants.MSG_ERROR_GRUPO_CUSTO_PERIODO_REMOVE);
            return Boolean.valueOf(false);
        }
    }

    /**
     * Busca e entidade pelo id.
     * 
     * @param id
     *            - id da entidade.
     * 
     * @return retorna a entidade com o id passado por parametro. Caso n�o
     *         exista retorna null.
     */
    public GrupoCustoPeriodo findGrupoCustoPeriodoById(final Long id) {
        return grupoCustoPeriodoDao.findById(id);
    }

	/**
	 * Busca a entidade com maior data inicio da vigencia.
	 * 
	 * @param grupoCusto
	 *            entidade populada com os valores do GrupoCusto.
	 * 
	 * @return lista de entidades que atendem ao criterio do GrupoCusto.
	 */
	@Override
	public GrupoCustoPeriodo findMaxStartDateByGrupoCusto(
			final GrupoCusto grupoCusto) {
		return grupoCustoPeriodoDao.findMaxStartDateByGrupoCusto(grupoCusto);
	}

    public List<GrupoCustoPeriodoAud> findHistoryByCodigo(final Long codigoGrupoCusto) {
        return grupoCustoPeriodoDao.findHistoryByCodigo(codigoGrupoCusto);
    }

}
