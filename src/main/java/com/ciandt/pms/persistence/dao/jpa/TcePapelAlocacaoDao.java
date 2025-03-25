package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.PapelAlocacao;
import com.ciandt.pms.model.TcePapelAlocacao;
import com.ciandt.pms.persistence.dao.ITcePapelAlocacaoDao;


/**
 * Define o DAO da entidade.
 * 
 * @author Alisson Fernando da Silva
 * @since 27/01/2010
 */
@Repository
public class TcePapelAlocacaoDao extends AbstractDao<Long, TcePapelAlocacao>
        implements ITcePapelAlocacaoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo TcePapelAlocacao
     */
    @Autowired
    public TcePapelAlocacaoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, TcePapelAlocacao.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<TcePapelAlocacao> findAll() {
        List<TcePapelAlocacao> listResult = getJpaTemplate().findByNamedQuery(
                TcePapelAlocacao.FIND_ALL);

        return listResult;
    }

    /**
     * Busca uma lista de entidades pelo PapelAlocacao.
     * 
     * @param papelAlocacao
     *            entidade populada com os valores do PapelAlocacao.
     * 
     * @return lista de entidades que atendem ao criterio do PapelAlocacao.
     */
    @SuppressWarnings("unchecked")
    public List<TcePapelAlocacao> findByPapelAlocacao(
            final PapelAlocacao papelAlocacao) {

        List<TcePapelAlocacao> listResult = getJpaTemplate().findByNamedQuery(
                TcePapelAlocacao.FIND_BY_PAPEL_ALOCACAO,
                new Object[] {papelAlocacao.getCodigoPapelAlocacao() });

        return listResult;
    }

    /**
     * Busca a entidade com maior data inicio da vigencia.
     * 
     * @param papelAlocacao
     *            entidade populada com os valores do PapelAlocacao.
     * 
     * @return lista de entidades que atendem ao criterio do PapelAlocacao.
     */
    @SuppressWarnings("unchecked")
    public TcePapelAlocacao findMaxStartDateByPapelAlocacao(
            final PapelAlocacao papelAlocacao) {

        List<TcePapelAlocacao> listResult = getJpaTemplate().findByNamedQuery(
                TcePapelAlocacao.FIND_MAX_START_DATE_BY_PAPEL_ALOCACAO,
                new Object[] {papelAlocacao.getCodigoPapelAlocacao(),
                        papelAlocacao.getCodigoPapelAlocacao() });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }
    
    /**
     * Busca a entidade no qual intervalo possua a 
     * pada passado por parametro, referente ao PapelAlocacao.
     * 
     * @param papelAlocacao
     *            entidade populada com os valores do PapelAlocacao.
     * @param date 
     *            Data que se deseja saber o valor do TCE do PapelAlocacao.
     * 
     * @return lista de entidades que atendem ao criterio do PapelAlocacao.
     */
    @SuppressWarnings("unchecked")
    public TcePapelAlocacao findByPapelAndDate(
            final PapelAlocacao papelAlocacao , final Date date) {
        
        List<TcePapelAlocacao> listResult = getJpaTemplate().findByNamedQuery(
                TcePapelAlocacao.FIND_BY_PAPEL_AND_DATE,
                new Object[] {papelAlocacao.getCodigoPapelAlocacao(), 
                        date, date});

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
        
    }

}