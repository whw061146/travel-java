# 智能旅游规划器网站项目开发步骤

本指南详细描述了基于 **Java MVC 架构** 开发的智能旅游规划器网站项目的完整开发步骤。项目采用**前后端分离架构**，技术栈包括：前端 **Vue + HTML/CSS/JavaScript**，后端 **Spring Boot (Java)**，数据库 **MySQL**，以及用于AI推荐的 **Kimi API（免费版）**。项目支持本地运行部署，主要功能涵盖用户注册登录、旅游行程规划、AI推荐目的地和路线、历史查询记录和用户反馈等模块。开发过程中将结合至少三种**设计模式**（如单例模式、工厂模式、策略模式等）以提高系统的可维护性和扩展性。下面将按阶段详细说明开发步骤、任务安排、项目结构、命名规范、各模块功能、开发顺序以及创新点。

## 各阶段任务安排

为确保项目顺利进行，我们将开发过程划分为多个阶段，每个阶段侧重不同的任务：

1. **需求分析与总体设计阶段**: 明确项目需求和功能模块，确定系统架构（采用前后端分离的MVC模式）、技术选型和设计模式的应用方案，撰写概要设计文档。
2. **开发环境搭建与项目初始化阶段**: 配置开发环境（安装JDK、MySQL、Node.js等），初始化后端Spring Boot项目和前端Vue项目，设置基本的工程结构和依赖，确保前后端运行的基础环境就绪。
3. **数据库设计阶段**: 设计数据库ER模型和表结构，包括用户、行程计划、历史记录、反馈等表；编写SQL脚本或通过建模工具创建数据库，在MySQL中建立相应的数据库和表，为后续开发提供数据基础。
4. **后端接口与业务实现阶段**: 根据功能模块逐一实现后端功能：
   - 实体类和数据访问层（DAO/Repository）开发
   - 业务服务层（Service）实现核心逻辑（集成设计模式，如策略模式封装AI推荐算法，工厂模式创建策略实例，单例模式管理AI接口客户端等）
   - 控制层（Controller）编写RESTful接口（用户注册登录、获取行程规划、查询历史、提交反馈等）
   - 集成Kimi AI接口进行智能推荐
   - 编写必要的配置（如安全配置、跨域配置等）
5. **前端页面设计与实现阶段**: 基于Vue框架开发前端界面：
   - 设计页面原型（注册/登录页、行程规划页、历史记录页、反馈页等），并划分组件
   - 实现各Vue组件和页面，定义路由导航，各页面通过表单和交互与后端API对接
   - 使用axios等工具与后端通信，处理登录状态（如保存token）、展示AI推荐结果、提交反馈等
   - 确保前端有良好的交互体验（如加载状态提示、表单校验等）
6. **联调测试与优化阶段**: 前后端集成联调，逐一测试各功能模块：
   - 测试用户注册、登录流程，保证认证机制正确
   - 测试行程规划功能，对不同输入检查AI推荐结果展示及保存历史记录功能
   - 测试历史查询记录展示、反馈提交流程
   - 修复在联调中发现的问题，优化性能（如对重复查询结果进行缓存以减少AI调用次数）和安全性（如密码加密存储、防止SQL注入、设置接口访问权限等）
7. **部署交付阶段**: 将前后端代码打包部署在本地环境运行：
   - 后端使用`mvn package`打包Spring Boot可执行JAR，前端使用`npm run build`打包静态资源
   - 将前端构建产物部署到后端的`static`目录或通过Nginx等本地服务托管，实现前端页面和后端服务的集成访问
   - 编写运行说明文档（README），指导如何启动后端服务、配置数据库、访问前端页面等
   - 最终交付包括源代码、数据库脚本以及本开发步骤文档`step.md`

各阶段在实际开发中可能会迭代进行，例如在联调测试阶段发现需求变化需要返回调整部分设计。但总体上遵循上述阶段顺序可以确保项目有条不紊地开展。

## 文件结构与命名规范

良好的项目文件结构和命名规范有助于团队协作和代码维护。本项目采用前后端分离存储代码，并为文档单独设立目录。下面给出建议的文件目录结构和命名规范：
```
smart-travel-planner/            -- 项目根目录
├── backend/                     -- 后端Spring Boot项目
│   ├── src/main/java/com/xxx/travelplanner/   -- Java源代码根包（按实际包名替换xxx）
│   │   ├── TravelPlannerApplication.java      -- 应用启动入口类（Spring Boot主类）
│   │   ├── config/                -- 配置类包（全局配置、Bean配置等）
│   │   │   └── KimiApiClient.java             -- Kimi API客户端封装（单例模式，封装AI接口调用）
│   │   ├── model/entity/          -- 实体类包（对应数据库表的Domain模型）
│   │   │   ├── User.java                      -- 用户实体（用户表，对应用户信息）
│   │   │   ├── Plan.java                      -- 行程计划实体（行程表，保存AI推荐结果等）
│   │   │   ├── Feedback.java                  -- 反馈实体（反馈表，保存用户反馈）
│   │   │   └── ... （其他实体，如需要）
│   │   ├── repository/           -- 数据访问层包（DAO仓库接口）
│   │   │   ├── UserRepository.java            -- 用户数据访问接口（继承JpaRepository或定义MyBatis映射）
│   │   │   ├── PlanRepository.java            -- 行程计划数据访问接口
│   │   │   └── FeedbackRepository.java        -- 反馈数据访问接口
│   │   ├── service/              -- 服务层接口包（业务逻辑接口）
│   │   │   ├── UserService.java               -- 用户服务接口（注册、登录等）
│   │   │   ├── PlanService.java               -- 行程规划服务接口（生成行程、查询历史）
│   │   │   ├── FeedbackService.java           -- 反馈服务接口
│   │   │   ├── RecommendationStrategy.java    -- 策略模式：推荐策略接口（定义推荐行程算法）
│   │   │   ├── AIRecommendationStrategy.java  -- 推荐策略实现：基于AI（Kimi API）的实现
│   │   │   ├── BasicRecommendationStrategy.java -- 推荐策略实现：基于固定规则的实现（备用方案）
│   │   │   └── RecommendationFactory.java     -- 工厂模式：推荐策略工厂，根据需要选择AI或其他策略
│   │   ├── service/impl/         -- 服务层实现包（业务逻辑实现）
│   │   │   ├── UserServiceImpl.java           -- 用户服务实现（实现注册、登录逻辑）
│   │   │   ├── PlanServiceImpl.java           -- 行程服务实现（实现调用推荐策略、保存行程等逻辑）
│   │   │   └── FeedbackServiceImpl.java       -- 反馈服务实现（实现保存反馈逻辑）
│   │   ├── controller/           -- 控制层包（处理HTTP请求的接口）
│   │   │   ├── UserController.java            -- 用户控制器（注册、登录接口）
│   │   │   ├── PlanController.java            -- 行程控制器（行程规划和历史记录接口）
│   │   │   └── FeedbackController.java        -- 反馈控制器（提交反馈接口）
│   │   └── util/                  -- 工具类包（公共工具，如加密、日期格式化等）
│   ├── src/main/resources/
│   │   ├── application.yml                   -- 应用配置文件（数据库连接配置、Kimi API密钥等）
│   │   ├── schema.sql                        -- 初始化数据库的SQL脚本（可选）
│   │   └── static/                           -- （可选）前端静态资源目录（构建后的前端文件）
│   ├── src/test/java/...                     -- 测试代码目录（与主代码包结构对应）
│   └── pom.xml                               -- Maven配置文件（依赖声明）
│
├── frontend/                    -- 前端Vue项目
│   ├── public/
│   │   └── index.html                      -- 应用入口HTML模板
│   ├── src/
│   │   ├── assets/                         -- 资源文件（图片、CSS等）
│   │   ├── components/                     -- 公共组件
│   │   │   ├── NavBar.vue                     -- 导航栏组件（例如顶栏菜单）
│   │   │   └── PlanCard.vue                   -- 行程展示卡片组件（用于显示推荐的某日行程）
│   │   ├── views/                          -- 页面组件
│   │   │   ├── Home.vue                       -- 首页/欢迎页组件
│   │   │   ├── Register.vue                   -- 用户注册页组件
│   │   │   ├── Login.vue                      -- 用户登录页组件
│   │   │   ├── Plan.vue                       -- 行程规划页组件（输入需求，展示AI推荐结果）
│   │   │   ├── History.vue                    -- 历史记录页组件（显示用户过去查询的行程计划）
│   │   │   └── Feedback.vue                   -- 用户反馈页组件（提交对行程或系统的反馈）
│   │   ├── router/
│   │   │   └── index.js                    -- 前端路由配置（定义各页面路由路径）
│   │   ├── store/                          -- 状态管理（Vuex或Pinia，可选，用于管理用户登录状态等）
│   │   ├── App.vue                         -- 根组件
│   │   └── main.js                         -- 前端应用入口JS（初始化Vue实例，挂载路由等）
│   ├── package.json                        -- 项目依赖配置文件
│   └── vue.config.js / vite.config.js      -- 构建配置文件（取决于使用Vue CLI或Vite）
│
├── docs/                        -- 文档目录
│   ├── step.md                           -- 项目开发步骤文档（即本文件）
│   └── README.md                         -- 项目说明文档（介绍项目概况、运行方法等）
└── README.md                    -- 仓库说明（简要说明项目，可引用docs下更详细文档）
```


### 命名规范

- **后端命名**: 按照Java惯例使用驼峰式命名。包名全部小写，采用组织域名+项目名组成，如`com.example.travelplanner`。类名使用大写开头的驼峰形式（如`UserServiceImpl`），接口以功能名命名（如`UserService`），实现类在接口名后加`Impl`。Controller类以业务模块+Controller结尾（如`PlanController`），Service以Service结尾，Repository以Repository结尾。数据库表对应的实体类名采用单数形式（如用户表`users`对应实体类`User`）。

- **前端命名**: 采用简洁有意义的英文命名。Vue单文件组件命名使用大写开头的驼峰形式(PascalCase)，文件名与组件名一致（如`Login.vue`包含组件`Login`）。组件名应体现组件功能，如`NavBar.vue`表示导航栏组件。路由路径尽量使用简短单词（如`/login`, `/plan`）。CSS样式命名可采用kebab-case或BEM方法，避免使用中文拼音。变量命名使用驼峰式，如`userName`，常量使用全大写加下划线，如`API_BASE_URL`。

- **数据库命名**: 数据表名、字段名建议使用全小写下划线风格。例如用户表命名为`user`或`tb_user`，字段使用`user_name`等。每张表的主键列统一命名为`id`，并使用自增或UUID。外键列命名为`<关联表>_id`形式，如行程表中的用户ID外键列可命名为`user_id`。

- **文档命名**: 文档使用有意义的英文或中文命名。主文档使用`README`或项目名作为文件名，如`README.md`，开发步骤文档命名为`step.md`。文档内的标题层级清晰，使用中文描述确保阅读无障碍。

通过以上结构划分和命名规范，可以保证项目组织清晰，各层次职责明确，方便团队协同和后期维护。

## 模块与主要文件功能说明

### 后端模块

#### 用户管理模块 👌

- User.java：用户实体类，对应数据库中的用户表，包含用户基本信息字段（如id、用户名、邮箱、密码哈希、注册时间等）。
- UserRepository.java：用户数据访问接口，提供对用户表的CRUD操作。如使用Spring Data JPA，可继承JpaRepository从而自动拥有常用方法；如使用MyBatis，则定义相应的@Mapper接口和XML。
- UserService.java：用户业务接口，定义用户注册register(UserDto user)、登录login(String username, String password)等方法。
- UserServiceImpl.java：用户业务接口实现，包含具体逻辑：
  - 注册：检查用户名/邮箱是否已存在，密码加密存储，调用UserRepository保存新用户等。
  - 登录：验证用户名密码，密码比对（使用加密算法检查明文密码与存储哈希是否匹配），成功则生成会话或JWT令牌返回前端。
- UserController.java：用户控制器，提供REST接口:
  - POST /api/register: 接收前端提交的注册信息，调用UserService注册新用户，返回操作结果（成功或失败原因）。
  - POST /api/login: 接收登录凭证，调用UserService验证。成功则建立会话（例如生成JWT Token或在后端Session记录登录态），返回登录成功和用户信息/令牌给前端。
- **注意**: 如采用JWT，无状态认证，需要在登录成功时返回token，前端后续请求附带该token；如采用Session，需要配置Spring Session或使用默认Http Session，并可能需要配置跨域允许cookie。

#### 行程规划模块  👌

- Plan.java：行程计划实体类，对应数据库中的行程/查询历史记录表。包含字段如id、用户id、查询内容（如目的地、天数、偏好等描述信息）、AI返回的行程结果（可以是文本描述或JSON结构）、生成时间等。
- PlanRepository.java：行程数据访问接口，提供保存新行程和按条件查询历史行程的方法。例如findByUserId按用户查询其历史计划列表。
- RecommendationStrategy.java：推荐策略接口（策略模式），定义生成行程的方法，例如generatePlan(TripRequest request)，参数封装用户请求的行程需求（目的地、时间等），返回Plan结果或行程方案数据。
- AIRecommendationStrategy.java：推荐策略接口的实现类，封装基于**AI接口（Kimi API）**的行程生成逻辑。核心功能：
  - 根据用户请求参数构建提示词Prompt ( 例如："请为我规划一次从{出发地}到{目的地}的{天数}日旅游行程，包括每日景点和路线推荐。")。
  - 调用KimiApiClient发送请求到Kimi大模型接口，获取AI生成的行程方案文本或数据。
  - 解析AI返回结果（如果返回纯文本，则直接存文本；如果我们约定AI返回JSON，则将其解析为Plan对象结构）。
  - 返回生成的行程方案（封装为Plan对象，包含计划详情）。
- BasicRecommendationStrategy.java：推荐策略接口的另一实现类，封装一个基本策略（不依赖AI）用于行程生成。可以简单实现为：基于预设的规则或模版生成旅行计划。例如根据目的地查找本地预置的热门景点清单，组合成行程。此策略可作为AI接口不可用时的备用方案，也是为了演示策略模式的扩展性。
- RecommendationFactory.java：推荐策略工厂类（工厂模式），负责根据一定条件选择并创建RecommendationStrategy实例。比如：
  - 默认返回AIRecommendationStrategy实例以使用AI推荐；
  - 当检测到AI接口不可用或用户选择“基础推荐”模式时，返回BasicRecommendationStrategy实例。 通过工厂封装实例化逻辑，Controller或Service层只需从工厂获取策略并调用，无需关心具体实现类，有助于以后扩展新的策略。
- PlanService.java：行程服务接口，定义行程规划相关业务方法，如Plan createPlan(TripRequest req)生成新行程、List<Plan> listHistory(Long userId)查询历史行程列表等。
- PlanServiceImpl.java：行程服务实现：
  - createPlan(TripRequest req): 内部会通过RecommendationFactory选择策略Strategy实例，调用其generatePlan(req)得到AI推荐结果；将结果持久化保存（PlanRepository.save）并返回给调用方。
  - listHistory(userId): 调用PlanRepository按用户ID查询历史Plan列表，返回前端。
  - （可选）在生成行程前检查用户的请求是否与最近一次查询重复，若是则可以直接返回已有结果以减少重复AI调用（简单缓存机制）。
- PlanController.java：行程控制器，提供行程相关接口:
  - POST /api/plan: 接收用户提交的行程需求参数（通过请求体，如目的地、天数、偏好等），调用PlanService.createPlan生成行程计划，返回计划结果给前端。结果中包括每日推荐的目的地和路线（具体展现由前端决定格式）。
  - GET /api/history: （需登录）获取当前用户的历史行程列表，调用PlanService.listHistory(userId)返回该用户所有过去查询过的行程简要信息。前端可点击某条记录再查看详情或重新规划。
  - **注意**: 该模块涉及将AI返回的数据保存数据库，对于可能较长的行程文本，可考虑在数据库中使用TEXT类型字段存储；另外要考虑AI接口的调用时延，可在前端调用时给予加载提示。


#### AI接口集成模块 👌

- KimiApiClient.java：AI接口客户端类，用于对接Kimi AI开放接口。（单例模式） 为确保全局只有一个AI客户端实例维护接口配置和授权，此类可设计为单例。
  - 属性包括Kimi API的基础URL、API Key等，从配置文件application.yml读取配置注入。
  - 方法sendRequest(String prompt)发送请求：使用HTTP客户端（如Spring的RestTemplate或WebClient）向Kimi API发送请求，传入prompt并附带API授权信息，等待并接收响应。
  - 方法返回AI回应的内容（字符串或结构化数据）。如果Kimi API返回分段流式数据，可在此类内处理拼接。
  - 由于是免费版API，可能有调用频率和长度限制，需要在实现时考虑重试或限流策略。
  - 该类被设计为单例：可以通过Spring Boot配置为@Component并默认为单例，也可以自行实现单例模式（例如私有构造函数+静态getInstance方法）确保整个应用共享一个HTTP客户端和配置，以减少资源开销和避免多次加载配置。
- 设计模式应用:
  - 单例模式：KimiApiClient正是应用了单例模式，确保系统中对AI接口的调用入口统一。这样也方便在此处集中处理AI接口的细节，如错误重试、日志记录等。
  - 工厂模式：前述RecommendationFactory根据需要决定返回AI策略或基本策略，屏蔽了对象创建的细节。
  - 策略模式：前述RecommendationStrategy及其不同实现，让行程生成算法可以灵活切换，符合开闭原则，方便将来扩展（比如增加新的算法或接入不同AI服务）。
- 历史记录模块: （该模块与行程模块紧密相关，部分已涵盖）
  - 利用Plan实体和PlanService.listHistory实现。无需新增实体，可以在Plan表中通过用户ID区分记录。
  - PlanController中的GET /api/history接口返回历史记录列表，前端可展示记录概要并通过指定ID获取详细内容。
  - 如需更复杂的历史管理，可引入HistoryService专门处理，但当前设计直接复用Plan的服务即可。
- 用户反馈模块:
  - Feedback.java：反馈实体类，对应反馈表。包含字段如id、用户id（可为空表示匿名反馈）、反馈内容、提交时间、处理状态等。
  - FeedbackRepository.java：反馈数据访问接口，提供保存新反馈和查询反馈记录的方法。开发初期可以只用到保存。
  - FeedbackService.java：反馈服务接口，定义方法如submitFeedback(Long userId, String content)提交反馈。
  - FeedbackServiceImpl.java：反馈服务实现，简单调用FeedbackRepository保存反馈内容。如有业务需求，可在此添加例如垃圾词过滤、自动回复等逻辑。
  - FeedbackController.java：反馈控制器，提供接口:
     - POST /api/feedback: 接收用户反馈内容（如果用户已登录可一并获得userId，未登录则userId为空或由前端标识匿名），调用FeedbackService保存反馈。返回提交成功或失败信息。
     - （可选）如果需要管理反馈，还可以提供管理员查询或删除反馈的接口，但本项目重点不在后台管理，暂不实现。
      
#### 历史记录模块 👌

- History.vue: 历史记录页组件。用户登录后可以查看过去提交的所有行程计划请求及结果摘要：
  - 页面加载时调用后端GET /api/history获取列表数据（每条包括请求概要如目的地和日期、生成时间等）。
  - 列表显示每次行程规划的概要信息，可能按时间排序。
  - 用户可以点击某一条记录，查看完整详情。实现方式可以是：
     - 简单起见，路由跳转到Plan.vue并传入该记录ID参数，再由Plan.vue判断如果有参数则通过GET /api/plan/{id}获取该行程详情显示。或者
     - 在History.vue中直接内嵌显示详情：通过在点击时调用一个获取详情的接口（如果没有专门接口，也可让history接口直接返回详情文本），然后展开显示。
  - 允许用户对某条历史记录重新规划（也就是复制之前的条件再次调用AI），可在每项提供“再次规划”按钮，点击后将相应条件填入Plan.vue的表单（可以借助路由传参或共享状态）

#### 用户反馈模块 👌

- Feedback.java：反馈实体类，对应反馈表。包含字段如id、用户id（可为空表示匿名反馈）、反馈内容、提交时间、处理状态等。
- FeedbackRepository.java：反馈数据访问接口，提供保存新反馈和查询反馈记录的方法。开发初期可以只用到保存。
- FeedbackService.java：反馈服务接口，定义方法如submitFeedback(Long userId, String content)提交反馈。
- FeedbackServiceImpl.java：反馈服务实现，简单调用FeedbackRepository保存反馈内容。如有业务需求，可在此添加例如垃圾词过滤、自动回复等逻辑。
- FeedbackController.java：反馈控制器，提供接口:
  - POST /api/feedback: 接收用户反馈内容（如果用户已登录可一并获得userId，未登录则userId为空或由前端标识匿名），调用FeedbackService保存反馈。返回提交成功或失败信息。
  - （可选）如果需要管理反馈，还可以提供管理员查询或删除反馈的接口，但本项目重点不在后台管理，暂不实现

### 前端模块


#### 用户界面与路由 👌

- **App.vue**: 应用根组件，通常包含导航栏和 `<router-view>`。可在此放置适用布局，例如页眉部分导航。

- **NavBar.vue**: 导航栏组件，包含网站的菜单链接。当用户登录后，导航栏显示如“行程规划”，“历史记录”，“反馈”，“退出登录”等菜单，未登录时显示“登录/注册”按钮。通过路由链接来实现页面跳转。

- **router/index.js**: 前端路由的配置，定义各URL路径对应加载的视图组件。例如：

  - `/register` → `Register.vue`
  - `/login` → `Login.vue`
  - `/plan` → `Plan.vue`
  - `/history` → `History.vue`
  - `/feedback` → `Feedback.vue`

- **设置路由守卫**: 对于需登录才能访问的路由（如 `/plan`, `/history`, `/feedback`），在进入路由时检查用户是否已登录（例如检查是否有有效JWT token或登录状态），未登录则重定向到登录页面。这样可以防止未授权的访问。


#### 注册与登录页面 👌

- **`Register.vue`**: 注册页组件。包含一个注册表单，字段有用户名、邮箱、密码（可能还有确认密码）。使用双向绑定(v-model)获取用户输入，点击提交时通过调用后端API完成注册。
  - **调用接口**: `POST /api/register`，发送用户输入的注册信息(JSON格式)。
  - **展示反馈**: 根据后端响应显示注册成功提示或错误信息（如用户名已存在等）。
  - 注册成功后，通常引导用户去登录页面。

- **`Login.vue`**: 登录页组件。包含登录表单，字段有用户名(或邮箱)和密码。
  - 点击登录按钮，通过axios调用`POST /api/login`接口提交凭证。
  - 如果登录成功，后端返回用户信息和令牌(token)。前端需保存此登录状态：
    - 如果用JWT，前端将token保存到localStorage或sessionStorage，后续请求在axios拦截器中附加该token在Authorization头；
    - 如果用Session+Cookie，浏览器会自动保存Set-Cookie的会话ID，axios请求允许携带凭证即可。
  - 登录成功后，前端路由跳转到行程规划页或首页，并更新导航栏显示已登录状态（比如显示用户名和退出按钮）。
  - 登录失败则提示错误，例如“用户名或密码不正确”。

#### 行程规划页面 👌

- **`Plan.vue`**: 行程规划页组件。核心功能页面，包含：

  - **输入区域**: 用于采集用户的旅行需求。可以是结构化表单（字段如目的地、出发地、出行日期或季节、天数、偏好类型如美食/文化/自然等）或简单的文本域让用户自由描述。推荐使用结构化表单并在前端组装prompt：
    - 例如下拉选择目的地城市，输入天数，勾选兴趣偏好标签（海滩、博物馆、美食...），这样可以指导AI更好地输出结果。

  - **提交按钮**: 点击后触发调用后端`POST /api/plan`接口。调用时，将用户选择/填写的需求参数整理成请求体JSON发送。可考虑在发送前组装出prompt字符串，如上所述，也可将各字段直接传给后端由后端组装prompt（两种皆可，根据设计取舍）。

  - **加载状态**: 因AI推荐需要时间，提交后应显示“正在为您规划行程，请稍候...”的加载提示，防止用户重复提交。

  - **展示结果区域**: 收到后端返回的行程计划后，将其友好地展示给用户。
    - 如果结果是多日行程文本，可以按照天数拆分展示，每天的计划用卡片(`PlanCard.vue`)或列表显示，每个地点可用列表项或段落表示。
    - 如AI返回JSON结构（包含每日景点数组等），则可以编程形式渲染UI。

  - **可能的交互**: 用户可以调整输入再次规划，或点击某日计划展开详情（如果有更详细信息，如景点介绍）。这些属于扩展功能，基础功能下展示文本即可。

  - **提示**: 
    - 如果AI结果过长，可在界面增加滚动；
    - 如果结果不满意，用户可修改条件重试。

  
#### 历史记录页面 👌

- **`History.vue`**: 历史记录页组件。用户登录后可以查看过去提交的所有行程计划请求及结果摘要：

  - **页面加载时**: 调用后端`GET /api/history`获取列表数据（每条包括请求概要如目的地和日期、生成时间等）。
  - **列表显示**: 每次行程规划的概要信息，可能按时间排序。
  - **用户点击记录**: 查看完整详情。实现方式可以是：
    - 简单起见，路由跳转到`Plan.vue`并传入该记录ID参数，再由`Plan.vue`判断如果有参数则通过`GET /api/plan/{id}`获取该行程详情显示。
    - 或者在`History.vue`中直接内嵌显示详情：通过在点击时调用一个获取详情的接口（如果没有专门接口，也可让`history`接口直接返回详情文本），然后展开显示。

  - **重新规划功能**: 允许用户对某条历史记录重新规划（即复制之前的条件再次调用AI），可在每项提供“再次规划”按钮，点击后将相应条件填入`Plan.vue`的表单（可以借助路由传参或共享状态）。


#### 用户反馈页面  👌

- **`Feedback.vue`**: 反馈页组件。提供一个表单让用户提交对系统或行程推荐的反馈意见。

  - **输入区域**: 包含一个多行文本域用于用户输入反馈内容，字数可有限制提示（比如200字以内）。
  - **可选项**: 包含一些选项/评分（例如对推荐结果的满意度1-5星），这取决于需求定义。

  - **提交按钮**: 调用后端`POST /api/feedback`接口，将反馈内容发送。可以附带用户ID（后端可从token解析得到）和可能的评分。
  - **提交成功后**: 提示感谢反馈。由于反馈通常不需要即时处理结果，后台只需存储，前端不需要等待任何返回的具体数据，只需确认提交成功。

  - **未登录用户反馈**: 若未登录用户也可提交反馈，前端应允许匿名提交（后端userId为空即可），或强制登录后反馈，这取决于产品要求。本项目可允许匿名反馈以降低用户门槛。

#### 通用交互组件 👌

除了上述主要页面组件，可能还包括一些辅助组件：

- **`LoadingSpinner.vue`**（加载动画组件）：在等待AI结果时显示。
- **`Message.vue`**（消息/通知组件）：用于统一弹出提示成功或错误信息，或者直接使用现有UI库提供的`Message`功能。

这些组件可按需创建，提升用户体验。

#### 与后端交互

前端通过axios发起HTTP请求。在`main.js`中配置axios默认`baseURL`指向后端API（如`http://localhost:8080/api`）。

- **JWT认证**: 如果使用JWT认证，配置axios请求拦截器，在有token时为每个请求添加`Authorization: Bearer <token>`头部；响应拦截器处理token过期的情况（如遇401未授权，自动跳转登录）。

- **数据格式处理**: 前端还需处理后端返回的数据格式。可以约定后端所有成功响应包装为 `{ code:0, data: ..., message: "OK" }` 形式，这样前端可以统一判断`code`是否为0来确定请求是否成功。若失败（`code != 0`），统一显示`message`错误信息。

- **错误处理**:
  - 包括表单验证提示（如必填项为空、高亮错误字段）。
  - 网络错误或服务器错误提示（如弹出“请求失败，请稍后重试”）。
    
通过以上模块划分和文件说明，开发者可以清楚每个文件/模块的作用。例如，当需要修改AI推荐逻辑时，只需修改AIRecommendationStrategy内部实现；要调整前端展示效果，可以修改对应Vue组件而不影响后台逻辑。各部分职责清晰，松耦合易扩展。

## 文件编写的先后顺序与注意事项
在实际开发中，遵循合理的顺序编写文件和模块有助于快速构建可运行的最小原型，然后再逐步完善功能。下面给出一个推荐的开发顺序以及每步需要注意的事项
#### 建立后端基础结构

（先后端并行时亦可先搭后端）

#### 1. 建立后端基础结构: （先后端并行时亦可先搭后端）

- **使用Spring Initializr**: 新建Spring Boot项目，添加必要依赖：
  - **Web**（提供REST接口）
  - **JPA或MyBatis**（数据库访问，二选一）
  - **MySQL驱动**
  - **可选**: **Spring Security**用于JWT

- **编写`TravelPlannerApplication.java`**: 启动类并测试空项目运行是否成功（确保环境配置正确）。

- **创建基础的包结构**:
  - 创建基础的包结构（如 `controller`, `service`, `repository`, `model`），创建一个简单的Controller（如`HelloController`提供`/api/hello`测试接口）启动项目访问以验证基础架构正常，后续再删除或改造这个测试部分。

- **注意**: 
  - 先配置`application.yml`，至少包括数据库连接配置（如`url`, `username`, `password`），以及可能的`server.port`、`logging`等。
  - 为防止跨域问题，在开发阶段可以临时全局允许CORS（在`TravelPlannerApplication`类或配置类上加`@CrossOrigin`注解，或定义一个`WebMvcConfigurer`配置`addCorsMappings`）。

#### 2.设计并创建数据库

1. **根据需求设计数据库表结构**:
   - 先在MySQL中创建项目数据库，例如命名为`travel_planner`。

2. **编写SQL脚本或使用数据库客户端创建表**:
   - **用户表**包含字段：
     - `id` (INT自增主键)
     - `username` (varchar)
     - `email` (varchar)
     - `password_hash` (varchar)
     - `created_time` (datetime)
     - `etc.`  
     - 并对`username`或`email`加唯一索引。

   - **行程表**包含字段：
     - `id` (自增主键)
     - `user_id` (外键关联用户)
     - `request` (text存储请求条件文本或JSON)
     - `result` (text存储AI返回行程)
     - `created_time` (datetime)
     - `etc.`

   - **反馈表**包含字段：
     - `id` (自增主键)
     - `user_id` (可为空)
     - `content` (text)
     - `rating` (int 可选)
     - `created_time` (datetime)
     - `etc.`
      
#### 3. **编写实体类和Repository(DAO)**

-  **根据数据库表创建实体类**:
   - 根据数据库表，创建JPA实体类或MyBatis对应的实体和映射：
     - 实体类字段命名与表列名映射（使用JPA时，如命名不一致可用`@Column`注解修饰）。
     - 添加必要的关系注解（如User和Plan一对多关系，在User实体上可有`List<Plan> plans`用`@OneToMany`标注；Plan中用`@ManyToOne`关联User）。如果不需要导航查询也可不建双向关系，仅存外键ID即可。

-  **创建Repository接口**:
   - 如使用JPA，`UserRepository extends JpaRepository<User, Long>`，可直接获取常用方法。自定义查询可用方法名解析或`@Query`注解。
   - 如使用MyBatis，创建Mapper接口并在`resources/mapper/`下编写对应XML。确保`application.yml`中配置了MyBatis的mapper扫描路径。

- **注意** 命名上Repository接口名应和实体类对应（如`User` -> `UserRepository`）。若用MyBatis需要考虑命名空间对应，XML文件名和接口名一致。

#### 4. 实现服务层(Service)

- **定义Service接口（UserService, PlanService, FeedbackService）和实现类**:
   - 先写接口，明确要提供哪些方法，让后续Controller调用。
   - 然后写实现，实现中调用Repository或其他服务。此阶段重点是业务逻辑，例如：
     - **UserService.register**：检查用户名是否存在（`UserRepository.findByUsername`）、哈希密码（使用如`BCryptPasswordEncoder`）、保存用户（`UserRepository.save`）。
     - **UserService.login**：根据用户名查找用户，验证密码哈希，生成JWT（若使用JWT，则引入`jjwt`库创建token；或使用Spring Security则配置`UserDetailsService`等）。
     - **PlanService.createPlan**：组装`TripRequest`（可由Controller传入已经解析好的参数对象），调用`RecommendationFactory`获取策略`strategy`，执行`strategy.generatePlan`得到`Plan`结果，`PlanRepository.save`保存，并返回结果。
     - **PlanService.listHistory**：直接`UserRepository`或`PlanRepository`查询即可。
     - **FeedbackService.submitFeedback**：封装`Feedback`实体（设置`userId`和`content`），保存到库。

- **设计模式应用注意**:
   - 此时实现`RecommendationFactory`和`Strategy`接口。在`PlanService`中，不要直接`new`具体策略，而是使用`RecommendationFactory`，例如：
   ```java
   RecommendationStrategy strategy = RecommendationFactory.getStrategy(preferBasic ? "basic" : "ai");
   Plan plan = strategy.generatePlan(request);
   ```
    这样体现工厂+策略模式的解耦。
   - 实现KimiApiClient的单例获取。如果用Spring，KimiApiClient加@Component单例；或PlanServiceImpl持有一个静态的KimiApiClient实例，第一次调用时初始化。
   - 在AIRecommendationStrategy中，通过KimiApiClient发送请求获取结果；BasicRecommendationStrategy则返回硬编码或简单逻辑结果。确保两者实现了相同接口便于替换。
- **注意**:
   - 处理异常情况，如AIRecommendationStrategy调用失败应抛出异常或返回错误标识，PlanService捕获后可以选择切换到Basic策略重试，或者向上抛出让Controller返回错误。
   - Service层尽量保持原子操作，一个业务功能一个方法，内部可涉及多个步骤（如生成行程+保存DB要么都成功要么回滚，可利用Spring @Transactional保障事务）

 
1. **注意**:
   - 密码应存储加密后的hash而非明文，推荐采用BCrypt算法。数据库连接可先用简单账号测试，后续可根据环境分profile配置。

2. **在后端项目中**:
   - 编写`schema.sql`并放入`resources`，让Spring Boot启动时自动初始化表结构（需在`application.yml`开启`spring.sql.init.schema-location`或JPA的`ddl-auto`设置为`update/create`）。也可以手动执行SQL。

#### 5. 编写控制器(Controller)

有了Service后，Controller易于编写。按模块创建控制器类，标注`@RestController`和对应的请求映射`@RequestMapping("/api")`：

- **UserController**: 实现注册和登录接口。注意返回值类型：
  - 注册成功可以返回通用`Result`对象，例：`{code:0, msg:"注册成功"}`。
  - 失败如用户名已存在返回：`{code:1, msg:"用户已存在"}`。
  - 登录成功返回包括用户基本信息和token（如果用JWT）：如`{code:0, data:{ token:"xxx", user:{id, username,...}}, msg:"登录成功"}`。
  - 登录失败返回相应错误码和信息。
  
  **安全**: 若使用Spring Security JWT，此处实际调用`AuthenticationManager`认证，但为了不复杂化，也可自定义。

- **PlanController**: 行程规划和历史接口。
  - **`/api/plan` (POST)**：请求体接收`TripRequest` DTO或直接使用`@RequestParam`获取参数。调用`PlanService.createPlan`得到`Plan`结果。
    - 返回时可以直接返回`Plan`对象序列化为JSON。但`Plan`包含可能很长的文本，可选择只返回行程文本和id、时间等必要字段，避免返回不需要的信息（通过自定义DTO或者在`Plan`实体上使用`@JsonIgnore`忽略不必要的属性）。
  
  - **`/api/history` (GET)**：通过当前登录用户身份获取其`userId`，然后调用`PlanService.listHistory`返回列表。返回值为`List<Plan>`或者自定义只包含概要信息的DTO列表。
  
  **权限**: 确保只有登录用户才能访问自己的历史。如果用JWT，在过滤器已经验证token并存储用户身份，可在controller方法参数中使用注解如`@AuthenticationPrincipal`获取当前用户，或简便起见由前端传当前用户ID并在后端二次校验token中的ID是否匹配请求的userId。

- **FeedbackController**:
  - **`/api/feedback` (POST)**：接收反馈内容（和可选评分）。如果前端没有附带用户信息，可在后端从token解析或Session获取当前用户ID；或者前端直接传`userId`（但需验证真实性）。
    - 调用`FeedbackService.submitFeedback`保存。
    - 返回`{code:0, msg:"感谢您的反馈"}`。
  
  反馈接口可以对未登录用户开放，所以不要强制用户ID，有则存无则置null。

  **注意**: 控制器方法要处理可能的异常，将其转为HTTP错误码或JSON错误响应。可以使用`@ControllerAdvice`统一异常处理（如捕获Service层抛出的自定义异常，返回标准错误JSON）。

- **跨域**: 如果前后端分开运行dev服务器，需要在后端开启CORS。简单做法是在控制器类上加`@CrossOrigin(origins="*")`允许所有源，开发时方便。生产环境应收紧域名。

完成控制器后，可以启动后端并使用Postman或curl测试接口：
  - 注册新用户 -> 数据库验证记录插入。
  - 登录获取token -> 尝试用token访问`/history`接口（或Session方式登录后Cookie访问）。
  - 调用`/plan`接口 -> 首次集成AI调用，此时可能需要准备Kimi API可用的key和检查调用是否通。
  
  由于AI接口可能返回较慢，测试时看接口是否能拿到结果或超时。可以暂时用`BasicRecommendationStrategy`来模拟快速返回以测试流程，之后调通AI。

#### 6. 搭建前端框架

1. **使用Vue CLI创建项目**:
   - 使用Vue CLI创建项目（例如`vue create frontend`）或使用Vite初始化Vue3项目。

2. **配置proxy以便本地调试API请求**:
   - 在开发时，Vue应用跑在比如`http://localhost:8081`，而后端在`8080`，需要解决跨域问题。
   - 可在`vue.config.js`中添加`devServer.proxy`，将`/api`的调用代理到`http://localhost:8080`，这样前端调用`/api/xxx`就会转发，不会有跨域问题。

3. **清理默认模板代码**:
   - 保留基本结构。在`App.vue`中引入导航栏`NavBar`组件并设置`<router-view/>`。
   - 配置好`router/index.js`路径和对应组件（先创建空的组件文件）。

4. **全局引入axios并配置**:
   - 创建`utils/request.js`封装axios实例，设置`baseURL`（如`http://localhost:8080`或`/api`根据是否用代理决定）。
   - 设置请求和响应拦截器处理token和错误。

5. **注意**:
   - Node版本和依赖版本可能导致一些问题，确保使用LTS版本的Node.js。
   - Vue CLI如果使用Vue3要选择对应配置（或者直接用Vite更轻量）。

#### 7. 开发前端页面

按模块实现各页面，推荐顺序如下：

- **注册/登录页**:
  - 先实现`Login.vue`和`Register.vue`的模板和基本交互。可以不用美化样式，确保表单输入和按钮提交逻辑正确。
  - **调试登录**: 使用真实后端接口，看能否成功获取并保存token。将token保存到`localStorage`，并在`NavBar.vue`中根据是否有token判断登录状态显示不同菜单。
  - 将登录状态提升为全局状态管理（Vuex/Pinia）或简单利用Vue提供的`provide/inject`，或在`NavBar`通过读取`localStorage`直接判断。简单起见，可用组件内部逻辑+event bus处理登录事件。

- **行程规划页**:
  - 搭建表单UI，包括目的地输入等。先不调用接口，模拟一个本地结果用于设计展示区域结构。
  - 实现提交调用后端接口获取数据：调用前显示`Loading`组件，拿到响应后渲染结果。需要解析后端返回的`Plan`：
    - 如果`Plan`内容是纯文本，直接插入到页面；
    - 如果是结构化，可以按字段显示。
  - **注意**: 结果可能较长，可放在一个滚动容器或折叠面板。
  - 测试不同输入的效果，调整prompt或显示格式。例如，对于多天行程，考虑在返回文本中使用特定分隔（如“Day1: ...”“Day2: ...”）以利于前端拆分格式化显示。

- **历史记录页**:
  - 实现获取历史列表并展示。可以使用简单的列表，每项显示地点、日期、时间等摘要。
  - 实现点击查看详情：可以在`History.vue`中借助已有`Plan`组件来展示详情。比如在`History.vue`中导入`PlanCard`组件用于显示每一历史项概要，并在点击时将`PlanCard`展开显示详细（`PlanCard`组件可以复用`Plan.vue`的显示逻辑）。
  - 或者导航到`Plan.vue`并通过路由参数让`Plan.vue`调用一次获取特定`Plan`的详情（需要后端提供`GET /api/plan/{id}`接口）。如果没有提供，前端也可从`History`拿到的数据里直接显示，因为我们可以在获取历史列表时就包含详情。但为性能考虑，通常列表不带大文本，只在需要时获取。

- **反馈页**:
  - 简单表单提交，调用接口，提示结果。这里没有复杂交互，主要注意清空表单和提示用户反馈已收到。
  - 可以加上防重复提交处理（按钮点击后禁用，待返回结果再恢复）。

- **导航和路由**:
  - 确保`NavBar`链接正确，并在不同登录状态有不同选项。实现退出登录功能（就是前端删除token和刷新页面或路由跳转到登录页，后端若用Session也应提供`/logout`接口清除会话）。
  - 在路由守卫中，拦截需要认证的路由，如果没有token则跳转登录页并提示。

- **样式美化**:
  - 使用CSS为表单和结果增加样式，使界面简洁友好。可使用CSS预处理器（Vue CLI默认可用SCSS等）。
  - 或引入UI框架，如Element Plus、Ant Design Vue等，快速使用其组件（表单、按钮、布局等）提高开发效率和一致性。
  - **响应式布局**: 考虑常用屏幕，使界面在不同分辨率下仍可用。但因本项目主要本地PC使用，响应式不是重心，可以适当调整。

- **注意**:
  - 前端调试过程中，若后端接口有变更，要及时调整调用。同时注意浏览器控制台的错误，如跨域、网络错误、JSON解析错误等，及时修正。
#### 8. 联调测试

1. **本地同时运行后端和前端（前端开发服务器）**:
   - 手动完整走一遍流程：
     - 注册新用户 -> 登录 -> 获取token。
     - 访问行程规划页 -> 输入有效信息 -> 提交 -> 等待AI结果 -> 成功显示行程。
       - 若AI调用失败，应该有相应提示或fallback（Basic策略）的结果显示。
     - 查看历史记录页 -> 新生成的行程应出现在列表 -> 点击查看内容正确。
     - 提交反馈 -> 数据库检查有记录，前端收到感谢提示。
     - 尝试退出登录 -> 确认受限页面无法访问。

2. **编写一些单元测试和集成测试（可选）**:
   - 后端可用JUnit写Service层测试，如对`UserService`密码加密登录逻辑的测试，`PlanService`在注入一个模拟的`RecommendationStrategy`时是否能正确保存`Plan`等。
   - 对Controller可用`MockMvc`进行REST接口调用测试。
   - 这些测试有助于在修改代码后自动验证主要功能。

3. **性能测试**:
   - 因为调用AI接口耗时较长，尝试并发请求查看系统行为。由于免费版AI可能有调用并发限制，此时系统应能正常排队或返回错误而不崩溃。

4. **注意**:
   - 确保将敏感配置如API密钥不硬编码在前端或公开场合。后端`application.yml`中可使用占位符，实际值通过环境变量注入，避免泄露仓库。
   - 数据库连接等在本地确保稳定，必要时调整连接池参数。
   - AI调用可能慢，可调整HTTP客户端超时时间配置，以免过早超时。
   - 对于已发现的问题进行修复，例如处理AI返回异常情况、前端界面兼容性、数据库字段长度不够等。

#### 9.项目优化与创新:
- 完成功能后，可以考虑一些优化点（见下节“创新点说明”）来提升系统表现和特色。此时的修改应小步前进，每次修改后测试基本功能未受影响。
- 文档完善：整理开发过程中的经验，补充README.md使用说明，完善step.md确保包含实际开发中关键步骤和注意事项。
- 版本管理：在完成各阶段或重要功能后，使用Git进行commit，并打tag标记里程碑版本。以便回溯和项目展示。
 
以上顺序并非一成不变，但遵循从后端基础->数据库->后端核心->前端核心->集成测试->优化的路线，能够快速构建骨架并逐步丰满细节。同时强调每步的注意事项防止踩坑，如配置问题、跨域、认证处理、错误处理等细节。
## 创新点说明

本项目在实现过程中引入了以下创新点和特色设计，以提高系统智能性和用户体验：
- 引入AI大模型生成行程: 最大的特色在于利用Kimi API提供的免费大模型接口来生成个性化的旅行行程方案。通过精心设计提示词(prompt)，系统能够根据用户提供的偏好和要求，让AI生成富有创意且贴合用户兴趣的行程安排。这比传统基于模板或攻略搜索的行程规划更加灵活，能够提供意想不到的惊喜方案。例如，用户偏好历史文化，AI可以推荐不那么知名但极具文化底蕴的小众景点，让行程更独特。Kimi API的免费版被用于降低成本，证明了小团队也可以集成大模型能力。此外，我们在调用AI时加入了结果解析和格式规范，例如约定AI回答按天数分段，从而前端可以有条理地展示行程。这种人机协同的规划方式是当前旅游应用的前沿趋势。
- 个性化推荐与策略模式: 系统设计了策略模式用于行程推荐算法，除了默认的AI推荐策略外，还实现了基础策略。基础推荐策略可以根据预先收集的热门景点和常规路线生成简单行程，或者针对某些熟悉的目的地给出经典线路。当AI不可用或用户选择不使用AI时，此策略保证功能的可用性。这本身不是直接的创新，但为系统提供了扩展性：未来可以添加更多策略，例如“节假日特别推荐策略”或“预算优先策略”等。策略模式的引入使我们能够很方便地根据用户偏好选择算法，例如根据用户的历史行为数据，动态调整AI prompt或选择不同的推荐逻辑，实现个性化推荐。举例来说，若检测到用户喜欢美食多于景点，可在AI prompt中特别要求推荐当地美食和餐厅；或者采用特定的策略算法先筛选美食点再组装行程。这些都为将来的智能化奠定基础。
- 设计模式应用提升系统架构: 除了策略模式，项目中运用了多种经典设计模式：
  - 单例模式: 用于KimiApiClient等需要全局唯一实例的组件，避免重复创建对象消耗资源，确保配置一致。单例的KimiApiClient还方便集中管理AI调用，例如实现简单的请求缓存：在单例中维护一个Map，对于相同的请求prompt若短时间再次调用则直接返回上次结果，减少API调用次数。这对免费额度的合理利用是一种创新尝试。
  - 工厂模式: 用于RecommendationFactory封装对象创建逻辑，使得新增策略时不影响现有代码。工厂模式也可用于前端组件的创建，如如果我们有多种展示卡片组件，可以用工厂选择使用哪一种样式展示AI结果。
  - MVC架构和分层设计: 这虽是常规架构但是良好实践的体现，通过明确分层（Controller-Service-Repository）和前后端分离，使开发并行、高内聚低耦合。在此基础上引入的设计模式让系统更加模块化，属于架构上的改进创新。
- **用户体验改进**: 前端在用户体验上也做了一些提升：
  - 结合导航守卫和token机制，实现无感知登录：登录后自动跳转并保持状态，用JWT实现免Session的分布式友好方案，即使部署到云端也易扩展。
  - 行程规划过程增加Loading动画和进度提示，以及错误提示，让用户了解系统状态，而不是毫无反馈地等待。
  - 对AI返回内容进行二次加工，如识别出景点名称并提供链接或图片（可扩展为调用地图API获取景点照片或地图位置）。这部分属于可选的扩展创新点：在后续版本中，我们可以将AI推荐的地点名称通过其他API（如Google Maps或百度地图）检索，显示地点的地图或者照片，从而把大模型的文本智能和传统API的数据结合，增强说服力和实用性。
- 反馈闭环与持续优化: 用户反馈模块的存在，使产品形成了用户->AI推荐->用户反馈->改进的闭环。收集到的反馈数据将有助于分析AI推荐效果的满意度。如果部署持续迭代，我们可以根据反馈调整AI prompt策略或过滤不合适的推荐，从而持续提升AI规划质量。例如，多位用户反馈某目的地生成的行程不合理，我们可以在prompt中加入额外约束（如避免某些不便的安排）。这种基于用户反馈改进AI输出的思路是一种创新实践。
- 本地部署与隐私: 项目支持本地运行，一方面便于开发调试，另一方面对终端用户来说也可选择在本地或私有环境使用，保障了个人行程偏好和历史数据的隐私安全，不必上传到公共服务器。这种设计在注重隐私的环境中具有优势，也是项目的一个亮点（用户的敏感信息仅存于自己的数据库中）。
- 扩展性考虑: 创新不止体现在现有功能，还包括对未来的考虑。通过良好的架构设计，我们预留了许多扩展空间：
  - 模块化的前后端使得以后可以很方便地替换AI接口（比如换用更高级的大模型服务）、增加移动端界面（因为后端提供API即可支撑多终端）。
  - 数据库结构可以扩展，如加入景点详细表、用户偏好标签，让AI推荐结合更精细的数据。
  - 可以引入更多设计模式优化，例如观察者模式用于在用户提交反馈后通知管理员，或在新行程生成后给用户发送通知（如果有消息系统的话）。
  - 采用插件式的策略架构，未来甚至可以开放让第三方开发新的推荐算法插件接入系统，为平台化做准备。


综上，本智能旅游规划器项目不仅实现了基本功能，更通过AI赋能和合理的软件设计，实现了个性化和智能化的创新。它融合了当下流行的大模型技术和经典的软件工程方法，既保证了系统稳定性又提供了令人耳目一新的用户体验，为旅游行程规划领域提供了一个有价值的解决方案示例。

---
**备注**: 开发完成后，请确保将本step.md文档存放在项目的docs目录下，供团队成员参考。在实际开发过程中，可能会根据遇到的问题和新需求对步骤进行调整，本文档也应当相应更新以保持准确。祝开发顺利！
