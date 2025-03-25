package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.Voucher;
import com.ciandt.pms.persistence.dao.IVoucherDao;


/**
 * 
 * A classe VoucherDao proporciona as funcionalidades de DAO para a entidade
 * Voucher.
 * 
 * @since 24/08/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Repository
public class VoucherDao extends AbstractDao<Long, Voucher> implements
        IVoucherDao {

    /**
     * Construtor.
     * 
     * @param factory
     *            factory
     */
    @Autowired
    public VoucherDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, Voucher.class);
    }

    /**
     * Busca por orcamento despesa.
     * 
     * @param entity
     *            orcamento despesa.
     * @return lista de entidades de orcamento despesa.
     */
    @SuppressWarnings("unchecked")
    public List<Voucher> findByOrcDesp(final OrcamentoDespesa entity) {
        List<Voucher> listResult =
                getJpaTemplate().findByNamedQuery(Voucher.FIND_BY_ORC_DESP,
                        new Object[]{entity.getCodigoOrcaDespesa()});
        return listResult;
    }

    /**
     * Busca todos os Vouchers com flag N na coluna de email enviado.
     * 
     * @return listResult
     */
    @SuppressWarnings("unchecked")
    public List<Voucher> findAllNotEmailSent() {
        List<Voucher> resultList =
                getJpaTemplate().findByNamedQuery(
                        Voucher.FIND_ALL_NOT_EMAIL_SENT);
        return resultList;
    }

    /**
     * Busca por numero do voucher (codigo).
     * @param numberVoucher numero do voucher
     * @return voucher
     */
    @SuppressWarnings("unchecked")
    public Voucher findByNumberVoucher(final String numberVoucher) {
        List<Voucher> listResult =
                getJpaTemplate().findByNamedQuery(
                        Voucher.FIND_BY_NUMBER_VOUCHER,
                        new Object[]{numberVoucher});
        return listResult.get(0);
    }

    /**
     * Busca Vouchers "OPEN" de {@code Pessoa} com data maior que {@code data}
     * 
     * @param Date
     * 			data
     * @return lista de {@link Voucher}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Voucher> findOpenVoucherByPessoaAndDate(final Pessoa pessoa, final Date data) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoPessoa", pessoa);
		params.put("dataCreation", data);

		List<Voucher> listResult = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						Voucher.FIND_OPEN_VOUCHER_BY_PESSOA_AND_DATE, params);

		return listResult;
    }

    /**
	 * Busca Vouchers "OPEN" de {@link OrcamentoDespesa} com data maior que
	 * {@code data}
	 * 
	 * @param OrcamentoDespesa
	 * 			orcamentoDespesa
	 * @param Date
	 * 			data
	 * @return lista de {@link Voucher}
	 */
    @Override
    @SuppressWarnings("unchecked")
    public List<Voucher> findOpenVoucherByOrcDespAndDate(final OrcamentoDespesa orcamentoDespesa, final Date date) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orcamentoDespesa", orcamentoDespesa);
		params.put("dataCreation", date);

		List<Voucher> listResult = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						Voucher.FIND_OPEN_VOUCHER_BY_ORC_DESP_AND_DATE, params);

		return listResult;
    }
}
