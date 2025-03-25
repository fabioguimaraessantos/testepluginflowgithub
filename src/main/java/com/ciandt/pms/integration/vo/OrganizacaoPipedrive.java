package com.ciandt.pms.integration.vo;

/**
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 *
 */
public class OrganizacaoPipedrive implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    public OrganizacaoPipedrive() {
    }


    public OrganizacaoPipedrive(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCombinedData() {
        return this.id.toString().concat(this.name);
    }
}
