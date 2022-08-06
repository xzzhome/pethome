package com.xzz.pet.domain;

import com.xzz.basic.domain.BaseDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetDetail extends BaseDomain {
    private Long pet_id;
    private String adoptNotice;
    private String intro;
}
