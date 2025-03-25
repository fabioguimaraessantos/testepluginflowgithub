package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;
import java.util.Observer;

import com.ciandt.pms.exception.dre.DreProcessoExecutavelException;
import com.ciandt.pms.model.DreProcesso;
import com.ciandt.pms.model.Pessoa;

/**
 * Interface que define a estrutura de execucao de um processo de closing DRE.
 * 
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * @since 16/10/2013
 */
public interface IAbstractDreProcessoExecutavelService {

	/**
	 * Realiza a execucao do processo.
	 * 
	 * @param dateRef
	 *            data base de execucao do processamento (pode ser retroativo)
	 * @param dreProcesso
	 *            o processo a ser executado
	 */
	void execute(final Date dateRef, final DreProcesso dreProcesso)
			throws DreProcessoExecutavelException;

	/**
	 * Sobrecarga do metodo execute para realizar a execucao do processo de
	 * Resource Validation.
	 * 
	 * @param dateRef
	 *            data base de execucao do processamento (pode ser retroativo)
	 * @param dreProcesso
	 *            o processo a ser executado
	 */
	public void execute(final Date dateRef, final DreProcesso dreProcesso,
			final List<Pessoa> pessoas) throws DreProcessoExecutavelException;

	/**
	 * Notifica o percentual do processo concluido atualizado aos Observers
	 * 
	 * @param percentualConcluido
	 *            percentual do processo já executado
	 */
	void notifyObservers(final Float percentualConcluido);

	/**
	 * Registra o objeto observer (que fica "escutando" qualquer atualização no
	 * percentual de conclusão do processo)
	 * 
	 * @param obs
	 *            Objeto que ficara observando
	 */
	void registerObserver(Observer obs);
	
	
	
	
	
	/**
	 * FIXME: *** ATENÇÃO *** 
	 * 
	 * Este método só foi criado para implementar o método do registro de
	 * plantão e hora extra por valor com o Antigo Closing DRE, quando mudar para
	 * o Novo Closing DRE, este método não precisa ser posto em produção, não
	 * precisa fazer merge
	 * 
	 * @param dataMes data do fechamento
	 */
	boolean processOldClosindDreByValue(final Date dataMes);

}