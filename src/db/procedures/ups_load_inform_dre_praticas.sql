create or replace procedure ups_load_inform_dre_praticas( flag number ) as
-----------------------------------------------------------------------
/*
   Informação sobre a carga.
   Ordem do Load:
   6 - Views de Atributos
     6.1 - PMS.VW_BI_PMS_ATRIBUTOS_DRE_MAT
     6.2 - PMS.VW_BI_PMS_ATRIBUTOS_DRE_MAT2
   1 - View de empréstimos de recurso
     1.1 - PMS.VW_BI_PMS_EMPREST_REC_CONS
     1.2 - PMS.VW_BI_PMS_EMPREST_REC
     1.3 - PMS.TB_BI_PMS_PRAT_DRE_MAT
   2 - View de de custos fixos de licença das consultorias
     2.1 - PMS.VW_BI_COSTF_LEAVE_UMKT_MAT
   5 -  Atualização da view de custos fixos das práticas
     5.1 - PMS.VW_BI_PMS_CUSTFIX_PRAT_DRE_MAT
   3 -Atualização das informações de empréstimos de recurso
     3.1 - PMS.TMP_BI_TOTCUS_EMPREC
     3.2 - PMS.tmp_bi_custind_emprec
     3.4 - PMS.tb_bi_pms_custo_emp_rec_dre
   4 -Carga das informações de Custos Indiretos, para a DRE das Praticas
     4.1 - PMS.tb_bi_pms_custos_ind_dre

*/
        cursor sel_inform is
        select s.cd_contrato_pratica,
               s.nome_contrato_pratica,
               m.moed_cd_moeda cd_moeda,
               s.moeda,
               s.dt_mes,
               s.lob_contrato_pratica,
               al.total_fte_aloc valor_total_fte,
               s.valor_custo_dir_tce_curr,
               s.valor_custo_dir_tce_brl,
               s.valor_custo_dir_infra_curr,
               s.valor_custo_dir_infra_brl,
               s.valor_custo_ind_tce_curr,
               s.valor_custo_ind_tce_brl,
               s.valor_custo_ind_infra_curr,
               s.valor_custo_ind_infra_brl,
               s.valor_hora_extra,
               s.valor_plantao,
               ( s.valor_custo_dir_tce_curr + s.valor_custo_dir_infra_curr + s.valor_custo_ind_tce_curr + s.valor_custo_ind_infra_curr +
                                         s.valor_hora_extra + s.valor_plantao ) valor_total_custo_curr,
               ( s.valor_custo_dir_tce_brl + s.valor_custo_dir_infra_brl + s.valor_custo_ind_tce_brl + s.valor_custo_ind_infra_brl +
                                         s.valor_hora_extra + s.valor_plantao ) valor_total_custo_brl
        from ( select s.flag,
                      s.cd_contrato_pratica,
                      s.nome_contrato_pratica,
                      case when s.moeda is null then 'BRL' else s.moeda end moeda,
                      s.dt_mes,
                      s.login,
                      s.lob_contrato_pratica,
                      s.valor_custo_dir_tce_curr,
                      s.valor_custo_dir_tce_brl,
                      s.valor_custo_dir_infra_curr,
                      s.valor_custo_dir_infra_brl,
                      s.valor_custo_ind_tce_brl,
                      s.valor_custo_ind_tce_curr,
                      s.valor_custo_ind_infra_brl,
                      s.valor_custo_ind_infra_curr,
                      s.valor_hora_extra,
                      s.valor_plantao
               from ( select flag,
                             cd_contrato_pratica,
                             nome_contrato_pratica,
                             moeda,
                             dt_mes,
                             login,
                             lob_contrato_pratica,
                             sum( s.valor_custo_dir_tce_curr )   over( partition by s.cd_contrato_pratica, s.dt_mes ) valor_custo_dir_tce_curr,
                             sum( s.valor_custo_dir_tce_brl )    over( partition by s.cd_contrato_pratica, s.dt_mes ) valor_custo_dir_tce_brl,
                             sum( s.valor_custo_dir_infra_curr ) over( partition by s.cd_contrato_pratica, s.dt_mes ) valor_custo_dir_infra_curr,
                             sum( s.valor_custo_dir_infra_brl )  over( partition by s.cd_contrato_pratica, s.dt_mes ) valor_custo_dir_infra_brl,
                             sum( s.valor_custo_ind_tce_brl )    over( partition by s.cd_contrato_pratica, s.dt_mes ) valor_custo_ind_tce_brl,
                             sum( s.valor_custo_ind_tce_curr )   over( partition by s.cd_contrato_pratica, s.dt_mes ) valor_custo_ind_tce_curr,
                             sum( s.valor_custo_ind_infra_brl )  over( partition by s.cd_contrato_pratica, s.dt_mes ) valor_custo_ind_infra_brl,
                             sum( s.valor_custo_ind_infra_curr ) over( partition by s.cd_contrato_pratica, s.dt_mes ) valor_custo_ind_infra_curr,
                             sum( s.valor_hora_extra )           over( partition by s.cd_contrato_pratica, s.dt_mes ) valor_hora_extra,
                             sum( s.valor_plantao )              over( partition by s.cd_contrato_pratica, s.dt_mes ) valor_plantao,
                             row_number() over( partition by s.cd_contrato_pratica, s.dt_mes
                                                order by     s.cd_contrato_pratica, s.dt_mes ) nlinha
                      from ( select s.flag,
                                    s.login,
                                    s.cd_contrato_pratica,
                                    s.nome_contrato_pratica,
                                    case when s.moeda is null then 'BRL' else s.moeda end moeda,
                                    s.dt_mes,
                                    s.lob lob_contrato_pratica,
                                    s.valor_custo_dir_tce_curr,
                                    s.valor_custo_dir_tce_brl,
                                    s.valor_custo_dir_infra_curr,
                                    s.valor_custo_dir_infra_brl,
                                    s.valor_custo_ind_tce_brl,
                                    s.valor_custo_ind_tce_curr,
                                    s.valor_custo_ind_infra_brl,
                                    s.valor_custo_ind_infra_curr,
                                    s.valor_hora_extra,
                                    s.valor_plantao
                             from vw_bi_pms_dre_servicos s ) s ) s
              where s.nlinha = 1 ) s,
             moeda m,
           ( select cd_contrato_pratica,
                   nome_contrato_pratica,
                   dt_mes,
                   lob_contrato_pratica,
                   total_fte_aloc
            from ( select ma.copr_cd_contrato_pratica cd_contrato_pratica,
                          cp.copr_nm_contrato_pratica nome_contrato_pratica,
                          ap.alpe_dt_alocacao_periodo dt_mes,
                          pr.prat_sg_pratica lob_contrato_pratica,
                          sum( ap.alpe_pr_alocacao_periodo ) over( partition by ma.copr_cd_contrato_pratica, ap.alpe_dt_alocacao_periodo ) total_fte_aloc,
                          row_number() over( partition by ma.copr_cd_contrato_pratica, ap.alpe_dt_alocacao_periodo
                                             order by ma.copr_cd_contrato_pratica, ap.alpe_dt_alocacao_periodo ) nlinha
                   from mapa_alocacao ma,
                        contrato_pratica cp,
                        alocacao al,
                        alocacao_periodo ap,
                        pratica pr
                   where ma.maal_cd_mapa_alocacao    = al.maal_cd_mapa_alocacao and
                         ma.copr_cd_contrato_pratica = cp.copr_cd_contrato_pratica and
                         al.aloc_cd_alocacao         = ap.aloc_cd_alocacao and
                         cp.prat_cd_pratica          = pr.prat_cd_pratica and
                         ma.maal_in_versao           = 'PB' )
            where nlinha = 1 ) al
     where s.cd_contrato_pratica = al.cd_contrato_pratica and
           s.dt_mes = al.dt_mes and
           s.moeda = m.moed_sg_moeda;
     --
     v_sel_inform sel_inform%rowtype;
     --
     cursor sel_inform2 is
     select cd_pessoa,
            cd_contrato_pratica,
            dt_mes,
            cd_moeda,
            moeda,
            login,
            valor_total_fte_ind,
            valor_total_custo_curr,
            valor_total_custo_brl,
            cd_grupo_custo,
            cd_periodo,
            lob_pessoa
     from (
     select inf.cd_pessoa,
               inf.cd_contrato_pratica,
               inf.dt_mes,
               inf.cd_moeda,
               inf.moeda,
               inf.login,
               inf.valor_total_fte_ind,
               inf.valor_total_custo_curr,
               inf.valor_total_custo_brl,
               inf.cd_grupo_custo,
               inf.cd_periodo,
               cl.celu_nm_centro_lucro lob_pessoa,
               row_number() over( partition by inf.cd_contrato_pratica, inf.dt_mes, inf.login
                                  order by inf.cd_contrato_pratica, inf.dt_mes, inf.login ) nlinha
        from grupo_custo_centro_lucro gccl,
             centro_lucro cl,
             ( select inf.cd_pessoa,
                      inf.cd_contrato_pratica,
                      inf.nome_contrato_pratica,
                      inf.dt_mes,
                      inf.cd_moeda,
                      inf.moeda,
                      inf.login,
                      inf.valor_total_fte_ind,
                      inf.valor_total_custo_curr,
                      inf.valor_total_custo_brl,
                      inf.cd_grupo_custo,
                      gcp.cd_periodo
               from grupo_custo gc,
                    ( select gcp.grcu_cd_grupo_custo cd_grupo_custo,
                             gcp.grcp_dt_inicio dt_inicio,
                             gcp.grcp_cd_gc_periodo cd_periodo,
                             case when gcp.grcp_dt_fim is not null
                                       then gcp.grcp_dt_fim
                                       else ( select p2.para_dt_parametro
                                              from parametro p2
                                              where p2.para_cd_parametro = 1 ) end dt_fim
                      from grupo_custo_periodo gcp ) gcp,
                    ( select inf.cd_pessoa,
                             inf.cd_contrato_pratica,
                             inf.nome_contrato_pratica,
                             inf.dt_mes,
                             inf.cd_moeda,
                             inf.moeda,
                             inf.login,
                             inf.valor_total_fte_ind,
                             inf.valor_total_custo_curr,
                             inf.valor_total_custo_brl,
                             pgc.cd_grupo_custo
                      from ( select pgc.pess_cd_pessoa cd_pessoa,
                                    pgc.grcu_cd_grupo_custo cd_grupo_custo,
                                    pgc.pegc_in_status,
                                    pgc.pegc_dt_inicio dt_inicio,
                                    case when pgc.pegc_dt_fim is not null
                                              then pgc.pegc_dt_fim
                                              else ( select p1.para_dt_parametro
                                                     from parametro p1
                                                     where p1.para_cd_parametro = 1 ) end dt_fim
                             from pessoa_grupo_custo pgc ) pgc,
                           ( select p1.pess_cd_pessoa cd_pessoa,
                                    inf.cd_contrato_pratica,
                                    inf.nome_contrato_pratica,
                                    inf.dt_mes,
                                    inf.cd_moeda,
                                    inf.moeda,
                                    inf.login,
                                    inf.valor_total_fte_ind,
                                    inf.valor_total_custo_curr,
                                    inf.valor_total_custo_brl
                              from pessoa p1,
                                   ( select cd_contrato_pratica,
                                            nome_contrato_pratica,
                                            dt_mes,
                                            login,
                                            cd_moeda,
                                            moeda,
                                            flag,
                                            valor_fte valor_total_fte_ind,
                                            valor_custo_dir_tce_curr,
                                            valor_custo_dir_tce_brl,
                                            valor_custo_dir_infra_curr,
                                            valor_custo_dir_infra_brl,
                                            valor_custo_ind_tce_curr,
                                            valor_custo_ind_tce_brl,
                                            valor_custo_ind_infra_curr,
                                            valor_custo_ind_infra_brl,
                                            valor_hora_extra,
                                            valor_plantao,
                                            ( valor_custo_dir_tce_curr + valor_custo_dir_infra_curr + valor_custo_ind_tce_curr + valor_custo_ind_infra_curr +
                                                                      valor_hora_extra + valor_plantao ) valor_total_custo_curr,
                                            ( valor_custo_dir_tce_brl + valor_custo_dir_infra_brl + valor_custo_ind_tce_brl + valor_custo_ind_infra_brl +
                                                                      valor_hora_extra + valor_plantao ) valor_total_custo_brl
                                     from ( select s.cd_contrato_pratica,
                                                   al.nome_contrato_pratica,
                                                   s.dt_mes,
                                                   s.login,
                                                   al.valor_fte,
                                                   s.flag,
                                                   m.moed_cd_moeda cd_moeda,
                                                   case when s.moeda is null then 'BRL' else s.moeda end moeda,
                                                   sum( s.valor_custo_dir_tce_curr )   over( partition by s.cd_contrato_pratica, s.dt_mes, s.login ) valor_custo_dir_tce_curr,
                                                   sum( s.valor_custo_dir_tce_brl )    over( partition by s.cd_contrato_pratica, s.dt_mes, s.login ) valor_custo_dir_tce_brl,
                                                   sum( s.valor_custo_dir_infra_curr ) over( partition by s.cd_contrato_pratica, s.dt_mes, s.login ) valor_custo_dir_infra_curr,
                                                   sum( s.valor_custo_dir_infra_brl )  over( partition by s.cd_contrato_pratica, s.dt_mes, s.login ) valor_custo_dir_infra_brl,
                                                   sum( s.valor_custo_ind_tce_curr )   over( partition by s.cd_contrato_pratica, s.dt_mes, s.login ) valor_custo_ind_tce_curr,
                                                   sum( s.valor_custo_ind_tce_brl )    over( partition by s.cd_contrato_pratica, s.dt_mes, s.login ) valor_custo_ind_tce_brl,
                                                   sum( s.valor_custo_ind_infra_curr ) over( partition by s.cd_contrato_pratica, s.dt_mes, s.login ) valor_custo_ind_infra_curr,
                                                   sum( s.valor_custo_ind_infra_brl )  over( partition by s.cd_contrato_pratica, s.dt_mes, s.login ) valor_custo_ind_infra_brl,
                                                   sum( s.valor_hora_extra )           over( partition by s.cd_contrato_pratica, s.dt_mes, s.login ) valor_hora_extra,
                                                   sum( s.valor_plantao )              over( partition by s.cd_contrato_pratica, s.dt_mes, s.login ) valor_plantao
                                            from vw_bi_pms_dre_servicos s,
                                                 moeda m,
                                                ( select cd_contrato_pratica,
                                                         nome_contrato_pratica,
                                                         dt_mes,
                                                         login,
                                                         valor_fte,
                                                         total_fte_aloc
                                                  from ( select ma.copr_cd_contrato_pratica cd_contrato_pratica,
                                                                cp.copr_nm_contrato_pratica nome_contrato_pratica,
                                                                ap.alpe_dt_alocacao_periodo dt_mes,
                                                                ap.alpe_pr_alocacao_periodo valor_fte,
                                                                pe.pess_cd_login login,
                                                                sum( ap.alpe_pr_alocacao_periodo ) over( partition by ma.copr_cd_contrato_pratica, ap.alpe_dt_alocacao_periodo, al.recu_cd_recurso ) total_fte_aloc,
                                                                row_number() over( partition by ma.copr_cd_contrato_pratica, ap.alpe_dt_alocacao_periodo, al.recu_cd_recurso
                                                                                   order by ma.copr_cd_contrato_pratica, ap.alpe_dt_alocacao_periodo, al.recu_cd_recurso ) nlinha
                                                         from mapa_alocacao ma,
                                                              contrato_pratica cp,
                                                              alocacao al,
                                                              pessoa pe,
                                                              alocacao_periodo ap
                                                         where ma.maal_cd_mapa_alocacao = al.maal_cd_mapa_alocacao and
                                                               ma.copr_cd_contrato_pratica = cp.copr_cd_contrato_pratica and
                                                               al.aloc_cd_alocacao      = ap.aloc_cd_alocacao and
                                                               al.recu_cd_recurso       = pe.recu_cd_recurso and
                                                               ma.maal_in_versao        = 'PB' )
                                                  where nlinha = 1 ) al
                                           where case when s.moeda is null then 'BRL' else s.moeda end = m.moed_sg_moeda and
                                                 s.cd_contrato_pratica = al.cd_contrato_pratica and
                                                 s.dt_mes = al.dt_mes and
                                                 s.login = al.login   )
                                    where valor_fte > 0 ) inf
                              where p1.pess_cd_login = inf.login ) inf
                      where inf.cd_pessoa = pgc.cd_pessoa (+) and
                            inf.dt_mes between pgc.dt_inicio (+) and pgc.dt_fim (+) ) inf
               where inf.cd_grupo_custo     = gc.grcu_cd_grupo_custo and
                     gc.grcu_cd_grupo_custo = gcp.cd_grupo_custo and
                     inf.dt_mes between gcp.dt_inicio (+) and gcp.dt_fim (+) ) inf
        where inf.cd_periodo = gccl.grcp_cd_gc_periodo and
              gccl.celu_cd_centro_lucro = cl.celu_cd_centro_lucro and
              cl.nacl_cd_natureza = 61 ) where nlinha = 1;
   --
   v_sel_inform2 sel_inform2%rowtype;
   --
   cursor sel_inform_cind is
        select fonte,
               sub_fonte,
               cd_pessoa,
               categoria,
               login,
               dt_mes,
               dt_admissao,
               dt_rescisao,
               cd_moeda,
               moeda,
               valor_cotacao,
               cd_contrato_pratica,
               nome_contrato_pratica,
               pratica,
               perc_alocavel,
               perc_alocacao,
               valor_tce,
               valor_tce_brl,
               vlr_custo_infra_base,
               vlr_custo_infra_base_brl,
               pct_ferias,
               pct_licenca,
               pct_distrib,
               valor_fte_cind,
               valor_custo_ind_tce_curr,
               valor_custo_ind_tce_brl,
               valor_custo_ind_infra_curr,
               valor_custo_ind_infra_brl,
               umkt,
              VALOR_CUSTO_DIR_CHBK_CURR,
              VALOR_CUSTO_DIR_CHBK_BRL,
             VALOR_CUSTO_DIR_CHBK_USD                 
        from vw_bi_pms_custos_ind_dre;
   --
   v_sel_inf_cind sel_inform_cind%rowtype;
   --
   cursor sel_inf_pratica_dre is
          select cd_contrato_pratica,
                 nome_contrato_pratica,
                 lob_contrato_pratica,
                 dt_mes,
                 nvl( cd_moeda, 1 ) cd_moeda,
                 nvl( moeda, 'BRL' ) moeda,
                 valor_cotacao,
                 grupo_custo,
                 login,
                 qtd_fte,
                 valor_receita_curr,
                 valor_total_fte,
                 vlr_preco_medio_fte,
                 valor_preco_medio_fte_brl,
                 vlr_receita_ajustada_fte,
                 vlr_receita_ajust_fte_brl,
                 vlr_receita_clob,
                 vlr_rec_emprestimo_lob_brl,
                 pratica
          from vw_bi_pms_pratica_dre/*_mat*/;
  --
  v_sel_inf_prat_dre sel_inf_pratica_dre%rowtype;

    -- Informacões para LOG
   LOG_DATA              date := trunc(sysdate);
   LOG_DT_CARGA_PROCESSO date;
   NOME_CARGA            varchar2(50) :='PRATICAS';
   BLOCO_UPD             varchar2(50) :='';
   DESCRICAO             varchar2(200) :='';
   STATUS                varchar2(30) :='';
   SQLERRO               varchar2(200) :='';
   NOME_PROC             varchar2(200):='UPS_LOAD_INFORM_DRE_PRATICAS';
   crlf      varchar2(2) := chr(13) || chr(10);
   assunto   varchar2(200) := '';
   mensagem  varchar(4000) := '=== Início da Carga '||to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' ) ||'  ===';
   contador1 integer;
   contador2 integer;

BEGIN
  --------------------------------------------------------------------------
  -- Atualização de carga das views de atributos
  --------------------------------------------------------------------------
  mensagem := mensagem || crlf || crlf || ':: '||NOME_PROC||' ::';
  SEND_MAIL('lahumada@cit.com.br', '[PMS][PRATICAS] Start Proc' , MENSAGEM);
  SEND_MAIL('alexandrel@cit.com.br', '[PMS][PRATICAS] Start Proc' , MENSAGEM);

--Tipo da Carga: 6 - Views de Atributos
--   6.1 - PMS.VW_BI_PMS_ATRIBUTOS_DRE_MAT
--   6.2 - PMS.VW_BI_PMS_ATRIBUTOS_DRE_MAT2
  IF (FLAG = 6 OR FLAG = 99) then
    MENSAGEM := MENSAGEM || CRLF || CRLF ||'Tipo da Carga: 6 - Views de Atributos';

--  6.1 - PMS.VW_BI_PMS_ATRIBUTOS_DRE_MAT
    begin
      BLOCO_UPD := '6 - PMS.VW_BI_PMS_ATRIBUTOS_DRE_MAT (flag 1 de 2)';
      LOG_DT_CARGA_PROCESSO := sysdate;
      SELECT COUNT(*) INTO CONTADOR1 FROM PMS.VW_BI_PMS_ATRIBUTOS_DRE_MAT;
         --Insert LOG
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;
      --REFRESH
      DBMS_MVIEW.REFRESH('"PMS"."VW_BI_PMS_ATRIBUTOS_DRE_MAT"');
      --
      SELECT COUNT(*) INTO CONTADOR2 FROM PMS.VW_BI_PMS_ATRIBUTOS_DRE_MAT;
      --LOG
      DESCRICAO:= 'FIM    - Linhas: '||to_char(contador2);
      -- LOG: As linhas carregadas foram menores que a original e este valor é maior ou igual a 1%? ERRO!
         if (contador2 < contador1) and ((contador2/contador1)< 0.01) then
               status   := 'ERRO';
               assunto  := '[ERRO%]';
               sqlerro := 'Variacao pocentagem: ' ||round(((contador2/contador1)*100),2) ||'%';
               mensagem := mensagem || crlf || crlf ||
                          '[ERRO] ##### PMS.VW_BI_PMS_ATRIBUTOS_DRE_MAT  ##### - ' || crlf ||
                          '       Variacao pocentagem: ' ||round(((contador2/contador1)*100),2) ||'%' || crlf ||
                          '       Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                          '       Linhas após Refresh: ' || to_char(contador2) || crlf ||
                          '       Diferença: ' || to_char(contador2 - contador1) || crlf ||
--                          'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                          '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
         else
             STATUS:='OK';
             mensagem := mensagem || crlf || crlf ||
                     '[OK] PMS.VW_BI_PMS_ATRIBUTOS_DRE_MAT' || crlf ||
                     '     Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                     '     Linhas após Refresh: ' || to_char(contador2) || crlf ||
                     '     Diferença: ' || to_char(contador2 - contador1) || crlf ||
                     '     Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                     '     Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
         end if;
    EXCEPTION
      WHEN OTHERS then
            STATUS:='ERRO';
            assunto  := '[ERRO]';
            SQLERRO:= sqlerrm;
            mensagem := mensagem || crlf || crlf ||
                        '[ERRO] ##### PMS.VW_BI_PMS_ATRIBUTOS_DRE_MAT  ##### - ' || crlf ||
                        '       SQL Erro: ' || sqlerrm || crlf ||
                        '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
    END;
     --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit;
--  LOG - zerar info
    SQLERRO:='';
    CONTADOR1 :=0;
    CONTADOR2 :=0;

-- 6.2 - PMS.VW_BI_PMS_ATRIB_DRE_MAT2
    begin
      MENSAGEM := MENSAGEM || CRLF || '6.2 - PMS.VW_BI_PMS_ATRIB_DRE_MAT2';
      BLOCO_UPD := '6 - PMS.VW_BI_PMS_ATRIB_DRE_MAT2 (flag 2 de 2)';
      LOG_DT_CARGA_PROCESSO := sysdate;
      select count(*)
      into   contador1
      from   pms.vw_bi_pms_atrib_dre_mat2;
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;
              --REFRESH
              DBMS_MVIEW.REFRESH('"PMS"."VW_BI_PMS_ATRIB_DRE_MAT2"');
              --
      SELECT COUNT(*) INTO CONTADOR2 FROM PMS.VW_BI_PMS_ATRIB_DRE_MAT2;
      --LOG
      DESCRICAO:= 'FIM    - Linhas: '||to_char(contador2);
      -- LOG: As linhas carregadas foram menores que a original e este valor é maior ou igual a 1%? ERRO!
         if (contador2 < contador1) and ((contador2/contador1)< 0.01) then
               status   := 'ERRO';
               assunto  := '[ERRO%]';
               sqlerro := 'Variacao pocentagem: ' ||round(((contador2/contador1)*100),2) ||'%';
               mensagem := mensagem || crlf || crlf ||
                          '[ERRO] ##### PMS.VW_BI_PMS_ATRIBUTOS_DRE_MAT  ##### - ' || crlf ||
                          '       Variacao pocentagem: ' ||round(((contador2/contador1)*100),2) ||'%' || crlf ||
                          '       Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                          '       Linhas após Refresh: ' || to_char(contador2) || crlf ||
                          '       Diferença: ' || to_char(contador2 - contador1) || crlf ||
--                          'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                          '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
         else
             STATUS:='OK';
             mensagem := mensagem || crlf || crlf ||
                     '[OK] PMS.VW_BI_PMS_ATRIBUTOS_DRE_MAT' || crlf ||
                     '     Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                     '     Linhas após Refresh: ' || to_char(contador2) || crlf ||
                     '     Diferença: ' || to_char(contador2 - contador1) || crlf ||
                     '     Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                     '     Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
         end if;
    exception
     WHEN OTHERS then
            STATUS:='ERRO';
            assunto  := '[ERRO]';
            SQLERRO:= sqlerrm;
            mensagem := mensagem || crlf || crlf ||
                        '[ERRO] ##### PMS.PMS.VW_BI_PMS_ATRIBUTOS_DRE_MAT2  ##### - ' || crlf ||
                        '       SQL Erro: ' || sqlerrm || crlf ||
                        '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
    END;

    --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit;

  END IF;
--  LOG - zerar info
    SQLERRO:='';
    CONTADOR1 :=0;
    CONTADOR2 :=0;
  --------------------------------------------------------
  -- Atualização da view de empréstimos de recurso
  --------------------------------------------------------
--   1 - View de empréstimos de recurso
--     1.1 - PMS.VW_BI_PMS_EMPREST_REC_CONS
--     1.2 - PMS.VW_BI_PMS_EMPREST_REC
--     1.3 - PMS.TB_BI_PMS_PRAT_DRE_MAT
  IF (FLAG = 1 OR FLAG = 99) then
      MENSAGEM := MENSAGEM || CRLF || CRLF ||'Tipo da Carga: 1 - View de empréstimos de recurso';

--     1.1 - PMS.VW_BI_PMS_EMPREST_REC_CONS
     begin
       BLOCO_UPD := '1 - PMS.VW_BI_PMS_EMPREST_REC_CONS (flag 1 de 3)';
       LOG_DT_CARGA_PROCESSO := sysdate;
       SELECT COUNT(*)
       INTO CONTADOR1
       FROM PMS.TB_BI_PMS_PRAT_DRE_MAT;
        --Insert LOG
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;
       --
       EXECUTE IMMEDIATE 'truncate table tb_bi_pms_prat_dre_mat';
       --
       OPEN SEL_INF_PRATICA_DRE;
       --
       FETCH SEL_INF_PRATICA_DRE INTO V_SEL_INF_PRAT_DRE;
       --
       WHILE SEL_INF_PRATICA_DRE%FOUND LOOP
             INSERT INTO TB_BI_PMS_PRAT_DRE_MAT
             VALUES ( V_SEL_INF_PRAT_DRE.CD_CONTRATO_PRATICA,
                      V_SEL_INF_PRAT_DRE.NOME_CONTRATO_PRATICA,
                      V_SEL_INF_PRAT_DRE.LOB_CONTRATO_PRATICA,
                      V_SEL_INF_PRAT_DRE.DT_MES,
                      V_SEL_INF_PRAT_DRE.CD_MOEDA,
                      V_SEL_INF_PRAT_DRE.MOEDA,
                      V_SEL_INF_PRAT_DRE.VALOR_COTACAO,
                      V_SEL_INF_PRAT_DRE.GRUPO_CUSTO,
                      V_SEL_INF_PRAT_DRE.LOGIN,
                      V_SEL_INF_PRAT_DRE.QTD_FTE,
                      V_SEL_INF_PRAT_DRE.VALOR_RECEITA_CURR,
                      V_SEL_INF_PRAT_DRE.VALOR_TOTAL_FTE,
                      V_SEL_INF_PRAT_DRE.VLR_PRECO_MEDIO_FTE,
                      V_SEL_INF_PRAT_DRE.VALOR_PRECO_MEDIO_FTE_BRL,
                      V_SEL_INF_PRAT_DRE.VLR_RECEITA_AJUSTADA_FTE,
                      V_SEL_INF_PRAT_DRE.VLR_RECEITA_AJUST_FTE_BRL,
                      V_SEL_INF_PRAT_DRE.VLR_RECEITA_CLOB,
                      V_SEL_INF_PRAT_DRE.VLR_REC_EMPRESTIMO_LOB_BRL,
                      V_SEL_INF_PRAT_DRE.PRATICA,
                      SYSDATE);
             --
             COMMIT;
             --
             FETCH SEL_INF_PRATICA_DRE INTO V_SEL_INF_PRAT_DRE;
       END LOOP;
       --
       CLOSE SEL_INF_PRATICA_DRE;
       --
       SELECT COUNT(*)
       INTO CONTADOR2
       FROM PMS.TB_BI_PMS_PRAT_DRE_MAT;

        DESCRICAO:= 'FIM    - Linhas: '||to_char(contador2);
      -- LOG: As linhas carregadas foram menores que a original e este valor é maior ou igual a 1%? ERRO!
         if (contador2 < contador1) and ((contador2/contador1)< 0.01) then
               status   := 'ERRO';
               assunto  := '[ERRO%]';
               sqlerro := 'Variacao pocentagem: ' ||round(((contador2/contador1)*100),2) ||'%';
               mensagem := mensagem || crlf || crlf ||
                          '[ERRO] ##### PMS.TB_BI_PMS_PRAT_DRE_MAT  ##### - ' || crlf ||
                          '       Variacao pocentagem: ' ||round(((contador2/contador1)*100),2) ||'%' || crlf ||
                          '       Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                          '       Linhas após Refresh: ' || to_char(contador2) || crlf ||
                          '       Diferença: ' || to_char(contador2 - contador1) || crlf ||
--                          'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                          '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
         else
             STATUS:='OK';
             mensagem := mensagem || crlf || crlf ||
                     '[OK] PMS.TB_BI_PMS_PRAT_DRE_MAT' || crlf ||
                     '     Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                     '     Linhas após Refresh: ' || to_char(contador2) || crlf ||
                     '     Diferença: ' || to_char(contador2 - contador1) || crlf ||
                     '     Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                     '     Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
         end if;
    EXCEPTION
      WHEN OTHERS then
            STATUS:='ERRO';
            assunto  := '[ERRO]';
            SQLERRO:= sqlerrm;
            mensagem := mensagem || crlf || crlf ||
                        '[ERRO] ##### PMS.TB_BI_PMS_PRAT_DRE_MAT  ##### - ' || crlf ||
                        '       SQL Erro: ' || sqlerrm || crlf ||
                        '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
    END;
     --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit;
--  LOG - zerar info
    SQLERRO:='';
    CONTADOR1 :=0;
    CONTADOR2 :=0;

--     1.2 - PMS.VW_BI_PMS_EMPREST_REC
     begin
        BLOCO_UPD := '1 - PMS.VW_BI_PMS_EMPREST_REC_CONS (flag 2 de 3)';
        LOG_DT_CARGA_PROCESSO := sysdate;
       SELECT COUNT(*)
       INTO CONTADOR1
       FROM PMS.VW_BI_PMS_EMPREST_REC;
       --Insert LOG
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;

                -- REFRESH
                DBMS_MVIEW.REFRESH('"PMS"."VW_BI_PMS_EMPREST_REC"');
                --
       SELECT COUNT(*)
       INTO CONTADOR2
       FROM PMS.VW_BI_PMS_EMPREST_REC;

         DESCRICAO:= 'FIM    - Linhas: '||to_char(contador2);
      -- LOG: As linhas carregadas foram menores que a original e este valor é maior ou igual a 1%? ERRO!
         if (contador2 < contador1) and ((contador2/contador1)< 0.01) then
               status   := 'ERRO';
               assunto  := '[ERRO%]';
               sqlerro := 'Variacao pocentagem: ' ||round(((contador2/contador1)*100),2) ||'%';
               mensagem := mensagem || crlf || crlf ||
                          '[ERRO] ##### PMS.VW_BI_PMS_EMPREST_REC  ##### - ' || crlf ||
                          '       Variacao pocentagem: ' ||round(((contador2/contador1)*100),2) ||'%' || crlf ||
                          '       Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                          '       Linhas após Refresh: ' || to_char(contador2) || crlf ||
                          '       Diferença: ' || to_char(contador2 - contador1) || crlf ||
--                          'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                          '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
         else
             STATUS:='OK';
             mensagem := mensagem || crlf || crlf ||
                     '[OK] PMS.VW_BI_PMS_EMPREST_REC' || crlf ||
                     '     Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                     '     Linhas após Refresh: ' || to_char(contador2) || crlf ||
                     '     Diferença: ' || to_char(contador2 - contador1) || crlf ||
                     '     Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                     '     Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
         end if;
    EXCEPTION
      WHEN OTHERS then
            STATUS:='ERRO';
            assunto  := '[ERRO]';
            SQLERRO:= sqlerrm;
            mensagem := mensagem || crlf || crlf ||
                        '[ERRO] ##### PMS.VW_BI_PMS_EMPREST_REC  ##### - ' || crlf ||
                        '       SQL Erro: ' || sqlerrm || crlf ||
                        '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
    END;
     --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit;
--  LOG - zerar info
    SQLERRO:='';
    CONTADOR1 :=0;
    CONTADOR2 :=0;

--     1.3 - PMS.TB_BI_PMS_PRAT_DRE_MAT
     begin
       BLOCO_UPD := '1 - PMS.VW_BI_PMS_EMPREST_REC_CONS (flag 3 de 3)';
       LOG_DT_CARGA_PROCESSO := sysdate;
       SELECT COUNT(*)
       INTO CONTADOR1
       FROM PMS.VW_BI_PMS_EMPREST_REC_CONS;
        --Insert LOG
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;
                   -- REFRESH
                   DBMS_MVIEW.REFRESH('"PMS"."VW_BI_PMS_EMPREST_REC_CONS"');
                   --
       SELECT COUNT(*)
       INTO CONTADOR2
       FROM PMS.VW_BI_PMS_EMPREST_REC_CONS;

                DESCRICAO:= 'FIM    - Linhas: '||to_char(contador2);
      -- LOG: As linhas carregadas foram menores que a original e este valor é maior ou igual a 1%? ERRO!
         if (contador2 < contador1) and ((contador2/contador1)< 0.01) then
               status   := 'ERRO';
               assunto  := '[ERRO%]';
               sqlerro := 'Variacao pocentagem: ' ||round(((contador2/contador1)*100),2) ||'%';
               mensagem := mensagem || crlf || crlf ||
                          '[ERRO] ##### PMS.VW_BI_PMS_EMPREST_REC_CONS  ##### - ' || crlf ||
                          '       Variacao pocentagem: ' ||round(((contador2/contador1)*100),2) ||'%' || crlf ||
                          '       Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                          '       Linhas após Refresh: ' || to_char(contador2) || crlf ||
                          '       Diferença: ' || to_char(contador2 - contador1) || crlf ||
--                          'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                          '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
         else
             STATUS:='OK';
             mensagem := mensagem || crlf || crlf ||
                     '[OK] PMS.VW_BI_PMS_EMPREST_REC_CONS' || crlf ||
                     '     Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                     '     Linhas após Refresh: ' || to_char(contador2) || crlf ||
                     '     Diferença: ' || to_char(contador2 - contador1) || crlf ||
                     '     Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                     '     Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
         end if;
    EXCEPTION
      WHEN OTHERS then
            STATUS:='ERRO';
            assunto  := '[ERRO]';
            SQLERRO:= sqlerrm;
            mensagem := mensagem || crlf || crlf ||
                        '[ERRO] ##### PMS.VW_BI_PMS_EMPREST_REC_CONS  ##### - ' || crlf ||
                        '       SQL Erro: ' || sqlerrm || crlf ||
                        '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
    END;
     --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit;
  END IF;
--  LOG - zerar info
    SQLERRO:='';
    CONTADOR1 :=0;
    CONTADOR2 :=0;
  --
  -------------------------------------------------------------------------
  -- Atualização da view de de custos fixos de licença das consultorias
  -------------------------------------------------------------------------
-- 2 - View de de custos fixos de licença das consultorias
--     2.1 - PMS.VW_BI_COSTF_LEAVE_UMKT_MAT
  IF (FLAG = 2 OR FLAG = 99) then
  MENSAGEM := MENSAGEM || CRLF || CRLF ||'Tipo da Carga: 2 - View de de custos fixos de licença das consultorias';

    begin
      BLOCO_UPD := '2 - PMS.VW_BI_COSTF_LEAVE_UMKT_MAT';
      LOG_DT_CARGA_PROCESSO := sysdate;
      SELECT COUNT(*) INTO CONTADOR1 FROM PMS.VW_BI_COSTF_LEAVE_UMKT_MAT;
         --Insert LOG
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;
                     -- REFRESH
                     DBMS_MVIEW.REFRESH('"PMS"."VW_BI_COSTF_LEAVE_UMKT_MAT"');
                     --
      SELECT COUNT(*) INTO CONTADOR2 FROM PMS.VW_BI_COSTF_LEAVE_UMKT_MAT;
      DESCRICAO:= 'FIM    - Linhas: '||to_char(contador2);
      -- LOG: As linhas carregadas foram menores que a original e este valor é maior ou igual a 1%? ERRO!
         if (contador2 < contador1) and ((contador2/contador1)< 0.01) then
               status   := 'ERRO';
               assunto  := '[ERRO%]';
               sqlerro := 'Variacao pocentagem: ' ||round(((contador2/contador1)*100),2) ||'%';
               mensagem := mensagem || crlf || crlf ||
                          '[ERRO] ##### PMS.VW_BI_COSTF_LEAVE_UMKT_MAT  ##### - ' || crlf ||
                          '       Variacao pocentagem: ' ||round(((contador2/contador1)*100),2) ||'%' || crlf ||
                          '       Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                          '       Linhas após Refresh: ' || to_char(contador2) || crlf ||
                          '       Diferença: ' || to_char(contador2 - contador1) || crlf ||
--                          'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                          '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
         else
             STATUS:='OK';
             mensagem := mensagem || crlf || crlf ||
                     '[OK] PMS.VW_BI_COSTF_LEAVE_UMKT_MAT' || crlf ||
                     '     Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                     '     Linhas após Refresh: ' || to_char(contador2) || crlf ||
                     '     Diferença: ' || to_char(contador2 - contador1) || crlf ||
                     '     Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                     '     Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
         end if;
    EXCEPTION
      WHEN OTHERS then
            STATUS:='ERRO';
            assunto  := '[ERRO]';
            SQLERRO:= sqlerrm;
            mensagem := mensagem || crlf || crlf ||
                        '[ERRO] ##### PMS.VW_BI_COSTF_LEAVE_UMKT_MAT  ##### - ' || crlf ||
                        '       SQL Erro: ' || sqlerrm || crlf ||
                        '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
    END;
     --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit;
  END IF;
--  LOG - zerar info
    SQLERRO:='';
    CONTADOR1 :=0;
    CONTADOR2 :=0;
  --
  --------------------------------------------------------------------------
  -- Atualização da view de custos fixos das práticas
  --------------------------------------------------------------------------
--   5 -  Atualização da view de custos fixos das práticas
--     5.1 - PMS.VW_BI_PMS_CUSTFIX_PRAT_DRE_MAT
  IF (FLAG = 5 OR FLAG = 99) THEN
    MENSAGEM := MENSAGEM || CRLF || CRLF ||'Tipo da Carga: 5 - Atualização da view de custos fixos das práticas';
    begin
      BLOCO_UPD := '5 - PMS.VW_BI_PMS_CUSTFIX_PRAT_DRE_MAT';
      LOG_DT_CARGA_PROCESSO := sysdate;
      SELECT COUNT(*)INTO CONTADOR1 FROM PMS.VW_BI_PMS_CUSTFIX_PRAT_DRE_MAT;
         --Insert LOG
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;
                  -- REFRESH
                  DBMS_MVIEW.REFRESH('"PMS"."VW_BI_PMS_CUSTFIX_PRAT_DRE_MAT"');
                  --
      SELECT COUNT(*) INTO CONTADOR2 FROM PMS.VW_BI_PMS_CUSTFIX_PRAT_DRE_MAT;
      DESCRICAO:= 'FIM    - Linhas: '||to_char(contador2);
      -- LOG: As linhas carregadas foram menores que a original e este valor é maior ou igual a 1%? ERRO!
         if (contador2 < contador1) and ((contador2/contador1)< 0.01) then
               status   := 'ERRO';
               assunto  := '[ERRO%]';
               sqlerro := 'Variacao pocentagem: ' ||round(((contador2/contador1)*100),2) ||'%';
               mensagem := mensagem || crlf || crlf ||
                          '[ERRO] ##### PMS.VW_BI_PMS_CUSTFIX_PRAT_DRE_MAT  ##### - ' || crlf ||
                          '       Variacao pocentagem: ' ||round(((contador2/contador1)*100),2) ||'%' || crlf ||
                          '       Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                          '       Linhas após Refresh: ' || to_char(contador2) || crlf ||
                          '       Diferença: ' || to_char(contador2 - contador1) || crlf ||
--                          'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                          '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
         else
             STATUS:='OK';
             mensagem := mensagem || crlf || crlf ||
                     '[OK] PMS.VW_BI_PMS_CUSTFIX_PRAT_DRE_MAT' || crlf ||
                     '     Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                     '     Linhas após Refresh: ' || to_char(contador2) || crlf ||
                     '     Diferença: ' || to_char(contador2 - contador1) || crlf ||
                     '     Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                     '     Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
         end if;
    EXCEPTION
      WHEN OTHERS then
            STATUS:='ERRO';
            assunto  := '[ERRO]';
            SQLERRO:= sqlerrm;
            mensagem := mensagem || crlf || crlf ||
                        '[ERRO] ##### PMS.VW_BI_PMS_CUSTFIX_PRAT_DRE_MAT  ##### - ' || crlf ||
                        '       SQL Erro: ' || sqlerrm || crlf ||
                        '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
    END;
     --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit;
  END IF;
--  LOG - zerar info
    SQLERRO:='';
    CONTADOR1 :=0;
    CONTADOR2 :=0;
  --
  --------------------------------------------------------------
  --  Atualização das informações de empréstimos de recurso.
  --------------------------------------------------------------
--  3 -Atualização das informações de empréstimos de recurso
--     3.1 - PMS.TMP_BI_TOTCUS_EMPREC
--     3.2 - PMS.tmp_bi_custind_emprec
--     3.3 - PMS.tb_bi_pms_custo_emp_rec_dre
  IF (FLAG = 3 OR FLAG = 99) THEN
    MENSAGEM := MENSAGEM || CRLF || CRLF ||'Tipo da Carga: 3 - Atualização das informações de empréstimos de recurso ';
--     3.1 - PMS.TMP_BI_TOTCUS_EMPREC
    begin
     BLOCO_UPD := '3 - PMS.TMP_BI_TOTCUS_EMPREC (frag 1 de 3)';
     LOG_DT_CARGA_PROCESSO := sysdate;
      SELECT COUNT(*) INTO CONTADOR1 FROM PMS.TMP_BI_TOTCUS_EMPREC;
       --Insert LOG
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;

      EXECUTE IMMEDIATE 'truncate table tmp_bi_totcus_emprec';
      --
      OPEN SEL_INFORM;
      --
      FETCH SEL_INFORM
        INTO V_SEL_INFORM;
      --
      WHILE SEL_INFORM%FOUND LOOP
        --
        INSERT INTO TMP_BI_TOTCUS_EMPREC
          (CD_CONTRATO_PRATICA,
           NOME_CONTRATO_PRATICA,
           CD_MOEDA,
           MOEDA,
           DT_MES,
           LOB_CONTRATO_PRATICA,
           VALOR_TOTAL_FTE,
           VALOR_CUSTO_DIR_TCE_CURR,
           VALOR_CUSTO_DIR_TCE_BRL,
           VALOR_CUSTO_DIR_INFRA_CURR,
           VALOR_CUSTO_DIR_INFRA_BRL,
           VALOR_CUSTO_IND_TCE_CURR,
           VALOR_CUSTO_IND_TCE_BRL,
           VALOR_CUSTO_IND_INFRA_CURR,
           VALOR_CUSTO_IND_INFRA_BRL,
           VALOR_HORA_EXTRA,
           VALOR_PLANTAO,
           VALOR_TOTAL_CUSTO_CURR,
           VALOR_TOTAL_CUSTO_BRL,
           DATA_ULT_ATUALIZACAO)
        VALUES
          (V_SEL_INFORM.CD_CONTRATO_PRATICA,
           V_SEL_INFORM.NOME_CONTRATO_PRATICA,
           V_SEL_INFORM.CD_MOEDA,
           V_SEL_INFORM.MOEDA,
           V_SEL_INFORM.DT_MES,
           V_SEL_INFORM.LOB_CONTRATO_PRATICA,
           V_SEL_INFORM.VALOR_TOTAL_FTE,
           V_SEL_INFORM.VALOR_CUSTO_DIR_TCE_CURR,
           V_SEL_INFORM.VALOR_CUSTO_DIR_TCE_BRL,
           V_SEL_INFORM.VALOR_CUSTO_DIR_INFRA_CURR,
           V_SEL_INFORM.VALOR_CUSTO_DIR_INFRA_BRL,
           V_SEL_INFORM.VALOR_CUSTO_IND_TCE_CURR,
           V_SEL_INFORM.VALOR_CUSTO_IND_TCE_BRL,
           V_SEL_INFORM.VALOR_CUSTO_IND_INFRA_CURR,
           V_SEL_INFORM.VALOR_CUSTO_IND_INFRA_BRL,
           V_SEL_INFORM.VALOR_HORA_EXTRA,
           V_SEL_INFORM.VALOR_PLANTAO,
           V_SEL_INFORM.VALOR_TOTAL_CUSTO_CURR,
           V_SEL_INFORM.VALOR_TOTAL_CUSTO_BRL,
           SYSDATE);
        --
        COMMIT;
        --
        FETCH SEL_INFORM
          INTO V_SEL_INFORM;
      END LOOP;
      --
      CLOSE SEL_INFORM;
      --LOG
      SELECT COUNT(*) INTO CONTADOR2 FROM PMS.TMP_BI_TOTCUS_EMPREC;
       DESCRICAO:= 'FIM    - Linhas: '||to_char(contador2);
      -- LOG: As linhas carregadas foram menores que a original e este valor é maior ou igual a 1%? ERRO!
         if (contador2 < contador1) and ((contador2/contador1)< 0.01) then
               status   := 'ERRO';
               assunto  := '[ERRO%]';
               sqlerro := 'Variacao pocentagem: ' ||round(((contador2/contador1)*100),2) ||'%';
               mensagem := mensagem || crlf || crlf ||
                          '[ERRO] ##### PMS.TMP_BI_TOTCUS_EMPREC  ##### - ' || crlf ||
                          '       Variacao pocentagem: ' ||round(((contador2/contador1)*100),2) ||'%' || crlf ||
                          '       Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                          '       Linhas após Refresh: ' || to_char(contador2) || crlf ||
                          '       Diferença: ' || to_char(contador2 - contador1) || crlf ||
--                          'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                          '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
         else
             STATUS:='OK';
             mensagem := mensagem || crlf || crlf ||
                     '[OK] PMS.TMP_BI_TOTCUS_EMPREC' || crlf ||
                     '     Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                     '     Linhas após Refresh: ' || to_char(contador2) || crlf ||
                     '     Diferença: ' || to_char(contador2 - contador1) || crlf ||
                     '     Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                     '     Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
         end if;
    EXCEPTION
      WHEN OTHERS then
            STATUS:='ERRO';
            assunto  := '[ERRO]';
            SQLERRO:= sqlerrm;
            mensagem := mensagem || crlf || crlf ||
                        '[ERRO] ##### PMS.TMP_BI_TOTCUS_EMPREC  ##### - ' || crlf ||
                        '       SQL Erro: ' || sqlerrm || crlf ||
                        '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
    END;
     --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit;
--  LOG - zerar info
    SQLERRO:='';
    CONTADOR1 :=0;
    CONTADOR2 :=0;
    -------------------------
--     3.2 - PMS.tmp_bi_custind_emprec
    begin
    BLOCO_UPD := '3 - PMS.tmp_bi_custind_emprec (frag 2 de 3)';
    LOG_DT_CARGA_PROCESSO := sysdate;
      SELECT COUNT(*) INTO CONTADOR1 FROM PMS.TMP_BI_CUSTIND_EMPREC;
      --Insert LOG
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;
      --
      EXECUTE IMMEDIATE 'truncate table tmp_bi_custind_emprec';
      --
      OPEN SEL_INFORM2;
      --
      FETCH SEL_INFORM2
        INTO V_SEL_INFORM2;
      --
      WHILE SEL_INFORM2%FOUND LOOP
        --
        INSERT INTO TMP_BI_CUSTIND_EMPREC
        VALUES
          (V_SEL_INFORM2.CD_PESSOA,
           V_SEL_INFORM2.CD_CONTRATO_PRATICA,
           V_SEL_INFORM2.DT_MES,
           V_SEL_INFORM2.CD_MOEDA,
           V_SEL_INFORM2.MOEDA,
           V_SEL_INFORM2.LOGIN,
           V_SEL_INFORM2.VALOR_TOTAL_FTE_IND,
           V_SEL_INFORM2.VALOR_TOTAL_CUSTO_CURR,
           V_SEL_INFORM2.VALOR_TOTAL_CUSTO_BRL,
           V_SEL_INFORM2.CD_GRUPO_CUSTO,
           V_SEL_INFORM2.CD_PERIODO,
           V_SEL_INFORM2.LOB_PESSOA,
           SYSDATE);
        --
        COMMIT;
        --
        FETCH SEL_INFORM2
          INTO V_SEL_INFORM2;
      END LOOP;
      --
      CLOSE SEL_INFORM2;
      SELECT COUNT(*) INTO CONTADOR2 FROM PMS.TMP_BI_CUSTIND_EMPREC;
       DESCRICAO:= 'FIM    - Linhas: '||to_char(contador2);
      -- LOG: As linhas carregadas foram menores que a original e este valor é maior ou igual a 1%? ERRO!
         if (contador2 < contador1) and ((contador2/contador1)< 0.01) then
               status   := 'ERRO';
               assunto  := '[ERRO%]';
               sqlerro := 'Variacao pocentagem: ' ||round(((contador2/contador1)*100),2) ||'%';
               mensagem := mensagem || crlf || crlf ||
                          '[ERRO] ##### PMS.tmp_bi_custind_emprec  ##### - ' || crlf ||
                          '       Variacao pocentagem: ' ||round(((contador2/contador1)*100),2) ||'%' || crlf ||
                          '       Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                          '       Linhas após Refresh: ' || to_char(contador2) || crlf ||
                          '       Diferença: ' || to_char(contador2 - contador1) || crlf ||
--                          'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                          '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
         else
             STATUS:='OK';
             mensagem := mensagem || crlf || crlf ||
                     '[OK] PMS.tmp_bi_custind_emprec' || crlf ||
                     '     Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                     '     Linhas após Refresh: ' || to_char(contador2) || crlf ||
                     '     Diferença: ' || to_char(contador2 - contador1) || crlf ||
                     '     Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                     '     Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
         end if;
    EXCEPTION
      WHEN OTHERS then
            STATUS:='ERRO';
            assunto  := '[ERRO]';
            SQLERRO:= sqlerrm;
            mensagem := mensagem || crlf || crlf ||
                        '[ERRO] ##### PMS.tmp_bi_custind_emprec  ##### - ' || crlf ||
                        '       SQL Erro: ' || sqlerrm || crlf ||
                        '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
    END;
     --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit;
--  LOG - zerar info
    SQLERRO:='';
    CONTADOR1 :=0;
    CONTADOR2 :=0;
    ------------========
--     3.3 - PMS.tb_bi_pms_custo_emp_rec_dre
    begin
    BLOCO_UPD := '3 - PMS.tb_bi_pms_custo_emp_rec_dre (frag 3 de 3)';
    LOG_DT_CARGA_PROCESSO := sysdate;
      SELECT COUNT(*) INTO CONTADOR1 FROM PMS.TB_BI_PMS_CUSTO_EMP_REC_DRE;
      --Insert LOG
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;
      --
      EXECUTE IMMEDIATE 'truncate table tb_bi_pms_custo_emp_rec_dre';
      --
      INSERT INTO TB_BI_PMS_CUSTO_EMP_REC_DRE
        SELECT CD_CONTRATO_PRATICA,
               NOME_CONTRATO_PRATICA,
               DT_MES,
               CD_MOEDA,
               MOEDA,
               0 VALOR_COTACAO,
               LOGIN,
               LOB_CONTRATO,
               VALOR_CUSTIND_CURR_POS,
               VALOR_CUSTIND_BRL_POS,
               LOB_PESSOA,
               VALOR_CUSTIND_CURR_NEG,
               VALOR_CUSTIND_BRL_NEG,
               FLAG,
               0 VALOR_HR_EXTRA_EMP_REC_CURR,
               0 VALOR_HR_EXTRA_EMP_REC_BRL,
               0 VALOR_PLANTAO_EMP_REC_CURR,
               0 VALOR_PLANTAO_EMP_REC_BRL,
               SYSDATE
          FROM VW_BI_PMS_CUSTO_EMP_REC_DRE PDR;
      --
      COMMIT;
      --LOG
      SELECT COUNT(*) INTO CONTADOR2 FROM PMS.TB_BI_PMS_CUSTO_EMP_REC_DRE;
       DESCRICAO:= 'FIM    - Linhas: '||to_char(contador2);
      -- LOG: As linhas carregadas foram menores que a original e este valor é maior ou igual a 1%? ERRO!
         if (contador2 < contador1) and ((contador2/contador1)< 0.01) then
               status   := 'ERRO';
               assunto  := '[ERRO%]';
               sqlerro := 'Variacao pocentagem: ' ||round(((contador2/contador1)*100),2) ||'%';
               mensagem := mensagem || crlf || crlf ||
                          '[ERRO] ##### PMS.tb_bi_pms_custo_emp_rec_dre  ##### - ' || crlf ||
                          '       Variacao pocentagem: ' ||round(((contador2/contador1)*100),2) ||'%' || crlf ||
                          '       Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                          '       Linhas após Refresh: ' || to_char(contador2) || crlf ||
                          '       Diferença: ' || to_char(contador2 - contador1) || crlf ||
--                          'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                          '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
         else
             STATUS:='OK';
             mensagem := mensagem || crlf || crlf ||
                     '[OK] PMS.tb_bi_pms_custo_emp_rec_dre' || crlf ||
                     '     Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                     '     Linhas após Refresh: ' || to_char(contador2) || crlf ||
                     '     Diferença: ' || to_char(contador2 - contador1) || crlf ||
                     '     Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                     '     Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
         end if;
    EXCEPTION
      WHEN OTHERS then
            STATUS:='ERRO';
            assunto  := '[ERRO]';
            SQLERRO:= sqlerrm;
            mensagem := mensagem || crlf || crlf ||
                        '[ERRO] ##### PMS.tb_bi_pms_custo_emp_rec_dre  ##### - ' || crlf ||
                        '       SQL Erro: ' || sqlerrm || crlf ||
                        '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
    END;
     --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit;
--  LOG - zerar info
    SQLERRO:='';
    CONTADOR1 :=0;
    CONTADOR2 :=0;

  END IF;
  --
  --------------------------------------------------------------------------
  -- Carga das informações de Custos Indiretos, para a DRE das Praticas
  --------------------------------------------------------------------------
--4 -Carga das informações de Custos Indiretos, para a DRE das Praticas
--     4.1 - PMS.tb_bi_pms_custos_ind_dre
  IF (FLAG = 4 OR FLAG = 99) THEN
    MENSAGEM := MENSAGEM || CRLF || CRLF ||'Tipo da Carga: 4 - Carga das informações de Custos Indiretos, para a DRE das Praticas' ;
    --LOG
    begin
    BLOCO_UPD := '4 - PMS.tb_bi_pms_custos_ind_dre';
    LOG_DT_CARGA_PROCESSO := sysdate;
      SELECT COUNT(*) INTO CONTADOR1 FROM PMS.TB_BI_PMS_CUSTOS_IND_DRE;
       --Insert LOG
         DESCRICAO:= 'INICIO - Linhas: '||to_char(contador1);
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,'OK',nome_proc);
         commit;
      --

      EXECUTE IMMEDIATE 'truncate table tb_bi_pms_custos_ind_dre';
      --
      OPEN SEL_INFORM_CIND;
      --
      FETCH SEL_INFORM_CIND
        INTO V_SEL_INF_CIND;
      --
      WHILE SEL_INFORM_CIND%FOUND LOOP
        INSERT INTO TB_BI_PMS_CUSTOS_IND_DRE
        VALUES
          (V_SEL_INF_CIND.FONTE,
           V_SEL_INF_CIND.SUB_FONTE,
           V_SEL_INF_CIND.CD_PESSOA,
           V_SEL_INF_CIND.CATEGORIA,
           V_SEL_INF_CIND.LOGIN,
           V_SEL_INF_CIND.DT_MES,
           V_SEL_INF_CIND.DT_ADMISSAO,
           V_SEL_INF_CIND.DT_RESCISAO,
           V_SEL_INF_CIND.CD_MOEDA,
           V_SEL_INF_CIND.MOEDA,
           V_SEL_INF_CIND.VALOR_COTACAO,
           V_SEL_INF_CIND.CD_CONTRATO_PRATICA,
           V_SEL_INF_CIND.NOME_CONTRATO_PRATICA,
           V_SEL_INF_CIND.PRATICA,
           V_SEL_INF_CIND.PERC_ALOCAVEL,
           V_SEL_INF_CIND.PERC_ALOCACAO,
           V_SEL_INF_CIND.VALOR_TCE,
           V_SEL_INF_CIND.VALOR_TCE_BRL,
           V_SEL_INF_CIND.VLR_CUSTO_INFRA_BASE,
           V_SEL_INF_CIND.VLR_CUSTO_INFRA_BASE_BRL,
           V_SEL_INF_CIND.PCT_FERIAS,
           V_SEL_INF_CIND.PCT_LICENCA,
           V_SEL_INF_CIND.PCT_DISTRIB,
           V_SEL_INF_CIND.VALOR_FTE_CIND,
           V_SEL_INF_CIND.VALOR_CUSTO_IND_TCE_CURR,
           V_SEL_INF_CIND.VALOR_CUSTO_IND_TCE_BRL,
           V_SEL_INF_CIND.VALOR_CUSTO_IND_INFRA_CURR,
           V_SEL_INF_CIND.VALOR_CUSTO_IND_INFRA_BRL,
           V_SEL_INF_CIND.UMKT,
           SYSDATE,
          VALOR_CUSTO_DIR_CHBK_CURR,
          VALOR_CUSTO_DIR_CHBK_BRL,
          VALOR_CUSTO_DIR_CHBK_USD  
           );
        --
        COMMIT;
        --
        FETCH SEL_INFORM_CIND
          INTO V_SEL_INF_CIND;
      END LOOP;
      --
      CLOSE SEL_INFORM_CIND;

      SELECT COUNT(*) INTO CONTADOR2 FROM PMS.TB_BI_PMS_CUSTOS_IND_DRE;
       DESCRICAO:= 'FIM    - Linhas: '||to_char(contador2);
      -- LOG: As linhas carregadas foram menores que a original e este valor é maior ou igual a 1%? ERRO!
         if (contador2 < contador1) and ((contador2/contador1)< 0.01) then
               status   := 'ERRO';
               assunto  := '[ERRO%]';
               sqlerro := 'Variacao pocentagem: ' ||round(((contador2/contador1)*100),2) ||'%';
               mensagem := mensagem || crlf || crlf ||
                          '[ERRO] ##### PMS.tb_bi_pms_custos_ind_dre  ##### - ' || crlf ||
                          '       Variacao pocentagem: ' ||round(((contador2/contador1)*100),2) ||'%' || crlf ||
                          '       Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                          '       Linhas após Refresh: ' || to_char(contador2) || crlf ||
                          '       Diferença: ' || to_char(contador2 - contador1) || crlf ||
--                          'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                          '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
         else
             STATUS:='OK';
             mensagem := mensagem || crlf || crlf ||
                     '[OK] PMS.tb_bi_pms_custos_ind_dre' || crlf ||
                     '     Linhas antes do Refresh: ' || to_char(contador1) || crlf ||
                     '     Linhas após Refresh: ' || to_char(contador2) || crlf ||
                     '     Diferença: ' || to_char(contador2 - contador1) || crlf ||
                     '     Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                     '     Data Execução: ' || to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' );
         end if;
    EXCEPTION
      WHEN OTHERS then
            STATUS:='ERRO';
            assunto  := '[ERRO]';
            SQLERRO:= sqlerrm;
            mensagem := mensagem || crlf || crlf ||
                        '[ERRO] ##### PMS.tb_bi_pms_custos_ind_dre  ##### - ' || crlf ||
                        '       SQL Erro: ' || sqlerrm || crlf ||
                        '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
    END;
     --Inserir LOG
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit;
--  LOG - zerar info
    SQLERRO:='';
    CONTADOR1 :=0;
    CONTADOR2 :=0;
 ------------------------------------
  END IF;
   mensagem := mensagem || crlf || crlf || '=== Fim da Carga '||to_char( sysdate, 'DD/MM/YYYY HH24:MI:SS' )||' ===';
   mensagem := mensagem || crlf || 'select * from pms.TB_BI_PMS_LOG_CARGAS where NOME_CARGA = ''PROFITLOSS'' order by 2 desc';
   ASSUNTO := ASSUNTO || ' [PMS][PRATICAS] Resultado da Carga';
   send_mail('lahumada@cit.com.br', assunto, mensagem);
   send_mail('alexandrel@cit.com.br', assunto, mensagem);
end ups_load_inform_dre_praticas;