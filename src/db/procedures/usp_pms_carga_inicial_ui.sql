create or replace procedure usp_pms_carga_inicial_ui as

/*cursor c_contratos_mega is
    select cd_mega, nm_contrato
    from vw_mega_contrato_cliente cc;

cursor c_contratos is
   select cont_cd_contrato, cont_nm_contrato
   from contrato;

cursor c_praticas is
   select prat_cd_pratica, prat_sg_pratica
   from pratica;
*/
cursor c_tabelas_alvo is
   select ta.cod_tabela, ta.des_tabela
   from vw_adm_rh_tabelas_alvo ta
   where not exists (select null from cidade_base cb
                     where cb.taal_sg_cidade_base = ta.cod_tabela);

cursor c_recurso_pe is
   select f.LOGIN, f.NM_FUNC, f.EMAIL, f.dt_admissao
   from vw_adm_rh_funcionarios f
   where not exists (select null from recurso r
                     where r.recu_cd_mnemonico = f.LOGIN
                     );

cursor c_pessoa is
   select  r.recu_cd_recurso, f.LOGIN, f.NM_FUNC, f.EMAIL, f.dt_admissao, cb.ciba_cd_cidade_base
   from recurso r, vw_adm_rh_funcionarios f, cidade_base cb
   where r.recu_in_ativo = 'A'
   and r.recu_in_tipo_recurso = 'PE'
   and r.recu_cd_mnemonico = f.LOGIN
   and f.cidade_base = cb.taal_sg_cidade_base;

 /* cursor c_contr_pratica_pv is
     select cp.copr_cd_contrato_pratica from contrato_pratica cp
     where not exists (select null from perfil_vendido pv
                  where cp.copr_cd_contrato_pratica = pv.copr_cd_contrato_pratica);
*/

v_cd_contrato_existente number;
v_existe_pratica number;
v_existe_cid_base number;

begin

 v_cd_contrato_existente := 0;
 -- cadastra contratos do mega
 /*for r_cont in c_contratos_mega loop

    select nvl(max(c.cont_cd_contrato), 0)
    into v_cd_contrato_existente
    from contrato c
    where c.cont_nm_contrato = r_cont.nm_contrato
    and c.cont_cd_reduzido_mega = r_cont.cd_mega;

    if v_cd_contrato_existente < 1 then
       insert into contrato (cont_cd_contrato, cont_nm_contrato, cont_cd_reduzido_mega, clie_cd_cliente)
       values (sq_cont_cd_contrato.nextval,r_cont.nm_contrato, r_cont.cd_mega, null );
    else
       update contrato c
       set c.cont_nm_contrato = r_cont.nm_contrato, c.cont_cd_reduzido_mega = r_cont.cd_mega
       where c.cont_cd_contrato = v_cd_contrato_existente;
    end if;

    v_cd_contrato_existente:=0;

 end loop;


 select count(*)
 into v_existe_pratica
 from pratica p;

 --se existir pratica, insere os contratos-praticas
 if (v_existe_pratica > 0) then
    for r_c in c_contratos loop
       for r_p in c_praticas loop
           insert into contrato_pratica(copr_cd_contrato_pratica, moed_cd_moeda_padrao, cont_cd_contrato, prat_cd_pratica, copr_nm_contrato_pratica, copr_ds_contrato_pratica)
                                 values(sq_copr_cd_contrato_pratica.nextval, 1, r_c.cont_cd_contrato, r_p.prat_cd_pratica,r_c.cont_nm_contrato || '-'|| r_p.prat_sg_pratica, r_c.cont_nm_contrato || '-'|| r_p.prat_sg_pratica);
       end loop;
    end loop;
 end if;
*/
 select count(*)
 into v_existe_cid_base
 from cidade_base;

 if ( v_existe_cid_base = 0) then
    for r_cb in c_tabelas_alvo loop
       insert into cidade_base(ciba_cd_cidade_base, ciba_nm_cidade_base, ciba_in_ativo, taal_sg_cidade_base)
                        values(sq_ciba_cd_cidade_base.nextval, r_cb.des_tabela, 'A',  r_cb.cod_tabela);
    end loop;
 end if;
commit;

    for  r_pess in c_recurso_pe loop
         insert into recurso(recu_cd_recurso, recu_cd_mnemonico, recu_in_tipo_recurso, recu_in_ativo)
         values(sq_recu_cd_recurso.nextval, r_pess.login, 'PE', 'A' );
    end loop;
commit;

   for r_pe in c_pessoa loop
       insert into pessoa(recu_cd_recurso, pess_cd_login, pess_nm_pessoa, pess_tx_email, pess_dt_admissao, pess_cd_pessoa, ciba_cd_cidade_base)
       values( r_pe.recu_cd_recurso, r_pe.login, r_pe.nm_func, r_pe.email, r_pe.dt_admissao, sq_pess_cd_pessoa.nextval ,r_pe.ciba_cd_cidade_base);
   end loop;

  /*
   for r_cppv in c_contr_pratica_pv loop
       insert into perfil_vendido (peve_cd_perfil_vendido, peve_nm_perfil_vendido, peve_vl_padrao, peve_in_ativo, copr_cd_contrato_pratica)
       values(sq_peve_cd_perfil_vendido.nextval, 'Time', 3000, 'A', r_cppv.copr_cd_contrato_pratica);
       insert into perfil_vendido (peve_cd_perfil_vendido, peve_nm_perfil_vendido, peve_vl_padrao, peve_in_ativo, copr_cd_contrato_pratica)
       values(sq_peve_cd_perfil_vendido.nextval, 'Scrum Master',4000, 'A', r_cppv.copr_cd_contrato_pratica);
       insert into perfil_vendido (peve_cd_perfil_vendido, peve_nm_perfil_vendido, peve_vl_padrao, peve_in_ativo, copr_cd_contrato_pratica)
       values(sq_peve_cd_perfil_vendido.nextval, 'Arquiteto' ,5000, 'A', r_cppv.copr_cd_contrato_pratica);
       insert into perfil_vendido (peve_cd_perfil_vendido, peve_nm_perfil_vendido, peve_vl_padrao, peve_in_ativo, copr_cd_contrato_pratica)
       values(sq_peve_cd_perfil_vendido.nextval, 'Gestor', 6000, 'A', r_cppv.copr_cd_contrato_pratica);
   end loop;
   */
commit;

end usp_pms_carga_inicial_ui;