create or replace function ufc_pms_integracao_receita_app(p_cd_receita_dfiscal IN number) return SYS_REFCURSOR is
  refcursor             SYS_REFCURSOR;
  p_result              number;
  -------------------------
  -- Escopo de variáveis --
  -------------------------
  v_message            varchar2(2000);
  v_error_code         number;
  v_error_num          number;
  v_error_text         varchar2(200);
  v_ped_in_sequencia   number;
  v_rece_dt_mes        date;
  v_remo_cd_receita_moeda    number;
  v_valor_receita_deal number := 0;
  v_valor_receita_moeda number := 0;

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
  v_infp_cd_erp_item    number; --AQUI--
  v_infp_cd_erp_servico number; --AQUI--

  -- Variaveis Intercompany
  v_defi_pr_intercompany   number;
  v_defi_in_intercompany   varchar2(1);
  v_empr_cd_emp_intercomp  number;
  v_empr_cd_emp_pai_intercomp  number;
  v_apl_in_codigo          number;
  v_valor_logins_intercomp number := 0;

  -- Variaveis Fatura Intecompany
  v_tpd_in_codigo number;

  v_tise_cd_tipo_servico NUMBER; --AQUI--


  --------------------------
  -- Escopo de constantes --
  --------------------------
  con_cod_cgc constant varchar2(3)     := 'CGC';
  con_cod_avista constant varchar2(8)  := 'PMS';
  con_cod_usu_inclusao constant number := -1;
  con_cod_st_classe constant varchar2(10) := 'Vd Serv';
  con_st_descricao constant varchar(50) := 'Item Receita';
  con_st_descricao_fatura constant varchar(50) := 'Item Fatura';
  -- Codigo da acao
  con_acao_in_codigo constant number := 573;
  -- Codigo da aplicacao
  con_apl_in_codigo constant number := 26;
  con_apl_in_codigo_export constant number := 24;
  con_apl_in_codigo_inter_comp constant number := 39; -- Utilizado para o inter-company

  con_tpd_in_codigo constant number := 33;
  con_cos_in_codigo constant number := 1;
  
  con_fonte_receita NUMBER := 1;

BEGIN

v_message := '';

-- Sequencial + 1
select nvl(max(PED_IN_SEQUENCIA),0)+1
into v_ped_in_sequencia
from VEN_PEDIDOVENDA;
v_error_num := 0;
  begin
    -- Código da Receita e do Deal_fiscal e Valor total da receita e da receita_deal_fiscal
    select rdf.remo_cd_receita_moeda, rdf.defi_cd_deal_fiscal, rdf.redf_vl_receita, rdf.redf_in_status, rm.remo_vl_total_moeda
    into v_remo_cd_receita_moeda, v_defi_cd_deal_fiscal, v_valor_receita_deal, v_redf_in_status, v_valor_receita_moeda
    from receita_deal_fiscal rdf, receita_moeda rm
    where rdf.remo_cd_receita_moeda = rm.remo_cd_receita_moeda and
          rdf.redf_cd_receita_dfiscal = p_cd_receita_dfiscal;
    v_error_num := v_error_num + 1;

    -- Busca o código do indice financeiro (se for real, insere null)
   select m.moed_cd_erp_ind_fin
     into v_erp_ind_in_codigo
     from receita_moeda rm, moeda m
    where rm.moed_cd_moeda = m.moed_cd_moeda
      and rm.remo_cd_receita_moeda = v_remo_cd_receita_moeda;
   v_error_num := v_error_num + 1;

    select count(*)
   into v_count_receita_df
   from receita_deal_fiscal rdf, receita_moeda rm
   where rdf.remo_cd_receita_moeda = rm.remo_cd_receita_moeda
   and rm.rece_cd_receita = (select rm1.rece_cd_receita from receita_moeda rm1 where rm1.remo_cd_receita_moeda = v_remo_cd_receita_moeda);

    if (v_redf_in_status is null or v_redf_in_status = 'W') then

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
                    from receita r, receita_moeda rm
                    where rm.rece_cd_receita = r.rece_cd_receita and
                    rm.remo_cd_receita_moeda = v_remo_cd_receita_moeda;
                    v_error_num := v_error_num + 1;

                    insert into mgweb.VEN_PEDIDOVENDA@ln_mgweb( PED_IN_SEQUENCIA
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

                    -- seleciona tipo de servico
                    SELECT tsdf.tise_cd_tipo_servico INTO v_tise_cd_tipo_servico
                    FROM tipo_servico_deal_fiscal tsdf
                    WHERE tsdf.defi_cd_deal_fiscal = v_defi_cd_deal_fiscal;                  
                   
                    -- seleciona pro_in_codigo e cos_in_codigo (respectivamente)
                    SELECT infp_cd_erp_item, infp_cd_erp_servico
                            into v_infp_cd_erp_item, v_infp_cd_erp_servico
                    FROM  integ_fatura_parametro i
                    WHERE i.tise_cd_tipo_servico =  v_tise_cd_tipo_servico
                    AND i.fore_cd_fonte_receita = con_fonte_receita --con_fonte_receita = 1 (constante)
                    AND i.empr_cd_empresa = v_empr_cd_empresa;

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
                          ,1
                          ,v_apl_in_codigo
                          ,to_char(v_erp_cd_proj_ide)
                          ,v_erp_cd_projeto
                          ,null
                          ,null
                          ,to_char(v_erp_cd_cus_ide)
                          ,v_erp_cd_ccusto
                          ,1
                          ,v_infp_cd_erp_item -- código do item/produto
                          ,con_st_descricao
                          ,1
                          ,v_valor_receita_deal
                          ,v_valor_receita_deal
                          ,con_cod_st_classe
                          ,'S'
                          ,con_cod_usu_inclusao
                          ,v_infp_cd_erp_servico);

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
                select e.empr_cd_erp_ccusto, e.empr_cd_erp_cus_ide, e.empr_cd_erp_projeto, e.empr_cd_erp_proj_ide, e.empr_cd_erp_filial, e.empr_cd_empresa_matriz, e.empr_cd_erp_tpd_codigo
                into v_erp_cd_ccusto, v_erp_cd_cus_ide,v_erp_cd_projeto, v_erp_cd_proj_ide, v_erp_cd_filial, v_empr_cd_emp_pai_intercomp, v_tpd_in_codigo
                from empresa e
                where e.empr_cd_empresa = v_empr_cd_emp_intercomp;
                v_error_num := v_error_num + 1;

                -- Soma valores dos logins Intercompany
                select sum(ir.itre_vl_total_item)
                into v_valor_logins_intercomp
                from item_receita ir, receita_moeda rm, receita r
                where ir.remo_cd_receita_moeda = rm.remo_cd_receita_moeda and
                      rm.rece_cd_receita = r.rece_cd_receita and
                      rm.remo_cd_receita_moeda = v_remo_cd_receita_moeda and
                      ir.pess_cd_pessoa in (select ptc.pess_cd_pessoa
                                          from pessoa_tipo_contrato ptc
                                          where ptc.empr_cd_empresa = v_empr_cd_emp_pai_intercomp
                                          and r.rece_dt_mes between ptc.petc_dt_inicio and nvl(ptc.petc_dt_fim, r.rece_dt_mes+1) );



                v_error_num := v_error_num + 1;

                if (v_erp_cd_ccusto is not null and
                    v_erp_cd_cus_ide is not null and
                    v_erp_cd_projeto is not null and
                    v_erp_cd_proj_ide is not null and
                    v_erp_cd_filial is not null and
					v_empr_cd_emp_pai_intercomp is not null and
					v_tpd_in_codigo is not null) then
                    -- Mes da Receita
                    select r.rece_dt_mes
                    into v_rece_dt_mes
                    from receita r, receita_moeda rm
                    where rm.rece_cd_receita = r.rece_cd_receita and
                    rm.remo_cd_receita_moeda = v_remo_cd_receita_moeda;
                    v_error_num := v_error_num + 1;

                    -- Valor da Receita = Percentual do Fiscal Deal * Valor dos Logins Intercompany * Percentual Intercompany
                    v_valor_receita_deal := (v_valor_receita_deal/v_valor_receita_moeda) * v_valor_logins_intercomp * (v_defi_pr_intercompany/100);

                    -- Insere a receita Intercompany
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

                    -- Insere o item da receita Intercompany
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
                          ,con_cod_usu_inclusao
                          ,con_COS_IN_CODIGO);

                     v_error_num := v_error_num + 1;

                      update receita_deal_fiscal rdf
                      set rdf.redf_in_status = 'I', rdf.redf_cd_erp_pedido = v_ped_in_sequencia
                      where rdf.redf_cd_receita_dfiscal = p_cd_receita_dfiscal;

                      v_error_num := v_error_num + 1;

                    -- Incrementa o codigo do pedido
                    v_ped_in_sequencia := v_ped_in_sequencia + 1;

                    -- Insere a fatura Intercompany
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
                           ,v_tpd_in_codigo
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

                    -- Insere o item da fatura Intercompany
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
                          ,con_st_descricao_fatura
                          ,1
                          ,v_valor_receita_deal
                          ,v_valor_receita_deal
                          ,con_cod_st_classe
                          ,'S'
                          ,con_cod_usu_inclusao
                          ,con_cos_in_codigo);

                     v_error_num := v_error_num + 1;

                      update receita_deal_fiscal rdf
                      set rdf.redf_in_status = 'I', rdf.redf_cd_erp_pedido_fatura = v_ped_in_sequencia
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
   from receita_deal_fiscal rdf, receita_moeda rm
   where rdf.remo_cd_receita_moeda = rm.remo_cd_receita_moeda
   and rm.rece_cd_receita = (select rm1.rece_cd_receita from receita_moeda rm1 where rm1.remo_cd_receita_moeda = v_remo_cd_receita_moeda)
   and rdf.redf_in_status = 'I';

   --se o numero de receita_deal_fiscal integrada for igual ao numero de receita_deal_fiscal
   if v_count_int_receita_df = v_count_receita_df then
      -- Receita Integrada
      update receita r
      set r.rece_in_versao = 'IN'
      where r.rece_cd_receita = (select rm.rece_cd_receita from receita_moeda rm where rm.remo_cd_receita_moeda = v_remo_cd_receita_moeda);
   else
      -- Receita Pendente
      update receita r
      set r.rece_in_versao = 'PD'
      where r.rece_cd_receita = (select rm.rece_cd_receita from receita_moeda rm where rm.remo_cd_receita_moeda = v_remo_cd_receita_moeda);
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

   open refcursor for select p_result from dual;

   return refcursor;

END ufc_pms_integracao_receita_app;


