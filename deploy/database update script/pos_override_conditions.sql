-- phpMyAdmin SQL Dump
-- version 3.5.5
-- http://www.phpmyadmin.net
--
-- 主机: localhost
-- 生成日期: 2013 年 11 月 27 日 12:04
-- 服务器版本: 5.5.29
-- PHP 版本: 5.4.10

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- 数据库: `gm4g_pos`
--

-- --------------------------------------------------------

--
-- 表的结构 `pos_override_conditions`
--

--
-- 转存表中的数据 `pos_override_conditions`
--

INSERT INTO `pos_override_conditions` (`over_shop_id`, `over_olet_id`, `over_name_l1`, `over_name_l2`, `over_name_l3`, `over_name_l4`, `over_name_l5`, `over_from_prtq_id`, `over_to_prtq_id`, `over_from_price_level`, `over_to_price_level`, `over_charge_sc1`, `over_charge_sc2`, `over_charge_sc3`, `over_charge_sc4`, `over_charge_sc5`, `over_charge_tax1`, `over_charge_tax2`, `over_charge_tax3`, `over_charge_tax4`, `over_charge_tax5`, `over_charge_tax6`, `over_charge_tax7`, `over_charge_tax8`, `over_charge_tax9`, `over_charge_tax10`, `over_charge_tax11`, `over_charge_tax12`, `over_charge_tax13`, `over_charge_tax14`, `over_charge_tax15`, `over_charge_tax16`, `over_charge_tax17`, `over_charge_tax18`, `over_charge_tax19`, `over_charge_tax20`, `over_charge_tax21`, `over_charge_tax22`, `over_charge_tax23`, `over_charge_tax24`, `over_charge_tax25`, `over_dpan_id`, `over_check_ordering_type`, `over_priority`, `over_start_date`, `over_end_date`, `over_start_time`, `over_end_time`, `over_time_by`, `over_week_mask`, `over_holiday`, `over_day_before_holiday`, `over_special_day`, `over_day_before_special_day`, `over_start_table`, `over_end_table`, `over_stgp_id`, `over_ordering_type`, `over_perd_id`, `over_sphr_id`, `over_status`, `created`, `modified`)
SELECT 0, 0, 'Waive Service Charge For Takeout Item', '豁免外賣項目服務收費', '豁免外卖项目服务收费', 'テイクアウトアイテムのサービス料を免除', '테이크 아웃 항목에 대한 서비스 요금 면제', 0, 0, 0, 0, 'x', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', 0, '', 0, NULL, NULL, NULL, NULL, '', '1111111', '', '', '', '', 0, 0, 0, 't', 0, 0, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_override_conditions WHERE over_shop_id = 0 AND over_olet_id = 0 AND over_name_l1 = 'Waive Service Charge For Takeout Item' AND over_ordering_type = 't');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
