create or replace view vw_consolida_receita_pms_tag as
select -- Informações de Receita Planejado X Realizado
       inf_rec.cd_contrato_pratica,
       inf_rec.nome_contrato_pratica,
       inf_rec.dt_mes,
       inf_rec.aloc_in_estagio,
       -- Novo preço, sendo calculado a partir da soma do valor do Contractors
       case when inf_rec.source = 'REVENUE'
                 then inf_rec.vlr_preco + case when cont.valor_contractors is null then 0 else cont.valor_contractors end
                 else inf_rec.vlr_preco end vlr_preco,
       case when inf_rec.valor_fte_ut_rate > 0
                 then inf_rec.vlr_preco / nvl( decode( inf_rec.valor_fte_ut_rate, 0, 1 ), 1 )
                 else inf_rec.vlr_preco end vlr_preco_fte,
       inf_rec.valor_fte valor_fte,
       inf_rec.valor_ut_rate,
       inf_rec.valor_fte_ut_rate,
       inf_rec.source,
       inf_rec.mes_fechto_revenue,
       -- Informações de TCE Orçado X Realizado
       case when inf_rec.dt_mes > tce.mes_f
                 then tce.vlr_custo_folha_planej
                 else tce.vlr_custo_folha_realiz end valor_tce,
       -- Contractors
       case when cont.valor_contractors is null then 0 else cont.valor_contractors end valor_contractors,
       inf_rec.cod_tag,
       inf_rec.desc_tag
from ( select fat.cd_contrato_pratica,
              fat.nome_contrato_pratica,
              fat.cod_fatura,
              fat.dt_mes,
              fat.dt_mes_comparacao,
              fat.defi_cd_deal_fiscal,
              fat.valor_contractors,
              fat.fatu_in_status,
              fat.nlinha
       from ( select fat.fatu_cd_fatura cod_fatura,
                     df.copr_cd_contrato_pratica cd_contrato_pratica,
                     cp.copr_nm_contrato_pratica nome_contrato_pratica,
                     fat.defi_cd_deal_fiscal,
                     fat.fatu_dt_previsao        dt_mes,
                     fat.fatu_in_status,
                     to_date( '01/' || to_char( fat.fatu_dt_previsao, 'mm/yyyy' ), 'DD/MM/YYYY' ) dt_mes_comparacao,
                     ifat.itfa_vl_item,
                     sum( case when ifat.fore_cd_fonte_receita = 3 then ifat.itfa_vl_item else 0 end )
                                    over( partition by df.copr_cd_contrato_pratica, to_date( '01/' || to_char( fat.fatu_dt_previsao, 'mm/yyyy' ), 'DD/MM/YYYY' ), fat.defi_cd_deal_fiscal, ifat.itfa_vl_item ) valor_contractors,
                     row_number() over( partition by df.copr_cd_contrato_pratica, to_date( '01/' || to_char( fat.fatu_dt_previsao, 'mm/yyyy' ), 'DD/MM/YYYY' ), fat.defi_cd_deal_fiscal
                                    order by df.copr_cd_contrato_pratica, to_date( '01/' || to_char( fat.fatu_dt_previsao, 'mm/yyyy' ), 'DD/MM/YYYY' ), fat.defi_cd_deal_fiscal ) nlinha
              from contrato_pratica cp,
                   fatura fat,
                   fonte_receita frec2,
                   item_fatura ifat,
                   deal_fiscal df
              where cp.copr_cd_contrato_pratica = df.copr_cd_contrato_pratica   and
                    df.defi_cd_deal_fiscal      = fat.defi_cd_deal_fiscal       and
                    ifat.fore_cd_fonte_receita  = frec2.fore_cd_fonte_receita   and
                    fat.fatu_cd_fatura          = ifat.fatu_cd_fatura           and
                    fat.fatu_in_status          = 'SB'                          ) fat
       where fat.nlinha = 1 and valor_contractors > 0 ) cont,
     ( select tce.cd_contrato_pratica,
              tce.nome_contrato_pratica,
              tce.dt_mes,
              case when tce.source = 'TCE_REALIZ' then vlr_custo_folha else 0 end vlr_custo_folha_realiz,
              case when tce.source = 'TCE_PLANEJ' then vlr_custo_folha else 0 end vlr_custo_folha_planej,
              tce.source,
              ( select md.modu_dt_fechamento dt_mes_mod
                from modulo md
                where md.modu_nm_modulo = 'Revenue' ) mes_f,
              count( tce.cd_contrato_pratica ) over( partition by tce.cd_contrato_pratica, tce.dt_mes ) total_tce
       from vw_bi_custo_tce_plan_realiz tce ) tce,
     ( select inf2.cd_contrato_pratica,
              inf2.nome_contrato_pratica,
              inf2.dt_mes,
              inf2.aloc_in_estagio,
              inf2.vlr_preco vlr_preco,
              case when inf2.valor_fte_ut_rate > 0
                        then inf2.vlr_preco / nvl( decode( inf2.valor_fte_ut_rate, 0, 1 ), 1 )
                        else inf2.vlr_preco end vlr_preco_fte,
              inf2.valor_fte valor_fte,
              inf2.valor_ut_rate,
              inf2.valor_fte_ut_rate,
              inf2.source,
              inf2.mes_fechto_revenue,
              inf2.cod_tag,
              inf2.desc_tag
       from ( select inf1.cd_contrato_pratica,
                     inf1.nome_contrato_pratica,
                     inf1.dt_mes,
                     inf1.aloc_in_estagio,
                     inf1.flag_mostra_almap,
                     inf1.vlr_preco_revenue,
                     case when inf1.source = 'REVENUE' and origem = 'Q1'
                               then vlr_preco_revenue
                          when inf1.source = 'REVENUE' and origem = 'Q2'
                               then vlr_preco_al_map
                          -- Somente traz o preço, REVENUES de meses fechados, e retorna 0, se o mês não está
                          -- fechado e não tem receita, não mostrando mais do ALLOCATION MAP
                          else case when inf1.flag_mostra_almap = 'S'
                                         then sum( vlr_preco_al_map ) over( partition by inf1.cd_contrato_pratica, inf1.dt_mes, inf1.aloc_in_estagio )
                                         else 0 end end vlr_preco,
                     inf1.valor_fte,
                     inf1.valor_ut_rate,
                     case when inf1.source = 'REVENUE'
                               then inf1.valor_fte_ut_rate
                               else ( inf1.valor_fte * inf1.valor_ut_rate ) end valor_fte_ut_rate,
                     inf1.source,
                     inf1.mes_fechto_revenue,
                     inf1.cod_tag,
                     inf1.desc_tag,
                     row_number() over( partition by inf1.cd_contrato_pratica, inf1.dt_mes, inf1.aloc_in_estagio, inf1.cod_tag
                                        order by inf1.cd_contrato_pratica, inf1.dt_mes, inf1.aloc_in_estagio desc, inf1.cod_tag ) nlinha
              from ( select origem,
                            case when cd_cont_pratica_rev is not null
                                      then cd_cont_pratica_rev
                                      else cd_cont_pratica_almap end cd_contrato_pratica,
                            case when cd_cont_pratica_rev is not null
                                      then nome_cont_pratica_rev
                                      else nome_cont_pratica_almap end nome_contrato_pratica,
                            case when cd_cont_pratica_rev is not null
                                      then dt_mes_rev
                                      else dt_mes_al_map end dt_mes,
                            vlr_preco_revenue,
                            vlr_preco_al_map,
                            case when ( case when cd_cont_pratica_rev is not null
                                                  then dt_mes_rev
                                                  else dt_mes_al_map end ) > mes_fechto_revenue
                                      then 'S'
                                      else 'N' end flag_mostra_almap,
                            case when cd_cont_pratica_rev is not null
                                      then valor_fte_rev
                                      else (  case when ( case when cd_cont_pratica_rev is not null
                                                                    then dt_mes_rev
                                                                    else dt_mes_al_map end ) > mes_fechto_revenue
                                                        then vlr_ut_rate_al_map
                                                        else 0 end )
                                      end valor_fte_ut_rate,
                            case when cd_cont_pratica_rev is not null
                                      then valor_fte_rev / nvl( decode( valor_fte_al_map, 0, 1 ), 1 )
                                      else (  case when ( case when cd_cont_pratica_rev is not null
                                                                    then dt_mes_rev
                                                                    else dt_mes_al_map end ) > mes_fechto_revenue
                                                         then vlr_ut_rate_al_map
                                                         else 0 end )
                                      end valor_ut_rate,
                            case when cd_cont_pratica_rev is not null
                                      then source_rev
                                      else ( case when ( case when cd_cont_pratica_rev is not null
                                                                   then dt_mes_rev
                                                                   else dt_mes_al_map end ) > mes_fechto_revenue
                                                  then source_al_map
                                                  else ( case when origem = 'Q2'
                                                                   then 'REVENUE'
                                                                   else '' end ) end )
                                      end source,
                            case when cd_cont_pratica_rev is not null
                                      then 'Commited'
                                      else ( case when aloc_in_estagio = 'CM'
                                                       then 'Commited'
                                                  when aloc_in_estagio = 'RV'
                                                       then 'Reserved'
                                                  when aloc_in_estagio = 'PH'
                                                       then 'Prospect High'
                                                  when aloc_in_estagio = 'PL'
                                                       then 'Prospect Low'
                                                  else aloc_in_estagio end ) end aloc_in_estagio,
                            -- Verificação se o mes atual ( ALMAP ou REVENUE ) é maior que o último mês Revenue fechado,
                            -- para que não mostre informações de ALMAP, para meses fechados, mesmo que não tenham Revenue
                            -- O mesmo é feito nas colunas de Valor e Source acima
                            case when ( case when cd_cont_pratica_rev is not null
                                                  then dt_mes_rev
                                                  else dt_mes_al_map end ) > mes_fechto_revenue
                                 then valor_fte_al_map
                                 else 0 end valor_fte,
                            mes_fechto_revenue,
                            case when cd_cont_pratica_rev is not null
                                      then cod_tag_rev
                                      else ( case when ( case when cd_cont_pratica_rev is not null
                                                                   then dt_mes_rev
                                                                   else dt_mes_al_map end ) > mes_fechto_revenue
                                                  then cod_tag_al_map
                                                  else ( case when origem = 'Q2'
                                                                   then cod_tag_rev
                                                                   else 0 end ) end )
                                      end cod_tag,
                            case when cd_cont_pratica_rev is not null
                                      then desc_tag_rev
                                      else ( case when ( case when cd_cont_pratica_rev is not null
                                                                   then dt_mes_rev
                                                                   else dt_mes_al_map end ) > mes_fechto_revenue
                                                  then desc_tag_al_map
                                                  else ( case when origem = 'Q2'
                                                                   then desc_tag_rev
                                                                   else '' end ) end )
                                      end desc_tag
                     from ( select 'Q1'                              origem,
                                   pl_tag.source                     source_al_map,
                                   pl_tag.cd_contrato_pratica        cd_cont_pratica_almap,
                                   pl_tag.nome_contrato_pratica      nome_cont_pratica_almap,
                                   pl_tag.dt_mes                     dt_mes_al_map,
                                   pl_tag.aloc_in_estagio            aloc_in_estagio,
                                   pl_tag.vlr_preco                  vlr_preco_al_map,
                                   pl_tag.valor_fte                  valor_fte_al_map,
                                   pl_tag.valor_fte_ut_rate / nvl( decode( pl_tag.valor_fte, 0, 1 ), 1 ) vlr_ut_rate_al_map,
                                   pl_tag.valor_fte_ut_rate          vlr_fte_ut_rate_al_map,
                                   pl_tag.cod_tag                    cod_tag_al_map,
                                   pl_tag.desc_tag                   desc_tag_al_map,
                                   --
                                   rec_tag.source                    source_rev,
                                   rec_tag.cd_contrato_pratica       cd_cont_pratica_rev,
                                   rec_tag.nome_contrato_pratica     nome_cont_pratica_rev,
                                   rec_tag.dt_mes                    dt_mes_rev,
                                   rec_tag.vlr_preco                 vlr_preco_revenue,
                                   rec_tag.valor_fte_ut_rate         vlr_fte_ut_rate_rev,
                                   rec_tag.valor_fte                 valor_fte_rev,
                                   rec_tag.cod_tag                   cod_tag_rev,
                                   rec_tag.desc_tag                  desc_tag_rev,
                                   -- Coluna existente aqui, para considerar que mês fechado que não tenha REVENUE,
                                   -- não deve ser mostrada também, a alocação do mesmo.
                                   ( select md.modu_dt_fechamento
                                     from modulo md
                                     where md.modu_nm_modulo = 'Revenue' ) mes_fechto_revenue
                            from ( select rec_tag.cd_contrato_pratica,
                                          rec_tag.nome_contrato_pratica,
                                          rec_tag.dt_mes,
                                          rec_tag.vlr_preco,
                                          rec_tag.valor_total_fte  valor_fte,
                                          0 valor_ut_rate,
                                          rec_tag.valor_total_fte valor_fte_ut_rate,
                                          'REVENUE' source,
                                          rec_tag.cod_tag,
                                          rec_tag.desc_tag
                                   from vw_realizado_receita_tag rec_tag
                                   /*where cd_contrato_pratica in ( 606 )*/ ) rec_tag,
                                 ( select pl_tag.cd_contrato_pratica,
                                          pl_tag.nome_contrato_pratica,
                                          pl_tag.dt_mes,
                                          pl_tag.vlr_preco,
                                          sum( pl_tag.valor_fte ) over( partition by pl_tag.cd_contrato_pratica, pl_tag.dt_mes, pl_tag.aloc_in_estagio ) valor_fte,
                                          pl_tag.valor_ut_rate valor_ut_rate,
                                          sum( pl_tag.valor_fte_ut_rate ) over( partition by pl_tag.cd_contrato_pratica, pl_tag.dt_mes, pl_tag.aloc_in_estagio ) valor_fte_ut_rate,
                                          pl_tag.aloc_in_estagio,
                                          'ALLOCATION MAP' source,
                                          pl_tag.cod_tag,
                                          pl_tag.desc_tag
                                   from vw_planejado_receita_tag pl_tag
                                   /*where cd_contrato_pratica in ( 606 )*/ ) pl_tag
                            where pl_tag.cd_contrato_pratica = rec_tag.cd_contrato_pratica (+) and
                                  pl_tag.dt_mes              = rec_tag.dt_mes (+)
                            union all
                            -- Registros de Contrac Lob / Month, que tem revenue mas não tem allocation map
                            select 'Q2'                                    origem,
                                   rec_tag.source                          source_rev,
                                   rec_tag.cd_contrato_pratica             cd_cont_pratica_rev,
                                   rec_tag.nome_contrato_pratica           nome_cont_pratica_rev,
                                   rec_tag.dt_mes                          dt_mes_rev,
                                   'Commited'                              aloc_in_estagio,
                                   rec_tag.vlr_preco                       vlr_preco_revenue,
                                   rec_tag.valor_fte                       valor_fte_rev,
                                   rec_tag.valor_fte_ut_rate / nvl( decode( rec_tag.valor_fte, 0, 1 ), 1 )   vlr_fte_ut_rate_rev,
                                   rec_tag.valor_fte_ut_rate               vlr_fte_ut_rate_al_map,
                                   rec_tag.cod_tag                         cod_tag_rec,
                                   rec_tag.desc_tag                        desc_tag_rec,
                                   --
                                   pl_tag.source                           source_al_map,
                                   pl_tag.cd_contrato_pratica              cd_cont_pratica_almap,
                                   pl_tag.nome_contrato_pratica            nome_cont_pratica_almap,
                                   pl_tag.dt_mes                           dt_mes_al_map,
                                   pl_tag.vlr_preco                        vlr_preco_al_map,
                                   pl_tag.valor_fte_ut_rate                vlr_ut_rate_al_map,
                                   pl_tag.valor_fte                        valor_fte_al_map,
                                   pl_tag.cod_tag                          cod_tag_al_map,
                                   pl_tag.desc_tag                         desc_tag_al_map,
                                   --
                                   ( select md.modu_dt_fechamento
                                     from modulo md
                                     where md.modu_nm_modulo = 'Revenue' ) mes_fechto_revenue
                            from ( select rec_tag.cd_contrato_pratica,
                                          rec_tag.nome_contrato_pratica,
                                          rec_tag.dt_mes,
                                          rec_tag.vlr_preco,
                                          rec_tag.valor_total_fte  valor_fte,
                                          0 valor_ut_rate,
                                          rec_tag.valor_total_fte valor_fte_ut_rate,
                                          'REVENUE' source,
                                          rec_tag.cod_tag,
                                          rec_tag.desc_tag
                                   from vw_realizado_receita_tag rec_tag ) rec_tag,
                                 ( select pl_tag.cd_contrato_pratica,
                                          pl_tag.nome_contrato_pratica,
                                          pl_tag.dt_mes,
                                          pl_tag.vlr_preco,
                                          sum( pl_tag.valor_fte ) over( partition by pl_tag.cd_contrato_pratica, pl_tag.dt_mes, pl_tag.aloc_in_estagio, pl_tag.cod_tag ) valor_fte,
                                          pl_tag.valor_ut_rate valor_ut_rate,
                                          sum( pl_tag.valor_fte_ut_rate ) over( partition by pl_tag.cd_contrato_pratica, pl_tag.dt_mes, pl_tag.aloc_in_estagio, pl_tag.cod_tag ) valor_fte_ut_rate,
                                          pl_tag.aloc_in_estagio,
                                          'ALLOCATION MAP' source,
                                          pl_tag.cod_tag,
                                          pl_tag.desc_tag
                                   from vw_planejado_receita_tag pl_tag ) pl_tag
                            where rec_tag.cd_contrato_pratica = pl_tag.cd_contrato_pratica (+) and
                                  rec_tag.dt_mes              = pl_tag.dt_mes (+) and
                                  pl_tag.cd_contrato_pratica is null
                            order by 4 ) ) inf1 ) inf2
       where nlinha = 1 ) inf_rec
where inf_rec.cd_contrato_pratica = tce.cd_contrato_pratica (+) and
      inf_rec.dt_mes              = tce.dt_mes (+) and
      inf_rec.cd_contrato_pratica = cont.cd_contrato_pratica (+) and
      inf_rec.dt_mes              = cont.dt_mes_comparacao (+) and
      ( ( case when inf_rec.dt_mes > tce.mes_f
                    then tce.vlr_custo_folha_planej
                    else tce.vlr_custo_folha_realiz end > 0 and tce.total_tce > 1 ) or
        ( tce.total_tce = 1  ) or
        ( tce.dt_mes is null ) )