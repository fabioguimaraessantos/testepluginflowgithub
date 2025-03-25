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
  v_count_not_int_receita_df number;

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
  v_infp_cd_erp_item    number; --PRO_IN_CODIGO
  v_infp_cd_erp_servico number; --COS_IN_CODIGO

  -- Variaveis Intercompany
  v_defi_pr_intercompany   number;
  v_defi_in_intercompany   varchar2(1);
  
  v_empr_cd_emp_intercomp  number;
  v_empr_cd_emp_pai_intercomp  number;
  v_apl_in_codigo          number;
  v_valor_logins_intercomp number := 0;

  -- Variaveis Fatura Intecompany
  v_tpd_in_codigo number;
  
  -- Variaveis Receita Plantao
  v_repl_vl_receita_plantao NUMBER;

  v_tise_cd_tipo_servico NUMBER;
  
  v_tise_cd_tipo_servico_mega NUMBER;
  v_tise_descricao VARCHAR(100);

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

  -- Tipo do item no Mega para Faturas Intercompany
  con_cos_in_codigo_fatura_int constant number := 12;

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
                
                SELECT c.CONV_CD_CCUSTO_MEGA, mc.CUS_IDE_ST_CODIGO, c.CONV_CD_PROJETO_MEGA, mp.PRO_IDE_ST_CODIGO, e.EMPR_CD_ERP_FILIAL
                INTO v_erp_cd_ccusto, v_erp_cd_cus_ide,v_erp_cd_projeto, v_erp_cd_proj_ide, v_erp_cd_filial
                FROM RECEITA r 
                INNER JOIN RECEITA_MOEDA rm ON r.RECE_CD_RECEITA = rm.RECE_CD_RECEITA
                INNER JOIN RECEITA_DEAL_FISCAL rdf ON rm.REMO_CD_RECEITA_MOEDA = rdf.REMO_CD_RECEITA_MOEDA
                INNER JOIN DEAL_FISCAL df ON rdf.DEFI_CD_DEAL_FISCAL = df.DEFI_CD_DEAL_FISCAL
                INNER JOIN empresa e ON df.EMPR_CD_EMPRESA = e.EMPR_CD_EMPRESA
                INNER JOIN CONVERGENCIA c ON r.COPR_CD_CONTRATO_PRATICA = c.COPR_CD_CONTRATO_PRATICA
                INNER JOIN VW_MEGA_CCUSTO mc ON c.CONV_CD_CCUSTO_MEGA = mc.CUS_IN_REDUZIDO
                INNER JOIN VW_MEGA_PROJETO mp ON c.CONV_CD_PROJETO_MEGA = mp.COD_IN_PROJETO
                WHERE rdf.REDF_CD_RECEITA_DFISCAL = p_cd_receita_dfiscal;
                v_error_num := v_error_num + 1;

                if (v_erp_cd_ccusto != 0 and
                    v_erp_cd_cus_ide != 0 and
                    v_erp_cd_projeto != 0 and
                    v_erp_cd_proj_ide != 0 and
                    v_erp_cd_filial != 0) then
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

                      -- Integração Receita Plantão

                      SELECT rp.REPL_VL_RECEITA_PLANTAO 
                      INTO v_repl_vl_receita_plantao
                      FROM RECEITA_PLANTAO rp 
                      WHERE rp.REDF_CD_RECEITA_DFISCAL = p_cd_receita_dfiscal;

                 IF v_repl_vl_receita_plantao > 0 THEN

                      V_PED_IN_SEQUENCIA := V_PED_IN_SEQUENCIA + 1;

                    INSERT INTO VEN_PEDIDOVENDA@ln_mgweb.CIT
                      (PED_IN_SEQUENCIA,
                       CLI_ST_TIPOCODIGO,
                       FIL_IN_CODIGO,
                       PED_IN_CODIGO,
                       TPD_IN_CODIGO,
                       CLI_ST_CODIGO,
                       CLI_TAU_ST_CODIGO,
                       COND_ST_CODIGO,
                       REP_IN_CODIGO,
                       TRA_IN_CODIGO,
                       ACAO_IN_CODIGO,
                       PED_DT_EMISSAO,
                       PED_CH_SITUACAO,
                       PED_CH_STATUSIMP,
                       ENA_IN_CODIGO,
                       IND_IN_CODIGO,
                       EQU_IN_CODIGO,
                       UIN_IN_CODIGO)
                    VALUES
                      (V_PED_IN_SEQUENCIA,
                       CON_COD_CGC,
                       V_ERP_CD_FILIAL,
                       V_PED_IN_SEQUENCIA,
                       CON_TPD_IN_CODIGO,
                       V_NR_CGC,
                       'C',
                       CON_COD_AVISTA,
                       NULL,
                       NULL,
                       CON_ACAO_IN_CODIGO,
                       LAST_DAY(V_RECE_DT_MES),
                       'N',
                       'N',
                       NULL,
                       V_ERP_IND_IN_CODIGO,
                       NULL,
                       CON_COD_USU_INCLUSAO);

                    v_error_num := v_error_num + 1;

                    -- código do item especifico para receita plantao
                    --v_infp_cd_erp_item := 1100;

                    select inrp_cd_erp_item
                      into v_infp_cd_erp_item
                      from integ_receita_parametro i
                    WHERE i.tise_cd_tipo_servico =  v_tise_cd_tipo_servico
                    AND i.fore_cd_fonte_receita = con_fonte_receita --con_fonte_receita = 1 (constante)
                    AND i.empr_cd_empresa = v_empr_cd_empresa;

                    INSERT INTO VEN_ITEMPEDIDOVENDA@ln_mgweb.CIT
                      (PED_IN_SEQUENCIA,
                       ITP_IN_SEQUENCIA,
                       APL_IN_CODIGO,
                       PROJ_IDE_ST_CODIGO,
                       PROJ_IN_REDUZIDO,
                       TPR_IN_CODIGO,
                       TPP_IN_CODIGO,
                       CUS_IDE_ST_CODIGO,
                       CUS_IN_REDUZIDO,
                       ITP_CH_FRETEPCONTA,
                       PRO_IN_CODIGO,
                       ITP_ST_DESCRICAO,
                       ITP_RE_QUANTIDADE,
                       ITP_RE_VALORUNITARIO,
                       ITP_RE_VALORTOTAL,
                       TPC_ST_CLASSE,
                       ITP_CH_STATUSIMP,
                       UIN_IN_CODIGO,
                       COS_IN_CODIGO)
                    VALUES
                      (V_PED_IN_SEQUENCIA,
                       1,
                       V_APL_IN_CODIGO,
                       TO_CHAR(V_ERP_CD_PROJ_IDE),
                       V_ERP_CD_PROJETO,
                       NULL,
                       NULL,
                       TO_CHAR(V_ERP_CD_CUS_IDE),
                       V_ERP_CD_CCUSTO,
                       1,
                       V_INFP_CD_ERP_ITEM -- código do item/produto
                      ,
                       CON_ST_DESCRICAO,
                       1,
                       v_repl_vl_receita_plantao,
                       v_repl_vl_receita_plantao,
                       CON_COD_ST_CLASSE,
                       'S',
                       CON_COD_USU_INCLUSAO,
                       V_INFP_CD_ERP_SERVICO);

                       v_error_num := v_error_num + 1;

                      update receita_plantao rp
                      set rp.REPL_IN_INTEGRACAO = 'I', rp.REPL_CD_ERP_PEDIDO = v_ped_in_sequencia
                      where rp.redf_cd_receita_dfiscal = p_cd_receita_dfiscal;

                      v_error_num := v_error_num + 1;

                      p_result := 1;
                  END IF;

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
                SELECT NVL(MAX(c.conv_cd_ccusto_mega),0), NVL(MAX(ccm.CUS_IDE_ST_CODIGO),0), NVL(MAX(c.conv_cd_projeto_mega),0), NVL(MAX(p.PRO_IDE_ST_CODIGO),0), NVL(MAX(e.empr_cd_erp_filial),0), NVL(MAX(e.empr_cd_empresa_matriz),0), NVL(MAX(e.empr_cd_erp_tpd_codigo),0)
                INTO v_erp_cd_ccusto, v_erp_cd_cus_ide, v_erp_cd_projeto, v_erp_cd_proj_ide, v_erp_cd_filial, v_empr_cd_emp_pai_intercomp, v_tpd_in_codigo
                FROM RECEITA r, RECEITA_MOEDA rm, CONVERGENCIA c, empresa e, RECEITA_DEAL_FISCAL rdf, DEAL_FISCAL df, VW_MEGA_PROJETO p, VW_MEGA_CCUSTO ccm
                WHERE rm.RECE_CD_RECEITA       = r.RECE_CD_RECEITA
                AND c.COPR_CD_CONTRATO_PRATICA = r.COPR_CD_CONTRATO_PRATICA
                AND rdf.REMO_CD_RECEITA_MOEDA = rm.REMO_CD_RECEITA_MOEDA
                AND df.DEFI_CD_DEAL_FISCAL = rdf.DEFI_CD_DEAL_FISCAL
                AND e.EMPR_CD_EMPRESA = df.EMPR_CD_EMP_INTERCOMP
                and p.COD_IN_PROJETO = c.CONV_CD_PROJETO_MEGA
                and p.PRO_PAD_IN_CODIGO = c.ERP_CD_PRO_PAD_PROJ
                and ccm.CUS_IN_REDUZIDO = c.CONV_CD_CCUSTO_MEGA
                and ccm.EMPR_CD_EMPRESA = e.EMPR_CD_EMPRESA_MATRIZ
                AND rm.REMO_CD_RECEITA_MOEDA = v_remo_cd_receita_moeda;
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

                if (v_erp_cd_ccusto != 0 and
                    v_erp_cd_cus_ide != 0 and
                    v_erp_cd_projeto != 0 and
                    v_erp_cd_proj_ide != 0 and
                    v_erp_cd_filial != 0 and
                    v_empr_cd_emp_pai_intercomp != 0 and
                    v_tpd_in_codigo != 0) then
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
                          ,v_infp_cd_erp_item
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

                    -- Seleciona o codigo e texto
                    SELECT ts.TISE_CD_ERP, ts.TISE_TX_ERP
                    INTO v_tise_cd_tipo_servico_mega, v_tise_descricao
                    FROM RECEITA_DEAL_FISCAL rdf
                    INNER JOIN DEAL_FISCAL df ON rdf.DEFI_CD_DEAL_FISCAL = df.DEFI_CD_DEAL_FISCAL
                    INNER JOIN TIPO_SERVICO_DEAL_FISCAL tsdf ON df.DEFI_CD_DEAL_FISCAL = tsdf.DEFI_CD_DEAL_FISCAL
                    INNER JOIN TIPO_SERVICO ts ON tsdf.TISE_CD_TIPO_SERVICO = ts.TISE_CD_TIPO_SERVICO
                    WHERE rdf.REDF_CD_RECEITA_DFISCAL = p_cd_receita_dfiscal;

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
                          ,v_tise_cd_tipo_servico_mega -- tipo do item
                          ,v_tise_descricao -- descricao da fatura
                          ,1
                          ,v_valor_receita_deal
                          ,v_valor_receita_deal
                          ,con_cod_st_classe
                          ,'S'
                          ,con_cod_usu_inclusao
                          ,con_cos_in_codigo_fatura_int);

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

   -- se o numero de receita_deal_fiscal integrada for igual ao numero de receita_deal_fiscal
   select count(*) 
      into v_count_not_int_receita_df
      from RECEITA_DEAL_FISCAL rdf
      where (rdf.REDF_IN_STATUS != 'I' or rdf.REDF_IN_STATUS is null)
      and rdf.REDF_VL_RECEITA > 0
      and REMO_CD_RECEITA_MOEDA = v_remo_cd_receita_moeda;
   
   if v_count_not_int_receita_df = 0 then
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

 