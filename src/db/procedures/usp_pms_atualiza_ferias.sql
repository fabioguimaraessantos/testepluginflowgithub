create or replace procedure usp_pms_atualiza_ferias is

cursor c_historico_ferias is
   select f.dt_saida dt_inicio, f.dt_retorno dt_fim, f.tipo, p.pess_cd_pessoa, p.pess_cd_login
   from vw_adm_rh_ferias f, pessoa p
   where f.login = p.pess_cd_login
   and f.tipo <> 'V' --exceto vendidas
   and f.dt_saida >= to_date('01/01/2009', 'dd/mm/yyyy');

-- Constante do Id para tipo férias
const_cd_tipo_overhead CONSTANT number := 1;

v_cd_alocacao_overhead number := 0;
v_pr_alocacao number := 0;
v_dt_alocacao date;

v_dt_ponteiro date;
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
             
             v_dt_ponteiro := r_fer.dt_inicio;
    
             while (v_dt_ponteiro <= r_fer.dt_fim) loop
                 
                 -- mes de alocacao  
                 v_dt_alocacao :=  trunc(v_dt_ponteiro, 'mm');
                 -- fim periodo 
                 v_fim_periodo := least(last_day(v_dt_ponteiro), r_fer.dt_fim);
                 -- dias alocados no mes
                 v_dias_alocacao_mes := (v_fim_periodo - v_dt_ponteiro)+1;
                 -- numero de dias totais do mes
                 v_dias_total_mes := to_number(to_char(v_fim_periodo , 'dd'));
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
                  
                  v_dt_ponteiro := trunc(v_dt_ponteiro, 'mm')+1;       
                                                  
              end loop;       
               
          exception when others then
             rollback;
          end;
      end if;

end loop;
commit;

end usp_pms_atualiza_ferias;
