package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.Voucher;


/**
 * 
 * A classe IVoucherService proporciona as funcionalidades de interface para
 * VoucherService.
 * 
 * @since 24/08/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Service
public interface IVoucherService {

    /**
     * Update Voucher.
     * 
     * @param voucher
     *            voucher
     */
	@Transactional
    void update(final Voucher voucher);

    /**
     * Busca por orcamento despesa.
     * 
     * @param entity
     *            orcamento despesa.
     * @return lista de entidades de orcamento despesa.
     */
    List<Voucher> findVoucherByOrcDesp(final OrcamentoDespesa entity);

    /**
     * Busca todos os Vouchers com flag N na coluna de email enviado.
     * 
     * @return listResult
     */
    List<Voucher> findAllNotEmailSent();

    /**
     * Busca voucher por id.
     * 
     * @param id
     *            id
     * @return voucher
     */
    Voucher findById(final Long id);

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
