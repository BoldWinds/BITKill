package cn.edu.bit.BITKill.model;

public enum Drug {
    NONE,   // 不使用药或者没有剩余的药
    POISON, //使用毒药或者剩余毒药
    ANTIDOTE,   // 使用解药或者剩余解药
    ALL     // ALL不适用于毒药或者解药的使用，只用于在后端保存女巫剩余所有的药
}
