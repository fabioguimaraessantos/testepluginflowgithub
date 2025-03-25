create or replace view vw_monitoramento_q3 as
select "FONTE","SUB_FONTE","NLINHA","LOGIN","DT_MES","DT_ADMISSAO","DT_RESCISAO","CD_PESSOA","CD_MOEDA","MOEDA","VALOR_COTACAO","CD_CONTRATO_PRATICA","NOME_CONTRATO_PRATICA","PRATICA","TOTAL_PERC_ALOC","PERC_ALOCAVEL","PERC_ALOCACAO","VALOR_TCE_ULT_MES","VALOR_CUSTO_INFRA_BASE","PCT_FERIAS","PCT_LICENCA","PCT_DISTRIB","VLR_FTE_CIND","VALOR_CUSTO_IND_TCE_CURR","VALOR_CUSTO_IND_TCE_BRL","VALOR_CUSTO_IND_INFRA_CURR","VALOR_CUSTO_IND_INFRA_BRL"
from ( select 'Q3' fonte,
                                          inf.sub_fonte,
                                          row_number() over( partition by inf.cd_contrato_pratica, inf.login, inf.dt_mes
                                                             order by inf.login, inf.dt_mes ) nlinha,
                                          inf.login,
                                          inf.dt_mes,
                                          inf.dt_admissao,
                                          inf.dt_rescisao,
                                          inf.cd_pessoa,
                                          --------------------------------------------------------------------------------------------------------
                                          -- Caso o codigo da moeda n?o seja retornado, indica que os valores s?o em R$, portanto e atribuido o
                                          -- codigo de moeda 1
                                          --------------------------------------------------------------------------------------------------------
                                          case when tce.moeda is null
                                                    then ult_tce.cd_moeda
                                                    else nvl( tce.cd_moeda, 1 ) end cd_moeda,
                                          --------------------------------------------------------------------------------------------------------
                                          -- Caso o nome da moeda n?o seja encontrado, e atribuido BRL, indicando que a moeda do contrato e em R$
                                          --------------------------------------------------------------------------------------------------------
                                          case when tce.moeda is null
                                                    then ult_tce.moeda
                                                    else nvl( tce.moeda, 'BRL' ) end moeda,
                                          -----------------------------------------------------------------------------------------------------------
                                          -- Caso o valor da cotac?o n?o seja encontrado, indicando que o contrato e em R$, e atribuido o valor de 1
                                          -----------------------------------------------------------------------------------------------------------
                                          case when tce.moeda is null
                                                    then ult_tce.valor_cotacao_tce
                                                    else nvl( tce.valor_cotacao, 1 ) end valor_cotacao,
                                          --
                                          inf.cd_contrato_pratica,
                                          inf.nome_contrato_pratica,
                                          inf.pratica,
                                          inf.total_perc_aloc,
                                          inf.perc_alocavel,
                                          0 perc_alocacao,
                                          valor_tce_ult_mes,
                                          valor_custo_infra_base,
                                          0 pct_ferias,
                                          0 pct_licenca,
                                          --
                                          round( -------------------------------------------------------------------------------------------------------------
                                                 -- Verifica se n?o e a query que retorna informac?es de intervalos de alocac?o, para n?o modificar o valor
                                                 -- do % correto ja calculado nas outras sub-queries
                                                 -------------------------------------------------------------------------------------------------------------
                                                  case when sub_fonte <> 'Q34'
                                                            then inf.perc_alocavel
                                                            else ---------------------------------------------------------------------------------------------------------------------------
                                                                 -- Verifica se o % total alocado no mes e igual ao %available, indicando que n?o e preciso distribuir mais nenhum custo.
                                                                 ---------------------------------------------------------------------------------------------------------------------------
                                                                 case when ( inf.perc_alocavel - inf.total_perc_aloc ) = 0
                                                                           then inf.perc_alocacao
                                                                           else ------------------------------------------------------------------------------------------------------------------------
                                                                                -- Se esta sobrando custo a distribuir, rateia de acordo com o % alocado em cada contrato, fazendo com que os c-lobs
                                                                                -- recebam esses custos proporcionalmente as alocac?es que tiveram.
                                                                                ------------------------------------------------------------------------------------------------------------------------
                                                                                case when inf.perc_alocacao = 0 and inf.total_perc_aloc = 0
                                                                                          then inf.perc_alocavel
                                                                                          else inf.perc_alocacao + ( ( inf.perc_alocacao / nvl( decode( inf.total_perc_aloc, 0, 1, inf.total_perc_aloc ), 1 ) ) * ( inf.perc_alocavel - inf.total_perc_aloc ) ) end end
                                                  end, 2 ) pct_distrib,
                                          round( -----------------------------------------------------------------------------------------------------------------
                                                 -- Verifica se n?o e a query que retorna informac?es de intervalos de alocac?o, para n?o modificar o valor
                                                 -- do % correto ja calculado nas outras sub-queries
                                                 -----------------------------------------------------------------------------------------------------------------
                                                 case when sub_fonte <> 'Q34'
                                                           then inf.perc_alocavel
                                                           else ---------------------------------------------------------------------------------------------------------------------------
                                                                -- Verifica se o % total alocado no mes e igual ao %available, indicando que n?o e preciso distribuir mais nenhum custo.
                                                                ---------------------------------------------------------------------------------------------------------------------------
                                                                case when ( inf.perc_alocavel - inf.total_perc_aloc ) = 0
                                                                          then inf.perc_alocacao
                                                                          else ------------------------------------------------------------------------------------------------------------------------
                                                                               -- Se esta sobrando custo a distribuir, rateia de acordo com o % alocado em cada contrato, fazendo com que os c-lobs
                                                                               -- recebam esses custos proporcionalmente as alocac?es que tiveram.
                                                                               ------------------------------------------------------------------------------------------------------------------------
                                                                               case when inf.perc_alocacao = 0 and inf.total_perc_aloc = 0
                                                                                         then inf.perc_alocavel
                                                                                         else inf.perc_alocacao + ( ( inf.perc_alocacao / nvl( decode( inf.total_perc_aloc, 0, 1, inf.total_perc_aloc ), 1 ) ) * ( inf.perc_alocavel - inf.total_perc_aloc ) ) end end
                                                 end, 2 ) vlr_fte_cind,
                                          -----------------------------------------------------------------------------------------------------------
                                          -- O Custo e calculado multiplicando-se o valor do ultimo TCE antes do intervalo de alocac?o identificado
                                          -- pelo % alocavel do colaborador. O mesmo vale para os Custos de Infra.
                                          -- Para o caso dos contratos em moeda estrangeira, converte para R$, multiplicando para a cotac?o da moeda
                                          -- do mes que esta sendo analisado. O mesmo vale para os Custos de Infra.
                                          -----------------------------------------------------------------------------------------------------------
                                          --
                                          -------------------------------------------------------------------------------------------------------------
                                          -- Verifica se n?o e a query que retorna informac?es de intervalos de alocac?o, para n?o modificar o valor
                                          -- do % correto ja calculado nas outras sub-queries
                                          -------------------------------------------------------------------------------------------------------------
                                          case when sub_fonte <> 'Q34'
                                                    then --------------------------------------------------------------------------------------------------------------------------------
                                                         -- Se n?o for a query dos intervalos de alocac?o, simplesmente multiplica o valor do %available pelo custo TCE do colaborador,
                                                         -- ja considerando, se o mes e futuro, onde busca o ultimo TCE realizado.
                                                         --------------------------------------------------------------------------------------------------------------------------------
                                                         ( inf.perc_alocavel * ( case when tce.login is not null
                                                                                           then tce.valor_tce
                                                                                           else ult_tce.valor_tce_ult_mes end ) )
                                                    else ------------------------------------------------------------------------------------------------------------------
                                                         -- Se for a query de intervalos de alocac?o, calcula o % de custos correto, de acordo com a alocac?o, para
                                                         -- depois multiplicar pelo valor do tce do colaborador, tambem ja considerando se o mes e futuro, onde busca o
                                                         -- o ultimo TCE realizado.
                                                         ------------------------------------------------------------------------------------------------------------------
                                                         round( case when sub_fonte <> 'Q34'
                                                                          then inf.perc_alocavel
                                                                          else -----------------------------------------------------------------------------------------------------------------------
                                                                               -- Se esta sobrando custo a distribuir, rateia de acordo com o % alocado em cada contrato, fazendo com que os c-lobs
                                                                               -- recebam esses custos proporcionalmente as alocac?es que tiveram.
                                                                               -----------------------------------------------------------------------------------------------------------------------
                                                                               case when ( inf.perc_alocavel - inf.total_perc_aloc ) = 0
                                                                                         then inf.perc_alocacao
                                                                                         else inf.perc_alocacao + ( ( inf.perc_alocacao / nvl( decode( inf.total_perc_aloc, 0, 1, inf.total_perc_aloc ), 1 ) ) * ( inf.perc_alocavel - inf.total_perc_aloc ) ) end
                                                                end, 2 ) * ( case when tce.login is not null
                                                                                       then tce.valor_tce
                                                                                       else ult_tce.valor_tce_ult_mes end )
                                                    end valor_custo_ind_tce_curr,
                                          -------------------------------------------------------------------------------------------------------------
                                          -- Verifica se n?o e a query que retorna informac?es de intervalos de alocac?o, para n?o modificar o valor
                                          -- do % correto ja calculado nas outras sub-queries
                                          -------------------------------------------------------------------------------------------------------------
                                          case when sub_fonte <> 'Q34'
                                                    then --------------------------------------------------------------------------------------------------------------------------------
                                                         -- Se n?o for a query referente aos intervalos de alocac?o, multiplica o %available pelo valor do tce, ja considerando
                                                         -- se o mes e futuro, onde busca o ultimo TCE realizado, e multiplica tambem pelo valor da cotac?o, para criac?o do campo de
                                                         -- custos em R$.
                                                         --------------------------------------------------------------------------------------------------------------------------------
                                                         ( ( inf.perc_alocavel * ( case when tce.login is not null
                                                                                             then tce.valor_tce
                                                                                             else ult_tce.valor_tce_ult_mes end ) ) * case when tce.moeda is null
                                                                                                                                                then ult_tce.valor_cotacao_tce
                                                                                                                                                else nvl( tce.valor_cotacao, 1 ) end )
                                                    else round( case when sub_fonte <> 'Q34'
                                                                          then inf.perc_alocavel
                                                                          else ------------------------------------------------------------------------------------------------------------------------
                                                                               -- Se esta sobrando custo a distribuir, rateia de acordo com o % alocado em cada contrato, fazendo com que os c-lobs
                                                                               -- recebam esses custos proporcionalmente as alocac?es que tiveram.
                                                                               ------------------------------------------------------------------------------------------------------------------------
                                                                               case when ( inf.perc_alocavel - inf.total_perc_aloc ) = 0
                                                                                         then inf.perc_alocacao
                                                                                         else inf.perc_alocacao + ( ( inf.perc_alocacao / nvl( decode( inf.total_perc_aloc, 0, 1, inf.total_perc_aloc ), 1 ) ) * ( inf.perc_alocavel - inf.total_perc_aloc ) ) end
                                                                 end, 2 ) * ( case when tce.login is not null
                                                                                        then tce.valor_tce
                                                                                        else ult_tce.valor_tce_ult_mes end ) * case when tce.moeda is null
                                                                                                                                         then ult_tce.valor_cotacao_tce
                                                                                                                                         else nvl( tce.valor_cotacao, 1 ) end
                                          end valor_custo_ind_tce_brl,
                                          ---------------------------------------------------------------------------------------------------------------
                                          -- Verifica se n?o e a query que retorna informac?es de intervalos de alocac?o, para n?o modificar o valor
                                          -- do % correto ja calculado nas outras sub-queries
                                          ---------------------------------------------------------------------------------------------------------------
                                          case when sub_fonte <> 'Q34'
                                                    then -------------------------------------------------------------------------------------------------------------------------------------
                                                         -- Se n?o for a query dos intervalos de alocac?o, simplesmente multiplica o valor do %available pelo custo de infra do colaborador,
                                                         -- ja considerando, se o mes e futuro, onde busca o ultimo CUSTO DE INFRA realizado.
                                                         -------------------------------------------------------------------------------------------------------------------------------------
                                                         ( inf.perc_alocavel * ( /*case when cib.ciba_cd_cidade_base is null
                                                                                           then ult_cus_infra.valor_custo_infra_base
                                                                                           else*/ cib.valor_custo_infra_base /*cib.cuib_vl_custo_mes_fte*/ /*end*/  ) )
                                                     else ----------------------------------------------------------------------------------------------------------------
                                                          -- Se for a query de intervalos de alocac?o, calcula o % de custos correto, de acordo com a alocac?o, para
                                                          -- depois multiplicar pelo valor do custo de infra do colaborador, tambem ja considerando se o mes e futuro,
                                                          -- onde busca o ultimo CUSTO DE INFRA realizado.
                                                          ----------------------------------------------------------------------------------------------------------------
                                                          round( case when sub_fonte <> 'Q34'
                                                                           then inf.perc_alocavel
                                                                           else case when ( inf.perc_alocavel - inf.total_perc_aloc ) = 0
                                                                                          then inf.perc_alocacao
                                                                                          else inf.perc_alocacao + ( ( inf.perc_alocacao / nvl( decode( inf.total_perc_aloc, 0, 1, inf.total_perc_aloc ), 1 ) ) * ( inf.perc_alocavel - inf.total_perc_aloc ) ) end
                                                                 end, 2 ) * ( /*case when cib.ciba_cd_cidade_base is null
                                                                                        then ult_cus_infra.valor_custo_infra_base
                                                                                        else */cib.valor_custo_infra_base /*cib.cuib_vl_custo_mes_fte*/ /*end*/  )
                                          end valor_custo_ind_infra_curr,
                                          --------------------------------------------------------------------------------------------------------------
                                          -- Verifica se n?o e a query que retorna informac?es de intervalos de alocac?o, para n?o modificar o valor
                                          -- do % correto ja calculado nas outras sub-queries
                                          --------------------------------------------------------------------------------------------------------------
                                          case when sub_fonte <> 'Q34'
                                                    then -----------------------------------------------------------------------------------------------------------------------------------------
                                                         -- Se n?o for a query referente aos intervalos de alocac?o, multiplica o %available pelo valor do custo de infra, ja considerando
                                                         -- se o mes e futuro, onde busca o ultimo CUSTO DE INFRA realizado, e multiplica tambem pelo valor da cotac?o, para criac?o do campo de
                                                         -- custos em R$.
                                                         -----------------------------------------------------------------------------------------------------------------------------------------
                                                         ( ( inf.perc_alocavel * ( /*case when cib.ciba_cd_cidade_base is null
                                                                                             then ult_cus_infra.valor_custo_infra_base
                                                                                             else*/ cib.valor_custo_infra_base /*cib.cuib_vl_custo_mes_fte*/ /*end*/  ) ) * case when tce.moeda is null
                                                                                                                                                 then ult_tce.valor_cotacao_tce
                                                                                                                                                 else nvl( tce.valor_cotacao, 1 ) end )
                                                    else ----------------------------------------------------------------------------------------------------------------
                                                         -- Se for a query de intervalos de alocac?o, calcula o % de custos correto, de acordo com a alocac?o, para
                                                         -- depois multiplicar pelo valor do custo de infra do colaborador, tambem ja considerando se o mes e futuro,
                                                         -- onde busca o ultimo CUSTO DE INFRA realizado.
                                                         ----------------------------------------------------------------------------------------------------------------
                                                         round( case when sub_fonte <> 'Q34'
                                                                          then inf.perc_alocavel
                                                                          else case when ( inf.perc_alocavel - inf.total_perc_aloc ) = 0
                                                                                         then inf.perc_alocacao
                                                                                         else inf.perc_alocacao + ( ( inf.perc_alocacao / nvl( decode( inf.total_perc_aloc, 0, 1, inf.total_perc_aloc ), 1 ) ) * ( inf.perc_alocavel - inf.total_perc_aloc ) ) end
                                                                end, 2 ) * ( /*case when cib.ciba_cd_cidade_base is null
                                                                                       then ult_cus_infra.valor_custo_infra_base
                                                                                       else*/ cib.valor_custo_infra_base /*cib.cuib_vl_custo_mes_fte*/ /*end*/  )  * case when tce.moeda is null
                                                                                                                                          then ult_tce.valor_cotacao_tce
                                                                                                                                          else nvl( tce.valor_cotacao, 1 ) end
                                                    end valor_custo_ind_infra_brl
                                   from -------------------------------------------------------------
                                        -- Sub-query que retorna o TCE para cada mes / colaborador
                                        -------------------------------------------------------------
                                        ( select tce.login,
                                                 ---------------------------------------------------------------------------
                                                 -- O mes e convertido dessa forma, para facilitar posteriores comparac?es
                                                 ---------------------------------------------------------------------------
                                                 to_date( '01/' || tce.mes, 'DD/MM/YYYY' ) dt_mes,
                                                 tce.total_custo_colab valor_tce,
                                                 moed.moed_cd_moeda cd_moeda,
                                                 nvl( tce.moeda_custo, 'BRL' ) moeda,
                                                 case when moed.moeda is null
                                                           then 1
                                                           else moed.valor_cotacao end valor_cotacao
                                          from adm_rh.tb_bi_custo_func_mgr_aux tce, --vw_bi_pms_tce tce,
                                               ( select moed_cd_moeda,
                                                        moeda,
                                                        to_date( '01/' || to_char( como_dt_dia, 'MM/YYYY'), 'DD/MM/YYYY') mes_cotacao,
                                                        valor_cotacao,
                                                        como_in_tipo
                                                 from ( select cm.moed_cd_moeda,
                                                               m.moed_sg_moeda moeda,
                                                               cm.como_dt_dia,
                                                               cm.como_in_tipo,
                                                               cm.como_vl_cotacao valor_cotacao,
                                                               -------------------------------------------------------------------------------
                                                               -- Coluna que retorna o ultimo dia de cotac?o de cada mes, para cada moeda.
                                                               -------------------------------------------------------------------------------
                                                               max( cm.como_dt_dia ) over( partition by cm.moed_cd_moeda, to_date( '01/' || to_char( cm.como_dt_dia, 'MM/YYYY'), 'DD/MM/YYYY') ) max_dt_dia_moeda
                                                        from pms.cotacao_moeda cm,
                                                             pms.moeda m
                                                        where cm.moed_cd_moeda = m.moed_cd_moeda and
                                                              cm.como_in_tipo = ------------------------------------------------------------------------------------------
                                                                                -- Para os meses anteriores ao mes corrente, retorna cotac?es do tipo REALIZADO. Do mes
                                                                                -- corrente em diante, retorna cotac?es do tipo PLANEJADO.
                                                                                ------------------------------------------------------------------------------------------
                                                                                case when to_date( '01/' || to_char( cm.como_dt_dia, 'MM/YYYY'), 'DD/MM/YYYY') <= to_date( '01/' || to_char( trunc(sysdate), 'MM/YYYY'), 'DD/MM/YYYY' )
                                                                                          then 'R'
                                                                                          else 'P' end )
                                                 where como_dt_dia = max_dt_dia_moeda
                                                 order by 1, 2 ) moed
                                          where to_date( '01/' || tce.mes, 'DD/MM/YYYY' ) = moed.mes_cotacao (+) and
                                                nvl( tce.moeda_custo, 'BRL' ) = moed.moeda (+) ) tce,
                                        --------------------------------------------------------------
                                        -- Tabela que retorna o custo de infra para cada CIDADE_BASE
                                        --------------------------------------------------------------
                                        --custo_infra_base cib,
                                        ------------------------------------------------------------
                                        -- Query que seleciona as cotac?es de moeda para cada mes
                                        ------------------------------------------------------------
                                        ( select moed_cd_moeda,
                                                 moeda,
                                                 to_date( '01/' || to_char( como_dt_dia, 'MM/YYYY'), 'DD/MM/YYYY') mes_cotacao,
                                                 valor_cotacao,
                                                 como_in_tipo
                                          from ( select cm.moed_cd_moeda,
                                                        m.moed_sg_moeda moeda,
                                                        cm.como_dt_dia,
                                                        cm.como_in_tipo,
                                                        cm.como_vl_cotacao valor_cotacao,
                                                        -------------------------------------------------------------------------------
                                                        -- Coluna que retorna o ultimo dia de cotac?o de cada mes, para cada moeda.
                                                        -------------------------------------------------------------------------------
                                                        max( cm.como_dt_dia ) over( partition by cm.moed_cd_moeda, to_date( '01/' || to_char( cm.como_dt_dia, 'MM/YYYY'), 'DD/MM/YYYY') ) max_dt_dia_moeda
                                                 from pms.cotacao_moeda cm,
                                                      pms.moeda m
                                                 where cm.moed_cd_moeda = m.moed_cd_moeda and
                                                       cm.como_in_tipo = ------------------------------------------------------------------------------------------
                                                                         -- Para os meses anteriores ao mes corrente, retorna cotac?es do tipo REALIZADO. Do mes
                                                                         -- corrente em diante, retorna cotac?es do tipo PLANEJADO.
                                                                         ------------------------------------------------------------------------------------------
                                                                         case when to_date( '01/' || to_char( cm.como_dt_dia, 'MM/YYYY'), 'DD/MM/YYYY') <= to_date( '01/' || to_char( trunc(sysdate), 'MM/YYYY'), 'DD/MM/YYYY' )
                                                                                   then 'R'
                                                                                   else 'P' end )
                                          where como_dt_dia = max_dt_dia_moeda
                                          order by 1, 2 ) moed,
                                        ---------------------------------------------------------------------
                                        -- Query que seleciona o ultimo TCE realizado de cada colaborador.
                                        ---------------------------------------------------------------------
                                        ( select pess_cd_pessoa,
                                                 login,
                                                 dt_mes,
                                                 cd_moeda,
                                                 desc_moeda moeda,
                                                 valor_cotacao_tce,
                                                 valor_tce_ult_mes,
                                                 dt_mes_ult_tce
                                          from ( select pess_cd_pessoa,
                                                        login,
                                                        dt_mes,
                                                        cd_moeda,
                                                        desc_moeda,
                                                        valor_cotacao_tce,
                                                        valor_tce_ult_mes,
                                                        max( dt_mes_ult_tce ) over( partition by pess_cd_pessoa ) dt_mes_ult_tce
                                                 from ( select pess_cd_pessoa,
                                                               login,
                                                               dt_mes,
                                                               cd_moeda,
                                                               desc_moeda,
                                                               valor_cotacao_tce,
                                                               valor_tce_ult_mes,
                                                               dt_mes_ult_tce
                                                        from ( select p.pess_cd_pessoa,
                                                                      t.login,
                                                                      ----------------------------------------------------------------------------------------------------
                                                                      -- Transforma a coluna mes numa coluna realmente de data, para facilitar comparac?es posteriores
                                                                      ----------------------------------------------------------------------------------------------------
                                                                      to_date( '01/' || t.mes, 'DD/MM/YYYY' )  dt_mes,
                                                                      p.pess_dt_admissao,
                                                                      t.total_custo_colab  valor_tce_ult_mes,
                                                                      m.moed_cd_moeda cd_moeda,
                                                                      m.moed_sg_moeda desc_moeda,
                                                                      case when t.valor_taxa_moeda_mes is not null then t.valor_taxa_moeda_mes else 1 end valor_cotacao_tce,
                                                                      ---------------------------------------------------------------------------------------------------------------
                                                                      -- Seleciona o ultimo mes com valor de TCE registrado, por PESSOA, considerando o ultimo mes de mapa fechado
                                                                      ---------------------------------------------------------------------------------------------------------------
                                                                      case when to_date( '01/' || t.mes, 'DD/MM/YYYY' ) = to_date( '01/' || to_char( p.pess_dt_admissao, 'MM/YYYY' ), 'DD/MM/YYYY' )
                                                                                then to_date( '01/' || t.mes, 'DD/MM/YYYY' )
                                                                                else max( case when to_date( '01/' || t.mes, 'DD/MM/YYYY' ) <= ( select modu_dt_fechamento
                                                                                                                                                 from pms.modulo
                                                                                                                                                 where modu_nm_modulo = 'Revenue' )
                                                                                                     then to_date( '01/' || t.mes, 'DD/MM/YYYY' ) end ) over( partition by p.pess_cd_pessoa )
                                                                                end dt_mes_ult_tce
                                                               from ------------------------------------------------------------------------------------
                                                                    -- Tabela de TCE, que e a base de informac?es para os custos de cada colaborador.
                                                                    ------------------------------------------------------------------------------------
                                                                    adm_rh.tb_bi_custo_func_mgr_aux t, --vw_bi_pms_tce t,
                                                                    pms.pessoa p,
                                                                    pms.moeda m
                                                               where t.login = p.pess_cd_login and
                                                                     case when t.moeda_custo is null then 'BRL' else t.moeda_custo end = m.moed_sg_moeda )
                                                        -----------------------------------------------------------------------------------------------------------------------
                                                        -- O ultimo TCE realizado n?o e o do mes corrente, apesar de existir na tabela ADM_RH, pois esse valor n?o tem
                                                        -- os beneficios, fazendo com que o custo final n?o seja calculado corretamente.
                                                        -----------------------------------------------------------------------------------------------------------------------
                                                        where dt_mes = case when dt_mes_ult_tce =  to_date( '01/' || to_char( trunc(sysdate),   'MM/YYYY'), 'DD/MM/YYYY') and
                                                                                 dt_mes_ult_tce <> to_date( '01/' || to_char( pess_dt_admissao, 'MM/YYYY' ), 'DD/MM/YYYY' )
                                                                                 then add_months( dt_mes_ult_tce, -1 )
                                                                                 else dt_mes_ult_tce end ) ult_tce )
                                          where dt_mes = dt_mes_ult_tce ) ult_tce,
                                        --------------------------------------------------------------------------------------------------------------
                                        -- Query que seleciona o ultimo custo de infra da base, para cada colaborador, tambem considerando somente
                                        -- meses ja fechados.
                                        --------------------------------------------------------------------------------------------------------------
--                                        ( select cd_cidade_base,
--                                                 dt_mes,
--                                                 valor_custo_infra_base
--                                          from ( select cib.ciba_cd_cidade_base cd_cidade_base,
--                                                        cib.cuib_dt_mes dt_mes,
--                                                        cib.cuib_vl_custo_mes_fte valor_custo_infra_base,
--                                                        max( cib.cuib_dt_mes ) over( partition by cib.ciba_cd_cidade_base ) dt_mes_ult_cus_infra
--                                                 from custo_infra_base cib )
--                                          where dt_mes = dt_mes_ult_cus_infra  ) ult_cus_infra,
                                         ( select cib.dt_mes,
                                                  pcib.login,
                                                  cib.cd_cidade_base,
                                                  cib.valor_custo valor_custo_infra_base
                                           from ( select cib.cuib_dt_mes             dt_mes,
                                                         cib.cuib_vl_custo_mes_fte   valor_custo,
                                                         cib.ciba_cd_cidade_base     cd_cidade_base
                                                  from pms.custo_infra_base cib ) cib,
                                                ( select pcb.ciba_cd_cidade_base   cd_cidade_base,
                                                         pcb.pess_cd_pessoa        cd_pessoa,
                                                         pe.pess_cd_login          login,
                                                         pcb.pecb_dt_inicio        dt_inicio,
                                                         case when pcb.pecb_dt_fim is not null
                                                                   then pcb.pecb_dt_fim
                                                                   else ( select para_dt_parametro
                                                                          from pms.parametro
                                                                          where para_cd_parametro = 1 ) end dt_fim
                                                  from pms.pessoa_cidade_base pcb,
                                                       pms.pessoa pe
                                                  where pcb.pess_cd_pessoa = pe.pess_cd_pessoa ) pcib
                                           where cib.cd_cidade_base = pcib.cd_cidade_base and
                                                 cib.dt_mes         between pcib.dt_inicio and pcib.dt_fim ) cib, --ult_cus_infra,
                                        -----------------------------------------------------------------------------------------------------------------------------------------
                                        -- Sub-queries que retornam os intervalos de alocac?o.
                                        -- Q31 -> Intervalos de 1 mes de alocac?o. Teve alocac?o em Jan/2010 e depois em Mar/2010, por exemplo. Entram aqui, somente os casos
                                        --        que tem uma unica alocac?o no mes anterior ao mes cuja informac?o esta sendo gerada.
                                        -- Q32 -> Intervalos de 2 ou mais meses de alocac?o, cuja ultima alocac?o foi somente para 1 contrato-pratica.
                                        -- Teve alocac?o em Jan/2010 e depois em Mai/2010, por exemplo.
                                        -- Q33 -> Intervalos de 2 ou mais meses de alocac?o, cuja ultima alocac?o foi para mais de 1 contrato-pratica.
                                        -- Teve alocac?o em Jan/2010 e depois em Mai/2010, por exemplo.
                                        -- Q34 -> Intervalos de 1 mes de alocac?o. Teve alocac?o em Jan/2010 e depos em Mar/2010, por exemplo. Entram aqui, somente os casos
                                        --        que tem mais que uma alocac?o no mes anterior ao mes cuja informac?o esta sendo gerada.
                                        -----------------------------------------------------------------------------------------------------------------------------------------
                                        ( select inf.sub_fonte,
                                                 inf.cd_pessoa,
                                                 inf.dt_admissao,
                                                 inf.dt_rescisao,
                                                 inf.cd_contrato_pratica,
                                                 inf.nome_contrato_pratica,
                                                 inf.pratica,
                                                 inf.cd_moeda,
                                                 inf.cd_recurso,
                                                 inf.login,
                                                 inf.cd_cidade_base,
                                                 inf.dt_mes,
                                                 inf.dt_ant,
                                                 ----------------------------------------------------------------------------------------------------
                                                 -- Calculo do percentual alocavel, de acordo com a data de admiss?o e rescis?o de cada colaborador
                                                 ----------------------------------------------------------------------------------------------------
                                                 case when ptc.cd_pessoa is not null
                                                           then ( ------------------------------------------------------------------------------------------
                                                                  -- % alocavel para o calculo, sempre levando em considerac?o os % referentes aos meses
                                                                  -- de admiss?o / rescis?o e/ou admiss?o / rescis?o.
                                                                  ------------------------------------------------------------------------------------------
                                                                  case when inf.perc_admis < ptc.perc_alocavel and inf.mes_ano_admissao is not null and inf.dt_mes = to_date( '01/' || inf.mes_ano_admissao, 'DD/MM/YYYY' )
                                                                            then inf.perc_admis
                                                                       when inf.perc_resc < ptc.perc_alocavel and inf.mes_ano_rescisao is not null and inf.dt_mes = to_date( '01/' || inf.mes_ano_rescisao, 'DD/MM/YYYY' )
                                                                            then inf.perc_resc
                                                                       when inf.perc_admis_resc < ptc.perc_alocavel and inf.mes_ano_admissao is not null and inf.mes_ano_rescisao is not null and
                                                                            ( inf.dt_mes = to_date( '01/' || inf.mes_ano_admissao, 'DD/MM/YYYY' ) or
                                                                              inf.dt_mes = to_date( '01/' || inf.mes_ano_rescisao, 'DD/MM/YYYY' ) )
                                                                            then inf.perc_admis_resc
                                                                       else ptc.perc_alocavel end )
                                                           else inf.perc_alocavel end perc_alocavel,
                                                 0 perc_alocacao,
                                                 0 total_perc_aloc
                                          from -----------------------------------------------------------------------------------------------------------
                                               -- Sub-query que retorna a vigencia de tipo de contrato para cada colaborador. A data limite de projec?o
                                               -- usada para todas as DREs, tambem e utilizada aqui, para termos uma data final da vigencia definida.
                                               -----------------------------------------------------------------------------------------------------------
                                               ( select ptc.petc_cd_pessoa_tp_contrato,
                                                        ptc.pess_cd_pessoa             cd_pessoa,
                                                        ptc.tico_cd_tipo_contrato      cd_tipo_contrato,
                                                        ptc.petc_pr_alocavel           perc_alocavel,
                                                        ptc.petc_dt_inicio,
                                                        case when ptc.petc_dt_fim is not null
                                                                  then ptc.petc_dt_fim
                                                                  else ( select para_dt_parametro
                                                                         from pms.parametro
                                                                         where para_cd_parametro = 1 ) end petc_dt_fim
                                                 from pms.pessoa_tipo_contrato ptc ) ptc,
                                               --------------------------------------------------------------------------------------------------
                                               -- Sub-query que retorna os intervalos com 1 mes de alocac?o, para os registros que tem somente
                                               -- 1 alocac?o no mes anterior ao que esta sendo gerado.
                                               --------------------------------------------------------------------------------------------------
                                               ( select 'Q31' sub_fonte,
                                                        pes4.cd_pessoa,
                                                        pes4.dt_admissao,
                                                        pes4.dt_rescisao,
                                                        inf.cd_contrato_pratica,
                                                        inf.nome_contrato_pratica,
                                                        inf.pratica,
                                                        inf.cd_moeda,
                                                        inf.cd_recurso,
                                                        pes4.login,
                                                        pes4.ciba_cd_cidade_base cd_cidade_base,
                                                        inf.dt_mes,
                                                        inf.dt_ant,
                                                        ----------------------------------------------------------------------------------------------------
                                                        -- Calculo do percentual alocavel, de acordo com a data de admiss?o e rescis?o de cada colaborador
                                                        ----------------------------------------------------------------------------------------------------
                                                        case when pes4.mes_ano_admissao is not null and pes4.mes_ano_rescisao is null and inf.dt_mes = to_date('01/' || pes4.mes_ano_admissao, 'DD/MM/YYYY')
                                                                  then ( pes4.perc_admis * pes4.pess_pr_alocavel )
                                                             when pes4.mes_ano_rescisao is not null and inf.dt_mes = to_date('01/' || pes4.mes_ano_rescisao, 'DD/MM/YYYY')
                                                                  then ( pes4.perc_resc * pes4.pess_pr_alocavel )
                                                             when pes4.mes_ano_admissao is not null and pes4.mes_ano_rescisao is not null and inf.dt_mes = to_date('01/' || pes4.mes_ano_rescisao, 'DD/MM/YYYY')
                                                                  then ( perc_admis_resc * pes4.pess_pr_alocavel )
                                                             else pes4.pess_pr_alocavel end perc_alocavel,
                                                        0 perc_alocacao,
                                                        0 total_perc_aloc,
                                                        pes4.mes_ano_admissao,
                                                        pes4.mes_ano_rescisao,
                                                        pes4.perc_admis,
                                                        pes4.perc_resc,
                                                        pes4.perc_admis_resc
                                                 from ---------------------------------------------------------------------------------------------------------------
                                                      -- View que calcula o percentual de admiss?o / rescis?o de cada PESSOA, fator que faz diferenca no
                                                      -- rateio final dos custos, para que o mesmo seja o mais correto possivel.
                                                      ---------------------------------------------------------------------------------------------------------------
                                                      vw_bi_pms_pessoa_perc_mes pes4,
                                                      ---------------------------------------------------------------------------------------
                                                      -- Sub-query que identifica as diferencas de meses entre os intervalos de alocac?o
                                                      ---------------------------------------------------------------------------------------
                                                      ( select inf.cd_contrato_pratica,
                                                               inf.cd_moeda,
                                                               inf.cd_cont_ant,
                                                               inf.nome_contrato_pratica,
                                                               inf.pratica,
                                                               inf.pess_cd_login,
                                                               inf.nome_cont_ant,
                                                               inf.dt_mes,
                                                               inf.dt_ant,
                                                               inf.cd_recurso,
                                                               inf.dif_meses_aloc,
                                                               inf.min_dt_aloc,
                                                               inf.max_dt_aloc
                                                        from ( select ma.copr_cd_contrato_pratica cd_contrato_pratica,
                                                                      cp4.copr_nm_contrato_pratica nome_contrato_pratica,
                                                                      prat4.prat_nm_pratica pratica,
                                                                      cp4.moed_cd_moeda_padrao cd_moeda,
                                                                      al4.recu_cd_recurso cd_recurso,
                                                                      ap4.alpe_dt_alocacao_periodo dt_mes,
                                                                      pe4.pess_cd_login,
                                                                      ---------------------------------------------------------------------------------------------------------------
                                                                      -- Primeira e ultima data de alocac?o por recurso, independente do contrato, mes ou qualquer outra variavel
                                                                      ---------------------------------------------------------------------------------------------------------------
                                                                      min( ap4.alpe_dt_alocacao_periodo ) over( partition by al4.recu_cd_recurso ) min_dt_aloc,
                                                                      max( ap4.alpe_dt_alocacao_periodo ) over( partition by al4.recu_cd_recurso ) max_dt_aloc,
                                                                      ----------------------------------
                                                                      -- Total de alocac?es por mes
                                                                      ----------------------------------
                                                                      sum( ap4.alpe_pr_alocacao_periodo ) over( partition by ap4.alpe_dt_alocacao_periodo, al4.recu_cd_recurso ) total_perc_aloc,
                                                                      count( cp4.copr_cd_contrato_pratica ) over( partition by ap4.alpe_dt_alocacao_periodo, al4.recu_cd_recurso ) total_aloc
                                                               from pms.mapa_alocacao ma,
                                                                    pms.alocacao al4,
                                                                    pms.alocacao_periodo ap4,
                                                                    pms.contrato_pratica cp4,
                                                                    pms.pratica prat4,
                                                                    pms.pessoa pe4
                                                               where ma.maal_cd_mapa_alocacao    = al4.maal_cd_mapa_alocacao and
                                                                     ma.copr_cd_contrato_pratica = cp4.copr_cd_contrato_pratica and
                                                                     al4.aloc_cd_alocacao        = ap4.aloc_cd_alocacao and
                                                                     cp4.prat_cd_pratica         = prat4.prat_cd_pratica and
                                                                     al4.recu_cd_recurso         = pe4.recu_cd_recurso and
                                                                     ------------------------------------------------------------------
                                                                     -- Somente mapas publicados. O status da alocac?o n?o importa.
                                                                     ------------------------------------------------------------------
                                                                     ma.maal_in_versao           = 'PB'
                                                               order by ap4.alpe_dt_alocacao_periodo ) al,
                                                             ( select al.cd_contrato_pratica,
                                                                      al.cd_moeda,
                                                                      al.cd_cont_ant,
                                                                      al.nome_contrato_pratica,
                                                                      al.pratica,
                                                                      al.pess_cd_login,
                                                                      al.nome_cont_ant,
                                                                      dt.dt_data dt_mes,
                                                                      al.dt_ant,
                                                                      al.cd_recurso,
                                                                      al.dif_meses_aloc,
                                                                      al.min_dt_aloc,
                                                                      al.max_dt_aloc
                                                               from  ( select dt_data
                                                                       from tb_bi_datas_corridas dt
                                                                       where to_char( dt_data, 'DD' ) = '01' and
                                                                             dt.dt_data between to_date( '01/12/2009', 'DD/MM/YYYY' ) and ( select para_dt_parametro
                                                                                                                                            from pms.parametro
                                                                                                                                            where para_cd_parametro = 1 ) ) dt,
                                                                     ( select cd_contrato_pratica,
                                                                              cd_moeda,
                                                                              cd_cont_ant,
                                                                              nome_contrato_pratica,
                                                                              pratica,
                                                                              pess_cd_login,
                                                                              nome_cont_ant,
                                                                              dt_mes,
                                                                              dt_ant,
                                                                              cd_recurso,
                                                                              dif_meses_aloc,
                                                                              min_dt_aloc,
                                                                              max_dt_aloc
                                                                       from ( select al.cd_contrato_pratica,
                                                                                     al.cd_moeda,
                                                                                     al.pess_cd_login,
                                                                                     -------------------------------------------------------------------------------
                                                                                     -- Codigo do contrato pratica da ultima alocac?o anterior ao intervalo
                                                                                     -------------------------------------------------------------------------------
                                                                                     lag( al.cd_contrato_pratica, 1, null ) over( partition by al.cd_recurso, al.min_dt_aloc order by al.cd_recurso, al.dt_mes ) cd_cont_ant,
                                                                                     al.nome_contrato_pratica,
                                                                                     al.pratica,
                                                                                     -------------------------------------------------------------------------------
                                                                                     -- Nome do contrato pratica da ultima alocac?o anterior ao intervalo
                                                                                     -------------------------------------------------------------------------------
                                                                                     lag( al.nome_contrato_pratica, 1, null ) over( partition by al.cd_recurso, al.min_dt_aloc order by al.cd_recurso, al.dt_mes ) nome_cont_ant,
                                                                                     al.dt_mes,
                                                                                     -------------------------------------------------------------------------------
                                                                                     -- Mes da ultima alocac?o anterior ao intervalo
                                                                                     -------------------------------------------------------------------------------
                                                                                     lag( al.dt_mes, 1, null ) over( partition by al.cd_recurso, al.min_dt_aloc order by al.cd_recurso, al.dt_mes ) dt_ant,
                                                                                     --------------------------------------------------------------------------------------------------------------------
                                                                                     -- Numero de meses entre a alocac?o atual, e a ultima alocac?o, para identificar se realmente existe o intervalo
                                                                                     --------------------------------------------------------------------------------------------------------------------
                                                                                     months_between( al.dt_mes, lag( al.dt_mes, 1, null ) over( partition by al.cd_recurso, al.min_dt_aloc order by al.cd_recurso, al.dt_mes ) ) dif_meses_aloc,
                                                                                     al.cd_recurso,
                                                                                     al.min_dt_aloc,
                                                                                     al.max_dt_aloc
                                                                              from ( select ma.copr_cd_contrato_pratica cd_contrato_pratica,
                                                                                            cp4.copr_nm_contrato_pratica nome_contrato_pratica,
                                                                                            prat4.prat_nm_pratica pratica,
                                                                                            cp4.moed_cd_moeda_padrao cd_moeda,
                                                                                            al4.recu_cd_recurso cd_recurso,
                                                                                            ap4.alpe_dt_alocacao_periodo dt_mes,
                                                                                            pe4.pess_cd_login,
                                                                                            ---------------------------------------------------------------------------------------------------------------
                                                                                            -- Primeira e ultima data de alocac?o por recurso, independente do contrato, mes ou qualquer outra variavel
                                                                                            ---------------------------------------------------------------------------------------------------------------
                                                                                            min( ap4.alpe_dt_alocacao_periodo ) over( partition by al4.recu_cd_recurso ) min_dt_aloc,
                                                                                            max( ap4.alpe_dt_alocacao_periodo ) over( partition by al4.recu_cd_recurso ) max_dt_aloc,
                                                                                            ----------------------------------
                                                                                            -- Total de alocac?es por mes
                                                                                            ----------------------------------
                                                                                            sum( ap4.alpe_pr_alocacao_periodo ) over( partition by ap4.alpe_dt_alocacao_periodo, al4.recu_cd_recurso ) total_perc_aloc,
                                                                                            count( cp4.copr_cd_contrato_pratica ) over( partition by ap4.alpe_dt_alocacao_periodo, al4.recu_cd_recurso ) total_aloc
                                                                                     from pms.mapa_alocacao ma,
                                                                                          pms.alocacao al4,
                                                                                          pms.alocacao_periodo ap4,
                                                                                          pms.contrato_pratica cp4,
                                                                                          pms.pratica prat4,
                                                                                          pms.pessoa pe4
                                                                                     where ma.maal_cd_mapa_alocacao    = al4.maal_cd_mapa_alocacao and
                                                                                           ma.copr_cd_contrato_pratica = cp4.copr_cd_contrato_pratica and
                                                                                           al4.aloc_cd_alocacao        = ap4.aloc_cd_alocacao and
                                                                                           cp4.prat_cd_pratica         = prat4.prat_cd_pratica and
                                                                                           al4.recu_cd_recurso         = pe4.recu_cd_recurso and
                                                                                           ------------------------------------------------------------------
                                                                                           -- Somente mapas publicados. O status da alocac?o n?o importa.
                                                                                           ------------------------------------------------------------------
                                                                                           ma.maal_in_versao           = 'PB'
                                                                                     order by ap4.alpe_dt_alocacao_periodo ) al
                                                                              where -------------------------------------------------------------------------------------------
                                                                                    -- Condic?o que limita essa sub-query, somente para os casos que tem somente 1 alocac?o
                                                                                    -- no mes anterior ao que a informac?o esta sendo gerada.
                                                                                    --------------------------------------------------------------------------------------------
                                                                                    total_aloc = 1 ) al
                                                                       ---------------------------------------------------
                                                                       -- Somente alocac?es com intervalo de 1 mes
                                                                       ---------------------------------------------------
                                                                       where dif_meses_aloc = 2 ) al
                                                               where dt.dt_data between al.min_dt_aloc (+) and al.max_dt_aloc (+)  and
                                                                     dt.dt_data = add_months( al.dt_mes, 1 ) ) inf
                                                        where inf.dt_mes = al.dt_mes (+) and
                                                              inf.pess_cd_login = al.pess_cd_login (+) and
                                                              al.dt_mes is null  ) inf
                                                 where pes4.cd_recurso = inf.cd_recurso and
                                                       dt_mes is not null ) inf
                                          where inf.cd_pessoa = ptc.cd_pessoa (+) and
                                                ----------------------------------------------------------------------
                                                -- Condic?o para vigencia do tipo de contrato de cada colaborador
                                                ----------------------------------------------------------------------
                                                inf.dt_mes    between ptc.petc_dt_inicio (+) and ptc.petc_dt_fim (+)
                                          union all
                                          ------------------------------------------------------------------------------------------------------------
                                          -- Query que retorna as informac?es dos intervalos de 2 ou mais meses de alocac?o, cuja ultima alocac?o
                                          -- foi somente para 1 contrato-pratica. Teve alocac?o em Jan/2010 e depois em Mai/2010, por exemplo.
                                          ------------------------------------------------------------------------------------------------------------
                                          select sub_fonte,
                                                 cd_pessoa,
                                                 dt_admissao,
                                                 dt_rescisao,
                                                 cd_contrato_pratica,
                                                 nome_contrato_pratica,
                                                 pratica,
                                                 cd_moeda,
                                                 cd_recurso,
                                                 login,
                                                 cd_cidade_base,
                                                 dt_mes,
                                                 dt_ant,
                                                 perc_alocavel,
                                                 case when perc_alocacao > total_perc_aloc and mod( perc_alocacao, total_perc_aloc ) = 0
                                                           then total_perc_aloc
                                                           else perc_alocacao end perc_alocacao,
                                                 total_perc_aloc
                                          from ( select inf.sub_fonte,
                                                        inf.cd_pessoa,
                                                        ---------------------------------------------------------------------------------------------------------------
                                                        -- Caso a admiss?o seja nula, retorna 01/01/2010, para facilitar comparac?es. Para o PMS, podemos fazer essa
                                                        -- atribuic?o de datas,
                                                        ---------------------------------------------------------------------------------------------------------------
                                                        nvl( inf.dt_admissao, to_date( '01/01/2010', 'DD/MM/YYYY' ) ) dt_admissao,
                                                        inf.dt_rescisao,
                                                        inf.cd_contrato_pratica,
                                                        inf.nome_contrato_pratica,
                                                        inf.pratica,
                                                        inf.cd_moeda,
                                                        inf.cd_recurso,
                                                        inf.login,
                                                        inf.cd_cidade_base,
                                                        inf.dt_mes,
                                                        inf.dt_ant,
                                                        case when ptc.cd_pessoa is not null
                                                                  then ( ---------------------------------------------------------------------------------------------
                                                                         -- % alocavel para o calculo, sempre levando em considerac?o os % referentes aos meses
                                                                         -- de admiss?o / rescis?o e/ou admiss?o / rescis?o.
                                                                         ---------------------------------------------------------------------------------------------
                                                                         case when inf.perc_admis < ptc.perc_alocavel and inf.mes_ano_admissao is not null and inf.dt_mes = to_date( '01/' || inf.mes_ano_admissao, 'DD/MM/YYYY' )
                                                                                   then inf.perc_admis
                                                                              when inf.perc_resc < ptc.perc_alocavel and inf.mes_ano_rescisao is not null and inf.dt_mes = to_date( '01/' || inf.mes_ano_rescisao, 'DD/MM/YYYY' )
                                                                                   then inf.perc_resc
                                                                              when inf.perc_admis_resc < ptc.perc_alocavel and inf.mes_ano_admissao is not null and inf.mes_ano_rescisao is not null and
                                                                                   ( inf.dt_mes = to_date( '01/' || inf.mes_ano_admissao, 'DD/MM/YYYY' ) or
                                                                                     inf.dt_mes = to_date( '01/' || inf.mes_ano_rescisao, 'DD/MM/YYYY' ) )
                                                                                   then inf.perc_admis_resc
                                                                              else ptc.perc_alocavel end )
                                                                  else inf.perc_alocavel end perc_alocavel,
                                                        count( inf.cd_contrato_pratica ) over( partition by inf.login, inf.dt_mes ) quant_alocacoes,
                                                        -------------------------------------------------------------------------------------------------------------------
                                                        -- Implementação feita para tirar as duplicações de alocação, no caso de projeções onde o mês base de projeção,
                                                        -- ( mês anterior ), o colaborador teve alocação em mais de 1 contrato
                                                        -------------------------------------------------------------------------------------------------------------------
                                                        --case when count( inf.cd_contrato_pratica ) over( partition by inf.login, inf.dt_mes ) = 2 --ERRO
                                                        case when count( inf.cd_contrato_pratica ) over( partition by inf.login, inf.dt_mes ) >= 2 --NOBORU 23/05/2019
                                                                  then sum( inf.perc_alocacao ) over( partition by inf.login, inf.dt_mes, inf.nome_contrato_pratica )
                                                                  else inf.perc_alocacao
                                                       end perc_alocacao,
                                                        row_number() over( partition by inf.login, inf.dt_mes, inf.nome_contrato_pratica
                                                                           order by     inf.login, inf.dt_mes, inf.nome_contrato_pratica ) nlinha,
                                                        inf.total_perc_aloc
                                                 from ------------------------------------------------------------------------------------------------------------
                                                      -- Sub-query que retorna a vigencia de tipo de contrato para cada colaborador. A data limite de projec?o
                                                      -- usada para todas as DREs, tambem e utilizada aqui, para termos uma data final da vigencia definida.
                                                      ------------------------------------------------------------------------------------------------------------
                                                      ( select ptc.petc_cd_pessoa_tp_contrato,
                                                               ptc.pess_cd_pessoa             cd_pessoa,
                                                               ptc.tico_cd_tipo_contrato      cd_tipo_contrato,
                                                               ptc.petc_pr_alocavel           perc_alocavel,
                                                               ptc.petc_dt_inicio,
                                                               case when ptc.petc_dt_fim is not null
                                                                         then ptc.petc_dt_fim
                                                                         else ( select para_dt_parametro
                                                                                from pms.parametro
                                                                                where para_cd_parametro = 1 ) end petc_dt_fim
                                                        from pms.pessoa_tipo_contrato ptc ) ptc,
                                                      ---------------------------------------------------------------------------------------------------------------------------------
                                                      -- Intervalos de 1 mes de alocac?o. Teve alocac?o em Jan/2010 e depos em Mar/2010, por exemplo. Entram aqui, somente os casos
                                                      -- que tem mais que uma alocac?o no mes anterior ao mes cuja informac?o esta sendo gerada.
                                                      ---------------------------------------------------------------------------------------------------------------------------------
                                                      ( select 'Q34'  sub_fonte,
                                                               pes4.cd_pessoa,
                                                               pes4.dt_admissao,
                                                               pes4.dt_rescisao,
                                                               al.cd_contrato_pratica,
                                                               al.nome_contrato_pratica,
                                                               al.pratica,
                                                               al.cd_moeda,
                                                               al.cd_recurso,
                                                               pes4.login,
                                                               pes4.ciba_cd_cidade_base cd_cidade_base,
                                                               inf.dt_mes,
                                                               null dt_ant,
                                                              ----------------------------------------------------------------------------------------------------
                                                              -- Calculo do percentual alocavel, de acordo com a data de admiss?o e rescis?o de cada colaborador
                                                              ----------------------------------------------------------------------------------------------------
                                                               case when pes4.mes_ano_admissao is not null and pes4.mes_ano_rescisao is null and inf.dt_mes = to_date('01/' || pes4.mes_ano_admissao, 'DD/MM/YYYY')
                                                                        then ( pes4.perc_admis * pes4.pess_pr_alocavel )
                                                                   when pes4.mes_ano_rescisao is not null and inf.dt_mes = to_date('01/' || pes4.mes_ano_rescisao, 'DD/MM/YYYY')
                                                                        then ( pes4.perc_resc * pes4.pess_pr_alocavel )
                                                                   when pes4.mes_ano_admissao is not null and pes4.mes_ano_rescisao is not null and inf.dt_mes = to_date('01/' || pes4.mes_ano_rescisao, 'DD/MM/YYYY')
                                                                        then ( perc_admis_resc * pes4.pess_pr_alocavel )
                                                                   else pes4.pess_pr_alocavel end perc_alocavel,
                                                               al.perc_alocacao,
                                                               al.total_perc_aloc,
                                                               pes4.mes_ano_admissao,
                                                               pes4.mes_ano_rescisao,
                                                               pes4.perc_admis,
                                                               pes4.perc_resc,
                                                               pes4.perc_admis_resc,
                                                               ROW_NUMBER() OVER (PARTITION  BY al.cd_contrato_pratica, al.alpe_cd_alocacao_periodo, pes4.login,inf.dt_mes ORDER BY al.cd_contrato_pratica, al.alpe_cd_alocacao_periodo, pes4.login,inf.dt_mes) LINHA

                                                        from ------------------------------------------------------------------------------------------------------------------
                                                             -- View que retorna as informac?es basicas de cada colaborador, fazendo o calculo do %de admiss?o / rescis?o
                                                             ------------------------------------------------------------------------------------------------------------------
                                                             vw_bi_pms_pessoa_perc_mes pes4,
                                                             -------------------------------------------------------------------------------------------------------------------------------
                                                             -- Query que retorna as informac?es da ultima alocac?o antes do intervalo, e faz o produto cartesiano gerando as informac?es
                                                             -- para todos os meses do intervalo
                                                             -------------------------------------------------------------------------------------------------------------------------------
                                                             ( select cd_contrato_pratica,
                                                                      nome_contrato_pratica,
                                                                      pratica,
                                                                      cd_moeda,
                                                                      cd_recurso,
                                                                      dt.dt_data dt_mes,
                                                                      total_aloc,
                                                                      dt_mes_ant,
                                                                      dif_meses
                                                               from -----------------------------------------------------------------------------------------------
                                                                    -- Tabela que retorna o primeiro dia de cada mes, para preenchimento do intervalo de custos
                                                                    -----------------------------------------------------------------------------------------------
                                                                    ( select dt_data
                                                                      from pms.tb_bi_datas_corridas dt
                                                                      where to_char(dt.dt_data, 'DD') = '01' ) dt,
                                                                    ----------------------------------------------------------------------------------------------------------------------
                                                                    -- Query que retorna a ultima alocac?o antes do intervalo, identificando o mesmo com a referida diferenca de meses.
                                                                    ----------------------------------------------------------------------------------------------------------------------
                                                                    ( select cd_contrato_pratica,
                                                                             nome_contrato_pratica,
                                                                             pratica,
                                                                             cd_moeda,
                                                                             cd_recurso,
                                                                             dt_mes,
                                                                             total_aloc,
                                                                             min_dt_aloc,
                                                                             max_dt_aloc,
                                                                             dt_mes_ant,
                                                                             -------------------------------------------------------------------
                                                                             -- Identificac?o da diferenca de meses do intervalo de alocac?o
                                                                             -------------------------------------------------------------------
                                                                             months_between( dt_mes, dt_mes_ant ) dif_meses
                                                                      from ( select cd_contrato_pratica,
                                                                                    nome_contrato_pratica,
                                                                                    pratica,
                                                                                    cd_moeda,
                                                                                    cd_recurso,
                                                                                    dt_mes,
                                                                                    total_aloc,
                                                                                    min_dt_aloc,
                                                                                    max_dt_aloc,
                                                                                    pr_aloc_periodo,
                                                                                    total_perc_aloc,
                                                                                    num_rep_recurso,
                                                                                    ---------------------------------------------------------------------------
                                                                                    -- Essa coluna identifica o mes da ultima alocac?o antes do intervalo.
                                                                                    ---------------------------------------------------------------------------
                                                                                    case when row_number() over(partition by cd_recurso, dt_mes, cd_contrato_pratica order by cd_recurso, dt_mes, num_rep_recurso) = 1
                                                                                              then ----------------------------------------------------------------------------
                                                                                                   -- Retorna o ultimo mes, quando se tratar de um mes intervalo de alocac?o
                                                                                                   ----------------------------------------------------------------------------
                                                                                                   lag(dt_mes, num_rep_recurso, null) over(partition by cd_recurso order by cd_recurso, dt_mes, num_rep_recurso)
                                                                                              else --------------------------------------------------------------------------------------
                                                                                                   -- Retorna o ultimo mes, quando se tratar de mais de 1 mes de intervalo de alocac?o
                                                                                                   --------------------------------------------------------------------------------------
                                                                                                   lag(dt_mes, num_rep_recurso, null) over(partition by cd_recurso order by cd_recurso, dt_mes, num_rep_recurso) end dt_mes_ant
                                                                             from -----------------------------------------------------------------------------------------------------------------------------
                                                                                  -- Query que retorna o status das alocac?es, de cada recurso. S?o as informac?es necessarias para identificac?o / acerto
                                                                                  -- dos intervalos de alocac?o.
                                                                                  -----------------------------------------------------------------------------------------------------------------------------
                                                                                  ( select ma.copr_cd_contrato_pratica  cd_contrato_pratica,
                                                                                           cp4.copr_nm_contrato_pratica nome_contrato_pratica,
                                                                                           prat4.prat_nm_pratica        pratica,
                                                                                           cp4.moed_cd_moeda_padrao     cd_moeda,
                                                                                           al4.recu_cd_recurso          cd_recurso,
                                                                                           ap4.alpe_dt_alocacao_periodo dt_mes,
                                                                                           ap4.alpe_pr_alocacao_periodo pr_aloc_periodo,
                                                                                           ---------------------------------------------------------------------------------------------------------------
                                                                                           -- Primeira e ultima data de alocac?o por recurso, independente do contrato, mes ou qualquer outra variavel
                                                                                           ---------------------------------------------------------------------------------------------------------------
                                                                                           min(ap4.alpe_dt_alocacao_periodo) over(partition by al4.recu_cd_recurso) min_dt_aloc,
                                                                                           max(ap4.alpe_dt_alocacao_periodo) over(partition by al4.recu_cd_recurso) max_dt_aloc,
                                                                                           count(cp4.copr_cd_contrato_pratica) over(partition by ap4.alpe_dt_alocacao_periodo) total_aloc,
                                                                                           row_number() over ( partition by al4.recu_cd_recurso, ap4.alpe_dt_alocacao_periodo
                                                                                                               order by ap4.alpe_dt_alocacao_periodo) num_rep_recurso,
                                                                                           sum(ap4.alpe_pr_alocacao_periodo) over(partition by ap4.alpe_dt_alocacao_periodo, al4.recu_cd_recurso) total_perc_aloc
                                                                                    from pms.mapa_alocacao    ma,
                                                                                         pms.alocacao         al4,
                                                                                         pms.alocacao_periodo ap4,
                                                                                         pms.contrato_pratica cp4,
                                                                                         pms.pratica          prat4,
                                                                                         pms.pessoa           pe4
                                                                                    where ma.maal_cd_mapa_alocacao = al4.maal_cd_mapa_alocacao
                                                                                      and al4.recu_cd_recurso = pe4.recu_cd_recurso
                                                                                      and ma.copr_cd_contrato_pratica = cp4.copr_cd_contrato_pratica
                                                                                      and al4.aloc_cd_alocacao = ap4.aloc_cd_alocacao
                                                                                      and cp4.prat_cd_pratica = prat4.prat_cd_pratica
                                                                                      and ------------------------------------------------------------------
                                                                                          -- Somente mapas publicados. O status da alocac?o n?o importa.
                                                                                          ------------------------------------------------------------------
                                                                                          ma.maal_in_versao = 'PB'
                                                                                    order by ap4.alpe_dt_alocacao_periodo ) al
                                                                             where -----------------------------------------------------------------------------------------------------------------------------
                                                                                   -- Condic?o que identifica os casos onde o ultimo mes antes do intervalo de alocac?o, tem alocac?o para mais de um c-lob
                                                                                   -----------------------------------------------------------------------------------------------------------------------------
                                                                                   total_aloc > 1 ) al ) al
                                                               where ---------------------------------------------------------------------------------------------------
                                                                     -- Como intervalo e de 2 ou mais meses, garante a replicac?o das informac?es da ultima alocac?o
                                                                     -- para todo intervalo
                                                                     ---------------------------------------------------------------------------------------------------
                                                                     dt.dt_data > dt_mes_ant and
                                                                     dt.dt_data < dt_mes and
                                                                     al.dif_meses  = 2 ) inf,
                                                             --------------------------------------------------------------------------------------------------------------------------
                                                             -- Replicac?o da query de informac?es de alocac?o do recurso, necessarias para identificac?o da alocac?o anterior ao
                                                             -- intervalo, cujas informac?es ser?o replicadas na montagem de informac?es do intervalo.
                                                             --------------------------------------------------------------------------------------------------------------------------
                                                             ( select ma.copr_cd_contrato_pratica cd_contrato_pratica,
                                                                      cp4.copr_nm_contrato_pratica nome_contrato_pratica,
                                                                      prat4.prat_nm_pratica pratica,
                                                                      cp4.moed_cd_moeda_padrao cd_moeda,
                                                                      al4.recu_cd_recurso cd_recurso,
                                                                      ap4.alpe_dt_alocacao_periodo dt_mes,
                                                                      ap4.alpe_pr_alocacao_periodo perc_alocacao,
                                                                      AP4.alpe_cd_alocacao_periodo,
                                                                      ---------------------------------------------------------------------------------------------------------------
                                                                      -- Primeira e ultima data de alocac?o por recurso, independente do contrato, mes ou qualquer outra variavel
                                                                      ---------------------------------------------------------------------------------------------------------------
                                                                      min( ap4.alpe_dt_alocacao_periodo )   over( partition by al4.recu_cd_recurso ) min_dt_aloc,
                                                                      max( ap4.alpe_dt_alocacao_periodo )   over( partition by al4.recu_cd_recurso ) max_dt_aloc,
                                                                      count( cp4.copr_cd_contrato_pratica ) over( partition by ap4.alpe_dt_alocacao_periodo ) total_aloc,
                                                                      sum( ap4.alpe_pr_alocacao_periodo )   over( partition by ap4.alpe_dt_alocacao_periodo, al4.recu_cd_recurso ) total_perc_aloc
                                                               from pms.mapa_alocacao ma,
                                                                    pms.alocacao al4,
                                                                    pms.alocacao_periodo ap4,
                                                                    pms.contrato_pratica cp4,
                                                                    pms.pratica prat4,
                                                                    pms.pessoa pe4
                                                               where ma.maal_cd_mapa_alocacao    = al4.maal_cd_mapa_alocacao and
                                                                     al4.recu_cd_recurso         = pe4.recu_cd_recurso and
                                                                     ma.copr_cd_contrato_pratica = cp4.copr_cd_contrato_pratica and
                                                                     al4.aloc_cd_alocacao        = ap4.aloc_cd_alocacao and
                                                                     cp4.prat_cd_pratica         = prat4.prat_cd_pratica and
                                                                     ------------------------------------------------------------------
                                                                     -- Somente mapas publicados. O status da alocac?o n?o importa.
                                                                     ------------------------------------------------------------------
                                                                     ma.maal_in_versao           = 'PB'
                                                               order by ap4.alpe_dt_alocacao_periodo ) al
                                                        where -------------------------------------------------------------------------------------------
                                                              -- Junc?o de informac?es, para montagem correta das informac?es do intervalo de alocac?o.
                                                              -------------------------------------------------------------------------------------------
                                                              inf.dt_mes_ant = al.dt_mes and
                                                              inf.cd_recurso = al.cd_recurso and --pes4.login in ( 'fuechi', 'mcardoso' ) and
                                                              inf.cd_recurso = pes4.cd_recurso ) inf
                                                 where inf.cd_pessoa = ptc.cd_pessoa (+) and
                                                       ----------------------------------------------------------------------------------------
                                                       -- Vigencia de tipo de contrato, para que o % available seja considerado corretamente.
                                                       ----------------------------------------------------------------------------------------
                                                       inf.dt_mes    between ptc.petc_dt_inicio (+) and ptc.petc_dt_fim (+)
                                                       AND INF.LINHA = 1
                                                        )
                                          where nlinha = 1
                                          union
                                          --------------------------------------------------------------------------------------------------------------------
                                          -- Sub-query que retorna os custos para intervalos de alocac?o, considerando a ultima alocac?o antes do intervalo,
                                          -- para os colaboradores com intervalo de 2 ou mais meses
                                          --------------------------------------------------------------------------------------------------------------------
                                          select inf.sub_fonte,
                                                 inf.cd_pessoa,
                                                 inf.dt_admissao,
                                                 inf.dt_rescisao,
                                                 inf.cd_contrato_pratica,
                                                 inf.nome_contrato_pratica,
                                                 inf.pratica,
                                                 inf.cd_moeda,
                                                 inf.cd_recurso,
                                                 inf.login,
                                                 inf.cd_cidade_base,
                                                 inf.dt_mes,
                                                 inf.dt_ant,
                                                 case when ptc.cd_pessoa is not null
                                                           then ( -------------------------------------------------------------------------------------------
                                                                  -- % alocavel para o calculo, sempre levando em considerac?o os % referentes aos meses
                                                                  -- de admiss?o / rescis?o e/ou admiss?o / rescis?o.
                                                                  -------------------------------------------------------------------------------------------
                                                                  case when inf.perc_admis < ptc.perc_alocavel and inf.mes_ano_admissao is not null and inf.dt_mes = to_date( '01/' || inf.mes_ano_admissao, 'DD/MM/YYYY' )
                                                                            then inf.perc_admis
                                                                       when inf.perc_resc < ptc.perc_alocavel and inf.mes_ano_rescisao is not null and inf.dt_mes = to_date( '01/' || inf.mes_ano_rescisao, 'DD/MM/YYYY' )
                                                                            then inf.perc_resc
                                                                       when inf.perc_admis_resc < ptc.perc_alocavel and inf.mes_ano_admissao is not null and inf.mes_ano_rescisao is not null and
                                                                            ( inf.dt_mes = to_date( '01/' || inf.mes_ano_admissao, 'DD/MM/YYYY' ) or
                                                                              inf.dt_mes = to_date( '01/' || inf.mes_ano_rescisao, 'DD/MM/YYYY' ) )
                                                                            then inf.perc_admis_resc
                                                                       else ptc.perc_alocavel end )
                                                           else inf.perc_alocavel end perc_alocavel,
                                                 inf.perc_alocacao,
                                                 inf.total_perc_aloc
                                          from ------------------------------------------------------------------------------------------------------------
                                               -- Sub-query que retorna a vigencia de tipo de contrato para cada colaborador. A data limite de projec?o
                                               -- usada para todas as DREs, tambem e utilizada aqui, para termos uma data final da vigencia definida.
                                               ------------------------------------------------------------------------------------------------------------
                                               ( select ptc.petc_cd_pessoa_tp_contrato,
                                                        ptc.pess_cd_pessoa             cd_pessoa,
                                                        ptc.tico_cd_tipo_contrato      cd_tipo_contrato,
                                                        ptc.petc_pr_alocavel           perc_alocavel,
                                                        pe.pess_dt_admissao            dt_admissao,
                                                        case when pe.pess_dt_admissao > ptc.petc_dt_inicio
                                                                  then to_date( '01/' || to_char( pe.pess_dt_admissao, 'MM/YYYY' ) , 'DD/MM/YYYY' )
                                                                  else ptc.petc_dt_inicio end petc_dt_inicio,
                                                        case when ptc.petc_dt_fim is not null
                                                                  then ptc.petc_dt_fim
                                                                  else ( select para_dt_parametro
                                                                         from pms.parametro
                                                                         where para_cd_parametro = 1 ) end petc_dt_fim
                                                 from pms.pessoa_tipo_contrato ptc,
                                                      ----------------------------------------------------------------------------------------------------------
                                                      -- Faz join com a tabela de PESSOA, para trazer a data de admiss?o, que e a data de inicio da vigencia.
                                                      ----------------------------------------------------------------------------------------------------------
                                                      pms.pessoa pe
                                                 where ptc.pess_cd_pessoa = pe.pess_cd_pessoa ) ptc,
                                               -----------------------------------------------------------------------------------------------------------------
                                               -- Intervalos de 2 ou mais meses de alocac?o, cuja ultima alocac?o foi somente para 1 contrato-pratica.
                                               -- Teve alocac?o em Jan/2010 e depois em Mai/2010, por exemplo.
                                               -----------------------------------------------------------------------------------------------------------------
                                               ( select 'Q32' sub_fonte,
                                                        pes4.cd_pessoa,
                                                        pes4.dt_admissao,
                                                        pes4.dt_rescisao,
                                                        inf.cd_contrato_pratica,
                                                        inf.nome_contrato_pratica,
                                                        inf.pratica,
                                                        inf.cd_moeda,
                                                        inf.cd_recurso,
                                                        pes4.login,
                                                        pes4.ciba_cd_cidade_base cd_cidade_base,
                                                        inf.dt_mes,
                                                        inf.dt_ant,
                                                        ----------------------------------------------------------------------------------------------------
                                                        -- Calculo do percentual alocavel, de acordo com a data de admiss?o e rescis?o de cada colaborador
                                                        ----------------------------------------------------------------------------------------------------
                                                        case when pes4.mes_ano_admissao is not null and pes4.mes_ano_rescisao is null and inf.dt_mes = to_date('01/' || pes4.mes_ano_admissao, 'DD/MM/YYYY')
                                                                  then ( pes4.perc_admis * pes4.pess_pr_alocavel )
                                                             when pes4.mes_ano_rescisao is not null and inf.dt_mes = to_date('01/' || pes4.mes_ano_rescisao, 'DD/MM/YYYY')
                                                                  then ( pes4.perc_resc * pes4.pess_pr_alocavel )
                                                             when ( pes4.mes_ano_admissao is not null and pes4.mes_ano_rescisao is not null ) and inf.dt_mes = to_date('01/' || pes4.mes_ano_admissao, 'DD/MM/YYYY')
                                                                  then ( perc_admis_resc * pes4.pess_pr_alocavel )
                                                             else pes4.pess_pr_alocavel end perc_alocavel,
                                                        0 perc_alocacao,
                                                        0 total_perc_aloc,
                                                        pes4.mes_ano_admissao,
                                                        pes4.mes_ano_rescisao,
                                                        pes4.perc_admis,
                                                        pes4.perc_resc,
                                                        pes4.perc_admis_resc
                                                 from ---------------------------------------------------------------------------------------------------------------
                                                      -- View que calcula o percentual de admiss?o / rescis?o de cada PESSOA, fator que faz diferenca no
                                                      -- rateio final dos custos, para que o mesmo seja o mais correto possivel.
                                                      ---------------------------------------------------------------------------------------------------------------
                                                      vw_bi_pms_pessoa_perc_mes pes4,
                                                      ----------------------------------------------------------------------------------------
                                                      -- Sub-query que retorna os registros de alocac?o, com mais de 2 meses de intervalo
                                                      ----------------------------------------------------------------------------------------
                                                      ( select al.cd_cont_ant    cd_contrato_pratica,
                                                               al.nome_cont_ant  nome_contrato_pratica,
                                                               al.pratica,
                                                               al.cd_recurso     cd_recurso,
                                                               al.cd_moeda,
                                                               al.dif_meses_aloc dif_meses_aloc,
                                                               al.dt_ant,
                                                               al.min_dt_aloc,
                                                               al.max_dt_aloc,
                                                               al.pess_dt_rescisao,
                                                               dt.dt_data dt_mes
                                                        from --------------------------------------------------
                                                             -- Query que retorna o primeiro dia de cada mes
                                                             --------------------------------------------------
                                                             ( select dt_data
                                                               from pms.tb_bi_datas_corridas
                                                               where to_char( dt_data, 'DD' ) = '01' ) dt,
                                                             -------------------------------------------------------------------------------------------------
                                                             -- Query que seleciona o total de alocac?es para cada colabordor / mes, fazendo as projec?es
                                                             -- de custos corretamente, quando ha a necessidade de rateio.
                                                             -------------------------------------------------------------------------------------------------
                                                             ( select dt_mes,
                                                                      login,
                                                                      total_aloc
                                                               from ( select ap.alpe_dt_alocacao_periodo dt_mes,
                                                                             p.pess_cd_login             login,
                                                                             count( ap.alpe_pr_alocacao_periodo ) over( partition by ap.alpe_dt_alocacao_periodo, p.pess_cd_login ) total_aloc,
                                                                             row_number() over( partition by ap.alpe_dt_alocacao_periodo, p.pess_cd_login
                                                                                                order by     ap.alpe_dt_alocacao_periodo, p.pess_cd_login ) nlinha
                                                                      from pms.mapa_alocacao ma,
                                                                           pms.contrato_pratica cp,
                                                                           pms.alocacao al,
                                                                           pms.alocacao_periodo ap,
                                                                           pms.pessoa p
                                                                      where ma.copr_cd_contrato_pratica = cp.copr_cd_contrato_pratica and
                                                                            ma.maal_cd_mapa_alocacao    = al.maal_cd_mapa_alocacao    and
                                                                            al.aloc_cd_alocacao         = ap.aloc_cd_alocacao         and
                                                                            al.recu_cd_recurso          = p.recu_cd_recurso           and
                                                                            ---------------------------------------------------------------------
                                                                            -- Somente mapas publicados, n?o importando o status da alocac?o
                                                                            ---------------------------------------------------------------------
                                                                            ma.maal_in_versao           = 'PB' ) tot_al
                                                               where nlinha = 1 ) tot_al,
                                                             -----------------------------------------------------------------------------------------------------------------
                                                             -- Query que retorna as alocac?es cujo intervalo e exatamente de mais que 1 mes, por exemplo, foi alocado em Janeiro e
                                                             -- depois so em Abril
                                                             -----------------------------------------------------------------------------------------------------------------
                                                             ( select cd_contrato_pratica,
                                                                      case when cd_moeda = cd_moeda_ant
                                                                                then cd_moeda
                                                                                else cd_moeda_ant end cd_moeda,
                                                                      cd_cont_ant,
                                                                      nome_contrato_pratica,
                                                                      login,
                                                                      pratica,
                                                                      nome_cont_ant,
                                                                      dt_mes,
                                                                      dt_ant,
                                                                      cd_recurso,
                                                                      dif_meses_aloc,
                                                                      min_dt_aloc,
                                                                      max_dt_aloc,
                                                                      pess_dt_rescisao,
                                                                      pess_dt_admissao dt_admissao
                                                               from ( select al.cd_contrato_pratica,
                                                                             al.cd_moeda,
                                                                             lag( al.cd_moeda, 1, null ) over( partition by al.cd_recurso, al.min_dt_aloc order by al.cd_recurso, al.dt_mes ) cd_moeda_ant,
                                                                             al.login,
                                                                             -------------------------------------------------------------------------------
                                                                             -- Codigo do contrato pratica da ultima alocac?o anterior ao intervalo
                                                                             -------------------------------------------------------------------------------
                                                                             lag( al.cd_contrato_pratica, 1, null ) over( partition by al.cd_recurso, al.min_dt_aloc order by al.cd_recurso, al.dt_mes ) cd_cont_ant,
                                                                             al.nome_contrato_pratica,
                                                                             al.pratica,
                                                                             -------------------------------------------------------------------------------
                                                                             -- Nome do contrato pratica da ultima alocac?o anterior ao intervalo
                                                                             -------------------------------------------------------------------------------
                                                                             lag( al.nome_contrato_pratica, 1, null ) over( partition by al.cd_recurso, al.min_dt_aloc order by al.cd_recurso, al.dt_mes ) nome_cont_ant,
                                                                             al.dt_mes,
                                                                             -------------------------------------------------------------------------------
                                                                             -- Mes da ultima alocac?o anterior ao intervalo
                                                                             -------------------------------------------------------------------------------
                                                                             lag( al.dt_mes, 1, null ) over( partition by al.cd_recurso, al.min_dt_aloc order by al.cd_recurso, al.dt_mes ) dt_ant,
                                                                             --------------------------------------------------------------------------------------------------------------------
                                                                             -- Numero de meses entre a alocac?o atual, e a ultima alocac?o, para identificar se realmente existe o intervalo
                                                                             --------------------------------------------------------------------------------------------------------------------
                                                                             months_between( al.dt_mes, lag( al.dt_mes, 1, null ) over( partition by al.cd_recurso, al.min_dt_aloc order by al.cd_recurso, al.dt_mes ) ) dif_meses_aloc,
                                                                             al.cd_recurso,
                                                                             al.min_dt_aloc,
                                                                             al.max_dt_aloc,
                                                                             al.pess_dt_admissao,
                                                                             al.pess_dt_rescisao
                                                                      from ( --------------------------------------------------------
                                                                             -- Query que retorna as alocac?es para cada recurso.
                                                                             --------------------------------------------------------
                                                                             select ma.copr_cd_contrato_pratica cd_contrato_pratica,
                                                                                    cp4.moed_cd_moeda_padrao cd_moeda,
                                                                                    cp4.copr_nm_contrato_pratica nome_contrato_pratica,
                                                                                    prat4.prat_nm_pratica pratica,
                                                                                    al4.recu_cd_recurso cd_recurso,
                                                                                    ap4.alpe_dt_alocacao_periodo dt_mes,
                                                                                    pe4.pess_dt_admissao,
                                                                                    pe4.pess_dt_rescisao,
                                                                                    pe4.pess_cd_login login,
                                                                                    ------------------------------------------------------------------
                                                                                    -- Menor e maior data de alocac?o para cada recurso
                                                                                    ------------------------------------------------------------------
                                                                                    min( ap4.alpe_dt_alocacao_periodo ) over( partition by al4.recu_cd_recurso ) min_dt_aloc,
                                                                                    max( ap4.alpe_dt_alocacao_periodo ) over( partition by al4.recu_cd_recurso ) max_dt_aloc
                                                                             from pms.mapa_alocacao ma,
                                                                                  pms.alocacao al4,
                                                                                  pms.alocacao_periodo ap4,
                                                                                  pms.contrato_pratica cp4,
                                                                                  pms.pessoa pe4,
                                                                                  pms.pratica prat4
                                                                             where ma.maal_cd_mapa_alocacao    = al4.maal_cd_mapa_alocacao and
                                                                                   ma.copr_cd_contrato_pratica = cp4.copr_cd_contrato_pratica and
                                                                                   al4.aloc_cd_alocacao        = ap4.aloc_cd_alocacao and
                                                                                   al4.recu_cd_recurso         = pe4.recu_cd_recurso and
                                                                                   cp4.prat_cd_pratica         = prat4.prat_cd_pratica and
                                                                                   ---------------------------------------------------------------------
                                                                                   -- Somente mapas publicados, n?o importando o status da alocac?o
                                                                                   ---------------------------------------------------------------------
                                                                                   ma.maal_in_versao           = 'PB'
                                                                             order by ap4.alpe_dt_alocacao_periodo ) al ) al
                                                               -----------------------------------------------
                                                               -- Somente diferencas maior que 2 meses
                                                               -----------------------------------------------
                                                               where dif_meses_aloc > 2 ) al
                                                        --------------------------------------------------------------------------------------
                                                        -- Condic?o deve ser dessa forma, para considerar o intervalo correto de alocac?es
                                                        --------------------------------------------------------------------------------------
                                                        where al.login  = tot_al.login and
                                                              al.dt_ant = tot_al.dt_mes and
                                                              ----------------------------------------------------------------------------------------
                                                              -- Filtra somente os intervalos, para colaboradores que tem somente 1 alocac?o no mes.
                                                              ----------------------------------------------------------------------------------------
                                                              tot_al.total_aloc = 1 and
                                                              dt.dt_data > al.dt_ant and
                                                              dt.dt_data < al.dt_mes and
                                                              ------------------------------------------------------------------------------------
                                                              -- Comparac?o com a data de admiss?o, evitando que ocorra a 'projec?o do passado'
                                                              ------------------------------------------------------------------------------------
                                                              dt.dt_data >= to_date( '01/' || to_char( al.dt_admissao, 'MM/YYYY' ), 'DD/MM/YYYY' ) ) inf
                                                 where inf.cd_recurso = pes4.cd_recurso ) inf
                                          where inf.cd_pessoa = ptc.cd_pessoa (+) and
                                                --------------------------------------------------------------------------------------
                                                -- Vigencia com a tabela PESSOA_TIPO_CONTRATO, para que traga o %alocavel correto.
                                                --------------------------------------------------------------------------------------
                                                inf.dt_mes    between ptc.petc_dt_inicio (+) and ptc.petc_dt_fim (+)
                                          union all
                                          -------------------------------------------------------------------------------------------------------------------
                                          -- Sub-query que gera os intervalos de alocac?o, para os colaboradores / contratos, que tem mais que 1 alocac?o
                                          -- dentro do mes anterior ao intervalo.
                                          -------------------------------------------------------------------------------------------------------------------
                                          select inf.sub_fonte,
                                                 inf.cd_pessoa,
                                                 inf.dt_admissao,
                                                 inf.dt_rescisao,
                                                 inf.cd_contrato_pratica,
                                                 inf.nome_contrato_pratica,
                                                 inf.pratica,
                                                 inf.cd_moeda,
                                                 inf.cd_recurso,
                                                 inf.login,
                                                 inf.cd_cidade_base,
                                                 inf.dt_mes,
                                                 inf.dt_ant,
                                                 -------------------------------------------------------------------------------------------------------
                                                 -- Calculo do percentual alocavel, rateando os custos proporcionalmente, de acordo com a distribuic?o
                                                 -- indicada na ultima alocac?o antes do intervalo identificado.
                                                 ------------------------------------------------------------------------------------------------------
                                                 case when inf.perc_alocavel = ptc.perc_alocavel
                                                           then ( ------------------------------------------------------------------------------------------
                                                                  -- % alocavel para o calculo, sempre levando em considerac?o os % referentes aos meses
                                                                  -- de admiss?o / rescis?o e/ou admiss?o / rescis?o.
                                                                  ------------------------------------------------------------------------------------------
                                                                  case when inf.perc_admis < ptc.perc_alocavel and inf.mes_ano_admissao is not null and inf.dt_mes = to_date( '01/' || inf.mes_ano_admissao, 'DD/MM/YYYY' )
                                                                            then inf.perc_admis
                                                                       when inf.perc_resc < ptc.perc_alocavel and inf.mes_ano_rescisao is not null and inf.dt_mes = to_date( '01/' || inf.mes_ano_rescisao, 'DD/MM/YYYY' )
                                                                            then inf.perc_resc
                                                                       when inf.perc_admis_resc < ptc.perc_alocavel and inf.mes_ano_admissao is not null and inf.mes_ano_rescisao is not null and
                                                                            ( inf.dt_mes = to_date( '01/' || inf.mes_ano_admissao, 'DD/MM/YYYY' ) or
                                                                              inf.dt_mes = to_date( '01/' || inf.mes_ano_rescisao, 'DD/MM/YYYY' ) )
                                                                            then inf.perc_admis_resc
                                                                       else ptc.perc_alocavel end )
                                                           else inf.perc_alocavel end perc_alocavel,
                                                 inf.perc_alocacao,
                                                 inf.total_perc_aloc
                                          from ------------------------------------------------------------------------------------------------------------
                                               -- Sub-query que retorna a vigencia de tipo de contrato para cada colaborador. A data limite de projec?o
                                               -- usada para todas as DREs, tambem e utilizada aqui, para termos uma data final da vigencia definida.
                                               ------------------------------------------------------------------------------------------------------------
                                               ( select ptc.petc_cd_pessoa_tp_contrato,
                                                        ptc.pess_cd_pessoa             cd_pessoa,
                                                        ptc.tico_cd_tipo_contrato      cd_tipo_contrato,
                                                        ptc.petc_pr_alocavel           perc_alocavel,
                                                        ptc.petc_dt_inicio,
                                                        case when ptc.petc_dt_fim is not null
                                                                  then ptc.petc_dt_fim
                                                                  else ( select para_dt_parametro
                                                                         from pms.parametro
                                                                         where para_cd_parametro = 1 ) end petc_dt_fim
                                                 from pms.pessoa_tipo_contrato ptc ) ptc,
                                               ( select al.sub_fonte,
                                                        al.cd_pessoa,
                                                        al.dt_admissao,
                                                        al.dt_rescisao,
                                                        tot_al.cd_contrato_pratica,
                                                        tot_al.nome_contrato_pratica,
                                                        tot_al.pratica,
                                                        al.cd_moeda,
                                                        al.cd_recurso,
                                                        al.login,
                                                        al.cd_cidade_base,
                                                        al.dt_mes,
                                                        al.dt_ant,
                                                        -------------------------------------------------------------------------------------------------------
                                                        -- Calculo do percentual alocavel, rateando os custos proporcionalmente, de acordo com a distribuic?o
                                                        -- indicada na ultima alocac?o antes do intervalo identificado.
                                                        ------------------------------------------------------------------------------------------------------
                                                        ( tot_al.perc_aloc / nvl( decode( tot_al.total_perc_aloc, 0, 1, tot_al.total_perc_aloc ), 1 ) ) * al.perc_alocavel perc_alocavel,
                                                        ---------------------------------------------------------------------------------------
                                                        -- Os valores de % de alocac?o s?o zerados, porque como estamos falando de projec?o
                                                        ---------------------------------------------------------------------------------------
                                                        0 perc_alocacao,
                                                        0 total_perc_aloc,
                                                        al.mes_ano_admissao,
                                                        al.mes_ano_rescisao,
                                                        al.perc_admis,
                                                        al.perc_resc,
                                                        al.perc_admis_resc
                                                 from ( -------------------------------------------------------------------------------
                                                        -- Sub-query que retorna o somatorio do % alocado, por colaborador / mes.
                                                        -------------------------------------------------------------------------------
                                                        select ma.copr_cd_contrato_pratica cd_contrato_pratica,
                                                               cp.copr_nm_contrato_pratica nome_contrato_pratica,
                                                               pr.prat_nm_pratica          pratica,
                                                               ap.alpe_dt_alocacao_periodo dt_mes,
                                                               p.pess_cd_login             login,
                                                               ap.alpe_pr_alocacao_periodo perc_aloc,
                                                               ----------------------------------------------------------------------------------------------------
                                                               -- Soma do % alocado no mes, para cada colaborador. Informac?o necessario para rateio dos custos
                                                               -- nas projec?es referentes aos intervalos de alocac?o.
                                                               ----------------------------------------------------------------------------------------------------
                                                               sum( ap.alpe_pr_alocacao_periodo ) over( partition by p.pess_cd_login, ap.alpe_dt_alocacao_periodo ) total_perc_aloc
                                                        from pms.mapa_alocacao ma,
                                                             pms.contrato_pratica cp,
                                                             pms.alocacao al,
                                                             pms.alocacao_periodo ap,
                                                             pms.pessoa p,
                                                             pms.pratica pr
                                                        where ma.copr_cd_contrato_pratica = cp.copr_cd_contrato_pratica and
                                                              ma.maal_cd_mapa_alocacao    = al.maal_cd_mapa_alocacao    and
                                                              cp.prat_cd_pratica          = pr.prat_cd_pratica          and
                                                              al.aloc_cd_alocacao         = ap.aloc_cd_alocacao         and
                                                              al.recu_cd_recurso          = p.recu_cd_recurso           and
                                                              --------------------------------------------------------------------
                                                              -- Somente mapas publicados, n?o importando o status da alocac?o
                                                              --------------------------------------------------------------------
                                                              ma.maal_in_versao           = 'PB'  ) tot_al,
                                                      ( -----------------------------------------------------------------------------------------------------------------
                                                        -- Sub-query que retorna os registros da ultima alocac?o individualmente, para o rateio proporcional correto,
                                                        -- dentro dos meses identificados como intervalo de alocac?o.
                                                        -----------------------------------------------------------------------------------------------------------------
                                                        select 'Q33' sub_fonte,
                                                               pes4.cd_pessoa,
                                                               pes4.dt_admissao,
                                                               pes4.dt_rescisao,
                                                               inf.cd_moeda,
                                                               inf.cd_recurso,
                                                               pes4.login,
                                                               pes4.ciba_cd_cidade_base cd_cidade_base,
                                                               inf.dt_mes,
                                                               inf.dt_ant,
                                                               ----------------------------------------------------------------------------------------------------
                                                               -- Calculo do percentual alocavel, de acordo com a data de admiss?o e rescis?o de cada colaborador
                                                               ----------------------------------------------------------------------------------------------------
                                                               case when pes4.mes_ano_admissao is not null and pes4.mes_ano_rescisao is null and inf.dt_mes = to_date('01/' || pes4.mes_ano_admissao, 'DD/MM/YYYY')
                                                                         then ( pes4.perc_admis * pes4.pess_pr_alocavel )
                                                                    when pes4.mes_ano_rescisao is not null and inf.dt_mes = to_date('01/' || pes4.mes_ano_rescisao, 'DD/MM/YYYY')
                                                                         then ( pes4.perc_resc * pes4.pess_pr_alocavel )
                                                                    when ( pes4.mes_ano_admissao is not null and pes4.mes_ano_rescisao is not null ) and inf.dt_mes = to_date('01/' || pes4.mes_ano_admissao, 'DD/MM/YYYY')
                                                                         then ( perc_admis_resc * pes4.pess_pr_alocavel )
                                                                    else pes4.pess_pr_alocavel end perc_alocavel,
                                                              pes4.mes_ano_admissao,
                                                              pes4.mes_ano_rescisao,
                                                              pes4.perc_admis,
                                                              pes4.perc_resc,
                                                              pes4.perc_admis_resc
                                                        from ---------------------------------------------------------------------------------------------------------------
                                                             -- View que calcula o percentual de admiss?o / rescis?o de cada PESSOA, fator que faz diferenca no
                                                             -- rateio final dos custos, para que o mesmo seja o mais correto possivel.
                                                             ---------------------------------------------------------------------------------------------------------------
                                                             vw_bi_pms_pessoa_perc_mes pes4,
                                                             ----------------------------------------------------------------------------------------
                                                             -- Sub-query que retorna os registros de alocac?o, com mais de 2 meses de intervalo
                                                             ----------------------------------------------------------------------------------------
                                                             ( select al.cd_cont_ant    cd_contrato_pratica,
                                                                      al.nome_cont_ant  nome_contrato_pratica,
                                                                      al.pratica,
                                                                      al.cd_recurso     cd_recurso,
                                                                      al.cd_moeda,
                                                                      al.dif_meses_aloc dif_meses_aloc,
                                                                      al.dt_ant,
                                                                      al.min_dt_aloc,
                                                                      al.max_dt_aloc,
                                                                      al.pess_dt_rescisao,
                                                                      dt.dt_data dt_mes
                                                               from --------------------------------------------------
                                                                    -- Query que retorna o primeiro dia de cada mes
                                                                    --------------------------------------------------
                                                                    ( select dt_data
                                                                      from pms.tb_bi_datas_corridas
                                                                      where to_char( dt_data, 'DD' ) = '01' ) dt,
                                                                    ----------------------------------------------------------------------------------------------------
                                                                    -- Query que retorna o total ( quantidade ) de alocac?es, para cada login / mes, para que filtre
                                                                    -- somente os colaboradores com 2 ou mais alocac?es dentro do mes.
                                                                    ----------------------------------------------------------------------------------------------------
                                                                    ( select dt_mes,
                                                                             login,
                                                                             total_aloc
                                                                      from ( select ap.alpe_dt_alocacao_periodo dt_mes,
                                                                                    p.pess_cd_login             login,
                                                                                    count( ap.alpe_pr_alocacao_periodo ) over( partition by ap.alpe_dt_alocacao_periodo, p.pess_cd_login ) total_aloc,
                                                                                    row_number() over( partition by ap.alpe_dt_alocacao_periodo, p.pess_cd_login
                                                                                                       order by     ap.alpe_dt_alocacao_periodo, p.pess_cd_login ) nlinha
                                                                             from pms.mapa_alocacao ma,
                                                                                  pms.contrato_pratica cp,
                                                                                  pms.alocacao al,
                                                                                  pms.alocacao_periodo ap,
                                                                                  pms.pessoa p
                                                                             where ma.copr_cd_contrato_pratica = cp.copr_cd_contrato_pratica and
                                                                                   ma.maal_cd_mapa_alocacao    = al.maal_cd_mapa_alocacao    and
                                                                                   al.aloc_cd_alocacao         = ap.aloc_cd_alocacao         and
                                                                                   al.recu_cd_recurso          = p.recu_cd_recurso           and
                                                                                   ----------------------------------------------------------------------
                                                                                   -- Somente mapas publicados, n?o importando o status da alocac?o
                                                                                   ----------------------------------------------------------------------
                                                                                   ma.maal_in_versao           = 'PB' ) tot_al
                                                                      where nlinha = 1 ) tot_al,
                                                                    -----------------------------------------------------------------------------------------------------------------
                                                                    -- Query que retorna as alocac?es cujo intervalo e exatamente de mais que 1 mes, por exemplo, foi alocado em Janeiro e
                                                                    -- depois so em Abril
                                                                    -----------------------------------------------------------------------------------------------------------------
                                                                    ( select cd_contrato_pratica,
                                                                             cd_moeda,
                                                                             cd_cont_ant,
                                                                             nome_contrato_pratica,
                                                                             login,
                                                                             pratica,
                                                                             nome_cont_ant,
                                                                             dt_mes,
                                                                             dt_ant,
                                                                             cd_recurso,
                                                                             dif_meses_aloc,
                                                                             min_dt_aloc,
                                                                             max_dt_aloc,
                                                                             pess_dt_rescisao
                                                                      from ( select al.cd_contrato_pratica,
                                                                                    al.cd_moeda,
                                                                                    al.login,
                                                                                    -------------------------------------------------------------------------------
                                                                                    -- Codigo do contrato pratica da ultima alocac?o anterior ao intervalo
                                                                                    -------------------------------------------------------------------------------
                                                                                    lag( al.cd_contrato_pratica, 1, null ) over( partition by al.cd_recurso, al.min_dt_aloc order by al.cd_recurso, al.dt_mes ) cd_cont_ant,
                                                                                    al.nome_contrato_pratica,
                                                                                    al.pratica,
                                                                                    -------------------------------------------------------------------------------
                                                                                    -- Nome do contrato pratica da ultima alocac?o anterior ao intervalo
                                                                                    -------------------------------------------------------------------------------
                                                                                    lag( al.nome_contrato_pratica, 1, null ) over( partition by al.cd_recurso, al.min_dt_aloc order by al.cd_recurso, al.dt_mes ) nome_cont_ant,
                                                                                    al.dt_mes,
                                                                                    -------------------------------------------------------------------------------
                                                                                    -- Mes da ultima alocac?o anterior ao intervalo
                                                                                    -------------------------------------------------------------------------------
                                                                                    lag( al.dt_mes, 1, null ) over( partition by al.cd_recurso, al.min_dt_aloc order by al.cd_recurso, al.dt_mes ) dt_ant,
                                                                                    --------------------------------------------------------------------------------------------------------------------
                                                                                    -- Numero de meses entre a alocac?o atual, e a ultima alocac?o, para identificar se realmente existe o intervalo
                                                                                    --------------------------------------------------------------------------------------------------------------------
                                                                                    months_between( al.dt_mes, lag( al.dt_mes, 1, null ) over( partition by al.cd_recurso, al.min_dt_aloc order by al.cd_recurso, al.dt_mes ) ) dif_meses_aloc,
                                                                                    al.cd_recurso,
                                                                                    al.min_dt_aloc,
                                                                                    al.max_dt_aloc,
                                                                                    al.pess_dt_rescisao
                                                                             from ( select ma.copr_cd_contrato_pratica cd_contrato_pratica,
                                                                                           cp4.moed_cd_moeda_padrao cd_moeda,
                                                                                           cp4.copr_nm_contrato_pratica nome_contrato_pratica,
                                                                                           prat4.prat_nm_pratica pratica,
                                                                                           al4.recu_cd_recurso cd_recurso,
                                                                                           ap4.alpe_dt_alocacao_periodo dt_mes,
                                                                                           pe4.pess_dt_rescisao,
                                                                                           pe4.pess_cd_login login,
                                                                                           ------------------------------------------------------------------
                                                                                           -- Menor e maior data de alocac?o para cada recurso
                                                                                           ------------------------------------------------------------------
                                                                                           min( ap4.alpe_dt_alocacao_periodo ) over( partition by al4.recu_cd_recurso ) min_dt_aloc,
                                                                                           max( ap4.alpe_dt_alocacao_periodo ) over( partition by al4.recu_cd_recurso ) max_dt_aloc
                                                                                    from pms.mapa_alocacao ma,
                                                                                         pms.alocacao al4,
                                                                                         pms.alocacao_periodo ap4,
                                                                                         pms.contrato_pratica cp4,
                                                                                         pms.pessoa pe4,
                                                                                         pms.pratica prat4
                                                                                    where ma.maal_cd_mapa_alocacao    = al4.maal_cd_mapa_alocacao and
                                                                                          ma.copr_cd_contrato_pratica = cp4.copr_cd_contrato_pratica and
                                                                                          al4.aloc_cd_alocacao        = ap4.aloc_cd_alocacao and
                                                                                          al4.recu_cd_recurso         = pe4.recu_cd_recurso and
                                                                                          cp4.prat_cd_pratica         = prat4.prat_cd_pratica and
                                                                                          -------------------------------------------------------------------
                                                                                          -- Somente mapas publicados, independente do status da alocac?o.
                                                                                          -------------------------------------------------------------------
                                                                                          ma.maal_in_versao           = 'PB'
                                                                                    order by ap4.alpe_dt_alocacao_periodo ) al ) al
                                                                             -----------------------------------------------
                                                                             -- Somente diferencas maior que 2 meses
                                                                             -----------------------------------------------
                                                                             where dif_meses_aloc > 2 ) al
                                                               --------------------------------------------------------------------------------------
                                                               -- Condic?o deve ser dessa forma, para considerar o intervalo correto de alocac?es
                                                               --------------------------------------------------------------------------------------
                                                               where al.login  = tot_al.login and
                                                                     al.dt_ant = tot_al.dt_mes and
                                                                     ---------------------------------------------------------
                                                                     -- Condic?o que identifica 2 ou mais alocac?es no mes
                                                                     ---------------------------------------------------------
                                                                     tot_al.total_aloc > 1 and
                                                                     --------------------------------------------------------------------------------------
                                                                     -- Condic?o deve ser dessa forma, para considerar o intervalo correto de alocac?es
                                                                     --------------------------------------------------------------------------------------
                                                                     dt.dt_data > al.dt_ant and
                                                                     dt.dt_data < al.dt_mes ) inf
                                                        where inf.cd_recurso = pes4.cd_recurso ) al
                                                 where tot_al.login  = al.login and
                                                       tot_al.dt_mes = al.dt_ant ) inf
                                          where -----------------------------------------------
                                                -- Vigencia da tabela PESSOA_TIPO_CONTRATO
                                                -----------------------------------------------
                                                inf.cd_pessoa = ptc.cd_pessoa (+) and
                                                inf.dt_mes    between ptc.petc_dt_inicio (+) and ptc.petc_dt_fim (+) ) inf
                                   where inf.login          = ult_tce.login (+) and
                                         --inf.cd_cidade_base = ult_cus_infra.cd_cidade_base (+) and
                                         --inf.dt_mes         = ult_cus_infra.dt_mes (+) and
                                         --inf.login          = ult_cus_infra.login (+) and
                                         inf.login          = tce.login (+) and
                                         inf.dt_mes         = tce.dt_mes (+) and
                                         inf.cd_cidade_base = cib.cd_cidade_base (+) and
                                         inf.dt_mes         = cib.dt_mes (+) and
                                         inf.login          = cib.login (+) and
                                         inf.cd_moeda       = moed.moed_cd_moeda (+) and
                                         inf.dt_mes         = moed.mes_cotacao (+)  ) inf
/*where inf.login = 'rsalles'
          and
          inf.dt_mes = '01-abr-2012'            */