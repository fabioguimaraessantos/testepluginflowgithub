package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.VwEstratificacaoUrResultado;
import com.ciandt.pms.model.VwEstratificacaoUrResultadoId;
import com.ciandt.pms.persistence.dao.IVwEstratificacaoUrResultadoDao;


/**
 * 
 * A classe VwTceFuncionarioDao proporciona as funcionalidades da camada de
 * persistencia referente a entidade VwTceFuncionario.
 * 
 * @since 03/03/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class VwEstratificacaoUrResultadoDao extends
        AbstractDao<VwEstratificacaoUrResultadoId, VwEstratificacaoUrResultado>
        implements IVwEstratificacaoUrResultadoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            - fabrica da entidade.
     */
    @Autowired
    public VwEstratificacaoUrResultadoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {

        super(factory, VwEstratificacaoUrResultado.class);

    }

    /**
     * Busca as entidades por contratoPratica e data.
     * 
     * @param contratoPratica
     *            - contratoPratica para busca
     * @param dataMes
     *            - data para busca
     * @return lista de entidades.
     */
    @SuppressWarnings("unchecked")
    public List<VwEstratificacaoUrResultado> findByContratoPraticaAndDataMes(
            final ContratoPratica contratoPratica, final Date dataMes) {

		List<VwEstratificacaoUrResultado> listResult = getJpaTemplate()
				.findByNamedQuery(
						VwEstratificacaoUrResultado.FIND_BY_CONTRATO_PRATICA_AND_DATA_MES,
						new Object[] {
								contratoPratica.getCodigoContratoPratica(),
								dataMes });

        return listResult;
    }

}
