# create database
create database sgqs_master;
create database sgqs_slave;


# create table
DROP TABLE IF EXISTS `sgqs_master`.`t_front_member`;
CREATE TABLE  `sgqs_master`.`t_front_member` (
  `front_member_key` int unsigned NOT NULL AUTO_INCREMENT,
  `front_member_login_id` varchar(12) DEFAULT NULL,
  `front_member_login_password` varchar(64) DEFAULT NULL,
  `front_member_login_dt` datetime DEFAULT NULL,
  `front_member_is_locked` tinyint(1) DEFAULT NULL,
  `front_member_login_fail_cnt` tinyint(2) DEFAULT '0',
  `front_member_login_fail_dt` datetime DEFAULT NULL,
  `front_member_approval_dt` datetime DEFAULT NULL,
  `front_member_status_tp` smallint(5) unsigned zerofill DEFAULT NULL,
  `front_member_tp` smallint(5) unsigned zerofill DEFAULT NULL COMMENT '権限',
  `front_member_name` varchar(50) DEFAULT NULL,
  `front_member_email` varchar(100) DEFAULT NULL,
  `front_member_reg_dt` datetime DEFAULT NULL,
  `front_member_upd_dt` datetime DEFAULT NULL,
  PRIMARY KEY (`front_member_key`),
  UNIQUE KEY `front_member_login_id` (`front_member_login_id`),
  KEY `t_front_member_idx_id` (`front_member_login_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sgqs_master`.`t_mst_code`;
CREATE TABLE  `sgqs_master`.`t_mst_code` (
  `mst_code_master_key` smallint(5) unsigned zerofill NOT NULL,
  `mst_code_sub_key` smallint(5) unsigned zerofill NOT NULL,
  `mst_code_nm` varchar(50) DEFAULT NULL,
  `mst_code_desc` varchar(200) DEFAULT NULL,
  `mst_code_is_enabled` tinyint(1) DEFAULT NULL,
  `mst_code_var_nm` varchar(50) DEFAULT NULL,
  `mst_code_priority` tinyint(2) DEFAULT '1',
  PRIMARY KEY (`mst_code_master_key`,`mst_code_sub_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `sgqs_slave`.`t_front_member_login_log`;
CREATE TABLE  `sgqs_slave`.`t_front_member_login_log` (
  `front_member_login_log_key` int unsigned NOT NULL AUTO_INCREMENT,
  `front_member_login_log_login_id` varchar(12) DEFAULT NULL,
  `front_member_login_log_login_ip` varchar(20) DEFAULT NULL,
  `front_member_login_log_login_dt` datetime DEFAULT NULL,
  PRIMARY KEY (`front_member_login_log_key`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


# default data insert
#---------------------------------
# t_front_member
# front_member_login_id:test, front_member_login_password:test1234
#---------------------------------
INSERT INTO t_front_member(front_member_key, front_member_login_id, front_member_login_password, front_member_is_locked, front_member_approval_dt, front_member_status_tp, front_member_tp, front_member_name, front_member_reg_dt, front_member_upd_dt)
VALUES (1, 'test1234', 'a5d683b05d77ac7649beb4a653b1bcc428d250e5e80ed982b6106debcb01faf5',  0, '2016-05-14 10:23:37', 5302, 5001, 'Test Member', '2016-05-14 12:12:12', '2016-05-14 14:57:16');

#---------------------------------
# t_mst_code
#---------------------------------
INSERT INTO t_mst_code (mst_code_master_key, mst_code_sub_key, mst_code_nm, mst_code_desc, mst_code_is_enabled, mst_code_var_nm, mst_code_priority) VALUES
(05000, 05000, '会員タイプ', '会員の一番大きい分岐', 0, 'MEM_TYPE', 0),
(05000, 05001, '通常会員', '通常会員', 1, 'MEM_TYPE_NORMAL', 1),
(05000, 05002, '特別会員', '特別会員', 1, 'MEM_TYPE_SPECIAL', 2),
(05300, 05300, '会員状態', '会員の現在の状態', 0, 'MEM_STATUS', 0),
(05300, 05301, '承認待ち', '会員登録し承認を待っている状態', 1, 'MEM_APPROVAL_PENDING_STATUS', 1),
(05300, 05302, '正常', '会員が承認され、正常に利用している状態', 1, 'MEM_NOMAL_STATUS', 2),
(05300, 05303, '一時停止', '管理者により一時停止された状態', 1, 'MEM_PAUSE_STATUS', 3),
(05300, 05304, '退会', '会員が退会した状態', 1, 'MEM_CANCEL_STATUS', 4),
(05300, 05305, '強制退会', '何らかの原因により管理者から強制的に退会された状態', 1, 'MEM_FORCED_CANCEL_STATUS', 4);


