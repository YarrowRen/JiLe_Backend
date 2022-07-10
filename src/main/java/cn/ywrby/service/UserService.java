package cn.ywrby.service;


import cn.ywrby.domain.*;

import java.util.List;

public interface UserService {

    /**
     * 验证成功，则返回VoUser对象
     * 验证失败，返回NULL或抛出报错
     * @param user
     * @return
     */
    public User verify(User user);



}
