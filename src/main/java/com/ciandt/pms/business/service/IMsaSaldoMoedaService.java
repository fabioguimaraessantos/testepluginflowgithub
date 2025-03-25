package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.MsaSaldoMoeda;

/**
 * 
 * A classe IMsaSaldoMoedaService proporciona a interface de acesso a camada de
 * serviço referente a entidade MsaSaldoMoeda.
 * 
 * @since 28/09/2012
 * @author <a href="mailto:diegos@ciandt.com">Diego Henrique Mila da Silva</a>
 * 
 */
@Service
public interface IMsaSaldoMoedaService {
    
    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será removida.
     */
    @Transactional
    void removeMsaSalMoe(final MsaSaldoMoeda entity);
    
	/**
	 * Atualiza uma entidade.
	 * 
	 * @param entity
	 *            que será atulizada.
	 */
	@Transactional
    void updateMsaSalMoe(final MsaSaldoMoeda entity);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista de MsaSaldoMoeda.
     */
    List<MsaSaldoMoeda> findMsaSalMoeAll();

    /**
     * Busca todas as entidades pelo Msa.
     * 
     * @return retorna uma lista de MsaSaldoMoeda do Msa passado por parâmetro.
     */
    List<MsaSaldoMoeda> findMsaSalMoeByMsa(final Msa msa);

    /**
     * Busca a entidade pelo id.
     * 
     * @param id
     *            - id da entidade
     * 
     * @return retorna uma instancia do tipo MsaSaldoMoeda
     */
    MsaSaldoMoeda findMsaSalMoeById(final Long id);

    /**
	 * Busca todas as entidades pro msa e ativas.
	 * 
	 * @param msa
	 *            the msa
	 * @return lista de entidades.
	 */
	List<MsaSaldoMoeda> findMsaSalMoeByMsaAndActive(final Msa msa);
}
