package cn.edu.bit.BITKill.model;

import java.util.HashMap;

public class VoteResult {

    // 标志是否平票，true为平票，false为有结果
    private boolean tie;
    private String result;

    private HashMap<String,String> voterTargetMap;
}
