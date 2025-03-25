package com.ciandt.pms.business.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IVwAuditFollowService;
import com.ciandt.pms.model.VwAuditFollow;
import com.ciandt.pms.persistence.dao.IVwAuditFollowDao;


/**
 * 
 * A classe VwAuditFollowService proporciona as funcionalidades da camada de
 * serviço referente a entidade VwAuditFollow.
 * 
 * @since 28/06/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class VwAuditFollowService implements IVwAuditFollowService {

    /** Instancia DAO da entidade VwAuditFollow. */
    @Autowired
    private IVwAuditFollowDao auditFollowDao;

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
    public List<VwAuditFollow> findAuFlByMapaAlocAndDate(
            final Long codigoMapaAloc, final Date revDate) {
        return auditFollowDao.findByMapaAlocAndDate(codigoMapaAloc, revDate);
    }

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
    public List<VwAuditFollow> findAuFlByRecursoAndDate(
            final Long codigoRecurso, final Date revDate) {
        return auditFollowDao.findByRecursoAndDate(codigoRecurso, revDate);
    }

    /**
     * Busca as entidades filtradas de acordo com os parametros. Retorna somente
     * uma lista com os nomes dos autores das auditorias (sem repetir).
     * 
     * @param codigo
     *            - codigo (Mapa ou Recurso) da revisao
     * @param revDate
     *            - data da revisao
     * @param isClobFollow
     *            - indicador se é follow de C-lob ou não (People)
     * 
     * @return retorna uma lista (de String) com os nomes dos autores das
     *         auditorias filtradas pelos parametros.
     */
    public List<String> findAuthorsByCodAndDate(final Long codigo,
            final Date revDate, final Boolean isClobFollow) {
        // variaveis
        List<String> authorsList = new ArrayList<String>();
        Map<String, String> authorMapAux = new HashMap<String, String>();

        // busca a lista de registros das revisoes (auditoria) do follow, pelo
        // codigo (Mapa ou Recurso) e data
        List<VwAuditFollow> auditFollowList = null;
        if (isClobFollow) {
            auditFollowList = this.findAuFlByMapaAlocAndDate(codigo, revDate);
        } else {
            auditFollowList = this.findAuFlByRecursoAndDate(codigo, revDate);
        }

        // itera a lista e guarda em um Map para guardar um de cada, sem
        // repeticoes de nomes
        for (VwAuditFollow vwAuFl : auditFollowList) {
            String codigoAutor = vwAuFl.getId().getCodigoAutor();
            authorMapAux.put(codigoAutor, codigoAutor);
        }

        // guarda na lista final de retorno
        Iterator<String> it = authorMapAux.keySet().iterator();
        while (it.hasNext()) {
            authorsList.add(it.next());
        }

        // ordena a lista
        Collections.sort(authorsList);

        return authorsList;
    }

    /**
     * Retorna a lista de Autores (nomes) em um String separados por virgula.
     * 
     * @param authorsList
     *            - lista de nomes de autores
     * 
     * @return a lista de Autores (nomes) em um String separados por virgula
     */
    public String getAuthorsString(final List<String> authorsList) {
        if (!authorsList.isEmpty()) {
            StringBuffer authorsStr = new StringBuffer();
            for (String autor : authorsList) {
                authorsStr.append(autor + ", ");
            }

            return authorsStr.substring(0, authorsStr.length() - 2).toString();
        } else {
            return "";
        }
    }

}