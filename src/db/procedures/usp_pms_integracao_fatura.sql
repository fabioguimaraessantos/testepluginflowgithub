create or replace procedure usp_pms_integracao_fatura(p_cd_fatura in number, p_login in varchar2, p_result out number) is
  -------------------------
  -- Escopo de variáveis --
  -------------------------
  v_message             varchar2(2000);
  v_error_code          number;
  v_error_num           number;
  v_error_text          varchar2(200);
  v_ped_in_sequencia    number;
  v_fatu_dt_previsao    date;
  
  v_defi_cd_deal_fiscal number;
  v_clie_cd_cliente     number;
  v_empr_cd_empresa     number;
  
  v_nr_cgc              varchar2(50);
  v_erp_cd_projeto      number;
  v_erp_ind_in_codigo   number;
  v_erp_cd_proj_ide     number;
  v_erp_cd_ccusto       number;
  v_erp_cd_cus_ide      number;
  v_erp_cd_filial       number;
  v_fatu_in_status      varchar2(3);
  v_tpd_in_codigo       number;
  v_acao_in_codigo      number; 
  v_infp_cd_erp_item    number;
  v_infp_cd_erp_servico number;
  v_fatu_tx_observacao  varchar2(4000);
  
  v_itp_in_sequencia number;
  -- Variavel do código de aplicação utilizado para indicar exportação (24)
  v_apl_in_codigo number;
  v_clie_in_tipo varchar2(3);
                
  --------------------------
  -- Escopo de constantes --
  --------------------------
  con_cod_cgc constant varchar2(3)      := 'CGC';
  con_cod_avista constant varchar2(8)   := 'PMS';
  con_cod_usu_inclusao constant number  := -1;
  con_pob_ch_tipoobservacao varchar2(1) := 'N';
  -- tipo de classe
  con_cod_st_classe constant varchar2(10) := 'Vd Serv';     -- Ci&T
  con_cod_st_classe_in constant varchar2(10) := 'VDS-NEW';  -- IN
  
  --con_st_descricao constant varchar(50) := 'Item Fatura';

  -- Codigo da acao
  con_acao_in_codigo constant number := 573;
  -- Codigo da aplicacao
  con_apl_in_codigo constant number := 26;
  con_apl_in_codigo_export constant number := 24;
  con_tpd_in_codigo constant number := 33;
    
  v_st_classe varchar2(10);    
  --------------------------
  -- Cursores --------------
  --------------------------
  -- Recupera os dados dos ItemFatura da Fatura corrente
  cursor crs_itens_fatura is
    select itfa.itfa_cd_item_fatura, itfa.itfa_vl_item, itfa.tise_cd_tipo_servico, tise.tise_nm_tipo_servico,
           itfa.fore_cd_fonte_receita, df.empr_cd_empresa
-- Se precisar consolidar
--    select sum(itfa.itfa_vl_item) itfa_vl_item, itfa.tise_cd_tipo_servico, 
--           itfa.fore_cd_fonte_receita, df.empr_cd_empresa
    from fatura fatu, deal_fiscal df, item_fatura itfa, tipo_servico tise, fonte_receita fr
    where fatu.fatu_cd_fatura = p_cd_fatura
      and fatu.fatu_cd_fatura = itfa.fatu_cd_fatura
      and itfa.tise_cd_tipo_servico = tise.tise_cd_tipo_servico
      and fr.fore_cd_fonte_receita = itfa.fore_cd_fonte_receita
      and df.defi_cd_deal_fiscal = fatu.defi_cd_deal_fiscal;
-- Se precisar consolidar
-- group by itfa.tise_cd_tipo_servico, itfa.fore_cd_fonte_receita, df.empr_cd_empresa

  -- Recupera os dados dos ItemFatura do tipo 'OT'(outros) da Fatura corrente
  -- Ou seja, recupera os itens que são diferente de serviços, e então, será realizado o reembolso
  cursor crs_itens_fatura_ot is
        select itfa.itfa_cd_item_fatura, itfa.itfa_vl_item, itfa.tise_cd_tipo_servico, tise.tise_nm_tipo_servico,
           itfa.fore_cd_fonte_receita, df.empr_cd_empresa
    from fatura fatu, deal_fiscal df, item_fatura itfa, tipo_servico tise, fonte_receita fr
    where fatu.fatu_cd_fatura = p_cd_fatura
      and fatu.fatu_cd_fatura = itfa.fatu_cd_fatura
      and itfa.tise_cd_tipo_servico = tise.tise_cd_tipo_servico
      and fr.fore_cd_fonte_receita = itfa.fore_cd_fonte_receita
      and df.defi_cd_deal_fiscal = fatu.defi_cd_deal_fiscal
      and fr.fore_in_tipo = 'OT';
    
    v_it_fatura crs_itens_fatura%rowtype;
    
begin

v_message := '';
v_st_classe := '';

  begin

  v_error_num := 0;  
  -- Sequencial + 1
  select nvl(max(PED_IN_SEQUENCIA),0)
  into v_ped_in_sequencia
  from VEN_PEDIDOVENDA;
  
  v_ped_in_sequencia := v_ped_in_sequencia +1;

  -- Recupera os dados da Fatura
  select fatu.fatu_in_status, fatu.defi_cd_deal_fiscal, fatu.fatu_tx_observacao
  into v_fatu_in_status, v_defi_cd_deal_fiscal, v_fatu_tx_observacao
  from fatura fatu
  where fatu.fatu_cd_fatura = p_cd_fatura;
  v_error_num := v_error_num + 1;
  
  -- Busca o código do indice financeiro (se for real, insere null)
  -- Codigo da Empresa Cliente e CiT
  select moed.moed_cd_erp_ind_fin, defi.clie_cd_cliente, defi.empr_cd_empresa
         into v_erp_ind_in_codigo, v_clie_cd_cliente, v_empr_cd_empresa
  from fatura fatu, deal_fiscal defi, contrato_pratica copr, moeda moed
  where fatu.fatu_cd_fatura = p_cd_fatura
        and fatu.defi_cd_deal_fiscal = defi.defi_cd_deal_fiscal
        and defi.copr_cd_contrato_pratica = copr.copr_cd_contrato_pratica
        and copr.moed_cd_moeda_padrao = moed.moed_cd_moeda (+);
  
  v_error_num := v_error_num + 1;
  
  --------
  if (v_fatu_in_status = 'AP' or v_fatu_in_status = 'ER') then
    -- Numero do CNPJ
    select clie.clie_cd_cnpj, clie.clie_in_tipo
    into v_nr_cgc, v_clie_in_tipo
    from cliente clie
    where clie.clie_cd_cliente = v_clie_cd_cliente;
    
    v_error_num := v_error_num + 1;
    
    --Se cliente internaciona, codigo de aplicação = 24
    if (v_clie_in_tipo = 'INT') then
       v_apl_in_codigo := con_apl_in_codigo_export;
    --Senão, código de aplicação 26   
    else
       v_apl_in_codigo := con_apl_in_codigo;
    end if;
    
    if (v_nr_cgc is not null) then
      
      -- Dados da Filial CiT
      -- recupera os codigos necessários para a integração com ERP
      select empr.empr_cd_erp_ccusto, empr.empr_cd_erp_cus_ide, empr.empr_cd_erp_projeto, 
             empr.empr_cd_erp_proj_ide, empr.empr_cd_erp_filial, 
             empr.EMPR_CD_ERP_TPD_CODIGO, empr.EMPR_CD_ERP_ACAO_CODIGO
      into v_erp_cd_ccusto, v_erp_cd_cus_ide, v_erp_cd_projeto, 
           v_erp_cd_proj_ide, v_erp_cd_filial, v_tpd_in_codigo, v_acao_in_codigo
      from empresa empr
      where empr.empr_cd_empresa = v_empr_cd_empresa;
      v_error_num := v_error_num + 1;
      
      -- 08/06 -> Modificado por Alexandre, para não considerar a Classe Default quando for IN
      if v_erp_cd_cus_ide = 2 then
         v_st_classe := con_cod_st_classe_in;
      else
         v_st_classe := con_cod_st_classe;
      end if;
      
      if (v_erp_cd_ccusto is not null and 
          v_erp_cd_cus_ide is not null and 
          v_erp_cd_projeto is not null and
          v_erp_cd_proj_ide is not null and 
          v_erp_cd_filial is not null and
          v_tpd_in_codigo is not null and
          v_acao_in_codigo is not null) then 
          
          -- Mes da Fatura
          select fatu.fatu_dt_previsao
          into v_fatu_dt_previsao
          from fatura fatu
          where fatu.fatu_cd_fatura = p_cd_fatura;
          
          v_error_num := v_error_num + 1;
             
            -- insere PEDIDO
            insert into mgweb.VEN_PEDIDOVENDA@ln_mgweb ( PED_IN_SEQUENCIA
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
                   ,v_tpd_in_codigo --
                   ,v_nr_cgc
                   ,'C'
                   ,con_cod_avista
                   ,null
                   ,null
                   ,v_acao_in_codigo --
                   ,v_fatu_dt_previsao
                   ,'N'
                   ,'N'
                   ,NULL
                   ,v_erp_ind_in_codigo
                   ,NULL
                   ,con_cod_usu_inclusao);
                   
            v_error_num := v_error_num + 1;
            
            -- insere a observacao do pedido 
            insert into mgweb.ven_observacaopedido@ln_mgweb (pob_ch_tipoobservacao, ped_in_sequencia, pob_st_observacao)
            values (con_pob_ch_tipoobservacao , v_ped_in_sequencia, v_fatu_tx_observacao);
            
            v_itp_in_sequencia := 0;
            -- insere ITEM DE PEDIDO
            for reg in crs_itens_fatura loop
                v_itp_in_sequencia := v_itp_in_sequencia + 1;
                
                select infp_cd_erp_item, infp_cd_erp_servico 
                into v_infp_cd_erp_item, v_infp_cd_erp_servico
                from  integ_fatura_parametro i
                where i.tise_cd_tipo_servico = reg.tise_cd_tipo_servico
                      and i.fore_cd_fonte_receita = reg.fore_cd_fonte_receita
                      and i.empr_cd_empresa = reg.empr_cd_empresa;              
            
              insert into mgweb.VEN_ITEMPEDIDOVENDA@ln_mgweb (PED_IN_SEQUENCIA
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
                                              ,UIN_IN_CODIGO
                                              ,COS_IN_CODIGO)
              values (v_ped_in_sequencia
                     ,v_itp_in_sequencia
                     ,v_apl_in_codigo
                     ,to_char(v_erp_cd_proj_ide)
                     ,v_erp_cd_projeto
                     ,null
                     ,null
                     ,to_char(v_erp_cd_cus_ide)
                     ,v_erp_cd_ccusto
                     ,1
                     ,v_infp_cd_erp_item -- código do item/produto
                     ,reg.tise_nm_tipo_servico
                     ,1
                     ,reg.itfa_vl_item
                     ,reg.itfa_vl_item
                     ,v_st_classe --con_cod_st_classe
                     ,'S'
                     ,con_cod_usu_inclusao
                     ,v_infp_cd_erp_servico);
              
              v_error_num := v_error_num + 1;
            end loop;
            
            update fatura fatu
            set fatu.fatu_in_status = 'IN', 
                fatu.fatu_cd_erp_pedido = v_ped_in_sequencia
            where fatu.fatu_cd_fatura = p_cd_fatura;
  
            v_error_num := v_error_num + 1;
  
            -- ***** INICIO INTEGRACAO DA FATURA DOS ITENS DE REEMBOLSO *********
            OPEN crs_itens_fatura_ot;
            
            FETCH crs_itens_fatura_ot into v_it_fatura;
  
            if (crs_itens_fatura_ot%notfound = false) then
            
              -- Sequencial + 1
              select nvl(max(PED_IN_SEQUENCIA),0)+1
              into v_ped_in_sequencia
              from VEN_PEDIDOVENDA;
              v_error_num := 0;
  
              -- insere PEDIDO
              insert into mgweb.VEN_PEDIDOVENDA@ln_mgweb ( PED_IN_SEQUENCIA
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
                     ,con_tpd_in_codigo--
                     ,v_nr_cgc
                     ,'C'
                     ,con_cod_avista
                     ,null
                     ,null
                     ,con_acao_in_codigo --
                     ,v_fatu_dt_previsao
                     ,'N'
                     ,'N'
                     ,NULL
                     ,v_erp_ind_in_codigo
                     ,NULL
                     ,con_cod_usu_inclusao);
              v_error_num := v_error_num + 1;
              
               v_itp_in_sequencia := 0;
              -- insere ITEM DE PEDIDO
              loop 
                  v_itp_in_sequencia := v_itp_in_sequencia + 1;
                  
                  select infp_cd_erp_item, infp_cd_erp_servico 
                  into v_infp_cd_erp_item, v_infp_cd_erp_servico
                  from  integ_fatura_parametro i
                  where i.tise_cd_tipo_servico = v_it_fatura.tise_cd_tipo_servico
                        and i.fore_cd_fonte_receita = v_it_fatura.fore_cd_fonte_receita
                        and i.empr_cd_empresa = v_it_fatura.empr_cd_empresa;              
              
                  insert into mgweb.VEN_ITEMPEDIDOVENDA@ln_mgweb (PED_IN_SEQUENCIA
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
                         ,v_itp_in_sequencia
                         ,v_apl_in_codigo
                         ,to_char(v_erp_cd_proj_ide)
                         ,v_erp_cd_projeto
                         ,null
                         ,null
                         ,to_char(v_erp_cd_cus_ide)
                         ,v_erp_cd_ccusto
                         ,1
                         ,v_infp_cd_erp_item -- pro_in_codigo (codigo do item/produto)
                         ,v_it_fatura.tise_nm_tipo_servico
                         ,1
                         ,v_it_fatura.itfa_vl_item
                         ,v_it_fatura.itfa_vl_item
                         ,v_st_classe  --con_cod_st_classe
                         ,'S'
                         ,con_cod_usu_inclusao);
                  
                  v_error_num := v_error_num + 1;
                  
                  fetch crs_itens_fatura_ot into v_it_fatura;
                  exit when crs_itens_fatura_ot%notfound;
                
              end loop;

            end if;
            -- ******** FIM INTEGRACAO DA FATURA DOS ITENS DE REEMBOLSO ********
            close crs_itens_fatura_ot;
                          
            v_error_num := v_error_num + 1;
             
            commit;
  
            p_result := 1;
                
      else -- v_erp_cd_ccusto, outros...
        v_message := 'Empresa Id: ' || v_empr_cd_empresa || ' possui parâmetros inválidos';
        update fatura fatu
        set fatu.fatu_in_status = 'ER', fatu.fatu_tx_error = v_message
        where fatu.fatu_cd_fatura = p_cd_fatura;
        commit;
        p_result := 0;
      end if; -- v_erp_cd_ccusto, outros...       
    else -- v_nr_cgc
      v_message := 'Cliente id: '|| v_clie_cd_cliente || ' com cnpj nulo';
      update fatura fatu
      set fatu.fatu_in_status = 'ER', fatu.fatu_tx_error = v_message
      where fatu.fatu_cd_fatura = p_cd_fatura;
      commit;
      p_result := 0;
    end if; -- v_nr_cgc
  else -- v_fatu_in_status
    p_result := 0;
  end if; -- v_fatu_in_status
  
  commit;
    
  exception
    when OTHERS then
      rollback;
      v_error_code := SQLCODE;
      v_error_text := SUBSTR(SQLERRM, 1,200);
      v_message := 'Error('|| v_error_num ||')'||' ['||v_error_code||']'|| ' - ' || v_error_text;
      update fatura fatu
      set fatu.fatu_in_status = 'ER', fatu.fatu_tx_error = v_message
      where fatu.fatu_cd_fatura = p_cd_fatura;
      commit;
      p_result := 0;
  end;
  
  -- se p_result = 1 integrou, senão deu erro
  -- grava na tabela historico_fatura Integrated ou Error
  if (p_result = 1) then
     insert into historico_fatura
      (hifa_cd_historico_fatura, fatu_cd_fatura, hifa_cd_autor, hifa_dt_alteracao, hifa_in_status_anterior, hifa_in_status_atual)
     values
      (sq_hifa_cd_historico_fatura.nextval, p_cd_fatura, p_login, sysdate, v_fatu_in_status, 'IN');
  else
      insert into historico_fatura
      (hifa_cd_historico_fatura, fatu_cd_fatura, hifa_cd_autor, hifa_dt_alteracao, hifa_in_status_anterior, hifa_in_status_atual)
     values
      (sq_hifa_cd_historico_fatura.nextval, p_cd_fatura, p_login, sysdate, v_fatu_in_status, 'ER');
  end if;
  
  commit;

end usp_pms_integracao_fatura;