package hsim.checkpoint.model.order;

import hsim.checkpoint.model.BaseModel;
import hsim.checkpoint.model.user.UserModel;
import lombok.Data;

import java.util.List;

/**
 * The type Common req hsim.checkpoint.model.
 */

@Data
public class OrderModel extends BaseModel {

    private UserModel user;
    private List<OrderItemModel> orderItems;
    private Long totalPrice;
}

