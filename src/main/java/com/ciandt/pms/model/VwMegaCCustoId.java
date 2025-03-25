/*
 * @(#) VwChpProjetosId.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * VwMegaCCustoId 
 * 
 * @since 30/09/2014
 * @author <a href="mailto:alan@ciandt.com">Alan Thiago do Prado</a>
 *
 */
@Embeddable
public class VwMegaCCustoId implements java.io.Serializable {

	/**
	 * Valor do serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Atributo gerado a partir da coluna CUS_IN_REDUZIDO.
	 */
	@Column(name = "CUS_IN_REDUZIDO")
	private Long codigoReduzido;

	/**
	 * Atributo gerado a partir da coluna CUS_PAD_IN_CODIGO.
	 */
	@Column(name = "CUS_PAD_IN_CODIGO")
	private Long codigoPadrao;

	/**
	 * Obtem o valor do atributo {@link VwMegaCCustoId#codigoReduzido}.<BR>
	 *
	 * @return the codigoReduzido
	 */
	public Long getCodigoReduzido() {
		return codigoReduzido;
	}

	/**
	 * Atualiza o valor do atributo codigoReduzido.<BR>
	 *
	 * @param codigoReduzido Novo valor para o atributo {@link VwMegaCCustoId#codigoReduzido}.
	 */
	public void setCodigoReduzido(Long codigoReduzido) {
		this.codigoReduzido = codigoReduzido;
	}

	/**
	 * Obtem o valor do atributo {@link VwMegaCCustoId#codigoPadrao}.<BR>
	 *
	 * @return the codigoPadrao
	 */
	public Long getCodigoPadrao() {
		return codigoPadrao;
	}

	/**
	 * Atualiza o valor do atributo codigoPadrao.<BR>
	 *
	 * @param codigoPadrao Novo valor para o atributo {@link VwMegaCCustoId#codigoPadrao}.
	 */
	public void setCodigoPadrao(Long codigoPadrao) {
		this.codigoPadrao = codigoPadrao;
	}

}
