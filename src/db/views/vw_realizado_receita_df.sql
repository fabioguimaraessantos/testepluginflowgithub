create or replace view vw_realizado_receita_df as
select inf.cd_contrato_pratica,
       inf.nome_contrato_pratica,
       inf.dt_mes,
       inf.versao,
       inf.resource_code,
       inf.resource_name,
       inf.valor_cotacao,
       inf.moeda,
       inf.defi_cd_deal_fiscal,
       inf.rec_deal_fiscal,
       inf.vlr_preco,
       inf.valor_fte,
       rfd.redf_vl_receita vlr_receita_rec_df
from receita_deal_fiscal rfd,
     ( select  cd_contrato_pratica,
               nome_contrato_pratica,
               dt_mes,
               versao,
               resource_code,
               resource_name,
               valor_cotacao,
               moeda,
               defi_cd_deal_fiscal,
               rec_deal_fiscal,
               case when cd_contrato_pratica in ( 441, 661, 686, 688, 685, 885 ) and dt_mes = '01-jan-2010'
                         then valor_rec_deal_fiscal
                         else valor end vlr_preco,
               vlr_total_fte  valor_fte,
               redf_in_status,
               nlinha
        from ( select cd_contrato_pratica,
                      nome_contrato_pratica,
                      versao,
                      dt_mes,
                      resource_code,
                      resource_name,
                      valor_cotacao,
                      moeda,
                      defi_cd_deal_fiscal,
                      rec_deal_fiscal,
                      total_rec_fiscal_deal,
                      total_df,
                      vlr_preco_hora,
                      sum( vlr_total_fte ) over( partition by cd_contrato_pratica, dt_mes, resource_code ) vlr_total_fte,
                      -- Essa divisão é necessária, para não duplicar informações de receita, por causa do relacionamento com o
                      -- Fiscal Deal. A cláusula THEN, trata os casos que tem mais de um Fiscal Deal. A cláusula ELSE, trata os
                      -- casos onde há somente um FISCAL_DEAL, porém, com mais de uma ocorrência de receita para o mesmo.
                      case when total_rec_fiscal_deal = total_df
                                then sum( vlr_preco_hora * vlr_total_fte * 168 ) over( partition by cd_contrato_pratica, dt_mes, resource_code
                                                                                       order by     cd_contrato_pratica, dt_mes, resource_code ) / total_rec_fiscal_deal
                                else ( case when total_df = 1
                                                 then sum( vlr_preco_hora * vlr_total_fte * 168 ) over( partition by cd_contrato_pratica, dt_mes, resource_code
                                                                                                        order by     cd_contrato_pratica, dt_mes, resource_code )
                                                 else sum( vlr_preco_hora * vlr_total_fte * 168 ) over( partition by cd_contrato_pratica, dt_mes, resource_code
                                                                                                        order by     cd_contrato_pratica, dt_mes, resource_code ) / total_df end ) end valor,
                      valor_rec_deal_fiscal,
                      vlr_receita_rec_df,
                      redf_in_status,
                      row_number() over( partition by cd_contrato_pratica, dt_mes, resource_code
                                         order by     cd_contrato_pratica, dt_mes, resource_code ) nlinha
               from ( select rc.copr_cd_contrato_pratica cd_contrato_pratica,
                             cp.copr_nm_contrato_pratica nome_contrato_pratica,
                             rfd.defi_cd_deal_fiscal,
                             rfd.defi_cd_deal_fiscal rec_deal_fiscal,
                             rc.rece_dt_mes dt_mes,
                             rc.rece_in_versao versao,
                             tp.tapr_cd_tabela_preco,
                             ir.peve_cd_perfil_vendido,
                             tot_df.total_df,
                             pes.resource_code,
                             pes.resource_name,
                             cm.como_vl_cotacao valor_cotacao,
                             m.moed_sg_moeda moeda,
                             rfd.redf_vl_receita valor_rec_deal_fiscal,
                             sum( ir.vlr_total_fte ) over( partition by rc.copr_cd_contrato_pratica, rc.rece_dt_mes, ir.peve_cd_perfil_vendido, rfd.defi_cd_deal_fiscal, pes.resource_code ) vlr_total_fte,
                             count( rfd.redf_cd_receita_dfiscal  ) over( partition by rfd.rece_cd_receita ) total_rec_fiscal_deal,
                             rc.rece_cd_receita,
                             rfd.redf_in_status,
                             itp.ittp_vl_item_tb_preco vlr_preco_hora,
                             sum( rfd.redf_vl_receita  ) over( partition by rc.copr_cd_contrato_pratica, rc.rece_dt_mes, ir.peve_cd_perfil_vendido, rfd.defi_cd_deal_fiscal, pes.resource_code ) vlr_receita_rec_df
                      from receita rc,
                           receita_moeda recmoe,
                           cotacao_moeda cm,
                           moeda m,
                           ( select rfd.redf_cd_receita_dfiscal,
                                    rm2.rece_cd_receita,
                                    rfd.defi_cd_deal_fiscal,
                                    rfd.redf_vl_receita,
                                    rfd.redf_tx_error,
                                    rfd.redf_cd_erp_pedido,
                                    rfd.redf_in_status
                             from receita_deal_fiscal rfd, receita_moeda rm2
                             where rfd.remo_cd_receita_moeda = rm2.remo_cd_receita_moeda ) rfd,
                           contrato_pratica cp,
                           tabela_preco tp,
                           item_tabela_preco itp,
                           ( select rm1.rece_cd_receita,
                                    rf.redf_in_status,
                                    count(*) total_df
                             from receita_deal_fiscal rf, receita_moeda rm1
                             where rf.remo_cd_receita_moeda = rm1.remo_cd_receita_moeda
                             group by rm1.rece_cd_receita,
                                    rf.redf_in_status ) tot_df,
                           ( select pes.rece_cd_receita,
                                    pes.pess_cd_login  resource_code,
                                    pes.pess_nm_pessoa resource_name
                             from ( select remo.rece_cd_receita,
                                           pes.pess_cd_login,
                                           pes.pess_nm_pessoa,
                                           count( pes.pess_cd_login ) over( partition by remo.rece_cd_receita, pes.pess_cd_login ) total,
                                           row_number() over( partition by remo.rece_cd_receita, pes.pess_cd_login
                                                              order by     remo.rece_cd_receita, pes.pess_cd_login ) nlinha
                                    from item_receita irec,
                                         pessoa pes,
                                         receita_moeda remo
                                    where irec.pess_cd_pessoa = pes.pess_cd_pessoa 
                                    and irec.remo_cd_receita_moeda = remo.remo_cd_receita_moeda) pes
                             where nlinha = 1  ) pes,
                           ( select copr_cd_contrato_pratica,
                                    rece_dt_mes,
                                    rece_cd_receita,
                                    peve_cd_perfil_vendido,
                                    vlr_total_fte,
                                    pess_cd_login
                             from ( select r1.copr_cd_contrato_pratica,
                                           r1.rece_dt_mes,
                                           r1.rece_in_versao,
                                           ir2.rece_cd_receita,
                                           p1.pess_cd_login,
                                           ir2.peve_cd_perfil_vendido,
                                           sum( ir2.vlr_total_fte ) over( partition by r1.copr_cd_contrato_pratica, r1.rece_dt_mes, p1.pess_cd_login, ir2.peve_cd_perfil_vendido ) vlr_total_fte,
                                           row_number() over( partition by r1.copr_cd_contrato_pratica, r1.rece_dt_mes, p1.pess_cd_login
                                                              order by r1.copr_cd_contrato_pratica, r1.rece_dt_mes, p1.pess_cd_login ) nlinha
                                    from receita r1,
                                         pessoa p1,
                                         ( select rece_cd_receita,
                                                  peve_cd_perfil_vendido,
                                                  pess_cd_pessoa,
                                                  vlr_total_fte
                                           from ( select rm.rece_cd_receita,
                                                         ir.peve_cd_perfil_vendido,
                                                         ir.pess_cd_pessoa,
                                                         sum( ir.itre_nr_fte ) over( partition by rm.rece_cd_receita, ir.peve_cd_perfil_vendido, ir.pess_cd_pessoa ) vlr_total_fte,
                                                         row_number() over( partition by rm.rece_cd_receita, ir.peve_cd_perfil_vendido, ir.pess_cd_pessoa
                                                                            order by rm.rece_cd_receita, ir.peve_cd_perfil_vendido  ) nlinha
                                                  from item_receita ir, receita_moeda rm
                                                  where rm.remo_cd_receita_moeda = ir.remo_cd_receita_moeda )
                                           where nlinha = 1 ) ir2
                                    where r1.rece_cd_receita = ir2.rece_cd_receita and
                                          ir2.pess_cd_pessoa  = p1.pess_cd_pessoa ) ) ir
                      where rc.copr_cd_contrato_pratica = cp.copr_cd_contrato_pratica  and
                            recmoe.rece_cd_receita      = rc.rece_cd_receita           and
                            recmoe.como_cd_cotacao_moeda= cm.como_cd_cotacao_moeda (+) and
                            cm.moed_cd_moeda            = m.moed_cd_moeda (+)          and
                            cp.msaa_cd_msa              = tp.msaa_cd_msa  and
                            not( cp.copr_cd_contrato_pratica in ( 441, 688 ) and rc.rece_dt_mes = '01-jan-2010' and rfd.redf_in_status = 'W' ) and
                            rc.rece_cd_receita          = ir.rece_cd_receita           and
                            rc.rece_cd_receita          = rfd.rece_cd_receita          and
                            rc.rece_cd_receita          = tot_df.rece_cd_receita       and
                            rc.rece_cd_receita          = pes.rece_cd_receita          and
                            pes.resource_code           = ir.pess_cd_login            and
                            tp.tapr_cd_tabela_preco     = itp.tapr_cd_tabela_preco     and
                            ir.peve_cd_perfil_vendido   = itp.peve_cd_perfil_vendido   and
                            rc.rece_in_versao           in ( 'PB', 'IN', 'PD' )        and
                            rc.rece_dt_mes between tp.tapr_dt_inicio_vigencia and ( case when tp.tapr_dt_fim_vigencia is not null
                                                                                              then tp.tapr_dt_fim_vigencia
                                                                                              else trunc(sysdate) end ) ) )
        where nlinha = 1 and not( cd_contrato_pratica in ( 441, 688 ) and dt_mes = '01-jan-2010' and redf_in_status = 'W' ) ) inf
where inf.rec_deal_fiscal = rfd.defi_cd_deal_fiscal