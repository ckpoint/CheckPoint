
# Check-Point
Http request auto validation library for spring framework

 Check-Point depends on [Jackson Json Processor](https://github.com/FasterXML/jackson), [Project Lombok](http://projectlombok.org/),
[Apache POI](https://github.com/apache/poi)

#### Check-Point is a library that automatically checks the integrity of request messages that developers are most likely to miss during the spring project development.
Null check, minimum, maximum value of message.. You do not need to code anymore, just only need to click the checkbox on the setting page.

##### I realized that I was born into a person who can not fully handle message exceptions and developed this library.

#### Are you a diligent person who can handle exceptions for every message?


# Installation

# Usage

## Table of Contents
- [ 1. Setting Api Controller Add ](#add-setting-controller)
- [ 2. Replace Annotation ](#replace-annotation)

## Add Setting Controller
Add the controller to the project as shown below.

- The controller to be added must extends the MsgSettingController.
- You can also use RequestMapping annotation to set a unique address value to receive the configuration api.

```java

@RestController
@RequestMapping("/uniquepath")
public class CheckPointController extends MsgSettingController { }

```

## Replace Annotation

- You can use the ValidationParam annotation to map a Request Parameter to Object.

```java
  @GetMapping("/find")
    public UserModel findUser(@ValidationParam BaseModel findModel) {
        UserModel userModel = new UserModel();
        userModel.setId(findModel.getId());
        return userModel;
    }
```
- You can use the ValidationBody annotation to map a json body to Object.

```java
  @PutMapping("/create")
    public OrderModel createOrder(@ValidationBody OrderModel orderModel) {
        return orderModel;
    }
```
