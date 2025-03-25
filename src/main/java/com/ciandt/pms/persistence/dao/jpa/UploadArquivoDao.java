package com.ciandt.pms.persistence.dao.jpa;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.UploadArquivo;
import com.ciandt.pms.persistence.dao.IUploadArquivoDao;


/**
 * 
 * A classe UploadArquivoDao proporciona as funcionalidades da
 * camada de persistencia referente a entidade UploadArquivo.
 *
 * @since 23/08/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Repository
public class UploadArquivoDao extends AbstractDao<Long, UploadArquivo> 
    implements IUploadArquivoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory - fabrica do entity manager.
     */
    @Autowired
    public UploadArquivoDao(@Qualifier("entityManagerFactory")
            final EntityManagerFactory factory) {
        super(factory, UploadArquivo.class);
    }
    

}
