/**
 * 
 */
package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.MapaAlocacaoFoto;
import com.ciandt.pms.model.vo.AlocacaoFotoRow;


/**
 * 
 * A classe IMapaAlocacaoFotoService proporciona a interface de acesso para a
 * camada de serviço referente a entidade MapaAlocacaoFoto.
 * 
 * @since 17/02/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IMapaAlocacaoFotoService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    @Transactional
    void createMapaAlocacaoFoto(final MapaAlocacaoFoto entity);

    /**
     * Insere uma lista de entidades.
     * 
     * @param mapaAlocList
     *            - lista de MapaAlocacao (snapshots) a serem gravados como
     *            foto.
     */
    @Transactional
    void createMapaAlocFotoList(final List<MapaAlocacao> mapaAlocList);

    /**
     * Atualiza a entidade.
     * 
     * @param entity
     *            entidade a ser atualizada.
     */
    @Transactional
    void updateMapaAlocacaoFoto(final MapaAlocacaoFoto entity);

    /**
     * Remove a entidade.
     * 
     * @param entity
     *            entidade a ser removida.
     * 
     */
    @Transactional
    void removeMapaAlocacaoFoto(final MapaAlocacaoFoto entity);

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    MapaAlocacaoFoto findMapaAlocacaoFotoById(final Long entityId);

    /**
     * Prepara uma matriz de alocações e percentuais Previous e Current para
     * serem exibidos nos e-mails para os followers.
     * 
     * @param startDate
     *            - data de início do range do follow
     * @param isClobFollow
     *            - indicador se é follow de C-lob ou não (People)
     * 
     * @return uma matriz de alocações e percentuais Previous e Current
     */
    Map<Long, List<AlocacaoFotoRow>> prepareMapaAlocSnapshotsMatrix(
            final Date startDate, final Boolean isClobFollow);
    
    /**
     * Envia o e-mail para os followers do MapaAlocacao e Pessoa.
     * 
     * @param mapaAlocSnapshotsMatrix
     *            - matriz com os dados da comparação
     * @param startDate
     *            - data de início do range do follow
     * @param isClobFollow
     *            - indicador se é follow de C-lob ou não (People)
     */
    void sendMapaAlocSnapshotsMail(
            final Map<Long, List<AlocacaoFotoRow>> mapaAlocSnapshotsMatrix,
            final Date startDate, final Boolean isClobFollow);

    /**
     * Atualiza o status dos registros as tabelas Foto.
     * 
     * @return retorna o status da execução da atualização. false caso erro,
     *         true caso sucesso.
     */
    Boolean updateStatusFotos();
    
    /**
     * Envia um email informando algum erro no Job.
     * 
     * @param errorMsg
     *            - mensagem do erro
     * @param stackTrace
     *            - stackTrace do erro
     */
    void sendSnapshotsErrorMail(final String errorMsg, final String stackTrace);

}