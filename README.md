这个工程是一个联系工程。基于micronaut框架完成的一个硬件资源信息收集。通过GraalVM工具打包成本地可执行文件。
## native 打包命令

```shell
    # 1、先执行 maven 打包命令
    mvn clean package -DskipTests
    # 2、使用 native-image-agent 辅助生成原生镜像所需要的配置文件,其中 config-output-dir 配置文件生成存放的位置了；
    java -agentlib:native-image-agent=config-output-dir=./src/main/resource/MATE-INFO/native-image/ -Dcom.sun.jna.nosys-true -jar target/
    # 3、原生镜像打包
    mvn -Pnative -DskipTests native:compile    
```