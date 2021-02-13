# Transmis

备用机通知提醒转发程序，会在收到短信或者未接电话时自动发送提醒。

### 权限说明

本程序需要以下权限才能正常工作：

1. 网络权限：用于发送提醒
2. 短信权限：用于监测收到短信的事件，并读取短信内容
3. 电话权限：用于监听电话状态，以及监测未接电话

同时程序为了在被系统杀死后也能及时监测短信和电话，注册了系统级监听。

在某些系统上（如小米、华为等）可能需要手动在启动管理中将本程序设置为「允许被系统唤醒」

备注：如果在MIUI系统工作时短信内容和电话号码显示为null，请检查权限设置中是否被默认选择了「空白通行证」

### 提醒内容设置

短信提醒：

1. 标题：默认为「你的备用机收到了一条新短信」，可进行设置，暂不支持通配符
2. 内容：默认为「电话：%s \n内容：%s \n\n」，通配符「%s」分别代表短信号码和短信内容

电话提醒：

1. 标题：默认为「你的备用机有一个未接电话」，可进行设置，暂不支持通配符
2. 内容：默认为「电话：%s \n时间：%s \n响铃：%s 秒」，通配符「%s」分别代表电话号码、呼入时间和响铃时间

### 提醒插件设置

本程序的提醒由插件方式提供，可同时启用多个插件。

#### 邮件插件

1. SMTP服务器：邮箱的发件服务器地址，如 smtp.qq.com
2. 端口号：SMTP服务器的端口号，注意本程序默认启用了SSL加密，一般SMTP服务器的端口号为 465
3. 发件人昵称：默认为 Transmis，若收件人和发件人地址相同，收到提醒时可能不会显示自定义的昵称，而显示 “我”
4. 发件人邮箱：用于发送提醒邮件的邮箱，例如 xxxx@qq.com
5. 发件人密码/授权码：用于发送提醒邮件的密码，QQ邮箱可在邮箱设置中生成一组三方邮件服务专用的授权码，其他邮箱可能需要输入登录密码
6. 收件人邮箱：用于接收提醒的邮箱，例如 yyyy@qq.com

#### 钉钉插件

1. 任意拉两个人成立一个群组，然后将其他人踢出群。
2. 在群设置->群机器人中添加一个新的「自定义机器人」
3. 复制自定义机器人的链接中的“access_token=”后面的内容
4. 点击Transmis首页的钉钉机器人设置，输入复制的Token

#### MailGun插件

1. 申请 mailgun 账号。
2. 在 mailgun 中添加需要转发的邮件域名。
3. 申请 key，并输入。
4. 设置发件人和收件人信息。

#### Telegram插件

1. 与 @BotFather 私聊，申请 Bot。 
2. 拿到 apiToken 拼接 telegram 回调 url，如：https://api.telegram.org/bot{apiToken}/sendMessage。
3. 拿到自己的 id 并输入。

#### IFTTT Webhooks 插件

1. 创建 ifttt webhooks 应用，并设定 event ，拿到 key。
2. 输入 event 和 key。

### 插件开发

本程序支持简单易用的插件化开发，仅需两步即可实现一个提醒插件：

1. 创建属于你自己的插件，继承IPlugin接口并实现相关方法
2. 在PluginManager中注册你的插件

欢迎提交PR提供各类插件实现

### 其他可选项

1. 合并长短信：收到同一发信人的连续多条短信时会自动将内容进行合并，不会多次提醒

# License

    Copyright (C) 2021 dss886

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/
