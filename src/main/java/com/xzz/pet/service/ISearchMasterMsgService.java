package com.xzz.pet.service;

import com.xzz.basic.service.IBaseService;
import com.xzz.pet.domain.PetType;
import com.xzz.pet.domain.SearchMasterMsg;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ISearchMasterMsgService extends IBaseService<SearchMasterMsg>  {


    void publish(SearchMasterMsg searchMasterMsg, Long id);
}