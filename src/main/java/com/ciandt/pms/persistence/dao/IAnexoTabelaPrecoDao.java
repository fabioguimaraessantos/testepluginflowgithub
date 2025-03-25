package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.AnexoTabelaPreco;
import com.ciandt.pms.model.TabelaPreco;


/**
 * 
 * A classe IAnexoTabelaPrecoDao proporciona as funcionalidades de interface
 * para TabelaPrecoDao.
 * 
 * @since 27/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
public interface IAnexoTabelaPrecoDao extends
        IAbstractDao<Long, AnexoTabelaPreco> {

    /**
     * Busca pro entidades de anexoTabelaPreco por tabelaPreco.
     * 
     * @param tabelaPreco
     *            tabela.
     * @return Lista com entidades de anexo tabela preco.
     */
    List<AnexoTabelaPreco> findAnexoTabelaPrecoByTabelaPreco(final TabelaPreco tabelaPreco);

}
