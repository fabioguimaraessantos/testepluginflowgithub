--------------------------------------------------------
--  DDL for Table CONTRATO_PRATICA_AUD
--------------------------------------------------------

  CREATE TABLE "PMS20"."CONTRATO_PRATICA_AUD" 
   ("COPR_CD_CONTRATO_PRATICA" NUMBER(18,0), 
	"REV" NUMBER(18,0), 
	"REVTYPE" NUMBER(3,0),
	"MSAA_CD_MSA" NUMBER(18,0), 
	"PRAT_CD_PRATICA" NUMBER(18,0), 
	"COPR_NM_CONTRATO_PRATICA" VARCHAR2(240 BYTE), 
	"COPR_DS_CONTRATO_PRATICA" VARCHAR2(200 BYTE), 
	"COPR_IN_STATUS" VARCHAR2(2 BYTE), 
	"PESS_CD_PESSOA_DN" NUMBER(18,0), 
	"COPR_PR_MARGEM" NUMBER, 
	"COPR_PR_COMISSAO" NUMBER, 
	"COPR_IN_DELETE_LOGICO" VARCHAR2(1 BYTE), 
	"CONT_CD_CONTRATO_AUX" NUMBER(18,0), 
	"COPR_NM_COMPOUND" VARCHAR2(240 BYTE), 
	"PESS_CD_APROVADOR" NUMBER(8,0), 
	"PESS_CD_GERENTE" NUMBER(8,0), 
	"COPR_IN_ATIVO" VARCHAR2(1 BYTE), 
	"COPR_DT_INICIAL" DATE, 
	"COPR_DT_FINAL" DATE
   )
--------------------------------------------------------
--  DDL for Index PK_CONTRATO_PRATICA_AUD
--------------------------------------------------------

  CREATE UNIQUE INDEX "PMS20"."PK_CONTRATO_PRATICA_AUD" ON "PMS20"."CONTRATO_PRATICA_AUD" ("COPR_CD_CONTRATO_PRATICA", "REV");
  
--------------------------------------------------------
--  Constraints for Table CONTRATO_PRATICA_AUD
--------------------------------------------------------

  ALTER TABLE "PMS20"."CONTRATO_PRATICA_AUD" ADD CONSTRAINT "PK_CONTRATO_PRATICA_AUD" PRIMARY KEY ("COPR_CD_CONTRATO_PRATICA", "REV");
  ALTER TABLE "PMS20"."CONTRATO_PRATICA_AUD" MODIFY ("REV" NOT NULL ENABLE);
  ALTER TABLE "PMS20"."CONTRATO_PRATICA_AUD" MODIFY ("COPR_CD_CONTRATO_PRATICA" NOT NULL ENABLE);
--------------------------------------------------------
--  Ref Constraints for Table CONTRATO_PRATICA_AUD
--------------------------------------------------------

  ALTER TABLE "PMS20"."CONTRATO_PRATICA_AUD" ADD CONSTRAINT "FK_REVI_COPR" FOREIGN KEY ("REV")
	  REFERENCES "PMS20"."REVINFO" ("REV") ENABLE;

  ALTER TABLE "PMS20"."CONTRATO_PRATICA_AUD" ADD CONSTRAINT "FK_COPR_COPRAUD" FOREIGN KEY ("COPR_CD_CONTRATO_PRATICA")
	  REFERENCES "PMS20"."CONTRATO_PRATICA" ("COPR_CD_CONTRATO_PRATICA") ENABLE;
	  
  ALTER TABLE "PMS20"."CONTRATO_PRATICA_AUD" ADD CONSTRAINT "FK_MSAA_COPRAUD" FOREIGN KEY ("MSAA_CD_MSA")
	  REFERENCES "PMS20"."MSA" ("MSAA_CD_MSA") ENABLE;
	  
  ALTER TABLE "PMS20"."CONTRATO_PRATICA_AUD" ADD CONSTRAINT "FK_PESS_COPRAUD" FOREIGN KEY ("PESS_CD_PESSOA_DN")
	  REFERENCES "PMS20"."PESSOA" ("PESS_CD_PESSOA") ENABLE;
  
  ALTER TABLE "PMS20"."CONTRATO_PRATICA_AUD" ADD CONSTRAINT "FK_PRAT_CTPRAUD" FOREIGN KEY ("PRAT_CD_PRATICA")
	  REFERENCES "PMS20"."PRATICA" ("PRAT_CD_PRATICA") ENABLE;