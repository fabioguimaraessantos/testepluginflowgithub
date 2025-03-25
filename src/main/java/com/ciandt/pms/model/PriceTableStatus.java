package com.ciandt.pms.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "PRICE_TABLE_STATUS")
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "PriceTableStatus.findPriceTableStatusAll", query = "SELECT pts FROM PriceTableStatus pts "
                + "ORDER BY pts.name ASC "),
        @NamedQuery(name = "PriceTableStatus.findByAcronym", query = "SELECT pts FROM PriceTableStatus pts "
                + "WHERE pts.acronym = ? "
                + "ORDER BY pts.name ASC "),
        @NamedQuery(name = "PriceTableStatus.findPriceTableStatus", query = "SELECT pts FROM PriceTableStatus pts "
                + "WHERE pts.acronym in (:acronyms) "
                + "ORDER BY pts.code ASC ")
})
public class PriceTableStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Constante para NamedQuery "PriceTableStatus.findPriceTableStatusAll". */
    public static final String FIND_PRICE_TABLE_STATUS_ALL =
            "PriceTableStatus.findPriceTableStatusAll";

    /** Constante para NamedQuery "PriceTableStatus.findByAcronym". */
    public static final String FIND_PRICE_TABLE_STATUS_BY_ACRONYM =
            "PriceTableStatus.findByAcronym";

    /** Constante para NamedQuery "PriceTableStatus.findPriceTableStatus". */
    public static final String FIND_PRICE_TABLE_STATUS =
            "PriceTableStatus.findPriceTableStatus";


    @Id
    @Column(name = "PTS_CD_PRICE_TB_STATUS", unique = true, nullable = false, precision = 18)
    private Long code;


    @Column(name = "PTS_NM_PRICE_TB_STATUS", length = 20)
    private String name;

    @Column(name = "PTS_AC_PRICE_TB_STATUS", length = 10)
    private String acronym;

    @Column(name = "PTS_TX_INFO", length = 100)
    private String description;

    @Override
    public String toString() {
        return "PriceTableStatus{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", acronym='" + acronym + '\'' +
                '}';
    }
}
