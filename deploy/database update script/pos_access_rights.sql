-- phpMyAdmin SQL Dump
-- version 3.5.5
-- http://www.phpmyadmin.net
--
-- 主机: localhost
-- 生成日期: 2014 年 01 月 24 日 02:14
-- 服务器版本: 5.5.29
-- PHP 版本: 5.4.11

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- 数据库: `hero_pos`
--

-- --------------------------------------------------------

--
-- 表的结构 `pos_access_rights`
--

--
-- 转存表中的数据 `pos_access_rights`
--

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'outlet_setting', 'Outlet Settings', '營業區設置', '营业区设置', '販売地域の設定', '영업 지역 설정', 'Outlet Settings', '營業區設置', '营业区设置', '販売地域の設定', '영업 지역 설정', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'outlet_setting');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'payment_method', 'Payment Method', '付款方法', '付款方法', 'お支払い方法', '지불 방법', 'Payment Method', '付款方法', '付款方法', 'お支払い方法', '지불 방법', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'payment_method');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'payment_class', 'Payment Class', '付款類型', '付款类型', 'お支払いの種類', '지불 유형', 'Payment Class', '付款類型', '付款类型', 'お支払いの種類', '지불 유형', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'payment_class');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'payment_group', 'Payment Group', '付款方法組合', '付款方法组合', 'お支払い方法の組み合わせ', '지불 방법 조합', 'Payment Group', '付款方法組合', '付款方法组合', 'お支払い方法の組み合わせ', '지불 방법 조합', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'payment_group');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'payment_permission_control', 'Payment Permission Control', '付款授權控制', '付款授权控制', 'お支払いの承認の制御', '결제 권한 부여 제어', 'Payment Permission Control', '付款授權控制', '付款授权控制', 'お支払いの承認の制御', '결제 권한 부여 제어', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'payment_permission_control');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'discount_type', 'Discount Type', '折扣類型', '折扣类型', '割引の種類', '할인 유형', 'Discount Type', '折扣類型', '折扣类型', '割引の種類', '할인 유형', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'discount_type');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'discount_group', 'Discount Group', '折扣組合', '折扣组合', '組み合わせ割引', '결합 할인 혜택', 'Discount Group', '折扣組合', '折扣组合', '組み合わせ割引', '결합 할인 혜택', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'discount_group');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'discount_permission_control', 'Discount Permission Control', '折扣授權控制', '折扣授权控制', '承認の制御を割引します', '할인 권한 부여 제어', 'Discount Permission Control', '折扣授權控制', '折扣授权控制', '承認の制御を割引します', '할인 권한 부여 제어', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'discount_permission_control');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'order_item_permission_control', 'Order Item Permission Control', '落單授權控制', '落单授权控制', '注文の承認の制御', '주문 권한 부여 제어', 'Order Item Permission Control', '落單授權控制', '落单授权控制', '注文の承認の制御', '주문 권한 부여 제어', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'order_item_permission_control');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'tax_and_sc_type', 'Tax & S.C. Type', '稅及服務費類型', '税及服务费类型', '税金とサービス料の種類', '세금 및 서비스 요금 유형', 'Tax & S.C. Type', '稅及服務費類型', '税及服务费类型', '税金とサービス料の種類', '세금 및 서비스 요금 유형', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'tax_and_sc_type');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'station', 'Station', '工作站', '工作站', 'ワークステーション', '워크스테이션', 'Station', '工作站', '工作站', 'ワークステーション', '워크스테이션', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'station');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'station_group', 'Station Group', '工作站組合', '工作站组合', 'ワーク ステーション グループ', '워크스테이션 그룹', 'Station Group', '工作站組合', '工作站组合', 'ワーク ステーション グループ', '워크스테이션 그룹', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'station_group');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'station_device', 'Station Device', '工作站機種', '工作站机种', 'ワーク ステーション モデル', '워크스테이션 모델', 'Station Device', '工作站機種', '工作站机种', 'ワーク ステーション モデル', '워크스테이션 모델', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'station_device');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'item_stock', 'Item Stock', '項目存貨', '项目存货', 'プロジェクトの在庫', '프로젝트 재고', 'Item Stock', '項目存貨', '项目存货', 'プロジェクトの在庫', '프로젝트 재고', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'item_stock');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'item_print_queue', 'Item Print Queue', '菜式列印隊列', '菜式打印队列', '料理印刷キュー', '요리 인쇄 큐', 'Item Print Queue', '菜式列印隊列', '菜式打印队列', '料理印刷キュー', '요리 인쇄 큐', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'item_print_queue');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'action_print_queue', 'Action Print Queue', '行動列印隊列', '行动打印队列', 'アクション印刷キュー', '작업 인쇄 큐', 'Action Print Queue', '行動列印隊列', '行动打印队列', 'アクション印刷キュー', '작업 인쇄 큐', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'action_print_queue');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'void_reason', 'Void Reason', '取消原因', '取消原因', 'キャンセルの理由', '취소 사유', 'Void Reason', '取消原因', '取消原因', 'キャンセルの理由', '취소 사유', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'void_reason');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'pantry_message', 'Pantry Message', '班地里訊息', '班地里讯息', 'クラスのフィールドの情報', '클래스 필드 정보', 'Pantry Message', '班地里訊息', '班地里讯息', 'クラスのフィールドの情報', '클래스 필드 정보', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'pantry_message');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'display_panel', 'Display Panel', '顯示面板', '显示面板', '表示パネル', '디스플레이 패널', 'Display Panel', '顯示面板', '显示面板', '表示パネル', '디스플레이 패널', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'display_panel');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'display_style', 'Display Style', '顯示樣式', '显示样式', '表示スタイル', '표시 스타일', 'Display Style', '顯示樣式', '显示样式', '表示スタイル', '표시 스타일', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'display_style');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'override_condition', 'Override Condition', '凌駕條件', '凌驾条件', '条件を上書き', '조건 무시', 'Override Condition', '凌駕條件', '凌驾条件', '条件を上書き', '조건 무시', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'override_condition');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'function', 'Function', '功能', '功能', '機能', '함수', 'Function', '功能', '功能', '機能', '함수', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'function');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'function_group', 'Function Group', '功能組合', '功能组合', '機能の組み合わせ', '특징의 조합', 'Function Group', '功能組合', '功能组合', '機能の組み合わせ', '특징의 조합', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'function_group');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'function_authority_control', 'Function Authority Control', '功能權限控制', '功能权限控制', '機能アクセス許可コントロール', '기능 사용 권한 제어', 'Function Authority Control', '功能權限控制', '功能权限控制', '機能アクセス許可コントロール', '기능 사용 권한 제어', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'function_authority_control');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'access_right_group', 'Access Right Group', '存取權限組合', '访问权限组合', 'アクセスのポートフォリオ', '액세스 포트폴리오', 'Access Right Group', '存取權限組合', '访问权限组合', 'アクセスのポートフォリオ', '액세스 포트폴리오', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'access_right_group');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'access_control', 'Access Control', '存取權限控制', '访问权限控制', 'アクセス制御', '액세스 제어', 'Access Control', '存取權限控制', '访问权限控制', 'アクセス制御', '액세스 제어', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'access_control');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'report_group', 'Report Group', '報表組合', '報表组合', 'レポートグループ', '보고서 그룹', 'Report Group', '報表組合', '報表组合', 'レポートグループ', '보고서 그룹', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'report_group');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'report_authority_control', 'Report Authority Control', '報表權限控制', '報表权限控制', 'レポート権限制御', '보고서 권한 제어', 'Report Authority Control', '報表權限控制', '報表权限控制', 'レポート権限制御', '보고서 권한 제어', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'report_authority_control');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'print_format', 'Print Format', '列印格式', '打印格式', '印刷用のフォーマット', '인쇄 형식', 'Print Format', '列印格式', '打印格式', '印刷用のフォーマット', '인쇄 형식', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'print_format');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'print_variable', 'Print Variable', '列印變數', '打印变量', '印刷時の変数', '인쇄 변수', 'Print Variable', '列印變數', '打印变量', '印刷時の変数', '인쇄 변수', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'print_variable');

INSERT INTO `pos_access_rights` (`arig_key`, `arig_name_l1`, `arig_name_l2`, `arig_name_l3`, `arig_name_l4`, `arig_name_l5`, `arig_short_name_l1`, `arig_short_name_l2`, `arig_short_name_l3`, `arig_short_name_l4`, `arig_short_name_l5`, `arig_status`, `created`, `modified`)
SELECT 'mix_and_match_rule', 'Mix and Match Rule', '配搭規則', '配搭规则', 'マッチは支配します', '경기 규칙', 'Mix and Match Rule', '配搭規則', '配搭规则', 'マッチは支配します', '경기 규칙', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_access_rights WHERE arig_key = 'mix_and_match_rule');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
