package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IMapaAlocacaoService;
import com.ciandt.pms.business.service.ITcePapelAlocacaoService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.PapelAlocacao;
import com.ciandt.pms.model.TcePapelAlocacao;
import com.ciandt.pms.persistence.dao.ITcePapelAlocacaoDao;


/**
 * Define o Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 01/08/2009
 */
@Service
public class TcePapelAlocacaoService implements ITcePapelAlocacaoService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private ITcePapelAlocacaoDao tcePapelAlocacaoDao;

    /** Instancia do Servico da entidade MapaAlocacao. */
    @Autowired
    private IMapaAlocacaoService mapaAlocacaoService;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     * @return retorna true se a data inicio da vigencia, é posterior a maior
     *         data existente no banco de dados. Caso contrario retorna false
     */
    public Boolean createTcePapelAlocacao(final TcePapelAlocacao entity) {
        TcePapelAlocacao tcePapelAlocacao = findMaxStartDateByPapelAlocacao(entity
                .getPapelAlocacao());
        // verifica se a data de vigencia é a maior
        if (tcePapelAlocacao != null) {
            if (entity.getDataInicio().after(tcePapelAlocacao.getDataInicio())) {
                tcePapelAlocacao.setDataFim(DateUtils.addMonths(entity
                        .getDataInicio(), -1));
                updateTcePapelAlocacao(tcePapelAlocacao);
            } else {
                Messages.showError("createTcePapelAlocacao",
                        Constants.MSG_ERROR_ADD_TCE_PAPEL_ALOCACAO_PERIOD);
                return Boolean.valueOf(false);
            }
        }

        tcePapelAlocacaoDao.create(entity);

        return Boolean.valueOf(true);
    }

    /**
     * Atualiza uma entidade.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    public void updateTcePapelAlocacao(final TcePapelAlocacao entity) {
        tcePapelAlocacaoDao.update(entity);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     * @return true se TcePapelAlocacao foi removida corretamente, coso
     *         contrário retorna false
     */
    public Boolean removeTcePapelAlocacao(final TcePapelAlocacao entity) {
        TcePapelAlocacao maxTcePapAloc = findMaxStartDateByPapelAlocacao(entity
                .getPapelAlocacao());

        // verifica se o último TcePapelAlocacao é o que esta sendo deletado
        if (maxTcePapAloc.getCodigoTcePapelAloc().compareTo(
                entity.getCodigoTcePapelAloc()) == 0) {

            // recupera a data de fechamento do MapaAlocacao
            Date closingDateMapaAlocacao = mapaAlocacaoService
                    .getClosingDateMapaAlocacao();

            // se a DataInicio do TCE for maior do que a data de fechamento
            // deleta normalmente
            if (entity.getDataInicio().after(closingDateMapaAlocacao)) {
                tcePapelAlocacaoDao.remove(entity);

                // pega o proximo maior, para setar a data final como null
                maxTcePapAloc = findMaxStartDateByPapelAlocacao(entity
                        .getPapelAlocacao());
                if (maxTcePapAloc != null) {
                    maxTcePapAloc.setDataFim(null);
                    updateTcePapelAlocacao(maxTcePapAloc);
                }

                return Boolean.valueOf(true);
            } else {
                Messages
                        .showError(
                                "removeTcePapelAlocacao",
                                Constants.MSG_ERROR_TCE_PAPEL_ALOCACAO_REMOVE_CLOS_DATE);
                return Boolean.valueOf(false);
            }
        } else {
            Messages.showError("removeTcePapelAlocacao",
                    Constants.MSG_ERROR_TCE_PAPEL_ALOCACAO_REMOVE);
            return Boolean.valueOf(false);
        }
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public TcePapelAlocacao findTcePapelAlocacaoById(final Long id) {
        return tcePapelAlocacaoDao.findById(id);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<TcePapelAlocacao> findTcePapelAlocacaoAll() {
        return tcePapelAlocacaoDao.findAll();
    }

    /**
     * Busca uma lista de entidades pelo PapelAlocacao.
     * 
     * @param papelAlocacao
     *            entidade populada com os valores do PapelAlocacao.
     * 
     * @return lista de entidades que atendem ao criterio do PapelAlocacao.
     */
    public List<TcePapelAlocacao> findTcePapelAlocacaoByPapelAlocacao(
            final PapelAlocacao papelAlocacao) {
        return tcePapelAlocacaoDao.findByPapelAlocacao(papelAlocacao);
    }

    /**
     * Busca a entidade com maior data inicio da vigencia.
     * 
     * @param papelAlocacao
     *            entidade populada com os valores do PapelAlocacao.
     * 
     * @return lista de entidades que atendem ao criterio do PapelAlocacao.
     */
    public TcePapelAlocacao findMaxStartDateByPapelAlocacao(
            final PapelAlocacao papelAlocacao) {
        return tcePapelAlocacaoDao
                .findMaxStartDateByPapelAlocacao(papelAlocacao);
    }
    
    /**
     * Busca a entidade no qual intervalo possua a 
     * pada passado por parametro, referente ao PapelAlocacao.
     * 
     * @param papelAlocacao
     *            entidade populada com os valores do PapelAlocacao.
     * @param date 
     *            Data que se deseja saber o valor do TCE do PapelAlocacao.
     * 
     * @return lista de entidades que atendem ao criterio do PapelAlocacao.
     */
    public TcePapelAlocacao findTcePapelAlocacaoByPapelAndDate(
            final PapelAlocacao papelAlocacao , final Date date) {
        return tcePapelAlocacaoDao.findByPapelAndDate(papelAlocacao, date);
    }

}