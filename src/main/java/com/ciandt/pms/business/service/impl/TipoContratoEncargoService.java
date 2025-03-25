package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ITipoContratoEncargoService;
import com.ciandt.pms.business.service.ITipoContratoService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.TipoContratoEncargo;
import com.ciandt.pms.persistence.dao.ITipoContratoEncargoDao;


/**
 * 
 * A classe TipoContratoEncargoService proporciona as funcionalidades da camada
 * de serviço referentwe a entidade TipoContratoEncargo.
 * 
 * @since 01/06/2010
 * @author cmantovani
 * 
 */
@Service
public class TipoContratoEncargoService implements ITipoContratoEncargoService {

    /** Entidade dao do TipoContratoEncargo. */
    @Autowired
    private ITipoContratoEncargoDao dao;

    /** Instancia do servico. */
    @Autowired
    private ITipoContratoService tipoContratoService;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            a ser inserida
     * @return Boolean
     */
    public Boolean createTipoContratoEncargo(final TipoContratoEncargo entity) {

        // atualiza as entidades / referencias do hibernate
        entity.setTipoContrato(tipoContratoService.findTipoContratoById(entity
                .getTipoContrato().getCodigoTipoContrato()));

        if (this.exists(entity)) {
            Messages.showWarning("createTipoContratoEncargo",
                    Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                    Constants.ENTITY_NAME_TIPO_CONTRATO_ENCARGO);

            return Boolean.valueOf(false);
        }

        TipoContratoEncargo next = dao.findNext(entity);
        TipoContratoEncargo previous = dao.findPrevious(entity);

        if (next != null) {
            entity.setDataFimVigencia(DateUtils.addMonths(next
                    .getDataInicioVigencia(), -1));
        }

        if (previous != null) {
            previous.setDataFimVigencia(DateUtils.addMonths(entity
                    .getDataInicioVigencia(), -1));

            dao.update(previous);
        }

        dao.create(entity);

        return Boolean.valueOf(true);

    }

    /**
     * Verifica se o tipoContratoEncargo já existe.
     * 
     * @param tipoContratoEncargo
     *            - Entidade do tipo TipoContratoEncargo
     * 
     * @return retorna true se existe, caso contrario false.
     */
    private Boolean exists(final TipoContratoEncargo tipoContratoEncargo) {
        TipoContratoEncargo tce = dao.findByStartDate(tipoContratoEncargo);

        if (tce == null) {
            return false;
        }

        return true;
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<TipoContratoEncargo> findTipoContratoEncargoAll() {
        return dao.findAll();
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public TipoContratoEncargo findTipoContratoEncargoById(final Long id) {
        return dao.findById(id);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     * @return Boolean
     */
    public Boolean removeTipoContratoEncargo(final TipoContratoEncargo entity) {

        TipoContratoEncargo tipoContratoEncargo = dao.findById(entity
                .getCodigoTipoContratoEncargo());

        TipoContratoEncargo previous = dao.findPrevious(tipoContratoEncargo);

        if (previous != null) {

            TipoContratoEncargo next = dao.findNext(tipoContratoEncargo);
            if (next != null) {
                previous.setDataFimVigencia(DateUtils.addMonths(next
                        .getDataInicioVigencia(), -1));
            } else {
                previous.setDataFimVigencia(null);
            }

            dao.update(previous);
        }

        dao.remove(tipoContratoEncargo);

        return Boolean.valueOf(true);
    }

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     */
    public void updateTipoContratoEncargo(final TipoContratoEncargo entity) {
        dao.update(entity);
    }

}
