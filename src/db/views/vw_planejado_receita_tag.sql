create or replace view vw_planejado_receita_tag as
select cd_contrato_pratica,
       nome_contrato_pratica,
       dt_mes,
       recu_cd_recurso,
       aloc_in_estagio,
       vlr_preco,
       valor_fte,
       valor_ut_rate,
       ( valor_fte * valor_ut_rate ) valor_fte_ut_rate,
       cod_tag,
       desc_tag
from ( select cp.copr_cd_contrato_pratica         cd_contrato_pratica,
              cp.copr_nm_contrato_pratica         nome_contrato_pratica,
              tp.tapr_cd_tabela_preco,
              tp.tapr_dt_inicio_vigencia,
              case when tp.tapr_dt_fim_vigencia is not null
                        then tp.tapr_dt_fim_vigencia
                        else ap.alpe_dt_alocacao_periodo end tapr_dt_fim_vigencia,
              ma.maal_tx_titulo,
              itp.ittp_vl_item_tb_preco,
              al.vlr_ut_rate                       valor_ut_rate,
              ap.alpe_pr_alocacao_periodo          valor_fte,
              ap.alpe_pr_alocacao_periodo,
              ap.alpe_dt_alocacao_periodo          dt_mes,
              itp.ittp_vl_item_tb_preco * 168      valor_por_fte,
              ( itp.ittp_vl_item_tb_preco * 168 ) * vlr_ut_rate * ap.alpe_pr_alocacao_periodo * fur.faum_pr_fator_ur  vlr_preco,
              al.aloc_in_estagio,
              al.recu_cd_recurso,
              eal.etiq_cd_etiqueta cod_tag,
              eq.etiq_nm_etiqueta desc_tag
       from ( select aloc_cd_alocacao,
                     cd_mapa_alocacao,
                     cd_perfil_vendido,
                     vlr_ut_rate,
                     aloc_in_estagio,
                     recu_cd_recurso
              from ( select al.aloc_cd_alocacao,
                            al.maal_cd_mapa_alocacao  cd_mapa_alocacao,
                            al.peve_cd_perfil_vendido cd_perfil_vendido,
                            al.aloc_vl_ur vlr_ut_rate,
                            al.aloc_in_estagio,
                            al.recu_cd_recurso
                     from alocacao al ) al ) al,
            tabela_preco tp,
            msa msa,
            item_tabela_preco itp,
            alocacao_periodo ap,
            contrato_pratica cp,
            mapa_alocacao ma,
            fator_ur_mes fur,
            etiqueta_alocacao eal,
            etiqueta eq
       where tp.tapr_cd_tabela_preco     = itp.tapr_cd_tabela_preco    and
             al.cd_perfil_vendido        = itp.peve_cd_perfil_vendido  and
             al.aloc_cd_alocacao         = ap.aloc_cd_alocacao         and
             al.aloc_cd_alocacao         = eal.aloc_cd_alocacao (+)    and
             eal.etiq_cd_etiqueta        = eq.etiq_cd_etiqueta  (+)    and
             cp.copr_cd_contrato_pratica = ma.copr_cd_contrato_pratica and
             ma.maal_cd_mapa_alocacao    = al.cd_mapa_alocacao         and
             --ma.copr_cd_contrato_pratica = tp.copr_cd_contrato_pratica and
             cp.msaa_cd_msa              = msa.msaa_cd_msa             and
             tp.msaa_cd_msa              = msa.msaa_cd_msa             and
             ma.maal_cd_mapa_alocacao    = fur.maal_cd_mapa_alocacao   and
             ap.alpe_dt_alocacao_periodo = fur.faum_dt_mes             and
             ma.maal_in_versao           = 'PB' )
where dt_mes between tapr_dt_inicio_vigencia and tapr_dt_fim_vigencia