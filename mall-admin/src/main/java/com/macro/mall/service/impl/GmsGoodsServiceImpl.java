package com.macro.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.macro.mall.dto.GmsGoodsParam;
import com.macro.mall.dto.GmsGoodsQueryParam;
import com.macro.mall.mapper.GmsGoodsMapper;
import com.macro.mall.model.GmsGoods;
import com.macro.mall.model.GmsGoodsExample;
import com.macro.mall.service.GmsGoodsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 商品管理Service实现类
 */
@Service
public class GmsGoodsServiceImpl implements GmsGoodsService {
    @Autowired
    private GmsGoodsMapper goodsMapper;

    @Override
    public int create(GmsGoodsParam goodsParam) {
        GmsGoods goods = new GmsGoods();
        BeanUtils.copyProperties(goodsParam, goods);
        goods.setDeleteStatus(0);
        goods.setVerifyStatus(0);
        goods.setSale(0);
        goods.setCreateTime(new Date());
        goods.setUpdateTime(new Date());
        return goodsMapper.insertSelective(goods);
    }

    @Override
    public GmsGoods getItem(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(Long id, GmsGoodsParam goodsParam) {
        GmsGoods goods = new GmsGoods();
        BeanUtils.copyProperties(goodsParam, goods);
        goods.setId(id);
        goods.setUpdateTime(new Date());
        return goodsMapper.updateByPrimaryKeySelective(goods);
    }

    @Override
    public int delete(Long id) {
        return goodsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int delete(List<Long> ids) {
        GmsGoodsExample example = new GmsGoodsExample();
        example.createCriteria().andIdIn(ids);
        return goodsMapper.deleteByExample(example);
    }

    @Override
    public List<GmsGoods> list(GmsGoodsQueryParam queryParam, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        GmsGoodsExample example = new GmsGoodsExample();
        example.setOrderByClause("sort desc, id desc");
        GmsGoodsExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteStatusEqualTo(0);
        if (queryParam.getPublishStatus() != null) {
            criteria.andPublishStatusEqualTo(queryParam.getPublishStatus());
        }
        if (queryParam.getVerifyStatus() != null) {
            criteria.andVerifyStatusEqualTo(queryParam.getVerifyStatus());
        }
        if (!StrUtil.isEmpty(queryParam.getKeyword())) {
            criteria.andNameLike("%" + queryParam.getKeyword() + "%");
        }
        if (!StrUtil.isEmpty(queryParam.getGoodsSn())) {
            criteria.andGoodsSnEqualTo(queryParam.getGoodsSn());
        }
        if (queryParam.getCategoryId() != null) {
            criteria.andCategoryIdEqualTo(queryParam.getCategoryId());
        }
        if (queryParam.getBrandId() != null) {
            criteria.andBrandIdEqualTo(queryParam.getBrandId());
        }
        return goodsMapper.selectByExample(example);
    }

    @Override
    public List<GmsGoods> list(String keyword) {
        GmsGoodsExample example = new GmsGoodsExample();
        GmsGoodsExample.Criteria criteria = example.createCriteria();
        criteria.andDeleteStatusEqualTo(0);
        if (!StrUtil.isEmpty(keyword)) {
            criteria.andNameLike("%" + keyword + "%");
            example.or(example.createCriteria().andGoodsSnLike("%" + keyword + "%"));
        }
        return goodsMapper.selectByExample(example);
    }

    @Override
    public int updatePublishStatus(List<Long> ids, Integer publishStatus) {
        GmsGoods goods = new GmsGoods();
        goods.setPublishStatus(publishStatus);
        goods.setUpdateTime(new Date());
        GmsGoodsExample example = new GmsGoodsExample();
        example.createCriteria().andIdIn(ids);
        return goodsMapper.updateByExampleSelective(goods, example);
    }

    @Override
    public int updateNewStatus(List<Long> ids, Integer newStatus) {
        GmsGoods goods = new GmsGoods();
        goods.setNewStatus(newStatus);
        goods.setUpdateTime(new Date());
        GmsGoodsExample example = new GmsGoodsExample();
        example.createCriteria().andIdIn(ids);
        return goodsMapper.updateByExampleSelective(goods, example);
    }

    @Override
    public int updateRecommendStatus(List<Long> ids, Integer recommendStatus) {
        GmsGoods goods = new GmsGoods();
        goods.setRecommendStatus(recommendStatus);
        goods.setUpdateTime(new Date());
        GmsGoodsExample example = new GmsGoodsExample();
        example.createCriteria().andIdIn(ids);
        return goodsMapper.updateByExampleSelective(goods, example);
    }

    @Override
    public int updateVerifyStatus(List<Long> ids, Integer verifyStatus) {
        GmsGoods goods = new GmsGoods();
        goods.setVerifyStatus(verifyStatus);
        goods.setUpdateTime(new Date());
        GmsGoodsExample example = new GmsGoodsExample();
        example.createCriteria().andIdIn(ids);
        return goodsMapper.updateByExampleSelective(goods, example);
    }

    @Override
    public int updateDeleteStatus(List<Long> ids, Integer deleteStatus) {
        GmsGoods goods = new GmsGoods();
        goods.setDeleteStatus(deleteStatus);
        goods.setUpdateTime(new Date());
        GmsGoodsExample example = new GmsGoodsExample();
        example.createCriteria().andIdIn(ids);
        return goodsMapper.updateByExampleSelective(goods, example);
    }
}
