package cn.edu.bit.BITKill.service;

import cn.edu.bit.BITKill.model.GlobalData;
import java.util.List;
import cn.edu.bit.BITKill.model.params.CommonResp;
import cn.edu.bit.BITKill.model.params.TimeResult;
import cn.edu.bit.BITKill.util.SendHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class HeartBeatService {

    // 每2s广播心跳包
    @Scheduled(fixedRate = 2000)
    @Async
    public void HeartBeat(){
        System.out.println(Thread.currentThread().getName());
        if (GlobalData.getUserSessionMap().size() != 0){
            List<String> players = new ArrayList(GlobalData.getUserSessionMap().keySet());
            SendHelper.sendMessageByList(players,new CommonResp<TimeResult>("heartbeat",true,"heartbeat package",new TimeResult(new Date().getTime())));
        }else{
            System.out.println("No user online");
        }
    }
}
