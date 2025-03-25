create or replace procedure usp_pms_carga_salario as

cursor c_salario_mensalista is
select  t.login, t.dt_inicio, t.dt_fim, t.valor ,t.nr_horas, p.pess_cd_pessoa, m.moed_cd_moeda
from vw_pms_colab_hist_sal_br_men@ln_mgrh.cit t, pessoa p, moeda m
where t.login =  p.pess_cd_login
and t.sg_moeda = m.moed_sg_moeda;

cursor c_salario_horista is
select  t.login, t.dt_inicio, t.dt_fim, t.valor ,t.nr_horas, p.pess_cd_pessoa, m.moed_cd_moeda
from vw_pms_colab_hist_sal_br_hor@ln_mgrh.cit t, pessoa p, moeda m
where t.login =  p.pess_cd_login
and t.sg_moeda = m.moed_sg_moeda;

cursor c_salario_exterior is
select  t.login, t.dt_inicio, t.dt_fim, t.valor ,t.nr_horas, p.pess_cd_pessoa, m.moed_cd_moeda
from vw_pms_colab_hist_sal_ext@ln_mgrh.cit t, pessoa p, moeda m
where t.login =  p.pess_cd_login
and t.sg_moeda = m.moed_sg_moeda;


begin

   for r_sm in c_salario_mensalista loop

       insert into pessoa_salario(pesa_cd_pessoa_salario
                                  ,pess_cd_pessoa
                                  ,moed_cd_moeda
                                  ,pesa_vl_salario
                                  ,pesa_vl_hora
                                  ,pesa_dt_inicio
                                  ,pesa_dt_fim
                                  ,pesa_dt_criacao)
       values( sq_pesa_cd_pessoa_salario.nextval
               ,r_sm.pess_cd_pessoa
               ,r_sm.moed_cd_moeda
               ,r_sm.valor
               ,r_sm.valor/r_sm.nr_horas
               ,r_sm.dt_inicio
               ,r_sm.dt_fim
               ,sysdate);
   end loop;
   
      for r_hr in c_salario_horista loop

       insert into pessoa_salario(pesa_cd_pessoa_salario
                                  ,pess_cd_pessoa
                                  ,moed_cd_moeda
                                  ,pesa_vl_salario
                                  ,pesa_vl_hora
                                  ,pesa_dt_inicio
                                  ,pesa_dt_fim
                                  ,pesa_dt_criacao)
       values( sq_pesa_cd_pessoa_salario.nextval
               ,r_hr.pess_cd_pessoa
               ,r_hr.moed_cd_moeda
               ,r_hr.valor*r_hr.nr_horas
               ,r_hr.valor
               ,r_hr.dt_inicio
               ,r_hr.dt_fim
               ,sysdate);
   end loop;
   
   for r_ex in c_salario_exterior loop

       insert into pessoa_salario(pesa_cd_pessoa_salario
                                  ,pess_cd_pessoa
                                  ,moed_cd_moeda
                                  ,pesa_vl_salario
                                  ,pesa_vl_hora
                                  ,pesa_dt_inicio
                                  ,pesa_dt_fim
                                  ,pesa_dt_criacao)
       values( sq_pesa_cd_pessoa_salario.nextval
               ,r_ex.pess_cd_pessoa
               ,r_ex.moed_cd_moeda
               ,r_ex.valor
               ,r_ex.valor/r_ex.nr_horas
               ,r_ex.dt_inicio
               ,r_ex.dt_fim
               ,sysdate);
   end loop;

commit;

end usp_pms_carga_salario;