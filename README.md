
# Check-Point
Http request auto validation library for spring framework

 Check-Point depends on [Jackson Json Processor](https://github.com/FasterXML/jackson), [Project Lombok](http://projectlombok.org/),
[Apache POI](https://github.com/apache/poi)

#### Check-Point is a library that automatically checks the integrity of request messages that developers are most likely to miss during the spring project development.
Null check, minimum, maximum value of message.. You do not need to code anymore, just only need to click the checkbox on the setting page.

##### I realized that I was born into a person who can not fully handle message exceptions and developed this library.

#### Are you a diligent person who can handle exceptions for every message?


# Installation

TODO

# Usage

## Table of Contents
- [ 1. Add Properties ](#add-properteis)
- [ 2. Setting Api Controller Add ](#add-setting-controller)
- [ 3. Add Annotation ](#add-annotation)
- [ 3. Add Custom Validation Rule](#add-custom-validation-rule)

## Add Properties

- {ckpoint.save.url:true} : for controller with UrlMapping annotation, save when new url recived.
- {ckpoint.max.deeplevel:5} : scan object default deep level
- {ckpoint.body.logging:true} : Whether the body of the request message is logged
- {ckpoint.password:taeon} : When connecting from the setting client, the authentication key value
- {ckpoint.path.repository:/checkpoint/validation.json} : The location of the json file where validation information will be stored

## Add Setting Controller
Add the controller to the project as shown below.

- The controller to be added must extends the MsgSettingController.
- You can also use RequestMapping annotation to set a unique address value to receive the configuration api.

```java

@RestController
@RequestMapping("/uniquepath")
public class CheckPointController extends MsgSettingController { }

```

## Add Annotation

#### @ValidationParam
- You can use the ValidationParam annotation to map a Request Parameter to Object.

```java
  @GetMapping("/find")
    public UserModel findUser(@ValidationParam BaseModel findModel) {
        UserModel userModel = new UserModel();
        userModel.setId(findModel.getId());
        ...
        return userModel;
    }
```
#### @ValidationBody
- You can use the ValidationBody annotation to map a json body to Object.

```java
  @PutMapping("/create")
    public OrderModel createOrder(@ValidationBody OrderModel orderModel) {
        ...
        return orderModel;
    }
```
#### @ValidationUrlMapping
- If the url and method can not be mapped to 1: 1, you can add an UrlMapping annotation.
- If the {{ckpoint.save.url}} property is true, it generates configuration data that maps to the request when a new url request is received.
- Setting the {{ckpoint.max.deeplevel}} property allows you to specify the depth of the object to be scanned at save time, and default value is 5
```java

    @RequestMapping("/proxy/**")
    @ValidationUrlMapping
    public void proxy(@ValidationParam ProxyModel proxymodel) {
        ...
    }
```

## Add Custom Validation Rule
- In addition to the basic rules, you can easily add a rule that does what you want.
- Define a class that  implements BaseValidaitonCheck as shown below.
```java

import hsim.checkpoint.core.component.validationRule.type.BaseValidationCheck;
import hsim.checkpoint.core.domain.ValidationData;
import hsim.checkpoint.exception.ValidationLibException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class EmptyValueCheck implements BaseValidationCheck {

    @Override
    public boolean check(Object receiveValue, Object standardValue) {

        if (receiveValue instanceof String) {
            return !((String) receiveValue).isEmpty();
        }
        return false;
    }

    @Override
    public Object replace(Object receiveValue, Object standardValue, ValidationData param) {

        if (receiveValue instanceof String && ((String) receiveValue).isEmpty()) {
            return standardValue;
        }
        return receiveValue;
    }

    @Override
    public void exception(ValidationData param, Object inputValue, Object standardValue) {
        throw new ValidationLibException("custom rule check error", HttpStatus.BAD_REQUEST);
    }

}
```

- Then add the rule using CheckPointHelper as below and call flush at the end.

```java
   CheckPointHelper checkPointHelper = new CheckPointHelper();
   checkPointHelper.addValidationRule("emptyValueCheck", StandardValueType.STRING, new EmptyValueCheck(), new AssistType().string());
   checkPointHelper.flush();
```
