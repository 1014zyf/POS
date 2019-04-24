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
-- 表的结构 `pos_configs`
--

--
-- 转存表中的数据 `pos_configs`
--

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'interface_url', 0, 'http://<Server IP>/hero_pos/', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'interface_url');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'tender_amount', 0, '[0.1,0.2,0.5,1,2,5,10,20,50,100,500,1000]', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'tender_amount');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'fast_food_not_print_receipt', 0, 'false', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'fast_food_not_print_receipt');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'ordering_panel_input_numpad', 0, 'false', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'ordering_panel_input_numpad');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'fast_food_auto_takeout', 0, 'false', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'fast_food_auto_takeout');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'ordering_panel_show_price', 0, 'false', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'ordering_panel_show_price');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'coupon_system', 'coupon_server', 0, '', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'coupon_system' AND scfg_variable = 'coupon_server');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'coupon_system', 'coupon_server_port', 0, '0', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'coupon_system' AND scfg_variable = 'coupon_server_port');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'coupon_system', 'coupon_server_locale', 0, '', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'coupon_system' AND scfg_variable = 'coupon_server_locale');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'not_check_stock', 0, 'false', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'not_check_stock');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'fast_food_not_auto_waive_service_charge', 0, 'false', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'fast_food_not_auto_waive_service_charge');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'business_hour_warn_level', 0, '0', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'business_hour_warn_level');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'auto_switch_from_pay_result_to_starting_page_time_control', 0, '3', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'auto_switch_from_pay_result_to_starting_page_time_control');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'ordering_timeout', 0, '0', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'ordering_timeout');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'open_table_screen_mode', 0, '0', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'open_table_screen_mode');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'audit_log_level', 0, '0', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'audit_log_level');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'fine_dining_not_print_receipt', 0, 'false', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'fine_dining_not_print_receipt');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'void_reason_for_payment_auto_discount', 0, '', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'void_reason_for_payment_auto_discount');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'auto_close_cashier_panel', 0, '0', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'auto_close_cashier_panel');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'not_allow_open_new_check', 0, 'false', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'not_allow_open_new_check');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'loyalty_member', 0, 'false', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'loyalty_member');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'not_allow_to_order_when_zero_stock', 0, 'false', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'not_allow_to_order_when_zero_stock');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'turn_off_testing_printer', 0, 'false', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'turn_off_testing_printer');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'reprint_guest_check_times', 0, 'false', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'reprint_guest_check_times');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'reprint_receipt_times', 0, 'false', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'reprint_receipt_times');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'payment_check_types', 0, 'false', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'payment_check_types');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'include_previous_same_level_discount', 0, 'false', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'include_previous_same_level_discount');

INSERT INTO `pos_configs` (`scfg_by`, `scfg_record_id`, `scfg_section`, `scfg_variable`, `scfg_index`, `scfg_value`, `scfg_remark`, `created`, `modified`)
SELECT '', 0, 'system', 'member_discount_not_validate_member_module', 0, 'false', NULL, NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_configs WHERE scfg_section = 'system' AND scfg_variable = 'member_discount_not_validate_member_module');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
