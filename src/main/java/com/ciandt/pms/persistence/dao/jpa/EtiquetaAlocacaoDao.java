package com.ciandt.pms.persistence.dao.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.EtiquetaAlocacao;
import com.ciandt.pms.model.EtiquetaAlocacaoId;
import com.ciandt.pms.persistence.dao.IEtiquetaAlocacaoDao;


/**
 * 
 * A classe EtiquetaDao proporciona as funcionalidades de acesso ao banco de
 * dados para a entidade Etiqueta.
 * 
 * @since 13/08/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class EtiquetaAlocacaoDao extends
        AbstractDao<EtiquetaAlocacaoId, EtiquetaAlocacao> implements
        IEtiquetaAlocacaoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public EtiquetaAlocacaoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, EtiquetaAlocacao.class);
    }

    /**
     * Busca um mapa de entidades pelo filtro.
     * 
     * @param codigoEtiqueta
     *            código da Etiqueta da busca.
     * @param codigoMapaAlocacao
     *            código do MapaAlocacao corrente.
     * 
     * @return mapa de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public Map<Long, EtiquetaAlocacao> findByEtiquetaAndMapaAlocacao(
            final Long codigoEtiqueta, final Long codigoMapaAlocacao) {
        if (codigoEtiqueta == null || codigoEtiqueta.longValue() <= 0
                || codigoMapaAlocacao == null || codigoMapaAlocacao <= 0) {
            return null;
        }

        List<EtiquetaAlocacao> listResult = getJpaTemplate().findByNamedQuery(
                EtiquetaAlocacao.FIND_BY_ETIQUETA_AND_MAPA_ALOCACAO,
                new Object[] {codigoEtiqueta, codigoMapaAlocacao });

        Map<Long, EtiquetaAlocacao> mapResult = new HashMap<Long, EtiquetaAlocacao>();

        for (EtiquetaAlocacao etiquetaAlocacao : listResult) {
            mapResult.put(etiquetaAlocacao.getId().getCodigoAlocacao(),
                    etiquetaAlocacao);
        }

        return mapResult;
    }

}
