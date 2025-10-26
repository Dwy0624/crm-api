package com.crm.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crm.common.result.PageResult;
import com.crm.entity.Product;
import com.crm.query.ProductQuery;
import com.crm.vo.ProductVO;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author crm
 * @since 2025-10-12
 */
public interface ProductService extends IService<Product> {
    PageResult<Product> getPage(ProductQuery query);
    //商品新增或修改
    void saveOrEdit(Product  product);

    //商品状态
    void batchUpdateProductState();
}