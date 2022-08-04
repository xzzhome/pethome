package com.xzz.user.domain;

import com.xzz.system.domain.Menu;
import lombok.Data;

import java.util.List;

@Data
public class LoginData {
    private Logininfo logininfo;		//用户登录信息
    private List<String> permissions; 	//当前登录人拥有的权限：保存的是sn
    private List<Menu> menus;			//当前登录人拥有的菜单
}