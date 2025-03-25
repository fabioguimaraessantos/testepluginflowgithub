package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAliquotaService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.Aliquota;
import com.ciandt.pms.model.Imposto;
import com.ciandt.pms.persistence.dao.IAliquotaDao;


/**
 * 
 * A classe AliquotaService proporciona as funcionalidades da camada de serviço
 * referentes a entidade Aliquota.
 * 
 * @since 07/10/2009
 * @author <a href="mailto:hkushima@ciandt.com">Henrique Takashi Kushima</a>
 * 
 */
@Service
public class AliquotaService implements IAliquotaService {

    /** Instancia do DAO da entidade TabelaPreco. */
    @Autowired
    private IAliquotaDao dao;

    /**
     * Cria uma entidade Aliquota.
     * 
     * @param entity
     *            da tabela de preço
     * 
     * @return retorna true se a data inicio da vigencia, é posterior a maior
     *         data existente no banco de dados. Caso contrario retorna false
     */
    public Boolean createAliquota(final Aliquota entity) {

        Aliquota al = dao.findMaxStartDateByImposto(entity.getImposto());
        // verifica se o imposto já existe
        if (al != null) {
            if (entity.getDataInicio().after(al.getDataInicio())) {
                al.setDataFim(DateUtils.addMonths(entity.getDataInicio(), -1));
                dao.update(al);
            } else {
                Messages.showError("createTabelaPreco",
                        Constants.MSG_ERROR_ADD_ALIQUOTA_PERIOD);
                return Boolean.FALSE;
            }
        }
        // }
        dao.create(entity);

        return Boolean.TRUE;
    }

    /**
     * Remove a entidade passada por parametro.
     * 
     * @param entity
     *            que será removida
     * @return true se aliquota foi removida corretamente, coso contrário
     *         retorna false
     */
    public Boolean removeAliquota(final Aliquota entity) {

        Aliquota maxTp = findMaxStartDateByImposto(entity.getImposto());

        // verifica se o maior tabela preço é o que esta sendo deletado
        if (maxTp.getCodigoAliquota().compareTo(entity.getCodigoAliquota()) == 0) {

            dao.remove(entity);
            // pega o proximo maior, para setar a data final como null
            maxTp = findMaxStartDateByImposto(entity.getImposto());
            if (maxTp != null) {
                maxTp.setDataFim(null);
                dao.update(maxTp);
            }
            return true;
        } else {
            Messages.showError("removeAliquota",
                    Constants.MSG_ERROR_ALIQUOTA_REMOVE);
            return false;
        }
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<Aliquota> findAliquotaAll() {
        return dao.findAll();
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public Aliquota findAliquotaById(final Long id) {
        return dao.findById(id);
    }

    /**
     * Busca uma lista de entidades pelo contrato pratica.
     * 
     * @param imposto
     *            entidade populada com os valores do imposto.
     * @return lista de entidades que atendem ao criterio do imposto.
     */
    public List<Aliquota> findAliquotaByImposto(final Imposto imposto) {
        return dao.findByImposto(imposto);
    }

    /**
     * Busca a entidade com maior data inicio da vigencia.
     * 
     * @param imposto
     *            entidade populada com os valores do imposto.
     * 
     * @return lista de entidades que atendem ao criterio do imposto.
     */
    public Aliquota findMaxStartDateByImposto(final Imposto imposto) {
        return dao.findMaxStartDateByImposto(imposto);
    }

    /**
     * Busca a entidade na data atual.
     * 
     * @param imposto
     *            entidade populada com os valores do imposto.
     * 
     * @return lista de entidades que atendem ao criterio do imposto.
     */
    public Aliquota findByImpostoByCurrentDate(final Imposto imposto) {
        return dao.findByImpostoByCurrentDate(imposto);
    }

}
