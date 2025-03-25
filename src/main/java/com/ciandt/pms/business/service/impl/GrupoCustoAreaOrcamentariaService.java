package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IGrupoCustoAreaOrcamentariaService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.GrupoCustoAreaOrcamentariaAud;
import com.ciandt.pms.persistence.dao.IGrupoCustoAreaOrcamentariaDao;
import com.ciandt.pms.persistence.dao.jpa.GrupoCustoAreaOrcamentaria;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * A classe ControleReajusteService proporciona as funcionalidades de serviço
 * referente a entidade ControleReajuste.
 *
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 11/12/2013
 */
@Service
public class GrupoCustoAreaOrcamentariaService extends AbstractGestaoReajusteService
        implements IGrupoCustoAreaOrcamentariaService {

    /**
     * Instancia do DAO da entidade.
     */
    @Autowired
    private IGrupoCustoAreaOrcamentariaDao dao;

    /**
     * Busca uma entidade pelo id.
     *
     * @param id da entidade.
     * @return entidade com o id passado por parametro.
     */
    @Override
    public GrupoCustoAreaOrcamentaria findById(final Long id) {
        return dao.findById(id);
    }

    /**
     * Retorna todas as entidades.
     *
     * @return retorna uma lista com todas as entidades.
     */
    @Override
    public List<GrupoCustoAreaOrcamentaria> findAll() {
        return dao.findAll();
    }

    /**
     * Insere uma entidade.
     *
     * @param entity
     */
    @Override
    @Transactional
    public Boolean create(final GrupoCustoAreaOrcamentaria gcao) {
        GrupoCustoAreaOrcamentaria gcaoAntigo = this
                .findMaxDataInicioByCodigoGrupoCusto(gcao.getGrupoCusto()
                        .getCodigoGrupoCusto());

        if (gcaoAntigo != null) {
            if (gcao.getDataInicio().after(gcaoAntigo.getDataInicio())) {

                gcaoAntigo.setDataFim(DateUtils.addMonths(
                        gcao.getDataInicio(), -1));
                this.update(gcaoAntigo);
            } else {
                Messages.showError("createCPGC", Constants.CPGC_VIGENCIA_INVALIDA);
                return Boolean.FALSE;
            }
        }

        dao.create(gcao);
        return Boolean.TRUE;
    }

    /**
     * Atualiza uma entidade.
     *
     * @param entity
     */
    @Override
    @Transactional
    public void update(final GrupoCustoAreaOrcamentaria entity) {
        dao.update(entity);
    }

    /**
     * Deleta uma entidade.
     *
     * @param entity
     */
    @Override
    @Transactional
    public Boolean remove(final GrupoCustoAreaOrcamentaria gcao) {
        GrupoCustoAreaOrcamentaria gcaoMaisRecente = this
                .findMaxDataInicioByCodigoGrupoCusto(gcao.getGrupoCusto()
                        .getCodigoGrupoCusto());

        if (gcaoMaisRecente.getGrupoCusto().getCodigoGrupoCusto()
                .compareTo(gcao.getGrupoCusto().getCodigoGrupoCusto()) == 0) {
            dao.remove(gcao);

            gcaoMaisRecente = this.findMaxDataInicioByCodigoGrupoCusto(gcao
                    .getGrupoCusto().getCodigoGrupoCusto());

            if (gcaoMaisRecente != null) {
                gcaoMaisRecente.setDataFim(null);
                dao.update(gcaoMaisRecente);
            }

            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public List<GrupoCustoAreaOrcamentaria> findByGrupoCustoId(Long codigoGrupoCusto) {
        return dao.findByGrupoCustoId(codigoGrupoCusto);
    }

    @Override
    public GrupoCustoAreaOrcamentaria findMaxDataInicioByCodigoGrupoCusto(
            Long codigoGrupoCusto) {
        return dao.findMaxDataInicioByCodigoGrupoCusto(codigoGrupoCusto);
    }

    @Override
    public List<GrupoCustoAreaOrcamentaria> findBySubAreaOrcamentariaAndVigencia(Long codigoAreaOrcamentaria, Date dataVigencia) {
        return dao.findBySubAreaOrcamentariaAndVigencia(codigoAreaOrcamentaria, dataVigencia);
    }

    @Override
    public List<GrupoCustoAreaOrcamentariaAud> findHistoryByCodigo(Long codigoGrupoCusto) {
        return dao.findHistoryByCodigo(codigoGrupoCusto);
    }
}
