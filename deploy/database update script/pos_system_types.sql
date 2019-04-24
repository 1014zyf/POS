-- phpMyAdmin SQL Dump
-- version 3.5.5
-- http://www.phpmyadmin.net
--
-- 主机: localhost
-- 生成日期: 2013 年 07 月 24 日 06:52
-- 服务器版本: 5.5.29
-- PHP 版本: 5.4.11

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
-- 表的结构 `pos_system_types`
--

--
-- 转存表中的数据 `pos_system_types`
--

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'station_device_type', 'android_tablet', 'Android Tablet', '安卓平板電腦', '安卓平板计算机', 'Android のタブレット コンピューター', '안 드 로이드 태블릿 컴퓨터', '', '', '', '', '', 1, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'station_device_type' AND styp_key = 'android_tablet');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'station_device_type', 'ipad', 'Apple iPad Tablet', '蘋果 iPad 平板電腦', '苹果 iPad 平板计算机', 'アップルの iPad のタブレット コンピューター', '애플의 iPad 태블릿 컴퓨터', '', '', '', '', '', 2, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'station_device_type' AND styp_key = 'ipad');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'station_device_type', 'touch_screen', 'Touch Screen', '觸摸顯示屏', '触摸显示屏', 'タッチ スクリーン', '터치 스크린', '', '', '', '', '', 3, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'station_device_type' AND styp_key = 'touch_screen');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'station_device_type', 'android_mobile', 'Android Mobile', '安卓手提電話', '安卓手提电话', 'Android の携帯電話', '안드로이드 휴대 전화', '', '', '', '', '', 4, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'station_device_type' AND styp_key = 'android_mobile');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'station_device_type', 'auto_station', 'Auto Station', '自動工作站', '自动工作站', '自動ワークステーション', '자동 워크 스테이션', '', '', '', '', '', 5, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'station_device_type' AND styp_key = 'auto_station');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'station_device_type', 'portal_station', 'Portal Station', '入口工作站', '入口工作站', 'ポータルステーション', '포털 스테이션', '', '', '', '', '', 6, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'station_device_type' AND styp_key = 'portal_station');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'station_device_type', 'self_order_kiosk', 'Self-Order Kiosk', '自助點菜機', '自助点菜机', 'セルフ サービス順序機械', '셀프 주문 시스템', '', '', '', '', '', 7, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'station_device_type' AND styp_key = 'self_order_kiosk');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'payment_type', 'cash', 'Cash', '現金付款', '现金付款', '現金の支払い', '현금 결제', '', '', '', '', '', 1, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'payment_type' AND styp_key = 'cash');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'payment_type', 'credit_card', 'Credit Card', '信用咭付款', '信用咭付款', 'クレジット カード決済', '신용 카드 결제', '', '', '', '', '', 2, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'payment_type' AND styp_key = 'credit_card');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'payment_type', 'duty_meal', 'Duty Meal', '值班餐', '值班餐', 'デューティ·ミール', '듀티 식사', '', '', '', '', '', 3, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'payment_type' AND styp_key = 'duty_meal');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'payment_type', 'on_credit', 'On Credit', '掛帳', '挂帐', '未払い債務', '미지급 채무', '', '', '', '', '', 4, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'payment_type' AND styp_key = 'on_credit');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'payment_type', 'voucher', 'Voucher', '憑證', '凭证', 'バウチャー', '바우처', '', '', '', '', '', 5, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'payment_type' AND styp_key = 'voucher');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'payment_type', 'coupon', 'Coupon', '優惠卷', '优惠卷', 'クーポン', '쿠폰', '', '', '', '', '', 6, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'payment_type' AND styp_key = 'coupon');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'payment_type', 'octopus', 'Octopus', '八達通', '八达通', 'タコ', '문어', '', '', '', '', '', 7, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'payment_type' AND styp_key = 'octopus');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'payment_type', 'reservation_payment', 'Reservation Payment', '預訂付款', '预订付款', '予約お支払い', '예약 결제', '', '', '', '', '', 8, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'payment_type' AND styp_key = 'reservation_payment');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'payment_type', 'member', 'Member', '會員', '会员', 'メンバー', '회원', '', '', '', '', '', 9, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'payment_type' AND styp_key = 'member');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'payment_type', 'employee_member', 'Employee Member', '員工會員', '员工会员', 'スタッフ', '직원 회원', '', '', '', '', '', 10, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'payment_type' AND styp_key = 'employee_member');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'payment_type', 'cash_voucher', 'Cash Voucher', '現金券', '现金券', '金券', '현금 결제', '', '', '', '', '', 11, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'payment_type' AND styp_key = 'cash_voucher');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'payment_type', 'points', 'Points', '點數', '点数', 'ポイント', '평면도 기능', '', '', '', '', '', 12, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'payment_type' AND styp_key = 'points');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'payment_type', 'rewrite_card', 'Rewrite Card', '重寫卡', '重写卡', 'カードを書き換える', '카드 다시 쓰기', '', '', '', '', '', 13, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'payment_type' AND styp_key = 'rewrite_card');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'payment_type', 'company_account', 'Company Account', '公司戶口', '公司户口', '会社アカウント', '회사 계정', '', '', '', '', '', 14, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'payment_type' AND styp_key = 'company_account');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'discount_type', 'member_discount', 'Member Discount', '會員專亨折扣', '会员专亨折扣', 'メンバー割引をハングします', '회원 할인 중단', '', '', '', '', '', 1, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'discount_type' AND styp_key = 'member_discount');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'discount_type', 'employee_discount', 'Employee Discount', '員工折扣', '员工折扣', '従業員割引', '직원 할인', '', '', '', '', '', 2, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'discount_type' AND styp_key = 'employee_discount');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'action_print_queue_type', 'delete_item', 'Delete Item', '刪除項目', '删除项目', 'アイテムを削除します', '항목 삭제', '', '', '', '', '', 1, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'action_print_queue_type' AND styp_key = 'delete_item');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'action_print_queue_type', 'change_quantity', 'Change Quantity', '更改數量', '更改数量', '数量を変更します', '수량을 변경', '', '', '', '', '', 2, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'action_print_queue_type' AND styp_key = 'change_quantity');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'action_print_queue_type', 'pantry_message', 'Pantry Message', '班地里訊息', '班地里讯息', 'パントリー メッセージ', '식료품 저장실 메시지', '', '', '', '', '', 3, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'action_print_queue_type' AND styp_key = 'pantry_message');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'action_print_queue_type', 'rush_order', 'Rush Order', '追單', '追单', '順序キーを押します', '보도 순서', '', '', '', '', '', 4, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'action_print_queue_type' AND styp_key = 'rush_order');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'action_print_queue_type', 'merge_table', 'Merge Table', '合併檯', '合并台', 'テーブルをマージします', '테이블 병합', '', '', '', '', '', 5, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'action_print_queue_type' AND styp_key = 'merge_table');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'action_print_queue_type', 'merge_table_item', 'Merge Table (With Item)', '合併檯 (連項目)', '合并台 (连项目)', '複合単位（偶数アイテム）', '결합 유닛 (심지어 항목)', '', '', '', '', '', 6, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'action_print_queue_type' AND styp_key = 'merge_table_item');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'action_print_queue_type', 'split_table', 'Split Table', '分檯', '分台', 'テーブル', '테이블', '', '', '', '', '', 7, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'action_print_queue_type' AND styp_key = 'split_table');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'action_print_queue_type', 'change_table', 'Change Table', '轉檯', '转台', 'テーブルを変更する', '테이블을 변경', '', '', '', '', '', 8, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'action_print_queue_type' AND styp_key = 'change_table');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'action_print_queue_type', 'change_table_item', 'Change Table (With Item)', '轉檯 (連菜式)', '转台 (连菜式)', 'ターン テーブル （と料理）', '(요리)와 턴테이블', '', '', '', '', '', 9, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'action_print_queue_type' AND styp_key = 'change_table_item');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'action_print_queue_type', 'void_check', 'Void Check', '取消帳單', '取消账单', '法案をキャンセルします。', '빌 취소', '', '', '', '', '', 10, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'action_print_queue_type' AND styp_key = 'void_check');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'action_print_queue_type', 'credit_card_auth_topup', 'Credit Card Authorization / Top-up/ Cancel', '信用卡授权/授权充值/取消授权', '信用卡授權/授權充值/取消授權', 'クレジットカードの承認/承認の再請求/キャンセルの承認', '신용 카드 승인 / 승인 재충전 / 취소 승인', '', '', '', '', '', 11, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'action_print_queue_type' AND styp_key = 'credit_card_auth_topup');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'action_print_queue_type', 'questionnaire', 'Questionnaire', '問卷', '问卷', 'アンケート', '질문 사항', '', '', '', '', '', 12, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'action_print_queue_type' AND styp_key = 'questionnaire');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'action_print_queue_type', 'loyalty_balance', 'Loyalty Balance', '積分計劃餘額', '积分计划余额', 'ロイヤルティ残高', '충성도 균형', '', '', '', '', '', 13, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'action_print_queue_type' AND styp_key = 'loyalty_balance');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'action_print_queue_type', 'member_balance', 'Member Balance', '會員餘額', '会员余额', '会員残高', '회원 잔액', '', '', '', '', '', 14, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'action_print_queue_type' AND styp_key = 'member_balance');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'action_print_queue_type', 'member_enrollment', 'Member Enrollment', '會員註冊', '会员注册', '会員登録', '회원 등록', '', '', '', '', '', 15, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'action_print_queue_type' AND styp_key = 'member_enrollment');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'display_style_type', 'button', 'Button', '按鈕', '按钮', 'ボタン', '버튼', '', '', '', '', '', 1, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'display_style_type' AND styp_key = 'button');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'display_style_type', 'frame', 'Frame', '框架', '框架', 'フレームワーク', '버튼', '', '', '', '', '', 2, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'display_style_type' AND styp_key = 'frame');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'display_style_type', 'window', 'Window', '視窗', '窗口', 'ウィンドウ', '창', '', '', '', '', '', 3, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'display_style_type' AND styp_key = 'window');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'display_panel_zone', 'ordering', 'Ordering', '點菜', '点菜', 'アラカルト', '일품 메뉴', '', '', '', '', '', 1, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'display_panel_zone' AND styp_key = 'ordering');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'display_panel_zone', 'item_selected', 'Select Item', '選擇菜式', '选择菜式', '料理を選択します', '요리 선택', '', '', '', '', '', 2, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'display_panel_zone' AND styp_key = 'item_selected');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'display_panel_zone', 'ordering_function', 'Ordering Function', '點菜功能', '点菜功能', 'ファンクションオーダー', '기능 주문', '', '', '', '', '', 3, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'display_panel_zone' AND styp_key = 'ordering_function');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'display_panel_zone', 'cashier', 'Cashier', '收銀', '收银', 'キャッシャー', '출납원', '', '', '', '', '', 4, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'display_panel_zone' AND styp_key = 'cashier');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'display_panel_zone', 'floor_plan_function', 'Floor Plan Function', '平面圖功能', '平面图功能', 'フロアプラン機能', '평면도 기능', '', '', '', '', '', 4, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'display_panel_zone' AND styp_key = 'floor_plan_function');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'station_device_type', 'self_order_kiosk', 'Self-Order Kiosk', '自助點菜機', '自助点菜机', 'セルフ サービス順序機械', '셀프 주문 시스템', '', '', '', '', '', 13, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'station_device_type' AND styp_key = 'self_order_kiosk');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'display_panel_zone', 'ordering_basket', 'Ordering Basket', '點菜籃', '点菜篮', '注文バスケット', '주문 바구니', '', '', '', '', '', 13, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'display_panel_zone' AND styp_key = 'ordering_basket');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'payment_type', 'rewrite_card', 'Rewrite Card', '讀寫卡', '读写卡', 'カードの読み書き', '카드 읽기 및 쓰기', '', '', '', '', '', 13, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'payment_type' AND styp_key = 'rewrite_card');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'action_print_queue_type', 'loyalty_balance', 'Loyalty Balance', '積分計劃餘額', '积分计划余额', 'ロイヤルティ残高', '충성도 균형', '', '', '', '', '', 13, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'action_print_queue_type' AND styp_key = 'loyalty_balance');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'action_print_queue_type', 'loyalty_auto_top_up', 'Loyalty Auto Top Up', '積分計劃自動充值', '积分计划自动充值', 'ポイントプラン自動再充電', '포인트 충전 자동 충전', '', '', '', '', '', 13, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'action_print_queue_type' AND styp_key = 'loyalty_auto_top_up');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'action_print_queue_type', 'award_list', 'Award List', '積分兌換表', '积分兑换表', 'ポイント償還フォーム', '수상 목록', '', '', '', '', '', 13, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'action_print_queue_type' AND styp_key = 'award_list');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'action_print_queue_type', 'tip_tracking', 'Tip Tracking', '小費分配紀錄', '小费分配纪录', 'チップの分布記録', '팁 할당 레코드', '', '', '', '', '', 18, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'action_print_queue_type' AND styp_key = 'tip_tracking');

INSERT INTO `pos_system_types` (`styp_type`, `styp_key`, `styp_name_l1`, `styp_name_l2`, `styp_name_l3`, `styp_name_l4`, `styp_name_l5`, `styp_short_name_l1`, `styp_short_name_l2`, `styp_short_name_l3`, `styp_short_name_l4`, `styp_short_name_l5`, `styp_seq`, `styp_status`, `created`, `modified`)
SELECT 'alert_message_type', 'print_queue_status', 'Print Queue Status', '列印隊列狀態', '打印队列状态', '印刷キューの状態', '인쇄 큐 상태', '', '', '', '', '', 1, '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_system_types WHERE styp_type = 'alert_message_type' AND styp_key = 'print_queue_status');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
