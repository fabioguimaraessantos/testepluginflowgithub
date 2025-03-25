package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.FaturaApagada;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.vo.FaturaRow;

/**
 * 
 * A classe IFaturaApagadaService proporciona a interface de acesso para a camada de
 * serviço referente a entidade FaturaApagada.
 * 
 * @since 30/10/2014
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * 
 */
@Service
public interface IFaturaApagadaService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 * 
	 * @return retorna true se criado com sucesso, caso contrario false
	 */
	@Transactional
	void create(final FaturaApagada entity);

	/**
	 * Atualiza a entidade.
	 * 
	 * @param entity
	 *            entidade a ser atualizada.
	 * 
	 * @return retorna true se update realizado com sucesso, caso contrario
	 *         retorna false.
	 */
	@Transactional
	void update(final FaturaApagada entity);

	/**
	 * Busca pelo id da entidade.
	 * 
	 * @param entityId
	 *            id da entidade
	 * 
	 * @return retorna a entidade
	 */
	FaturaApagada findById(final Long entityId);

	/**
	 * Busca uma lista de entidades pelo filtro. Porem nao carrega previamente
	 * as entidades relacionadas a esta.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 * @param dataPrevisaoBeg
	 *            dataPrevisao inicial.
	 * @param dataPrevisaoEnd
	 *            dataPrevisao final.
	 * @param cli
	 *            entidade cliente.
	 * @param cp
	 *            entidade msa.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<FaturaRow> findFaturaByFilter(final Fatura filter, final Cliente cli,
			final Msa msa, final Date dataPrevisaoBeg,
			final Date dataPrevisaoEnd);

}