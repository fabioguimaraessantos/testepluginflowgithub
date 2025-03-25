CREATE OR REPLACE PROCEDURE usp_pms_integracao_receita(p_cd_receita_dfiscal IN NUMBER, p_result OUT NUMBER) is

  -------------------------
  -- Escopo de variáveis --
  -------------------------
  v_message            varchar2(2000);
  v_error_code         number;
  v_error_num          number;
  v_error_text         varchar2(200);
  v_ped_in_sequencia   number;
  v_rece_dt_mes        date;
  v_rece_cd_receita    number;
  v_valor_receita_deal number := 0;

  v_defi_cd_deal_fiscal number;
  v_clie_cd_cliente     number;
  v_clie_in_tipo        varchar2(5);
  v_empr_cd_empresa     number;

  v_nr_cgc            varchar2(50);
  v_erp_cd_projeto    number;
  v_erp_cd_proj_ide   number;
  v_erp_cd_ccusto     number;
  v_erp_cd_cus_ide    number;
  v_erp_cd_filial     number;
  v_erp_ind_in_codigo number;
  v_redf_in_status    varchar2(3);
  v_count_receita_df number;
  v_count_int_receita_df number;
  
  -- Variaveis Intercompany
  v_defi_pr_intercompany   number;
  v_defi_in_intercompany   varchar2(1);
  v_empr_cd_emp_intercomp  number;
  v_apl_in_codigo          number;

  
  --------------------------
  -- Escopo de constantes --
  --------------------------
  con_cod_cgc constant varchar2(3)     := 'CGC';
  con_cod_avista constant varchar2(8)  := 'PMS';
  con_cod_usu_inclusao constant number := -1;
  con_cod_st_classe constant varchar2(10) := 'Vd Serv';
  con_st_descricao constant varchar(50) := 'Item Receita';
  -- Codigo da acao
  con_acao_in_codigo constant number := 573;
  -- Codigo da aplicacao
  con_apl_in_codigo constant number := 26;
  con_apl_in_codigo_export constant number := 24;
  con_apl_in_codigo_inter_comp constant number := 39; -- Utilizado para o inter-company
  
  con_tpd_in_codigo constant number := 33;
  
BEGIN

v_message := '';

-- Sequencial + 1
select nvl(max(PED_IN_SEQUENCIA),0)+1
into v_ped_in_sequencia
from VEN_PEDIDOVENDA;
v_error_num := 0;
  begin
    -- Código da Receita e do Deal_fiscal
    select rdf.rece_cd_receita, rdf.defi_cd_deal_fiscal, rdf.redf_vl_receita, rdf.redf_in_status
    into v_rece_cd_receita, v_defi_cd_deal_fiscal, v_valor_receita_deal, v_redf_in_status
    from receita_deal_fiscal rdf
    where rdf.redf_cd_receita_dfiscal = p_cd_receita_dfiscal;
    v_error_num := v_error_num + 1;

    -- Busca o código do indice financeiro (se for real, insere null)
    select m.moed_cd_erp_ind_fin
    into v_erp_ind_in_codigo
    from receita r, contrato_pratica cp, moeda m
    where r.copr_cd_contrato_pratica = cp.copr_cd_contrato_pratica
    and cp.moed_cd_moeda_padrao = m.moed_cd_moeda (+)
    and r.rece_cd_receita = v_rece_cd_receita;
    v_error_num := v_error_num + 1;
    
    select count(*)
    into v_count_receita_df
    from receita_deal_fiscal rdf
    where rdf.rece_cd_receita = v_rece_cd_receita;
    
    if (v_redf_in_status = 'W') then

        -- Codigo da Empresa Cliente e CiT
        select df.clie_cd_cliente, df.empr_cd_empresa, df.defi_pr_intercompany, df.defi_in_intercompany, df.empr_cd_emp_intercomp
        into v_clie_cd_cliente, v_empr_cd_empresa, v_defi_pr_intercompany, v_defi_in_intercompany, v_empr_cd_emp_intercomp
        from deal_fiscal df
        where df.defi_cd_deal_fiscal = v_defi_cd_deal_fiscal;
        v_error_num := v_error_num + 1;
        
        -- Itengração de receitas Não-Intercompany
        if (v_defi_in_intercompany = 'N') then
            -- Numero do CNPJ
            select c.clie_cd_cnpj, c.clie_in_tipo
            into v_nr_cgc, v_clie_in_tipo
            from cliente c
            where c.clie_cd_cliente = v_clie_cd_cliente;
            v_error_num := v_error_num + 1;
            
            if ( v_nr_cgc is not null ) then
               
               -- Para empresas internacionais o código de aplicação eh 24
               if(v_clie_in_tipo = 'INT')then
                  v_apl_in_codigo := con_apl_in_codigo_export;                 
               else
                  v_apl_in_codigo := con_apl_in_codigo;
               end if;
               
                -- Dados da Filial CiT
                select e.empr_cd_erp_ccusto, e.empr_cd_erp_cus_ide, e.empr_cd_erp_projeto, e.empr_cd_erp_proj_ide, e.empr_cd_erp_filial
                into v_erp_cd_ccusto, v_erp_cd_cus_ide,v_erp_cd_projeto, v_erp_cd_proj_ide, v_erp_cd_filial
                from empresa e
                where e.empr_cd_empresa = v_empr_cd_empresa;
                v_error_num := v_error_num + 1;
                
                if (v_erp_cd_ccusto is not null and 
                    v_erp_cd_cus_ide is not null and 
                    v_erp_cd_projeto is not null and
                    v_erp_cd_proj_ide is not null and 
                    v_erp_cd_filial is not null) then 
                    -- Mes da Receita
                    select r.rece_dt_mes
                    into v_rece_dt_mes
                    from receita r
                    where r.rece_cd_receita = v_rece_cd_receita;
                    v_error_num := v_error_num + 1;
            
                    insert into VEN_PEDIDOVENDA ( PED_IN_SEQUENCIA
                                                 ,CLI_ST_TIPOCODIGO
                                                 ,FIL_IN_CODIGO
                                                 ,PED_IN_CODIGO
                                                 ,TPD_IN_CODIGO
                                                 ,CLI_ST_CODIGO
                                                 ,CLI_TAU_ST_CODIGO
                                                 ,COND_ST_CODIGO
                                                 ,REP_IN_CODIGO
                                                 ,TRA_IN_CODIGO
                                                 ,ACAO_IN_CODIGO
                                                 ,PED_DT_EMISSAO
                                                 ,PED_CH_SITUACAO
                                                 ,PED_CH_STATUSIMP
                                                 ,ENA_IN_CODIGO
                                                 ,IND_IN_CODIGO
                                                 ,EQU_IN_CODIGO
                                                 ,UIN_IN_CODIGO)
                    values (v_ped_in_sequencia
                           ,con_cod_cgc
                           ,v_erp_cd_filial
                           ,v_ped_in_sequencia
                           ,con_tpd_in_codigo
                           ,v_nr_cgc
                           ,'C'
                           ,con_cod_avista
                           ,null
                           ,null
                           ,con_acao_in_codigo
                           ,last_day(v_rece_dt_mes)
                           ,'N'
                           ,'N'
                           ,NULL
                           ,v_erp_ind_in_codigo
                           ,NULL
                           ,con_cod_usu_inclusao);
                    v_error_num := v_error_num + 1;
            
                    insert into VEN_ITEMPEDIDOVENDA (PED_IN_SEQUENCIA
                                                    ,ITP_IN_SEQUENCIA
                                                    ,APL_IN_CODIGO
                                                    ,PROJ_IDE_ST_CODIGO
                                                    ,PROJ_IN_REDUZIDO
                                                    ,TPR_IN_CODIGO
                                                    ,TPP_IN_CODIGO
                                                    ,CUS_IDE_ST_CODIGO
                                                    ,CUS_IN_REDUZIDO
                                                    ,ITP_CH_FRETEPCONTA
                                                    ,PRO_IN_CODIGO
                                                    ,ITP_ST_DESCRICAO
                                                    ,ITP_RE_QUANTIDADE
                                                    ,itp_re_valorunitario
                                                    ,ITP_RE_VALORTOTAL
                                                    ,TPC_ST_CLASSE
                                                    ,ITP_CH_STATUSIMP
                                                    ,UIN_IN_CODIGO)
                          values (v_ped_in_sequencia
                          ,1
                          ,v_apl_in_codigo
                          ,to_char(v_erp_cd_proj_ide)
                          ,v_erp_cd_projeto
                          ,null
                          ,null
                          ,to_char(v_erp_cd_cus_ide)
                          ,v_erp_cd_ccusto
                          ,1
                          ,1
                          ,con_st_descricao
                          ,1
                          ,v_valor_receita_deal
                          ,v_valor_receita_deal
                          ,con_cod_st_classe
                          ,'S'
                          ,con_cod_usu_inclusao);
            
                     v_error_num := v_error_num + 1;
            
                      update receita_deal_fiscal rdf
                      set rdf.redf_in_status = 'I', rdf.redf_cd_erp_pedido = v_ped_in_sequencia
                      where rdf.redf_cd_receita_dfiscal = p_cd_receita_dfiscal;
            
                      v_error_num := v_error_num + 1;
            
                      commit;
            
                      p_result := 1;
                else
                      v_message := 'Empresa Id: ' ||v_empr_cd_empresa || ' possui parâmetros inválidos';
                      update receita_deal_fiscal rdf
                      set rdf.redf_in_status = 'E', rdf.redf_tx_error = v_message
                      where rdf.redf_cd_receita_dfiscal = p_cd_receita_dfiscal;
                      commit;
                      p_result := 0;
                end if;
            else
                  v_message := 'Cliente id: '|| v_clie_cd_cliente || ' com cnpj nulo';
                  update receita_deal_fiscal rdf
                  set rdf.redf_in_status = 'E', rdf.redf_tx_error = v_message
                  where rdf.redf_cd_receita_dfiscal = p_cd_receita_dfiscal;
                  commit;
                  p_result := 0;
            end if;
            
        -- Receita Intercompany    
        elsif( v_defi_pr_intercompany > 0 and v_empr_cd_emp_intercomp is not null) then
        
            -- Numero do CNPJ da empresa a ser Faturada (cliente)
            select e.empr_nr_cnpj 
            into v_nr_cgc
            from empresa e
            where e.empr_cd_empresa = v_empr_cd_empresa;
            v_error_num := v_error_num + 1;
            
            if ( v_nr_cgc is not null ) then
                 -- Dados da Filial CiT 
                select e.empr_cd_erp_ccusto, e.empr_cd_erp_cus_ide, e.empr_cd_erp_projeto, e.empr_cd_erp_proj_ide, e.empr_cd_erp_filial
                into v_erp_cd_ccusto, v_erp_cd_cus_ide,v_erp_cd_projeto, v_erp_cd_proj_ide, v_erp_cd_filial
                from empresa e
                where e.empr_cd_empresa = v_empr_cd_emp_intercomp;
                v_error_num := v_error_num + 1; 
                
                if (v_erp_cd_ccusto is not null and 
                    v_erp_cd_cus_ide is not null and 
                    v_erp_cd_projeto is not null and
                    v_erp_cd_proj_ide is not null and 
                    v_erp_cd_filial is not null) then 
                    -- Mes da Receita
                    select r.rece_dt_mes
                    into v_rece_dt_mes
                    from receita r
                    where r.rece_cd_receita = v_rece_cd_receita;
                    v_error_num := v_error_num + 1;
                    
                    -- Valor da Receita = Valor da Receita * Percentual Intercompany
                    v_valor_receita_deal := v_valor_receita_deal * (v_defi_pr_intercompany/100);
            
                    insert into VEN_PEDIDOVENDA ( PED_IN_SEQUENCIA
                                                 ,CLI_ST_TIPOCODIGO
                                                 ,FIL_IN_CODIGO
                                                 ,PED_IN_CODIGO
                                                 ,TPD_IN_CODIGO
                                                 ,CLI_ST_CODIGO
                                                 ,CLI_TAU_ST_CODIGO
                                                 ,COND_ST_CODIGO
                                                 ,REP_IN_CODIGO
                                                 ,TRA_IN_CODIGO
                                                 ,ACAO_IN_CODIGO
                                                 ,PED_DT_EMISSAO
                                                 ,PED_CH_SITUACAO
                                                 ,PED_CH_STATUSIMP
                                                 ,ENA_IN_CODIGO
                                                 ,IND_IN_CODIGO
                                                 ,EQU_IN_CODIGO
                                                 ,UIN_IN_CODIGO)
                    values (v_ped_in_sequencia
                           ,con_cod_cgc
                           ,v_erp_cd_filial
                           ,v_ped_in_sequencia
                           ,con_tpd_in_codigo
                           ,v_nr_cgc
                           ,'C'
                           ,con_cod_avista
                           ,null
                           ,null
                           ,con_acao_in_codigo
                           ,last_day(v_rece_dt_mes)
                           ,'N'
                           ,'N'
                           ,NULL
                           ,v_erp_ind_in_codigo     
                           ,NULL
                           ,con_cod_usu_inclusao);
                    v_error_num := v_error_num + 1;
            
                    insert into VEN_ITEMPEDIDOVENDA (PED_IN_SEQUENCIA
                                                    ,ITP_IN_SEQUENCIA
                                                    ,APL_IN_CODIGO
                                                    ,PROJ_IDE_ST_CODIGO
                                                    ,PROJ_IN_REDUZIDO
                                                    ,TPR_IN_CODIGO
                                                    ,TPP_IN_CODIGO
                                                    ,CUS_IDE_ST_CODIGO
                                                    ,CUS_IN_REDUZIDO
                                                    ,ITP_CH_FRETEPCONTA
                                                    ,PRO_IN_CODIGO
                                                    ,ITP_ST_DESCRICAO
                                                    ,ITP_RE_QUANTIDADE
                                                    ,itp_re_valorunitario
                                                    ,ITP_RE_VALORTOTAL
                                                    ,TPC_ST_CLASSE
                                                    ,ITP_CH_STATUSIMP
                                                    ,UIN_IN_CODIGO)
                          values (v_ped_in_sequencia
                          ,1
                          ,con_apl_in_codigo_inter_comp
                          ,to_char(v_erp_cd_proj_ide)
                          ,v_erp_cd_projeto
                          ,null
                          ,null
                          ,to_char(v_erp_cd_cus_ide)
                          ,v_erp_cd_ccusto
                          ,1
                          ,1
                          ,con_st_descricao
                          ,1
                          ,v_valor_receita_deal
                          ,v_valor_receita_deal
                          ,con_cod_st_classe
                          ,'S'
                          ,con_cod_usu_inclusao);
            
                     v_error_num := v_error_num + 1;
            
                      update receita_deal_fiscal rdf
                      set rdf.redf_in_status = 'I', rdf.redf_cd_erp_pedido = v_ped_in_sequencia
                      where rdf.redf_cd_receita_dfiscal = p_cd_receita_dfiscal;
            
                      v_error_num := v_error_num + 1;
            
                      commit;
            
                      p_result := 1;
                 else
                      v_message := 'Empresa Id: ' ||v_empr_cd_emp_intercomp || ' possui parâmetros inválidos';
                      update receita_deal_fiscal rdf
                      set rdf.redf_in_status = 'E', rdf.redf_tx_error = v_message
                      where rdf.redf_cd_receita_dfiscal = p_cd_receita_dfiscal;
                      commit;
                      p_result := 0;
                end if;                
                      
            else 
                 v_message := 'Empresa id: '|| v_empr_cd_empresa || ' com cnpj nulo';
                 
                 update receita_deal_fiscal rdf
                 set rdf.redf_in_status = 'E', rdf.redf_tx_error = v_message
                 where rdf.redf_cd_receita_dfiscal = p_cd_receita_dfiscal;
                 commit;
                 p_result := 0;
            end if;
                      
        else
            v_message := 'Parametros Intercompany Imcompletos (Id Deal Fiscal: ' || v_defi_cd_deal_fiscal || ')';
                 
            update receita_deal_fiscal rdf
            set rdf.redf_in_status = 'E', rdf.redf_tx_error = v_message
            where rdf.redf_cd_receita_dfiscal = p_cd_receita_dfiscal;
            commit;
            p_result := 0;
            
        end if;                  

   else
        p_result := 0;
   end if;

   select count(*) 
   into v_count_int_receita_df
   from receita_deal_fiscal t
   where rece_cd_receita = v_rece_cd_receita
   and redf_in_status = 'I';
   
   --se o numero de receita_deal_fiscal integrada for igual ao numero de receita_deal_fiscal
   if v_count_int_receita_df = v_count_receita_df then
      -- Receita Integrada
      update receita r
      set r.rece_in_versao = 'IN'
      where r.rece_cd_receita = v_rece_cd_receita;
   else
      -- Receita Pendente
      update receita r
      set r.rece_in_versao = 'PD'
      where r.rece_cd_receita = v_rece_cd_receita; 
   end if;

   commit;
   
   exception
      when OTHERS then
        rollback;
        v_error_code := SQLCODE;
        v_error_text := SUBSTR(SQLERRM, 1,200);
        v_message := 'Error('|| v_error_num|| ')'||' ['||v_error_code||']'|| ' - ' || v_error_text;
        update receita_deal_fiscal rdf
        set rdf.redf_in_status = 'E', rdf.redf_tx_error = v_message
        where rdf.redf_cd_receita_dfiscal = p_cd_receita_dfiscal;
        commit;
        p_result := 0;
   end;

END usp_pms_integracao_receita;