create or replace view vw_realizado_receita as
select cd_contrato_pratica,
       nome_contrato_pratica,
       dt_mes,
       resource_code,
       resource_name,
       valor_cotacao,
       cd_moeda,
       moeda,
       vlr_preco,
       valor_fte,
       vlr_fte_old,
       rece_cd_receita,
       umkt,
       sso,
       bm,
       pratica,
       nome_perfil_vendido,
       cd_grupo_custo,
       nm_grupo_custo,
       perfil_vendido,
       perfil_padrao
from ( select cd_contrato_pratica,
              nome_contrato_pratica,
              dt_mes,
              resource_code,
              resource_name,
              valor_cotacao,
              cd_moeda,
              moeda,
              vlr_preco,
              valor_fte,
              vlr_fte_old,
              rece_cd_receita,
              nlinha,
              umkt,
              sso,
              bm,
              pratica,
              nome_perfil_vendido,
              cd_grupo_custo,
              nm_grupo_custo,
              row_number() over( partition by cd_contrato_pratica, dt_mes, resource_code order by     cd_contrato_pratica, dt_mes, resource_code ) nlinha2,
              perfil_vendido,
              perfil_padrao
       from ( select cd_contrato_pratica,
                     nome_contrato_pratica,
                     dt_mes,
                     versao,
                     resource_code,
                     resource_name,
                     valor_cotacao,
                     cd_moeda,
                     moeda,
                     vlr_preco,
                     valor_fte,
                     vlr_fte_old,
                     rece_cd_receita,
                     umkt,
                     sso,
                     bm,
                     pratica,
                     nome_perfil_vendido,
                     cd_grupo_custo,
                     nm_grupo_custo,
                     case when umkt is not null then 1
                          when sso is not null then 2
                          when bm is not null then 3
                          when pratica is not null then 4
                     end nlinha,
                     perfil_vendido,
                     perfil_padrao
              from ( select inf.cd_contrato_pratica,
                            inf.nome_contrato_pratica,
                            inf.dt_mes,
                            inf.versao,
                            inf.resource_code,
                            inf.resource_name,
                            inf.valor_cotacao,
                            inf.cd_moeda,
                            inf.moeda,
                            inf.vlr_preco,
                            inf.valor_fte,
                            inf.vlr_fte_old,
                            inf.rece_cd_receita,
                            inf.nome_perfil_vendido,
                            inf.cd_grupo_custo,
                            inf.nm_grupo_custo,
                            atrib.umkt,
                            atrib.sso,
                            atrib.bm,
                            atrib.pratica,
                            row_number() over( partition by inf.cd_contrato_pratica, inf.dt_mes, inf.resource_code, atrib.umkt, atrib.sso, atrib.bm, inf.perfil_vendido, inf.valor_fte
                                               order by     inf.cd_contrato_pratica, inf.dt_mes, inf.resource_code, atrib.umkt, atrib.sso, atrib.bm, inf.perfil_vendido ) nlinha,
                            inf.perfil_vendido,
                            inf.perfil_padrao
                     from ( select inf.cd_contrato_pratica,
                                   inf.nome_contrato_pratica,
                                   inf.pratica,
                                   inf.dt_mes,
                                   inf.versao,
                                   inf.resource_code,
                                   inf.resource_name,
                                   inf.valor_cotacao,
                                   inf.cd_moeda,
                                   inf.moeda,
                                   inf.vlr_preco,
                                   inf.valor_fte,
                                   inf.vlr_fte_old,
                                   inf.rece_cd_receita,
                                   inf.nome_perfil_vendido,
                                       inf.cd_grupo_custo,
                                       inf.nm_grupo_custo,
                                   inf.perfil_vendido,
                                   inf.perfil_padrao
                            from ( select cd_contrato_pratica,
                                          nome_contrato_pratica,
                                          pratica,
                                          dt_mes,
                                          versao,
                                          resource_code,
                                          resource_name,
                                          valor_cotacao,
                                          cd_moeda,
                                          moeda,
                                          valor vlr_preco,
                                          vlr_total_fte  valor_fte,
                                          vlr_fte_old,
                                          rece_cd_receita,
                                          nlinha,
                                          nome_perfil_vendido,
                                               cd_grupo_custo,
                                               nm_grupo_custo,
                                          perfil_vendido,
                                          perfil_padrao
                                   from ( select cd_contrato_pratica,
                                                 nome_contrato_pratica,
                                                 pratica,
                                                 versao,
                                                 dt_mes,
                                                 resource_code,
                                                 resource_name,
                                                 valor_cotacao,
                                                 cd_moeda,
                                                 moeda,
                                                 vlr_preco_hora,
                                                 total,
                                                 vlr_total_fte vlr_fte_old,
                                                 sum( vlr_total_fte ) over( partition by cd_contrato_pratica, dt_mes, perfil_vendido, resource_code
                                                                            order by     cd_contrato_pratica, dt_mes, perfil_vendido, resource_code )  vlr_total_fte,
                                                 rece_cd_receita,
                                                 nome_perfil_vendido,
                                                 cd_grupo_custo,
                                                 nm_grupo_custo,
                                                 -- Essa divis?o e necessaria, para n?o duplicar informac?es de receita, por causa do relacionamento com o
                                                 -- Fiscal Deal. A clausula THEN, trata os casos que tem mais de um Fiscal Deal. A clausula ELSE, trata os
                                                 -- casos onde ha somente um FISCAL_DEAL, porem, com mais de uma ocorrencia de receita para o mesmo.

                                                 sum( vlr_preco_hora * vlr_total_fte * 168 ) over( partition by cd_contrato_pratica, dt_mes, perfil_vendido, resource_code order by     cd_contrato_pratica, dt_mes, resource_code ) valor,
                                                 row_number() over( partition by cd_contrato_pratica, dt_mes, perfil_vendido, resource_code
                                                                    order by     cd_contrato_pratica, dt_mes, perfil_vendido, resource_code ) nlinha,
                                                 perfil_vendido,
                                                 perfil_padrao
                                         from (select inf.*,
                                                               gc.grcu_cd_grupo_custo cd_grupo_custo,
                                                               gc.grcu_nm_grupo_custo nm_grupo_custo
                                          from ( select rc.copr_cd_contrato_pratica cd_contrato_pratica,
                                                        cp.copr_nm_contrato_pratica nome_contrato_pratica,
                                                        prat.prat_nm_pratica pratica,
                                                        rc.rece_dt_mes dt_mes,
                                                        rc.rece_in_versao versao,
                                                        tp.tapr_cd_tabela_preco,
                                                        ir.peve_cd_perfil_vendido,
                                                        ir.nome_perfil_vendido,
                                                        pes.resource_code,
                                                        pes.resource_name,
                                                        cm.como_vl_cotacao valor_cotacao,
                                                        m.moed_cd_moeda cd_moeda,
                                                        m.moed_sg_moeda moeda,
                                                        ir.vlr_total_fte vlr_total_fte,
                                                        rc.rece_cd_receita,
                                                        ir.ir_vlr_preco_hora vlr_preco_hora,
                                                        pes.pess_cd_pessoa,
                                                        pes.total,
                                                        pv.peve_cd_perfil_vendido perfil_vendido,
                                                        pv.pepa_cd_perfil_padrao perfil_padrao
                                                 from receita rc,
                                                      cotacao_moeda cm,
                                                      moeda m,
                                                      contrato_pratica cp,
                                                      pratica prat,
                                                      tabela_preco tp,
                                                      item_tabela_preco itp,
                                                      perfil_vendido pv,
                                                      receita_moeda recmoe,
                                                      ( select pes.rece_cd_receita,
                                                               pes.pess_cd_pessoa,
                                                               pes.pess_cd_login  resource_code,
                                                               pes.pess_nm_pessoa resource_name,
                                                               total
                                                        from ( select remo.rece_cd_receita,
                                                                      pes.pess_cd_pessoa,
                                                                      pes.pess_cd_login,
                                                                      pes.pess_nm_pessoa,
                                                                      count( pes.pess_cd_login ) over( partition by remo.rece_cd_receita, pes.pess_cd_login ) total,
                                                                      row_number() over( partition by remo.rece_cd_receita, pes.pess_cd_login order by remo.rece_cd_receita, pes.pess_cd_login ) nlinha
                                                               from item_receita irec,
                                                                    receita_moeda remo,
                                                                    pessoa pes
                                                               where irec.pess_cd_pessoa = pes.pess_cd_pessoa
                                                               and irec.remo_cd_receita_moeda = remo.remo_cd_receita_moeda) pes
                                                        where nlinha = 1  ) pes,
                                                        ( select copr_cd_contrato_pratica,
                                                                 rece_dt_mes,
                                                                 rece_cd_receita,
                                                                 peve_cd_perfil_vendido,
                                                                 nome_perfil_vendido,
                                                                 vlr_total_fte,
                                                                 pess_cd_login,
                                                                 ir_vlr_preco_hora
                                                          from ( select r1.copr_cd_contrato_pratica,
                                                                        r1.rece_dt_mes,
                                                                        r1.rece_in_versao,
                                                                        ir2.rece_cd_receita,
                                                                        ir2.ir_vlr_preco_hora,
                                                                        p1.pess_cd_login,
                                                                        ir2.peve_cd_perfil_vendido,
                                                                        ir2.nome_perfil_vendido,
                                                                        sum( ir2.vlr_total_fte ) over( partition by r1.copr_cd_contrato_pratica, r1.rece_dt_mes, p1.pess_cd_login, ir2.peve_cd_perfil_vendido ) vlr_total_fte,
                                                                        row_number() over( partition by r1.copr_cd_contrato_pratica, r1.rece_dt_mes, p1.pess_cd_login
                                                                                           order by r1.copr_cd_contrato_pratica, r1.rece_dt_mes, p1.pess_cd_login ) nlinha
                                                                 from receita r1,
                                                                      pessoa p1,
                                                                      ( select rece_cd_receita,
                                                                               peve_cd_perfil_vendido,
                                                                               nome_perfil_vendido,
                                                                               pess_cd_pessoa,
                                                                               ir_vlr_preco_hora,
                                                                               vlr_total_fte
                                                                        from ( select rm.rece_cd_receita,
                                                                                      ir.peve_cd_perfil_vendido,
                                                                                      ir.pess_cd_pessoa,
                                                                                      ir.itre_vl_perfil_vendido_hora ir_vlr_preco_hora,
                                                                                      pv.peve_nm_perfil_vendido nome_perfil_vendido,
                                                                                      sum( ir.itre_nr_fte ) over( partition by rm.rece_cd_receita, ir.peve_cd_perfil_vendido, ir.pess_cd_pessoa ) vlr_total_fte,
                                                                                      row_number() over( partition by rm.rece_cd_receita, ir.peve_cd_perfil_vendido, ir.pess_cd_pessoa order by rm.rece_cd_receita, ir.peve_cd_perfil_vendido  ) nlinha
                                                                               from item_receita ir,
                                                                                    receita_moeda rm,
                                                                                    perfil_vendido pv
                                                                               where ir.peve_cd_perfil_vendido = pv.peve_cd_perfil_vendido 
                                                                               and rm.remo_cd_receita_moeda = ir.remo_cd_receita_moeda)
                                                                        where nlinha = 1 ) ir2
                                                                 where r1.rece_cd_receita = ir2.rece_cd_receita and
                                                                       ir2.pess_cd_pessoa  = p1.pess_cd_pessoa ) ) ir
                                                 where rc.copr_cd_contrato_pratica = cp.copr_cd_contrato_pratica  and
                                                       cp.prat_cd_pratica          = prat.prat_cd_pratica         and
                                                       recmoe.rece_cd_receita      = rc.rece_cd_receita           and
                                                       recmoe.como_cd_cotacao_moeda= cm.como_cd_cotacao_moeda (+) and
                                                       cm.moed_cd_moeda            = m.moed_cd_moeda (+)          and
                                                       cp.msaa_cd_msa = tp.msaa_cd_msa                            and
                                                       rc.rece_cd_receita          = ir.rece_cd_receita           and
                                                       rc.rece_cd_receita          = pes.rece_cd_receita          and
                                                       pes.resource_code           = ir.pess_cd_login             and
                                                       tp.tapr_cd_tabela_preco     = itp.tapr_cd_tabela_preco     and
                                                       itp.peve_cd_perfil_vendido  = pv.peve_cd_perfil_vendido    and
                                                       ir.peve_cd_perfil_vendido   = itp.peve_cd_perfil_vendido   and
                                                       rc.rece_in_versao in ('PB', 'IN', 'PD') and
                                                       rc.rece_dt_mes between tp.tapr_dt_inicio_vigencia and (case when tp.tapr_dt_fim_vigencia is not null then
                                                                                                                        tp.tapr_dt_fim_vigencia else
                                                                                                                        trunc(sysdate) end)) inf, /*#*/
                                                      (select pess_cd_pessoa,
                                                              grcu_cd_grupo_custo,
                                                              pgc.pegc_dt_inicio,
                                                              (case
                                                                 when pgc.pegc_dt_fim is null then
                                                                  (select para_dt_parametro from   parametro)
                                                                 else pgc.pegc_dt_fim
                                                              end) pegc_dt_fim
                                                       from   pessoa_grupo_custo pgc) pgc,
                                                      grupo_custo gc
                                               where  inf.pess_cd_pessoa      = pgc.pess_cd_pessoa(+) and
                                                      pgc.grcu_cd_grupo_custo = gc.grcu_cd_grupo_custo(+) and
                                                      inf.dt_mes between pgc.pegc_dt_inicio(+) and (case when pgc.pegc_dt_fim(+) is null then
                                                                                                              (select para_dt_parametro from   parametro) else
                                                                                                               pgc.pegc_dt_fim(+) end)))) inf ) inf,
                          ( select dt_mes,
                                   cd_contrato_pratica,
                                   umkt,
                                   sso,
                                   lob pratica,
                                   bm
                            from ( select dt_mes,
                                          cd_contrato_pratica,
                                          max( case when flag = 1 then nome_centro_lucro else '' end ) over( partition by dt_mes, cd_contrato_pratica ) umkt,
                                          max( case when flag = 2 then nome_centro_lucro else '' end ) over( partition by dt_mes, cd_contrato_pratica ) sso,
                                          max( case when flag = 3 then nome_centro_lucro else '' end ) over( partition by dt_mes, cd_contrato_pratica ) bm,
                                          max( case when flag = 4 then nome_centro_lucro else '' end ) over( partition by dt_mes, cd_contrato_pratica ) lob,
                                          row_number() over( partition by dt_mes, cd_contrato_pratica order by dt_mes, cd_contrato_pratica ) nlinha
                                   from ( select dt.dt_data dt_mes,
                                                 inf.cd_contrato_pratica,
                                                 inf.nome_centro_lucro,
                                                 inf.cd_natureza,
                                                 inf.nome_natureza_clucro,
                                                 case when inf.cd_natureza = 1 then 1
                                                      when inf.cd_natureza = 2 then 2
                                                      when inf.cd_natureza = 3 then 3
                                                      when inf.cd_natureza = 61 then 4
                                                 end flag
                                          from ( select dt.dt_data
                                                 from tb_bi_datas_corridas dt
                                                 where to_char( dt.dt_data, 'DD' ) = '01' and
                                                       dt.dt_data >= to_date( '01/01/2010', 'DD/MM/YYYY' ) and
                                                       dt.dt_data <= ( select para_dt_parametro
                                                                       from parametro
                                                                       where para_cd_parametro = 1 )  ) dt,
                                               ( select cpcl.copr_cd_contrato_pratica cd_contrato_pratica,
                                                        cpcl.cpcl_dt_inicio_vigencia,
                                                        case when cpcl.cpcl_dt_fim_vigencia is not null
                                                                  then cpcl.cpcl_dt_fim_vigencia
                                                                  else ( select para_dt_parametro
                                                                         from parametro p1
                                                                         where p1.para_cd_parametro = 1 ) end cpcl_dt_fim_vigencia,
                                                        cl.celu_cd_centro_lucro cd_centro_lucro,
                                                        cl.celu_nm_centro_lucro nome_centro_lucro,
                                                        ncl.nacl_cd_natureza    cd_natureza,
                                                        ncl.nacl_nm_natureza    nome_natureza_clucro
                                                 from contrato_pratica_centro_lucro cpcl,
                                                      centro_lucro cl,
                                                      natureza_centro_lucro ncl
                                                 where cpcl.celu_cd_centro_lucro = cl.celu_cd_centro_lucro and
                                                       cl.nacl_cd_natureza       = ncl.nacl_cd_natureza
                                                 order by cpcl.copr_cd_contrato_pratica,
                                                          ncl.nacl_cd_natureza ) inf
                                          where dt.dt_data between inf.cpcl_dt_inicio_vigencia and inf.cpcl_dt_fim_vigencia
                                          order by dt.dt_data,
                                                   inf.cd_natureza ) inf ) inf
                            where nlinha = 1  ) atrib
                     where inf.cd_contrato_pratica = atrib.cd_contrato_pratica (+) and
                           inf.dt_mes              = atrib.dt_mes (+) )
              where nlinha = 1 ) )
where nlinha = 1