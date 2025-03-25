package com.ciandt.pms.persistence.dao.jpa;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.LogAuditoria;
import com.ciandt.pms.persistence.dao.ILogAuditoriaDao;


/**
 * 
 * A classe LogAuditoriaDao proporciona as funcionalidades de acesso ao banco de
 * dados referente a entidade LogAuditoria.
 * 
 * @since 22/09/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class LogAuditoriaDao extends AbstractDao<Long, LogAuditoria> implements ILogAuditoriaDao {

    /**
     * Construtor padrão.
     * 
     * @param factory da entidade
     */
    @Autowired
    public LogAuditoriaDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, LogAuditoria.class);
    }

}
