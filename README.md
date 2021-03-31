# 定时

## 澄清概念
Spring Boot Schedule 并不是Quartz！（CRON表达式在星期上略有不同）

## 开启 Spring Boot Schedule

```Java
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Scheduled(cron = "* * * * * *")
```

## 开启 Quartz


## 初始化工程
```shell
…or create a new repository on the command line
echo "# test-quartz" >> README.md
git init
git add README.md
git commit -m "first commit"
git branch -M main
git remote add origin git@github.com:xconfigurator/test-quartz.git
git push -u origin main

…or push an existing repository from the command line
git remote add origin git@github.com:xconfigurator/test-quartz.git
git branch -M main
git push -u origin main
…or import code from another repository
You can initialize this repository with code from a Subversion, Mercurial, or TFS project.
```