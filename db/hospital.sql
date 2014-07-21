-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jul 21, 2014 at 07:37 AM
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
(1, 'admin', 'admin', 1, '1405748002'),
(2, '1', '1', 1, '1405782887');

-- --------------------------------------------------------

--
-- Table structure for table `department`
--

CREATE TABLE IF NOT EXISTS `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '科室名称',
  `desc` text COMMENT '科室简介',
  `directorId` int(11) NOT NULL COMMENT '主任ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='科室' AUTO_INCREMENT=2 ;

--
-- Dumping data for table `department`
--

INSERT INTO `department` (`id`, `name`, `desc`, `directorId`) VALUES
(1, '骨科', '骨科是各大医院最常见的科室之一，主要研究骨骼肌肉系统的解剖、生理与病理，运用药物、手术及物理方法保持和发展这一系统的正常形态与功能', 0);

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
  `doctorcol` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='医生信息表' AUTO_INCREMENT=2 ;

--
-- Dumping data for table `doctor`
--

INSERT INTO `doctor` (`id`, `account`, `password`, `name`, `roleId`, `sex`, `age`, `desc`, `departmentId`, `del`, `image`, `doctorcol`) VALUES
(1, 'zhangsan', 'zhangsan', '张三', 2, 1, 45, '张三，骨科主治医生，骨科主任', 1, 1, NULL, NULL);

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
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='权限表' AUTO_INCREMENT=26 ;

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
(25, '权限设置', '/admin/role/permission', 1);

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
  `doctorId` int(11) NOT NULL COMMENT '医生ID',
  `time` varchar(45) NOT NULL COMMENT '挂号时间',
  `remark` text COMMENT '备注',
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
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='角色表' AUTO_INCREMENT=6 ;

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`id`, `name`, `remark`) VALUES
(1, '管理员', '拥有所有权限'),
(2, '主任医师', '同时也是部门主任'),
(3, '副主任医师', '副主任医师'),
(4, '医生', '普通的医师'),
(5, '挂号管理员', '只拥有挂号管理的权限');

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
(5, 5);

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
