package com.ciandt.pms.control.jsf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICargoPmsCargoService;
import com.ciandt.pms.business.service.ICargoService;
import com.ciandt.pms.business.service.IPerfilPadraoService;
import com.ciandt.pms.business.service.IVwHrsCargoService;
import com.ciandt.pms.control.jsf.bean.CargoBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.CargoPms;
import com.ciandt.pms.model.CargoPmsCargo;
import com.ciandt.pms.model.CargoPmsCargoId;
import com.ciandt.pms.model.VwHrsCargo;
import com.ciandt.pms.model.vo.CargoPmsRow;

/**
 * 
 * A classe CargoController proporciona as funcionalidades de controler para a
 * entidade de cargo.
 * 
 * @since 04/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Controller
@RolesAllowed({ "BUS.DEFAULT_PRICE_TABLE:VW" })
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class CargoController {

	/*********** BEANS **************************/

	/** Bean do Cargo. */
	@Autowired
	private CargoBean bean;

	/*********** SERVICES **************************/

	/** Instancia do servico VwHrsCargo. */
	@Autowired
	private IVwHrsCargoService vwHrsCargoService;

	/** Instancia do servico Cargo. */
	@Autowired
	private ICargoService cargoService;

	/** Instancia do servico CargoPmsCargo. */
	@Autowired
	private ICargoPmsCargoService cargoPmsCargoService;

	/** Inastancia do servico. */
	@Autowired
	private IPerfilPadraoService perfilPadraoService;

	private List<CargoPms> allCargos;
	private List<VwHrsCargo> allVwHrsCargo;
	private Map<Long, List<VwHrsCargo>> mapVwHrsCargo;

	/**
	 * @return the bean
	 */
	public CargoBean getBean() {
		return bean;
	}

	/**
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(final CargoBean bean) {
		this.bean = bean;
	}

	/**
	 * Prepare para a tela de add Cargo.
	 * 
	 */
	public void prepareAddCargo() {
		bean.setFlagUpdate(Boolean.valueOf(false));
		loadCargos();
		bean.reset();
	}

	private void loadCargos(){
		allCargos = cargoService.findAllCargoPms();
		allVwHrsCargo = new ArrayList<>();
		mapVwHrsCargo = new HashMap<>();

		for (CargoPms cargoPms : allCargos) {
			List<VwHrsCargo> listaVw = listVwHrsCargoByCargoPms(cargoPms);
			allVwHrsCargo.addAll(listaVw);
			mapVwHrsCargo.put(cargoPms.getCodigoCargoPms(), listaVw);
		}

		bean.setListaCargoPmsRow(loadListaCargoPmsRow(allCargos));
		bean.setListPickList(loadItensPickList(vwHrsCargoService.findAllActive()));
	}

	/**
	 * Cria uma instancia de cargo.
	 */
	public void createCargo() {
		CargoPms cargoPms = bean.getTo();

		if (!cargoService.createCargo(cargoPms)) {
			Messages.showError("cretaCargto",
					Constants.DEFAULT_MSG_ERROR_INVALID_DATE);
		} else {
			Messages.showSucess("cretaCargto",
					Constants.GENEREC_MSG_SUCCESS_ADD);
		}

		List<CargoPmsCargo> listCargoPmsCargo = new ArrayList<CargoPmsCargo>();

		String[] grantedCargos = bean.getGrantedCargos();

		for (String grantedCargo : grantedCargos) {
			VwHrsCargo cargo = vwHrsCargoService.findVwHrsCargoByCodigo(Long
					.valueOf(grantedCargo));

			CargoPmsCargo cargoPmsCargo = new CargoPmsCargo(
					new CargoPmsCargoId(cargoPms.getCodigoCargoPms(), cargo
							.getId().getCodigoCargo()));
			cargoPmsCargo.setCargoPms(cargoPms);

			listCargoPmsCargo.add(cargoPmsCargo);
		}

		for (CargoPmsCargo cpc : listCargoPmsCargo) {
			cargoPmsCargoService.createCargoPmsCargo(cpc);
		}

		bean.reset();

		loadCargos();

	}

	/**
	 * Remove cargoPms.
	 */
	public void removeCargo() {

		CargoPmsRow cargoPmsRow = bean.getCargoPmsRow();

		CargoPms cargo = cargoService.findCargoPmsById(cargoPmsRow
				.getCargoPms().getCodigoCargoPms());

		if (!perfilPadraoService.findByCargoPms(cargo).isEmpty()) {
			Messages.showError("remove", Constants.CAN_NOT_REMOVED_POSITION);
		} else {
			cargoService.removeCargo(cargo);
			Messages.showSucess("removeCargo",
					Constants.GENEREC_MSG_SUCCESS_REMOVE);
		}

		loadCargos();

		bean.reset();

	}

	/**
	 * Carrega lista de Vo para cargoPms.
	 * 
	 * @param listaCargoPms
	 *            lista
	 * @return lista.
	 */
	private List<CargoPmsRow> loadListaCargoPmsRow(final List<CargoPms> listaCargoPms) {
		List<CargoPmsRow> listResult = new ArrayList<CargoPmsRow>();
		for (CargoPms cargoPms : listaCargoPms) {
			listResult.add(new CargoPmsRow(cargoPms, mapVwHrsCargo.get(cargoPms.getCodigoCargoPms())));
		}
		return listResult;
	}

	/**
	 * Mostra ou esconde a lista de VwHrs Cargo (maizinho).
	 */
	public void showHideVwHrsCargo() {
		CargoPmsRow cargoPmsRow = bean.getCargoPmsRow();
		cargoPmsRow.setShowVwHrsCargo(!cargoPmsRow.getShowVwHrsCargo());
	}

	/**
	 * Prepare edit cargoPms.
	 */
	public void prepareEdit() {
		bean.setFlagUpdate(Boolean.TRUE);
		// Recupera bean.
		CargoPmsRow cargoPmsRow = bean.getCargoPmsRow();

		loadCargos();

		// Lista grantedCargos
		List<VwHrsCargo> listaVwHrsCargo = mapVwHrsCargo.get(cargoPmsRow.getCargoPms().getCodigoCargoPms());
		String[] grantedCargos = new String[listaVwHrsCargo.size()];
		for (int i = 0; i < listaVwHrsCargo.size(); i++) {
			VwHrsCargo cargo = listaVwHrsCargo.get(i);
			grantedCargos[i] = String.valueOf(cargo.getId().getCodigoCargo());
			bean.getListPickList().add(new SelectItem(""
					+ cargo.getId().getCodigoCargo(), cargo.getId()
					.getNomeCargo()));
		}
		bean.setGrantedCargos(grantedCargos);

		// Seta valores
		bean.setTo(bean.getCargoPmsRow().getCargoPms());

	}

	/**
	 * Atualiza CargoPms.
	 */
	public void updateCargoPms() {
		// Recupera bean.
		CargoPmsRow cargoPmsRow = bean.getCargoPmsRow();
		// Busca cargoPms.
		CargoPms cargoPms = cargoService.findCargoPmsById(cargoPmsRow
				.getCargoPms().getCodigoCargoPms());

		// Lista de CargoPmsCargo do cargoPms.
		List<CargoPmsCargo> listaCargoPmsCargo = cargoPmsCargoService
				.findCargoPmsCargoByCargoPms(cargoPms);

		// Remove os CargoPmsCargo atuais para posterior inserção.
		for (CargoPmsCargo cargoPmsCargo : listaCargoPmsCargo) {
			cargoPmsCargoService.removeCargoPmsCargo(cargoPmsCargo);
		}

		List<CargoPmsCargo> listCargoPmsCargo = new ArrayList<CargoPmsCargo>();

		// Lista de novos cargos.
		String[] grantedCargos = bean.getGrantedCargos();

		for (String grantedCargo : grantedCargos) {
			VwHrsCargo cargo = vwHrsCargoService.findVwHrsCargoByCodigo(Long
					.valueOf(grantedCargo));

			CargoPmsCargo cargoPmsCargo = new CargoPmsCargo(
					new CargoPmsCargoId(cargoPms.getCodigoCargoPms(), cargo
							.getId().getCodigoCargo()));
			cargoPmsCargo.setCargoPms(cargoPms);

			listCargoPmsCargo.add(cargoPmsCargo);
		}

		// Cria os novos cargos de grantedCargos.
		for (CargoPmsCargo cargoPmsCargo : listCargoPmsCargo) {
			cargoPmsCargoService.createCargoPmsCargo(cargoPmsCargo);
		}

		// Seta o nome do cargoPms.
		cargoPms.setNomeCargoPms(bean.getTo().getNomeCargoPms());

		// Atuliza cargoPms.
		if (Boolean.TRUE.equals(cargoService.updateCargo(cargoPms))) {
			Messages.showSucess("updateCargoPms",
					Constants.GENEREC_MSG_SUCCESS_UPDATE);
		}

		loadCargos();

		bean.reset();
		bean.setFlagUpdate(Boolean.FALSE);

	}

	/**
	 * Lista de VwHrsCargo por cargoPms.
	 * 
	 * @param cargoPms
	 *            cargoPms.
	 * @return lista
	 */
	private List<VwHrsCargo> listVwHrsCargoByCargoPms(final CargoPms cargoPms) {
		List<VwHrsCargo> listResult = new ArrayList<VwHrsCargo>();

		for (CargoPmsCargo cargoPmsCargo : cargoPmsCargoService.findCargoPmsCargoByCargoPms(cargoPms)) {
			VwHrsCargo vwHrsCargo = vwHrsCargoService.findVwHrsCargoByCodigo(cargoPmsCargo.getId().getCodigoCargo());
			if (vwHrsCargo != null) {
				listResult.add(vwHrsCargo);
			}
		}

		return listResult;
	}

	/**
	 * Carrega lado esquerdo da pickList.
	 * 
	 * @param listaVwHrsCargo
	 *            lista
	 * @return lista de Cargos.
	 */
	private List<SelectItem> loadItensPickList(
			final List<VwHrsCargo> listaVwHrsCargo) {
		List<SelectItem> listResult = new ArrayList<SelectItem>();

		// Preenche pickList
		for (VwHrsCargo cargoVw : listaVwHrsCargo) {
			// Verifica se cargo ja esta agrupado ou nao
			if (!allVwHrsCargo.contains(cargoVw)) {
				listResult.add(new SelectItem(""
						+ cargoVw.getId().getCodigoCargo(), cargoVw.getId()
						.getNomeCargo()));
			}
		}

		return listResult;

	}

}
