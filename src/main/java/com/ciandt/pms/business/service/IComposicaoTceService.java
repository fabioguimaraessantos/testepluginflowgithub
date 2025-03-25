package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.ComposicaoTce;


/**
 * 
 * A classe IComposicaoTceService proporciona a interface de acesso a camada de
 * servi�o referente a entidade ComposicaoTce.
 * 
 * @since 07/06/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IComposicaoTceService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     * @return true se criado com sucesso, caso contrario retorna false
     */
    @Transactional
    Boolean createComposicaoTce(final ComposicaoTce entity);

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
    @Transactional
    Boolean updateComposicaoTce(final ComposicaoTce entity,
            final Boolean isDifferentCodigoLogin);

    /**
     * Remove a entidade passada por parametro.
     * 
     * @param entity
     *            que ser� removida.
     * 
     */
    @Transactional
    void removeComposicaoTce(final ComposicaoTce entity);

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    ComposicaoTce findCompTceById(final Long entityId);
    
    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<ComposicaoTce> findCompTceByFilter(final ComposicaoTce filter);
    
    /**
     * Busca uma lista de entidades pelo filtro e com algum valor nulo.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<ComposicaoTce> findCompTceByFilterMissBlank(final ComposicaoTce filter);
    
    /**
     * Faz a c�pia do registro atual. Busca o �ltimo registro ComposicaoTce da
     * Pessoa (maior data) e copia os campos de valores.
     * 
     * @param compTce Registro que ser� atualizado
     * 
     * @return retorna true se copiou/atualizou com sucesso, caso contrario false.
     */
    @Transactional
    Boolean copyCompTce(final ComposicaoTce compTce);
    
    /**
     * Executa a sincroniza��o do TCE completa: apaga tudo e grava novamente.
     * 
     * @param dataMes
     *            - data m�s
     * 
     * @return true se a sincroniza��o ocorrou corretamente. False caso contr�rio.
     * 
     * @throws IntegrityConstraintException
     *             - exce��o caso ocorra algum erro na remo��o
     */
    @Transactional
    Boolean syncCompTceFull(final Date dataMes) throws IntegrityConstraintException;

    /**
     * Executa a sincroniza��o do TCE parcial: mant�m os registros do tipo
     * Manual, apaga somente os do tipo Sincronizado e grava novamente.
     * 
     * @param dataMes
     *            - data m�s
     * 
     * @return true se a sincroniza��o ocorrou corretamente. False caso contr�rio
     * 
     * @throws IntegrityConstraintException
     *             - exce��o caso ocorra algum erro na remo��o
     */
    @Transactional
    Boolean syncCompTcePartial(final Date dataMes) throws IntegrityConstraintException;

}