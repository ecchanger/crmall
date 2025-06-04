CREATE TABLE `product_management` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品管理ID',
  `product_id` bigint(20) NOT NULL COMMENT '关联的商品ID',
  `management_status` int(1) NOT NULL DEFAULT '0' COMMENT '管理状态：0->待处理；1->处理中；2->已完成',
  `priority` int(1) NOT NULL DEFAULT '2' COMMENT '优先级：1->低；2->中；3->高；4->紧急',
  `assignee` varchar(100) DEFAULT NULL COMMENT '负责人',
  `expected_completion_time` datetime DEFAULT NULL COMMENT '预计完成时间',
  `actual_completion_time` datetime DEFAULT NULL COMMENT '实际完成时间',
  `management_notes` text COMMENT '管理备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_management_status` (`management_status`),
  KEY `idx_priority` (`priority`),
  KEY `idx_assignee` (`assignee`),
  KEY `idx_expected_completion_time` (`expected_completion_time`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_product_management_product_id` FOREIGN KEY (`product_id`) REFERENCES `pms_product` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商品管理表';

INSERT INTO `product_management` (`product_id`, `management_status`, `priority`, `assignee`, `expected_completion_time`, `management_notes`) VALUES
(1, 0, 3, 'admin', '2025-06-10 18:00:00', '需要更新商品描述和图片'),
(2, 1, 2, 'manager', '2025-06-08 12:00:00', '正在处理库存调整'),
(3, 2, 1, 'admin', '2025-06-05 15:00:00', '已完成价格调整'),
(4, 0, 4, 'manager', '2025-06-06 09:00:00', '紧急处理商品下架问题'),
(5, 1, 3, 'admin', '2025-06-09 14:00:00', '处理商品属性更新');
