package com.ciandt.pms.business.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.time.DateUtils;
import org.richfaces.model.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IApropriacaoHoraExtraService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IUploadArquivoService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.ApropriacaoHoraExtra;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.UploadArquivo;
import com.ciandt.pms.persistence.dao.IApropriacaoHoraExtraDao;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;
import com.ciandt.pms.util.UploadUtil;

/**
 * A classe ApropriacaoHoraExtraService proporciona as funcionalidades da camada
 * de negócio referente a entidade {@link ApropriacaoHoraExtra}.
 * 
 * @since 22/10/2013
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 */
@Service
public class ApropriacaoHoraExtraService implements
		IApropriacaoHoraExtraService {

	/** Instancia do Dao da entidade ApropriacaoHoraExtra. */
	@Autowired
	private IApropriacaoHoraExtraDao apropriacaoHoraExtraDao;

	/** Instancia do servico PessoaService. */
	@Autowired
	private IPessoaService pessoaService;

	/** Instancia do servico Modulo. */
	@Autowired
	private IModuloService moduloService;

	/** Instancia do servico Moeda. */
	@Autowired
	private IMoedaService moedaService;

	/** Instancia do sevico {@link UploadArquivo}. */
	@Autowired
	private IUploadArquivoService uploadArquivoService;

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	private static final String SEPARADOR = "; ";

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	public boolean createApropriacaoHoraExtra(final ApropriacaoHoraExtra entity) {

		boolean sucess = true;

		entity.setDataAtualizacao(new Date());

		entity.setDataMes(DateUtils.truncate(entity.getDataMes(),
				Calendar.MONTH));

		Date closingDate = moduloService.getClosingDateBancoHoras();
		if (DateUtil.before(entity.getDataMes(), closingDate)
				|| DateUtil.isSameDate(entity.getDataMes(), closingDate,
						Calendar.MONTH)) {
			// Se a data da hora extra for anterior a data do closing date do
			// Módulo de horas extras e plantão, mostra mensagem de erro
			Messages.showError("createOvertime",
					Constants.UPLOAD_MSG_ERROR_BEFORE_CLOSING_DATE);
			sucess = false;
		}

		// Consulta hora extra por pessoa e MM/yyyy
		List<ApropriacaoHoraExtra> horasExtras = apropriacaoHoraExtraDao
				.findApropriacaoHoraExtraByDataAndCdPessoa(entity.getDataMes(),
						entity.getPessoa());
		if (horasExtras.size() > 0) {
			// Não pode inserir valor com mesmo login e MM/yyyy. Se existir
			// registro para pessoa e MM/yyyy, mostra mensagem de erro.
			Messages.showError("createOvertime",
					Constants.UPLOAD_PLANTAO_HE_MSG_ERRO_REGISTRO_JA_EXISTENTE);
			sucess = false;
		}

		if (sucess) {

			apropriacaoHoraExtraDao.create(entity);

			Messages.showSucess("createOvertime",
					Constants.DEFAULT_MSG_SUCCESS_SAVE,
					Constants.ENTITY_NAME_APROPRIACAO_HORA_EXTRA);
		}
		return sucess;
	}

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que sera atualizada.
	 * 
	 */
	public void updateApropriacaoHoraExtra(final ApropriacaoHoraExtra entity) {
		apropriacaoHoraExtraDao.update(entity);
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que sera apagada.
	 */
	public void removeApropriacaoHoraExtra(final ApropriacaoHoraExtra entity) {
		apropriacaoHoraExtraDao.remove(entity);
	}

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	public ApropriacaoHoraExtra findApropriacaoHoraExtraById(final Long id) {
		return apropriacaoHoraExtraDao.findById(id);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	public List<ApropriacaoHoraExtra> findApropriacaoHoraExtraAllActive() {
		return apropriacaoHoraExtraDao.findAll();
	}

	/**
	 * Checa se o objeto {@link ApropriacaoHoraExtra} contem erro (objeto obtido
	 * via upload).
	 * 
	 * @param apropriacaoHoraExtra
	 *            {@link ApropriacaoHoraExtra}
	 * @param mapLogins
	 *            Map com logins
	 * @return String erro
	 */
	private String validateApropriacaoHoraExtraUploadedItem(
			ApropriacaoHoraExtra apropriacaoHoraExtra,
			final Map<String, Date> mapLogins) {

		String login = apropriacaoHoraExtra.getLogin();
		StringBuilder srtErro = new StringBuilder("");

		if (apropriacaoHoraExtra.getDataMes() != null) {

			// Valida se o existe login com o mesmo MM/yyyy repetido no arquivo
			if (mapLogins.get(apropriacaoHoraExtra.getLogin()) != null
					&& mapLogins.get(apropriacaoHoraExtra.getLogin()).equals(
							DateUtils.truncate(
									apropriacaoHoraExtra.getDataMes(),
									Calendar.MONTH))) {
				srtErro.append(BundleUtil
						.getBundle(Constants.UPLOAD_PLANTAO_HE_MSG_ERRO_REGISTRO_DUPLICADO_NO_ARQUIVO));
				srtErro.append(SEPARADOR);
			}

			// Valida login
			if (login != null) {
				Pessoa pessoa = pessoaService.findPessoaByLogin(login);
				if (pessoa != null) {
					// Verifica se não existe registro para o login e data
					apropriacaoHoraExtra.setPessoa(pessoa);
					if (this.existsApropriacaoHoraExtra(apropriacaoHoraExtra)) {
						srtErro.append(BundleUtil
								.getBundle(Constants.UPLOAD_PLANTAO_HE_MSG_ERRO_REGISTRO_JA_EXISTENTE));
						srtErro.append(SEPARADOR);
					}
				} else {
					srtErro.append(BundleUtil
							.getBundle(Constants.UPLOAD_ERROR_INVALID_LOGIN));
					srtErro.append(SEPARADOR);
				}
			} else {
				srtErro.append(BundleUtil
						.getBundle(Constants.UPLOAD_MSG_ERROR_READING));
				srtErro.append(SEPARADOR);
			}

			// Valida se a data com a data de fechamento do banco de
			// horas/plantão
			if (!DateUtil.after(apropriacaoHoraExtra.getDataMes(),
					moduloService.getClosingDateBancoHoras())) {
				srtErro.append(BundleUtil
						.getBundle(Constants.UPLOAD_MSG_ERROR_BEFORE_CLOSING_DATE));
				srtErro.append(SEPARADOR);
			}
		} else {
			// Valida formato da data
			srtErro.append(BundleUtil
					.getBundle(Constants.UPLOAD_PLANTAO_HE_MSG_ERRO_FORMATO_DATA));
			srtErro.append(SEPARADOR);
		}

		// Valida a sigla da moeda

		// Valida a sigla da moeda
		Moeda moeda = moedaService.findMoedaByAcronym(apropriacaoHoraExtra
				.getSiglaMoeda());
		apropriacaoHoraExtra.setMoeda(moeda);

		if (apropriacaoHoraExtra.getSiglaMoeda() == null || moeda == null) {
			srtErro.append(BundleUtil
					.getBundle(
							Constants.UPLOAD_PLANTAO_HE_MSG_ERRO_SIGLA_MOEDA,
							apropriacaoHoraExtra.getSiglaMoeda() != null ? apropriacaoHoraExtra
									.getSiglaMoeda() : "null"));
			srtErro.append(SEPARADOR);
		}

		// Valida valor
		if (apropriacaoHoraExtra.getValorHoraExtra() == null) {
			srtErro.append(BundleUtil
					.getBundle(Constants.UPLOAD_PLANTAO_HE_MSG_ERRO_FORMATO_VALOR));
			srtErro.append(SEPARADOR);
		}

		if (srtErro.length() > 0) {
			// Retira o ", " do ultimo registro
			return srtErro.toString().substring(0, srtErro.length() - 2);
		}

		return srtErro.toString();
	}

	/**
	 * Configura um objeto do tipo {@link UploadArquivo}.
	 * 
	 * @param uploadItem
	 *            {@link UploadItem}
	 * @param padraoArq
	 *            {@link PadraoArquivo}
	 * @param apropriacaoHorasExtras
	 *            lista contendo os itens (ApropriacaoHoraExtra)
	 * 
	 * @return {@link UploadArquivo}
	 */
	private UploadArquivo buildUploadArquivo(final UploadItem uploadItem,
			final PadraoArquivo padraoArq,
			final List<ApropriacaoHoraExtra> apropriacaoHorasExtras) {

		UploadArquivo uploadArquivo = new UploadArquivo();
		List<ApropriacaoHoraExtra> validApropriacaoHorasExtras = new ArrayList<ApropriacaoHoraExtra>();
		Map<String, Date> mapLogins = new HashMap<String, Date>();

		int count = 1;

		for (ApropriacaoHoraExtra apropriacaoHoraExtra : apropriacaoHorasExtras) {
			String error = this.validateApropriacaoHoraExtraUploadedItem(
					apropriacaoHoraExtra, mapLogins);

			if (error.isEmpty()) {
				Date dataMes = DateUtils.truncate(
						apropriacaoHoraExtra.getDataMes(), Calendar.MONTH);

				mapLogins.put(apropriacaoHoraExtra.getLogin(), dataMes);

				// trunca a data pelo mês, pois o dia não deve ser considerado
				apropriacaoHoraExtra.setDataMes(dataMes);
				apropriacaoHoraExtra.setUploadArquivo(uploadArquivo);
				// relaciona com o arquivo de upload.
				validApropriacaoHorasExtras.add(apropriacaoHoraExtra);
			} else {
				uploadArquivo.addErrors("Line " + count + ": " + error);
			}

			count++;
		}

		String fileName = uploadItem.getFileName();
		uploadArquivo.setNomeArquivo(fileName);
		uploadArquivo.setCodigoAutor(LoginUtil.getLoggedUsername());
		uploadArquivo.setDataDataHoraUpload(new Date());
		uploadArquivo.setValorBytes(String.valueOf(uploadItem.getFileSize()));
		uploadArquivo.setDataDataHoraUpload(new Date());
		uploadArquivo.setSiglaTabelaAlvo("APHE");
		uploadArquivo.setTextoExtArquivo(fileName.substring(
				fileName.lastIndexOf('.') + 1, fileName.length()));
		uploadArquivo.setPadraoArquivo(padraoArq);
		uploadArquivo.setApropriacaoHoraExtras(validApropriacaoHorasExtras);

		return uploadArquivo;
	}

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
	@Override
	public UploadArquivo uploadApropriacaoHoraExtra(
			final UploadItem uploadItem, final PadraoArquivo padraoArq) {

		List<ApropriacaoHoraExtra> apropriacaoHorasExtras = UploadUtil
				.csvFileToElementList(uploadItem, padraoArq,
						ApropriacaoHoraExtra.class);

		return this.buildUploadArquivo(uploadItem, padraoArq,
				apropriacaoHorasExtras);
	}

	/**
	 * Checa se a entidade existe na base de dados.
	 * 
	 * @param apropriachaoHoraExtra
	 * @return true caso a entidade já exista na base de dados
	 */
	@Override
	public boolean existsApropriacaoHoraExtra(
			ApropriacaoHoraExtra apropriachaoHoraExtra) {
		return apropriacaoHoraExtraDao.exists(apropriachaoHoraExtra);
	}

	/**
	 * Salva o arquivo que foi "uppado".
	 * 
	 * @param uploadArquivo
	 *            arquivo a ser salvo
	 * @param data
	 *            bytes
	 * @throws IOException
	 */
	@Override
	@Transactional
	public void saveUploadFile(UploadArquivo uploadArquivo, byte[] data)
			throws IOException {

		uploadArquivoService.createUploadArquivo(uploadArquivo);
		String fileName = uploadArquivo.getCodigoUploadArquivo() + "-"
				+ uploadArquivo.getNomeArquivo();

		String path = (String) systemProperties
				.get(Constants.UPLOAD_HORA_EXTRA_POR_VALOR_DESTINATION_PAHT);

		uploadArquivo.setTextoCaminho(path);
		uploadArquivo.setNomeArquivo(fileName);

		uploadArquivoService.updateUploadArquivo(uploadArquivo);
		
		FileOutputStream fos = new FileOutputStream(path + fileName);
		fos.write(data);
		fos.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ciandt.pms.business.service.IApropriacaoHoraExtraService#
	 * findApropriacaoHoraExtraByData(java.util.Date)
	 */
	public List<ApropriacaoHoraExtra> findApropriacaoHoraExtraByData(
			final Date dataFechamento) {
		return apropriacaoHoraExtraDao
				.findApropriacaoHoraExtraByData(dataFechamento);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ciandt.pms.business.service.IApropriacaoHoraExtraService#
	 * findApropriacaoHoraExtraByDataAndCdPessoa(java.util.Date,
	 * com.ciandt.pms.model.Pessoa)
	 */
	public List<ApropriacaoHoraExtra> findApropriacaoHoraExtraByDataAndCdPessoa(
			final Date dataFechamento, final Pessoa pessoa) {
		return apropriacaoHoraExtraDao
				.findApropriacaoHoraExtraByDataAndCdPessoa(dataFechamento,
						pessoa);
	}

}