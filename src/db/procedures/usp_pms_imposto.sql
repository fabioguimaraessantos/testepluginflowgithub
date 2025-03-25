CREATE OR REPLACE PROCEDURE "USP_PMS_IMPOSTO"  as

   log_data              date := trunc(sysdate);
   log_dt_carga_processo date;
   nome_carga            varchar2(50) := '  PMS_IMPOSTO ';
   sqlerro               varchar2(200) := '';
   nome_proc             varchar2(200) := ' USP_PMS_IMPOSTO ';
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
          execute immediate  'truncate table tb_bi_pms_impostos_dre';
     exception when others then
          erro := 1;

          --sqlerro  := sqlerrm;
          mensagem := mensagem || crlf || crlf ||
                              '[ERRO] ##### PMS.TB_BI_PMS_IMPOSTO_DRE  - (LIMPAR A  TABELA)  ##### - ' || crlf ||
                              '       SQL Erro: ' || sqlerrm || crlf ||
                              '       Data Execucao: ' ||
                              to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
     end;

      --- SE HOUVER ERRO PARA LIMPAR A TABELA, NEM GRAVA PRA NÃO PERDER TEMPO.
     if (erro = 0) then
               --- GRAVA NA TABELA
              begin
                   insert into  tb_bi_pms_impostos_dre
                           select * from vw_bi_pms_impostos_dre imp;
                   commit;
               exception when others then
                   erro := 2;

                   --sqlerro  := sqlerrm;
                   mensagem := mensagem || crlf || crlf ||
                                        '[ERRO] #####  PMS.TB_BI_PMS_IMPOSTO_DRE   - (GRAVAR DADOS)  ##### - ' || crlf ||
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

end USP_PMS_IMPOSTO;