- [背景](#背景)
- [效果](#效果)
- [Link](#link)
- [TODO](#todo)


<small><i><a href='http://ecotrust-canada.github.io/markdown-toc/'>Table of contents generated with markdown-toc</a></i></small>

## 背景
快速导出 ChangeList 的文件，并附带文件路径列表

当使用 PHP 为语言进行开发时，时常有增量覆盖系统环境中的个别文件，并且由于个人开发项目时，ChangeList 进行管理。于是使用此插件，可以批量导出某个 ChangeList 中的文件

## 效果

![](docs/gif/20231009_105331.gif)

1. 在一个有初始化git的仓库中.你可以对修改的文件进行导出
2. 方式一:在commit的左侧窗口中,选择修改的 ChangeList 组,右键导出
3. 方式二:在搁置中,右键导出
4. 方式三:在 Git Log 中,选择某个 commit, 右键导出

## Link
- [jetbrains plugins 插件主页](https://plugins.jetbrains.com/plugin/21528-changefile-export)
## TODO

- [x] 导出指定的 ChangeList 中的文件，并生成文件夹
- [ ] 针对ChangeList 中勾选的文件进行导出
- [ ] 编写脚本，在创建前，根据 path.txt ,对目标文件进行备份

