CREATE OR REPLACE VIEW VW_HRS_CARGO AS
SELECT CARG.GARG_CD_CARGO CARG_CD_CARGO
,CARG.GARG_NM_CARGO CARG_NM_CARGO
,CARG.GARG_IN_ATIVO CARG_IN_ATIVO
FROM HRS.CARGO CARG
WHERE carg.GARG_NM_CARGO not like 'Analis%'
and carg.GARG_NM_CARGO not like 'Assis%'
and carg.GARG_NM_CARGO not like 'Aux%'
and lower(garg_nm_cargo) not like '%vendas%'
and lower(garg_nm_cargo) not like '%tradutor%'
and lower(garg_nm_cargo) not like '%coordenador%'
and lower(garg_nm_cargo) not like '%area%'
and carg.garg_cd_cargo not in (249,350, 351,390, 391, 393, 395, 415, 389, 304, 315, 348, 412, 373, 303, 313, 312, 357, 320, 345, 285)