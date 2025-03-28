create or replace procedure USP_PMS_ATUALIZA_ESTRATIFIC_UR(p_result out number) is

--constants
c_cd_motivo constant NUMBER(18) := 5; --Undefined
c_subject   constant varchar2(30) := '[PMS] UR Review Updates';
--c_crlf varchar2(2) := chr(13) || chr(10);

--vari�veis
v_msg         varchar2(1000);
--v_error_text  varchar2(500);

--cursores
cursor crs_estrat_ur is
select vw.esur_cd_estratificacao_ur cd_estrat_ur,
       vw.esur_cd_item_estrat_ur cd_item_estrat_ur,
       vw.esur_dt_mes dt_mes,
       vw.esur_vl_diferenca_real vl_dif_real,
       vw.esur_vl_diferenca_review vl_dif_review,
       vw.esur_vl_total_alocado vl_total_aloc,
       vw.esur_vl_total_receitado vl_total_rec,
       vw.esur_cd_pessoa cd_pessoa,
       p.pess_tx_email tx_email_criador,
       cp.copr_nm_contrato_pratica nm_contrato_pratica
from vw_estratificacao_ur_resultado vw,
     estratificacao_ur eu,
     pessoa p,
     contrato_pratica cp
where vw.esur_cd_estratificacao_ur = eu.esur_cd_estratificacao_ur
  and eu.esur_cd_login_criador = p.pess_cd_login
  and vw.esur_cd_contrato_pratica = cp.copr_cd_contrato_pratica;

begin

  begin

    for reg in crs_estrat_ur loop
        if (reg.cd_item_estrat_ur is null and reg.vl_dif_real < 0) then
            insert into item_estratificacao_ur
                (iteu_cd_item_estrat_ur, esur_cd_estratificacao_ur, pess_cd_pessoa, exde_cd_motivo_desvio, iteu_nr_fte_alocado, iteu_nr_fte_receitado, iteu_vl_diferenca)
            values
                (sq_iteu_cd_item_estrat_ur.nextval, reg.cd_estrat_ur, reg.cd_pessoa, c_cd_motivo, reg.vl_total_aloc, reg.vl_total_rec, reg.vl_dif_real);
                 
            update estratificacao_ur
               set esur_dt_atualizacao = sysdate
             where esur_cd_estratificacao_ur = reg.cd_estrat_ur;
             
            v_msg := 'The UR Review for C-Lob ' || reg.nm_contrato_pratica || ' on ' || to_char(reg.dt_mes,'mm/yyyy') || ' must be updated.';
             
            USP_SEND_MAIL(reg.tx_email_criador, c_subject, v_msg);

        elsif (reg.vl_dif_real < reg.vl_dif_review and reg.vl_dif_real < 0) then
            update item_estratificacao_ur
               set iteu_nr_fte_alocado = reg.vl_total_aloc,
                   iteu_nr_fte_receitado = reg.vl_total_rec,
                   iteu_vl_diferenca = reg.vl_dif_real,
                   exde_cd_motivo_desvio = c_cd_motivo
             where iteu_cd_item_estrat_ur = reg.cd_item_estrat_ur;
             
            update estratificacao_ur
               set esur_dt_atualizacao = sysdate
             where esur_cd_estratificacao_ur = reg.cd_estrat_ur;
             
            v_msg := 'The UR Review for C-Lob ' || reg.nm_contrato_pratica || ' on ' || to_char(reg.dt_mes,'mm/yyyy') || ' must be updated.';
             
            USP_SEND_MAIL(reg.tx_email_criador, c_subject, v_msg);

        elsif (reg.vl_dif_real <> reg.vl_dif_review) then
            update item_estratificacao_ur
               set iteu_nr_fte_alocado = reg.vl_total_aloc,
                   iteu_nr_fte_receitado = reg.vl_total_rec,
                   iteu_vl_diferenca = reg.vl_dif_real
             where iteu_cd_item_estrat_ur = reg.cd_item_estrat_ur;
             
            update estratificacao_ur
               set esur_dt_atualizacao = sysdate
             where esur_cd_estratificacao_ur = reg.cd_estrat_ur;
             
            v_msg := 'The UR Review for C-Lob ' || reg.nm_contrato_pratica || ' on ' || to_char(reg.dt_mes,'mm/yyyy') || ' must be updated.';
             
            USP_SEND_MAIL(reg.tx_email_criador, c_subject, v_msg);

        end if;
    end loop;

    commit;

    -- se tudo executou corretamente, retorna um
    p_result := 1;

  exception
    -- caso der algum erro d� rollback e retorna zero
    when OTHERS then
      rollback;
      --v_error_text := SUBSTR(SQLERRM, 1,200) || ' CODE:' || SQLCODE;

    p_result := 0;

  end;

end USP_PMS_ATUALIZA_ESTRATIFIC_UR;