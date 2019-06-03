:: 生产环境发布脚本

:: 删除 demo-main 模块下的静态资源文件
:: rd /s /q demo-main\src\main\resources\demo-web

:: 编译静态资源
:: cd demo-web
:: call gulp

:: 执行生产环境打包命令
:: 加上 -X 参数打印详细日志
cd ..
call mvn clean install -Dmaven.test.skip=true -Pprod
:: call mvn clean install -Dmaven.test.skip=true -Pprod
:: call mvn clean install -Dmaven.test.skip=true -Pprod -X

copy demo-deploy\target\demo.war demo.war

pause