package com.ciandt.pms.business.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.DreMes;

/**
 * 
 * A classe IDreMesService proporciona a interface de acesso para a camada de
 * servico referente a entidade DreMes.
 * 
 * @since 10/10/2013
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 */
@Service
public interface IDreMesService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	void createDreMes(final DreMes entity);

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será atualizada.
	 */
	void updateDreMes(final DreMes entity);

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que será apagada.
	 * 
	 */
	void removeDreMes(final DreMes entity);

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	DreMes findDreMesById(final Long id);

	/**
	 * Realiza a busca de um {@link DreMes} por uma data base.
	 * 
	 * @param dataMes
	 *            data base para busca da entidade.
	 * @return {@link DreMes}
	 */
	DreMes findDreMesByDataMes(final Date dataMes);

	/**
	 * Verifica se o DreMes foi processado.
	 * 
	 * @param dreMes
	 *            DreMes.
	 * @return {@link boolean}
	 */
	boolean isDreMesCompleted(final DreMes dreMes);

}
