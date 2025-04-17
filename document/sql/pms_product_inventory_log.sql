-- 商品库存变动日志表
CREATE TABLE `pms_product_inventory_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL COMMENT '商品ID',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'SKU ID',
  `before_stock` int(11) DEFAULT NULL COMMENT '库存变动前数量',
  `after_stock` int(11) DEFAULT NULL COMMENT '库存变动后数量',
  `change_stock` int(11) DEFAULT NULL COMMENT '变动数量',
  `operate_type` int(1) DEFAULT NULL COMMENT '操作类型：1->入库；2->出库；3->调整',
  `operate_man` varchar(64) DEFAULT NULL COMMENT '操作人',
  `note` varchar(500) DEFAULT NULL COMMENT '操作备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='商品库存变动日志表';