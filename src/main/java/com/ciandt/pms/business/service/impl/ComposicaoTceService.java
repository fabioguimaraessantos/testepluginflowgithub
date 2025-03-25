package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IComposicaoTceService;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.ComposicaoTce;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.persistence.dao.IComposicaoTceDao;


/**
 * 
 * A classe ComposicaoTceService proporciona a funcionalidade da camada de
 * servi�o referente a entidade ComposicaoTce.
 * 
 * @since 07/06/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class ComposicaoTceService implements IComposicaoTceService {

    /** instancia do dao da entidade ComposicaoTce. */
    @Autowired
    private IComposicaoTceDao dao;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     * @return true se criado com sucesso, caso contrario retorna false
     */
    public Boolean createComposicaoTce(final ComposicaoTce entity) {
        if (this.existsCompTce(entity.getPessoa(), entity.getDataMes())) {
            return Boolean.valueOf(false);
        }

        dao.create(entity);

        return Boolean.valueOf(true);
    }

    /**
     * Executa um update na entidade passada por parametro.
     * 
     * @param entity
     *            que ser� atualizada
     * 
     */
    private void updateComposicaoTce(final ComposicaoTce entity) {
        dao.update(entity);
    }

    /**
     * Executa um update na entidade passada por parametro.
     * 
     * @param entity
     *            que ser� atualizada
     * @param isDifferentCodigoLogin
     *            indicador se logins s�o diferentes ou n�o
     * 
     * @return true se atualizado com sucesso, caso contrario retorna false
     */
    public Boolean updateComposicaoTce(final ComposicaoTce entity,
            final Boolean isDifferentCodigoLogin) {
        // se os logins forem diferentes, quer dizer que foi editado o campo
        // login e precisa verificar se j� existe a rela��o login/m�s na base.
        // Caso for o mesmo login n�o faz a verifica��o e atualiza normalmente.
        if (isDifferentCodigoLogin
                && this.existsCompTce(entity.getPessoa(), entity.getDataMes())) {
            return Boolean.valueOf(false);
        }

        dao.update(entity);

        return Boolean.valueOf(true);
    }

    /**
     * Remove a entidade passada por parametro.
     * 
     * @param entity
     *            que ser� removida
     */
    public void removeComposicaoTce(final ComposicaoTce entity) {
        dao.remove(entity);
    }

    /**
     * Remove as entidades pela data e tipo.
     * 
     * @param dataMes
     *            - data da vigencia.
     * @param indicadorTipo
     *            - tipo MN ou SY.
     * 
     * @return true se a remo��o ocorrou corretamente. False caso contr�rio.
     */
    private Boolean removeComposicaoTce(final Date dataMes,
            final String indicadorTipo) {
        return dao.removeByDateAndType(dataMes, indicadorTipo);
    }

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    public ComposicaoTce findCompTceById(final Long entityId) {
        return dao.findById(entityId);
    }

    /**
     * Busca pela Pessoa e pela Data.
     * 
     * @param pessoa
     *            - Pessoa utilizado na busca.
     * @param dataMes
     *            - data da vigencia.
     * 
     * @return a entidade que atende aos criterios do filtro
     */
    private ComposicaoTce findCompTceByPessoaAndDate(final Pessoa pessoa,
            final Date dataMes) {
        return dao.findByPessoaAndDate(pessoa, dataMes);
    }

    /**
     * Busca o �ltimo registro ComposicaoTce da Pessoa (maior data).
     * 
     * @param pessoa
     *            - Pessoa utilizado na busca.
     * @param dataMes
     *            - data da vigencia.
     * 
     * @return a entidade que atende aos criterios do filtro
     */
    private ComposicaoTce findByPessoaAndMaxDate(final Pessoa pessoa,
            final Date dataMes) {
        return dao.findByPessoaAndMaxDate(pessoa, dataMes);
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public List<ComposicaoTce> findCompTceByFilter(final ComposicaoTce filter) {
        return dao.findByFilter(filter);
    }

    /**
     * Busca uma lista de entidades pelo filtro e com algum valor nulo.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public List<ComposicaoTce> findCompTceByFilterMissBlank(
            final ComposicaoTce filter) {
        return dao.findByFilterMissBlank(filter);
    }

    /**
     * Verifica se existe ComposicaoTce pela Pessoa e dataMes.
     * 
     * @param pessoa
     *            - entidade Pessoa
     * @param dataMes
     *            - data m�s do registro
     * 
     * @return retorna true se existe, caso contrario false.
     */
    private Boolean existsCompTce(final Pessoa pessoa, final Date dataMes) {
        ComposicaoTce compTce = this
                .findCompTceByPessoaAndDate(pessoa, dataMes);

        if (compTce == null) {
            return Boolean.valueOf(false);
        }

        return Boolean.valueOf(true);
    }

    /**
     * Faz a c�pia do registro atual. Busca o �ltimo registro ComposicaoTce da
     * Pessoa (maior data) e copia os campos de valores.
     * 
     * @param compTce
     *            Registro que ser� atualizado
     * 
     * @return retorna true se copiou/atualizou com sucesso, caso contrario
     *         false.
     */
    public Boolean copyCompTce(final ComposicaoTce compTce) {
        ComposicaoTce lastCompTce = this.findByPessoaAndMaxDate(compTce
                .getPessoa(), compTce.getDataMes());

        // verifica se encontrou registro antes do registro atual. Se sim,
        // atualiza o registro atual e grava no banco com o status Manual e
        // retorna true. Caso contr�rio retorna false
        if (lastCompTce != null) {
            compTce.setTipoContrato(lastCompTce.getTipoContrato());
            compTce.setMoeda(lastCompTce.getMoeda());
            compTce.setValorSalario(lastCompTce.getValorSalario());
            compTce.setValorBeneficios(lastCompTce.getValorBeneficios());
            compTce.setNumeroHorasJornada(lastCompTce.getNumeroHorasJornada());
            compTce.setIndicadorTipo(Constants.TYPE_MANUAL);

            this.updateComposicaoTce(compTce);

            return Boolean.valueOf(true);
        } else {
            return Boolean.valueOf(false);
        }
    }

    /**
     * Executa a sincroniza��o do TCE completa: apaga tudo e grava novamente.
     * 
     * @param dataMes
     *            - data m�s
     * 
     * @return true se a sincroniza��o ocorrou corretamente. False caso
     *         contr�rio.
     * 
     * @throws IntegrityConstraintException
     *             - exce��o caso ocorra algum erro na remo��o
     */
    public Boolean syncCompTceFull(final Date dataMes)
            throws IntegrityConstraintException {
        Boolean isProcessOk = this.removeComposicaoTce(dataMes, "");

        if (!isProcessOk) {
            throw new IntegrityConstraintException();
        }

        return isProcessOk;
    }

    /**
     * Executa a sincroniza��o do TCE parcial: mant�m os registros do tipo
     * Manual, apaga somente os do tipo Sincronizado e grava novamente.
     * 
     * @param dataMes
     *            - data m�s
     * 
     * @return true se a sincroniza��o ocorrou corretamente. False caso
     *         contr�rio.
     * 
     * @throws IntegrityConstraintException
     *             - exce��o caso ocorra algum erro na remo��o
     */
    public Boolean syncCompTcePartial(final Date dataMes)
            throws IntegrityConstraintException {
        Boolean isProcessOk = this.removeComposicaoTce(dataMes,
                Constants.TYPE_SYNC);

        if (!isProcessOk) {
            throw new IntegrityConstraintException();
        }

        return isProcessOk;
    }

}