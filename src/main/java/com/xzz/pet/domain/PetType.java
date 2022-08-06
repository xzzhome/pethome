package com.xzz.pet.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xzz.basic.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetType extends BaseDomain {
    private String name;
    private String description;
    private String dirPath;
    private Long pid;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<PetType> children=new ArrayList<>();
}
