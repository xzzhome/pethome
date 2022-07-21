package com.xzz.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemDetail {
    private Long id;
    private String name;
    private Long types_id;
    private SystemType systemType;
}
