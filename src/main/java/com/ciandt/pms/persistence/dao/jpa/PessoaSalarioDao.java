package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaSalario;
import com.ciandt.pms.persistence.dao.IPessoaSalarioDao;


/**
 * 
 * A classe PessoaSalarioDao proporciona as funcionalidades da camada de
 * persistencia referente a entidade PessoaSalario.
 * 
 * @since 02/08/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
@Deprecated
public class PessoaSalarioDao extends AbstractDao<Long, PessoaSalario>
        implements IPessoaSalarioDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            - fabrica da entidade.
     */
    public PessoaSalarioDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, PessoaSalario.class);
    }

    /**
     * Busca pelo proximo, referente a data de inicio.
     * 
     * @param pessoaSalario
     *            - entidade do tipo PessoaSalario.
     * 
     * @return retorna o proximo PessoaSalario.
     */
    @SuppressWarnings("unchecked")
    public PessoaSalario findNext(final PessoaSalario pessoaSalario) {

        Long codigo = pessoaSalario.getPessoa().getCodigoPessoa();

        List<PessoaSalario> listResult = getJpaTemplate().findByNamedQuery(
                PessoaSalario.FIND_NEXT,
                new Object[] {codigo, codigo, pessoaSalario.getDataInicio() });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    /**
     * Busca pelo anterior, referente a data de inicio.
     * 
     * @param pessoaSalario
     *            - entidade do tipo PessoaSalario.
     * 
     * @return retorna o PessoaSalario anterior.
     */
    @SuppressWarnings("unchecked")
    public PessoaSalario findPrevious(final PessoaSalario pessoaSalario) {

        Long codigo = pessoaSalario.getPessoa().getCodigoPessoa();

        List<PessoaSalario> listResult = getJpaTemplate().findByNamedQuery(
                PessoaSalario.FIND_PREVIOUS,
                new Object[] {codigo, codigo, pessoaSalario.getDataInicio() });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    /**
     * Busca pelo fator de reajuste referente a data de inicio.
     * 
     * @param pessoaSalario
     *            - entidade do tipo PessoaSalario.
     * 
     * @return retorna o PessoaSalario.
     */
    @SuppressWarnings("unchecked")
    public PessoaSalario findByStartDate(final PessoaSalario pessoaSalario) {

        List<PessoaSalario> listResult = getJpaTemplate().findByNamedQuery(
                PessoaSalario.FIND_BY_START_DATE,
                new Object[] {pessoaSalario.getDataInicio(),
                        pessoaSalario.getPessoa().getCodigoPessoa() });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    /**
     * Busca pela Pessoa e pela Data de vigencia.
     * 
     * @param pessoa
     *            - Pessoa utilizado na busca.
     * @param dataMes
     *            - data da vigencia.
     * 
     * @return a entidade que atende aos criterios do filtro
     */
    @SuppressWarnings("unchecked")
    public PessoaSalario findByPessoaAndDate(final Pessoa pessoa,
            final Date dataMes) {

        List<PessoaSalario> listResult = getJpaTemplate().findByNamedQuery(
                PessoaSalario.FIND_BY_PESSOA_AND_DATE,
                new Object[] {pessoa.getCodigoPessoa(), dataMes, dataMes });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

}