package com.xzz.system.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class SystemType {
    private Long id;
    private String sn;
    private String name;

    //部门树最后一级没有数据就不显示
    /*@JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<SystemDetail> children = new ArrayList<>();*/
}
