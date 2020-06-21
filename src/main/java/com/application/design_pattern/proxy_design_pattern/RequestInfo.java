package com.application.design_pattern.proxy_design_pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestInfo {
    private String apiName;
    private Long responseTime;
    private Long startTimestamp;
}
