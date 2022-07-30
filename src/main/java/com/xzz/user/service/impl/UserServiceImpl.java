package com.xzz.user.service.impl;

import com.xzz.basic.Constant.VerifyCodeConstants;
import com.xzz.basic.exception.BusinessException;
import com.xzz.basic.service.impl.BaseServiceImpl;
import com.xzz.basic.util.JsonResult;
import com.xzz.basic.util.Md5Utils;
import com.xzz.basic.util.StrUtils;
import com.xzz.user.domain.Logininfo;
import com.xzz.user.domain.User;
import com.xzz.user.dto.UserDto;
import com.xzz.user.mapper.LogininfoMapper;
import com.xzz.user.mapper.UserMapper;
import com.xzz.user.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService {
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LogininfoMapper logininfoMapper;

    @Override
    public void phoneRegister(UserDto userDto) {
        String phone = userDto.getPhone();
        String phoneCode = userDto.getPhoneCode();
        String password = userDto.getPassword();
        String confirmPassword = userDto.getConfirmPassword();

        //1.校验
        //1.1.空值校验
        if(StringUtils.isEmpty(phone)
                || StringUtils.isEmpty(phoneCode)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(confirmPassword)){
            throw new BusinessException("请输入正确的数据!!!");
        }
        //1.2.两次密码是否相同
        if(!password.equals(confirmPassword)){
            throw new BusinessException("两次密码不一致!!!");
        }
        //1.3.验证码是否过期
        Object obj = redisTemplate.opsForValue().get(VerifyCodeConstants.REGISTER_CODE_PREFIX + phone);
        if(obj == null){
            throw new BusinessException("验证码无效，请重新获取!!!");
        }
        //1.4.验证码是否正确  //  9527:1223555555
        String codeTmp = obj.toString().split(":")[0];
        if(!codeTmp.equalsIgnoreCase(phoneCode)){
            throw new BusinessException("验证码错误!!!");
        }

        //2.添加进入数据库；前端传的参数包括验证码，数据库不需要，重新封装
        User user = userDto2User(userDto);//调用下面方法，把前端传的dto中的手机、密码，重新封装进入User
        Logininfo logininfo = User2Logininfo(user);//调用下面方法，上同，重新封装进入Logininfo
        //2.1添加进入t_logininfo表
        logininfoMapper.save(logininfo);
        //2.2添加进入t_User表
        user.setLogininfo_id(logininfo.getId());
        userMapper.save(user);
    }

    //后端添加到数据库，需要的是user；但是前端传的是userDto，所以需要转成user
    private User userDto2User(UserDto userDto){
        User user = new User();
        user.setUsername(userDto.getPhone());
        user.setPhone(userDto.getPhone());
        //获取随机盐值
        String salt = StrUtils.getComplexRandomString(32);
        user.setSalt(salt);
        //获取加密之后的密码：双重加密，盐值拼接密码，再用MD5加密
        String md5Password = Md5Utils.encrypByMd5(userDto.getPassword()+salt);
        user.setPassword(md5Password);
        return user;
    }

    //t_logininfo表，本身就是t_user的子表，可以从user中copy进来，不需要用userDto转logininfo
    private Logininfo User2Logininfo(User user){
        Logininfo logininfo = new Logininfo();
        BeanUtils.copyProperties(user,logininfo);
        logininfo.setType(1);
        return logininfo;
    }

}
