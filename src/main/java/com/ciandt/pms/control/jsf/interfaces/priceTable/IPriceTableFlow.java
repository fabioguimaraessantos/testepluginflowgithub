package com.ciandt.pms.control.jsf.interfaces.priceTable;

import com.ciandt.pms.control.jsf.pojo.PriceTablePojo;
import com.ciandt.pms.enums.PriceTableMemberType;
import com.ciandt.pms.exception.PriceTableException;
import com.ciandt.pms.model.ItemTabelaPreco;
import com.ciandt.pms.model.PriceTableHistory;
import com.ciandt.pms.model.PriceTableStatus;
import com.ciandt.pms.model.TabelaPreco;

import java.util.List;

public interface IPriceTableFlow {

    /**
     *
     */
    Long STATUS_DRAFT = 1L;
    Long STATUS_READY_FOR_APRROVAL = 2L;
    Long STATUS_ON_APRROVAL = 3L;
    Long STATUS_APPROVED = 4L;
    Long STATUS_NOT_APPROVED = 5L;
    Long STATUS_DELETED = 6L;


    /**
     * Creates a new Price Table with Status Draft.
     * @param priceTable - Price Table to Create.
     * @return Boolean - True - If creates.
     */
    boolean create(TabelaPreco priceTable);

    /**
     * @param pojo Pojo which load info based on status flow.
     */
    void prepareUpdate(PriceTablePojo pojo);

    /**
     * @param pojo Pojo that contains all infos to proceed in status flow
     */
    void update(PriceTablePojo pojo) throws PriceTableException;

    /**
     *
     */
    List<PriceTableStatus> priceTableStatusList(PriceTableMemberType member);

    /**
     * Returns "In Progress" status codes (e.g. not approved yet)
     * @return list of status codes
     */
    List<Long> findPriceTableStatusInProgress();

    /**
     *
     * @param priceTable
     * @param login
     * @return
     */
    PriceTableHistory createHistory(Long priceTable, String login);

    /**
     *
     * @param isApprover
     * @return
     */
    String outcome(boolean isApprover);

    /**
     *
     * @param item
     * @param login
     * @return
     */
    Boolean removeItemTabelaPreco(ItemTabelaPreco item, String login);

    /**
     *
     * @param item
     * @param login
     * @return
     */
    Boolean createItemTabelaPreco(ItemTabelaPreco item, String login);
}