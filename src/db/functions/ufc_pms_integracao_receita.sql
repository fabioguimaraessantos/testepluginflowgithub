CREATE OR REPLACE FUNCTION ufc_pms_integracao_receita(p_cd_receita_dfiscal IN NUMBER)
return number is
  
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
  v_empr_cd_empresa     number;
  
  v_nr_cgc            varchar2(50);
  v_erp_cd_projeto    number;
  v_erp_cd_proj_ide   number;
  v_erp_cd_ccusto     number;
  v_erp_cd_cus_ide    number;
  v_redf_in_status    varchar2(3);

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
  con_tpd_in_codigo constant number := 33;
  
  -- projeto /ccusto
  con_proj_ide_st_codigo constant varchar2(6) := 3;
  con_proj_in_reduzido constant number := 6224;
  con_cus_ide_st_codigo constant varchar2(6) := 1;
  con_cus_in_reduzido constant number :=  101;

BEGIN

v_message := '';

-- Sequencial + 1
select max(PED_IN_SEQUENCIA)+1 
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
    
    
    if (v_redf_in_status = 'W') then
    
        -- Codigo da Empresa Cliente e CiT
        select df.clie_cd_cliente, df.empr_cd_empresa
        into v_clie_cd_cliente, v_empr_cd_empresa
        from deal_fiscal df
        where df.defi_cd_deal_fiscal = v_defi_cd_deal_fiscal;
        v_error_num := v_error_num + 1;
        
        -- Numero do CNPJ
        select c.clie_cd_cnpj
        into v_nr_cgc
        from cliente c
        where c.clie_cd_cliente = v_clie_cd_cliente;
        v_error_num := v_error_num + 1;
        
        -- Dados da Filial CiT
        select e.empr_cd_erp_ccusto, e.empr_cd_erp_cus_ide, e.empr_cd_erp_projeto, e.empr_cd_erp_proj_ide
        into v_erp_cd_ccusto, v_erp_cd_cus_ide,v_erp_cd_projeto, v_erp_cd_proj_ide
        from empresa e
        where e.empr_cd_empresa = v_empr_cd_empresa;
        v_error_num := v_error_num + 1;
        
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
               ,3   
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
               ,NULL
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
              ,con_apl_in_codigo 
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
          set rdf.redf_in_status = 'I'
          where rdf.redf_cd_receita_dfiscal = p_cd_receita_dfiscal;    
          
          v_error_num := v_error_num + 1;
          
          commit;
        
          return(1);
          
   else 
        return(0);
   end if;

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
        return(0);
   end;

END ufc_pms_integracao_receita;