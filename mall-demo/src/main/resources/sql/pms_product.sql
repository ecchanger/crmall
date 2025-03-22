CREATE TABLE `pms_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL COMMENT '商品名称',
  `description` text COMMENT '商品描述',
  `price` decimal(10,2) NOT NULL COMMENT '商品价格',
  `stock` int(11) NOT NULL COMMENT '库存',
  `category_id` bigint(20) DEFAULT NULL COMMENT '商品分类ID',
  `pic` varchar(255) DEFAULT NULL COMMENT '商品主图',
  `publish_status` int(1) DEFAULT '0' COMMENT '上架状态：0->下架；1->上架',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='商品信息表';
