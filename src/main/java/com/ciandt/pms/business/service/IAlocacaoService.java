package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.Alocacao;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.PerfilVendido;
import com.ciandt.pms.model.Recurso;
import com.ciandt.pms.model.vo.AlocacaoRow;


/**
 * 
 * A classe IAlocacaoService proporciona a interface de servico para a entidade
 * Alocação.
 * 
 * @since 14/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface IAlocacaoService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    @Transactional
    void createAlocacao(final Alocacao entity);

    /**
     * Atualiza a entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    @Transactional
    void updateAlocacao(final Alocacao entity);

    /**
     * Remove a entidade passado por parametro.
     * 
     * @param entity
     *            que será removida.
     * 
     */
    @Transactional
    void removeAlocacao(final Alocacao entity);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    Alocacao findAlocacaoById(final Long id);

    /**
     * Realiza o update a data inicio e fim.
     * 
     * @param alocacao
     *            - alocação a ser editada
     * @param startDate
     *            - data inicio
     * @param endDate
     *            - data fim
     * 
     * @return retorna true se inserido com sucesso caso contrario false.
     */
    @Transactional
    Boolean updateAlocacaoValidity(final Alocacao alocacao,
            final Date startDate, final Date endDate);

    /**
     * Busca por alocações de uma mapa.
     * 
     * @param mapa
     *            - entidade do tipo MapaAlocacao.
     * 
     * @return uma lista de Alocações do Mapa.
     */
    List<Alocacao> findAlocacaoByMapaAlocacao(final MapaAlocacao mapa);

    /**
     * Metodo que realiza a cópia de Alocacao. Cada cópia o PerfilVendido
     * 
     * @param alocacaoRowList
     *            - lista de Alocacao a serem copiadas
     * @param perfilVendido
     *            - PerfilVendido das Alocacao clonadas
     * @param indicadorEstagioClone
     *            - indicadorEstagio das Alocacao clonadas
     * @param startMonthClone
     *            - mes da data inicial para fazer o clone
     * @param startYearClone
     *            - ano da data inicial para fazer o clone
     * @param validityMonthEnd
     *            - mes da data final da vigencia do MapaAlocacao
     * @param validityYearEnd
     *            - ano da data final da vigencia do MapaAlocacao
     * 
     * @return true se copiou normalmente false caso tenha ocorrido algum erro
     */
    @Transactional
    Boolean copyAlocacao(final List<AlocacaoRow> alocacaoRowList,
            final PerfilVendido perfilVendido,
            final String indicadorEstagioClone, final String startMonthClone,
            final String startYearClone, final String validityMonthEnd,
            final String validityYearEnd);

    /**
     * Busca por alocações de uma mapa dentro de um periodo.
     * 
     * @param mapa
     *            - entidade do tipo MapaAlocacao.
     * 
     * @return uma lista de Alocações do Mapa.
     */
    List<Alocacao> findAlocacaoByMapaAlocacaoAndPeriod(final MapaAlocacao mapa);

    /**
     * Busca por alocações de uma mapa dentro de um periodo.
     * 
     * @param mapa
     *            - entidade do tipo MapaAlocacao.
     * @param startDate
     *            - data inicio do perido
     * @param endDate
     *            - data fim do periodo
     * 
     * @return uma lista de Alocações do Mapa.
     */
    List<Alocacao> findAlocacaoByMapaAlocacaoAndPeriod(final MapaAlocacao mapa,
            final Date startDate, final Date endDate);

    /**
     * Busca a alocacao de uma mapa de acordo com o recurso e perfilvendido.
     * 
     * @param mapa
     *            - entidade do tipo MapaAlocacao.
     * 
     * @param recurso
     *            - entidade do tipo Recurso.
     * 
     * @param perfil
     *            - entidade do tipo PerfilVendido.
     * 
     * @return uma Alocacao.
     */
    Alocacao findByMapaAlocacaoAndRecursoAndPerfil(final MapaAlocacao mapa,
            final Recurso recurso, final PerfilVendido perfil);

    /**
     * Busca alocações vigentes do mapa relacionado ao contract-lob
     *
     * @param contratoPraticaCode ID do Contract-LOB
     * @param closingMapDate data do ultimo fechamento
     * @return {@link List<Alocacao>} vigentes
     */
    List<Alocacao> findVigentesByContratoPratica(Long contratoPraticaCode, Date closingMapDate);

}