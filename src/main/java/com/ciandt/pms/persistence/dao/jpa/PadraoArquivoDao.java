package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.persistence.dao.IPadraoArquivoDao;


/**
 * 
 * A classe PadraoArquivoDao proporciona as funcionalidades
 * da camada de persistencia referente a entidade PadraoArquivo.
 *
 * @since 19/08/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Repository
public class PadraoArquivoDao 
    extends AbstractDao<Long, PadraoArquivo> implements IPadraoArquivoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory - fabrica da entidade
     * 
     */
    @Autowired
    public PadraoArquivoDao(@Qualifier("entityManagerFactory")
            final EntityManagerFactory factory) {
        super(factory, PadraoArquivo.class);
    }

    /**
     * Busca todos os padrões de arquivo.
     * 
     * @return retorna uma lista de padrões de arquivos.
     */
    @SuppressWarnings("unchecked")
    public List<PadraoArquivo> findAll() {
        List<PadraoArquivo> listResult = getJpaTemplate()
            .findByNamedQuery(PadraoArquivo.FIND_ALL);

        return listResult;
    }
}
