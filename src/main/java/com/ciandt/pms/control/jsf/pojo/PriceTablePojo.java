package com.ciandt.pms.control.jsf.pojo;

import com.ciandt.pms.control.jsf.bean.ItemTabelaPrecoBean;
import com.ciandt.pms.control.jsf.bean.TabelaPrecoBean;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.PriceTableStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceTablePojo {

    private TabelaPrecoBean tabelaPrecoBean;
    private ItemTabelaPrecoBean itemTabelaPrecoBean;
    private String login;
    private Msa msa;
    private Long statusPms;
}
