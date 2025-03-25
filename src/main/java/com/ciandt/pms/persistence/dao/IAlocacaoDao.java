package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ciandt.pms.model.Alocacao;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.PerfilVendido;
import com.ciandt.pms.model.Recurso;


/**
 * 
 * A classe IAlocacaoDao proporciona a interface de DAO para a entidade
 * Alocacao.
 * 
 * @since 14/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public interface IAlocacaoDao extends IAbstractDao<Long, Alocacao> {

    /**
     * Busca por alocações de uma mapa.
     * 
     * @param mapa
     *            - entidade do tipo MapaAlocacao.
     * 
     * @return uma lista de Alocações do Mapa.
     */
    List<Alocacao> findByMapaAlocacao(final MapaAlocacao mapa);

    /**
     * Busca por alocações de uma mapa dentro de um periodo.
     * 
     * @param mapa
     *            - entidade do tipo MapaAlocacao.
     * 
     * @param startDate
     *            - Data inicial da vigencia
     * 
     * @param endDate
     *            - Data final da vigencia.
     * 
     * @return uma lista de Alocações do Mapa.
     */
    List<Alocacao> findByMapaAlocacaoAndPeriod(final MapaAlocacao mapa,
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
     * Busca as alocações vigentes a partir do contract-lob.
     * @param contratoPraticaCode o código do contract-lob
     * @param closingMapDate data do ultimo fechamento
     * @return {@link List<Alocacao>} lista de alocações vigentes
     */
    List<Alocacao> findVigentesByContratoPratica(Long contratoPraticaCode, Date closingMapDate);
}
