/**
 * Classe FaturaDao que implementa a camada de DAO da entidade Fatura 
 */
package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.FaturaApagada;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.persistence.dao.IFaturaApagadaDao;
import com.ciandt.pms.Constants;


/**
 * 
 * A classe FaturaApagadaDao proporciona as funcionalidades de acesso ao banco de dados
 * para a entidade FaturaApagada.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * 
 */
@Repository
public class FaturaApagadaDao extends AbstractDao<Long, FaturaApagada> implements IFaturaApagadaDao {

    /** Intancia do entity manager do hibernate. */
    private EntityManager entityManager;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public FaturaApagadaDao(@Qualifier("entityManagerFactory") 
            final EntityManagerFactory factory) {
        super(factory, FaturaApagada.class);

        entityManager = factory.createEntityManager();
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * @param dataPrevisaoBeg
     *            dataPrevisao inicial.
     * @param dataPrevisaoEnd
     *            dataPrevisao final.
     * @param cli
     *          entidade cliente.
     * @param msa
     *          entidade msa.                    
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @Override
    @SuppressWarnings("unchecked")
	public List<FaturaApagada> findByFilter(final Fatura filter,
			final Cliente cli, final Msa msa, final Date dataPrevisaoBeg,
			final Date dataPrevisaoEnd) {

        Long codigoCliente = Long.valueOf(0);
        if (cli != null) {
            codigoCliente = cli.getCodigoCliente();
        }

        Long codigoMsa = Long.valueOf(0);
        if (msa != null) {
            codigoMsa = msa.getCodigoMsa();
        }

        Long codigoDealFiscal = Long.valueOf(0);
        if (filter.getDealFiscal() != null) {
            codigoDealFiscal = filter.getDealFiscal().getCodigoDealFiscal();
        }

        Long codigoMoeda = Long.valueOf(0);
        if (filter.getMoeda() != null) {
            codigoMoeda = filter.getMoeda().getCodigoMoeda();
        }

        String numeroDoc = null;
        if (filter.getNumeroDoc() != null) {
            numeroDoc = filter.getNumeroDoc();
        }

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dataPrevisaoBeg", dataPrevisaoBeg);
		params.put("dataPrevisaoEnd", dataPrevisaoEnd);
		params.put("codigoCliente", codigoCliente);
		params.put("codigoMsa", codigoMsa);
		params.put("codigoDealFiscal", codigoDealFiscal);
		params.put("codigoMoeda", codigoMoeda);
		params.put("numeroDoc", numeroDoc);		
		params.put("codigoLoginAe", filter.getCodigoLoginAe());

        List<FaturaApagada> results;
        params.put("indicadorStatus", filter.getIndicadorStatus());
        results = getJpaTemplate().findByNamedQueryAndNamedParams(FaturaApagada.FIND_BY_FILTER, params);

		return results;
	}
}