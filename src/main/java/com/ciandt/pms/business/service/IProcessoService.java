package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.DreMes;
import com.ciandt.pms.model.Processo;
import com.ciandt.pms.model.vo.ProcessoRow;

/**
 * 
 * A classe IProcessoService proporciona a interface de acesso para a camada de
 * servico referente a entidade Processo.
 * 
 * @since 10/10/2013
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 */
@Service
public interface IProcessoService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	void createProcesso(final Processo entity);

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será atualizada.
	 */
	void updateProcesso(final Processo entity);

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que será apagada.
	 * 
	 */
	void removeProcesso(final Processo entity);

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	Processo findProcessoById(final Long id);

	/**
	 * Busca entidades pelo indicador:
	 * 
	 * @param indicador
	 *            <li>A - Ativo</li> <li>I - Inativo</li>
	 * 
	 * @return {@link Processo}
	 */
	List<Processo> findAllByIndicadorAtivo(final String indicador);

	/**
	 * Busca os processos ativos e seus status por MM/yyyy
	 * 
	 * @param monthYear
	 *            Date
	 */
	List<ProcessoRow> findProcessoByDate(final Date monthYear);

	/**
	 * Invalida todos os processos dependentes de {@code processo}
	 * recursivamente.
	 * 
	 * @param dreMes
	 *            {@link DreMes}
	 * @param processo
	 *            {@link Processo}
	 */
	void invalidateDependingProcessoCascade(final DreMes dreMes,
			final Processo processo);
}
