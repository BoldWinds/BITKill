package cn.edu.bit.BITKill.model;

// 保存游戏相关状态的枚举类
public enum GameState{
    START,      // 游戏开始
    KILL,       // 狼人刀人
    PROPHET,    // 预言家查验
    WITCH,      // 女巫操作
    ELECT,      // 选举警长
    VOTE,       // 投票环节
    WORDS,      // 遗言
    END,        // 游戏结束

}
