create or replace view vw_consolida_receita_pms as
select recusd.origem,
       recusd.cd_contrato_pratica,
       recusd.nome_contrato_pratica,
       recusd.pratica,
       recusd.dt_mes,
       recusd.aloc_in_estagio,
       recusd.resource_code,
       recusd.resource_name,
       recusd.valor_cotacao,
       recusd.cd_moeda,
       recusd.moeda,
       recusd.umkt,
       recusd.bm,
       recusd.sso,
       recusd.perfil_vendido,

      recusd.perfil_padrao ,

       recusd.vlr_preco,
       recusd.vlr_preco_brl,
       recusd.vlr_preco_fte,
       recusd.valor_fte valor_fte,
       recusd.valor_ut_rate,
       recusd.valor_fte_ut_rate,
       recusd.source,
       recusd.mes_fechto_revenue,
       -------------------------------------------------
       -- Informac?es de TCE Orcado X Realizado
       -------------------------------------------------
       recusd.valor_tce,
       -----------------------------
       -- Contractors
       -----------------------------
       recusd.revenue_source,
       recusd.valor_reembolso_currency,
       recusd.valor_reembolso_brl,
       recusd.valor_contractors,
       recusd.valor_contractors_brl,
       recusd.valor_licenses_currency,
       recusd.valor_licenses_brl,
       recusd.valor_bonus_currency,
       recusd.valor_bonus_brl,
       recusd.cd_receita,
       recusd.nome_perfil_vendido,
       recusd.cont_cd_contrato,
       recusd.clie_cd_cliente,

       ---------------------------------------------------------------------------------------------------------------------------------
       -- Novo datapoint Valor da Revenue em USD. Se a moeda da Revenue ja for USD, somente repete o valor de Valor Revenue Currency.
       -- Caso contrario, pega a cotac?o do USD do mes, e converte o valor para USD.
       ---------------------------------------------------------------------------------------------------------------------------------
       case when recusd.cd_moeda = 2
                then recusd.vlr_preco
                else ( recusd.vlr_preco_brl / moed.valor_cotacao ) end valor_preco_usd,
       case when recusd.cd_moeda = 1
                 then 1
                 else recusd.valor_cotacao end valor_cotacao_preco,
       -------------------------------------------------------------------------------------
       -- Cotac?o da moeda em USD, utilizada para montagem do datapoint VALOR_RECEITA_USD
       -------------------------------------------------------------------------------------
       case when recusd.vlr_preco = 0
                 then 0
                 else moed.valor_cotacao end valor_cotacao_usd,
       recusd.cd_grupo_custo,
       recusd.nm_grupo_custo,
       recusd.cd_cidade_base_aloc,
       recusd.sigla_cidade_base_aloc
  from (select moed_cd_moeda, /*moed*/
               moeda,
               ------------------------------------------------------------------------------------------------------------------
               -- Transforma o resultado da coluna MES_ANO, numa coluna do tipo DATE, para facilitar comparac?es posteriores.
               ------------------------------------------------------------------------------------------------------------------
               to_date( '01/' || to_char( como_dt_dia, 'MM/YYYY'), 'DD/MM/YYYY') mes_cotacao,
               valor_cotacao,
               como_in_tipo
         from (select cm.moed_cd_moeda,
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

(select inf.origem,
       inf.cd_contrato_pratica,
       inf.nome_contrato_pratica,
       inf.pratica,
       inf.dt_mes,
       inf.aloc_in_estagio,
       inf.resource_code,
       inf.resource_name,
       inf.valor_cotacao,
       inf.cd_moeda,
       inf.moeda,
       inf.umkt,
       inf.bm,
       inf.sso,
       inf.perfil_vendido,
       inf.perfil_padrao,
       inf.vlr_preco,
       inf.vlr_preco_brl,
       inf.vlr_preco_fte,
       inf.valor_fte valor_fte,
       inf.valor_ut_rate,
       inf.valor_fte_ut_rate,
       inf.source,
       inf.mes_fechto_revenue,
       -------------------------------------------------
       -- Informac?es de TCE Orcado X Realizado
       -------------------------------------------------
       inf.valor_tce,
       -----------------------------
       -- Contractors
       -----------------------------
       inf.revenue_source,
       inf.valor_reembolso_currency,
       inf.valor_reembolso_brl,
       inf.valor_contractors,
       inf.valor_contractors_brl,
       inf.valor_licenses_currency,
       inf.valor_licenses_brl,
       inf.valor_bonus_currency,
       inf.valor_bonus_brl,
       inf.cd_receita,
       inf.nome_perfil_vendido,
       inf.cont_cd_contrato,
       inf.clie_cd_cliente,/*,
       inf.cpcl_cd_contrato_pratica_cl,
       inf.celu_cd_centro_lucro,
       inf.nacl_cd_natureza*/--
       inf.cd_grupo_custo,
       inf.nm_grupo_custo,
       inf.cd_cidade_base_aloc,
       inf.sigla_cidade_base_aloc
  from  (select origem,
              cd_contrato_pratica,
              nome_contrato_pratica,
              pratica,
              dt_mes,
              aloc_in_estagio,
              resource_code,
              resource_name,
              valor_cotacao,
              cd_moeda,
              moeda,
              umkt,
              bm,
              sso,
              perfil_vendido,
              perfil_padrao,
              vlr_preco_currency vlr_preco,
              vlr_preco_brl,
              vlr_preco_fte,
              valor_fte valor_fte,
              valor_ut_rate,
              valor_fte_ut_rate,
              source,
              mes_fechto_revenue,
              -------------------------------------------------
              -- Informac?es de TCE Orcado X Realizado
              -------------------------------------------------
              valor_tce,
              -----------------------------
              -- Contractors
              -----------------------------
              revenue_source,
              valor_reembolso_currency,
              valor_reembolso_brl,
              valor_contractors_currency valor_contractors,
              valor_contractors_brl,
              valor_licenses_currency,
              valor_licenses_brl,
              valor_bonus_currency,
              valor_bonus_brl,
              cd_receita,
              nome_perfil_vendido,
              co.cont_cd_contrato,
              cli.clie_cd_cliente clie_cd_cliente,/*,
              cpclc.cpcl_cd_contrato_pratica_cl,
              clcr.celu_cd_centro_lucro,
              ncl.nacl_cd_natureza*/
              cd_grupo_custo,
              nm_grupo_custo,
              cd_cidade_base_aloc,
              sigla_cidade_base_aloc
       from ( select 'Q1' origem, --
                      cd_contrato_pratica,
                      nome_contrato_pratica,
                      pratica,
                      dt_mes,
                      aloc_in_estagio,
                      resource_code,
                      resource_name,
                      valor_cotacao,
                      cd_moeda,
                      moeda,
                      umkt,
                      bm,
                      sso,
                      to_char(perfil_vendido) perfil_vendido,
                      to_char (perfil_padrao) perfil_padrao,
                      vlr_preco vlr_preco_currency,
                      case when moeda <> 'BRL'
                                then vlr_preco * valor_cotacao
                                else vlr_preco end vlr_preco_brl,
                      vlr_preco_fte,
                      valor_fte valor_fte,
                      valor_ut_rate,
                      valor_fte_ut_rate,
                      source,
                      mes_fechto_revenue,
                      ----------------------------------------------
                      -- Informac?es de TCE Orcado X Realizado
                      ----------------------------------------------
                      valor_tce,
                      ---------------------
                      -- Invoices
                      ---------------------
                      revenue_source,
                      --
                      valor_reembolso_currency,
                      valor_reembolso_brl,
                      --
                      valor_contractors_currency,
                      valor_contractors_brl,
                      --
                      valor_licenses_currency,
                      valor_licenses_brl,
                      --
                      valor_bonus_currency,
                      valor_bonus_brl,
                      cd_receita,
                      nome_perfil_vendido,
                      cd_grupo_custo,
                      nm_grupo_custo,
                      cd_cidade_base_aloc,
                      sigla_cidade_base_aloc
               from ( select --------------------------------------------------------------
                             -- Informac?es de Receita Planejado X Realizado
                             --------------------------------------------------------------
                             inf_rec.cd_contrato_pratica,
                             inf_rec.nome_contrato_pratica,
                             inf_rec.pratica,
                             inf_rec.dt_mes,
                             inf_rec.aloc_in_estagio,
                             inf_rec.resource_code,
                             inf_rec.resource_name,
                             inf_rec.valor_cotacao,
                             inf_rec.cd_moeda,
                             inf_rec.moeda,
                             inf_rec.umkt,
                             inf_rec.bm,
                             inf_rec.vlr_preco vlr_preco,
                             inf_rec.sso,
                             to_char (inf_rec.perfil_vendido) perfil_vendido,
                             to_char (inf_rec.perfil_padrao) perfil_padrao, --erro
                             case when inf_rec.valor_fte_ut_rate > 0
                                       then inf_rec.vlr_preco / nvl( decode( inf_rec.valor_fte_ut_rate, 0, 1 ), 1 )
                                       else inf_rec.vlr_preco end vlr_preco_fte,
                             inf_rec.valor_fte valor_fte,
                             inf_rec.valor_ut_rate,
                             inf_rec.valor_fte_ut_rate valor_fte_ut_rate,
                             inf_rec.source,
                             inf_rec.mes_fechto_revenue,
                             inf_rec.cd_receita,
                             inf_rec.nome_perfil_vendido,
                             inf_rec.cd_grupo_custo,
                             inf_rec.nm_grupo_custo,
                             inf_rec.cd_cidade_base_aloc,
                             inf_rec.sigla_cidade_base_aloc,
                             --------------------------------------------------
                             -- Informac?es de TCE Orcado X Realizado
                             --------------------------------------------------
                             0 valor_tce,
                             -------------------
                             -- Invoices
                             -------------------
                             fat.revenue_source,
                             fat.valor_reembolso_currency,
                             fat.valor_reembolso_brl,
                             fat.valor_contractors_currency,
                             fat.valor_contractors_brl,
                             fat.valor_licenses_currency,
                             fat.valor_licenses_brl,
                             fat.valor_bonus_currency,
                             fat.valor_bonus_brl,
                             --                                                                          /*Prospect High - Makro-ADS - NOV/2012*/
                             row_number() over( partition by inf_rec.cd_contrato_pratica, inf_rec.dt_mes, inf_rec.aloc_in_estagio, inf_rec.perfil_vendido, inf_rec.resource_code
                                                order by inf_rec.cd_contrato_pratica, inf_rec.dt_mes, inf_rec.aloc_in_estagio, inf_rec.perfil_vendido, inf_rec.resource_code ) nlinha
                      from ( select fat.cd_contrato_pratica,
                                    fat.nome_contrato_pratica,
                                    fat.dt_mes,
                                    fat.valor_reembolso_currency,
                                    fat.valor_reembolso_brl,
                                    fat.valor_contractors_currency,
                                    fat.valor_contractors_brl,
                                    fat.valor_licenses_currency,
                                    fat.valor_licenses_brl,
                                    fat.valor_bonus_currency,
                                    fat.valor_bonus_brl,
                                    fat.revenue_source
                             from ( select i.cd_contrato_pratica,
                                           i.nome_contrato_pratica,
                                           i.dt_mes_comparacao dt_mes,
                                           i.desc_fonte_receita revenue_source,
                                           i.profit_center_type,
                                           case when i.desc_fonte_receita = 'Expenses Reimb' then i.valor_item else 0 end  valor_reembolso_currency,
                                           case when i.desc_fonte_receita = 'Expenses Reimb' then i.valor_item_brl else 0 end  valor_reembolso_brl,
                                           case when i.desc_fonte_receita = 'Contractors' then i.valor_item else 0 end  valor_contractors_currency,
                                           case when i.desc_fonte_receita = 'Contractors' then i.valor_item_brl else 0 end  valor_contractors_brl,
                                           case when i.desc_fonte_receita = 'Licenses' then i.valor_item else 0 end  valor_licenses_currency,
                                           case when i.desc_fonte_receita = 'Licenses' then i.valor_item_brl else 0 end  valor_licenses_brl,
                                           case when i.desc_fonte_receita = 'Bonus'  then i.valor_item else 0 end  valor_bonus_currency,
                                           case when i.desc_fonte_receita = 'Bonus'  then i.valor_item_brl else 0 end  valor_bonus_brl
                                    from ( select pi.cd_contrato_pratica,
                                                  pi.nome_contrato_pratica,
                                                  pi.valor_item,
                                                  pi.invoice_status,
                                                  pi.fore_nm_fonte_receita desc_fonte_receita,
                                                  pi.clie_nm_cliente_pai,
                                                  pi.dt_mes_comparacao,
                                                  pi.profit_center,
                                                  pi.profit_center_type,
                                                  sum(decode(pi.currency_acronym, 'BRL', 1, 'USD', 1.8, 'GBP', 3.02, 'EUR', 2.44, 1) * ( pi.valor_item ) ) valor_item_brl
                                           from vw_bi_pms_invoice pi
                                           group by pi.cd_contrato_pratica,
                                                    pi.nome_contrato_pratica,
                                                    pi.valor_item,
                                                    pi.invoice_status,
                                                    pi.fore_nm_fonte_receita,
                                                    pi.clie_nm_cliente_pai,
                                                    pi.dt_mes_comparacao,
                                                    pi.profit_center,
                                                    pi.profit_center_type ) i
                                    where profit_center_type = 'UMKT'  ) fat ) fat,
                           ( select inf2.cd_contrato_pratica,
                                    inf2.nome_contrato_pratica,
                                    inf2.pratica,
                                    inf2.dt_mes,
                                    inf2.aloc_in_estagio,
                                    inf2.vlr_preco vlr_preco,
                                    inf2.resource_code,
                                    inf2.resource_name,
                                    inf2.valor_cotacao,
                                    inf2.cd_moeda,
                                    inf2.moeda,
                                    inf2.umkt,
                                    inf2.bm,
                                    inf2.sso,
                                    inf2.perfil_vendido,
                                    inf2.perfil_padrao,
                                    case when inf2.valor_fte_ut_rate > 0
                                              then inf2.vlr_preco / nvl( decode( inf2.valor_fte_ut_rate, 0, 1 ), 1 )
                                              else inf2.vlr_preco end vlr_preco_fte,
                                    inf2.valor_fte valor_fte,
                                    inf2.valor_ut_rate,
                                    inf2.valor_fte_ut_rate,
                                    inf2.source,
                                    inf2.mes_fechto_revenue,
                                    inf2.cd_receita,
                                    inf2.nome_perfil_vendido,
                                    inf2.cd_grupo_custo,
                                    inf2.nm_grupo_custo,
                                    inf2.cd_cidade_base_aloc,
                                    inf2.sigla_cidade_base_aloc
                             from ( select inf1.origem,
                                           inf1.cd_contrato_pratica,
                                           inf1.nome_contrato_pratica,
                                           inf1.pratica,
                                           inf1.dt_mes,
                                           inf1.aloc_in_estagio,
                                           inf1.flag_mostra_almap,
                                           inf1.vlr_preco_revenue,
                                           inf1.resource_code,
                                           inf1.resource_name,
                                           inf1.valor_cotacao,
                                           inf1.cd_moeda,
                                           inf1.moeda,
                                           inf1.umkt,
                                           inf1.bm,
                                           inf1.sso,
                                           inf1.perfil_vendido,
                                           inf1.perfil_padrao,
                                           case when inf1.source = 'REVENUE' and origem = 'Q11'
                                                     then vlr_preco_revenue
                                                when inf1.source = 'REVENUE' and origem = 'Q12'
                                                     then ( case when vlr_preco_al_map is not null
                                                                      then vlr_preco_al_map
                                                                      else vlr_preco_revenue end )
                                                -- Somente traz o preco, REVENUES de meses fechados, e retorna 0, se o mes n?o esta
                                                -- fechado e n?o tem receita, n?o mostrando mais do ALLOCATION MAP
                                                else case when inf1.flag_mostra_almap = 'S'
                                                               then sum( vlr_preco_al_map ) over( partition by inf1.cd_contrato_pratica, inf1.dt_mes, inf1.aloc_in_estagio, inf1.perfil_vendido, inf1.resource_code )
                                                               else 0 end end vlr_preco,
                                           case when inf1.valor_fte is null and inf1.source = 'REVENUE'
                                                     then ( select prec1.valor_fte
                                                            from vw_planejado_receita prec1
                                                            where prec1.cd_contrato_pratica = inf1.cd_contrato_pratica and
                                                                  prec1.dt_mes              = inf1.dt_mes and
                                                                  rownum = 1 )
                                                     else inf1.valor_fte end valor_fte,
                                           inf1.valor_ut_rate,
                                           inf1.valor_fte_ut_rate  valor_fte_ut_rate,
                                           inf1.source,
                                           inf1.mes_fechto_revenue,
                                           inf1.cd_receita,
                                           inf1.nome_perfil_vendido,
                                           inf1.cd_grupo_custo,
                                           inf1.nm_grupo_custo,
                                           inf1.cd_cidade_base_aloc,
                                           inf1.sigla_cidade_base_aloc,
                                           row_number() over( partition by inf1.cd_contrato_pratica, inf1.dt_mes, inf1.aloc_in_estagio, inf1.perfil_vendido, inf1.resource_code
                                                              order by inf1.cd_contrato_pratica, inf1.dt_mes, inf1.aloc_in_estagio desc, inf1.perfil_vendido, inf1.resource_code ) nlinha
                                    from ( select origem,
                                                  case when cd_cont_pratica_rev is not null
                                                            then cd_cont_pratica_rev
                                                            else cd_cont_pratica_almap end cd_contrato_pratica,
                                                  case when cd_cont_pratica_rev is not null
                                                            then nome_cont_pratica_rev
                                                            else nome_cont_pratica_almap end nome_contrato_pratica,
                                                  case when cd_cont_pratica_rev is not null
                                                            then pratica_rev
                                                            else pratica_almap end pratica,
                                                  case when cd_cont_pratica_rev is not null
                                                            then dt_mes_rev
                                                            else dt_mes_al_map end dt_mes,
                                                  case when cd_cont_pratica_rev is not null
                                                            then resource_code_rev
                                                            else resource_code_al_map end resource_code,
                                                  case when cd_cont_pratica_rev is not null
                                                            then resource_name_rev
                                                            else resource_name_al_map end resource_name,
                                                  case when cd_cont_pratica_rev is not null
                                                            then valor_cotacao_rev
                                                            else valor_cotacao_al_map end valor_cotacao,
                                                  case when cd_cont_pratica_rev is not null
                                                            then cd_moeda_rev
                                                            else cd_moeda_al_map end cd_moeda,
                                                  case when cd_cont_pratica_rev is not null
                                                            then moeda_rev
                                                            else moeda_al_map end moeda,
                                                  case when cd_cont_pratica_rev is not null
                                                            then umkt_rev
                                                            else umkt_al_map end umkt,
                                                  case when cd_cont_pratica_rev is not null
                                                            then bm_rev
                                                            else bm_al_map end bm,
                                                  case when cd_cont_pratica_rev is not null
                                                            then sso_rev
                                                            else sso_al_map end sso,
                                                  case when cd_cont_pratica_rev is not null
                                                            then perfil_vendido_rev
                                                            else perfil_vendido_al_map end perfil_vendido,
                                                  case when cd_cont_pratica_rev is not null
                                                            then perfil_padrao_rev
                                                            else perfil_padrao_al_mal end perfil_padrao,
                                                  vlr_preco_revenue,
                                                  vlr_preco_al_map,
                                                  case when ( case when cd_cont_pratica_rev is not null
                                                                        then dt_mes_rev
                                                                        else dt_mes_al_map end ) > mes_fechto_revenue
                                                            then 'S'
                                                            else ( case when ( case when cd_cont_pratica_rev is not null
                                                                                          then dt_mes_rev
                                                                                          else dt_mes_al_map end ) > mes_fechto_revenue
                                                                              then 'N' --vlr_ut_rate_al_map
                                                                              else 'S' end ) end flag_mostra_almap, --'N' end flag_mostra_almap,
                                                  case when cd_cont_pratica_rev is not null and origem = 'Q11'
                                                            then valor_fte_rev
                                                            else ( case when ( case when cd_cont_pratica_rev is not null
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
                                                                             else ( case when origem = 'Q12'
                                                                                              then 'REVENUE'
                                                                                              else null end ) end )
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
                                                  -- Verificac?o se o mes atual ( ALMAP ou REVENUE ) e maior que o ultimo mes Revenue fechado,
                                                  -- para que n?o mostre informac?es de ALMAP, para meses fechados, mesmo que n?o tenham Revenue
                                                  -- O mesmo e feito nas colunas de Valor e Source acima
                                                  valor_fte_al_map valor_fte,
                                                  mes_fechto_revenue,
                                                  cd_receita,
                                                  nome_perfil_vendido,
                                                  cd_cidade_base_aloc,
                                                  sigla_cidade_base_aloc,
                                                  case when cd_grupo_custo_rev is not null
                                                                   then cd_grupo_custo_rev
                                                                   else cd_grupo_custo_al_map end cd_grupo_custo,
                                                              case when cd_grupo_custo_rev is not null
                                                                   then nm_grupo_custo_rev
                                                                   else nm_grupo_custo_al_map end nm_grupo_custo
                                           from ( select 'Q11'                              origem,
                                                         pl.source                         source_al_map,
                                                         pl.cd_contrato_pratica            cd_cont_pratica_almap,
                                                         pl.nome_contrato_pratica          nome_cont_pratica_almap,
                                                         pl.pratica                        pratica_almap,
                                                         pl.dt_mes                         dt_mes_al_map,
                                                         pl.aloc_in_estagio                aloc_in_estagio,
                                                         pl.vlr_preco                      vlr_preco_al_map,
                                                         pl.valor_fte                      valor_fte_al_map,
                                                         pl.valor_fte_ut_rate / nvl( decode( pl.valor_fte, 0, 1 ), 1 ) vlr_ut_rate_al_map,
                                                         pl.valor_fte_ut_rate              vlr_fte_ut_rate_al_map,
                                                         pl.resource_code                  resource_code_al_map,
                                                         pl.resource_name                  resource_name_al_map,
                                                         pl.valor_cotacao                  valor_cotacao_al_map,
                                                         pl.cd_moeda                       cd_moeda_al_map,
                                                         pl.moeda                          moeda_al_map,
                                                         pl.umkt                           umkt_al_map,
                                                         pl.bm                             bm_al_map,
                                                         pl.sso                            sso_al_map,
                                                         pl.cd_grupo_custo                 cd_grupo_custo_al_map,
                                                         pl.nm_grupo_custo                 nm_grupo_custo_al_map,
                                                         pl.perfil_vendido                 perfil_vendido_al_map,
                                                         pl.perfil_padrao                  perfil_padrao_al_mal,
                                                         pl.cd_cidade_base_aloc,
                                                         pl.sigla_cidade_base_aloc,
                                                         --
                                                         rec.source                        source_rev,
                                                         rec.cd_contrato_pratica           cd_cont_pratica_rev,
                                                         rec.nome_contrato_pratica         nome_cont_pratica_rev,
                                                         rec.pratica                       pratica_rev,
                                                         rec.dt_mes                        dt_mes_rev,
                                                         rec.vlr_preco                     vlr_preco_revenue,
                                                         rec.valor_fte_ut_rate             vlr_fte_ut_rate_rev,
                                                         rec.valor_fte                     valor_fte_rev,
                                                         rec.resource_code                 resource_code_rev,
                                                         rec.resource_name                 resource_name_rev,
                                                         rec.valor_cotacao                 valor_cotacao_rev,
                                                         rec.cd_moeda                      cd_moeda_rev,
                                                         rec.moeda                         moeda_rev,
                                                         rec.umkt                          umkt_rev,
                                                         rec.bm                            bm_rev,
                                                         rec.sso                           sso_rev,
                                                         rec.rece_cd_receita               cd_receita,
                                                         -- Alterado por Anderia - Default Profile -------------
                                                         case when rec.nome_perfil_vendido is not null
                                                              then  rec.nome_perfil_vendido
                                                              else pl.nome_perfil_vend end nome_perfil_vendido,
                                                         -------------------------------------------------------
                                                         rec.perfil_vendido                perfil_vendido_rev,
                                                         rec.perfil_padrao                 perfil_padrao_rev,
                                                         rec.cd_grupo_custo                cd_grupo_custo_rev,
                                                         rec.nm_grupo_custo                nm_grupo_custo_rev,
                                                         -- Coluna existente aqui, para considerar que mes fechado que n?o tenha REVENUE,
                                                         -- n?o deve ser mostrada tambem, a alocac?o do mesmo.
                                                         ( select md.modu_dt_fechamento
                                                           from modulo md
                                                           where md.modu_nm_modulo = 'Revenue' ) mes_fechto_revenue
                                                  from ( select rec.cd_contrato_pratica,
                                                                rec.nome_contrato_pratica,
                                                                rec.pratica,
                                                                rec.dt_mes,
                                                                rec.vlr_preco,
                                                                sum( rec.valor_fte ) over( partition by rec.cd_contrato_pratica, rec.dt_mes, rec.perfil_vendido, rec.resource_code )  valor_fte,
                                                                0 valor_ut_rate,
                                                                rec.valor_fte valor_fte_ut_rate,
                                                                'REVENUE' source,
                                                                rec.resource_code,
                                                                rec.resource_name,
                                                                rec.valor_cotacao,
                                                                rec.cd_moeda,
                                                                rec.moeda,
                                                                rec.umkt,
                                                                rec.bm,
                                                                rec.sso,
                                                                rec.rece_cd_receita,
                                                                rec.nome_perfil_vendido,
                                                                rec.cd_grupo_custo,
                                                                rec.nm_grupo_custo,
                                                                rec.perfil_vendido,
                                                                rec.perfil_padrao
                                                         from vw_realizado_receita rec
                                                         --where --rec.nome_contrato_pratica = 'Coca Cola-ADS' and
                                                               /*rec.dt_mes = '01-jan-2010'*/ ) rec,
                                                       ( select pl.cd_contrato_pratica,
                                                                pl.nome_contrato_pratica,
                                                                pl.pratica,
                                                                pl.dt_mes,
                                                                pl.vlr_preco,
                                                                sum( pl.valor_fte ) over( partition by pl.cd_contrato_pratica, pl.dt_mes, pl.aloc_in_estagio, pl.resource_code, pl.perfil_vendido ) valor_fte,
                                                                pl.valor_ut_rate valor_ut_rate,
                                                                sum( pl.valor_fte_ut_rate ) over( partition by pl.cd_contrato_pratica, pl.dt_mes, pl.aloc_in_estagio, pl.resource_code, pl.perfil_vendido ) valor_fte_ut_rate,
                                                                pl.aloc_in_estagio,
                                                                'ALLOCATION MAP' source,
                                                                pl.resource_code,
                                                                pl.resource_name,
                                                                pl.valor_cotacao,
                                                                pl.cd_moeda,
                                                                pl.moeda,
                                                                pl.umkt,
                                                                pl.bm,
                                                                pl.sso,
                                                                pl.cd_grupo_custo,
                                                                pl.nm_grupo_custo,
                                                                pl.perfil_vendido,
                                                                pl.perfil_padrao,
                                                                pl.nome_perfil_vend,
                                                                pl.cd_cidade_base_aloc,
                                                                pl.sigla_cidade_base_aloc
                                                         from vw_planejado_receita pl
                                                         /*where pl.nome_contrato_pratica like 'ForecastTorreNilton%' and
                                                               pl.dt_mes = '01-jan-2013'*/ ) pl
                                                  where pl.cd_contrato_pratica = rec.cd_contrato_pratica (+) and
                                                        pl.dt_mes              = rec.dt_mes (+) and
                                                        pl.resource_code       = rec.resource_code (+) ) --correto
                                           union all
                                           select origem,
                                                  case when cd_cont_pratica_rev is not null
                                                            then cd_cont_pratica_rev
                                                            else cd_cont_pratica_almap end cd_contrato_pratica,
                                                  case when cd_cont_pratica_rev is not null
                                                            then nome_cont_pratica_rev
                                                            else nome_cont_pratica_almap end nome_contrato_pratica,
                                                  case when cd_cont_pratica_rev is not null
                                                            then pratica_rev
                                                            else pratica_almap end pratica,
                                                  case when cd_cont_pratica_rev is not null
                                                            then dt_mes_rev
                                                            else dt_mes_al_map end dt_mes,
                                                  case when cd_cont_pratica_rev is not null
                                                            then resource_code_rev
                                                            else resource_code_al_map end resource_code,
                                                  case when cd_cont_pratica_rev is not null
                                                            then resource_name_rev
                                                            else resource_name_al_map end resource_name,
                                                  case when cd_cont_pratica_rev is not null
                                                            then valor_cotacao_rev
                                                            else valor_cotacao_al_map end valor_cotacao,
                                                  case when cd_cont_pratica_rev is not null
                                                            then cd_moeda_rev
                                                            else cd_moeda_al_map end cd_moeda,
                                                  case when cd_cont_pratica_rev is not null
                                                            then moeda_rev
                                                            else moeda_al_map end moeda,
                                                  case when cd_cont_pratica_rev is not null
                                                            then umkt_rev
                                                            else umkt_al_map end umkt,
                                                  case when cd_cont_pratica_rev is not null
                                                            then bm_rev
                                                            else bm_al_map end bm,
                                                  case when cd_cont_pratica_rev is not null
                                                            then sso_rev
                                                            else sso_al_map end bm,
                                                  case when cd_cont_pratica_rev is not null
                                                            then perfil_vendido_rev
                                                            else perfil_padrao_al_map end perfil_vendido,
                                                  case when cd_cont_pratica_rev is not null
                                                            then perfil_padrao_rev
                                                            else perfil_padrao_al_map end perfil_padrao,
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
                                                                             else ( case when origem = 'Q12'
                                                                                              then 'REVENUE'
                                                                                              else null end ) end )
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
                                                  -- Verificac?o se o mes atual ( ALMAP ou REVENUE ) e maior que o ultimo mes Revenue fechado,
                                                  -- para que n?o mostre informac?es de ALMAP, para meses fechados, mesmo que n?o tenham Revenue
                                                  -- O mesmo e feito nas colunas de Valor e Source acima
                                                  valor_fte_al_map valor_fte,
                                                  mes_fechto_revenue,
                                                  cd_receita,
                                                  nome_perfil_vendido,
                                                  cd_cidade_base_aloc,
                                                  sigla_cidade_base_aloc,
                                                  case when cd_grupo_custo_rev is not null
                                                                   then cd_grupo_custo_rev
                                                                   else cd_grupo_custo_al_map end cd_grupo_custo,
                                                              case when cd_grupo_custo_rev is not null
                                                                   then nm_grupo_custo_rev
                                                                   else nm_grupo_custo_al_map end nm_grupo_custo
                                           from ( select 'Q12'                                   origem,
                                                         rec.source                              source_rev,
                                                         rec.cd_contrato_pratica                 cd_cont_pratica_rev,
                                                         rec.nome_contrato_pratica               nome_cont_pratica_rev,
                                                         rec.pratica                             pratica_rev,
                                                         rec.dt_mes                              dt_mes_rev,
                                                         'Commited'                              aloc_in_estagio,
                                                         rec.vlr_preco                           vlr_preco_revenue,
                                                         sum( rec.valor_fte ) over( partition by rec.cd_contrato_pratica, rec.dt_mes, rec.resource_code )  valor_fte_rev,
                                                         rec.valor_fte_ut_rate / nvl( decode( rec.valor_fte, 0, 1 ), 1 )   vlr_fte_ut_rate_rev,
                                                         rec.valor_fte_ut_rate                   vlr_fte_ut_rate_al_map,
                                                         rec.resource_code                       resource_code_rev,
                                                         rec.resource_name                       resource_name_rev,
                                                         rec.valor_cotacao                       valor_cotacao_rev,
                                                         rec.cd_moeda                            cd_moeda_rev,
                                                         rec.moeda                               moeda_rev,
                                                         rec.umkt                                umkt_rev,
                                                         rec.bm                                  bm_rev,
                                                         rec.sso                                 sso_rev,
                                                         rec.rece_cd_receita                     cd_receita,
                                                         rec.nome_perfil_vendido                 nome_perfil_vendido,
                                                         rec.cd_grupo_custo                      cd_grupo_custo_rev,
                                                         rec.nm_grupo_custo                      nm_grupo_custo_rev,
                                                         rec.perfil_vendido                      perfil_vendido_rev,
                                                         rec.perfil_padrao                       perfil_padrao_rev,
                                                         --
                                                         pl.source                               source_al_map,
                                                         pl.cd_contrato_pratica                  cd_cont_pratica_almap,
                                                         pl.nome_contrato_pratica                nome_cont_pratica_almap,
                                                         pl.pratica                              pratica_almap,
                                                         pl.dt_mes                               dt_mes_al_map,
                                                         pl.vlr_preco                            vlr_preco_al_map,
                                                         pl.valor_fte_ut_rate                    vlr_ut_rate_al_map,
                                                         pl.valor_fte                            valor_fte_al_map,
                                                         pl.resource_code                        resource_code_al_map,
                                                         pl.resource_name                        resource_name_al_map,
                                                         pl.valor_cotacao                        valor_cotacao_al_map,
                                                         pl.cd_moeda                             cd_moeda_al_map,
                                                         pl.moeda                                moeda_al_map,
                                                         pl.umkt                                 umkt_al_map,
                                                         pl.bm                                   bm_al_map,
                                                         pl.sso                                  sso_al_map,
                                                         pl.perfil_vendido                       perfil_vendido_al_map,
                                                         pl.perfil_padrao                        perfil_padrao_al_map,
                                                         pl.cd_grupo_custo                       cd_grupo_custo_al_map,
                                                         pl.nm_grupo_custo                       nm_grupo_custo_al_map,
                                                         pl.cd_cidade_base_aloc,
                                                         pl.sigla_cidade_base_aloc,
                                                         ( select md.modu_dt_fechamento
                                                           from modulo md
                                                           where md.modu_nm_modulo = 'Revenue' ) mes_fechto_revenue
                                                  from ( select rec.cd_contrato_pratica,
                                                                rec.nome_contrato_pratica,
                                                                rec.pratica,
                                                                rec.dt_mes,
                                                                rec.vlr_preco,
                                                                rec.valor_fte  valor_fte,
                                                                0 valor_ut_rate,
                                                                rec.valor_fte valor_fte_ut_rate,
                                                                'REVENUE' source,
                                                                rec.resource_code,
                                                                rec.resource_name,
                                                                rec.valor_cotacao,
                                                                rec.cd_moeda,
                                                                rec.moeda,
                                                                rec.umkt,
                                                                rec.bm,
                                                                rec.sso,
                                                                rec.rece_cd_receita,
                                                                rec.nome_perfil_vendido,
                                                                rec.cd_grupo_custo,
                                                                rec.nm_grupo_custo,
                                                                rec.perfil_vendido,
                                                                rec.perfil_padrao
                                                         from vw_realizado_receita rec ) rec,
                                                       ( select pl.cd_contrato_pratica,
                                                                pl.nome_contrato_pratica,
                                                                pl.pratica,
                                                                pl.dt_mes,
                                                                pl.vlr_preco,
                                                                sum( pl.valor_fte ) over( partition by pl.cd_contrato_pratica, pl.dt_mes, pl.aloc_in_estagio, pl.resource_code ) valor_fte,
                                                                pl.valor_ut_rate valor_ut_rate,
                                                                pl.valor_fte_ut_rate valor_fte_ut_rate,
                                                                pl.aloc_in_estagio,
                                                                'ALLOCATION MAP' source,
                                                                pl.resource_code,
                                                                pl.resource_name,
                                                                pl.valor_cotacao,
                                                                pl.cd_moeda,
                                                                pl.moeda,
                                                                pl.umkt,
                                                                pl.bm,
                                                                pl.sso,
                                                                pl.cd_grupo_custo,
                                                                pl.nm_grupo_custo,
                                                                pl.perfil_vendido,
                                                                pl.perfil_padrao,
                                                                pl.nome_perfil_vend,
                                                                pl.cd_cidade_base_aloc,
                                                                pl.sigla_cidade_base_aloc
                                                         from vw_planejado_receita pl
                                                          ) pl
                                                  where rec.cd_contrato_pratica = pl.cd_contrato_pratica (+) and
                                                        rec.dt_mes              = pl.dt_mes (+)  and
                                                        rec.resource_code       = pl.resource_code (+) and
                                                        pl.cd_contrato_pratica is null ) ) inf1
                                     where inf1.source in ( 'REVENUE', 'ALLOCATION MAP' ) ) inf2
                             where nlinha = 1  ) inf_rec
                      where inf_rec.cd_contrato_pratica = fat.cd_contrato_pratica (+) and
                            inf_rec.dt_mes              = fat.dt_mes (+) /*and --inf_rec.cd_contrato_pratica = 382 and
                            inf_rec.dt_mes = '01-jan-2010' */
                             )

               where nlinha = 1

               union all
               -- Registros que tem invoices mas n?o tem revenues
               select inf.origem,
                      inf.cd_contrato_pratica,
                      inf.nome_contrato_pratica,
                      inf.pratica,
                      inf.dt_mes,
                      inf.stage,
                      inf.resource_code,
                      inf.resource_name,
                      inf.valor_cotacao,
                      inf.cd_moeda,
                      inf.moeda,
                      cpcl.umkt,
                      inf.bm,
                      inf.sso,
                      null perfil_vendido,
                      null perfil_padrao,
                      inf.vlr_preco,
                      inf.vlr_preco_currency,
                      inf.vlr_preco_fte,
                      inf.valor_fte,
                      inf.valor_ut_rate,
                      inf.valor_fte_ut_rate,
                      inf.source,
                      inf.mes_fechto_revenue,
                      -- Informac?es de TCE Orcado X Realizado
                      inf.valor_tce,
                      -- Invoices
                      inf.revenue_source,
                      --
                      inf.valor_reembolso_currency,
                      inf.valor_reembolso_brl,
                      --
                      inf.valor_contractors_currency,
                      inf.valor_contractors_brl,
                      --
                      inf.valor_licenses_currency,
                      inf.valor_licenses_brl,
                      --
                      inf.valor_bonus_currency,
                      inf.valor_bonus_brl,
                      0 cd_receita,
                      null nome_perfil_vendido,
                      cd_grupo_custo,
                      nm_grupo_custo,
                      0 cd_cidade_base_aloc,
                      null sigla_cidade_base_aloc
               from ( select 'Q2' origem,
                             fat.cd_contrato_pratica,
                             fat.nome_contrato_pratica,
                             fat.pratica,
                             fat.dt_mes,
                             'Commited' stage,
                             null resource_code,
                             null resource_name,
                             0  valor_cotacao,
                             0 cd_moeda,
                             null moeda,
                             cpcl_bm.bm,
                             cpcl_sso.sso,
                             0 vlr_preco,  -->> como n?o tem Revenue, deve trazer do Allocation Map ?
                             0 vlr_preco_currency,
                             null vlr_preco_fte,
                             null valor_fte,
                             null valor_ut_rate,
                             null valor_fte_ut_rate,
                             'Invoice' source,
                             ( select md.modu_dt_fechamento
                               from modulo md
                               where md.modu_nm_modulo = 'Revenue' ) mes_fechto_revenue,
                             -- Informac?es de TCE Orcado X Realizado
                             0 valor_tce,
                             -- Invoices
                             fat.revenue_source,
                             --
                             valor_reembolso_currency,
                             valor_reembolso_brl,
                             --
                             valor_contractors_currency,
                             valor_contractors_brl,
                             --
                             valor_licenses_currency,
                             valor_licenses_brl,
                             --
                             valor_bonus_currency,
                             valor_bonus_brl,
                             rec.cd_grupo_custo,
                             rec.nm_grupo_custo
                      from ( select fat.cd_contrato_pratica,
                                    fat.nome_contrato_pratica,
                                    fat.pratica,
                                    fat.dt_mes,
                                    fat.valor_reembolso_currency,
                                    fat.valor_reembolso_brl,
                                    fat.valor_contractors_currency,
                                    fat.valor_contractors_brl,
                                    fat.valor_licenses_currency,
                                    fat.valor_licenses_brl,
                                    fat.valor_bonus_currency,
                                    fat.valor_bonus_brl,
                                    fat.revenue_source
                             from ( select fat.cd_contrato_pratica,
                                           fat.nome_contrato_pratica,
                                           fat.pratica,
                                           fat.dt_mes,
                                           fat.valor_reembolso_currency,
                                           fat.valor_reembolso_brl,
                                           fat.valor_contractors_currency,
                                           fat.valor_contractors_brl,
                                           fat.valor_licenses_currency,
                                           fat.valor_licenses_brl,
                                           fat.valor_bonus_currency,
                                           fat.valor_bonus_brl,
                                           fat.revenue_source
                                    from ( select i.cd_contrato_pratica,
                                                  i.nome_contrato_pratica,
                                                  i.pratica,
                                                  i.dt_mes_comparacao dt_mes,
                                                  i.desc_fonte_receita revenue_source,
                                                  i.profit_center_type,
                                                  case when i.desc_fonte_receita = 'Expenses Reimb' then i.valor_item else 0 end  valor_reembolso_currency,
                                                  case when i.desc_fonte_receita = 'Expenses Reimb' then i.valor_item_brl else 0 end  valor_reembolso_brl,
                                                  case when i.desc_fonte_receita = 'Contractors' then i.valor_item else 0 end  valor_contractors_currency,
                                                  case when i.desc_fonte_receita = 'Contractors' then i.valor_item_brl else 0 end  valor_contractors_brl,
                                                  case when i.desc_fonte_receita = 'Licenses' then i.valor_item else 0 end  valor_licenses_currency,
                                                  case when i.desc_fonte_receita = 'Licenses' then i.valor_item_brl else 0 end  valor_licenses_brl,
                                                  case when i.desc_fonte_receita = 'Bonus'  then i.valor_item else 0 end  valor_bonus_currency,
                                                  case when i.desc_fonte_receita = 'Bonus'  then i.valor_item_brl else 0 end  valor_bonus_brl
                                           from ( select pi.cd_contrato_pratica,
                                                         pi.nome_contrato_pratica,
                                                         prat.prat_nm_pratica pratica,
                                                         pi.valor_item,
                                                         pi.invoice_status,
                                                         pi.fore_nm_fonte_receita desc_fonte_receita,
                                                         pi.clie_nm_cliente_pai,
                                                         pi.dt_mes_comparacao,
                                                         pi.profit_center,
                                                         pi.profit_center_type,
                                                         sum(decode(pi.currency_acronym, 'BRL', 1, 'USD', 1.8, 'GBP', 3.02, 'EUR', 2.44, 1) * ( pi.valor_item ) ) valor_item_brl
                                                  from vw_bi_pms_invoice pi,
                                                       contrato_pratica cp,
                                                       pratica prat
                                                  where cp.copr_cd_contrato_pratica = pi.cd_contrato_pratica (+) and
                                                        cp.prat_cd_pratica          = prat.prat_cd_pratica
                                                  group by pi.cd_contrato_pratica,
                                                           pi.nome_contrato_pratica,
                                                           prat.prat_nm_pratica,
                                                           pi.valor_item,
                                                           pi.invoice_status,
                                                           pi.fore_nm_fonte_receita,
                                                           pi.clie_nm_cliente_pai,
                                                           pi.dt_mes_comparacao,
                                                           pi.profit_center,
                                                           pi.profit_center_type ) i
                                           where profit_center_type = 'UMKT'  ) fat  ) fat ) fat,
                                    vw_realizado_receita rec,
                                  ( select cpcl.copr_cd_contrato_pratica,
                                           cpcl.celu_cd_centro_lucro,
                                           cpcl.cpcl_dt_inicio_vigencia,
                                           case when cpcl.cpcl_dt_fim_vigencia is not null
                                                     then cpcl.cpcl_dt_fim_vigencia
                                                     else ( select para_dt_parametro
                                                            from parametro p1
                                                            where p1.para_cd_parametro = 1 ) end cpcl_dt_fim_vigencia,
                                           cl.celu_nm_centro_lucro bm
                                    from contrato_pratica_centro_lucro cpcl,
                                         centro_lucro cl
                                    where cpcl.celu_cd_centro_lucro = cl.celu_cd_centro_lucro and
                                          cl.nacl_cd_natureza = 3 ) cpcl_bm,
                                  ( select cpcl.copr_cd_contrato_pratica,
                                           cpcl.celu_cd_centro_lucro,
                                           cpcl.cpcl_dt_inicio_vigencia,
                                           case when cpcl.cpcl_dt_fim_vigencia is not null
                                                     then cpcl.cpcl_dt_fim_vigencia
                                                     else ( select para_dt_parametro
                                                            from parametro p1
                                                            where p1.para_cd_parametro = 1 ) end cpcl_dt_fim_vigencia,
                                           cl.celu_nm_centro_lucro sso
                                    from contrato_pratica_centro_lucro cpcl,
                                         centro_lucro cl
                                    where cpcl.celu_cd_centro_lucro = cl.celu_cd_centro_lucro and
                                          cl.nacl_cd_natureza = 2 ) cpcl_sso
                      where fat.cd_contrato_pratica = rec.cd_contrato_pratica (+) and
                            fat.dt_mes              = rec.dt_mes (+) and
                            fat.cd_contrato_pratica = cpcl_bm.copr_cd_contrato_pratica (+) and
                            fat.dt_mes between cpcl_bm.cpcl_dt_inicio_vigencia (+) and cpcl_bm.cpcl_dt_fim_vigencia (+) and
                            fat.cd_contrato_pratica = cpcl_sso.copr_cd_contrato_pratica (+) and
                            fat.dt_mes between cpcl_sso.cpcl_dt_inicio_vigencia (+) and cpcl_sso.cpcl_dt_fim_vigencia (+) and
                            rec.dt_mes is null ) inf,
                    ( select cpcl.copr_cd_contrato_pratica,
                             cpcl.celu_cd_centro_lucro,
                             cpcl.cpcl_dt_inicio_vigencia,
                             case when cpcl.cpcl_dt_fim_vigencia is not null
                                       then cpcl.cpcl_dt_fim_vigencia
                                       else ( select para_dt_parametro
                                              from parametro p1
                                              where p1.para_cd_parametro = 1 ) end cpcl_dt_fim_vigencia,
                             cl.celu_nm_centro_lucro umkt
                      from contrato_pratica_centro_lucro cpcl,
                           centro_lucro cl
                      where cpcl.celu_cd_centro_lucro = cl.celu_cd_centro_lucro and
                            cl.nacl_cd_natureza = 1 ) cpcl
               where inf.cd_contrato_pratica = cpcl.copr_cd_contrato_pratica (+) and
                     inf.dt_mes between cpcl.cpcl_dt_inicio_vigencia (+) and cpcl.cpcl_dt_fim_vigencia (+)
               union all
               ------------------------------------------------------------------------------------------------------------------------
               -- Query que retorna registro de colaboradores rescindidos, para que seja possivel projetar os custos corretamente no
               -- mes da rescis?o, pois n?o deixa de ser um mes trabalhado.
               ------------------------------------------------------------------------------------------------------------------------
               select origem,
                      cd_contrato_pratica,
                      nome_contrato_pratica,
                      pratica,
                      dt_mes,
                      stage,
                      resource_code,
                      resource_name,
                      valor_cotacao,
                      cd_moeda,
                      moeda,
                      umkt,
                      bm,
                      sso,
                      null perfil_vendido,
                      null perfil_padrao,
                      vlr_preco,
                      vlr_preco_currency,
                      vlr_preco_fte,
                      valor_fte,
                      valor_ut_rate,
                      valor_fte_ut_rate,
                      source,
                      mes_fechto_revenue,
                      ----------------------------------------------------
                      -- Informac?es de TCE Orcado X Realizado
                      ----------------------------------------------------
                      valor_tce,
                      ----------------------------------------------------
                      -- Invoices
                      ----------------------------------------------------
                      revenue_source,
                      --
                      valor_reembolso_currency,
                      valor_reembolso_brl,
                      --
                      valor_contractors_currency,
                      valor_contractors_brl,
                      --
                      valor_licenses_currency,
                      valor_licenses_brl,
                      --
                      valor_bonus_currency,
                      valor_bonus_brl,
                      0 cd_receita,
                      null nome_perfil_vendido,
                      cd_grupo_custo,
                      nm_grupo_custo,
                      0 cd_cidade_base_aloc,
                      null sigla_cidade_base_aloc
               from ( select inf.origem,
                             inf.cd_contrato_pratica,
                             inf.nome_contrato_pratica,
                             inf.pratica,
                             inf.dt_mes,
                             inf.stage,
                             inf.resource_code,
                             inf.resource_name,
                             inf.valor_cotacao,
                             inf.cd_moeda,
                             inf.moeda,
                             cpcl.umkt,
                             cpcl2.bm,
                             cpcl3.sso,
                             0 vlr_preco,
                             0 vlr_preco_currency,
                             null vlr_preco_fte,
                             null valor_fte,
                             null valor_ut_rate,
                             null valor_fte_ut_rate,
                             'RES' source,
                             ( select md.modu_dt_fechamento
                               from modulo md
                               where md.modu_nm_modulo = 'Revenue' ) mes_fechto_revenue,
                             ----------------------------------------------------
                             -- Informac?es de TCE Orcado X Realizado
                             ----------------------------------------------------
                             0 valor_tce,
                             ----------------------------------------------------
                             -- Invoices
                             ----------------------------------------------------
                             null revenue_source,
                             --
                             0 valor_reembolso_currency,
                             0 valor_reembolso_brl,
                             --
                             0 valor_contractors_currency,
                             0 valor_contractors_brl,
                             --
                             0 valor_licenses_currency,
                             0 valor_licenses_brl,
                             --
                             0 valor_bonus_currency,
                             0 valor_bonus_brl,
                             gc.grcu_cd_grupo_custo cd_grupo_custo,
                             gc.grcu_nm_grupo_custo nm_grupo_custo
                      from ( select 'Q3' origem,
                                    ult_aloc.cd_contrato_pratica,
                                    ult_aloc.nome_contrato_pratica,
                                    ult_aloc.pratica,
                                    to_date( '01/' || to_char( pe3.pess_dt_rescisao, 'MM/YYYY' ), 'DD/MM/YYYY') dt_mes,
                                    null stage,
                                    pe3.pess_cd_login resource_code,
                                    pe3.pess_nm_pessoa resource_name,
                                    pe3.pess_cd_pessoa,
                                    0  valor_cotacao,
                                    cd_moeda,
                                    moeda,
                                    0 vlr_preco,
                                    0 vlr_preco_currency,
                                    null vlr_preco_fte,
                                    null valor_fte,
                                    null valor_ut_rate,
                                    null valor_fte_ut_rate,
                                    'RES' source,
                                    ( select md.modu_dt_fechamento
                                      from modulo md
                                      where md.modu_nm_modulo = 'Revenue' ) mes_fechto_revenue,
                                    ----------------------------------------------------
                                    -- Informac?es de TCE Orcado X Realizado
                                    ----------------------------------------------------
                                    0 valor_tce,
                                    ----------------------------------------------------
                                    -- Invoices
                                    ----------------------------------------------------
                                    null revenue_source,
                                    --
                                    0 valor_reembolso_currency,
                                    0 valor_reembolso_brl,
                                    --
                                    0 valor_contractors_currency,
                                    0 valor_contractors_brl,
                                    --
                                    0 valor_licenses_currency,
                                    0 valor_licenses_brl,
                                    --
                                    0 valor_bonus_currency,
                                    0 valor_bonus_brl,
                                    row_number() over( partition by ult_aloc.cd_contrato_pratica, to_date( '01/' || to_char( pe3.pess_dt_rescisao, 'MM/YYYY' ), 'DD/MM/YYYY'), pe3.pess_cd_login
                                                       order by ult_aloc.cd_contrato_pratica, to_date( '01/' || to_char( pe3.pess_dt_rescisao, 'MM/YYYY' ), 'DD/MM/YYYY'), pe3.pess_cd_login ) nlinha
                             from pessoa pe3,
                                  contrato_pratica cp,
                                  --------------------------------------------------------------------------------------
                                  -- Query que seleciona a ultima alocac?o para cada RECURSO / CLOB / MES / PRATICA
                                  --------------------------------------------------------------------------------------
                                  ( select pe2.pess_cd_pessoa,
                                           ult_aloc.recu_cd_recurso,
                                           ult_aloc.cd_contrato_pratica,
                                           ult_aloc.nome_contrato_pratica,
                                           ult_aloc.pratica,
                                           ult_aloc.dt_mes_ult_aloc,
                                           ult_aloc.cd_moeda,
                                           ult_aloc.moeda
                                    from pessoa pe2,
                                         ( select cd_contrato_pratica,
                                                  nome_contrato_pratica,
                                                  pratica,
                                                  dt_mes,
                                                  cd_moeda,
                                                  moeda,
                                                  recu_cd_recurso,
                                                  dt_mes_ult_aloc
                                           from ( select ma.copr_cd_contrato_pratica cd_contrato_pratica,
                                                         cp.copr_nm_contrato_pratica nome_contrato_pratica,
                                                         prat.prat_nm_pratica pratica,
                                                         ap.alpe_dt_alocacao_periodo dt_mes,
                                                         al.recu_cd_recurso,
                                                         m.moed_cd_moeda cd_moeda,
                                                         m.moed_sg_moeda moeda,
                                                         max( ap.alpe_dt_alocacao_periodo ) over( partition by al.recu_cd_recurso ) dt_mes_ult_aloc
                                                  from mapa_alocacao ma,
                                                       alocacao al,
                                                       alocacao_periodo ap,
                                                       contrato_pratica cp,
                                                       pratica prat,
                                                       moeda m
                                                  where ma.copr_cd_contrato_pratica = cp.copr_cd_contrato_pratica and
                                                        cp.prat_cd_pratica          = prat.prat_cd_pratica and
                                                        ma.maal_cd_mapa_alocacao    = al.maal_cd_mapa_alocacao and
                                                        al.aloc_cd_alocacao         = ap.aloc_cd_alocacao and
                                                        cp.moed_cd_moeda_padrao     = m.moed_cd_moeda ) ap
                                           where dt_mes = dt_mes_ult_aloc ) ult_aloc
                                    where pe2.recu_cd_recurso = ult_aloc.recu_cd_recurso ) ult_aloc
                             where pe3.pess_cd_pessoa           = ult_aloc.pess_cd_pessoa (+) and
                                   ult_aloc.cd_contrato_pratica = cp.copr_cd_contrato_pratica and
                                   ------------------------------------------------------------------
                                   -- Condic?o que filtra somente os colaboradores rescindidos
                                   ------------------------------------------------------------------
                                   pe3.pess_dt_rescisao is not null ) inf,
                             pessoa_grupo_custo pgc,
                             grupo_custo gc,
                           ( select cpcl.copr_cd_contrato_pratica cd_contrato_pratica,
                                    cpcl.celu_cd_centro_lucro,
                                    cpcl.cpcl_dt_inicio_vigencia,
                                    case when cpcl.cpcl_dt_fim_vigencia is not null
                                              then cpcl.cpcl_dt_fim_vigencia
                                              else ( select para_dt_parametro
                                                     from parametro p1
                                                     where p1.para_cd_parametro = 1 ) end cpcl_dt_fim_vigencia,
                                    cl.celu_nm_centro_lucro umkt
                             from contrato_pratica_centro_lucro cpcl,
                                  centro_lucro cl
                             where cpcl.celu_cd_centro_lucro = cl.celu_cd_centro_lucro and
                                  cl.nacl_cd_natureza = 1 ) cpcl,
                           ( select cpcl.copr_cd_contrato_pratica cd_contrato_pratica,
                                    cpcl.celu_cd_centro_lucro,
                                    cpcl.cpcl_dt_inicio_vigencia,
                                    case when cpcl.cpcl_dt_fim_vigencia is not null
                                              then cpcl.cpcl_dt_fim_vigencia
                                              else ( select para_dt_parametro
                                                     from parametro p1
                                                     where p1.para_cd_parametro = 1 ) end cpcl_dt_fim_vigencia,
                                    cl.celu_nm_centro_lucro bm
                             from contrato_pratica_centro_lucro cpcl,
                                  centro_lucro cl
                             where cpcl.celu_cd_centro_lucro = cl.celu_cd_centro_lucro and
                                  cl.nacl_cd_natureza = 3 ) cpcl2,
                           ( select cpcl.copr_cd_contrato_pratica cd_contrato_pratica,
                                    cpcl.celu_cd_centro_lucro,
                                    cpcl.cpcl_dt_inicio_vigencia,
                                    case when cpcl.cpcl_dt_fim_vigencia is not null
                                              then cpcl.cpcl_dt_fim_vigencia
                                              else ( select para_dt_parametro
                                                     from parametro p1
                                                     where p1.para_cd_parametro = 1 ) end cpcl_dt_fim_vigencia,
                                    cl.celu_nm_centro_lucro sso
                             from contrato_pratica_centro_lucro cpcl,
                                  centro_lucro cl
                             where cpcl.celu_cd_centro_lucro = cl.celu_cd_centro_lucro and
                                  cl.nacl_cd_natureza = 2 ) cpcl3
                    where inf.cd_contrato_pratica = cpcl.cd_contrato_pratica and
                          inf.dt_mes between cpcl.cpcl_dt_inicio_vigencia (+) and cpcl.cpcl_dt_fim_vigencia (+) and
                          inf.cd_contrato_pratica = cpcl2.cd_contrato_pratica and
                          inf.dt_mes between cpcl2.cpcl_dt_inicio_vigencia (+) and cpcl2.cpcl_dt_fim_vigencia (+) and
                          inf.cd_contrato_pratica = cpcl3.cd_contrato_pratica and
                          inf.dt_mes between cpcl3.cpcl_dt_inicio_vigencia (+) and cpcl3.cpcl_dt_fim_vigencia (+) and
                          inf.pess_cd_pessoa = pgc.pess_cd_pessoa(+) and
                                      inf.dt_mes between pgc.pegc_dt_inicio(+) and (case when pgc.pegc_dt_fim(+) is not null then
                                                                                      pgc.pegc_dt_fim(+)
                                                                                      else (select para_dt_parametro from parametro) end) and
                                      pgc.grcu_cd_grupo_custo = gc.grcu_cd_grupo_custo(+)     and
                          nlinha = 1 ) inf )inf,
          contrato_pratica cpr,
          contrato co,
          vw_bi_cliente cli
          where  inf.cd_contrato_pratica(+) = cpr.copr_cd_contrato_pratica
           and   cpr.cont_cd_contrato(+)    = co.cont_cd_contrato
           and   co.clie_cd_cliente(+)      = cli.clie_cd_cliente
) inf
union all
select
       inf.origem,
       inf.cd_contrato_pratica,
       inf.nome_contrato_pratica,
       inf.pratica,
       inf.dt_mes,
       'Adjustment' aloc_in_estagio,
       inf.resource_code,
       inf.resource_name,
       cm.como_vl_cotacao vl_cotacao,
       inf.cd_moeda,
       inf.moeda,
       inf.umkt,
       inf.bm,
       inf.sso,
       null perfil_vendido,
       null perfil_padrao,
       inf.vlr_preco,
       case
                  when cm.como_vl_cotacao is not null then
                     cm.como_vl_cotacao * inf.vlr_preco
                   else inf.vlr_preco
                end vlr_preco_brl,
       0 vlr_preco_fte,
       0 valor_fte,
       0 valor_ut_rate,
       0 valor_fte_ut_rate,
       'AJ_REC' source,
       null mes_fechto_revenue,
       -------------------------------------------------
       -- Informações de TCE Orçado X Realizado
       -------------------------------------------------
       0 valor_tce,
       -----------------------------
       -- Contractors
       -----------------------------
       null revenue_source,
       0 valor_reembolso_currency,
       0 valor_reembolso_brl,
       0 valor_contractors,
       0 valor_contractors_brl,
       0 valor_licenses_currency,
       0 valor_licenses_brl,
       0 valor_bonus_currency,
       0 valor_bonus_brl,
       0 cd_receita,
       null nome_perfil_vendido,
       0 cont_cd_contrato,
       0 clie_cd_cliente,
       0 cd_grupo_custo,
       null nome_grupo_custo,
       0 cd_cidade_base_aloc,
       null sigla_cidade_base_aloc
from   cotacao_moeda cm,
       (select inf.origem,
               inf.cd_contrato_pratica,
               inf.nome_contrato_pratica,
               inf.celu_cd_centro_lucro,
               inf.pratica,
               inf.dt_mes,
               inf.resource_code,
               inf.resource_name,
               inf.cd_moeda,
               inf.moeda,
               inf.vlr_preco,
               inf.cd_cotacao,
               inf.maxsso sso,
               inf.maxbm bm,
               inf.maxumkt umkt
        from   (select inf.origem,
                       bm.nome bm,
                       inf.umkt,
                       inf.sso,
                       inf.cd_contrato_pratica,
                       inf.nome_contrato_pratica,
                       inf.celu_cd_centro_lucro,
                       inf.pratica,
                       inf.dt_mes,
                       inf.resource_code,
                       inf.resource_name,
                       inf.cd_moeda,
                       inf.moeda,
                       inf.vlr_preco,
                       inf.cd_cotacao,
                       max(inf.sso) over(partition by inf.nome_contrato_pratica, inf.dt_mes, inf.cd_ajuste) maxsso,
                       max(bm.nome) over(partition by inf.nome_contrato_pratica, inf.dt_mes, inf.cd_ajuste) maxbm,
                       max(inf.umkt) over(partition by inf.nome_contrato_pratica, inf.dt_mes, inf.cd_ajuste) maxumkt,
                       row_number() over(partition by inf.nome_contrato_pratica, inf.dt_mes, inf.cd_ajuste order by inf.nome_contrato_pratica, inf.dt_mes, inf.cd_ajuste) nlinha
                from   (select cl.celu_cd_centro_lucro,
                               cl.celu_nm_centro_lucro nome
                        from   centro_lucro          cl,
                               natureza_centro_lucro ncl
                        where  ncl.nacl_cd_natureza = cl.nacl_cd_natureza and
                               ncl.nacl_cd_natureza = 3 --BM
                        ) bm,
                       (select inf.origem,
                               inf.cd_contrato_pratica,
                               inf.nome_contrato_pratica,
                               inf.celu_cd_centro_lucro,
                               inf.pratica,
                               inf.dt_mes,
                               inf.resource_code,
                               inf.resource_name,
                               inf.cd_moeda,
                               inf.moeda,
                               inf.umkt,
                               inf.vlr_preco,
                               sso.nome sso,
                               inf.cd_ajuste,
                               inf.cd_cotacao
                        from   (select cl.celu_cd_centro_lucro,
                                       cl.celu_nm_centro_lucro nome
                                from   centro_lucro          cl,
                                       natureza_centro_lucro ncl
                                where  ncl.nacl_cd_natureza = cl.nacl_cd_natureza and
                                       ncl.nacl_cd_natureza = 2 --SSO
                                ) sso,
                               (select inf.origem,
                                       inf.cd_contrato_pratica,
                                       inf.nome_contrato_pratica,
                                       cpl.celu_cd_centro_lucro,
                                       cpl.copr_cd_contrato_pratica,
                                       inf.pratica,
                                       inf.dt_mes,
                                       inf.resource_code,
                                       inf.resource_name,
                                       inf.cd_moeda,
                                       inf.moeda,
                                       umkt.nome umkt,
                                       inf.vlr_preco,
                                       umkt.nome sso,
                                       inf.cd_ajuste,
                                       inf.cd_cotacao
                                from   (select cl.celu_cd_centro_lucro,
                                               cl.celu_nm_centro_lucro nome
                                        from   centro_lucro          cl,
                                               natureza_centro_lucro ncl
                                        where  ncl.nacl_cd_natureza =
                                               cl.nacl_cd_natureza and
                                               ncl.nacl_cd_natureza = 1 --UMKT
                                        ) umkt,
                                       contrato_pratica_centro_lucro cpl,
                                       (select 'AJ_REC' origem,
                                               cp.copr_cd_contrato_pratica cd_contrato_pratica,
                                               cp.copr_nm_contrato_pratica nome_contrato_pratica,
                                               pr.prat_nm_pratica      pratica,
                                               ar.ajre_dt_mes_ajuste   dt_mes,
                                               ar.ajre_cd_login_autor  resource_code,
                                               pe.pess_nm_pessoa       resource_name,
                                               cp.moed_cd_moeda_padrao cd_moeda,
                                               mo.moed_sg_moeda        moeda,
                                               ar.ajre_vl_ajuste         vlr_preco,
                                               ar.ajre_cd_ajuste_receita cd_ajuste,
                                               r.como_cd_cotacao_moeda   cd_cotacao,
                                               ar.ajre_dt_mes_ajuste     dt_mes_ajuste
                                        from
                                               moeda               mo,
                                               pessoa              pe,
                                               pratica             pr,
                                               contrato_pratica    cp,
                                               receita_deal_fiscal rfd,
                                               receita             r,
                                               ajuste_receita      ar
                                        where  ar.redf_cd_receita_dfiscal =
                                               rfd.redf_cd_receita_dfiscal and
                                               rfd.rece_cd_receita =
                                               r.rece_cd_receita and
                                               cp.copr_cd_contrato_pratica =
                                               r.copr_cd_contrato_pratica and
                                               pr.prat_cd_pratica =
                                               cp.prat_cd_pratica and
                                               pe.pess_cd_login =
                                               ar.ajre_cd_login_autor and
                                               mo.moed_cd_moeda =
                                               cp.moed_cd_moeda_padrao
                                               ) inf
                                where  inf.dt_mes_ajuste between
                                       cpl.cpcl_dt_inicio_vigencia and
                                       case when  cpl.cpcl_dt_fim_vigencia is not null then
                                                  cpl.cpcl_dt_fim_vigencia
                                            else (select para_dt_parametro
                                                  from   parametro
                                                  where  para_cd_parametro = 1) end and
                                       umkt.celu_cd_centro_lucro(+) = cpl.celu_cd_centro_lucro and
                                       cpl.copr_cd_contrato_pratica = inf.cd_contrato_pratica) inf
                        where sso.celu_cd_centro_lucro(+) = inf.celu_cd_centro_lucro) inf
                where  bm.celu_cd_centro_lucro(+) = inf.celu_cd_centro_lucro
                ) inf
        where  inf.nlinha = 1) inf
where  cm.como_cd_cotacao_moeda(+) = inf.cd_cotacao) recusd
Where moed.mes_cotacao(+) = recusd.dt_mes