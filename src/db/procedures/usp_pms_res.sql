CREATE OR REPLACE PROCEDURE USP_PMS_RES (flag number) as
   log_data              date := trunc(sysdate);
--INTERVAL:
   -- FLAG
   -- 1 = COST_RES
   -- 2 = DESP_RES
   -- 99 = TODOS
   -- Procedure de carga
        -- 1 - ORIGEM: COST_RES CATEGORIA: Services CAMPO: valor_tce_admin
        -- 2 - ORIGEM: COST_RES CATEGORIA: Services CAMPO: valor_custo_tce_admin_brl
        -- 3 - ORIGEM: COST_RES CATEGORIA: Services CAMPO: valor_custo_dir_tce_inov_brl
        -- 4 - ORIGEM: COST_RES CATEGORIA: Services CAMPO: valor_custo_ind_tce_inov_brl
        -- 5 - ORIGEM: COST_RES CATEGORIA: Services CAMPO: valor_hora_extra_admin
        -- 6 - ORIGEM: COST_RES CATEGORIA: Services CAMPO: valor_custlic_admin_brl
        -- 7 - ORIGEM: COST_RES CATEGORIA: Services CAMPO: valor_fte_cind_admin
        -- 8 - ORIGEM: DESP_RES CATEGORIA: Travel CAMPO: valor_despesa_admin_brl
        -- 9 - ORIGEM: DESP_RES CATEGORIA: Travel CAMPO: valor_despesa_clob_brl
        -- 10 - ORIGEM: DESP_RES CATEGORIA: Travel CAMPO: valor_despesa_cg_brl
        -- 11 - ORIGEM: DESP_RES CATEGORIA: Contractors CAMPO: valor_despesa_admin_brl
        -- 12 - ORIGEM: DESP_RES CATEGORIA: Contractors CAMPO: valor_despesa_clob_brl
        -- 13 - ORIGEM: DESP_RES CATEGORIA: Contractors CAMPO: valor_despesa_cg_brl
        -- 14 - ORIGEM: DESP_RES CATEGORIA: Others CAMPO: valor_despesa_admin_brl
        -- 15 - ORIGEM: DESP_RES CATEGORIA: Others CAMPO: valor_despesa_clob_brl
        -- 16 - ORIGEM: DESP_RES CATEGORIA: Others CAMPO: valor_despesa_cg_brl
   -- Informac?es para LOG
   log_dt_carga_processo date;
   log_status                varchar2(30) := '';
   log_origem                varchar2(50) := '';
   log_categoria             varchar2(50) := '';
   log_campo                 varchar2(150):= '';
   log_diferenca             number;
   log_descricao             varchar2(200) := '';
   log_sqlerro               varchar2(200) := '';
   nome_proc             varchar2(200) := 'USP_PMS_RES';
   --
   crlf      varchar2(2) := chr(13) || chr(10); -- <ENTER>
   assunto   varchar2(200) := '';
   mensagem  varchar2(7000) := '=== Inicio da Carga ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') || ' ===' ;
   valor_pre integer;
   --VARIAVEIS PARA LOG
      lo_COST_RES_Serv_tceadm integer;
      lo_COST_RES_Serv_custceadm integer;
      lo_COST_RES_Serv_cusdirtceino integer;
      lo_COST_RES_Serv_cusindtceino integer;
      lo_COST_RES_Serv_horextadm integer;
      lo_COST_RES_Serv_cuslicadm integer;
      lo_COST_RES_Serv_ftecinadm integer;
      lo_DESP_RES_Trav_despadm integer;
      lo_DESP_RES_Trav_despclob integer;
      lo_DESP_RES_Trav_despcg integer;
      lo_DESP_RES_Cont_despadm integer;
      lo_DESP_RES_Cont_despclob integer;
      lo_DESP_RES_Cont_despcg integer;
      lo_DESP_RES_Othe_despadm integer;
      lo_DESP_RES_Othe_despclob integer;
      lo_DESP_RES_Othe_despcg integer;
begin
   --------------------------------------------------------
   -- VALIDACAO VW_BI_PMS_RECEITAS
   --------------------------------------------------------
   mensagem := mensagem || crlf || ':: PROCEDURE: ' || nome_proc || ' ::' || crlf || 'FLAG: '||flag || crlf || crlf;
   send_mail('lahumada@cit.com.br', '[PMS][RES] Start Proc', mensagem);
   send_mail('alexandrel@cit.com.br', '[PMS][RES] Start Proc', mensagem);
   send_mail('lnoboru@cit.com.br', '[PMS][RES] Start Proc', mensagem);
      --------------------------------------------
                            select
                                  round(sum(vl_COST_RES_Serv_tceadm),2) vl_COST_RES_Serv_tceadm,
                                  round(sum(vl_COST_RES_Serv_custceadm),2) vl_COST_RES_Serv_custceadm,
                                  round(sum(vl_COST_RES_Serv_cusdirtceino),2) vl_COST_RES_Serv_cusdirtceino,
                                  round(sum(vl_COST_RES_Serv_cusindtceino),2) vl_COST_RES_Serv_cusindtceino,
                                  round(sum(vl_COST_RES_Serv_horextadm),2) vl_COST_RES_Serv_horextadm,
                                  round(sum(vl_COST_RES_Serv_cuslicadm),2) vl_COST_RES_Serv_cuslicadm,
                                  round(sum(vl_COST_RES_Serv_ftecinadm),2) vl_COST_RES_Serv_ftecinadm,
                                  round(sum(vl_DESP_RES_Trav_despadm),2) vl_DESP_RES_Trav_despadm,
                                  round(sum(vl_DESP_RES_Trav_despclob),2) vl_DESP_RES_Trav_despclob,
                                  round(sum(vl_DESP_RES_Trav_despcg),2) vl_DESP_RES_Trav_despcg,
                                  round(sum(vl_DESP_RES_Cont_despadm),2) vl_DESP_RES_Cont_despadm,
                                  round(sum(vl_DESP_RES_Cont_despclob),2) vl_DESP_RES_Cont_despclob,
                                  round(sum(vl_DESP_RES_Cont_despcg),2) vl_DESP_RES_Cont_despcg,
                                  round(sum(vl_DESP_RES_Othe_despadm),2) vl_DESP_RES_Othe_despadm,
                                  round(sum(vl_DESP_RES_Othe_despclob),2) vl_DESP_RES_Othe_despclob,
                                  round(sum(vl_DESP_RES_Othe_despcg),2) vl_DESP_RES_Othe_despcg
                              into
                                    lo_COST_RES_Serv_tceadm,
                                    lo_COST_RES_Serv_custceadm,
                                    lo_COST_RES_Serv_cusdirtceino,
                                    lo_COST_RES_Serv_cusindtceino,
                                    lo_COST_RES_Serv_horextadm,
                                    lo_COST_RES_Serv_cuslicadm,
                                    lo_COST_RES_Serv_ftecinadm,
                                    lo_DESP_RES_Trav_despadm,
                                    lo_DESP_RES_Trav_despclob,
                                    lo_DESP_RES_Trav_despcg,
                                    lo_DESP_RES_Cont_despadm,
                                    lo_DESP_RES_Cont_despclob,
                                    lo_DESP_RES_Cont_despcg,
                                    lo_DESP_RES_Othe_despadm,
                                    lo_DESP_RES_Othe_despclob,
                                    lo_DESP_RES_Othe_despcg
                              from (
                              select
                                        case when s.origem = 'COST_RES' and s.categoria = 'Services' then
                                                           sum(s.valor_tce_admin)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                                                          else 0
                                                     end vl_COST_RES_Serv_tceadm,   --1
                                        case when s.origem = 'COST_RES' and s.categoria = 'Services' then
                                                           sum(s.valor_custo_tce_admin_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                                                          else 0
                                                     end vl_COST_RES_Serv_custceadm, --2
                                        case when s.origem = 'COST_RES' and s.categoria = 'Services' then
                                                           sum(s.valor_custo_dir_tce_inov_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                                                          else 0
                                                     end vl_COST_RES_Serv_cusdirtceino,  --3
                                        case when s.origem = 'COST_RES' and s.categoria = 'Services' then
                                                           sum(s.valor_custo_ind_tce_inov_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                                                          else 0
                                                     end vl_COST_RES_Serv_cusindtceino, --4
                                        case when s.origem = 'COST_RES' and s.categoria = 'Services' then
                                                           sum(s.valor_hora_extra_admin)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                                                          else 0
                                                     end vl_COST_RES_Serv_horextadm, --5
                                        case when s.origem = 'COST_RES' and s.categoria = 'Services' then
                                                           sum(s.valor_custlic_admin_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                                                          else 0
                                                     end vl_COST_RES_Serv_cuslicadm, --6
                                        case when s.origem = 'COST_RES' and s.categoria = 'Services' then
                                                           sum(s.valor_fte_cind_admin)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                                                          else 0
                                                     end vl_COST_RES_Serv_ftecinadm, --7
                                        case when s.origem = 'DESP_RES' and s.categoria = 'Travel' then
                                                           sum(s.valor_despesa_admin_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                                                          else 0
                                                     end vl_DESP_RES_Trav_despadm, --8
                                        case when s.origem = 'DESP_RES' and s.categoria = 'Travel' then
                                                           sum(s.valor_despesa_clob_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                                                          else 0
                                                     end vl_DESP_RES_Trav_despclob, --9
                                        case when s.origem = 'DESP_RES' and s.categoria = 'Travel' then
                                                           sum(s.valor_despesa_cg_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                                                          else 0
                                                     end vl_DESP_RES_Trav_despcg,  --10
                                        case when s.origem = 'DESP_RES' and s.categoria = 'Contractors' then
                                                           sum(s.valor_despesa_admin_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                                                          else 0
                                                     end vl_DESP_RES_Cont_despadm, --11
                                        case when s.origem = 'DESP_RES' and s.categoria = 'Contractors' then
                                                           sum(s.valor_despesa_clob_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                                                          else 0
                                                     end vl_DESP_RES_Cont_despclob,  --12
                                        case when s.origem = 'DESP_RES' and s.categoria = 'Contractors' then
                                                           sum(s.valor_despesa_cg_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                                                          else 0
                                                     end vl_DESP_RES_Cont_despcg,  --13
                                        case when s.origem = 'DESP_RES' and s.categoria = 'Others' then
                                                           sum(s.valor_despesa_admin_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                                                          else 0
                                                     end vl_DESP_RES_Othe_despadm, --14
                                        case when s.origem = 'DESP_RES' and s.categoria = 'Others' then
                                                           sum(s.valor_despesa_clob_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                                                          else 0
                                                     end vl_DESP_RES_Othe_despclob, --15
                                        case when s.origem = 'DESP_RES' and s.categoria = 'Others' then
                                                           sum(s.valor_despesa_cg_brl)over (partition by s.origem, s.categoria order by s.origem, s.categoria)
                                                          else 0
                                                     end vl_DESP_RES_Othe_despcg,   --16
                                                  row_number() over (partition by s.origem, s.categoria order by s.origem, s.categoria) nlinha
                              from (
                                                   select 'Q6' fonte,
                                                          inf.origem,
                                                          inf.cd_contrato_pratica,
                                                          inf.nome_contrato_pratica,
                                                          inf.dt_mes,
                                                          inf.umkt,
                                                          inf.sso,
                                                          inf.lob,
                                                          inf.bm,
                                                          --
                                                          inf.categoria,
                                                          inf.moeda_custo,
                                                          inf.pratica,
                                                          inf.login,
                                                          inf.login_mgr,
                                                          --
                                                          inf.cd_grupo_custo,
                                                          inf.nome_grupo_custo,
                                                          inf.nome_area,
                                                          inf.cont_cd_contrato,
                                                          inf.clie_cd_cliente,
                                                          --
                                                          -----------------------------------------------------------------------------------------------
                                                          -- Valores Receita
                                                          -----------------------------------------------------------------------------------------------
                                                          cd_moeda_revenue,
                                                          ' ' aloc_in_estagio,
                                                          valor_receita_currency,
                                                          valor_receita_brl,
                                                          0 valor_receita_usd,
                                                          valor_fte_revenue,
                                                          valor_cotacao_revenue,
                                                          0 valor_cotacao_usd,
                                                          valor_ut_rate,
                                                          valor_fte_ut_rate,
                                                          -----------------------------------------------------------------------------------------------
                                                          -- Valores Impostos
                                                          -----------------------------------------------------------------------------------------------
                                                          valor_imposto_original,
                                                          valor_imposto_convertido,
                                                          0 valor_imposto_usd,
                                                          0 valor_cotacao_usd_imp,
                                                          ---------------------------------------------------------
                                                          -- Receita de recursos emprestados - Contratos
                                                          ---------------------------------------------------------
                                                          cd_moeda_rev_out_sourc,
                                                          valor_rev_out_sourc,
                                                          valor_rev_out_sourc_brl,
                                                          0 valor_rev_out_sourc_usd,
                                                          valor_cotacao_out_source,
                                                          ---------------------------------------------------------
                                                          -- Receita de recursos emprestados - Recursos
                                                          ---------------------------------------------------------
                                                          cd_moeda_rev_out_alloc,
                                                          valor_rev_out_alloc,
                                                          valor_rev_out_alloc_brl,
                                                          0 valor_rev_out_alloc_usd,
                                                          0 valor_cotacao_out_alloc,
                                                          0 valor_cotacao_usd_emp_rec,
                                                          -----------------------------------------------------------------------------------------------
                                                          -- Despesa Realizada
                                                          -----------------------------------------------------------------------------------------------
                                                          valor_desp_rel_curr,
                                                          valor_desp_rel_brl,
                                                          0 valor_desp_rel_usd,
                                                          0 valor_cota_desp_rel_usd,
                                                          -----------------------------------------------------------------------------------------------
                                                          -- Despesas Fixas por UMKT
                                                          -----------------------------------------------------------------------------------------------
                                                          valor_despfix_ratumkt_curr,
                                                          valor_despfix_ratumkt_brl,
                                                          0 valor_despfix_ratumkt_usd,
                                                          0 vlr_cota_despfix_ratumkt_usd,
                                                          -----------------------------------------------------------------------------------------------
                                                          -- Despesas Fixas por Pratica
                                                          -----------------------------------------------------------------------------------------------
                                                          vlr_despfix_lob_curr,
                                                          vlr_despfix_lob_brl,
                                                          0 vlr_despfix_lob_usd,
                                                          0 vlr_cota_despfix_lob_usd,
                                                          -----------------------------------------------------------------------------------------------
                                                          -- Despesas Contabeis - Comercial
                                                          -----------------------------------------------------------------------------------------------
                                                          vlr_desp_contab_inter,
                                                          vlr_desp_contab_brl,
                                                          -----------------------------------------------------------------------------------------------
                                                          -- Despesas Contabeis - Qualidade
                                                          -----------------------------------------------------------------------------------------------
                                                          vlr_quality_costs_brl,
                                                          0 vlr_quality_costs_usd,
                                                          0 valor_cota_quali_costs_usd,
                                                          -----------------------------------------------------------------------------------------------
                                                          -- Despesa Planejada
                                                          -----------------------------------------------------------------------------------------------
                                                          valor_desp_plan_curr,
                                                          valor_desp_plan_brl,
                                                          0 valor_desp_plan_usd,
                                                          0 valor_cota_desp_plan_usd,
                                                          tipo_licenca,
                                                          -----------------------------------------------------------------------------------------------
                                                          -- Custos Diretos
                                                          -----------------------------------------------------------------------------------------------
                                                          cd_moeda_cdir,
                                                          valor_custo_dir_tce_curr,
                                                          valor_custo_dir_tce_brl,
                                                          0 valor_custo_dir_tce_usd,
                                                          valor_custo_dir_infra_curr,
                                                          valor_custo_dir_infra_brl,
                                                          0 valor_custo_dir_infra_usd,
                                                          valor_fte_cdir,
                                                          valor_fte_ferias_cdir,
                                                          valor_fte_licenca,
                                                          valor_cotacao_cdir,
                                                          0 valor_cotacao_cdir_usd,
                                                          -----------------------------------------------------------------------------------------------
                                                          -- Custos Indiretos
                                                          -----------------------------------------------------------------------------------------------
                                                          cd_moeda_cind,
                                                          valor_custo_ind_tce_curr,
                                                          valor_custo_ind_tce_brl,
                                                          0 valor_custo_ind_tce_usd,
                                                          valor_custo_ind_infra_curr,
                                                          valor_custo_ind_infra_brl,
                                                          0 valor_custo_ind_infra_usd,
                                                          valor_fte_cind,
                                                          valor_fte_ferias_cind,
                                                          valor_cotacao_cind,
                                                          0 valor_cotacao_cind_usd,
                                                          vlr_vacation_fte,
                                                          -----------------------------------------------------------------------------------------------
                                                          -- IT Charge Back
                                                          -----------------------------------------------------------------------------------------------
                                                          cd_ti_recurso,
                                                          nome_ti_recurso,
                                                          valor_custo_unit_ti_rec,
                                                          quant_ti_recursos,
                                                          valor_custo_item,
                                                          0 valor_custo_item_usd,
                                                          0 valor_cotacao_itchbk_usd,
                                                          ---------------------------------------------------------
                                                          -- Custo Licenca - UMKT
                                                          ---------------------------------------------------------
                                                          cd_moeda_custlic_ratumkt,
                                                          valor_custlic_rateio_umkt,
                                                          valor_custlic_rateio_umkt_brl,
                                                          0 valor_custlic_rateio_umkt_usd,
                                                          valor_cota_custlic_ratumkt,
                                                          0 valor_cota_custlic_ratumkt_usd,
                                                          pr_aloc_licenca_umkt,
                                                          ---------------------------------------------------------
                                                          -- Custo Licenca - Pratica
                                                          ---------------------------------------------------------
                                                          cd_moeda_custlic_ratprat,
                                                          valor_custlic_rateio_prat,
                                                          valor_custlic_ratprat_brl,
                                                          0 valor_custlic_ratprat_usd,
                                                          valor_cota_custlic_ratprat,
                                                          0 valor_cota_custlic_ratprat_usd,
                                                          pr_aloc_licenca_lob,
                                                          -----------------------------------------------------------------------------------------------
                                                          -- Hora Extra / Plant?o
                                                          -----------------------------------------------------------------------------------------------
                                                          valor_hora_extra,
                                                          0 valor_hora_extra_usd,
                                                          valor_plantao,
                                                          0 valor_plantao_usd,
                                                          0 valor_cota_hextpl_usd,
                                                          ---------------------------------------------------------
                                                          -- Custo de recursos emprestados - Contratos
                                                          ---------------------------------------------------------
                                                          cd_moeda_custo_out_sourc,
                                                          valor_custo_out_sourc,
                                                          valor_custo_out_sourc_brl,
                                                          0 valor_custo_out_sourc_usd,
                                                          valor_cota_custo_out_sourc,
                                                          0 valor_cota_custo_out_sourc_usd,
                                                          ---------------------------------------------------------
                                                          -- Custo de recursos emprestados - Recursos
                                                          ---------------------------------------------------------
                                                          cd_moeda_custo_out_alloc,
                                                          valor_custo_out_aloc,
                                                          valor_custo_out_alloc_brl,
                                                          0 valor_custo_out_alloc_usd,
                                                          valor_cota_custo_out_alloc,
                                                          0 valor_cota_custo_out_alloc_usd,
                                                          ---------------------------------------------------------
                                                          -- LOB Fixed Costs - rateio por UMKT
                                                          ---------------------------------------------------------
                                                          cd_moeda_lob_fixcost,
                                                          vlr_cust_rat_lob_fixcost_brl,
                                                          perc_receita_lob_fixcost,
                                                          vlr_rec_lob_fixcost_curr,
                                                          vlr_rec_lob_fixcost_brl,
                                                          vlr_rec_lob_fixcost_usd,
                                                          vlr_rec_clob_lobfixcost_curr,
                                                          vlr_rec_clob_lobfixcost_brl,
                                                          vlr_rec_clob_lobfixcost_usd,
                                                          vlr_tot_fte_lob_fixcost,
                                                          ---------------------------------------------------------
                                                          -- Custo Fixo - Pratica - sem alocacao
                                                          ---------------------------------------------------------
                                                          cd_moeda_custfix_ratprat,
                                                          valor_custfix_rateio_prat,
                                                          valor_tce_custfix_ratprat,
                                                          valor_custinfra_fix_ratprat,
                                                          valor_custfix_ratprat_brl,
                                                          0 valor_custfix_ratprat_usd,
                                                          valor_tce_custfix_ratprat_brl,
                                                          0 valor_tce_custfix_ratprat_usd,
                                                          valor_cusinfra_fix_ratprat_brl,
                                                          0 valor_cusinfra_fix_ratprat_usd,
                                                          valor_custfix_lob_ferias,
                                                          valor_fte_custfix_ratprat,
                                                          vlr_fte_cusfix_ferias_ratprat,
                                                          valor_cota_custfix_ratprat,
                                                          0 valor_cota_custfix_ratprat_usd,
                                                          ---------------------------------------------------------
                                                          -- Custo Fixo - UMKT
                                                          ---------------------------------------------------------
                                                          cd_moeda_custfix_ratumkt,
                                                          valor_custfix_rateio_umkt,
                                                          valor_tce_custfix_ratumkt,
                                                          valor_custinfra_fix_ratumkt,
                                                          valor_custfix_rateio_umkt_brl,
                                                          0 valor_custfix_rateio_umkt_usd,
                                                          valor_tce_custfix_ratumkt_brl,
                                                          0 valor_tce_custfix_ratumkt_usd,
                                                          valor_custinf_fix_ratumkt_brl,
                                                          0 valor_custinf_fix_ratumkt_usd,
                                                          valor_custfix_umkt_ferias,
                                                          valor_fte_custfix_ratumkt,
                                                          vlr_fte_cusfix_ferias_ratumkt,
                                                          valor_cota_custfix_ratumkt,
                                                          0 valor_cota_custfix_ratumkt_usd,
                                                          ------------------------
                                                          -- Technical Sales - Custo Direto
                                                          ------------------------
                                                          vlr_tecsal_cdir_tce_curr,
                                                          vlr_tecsal_cdir_tce_brl,
                                                          0 vlr_tecsal_cdir_tce_usd,
                                                          vlr_tecsal_cdir_infra_curr,
                                                          vlr_tecsal_cdir_infra_brl,
                                                          0 vlr_tecsal_cdir_infra_usd,
                                                          ------------------------
                                                          -- Technical Sales - Custo Indireto
                                                          ------------------------
                                                          vlr_tecsal_cind_tce_curr,
                                                          vlr_tecsal_cind_tce_brl,
                                                          0 vlr_tecsal_cind_tce_usd,
                                                          vlr_tecsal_cind_infra_curr,
                                                          vlr_tecsal_cind_infra_brl,
                                                          0 vlr_tecsal_cind_infra_usd,
                                                          ------------------------
                                                          -- Technical Sales - Ferias
                                                          ------------------------
                                                          tecsal_pr_ferias,
                                                          ------------------------
                                                          -- Technical Sales - Custo Licencas por UMKT
                                                          ------------------------
                                                          vlr_tecsal_custlic_curr,
                                                          vlr_tecsal_custlic_brl,
                                                          0 vlr_tecsal_custlic_usd,
                                                          tecsal_pr_aloc_licenca,
                                                          ------------------------
                                                          -- Technical Sales - Horas Extras / Plant?o
                                                          ------------------------
                                                          vlr_tecsal_hora_extra,
                                                          0 vlr_tecsal_hora_extra_usd,
                                                          vlr_tecsal_plantao,
                                                          0 vlr_tecsal_plantao_usd,
                                                          ------------------------
                                                          -- Technical Sales - Despesas Realizadas
                                                          ------------------------
                                                          vlr_tecsal_desp_curr,
                                                          vlr_tecsal_desp_brl,
                                                          0 vlr_tecsal_desp_usd,
                                                          --
                                                          0 vlr_cota_tecsal_usd,
                                                          -----------------------------------------------------------------------------------------------
                                                          -- Comissoes
                                                          -----------------------------------------------------------------------------------------------
                                                          -- tipo comissao acelerador
                                                          vl_comiss_acel_curr,
                                                          vl_comiss_acel_brl,
                                                          0 vl_comiss_acel_usd,
                                                          -- tipo comissao invoice
                                                          vl_comiss_inv_curr,
                                                          vl_comiss_inv_brl,
                                                          0 vl_comiss_inv_usd,
                                                          --
                                                          0 vl_cota_comiss_inv_usd,
                                                          ---------------------------------------------
                                                          -- TCE - Marketing, DN, AE - Rateio
                                                          -----------------------------------------------
                                                          vlr_custrat_mkt_curr,
                                                          vlr_custrat_mkt_brl,
                                                          0 vlr_custrat_mkt_usd,
                                                          vlr_custrat_dn_curr,
                                                          vlr_custrat_dn_brl,
                                                          0 vlr_custrat_dn_usd,
                                                          vlr_custrat_ae_curr,
                                                          vlr_custrat_ae_brl,
                                                          0 vlr_custrat_ae_usd,
                                                          0 vlr_cota_tce_mkt_dn_ae_rat,
                                                          ------------------------------------------------
                                                          -- TCE - custos fixos MKT / DN / AE
                                                          -----------------------------------------------
                                                          vlr_cust_colab_mkt_curr,
                                                          vlr_cust_colab_mkt_brl,
                                                          0 vlr_cust_colab_mkt_usd,
                                                          vlr_cust_colab_dn_curr,
                                                          vlr_cust_colab_dn_brl,
                                                          0 vlr_cust_colab_dn_usd,
                                                          vlr_cust_colab_ae_curr,
                                                          vlr_cust_colab_ae_brl,
                                                          0 vlr_cust_colab_ae_usd,
                                                          0 valor_cota_tce_mkt_dn_ae_usd,
                                                          ---------------------------------------------------
                                                          -- Despesas Travel / Others -> Rateio
                                                          --------------------------------------------------
                                                          vlr_desprat_mkt_trav_curr,
                                                          valor_desprat_dn_trav_curr,
                                                          valor_desprat_ae_trav_curr,
                                                          vlr_desprat_mkt_oth_curr,
                                                          valor_desprat_dn_oth_curr,
                                                          valor_desprat_ae_oth_curr,
                                                          --
                                                          vlr_desprat_mkt_trav_brl,
                                                          valor_desprat_dn_trav_brl,
                                                          valor_desprat_ae_trav_brl,
                                                          vlr_desprat_mkt_oth_brl,
                                                          valor_desprat_dn_oth_brl,
                                                          valor_desprat_ae_oth_brl,
                                                          --
                                                          0 valor_desprat_mkt_trav_usd,
                                                          0 valor_desprat_dn_trav_usd,
                                                          0 valor_desprat_ae_trav_usd,
                                                          0 vlr_desprat_mkt_oth_usd,
                                                          0 valor_desprat_dn_oth_usd,
                                                          0 valor_desprat_ae_oth_usd,
                                                          0 valor_cota_desp_rateio_usd,
                                                          -------------------------------------------------
                                                          -- Despesas Travel / Others -> Diretas
                                                          ------------------------------------------------
                                                          vlr_desp_mkt_travel_curr,
                                                          vlr_desp_mkt_travel_brl,
                                                          0 vlr_desp_mkt_travel_usd,
                                                          vlr_desp_mkt_others_curr,
                                                          vlr_desp_mkt_others_brl,
                                                          0 vlr_desp_mkt_others_usd,
                                                          vlr_desp_dn_travel_curr,
                                                          vlr_desp_dn_travel_brl,
                                                          0 vlr_desp_dn_travel_usd,
                                                          vlr_desp_dn_others_curr,
                                                          vlr_desp_dn_others_brl,
                                                          0 vlr_desp_dn_others_usd,
                                                          vlr_desp_ae_travel_curr,
                                                          vlr_desp_ae_travel_brl,
                                                          0 vlr_desp_ae_travel_usd,
                                                          vlr_desp_ae_others_curr,
                                                          vlr_desp_ae_others_brl,
                                                          0 vlr_desp_ae_others_usd,
                                                          0 valor_cota_desp_direta_usd,
                                                          --------------------------------------------------------------------------------------------
                                                          -- Informac?es RES
                                                          --------------------------------------------------------------------------------------------
                                                          inf.valor_tce_admin,
                                                          inf.valor_cinfra_base_admin,
                                                          inf.valor_custo_tce_admin_cur,
                                                          inf.valor_custo_tce_admin_brl,
                                                          case when inf.moeda_custo = 'USD'
                                                                    then inf.valor_custo_tce_admin_cur
                                                                    else ( inf.valor_custo_tce_admin_brl / moed.valor_cotacao ) end valor_custo_tce_admin_usd,
                                                          inf.valor_custo_infra_admin_cur,
                                                          inf.valor_custo_infra_admin_brl,
                                                          case when inf.moeda_custo = 'USD'
                                                                    then inf.valor_custo_infra_admin_cur
                                                                    else ( inf.valor_custo_infra_admin_brl / moed.valor_cotacao ) end valor_custo_infra_admin_usd,
                                                          inf.pct_custo_admin,
                                                          inf.valor_custo_dir_tce_inov_curr,
                                                          inf.valor_custo_dir_tce_inov_brl,
                                                          case when inf.moeda_custo = 'USD'
                                                                    then inf.valor_custo_dir_tce_inov_curr
                                                                    else ( inf.valor_custo_dir_tce_inov_brl / moed.valor_cotacao ) end valor_custo_dir_tce_inov_usd,
                                                          inf.valor_cdir_infra_inov_curr,
                                                          inf.valor_cdir_infra_inov_brl,
                                                          case when inf.moeda_custo = 'USD'
                                                                    then inf.valor_cdir_infra_inov_curr
                                                                    else ( inf.valor_cdir_infra_inov_brl / moed.valor_cotacao ) end valor_cdir_infra_inov_usd,
                                                          inf.valor_custo_ind_tce_inov_curr,
                                                          inf.valor_custo_ind_tce_inov_brl,
                                                          case when inf.moeda_custo = 'USD'
                                                                    then inf.valor_custo_ind_tce_inov_curr
                                                                    else ( inf.valor_custo_ind_tce_inov_brl / moed.valor_cotacao ) end valor_custo_ind_tce_inov_usd,
                                                          inf.valor_cind_infra_inov_curr,
                                                          inf.valor_cind_infra_inov_brl,
                                                          case when inf.moeda_custo = 'USD'
                                                                    then inf.valor_cind_infra_inov_curr
                                                                    else ( inf.valor_cind_infra_inov_brl / moed.valor_cotacao ) end valor_cind_infra_inov_usd,
                                                          inf.valor_hora_extra_admin,
                                                          ( inf.valor_hora_extra_admin / moed.valor_cotacao ) valor_hora_extra_admin_usd,
                                                          inf.valor_plantao_admin,
                                                          ( inf.valor_plantao_admin / moed.valor_cotacao ) valor_plantao_admin_usd,
                                                          inf.valor_custlic_admin_curr,
                                                          inf.valor_custlic_admin_brl,
                                                          case when inf.moeda_custo = 'USD'
                                                                    then inf.valor_custlic_admin_curr
                                                                    else ( inf.valor_custlic_admin_brl / moed.valor_cotacao ) end valor_custlic_admin_usd,
                                                          inf.valor_pct_ferias_admin,
                                                          inf.valor_pct_licenca_admin,
                                                          inf.valor_pct_aloc_inov_admin,
                                                          inf.valor_pct_aloc_ninov_admin,
                                                          inf.valor_fte_cind_admin,
                                                          inf.valor_despesa_admin_curr,
                                                          inf.valor_despesa_admin_brl,
                                                          case when inf.moeda_custo = 'USD'
                                                                    then inf.valor_despesa_admin_curr
                                                                    else ( inf.valor_despesa_admin_brl / moed.valor_cotacao ) end valor_despesa_admin_usd,
                                                          inf.valor_despesa_clob_curr,
                                                          inf.valor_despesa_cg_curr,
                                                          inf.valor_despesa_clob_brl,
                                                          case when inf.moeda_custo = 'USD'
                                                                    then inf.valor_despesa_clob_curr
                                                                    else ( inf.valor_despesa_clob_brl / moed.valor_cotacao ) end valor_despesa_clob_usd,
                                                          inf.valor_despesa_cg_brl,
                                                          case when inf.moeda_custo = 'USD'
                                                                    then inf.valor_despesa_cg_curr
                                                                    else ( inf.valor_despesa_cg_brl / moed.valor_cotacao ) end valor_despesa_cg_usd,
                                                          case when origem not like '%RES'
                                                                    then 0
                                                                    else moed.valor_cotacao end valor_cotacao_res_usd--,
                                                          --
                                                   --       0 cd_tipo_contrato,
                                                   --       '' nome_tipo_contrato,
                                                   --       '' flag_exec
                                                   from ( select moed_cd_moeda,
                                                                 moeda,
                                                                 ------------------------------------------------------------------------------------------------------------------
                                                                 -- Transforma o resultado da coluna MES_ANO, numa coluna do tipo DATE, para facilitar comparac?es posteriores.
                                                                 ------------------------------------------------------------------------------------------------------------------
                                                                 to_date( '01/' || to_char( como_dt_dia, 'MM/YYYY'), 'DD/MM/YYYY') mes_cotacao,
                                                                 valor_cotacao,
                                                                 como_in_tipo
                                                          from ( select cm.moed_cd_moeda,
                                                                        m.moed_sg_moeda moeda,
                                                                        cm.como_dt_dia,
                                                                        cm.como_in_tipo,
                                                                        cm.como_vl_cotacao valor_cotacao,
                                                                        -----------------------------------------------------------------------------------------------
                                                                        -- Func?o que retorna o ultimo dia de cada mes, cuja cotac?o deve ser utilizado quando o mes
                                                                        -- ja esteja fechado.
                                                                        ------------------------------------------------------------------------------------------------
                                                                        max( cm.como_dt_dia ) over( partition by cm.moed_cd_moeda, to_date( '01/' || to_char( cm.como_dt_dia, 'MM/YYYY'), 'DD/MM/YYYY') ) max_dt_dia_moeda
                                                                 from cotacao_moeda cm,
                                                                      moeda m
                                                                 where cm.moed_cd_moeda = m.moed_cd_moeda and
                                                                       m.moed_cd_moeda  = 2 and
                                                                       cm.como_in_tipo = -------------------------------------------------------------------------------------------
                                                                                         -- Se o dia/mes que esta sendo verificado for menor que a data atual, a cotac?o utilizada
                                                                                         -- deve ser do tipo "REALIZADO", sen?o, deve ser do tipo "PLANEJADO".
                                                                                         -------------------------------------------------------------------------------------------
                                                                                         case when to_date( '01/' || to_char( cm.como_dt_dia, 'MM/YYYY'), 'DD/MM/YYYY') <= to_date( '01/' || to_char( trunc(sysdate), 'MM/YYYY'), 'DD/MM/YYYY' )
                                                                                                   then 'R'
                                                                                                   else 'P' end )
                                                          where ----------------------------------------------------------------------------
                                                                -- Condic?o que garante que a cotac?o do ultimo dia do mes seja retornada
                                                                ----------------------------------------------------------------------------
                                                                como_dt_dia = max_dt_dia_moeda
                                                          order by 1, 2 ) moed,
                                                       (  select inf.fonte origem,
                                                                 inf.cd_contrato_pratica,
                                                                 inf.nome_contrato_pratica,
                                                                 inf.dt_mes,
                                                                 inf.umkt,
                                                                 inf.sso,
                                                                 inf.lob,
                                                                 inf.bm,
                                                                 --
                                                                 inf.categoria,
                                                                 inf.moeda_custo,
                                                                 inf.lob pratica,
                                                                 inf.login,
                                                                 pe.pess_cd_login_mgr login_mgr,
                                                                 --
                                                                 inf.cd_grupo_custo,
                                                                 inf.nome_grupo_custo,
                                                                 inf.nome_area,
                                                                 co.cont_cd_contrato,
                                                                 cli.clie_cd_cliente,
                                                                 --
                                                                 -----------------------------------------------------------------------------------------------
                                                                 -- Valores Receita
                                                                 -----------------------------------------------------------------------------------------------
                                                                 0 cd_moeda_revenue,
                                                                 0 valor_receita_currency,
                                                                 0 valor_receita_brl,
                                                                 0 valor_fte_revenue,
                                                                 0 valor_cotacao_revenue,
                                                                 0 valor_ut_rate,
                                                                 0 valor_fte_ut_rate,
                                                                 -----------------------------------------------------------------------------------------------
                                                                 -- Valores Impostos
                                                                 -----------------------------------------------------------------------------------------------
                                                                 0 valor_imposto_original,
                                                                 0 valor_imposto_convertido,
                                                                 ---------------------------------------------------------
                                                                 -- Receita de recursos emprestados - Contratos
                                                                 ---------------------------------------------------------
                                                                 0 cd_moeda_rev_out_sourc,
                                                                 0 valor_rev_out_sourc,
                                                                 0 valor_rev_out_sourc_brl,
                                                                 0 valor_cotacao_out_source,
                                                                 ---------------------------------------------------------
                                                                 -- Receita de recursos emprestados - Recursos
                                                                 ---------------------------------------------------------
                                                                 0 cd_moeda_rev_out_alloc,
                                                                 0 valor_rev_out_alloc,
                                                                 0 valor_rev_out_alloc_brl,
                                                                 0 valor_cotacao_out_alloc,
                                                                 -----------------------------------------------------------------------------------------------
                                                                 -- Despesa Realizada
                                                                 -----------------------------------------------------------------------------------------------
                                                                 0 valor_desp_rel_curr,
                                                                 0 valor_desp_rel_brl,
                                                                 -----------------------------------------------------------------------------------------------
                                                                 -- Despesas Fixas por UMKT
                                                                 -----------------------------------------------------------------------------------------------
                                                                 0 valor_despfix_ratumkt_curr,
                                                                 0 valor_despfix_ratumkt_brl,
                                                                 -----------------------------------------------------------------------------------------------
                                                                 -- Despesas Fixas por Pratica
                                                                 -----------------------------------------------------------------------------------------------
                                                                 0 vlr_despfix_lob_curr,
                                                                 0 vlr_despfix_lob_brl,
                                                                 -----------------------------------------------------------------------------------------------
                                                                 -- Despesas Contabeis - Comercial
                                                                 -----------------------------------------------------------------------------------------------
                                                                 0 vlr_desp_contab_inter,
                                                                 0 vlr_desp_contab_brl,
                                                                 -----------------------------------------------------------------------------------------------
                                                                 -- Despesas Contabeis - Qualidade
                                                                 -----------------------------------------------------------------------------------------------
                                                                 0 vlr_quality_costs_brl,
                                                                 -----------------------------------------------------------------------------------------------
                                                                 -- Despesa Planejada
                                                                 -----------------------------------------------------------------------------------------------
                                                                 0 valor_desp_plan_curr,
                                                                 0 valor_desp_plan_brl,
                                                                 '' tipo_licenca,
                                                                 -----------------------------------------------------------------------------------------------
                                                                 -- Custos Diretos
                                                                 -----------------------------------------------------------------------------------------------
                                                                 0 cd_moeda_cdir,
                                                                 --
                                                                 0 valor_custo_dir_tce_curr,
                                                                 0 valor_custo_dir_tce_brl,
                                                                 0 valor_custo_dir_infra_curr,
                                                                 0 valor_custo_dir_infra_brl,
                                                                 0 valor_fte_cdir,
                                                                 0 valor_fte_ferias_cdir,
                                                                 0 valor_fte_licenca,
                                                                 0 valor_cotacao_cdir,
                                                                 -----------------------------------------------------------------------------------------------
                                                                 -- Custos Indiretos
                                                                 -----------------------------------------------------------------------------------------------
                                                                 0 cd_moeda_cind,
                                                                 --
                                                                 0 valor_custo_ind_tce_curr,
                                                                 0 valor_custo_ind_tce_brl,
                                                                 0 valor_custo_ind_infra_curr,
                                                                 0 valor_custo_ind_infra_brl,
                                                                 0 valor_fte_cind,
                                                                 0 valor_fte_ferias_cind,
                                                                 0 valor_cotacao_cind,
                                                                 0 vlr_vacation_fte,
                                                                 -----------------------------------------------------------------------------------------------
                                                                 -- IT Charge Back
                                                                 -----------------------------------------------------------------------------------------------
                                                                 0 cd_ti_recurso,
                                                                 '' nome_ti_recurso,
                                                                 0 valor_custo_unit_ti_rec,
                                                                 0 quant_ti_recursos,
                                                                 0 valor_custo_item,
                                                                 ---------------------------------------------------------
                                                                 -- Custo Licenca - UMKT
                                                                 ---------------------------------------------------------
                                                                 0 cd_moeda_custlic_ratumkt,
                                                                 --
                                                                 0 valor_custlic_rateio_umkt,
                                                                 0 valor_custlic_rateio_umkt_brl,
                                                                 0 valor_cota_custlic_ratumkt,
                                                                 0 pr_aloc_licenca_umkt,
                                                                 ---------------------------------------------------------
                                                                 -- Custo Licenca - Pratica
                                                                 ---------------------------------------------------------
                                                                 0 cd_moeda_custlic_ratprat,
                                                                 --
                                                                 0 valor_custlic_rateio_prat,
                                                                 0 valor_custlic_ratprat_brl,
                                                                 0 valor_cota_custlic_ratprat,
                                                                 0 pr_aloc_licenca_lob,
                                                                 -----------------------------------------------------------------------------------------------
                                                                 -- Hora Extra / Plant?o
                                                                 -----------------------------------------------------------------------------------------------
                                                                 0 valor_hora_extra,
                                                                 0 valor_plantao,
                                                                 ---------------------------------------------------------
                                                                 -- Custo de recursos emprestados - Contratos
                                                                 ---------------------------------------------------------
                                                                 0 cd_moeda_custo_out_sourc,
                                                                 --
                                                                 0 valor_custo_out_sourc,
                                                                 0 valor_custo_out_sourc_brl,
                                                                 0 valor_cota_custo_out_sourc,
                                                                 ---------------------------------------------------------
                                                                 -- Custo de recursos emprestados - Recursos
                                                                 ---------------------------------------------------------
                                                                 0 cd_moeda_custo_out_alloc,
                                                                 --
                                                                 0 valor_custo_out_aloc,
                                                                 0 valor_custo_out_alloc_brl,
                                                                 0 valor_cota_custo_out_alloc,
                                                                 ---------------------------------------------------------
                                                                 -- LOB Fixed Costs - rateio por UMKT
                                                                 ---------------------------------------------------------
                                                                 0 cd_moeda_lob_fixcost,
                                                                 --
                                                                 0 vlr_cust_rat_lob_fixcost_brl,
                                                                 0 perc_receita_lob_fixcost,
                                                                 0 vlr_rec_lob_fixcost_curr,
                                                                 0 vlr_rec_lob_fixcost_brl,
                                                                 0 vlr_rec_lob_fixcost_usd,
                                                                 0 vlr_rec_clob_lobfixcost_curr,
                                                                 0 vlr_rec_clob_lobfixcost_brl,
                                                                 0 vlr_rec_clob_lobfixcost_usd,
                                                                 0 vlr_tot_fte_lob_fixcost,
                                                                 ---------------------------------------------------------
                                                                 -- Custo Fixo - Pratica - sem alocacao
                                                                 ---------------------------------------------------------
                                                                 0 cd_moeda_custfix_ratprat,
                                                                 --
                                                                 0 valor_custfix_rateio_prat,
                                                                 0 valor_tce_custfix_ratprat,
                                                                 0 valor_custinfra_fix_ratprat,
                                                                 0 valor_custfix_ratprat_brl,
                                                                 0 valor_tce_custfix_ratprat_brl,
                                                                 0 valor_cusinfra_fix_ratprat_brl,
                                                                 0 valor_custfix_lob_ferias,
                                                                 0 valor_fte_custfix_ratprat,
                                                                 0 vlr_fte_cusfix_ferias_ratprat,
                                                                 0 valor_cota_custfix_ratprat,
                                                                 ---------------------------------------------------------
                                                                 -- Custo Fixo - UMKT
                                                                 ---------------------------------------------------------
                                                                 0 cd_moeda_custfix_ratumkt,
                                                                 --
                                                                 0 valor_custfix_rateio_umkt,
                                                                 0 valor_tce_custfix_ratumkt,
                                                                 0 valor_custinfra_fix_ratumkt,
                                                                 0 valor_custfix_rateio_umkt_brl,
                                                                 0 valor_tce_custfix_ratumkt_brl,
                                                                 0 valor_custinf_fix_ratumkt_brl,
                                                                 0 valor_custfix_umkt_ferias,
                                                                 0 valor_fte_custfix_ratumkt,
                                                                 0 vlr_fte_cusfix_ferias_ratumkt,
                                                                 0 valor_cota_custfix_ratumkt,
                                                                 ------------------------
                                                                 -- Technical Sales - Custo Direto
                                                                 ------------------------
                                                                 0 vlr_tecsal_cdir_tce_curr,
                                                                 0 vlr_tecsal_cdir_tce_brl,
                                                                 0 vlr_tecsal_cdir_infra_curr,
                                                                 0 vlr_tecsal_cdir_infra_brl,
                                                                 ------------------------
                                                                 -- Technical Sales - Custo Indireto
                                                                 ------------------------
                                                                 0 vlr_tecsal_cind_tce_curr,
                                                                 0 vlr_tecsal_cind_tce_brl,
                                                                 0 vlr_tecsal_cind_infra_curr,
                                                                 0 vlr_tecsal_cind_infra_brl,
                                                                 ------------------------
                                                                 -- Technical Sales - Ferias
                                                                 ------------------------
                                                                 0 tecsal_pr_ferias,
                                                                 ------------------------
                                                                 -- Technical Sales - Custo Licencas por UMKT
                                                                 ------------------------
                                                                 0 vlr_tecsal_custlic_curr,
                                                                 0 vlr_tecsal_custlic_brl,
                                                                 0 tecsal_pr_aloc_licenca,
                                                                 ------------------------
                                                                 -- Technical Sales - Horas Extras / Plant?o
                                                                 ------------------------
                                                                 0 vlr_tecsal_hora_extra,
                                                                 0 vlr_tecsal_plantao,
                                                                 ------------------------
                                                                 -- Technical Sales - Despesas Realizadas
                                                                 ------------------------
                                                                 0 vlr_tecsal_desp_curr,
                                                                 0 vlr_tecsal_desp_brl,
                                                                 -----------------------------------------------------------------------------------------------
                                                                 -- Comissoes
                                                                 -----------------------------------------------------------------------------------------------
                                                                 -- tipo comissao acelerador
                                                                 0 vl_comiss_acel_curr,
                                                                 0 vl_comiss_acel_brl,
                                                                 -- tipo comissao invoice
                                                                 0 vl_comiss_inv_curr,
                                                                 0 vl_comiss_inv_brl,
                                                                 ---------------------------------------------
                                                                 -- TCE - Marketing, DN, AE - Rateio
                                                                 -----------------------------------------------
                                                                 0 vlr_custrat_mkt_curr,
                                                                 0 vlr_custrat_mkt_brl,
                                                                 0 vlr_custrat_dn_curr,
                                                                 0 vlr_custrat_dn_brl,
                                                                 0 vlr_custrat_ae_curr,
                                                                 0 vlr_custrat_ae_brl,
                                                                 ------------------------------------------------
                                                                 -- TCE - custos fixos MKT / DN / AE
                                                                 -----------------------------------------------
                                                                 0 vlr_cust_colab_mkt_curr,
                                                                 0 vlr_cust_colab_mkt_brl,
                                                                 0 vlr_cust_colab_dn_curr,
                                                                 0 vlr_cust_colab_dn_brl,
                                                                 0 vlr_cust_colab_ae_curr,
                                                                 0 vlr_cust_colab_ae_brl,
                                                                 ---------------------------------------------------
                                                                  -- Despesas Travel / Others -> Rateio
                                                                  --------------------------------------------------
                                                                  0 vlr_desprat_mkt_trav_curr,
                                                                  0 valor_desprat_dn_trav_curr,
                                                                  0 valor_desprat_ae_trav_curr,
                                                                  0 vlr_desprat_mkt_oth_curr,
                                                                  0 valor_desprat_dn_oth_curr,
                                                                  0 valor_desprat_ae_oth_curr,
                                                                  0 vlr_desprat_mkt_trav_brl,
                                                                  0 valor_desprat_dn_trav_brl,
                                                                  0 valor_desprat_ae_trav_brl,
                                                                  0 vlr_desprat_mkt_oth_brl,
                                                                  0 valor_desprat_dn_oth_brl,
                                                                  0 valor_desprat_ae_oth_brl,
                                                                  -------------------------------------------------
                                                                  -- Despesas Travel / Others -> Diretas
                                                                  ------------------------------------------------
                                                                  0 vlr_desp_mkt_travel_curr,
                                                                  0 vlr_desp_mkt_travel_brl,
                                                                  0 vlr_desp_mkt_others_curr,
                                                                  0 vlr_desp_mkt_others_brl,
                                                                  0 vlr_desp_dn_travel_curr,
                                                                  0 vlr_desp_dn_travel_brl,
                                                                  0 vlr_desp_dn_others_curr,
                                                                  0 vlr_desp_dn_others_brl,
                                                                  0 vlr_desp_ae_travel_curr,
                                                                  0 vlr_desp_ae_travel_brl,
                                                                  0 vlr_desp_ae_others_curr,
                                                                  0 vlr_desp_ae_others_brl,
                                                                  --------------------------------------------------------------------------------------------
                                                                  -- Informac?es RES
                                                                  --------------------------------------------------------------------------------------------
                                                                  inf.valor_tce                   valor_tce_admin,
                                                                  inf.valor_custo_infra_base      valor_cinfra_base_admin,
                                                                  inf.valor_custo_res_tce_cur     valor_custo_tce_admin_cur,
                                                                  inf.valor_custo_res_tce_brl     valor_custo_tce_admin_brl,
                                                                  inf.valor_custo_res_infra_cur   valor_custo_infra_admin_cur,
                                                                  inf.valor_custo_res_infra_brl   valor_custo_infra_admin_brl,
                                                                  inf.pct_custo_res               pct_custo_admin,
                                                                  inf.valor_custo_dir_tce_curr    valor_custo_dir_tce_inov_curr,
                                                                  inf.valor_custo_dir_tce_brl     valor_custo_dir_tce_inov_brl,
                                                                  inf.valor_custo_dir_infra_curr  valor_cdir_infra_inov_curr,
                                                                  inf.valor_custo_dir_infra_brl   valor_cdir_infra_inov_brl,
                                                                  inf.valor_custo_ind_tce_curr    valor_custo_ind_tce_inov_curr,
                                                                  inf.valor_custo_ind_tce_brl     valor_custo_ind_tce_inov_brl,
                                                                  inf.valor_custo_ind_infra_curr  valor_cind_infra_inov_curr,
                                                                  inf.valor_custo_ind_infra_brl   valor_cind_infra_inov_brl,
                                                                  inf.valor_custo_hora_extra      valor_hora_extra_admin,
                                                                  inf.valor_plantao               valor_plantao_admin,
                                                                  inf.vl_custo_licenca_curr       valor_custlic_admin_curr,
                                                                  inf.vl_custo_licenca_brl        valor_custlic_admin_brl,
                                                                  inf.pct_ferias                  valor_pct_ferias_admin,
                                                                  inf.pct_licenca                 valor_pct_licenca_admin,
                                                                  inf.total_aloc_inno             valor_pct_aloc_inov_admin,
                                                                  inf.total_aloc_nao_inno         valor_pct_aloc_ninov_admin,
                                                                  inf.valor_fte_cind              valor_fte_cind_admin,
                                                                  inf.valor_despesa_cur           valor_despesa_admin_curr,
                                                                  inf.valor_despesa_brl           valor_despesa_admin_brl,
                                                                  inf.valor_despesa_clob_curr,
                                                                  inf.valor_despesa_cg_curr,
                                                                  inf.valor_despesa_clob_brl,
                                                                  inf.valor_despesa_cg_brl
                                                           from ( select inf2.fonte,
                                                                         inf2.origem,
                                                                         inf2.cd_pessoa,
                                                                         inf2.login,
                                                                         inf2.dt_mes,
                                                                         inf2.categoria,
                                                                         inf2.cd_contrato_pratica,
                                                                         inf2.nome_contrato_pratica,
                                                                         inf2.cd_cidade_base,
                                                                         inf2.dt_admissao,
                                                                         inf2.dt_rescisao,
                                                                         inf2.perc_alocavel,
                                                                         --
                                                                         '' umkt,
                                                                         '' sso,
                                                                         '' lob,
                                                                         '' bm,
                                                                         --
                                                                         inf2.status,
                                                                         inf2.dt_inicio_pgc,
                                                                         inf2.dt_fim_pgc,
                                                                         --
                                                                         inf2.cd_grupo_custo,
                                                                         inf2.nome_grupo_custo,
                                                                         inf2.nome_area,
                                                                         --
                                                                         inf2.cd_tipo_contrato,
                                                                         inf2.nome_tipo_contrato,
                                                                         inf2.dt_inicio_ptc,
                                                                         inf2.dt_fim_ptc,
                                                                         --
                                                                         inf2.pct_ferias,
                                                                         inf2.pct_licenca,
                                                                         --
                                                                         inf2.vl_custo_licenca_curr,
                                                                         inf2.vl_custo_licenca_brl,
                                                                         --
                                                                         inf2.valor_tce,
                                                                         inf2.valor_custo_infra_base,
                                                                         inf2.cd_moeda,
                                                                         inf2.moeda_custo,
                                                                         inf2.valor_cotacao,
                                                                         --
                                                                         inf2.valor_custo_hora_extra,
                                                                         inf2.valor_plantao,
                                                                         --
                                                                         inf2.pct_alocacao_innov,
                                                                         inf2.pct_alocacao_nao_innov,
                                                                         inf2.total_aloc_inno,
                                                                         inf2.total_aloc_nao_inno,
                                                                         inf2.vl_custo_alocacao_innov,
                                                                         inf2.vl_custo_alocacao_nao_innov,
                                                                         --
                                                                         inf2.pct_custo_res,
                                                                         inf2.valor_custo_res_tce_cur,
                                                                         inf2.valor_custo_res_tce_brl,
                                                                         inf2.valor_custo_res_infra_cur,
                                                                         inf2.valor_custo_res_infra_brl,
                                                                         --
                                                                         inf2.valor_custo_dir_tce_curr,
                                                                         inf2.valor_custo_dir_tce_brl,
                                                                         inf2.valor_custo_dir_infra_curr,
                                                                         inf2.valor_custo_dir_infra_brl,
                                                                         --
                                                                         inf2.valor_fte_cind,
                                                                         inf2.valor_custo_ind_tce_curr,
                                                                         inf2.valor_custo_ind_tce_brl,
                                                                         inf2.valor_custo_ind_infra_curr,
                                                                         inf2.valor_custo_ind_infra_brl,
                                                                         --
                                                                         inf2.valor_despesa_cur,
                                                                         inf2.valor_despesa_brl,
                                                                         inf2.valor_despesa_clob_curr,
                                                                         inf2.valor_despesa_cg_curr,
                                                                         inf2.valor_despesa_clob_brl,
                                                                         inf2.valor_despesa_cg_brl
                                                                  from ( select fonte,
                                                                                sub_fonte,
                                                                                origem,
                                                                                cd_pessoa,
                                                                                login,
                                                                                dt_mes,
                                                                                categoria,
                                                                                cd_contrato_pratica,
                                                                                nome_contrato_pratica,
                                                                                cd_cidade_base,
                                                                                dt_admissao,
                                                                                dt_rescisao,
                                                                                perc_alocavel,
                                                                                status,
                                                                                dt_inicio_pgc,
                                                                                dt_fim_pgc,
                                                                                cd_grupo_custo,
                                                                                nome_grupo_custo,
                                                                                nome_area,
                                                                                cd_tipo_contrato,
                                                                                nome_tipo_contrato,
                                                                                dt_inicio_ptc,
                                                                                dt_fim_ptc,
                                                                                pct_ferias,
                                                                                pct_licenca,
                                                                                vl_custo_licenca_curr,
                                                                                vl_custo_licenca_brl,
                                                                                valor_tce,
                                                                                valor_custo_infra_base,
                                                                                cd_moeda,
                                                                                moeda_custo,
                                                                                valor_cotacao,
                                                                                valor_custo_hora_extra,
                                                                                valor_plantao,
                                                                                pct_alocacao_innov,
                                                                                pct_alocacao_nao_innov,
                                                                                total_aloc_inno,
                                                                                total_aloc_nao_inno,
                                                                                vl_custo_alocacao_innov,
                                                                                vl_custo_alocacao_nao_innov,
                                                                                pct_custo_res,
                                                                                valor_custo_res_tce_cur,
                                                                                valor_custo_res_tce_brl,
                                                                                valor_custo_res_infra_cur,
                                                                                valor_custo_res_infra_brl,
                                                                                valor_custo_dir_tce_curr,
                                                                                valor_custo_dir_tce_brl,
                                                                                valor_custo_dir_infra_curr,
                                                                                valor_custo_dir_infra_brl,
                                                                                valor_fte_cind,
                                                                                valor_custo_ind_tce_curr,
                                                                                valor_custo_ind_tce_brl,
                                                                                valor_custo_ind_infra_curr,
                                                                                valor_custo_ind_infra_brl,
                                                                                valor_despesa_cur,
                                                                                valor_despesa_brl,
                                                                                valor_despesa_clob_curr,
                                                                                valor_despesa_cg_curr,
                                                                                valor_despesa_clob_brl,
                                                                                valor_despesa_cg_brl
                                                                         from tb_bi_pms_informacoes_res inf2
                                                                         where cd_contrato_pratica not in ( 3943, 3663 ) or cd_contrato_pratica is null ) inf2
                                                                  where ( ( inf2.cd_contrato_pratica is null ) or
                                                                          ( inf2.cd_contrato_pratica = 0 ) or
                                                                          ( inf2.cd_contrato_pratica is not null and origem = 'DESP_RES' ) )
                                                                  union all
                                                                  select inf2.fonte,
                                                                         inf2.origem,
                                                                         inf2.cd_pessoa,
                                                                         inf2.login,
                                                                         inf2.dt_mes,
                                                                         inf2.categoria,
                                                                         inf2.cd_contrato_pratica,
                                                                         inf2.nome_contrato_pratica,
                                                                         inf2.cd_cidade_base,
                                                                         inf2.dt_admissao,
                                                                         inf2.dt_rescisao,
                                                                         inf2.perc_alocavel,
                                                                         --
                                                                         atrib.umkt,
                                                                         atrib.sso,
                                                                         atrib.lob,
                                                                         atrib.bm,
                                                                         --
                                                                         inf2.status,
                                                                         inf2.dt_inicio_pgc,
                                                                         inf2.dt_fim_pgc,
                                                                         --
                                                                         inf2.cd_grupo_custo,
                                                                         inf2.nome_grupo_custo,
                                                                         inf2.nome_area,
                                                                         --
                                                                         inf2.cd_tipo_contrato,
                                                                         inf2.nome_tipo_contrato,
                                                                         inf2.dt_inicio_ptc,
                                                                         inf2.dt_fim_ptc,
                                                                         --
                                                                         inf2.pct_ferias,
                                                                         inf2.pct_licenca,
                                                                         --
                                                                         inf2.vl_custo_licenca_curr,
                                                                         inf2.vl_custo_licenca_brl,
                                                                         --
                                                                         inf2.valor_tce,
                                                                         inf2.valor_custo_infra_base,
                                                                         inf2.cd_moeda,
                                                                         inf2.moeda_custo,
                                                                         inf2.valor_cotacao,
                                                                         --
                                                                         inf2.valor_custo_hora_extra,
                                                                         inf2.valor_plantao,
                                                                         --
                                                                         inf2.pct_alocacao_innov,
                                                                         inf2.pct_alocacao_nao_innov,
                                                                         inf2.total_aloc_inno,
                                                                         inf2.total_aloc_nao_inno,
                                                                         inf2.vl_custo_alocacao_innov,
                                                                         inf2.vl_custo_alocacao_nao_innov,
                                                                         --
                                                                         inf2.pct_custo_res,
                                                                         inf2.valor_custo_res_tce_cur,
                                                                         inf2.valor_custo_res_tce_brl,
                                                                         inf2.valor_custo_res_infra_cur,
                                                                         inf2.valor_custo_res_infra_brl,
                                                                         --
                                                                         inf2.valor_custo_dir_tce_curr,
                                                                         inf2.valor_custo_dir_tce_brl,
                                                                         inf2.valor_custo_dir_infra_curr,
                                                                         inf2.valor_custo_dir_infra_brl,
                                                                         --
                                                                         inf2.valor_fte_cind,
                                                                         inf2.valor_custo_ind_tce_curr,
                                                                         inf2.valor_custo_ind_tce_brl,
                                                                         inf2.valor_custo_ind_infra_curr,
                                                                         inf2.valor_custo_ind_infra_brl,
                                                                         --
                                                                         inf2.valor_despesa_cur,
                                                                         inf2.valor_despesa_brl,
                                                                         inf2.valor_despesa_clob_curr,
                                                                         inf2.valor_despesa_cg_curr,
                                                                         inf2.valor_despesa_clob_brl,
                                                                         inf2.valor_despesa_cg_brl
                                                                  from ( select fonte,
                                                                                sub_fonte,
                                                                                origem,
                                                                                cd_pessoa,
                                                                                login,
                                                                                dt_mes,
                                                                                categoria,
                                                                                cd_contrato_pratica,
                                                                                nome_contrato_pratica,
                                                                                cd_cidade_base,
                                                                                dt_admissao,
                                                                                dt_rescisao,
                                                                                perc_alocavel,
                                                                                status,
                                                                                dt_inicio_pgc,
                                                                                dt_fim_pgc,
                                                                                cd_grupo_custo,
                                                                                nome_grupo_custo,
                                                                                nome_area,
                                                                                cd_tipo_contrato,
                                                                                nome_tipo_contrato,
                                                                                dt_inicio_ptc,
                                                                                dt_fim_ptc,
                                                                                pct_ferias,
                                                                                pct_licenca,
                                                                                vl_custo_licenca_curr,
                                                                                vl_custo_licenca_brl,
                                                                                valor_tce,
                                                                                valor_custo_infra_base,
                                                                                cd_moeda,
                                                                                moeda_custo,
                                                                                valor_cotacao,
                                                                                valor_custo_hora_extra,
                                                                                valor_plantao,
                                                                                pct_alocacao_innov,
                                                                                pct_alocacao_nao_innov,
                                                                                total_aloc_inno,
                                                                                total_aloc_nao_inno,
                                                                                vl_custo_alocacao_innov,
                                                                                vl_custo_alocacao_nao_innov,
                                                                                pct_custo_res,
                                                                                valor_custo_res_tce_cur,
                                                                                valor_custo_res_tce_brl,
                                                                                valor_custo_res_infra_cur,
                                                                                valor_custo_res_infra_brl,
                                                                                valor_custo_dir_tce_curr,
                                                                                valor_custo_dir_tce_brl,
                                                                                valor_custo_dir_infra_curr,
                                                                                valor_custo_dir_infra_brl,
                                                                                valor_fte_cind,
                                                                                valor_custo_ind_tce_curr,
                                                                                valor_custo_ind_tce_brl,
                                                                                valor_custo_ind_infra_curr,
                                                                                valor_custo_ind_infra_brl,
                                                                                valor_despesa_cur,
                                                                                valor_despesa_brl,
                                                                                valor_despesa_clob_curr,
                                                                                valor_despesa_cg_curr,
                                                                                valor_despesa_clob_brl,
                                                                                valor_despesa_cg_brl
                                                                         from tb_bi_pms_informacoes_res inf2
                                                                         where cd_contrato_pratica not in ( 3943, 3663 ) or cd_contrato_pratica is null ) inf2,
                                                                       vw_bi_pms_atrib_dre_mat2 atrib
                                                                  where atrib.cd_contrato_pratica = inf2.cd_contrato_pratica (+) and
                                                                        atrib.dt_mes              = inf2.dt_mes (+) ) inf,
                                                                pessoa pe,
                                                                contrato_pratica cp,
                                                                contrato co,
                                                                vw_bi_cliente cli
                                                           where inf.login               = pe.pess_cd_login (+) and
                                                                 inf.cd_contrato_pratica = cp.copr_cd_contrato_pratica (+) and
                                                                 cp.cont_cd_contrato     = co.cont_cd_contrato(+) and
                                                                 co.clie_cd_cliente      = cli.clie_cd_cliente(+) ) inf
                                                   where inf.dt_mes = moed.mes_cotacao (+)
                                                   and inf.dt_mes >= '01-jan-2011' and
                                                         inf.dt_mes <= ( select m.modu_dt_fechamento
                                                                         from pms.modulo m
                                                                         where m.modu_cd_modulo = 2 ) --data de fechamento

                              ) s
                              ) where nlinha = 1;

/*
                     lo_COST_RES_Serv_tceadm  := round(lo_COST_RES_Serv_tceadm,2);
                     lo_COST_RES_Serv_custceadm := round(lo_COST_RES_Serv_custceadm,2);
                     lo_COST_RES_Serv_cusdirtceino := round(lo_COST_RES_Serv_cusdirtceino,2);
                     lo_COST_RES_Serv_cusindtceino := round(lo_COST_RES_Serv_cusindtceino,2);
                     lo_COST_RES_Serv_horextadm := round(lo_COST_RES_Serv_horextadm,2);
                     lo_COST_RES_Serv_cuslicadm := round(lo_COST_RES_Serv_cuslicadm,2);
                     lo_COST_RES_Serv_ftecinadm := round(lo_COST_RES_Serv_ftecinadm,2);
                     lo_DESP_RES_Trav_despadm := round(lo_DESP_RES_Trav_despadm,2);
                     lo_DESP_RES_Trav_despclob := round(lo_DESP_RES_Trav_despclob,2);
                     lo_DESP_RES_Trav_despcg := round(lo_DESP_RES_Trav_despcg,2);
                     lo_DESP_RES_Cont_despadm := round(lo_DESP_RES_Cont_despadm,2);
                     lo_DESP_RES_Cont_despclob := round(lo_DESP_RES_Cont_despclob,2);
                     lo_DESP_RES_Cont_despcg := round(lo_DESP_RES_Cont_despcg,2);
                     lo_DESP_RES_Othe_despadm := round(lo_DESP_RES_Othe_despadm,2);
                     lo_DESP_RES_Othe_despclob := round(lo_DESP_RES_Othe_despclob,2);
                     lo_DESP_RES_Othe_despcg := round(lo_DESP_RES_Othe_despcg,2);*/

       if( flag = 1 or flag = 99) then --COST_RES
       begin
        ----------------------------------------------------------------------------------------------
        -- 1 - ORIGEM: COST_RES CATEGORIA: Services CAMPO: valor_tce_admin
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COST_RES';
         log_categoria := 'Services';
         log_campo := 'valor_tce_admin';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '1- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
               select valor
               into   valor_pre
               from  tb_bi_pms_log_sum_cons_dre
               where seq = (select max(seq)
                             from   tb_bi_pms_log_sum_cons_dre
                             where  origem = log_origem and
                                    categoria = log_categoria and
                                    NOME_PROC = nome_proc and
                                    nome_campo = log_campo and
                                    status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := 1;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_COST_RES_Serv_tceadm/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100-log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_COST_RES_Serv_tceadm) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100-log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_COST_RES_Serv_tceadm) || crlf;
         end if;
         insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              lo_COST_RES_Serv_tceadm,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
       -- dbms_lock.sleep(1);
       ----------------------------------------------------------------------------------------------
        -- 2 - ORIGEM: COST_RES CATEGORIA: Services CAMPO: valor_custo_tce_admin_brl
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COST_RES';
         log_categoria := 'Services';
         log_campo := 'valor_custo_tce_admin_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '2- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
               select valor
               into   valor_pre
               from  tb_bi_pms_log_sum_cons_dre
               where seq = (select max(seq)
                             from   tb_bi_pms_log_sum_cons_dre
                             where  origem = log_origem and
                                    categoria = log_categoria and
                                    NOME_PROC = nome_proc and
                                    nome_campo = log_campo and
                                    status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := lo_COST_RES_Serv_custceadm;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_COST_RES_Serv_custceadm/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100-log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_COST_RES_Serv_custceadm) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100-log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_COST_RES_Serv_custceadm) || crlf;
         end if;
         insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              lo_COST_RES_Serv_custceadm,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 3 - ORIGEM: COST_RES CATEGORIA: Services CAMPO: valor_custo_dir_tce_inov_brl
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COST_RES';
         log_categoria := 'Services';
         log_campo := 'valor_custo_dir_tce_inov_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '3- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := lo_COST_RES_Serv_cusdirtceino;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_COST_RES_Serv_cusdirtceino/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_COST_RES_Serv_cusdirtceino) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_COST_RES_Serv_cusdirtceino) || crlf;
         end if;
         insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              lo_COST_RES_Serv_cusdirtceino,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 4 - ORIGEM: COST_RES CATEGORIA: Services CAMPO: valor_custo_ind_tce_inov_brl
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COST_RES';
         log_categoria := 'Services';
         log_campo := 'valor_custo_ind_tce_inov_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '4- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := lo_COST_RES_Serv_cusindtceino;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_COST_RES_Serv_cusindtceino/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_COST_RES_Serv_cusindtceino) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_COST_RES_Serv_cusindtceino) || crlf;
         end if;
         insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              lo_COST_RES_Serv_cusindtceino,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 5 - ORIGEM: COST_RES CATEGORIA: Services CAMPO: valor_hora_extra_admin
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COST_RES';
         log_categoria := 'Services';
         log_campo := 'valor_hora_extra_admin';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '5- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := lo_COST_RES_Serv_horextadm;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_COST_RES_Serv_horextadm/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_COST_RES_Serv_horextadm) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_COST_RES_Serv_horextadm) || crlf;
         end if;
         insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              lo_COST_RES_Serv_horextadm,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 6 - ORIGEM: COST_RES CATEGORIA: Services CAMPO: valor_custlic_admin_brl
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COST_RES';
         log_categoria := 'Services';
         log_campo := 'valor_custlic_admin_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '6- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := lo_COST_RES_Serv_cuslicadm;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_COST_RES_Serv_cuslicadm/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_COST_RES_Serv_cuslicadm) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_COST_RES_Serv_cuslicadm) || crlf;
         end if;
         insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              lo_COST_RES_Serv_cuslicadm,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 7 - ORIGEM: COST_RES CATEGORIA: Services CAMPO: valor_fte_cind_admin
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'COST_RES';
         log_categoria := 'Services';
         log_campo := 'valor_fte_cind_admin';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '7- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := lo_COST_RES_Serv_ftecinadm;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_COST_RES_Serv_ftecinadm/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_COST_RES_Serv_ftecinadm) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_COST_RES_Serv_ftecinadm) || crlf;
         end if;
         insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              lo_COST_RES_Serv_ftecinadm,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
       end;
     end if;
     if(flag = 2 or flag = 99) then --DESP_RES
     begin
        ----------------------------------------------------------------------------------------------
        -- 8 - ORIGEM: DESP_RES CATEGORIA: Travel CAMPO: valor_despesa_admin_brl
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESP_RES';
         log_categoria := 'Travel';
         log_campo := 'valor_despesa_admin_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '8- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := lo_DESP_RES_Trav_despadm;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_DESP_RES_Trav_despadm/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESP_RES_Trav_despadm) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESP_RES_Trav_despadm) || crlf;
         end if;
         insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              lo_DESP_RES_Trav_despadm,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 9 - ORIGEM: DESP_RES CATEGORIA: Travel CAMPO: valor_despesa_clob_brl
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESP_RES';
         log_categoria := 'Travel';
         log_campo := 'valor_despesa_clob_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '9- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := lo_DESP_RES_Trav_despclob;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_DESP_RES_Trav_despclob/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESP_RES_Trav_despclob) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESP_RES_Trav_despclob) || crlf;
         end if;
         insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              lo_DESP_RES_Trav_despclob,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;

        ----------------------------------------------------------------------------------------------
        -- 10 - ORIGEM: DESP_RES CATEGORIA: Travel CAMPO: valor_despesa_cg_brl
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESP_RES';
         log_categoria := 'Travel';
         log_campo := 'valor_despesa_cg_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '10- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := lo_DESP_RES_Trav_despcg;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_DESP_RES_Trav_despcg/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESP_RES_Trav_despcg) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESP_RES_Trav_despcg) || crlf;
         end if;
         insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              lo_DESP_RES_Trav_despcg,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 11 - ORIGEM: DESP_RES CATEGORIA: Contractors CAMPO: valor_despesa_admin_brl
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESP_RES';
         log_categoria := 'Contractors';
         log_campo := 'valor_despesa_admin_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '11- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := lo_DESP_RES_Cont_despadm;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_DESP_RES_Cont_despadm/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESP_RES_Cont_despadm) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESP_RES_Cont_despadm) || crlf;
         end if;
         insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              lo_DESP_RES_Cont_despadm,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 12 - ORIGEM: DESP_RES CATEGORIA: Contractors CAMPO: valor_despesa_clob_brl
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESP_RES';
         log_categoria := 'Contractors';
         log_campo := 'valor_despesa_clob_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '12- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := lo_DESP_RES_Cont_despclob;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_DESP_RES_Cont_despclob/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESP_RES_Cont_despclob) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESP_RES_Cont_despclob) || crlf;
         end if;
         insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              lo_DESP_RES_Cont_despclob,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 13 - ORIGEM: DESP_RES CATEGORIA: Contractors CAMPO: valor_despesa_cg_brl
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESP_RES';
         log_categoria := 'Contractors';
         log_campo := 'valor_despesa_cg_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '13- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := lo_DESP_RES_Cont_despcg;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_DESP_RES_Cont_despcg/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESP_RES_Cont_despcg) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESP_RES_Cont_despcg) || crlf;
         end if;
         insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              lo_DESP_RES_Cont_despcg,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;

        ----------------------------------------------------------------------------------------------
        -- 14 - ORIGEM: DESP_RES CATEGORIA: Others CAMPO: valor_despesa_admin_brl
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESP_RES';
         log_categoria := 'Others';
         log_campo := 'valor_despesa_admin_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '14- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := lo_DESP_RES_Othe_despadm;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_DESP_RES_Othe_despadm/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESP_RES_Othe_despadm) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESP_RES_Othe_despadm) || crlf;
         end if;
         insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              lo_DESP_RES_Othe_despadm,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 15 - ORIGEM: DESP_RES CATEGORIA: Others CAMPO: valor_despesa_clob_brl
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESP_RES';
         log_categoria := 'Others';
         log_campo := 'valor_despesa_clob_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '15- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := lo_DESP_RES_Othe_despclob;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_DESP_RES_Othe_despclob/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESP_RES_Othe_despclob) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESP_RES_Othe_despclob) || crlf;
         end if;
         insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              lo_DESP_RES_Othe_despclob,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
        ----------------------------------------------------------------------------------------------
        -- 16 - ORIGEM: DESP_RES CATEGORIA: Others CAMPO: valor_despesa_cg_brl
        ----------------------------------------------------------------------------------------------
         --LOG
         log_origem := 'DESP_RES';
         log_categoria := 'Others';
         log_campo := 'valor_despesa_cg_brl';
         log_dt_carga_processo := sysdate;
         log_descricao := 'ORIGEM: '||log_origem||' CATEGORIA: '||log_categoria||' CAMPO: '||log_campo;
         mensagem := mensagem || crlf || '16- ' || ' DATA: '||to_char(log_dt_carga_processo, 'DD/MM/YYYY HH24:MI:SS') || crlf;

         -- EXTRAIR DA TABELA LOG O ULTIMO VALOR OK REGISTRADO (REV/Contractors)
         begin
         select valor
         into   valor_pre
         from  tb_bi_pms_log_sum_cons_dre
         where seq = (select max(seq)
                       from   tb_bi_pms_log_sum_cons_dre
                       where  origem = log_origem and
                              categoria = log_categoria and
                              NOME_PROC = nome_proc and
                              nome_campo = log_campo and
                              status = 'OK');
         exception
             WHEN OTHERS THEN
                valor_pre := lo_DESP_RES_Othe_despcg;
         end;
         -- VALIDACAO
         log_diferenca := TRUNC(((lo_DESP_RES_Othe_despcg/nvl(valor_pre,1))*100),2);
         if (log_diferenca < 99) then  --DIFERENCA MENOR QUE 99%
                 log_status   := 'ERRO';
                 assunto  := '[ERRO%]';
                 log_sqlerro  := 'Variacao %: '||to_char(100 - log_diferenca);
                 mensagem := mensagem || crlf ||
                        '[ERRO] #### '||log_descricao||' #### '|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESP_RES_Othe_despcg) || crlf;
            else
                 log_status   := 'OK';
                 mensagem := mensagem || crlf ||
                        '[OK] '||log_descricao|| crlf ||
                        '       Variacao Porcentagem: ' ||to_char(100 - log_diferenca)|| '%' || crlf ||
                        '       Valor antes: ' || to_char(valor_pre) || crlf ||
                        '       Valor apos: ' || to_char(lo_DESP_RES_Othe_despcg) || crlf;
         end if;
         insert into tb_bi_pms_log_sum_cons_dre
            (  SEQ,
               DATA_LOG,
               NOME_PROC,
               ORIGEM,
               CATEGORIA,
               NOME_CAMPO,
               STATUS,
               VALOR,
               DIFERENCA,
               SQLERRO,
               DATA_UPD
            )
         values
            ( sq_log_sum_cons_dre.nextval,
              log_data,
              nome_proc,
              log_ORIGEM,
              log_categoria,
              log_campo,
              log_status,
              lo_DESP_RES_Othe_despcg,
              log_diferenca,
              log_sqlerro,
              log_dt_carga_processo
            );
         commit;
         --  LOG - ZERAR INFORMACOES PARA PROXIMA VALIDACAO
        log_sqlerro   := '';
        valor_pre := 0;
       end;
     end if;
   mensagem := mensagem || crlf || crlf || '=== Fim da Carga ' ||to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') || ' ===';
   mensagem := mensagem || crlf ||'select * from tb_bi_pms_log_sum_cons_dre where nome_proc '||nome_proc||' order by 3 desc';
   assunto  := assunto || ' [PMS][RES] Resultado da Carga';
  send_mail('lahumada@cit.com.br', assunto, mensagem);
   send_mail('alexandrel@cit.com.br', assunto, mensagem);
   send_mail('lnoboru@cit.com.br', assunto, mensagem);
   if log_status = 'ERRO' then
      mensagem := 'ATTENTION: Please, contact Ciandt IT Business Intelligence Team as soon as possible. ' || crlf || crlf ||
                  mensagem;
--      send_mail('infra_ti@cit.com.br', assunto, mensagem);
   end if;
end USP_PMS_RES;