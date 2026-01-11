# 后端安全修复建议（自动生成）

以下为在本仓库 `backend` 模块中通过依赖扫描识别到的高优先级问题与建议修复措施。请按优先级逐项处理并在变更后运行完整回归测试。

## Top 优先级（P0）
- `cn.hutool:hutool-all:5.8.20` — CRITICAL（SQL 注入 / Aviator 模板）
  - 建议：升级到厂商修复的最新版本，或移除/替换 `aviator` 模板相关功能。

- `org.apache.tomcat.embed:tomcat-embed-core:9.0.79` — 多个 HIGH/CRITICAL CVE（multipart、HTTP/2、TOCTOU 等）
  - 建议：通过升级 Spring Boot 到最新 2.7.x 补丁或将 Tomcat 提升到安全版本（9.0.104+ / 按 CVE 建议版本）来修复；临时缓解：在 Tomcat/应用层启用严格的上传限制与 header/HTTP2 限制。

- `commons-fileupload:commons-fileupload:1.4`（HIGH）
  - 建议：升级到 `1.6` 或受修复的版本，并在服务器端明确设置 `FileUploadBase#setFileCountMax` / 文件大小上限。

- `commons-io:commons-io:2.11.0`（HIGH）
  - 建议：升级到 `2.14.0` 或更高。

- `org.yaml:snakeyaml:1.30`（HIGH）
  - 建议：升级至 `1.31+`（或最新），并使用 `SafeConstructor`/限制嵌套深度解析非受信任 YAML。

- `ch.qos.logback:logback-core:1.2.12` / `logback-classic:1.2.12`（HIGH）
  - 建议：升级或禁用不必要的 receiver/Janino 插件；审查日志配置，避免允许未经授权写入配置文件。


## 次优先（P1）
- `commons-compress:1.21` → 建议升级到 `1.26`
- `commons-lang3:3.12.0` → 建议升级到 `3.18.0`
- `com.google.guava:27.0.1-android` → 建议升级到 `32.0.1`（注意 Android vs JVM 区别）
- `com.mysql:mysql-connector-j:8.0.33` → 升级到受支持的安全补丁（参照 MySQL 官方 CVE 公告）


## 快速操作建议（步骤）
1. 在小型分支上按优先级逐个升级依赖，优先处理 P0。对每次升级执行 `mvn -DskipTests=false clean verify`。
2. 若依赖为传递依赖且无法直接升级，使用 `<dependencyManagement>` 或 `<exclusions>` 临时覆盖版本或排除传递依赖，然后测试。
3. 升级 Spring Boot 到最新 2.7.x 补丁以取到框架管理的安全补丁（Tomcat、Jackson 等）。评估 3.x 升级作为长期计划。
4. 针对 `snakeyaml` 使用 `SafeConstructor`，对于文件上传使用明确的文件/部件数量及大小限制。

## 推荐的 dependencyManagement 示例（请在测试通过后合并）
```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.apache.commons</groupId>
      <artifactId>commons-compress</artifactId>
      <version>1.26</version>
    </dependency>
    <dependency>
      <groupId>commons-fileupload</groupId>
      <artifactId>commons-fileupload</artifactId>
      <version>1.6</version>
    </dependency>
    <dependency>
      <groupId>commons-io</groupId>
      <artifactId>commons-io</artifactId>
      <version>2.14.0</version>
    </dependency>
    <dependency>
      <groupId>org.apache.commons</groupId>
      <artifactId>commons-lang3</artifactId>
      <version>3.18.0</version>
    </dependency>
    <dependency>
      <groupId>org.yaml</groupId>
      <artifactId>snakeyaml</artifactId>
      <version>1.33</version>
    </dependency>
    <dependency>
      <groupId>com.google.guava</groupId>
      <artifactId>guava</artifactId>
      <version>32.0.1-jre</version>
    </dependency>
  </dependencies>
</dependencyManagement>
```

> 说明：上述版本为建议版本，需在你的 CI/环境中进行回归测试以验证兼容性；对于 Spring Boot 管理的库（Tomcat、Jackson），优先通过升级 Spring Boot 补丁来获得安全修复。

---
自动生成于仓库扫描后。若需我替你在 `backend/pom.xml` 中直接应用上述 `dependencyManagement`，请回复 `apply`。若要我先生成逐项 PR（每次升级一个依赖并运行测试），回复 `pr`。
