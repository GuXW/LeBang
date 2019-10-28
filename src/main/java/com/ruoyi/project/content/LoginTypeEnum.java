package com.ruoyi.project.content;

public enum LoginTypeEnum {

    PC(0,"web端登录"),
    APP(1,"小程序端登录"),
    WEB_APP(2,"webapp登录");

    private Integer value;
    private String text;

    LoginTypeEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public static LoginTypeEnum findByValue(Integer value){
        if (null==value){
            return null;
        }
        for (LoginTypeEnum e : LoginTypeEnum.values()){
            if (value.intValue()==e.getValue().intValue()){
                return e;
            }
        }
        return null;
    }

}
