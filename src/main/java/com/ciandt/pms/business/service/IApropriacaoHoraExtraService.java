package com.ciandt.pms.business.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.richfaces.model.UploadItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.ApropriacaoHoraExtra;
import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.UploadArquivo;

/**
 * 
 * A interface IApropriacaoHoraExtraService proporciona a interface de acesso a
 * camada de serviço referente a entidade ApropriacaoHoraExtra.
 * 
 * @since 22/10/2013
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
@Service
public interface IApropriacaoHoraExtraService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	@Transactional
	boolean createApropriacaoHoraExtra(final ApropriacaoHoraExtra entity);

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que sera atualizada.
	 * 
	 */
	@Transactional
	void updateApropriacaoHoraExtra(final ApropriacaoHoraExtra entity);

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que sera apagada.
	 */
	@Transactional
	void removeApropriacaoHoraExtra(final ApropriacaoHoraExtra entity);

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	ApropriacaoHoraExtra findApropriacaoHoraExtraById(final Long id);

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<ApropriacaoHoraExtra> findApropriacaoHoraExtraAllActive();

	/**
	 * Salva o upload do arquivo de plantao e salva.
	 * 
	 * @param uploadItem
	 *            - arquivo que foi "upado"
	 * 
	 * @param padraoArq
	 *            - padrão do arquivo.
	 * 
	 * @throws IOException
	 *             lnaça exceção caso não seja possível salvar o arquivo de
	 *             upload
	 * 
	 * @return {@link UploadArquivo}
	 */
	UploadArquivo uploadApropriacaoHoraExtra(final UploadItem uploadItem,
			final PadraoArquivo padraoArq) throws IOException;

	/**
	 * Checa se existe a entidade existe na base de dados.
	 * 
	 * @param apropriacaoHoraExtra
	 * @return true caso a entidade já exista na base de dados
	 */
	boolean existsApropriacaoHoraExtra(final ApropriacaoHoraExtra apropriacaoHoraExtra);

	/**
	 * Salva o arquivo que foi "uppado".
	 * 
	 * @param uploadArquivo
	 *            arquivo a ser salvo
	 * @param data
	 *            bytes
	 */
	void saveUploadFile(UploadArquivo uploadArquivo, byte[] data)
			throws IOException;
	
	/**
	 * Busca uma lista de entidades pela dataFechamento.
	 * 
	 * @param dataFechamento
	 *            01/mes/yyyy do fechamento.
	 * 
	 * @return lista de entidades que atendem ao criterio do filtro.
	 */
	List<ApropriacaoHoraExtra> findApropriacaoHoraExtraByData(
			final Date dataFechamento);

	/**
	 * Busca uma lista de entidades pela dataFechamento e codigo da pessoa.
	 * 
	 * @param dataFechamento
	 *            01/mes/yyyy do fechamento.
	 * @param pessoa
	 *            {@link Pessoa}
	 * 
	 * @return lista de entidades que atendem ao criterio do filtro.
	 */
	List<ApropriacaoHoraExtra> findApropriacaoHoraExtraByDataAndCdPessoa(
			final Date dataFechamento, final Pessoa pessoa);
}
