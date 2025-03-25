package com.ciandt.pms.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecalculateMessageDTO {

    private Long mapCode;
    private Integer retryCount;
}
