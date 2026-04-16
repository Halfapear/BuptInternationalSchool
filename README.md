[![GitHub license](https://img.shields.io/github/license/Halfapear/BuptInternationalSchool)](https://github.com/Halfapear/BuptInternationalSchool/blob/LICENSE)
[![GitHub stars](https://img.shields.io/github/stars/Halfapear/BuptInternationalSchool)](https://github.com/Halfapear/BuptInternationalSchool/stargazers)

<div style="text-align: center;">
<img src="https://count.getloli.com/get/@Halfapear?theme=rule34" alt="Halfapear">
</div>

# ✨北邮果园资料库

本仓库所有的资料都来自网络的收集和同学的分享，仅供学习和研究使用，版权归原作者所有，在此不以盈利为目的，如有侵权请联系 [1783148122@bupt.edu.cn](1783148122@bupt.edu.cn)删除

资料仅供学习，勿做商用

如果发现有一些资料已经过时，希望你能在[issue](https://github.com/Halfapear/BuptInternationalSchool/issues)里提出来，方便我们及时的将它扔进垃圾堆~

欢迎同学们pr补充！

ps：有你们最想要的往年题[手动狗头]确定不点个 star 再走

想要交流欢迎加 __QQ__ 1783148122 请注明来意

2025.1.21 整了个群 方便吹水 580976072

想要持续关注更新可以点击 Watch（会发邮件）

## 🌐资料推荐
10.16添加 随便写点觉得好的资料

__C语言__ 参考书建议《明解C语言》（确实很浅显易懂 工具类的东西其实我就不喜欢听课）来预习和入门，你问为什么不看《C primier plus》入门 如果你看得下去也可以 砖头适合当工具书参考书；课程建议 b站-翁凯  

__线性代数__ 非常建议那本黄书（和果园课程最贴近的一本 除了其中一章）例子-线性代数 原书10版 史蒂文 利昂 数学经典教材 9787111717294 机械工业出版社 也可以看看《线性代数应该这样学》这本提一下兴趣

__高数__ 经典宋浩 你想写什么吉米多维奇也可以 其实把所有理论学科把ppt看看 例题做做 作业再做做已经绰绰有余了

__电路__ 推荐 [电气工程杨](https://www.bilibili.com/video/BV1gF411E7FW/?spm_id_from=333.337.search-card.all.click&vd_source=3992bc92d9488eb34391d041e92266ba)的 简练易懂 你在跟着听三节课的RLC别人10min讲明白了

__信号__ 非常推荐 [2022浙江大学信号与系统 - 胡浩基老师](https://www.bilibili.com/video/BV1g94y1Q76G/?spm_id_from=333.337.search-card.all.click&vd_source=3992bc92d9488eb34391d041e92266ba),不过唯一不太好是讲的偏多 留足时间看就行

__计网__ 计网知识非常散 你最后很可能发现学期中学的知识点期末都想不起来 还是得期末突击；课程看湖科大的 讲的很好；不得不提的是 计网的应用是很好玩的 只考这个破试会失去很多乐趣

23前十P指导的资料 https://plutoandsun.github.io/

从大二下开始 英方课逐渐增多 直接对照着ppt和ai一起学就行 加点学长学姐的整理调调味（我已经尽可能收集了）

<img width="1480" height="840" alt="dd4bd1e006079fa0a3f1ebb2684722c7" src="https://github.com/user-attachments/assets/ff8df299-5da5-4ae1-8e28-eb60c48b80e0" />


__Github使用__ 

  github最基础使用下载clone 上传三板斧add commit push；
  
  如何配ssh自己查；github入门可参考：[知乎：如何使用github](https://www.zhihu.com/question/20070065/answer/517839193)；
  
  github进阶可看【十分钟学会正确的github工作流，和开源作者们使用同一套流程】 https://www.bilibili.com/video/BV19e4y1q7JJ/?share_source=copy_web&vd_source=5cdb672a85240041a0304cff7a81bfba

### 轻量化下载/上传（建议）

为了避免大文件导致 push 慢、拉取慢或失败，本仓库使用下面的分层策略：

- `<= 20MB`：正常放仓库。
- `20MB - 100MB`：尽量压缩后再上传，或放到分仓。
- `> 100MB`：不进主仓（GitHub 限制），由 `.gitignore` 自动维护。

每次新增资料后，建议先执行：

```powershell
powershell -ExecutionPolicy Bypass -File scripts/sync-light.ps1
git status
```

如果你只想下载某一部分目录，使用稀疏拉取：

```bash
git clone --filter=blob:none --sparse git@github.com:Halfapear/BuptInternationalSchool.git
cd BuptInternationalSchool
git sparse-checkout init --cone
git sparse-checkout set 大二下/DSP 大二下/JAVA
```

如果你长期维护大量原始视频/安装包，建议开一个分仓（例如 `BuptInternationalSchool-assets`）：

- 主仓只放课程文档、代码、轻量资料。
- 分仓放原始大文件和安装包。
- 主仓 README 里给分仓链接。

找电子书要会用zlibrary了

## 📄开源协议

该库使用Non-Commercial Creative Commons (CC BY-NC) 不允许将内容用于任何商业化的活动，包括直接出售、商业广告或通过内容产生利润的其他方式

前段时间何学长帮我们踩了坑，也许所有果园的同学们都需要了解一下开源许可证

你可以参考[open-source-licenses-in-depth](https://github.com/shaokeyibb/open-source-licenses-in-depth?tab=readme-ov-file)

【作为一个开源人，聊聊何同学的这次“侵权”】 https://www.bilibili.com/video/BV1BfSNYkEMT/?share_source=copy_web&vd_source=5cdb672a85240041a0304cff7a81bfba 不管你对开源是否感兴趣 都可以看看这个视频


# 北邮其他资料

如果你想浏览或补充北邮的其他资料 欢迎到[BUPTStudyMaterials](https://github.com/Halfapear/BUPTStudyMaterials) ~

# 赞助博主喝杯蜜雪

<img width="760" height="187" alt="image" src="https://github.com/user-attachments/assets/04212f85-17c1-4f38-92f5-567cff7d704e" />

<img width="200" alt="b371e45ffe9ae550d46cd5204661156e" src="https://github.com/user-attachments/assets/d2018a28-027b-47e9-b6d6-018ed1688a37" />

