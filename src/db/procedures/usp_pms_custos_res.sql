CREATE OR REPLACE PROCEDURE "USP_PMS_CUSTOS_RES"  as

   log_data              date := trunc(sysdate);
   log_dt_carga_processo date;
   nome_carga            varchar2(50) := '  CUSTOS RES ';
   sqlerro               varchar2(200) := '';
   nome_proc             varchar2(200) := ' USP_PMS_CUSTOS_RES ';
   sms_msg               varchar2(200) := 'Objeto: ';

   contador integer := 0;
   erro integer := 0;


   crlf      varchar2(2) := chr(13) || chr(10);
   assunto   varchar2(200) := '';
   mensagem  varchar2(4000) := '=== Inicio da Carga ' ||
                               to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') ||
                               '  ===';
begin


   assunto := ' [PMS][' || nome_carga || ']' || '[' || nome_proc || ']';

   send_mail('lnoboru@ciandt.com', assunto, mensagem);
   send_mail('alexandrel@ciandt.com', assunto, mensagem);
   send_mail('tspadari@ciandt.com', assunto, mensagem);
   send_mail('llino@ciandt.com', assunto, mensagem);
   send_mail('andreiap@ciandt.com', assunto, mensagem);
   



     --- FAZ A LIMPEZA DA TABELA PARA RECEBER NOVOS DADOS
     begin
          execute immediate  'truncate table  TB_bi_pms_custos_res';
     exception when others then
          erro := 1;

          --sqlerro  := sqlerrm;
          mensagem := mensagem || crlf || crlf ||
                              '[ERRO] ##### PMS. tb_bi_pms_custos_res  - (LIMPAR A  TABELA)  ##### - ' || crlf ||
                              '       SQL Erro: ' || sqlerrm || crlf ||
                              '       Data Execucao: ' ||
                              to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
     end;

      --- SE HOUVER ERRO PARA LIMPAR A TABELA, NEM GRAVA PRA NÃO PERDER TEMPO.
     if (erro = 0) then
               --- GRAVA NA TABELA
              begin
                    insert into TB_bi_pms_custos_res
                           select * from VW_bi_pms_custos_res;
                           --select * from vw_bi_pms_custos_res_aux;
                   commit;
               exception when others then
                   erro := 2;

                   --sqlerro  := sqlerrm;
                   mensagem := mensagem || crlf || crlf ||
                                        '[ERRO] ##### PMS. tb_bi_pms_custos_res  - (GRAVAR DADOS)  ##### - ' || crlf ||
                                        '       SQL Erro: ' || sqlerrm || crlf ||
                                        '       Data Execucao: ' ||
                                        to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
               end;
     end if;

   if (erro = 0)  then
        mensagem   := '=== Fim da Carga ' ||
                                    to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') ||
                                    '  ===';
   else
      assunto := 'ERRO '  ||  assunto;
   end if;

   send_mail('lnoboru@ciandt.com', assunto, mensagem);

   send_mail('alexandrel@ciandt.com', assunto, mensagem);
   send_mail('tspadari@ciandt.com', assunto, mensagem);
   send_mail('llino@ciandt.com', assunto, mensagem);
   send_mail('andreiap@ciandt.com', assunto, mensagem);
   
   

end USP_PMS_CUSTOS_RES;