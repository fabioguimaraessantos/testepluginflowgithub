package com.ciandt.pms.business.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.richfaces.model.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.business.service.IAnexoTabelaPrecoService;
import com.ciandt.pms.business.service.ITabelaPrecoService;
import com.ciandt.pms.model.AnexoTabelaPreco;
import com.ciandt.pms.model.TabelaPreco;
import com.ciandt.pms.persistence.dao.IAnexoTabelaPrecoDao;
import com.ciandt.pms.util.LoginUtil;

/**
 * 
 * A classe AnexoTabelaPrecoService proporciona as funcionalidades de servico
 * para a enitdade AnexoTabelaPReco.
 * 
 * @since 27/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Service
public class AnexoTabelaPrecoService implements IAnexoTabelaPrecoService {

	/** Inastancia de dao. */
	@Autowired
	private IAnexoTabelaPrecoDao dao;

	/** Instancia do servico da {@link TabelaPreco}. */
	@Autowired
	private ITabelaPrecoService tabelaPrecoService;
	
	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/**
	 * Cria uma entidade no banco.
	 * 
	 * @param entity
	 *            entidade
	 * @return true
	 */
	@Transactional
	public Boolean createAnexoTabelaPreco(final AnexoTabelaPreco entity) {
		dao.create(entity);
		return true;
	}

	/**
	 * Busca pro entidades de anexoTabelaPreco por tabelaPreco.
	 * 
	 * @param tabelaPreco
	 *            tabela.
	 * @return Lista com entidades de anexo tabela preco.
	 */
	public List<AnexoTabelaPreco> findAnexoTabelaPrecoByTabelaPreco(
			final TabelaPreco tabelaPreco) {
		return dao.findAnexoTabelaPrecoByTabelaPreco(tabelaPreco);
	}

	/**
	 * Busca por id.
	 * 
	 * @param id
	 *            id
	 * @return entidade
	 */
	public AnexoTabelaPreco findAnexoTabelaPrecoById(final Long id) {
		return dao.findById(id);
	}

	/**
	 * Remove entidade do banco.
	 * 
	 * @param anexo
	 *            anexo
	 * @return true
	 */
	@Transactional
	public Boolean removeAnexoTabelaPreco(final AnexoTabelaPreco anexo) {
		dao.remove(anexo);
		return true;
	}

	/**
	 * Upload do anexoTabelaPreco.
	 * 
	 * @param uploadItem
	 *            uploadItem.
	 * @param tabelaPreco
	 *            tabelaPreco.
	 * @param coment
	 *            comentario.
	 * @throws IOException
	 *             exception.
	 * @return AnexotabelaPreco.
	 */
	@Transactional
	public AnexoTabelaPreco uploadTabelaPreco(final UploadItem uploadItem,
			final TabelaPreco tabelaPreco, final String coment)
			throws IOException {

		AnexoTabelaPreco anexoTabelaPreco = new AnexoTabelaPreco();

		String fileName = uploadItem.getFileName();
		anexoTabelaPreco.setTextoNomeArquivo(fileName);
		anexoTabelaPreco.setDataUpload(new Date());
		anexoTabelaPreco.setTabelaPreco(tabelaPreco);
		anexoTabelaPreco.setTextoAutor(LoginUtil.getLoggedUsername());
		anexoTabelaPreco.setTextoComentario(coment);

		this.createAnexoTabelaPreco(anexoTabelaPreco);

		anexoTabelaPreco = this.findAnexoTabelaPrecoById(anexoTabelaPreco
				.getCodigoAnexoTabPreco());

		return anexoTabelaPreco;

	}

	/**
	 * Save anexoTabelaPrevo.
	 * 
	 * @param anexoTabelaPreco
	 *            anexoTabelaPreco
	 * @param fileBytes
	 *            bytes.
	 * @throws IOException
	 *             excecao.
	 * 
	 */
	@Transactional
	public void saveUploadAnexo(final AnexoTabelaPreco anexoTabelaPreco,
			final byte[] fileBytes) throws IOException {

		String fileName = anexoTabelaPreco.getTabelaPreco()
				.getCodigoTabelaPreco()
				+ "-"
				+ anexoTabelaPreco.getTextoNomeArquivo();
		TabelaPreco tp = tabelaPrecoService
				.findTabelaPrecoById(anexoTabelaPreco.getTabelaPreco()
						.getCodigoTabelaPreco());

		anexoTabelaPreco.setTabelaPreco(tp);

		anexoTabelaPreco.setTextoNomeArquivo(fileName);

		
		String path = (String) systemProperties
				.get("upload.anexo_tabela_preco.path");


		FileOutputStream fos = new FileOutputStream(path + fileName);

		fos.write(fileBytes);
		fos.close();
	}

}
