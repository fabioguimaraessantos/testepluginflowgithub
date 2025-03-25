create or replace procedure usp_pms_atualiza_recurso is

cursor c_recurso_pessoa is
   select f.LOGIN, f.NM_FUNC, f.EMAIL, f.dt_admissao, f.LOGIN_MGR, 
          cb.ciba_cd_cidade_base, f.jornada_hrs_dia, f.st_ativo, f.dt_saida
   from vw_adm_rh_funcionarios f, cidade_base cb
   where f.cidade_base = cb.taal_sg_cidade_base
   and not exists (select null from pessoa p
               where p.pess_cd_login = f.LOGIN);

v_cd_recurso number := 0;
v_pr_alocavel number := 0;

begin

for  r_pess in c_recurso_pessoa loop

     -- verifica se já foi cadastrado
     select nvl(max(r.recu_cd_recurso), 0)
     into v_cd_recurso
     from recurso r
     where r.recu_cd_mnemonico = r_pess.login;

     -- jornada
     if (r_pess.jornada_hrs_dia = 6) then
        v_pr_alocavel := 0.75;
     else
        v_pr_alocavel := 1;
     end if;

     -- recurso não cadastrado
     if (v_cd_recurso = 0 ) then
         -- Seleciona o código do recurso
         select sq_recu_cd_recurso.nextval
         into v_cd_recurso
         from dual;

         begin
             -- insere na tabela de recurso
             insert into recurso(recu_cd_recurso, recu_cd_mnemonico, recu_in_tipo_recurso, recu_in_ativo)
             values(v_cd_recurso, r_pess.login, 'PE', r_pess.st_ativo );


             -- insere na tabela de pessoa
             insert into pessoa(pess_cd_pessoa
                                ,recu_cd_recurso
                                ,ciba_cd_cidade_base
                                ,pess_cd_login
                                ,pess_nm_pessoa
                                ,pess_tx_email
                                ,pess_dt_admissao
                                ,pess_cd_login_mgr
                                ,pess_dt_mes_validado
                                ,pess_pr_alocavel
                                ,pess_dt_rescisao)
              values( sq_pess_cd_pessoa.nextval
                     ,v_cd_recurso
                     ,r_pess.ciba_cd_cidade_base
                     ,r_pess.login
                     ,r_pess.nm_func
                     ,r_pess.email
                     ,r_pess.dt_admissao
                     ,r_pess.LOGIN_MGR
                     , null
                     ,v_pr_alocavel
                     ,r_pess.dt_saida
                     );
          exception when others then
             rollback;
          end;

      --  recurso cadastrado
      else
          -- atualiza a tabela recurso
          update recurso r
          set r.recu_in_ativo =  r_pess.st_ativo
          where r.recu_cd_recurso = v_cd_recurso
          and r.recu_cd_mnemonico = r_pess.login;

          -- atualiza a tabela pessoa
          update pessoa p142
          set p.pess_cd_login_mgr = r_pess.login_mgr,
              p.pess_pr_alocavel = v_pr_alocavel,
              p.ciba_cd_cidade_base = r_pess.ciba_cd_cidade_base,
              p.pess_dt_rescisao = r_pess.dt_saida
          where p.recu_cd_recurso = v_cd_recurso
          and p.pess_cd_login = r_pess.login;

      end if;

end loop;
commit;

end usp_pms_atualiza_recurso;
