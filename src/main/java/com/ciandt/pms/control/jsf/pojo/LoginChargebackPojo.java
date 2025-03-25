package com.ciandt.pms.control.jsf.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsefa.csv.annotation.CsvDataType;
import org.jsefa.csv.annotation.CsvField;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@CsvDataType
public class LoginChargebackPojo implements Serializable {

    private Long id;
    @CsvField(pos = 0, required = true)
    private String login;
    private boolean correct;
    private String error;
}
