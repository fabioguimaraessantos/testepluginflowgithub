create or replace view vw_realizado_receita_tag as
select inf.cd_contrato_pratica,
       inf.nome_contrato_pratica,
       inf.dt_mes,
       inf.versao,
       rfd.defi_cd_deal_fiscal,
       rfd.redf_cd_receita_dfiscal rec_deal_fiscal,
       case when inf.cd_contrato_pratica in ( 441, 661, 686, 688, 685, 885 ) and dt_mes = '01-jan-2010'
                 then rfd.redf_vl_receita
                 else vlr_preco_hora end vlr_preco,
       sum( inf.valor_total_fte ) over( partition by inf.cd_contrato_pratica, inf.dt_mes, inf.peve_cd_perfil_vendido, rfd.defi_cd_deal_fiscal, inf.cod_tag ) valor_total_fte,
       sum( rfd.redf_vl_receita  ) over( partition by inf.cd_contrato_pratica, inf.dt_mes, inf.peve_cd_perfil_vendido, rfd.defi_cd_deal_fiscal, inf.cod_tag ) vlr_receita_rec_df,
       rfd.redf_in_status,
       nlinha,
       inf.cod_tag,
       inf.desc_tag
from ( select rfd.redf_cd_receita_dfiscal,
              remo.rece_cd_receita,
              rfd.defi_cd_deal_fiscal,
              rfd.redf_vl_receita,
              rfd.redf_tx_error,
              rfd.redf_cd_erp_pedido,
              rfd.redf_in_status
       from receita_deal_fiscal rfd, receita_moeda remo
       where remo.remo_cd_receita_moeda = rfd.remo_cd_receita_moeda ) rfd,
     ( select cd_contrato_pratica,
              nome_contrato_pratica,
              rece_cd_receita,
              dt_mes,
              cod_tag,
              desc_tag,
              versao,
              peve_cd_perfil_vendido,
              sum( valor_calc ) over( partition by cd_contrato_pratica, dt_mes, cod_tag ) vlr_preco_hora,
              sum( valor_total_fte ) over( partition by cd_contrato_pratica, dt_mes, cod_tag ) valor_total_fte,
              row_number() over( partition by cd_contrato_pratica, dt_mes, cod_tag
                                 order by     cd_contrato_pratica, dt_mes, cod_tag ) nlinha
       from ( select rec2.copr_cd_contrato_pratica cd_contrato_pratica,
                     cp.copr_nm_contrato_pratica   nome_contrato_pratica,
                     --
                     rec2.rece_dt_mes              dt_mes,
                     rec2.rece_in_versao           versao,
                     --
                     irec2.pess_cd_pessoa,
                     irec2.itre_cd_item_receita,
                     irec2.itre_vl_total_item,
                     irec2.itre_nr_fte,
                     rec2.rece_cd_receita,
                     irec2.peve_cd_perfil_vendido,
                     --
                     p.pess_cd_login,
                     tp.tapr_cd_tabela_preco,
                     itp.ittp_vl_item_tb_preco     vlr_preco_hora,
                     eirec.etiq_cd_etiqueta        cod_tag,
                     eq.etiq_nm_etiqueta           desc_tag,
                     ( itp.ittp_vl_item_tb_preco * irec2.itre_nr_fte * 168 ) valor_calc,
                     irec2.itre_nr_fte valor_total_fte
              from receita                 rec2,
                   receita_moeda           remo2,
                   item_receita            irec2,
                   contrato_pratica        cp,
                   pessoa                  p,
                   etiqueta_item_receita   eirec,
                   etiqueta                eq,
                   tabela_preco            tp,
                   item_tabela_preco       itp
              where rec2.rece_cd_receita          = remo2.rece_cd_receita          and
                    remo2.remo_cd_receita_moeda   = irec2.remo_cd_receita_moeda    and
                    rec2.copr_cd_contrato_pratica = cp.copr_cd_contrato_pratica    and
                    irec2.pess_cd_pessoa          = p.pess_cd_pessoa               and
                    tp.tapr_cd_tabela_preco       = itp.tapr_cd_tabela_preco       and
                    irec2.peve_cd_perfil_vendido  = itp.peve_cd_perfil_vendido     and
                    irec2.itre_cd_item_receita    = eirec.itre_cd_item_receita (+) and
                    eirec.etiq_cd_etiqueta        = eq.etiq_cd_etiqueta (+)        and
                    rec2.rece_in_versao           in ( 'PB', 'IN' ) and
                    rec2.rece_dt_mes between tp.tapr_dt_inicio_vigencia and ( case when tp.tapr_dt_fim_vigencia is not null
                                                                                        then tp.tapr_dt_fim_vigencia
                                                                                        else trunc(sysdate) end ) ) ) inf
where rfd.rece_cd_receita = inf.rece_cd_receita and
      nlinha = 1 and
      not( cd_contrato_pratica in ( 441, 688 ) and dt_mes = '01-jan-2010' and redf_in_status = 'W' )