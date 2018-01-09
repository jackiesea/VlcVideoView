### MrCSH_VlcVideoView

基于mengzhidaren的Vlc-sdk-lib（lib版本号见libvlc文件夹下的version）修改，感谢<br>

  * [mengzhidaren/Vlc-sdk-lib]https://github.com/mengzhidaren/Vlc-sdk-lib <br>

提取自本人项目，使用时根据需要修改 <br>
类MyVlcVideoView.class代码略乱，使用时建议按照MVC模式重构 <br>


【停止更新】
---

# 实现的功能
```
竖屏/全屏切换
进度条拖动播放
实时修改播放倍速
支持手势操作（左区上下滑动调节亮度、右区上下滑动调节音量、水平滑动快进快退）
记录播放位置，从上一次播放位置开始播放
监听网络变化
```

# 使用方法
```
<org.videolan.vlc.MyVlcVideoView
   android:id="@+id/video_view"
   android:layout_width="match_parent"
   android:layout_height="match_parent" />
```

### DEMO效果预览
![](https://raw.githubusercontent.com/jackiesea/MrCSH_VlcVideoView/master/capture/1.png)
![](https://raw.githubusercontent.com/jackiesea/MrCSH_VlcVideoView/master/capture/2.png)
![](https://raw.githubusercontent.com/jackiesea/MrCSH_VlcVideoView/master/capture/3.png)
![](https://raw.githubusercontent.com/jackiesea/MrCSH_VlcVideoView/master/capture/4.png)
![](https://raw.githubusercontent.com/jackiesea/MrCSH_VlcVideoView/master/capture/5.png)
![](https://raw.githubusercontent.com/jackiesea/MrCSH_VlcVideoView/master/capture/6.png)
![](https://raw.githubusercontent.com/jackiesea/MrCSH_VlcVideoView/master/capture/7.png)