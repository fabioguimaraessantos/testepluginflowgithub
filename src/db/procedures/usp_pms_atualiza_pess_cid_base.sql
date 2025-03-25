create or replace procedure USP_PMS_ATUALIZA_PESS_CID_BASE as
       cursor sel_inform is
              select pe.pess_cd_pessoa,
                     pe.pess_cd_login,
                     pe.pess_dt_admissao,
                     pe.pess_dt_rescisao,
                     pe.ciba_cd_cidade_base
              from pessoa pe,
                   pessoa_cidade_base pcb
              where pe.pess_cd_pessoa = pcb.pess_cd_pessoa (+) and
                    pcb.pess_cd_pessoa is null;
       --
       v_sel sel_inform%rowtype;
       --
      -- v_cont number;
       
       
    log_data              date := trunc(sysdate);
   log_dt_carga_processo date;
   nome_carga            varchar2(50) :=  '(7) PRATICAS';
   bloco_upd             varchar2(50) := 'PMS.PESSOA_CIDADE_BASE';
   descricao             varchar2(200) := '';
   status                varchar2(30) := '';
   sqlerro               varchar2(200) := '';
   nome_proc             varchar2(200) := 'USP_PMS_ATUALIZA_PESS_CID_BASE ';
   crlf                  varchar2(2) := chr(13) || chr(10);
   assunto               varchar2(200) := '';
   mensagem              varchar2(4000) := '=== Inicio da Carga ' ||
                                           to_char(sysdate,
                                                   'DD/MM/YYYY HH24:MI:SS') ||
                                           '  ===';
   contador1             integer;
   contador2             integer;
   sms_msg               varchar2(200) := 'Objeto: ';
          
begin

   mensagem := mensagem || crlf || ':: PROCEDURE: ' || nome_proc || ' ::';
   mensagem := mensagem || crlf || crlf ||  'Teste'; 
   
   send_mail('lnoboru@ciandt.com','[PMS][' || nome_carga || '] Start Proc',mensagem);   
   send_mail('alexandrel@ciandt.com','[PMS][' || nome_carga || '] Start Proc',mensagem);      
         
         --Insert LOG
         descricao := 'INICIO - Linhas: ' || to_char(contador1);
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
     /** TEM SEQUENCE!!!     
      * select max( pcb.pecb_cd_pessoa_cidade_base )
      * into v_cont
      * from pessoa_cidade_base pcb;
      **/
     --
     open sel_inform;
     --
     fetch sel_inform into v_sel;
     --
     while sel_inform%found loop
           --v_cont := v_cont + 1;
           --
           insert into pessoa_cidade_base
           values( sq_pecb_cd_pessoa_cidade_base.nextval,
                   v_sel.pess_cd_pessoa,
                   v_sel.ciba_cd_cidade_base,
                   to_date( '01/' || to_char( v_sel.pess_dt_admissao, 'MM/YYYY' ), 'DD/MM/YYYY' ),
                   null,
                   sysdate );
           --
           commit;
           --
           fetch sel_inform into v_sel;
     end loop;
     --
     close sel_inform;
     
         --Insert LOG
         descricao := 'FIM - Linhas: ' || to_char(contador1);
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
         
    mensagem :=   '===Fim da Carga ' || to_char(sysdate,  'DD/MM/YYYY HH24:MI:SS') || '  === ';
   mensagem := mensagem || crlf || crlf ||  'Teste2'; 
   
   send_mail('lnoboru@ciandt.com','[PMS][' || nome_carga || '] Start Proc',mensagem);   
   send_mail('alexandrel@ciandt.com','[PMS][' || nome_carga || '] Start Proc',mensagem);      
           
end USP_PMS_ATUALIZA_PESS_CID_BASE;