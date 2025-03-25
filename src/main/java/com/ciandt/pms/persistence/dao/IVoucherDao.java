package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.Voucher;


/**
 * 
 * A classe IVoucherDao proporciona as funcionalidades de interface para
 * VoucherDao.
 * 
 * @since 24/08/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Repository
public interface IVoucherDao extends IAbstractDao<Long, Voucher> {

    /**
     * Busca por orcamento despesa.
     * 
     * @param entity
     *            orcamento despesa.
     * @return lista de entidades de orcamento despesa.
     */
    List<Voucher> findByOrcDesp(final OrcamentoDespesa entity);

    /**
     * Busca todos os Vouchers com flag N na coluna de email enviado.
     * 
     * @return listResult
     */
    List<Voucher> findAllNotEmailSent();

    /**
     * Busca por numero do voucher (codigo).
     * 
     * @param numberVoucher
     *            numero do voucher
     * @return voucher
     */
    Voucher findByNumberVoucher(final String numberVoucher);

    /**
     * Busca Vouchers "OPEN" de {@code Pessoa} com data maior que {@code data}
     * 
     * @param Pessoa
     * 			pessoa
     * @param Date
     * 			data
     * @return lista de {@link Voucher}
     */
    List<Voucher> findOpenVoucherByPessoaAndDate(final Pessoa pessoa, final Date data);

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
    List<Voucher> findOpenVoucherByOrcDespAndDate(final OrcamentoDespesa orcamentoDespesa, final Date date);
}
