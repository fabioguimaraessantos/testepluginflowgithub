create or replace procedure USP_PMS_CUSTOS_TCE_ERP is

begin

insert into tce_mes
(tcme_cd_tce_mes, 
 tcme_dt_tce_mes, 
 tcme_vl_tce_mes, 
 tcme_vl_taxa,
 pess_cd_pessoa, 
 moed_cd_moeda,
 tico_cd_tipo_contrato
)
select sq_tcme_cd_tce_mes.nextval,
       a.tcfu_dt_mes,
       a.tcfu_tce,
       -- valor taxa
       CASE
       WHEN a.valor_taxa IS not NULL
       THEN a.valor_taxa
       when (a.valor_taxa is null and (a.cod_moeda is null or a.cod_moeda = 1))
       then 1
       ELSE null             
       END AS valor_taxa,
       -- cod pessoa
       a.pess_cd_pessoa,
       -- moeda
       CASE WHEN a.cod_moeda IS not NULL 
       THEN (select m.moed_cd_moeda from moeda m 
             where m.moed_sg_moeda = a.cod_moeda)
       ELSE 1 --moeda padrao
       END AS cod_moeda,
       a.tico_cd_tipo_contrato
from ( select p.pess_cd_pessoa, 
              v.tcfu_dt_mes,
              v.tcfu_tce,
              v.valor_taxa,
              v.cod_moeda,
              v.tico_cd_tipo_contrato
       from vw_adm_rh_tce_funcionario v, 
            pessoa p,
            ( select p3.pess_cd_login, max( p3.pess_cd_pessoa ) max_cod 
              from pessoa p3
              group by p3.pess_cd_login ) max_p             
       where v.tcfu_login = p.pess_cd_login and
             p.pess_cd_login = max_p.pess_cd_login and
             p.pess_cd_pessoa = max_p.max_cod) a,
     tce_mes b
where a.pess_cd_pessoa = b.pess_cd_pessoa (+) and
      a.tcfu_dt_mes    = b.tcme_dt_tce_mes (+) and
      b.pess_cd_pessoa is null;

commit;

end USP_PMS_CUSTOS_TCE_ERP;