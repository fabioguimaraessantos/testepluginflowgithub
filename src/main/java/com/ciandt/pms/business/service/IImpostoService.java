package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.Imposto;


/**
 * 
 * Define a interface do Service da entidade.
 * 
 * @since 08/10/2009
 * @author <a href="mailto:hkushima@ciandt.com">Henrique Takashi Kushima</a>
 * 
 */
@Service
public interface IImpostoService {

    // VERIFICAR!!!
    /**
     * Cria uma entidade.
     * 
     * @param entity
     *            que sera criada.
     * 
     * @return true se for inserida com sucesso.
     */
    Boolean createImposto(final Imposto entity);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que sera apagada.
     * 
     */
    @Transactional
    void removeImposto(final Imposto entity);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     * 
     */
    List<Imposto> findImpostoAll();

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     * 
     */
    Imposto findImpostoById(final Long id);

}
