create or replace view vw_mega_contrato_cliente as
select p.pro_in_reduzido cd_mega, p.pro_st_descricao nm_contrato
from mgglo.glo_projetos@ln_mgweb.cit p
where p.pro_in_nivel = 1
and p.pro_st_grupoext not like '200%'
and p.pro_st_grupoext not like '300%'
and p.pro_st_grupoext <> 9