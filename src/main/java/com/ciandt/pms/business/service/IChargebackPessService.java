package com.ciandt.pms.business.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.ChargebackPessoa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.TiRecurso;
import com.ciandt.pms.model.vo.ChargebackRow;


/**
 * 
 * A classe IChargebackPessService proporciona a interface de acesso a camada de
 * serviço referente a entidade ChargebackPessoa.
 * 
 * @since 01/07/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IChargebackPessService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     * @return true se criado com sucesso, caso contrario retorna false
     */
    @Transactional
    Boolean createChbackPess(final ChargebackPessoa entity);

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que sera atualizada.
     * @param isDifferentReg
     *            indicador se registros são diferentes ou não
     * 
     * @return true se atualizado com sucesso, caso contrario retorna false
     */
    @Transactional
    Boolean updateChbackPess(final ChargebackPessoa entity,
            final Boolean isDifferentReg);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que sera apagada.
     * 
     */
    @Transactional
    void removeChbackPess(final ChargebackPessoa entity);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    ChargebackPessoa findChbackPessById(final Long id);

    /**
     * Busca e prepara a o Mapa de Chargeback para o gerenciamento, referente a
     * um recurso.
     * 
     * @param tiRecurso
     *            - entidade do tipo ItRecurso.
     * @param startDate
     *            - data inicio do periodo
     * @param endDate
     *            - data fim do periodo
     * 
     * @return returna um lista de ChargebackRow referente ao mapa de
     *         Chargeback.
     */
    List<ChargebackRow> findChbackPessAndPrepareManagePerItResource(
            final TiRecurso tiRecurso, final Date startDate, final Date endDate);

    /**
     * Busca e prepara a o Mapa de Chargeback para o gerenciamento, referente a
     * uma Pessoa.
     * 
     * @param pessoa
     *            - entidade do tipo Pessoa.
     * @param startDate
     *            - data inicio do periodo
     * @param endDate
     *            - data fim do periodo
     * 
     * @return returna um lista de ChargebackRow referente ao mapa de
     *         Chargeback.
     */
    List<ChargebackRow> findChbackPessAndPrepareManagePerPessoa(
            final Pessoa pessoa, final Date startDate, final Date endDate);

    /**
     * Realiza uma busca por ChargebackPessoa por Pessoa, TiRecurso e Data.
     * Estes tres compoem uma cahva unica.
     * 
     * @param tiRecurso
     *            do tipo TiRecurso.
     * 
     * @param pessoa
     *            do tipo Pessoa.
     * 
     * @param monthDate
     *            data mes.
     * 
     * @return lista de ChargebackPessoa que atendem ao criterio de filtro
     */
    ChargebackPessoa findChbackPessByUniqueKey(final TiRecurso tiRecurso,
            final Pessoa pessoa, final Date monthDate);

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * @param multCodTiRecMap
     *            multiplos codigos de TiRecurso.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<ChargebackPessoa> findChbackPessByFilter(
            final ChargebackPessoa filter,
            final Map<Long, String> multCodTiRecMap);

    /**
     * Busca uma lista de entidades pelo filtro e com algum valor nulo.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<ChargebackPessoa> findChbackPessByFilterMissBlank(
            final ChargebackPessoa filter);

    /**
     * Realiza o update do 'Mapa' do chargeback.
     * 
     * @param chargebackRowList
     *            - Lista do Mapa do chargeback.
     * 
     * @return retorna true se update realizado com sucesso, caso contrario
     *         false.
     */
    @Transactional
    Boolean updateManageChargebackPess(
            final List<ChargebackRow> chargebackRowList);

    /**
     * Remove todas as linhas selecionadas.
     * 
     * @param numUnits
     *            - Numero de unidades
     * @param rowList
     *            - Lista com as linas
     * 
     * @return retorna true se removido com sucesso, caso contrario false.
     */
    @Transactional
    Boolean updateAllNumUnitsPess(final BigDecimal numUnits,
            final List<ChargebackRow> rowList);

    /**
     * Cria a linha do Mapa.
     * 
     * @param chbackPess
     *            - entidade do tipo ChargebackPessoa
     * @param startDate
     *            - data inicio do peridod
     * @param endDate
     *            = data fim do periodo
     * 
     * @return a linha criada, entidade do tipo ChargebackRow.
     */
    @Transactional
    ChargebackRow createRow(final ChargebackPessoa chbackPess,
            final Date startDate, final Date endDate);

    /**
     * Remove todas as linhas selecionadas.
     * 
     * @param rowList
     *            - Lista com as linas
     * 
     * @return retorna true se removido com sucesso, caso contrario false.
     */
    @Transactional
    Boolean removeAllSelectedRow(final List<ChargebackRow> rowList);

    /**
     * Executa a sincronização do ChargebackPessoa completa: apaga tudo e grava
     * novamente.
     * 
     * @param dataMes
     *            - data mês
     * 
     * @return true se a sincronização ocorrou corretamente. False caso
     *         contrário.
     * 
     * @throws IntegrityConstraintException
     *             - exceção caso ocorra algum erro na remoção
     */
    @Transactional
    Boolean syncChbackPessFull(final Date dataMes)
            throws IntegrityConstraintException;

    /**
     * Executa a sincronização do ChargebackPessoa parcial: mantém os registros
     * do tipo Manual, apaga somente os do tipo Sincronizado e grava novamente.
     * 
     * @param dataMes
     *            - data mês
     * 
     * @return true se a sincronização ocorrou corretamente. False caso
     *         contrário.
     * 
     * @throws IntegrityConstraintException
     *             - exceção caso ocorra algum erro na remoção
     */
    @Transactional
    Boolean syncChbackPessPartial(final Date dataMes)
            throws IntegrityConstraintException;

    /**
     * Validação do HistoryLock. Caso startDate (data de início da vigência)
     * seja maior do que a data History Lock, a vigência é válida e pode ser
     * adicionada ou removida. Caso contrário, a vigência não pode ser
     * adicionada nem removida.
     * 
     * @param startDate
     *            - data de início da vigência
     * @param showMsgError
     *            - mostra mensagem de erro caso startDate não seja válido
     * 
     * @return true se startDate for maior do que HistoryLock. false caso
     *         contrário
     */
    Boolean verifyChbackClosingDate(final Date startDate,
            final Boolean showMsgError);

    /**
     * Envia um email para os usuarios que possuem licença de software alocadas.
     */
    void sendSoftwareLicenseMail();

    boolean verifyChbackMonthApproved(Date startDate, Long codigoTiRecurso);

    ChargebackPessoa findByPessoaAndTiRecursoAndEndDate(ChargebackPessoa chbackPessoa);
}