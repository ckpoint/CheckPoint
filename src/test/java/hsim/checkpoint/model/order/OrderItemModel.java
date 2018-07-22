package hsim.checkpoint.model.order;

import hsim.checkpoint.model.BaseModel;
import hsim.checkpoint.model.product.ProductModel;
import lombok.Data;

@Data
public class OrderItemModel extends BaseModel {

    private ProductModel product;
    private Integer orderUnit;

}
