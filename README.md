基于micronaut框架完成的一个硬件资源信息收集。通过GraalVM工具打包成本地可执行文件。
## native 打包命令

```shell
    # 1、先执行 maven 打包命令
    mvn clean package -DskipTests
    # 2、使用 native-image-agent 辅助生成原生镜像所需要的配置文件,其中 config-output-dir 配置文件生成存放的位置了；
    java -agentlib:native-image-agent=config-output-dir=./src/main/resource/MATE-INFO/native-image/ -Dcom.sun.jna.nosys-true -jar target/xxx.jar
    # 3、原生镜像打包
    mvn -Pnative -DskipTests native:compile    
```

## GraalVM Native Image 打包优化

### 运行Agent生成配置
```shell
# 使用 native-image-agent 运行打包好的应用程序，并覆盖所有功能
java -agentlib:native-image-agent=config-output-dir=src/main/resources/META-INF/native-image -jar target/your-javafx-app.jar
```
* **config-output-dir** : 指定配置文件输出目录，推荐放在 src/main/resources/META-INF/native-image，这样构建时会自动包含
* 操作运行此命令后，务必在你的应用中执行所有可能的功能路径（点击所有按钮、打开所有窗口、触发所有通信等）。Agent 会记录下所有通过反射、资源访问、JNI 等动态行为使用的类和方法。
### 配置构建工具(maven)
```xml
<plugin>
    <groupId>org.graalvm.buildtools</groupId>
    <artifactId>native-maven-plugin</artifactId>
    <version>0.9.23</version> <!-- 使用最新稳定版 -->
    <executions>
        <execution>
            <id>build-native</id>
            <goals>
                <goal>build</goal>
            </goals>
            <phase>package</phase>
        </execution>
    </executions>
    <configuration>
        <!-- 核心优化选项 -->
        <buildArgs>
            <!-- 启用高级优化 -->
            -O2
            
            <!-- 禁止回退到 JVM 模式 -->
            --no-fallback
            
            <!-- 根据应用类型选择 GC -->
            <!-- 对于短时运行的应用 (如 CLI 工具) -->
            --gc=epsilon
            <!-- 对于长时运行的应用 (如服务器, HMI) -->
            --gc=z
            
            <!-- 显式包含必要的资源 (使用正则表达式) -->
            --resources=.*\\.fxml
            --resources=.*\\.css
            --resources=.*\\.(png|jpg|jpeg|gif)
            --resources=logback\\.xml <!-- 如果使用 logback -->
            
            <!-- (可选) 静态链接 (需要 musl libc 版本的 GraalVM) -->
            --static
            
            <!-- (可选) 在构建时初始化某些无副作用的类，减小运行时内存 -->
            --initialize-at-build-time=ch.qos.logback,org.slf4j
            
            <!-- (可选) 强制在运行时初始化的类 (避免构建时初始化失败) -->
            --initialize-at-run-time=java.awt,com.sun.javafx
            
            <!-- 启用详细构建信息 (可选，用于调试) -->
            --verbose
        </buildArgs>
        
        <!-- 指定主类 -->
        <mainClass>com.example.MainApp</mainClass>
        
        <!-- (重要) 启用从 META-INF/native-image 目录加载配置 -->
        <metadataType>MANAGED</metadataType>
    </configuration>
</plugin>
```
### 执行构建命令
```shell

# 清理并构建 (包括 native image)
mvn clean package

# 或者，如果只想构建 native image (跳过测试)
mvn clean package -DskipTests
```

### 交叉编译
