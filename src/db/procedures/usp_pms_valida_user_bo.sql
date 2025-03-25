create or replace procedure USP_PMS_valida_user_bo is
--INTERVALO: trunc(sysdate +1) + 7 + 3/24
--cursor que lista de todos os funcionarios inativos da Ci&T
CURSOR c_func IS
   select login
   from   tb_bi_pms_bo_users f
   where f.status = 'A';
--   where  f.st_ativo = 'I' and substr( f.login, 1, 1 ) = 'w';
 --  stats  integer :=0;
   login_inat varchar2(100):=' ';
   NOME_CARGA            varchar2(50) :='BO_USUARIOS';
   NOME_PROC             varchar2(200):='USP_PMS_valida_user_bo';
   crlf      varchar2(2) := chr(13) || chr(10);
   assunto   varchar2(200) := '';
   mensagem  varchar2(4000) := '=== Início da VALIDACAO '||to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' ) ||'  ===';
begin
  --------------------------------------------------------------------------
   mensagem := mensagem || crlf || ':: PROCEDURE: '||nome_proc||' ::';
   send_mail('lahumada@cit.com.br', '[PMS]['||nome_carga||'] Start Proc', mensagem);
   send_mail('alexandrel@cit.com.br', '[PMS]['||nome_carga||'] Start Proc', mensagem);
   for r_func in c_func loop
     --recuperar na variavel login_inat, o login que está inativo, se encontrar.
     begin
           select nvl(tb.login,'')  --no data foud
           into login_inat
           from adm_rh.chp_funcionarios tb
           where tb.login = r_func.login
           and tb.ST_ATIVO = 'I';
      exception
          when no_data_found then
            login_inat:= ' ';
      end;
     --
     if login_inat <> ' ' then
       mensagem := crlf ||'Usuário foi DESLIGADO! Inativar no Business Object: '|| login_inat;
       assunto := '[LOGIN]';
   --    stats:=1;
             --- Update
             update tb_bi_pms_bo_users
             set status = 'I'
             where login = login_inat;
             commit;
     end if;
     login_inat := ' ';
   end LOOP;
     --se encontrou algum login, então envia e-mail
--     if stats = 1 then
         mensagem := mensagem || crlf || crlf || '=== Fim '||to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' )||' ===';
         ASSUNTO := ASSUNTO || '[PMS]['||NOME_CARGA||'] Check de User no BO inativado';
         send_mail('lahumada@cit.com.br', assunto, mensagem);
         send_mail('alexandrel@cit.com.br', assunto, mensagem);
--    end if;
end USP_PMS_valida_user_bo;