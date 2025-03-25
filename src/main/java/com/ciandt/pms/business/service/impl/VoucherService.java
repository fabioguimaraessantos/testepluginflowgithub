package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IVoucherService;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.Voucher;
import com.ciandt.pms.persistence.dao.IVoucherDao;


/**
 * 
 * A classe VoucherService proporciona as funcionalidades de servico para a
 * entidade Voucher.
 * 
 * @since 24/08/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Service
public class VoucherService implements IVoucherService {

    /** Instancia de VoucherDao. */
    @Autowired
    private IVoucherDao voucherDao;
    
    
    /**
     * Update Voucher.
     * @param voucher voucher
     */
    public void update(final Voucher voucher) {
        voucherDao.update(voucher);
    }

    /**
     * Busca por orcamento despesa.
     * 
     * @param entity
     *            orcamento despesa.
     * @return lista de entidades de orcamento despesa.
     */
    public List<Voucher> findVoucherByOrcDesp(final OrcamentoDespesa entity) {
        return voucherDao.findByOrcDesp(entity);
    }

    /**
     * Busca todos os Vouchers com flag N na coluna de email enviado.
     * 
     * @return listResult
     */
    public List<Voucher> findAllNotEmailSent() {
        return voucherDao.findAllNotEmailSent();
    }

    /**
     * Busca voucher por id.
     * 
     * @param id
     *            id
     * @return voucher
     */
    public Voucher findById(final Long id) {
        return voucherDao.findById(id);
    }

    /**
     * Busca por numero do voucher (codigo).
     * 
     * @param numberVoucher
     *            numero do voucher
     * @return voucher
     */
    public Voucher findByNumberVoucher(final String numberVoucher) {       
        return voucherDao.findByNumberVoucher(numberVoucher);
    }
    
    /**
     * Busca Vouchers "OPEN" de {@code Pessoa} com data maior que {@code data}
     * 
     * @param Date
     * 			data
     * @return lista de {@link Voucher}
     */
    public List<Voucher> findOpenVoucherByPessoaAndDate(final Pessoa pessoa, final Date data) {
    	return voucherDao.findOpenVoucherByPessoaAndDate(pessoa, data);
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
    public List<Voucher> findOpenVoucherByOrcDespAndDate(final OrcamentoDespesa orcamentoDespesa, final Date date) {
    	return voucherDao.findOpenVoucherByOrcDespAndDate(orcamentoDespesa, date);
    }
}
