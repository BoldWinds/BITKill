# BITKill

## 软件的运行与开发环境

1. 开发环境：IntelliJ IDEA 2022.3.1
2. 运行环境：jdk 19

## 软件的安装与卸载方法

### 使用docker镜像运行（推荐）

在确认电脑上安装docker后，在shell中输入以下获取最新的bitkill镜像：

```shell
$ docker pull boldwinds/bitkill:latest
```

该镜像占用容器的8080端口，至于容器到实体机的端口映射用户可自己选择更改

启动运行：

```shell
$ docker run -d -p 8080:8080 boldwinds/bitkill
```

卸载时使用docker相关命令即可

### 使用jar包运行

直接运行jar包即可：

```shell
$ java -jar bitkill.jar
```

