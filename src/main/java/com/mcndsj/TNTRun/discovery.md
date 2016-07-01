# 关于我现在的发现，和我知道的功能

## 简述
一个能玩 TNTRun 游戏的小插件

## 指令
就一个 /tnt
1. 权限：游戏内OP玩家
2. 使用方法：

| 命令 | 说明 |
|:--------|:------|
| /tnt game <地图名字> | 修改游戏的名字 |
| /tnt spawn | 修改游戏出生点为玩家所在的位置 |
| /tnt loc | 这个有点复杂，如果地图已经设置上边界却没有设置下边界那么会设置下边界，否则设置上边界。设置为玩家所在的位置 |
| /tnt save | 保存地图 |
| /tnt start | 启动游戏！ |

 \*以上所有指令操控的游戏，都是玩家所在的地图所在的游戏

## 配置地图步骤

1. 确保服务器启动且可连入，确保有一个OP玩家账户
2. 进入服务器，传送到需要设置地图的游戏地图
3. 输入 `/tnt game <地图名字>` 表示我要创建一个新的地图，比如 `/tnt game example` 创建一个名为 `example` 的地图
4. 使用飞行或传送等手段让玩家位置达到地图上边界（即活动区域x、y、z坐标最大值），输入 `/tnt loc`
5. 到地图下边界，再输入 `/tnt loc`
6. 到地图出生点，输入 `/tnt spawn`
7. 输入 `/tnt save` 保存。此时在插件数据文件夹的 `maps` 文件夹会出现一个代表地图配置的文件，内为json格式，说明保存成功。
8. 输入 `/tnt start` 来启动游戏。

## 测试平台

* CPU: Intel(R) Core(TM) i5-4200U @ 1.60 GHz  2.30GHz 4C/4T
* Memory: 8.00 GB (7.71 GB Usable)
* Minecraft: 1.8.8
* OS: Windows 7 Ultimate 
* Platform: Spigot 1.8.8-R0.1-SNAPSHOT
* SDK: Java(TM) SE Runtime Environment (build 1.8.0_91-b14)
