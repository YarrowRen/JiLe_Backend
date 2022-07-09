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

    /**
     * 根据校园码获取学生基本信息
     * @param username
     * @return
     */
    public User findUserByUsername(String username);



    boolean insertUser(User user,String courseNum);


}
