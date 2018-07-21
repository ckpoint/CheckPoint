package hsim.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * The type Common req model.
 */
@Getter
@Setter
@ToString
public class CommonReqModel {
    private long id;

    private String loginId;
    private String email;
    private String domain;
    private String name;
    private Child parent1;

    private String param1;
    private String param2;
    private double size;
    private List<Child> childs;

}

