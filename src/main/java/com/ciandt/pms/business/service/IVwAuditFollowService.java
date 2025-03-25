package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.VwAuditFollow;


/**
 * 
 * A classe IVwAuditFollowService proporciona a interface de acesso a camada de
 * serviço referente a entidade VwAuditFollow.
 * 
 * @since 28/06/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IVwAuditFollowService {

    /**
     * Busca as entidades filtradas de acordo com os parametros.
     * 
     * @param codigoMapaAloc
     *            - codigo do MapaAlocacao da revisao
     * @param revDate
     *            - data da revisao
     * 
     * @return retorna uma lista com as entidades filtradas de acordo com os
     *         parametros.
     */
    List<VwAuditFollow> findAuFlByMapaAlocAndDate(final Long codigoMapaAloc,
            final Date revDate);

    /**
     * Busca as entidades filtradas de acordo com os parametros.
     * 
     * @param codigoRecurso
     *            - codigo do Recurso da revisao
     * @param revDate
     *            - data da revisao
     * 
     * @return retorna uma lista com as entidades filtradas de acordo com os
     *         parametros.
     */
    List<VwAuditFollow> findAuFlByRecursoAndDate(final Long codigoRecurso,
            final Date revDate);

    /**
     * Busca as entidades filtradas de acordo com os parametros. Retorna somente
     * uma lista com os nomes dos autores das auditorias (sem repetir).
     * 
     * @param codigoMapaAloc
     *            - codigo do MapaAlocacao da revisao
     * @param revDate
     *            - data da revisao
     * @param isClobFollow
     *            - indicador se é follow de C-lob ou não (People)
     * 
     * @return retorna uma lista (de String) com os nomes dos autores das
     *         auditorias filtradas pelos parametros.
     */
    List<String> findAuthorsByCodAndDate(final Long codigoMapaAloc,
            final Date revDate, final Boolean isClobFollow);

    /**
     * Retorna a lista de Autores (nomes) em um String separados por virgula.
     * 
     * @param authorsList
     *            - lista de nomes de autores
     * 
     * @return a lista de Autores (nomes) em um String separados por virgula
     */
    String getAuthorsString(final List<String> authorsList);

}