package hsim.checkpoint.model.product;

import hsim.checkpoint.model.BaseModel;
import lombok.Data;

@Data
public class ProductModel extends BaseModel {

    private String name;
    private ProductType type;
    private String description;
    private String photoUrl;
    private Long basePrice;
    private Double discountPercent;
}
