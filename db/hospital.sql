-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jul 27, 2014 at 04:08 PM
-- Server version: 5.6.12-log
-- PHP Version: 5.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `hospital`
--
CREATE DATABASE IF NOT EXISTS `hospital` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `hospital`;

-- --------------------------------------------------------

--
-- Table structure for table `admin`
--

CREATE TABLE IF NOT EXISTS `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(45) NOT NULL COMMENT '管理员账号',
  `password` varchar(45) NOT NULL COMMENT '管理员密码',
  `roleId` int(11) NOT NULL COMMENT '角色ID',
  `time` varchar(45) NOT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `account_UNIQUE` (`account`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='管理员表' AUTO_INCREMENT=3 ;

--
-- Dumping data for table `admin`
--

INSERT INTO `admin` (`id`, `account`, `password`, `roleId`, `time`) VALUES
(1, 'admin', 'admin', 1, '1406474271565'),
(2, '1', '1', 5, '1405782887');

-- --------------------------------------------------------

--
-- Table structure for table `department`
--

CREATE TABLE IF NOT EXISTS `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '科室名称',
  `desc` text COMMENT '科室简介',
  `directorId` int(11) DEFAULT NULL COMMENT '主任ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='科室' AUTO_INCREMENT=10 ;

--
-- Dumping data for table `department`
--

INSERT INTO `department` (`id`, `name`, `desc`, `directorId`) VALUES
(1, '骨科', '骨科是各大医院最常见的科室之一，主要研究骨骼肌肉系统的解剖、生理与病理，运用药物、手术及物理方法保持和发展这一系统的正常形态与功能', 0),
(2, '胸外科', '胸外科始建于1955年，是三江地区建立最早的集临床、教学、科研于一体的全面发展的专业科室。55年来，历任科领导，本着科学、务实、开拓、进取的精神带领全科医护人员与时俱进、努力拼搏，在胸心外科领域多次填补三江地区的医学空白。经过几代人的辛勤耕耘，目前已发展成为享誉黑龙江省东部地区的精品科室。五十年代开展肺和食管手术；六十年代开展常温下心脏手术；七十年代开展低温下心脏直视手术；八十年代后期开展体外循环下心脏直视手术；九十年代后期开展肺癌、食管癌等胸部恶性肿瘤的综合治疗；新世纪初在全地区率先开展了电视胸腔镜下微创手术。至今累计完成胸外科手术已超过8000余例。其中包括：肺癌根治，肺支气管袖式切除，食管癌、贲门癌的根治及人工贲门植入术，纵膈肿瘤切除及重症肌无力的外科治疗，胸腔镜下肺大疱切除、自发性气胸手术根治及晚期肺气肿患者肺减容术等多项复杂疑难手术。2004年为加深专业化程度实现了普胸外科与心脏外科的分离。胸外科现有专科医生5人，护士9人，其中主任医师2名，主治医师3名，其中硕士研究生2名，开放床位29张，拥有先进的熊-1000呼吸机、多参数监护仪、电视胸腔镜及多种微量泵等一系列医疗设备，硬件实力在本地区处于领先水平。在学科带头人廖明主任的带领下，以过硬的技术和精益求精的态度，承担了为全市人民解除胸心疾苦的重任，受到群众和有关部门的肯定和褒奖，甚至吸引了部分外地患者前来求医。受到社会各界和有关部门的表扬。\r\n     科室专业技术:在三江地区首先开展了以手术为主的肺癌综合治疗，肺支气管袖式切除，食管癌、贲门癌的根治及人工贲门植入术，纵膈肿瘤切除及重症肌无力的外科治疗，胸腔镜下肺大疱切除、自发性气胸手术根治及晚期肺气肿患者肺减容术等多项复杂疑难手术。\r\n', 0),
(4, '肛肠科', '肛肠科以结肠、直肠及肛门疾病为诊治范围，依托三级甲等医院综合实力，全面开展结直肠肛门外科疾病的预防、诊断、治疗和康复工作。科室专业力量雄厚，技术水平领先。在省内率先开展了直肠前突应用补片无张力修补术，使直肠前突的治疗达到国内领先水平；在我市首家开展PPH手术。采用多种方法治疗各类痔（注射、套扎即PPH手术、外剥内扎术、吻合器痔上粘膜切除术即PPH手术），微创无痛手术治疗各类肛瘘、肛裂、肛周脓肿，创伤小、痛苦少；治疗各类出口梗阻型便秘（直肠前突、直肠粘膜内脱垂、耻骨直肠肌肥厚）；开展全结肠切除、“J”型储袋回肠肛管吻合治疗溃疡性结肠炎、结肠癌根治术、直肠癌低位保肛手术等。\r\n   肛肠科全体医务人员团结一心，树立以人为本的思想，用一流的技术、一流的服务态度、合理的费用，让您远离肛肠疾病，享受健康生活。\r\n', 1),
(5, '妇产科', '妇产科是黑龙江省东部地区妇产科诊治中心，现有医护人员70人，其中正高职称13人，副高职称16人，中级职称17人，硕士研究生5人。全科医护人员曾多次派往北京、上海、广州等著名医院进修。\r\n　　医务人员整体业务素质过硬，技术精湛。设有妇科、产科、门诊（含计划生育），开放床位80张。妇科临床疾病的救治水平全省一流，规范化治疗妇科肿瘤，宫颈癌根治术、盆腹腔淋巴清扫术、卵巢癌细胞减灭术等术式已居全国前沿水平。坚持以微创手术为特色，应用腹腔镜、宫腔镜广泛开展妇科微创手术，如腹腔镜下异位妊娠手术、卵巢肿瘤剔除术、子宫肌瘤剥除术、子宫切除术；宫腔镜下子宫黏膜肌瘤切除术、子宫内膜切除治疗功血、子宫内膜息肉切除、子宫纵膈切除术；阴式子宫切除术、阴式子宫肌瘤核出术、阴式卵巢囊肿（切）剔除术、阴道前后壁修补术等，通过电子阴道镜、TCT检查、聚焦超声、LEEP手术对宫颈疾病进行系统规范的诊断治疗。运用阴道彩超、CT、MRI等先进检测手段，使妇科肿瘤得以早期发现、早期手术，有效延长生命。\r\n　　产科主要擅长各种高危妊娠的筛查、监护及治疗，妊娠合并症及妊娠并发症的诊断与治疗，在对难产、高危妊娠及产科并发症的诊断与治疗上均有丰富的经验，承担着我市及周边地区危、急、重患者的抢救工作。是省内较早开展无痛分娩、导乐陪伴分娩、家庭化分娩的产科，并全面筛查新生儿疾病及新生儿听力障碍，开展新生儿抚触与新生儿泳疗。产科门诊专门开设有孕妇学校，采取从“产前检查－入院分娩－出院－产后访视”全程“一条龙”跟踪服务模式。', NULL),
(6, '儿科', '儿科成立于医院创建之初，已有六十年历史，多年来承担着三江地区几十万儿童的医疗保健任务，承担了疑难病会诊工作、进修医生培训及省内医学院校学生带教工作。\r\n　　儿科现拥有主任医师5人、副主任医师2人、主治医师2人、医师4人，其中博士后1人、硕士3人，护理人员12人，其中副主任护师2人、主管护师2人。科室技术力量雄厚，技术水平一流，诊疗疾病涉及多学科、多系统，除常见病外还可诊治糖尿病、甲状腺功能减低、超低体重儿、白血病、再障、难治性肾病、难治性心衰、支气管哮喘、蛋白质丢失性肠炎等。\r\n　　近十五年来，增设了儿保门诊，负责0-7岁小儿健康检查，开展营养咨询、膳食分析、母乳喂养指导，遗传代谢性疾病和先天性疾病咨询、诊断和筛查，智力测试、社会心理和社会适应能力测试，气质测试及相关疾病的早期发现和诊治（如精神发育迟滞、多动症、抽动障碍、孤独症、遗尿症、学习困难等）。开设了三江地区唯一的矮小门诊。近两年来，开展了小儿癫痫系统诊疗，如视频脑电图的应用，抗癫痫药物的血药浓度监测等，居国内先进水平。\r\n　　儿科全体医护人员将为您提供优质的医疗服务。\r\n　　我们的服务宗旨是：保障儿童健康，提高生命质量。\r\n', 1),
(7, '眼科', '眼科是我省东部地区技术力量最强的治疗眼病的专业科室，有主任医师1名，副主任医师5名，医学博士1名，医学硕士4名，科室设备齐全，居全市之首，拥有德国蔡司公司生产的最新型三维OCT、全自动视野检测仪。YAG激光治疗机，莱卡眼科手术显微镜，美国博士伦公司生产的CX-3000超声乳仪，玻璃体切割、眼底激光一体机，美国爱尔康公司生产的超声乳化仪，美国科医仁公司生产的氩离子激光治疗机，日本托普康公司生产的全自动组合验光仪，全自动眼压计，角膜地形图仪，荧光眼底造影机，眼科A/B型超声诊断仪。KTP泪道激光治疗机，美国雷塞公司生产的准分子激光治疗机等一系列先进设备。\r\n　　眼科年门诊量近3万人，年手术量1000余例。能独立开展白内障超声乳化摘除及折叠式人工晶体植入术。玻璃体视网膜显微手术治疗糖尿病及各种复杂性、外伤性玻璃体视网膜病变，各种类型青光眼的诊断及治疗；各种眼底疾病的诊断和激光治疗，各种斜视的诊断及治疗，准分子激光矫正近视、远视及散光；眼外伤的诊断及治疗，仿真活动义眼植入术，激光治疗泪道阻塞性疾病，眼眶肿瘤的手术治疗，眼科激光治疗各种眼部疾病，眼部整形手术；专业医学验光配镜等项目。\r\n　　服务宗旨:用精湛的技术救治病患，让更多人重见光明。\r\n', NULL),
(8, '神经外科', ' 神经外科始建于1959年，现有床位57张，开放床位70张。医生15人，护士22人，主任医师4 人，副主任医师4人，硕士研究生4人，副主任护师2人，主管护师3人。65%以上医师曾在北京、天津、上海、哈尔滨等地进修。现已独立开展颅内各部位肿瘤切除术，如：延髓腹侧脑膜瘤，前﹑中﹑后颅凹底肿瘤，听神经肿瘤，巨大垂体刘、第四脑室、侧脑室肿瘤，大脑皮层功能区胶质瘤等显微镜下全切术。熟练开展颅内动脉瘤开颅夹闭术，如大脑前交通动脉瘤﹑大脑后交通动脉瘤﹑颈内动脉瘤﹑大脑中动脉瘤等。熟练完成颅内巨大脑血管动静脉畸型切除术。独立开展颅内动脉瘤血管内介入治疗，包括球囊和支架辅助介入栓塞术。已独立开展难治性癫痫外科手术，包括胼胝体切开，部份颞叶切除，海马部分切除和致痫病灶切除等。熟练开展脊髓肿瘤切除，包括多节段脊髓肿瘤切除术。神经内镜下第Ⅲ脑室底造瘘治疗梗阻性脑积水，单鼻孔经蝶入路垂体瘤切除术，chiari畸型手术，立体定向CT下脑脓肿穿刺抽吸术，立体定向软通道血肿抽吸术治疗高血压脑出血及脑室出血，特急特重型颅脑损伤救治。PICC置管术及上述疾病的专科护理。\r\n    科室已组建六个专业医疗组：颅底外科组、脑血管病组、脑室镜组、癫痫外科组、脑胶质瘤组、立体定向微创组。针对不同病人给予个体化、专业化、系统化、规范化治疗。获佳木斯市科技进步一等奖三次，二等奖六次。\r\n    服务理念：用专业的心，做专业的事。\r\n', NULL),
(9, '消化内科', '消化内科为我院重点科室，设有床位40张，附设有消化内镜中心。科室共有医护人员33人，其中医生13人，有主任医师3名，副主任医师1名，主治医师2名，住院医师7名。内镜中心拥有世界上先进的日本富士能电子胃肠镜4台套，可同时进行胃镜、肠镜检查及治疗，以及治疗性ERCP等工作。科室擅长对上消化道大出血，重型胰腺炎及重型肝病的抢救治疗以及消化系统复杂及疑难疾病的诊断及治疗。目前常规开展的内镜治疗项目有：\r\n     1、经内镜结扎及硬化术治疗肝硬化门脉高压食管及胃底静脉曲张出血，特别是对急性出血的急诊止血治疗。\r\n     2、应用治疗性ERCP行十二指肠乳头切开取石治疗胆总管结石。\r\n     3、应用经内镜胆管引流术治疗梗阻性黄疸\r\n     4、应用经皮经肝胆管引流技术治疗梗阻性黄疸。\r\n     5、应用内镜下注射药物及止血夹子治疗消化性溃疡及其他血管病变所致的消化道出血。\r\n     6、应用高频电凝、氩气刀及内镜套扎器治疗不同类型的消化道息肉及腺瘤，特别擅长对消化道巨大及复杂腺瘤的内镜治疗。\r\n     7、应用经内镜粘膜切除术（EMR）及粘膜剥离术（ESD）治疗侧向生长的消化道粘膜病变，粘膜下病变及早期癌。\r\n     8、应用经内镜扩张及支架置入术治疗食管良、恶性病变所致的食管狭窄，解决病人的进食问题。\r\n　　其中经内镜治疗食管、胃底静脉曲张出血及经内镜治疗胆总管结石等项目本地区只有我院常规开展，是我院特色治疗项目。\r\n', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `doctor`
--

CREATE TABLE IF NOT EXISTS `doctor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(45) NOT NULL COMMENT '医生账号',
  `password` varchar(45) NOT NULL COMMENT '医生登录密码',
  `name` varchar(45) NOT NULL COMMENT '医生姓名',
  `roleId` int(11) NOT NULL COMMENT '角色ID',
  `sex` int(11) NOT NULL COMMENT '医生性别',
  `age` int(11) NOT NULL COMMENT '医生年龄',
  `desc` text COMMENT '医生简介',
  `departmentId` int(11) NOT NULL COMMENT '科室ID',
  `del` int(11) DEFAULT NULL COMMENT '是否已删除',
  `image` varchar(100) DEFAULT NULL COMMENT '头像url',
  `time` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='医生信息表' AUTO_INCREMENT=24 ;

--
-- Dumping data for table `doctor`
--

INSERT INTO `doctor` (`id`, `account`, `password`, `name`, `roleId`, `sex`, `age`, `desc`, `departmentId`, `del`, `image`, `time`) VALUES
(1, 'zhangsan', 'zhangsan', '张三', 2, 1, 45, '张三，骨科主治医生，骨科主任', 1, 1, '/upload/avatar/1406135378531.gif', '1406474082577'),
(6, 'jingdongshangcheng', 'jingdongshangcheng', '京东商城', 4, 0, 23, '顶顶顶顶顶顶顶顶', 1, 1, '/uploads/images/1405961886822.png', NULL),
(11, 'fanyanqiu', 'fanyanqiu', '范彦秋', 2, 0, 45, '1988年毕业于哈尔滨医科大学医学系，研究生学历，现任黑龙江省医学会消化内镜分会委员，佳木斯市医学会消化内镜分会副主任委员，2001年在北京友谊医院进修一年。熟练掌握消化系统急、危、重症病人的抢救治疗，上消化道出血的内镜下注射止血及钛夹止血治疗；肝硬化并食管静脉曲张出血的内镜下套扎止血治疗；胃石症的内镜下碎石治疗；胃肠息肉的内镜切除术及消化道异物取出术等。\r\n', 9, 1, '/admin/uploads/avatar/1406124112441.gif', NULL),
(12, 'zuohongjia', 'zuohongjia', '栾宏佳', 2, 1, 48, '1988年毕业于哈尔滨医科大学医疗系，现任佳木斯市骨科学会副主任委员，省关节外科专业委员，曾到南京铁道医学院、北医三院、积水潭医院进修。 \r\n     专业特长：擅长对于骨科常见病及疑难病诊断及治疗。对于肌皮瓣的游离移植，全膝、全髋、人工关节置换和人工关节翻修术，腔镜下关节微创手术，颈椎、胸椎、腰椎前后路手术都有较高造诣。\r\n    从医格言：精益求精，尽职尽责。\r\n', 1, 1, '/admin/uploads/avatar/1406128941596.gif', NULL),
(20, 'wangwu', 'wangwu', '王五', 2, 1, 22, 'ssss', 1, NULL, '\\upload\\avatar\\1406209894191.png', '1406475130166'),
(21, 'zhangsan', 'zhangsan', '张三', 2, 1, 33, '的顶顶顶顶顶', 5, NULL, '\\upload\\avatar\\1406210444103.png', NULL),
(22, 'maliu', 'maliu', '马六', 2, 1, 55, '的顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶', 1, NULL, '\\upload\\avatar\\1406475076168.png', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `message`
--

CREATE TABLE IF NOT EXISTS `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL COMMENT '浏览内容',
  `time` varchar(45) NOT NULL COMMENT '发布时间',
  `userId` int(11) NOT NULL COMMENT '发布者ID（用户ID）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户留言' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `permission`
--

CREATE TABLE IF NOT EXISTS `permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '权限名称',
  `url` varchar(45) DEFAULT NULL COMMENT 'url',
  `pid` int(11) DEFAULT NULL COMMENT '父权限ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='权限表' AUTO_INCREMENT=27 ;

--
-- Dumping data for table `permission`
--

INSERT INTO `permission` (`id`, `name`, `url`, `pid`) VALUES
(1, '权限管理', NULL, 0),
(2, '我的信息', NULL, 0),
(3, '公告管理', NULL, 0),
(4, '评论管理', NULL, 0),
(5, '预约管理', NULL, 0),
(6, '科室管理', NULL, 0),
(7, '用户信息', NULL, 0),
(8, '医生管理', NULL, 0),
(9, '预约处理', NULL, 0),
(10, '排班管理', NULL, 0),
(11, '基本信息', '/admin/myinfo', 2),
(12, '修改密码', '/admin/myinfo/password', 2),
(13, '科室列表', '/admin/department/index', 6),
(14, '添加科室', '/admin/department/add', 6),
(15, '医生列表', '/admin/doctor/index', 8),
(16, '添加医生信息', '/admin/doctor/add', 8),
(17, '预约列表', '/admin/register/index', 5),
(18, '我的预约', '/admin/register/process', 9),
(19, '公告列表', '/admin/post/index', 3),
(20, '添加公告', '/admin/post/add', 3),
(21, '评论列表', '/admin/message/index', 4),
(22, '用户列表', '/admin/user/index', 7),
(23, '角色列表', '/admin/role/index', 1),
(24, '添加角色', '/admin/role/add', 1),
(25, '权限设置', '/admin/role/permission', 1),
(26, '医生排班', '/admin/schedule/index', 10);

-- --------------------------------------------------------

--
-- Table structure for table `post`
--

CREATE TABLE IF NOT EXISTS `post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL COMMENT '公告标题',
  `content` text COMMENT '公告内容',
  `author` varchar(45) NOT NULL COMMENT '公告作者',
  `del` int(11) NOT NULL COMMENT '是否被删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公告表' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `register`
--

CREATE TABLE IF NOT EXISTS `register` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL COMMENT '用户ID',
  `departmentId` int(11) NOT NULL COMMENT '科室ID',
  `doctorId` int(11) NOT NULL COMMENT '医生ID',
  `date` varchar(45) NOT NULL COMMENT '挂号时间（年月日）',
  `period` int(11) NOT NULL COMMENT '挂号时间段（上午、下午）',
  `remark` text COMMENT '备注',
  `status` int(11) DEFAULT '0' COMMENT '审核状态（待审核，通过，未通过）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='挂号表' AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE IF NOT EXISTS `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '角色名',
  `remark` varchar(45) DEFAULT NULL COMMENT '备注',
  `type` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='角色表' AUTO_INCREMENT=6 ;

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`id`, `name`, `remark`, `type`) VALUES
(1, '管理员', '拥有所有权限', 0),
(2, '主任医师', '同时也是部门主任', 1),
(3, '副主任医师', '副主任医师', 1),
(4, '医生', '普通的医师', 1),
(5, '挂号管理员', '只拥有挂号管理的权限', 0);

-- --------------------------------------------------------

--
-- Table structure for table `role_permission`
--

CREATE TABLE IF NOT EXISTS `role_permission` (
  `roleId` int(11) NOT NULL COMMENT '角色ID',
  `permissionId` int(11) NOT NULL COMMENT '权限ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限对应表';

--
-- Dumping data for table `role_permission`
--

INSERT INTO `role_permission` (`roleId`, `permissionId`) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8),
(2, 2),
(2, 9),
(3, 2),
(3, 3),
(3, 9),
(4, 2),
(4, 3),
(4, 9),
(5, 2),
(5, 5),
(5, 10);

-- --------------------------------------------------------

--
-- Table structure for table `schedule`
--

CREATE TABLE IF NOT EXISTS `schedule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `doctorId` int(11) NOT NULL COMMENT '医生ID',
  `date` varchar(45) NOT NULL COMMENT '上班时间（年月日）',
  `amTotal` int(11) DEFAULT '0' COMMENT '上午预约总数',
  `amNum` int(11) DEFAULT '0' COMMENT '上午实际预约数',
  `pmTotal` int(11) DEFAULT '0' COMMENT '下午预约总数',
  `pmNum` int(11) DEFAULT '0' COMMENT '下午实际预约数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='医生排班表' AUTO_INCREMENT=77 ;

--
-- Dumping data for table `schedule`
--

INSERT INTO `schedule` (`id`, `doctorId`, `date`, `amTotal`, `amNum`, `pmTotal`, `pmNum`) VALUES
(1, 13, '20140728', 2, 0, 4, 0),
(2, 14, '20140728', 4, 0, 0, 0),
(3, 15, '20140728', 2, 0, 2, 0),
(4, 16, '20140728', 3, 0, 0, 0),
(5, 23, '20140728', 5, 0, 0, 0),
(6, 13, '20140728', 1, 0, 5, 0),
(7, 14, '20140728', 5, 0, 2, 0),
(8, 15, '20140728', 0, 0, 2, 0),
(9, 16, '20140728', 3, 0, 1, 0),
(10, 23, '20140728', 4, 0, 0, 0),
(11, 13, '20140729', 3, 0, 2, 0),
(12, 14, '20140729', 3, 0, 0, 0),
(13, 15, '20140729', 2, 0, 5, 0),
(14, 16, '20140729', 4, 0, 1, 0),
(15, 23, '20140729', 4, 0, 5, 0),
(16, 13, '20140730', 3, 0, 3, 0),
(17, 14, '20140730', 0, 0, 1, 0),
(18, 15, '20140730', 3, 0, 5, 0),
(19, 16, '20140730', 6, 0, 2, 0),
(20, 23, '20140730', 6, 0, 0, 0),
(21, 13, '20140731', 4, 0, 3, 0),
(22, 14, '20140731', 2, 0, 2, 0),
(23, 15, '20140731', 6, 0, 1, 0),
(24, 16, '20140731', 2, 0, 0, 0),
(25, 23, '20140731', 0, 0, 7, 0),
(26, 13, '20140801', 4, 0, 0, 0),
(27, 14, '20140801', 0, 0, 2, 0),
(28, 15, '20140801', 4, 0, 0, 0),
(29, 16, '20140801', 0, 0, 3, 0),
(30, 23, '20140801', 3, 0, 0, 0),
(31, 13, '20140802', 3, 0, 2, 0),
(32, 14, '20140802', 4, 0, 3, 0),
(33, 15, '20140802', 5, 0, 5, 0),
(34, 16, '20140802', 2, 0, 5, 0),
(35, 23, '20140802', 4, 0, 6, 0),
(36, 13, '20140803', 4, 0, 2, 0),
(37, 14, '20140803', 2, 0, 0, 0),
(38, 15, '20140803', 2, 0, 5, 0),
(39, 16, '20140803', 7, 0, 3, 0),
(40, 23, '20140803', 1, 0, 3, 0),
(41, 13, '20140804', 3, 0, 0, 0),
(42, 14, '20140804', 0, 0, 3, 0),
(43, 15, '20140804', 3, 0, 0, 0),
(44, 16, '20140804', 0, 0, 3, 0),
(45, 23, '20140804', 3, 0, 0, 0),
(46, 13, '20140805', 3, 0, 3, 0),
(47, 14, '20140805', 2, 0, 5, 0),
(48, 15, '20140805', 4, 0, 1, 0),
(49, 16, '20140805', 7, 0, 3, 0),
(50, 23, '20140805', 3, 0, 0, 0),
(51, 13, '20140806', 3, 0, 0, 0),
(52, 14, '20140806', 0, 0, 5, 0),
(53, 15, '20140806', 4, 0, 5, 0),
(54, 16, '20140806', 2, 0, 0, 0),
(55, 23, '20140806', 0, 0, 1, 0),
(56, 21, '20140728', 3, 0, 3, 0),
(57, 21, '20140729', 3, 0, 4, 0),
(58, 21, '20140730', 5, 0, 5, 0),
(59, 21, '20140731', 2, 0, 4, 0),
(60, 21, '20140801', 4, 0, 4, 0),
(61, 21, '20140802', 5, 0, 5, 0),
(62, 21, '20140803', 6, 0, 3, 0),
(63, 21, '20140804', 3, 0, 3, 0),
(64, 21, '20140805', 6, 0, 4, 0),
(65, 21, '20140806', 3, 0, 3, 0),
(66, 22, '20140728', 4, 0, 0, 0),
(67, 22, '20140729', 5, 0, 5, 0),
(68, 22, '20140730', 4, 0, 4, 0),
(69, 22, '20140731', 2, 0, 4, 0),
(70, 22, '20140801', 4, 0, 3, 0),
(71, 22, '20140802', 4, 0, 5, 0),
(72, 22, '20140803', 3, 0, 3, 0),
(73, 22, '20140804', 2, 0, 5, 0),
(74, 22, '20140805', 5, 0, 4, 0),
(75, 22, '20140806', 4, 0, 2, 0),
(76, 11, '20140728', 5, 0, 5, 0);

-- --------------------------------------------------------

--
-- Table structure for table `schedule_status`
--

CREATE TABLE IF NOT EXISTS `schedule_status` (
  `departmentId` int(11) NOT NULL COMMENT '科室ID',
  `date` varchar(45) NOT NULL COMMENT '上班时间（年月日）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='排班状态表';

--
-- Dumping data for table `schedule_status`
--

INSERT INTO `schedule_status` (`departmentId`, `date`) VALUES
(2, '20140728'),
(2, '20140728'),
(2, '20140729'),
(2, '20140730'),
(2, '20140731'),
(2, '20140801'),
(2, '20140802'),
(2, '20140803'),
(2, '20140804'),
(2, '20140805'),
(2, '20140806'),
(5, '20140728'),
(5, '20140729'),
(5, '20140730'),
(5, '20140731'),
(5, '20140801'),
(5, '20140802'),
(5, '20140803'),
(5, '20140804'),
(5, '20140805'),
(5, '20140806'),
(6, '20140728'),
(6, '20140729'),
(6, '20140730'),
(6, '20140731'),
(6, '20140801'),
(6, '20140802'),
(6, '20140803'),
(6, '20140804'),
(6, '20140805'),
(6, '20140806'),
(9, '20140728');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(45) NOT NULL COMMENT '用户账号',
  `password` varchar(45) NOT NULL COMMENT '用户登录密码',
  `name` varchar(45) NOT NULL COMMENT '用户姓名',
  `address` varchar(45) DEFAULT NULL COMMENT '地址',
  `sex` int(11) NOT NULL COMMENT '用户性别',
  `email` varchar(45) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(13) DEFAULT NULL COMMENT '电话号码',
  `qq` varchar(45) DEFAULT NULL COMMENT 'QQ号',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `del` int(11) DEFAULT NULL COMMENT '是否可用',
  `identity` varchar(18) NOT NULL COMMENT '身份证号',
  `time` varchar(45) NOT NULL COMMENT '登录时间',
  `usercol1` varchar(45) DEFAULT NULL,
  `usercol2` varchar(45) DEFAULT NULL COMMENT '用户信息表',
  `usercol3` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表' AUTO_INCREMENT=1 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
