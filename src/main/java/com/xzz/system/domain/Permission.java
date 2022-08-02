package com.xzz.system.domain;

import com.xzz.basic.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permission extends BaseDomain {
    private String name;
    private String url;
    private String descs;
    private String sn;
}