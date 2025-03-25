CREATE OR REPLACE PROCEDURE "USP_PMS_PRATICAS" (flag number) as
--INTERVAL: trunc(sysdate + 1)+ 2/24
   -----------------------------------------------------------------------
   /*
      Informac?o sobre a carga.
      Ordem do Load:
      6 - Views de Atributos
        6.1 - PMS.VW_BI_PMS_ATRIBUTOS_DRE_MAT
        6.2 - PMS.VW_BI_PMS_ATRIBUTOS_DRE_MAT2
      1 - View de emprestimos de recurso
        1.1 - PMS.VW_BI_PMS_EMPREST_REC_CONS
        1.2 - PMS.VW_BI_PMS_EMPREST_REC
        1.3 - PMS.TB_BI_PMS_PRAT_DRE_MAT
      2 - View de de custos fixos de licenca das consultorias
        2.1 - PMS.VW_BI_COSTF_LEAVE_UMKT_MAT
      5 -  Atualizac?o da view de custos fixos das praticas
        5.1 - PMS.VW_BI_PMS_CUSTFIX_PRAT_DRE_MAT
      3 -Atualizac?o das informac?es de emprestimos de recurso
        3.1 - PMS.TMP_BI_TOTCUS_EMPREC
        3.2 - PMS.tmp_bi_custind_emprec
        3.4 - PMS.tb_bi_pms_custo_emp_rec_dre
      4 -Carga das informac?es de Custos Indiretos, para a DRE das Praticas
        4.1 - PMS.tb_bi_pms_custos_ind_dre
      7 -Carga das informac?es de Histórico de Base de Colaborador
      8 - Carga das informações de Hora extra e Plantão

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
             (s.valor_custo_dir_tce_curr + s.valor_custo_dir_infra_curr +
             s.valor_custo_ind_tce_curr + s.valor_custo_ind_infra_curr +
             s.valor_hora_extra + s.valor_plantao) valor_total_custo_curr,
             (s.valor_custo_dir_tce_brl + s.valor_custo_dir_infra_brl +
             s.valor_custo_ind_tce_brl + s.valor_custo_ind_infra_brl +
             s.valor_hora_extra + s.valor_plantao) valor_total_custo_brl
      from   (select s.flag,
                     s.cd_contrato_pratica,
                     s.nome_contrato_pratica,
                     case
                        when s.moeda is null then
                         'BRL'
                        else
                         s.moeda
                     end moeda,
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
              from   (select flag,
                             cd_contrato_pratica,
                             nome_contrato_pratica,
                             moeda,
                             dt_mes,
                             login,
                             lob_contrato_pratica,
                             sum(s.valor_custo_dir_tce_curr) over(partition by s.cd_contrato_pratica, s.dt_mes) valor_custo_dir_tce_curr,
                             sum(s.valor_custo_dir_tce_brl) over(partition by s.cd_contrato_pratica, s.dt_mes) valor_custo_dir_tce_brl,
                             sum(s.valor_custo_dir_infra_curr) over(partition by s.cd_contrato_pratica, s.dt_mes) valor_custo_dir_infra_curr,
                             sum(s.valor_custo_dir_infra_brl) over(partition by s.cd_contrato_pratica, s.dt_mes) valor_custo_dir_infra_brl,
                             sum(s.valor_custo_ind_tce_brl) over(partition by s.cd_contrato_pratica, s.dt_mes) valor_custo_ind_tce_brl,
                             sum(s.valor_custo_ind_tce_curr) over(partition by s.cd_contrato_pratica, s.dt_mes) valor_custo_ind_tce_curr,
                             sum(s.valor_custo_ind_infra_brl) over(partition by s.cd_contrato_pratica, s.dt_mes) valor_custo_ind_infra_brl,
                             sum(s.valor_custo_ind_infra_curr) over(partition by s.cd_contrato_pratica, s.dt_mes) valor_custo_ind_infra_curr,
                             sum(s.valor_hora_extra) over(partition by s.cd_contrato_pratica, s.dt_mes) valor_hora_extra,
                             sum(s.valor_plantao) over(partition by s.cd_contrato_pratica, s.dt_mes) valor_plantao,
                             row_number() over(partition by s.cd_contrato_pratica, s.dt_mes order by s.cd_contrato_pratica, s.dt_mes) nlinha
                      from   (select s.flag,
                                     s.login,
                                     s.cd_contrato_pratica,
                                     s.nome_contrato_pratica,
                                     case
                                        when s.moeda is null then
                                         'BRL'
                                        else
                                         s.moeda
                                     end moeda,
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
                              from   tb_bi_pms_dre_servicos s) s) s
              where  s.nlinha = 1) s,
             moeda m,
             (select cd_contrato_pratica,
                     nome_contrato_pratica,
                     dt_mes,
                     lob_contrato_pratica,
                     total_fte_aloc
              from   (select ma.copr_cd_contrato_pratica cd_contrato_pratica,
                             cp.copr_nm_contrato_pratica nome_contrato_pratica,
                             ap.alpe_dt_alocacao_periodo dt_mes,
                             pr.prat_sg_pratica lob_contrato_pratica,
                             sum(ap.alpe_pr_alocacao_periodo) over(partition by ma.copr_cd_contrato_pratica, ap.alpe_dt_alocacao_periodo) total_fte_aloc,
                             row_number() over(partition by ma.copr_cd_contrato_pratica, ap.alpe_dt_alocacao_periodo order by ma.copr_cd_contrato_pratica, ap.alpe_dt_alocacao_periodo) nlinha
                      from   mapa_alocacao    ma,
                             contrato_pratica cp,
                             alocacao         al,
                             alocacao_periodo ap,
                             pratica          pr
                      where  ma.maal_cd_mapa_alocacao =
                             al.maal_cd_mapa_alocacao
                      and    ma.copr_cd_contrato_pratica =
                             cp.copr_cd_contrato_pratica
                      and    al.aloc_cd_alocacao = ap.aloc_cd_alocacao
                      and    cp.prat_cd_pratica = pr.prat_cd_pratica
                      and    ma.maal_in_versao = 'PB')
              where  nlinha = 1) al
      where  s.cd_contrato_pratica = al.cd_contrato_pratica
      and    s.dt_mes = al.dt_mes
      and    s.moeda = m.moed_sg_moeda;
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
      from   (select inf.cd_pessoa,
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
                     row_number() over(partition by inf.cd_contrato_pratica, inf.dt_mes, inf.login order by inf.cd_contrato_pratica, inf.dt_mes, inf.login) nlinha
              from   grupo_custo_centro_lucro gccl,
                     centro_lucro cl,
                     (select inf.cd_pessoa,
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
                      from   grupo_custo gc,
                             (select gcp.grcu_cd_grupo_custo cd_grupo_custo,
                                     gcp.grcp_dt_inicio dt_inicio,
                                     gcp.grcp_cd_gc_periodo cd_periodo,
                                     case
                                        when gcp.grcp_dt_fim is not null then
                                         gcp.grcp_dt_fim
                                        else
                                         (select p2.para_dt_parametro
                                          from   parametro p2
                                          where  p2.para_cd_parametro = 1)
                                     end dt_fim
                              from   grupo_custo_periodo gcp) gcp,
                             (select inf.cd_pessoa,
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
                              from   (select pgc.pess_cd_pessoa cd_pessoa,
                                             pgc.grcu_cd_grupo_custo cd_grupo_custo,
                                             pgc.pegc_in_status,
                                             pgc.pegc_dt_inicio dt_inicio,
                                             case
                                                when pgc.pegc_dt_fim is not null then
                                                 pgc.pegc_dt_fim
                                                else
                                                 (select p1.para_dt_parametro
                                                  from   parametro p1
                                                  where  p1.para_cd_parametro = 1)
                                             end dt_fim
                                      from   pessoa_grupo_custo pgc) pgc,
                                     (select p1.pess_cd_pessoa cd_pessoa,
                                             inf.cd_contrato_pratica,
                                             inf.nome_contrato_pratica,
                                             inf.dt_mes,
                                             inf.cd_moeda,
                                             inf.moeda,
                                             inf.login,
                                             inf.valor_total_fte_ind,
                                             inf.valor_total_custo_curr,
                                             inf.valor_total_custo_brl
                                      from   pessoa p1,
                                             (select cd_contrato_pratica,
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
                                                     (valor_custo_dir_tce_curr +
                                                     valor_custo_dir_infra_curr +
                                                     valor_custo_ind_tce_curr +
                                                     valor_custo_ind_infra_curr +
                                                     valor_hora_extra +
                                                     valor_plantao) valor_total_custo_curr,
                                                     (valor_custo_dir_tce_brl +
                                                     valor_custo_dir_infra_brl +
                                                     valor_custo_ind_tce_brl +
                                                     valor_custo_ind_infra_brl +
                                                     valor_hora_extra +
                                                     valor_plantao) valor_total_custo_brl
                                              from   (select s.cd_contrato_pratica,
                                                             al.nome_contrato_pratica,
                                                             s.dt_mes,
                                                             s.login,
                                                             al.valor_fte,
                                                             s.flag,
                                                             m.moed_cd_moeda cd_moeda,
                                                             case
                                                                when s.moeda is null then
                                                                 'BRL'
                                                                else
                                                                 s.moeda
                                                             end moeda,
                                                             sum(s.valor_custo_dir_tce_curr) over(partition by s.cd_contrato_pratica, s.dt_mes, s.login) valor_custo_dir_tce_curr,
                                                             sum(s.valor_custo_dir_tce_brl) over(partition by s.cd_contrato_pratica, s.dt_mes, s.login) valor_custo_dir_tce_brl,
                                                             sum(s.valor_custo_dir_infra_curr) over(partition by s.cd_contrato_pratica, s.dt_mes, s.login) valor_custo_dir_infra_curr,
                                                             sum(s.valor_custo_dir_infra_brl) over(partition by s.cd_contrato_pratica, s.dt_mes, s.login) valor_custo_dir_infra_brl,
                                                             sum(s.valor_custo_ind_tce_curr) over(partition by s.cd_contrato_pratica, s.dt_mes, s.login) valor_custo_ind_tce_curr,
                                                             sum(s.valor_custo_ind_tce_brl) over(partition by s.cd_contrato_pratica, s.dt_mes, s.login) valor_custo_ind_tce_brl,
                                                             sum(s.valor_custo_ind_infra_curr) over(partition by s.cd_contrato_pratica, s.dt_mes, s.login) valor_custo_ind_infra_curr,
                                                             sum(s.valor_custo_ind_infra_brl) over(partition by s.cd_contrato_pratica, s.dt_mes, s.login) valor_custo_ind_infra_brl,
                                                             sum(s.valor_hora_extra) over(partition by s.cd_contrato_pratica, s.dt_mes, s.login) valor_hora_extra,
                                                             sum(s.valor_plantao) over(partition by s.cd_contrato_pratica, s.dt_mes, s.login) valor_plantao
                                                      from   tb_bi_pms_dre_servicos s,
                                                             moeda m,
                                                             (select cd_contrato_pratica,
                                                                     nome_contrato_pratica,
                                                                     dt_mes,
                                                                     login,
                                                                     valor_fte,
                                                                     total_fte_aloc
                                                              from   (select ma.copr_cd_contrato_pratica cd_contrato_pratica,
                                                                             cp.copr_nm_contrato_pratica nome_contrato_pratica,
                                                                             ap.alpe_dt_alocacao_periodo dt_mes,
                                                                             ap.alpe_pr_alocacao_periodo valor_fte,
                                                                             pe.pess_cd_login login,
                                                                             sum(ap.alpe_pr_alocacao_periodo) over(partition by ma.copr_cd_contrato_pratica, ap.alpe_dt_alocacao_periodo, al.recu_cd_recurso) total_fte_aloc,
                                                                             row_number() over(partition by ma.copr_cd_contrato_pratica, ap.alpe_dt_alocacao_periodo, al.recu_cd_recurso order by ma.copr_cd_contrato_pratica, ap.alpe_dt_alocacao_periodo, al.recu_cd_recurso) nlinha
                                                                      from   mapa_alocacao    ma,
                                                                             contrato_pratica cp,
                                                                             alocacao         al,
                                                                             pessoa           pe,
                                                                             alocacao_periodo ap
                                                                      where  ma.maal_cd_mapa_alocacao =
                                                                             al.maal_cd_mapa_alocacao
                                                                      and    ma.copr_cd_contrato_pratica =
                                                                             cp.copr_cd_contrato_pratica
                                                                      and    al.aloc_cd_alocacao =
                                                                             ap.aloc_cd_alocacao
                                                                      and    al.recu_cd_recurso =
                                                                             pe.recu_cd_recurso
                                                                      and    ma.maal_in_versao = 'PB')
                                                              where  nlinha = 1) al
                                                      where  case when
                                                       s.moeda is null then 'BRL' else s.moeda end = m.moed_sg_moeda and s.cd_contrato_pratica = al.cd_contrato_pratica and s.dt_mes = al.dt_mes and s.login = al.login)
                                              where  valor_fte > 0) inf
                                      where  p1.pess_cd_login = inf.login) inf
                              where  inf.cd_pessoa = pgc.cd_pessoa(+)
                              and    inf.dt_mes between pgc.dt_inicio(+) and
                                     pgc.dt_fim(+)) inf
                      where  inf.cd_grupo_custo = gc.grcu_cd_grupo_custo
                      and    gc.grcu_cd_grupo_custo = gcp.cd_grupo_custo
                      and    inf.dt_mes between gcp.dt_inicio(+) and
                             gcp.dt_fim(+)) inf
              where  inf.cd_periodo = gccl.grcp_cd_gc_periodo
              and    gccl.celu_cd_centro_lucro = cl.celu_cd_centro_lucro
              and    cl.nacl_cd_natureza = 61)
      where  nlinha = 1;
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
               VALOR_CUSTO_IND_CHBK_CURR,
               VALOR_CUSTO_IND_CHBK_BRL,
               VALOR_CUSTO_IND_CHBK_USD              
      from   vw_bi_pms_custos_ind_dre;
   --
   v_sel_inf_cind sel_inform_cind%rowtype;
   --
   cursor sel_inf_pratica_dre is
      select cd_contrato_pratica,
             nome_contrato_pratica,
             lob_contrato_pratica,
             dt_mes,
             nvl(cd_moeda, 1) cd_moeda,
             nvl(moeda, 'BRL') moeda,
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
      from   vw_bi_pms_pratica_dre /*_mat*/
      ;
   --
   v_sel_inf_prat_dre sel_inf_pratica_dre%rowtype;
   -- Informac?es para LOG
   log_data              date := trunc(sysdate);
   log_dt_carga_processo date;
   nome_carga            varchar2(50) := '(6) PRATICAS';
   bloco_upd             varchar2(50) := '';
   descricao             varchar2(200) := '';
   status                varchar2(30) := '';
   sqlerro               varchar2(200) := '';
   nome_proc             varchar2(200) := 'USP_PMS_praticas';
   crlf                  varchar2(2) := chr(13) || chr(10);
   assunto               varchar2(200) := '';
   mensagem              varchar2(4000) := '=== Inicio da Carga ' ||
                                           to_char(sysdate,
                                                   'DD/MM/YYYY HH24:MI:SS') ||
                                           '  ===';
   contador1             integer;
   contador2             integer;
   cod_modulo            integer;
   sms_msg               varchar2(200) := 'Objeto: ';
   
begin
   --------------------------------------------------------------------------
   -- Atualizac?o de carga das views de atributos
   --------------------------------------------------------------------------
   mensagem := mensagem || crlf || ':: PROCEDURE: ' || nome_proc || ' ::';
   send_mail('lnoboru@ciandt.com','[PMS][' || nome_carga || '] Start Proc',mensagem);   
   --send_mail('lahumada@cit.com.br','[PMS][' || nome_carga || '] Start Proc',mensagem);
   send_mail('alexandrel@cit.com.br','[PMS][' || nome_carga || '] Start Proc',mensagem);
   
     send_mail('tspadari@ciandt.com','[PMS][' || nome_carga || '] Start Proc',mensagem);
     send_mail('llino@ciandt.com','[PMS][' || nome_carga || '] Start Proc',mensagem);
     send_mail('andreiap@ciandt.com','[PMS][' || nome_carga || '] Start Proc',mensagem); 
   
   
   
   select count(*) 
   into cod_modulo
   from   modulo
   where  modu_cd_modulo = 2 and
          ((modu_dt_fechamento = case when(to_char(sysdate, 'dd') <= 5)
                                      then(to_date('01/' || to_char(add_months(sysdate, -2), 'mm/yyyy'), 'dd/mm/yyyy'))
                                      else(to_date('01/' || to_char(add_months(sysdate, -1), 'mm/yyyy'), 'dd/mm/yyyy')) 
                                      end) or
            modu_dt_fechamento = (to_date('01/' || to_char(add_months(sysdate, -1), 'mm/yyyy'), 'dd/mm/yyyy')));
   
   if cod_modulo = 1 then   
            ---------------------------------------------------------------------------
            --Tipo da Carga: 6 - Views de Atributos
            ---------------------------------------------------------------------------
            --   6.1 - PMS.VW_BI_PMS_ATRIBUTOS_DRE_MAT
            --   6.2 - PMS.VW_BI_PMS_ATRIBUTOS_DRE_MAT2   
            if (flag = 6 or flag = 99) then
               mensagem := mensagem || crlf || crlf ||
                           'Tipo da Carga: 6 - Views de Atributos';
               --  6.1 - PMS.VW_BI_PMS_ATRIBUTOS_DRE_MAT
               begin
                  bloco_upd             := '6 - PMS.VW_BI_PMS_ATRIBUTOS_DRE_MAT (flag 1 de 2)';
                  log_dt_carga_processo := sysdate;
                  select count(*)
                  into   contador1
                  from   pms.vw_bi_pms_atributos_dre_mat
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
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
                  --REFRESH
                  dbms_mview.refresh('"PMS"."VW_BI_PMS_ATRIBUTOS_DRE_MAT"');
                  --
                  select count(*)
                  into   contador2
                  from   pms.vw_bi_pms_atributos_dre_mat
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
                  --LOG
                  descricao := 'FIM    - Linhas: ' || to_char(contador2);
                  -- LOG: As linhas carregadas foram menores que a original e este valor e maior ou igual a 1%? ERRO!
                  if (contador2 < contador1) and ((contador2 / contador1) < 0.99) then
                     status   := 'ERRO';
                     assunto  := '[ERRO%]';
                     sqlerro  := 'Variacao porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%';
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.VW_BI_PMS_ATRIBUTOS_DRE_MAT  ##### - ' || crlf ||
                                 '       Variacao Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '       Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '       Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '       Diferenca: ' ||
                                 to_char(contador2 - contador1) || crlf ||
                                -- 'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg || 'VW_BI_PMS_ATRIBUTOS_DRE_MAT; ERRO: ' ||
                                sqlerro || '; Data: ' ||
                                to_char(sysdate, 'DD/MM/YYYY');
                   /*  send_mail('noc-cit-sms@cit.com.br',
                               assunto || '[PMS][' || nome_carga ||
                               '] Resultado da Carga',
                               sms_msg);*/
                     --limpa msg
                     sms_msg := 'Objeto: ';
                  else
                     status   := 'OK';
                     mensagem := mensagem || crlf || crlf ||
                                 '[OK] PMS.VW_BI_PMS_ATRIBUTOS_DRE_MAT' || crlf ||
                                 '     Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '     Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '     Diferenca: ' || to_char(contador2 - contador1) || crlf ||
                                 '     Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '     Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                  end if;
               exception
                  when others then
                     status   := 'ERRO';
                     assunto  := '[ERRO]';
                     sqlerro  := sqlerrm;
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.VW_BI_PMS_ATRIBUTOS_DRE_MAT  ##### - ' || crlf ||
                                 '       SQL Erro: ' || sqlerrm || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg || 'VW_BI_PMS_ATRIBUTOS_DRE_MAT; ERRO: ' ||
                                sqlerro || '; Data: ' ||
                                to_char(sysdate, 'DD/MM/YYYY');
                   /*  send_mail('noc-cit-sms@cit.com.br',
                               assunto || '[PMS][' || nome_carga ||
                               '] Resultado da Carga',
                               sms_msg);*/
                     --limpa msg
                     sms_msg := 'Objeto: ';
               end;
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
               --
               sqlerro   := '';
               contador1 := 0;
               contador2 := 0;
               dbms_lock.sleep(1);
               ---------------------------------------------
               -- 6.2 - PMS.VW_BI_PMS_ATRIB_DRE_MAT2
               ---------------------------------------------
               begin
                  mensagem              := mensagem || crlf || crlf ||
                                           '6.2 - PMS.VW_BI_PMS_ATRIB_DRE_MAT2';
                  bloco_upd             := '6 - PMS.VW_BI_PMS_ATRIB_DRE_MAT2 (flag 2 de 2)';
                  log_dt_carga_processo := sysdate;
                  --
                  select count(*)
                  into   contador1
                  from   pms.vw_bi_pms_atrib_dre_mat2
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
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
                  --REFRESH
                  --
                  dbms_mview.refresh('"PMS"."VW_BI_PMS_ATRIB_DRE_MAT2"');
                  --
                  --
                  select count(*)
                  into   contador2
                  from   pms.vw_bi_pms_atrib_dre_mat2
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
                  --LOG
                  descricao := 'FIM    - Linhas: ' || to_char(contador2);
                  -- LOG: As linhas carregadas foram menores que a original e este valor e maior ou igual a 1%? ERRO!
                  if (contador2 < contador1) and ((contador2 / contador1) < 0.99) then
                     status   := 'ERRO';
                     assunto  := '[ERRO%]';
                     sqlerro  := 'Variacao porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%';
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.VW_BI_PMS_ATRIB_DRE_MAT2  ##### - ' || crlf ||
                                 '       Variacao Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '       Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '       Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '       Diferenca: ' ||
                                 to_char(contador2 - contador1) || crlf ||
                                --   'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg || 'PMS.VW_BI_PMS_ATRIB_DRE_MAT2; ERRO: ' ||
                                sqlerro || '; Data: ' ||
                                to_char(sysdate, 'DD/MM/YYYY');
         /*            send_mail('noc-cit-sms@cit.com.br',
                               assunto || '[PMS][' || nome_carga ||
                               '] Resultado da Carga',
                               sms_msg);*/
                     --limpa msg
                     sms_msg := 'Objeto: ';
                  else
                     status   := 'OK';
                     mensagem := mensagem || crlf || crlf ||
                                 '[OK] PMS.VW_BI_PMS_ATRIB_DRE_MAT2' || crlf ||
                                 '     Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '     Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '     Diferenca: ' || to_char(contador2 - contador1) || crlf ||
                                 '     Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '     Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                  end if;
               exception
                  when others then
                     status   := 'ERRO';
                     assunto  := '[ERRO]';
                     sqlerro  := sqlerrm;
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.PMS.VW_BI_PMS_ATRIB_DRE_MAT2  ##### - ' || crlf ||
                                 '       SQL Erro: ' || sqlerrm || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg || 'PMS.PMS.VW_BI_PMS_ATRIB_DRE_MAT2; ERRO: ' ||
                                sqlerro || '; Data: ' ||
                                to_char(sysdate, 'DD/MM/YYYY');
         /*            send_mail('noc-cit-sms@cit.com.br',
                               assunto || '[PMS][' || nome_carga ||
                               '] Resultado da Carga',
                               sms_msg);*/
                     --limpa msg
                     sms_msg := 'Objeto: ';
               end;
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
            --  LOG - zerar info
            sqlerro   := '';
            contador1 := 0;
            contador2 := 0;
            dbms_lock.sleep(1);
            --------------------------------------------------------
            -- Atualizac?o da view de emprestimos de recurso
            --------------------------------------------------------
            --   1 - View de emprestimos de recurso
            --     1.1 - PMS.VW_BI_PMS_EMPREST_REC_CONS
            --     1.2 - PMS.VW_BI_PMS_EMPREST_REC
            --     1.3 - PMS.TB_BI_PMS_PRAT_DRE_MAT
            if (flag = 1 or flag = 99) then
               mensagem := mensagem || crlf || crlf ||
                           'Tipo da Carga: 1 - View de emprestimos de recurso';
               --     1.1 - PMS.tb_bi_pms_prat_dre_mat
               begin
                  bloco_upd             := '1 - PMS.tb_bi_pms_prat_dre_mat (flag 1 de 3)';
                  log_dt_carga_processo := sysdate;
                  --
                  select count(*)
                  into   contador1
                  from   pms.tb_bi_pms_prat_dre_mat
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
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
                  --
                  --
                  execute immediate 'truncate table tb_bi_pms_prat_dre_mat';
                  --
                  --
                  open sel_inf_pratica_dre;
                  --
                  fetch sel_inf_pratica_dre
                     into v_sel_inf_prat_dre;
                  --
                  while sel_inf_pratica_dre%found loop
                     insert into tb_bi_pms_prat_dre_mat
                     values
                        (v_sel_inf_prat_dre.cd_contrato_pratica,
                         v_sel_inf_prat_dre.nome_contrato_pratica,
                         v_sel_inf_prat_dre.lob_contrato_pratica,
                         v_sel_inf_prat_dre.dt_mes,
                         v_sel_inf_prat_dre.cd_moeda,
                         v_sel_inf_prat_dre.moeda,
                         v_sel_inf_prat_dre.valor_cotacao,
                         v_sel_inf_prat_dre.grupo_custo,
                         v_sel_inf_prat_dre.login,
                         v_sel_inf_prat_dre.qtd_fte,
                         v_sel_inf_prat_dre.valor_receita_curr,
                         v_sel_inf_prat_dre.valor_total_fte,
                         v_sel_inf_prat_dre.vlr_preco_medio_fte,
                         v_sel_inf_prat_dre.valor_preco_medio_fte_brl,
                         v_sel_inf_prat_dre.vlr_receita_ajustada_fte,
                         v_sel_inf_prat_dre.vlr_receita_ajust_fte_brl,
                         v_sel_inf_prat_dre.vlr_receita_clob,
                         v_sel_inf_prat_dre.vlr_rec_emprestimo_lob_brl,
                         v_sel_inf_prat_dre.pratica,
                         sysdate);
                     --
                     commit;
                     --
                     fetch sel_inf_pratica_dre
                        into v_sel_inf_prat_dre;
                  end loop;
                  --
                  close sel_inf_pratica_dre;
                  --
                  select count(*)
                  into   contador2
                  from   pms.tb_bi_pms_prat_dre_mat
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
                  descricao := 'FIM    - Linhas: ' || to_char(contador2);
                  -- LOG: As linhas carregadas foram menores que a original e este valor e maior ou igual a 1%? ERRO!
                  if (contador2 < contador1) and ((contador2 / contador1) < 0.99) then
                     status   := 'ERRO';
                     assunto  := '[ERRO%]';
                     sqlerro  := 'Variacao porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%';
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.tb_bi_pms_prat_dre_mat  ##### - ' || crlf ||
                                 '       Variacao Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '       Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '       Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '       Diferenca: ' ||
                                 to_char(contador2 - contador1) || crlf ||
                                --                          'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg || 'PMS.tb_bi_pms_prat_dre_mat; ERRO: ' ||
                                sqlerro || '; Data: ' ||
                                to_char(sysdate, 'DD/MM/YYYY');
         /*            send_mail('noc-cit-sms@cit.com.br',
                               assunto || '[PMS][' || nome_carga ||
                               '] Resultado da Carga',
                               sms_msg);*/
                     --limpa msg
                     sms_msg := 'Objeto: ';
                  else
                     status   := 'OK';
                     mensagem := mensagem || crlf || crlf ||
                                 '[OK] PMS.TB_BI_PMS_PRAT_DRE_MAT' || crlf ||
                                 '     Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '     Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '     Diferenca: ' || to_char(contador2 - contador1) || crlf ||
                                 '     Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '     Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                  end if;
               exception
                  when others then
                     status   := 'ERRO';
                     assunto  := '[ERRO]';
                     sqlerro  := sqlerrm;
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.tb_bi_pms_prat_dre_mat  ##### - ' || crlf ||
                                 '       SQL Erro: ' || sqlerrm || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg || 'PMS.TB_BI_PMS_PRAT_DRE_MAT; ERRO: ' ||
                                sqlerro || '; Data: ' ||
                                to_char(sysdate, 'DD/MM/YYYY');
                     /*send_mail('noc-cit-sms@cit.com.br',
                               assunto || '[PMS][' || nome_carga ||
                               '] Resultado da Carga',
                               sms_msg);*/
                     --limpa msg
                     sms_msg := 'Objeto: ';
               end;
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
               --  LOG - zerar info
               sqlerro   := '';
               contador1 := 0;
               contador2 := 0;
               dbms_lock.sleep(1);
               --------------------------------------------------
               --     1.2 - PMS.VW_BI_PMS_EMPREST_REC
               --------------------------------------------------
               begin
                  bloco_upd             := '1 - PMS.VW_BI_PMS_EMPREST_REC (flag 2 de 3)';
                  log_dt_carga_processo := sysdate;
                  --       SELECT COUNT(*)       INTO CONTADOR1        FROM ;
                  select count(*)
                  into   contador1
                  from   pms.vw_bi_pms_emprest_rec
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
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
                  -- REFRESH
                  dbms_mview.refresh('"PMS"."VW_BI_PMS_EMPREST_REC"');
                  --
                  select count(*)
                  into   contador2
                  from   pms.vw_bi_pms_emprest_rec
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
                  descricao := 'FIM    - Linhas: ' || to_char(contador2);
                  -- LOG: As linhas carregadas foram menores que a original e este valor e maior ou igual a 1%? ERRO!
                  if (contador2 < contador1) and ((contador2 / contador1) < 0.99) then
                     status   := 'ERRO';
                     assunto  := '[ERRO%]';
                     sqlerro  := 'Variacao porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%';
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.VW_BI_PMS_EMPREST_REC  ##### - ' || crlf ||
                                 '       Variacao Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '       Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '       Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '       Diferenca: ' ||
                                 to_char(contador2 - contador1) || crlf ||
                                --  'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg || 'PMS.VW_BI_PMS_EMPREST_REC; ERRO: ' ||
                                sqlerro || '; Data: ' ||
                                to_char(sysdate, 'DD/MM/YYYY');
         /*            send_mail('noc-cit-sms@cit.com.br',
                               assunto || '[PMS][' || nome_carga ||
                               '] Resultado da Carga',
                               sms_msg);*/
                     --limpa msg
                     sms_msg := 'Objeto: ';
                  else
                     status   := 'OK';
                     mensagem := mensagem || crlf || crlf ||
                                 '[OK] PMS.VW_BI_PMS_EMPREST_REC' || crlf ||
                                 '     Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '     Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '     Diferenca: ' || to_char(contador2 - contador1) || crlf ||
                                 '     Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '     Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                  end if;
               exception
                  when others then
                     status   := 'ERRO';
                     assunto  := '[ERRO]';
                     sqlerro  := sqlerrm;
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.VW_BI_PMS_EMPREST_REC  ##### - ' || crlf ||
                                 '       SQL Erro: ' || sqlerrm || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg || 'PMS.VW_BI_PMS_EMPREST_REC; ERRO: ' ||
                                sqlerro || '; Data: ' ||
                                to_char(sysdate, 'DD/MM/YYYY');
         /*            send_mail('noc-cit-sms@cit.com.br',
                               assunto || '[PMS][' || nome_carga ||
                               '] Resultado da Carga',
                               sms_msg);*/
                     --limpa msg
                     sms_msg := 'Objeto: ';
               end;
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
               --  LOG - zerar info
               sqlerro   := '';
               contador1 := 0;
               contador2 := 0;
               dbms_lock.sleep(1);
               --------------------------------------------------
               --     1.3 - PMS.TB_BI_PMS_PRAT_DRE_MAT
               --------------------------------------------------
               begin
                  bloco_upd             := '1 - PMS.VW_BI_PMS_EMPREST_REC_CONS (flag 3 de 3)';
                  log_dt_carga_processo := sysdate;
                  --
                  select count(*)
                  into   contador1
                  from   pms.vw_bi_pms_emprest_rec_cons
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
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
                  -- REFRESH
                  --
                  dbms_mview.refresh('"PMS"."VW_BI_PMS_EMPREST_REC_CONS"');
                  --
                  --
                  select count(*)
                  into   contador2
                  from   pms.vw_bi_pms_emprest_rec_cons
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
                  descricao := 'FIM    - Linhas: ' || to_char(contador2);
                  -- LOG: As linhas carregadas foram menores que a original e este valor e maior ou igual a 1%? ERRO!
                  if (contador2 < contador1) and ((contador2 / contador1) < 0.99) then
                     status   := 'ERRO';
                     assunto  := '[ERRO%]';
                     sqlerro  := 'Variacao porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%';
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.VW_BI_PMS_EMPREST_REC_CONS  ##### - ' || crlf ||
                                 '       Variacao Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '       Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '       Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '       Diferenca: ' ||
                                 to_char(contador2 - contador1) || crlf ||
                                -- 'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg || 'PMS.VW_BI_PMS_EMPREST_REC_CONS; ERRO: ' ||
                                sqlerro || '; Data: ' ||
                                to_char(sysdate, 'DD/MM/YYYY');
         /*            send_mail('noc-cit-sms@cit.com.br',
                               assunto || '[PMS][' || nome_carga ||
                               '] Resultado da Carga',
                               sms_msg);*/
                     --limpa msg
                     sms_msg := 'Objeto: ';
                  else
                     status   := 'OK';
                     mensagem := mensagem || crlf || crlf ||
                                 '[OK] PMS.VW_BI_PMS_EMPREST_REC_CONS' || crlf ||
                                 '     Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '     Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '     Diferenca: ' || to_char(contador2 - contador1) || crlf ||
                                 '     Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '     Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                  end if;
               exception
                  when others then
                     status   := 'ERRO';
                     assunto  := '[ERRO]';
                     sqlerro  := sqlerrm;
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.VW_BI_PMS_EMPREST_REC_CONS  ##### - ' || crlf ||
                                 '       SQL Erro: ' || sqlerrm || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg || 'PMS.VW_BI_PMS_EMPREST_REC_CONS; ERRO: ' ||
                                sqlerro || '; Data: ' ||
                                to_char(sysdate, 'DD/MM/YYYY');
         /*            send_mail('noc-cit-sms@cit.com.br',
                               assunto || '[PMS][' || nome_carga ||
                               '] Resultado da Carga',
                               sms_msg);*/
                     --limpa msg
                     sms_msg := 'Objeto: ';
               end;
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
            --  LOG - zerar info
            sqlerro   := '';
            contador1 := 0;
            contador2 := 0;
            dbms_lock.sleep(1);
            --
            -------------------------------------------------------------------------
            -- Atualizac?o da view de de custos fixos de licenca das consultorias
            -------------------------------------------------------------------------
            -- 2 - View de de custos fixos de licenca das consultorias
            --     2.1 - PMS.VW_BI_COSTF_LEAVE_UMKT_MAT
            if (flag = 2 or flag = 99) then
               mensagem := mensagem || crlf || crlf ||
                           'Tipo da Carga: 2 - View de de custos fixos de licenca das consultorias';
               begin
                  bloco_upd             := '2 - PMS.VW_BI_COSTF_LEAVE_UMKT_MAT';
                  log_dt_carga_processo := sysdate;
                  --
                  select count(*)
                  into   contador1
                  from   pms.vw_bi_costf_leave_umkt_mat
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
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
                  -- REFRESH
                  --
                  dbms_mview.refresh('"PMS"."VW_BI_COSTF_LEAVE_UMKT_MAT"');
                  --
                  --
                  select count(*)
                  into   contador2
                  from   pms.vw_bi_costf_leave_umkt_mat
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
                  descricao := 'FIM    - Linhas: ' || to_char(contador2);
                  -- LOG: As linhas carregadas foram menores que a original e este valor e maior ou igual a 1%? ERRO!
                  if (contador2 < contador1) and ((contador2 / contador1) < 0.99) then
                     status   := 'ERRO';
                     assunto  := '[ERRO%]';
                     sqlerro  := 'Variacao porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%';
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.VW_BI_COSTF_LEAVE_UMKT_MAT  ##### - ' || crlf ||
                                 '       Variacao Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '       Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '       Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '       Diferenca: ' ||
                                 to_char(contador2 - contador1) || crlf ||
                                -- 'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg || 'PMS.VW_BI_COSTF_LEAVE_UMKT_MAT; ERRO: ' ||
                                sqlerro || '; Data: ' ||
                                to_char(sysdate, 'DD/MM/YYYY');
         /*            send_mail('noc-cit-sms@cit.com.br',
                               assunto || '[PMS][' || nome_carga ||
                               '] Resultado da Carga',
                               sms_msg);*/
                     --limpa msg
                     sms_msg := 'Objeto: ';
                  else
                     status   := 'OK';
                     mensagem := mensagem || crlf || crlf ||
                                 '[OK] PMS.VW_BI_COSTF_LEAVE_UMKT_MAT' || crlf ||
                                 '     Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '     Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '     Diferenca: ' || to_char(contador2 - contador1) || crlf ||
                                 '     Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '     Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                  end if;
               exception
                  when others then
                     status   := 'ERRO';
                     assunto  := '[ERRO]';
                     sqlerro  := sqlerrm;
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.VW_BI_COSTF_LEAVE_UMKT_MAT  ##### - ' || crlf ||
                                 '       SQL Erro: ' || sqlerrm || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg || 'PMS.VW_BI_COSTF_LEAVE_UMKT_MAT; ERRO: ' ||
                                sqlerro || '; Data: ' ||
                                to_char(sysdate, 'DD/MM/YYYY');
         /*            send_mail('noc-cit-sms@cit.com.br',
                               assunto || '[PMS][' || nome_carga ||
                               '] Resultado da Carga',
                               sms_msg);*/
                     --limpa msg
                     sms_msg := 'Objeto: ';
               end;
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
            --  LOG - zerar info
            sqlerro   := '';
            contador1 := 0;
            contador2 := 0;
            dbms_lock.sleep(1);
            --
            --------------------------------------------------------------------------
            -- Atualizac?o da view de custos fixos das praticas
            --------------------------------------------------------------------------
            --   5 -  Atualizac?o da view de custos fixos das praticas
            --     5.1 - PMS.VW_BI_PMS_CUSTFIX_PRAT_DRE_MAT
            if (flag = 5 or flag = 99) then
               mensagem := mensagem || crlf || crlf ||
                           'Tipo da Carga: 5 - Atualizac?o da view de custos fixos das praticas';
               begin
                  bloco_upd             := '5 - PMS.VW_BI_PMS_CUSTFIX_PRAT_DRE_MAT';
                  log_dt_carga_processo := sysdate;
                  --
                  select count(*)
                  into   contador1
                  from   pms.vw_bi_pms_custfix_prat_dre_mat
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
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
                  -- REFRESH
                  --
                  dbms_mview.refresh('"PMS"."VW_BI_PMS_CUSTFIX_PRAT_DRE_MAT"');
                  --
                  --
                  select count(*)
                  into   contador2
                  from   pms.vw_bi_pms_custfix_prat_dre_mat
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
                  descricao := 'FIM    - Linhas: ' || to_char(contador2);
                  -- LOG: As linhas carregadas foram menores que a original e este valor e maior ou igual a 1%? ERRO!
                  if (contador2 < contador1) and ((contador2 / contador1) < 0.99) then
                     status   := 'ERRO';
                     assunto  := '[ERRO%]';
                     sqlerro  := 'Variacao porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%';
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.VW_BI_PMS_CUSTFIX_PRAT_DRE_MAT  ##### - ' || crlf ||
                                 '       Variacao Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '       Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '       Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '       Diferenca: ' ||
                                 to_char(contador2 - contador1) || crlf ||
                                --  'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg ||
                                'PMS.VW_BI_PMS_CUSTFIX_PRAT_DRE_MAT; ERRO: ' ||
                                sqlerro || '; Data: ' ||
                                to_char(sysdate, 'DD/MM/YYYY');
         /*            send_mail('noc-cit-sms@cit.com.br',
                               assunto || '[PMS][' || nome_carga ||
                               '] Resultado da Carga',
                               sms_msg);*/
                     --limpa msg
                     sms_msg := 'Objeto: ';
                  else
                     status   := 'OK';
                     mensagem := mensagem || crlf || crlf ||
                                 '[OK] PMS.VW_BI_PMS_CUSTFIX_PRAT_DRE_MAT' || crlf ||
                                 '     Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '     Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '     Diferenca: ' || to_char(contador2 - contador1) || crlf ||
                                 '     Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '     Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                  end if;
               exception
                  when others then
                     status   := 'ERRO';
                     assunto  := '[ERRO]';
                     sqlerro  := sqlerrm;
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.VW_BI_PMS_CUSTFIX_PRAT_DRE_MAT  ##### - ' || crlf ||
                                 '       SQL Erro: ' || sqlerrm || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg ||
                                'PMS.VW_BI_PMS_CUSTFIX_PRAT_DRE_MAT; ERRO: ' ||
                                sqlerro || '; Data: ' ||
                                to_char(sysdate, 'DD/MM/YYYY');
         /*            send_mail('noc-cit-sms@cit.com.br',
                               assunto || '[PMS][' || nome_carga ||
                               '] Resultado da Carga',
                               sms_msg);*/
                     --limpa msg
                     sms_msg := 'Objeto: ';
               end;
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
            --  LOG - zerar info
            sqlerro   := '';
            contador1 := 0;
            contador2 := 0;
            dbms_lock.sleep(1);
            --
            --------------------------------------------------------------
            --  Atualizac?o das informac?es de emprestimos de recurso.
            --------------------------------------------------------------
            --  3 -Atualizac?o das informac?es de emprestimos de recurso
            --     3.1 - PMS.TMP_BI_TOTCUS_EMPREC
            --     3.2 - PMS.tmp_bi_custind_emprec
            --     3.3 - PMS.tb_bi_pms_custo_emp_rec_dre
            if (flag = 3 or flag = 99) then
               mensagem := mensagem || crlf || crlf ||
                           'Tipo da Carga: 3 - Atualizac?o das informac?es de emprestimos de recurso ';
               --------------------------------------------
               --     3.1 - PMS.TMP_BI_TOTCUS_EMPREC
               --------------------------------------------
               begin
                  bloco_upd             := '3 - PMS.TMP_BI_TOTCUS_EMPREC (frag 1 de 3)';
                  log_dt_carga_processo := sysdate;
                  --
                  select count(*)
                  into   contador1
                  from   pms.tmp_bi_totcus_emprec
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
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
                  execute immediate 'truncate table tmp_bi_totcus_emprec';
                  --
                  open sel_inform;
                  --
                  fetch sel_inform
                     into v_sel_inform;
                  --
                  while sel_inform%found loop
                     --
                     insert into tmp_bi_totcus_emprec
                        (cd_contrato_pratica,
                         nome_contrato_pratica,
                         cd_moeda,
                         moeda,
                         dt_mes,
                         lob_contrato_pratica,
                         valor_total_fte,
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
                         valor_total_custo_curr,
                         valor_total_custo_brl,
                         data_ult_atualizacao)
                     values
                        (v_sel_inform.cd_contrato_pratica,
                         v_sel_inform.nome_contrato_pratica,
                         v_sel_inform.cd_moeda,
                         v_sel_inform.moeda,
                         v_sel_inform.dt_mes,
                         v_sel_inform.lob_contrato_pratica,
                         v_sel_inform.valor_total_fte,
                         v_sel_inform.valor_custo_dir_tce_curr,
                         v_sel_inform.valor_custo_dir_tce_brl,
                         v_sel_inform.valor_custo_dir_infra_curr,
                         v_sel_inform.valor_custo_dir_infra_brl,
                         v_sel_inform.valor_custo_ind_tce_curr,
                         v_sel_inform.valor_custo_ind_tce_brl,
                         v_sel_inform.valor_custo_ind_infra_curr,
                         v_sel_inform.valor_custo_ind_infra_brl,
                         v_sel_inform.valor_hora_extra,
                         v_sel_inform.valor_plantao,
                         v_sel_inform.valor_total_custo_curr,
                         v_sel_inform.valor_total_custo_brl,
                         sysdate);
                     --
                     commit;
                     --
                     fetch sel_inform
                        into v_sel_inform;
                  end loop;
                  --
                  close sel_inform;
                  --LOG
                  --
                  select count(*)
                  into   contador2
                  from   pms.tmp_bi_totcus_emprec
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
                  descricao := 'FIM    - Linhas: ' || to_char(contador2);
                  -- LOG: As linhas carregadas foram menores que a original e este valor e maior ou igual a 1%? ERRO!
                  if (contador2 < contador1) and ((contador2 / contador1) < 0.99) then
                     status   := 'ERRO';
                     assunto  := '[ERRO%]';
                     sqlerro  := 'Variacao porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%';
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.TMP_BI_TOTCUS_EMPREC  ##### - ' || crlf ||
                                 '       Variacao Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '       Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '       Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '       Diferenca: ' ||
                                 to_char(contador2 - contador1) || crlf ||
                                --  'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg || 'PMS.TMP_BI_TOTCUS_EMPREC; ERRO: ' ||
                                sqlerro || '; Data: ' ||
                                to_char(sysdate, 'DD/MM/YYYY');
         /*            send_mail('noc-cit-sms@cit.com.br',
                               assunto || '[PMS][' || nome_carga ||
                               '] Resultado da Carga',
                               sms_msg);*/
                     --limpa msg
                     sms_msg := 'Objeto: ';
                  else
                     status   := 'OK';
                     mensagem := mensagem || crlf || crlf ||
                                 '[OK] PMS.TMP_BI_TOTCUS_EMPREC' || crlf ||
                                 '     Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '     Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '     Diferenca: ' || to_char(contador2 - contador1) || crlf ||
                                 '     Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '     Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                  end if;
               exception
                  when others then
                     status   := 'ERRO';
                     assunto  := '[ERRO]';
                     sqlerro  := sqlerrm;
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.TMP_BI_TOTCUS_EMPREC  ##### - ' || crlf ||
                                 '       SQL Erro: ' || sqlerrm || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg || 'PMS.TMP_BI_TOTCUS_EMPREC; ERRO: ' ||
                                sqlerro || '; Data: ' ||
                                to_char(sysdate, 'DD/MM/YYYY');
         /*            send_mail('noc-cit-sms@cit.com.br',
                               assunto || '[PMS][' || nome_carga ||
                               '] Resultado da Carga',
                               sms_msg);*/
                     --limpa msg
                     sms_msg := 'Objeto: ';
               end;
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
               --  LOG - zerar info
               sqlerro   := '';
               contador1 := 0;
               contador2 := 0;
               dbms_lock.sleep(1);
               --------------------------------------------
               --     3.2 - PMS.tmp_bi_custind_emprec
               --------------------------------------------
               begin
                  bloco_upd             := '3 - PMS.tmp_bi_custind_emprec (frag 2 de 3)';
                  log_dt_carga_processo := sysdate;
                  --
                  select count(*)
                  into   contador1
                  from   pms.tmp_bi_custind_emprec
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
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
                  --
                  --
                  execute immediate 'truncate table tmp_bi_custind_emprec';
                  --
                  --
                  open sel_inform2;
                  --
                  fetch sel_inform2
                     into v_sel_inform2;
                  --
                  while sel_inform2%found loop
                     --
                     insert into tmp_bi_custind_emprec
                     values
                        (v_sel_inform2.cd_pessoa,
                         v_sel_inform2.cd_contrato_pratica,
                         v_sel_inform2.dt_mes,
                         v_sel_inform2.cd_moeda,
                         v_sel_inform2.moeda,
                         v_sel_inform2.login,
                         v_sel_inform2.valor_total_fte_ind,
                         v_sel_inform2.valor_total_custo_curr,
                         v_sel_inform2.valor_total_custo_brl,
                         v_sel_inform2.cd_grupo_custo,
                         v_sel_inform2.cd_periodo,
                         v_sel_inform2.lob_pessoa,
                         sysdate);
                     --
                     commit;
                     --
                     fetch sel_inform2
                        into v_sel_inform2;
                  end loop;
                  --
                  close sel_inform2;
                  --
                  select count(*)
                  into   contador2
                  from   pms.tmp_bi_custind_emprec
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
                  descricao := 'FIM    - Linhas: ' || to_char(contador2);
                  -- LOG: As linhas carregadas foram menores que a original e este valor e maior ou igual a 1%? ERRO!
                  if (contador2 < contador1) and ((contador2 / contador1) < 0.99) then
                     status   := 'ERRO';
                     assunto  := '[ERRO%]';
                     sqlerro  := 'Variacao porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%';
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.tmp_bi_custind_emprec  ##### - ' || crlf ||
                                 '       Variacao Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '       Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '       Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '       Diferenca: ' ||
                                 to_char(contador2 - contador1) || crlf ||
                                -- 'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg || 'PMS.tmp_bi_custind_emprec; ERRO: ' ||
                                sqlerro || '; Data: ' ||
                                to_char(sysdate, 'DD/MM/YYYY');
         /*            send_mail('noc-cit-sms@cit.com.br',
                               assunto || '[PMS][' || nome_carga ||
                               '] Resultado da Carga',
                               sms_msg);*/
                     --limpa msg
                     sms_msg := 'Objeto: ';
                  else
                     status   := 'OK';
                     mensagem := mensagem || crlf || crlf ||
                                 '[OK] PMS.tmp_bi_custind_emprec' || crlf ||
                                 '     Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '     Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '     Diferenca: ' || to_char(contador2 - contador1) || crlf ||
                                 '     Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '     Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                  end if;
               exception
                  when others then
                     status   := 'ERRO';
                     assunto  := '[ERRO]';
                     sqlerro  := sqlerrm;
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.tmp_bi_custind_emprec  ##### - ' || crlf ||
                                 '       SQL Erro: ' || sqlerrm || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg || 'PMS.tmp_bi_custind_emprec; ERRO: ' ||
                                sqlerro || '; Data: ' ||
                                to_char(sysdate, 'DD/MM/YYYY');
         /*            send_mail('noc-cit-sms@cit.com.br',
                               assunto || '[PMS][' || nome_carga ||
                               '] Resultado da Carga',
                               sms_msg);*/
                     --limpa msg
                     sms_msg := 'Objeto: ';
               end;
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
               --  LOG - zerar info
               sqlerro   := '';
               contador1 := 0;
               contador2 := 0;
               dbms_lock.sleep(1);
               --------------------------------------------
               --     3.3 - PMS.tb_bi_pms_custo_emp_rec_dre
               --------------------------------------------
               begin
                  bloco_upd             := '3 - PMS.tb_bi_pms_custo_emp_rec_dre (frag 3 de 3)';
                  log_dt_carga_processo := sysdate;
                  --
                  select count(*)
                  into   contador1
                  from   pms.tb_bi_pms_custo_emp_rec_dre
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
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
                  --
                  --
                  execute immediate 'truncate table tb_bi_pms_custo_emp_rec_dre';
                  --
                  --
                  insert into tb_bi_pms_custo_emp_rec_dre
                     select cd_contrato_pratica,
                            nome_contrato_pratica,
                            dt_mes,
                            cd_moeda,
                            moeda,
                            0 valor_cotacao,
                            login,
                            lob_contrato,
                            valor_custind_curr_pos,
                            valor_custind_brl_pos,
                            lob_pessoa,
                            valor_custind_curr_neg,
                            valor_custind_brl_neg,
                            flag,
                            0 valor_hr_extra_emp_rec_curr,
                            0 valor_hr_extra_emp_rec_brl,
                            0 valor_plantao_emp_rec_curr,
                            0 valor_plantao_emp_rec_brl,
                            sysdate,
                            pdr.valor_total_fte,
                            pdr.valor_total_fte_ind
                     from   vw_bi_pms_custo_emp_rec_dre pdr;
                  --
                  commit;
                  --LOG
                  --
                  select count(*)
                  into   contador2
                  from   pms.tb_bi_pms_custo_emp_rec_dre
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
                  descricao := 'FIM    - Linhas: ' || to_char(contador2);
                  -- LOG: As linhas carregadas foram menores que a original e este valor e maior ou igual a 1%? ERRO!
                  if (contador2 < contador1) and ((contador2 / contador1) < 0.99) then
                     status   := 'ERRO';
                     assunto  := '[ERRO%]';
                     sqlerro  := 'Variacao porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%';
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.tb_bi_pms_custo_emp_rec_dre  ##### - ' || crlf ||
                                 '       Variacao Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '       Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '       Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '       Diferenca: ' ||
                                 to_char(contador2 - contador1) || crlf ||
                                -- 'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg || 'PMS.tb_bi_pms_custo_emp_rec_dre; ERRO: ' ||
                                sqlerro || '; Data: ' ||
                                to_char(sysdate, 'DD/MM/YYYY');
         /*            send_mail('noc-cit-sms@cit.com.br',
                               assunto || '[PMS][' || nome_carga ||
                               '] Resultado da Carga',
                               sms_msg);*/
                     --limpa msg
                     sms_msg := 'Objeto: ';
                  else
                     status   := 'OK';
                     mensagem := mensagem || crlf || crlf ||
                                 '[OK] PMS.tb_bi_pms_custo_emp_rec_dre' || crlf ||
                                 '     Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '     Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '     Diferenca: ' || to_char(contador2 - contador1) || crlf ||
                                 '     Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '     Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                  end if;
               exception
                  when others then
                     status   := 'ERRO';
                     assunto  := '[ERRO]';
                     sqlerro  := sqlerrm;
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.tb_bi_pms_custo_emp_rec_dre  ##### - ' || crlf ||
                                 '       SQL Erro: ' || sqlerrm || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg || 'PMS.tb_bi_pms_custo_emp_rec_dre; ERRO: ' ||
                                sqlerro || '; Data: ' ||
                                to_char(sysdate, 'DD/MM/YYYY');
         /*            send_mail('noc-cit-sms@cit.com.br',
                               assunto || '[PMS][' || nome_carga ||
                               '] Resultado da Carga',
                               sms_msg);*/
                     --limpa msg
                     sms_msg := 'Objeto: ';
               end;
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
               --  LOG - zerar info
               sqlerro   := '';
               contador1 := 0;
               contador2 := 0;
               dbms_lock.sleep(1);
            end if;
            --
            --------------------------------------------------------------------------
            -- Carga das informac?es de Custos Indiretos, para a DRE das Praticas
            --------------------------------------------------------------------------
            --4 -Carga das informac?es de Custos Indiretos, para a DRE das Praticas
            --     4.1 - PMS.tb_bi_pms_custos_ind_dre
            if (flag = 4 or flag = 99) then
               mensagem := mensagem || crlf || crlf ||
                           'Tipo da Carga: 4 - Carga das informac?es de Custos Indiretos, para a DRE das Praticas';
               --LOG
               begin
                  bloco_upd             := '4 - PMS.tb_bi_pms_custos_ind_dre';
                  log_dt_carga_processo := sysdate;
                  --
                  select count(*)
                  into   contador1
                  from   pms.tb_bi_pms_custos_ind_dre
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
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
                  --
                  --
                  execute immediate 'truncate table tb_bi_pms_custos_ind_dre';
                  --
                  --
                  open sel_inform_cind;
                  --
                  fetch sel_inform_cind
                     into v_sel_inf_cind;
                  --
                  while sel_inform_cind%found loop
                     insert into tb_bi_pms_custos_ind_dre
                     values
                        (v_sel_inf_cind.fonte,
                         v_sel_inf_cind.sub_fonte,
                         v_sel_inf_cind.cd_pessoa,
                         v_sel_inf_cind.categoria,
                         v_sel_inf_cind.login,
                         v_sel_inf_cind.dt_mes,
                         v_sel_inf_cind.dt_admissao,
                         v_sel_inf_cind.dt_rescisao,
                         v_sel_inf_cind.cd_moeda,
                         v_sel_inf_cind.moeda,
                         v_sel_inf_cind.valor_cotacao,
                         v_sel_inf_cind.cd_contrato_pratica,
                         v_sel_inf_cind.nome_contrato_pratica,
                         v_sel_inf_cind.pratica,
                         v_sel_inf_cind.perc_alocavel,
                         v_sel_inf_cind.perc_alocacao,
                         v_sel_inf_cind.valor_tce,
                         v_sel_inf_cind.valor_tce_brl,
                         v_sel_inf_cind.vlr_custo_infra_base,
                         v_sel_inf_cind.vlr_custo_infra_base_brl,
                         v_sel_inf_cind.pct_ferias,
                         v_sel_inf_cind.pct_licenca,
                         v_sel_inf_cind.pct_distrib,
                         v_sel_inf_cind.valor_fte_cind,
                         v_sel_inf_cind.valor_custo_ind_tce_curr,
                         v_sel_inf_cind.valor_custo_ind_tce_brl,
                         v_sel_inf_cind.valor_custo_ind_infra_curr,
                         v_sel_inf_cind.valor_custo_ind_infra_brl,
                         v_sel_inf_cind.umkt,
                         sysdate,
                         v_sel_inf_cind.VALOR_CUSTO_IND_CHBK_CURR,
                         v_sel_inf_cind.VALOR_CUSTO_IND_CHBK_BRL,
                         v_sel_inf_cind.VALOR_CUSTO_IND_CHBK_USD
                         );
                     --
                     commit;
                     --
                     fetch sel_inform_cind
                        into v_sel_inf_cind;
                  end loop;
                  --
                  close sel_inform_cind;
                  --
                  declare
                         cursor sel_inform is
                                select fonte,
                                       login,
                                       dt_mes,
                                       nome_contrato_pratica,
                                       cd_moeda,
                                       valor_cotacao,
                                       cd_moeda_infra,
                                       valor_cotacao_infra,
                                       valor_custo_ind_infra_curr,
                                       valor_custo_ind_infra_brl
                                from vw_bi_pms_conf_infra_cind;
                         --
                         v_sel_inform sel_inform%rowtype;
                  begin
                       open sel_inform;
                       --
                       fetch sel_inform into v_sel_inform;
                       --
                       while sel_inform%found loop
                             update tb_bi_pms_custos_ind_dre
                             set valor_custo_ind_infra_brl = v_sel_inform.valor_custo_ind_infra_curr * v_sel_inform.valor_cotacao_infra
                             where login  = v_sel_inform.login and
                                   dt_mes = v_sel_inform.dt_mes AND
                                   nome_contrato_pratica = v_sel_inform.nome_contrato_pratica;
                             --
                             commit;
                             --
                             fetch sel_inform into v_sel_inform;
                       end loop;
                       --
                       close sel_inform;
                  end;
                  --                  
                  select count(*)
                  into   contador2
                  from   pms.tb_bi_pms_custos_ind_dre
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
                  descricao := 'FIM    - Linhas: ' || to_char(contador2);
                  -- LOG: As linhas carregadas foram menores que a original e este valor e maior ou igual a 1%? ERRO!
                  if (contador2 < contador1) and ((contador2 / contador1) < 0.99) then
                     status   := 'ERRO';
                     assunto  := '[ERRO%]';
                     sqlerro  := 'Variacao porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%';
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.tb_bi_pms_custos_ind_dre  ##### - ' || crlf ||
                                 '       Variacao Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '       Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '       Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '       Diferenca: ' ||
                                 to_char(contador2 - contador1) || crlf ||
                                -- 'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg ||'PMS.tb_bi_pms_custos_ind_dre; ERRO: '|| sqlerro ||'; Data: '||to_char(sysdate, 'DD/MM/YYYY');
         --            send_mail('noc-cit-sms@cit.com.br', assunto||'[PMS]['||NOME_CARGA||'] Resultado da Carga', sms_msg);
                     --limpa msg
                     sms_msg := 'Objeto: ';
                  else
                     status   := 'OK';
                     mensagem := mensagem || crlf || crlf ||
                                 '[OK] PMS.tb_bi_pms_custos_ind_dre' || crlf ||
                                 '     Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '     Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '     Diferenca: ' || to_char(contador2 - contador1) || crlf ||
                                 '     Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '     Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                  end if;
               exception
                  when others then
                     status   := 'ERRO';
                     assunto  := '[ERRO]';
                     sqlerro  := sqlerrm;
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.tb_bi_pms_custos_ind_dre  ##### - ' || crlf ||
                                 '       SQL Erro: ' || sqlerrm || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg || 'tb_bi_pms_custos_ind_dre; ERRO: ' ||
                                sqlerro || '; Data: ' ||
                                to_char(sysdate, 'DD/MM/YYYY');
           /*          send_mail('noc-cit-sms@cit.com.br',
                               assunto || '[PMS][' || nome_carga ||
                               '] Resultado da Carga',
                               sms_msg);
           */          --limpa msg
                     sms_msg := 'Objeto: ';
               end;
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
               --  LOG - zerar info
               sqlerro   := '';
               contador1 := 0;
               contador2 := 0;
               dbms_lock.sleep(1);
               ------------------------------------
               --  LOG - zerar info
               sqlerro   := '';
               contador1 := 0;
               contador2 := 0;
               dbms_lock.sleep(1);
            end if;
            --
            --------------------------------------------------------------------------
            --7 -Carga das informac?es de Histórico de Base de Colaborador
            --------------------------------------------------------------------------
            if (flag = 7 or flag = 99) then
               mensagem := mensagem || crlf || crlf ||
                           'Tipo da Carga: 7 - Carga do Histórico de Base de Colaborador';
               --LOG
               begin
                  bloco_upd             := '7 - PMS.tb_bi_pms_hist_base_colab';
                  log_dt_carga_processo := sysdate;
                  --
                  select count(*)
                  into   contador1
                  from   pms.tb_bi_pms_hist_base_colab
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
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
                 
         
               USP_PMS_ATUALIZA_PESS_CID_BASE;          
                  --
                  --
                  execute immediate 'truncate table tb_bi_pms_hist_base_colab';
                  --
                  --
                  open sel_inform_cind;
                  --
                  insert into tb_bi_pms_hist_base_colab
                       select * from  vw_bi_pms_hist_base_colab
                       
/*                  select cib.dt_mes,
                            pcib.login,
                            pcib.cd_pessoa,
                            cib.cd_cidade_base,
                            cib.valor_custo valor_custo_infra_base,
                            sigla_cidade_base
                     from ( select cib.cuib_dt_mes             dt_mes,
                                   cib.cuib_vl_custo_mes_fte   valor_custo,
                                   cib.ciba_cd_cidade_base     cd_cidade_base
                            from pms.custo_infra_base cib ) cib,
                          ( select pcb.ciba_cd_cidade_base   cd_cidade_base,
                                   cib.taal_sg_cidade_base   sigla_cidade_base,
                                   pcb.pess_cd_pessoa        cd_pessoa,
                                   pe.pess_cd_login          login,
                                   pcb.pecb_dt_inicio        dt_inicio,
                                   case when pcb.pecb_dt_fim is not null
                                             then pcb.pecb_dt_fim
                                             else ( select para_dt_parametro
                                                    from pms.parametro
                                                  where para_cd_parametro = 1 ) end dt_fim
                          from pms.pessoa_cidade_base pcb,
                               pms.pessoa pe,
                               pms.cidade_base cib
                          where pcb.pess_cd_pessoa = pe.pess_cd_pessoa and
                                pcb.ciba_cd_cidade_base = cib.ciba_cd_cidade_base  ) pcib
                     where cib.cd_cidade_base = pcib.cd_cidade_base and
                         cib.dt_mes         between pcib.dt_inicio and pcib.dt_fim;*/
                     --
                     commit;
         
                  --
                  select count(*)
                  into   contador2
                  from   pms.tb_bi_pms_hist_base_colab
                  where  dt_mes <= (select modu_dt_fechamento
                                    from   modulo
                                    where  modu_cd_modulo = 2);
                  descricao := 'FIM    - Linhas: ' || to_char(contador2);
                  -- LOG: As linhas carregadas foram menores que a original e este valor e maior ou igual a 1%? ERRO!
                  if (contador2 < contador1) and ((contador2 / contador1) < 0.99) then
                     status   := 'ERRO';
                     assunto  := '[ERRO%]';
                     sqlerro  := 'Variacao porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%';
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.tb_bi_pms_custos_ind_dre  ##### - ' || crlf ||
                                 '       Variacao Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '       Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '       Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '       Diferenca: ' ||
                                 to_char(contador2 - contador1) || crlf ||
                                -- 'Porcentagem: ' ||round(((1-(contador2/contador1))*100),2) ||'%' || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg ||'PMS.tb_bi_pms_custos_ind_dre; ERRO: '|| sqlerro ||'; Data: '||to_char(sysdate, 'DD/MM/YYYY');
         --            send_mail('noc-cit-sms@cit.com.br', assunto||'[PMS]['||NOME_CARGA||'] Resultado da Carga', sms_msg);
                     --limpa msg
                     sms_msg := 'Objeto: ';
                  else
                     status   := 'OK';
                     mensagem := mensagem || crlf || crlf ||
                                 '[OK] PMS.tb_bi_pms_hist_base_colab' || crlf ||
                                 '     Linhas antes do Refresh: ' ||
                                 to_char(contador1) || crlf ||
                                 '     Linhas apos Refresh: ' || to_char(contador2) || crlf ||
                                 '     Diferenca: ' || to_char(contador2 - contador1) || crlf ||
                                 '     Porcentagem: ' ||
                                 round(((-1 * (1 - (contador2 / contador1))) * 100), 2) || '%' || crlf ||
                                 '     Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                  end if;
               exception
                  when others then
                     status   := 'ERRO';
                     assunto  := '[ERRO]';
                     sqlerro  := sqlerrm;
                     mensagem := mensagem || crlf || crlf ||
                                 '[ERRO] ##### PMS.tb_bi_pms_hist_base_colab  ##### - ' || crlf ||
                                 '       SQL Erro: ' || sqlerrm || crlf ||
                                 '       Data Execuc?o: ' ||
                                 to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
                     --envio alerta SMS INFRA
                     sms_msg := sms_msg || 'tb_bi_pms_custos_ind_dre; ERRO: ' ||
                                sqlerro || '; Data: ' ||
                                to_char(sysdate, 'DD/MM/YYYY');
           /*          send_mail('noc-cit-sms@cit.com.br',
                               assunto || '[PMS][' || nome_carga ||
                               '] Resultado da Carga',
                               sms_msg);
           */          --limpa msg
                     sms_msg := 'Objeto: ';
               end;
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
               --  LOG - zerar info
               sqlerro   := '';
               contador1 := 0;
               contador2 := 0;
               dbms_lock.sleep(1);
               ------------------------------------
            end if;
            --
            if ( flag = 8 or flag = 99 ) then
               execute immediate 'truncate table tb_bi_pms_hext_plantao';
               --
               insert into tb_bi_pms_hext_plantao
               select fonte,
                      pess_cd_pessoa,
                      pess_cd_login,
                      pess_pr_alocavel,
                      status,
                      cupm_cd_custo_pessoa_mes,
                      dt_mes,
                      valor_salario,
                      valor_jornada,
                      origem,
                      vlr_salario_jornada,
                      cupc_pr_apropriado,
                      cupc_nr_horas,
                      cupc_vl_fator,
                      valor_custo_cp,
                      cd_contrato_pratica,
                      nome_contrato_pratica,
                      pratica,
                      cd_tipo_contrato,
                      valor_hora_extra,
                      valor_plantao,
                      umkt
               from vw_bi_pms_hext_plantao;
               --
               commit;
            end if;
  else
         BLOCO_UPD := 'IF DT_FECHMNTO MODULO = 1';
         DESCRICAO:= 'ATENCAO - TABELA MODULO NAO CONTEM DATA FECHAMENTO DO MES ANTERIOR!';
         STATUS:='ERRO';
         SQLERRO:= 'DATA FECHAMENTO DO MODULO DO MES ANTERIOR NAO EXISTE!';
         assunto  := '[ERRO]';
         LOG_DT_CARGA_PROCESSO := sysdate;
         insert into TB_BI_PMS_LOG_CARGAS(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc)
         values(log_data,log_dt_carga_processo,nome_carga,bloco_upd,descricao,status,sqlerro,nome_proc);
         commit;         
         mensagem := mensagem || crlf || crlf ||'[ERRO] ##### MODULO - Check Data FECHAMENTO ##### - ' 
                              || crlf || '       Data Execução: ' || to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS');
            --send_mail('lahumada@cit.com.br', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);
            send_mail('alexandrel@cit.com.br', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);
            send_mail('lnoboru@cit.com.br', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);    
           
            send_mail('tspadari@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);    
            send_mail('llino@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);    
            send_mail('andreiap@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);               
                    
          --  send_mail('it_oncall@ciandt.com', assunto|| ' [PMS][' || nome_carga || '] Check Data Admissao - PMS.PESSOA', mensagem);
          --  sms_msg := sms_msg || 'pms.pessoa - Data Admissao is Null';
          --  send_mail('noc-cit-sms@cit.com.br',assunto || '[PMS][' || nome_carga ||']  Check Data Admissao - PMS.PESSOA',sms_msg);
   end if;  

   mensagem := mensagem || crlf || crlf || '=== Fim da Carga ' ||
               to_char(sysdate, 'DD/MM/YYYY HH24:MI:SS') || ' ===';
   mensagem := mensagem || crlf ||
               'select * from pms.TB_BI_PMS_LOG_CARGAS where NOME_CARGA = '''|| nome_carga ||''' order by 2 desc';
   mensagem := mensagem || crlf || 'FLAG: ' || flag;
   assunto  := assunto || ' [PMS][' || nome_carga || '] Resultado da Carga';
   send_mail('lnoboru@ciandt.com', assunto, mensagem);   
   --send_mail('lahumada@cit.com.br', assunto, mensagem);
   send_mail('alexandrel@cit.com.br', assunto, mensagem);
  
   send_mail('tspadari@ciandt.com', assunto, mensagem);
   send_mail('llino@ciandt.com', assunto, mensagem);
   send_mail('andreiap@ciandt.com', assunto, mensagem);
    
   

      if status = 'ERRO' then
               mensagem := 'ATTENTION: Please, contact Ciandt IT Business Intelligence Team as soon as possible. '|| crlf || crlf || mensagem;
  --             send_mail('infra_ti@cit.com.br', assunto, mensagem);
      end if;
end USP_PMS_praticas;