package com.ruoyi.project.lb.test;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.lb.sensitive.domain.EachTeam;
import com.ruoyi.project.lb.sensitive.service.ISysUserSensitiveService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QueryMyTeamTest {

    @Autowired
    private ISysUserSensitiveService sysUserSensitiveService;

    @Test
    public void getMyTeam(){
        try {
            List<EachTeam> eachTeams = sysUserSensitiveService.queryMyTeam(1l);
            System.out.println(111);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMyInviter(){
        try {
            System.out.println(1111);
            sysUserSensitiveService.upgrade(111l);
            System.out.println(111);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
