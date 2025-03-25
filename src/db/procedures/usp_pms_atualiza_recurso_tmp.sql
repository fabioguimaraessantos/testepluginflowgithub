create or replace procedure usp_pms_atualiza_recurso_tmp is
   cursor c_recurso_pessoa is
      select f.login,
             f.nm_func,
             f.email,
             f.dt_admissao,
             decode(f.login_mgr, 'razzolini', 'cgimenes', f.login_mgr) login_mgr,
             cb.ciba_cd_cidade_base,
             f.jornada_hrs_dia,
             f.st_ativo,
             f.dt_saida,
             tc.tico_cd_tipo_contrato,
             f.mentor,
             f.ds_papel,
             ca.nm_cargo
      from   vw_adm_rh_funcionarios f,
             cidade_base            cb,
             tipo_contrato          tc,
             vw_adm_rh_cargos       ca
      where  f.cidade_base = cb.ciba_sg_cidade_base
      and    f.cd_cargo = ca.cd_cargo(+)
      and    f.login not in
             ('lmaruyama', 'rodrigues', /*'agroot',*/
              'suzaner', 'jmoreno', 'dmarques', 'v.gmenezes', 'v.pmello')
      and    f.tp_contrato = tc.tico_sg_tipo_contrato;
   v_cd_recurso  number := 0;
   v_pr_alocavel number := 0;
   v_mentor      varchar2(20);
   -- Informacões para LOG
   log_data              date := trunc(sysdate);
   log_dt_carga_processo date;
   nome_carga            varchar2(50) := 'RECURSO';
   bloco_upd             varchar2(50) := '';
   descricao             varchar2(200) := '';
   status                varchar2(30) := '';
   sqlerro               varchar2(200) := '';
   nome_proc             varchar2(200) := 'usp_pms_atualiza_recurso';
   --
   crlf      varchar2(2) := chr(13) || chr(10);
   assunto   varchar2(200) := '';
   mensagem  varchar(2000) := '=== Início da Carga ' ||
                              to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') ||
                              ' ===';
   contador1 integer;
   contador2 integer;
begin
   mensagem := mensagem || crlf ||
               ':: Procedure: pms.usp_pms_atualiza_recurso ::';
   send_mail('lahumada@cit.com.br', '[PMS][RECURSOS] Start Proc', mensagem);
   send_mail('alexandrel@cit.com.br', '[PMS][RECURSOS]Start Proc', mensagem);
   -- info para log
   bloco_upd := 'recurso e pessoa (flag 1 de 1)';
   --
   select count(*)
   into   contador1
   from   pms20.recurso;
   -- Inserir LOG
   descricao             := 'INICIO - Linhas (recurso): ' ||
                            to_char(contador1);
   log_dt_carga_processo := sysdate;
   insert into tb_bi_pms_log_cargas
      (log_data,
       log_dt_carga_processo,
       nome_carga,
       bloco_upd,
       descricao,
       status,
       nome_proc)
   values
      (log_data,
       log_dt_carga_processo,
       nome_carga,
       bloco_upd,
       descricao,
       'OK',
       nome_proc);
   commit;
   --
   for r_pess in c_recurso_pessoa loop
      -- verifica se já foi cadastrado
      select nvl(max(r.recu_cd_recurso), 0)
      into   v_cd_recurso
      from   recurso r
      where  r.recu_cd_mnemonico = r_pess.login;
      -- jornada
      if (r_pess.jornada_hrs_dia = 6) then
         v_pr_alocavel := 0.75;
      else
         v_pr_alocavel := 1;
      end if;
      -- recurso não cadastrado
      begin -- tratamento log
         if (v_cd_recurso = 0) then
            -- Seleciona o código do recurso
            select sq_recu_cd_recurso.nextval
            into   v_cd_recurso
            from   dual;
            begin
               -- insere na tabela de recurso
               insert into recurso
                  (recu_cd_recurso,
                   recu_cd_mnemonico,
                   recu_in_tipo_recurso,
                   recu_in_ativo)
               values
                  (v_cd_recurso, r_pess.login, 'PE', r_pess.st_ativo);
               -- insere na tabela de pessoa
               insert into pessoa
                  (pess_cd_pessoa,
                   recu_cd_recurso,
                   ciba_cd_cidade_base,
                   pess_cd_login,
                   pess_nm_pessoa,
                   pess_tx_email,
                   pess_dt_admissao,
                   pess_cd_login_mgr,
                   pess_dt_mes_validado,
                   pess_pr_alocavel,
                   pess_dt_rescisao,
                   tico_cd_tipo_contrato,
                   pess_cd_login_mentor,
                   pess_tx_papel,
                   pess_tx_cargo)
               values
                  (sq_pess_cd_pessoa.nextval,
                   v_cd_recurso,
                   r_pess.ciba_cd_cidade_base,
                   r_pess.login,
                   r_pess.nm_func,
                   r_pess.email,
                   r_pess.dt_admissao,
                   r_pess.login_mgr,
                   null,
                   v_pr_alocavel,
                   r_pess.dt_saida,
                   r_pess.tico_cd_tipo_contrato,
                   r_pess.mentor,
                   r_pess.ds_papel,
                   r_pess.nm_cargo);
            exception
               when others then
                  status   := 'ERRO'; -- log
                  sqlerro  := sqlerrm; -- log
                  mensagem := mensagem || crlf || crlf ||
                              '[ERRO] ##### PMS.recurso ou pms.pessoa  ##### (rollback) - ' ||
                              sqlerrm || crlf || 'Data Execução: ' ||
                              to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                  assunto  := '[ERRO]';
                  rollback;
            end;
            --  recurso cadastrado
         else
            -- atualiza a tabela recurso
            update recurso r
            set    r.recu_in_ativo = r_pess.st_ativo
            where  r.recu_cd_recurso = v_cd_recurso
            and    r.recu_cd_mnemonico = r_pess.login;
            if (r_pess.login = 'cgimenes' or r_pess.login = 'wrj') then
               v_mentor := 'ostanelli';
            else
               v_mentor := r_pess.mentor;
            end if;
            -- atualiza a tabela pessoa
            update pessoa p
            set    p.pess_cd_login_mgr = r_pess.login_mgr,
                   --   p.pess_pr_alocavel = v_pr_alocavel,    -- quando alguém fizer algum comentario, favor deixar comentado. (katsumi)
                   --  p.ciba_cd_cidade_base = r_pess.ciba_cd_cidade_base,
                   p.pess_dt_rescisao      = r_pess.dt_saida,
                   p.tico_cd_tipo_contrato = r_pess.tico_cd_tipo_contrato,
                   p.pess_cd_login_mentor  = v_mentor,
                   p.pess_tx_papel         = r_pess.ds_papel,
                   p.pess_tx_cargo         = r_pess.nm_cargo,
                   p.ciba_cd_cidade_base   = r_pess.ciba_cd_cidade_base
            where  p.recu_cd_recurso = v_cd_recurso
            and    p.pess_cd_login = r_pess.login;
         end if;
      exception
         --tratamento log
         when others then
            status   := 'ERRO'; -- log
            sqlerro  := sqlerrm; -- log
            mensagem := mensagem || crlf || crlf ||
                        '[ERRO] ##### PMS.recurso  ##### - ' ||
                        sqlerrm || crlf || 'Data Execução: ' ||
                        to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
            assunto  := '[ERRO]';
      end;
   end loop;
   commit;
   if status = 'ERRO' then
      --Inserir LOG
      log_dt_carga_processo := sysdate;
      insert into tb_bi_pms_log_cargas
         (log_data,
          log_dt_carga_processo,
          nome_carga,
          bloco_upd,
          descricao,
          status,
          sqlerro,
          nome_proc)
      values
         (log_data,
          log_dt_carga_processo,
          nome_carga,
          bloco_upd,
          descricao,
          status,
          sqlerro,
          nome_proc);
      commit;
   else
      select count(*)
      into   contador2
      from   pms20.recurso;
      -- Info LOG
      descricao := 'FIM    - Linhas: ' || to_char(contador2);
      status    := 'OK';
      --
      mensagem := mensagem || crlf || crlf ||
                  '[OK] PMS.recurso' || crlf ||
                  'Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                  'Linhas após Refresh: ' || to_char(contador2) || crlf ||
                  'Diferença: ' || to_char(contador2 - contador1) || crlf ||
                  'Data Execução: ' ||
                  to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
      --Inserir LOG
      log_dt_carga_processo := sysdate;
      insert into tb_bi_pms_log_cargas
         (log_data,
          log_dt_carga_processo,
          nome_carga,
          bloco_upd,
          descricao,
          status,
          sqlerro,
          nome_proc)
      values
         (log_data,
          log_dt_carga_processo,
          nome_carga,
          bloco_upd,
          descricao,
          status,
          sqlerro,
          nome_proc);
      commit;
   end if;
   mensagem := mensagem || crlf || crlf || '=== Fim da Carga ' ||
               to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') || ' ===';
   mensagem := mensagem || crlf ||
               'select * from pms.TB_BI_PMS_LOG_CARGAS where NOME_CARGA = ''RECURSO'' order by 2 desc';
   assunto  := assunto || '[PMS][RECURSOS] Resultado da Carga';
   send_mail('lahumada@cit.com.br', assunto, mensagem);
   send_mail('alexandrel@cit.com.br', assunto, mensagem);
end usp_pms_atualiza_recurso_tmp;
