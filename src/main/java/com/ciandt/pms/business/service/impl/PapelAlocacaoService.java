package com.ciandt.pms.business.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.IPapelAlocacaoService;
import com.ciandt.pms.business.service.IRecursoService;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.PapelAlocacao;
import com.ciandt.pms.model.Recurso;
import com.ciandt.pms.model.TcePapelAlocacao;
import com.ciandt.pms.persistence.dao.IPapelAlocacaoDao;


/**
 * Define o Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 01/08/2009
 */
@Service
public class PapelAlocacaoService implements IPapelAlocacaoService {

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /**
     * Instancia do DAO da entidade.
     */
    @Autowired
    private IPapelAlocacaoDao papelAlocacaoDao;

    /** Instancia do servico Recurso. */
    @Autowired
    private IRecursoService recursoService;

    /** Instancia do servico Moeda. */
    @Autowired
    private IMoedaService moedaService;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createPapelAlocacao(final PapelAlocacao entity) {
        // cria primeiramente o Recurso, associa ao PapelAlocacao e cria o
        // PapelAlocacao
        Recurso recurso = new Recurso();
        recurso.setCodigoMnemonico(entity.getNomePapelAlocacao());
        recurso.setIndicadorTipoRecurso(Constants.RESOURCE_TYPE_PA);
        recurso.setIndicadorAtivo(Constants.ACTIVE);
        recursoService.createRecurso(recurso);

        // atribui o Recurso criado ao PapelAlocacao
        entity.setRecurso(recurso);
        papelAlocacaoDao.create(entity);
    }

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    public void updatePapelAlocacao(final PapelAlocacao entity) {
        //realiza o update do papel alocacao
        papelAlocacaoDao.update(entity);
        //realiza o update do recurso, 
        //associado ao papel alocacao
        Recurso recurso = entity.getRecurso();
        recurso.setCodigoMnemonico(entity.getNomePapelAlocacao());
        recursoService.updateRecurso(recurso);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * @throws IntegrityConstraintException
     *             - tratamento erro de referência de integridade
     * 
     */
    public void removePapelAlocacao(final PapelAlocacao entity)
            throws IntegrityConstraintException {
        // verifica se existem TcePapelAlocacao relacionados, se sim lança
        // exceção
        if (entity.getTcePapelAlocacaos().size() > 0) {
            throw new IntegrityConstraintException(
                    Constants.DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE);
        }

        // após remover o PapelAlocacao, remove o Recurso associado
        Long codigoRecurso = entity.getRecurso().getCodigoRecurso();
        papelAlocacaoDao.remove(entity);
        recursoService.removeRecurso(recursoService
                .findRecursoById(codigoRecurso));
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public PapelAlocacao findPapelAlocacaoById(final Long id) {
        PapelAlocacao papelAlocacao = papelAlocacaoDao.findById(id);
        if (papelAlocacao != null) {
            List<TcePapelAlocacao> tcePapelAlocacaoList = papelAlocacao
                    .getTcePapelAlocacaos();
            if (tcePapelAlocacaoList != null && !tcePapelAlocacaoList.isEmpty()) {
                orderTcePapelAlocacaoList(tcePapelAlocacaoList);
            }
        }

        return papelAlocacao;
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public List<PapelAlocacao> findPapelAlocacaoByFilter(
            final PapelAlocacao filter) {
        return papelAlocacaoDao.findByFilter(filter);
    }

    /**
     * Busca uma lista de entidades pelo filtro - Fetch.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public List<PapelAlocacao> findPapelAlocacaoByFilterFetch(
            final PapelAlocacao filter) {
        return papelAlocacaoDao.findByFilterFetch(filter);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<PapelAlocacao> findPapelAlocacaoAll() {

        return papelAlocacaoDao.findAll();
    }
    
    /**
     * Retorna todas as entidades ativas.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<PapelAlocacao> findPapelAlocacaoAllActive() {

        return papelAlocacaoDao.findAllActive();
    }

    /**
     * Recuprea a moeda corrente.
     * 
     * @param moeda
     *            - Moeda corrente
     * @return o pattern da Moeda.
     */
    public String getCurrency(final Moeda moeda) {
        if (moeda != null && moeda.getCodigoMoeda() != null
                && !StringUtils.isEmpty(moeda.getSiglaMoeda())) {
            // atribui o pattern dos valores conforme sigla da moeda
            return systemProperties
                    .getProperty(Constants.DEFAULT_PROPERTY_PATTERN_CURRENCY
                            + moeda.getSiglaMoeda().toLowerCase());
            // se a Moeda é null, força Moeda padrão por default no TCE do
            // PapelAlocacao
        } else {
            String defaultCurrencyCode = systemProperties
                    .getProperty(Constants.DEFAULT_PROPERTY_CURRENCY_CODE);

            if (!StringUtils.isEmpty(defaultCurrencyCode)) {
                Moeda defaultMoeda = moedaService.findMoedaById(Long
                        .valueOf(defaultCurrencyCode));

                if (defaultMoeda != null) {
                    return defaultMoeda.getSiglaMoeda();
                }
            }
        }

        return "";
    }

    /**
     * Ordena a lista de TcePapelAlocacao do PapelAlocacao corrente.
     * 
     * @param tcePapelAlocacaoList
     *            - lista de TcePapelAlocacao do PapelAlocacao corrente
     */
    private void orderTcePapelAlocacaoList(
            final List<TcePapelAlocacao> tcePapelAlocacaoList) {
        Collections.sort(tcePapelAlocacaoList,
                new Comparator<TcePapelAlocacao>() {
                    public int compare(final TcePapelAlocacao tcePA1,
                            final TcePapelAlocacao tcePA2) {
                        return tcePA1.getDataInicio().compareTo(
                                tcePA2.getDataInicio());
                    }
                });
    }

    /**
     * Retorna todas as entidades.
     * 
     * @param recurso
     *            - Recurso associado ao PapelAlocacao.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public PapelAlocacao findPapelAlocacaoByRecurso(final Recurso recurso) {
        return papelAlocacaoDao.findByRecurso(recurso);
    }

    /**
     * Busca uma lista de entidades sem associacao com o TcePapelAlocacao.
     * 
     * @param dataMes
     *            - data do fechamento.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public List<PapelAlocacao> findPapelAlocAllNotTceAssociated(final Date dataMes) {
        return papelAlocacaoDao.findAllNotTceAssociated(dataMes);
    }

}