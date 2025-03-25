package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.MapaDespesa;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.persistence.dao.IMapaDespesaDao;


/**
 * 
 * A classe MapaDespesaDao proporciona as funcionalidades da camada
 * de persistencia referente a entidade MapaDespesa.
 *
 * @since 28/04/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Repository
public class MapaDespesaDao extends AbstractDao<Long, MapaDespesa> implements
        IMapaDespesaDao {

    /**
     * Construtor padrão.
     * 
     * @param factory - Fabrica da entidade MapaDespesaDao.
     */
    public MapaDespesaDao(@Qualifier("entityManagerFactory") 
            final EntityManagerFactory factory) {
        super(factory, MapaDespesa.class);
    }

    /**
     * Busca um mapa de despesa pelo contrato pratica. 
     * 
     * @param cp - Entidade do tipo ContratoPratica, utilizado na busca.
     * 
     * @return retorna uma entidade do tipo MapaDespesa
     */
    @SuppressWarnings("unchecked")
    public MapaDespesa findByContratoPratica(final ContratoPratica cp) {
        
        List<MapaDespesa> listResult = getJpaTemplate()
            .findByNamedQuery(MapaDespesa.FIND_BY_CONTRATO_PRATICA,
                new Object[] {cp.getCodigoContratoPratica()});

        if (listResult.isEmpty()) {
            return null;
        }
        
        return listResult.get(0);
    }
    
    /**
     * Busca uma lista de entidades pelo filtro. 
     * 
     * @param filter
     *            entidade populada com os valores do filtro e carrega.
     * @param cli
     *          entidade Cliente
     * @param msa 
     *          entidade Msa
     * @param natureza
     *          entidade NaturezaCentroLucro
     * @param centroLucro 
     *          entidade CentroLucro         
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<MapaDespesa> findByFilter(final MapaDespesa filter,
            final Cliente cli, final Msa msa, 
            final NaturezaCentroLucro natureza, final CentroLucro centroLucro) {
        
        Long codigoContratoPratica = Long.valueOf(0);
        if (filter.getContratoPratica() != null) {
            codigoContratoPratica = filter.getContratoPratica().getCodigoContratoPratica(); 
        }
        
        Long idCliente = cli.getCodigoCliente();
        if (idCliente == null) {
            idCliente = Long.valueOf(0);
        }
        
        Long idContrato = msa.getCodigoMsa();
        if (idContrato == null) {
            idContrato = Long.valueOf(0);
        }
        
        Long idNatureza = natureza.getCodigoNatureza();
        if (idNatureza == null) {
            idNatureza = Long.valueOf(0);
        }
        
        Long idCentroLucro = centroLucro.getCodigoCentroLucro();
        if (idCentroLucro == null) {
            idCentroLucro = Long.valueOf(0);
        }
        
        List<MapaDespesa> listResult = getJpaTemplate()
                .findByNamedQuery(MapaDespesa.FIND_BY_FILTER,
                        new Object[] {
                                codigoContratoPratica, codigoContratoPratica,
                                idContrato, idContrato,
                                idCliente, idCliente,
                                idCentroLucro, idCentroLucro,
                                idNatureza, idNatureza});
        return listResult;
    }

}
