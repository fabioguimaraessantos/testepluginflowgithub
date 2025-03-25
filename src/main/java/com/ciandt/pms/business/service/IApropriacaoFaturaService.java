package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.FaturaReceita;
import com.ciandt.pms.model.ReceitaDealFiscal;
import com.ciandt.pms.model.vo.ApropriacaoFaturaFormFilter;
import com.ciandt.pms.model.vo.ApropriacaoFaturaRow;
import com.ciandt.pms.model.vo.FaturaReceitaRow;


/**
 * 
 * A classe IApropriacaoFaturaService proporciona a interface 
 * de acesso ao serviço de ApropriacaoFatura.
 *
 * @since 19/01/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public interface IApropriacaoFaturaService {

    /**
     * Realiza a busca pelas entidades ReceitaDealFiscal.
     * 
     * @param formFilter - form do filtro
     * 
     * @return - retorna uma lista de ApropriacaoFaturaRow, com
     * o resultado da busca
     */
    List<ApropriacaoFaturaRow> findApropriacaoFaturaByFilter(
            final ApropriacaoFaturaFormFilter formFilter);
    
    /**
     * Prepara a a lista de FaturaReceita que será exibido na tela
     * de gerenciamento da apropriação.
     * 
     * @param rdf - ReceitaDealFiscal utilizado para buscar a lista.
     * 
     * @return lista de FaturaReceita.
     */
    List<FaturaReceitaRow> prepareManage(final ReceitaDealFiscal rdf);
    
    /**
     * Realiza a apropriação. Salva as associações entre receita e fatura. 
     * 
     * @param fatRecList - Lista com a faturas a serem associadas.
     * 
     * @return retorna true se salvo com sucesso, caso contrario false.
     */
    @Transactional
    Boolean save(final List<FaturaReceitaRow> fatRecList);
    
    /**
     * Realiza a validação do valor associado da fatura com a receita.
     * 
     * @param fr - FaturaReceita a ser associado.
     * 
     * @return retorna true se total do valor associado <= ao total da fatura.
     */
    Boolean validateAssociateValue(final FaturaReceita fr);
    
    /**
     * Retorna uma lista com as Receitas(FaturaReceita) associados
     * a fatura passada por parametro.
     * 
     * @param fatura - entidade do tipo Fatura. 
     * 
     * @return retorna uma lista de FaturaReceita
     */
    List<FaturaReceita> findRevenuesByInvoice(final Fatura fatura);
}
