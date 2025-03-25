package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.DreMes;
import com.ciandt.pms.model.DreProcesso;

/**
 * 
 * A classe IDreProcessoService proporciona a interface de acesso para a camada
 * de servico referente a entidade DreProcesso.
 * 
 * @since 10/10/2013
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 */
@Service
public interface IDreProcessoService {
	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	void createDreProcesso(final DreProcesso entity);

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será atualizada.
	 */
	void updateDreProcesso(final DreProcesso entity);

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que será apagada.
	 * 
	 */
	void removeDreProcesso(final DreProcesso entity);

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	DreProcesso findDreProcessoById(final Long id);

	/**
	 * Retorna o último processo executado.
	 * 
	 * @param codigoProcesso
	 *            Código do processo
	 * @param monthYear
	 *            MM/yyyy
	 * @param indicadorPorLogin
	 *            - <li>S - Processo executado para um mes/ano, especificando
	 *            login</li><li>N - Processo executado para um mes/ano, sem
	 *            especificar um login</li>
	 * @return {@link DreProcesso}
	 */
	DreProcesso findLastByProcessoDataAndIndPorLogin(final Long codigoProcesso,
			final Date monthYear, final String indicadorPorLogin);

	/**
	 * Retorna todos DreProcesso com um Dremes
	 * 
	 * @param dreMes
	 *            dreMes
	 * @return Lista de {@link DreProcesso}
	 */
	List<DreProcesso> findAllDreProcessByDreMes(final DreMes dreMes);

}
