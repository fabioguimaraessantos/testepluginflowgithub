create or replace view vw_planejado_receita as
select inf.cd_contrato_pratica,
       inf.nome_contrato_pratica,
       inf.pratica,
       inf.dt_mes,
       inf.dt_mes_comparacao,
       inf.recu_cd_recurso,
       inf.aloc_in_estagio,
          case when aloc_in_estagio = 'CM'
                           then 'Commited'
                      when aloc_in_estagio = 'RV'
                           then 'Reserved'
                      when aloc_in_estagio = 'PH'
                           then 'Prospect High'
                      when aloc_in_estagio = 'PL'
                           then 'Prospect Low'
                       else aloc_in_estagio
       end aloc_in_estagio2,
       inf.cd_cidade_base_aloc,
       inf.sigla_cidade_base_aloc,
       inf.vlr_preco,
       inf.valor_fte,
       inf.valor_ut_rate,
       inf.valor_fte_ut_rate,
       inf.resource_code,
       inf.resource_name,
       inf.cd_moeda,
       inf.moeda,
       inf.valor_cotacao,
       inf.fator_ur_mes,
       inf.umkt,
       inf.bm,
       cpcl_sso.sso,
       inf.cd_grupo_custo,
       inf.nm_grupo_custo,
       inf.perfil_vendido,
       inf.perfil_padrao,
       inf.nome_perfil_vend
       --inf.aloc_in_estagio
from ( select inf.cd_contrato_pratica,
              inf.nome_contrato_pratica,
              inf.pratica,
              inf.dt_mes,
              inf.dt_mes_comparacao,
              inf.recu_cd_recurso,
              inf.aloc_in_estagio,
              inf.cd_cidade_base_aloc,
              inf.sigla_cidade_base_aloc,
              inf.vlr_preco,
              inf.valor_fte,
              inf.valor_ut_rate,
              inf.valor_fte_ut_rate,
              inf.resource_code,
              inf.resource_name,
              inf.cd_moeda,
              inf.moeda,
              inf.valor_cotacao,
              inf.fator_ur_mes,
              inf.umkt,
              cpcl_bm.bm,
              inf.cd_grupo_custo,
              inf.nm_grupo_custo,
              inf.perfil_vendido,
              inf.perfil_padrao,
              inf.nome_perfil_vend
       from ( select inf.cd_contrato_pratica,
                     inf.nome_contrato_pratica,
                     inf.pratica,
                     inf.dt_mes,
                     inf.dt_mes_comparacao,
                     inf.recu_cd_recurso,
                     inf.aloc_in_estagio,
                     inf.cd_cidade_base_aloc,
                     inf.sigla_cidade_base_aloc,
                     inf.vlr_preco,
                     inf.valor_fte,
                     inf.valor_ut_rate,
                     inf.valor_fte_ut_rate,
                     inf.resource_code,
                     inf.resource_name,
                     inf.cd_moeda,
                     inf.moeda,
                     inf.valor_cotacao,
                     inf.fator_ur_mes,
                     cl.celu_nm_centro_lucro umkt,
                     inf.cd_grupo_custo,
                     inf.nm_grupo_custo,
                     inf.perfil_vendido,
                     inf.perfil_padrao,
                     inf.nome_perfil_vend
              from ( select inf.cd_contrato_pratica,
                            inf.nome_contrato_pratica,
                            inf.pratica,
                            inf.dt_mes,
                            inf.dt_mes_comparacao,
                            inf.recu_cd_recurso,
                            inf.aloc_in_estagio,
                            inf.cd_cidade_base_aloc,
                            inf.sigla_cidade_base_aloc,
                            inf.vlr_preco,
                            inf.valor_fte,
                            inf.valor_ut_rate,
                            inf.valor_fte_ut_rate,
                            inf.resource_code,
                            inf.resource_name,
                            m.moed_cd_moeda cd_moeda,
                            m.moed_sg_moeda moeda,
                            cm.valor_cotacao,
                            inf.fator_ur_mes,
                            inf.cd_grupo_custo,
                            inf.nm_grupo_custo,
                            inf.perfil_vendido,
                            inf.perfil_padrao,
                            inf.nome_perfil_vend
                     from moeda m,
                          ( select moed_cd_moeda,
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
                                         --m.moed_cd_moeda = 2 and
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
                            order by 1, 2 ) cm,
                          ( select cd_contrato_pratica,
                                   nome_contrato_pratica,
                                   pratica,
                                   cd_moeda,
                                   dt_mes,
                                   last_day( dt_mes ) dt_mes_comparacao,
                                   recu_cd_recurso,
                                   aloc_in_estagio,
                                   cd_cidade_base_aloc,
                                   sigla_cidade_base_aloc,
                                   vlr_preco,
                                   valor_fte,
                                   valor_ut_rate,
                                   ( valor_fte * valor_ut_rate ) valor_fte_ut_rate,
                                   login resource_code,
                                   nome_pessoa resource_name,
                                   fator_ur_mes,
                                   cd_grupo_custo,
                                   nm_grupo_custo,
                                   perfil_vendido,
                                   perfil_padrao,
                                   nome_perfil_vend
                             from  (  select inf0.*,
                                              gc.grcu_cd_grupo_custo cd_grupo_custo,
                                              gc.grcu_nm_grupo_custo nm_grupo_custo
                                    from
                                     ( select cp.copr_cd_contrato_pratica         cd_contrato_pratica,
                                          cp.copr_nm_contrato_pratica         nome_contrato_pratica,
                                          --cp.moed_cd_moeda_padrao             cd_moeda,
                                          pv.moed_cd_moeda cd_moeda,
                                          prat.prat_nm_pratica                pratica,
                                          tp.tapr_cd_tabela_preco,
                                          tp.tapr_dt_inicio_vigencia,
                                          case when tp.tapr_dt_fim_vigencia is not null
                                                    then tp.tapr_dt_fim_vigencia
                                                    else ap.alpe_dt_alocacao_periodo end tapr_dt_fim_vigencia,
                                          ma.maal_tx_titulo,
                                          itp.ittp_vl_item_tb_preco,
                                          al.vlr_ut_rate * fur.faum_pr_fator_ur                     valor_ut_rate,
                                          ap.alpe_pr_alocacao_periodo          valor_fte,
                                          ap.alpe_pr_alocacao_periodo,
                                          ap.alpe_dt_alocacao_periodo          dt_mes,
                                          itp.ittp_vl_item_tb_preco * 168      valor_por_fte,
                                          ( itp.ittp_vl_item_tb_preco * 168 ) * vlr_ut_rate * ap.alpe_pr_alocacao_periodo * fur.faum_pr_fator_ur  vlr_preco,
                                          al.aloc_in_estagio,
                                          al.recu_cd_recurso,
                                          al.cd_cidade_base_aloc,
                                          al.sigla_cidade_base_aloc,
                                          case when pes.recu_cd_recurso is not null
                                                    then pes.pess_cd_login
                                                    else rec.recu_cd_mnemonico end login,
                                          pes.pess_nm_pessoa nome_pessoa,
                                          pes.pess_cd_pessoa,
                                          fur.faum_pr_fator_ur fator_ur_mes,
                                          pv.peve_cd_perfil_vendido perfil_vendido,
                                          pv.pepa_cd_perfil_padrao perfil_padrao,
                                          pv.peve_nm_perfil_vendido nome_perfil_vend
                                   from ( select aloc_cd_alocacao,
                                                 cd_mapa_alocacao,
                                                 cd_perfil_vendido,
                                                 vlr_ut_rate,
                                                 aloc_in_estagio,
                                                 recu_cd_recurso,
                                                 ciba_cd_cidade_base cd_cidade_base_aloc,
                                                 sigla_cidade_base_aloc
                                          from ( select al.aloc_cd_alocacao,
                                                        al.maal_cd_mapa_alocacao  cd_mapa_alocacao,
                                                        al.peve_cd_perfil_vendido cd_perfil_vendido,
                                                        al.aloc_vl_ur vlr_ut_rate,
                                                        al.aloc_in_estagio,
                                                        al.recu_cd_recurso,
                                                        al.ciba_cd_cidade_base,
                                                        cb.ciba_sg_cidade_base sigla_cidade_base_aloc
                                                 from alocacao al,
                                                      cidade_base cb
                                                 where al.ciba_cd_cidade_base = cb.ciba_cd_cidade_base ) al ) al,
                                        tabela_preco tp,
                                        item_tabela_preco itp,
                                        perfil_vendido pv,
                                        alocacao_periodo ap,
                                        contrato_pratica cp,
                                        pratica prat,
                                        mapa_alocacao ma,
                                        fator_ur_mes fur,
                                        recurso rec,
                                        pessoa pes,
                                        msa msa
                                   where tp.tapr_cd_tabela_preco     = itp.tapr_cd_tabela_preco    and
                                         itp.peve_cd_perfil_vendido  = pv.peve_cd_perfil_vendido   and
                                         al.cd_perfil_vendido        = itp.peve_cd_perfil_vendido  and
                                         al.aloc_cd_alocacao         = ap.aloc_cd_alocacao         and
                                         cp.copr_cd_contrato_pratica = ma.copr_cd_contrato_pratica and
                                         cp.prat_cd_pratica          = prat.prat_cd_pratica        and
                                         ma.maal_cd_mapa_alocacao    = al.cd_mapa_alocacao         and
                                         -- ma.copr_cd_contrato_pratica = tp.copr_cd_contrato_pratica and
                                         cp.msaa_cd_msa              = msa.msaa_cd_msa             and
                                         tp.msaa_cd_msa              = msa.msaa_cd_msa             and
                                         ma.maal_cd_mapa_alocacao    = fur.maal_cd_mapa_alocacao   and
                                         ap.alpe_dt_alocacao_periodo = fur.faum_dt_mes             and
                                         al.recu_cd_recurso          = rec.recu_cd_recurso (+)     and
                                         rec.recu_cd_recurso         = pes.recu_cd_recurso (+)     and
                                    --     cp.copr_nm_contrato_pratica = 'MRV-AMS' and
                                     --    ap.alpe_dt_alocacao_periodo = '01-ago-2012' and
                                         ma.maal_in_versao           = 'PB' ) inf0,
                                         (select pess_cd_pessoa,
                                                      grcu_cd_grupo_custo,
                                                      pgc.pegc_dt_inicio,
                                                      (case when pgc.pegc_dt_fim is null then
                                                            (select para_dt_parametro from   parametro)
                                                            else pgc.pegc_dt_fim
                                                       end) pegc_dt_fim
                                               from   pessoa_grupo_custo pgc) pgc,
                                               grupo_custo gc
                                       where  inf0.pess_cd_pessoa      = pgc.pess_cd_pessoa(+) and
                                              pgc.grcu_cd_grupo_custo = gc.grcu_cd_grupo_custo(+) and
                                              inf0.dt_mes between pgc.pegc_dt_inicio(+) and (case when pgc.pegc_dt_fim(+) is null then
                                                                                                  (select para_dt_parametro from   parametro) else
                                                                                                   pgc.pegc_dt_fim(+) end))
                            where dt_mes between tapr_dt_inicio_vigencia and tapr_dt_fim_vigencia  ) inf
                     where inf.cd_moeda            = m.moed_cd_moeda  and
                           inf.cd_moeda            = cm.moed_cd_moeda (+) and
                           inf.dt_mes              = cm.mes_cotacao (+) ) inf,
                   ( select cpcl.copr_cd_contrato_pratica,
                            cpcl.celu_cd_centro_lucro,
                            cpcl.cpcl_dt_inicio_vigencia,
                            case when cpcl.cpcl_dt_fim_vigencia is not null
                                      then cpcl.cpcl_dt_fim_vigencia
                                      else ( select para_dt_parametro
                                             from parametro p1
                                             where p1.para_cd_parametro = 1 ) end cpcl_dt_fim_vigencia
                     from contrato_pratica_centro_lucro cpcl ) cpcl,
                   centro_lucro cl
              where inf.cd_contrato_pratica = cpcl.copr_cd_contrato_pratica (+) and
                    cpcl.celu_cd_centro_lucro = cl.celu_cd_centro_lucro and
                    cl.nacl_cd_natureza = 1 and
                    inf.dt_mes between cpcl.cpcl_dt_inicio_vigencia (+) and cpcl.cpcl_dt_fim_vigencia (+) ) inf,
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
                    cl.nacl_cd_natureza = 3 ) cpcl_bm
       where inf.cd_contrato_pratica = cpcl_bm.copr_cd_contrato_pratica (+) and
             inf.dt_mes between cpcl_bm.cpcl_dt_inicio_vigencia (+) and cpcl_bm.cpcl_dt_fim_vigencia (+) ) inf,
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
where inf.cd_contrato_pratica = cpcl_sso.copr_cd_contrato_pratica (+) and
      inf.dt_mes between cpcl_sso.cpcl_dt_inicio_vigencia (+) and cpcl_sso.cpcl_dt_fim_vigencia (+)