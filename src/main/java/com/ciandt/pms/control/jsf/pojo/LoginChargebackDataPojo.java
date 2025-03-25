package com.ciandt.pms.control.jsf.pojo;

import lombok.Data;

@Data
public class LoginChargebackDataPojo {

    private String name;
    private String type;
    private String extension;
    private byte[] data;
    private int size;
    private boolean upload;
}