package com.ciandt.pms.control.jsf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.springframework.beans.factory.annotation.Autowired;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.control.jsf.bean.ApropriacaoHoraExtraBean;
import com.ciandt.pms.control.jsf.bean.ApropriacaoPlantaoBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.model.Pessoa;

/**
 * Define o Controller pai da entidade Apropriacao Plantão e Apropriação Hora
 * Extra.
 * 
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 * @since 21/01/2014
 */
public abstract class ApropriacaoController {

	/** Instancia do servico Pessoa. */
	@Autowired
	private IPessoaService pessoaService;

	/** Instancia do servico Moeda. */
	@Autowired
	private IMoedaService moedaService;

	abstract String prepareSearch();

	abstract void search();

	abstract String prepareUpdate();

	abstract void update();

	abstract String prepareCreate();

	abstract void create();

	abstract void remove();

	abstract String prepareUpload();

	/**
	 * Ação utilizada no autocomplete da Pessoa. Retorna uma lista de Pessoas.
	 * 
	 * @param value
	 *            - valor (login) utilizado na busca das Pessoas
	 * 
	 * @return retorna uma lista de recurso
	 */
	public List<Pessoa> autoCompletePessoa(final Object value) {
		return pessoaService.findPessoaByLikeLogin((String) value);
	}

	/**
	 * Realiza a validaçao do campo Login.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validatePessoa(final FacesContext context,
			final UIComponent component, final Object value) {

		String login = (String) value;

		if ((login != null) && (!"".equals(login))) {
			Pessoa pessoa = pessoaService.findPessoaByLogin(login);

			if (pessoa == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Popula a lista de Moeda para combobox.
	 */
	protected void loadMoedaList(Object bean) {

		Map<String, Long> moedaMap = new HashMap<String, Long>();
		List<String> moedaList = new ArrayList<String>();

		final List<Moeda> pMoedaList = moedaService.findMoedaAll();

		for (Moeda moeda : pMoedaList) {
			moedaMap.put(moeda.getNomeMoeda(), moeda.getCodigoMoeda());
			moedaList.add(moeda.getNomeMoeda());
		}

		if (bean instanceof ApropriacaoPlantaoBean) {
			ApropriacaoPlantaoBean apropriacaoPlantaoBean = (ApropriacaoPlantaoBean) bean;
			apropriacaoPlantaoBean.setMoedaList(moedaList);
			apropriacaoPlantaoBean.setMoedaMap(moedaMap);
		} else if (bean instanceof ApropriacaoHoraExtraBean) {
			ApropriacaoHoraExtraBean apropriacaoHoraExtraBean = (ApropriacaoHoraExtraBean) bean;
			apropriacaoHoraExtraBean.setMoedaList(moedaList);
			apropriacaoHoraExtraBean.setMoedaMap(moedaMap);
		}
	}

	/**
	 * Verifica se o valor do atributo Moeda é valido. Se o valor não é nulo e
	 * existe no moedaMap, então o valor é valido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	protected void validateMoeda(final FacesContext context,
			final UIComponent component, final Object value,
			final Map<String, Long> moedaMap) {

		Long codigoMoeda = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codigoMoeda = moedaMap.get(strValue);
			if (codigoMoeda == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Pega login do usuário logado
	 * 
	 * @return String login do usuário logado
	 */
	protected String getUsuarioLogado() {
		FacesContext context = FacesContext.getCurrentInstance();

		if (context != null) {

			// Pega o login do usuario logado no sistema
			return (String) context.getExternalContext().getSessionMap()
					.get(Constants.SPRING_SECURITY_LAST_USERNAME);
		}
		return null;
	}

	/**
	 * Popula a lista de Padrão de Arquivo
	 * 
	 * @param padraoArquivos
	 *            lista de PardaoArquivo.
	 * 
	 */
	protected void loadPadraoArquivoList(
			final List<PadraoArquivo> padraoArquivos, Object bean) {

		Map<String, Long> padraoArquivoMap = new HashMap<String, Long>();
		List<String> padraoArquivoList = new ArrayList<String>();

		for (PadraoArquivo pr : padraoArquivos) {
			padraoArquivoMap.put(pr.getNomePadraoArquivo(),
					pr.getCodigoPadraoArquivo());
			padraoArquivoList.add(pr.getNomePadraoArquivo());
		}

		if (bean instanceof ApropriacaoPlantaoBean) {
			ApropriacaoPlantaoBean apropriacaoPlantaoBean = (ApropriacaoPlantaoBean) bean;

			if (!padraoArquivos.isEmpty()) {
				apropriacaoPlantaoBean.getPadraoArquivo().setNomePadraoArquivo(
						padraoArquivos.get(0).getNomePadraoArquivo());
			}

			apropriacaoPlantaoBean.setPadraoArquivoMap(padraoArquivoMap);
			apropriacaoPlantaoBean.setPadraoArquivoList(padraoArquivoList);

		} else if (bean instanceof ApropriacaoHoraExtraBean) {
			ApropriacaoHoraExtraBean apropriacaoHoraExtraBean = (ApropriacaoHoraExtraBean) bean;

			if (!padraoArquivos.isEmpty()) {
				apropriacaoHoraExtraBean.getPadraoArquivo()
						.setNomePadraoArquivo(
								padraoArquivos.get(0).getNomePadraoArquivo());
			}

			apropriacaoHoraExtraBean.setPadraoArquivoMap(padraoArquivoMap);
			apropriacaoHoraExtraBean.setPadraoArquivoList(padraoArquivoList);
		}
	}

}