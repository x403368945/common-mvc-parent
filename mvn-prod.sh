#!/usr/bin/env bash
# 生产环境发布脚本

# 删除 demo-main 模块下的静态资源文件
# rm-rf demo-main/src/main/resources/demo-web

# 编译静态资源
# cd demo-web
# gulp
# cd ..

# 执行生产环境打包命令
# 加上 -X 参数打印详细日志
mvnw.cmd clean install -Dmaven.test.skip=true -Pprod
# mvnw.cmd clean install -Dmaven.test.skip=true -Pprod
# mvnw.cmd clean install -Dmaven.test.skip=true -Pprod -X

cp app-demo/target/app-demo.jar app-demo.jar
