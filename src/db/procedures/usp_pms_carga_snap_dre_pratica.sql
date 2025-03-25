create or replace procedure usp_pms_carga_snap_dre_pratica as
       cursor sel_inform is
              select origem,
                     cd_contrato_pratica,
                     nome_contrato_pratica,
                     dt_mes,
                     umkt,
                     sso,
                     lob,
                     bm,
                     --
                     categoria,
                     moeda,
                     pratica,
                     resource_code,
                     tipo_licenca,
                     -----------------------------------------------------------------------------------------------
                     -- Valores Receita
                     -----------------------------------------------------------------------------------------------
                     cd_moeda_revenue,
                     --
                     valor_receita_currency,
                     valor_receita_brl,
                     valor_fte_revenue,
                     valor_cotacao_revenue,
                     ---------------------------------------------------------
                     -- Receita de recursos emprestados - Contratos
                     ---------------------------------------------------------
                     cd_moeda_rev_out_sourc,
                     --
                     valor_rev_out_sourc,
                     valor_rev_out_sourc_brl,
                     valor_cotacao_out_source,
                     ---------------------------------------------------------
                     -- Receita de recursos emprestados - Recursos
                     ---------------------------------------------------------
                     cd_moeda_rev_out_alloc,
                     --
                     valor_rev_out_alloc,
                     valor_rev_out_alloc_brl,
                     valor_cotacao_out_alloc,
                     ---------------------------------------------------------
                     -- Custo de recursos emprestados - Contratos
                     ---------------------------------------------------------
                     cd_moeda_custo_out_sourc,
                     --
                     valor_custo_out_sourc,
                     valor_custo_out_sourc_brl,
                     valor_cota_custo_out_sourc,
                     ---------------------------------------------------------
                     -- Custo de recursos emprestados - Recursos
                     ---------------------------------------------------------
                     cd_moeda_custo_out_alloc,
                     --
                     valor_custo_out_alloc,
                     valor_custo_out_alloc_brl,
                     valor_cota_custo_out_alloc,
                     ---------------------------------------------------------
                     -- Custo Fixo - Pratica
                     ---------------------------------------------------------
                     cd_moeda_custfix_ratprat,
                     --
                     valor_custfix_rateio_prat,
                     valor_tce_custfix_ratprat,
                     valor_custinfra_fix_ratprat,
                     valor_custfix_ratprat_brl,
                     valor_tce_custfix_ratprat_brl,
                     valor_cusinfra_fix_ratprat_brl,
                     valor_custfix_lob_ferias,
                     valor_fte_custfix_ratprat,
                     vlr_fte_cusfix_ferias_ratprat,
                     valor_cota_custfix_ratprat,
                     ---------------------------------------------------------
                     -- Custo Fixo - UMKT
                     ---------------------------------------------------------
                     cd_moeda_custfix_ratumkt,
                     --
                     valor_custfix_rateio_umkt,
                     valor_tce_custfix_ratumkt,
                     valor_custinfra_fix_ratumkt,
                     valor_custfix_rateio_umkt_brl,
                     valor_tce_custfix_ratumkt_brl,
                     valor_custinf_fix_ratumkt_brl,
                     valor_custfix_umkt_ferias,
                     valor_fte_custfix_ratumkt,
                     vlr_fte_cusfix_ferias_ratumkt,
                     valor_cota_custfix_ratumkt,
                     ---------------------------------------------------------
                     -- Custo Licenca - Pratica
                     ---------------------------------------------------------
                     cd_moeda_custlic_ratprat,
                     --
                     valor_custlic_rateio_prat,
                     valor_custlic_ratprat_brl,
                     valor_cota_custlic_ratprat,
                     ---------------------------------------------------------
                     -- Custo Licenca - UMKT
                     ---------------------------------------------------------
                     cd_moeda_custlic_ratumkt,
                     --
                     valor_custlic_rateio_umkt,
                     valor_custlic_rateio_umkt_brl,
                     valor_cota_custlic_ratumkt,
                     -----------------------------------------------------------------------------------------------
                     -- Valores Impostos
                     -----------------------------------------------------------------------------------------------
                     valor_imposto_original,
                     valor_imposto_convertido,
                     -----------------------------------------------------------------------------------------------
                     -- Custos Diretos
                     -----------------------------------------------------------------------------------------------
                     cd_moeda_cdir,
                     --
                     valor_custo_dir_tce_curr,
                     valor_custo_dir_tce_brl,
                     valor_custo_dir_infra_curr,
                     valor_custo_dir_infra_brl,
                     valor_fte_cdir,
                     valor_fte_ferias_cdir,
                     valor_cotacao_cdir,
                     -----------------------------------------------------------------------------------------------
                     -- Custos Indiretos
                     -----------------------------------------------------------------------------------------------
                     cd_moeda_cind,
                     --
                     valor_custo_ind_tce_curr,
                     valor_custo_ind_tce_brl,
                     valor_custo_ind_infra_curr,
                     valor_custo_ind_infra_brl,
                     valor_fte_cind,
                     valor_fte_ferias_cind,
                     valor_cotacao_cind,
                     -----------------------------------------------------------------------------------------------
                     -- Despesa Realizada
                     -----------------------------------------------------------------------------------------------
                     valor_despesa_curr,
                     valor_despesa_brl,
                     -----------------------------------------------------------------------------------------------
                     -- Despesa Planejada
                     -----------------------------------------------------------------------------------------------
                     valor_desp_plan_curr,
                     valor_desp_plan_brl,
                     -----------------------------------------------------------------------------------------------
                     -- Hora Extra / Plantão
                     -----------------------------------------------------------------------------------------------
                     valor_hora_extra,
                     valor_plantao,
                     -----------------------------------------------------------------------------------------------
                     -- Hora Extra / Plantão -> Empréstimo de Recursos
                     -----------------------------------------------------------------------------------------------
                     valor_hr_extra_emp_rec_curr,
                     valor_hr_extra_emp_rec_brl,
                     valor_plantao_emp_rec_curr,
                     valor_plantao_emp_rec_brl,
                     -----------------------------------------------------------------------------------------------
                     -- Despesas Fixas por Prática
                     -----------------------------------------------------------------------------------------------
                     valor_despfix_ratprat_curr,
                     valor_despfix_ratprat_brl,
                     -----------------------------------------------------------------------------------------------
                     -- Despesas Fixas por UMKT
                     -----------------------------------------------------------------------------------------------
                     valor_despfix_ratumkt_curr,
                     valor_despfix_ratumkt_brl,
                     -----------------------------------------------------------------------------------------------
                     -- IT Charge Back
                     -----------------------------------------------------------------------------------------------
                     cd_ti_recurso,
                     nome_ti_recurso,
                     valor_custo_unit_ti_rec,
                     quant_ti_recursos,
                     valor_custo_item
              from vw_bi_pms_dre_praticas;
       --
       v_sel_inform sel_inform%rowtype;
       --
       v_versao varchar2(100);
       --
       v_num_versao number;
begin
     v_versao := '';
     v_num_versao := 0;
     --
     v_versao := 'v_' || to_char( sysdate, 'ddmmyyyyhh24' );
     --
     select max( num_versao )
     into v_num_versao
     from tb_bi_pms_dre_praticas_valid;
     --
     if v_num_versao is null or v_num_versao = 0 then
        v_num_versao := 1;
     else
        v_num_versao := v_num_versao + 1;
     end if;
     --
     if v_num_versao = 5 then
        delete from tb_bi_pms_dre_praticas_valid
        where num_versao = 1;
        --
        commit;
        --
        update tb_bi_pms_dre_praticas_valid
        set num_versao = num_versao - 1;
        --
        commit;
     end if;
     --
     open sel_inform;
     --
     fetch sel_inform into v_sel_inform;
     --
     while sel_inform%found loop
           insert into tb_bi_pms_dre_praticas_valid
           values( v_sel_inform.origem,
                   v_sel_inform.cd_contrato_pratica,
                   v_sel_inform.nome_contrato_pratica,
                   v_sel_inform.dt_mes,
                   v_sel_inform.umkt,
                   v_sel_inform.sso,
                   v_sel_inform.lob,
                   v_sel_inform.bm,
                   v_sel_inform.categoria,
                   v_sel_inform.moeda,
                   v_sel_inform.pratica,
                   v_sel_inform.resource_code,
                   v_sel_inform.tipo_licenca,
                   v_sel_inform.cd_moeda_revenue,
                   v_sel_inform.valor_receita_currency,
                   v_sel_inform.valor_receita_brl,
                   v_sel_inform.valor_fte_revenue,
                   v_sel_inform.valor_cotacao_revenue,
                   v_sel_inform.cd_moeda_rev_out_sourc,
                   v_sel_inform.valor_rev_out_sourc,
                   v_sel_inform.valor_rev_out_sourc_brl,
                   v_sel_inform.valor_cotacao_out_source,
                   v_sel_inform.cd_moeda_rev_out_alloc,
                   v_sel_inform.valor_rev_out_alloc,
                   v_sel_inform.valor_rev_out_alloc_brl,
                   v_sel_inform.valor_cotacao_out_alloc,
                   v_sel_inform.cd_moeda_custo_out_sourc,
                   v_sel_inform.valor_custo_out_sourc,
                   v_sel_inform.valor_custo_out_sourc_brl,
                   v_sel_inform.valor_cota_custo_out_sourc,
                   v_sel_inform.cd_moeda_custo_out_alloc,
                   v_sel_inform.valor_custo_out_alloc,
                   v_sel_inform.valor_custo_out_alloc_brl,
                   v_sel_inform.valor_cota_custo_out_alloc,
                   v_sel_inform.cd_moeda_custfix_ratprat,
                   v_sel_inform.valor_custfix_rateio_prat,
                   v_sel_inform.valor_tce_custfix_ratprat,
                   v_sel_inform.valor_custinfra_fix_ratprat,
                   v_sel_inform.valor_custfix_ratprat_brl,
                   v_sel_inform.valor_tce_custfix_ratprat_brl,
                   v_sel_inform.valor_cusinfra_fix_ratprat_brl,
                   v_sel_inform.valor_custfix_lob_ferias,
                   v_sel_inform.valor_fte_custfix_ratprat,
                   v_sel_inform.vlr_fte_cusfix_ferias_ratprat,
                   v_sel_inform.valor_cota_custfix_ratprat,
                   v_sel_inform.cd_moeda_custfix_ratumkt,
                   v_sel_inform.valor_custfix_rateio_umkt,
                   v_sel_inform.valor_tce_custfix_ratumkt,
                   v_sel_inform.valor_custinfra_fix_ratumkt,
                   v_sel_inform.valor_custfix_rateio_umkt_brl,
                   v_sel_inform.valor_tce_custfix_ratumkt_brl,
                   v_sel_inform.valor_custinf_fix_ratumkt_brl,
                   v_sel_inform.valor_custfix_umkt_ferias,
                   v_sel_inform.valor_fte_custfix_ratumkt,
                   v_sel_inform.vlr_fte_cusfix_ferias_ratumkt,
                   v_sel_inform.valor_cota_custfix_ratumkt,
                   v_sel_inform.cd_moeda_custlic_ratprat,
                   v_sel_inform.valor_custlic_rateio_prat,
                   v_sel_inform.valor_custlic_ratprat_brl,
                   v_sel_inform.valor_cota_custlic_ratprat,
                   v_sel_inform.cd_moeda_custlic_ratumkt,
                   v_sel_inform.valor_custlic_rateio_umkt,
                   v_sel_inform.valor_custlic_rateio_umkt_brl,
                   v_sel_inform.valor_cota_custlic_ratumkt,
                   v_sel_inform.valor_imposto_original,
                   v_sel_inform.valor_imposto_convertido,
                   v_sel_inform.cd_moeda_cdir,
                   v_sel_inform.valor_custo_dir_tce_curr,
                   v_sel_inform.valor_custo_dir_tce_brl,
                   v_sel_inform.valor_custo_dir_infra_curr,
                   v_sel_inform.valor_custo_dir_infra_brl,
                   v_sel_inform.valor_fte_cdir,
                   v_sel_inform.valor_fte_ferias_cdir,
                   v_sel_inform.valor_cotacao_cdir,
                   v_sel_inform.cd_moeda_cind,
                   v_sel_inform.valor_custo_ind_tce_curr,
                   v_sel_inform.valor_custo_ind_tce_brl,
                   v_sel_inform.valor_custo_ind_infra_curr,
                   v_sel_inform.valor_custo_ind_infra_brl,
                   v_sel_inform.valor_fte_cind,
                   v_sel_inform.valor_fte_ferias_cind,
                   v_sel_inform.valor_cotacao_cind,
                   v_sel_inform.valor_despesa_curr,
                   v_sel_inform.valor_despesa_brl,
                   v_sel_inform.valor_desp_plan_curr,
                   v_sel_inform.valor_desp_plan_brl,
                   v_sel_inform.valor_hora_extra,
                   v_sel_inform.valor_plantao,
                   v_sel_inform.valor_hr_extra_emp_rec_curr,
                   v_sel_inform.valor_hr_extra_emp_rec_brl,
                   v_sel_inform.valor_plantao_emp_rec_curr,
                   v_sel_inform.valor_plantao_emp_rec_brl,
                   v_sel_inform.valor_despfix_ratprat_curr,
                   v_sel_inform.valor_despfix_ratprat_brl,
                   v_sel_inform.valor_despfix_ratumkt_curr,
                   v_sel_inform.valor_despfix_ratumkt_brl,
                   v_sel_inform.cd_ti_recurso,
                   v_sel_inform.nome_ti_recurso,
                   v_sel_inform.valor_custo_unit_ti_rec,
                   v_sel_inform.quant_ti_recursos,
                   v_sel_inform.valor_custo_item,
                   sysdate,
                   v_versao,
                   v_num_versao );
           --
           commit;
           --
           fetch sel_inform into v_sel_inform;
        end loop;
        --
        close sel_inform;
end usp_pms_carga_snap_dre_pratica;
