package com.xzz.pet.service;

import com.xzz.basic.query.PageList;
import com.xzz.basic.service.IBaseService;
import com.xzz.pet.domain.PetType;
import com.xzz.pet.domain.SearchMasterMsg;
import com.xzz.pet.dto.AcceptDto;
import com.xzz.pet.query.SearchMasterMsgQuery;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ISearchMasterMsgService extends IBaseService<SearchMasterMsg>  {


    void publish(SearchMasterMsg searchMasterMsg, Long id);

    PageList<SearchMasterMsg> toHandle(SearchMasterMsgQuery query, HttpServletRequest request);

    PageList<SearchMasterMsg> userSearchMasterMsg(SearchMasterMsgQuery query, HttpServletRequest request);

    void reject(Long id);

    void accept(AcceptDto dto);
}