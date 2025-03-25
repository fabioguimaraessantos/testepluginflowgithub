package com.ciandt.pms.business.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.richfaces.model.UploadItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.ApropriacaoPlantao;
import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.UploadArquivo;

/**
 * A interface IApropriacaoPlantaoService proporciona a interface de acesso a
 * camada de serviço referente a entidade ApropriacaoPlantao.
 * 
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * @since 22/10/2013
 */
@Service
public interface IApropriacaoPlantaoService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	@Transactional
	boolean createApropriacaoPlantao(final ApropriacaoPlantao entity);

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que sera atualizada.
	 * 
	 */
	@Transactional
	void updateApropriacaoPlantao(final ApropriacaoPlantao entity);

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que sera apagada.
	 */
	@Transactional
	void removeApropriacaoPlantao(final ApropriacaoPlantao entity);

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	ApropriacaoPlantao findApropriacaoPlantaoById(final Long id);

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<ApropriacaoPlantao> findApropriacaoPlantaoAllActive();

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
	UploadArquivo uploadApropriacaoPlantao(final UploadItem uploadItem,
			final PadraoArquivo padraoArq) throws IOException;

	/**
	 * Checa se existe a entidade existe na base de dados.
	 * 
	 * @param apropriacaoPlantao
	 * @return true caso a entidade já exista na base de dados
	 */
	boolean existsApropriacaoPlantao(final ApropriacaoPlantao apropriacaoPlantao);

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
     *            01/mm/yyyy do fechamento.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    List<ApropriacaoPlantao> findApropriacaoPlantaoByData(final Date dataFechamento);
    
	/**
	 * Busca uma lista de entidades pela dataFechamento e codigo da pessoa.
	 * 
	 * @param dataFechamento
     *            01/mm/yyyy do fechamento.
	 * @param pessoa
	 *            {@link Pessoa}
	 * 
	 * @return lista de entidades que atendem ao criterio do filtro.
	 */
	List<ApropriacaoPlantao> findApropriacaoPlantaoByDataAndCdPessoa(
			final Date dataFechamento, final Pessoa pessoa);
}
