create or replace procedure usp_pms_atualiza_pess_tp_cont as
     cursor sel_inform is --- nao rodar esta procedure
            select cd_pessoa,
                   tc.tico_cd_tipo_contrato cd_tipo_contrato,
                   perc_alocavel,
                   dt_inicio,
                   case when login = lead( login, 1, null ) over( partition by login order by login )
                             then lead( dt_inicio, 1, null ) over( partition by login order by login ) - 1
                             else null end dt_fim,
                   trunc(sysdate) dt_criacao,
                   null empresa,
                   null vl_jornada
            from tipo_contrato tc,
                 ( select pe.pess_cd_pessoa cd_pessoa,
                          hist.login,
                          hist.mes,
                          case when hist.tp_contrato = '25'
                                    then 'PJC'
                               when hist.tp_contrato = '27'
                                    then 'PJF'
                               else hist.tp_contrato end tp_contrato,
                          to_date( '01/01/2010', 'DD/MM/YYYY') dt_inicio,
                          null dt_fim,
                          case when hist.tp_contrato = 'EST'
                                    then 0.75
                                    else 1 end perc_alocavel
                   from pessoa pe,
                        ( select login,
                                 to_date( '01/' || mes, 'DD/MM/YYYY') mes,
                                 tp_contrato,
                                 lag( tp_contrato, 1, null ) over( partition by login order by login , to_date( '01/' || mes, 'DD/MM/YYYY') ) tp_contrato_ant
                          from adm_rh.tb_bi_custo_func_mgr_aux t
                          where ( t.mes = '12/2009' or t.mes like '%2010%' )
                          group by login,
                                   to_date( '01/' || mes, 'DD/MM/YYYY'),
                                   tp_contrato
                          order by 1, 2 ) hist
                   where pe.pess_cd_login = hist.login and
                         tp_contrato_ant is null
                   union all
                   select cd_pessoa,
                          login,
                          mes,
                          case when tp_contrato = '25'
                                    then 'PJC'
                               when tp_contrato = '27'
                                    then 'PJF'
                               else tp_contrato end tp_contrato,
                          dt_inicio,
                          dt_fim,
                          perc_alocavel
                   from ( select pe.pess_cd_pessoa cd_pessoa,
                                 hist.login,
                                 hist.mes,
                                 hist.tp_contrato,
                                 min( hist.mes ) over( partition by hist.login ) dt_inicio,
                                 null dt_fim,
                                 case when hist.tp_contrato = 'EST'
                                           then 0.75
                                           else 1 end perc_alocavel
                          from pessoa pe,
                               ( select login,
                                        to_date( '01/' || mes, 'DD/MM/YYYY') mes,
                                        tp_contrato,
                                        lag( tp_contrato, 1, null ) over( partition by login
                                                                          order by login, to_date( '01/' || mes, 'DD/MM/YYYY') ) tp_contrato_ant
                                 from adm_rh.tb_bi_custo_func_mgr_aux t
                                 where ( t.mes = '12/2009' or t.mes like '%2010%' )
                                 group by login,
                                          to_date( '01/' || mes, 'DD/MM/YYYY'),
                                          tp_contrato
                                 order by 1, 2 ) hist
                          where pe.pess_cd_login = hist.login and
                                tp_contrato <> tp_contrato_ant and
                                tp_contrato_ant is not null and
                                tp_contrato = 'CLT' )
                   where mes = dt_inicio
                   order by login, dt_inicio ) tce
            where tc.tico_nm_tipo_contrato = tce.tp_contrato
            order by login, mes;
--
  v sel_inform%rowtype;
begin

     -----------------------------
     --Não rodar esta procedure --
     -----------------------------

     execute immediate 'truncate table pessoa_tipo_contrato';
     --
     open sel_inform;
     --
     fetch sel_inform into v;
     --
     while sel_inform%found loop
           insert into pessoa_tipo_contrato
           values( sq_petc_cd_pessoa_tp_contrato.nextval,
                   v.cd_pessoa,
                   v.cd_tipo_contrato,
                   v.perc_alocavel,
                   v.dt_inicio,
                   v.dt_fim,
                   v.dt_criacao,
                   v.empresa,
                   v.vl_jornada );
           --
           commit;
           --
           fetch sel_inform into v;
     end loop;
     --
     close sel_inform;
     --
     commit;
     
     -----------------------------
     --Não rodar esta procedure --
     -----------------------------
end usp_pms_atualiza_pess_tp_cont;