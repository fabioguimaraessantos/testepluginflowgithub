create or replace procedure usp_pms_ferias is
--INTERVAL: trunc(sysdate +1) + 22/24

cursor c_historico_ferias is
/*   select f.dt_saida dt_inicio, f.dt_retorno dt_fim, f.tipo, p.pess_cd_pessoa, p.pess_cd_login
   from vw_hrs_ferias f, pessoa p
   where f.login = p.pess_cd_login
   and f.dt_saida >= to_date('01/01/2009', 'dd/mm/yyyy');
   
A alteração foi feita para que nenhum registro de férias do passado (em meses fechados do mapa de aloc.) 
fossem alteradas - katsumi@ciandt.com - 05/12/2011   
*/   
   select f.dt_saida dt_inicio, f.dt_retorno dt_fim, f.tipo, p.pess_cd_pessoa, p.pess_cd_login
   from vw_hrs_ferias f, pessoa p, modulo m
   where f.login = p.pess_cd_login
   and m.modu_cd_modulo = 1
   and f.dt_saida >= to_date('01/01/2009', 'dd/mm/yyyy')
   and f.dt_saida >= add_months(m.modu_dt_fechamento, 1);

cursor c_remove_ferias is
/*select ao.alov_cd_alocacao_overhead
from alocacao_overhead ao, pessoa p
where ao.pess_cd_pessoa = p.pess_cd_pessoa
and ao.tiov_cd_tipo_overhead = 1
and not exists (select null from vw_hrs_ferias f
                where f.login = p.pess_cd_login
                and f.dt_saida = ao.alov_dt_inicio
                and f.dt_retorno = ao.alov_dt_fim
                and f.tipo = ao.alov_in_status );
                
A alteração foi feita para que nenhum registro de férias do passado (em meses fechados do mapa de aloc.) 
fossem alteradas - katsumi@ciandt.com - 05/12/2011                
                */
select ao.alov_cd_alocacao_overhead
from alocacao_overhead ao, pessoa p, modulo m
where ao.pess_cd_pessoa = p.pess_cd_pessoa
and m.modu_cd_modulo = 1
and ao.tiov_cd_tipo_overhead = 1
and ao.alov_dt_inicio > add_months(m.modu_dt_fechamento, 1)
and not exists (select null from vw_hrs_ferias f
                where f.login = p.pess_cd_login
                and f.dt_saida = ao.alov_dt_inicio
                and f.dt_retorno = ao.alov_dt_fim
                and f.tipo = ao.alov_in_status);                

-- Constante do Id para tipo férias
const_cd_tipo_overhead CONSTANT number := 1;

v_cd_alocacao_overhead number := 0;

--Variaveis para calculo do percentual de alocacao
v_pr_alocacao number := 0;
v_dt_alocacao date;
v_ini_periodo date;
v_fim_periodo date;
v_dias_alocacao_mes number := 0;
v_dias_total_mes number :=0;

begin

for r_fer in c_historico_ferias loop

     -- verifica se já foi cadastrado o periodo para o login
     select nvl(max(ao.alov_cd_alocacao_overhead ), 0)
     into v_cd_alocacao_overhead
     from alocacao_overhead ao
     where ao.pess_cd_pessoa = r_fer.pess_cd_pessoa
     and ao.alov_dt_inicio = r_fer.dt_inicio
     and ao.alov_dt_fim = r_fer.dt_fim;

     -- ferias não cadastradas
     if (v_cd_alocacao_overhead = 0 ) then
         -- Seleciona o código da alocacao overhead
         select sq_alov_cd_alocacao_overhead.nextval
         into v_cd_alocacao_overhead
         from dual;

         begin
             -- insere na tabela alocacao_overhead
             insert into alocacao_overhead(alov_cd_alocacao_overhead, pess_cd_pessoa, tiov_cd_tipo_overhead, alov_dt_inicio, alov_dt_fim, alov_in_status)
             values(v_cd_alocacao_overhead, r_fer.pess_cd_pessoa, const_cd_tipo_overhead, r_fer.dt_inicio, r_fer.dt_fim, r_fer.tipo );

             v_ini_periodo := r_fer.dt_inicio;

             while (v_ini_periodo <= r_fer.dt_fim) loop

                 -- mes de alocacao
                 v_dt_alocacao :=  trunc(v_ini_periodo, 'mm');
                 -- fim periodo
                 v_fim_periodo := least(last_day(v_ini_periodo), r_fer.dt_fim);
                 -- dias alocados no mes
                 v_dias_alocacao_mes := (v_fim_periodo - v_ini_periodo)+1;
                 -- numero de dias totais do mes
                 v_dias_total_mes := to_number(to_char(last_day(v_ini_periodo) , 'dd'));
                 -- Percentual de alocaçao
                 v_pr_alocacao:= v_dias_alocacao_mes / v_dias_total_mes;

                 -- insere na tabela alocacao_periodo_oh
                 insert into alocacao_periodo_oh(alpo_cd_aloc_periodo_oh
                                                ,alov_cd_alocacao_overhead
                                                ,alpo_dt_aloc_periodo_oh
                                                ,alpo_pr_aloc_periodo_oh)
                  values( sq_alpo_cd_aloc_periodo_oh.nextval
                         ,v_cd_alocacao_overhead
                         ,v_dt_alocacao
                         ,v_pr_alocacao );

                  v_ini_periodo := add_months(trunc(v_ini_periodo, 'mm'), 1);

              end loop;

          exception when others then
             rollback;
          end;
      -- cadastro de férias já feito
      else

          select nvl(max(ao.alov_cd_alocacao_overhead ), 0)
          into v_cd_alocacao_overhead
          from alocacao_overhead ao
          where ao.pess_cd_pessoa = r_fer.pess_cd_pessoa
          and ao.alov_dt_inicio = r_fer.dt_inicio
          and ao.alov_dt_fim = r_fer.dt_fim
          and ao.alov_in_status = r_fer.tipo;

          -- o status está diferente
          if (v_cd_alocacao_overhead = 0) then
              -- atualiza o status
              update alocacao_overhead ao
              set ao.alov_in_status = r_fer.tipo
              where ao.alov_dt_inicio = r_fer.dt_inicio
              and ao.alov_dt_fim = r_fer.dt_fim
              and ao.pess_cd_pessoa = r_fer.pess_cd_pessoa;
          end if;

      end if;

end loop;
commit;

-- remove as ferias
for r_rem in c_remove_ferias loop

    -- remove os períodos da alocacao
    delete alocacao_periodo_oh al
    where al.alov_cd_alocacao_overhead = r_rem.alov_cd_alocacao_overhead;
    -- remove a alocacao
    delete alocacao_overhead ao
    where ao.alov_cd_alocacao_overhead = r_rem.alov_cd_alocacao_overhead;

    commit;
end loop;


end usp_pms_ferias;