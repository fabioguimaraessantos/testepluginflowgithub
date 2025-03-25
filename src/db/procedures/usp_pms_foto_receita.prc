create or replace procedure usp_pms_foto_receita (p_data_mes in date) is

-- Cursor que busca a receita projetada para cada contrato-prática, mês e moeda
cursor c_receita_mes(p_mes date) is
select cp.copr_cd_contrato_pratica
      ,cp.copr_nm_contrato_pratica
      ,mo.moed_cd_moeda
      ,mo.moed_sg_moeda
      ,ap.alpe_dt_alocacao_periodo
      ,sum(ap.alpe_pr_alocacao_periodo) total_fte
      ,sum(ap.alpe_pr_alocacao_periodo * 168 * pr.ittp_vl_item_tb_preco * a.aloc_vl_ur * fum.faum_pr_fator_ur)  total_receita
from contrato_pratica cp
    ,mapa_alocacao m
    ,alocacao a
    ,perfil_vendido pv
    ,alocacao_periodo ap
    ,fator_ur_mes fum
    ,moeda mo
    ,(select tp.tapr_dt_inicio_vigencia, tp.tapr_dt_fim_vigencia, itp.peve_cd_perfil_vendido, itp.ittp_vl_item_tb_preco, tp.moed_cd_moeda
      from tabela_preco tp, item_tabela_preco itp
      where tp.tapr_cd_tabela_preco = itp.tapr_cd_tabela_preco) pr
where m.maal_in_versao = 'PB'
  AND m.maal_cd_mapa_alocacao = a.maal_cd_mapa_alocacao
  AND a.peve_cd_perfil_vendido = pv.peve_cd_perfil_vendido    
  and ap.alpe_dt_alocacao_periodo = p_mes
  and pv.moed_cd_moeda = mo.moed_cd_moeda
  and cp.copr_cd_contrato_pratica = m.copr_cd_contrato_pratica
  and m.maal_cd_mapa_alocacao = a.maal_cd_mapa_alocacao
  and a.aloc_cd_alocacao = ap.aloc_cd_alocacao
  and m.maal_cd_mapa_alocacao = fum.maal_cd_mapa_alocacao
  and ap.alpe_dt_alocacao_periodo = fum.faum_dt_mes
  and a.peve_cd_perfil_vendido = pr.peve_cd_perfil_vendido
  and ap.alpe_dt_alocacao_periodo between pr.tapr_dt_inicio_vigencia and nvl(pr.tapr_dt_fim_vigencia, last_day(sysdate))
  group by cp.copr_cd_contrato_pratica
          ,cp.copr_nm_contrato_pratica
          ,mo.moed_cd_moeda
          ,mo.moed_sg_moeda
          ,ap.alpe_dt_alocacao_periodo;

v_rere_cd_receita_resultado number;

begin

/***
** Esta procedure tem como objetivo criar "fotografias" mensais da receita projetada para um determinado mês.
** Ela deverá rodar todo dia 3 de cada mês para consolidar a receita projetada do mês.
** Ex.: Para o mês de janeiro/2012, a procedure rodará no dia 03/01/2012 consolidando a receita dos mapas publicados
** Levando em consideração o FTE Alocado, o Valor do Perfil Vendido, o FTE alocado e UR (tanto o fator_ur_mes, quanto
** o UR da alocação.
** Autor: Katsumi Arackawa Junior - 04/01/2012
***/

-- insere a receita planejada a partir do mapa de alocação
-- obs: para perfis que não têm preço vigente, não serão contabilizadas como receita.

if (to_char(sysdate, 'dd') = '04') then

    for r_rec in c_receita_mes( trunc(p_data_mes, 'mm') ) loop

        v_rere_cd_receita_resultado := 0;

        begin
            select nvl(r.rere_cd_receita_resultado , 0)
            into v_rere_cd_receita_resultado
            from receita_resultado r
            where r.copr_cd_contrato_pratica = r_rec.copr_cd_contrato_pratica
            and r.moed_cd_moeda = r_rec.moed_cd_moeda
            and r.rere_dt_mes = r_rec.alpe_dt_alocacao_periodo;
        exception
            when no_data_found then
                v_rere_cd_receita_resultado := 0;
        end;


        if v_rere_cd_receita_resultado != 0 then

            update receita_resultado rr
            set rr.rere_vl_receita_planejada = r_rec.total_receita
               ,rr.rere_nr_fte_planejado = r_rec.total_fte
               ,rr.rere_dt_foto = sysdate
               ,rr.rere_tx_observacao = null
               ,rr.more_cd_motivo_resultado = null
            where rr.rere_cd_receita_resultado = v_rere_cd_receita_resultado;

        else

            insert into receita_resultado (rere_cd_receita_resultado
                                          ,copr_cd_contrato_pratica
                                          ,moed_cd_moeda
                                          ,rere_dt_mes
                                          ,rere_tx_observacao
                                          ,rere_nr_fte_planejado
                                          ,rere_vl_receita_planejada
                                          ,rere_vl_receita_realizada
                                          ,more_cd_motivo_resultado
                                          ,rere_dt_foto)
            values (sq_rere_cd_receita_resultado.nextval,
                   r_rec.copr_cd_contrato_pratica,
                   r_rec.moed_cd_moeda,
                   r_rec.alpe_dt_alocacao_periodo,
                   null,
                   r_rec.total_fte,
                   r_rec.total_receita,
                   null,
                   null,
                   sysdate);
        end if;
    end loop;

    commit;
end if;

end usp_pms_foto_receita;
