-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- 主机: localhost
-- 生成日期: 2013 年 07 月 24 日 02:43
-- 服务器版本: 5.5.27-log
-- PHP 版本: 5.4.3

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
-- 表的结构 `pos_functions`
--

--
-- 转存表中的数据 `pos_functions`
--

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'print_check', 'Print Check', '印單', '印单', '単一のプリント', '단일 인쇄', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'print_check');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'cancel', 'Cancel', '取消', '取消', 'キャンセル', '취소', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'cancel');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'send_check', 'Send Check', '送單', '送单', 'シングルを送る', '단일 전송', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'send_check');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'change_table', 'Change Table', '更改檯號', '更改台号', '局番を変更する', '내선 번호를 변경', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'change_table');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'delete_item', 'Delete Item', '刪除項目', '删除项目', 'アイテムを削除', '항목 삭제', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'delete_item');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'change_cover', 'Change Cover', '更改客人數量', '更改客人数量', 'ゲストの数を変更します', '손님의 수를 변경', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'change_cover');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'item_discount', 'Item Discount', '項目折扣', '项目折扣', 'プロジェクト割引', '프로젝트 할인', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'item_discount');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'paid', 'Paid', '付款', '付款', '支払い', '지불', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'paid');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'logout', 'Exit', '退出', '退出', 'やめる', '종료', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'logout');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'print_and_paid', 'Print And Paid', '印單及付款', '印单及付款', 'インドの注文と支払い', '인도 주문 및 결제', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'print_and_paid');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'void_check', 'Void Check', '取消單據', '取消单据', '文書をキャンセル', '문서 취소', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'void_check');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'release_payment', 'Release Payment', '取消付款', '取消付款', '支払いをキャンセル', '결제 취소', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'release_payment');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'change_language', 'Change Language', '轉換語言', '转换语言', '変換言語', '변환 언어', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'change_language');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'delete_last', 'Delete Last', '刪除最後項目', '删除最后项目', '最後の削除', '마지막으로 삭제', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'delete_last');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'change_quantity_last', 'Change Last Item Qty', '更改最後項目數量', '更改最后项目数量', 'プロジェクトの最後の番号を変更します', '프로젝트의 마지막 번호를 변경', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'change_quantity_last');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'guest_check_preview', 'Guest Check Preview', '單據預覽', '单据预览', 'ドキュメントのプレビュー', '문서 미리보기', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'guest_check_preview');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'unlock_table', 'Unlock Table', '解除檯號鎖', '解除台号锁', 'ステーション番号ロックを持ち上げ', '국번 잠금 장치를 들어 올립니다', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'unlock_table');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'check_history', 'Check History', '單據歷史', '单据历史', '歴史的文書', '역사적 문서', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'check_history');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'item_modifier', 'Item Modifier', '項目改碼', '项目改码', 'プロジェクトは、コードを変更', '프로젝트 코드를 변경', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'item_modifier');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'waive_sc_tax', 'Waive SC / Tax', '免服務費/稅', '免服务费/税', '無料のサービス料/税', '무료 서비스 수수료 / 세금', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'waive_sc_tax');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'add_sc_tax', 'Add SC / Tax', '加服務費 / 稅', '加服务费 / 税', 'サービス料/税プラス', '플러스 서비스 요금 / 세금', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'add_sc_tax');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'item_void_discount_multiple_items', 'Void Item Discount for Multiple Items', '刪除項目折扣於多樣項目', '删除项目折扣于多样项目', '複数の項目上のアイテムの割引を削除', 'Void Item Discount for Multiple Items', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'item_void_discount_multiple_items');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'item_discount_multiple_items', 'Item Discount For Multiple Item', '項目折扣於多樣項目', '项目折扣于多样项目', '複数の項目をプロジェクトの割引', '여러 항목 프로젝트에 대한 할인', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'item_discount_multiple_items');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'open_drawer', 'Open Drawer', '開櫃', '开柜', 'オープンキャビネット', '캐비닛을 엽니 다', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'open_drawer');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'cashier_mode', 'Cashier Mode', '收銀模式', '收银模式', 'キャッシャーモード', '캐셔 모드', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'cashier_mode');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'web_report', 'Report', '報表', '报表', 'レポート', '신고', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'web_report');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'check_listing', 'Check Listing', '單據列表', '单据列表', 'ドキュメントリスト', '문서 목록', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'check_listing');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'switch_user', 'Switch User', '切換用戶', '切换用户', 'ユーザーの切り替え', '사용자 전환을', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'switch_user');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'split_item_to_other_table', 'Split Item To Other Table', '分拆項目至其他檯', '拆分项目至其他台', '他局へのサブプロジェクト倍', '다른 방송국에 접어 하위 프로젝트', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'split_item_to_other_table');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'merge_table', 'Merge Table', '合併檯', '合并台', '合併', '합병', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'merge_table');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'daily_start', 'Daily Start', '每日開始', '每日开始', '日が始まります', '하루가 시작됩니다', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'daily_start');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'daily_close', 'Daily Close', '每日結束', '每日結束', '一日の終わり', '매일 끝', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'daily_close');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'reload_business_setting', 'Reload Business Setup', '重新讀取營業設置', '重新读取营业设置', '再読み込みビジネス設定', '다시 읽어 비즈니스 설정', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'reload_business_setting');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'reprint_receipt', 'Re-print receipt', '重印收據', '重印收据', '領収書の再印刷', '영수증을 다시 인쇄', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'reprint_receipt');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'open_share_table', 'Open share table', '開延伸檯', '开延伸台', 'エクステンションテーブルの上', '확장 테이블에', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'open_share_table');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'testing_printer', 'Test printer', '測試打印機', '测试打印机', 'プリンタをテストします。', '프린터를 테스트합니다', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'testing_printer');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'mark_delivery', 'Mark delivery', '設定已出菜項目', '设定已出菜项目', 'プロジェクトは、メニューを設定されている', '프로젝트 메뉴에서 설정되었습니다', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'mark_delivery');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'print_serving_list', 'Serving List', '上菜單', '上菜单', 'メニューにある', '메뉴에서', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'print_serving_list');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'rush_order', 'Rush Order', '追單', '追单', '順序をラッシュ', '붐빔 순서', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'rush_order');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'pantry_message', 'Pantry Message', '班地里訊息', '班地里讯息', 'パントリーメッセージ', '식료품 저장실 메시지', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'pantry_message');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'admin_mode', 'Admin Mode', '經理系統', '经理系统', 'マネージャーのシステム', '관리자 시스템', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'admin_mode');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'toggle_fast_food_mode', 'Toggle Fast Food Mode', '轉換快餐模式', '转换快餐模式', '高速のモード変換', '고속 모드 전환', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'toggle_fast_food_mode');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'stock_delivery_invoice_setup', 'Stock Delivery Order Setup', '發貨單設置', '发货单设置', '請求書の設定', '송장 설정', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'stock_delivery_invoice_setup');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'receive_stock_delivery_invoice', 'Receive Stock Delivery Invoice', '接收發貨單', '接收发货单', '請求書を受け取る', '송장을받을', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'receive_stock_delivery_invoice');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'switch_outlet', 'Switch Outlet', '轉換營業區', '转换营业区', 'コンバージョンビジネス地区', '변환 사업 지구', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'switch_outlet');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'preorder', 'Retrieve pre-order items', '讀取自助點菜項目', '读取自助点菜项目', 'アラカルトアイテムをビュッフェ読む', '일품 요리를 뷔페 읽기', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'preorder');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'set_member', 'Set Member', '設定會員', '设定会员', 'メンバーを設定します', '회원 설정', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'set_member');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'increment_course', 'Increment Course', '增加出菜次序', '增加出菜次序', '皿を高めるため', '요리를 높이기 위해', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'increment_course');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'takeout', 'takeout', '外賣', '外卖', '配信', '배달', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'takeout');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'reprint_last_receipt', 'Reprint last receipt', '重印最後一張收據', '重印最后一张收据', '最後の領収書の再印刷', '마지막 영수증을 다시 인쇄', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'reprint_last_receipt');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'last_20_paid_check', 'Last 20 paid checks', '最後20張已付款收據', '最后20张已付款收据', '最後の20有料チェック', '지난 20 지불 확인', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'last_20_paid_check');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'stock_operation', 'Stock Operation', '存貨管理', '存货管理', '在庫管理', '재고 관리', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'stock_operation');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'toggle_non_revenue', 'Toggle Non-revenue', '轉換非營業收入', '转换非营业收入', '営業外収益に変換', '변환 영업 이익', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'toggle_non_revenue');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'check_dine_in', 'Dine-In', '堂食', '堂食', 'ダインイン', '식사 - 인', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'check_dine_in');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'check_takeout', 'Check Takeout', '全單外賣', '全单外卖', 'ビルテイクアウェイ', '법안테이크 아웃', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'check_takeout');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'toggle_revenue', 'Toggle Revenue', '轉換營業收入', '转换营业收入', '営業利益に変換', '영업 이익을 변환', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'toggle_revenue');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'item_discount_all_items', 'Item Discount For All Items', '項目折扣於所有項目', '项目折扣于所有项目', 'すべての項目のプロジェクト割引', '모든 품목에 프로젝트 할인', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'item_discount_all_items');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'delete_whole_last_item', 'Delete Last Item (Whole)', '刪除最後項目(整個)', '删除最后项目(整个)', '最後の項目を削除します(全体)', '마지막 메뉴 항목을 삭제 (전체)', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'delete_whole_last_item');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'void_paid_check', 'Void Paid Check', '取消已付單據', '取消已付单据', '支払った領収書を取り消す', '지불 영수증 취소', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'void_paid_check');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'receipt_preview', 'Receipt Preview', '收據預覽', '收据预览', 'レシートプレビュー', '영수증 미리보기', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'receipt_preview');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'sell_coupon', 'Sell Coupon', '售賣優惠卷', '售卖优惠卷', 'クーポンを販売', '쿠폰 판매', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'sell_coupon');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'direct_report', 'Report - Direct Generate', '報表 - 直接產生', '报表 - 直接产生', 'ステートメント - 直接である', '보고서 - 직접이다', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'direct_report');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'new_item_no_kitchen_slip', 'Item no kitchen slip', '補單已做', '补单已做', '単一サプリメントは行われてい', '하나의 보충이 완료되었습니다', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'new_item_no_kitchen_slip');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'add_item_by_code', 'Enter Item Code', '輸入項目編號', '输入项目编号', '商品コードを入力してください', '상품 코드를 입력', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'add_item_by_code');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'stock_soldout', 'Item Soldout', '項目沽清', '项目售罄', '商品は売り切れ', '매진', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'stock_soldout');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'adjust_tips', 'Adjust Tips', '更改小費', '更改小费', '先端を変える', '팁 변경', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'adjust_tips');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'open_first_drawer', 'Open Drawer 1', '開抽屜1', '开抽屉1', '引き出しを開く1', '서랍을 열고1', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'open_first_drawer');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'open_second_drawer', 'Open Drawer 2', '開抽屜2', '开抽屉2', '引き出しを開く2', '서랍을 열고2', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'open_second_drawer');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'search_item', 'Search Item', '搜尋項目', '搜寻项目', '探索項目', '검색 항목', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'search_item');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'delete_multiple_item', 'Delete Multiple Item', '刪除多個項目', '删除多个项目', '複数の項目を削除する', '여러 항목 삭제', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'delete_multiple_item');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'park_order_by_station', 'Parking by station', '以工作站暫時擱置單據', '以工作站暂时搁置单据', '未決でのワークステーションへのドキュメント', '휴지의 워크 스테이션에 문서', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'park_order_by_station');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'retrieve_park_order_by_station', 'Retrieve station park order', '讀取擱置單據 (工作站)', '读取搁置单据 (工作站)', '棚上げ文書を読む（ワークステーション）', '보류 된 문서를 읽을 (워크 스테이션)', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'retrieve_park_order_by_station');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'preorder_by_table', 'Retrieve pre-order items (Table)', '讀取自助點菜項目 (檯號)', '读取自助点菜项目 (台号)', 'ビュッフェアラカルト項目を読む（テラス）', '뷔페 일품 요리 을 읽고(테라스)', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'preorder_by_table');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'pms_enquiry', 'PMS Enquiry', 'PMS查詢', 'PMS查询', 'PMS問い合わせ', 'PMS 문의', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'pms_enquiry');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'user_time_in_out', 'User Time In/Out', '員工上班/下班時間', '员工上班/下班时间', 'スタッフの仕事/労働時間', '직원 일 / 근무 시간', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'user_time_in_out');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'octopus_read_card', 'Read Octopus Card', '讀取八達通卡資料', '读取八达通卡资料', 'オクトパスカードのデータを読み取る', '옥토퍼스 카드 데이터 읽기', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'octopus_read_card');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'octopus_add_value', 'Octopus Add Value', '八達通卡增值', '八达通卡增值', 'タコ鑑賞', '낙지 감사', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'octopus_add_value');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'octopus_reconnect', 'OCTOPUS Re-connect', '修復八達通機', '修复八达通机', 'タコの機械の修理', '낙지 기계 수리', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'octopus_reconnect');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'octopus_generate_trans_file', 'Generate OCTOPUS transaction file', '製作八達通交易記錄文件', '制作八达通交易记录文件', '生産タコトランザクション·ログ·ファイル', '생산 낙지 트랜잭션 로그 파일', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'octopus_generate_trans_file');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'cashier_settlement', 'Cashier Settlement', '收銀員截數', '收银员截数', 'レジ決済', '캐셔 정산', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'cashier_settlement');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'restart_pms_shell', 'Restart PMS connection program', '重新啟動連接PMS程式', '重新启动连接的PMS程式', 'PMSを接続するために、プログラムを再起動', 'PMS를 연결하는 프로그램을 다시 시작', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'restart_pms_shell');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'set_menu_replace_item', 'Set Menu Replace Item', '菜單更換項目', '菜单更换项目', '設定メニュー項目を交換してください', '설정 메뉴 항목을 대체', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'set_menu_replace_item');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'repeat_item', 'Repeat Item', '重複項目', '重复项目', '繰り返し項目', '반복 항목', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'repeat_item');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'repeat_multiple_items', 'Repeat Multiple Items', '重複多個項目', '重复多个项目', '複数のアイテムを繰り返し', '여러 항목을 반복합니다', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'repeat_multiple_items');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'split_table', 'Split Table', '分檯', '分台', 'サブステーション', '서브 스테이션', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'split_table');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'stock_balance_chg', 'Stock Balance Change', '更改存貨結餘', '更改存货结余', '在庫残高を変更します。', '재고 잔액 변경', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'stock_balance_chg');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'split_check_by_options', 'Split Check By Options', '分單選項', '分单选项', 'つのオプションに分割', '하나의 옵션으로 분할', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'split_check_by_options');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'check_discount', 'Check Discount', '全單折扣', '全单折扣', '合計手形割引', '총 청구 할인', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'check_discount');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'void_check_discount', 'Void Check Discount', '刪除帳單折扣', '删除帐单折扣', '手形割引を削除', '청구 할인 삭제', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'void_check_discount');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'adjust_payments', 'Adjust Payments', '修正付款', '修正付款', '補正支払い', '보정 지급', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'adjust_payments');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'paid_in', 'Paid In', '存入', '存入', 'で支払わ', '지급', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'paid_in');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'paid_out', 'Paid Out', '支出', '支出', '支払わ', '납입', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'paid_out');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'drop', 'Drop', '提取', '提取', '抽出', '추출물', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'drop');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'retrieve_ordered_items_by_resv_confirm_no', 'Retrieve ordered items in reservation', '讀取預訂的已點選菜式', '读取预订的已点选菜式', '料理の本を読むをクリックしていた', '요리 책을 읽기를 클릭왔다', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'retrieve_ordered_items_by_resv_confirm_no');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'stop_pms_shell', 'Stop PMS connection program', '停止連接PMS程式', '停止连接PMS程式', '接続PMSプログラムを停止', '연결 PMS 프로그램을 중지', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'stop_pms_shell');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'toggle_self_order_kiosk_mode', 'Toggle Self-Order Kiosk Mode', '轉換自助點菜模式', '转换自助点菜模式', 'ラ·モードA変換ビュッフェ', '라 모드 변환 뷔페', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'toggle_self_order_kiosk_mode');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'set_call_number', 'Set Call Number', '設定呼叫號碼', '设定呼叫号码', '設定呼出番号', '설정 전화 번호', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'set_call_number');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'insert_item', 'Insert Item', '插入項目', '插入项目', 'プロジェクトの挿入', '삽입 프로젝트', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'insert_item');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'table_message_reminder', 'Table Message Reminder', '單據備忘訊息', '单据备忘讯息', 'テーブルメッセージのリマインダー', '표 메시지 알림', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'table_message_reminder');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'add_edit_check_info', 'Add/Edit Check Info', '增加/修改單據資料', '增加/修改单据资料', '追加/文書情報を変更する', '추가 / 문서 정보를 수정', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'add_edit_check_info');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'manual_change_price_level', 'Manual Change Price Level', '價格轉換', '价格转换', '転換価格', '전환 가격', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'manual_change_price_level');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'restart_payment_interface_shell', 'Restart payment interface program', '重新啟動付款程式', '重新启动付款程式', 'プログラムを支払うためにお金を再起動します', '프로그램을 지불 할 돈을 다시 시작', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'restart_payment_interface_shell');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'stop_payment_interface_shell', 'Stop payment interface program', '停止付款程式', '停止付款程式', 'プログラムの支払いを停止します', '프로그램에 대한 지불 정지', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'stop_payment_interface_shell');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'restart_auto_station', 'Restart auto station', '重新啟動自動工作站', '重新启动自动工作站', '自動ワークステーションを再起動します', '자동 워크 스테이션을 다시 시작합니다', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'restart_auto_station');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'check_extra_charge', 'Check Extra Charge', '帳單額外費用', '帐单额外费用', '請求余分なコスト', '결제 추가 비용', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'check_extra_charge');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'void_check_extra_charge', 'Void Check Extra Charge', '刪除帳單額外費用', '删除帐单额外费用', '法案追加コストを削除', '법안 추가 비용을 삭제', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'void_check_extra_charge');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'set_other_taiwan_gui_type', 'Set Other Taiwan GUI Type', '更改台灣統一發票類別', '更改台湾统一发票类别', '台湾の統一インボイスチェンジカテゴリ', '대만의 통일 송장 변경 카테고리', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'set_other_taiwan_gui_type');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'partial_send_check', 'Partial Send Check', '部分送出項目', '部分送出项目', '部分的な送信チェック', '부분 전송 확인', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'partial_send_check');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'reprint_last_taiwan_gui_receipt', 'Reprint Last GUI Receipt', '重印最後一張台灣統一發票', '重印最后一张台湾统一发票', '最後台湾の統一請求書復刻', '대만의 통일 송장 재판은 지난', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'reprint_last_taiwan_gui_receipt');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'reprint_taiwan_gui_receipt', 'Reprint GUI Receipt', '重印台灣統一發票', '重印台湾统一发票', '台湾の統一請求書復刻', '대만의 통일 송장 재 인쇄', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'reprint_taiwan_gui_receipt');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'set_taiwan_gui_next_trans_num', 'Set Taiwan GUI Next Transaction Number', '設定台灣統一發票號', '设定台湾统一发票号', '台湾は、均一な請求書番号を設定しました', '대만은 균일 한 송장 번호를 설정', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'set_taiwan_gui_next_trans_num');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'print_paid_check', 'Print Paid Check', '列印已付帳單', '列印已付帐单', '印刷支払わチェック', '인쇄 지불 확인', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'print_paid_check');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'select_pending_item', 'Select Pending Item', '選擇延後傳送廚房單項目', '选择延后传送厨房单项目', '遅延伝送キッチン単一のプロジェクトを選択してください', '지연 전송 부엌 하나의 프로젝트를 선택', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'select_pending_item');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'print_pending_item', 'Print Pending Item', '列印延後傳送廚房單項目', '列印延后传送厨房单项目', '印刷遅延伝送キッチン単一のプロジェクト', '인쇄 지연 전송 부엌 하나의 프로젝트', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'print_pending_item');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'reprint_guest_check', 'Reprint Guest Check', '重印單據', '重印单据', '再印刷文書', '재 인쇄 문서', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'reprint_guest_check');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'svc_enquiry', 'SVC Enquiry', 'SVC查詢', 'SVC查询', 'SVCのお問い合わせ', 'SVC 문의', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'svc_enquiry');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'change_ordered_item_price_level', 'Change Ordered Item Price Level', '轉換已下單項目價格', '轉換已下單項目價格', '転換価格は、一つのプロジェクトの下にされています', '전환 가격은 하나의 프로젝트 아래에있다', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'change_ordered_item_price_level');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'multiple_rush_order', 'Rush Order for Multiple Items', '追單於多樣項目', '追单于多样项目', 'チェイスChanyu多様なプロジェクト', '체이스 Chanyu 다양한 프로젝트', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'multiple_rush_order');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'override_condition_activation', 'Override Condition Activation', '凌駕條件啟動', '凌驾条件启动', '条件をオーバーライド開始', '최우선 조건 시작', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'override_condition_activation');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'change_password', 'Change Password', '更改密碼', '更改密码', 'パスワードを変更', '암호 변경', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'change_password');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'search_panel_hot_item', 'Search panel hot item', '搜尋面板項目', '搜寻面板项目', '検索パネルプロジェクト', '검색 패널 프로젝트', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'search_panel_hot_item');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'svc_coupon_redeem_item', 'SVC Coupon Enquiry', 'SVC優惠券查詢', 'SVC优惠券查询', 'SVC割引クーポン', 'SVC할인 쿠폰', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'svc_coupon_redeem_item');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'loyalty_member_bonus_redemption', 'Loyalty Member Bonus Redemption', '忠誠會員優惠換取', '忠诚会员优惠换取', 'ロイヤリティメンバーボーナス償還', '로열티 회원 보너스 보너스', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'loyalty_member_bonus_redemption');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'check_cover_discount', 'Check Cover Discount', '帳單人數折扣', '帐单人数折扣', '手形割引の数', '할인 지폐의 수', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'check_cover_discount');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'item_cover_discount', 'Item Cover Discount', '菜式人數折扣', '菜式人数折扣', '皿数割引', '요리 할인의 수', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'item_cover_discount');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'assign_employee_card_number', 'Assign Employee Card Number', '分配員工卡編號', '分配员工卡编号', '従業員カード番号を割り当て', '직원 카드 번호를 할당', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'assign_employee_card_number');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'pms_redeem_package', 'PMS Redeem Package', 'PMS換取套票', 'PMS换取套票', 'PMS交換パッケージ', 'PMS 교환 패키지', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'pms_redeem_package');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'print_detail_check', 'Print Detail Check', '詳細賬單', '详细账单', '詳細課金', '자세한 결제', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'print_detail_check');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'membership_voucher_redempt', 'Membership Voucher Redemption', '會員優惠卷兌換', '会员积分兑换', 'グレード チェンジ', '등급 변경', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'membership_voucher_redempt');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'membership_registration', 'Membership Registration', '會員註冊', '会员注册', '登録メンバー', '등록 된 회원', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'membership_registration');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'search_item_by_sku', 'Search item by SKU', '按SKU編號搜尋項目', '按SKU编号搜寻项目', 'SKU 番号でアイテムを探す', 'SKU 번호 항목에 대 한 검색', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'search_item_by_sku');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'refund_item', 'Refund Item', '退款項目', '退款项目', '払い戻しアイテム', '상품 환불', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'refund_item');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'toggle_ordering_basket_information', 'Toggle Ordering Basket Information', '切換購物籃資訊', '切换购物篮资讯', 'スイッチング買い物かご情報', '전환 장바구니 정보', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'toggle_ordering_basket_information');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'input_taiwan_gui_carrier', 'Input Taiwan GUI Carrier', '輸入台灣統一發票載具', '输入台湾统一发票载具', '台湾GUIのキャリアを入力してください', '대만 GUI 캐리어를 입력', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'input_taiwan_gui_carrier'); 

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'negative_check', 'Negative Check', '負數賬單', '负数账单', '負の課金', '부정 결제', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'negative_check');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'print_past_date_check', 'Print Past Date Check', '列印過去日子單據', '列印过去日子单据', '最終日の手形を印刷', '마지막 날 지폐를 인쇄하기', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'print_past_date_check');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'split_item_by_seat', 'Split Item By Seat', '按座位分拆項目', '按座位分拆项目', 'プレススプリット席プロジェクト', '를 눌러 분할 시트 프로젝트', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'split_item_by_seat');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'toggle_taiwan_gui_mode', 'Toggle Taiwan GUI Mode', '切換台灣統一發票模式', '切换台湾统一发票模式', '台湾GUIモードスイッチ', '대만 GUI 모드 스위치', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'toggle_taiwan_gui_mode');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'edit_taiwan_gui_configure', 'Edit Taiwan GUI Configure', '更改台灣統一號碼設定', '更改台湾统一号码设定', '台湾統一番号の設定を変更します', '대만 통일 번호 설정 변경', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'edit_taiwan_gui_configure');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'assign_check_to_target_outlet', 'Assign To Target Outlet', '分配帳單至目標營業區', '分配帐单至目标营业区','対象のビジネス地区に割り当てられました', '목표 결제 사업 지구로 지정', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'assign_check_to_target_outlet');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'assign_check_type', 'Assign Check Type', '設定帳單類型', '设定帐单类型', 'チェック・タイプを割り当てる', '체크 유형 지정', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'assign_check_type');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'transfer_check_to_target_outlet', 'Transfer Check To Target Outlet', '轉移帳單至目標營業區', '转移帐单至目标营业区','対象のビジネス地区に転送請求', '청구 대상 사업 지구로 전송', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'transfer_check_to_target_outlet');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'ogs_get_preorder', 'Get OGS Pre-order', '讀取OGS點菜項目', '读取OGS点菜项目','OGSを読むアラカルトアイテム', 'OGS 라 카르테 항목 읽기', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'ogs_get_preorder');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'add_advance_order', 'Add Advance Order Information', '新增預訂單', '添加预订单', '新規予約', '새로운 예약', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'add_advance_order');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'retrieve_advance_order', 'Retrieve Advance Order', '讀取預訂單', '读取预订单', '予約を読みます', '읽기 예약', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'retrieve_advance_order');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'assign_default_payment', 'Assign Default Payment', '設定預設付款方式', '设定预设付款方式', '設定されたデフォルトのお支払い方法', '설정 기본 결제 수단', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'assign_default_payment');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'restart_portal_station', 'Restart portal station', '重新啟動入口工作站', '重新启动入口工作站', 'ポータルステーションを再起動する', '입구 스테이션을 재시작', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'restart_portal_station');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'change_last_item_qty_by_electronic_scale', 'Change last item quantity by electronic scale', '根據電子磅修改項目數量', '根据电子磅修改项目数量', 'エレクトロニクスのポンドの数に応じてプロジェクトを変更します', '전자의 파운드의 수에 따라 프로젝트를 수정', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'change_last_item_qty_by_electronic_scale');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'loyalty_start_redeem', 'Loyalty Start Redeem', '積分計劃換開始', '积分计划开始兑换', 'ロイヤルティスタートリプレイメント', '충성도 시작 사용', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'loyalty_start_redeem');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'loyalty_check_balance', 'Loyalty Check Balance', '積分計劃檢查餘額', '积分计划检查余额', 'ロイヤリティチェック残高', '충성도 확인 잔액', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'loyalty_check_balance');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'loyalty_cancel_redeem', 'Loyalty Cancel Redeem', '積分計劃取消兌換', '积分计划取消兑换', 'ロイヤリティの取り消しの取り消し', '충성도 취소 쿠폰', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'loyalty_cancel_redeem');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'loyalty_search_member', 'Loyalty Search Member', '積分計劃搜索會員', '积分计划搜索会员', 'ロイヤリティ検索メンバー', '충성도 검색 회원', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'loyalty_search_member');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'loyalty_svc_add_value', 'Loyalty SVC Add Value', '積分計劃SVC增值', '积分计划SVC增值', 'ロイヤリティSVC値を追加', '충성도 SVC 값 추가', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'loyalty_svc_add_value');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'loyalty_svc_issue_card', 'Loyalty SVC Issue Card', '積分計劃SVC發卡', '积分计划SVC发卡', 'ロイヤリティSVC発行カード', '충성도 SVC 이슈 카드', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'loyalty_svc_issue_card');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'loyalty_svc_check_value', 'Loyalty SVC Check Value', '積分計劃SVC檢查值', '积分计划SVC检查值', 'ロイヤルティSVCチェック値', '충성도 SVC 검사 값', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'loyalty_svc_check_value');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'loyalty_svc_suspend_card', 'Loyalty SVC Suspend Card', '積分計劃SVC卡暫停', '积分计划SVC卡暂停', 'ロイヤルティSVCカードを中断する', '충성도 SVC 카드를 정지시키다', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'loyalty_svc_suspend_card');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'loyalty_svc_transfer_card', 'Loyalty SVC Transfer Card', '積分計劃SVC轉換卡', '积分计划SVC转换卡', 'ロイヤルティSVC転送カード', '충성도 SVC 전송 카드', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'loyalty_svc_transfer_card');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'add_deposit', 'Add Deposit', '添加訂金', '添加订金', '預金を追加する', '보증금 추가', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'add_deposit');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'use_deposit', 'Use Deposit', '使用訂金', '使用订金', '預金を使用する', '보증금 사용', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'use_deposit');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'assign_table_attributes', 'Assign Table Attributes', '設定檯屬性', '设定台属性', '設定し、ステージのプロパティ', '설정 단계의 특성', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'assign_table_attributes');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'payment_interface_card_authorization', 'Payment Interface Card Authorization', '付款介面信用卡授權', '付款介面信用卡授权', '支払いインタフェースのクレジットカード認証', '결제 인터페이스 신용 카드 승인', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'payment_interface_card_authorization');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'payment_interface_cancel_card_authorization', 'Payment Interface Cancel Card Authorization', '付款介面取消信用卡授權', '付款介面取消信用卡授权', '支払いインタフェースキャンセルクレジットカード認証', '지불 인터페이스 취소 신용 카드 승인', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'payment_interface_cancel_card_authorization');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'payment_interface_transfer_card_authorization', 'Payment Interface Transfer Card Authorization', '付款介面轉移信用卡授權', '付款介面转移信用卡授权', '支払いインタフェース転送クレジットカード認証', '결제 인터페이스 이전 신용 카드 승인', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'payment_interface_transfer_card_authorization');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'payment_interface_cancel_complete_card_authorization', 'Payment Interface Cancel Complete Card Authorization', '付款介面取消已認證信用卡授權', '付款介面取消已认证信用卡授权', '支払いインタフェースのキャンセル認定されたクレジットカード認証', '지불 인터페이스 취소됨 인증 된 신용 카드 승인', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'payment_interface_cancel_complete_card_authorization');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'edit_check_gratuity', 'Edit Check Gratuity', '編輯賬單賞錢', '编辑账单赏钱', '請求書を編集する', '청구서 수정', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'edit_check_gratuity');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'assign_drawer_ownership', 'Assign Drawer Ownership', '分配收銀員抽屜擁有權', '分配收银员抽屉拥有权', 'キャッシャー引き出し所有権の割り当て', '계산원 서랍 소유권 지정', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'assign_drawer_ownership');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'take_drawer_ownership', 'Take Drawer Ownership', '取得收銀員抽屜擁有權', '取得收银员抽屉拥有权', 'キャッシャーの引き出し所有権を取得する', '계산원 서랍 소유권 가져 오기', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'take_drawer_ownership');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'change_meal_period', 'Change Meal Period', '更換餐飲時段', '更换餐饮时段', '更换餐飲時事段', '식사 시간 바꾸기', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'change_meal_period');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'disable_mix_and_match_rules', 'Disable Mix and Match Rules', '取消配搭規則', '取消配搭规则', '一致するルールをキャンセルする', '일치 규칙 취소', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'disable_mix_and_match_rules');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'edit_set_menu', 'Edit Set Menu', '修改套餐', '修改套餐', 'セットセット編集メニュー', '세트 메뉴 편집', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'edit_set_menu');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'confirm_order_dialog', 'Confirm Order Dialog', '確認點菜', '确认点菜', '注文を確認する', '주문 확인', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'confirm_order_dialog');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'assign_ordering_type', 'Assign Ordering Type', '分配點菜類型', '分配点菜类型', 'アラカルトタイプを割り当てる', '일품 유형 지정', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'assign_ordering_type');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'search_advance_order', 'Search Advance Order', '搜尋預訂單', '搜寻预订单', '先行予約を検索する', '선주문 검색', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'search_advance_order');


INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'scanpay_status_enquiry', 'Payment Status Enquiry For Payment Interface', '查詢付款介面的付款狀態', '查询付款介面的付款状态', '支払インタフェースのための支払ステータス照会', '지급 인터페이스에 대한 지급 상태 조회', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'scanpay_status_enquiry');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'mark_table_status_to_vacant', 'Mark Table Status To Vacant', '更新檯的狀態為空置', '更新台的状态为空置', 'ステータスが空に更新されます', '대만 상태가 비어있는 상태로 업데이트됩니다.', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'mark_table_status_to_vacant');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'wastage_operation', 'Wastage Operation', '報銷操作', '报销操作', '払い戻し作業', '상환 운영', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'wastage_operation');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'search_item_stock', 'Search Item Stock', '搜尋項目存貨', '搜寻项目存货', 'アイテムの在庫を検索する', '상품 재고 검색', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'search_item_stock');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'refresh_table_status_by_table_num', 'Refresh Table Status By Table Number', '刷新特定台號狀態', '刷新特定台号状态', 'テーブル番号でテーブルステータスをリフレッシュする', '테이블 번호별로 테이블 상태 새로 고침', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'refresh_table_status_by_table_num');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'check_listing_by_table_reference', 'Check Listing By Table Reference', '檯參考號查詢單據列表', '台参考号查询单据列表', '台湾の参照番号照会リスト', '대만 참조 번호 쿼리 목록', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'check_listing_by_table_reference');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'override_item_price', 'Override Item Price', '凌駕項目價格', '凌驾项目价格', 'プロジェクト価格を上書きする', '프로젝트 가격 무시', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'override_item_price');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'input_table_reference', 'Input Table Reference', '輸入檯參考號', '输入台参考号', '入力ステーションの参照番号', '입력 스테이션 참조 번호', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'input_table_reference');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'toggle_bar_tab_mode', 'Toggle Bar Mode', '轉換酒吧模式', '转换酒吧模式', 'スイッチバーモード', '스위치 막대 모드', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'toggle_bar_tab_mode');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'set_maximum_charge', 'Set Maximum Charge', '設置最高消費金額', '设置最高消费金额', 'セットの最大消費量', '설정된 최대 소비 금액', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'set_maximum_charge');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'set_minimum_charge', 'Set Minimum Charge', '設置最低消費金額', '设置最低消费金额', '最低料金を設定する', '최소 요금 설정', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'set_minimum_charge');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'payment_interface_card_authorization_and_send_check', 'Payment Interface Card Authorization And Send Check', '付款介面信用卡授權及送單', '付款介面信用卡授权及送单', '支払いインタフェースクレジットカードの承認と配達', '지불 인터페이스 신용 카드 승인 및 전달', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'payment_interface_card_authorization_and_send_check');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'repeat_round_items', 'Repeat Round Items', '重複上回項目', '重复上回项目', '最後のプロジェクトを繰り返します', '마지막 프로젝트 반복', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'repeat_round_items');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'void_payment', 'Void Payment', '取消付款', '取消付款', '無効な支払い', '무효 지급', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'void_payment');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'split_revenue', 'Split Revenue', '分拆營業收入', '分拆营业收入', '営業利益の分割', '분할 영업 이익', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'split_revenue');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'membership_issue_card', 'Membership Issue Card', '會員卡發卡', '会员卡发卡', 'メンバーシップ カード', '회원 카드 카드', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'membership_issue_card');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'patron_inquiry', 'Patron Inquiry', '賭場接口顧客查詢', '赌场接口顾客查询', 'カジノインターフェイスの顧客の問い合わせ', '카지노 인터페이스 고객 문의', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'patron_inquiry');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'comp_inquiry', 'Comp Inquiry', '賭場接口招待查詢', '赌场接口招待查询', 'カジノインターフェイスのエンターテインメントクエリ', '카지노 인터페이스 엔터테인먼트 쿼리', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'comp_inquiry');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'gaming_enquiry', 'Gaming Enquiry', '賭場接口查詢', '赌场接口查询', 'カジノのインターフェイスクエリ', '카지노 인터페이스 쿼리', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'gaming_enquiry');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'duty_meal_on_credit_balance_enquiry', 'Duty Meal / On Credit Balance Enquiry', '值班餐/掛帳餘額查詢', '值班餐/挂帐余额查询', '義務食事/口座残高照会', '면세/계좌 잔액 조회', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'duty_meal_on_credit_balance_enquiry');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'split_item_with_quantity', 'Split Item With Quantity', '以數量分拆項目', '以数量拆分项目', '数量を含むアイテムを分割する', '수량이있는 항목 분할', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'split_item_with_quantity');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'stock_soldout_by_shop', 'Item Soldout by Shop', '項目於單一商店沽清', '项目于单一商店沽清', 'プロジェクトは単一の店舗ではっきりしています', '프로젝트는 단일 저장소에서 명확합니다.', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'stock_soldout_by_shop');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'get_award_list', 'Get Award List', '查詢積分兌換表', '查询积分兑换表', '賞品リストを入手する', '수상 목록 받기', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'get_award_list');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'check_data_update_history', 'Check Data Update History', '檢查數據更新歷史紀錄', '检查数据更新历史纪录', 'データの更新履歴を確認する', '데이터 업데이트 기록 확인', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'check_data_update_history');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'restart_surveillance_shell', 'Restart Surveillance Connection Program', '重新啟動連接監控程式', '重新启动连接监控程式', '接続モニタを再起動します', '연결 모니터를 다시 시작하십시오', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'restart_surveillance_shell');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'stop_surveillance_shell', 'Stop Surveillance Connection Program', '停止啟動連接監控程式', '停止启动连接监控程式', '接続モニターの起動を停止する', '연결 모니터 시작 중지', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'stop_surveillance_shell');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'toggle_print_receipt', 'Toggle Print Receipt', '切換列印收據', '切换打印收据', '切り替え印刷領収書', '설정/해제 인쇄 영수증', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'toggle_print_receipt');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'toggle_cover', 'Toggle Cover', '切換客人數目', '切换客人数目', 'ゲスト数を切り替える', '손님 수를 전환하십시오.', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'toggle_cover');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'own_tips_and_sc_out', 'Owner Tips And Service Charge Out', '修改個人小費及服務費支出', '修改个人小费及服务费支出', '個人的なヒントとサービス料金を変更する', '개인 팁 및 서비스 수수료 수정', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'own_tips_and_sc_out');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'tips_and_sc_out_from_other_employee', 'Tips And Service Charge Out From Other Employee', '修改其他員工小費及服務費支出', '修改其他员工小费及服务费支出', '他の従業員のヒントとサービス料を変更する', '다른 직원 팁 및 서비스 수수료 수정', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'tips_and_sc_out_from_other_employee');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'bonus_codes', 'Attach Bonus Codes', '設定積分代碼', '设定积分代码', 'ボーナスコードを設定する', '보너스 코드 설정', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'bonus_codes');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'membership_affiliation', 'Membership Affiliation', '附屬會員', '附属会员', '会員の所属', '회원 가입', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'membership_affiliation');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'set_closed_check_member', 'Set Closed Check Member', '為已付款帳單設定會員', '为已付款帐单设定会员', '支払明細書の会員資格を設定する', '유료 청구서 회원 가입', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'set_closed_check_member');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'search_membership_member', 'Search Membership Member', '搜索會員', '搜索会员', 'メンバー検索', '회원 검색', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'search_membership_member');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'set_member_discount', 'Set Member Discount', '設置會員折扣', '设置会员折扣', '会員割引を設定します。', '회원 할인 설정', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'set_member_discount');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'toggle_ordering_basket_with_consolidate_items', 'Toggle Ordering Basket With Consolidate Items', '點菜籃切換合併項目功能', '点菜篮切换合并项目功能', 'アラカルトバスケットスイッチマージプロジェクト機能', '단품 바구니 교환 병합 프로젝트 기능', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'toggle_ordering_basket_with_consolidate_items');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'lock_table', 'Lock Table', '鎖定檯號', '锁定台号', 'ロックテーブル', '잠금 테이블', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'lock_table');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'print_pending_item_with_quantity', 'Print Pending Item With Quantity', '選擇列印延後傳送廚房單項目數量', '选择打印延后传送厨房单项目数量', '選択印刷遅延配信キッチン単品数量', '선택 인쇄 지연 배달 주방 단일 항목 수량', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'print_pending_item_with_quantity');

INSERT INTO `pos_functions` (`func_key`, `func_name_l1`, `func_name_l2`, `func_name_l3`, `func_name_l4`, `func_name_l5`, `func_short_name_l1`, `func_short_name_l2`, `func_short_name_l3`, `func_short_name_l4`, `func_short_name_l5`, `func_seq`, `func_ask_password`, `func_status`, `created`, `modified`)
SELECT 'first_page', 'Open First Page', '打開第一頁', '打开第一页', '最初のページを開く', '첫 페이지 열기', '', '', '', '', '', 0, '', '', NOW(), NOW()
FROM dual WHERE NOT EXISTS (SELECT * FROM pos_functions WHERE func_key = 'first_page');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
