package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IMsaSaldoMoedaService;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.MsaSaldoMoeda;
import com.ciandt.pms.persistence.dao.IMsaSaldoMoedaDao;

/**
 * 
 * A classe MsaSaldoMoedaService proporciona as funcionalidades da camada de
 * serviço referente a entidade MsaSaldoMoeda.
 * 
 * @since 28/09/2012
 * @author <a href="mailto:diegos@ciandt.com">Diego Henrique Mila da Silva</a>
 * 
 */
@Service
public class MsaSaldoMoedaService implements IMsaSaldoMoedaService {

	/** Instancia do DAO do MsaSaldoMoeda. */
	@Autowired
	private IMsaSaldoMoedaDao dao;

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que será removida.
	 */
	public void removeMsaSalMoe(final MsaSaldoMoeda entity) {
		dao.remove(entity);
	}

	/**
	 * Atualiza uma entidade.
	 * 
	 * @param entity
	 *            que será atulizada.
	 */
	public void updateMsaSalMoe(final MsaSaldoMoeda entity) {
		dao.update(entity);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista de MsaSaldoMoeda.
	 */
	public List<MsaSaldoMoeda> findMsaSalMoeAll() {
		return dao.findAll();
	}

	/**
	 * Busca todas as entidades pelo Msa.
	 * 
	 * @return retorna uma lista de MsaSaldoMoeda do Msa passado por parâmetro.
	 */
	public List<MsaSaldoMoeda> findMsaSalMoeByMsa(final Msa msa) {
		return dao.findByMsa(msa);
	}

	/**
	 * Busca a entidade pelo id.
	 * 
	 * @param id
	 *            - id da entidade
	 * 
	 * @return retorna uma instancia do tipo MsaSaldoMoeda
	 */
	public MsaSaldoMoeda findMsaSalMoeById(final Long id) {
		return dao.findById(id);
	}

	/**
	 * Busca todas as entidades pro msa e ativas.
	 * 
	 * @param msa
	 *            the msa
	 * @return lista de entidades.
	 */
	public List<MsaSaldoMoeda> findMsaSalMoeByMsaAndActive(final Msa msa) {
		return dao.findByMsaAndActive(msa);
	}
}
