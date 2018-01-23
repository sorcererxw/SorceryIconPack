# 常见问题

## 如何使用?

Sorcery 图标提供多种图标替换方式, 可以点击进入Sorcery 图标应用抽屉内的"应用图标"来查看详情, 会根据你的设备情况进行推荐

### MIUI & FLYME

Sorcery 图标已上架MIUI和FLYME主题商店, 可下载对应版本以得到最佳的体验

- [MIUI](http://zhuti.xiaomi.com/detail/db2b63b2-b14e-4f5f-96bf-c12a730d3fd6)
- [FLYME](http://theme.flyme.cn/themes/public/detail?package_name=com.meizu.theme.sorcery&mzos=5.0)

### 支持图标包的第三方启动器(桌面)

进入启动器(桌面)设置页面, 找到外观或者图标之类的选项, 进入即可选择图标包进行使用

以下是一些常见的支持第三方图标包的桌面

- [Nova Launcher](https://play.google.com/store/apps/details?id=com.teslacoilsw.launcher)
- [Action Launcher](https://play.google.com/store/apps/details?id=com.actionlauncher.playstore)
- [Apex Launcher](https://play.google.com/store/apps/details?id=com.anddoes.launcher)
- [TSF Launcher]() 
- [ADW Launcher](https://play.google.com/store/apps/details?id=org.adw.launcher)
- [Arrow Launcher](https://play.google.com/store/apps/details?id=com.microsoft.launcher)
- [Evie Launcher](https://play.google.com/store/apps/details?id=is.shortcut)
- [Smart Launcher](https://play.google.com/store/apps/details?id=ginlemon.flowerfree)

### 缓存替换 

由于一些桌面本身不支持使用图标包来自定义桌面图标, Sorcery 图标首创通过替换桌面的本地图标缓存来改变桌面的应用图标, **需要Root权限**

点击进入Sorcery 图标应用抽屉内的"应用图标"来查看详情

现已支持以下桌面

- [Google Now Launcher](https://play.google.com/store/apps/details?id=com.google.android.launcher)
- [Pixel Launcher](https://play.google.com/store/apps/details?id=com.google.android.apps.nexuslauncher)
- [锤子桌面(商店下载版本)](http://www.smartisan.com/apps/launcher)

*由于是替换缓存, 所以暂时无法实时替换新增应用的图标, 所以在应用安装或者更新之后需要手动重新替换图标, 为了更好的图标体验, 请优先考虑使用支持图标包的第三方桌面*

### XPOSED

如果你的设备已经刷入XPOSED, 可以使用通过XPOSED框架进行替换,点击进入Sorcery 图标应用抽屉内的"应用图标"来使用 ,具体详见下方

## 如何单独定义某个应用的图标?

部分桌面支持单独定义某个应用的图标, 现在就以Nova Launcher为例, 来介绍一下如何单独定义某个应用的图标, 其他的启动器都是异曲同工的

在启动器内长按想要替换的应用, 选择编辑, 会出现"编辑应用"窗口, 点击应用图标, 会显示"选择主题"界面, 找到Sorcery 图标, 此时, 你可以选择单击或者长按这一项

如果是单击, 则会罗列出所有Sorcery 图标中的图标, 选择一个即可

如果是长按, 则可以进入Sorcery 图标应用内进行选择, 可以通过分类和搜索更快找到对应图标

## 什么是XPOSED全局替换?

通过XPOSED框架, Sorcery 图标 在系统级别上替换了对应应用的图标资源, 从而做到无论是系统还是第三方应用无论中都只能读取到被替换过的图标, 以此来在系统级别上使用图标包

## 为何Xposed全局替换无效?

- 请确保系统的SELinux状态为permissive模式, 具体可以参考[折腾无果？先试试禁用SELinux - 少数派](https://sspai.com/post/32197)
- 如果仅仅是桌面图标没有发生变化, 但是系统内其他地方显示的应用图标已经改变(比如 设置->应用), 则是因为桌面的图标缓存尚未刷新导致的, 只需要清空一下桌面数据或者重新安装桌面即可

## 造成设备卡顿或者更多耗电?

Sorcery 作为一个图标包是不会驻留后台, 所以也不存在造成设备卡顿和额外耗电
而如果应用到启动器(桌面)上的时候, 启动器不会因为应用了一个图标包而消耗额外的电量