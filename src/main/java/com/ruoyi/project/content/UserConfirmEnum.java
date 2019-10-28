package com.ruoyi.project.content;

public enum UserConfirmEnum {

    no_submit(0,"未上传"),
    no_check(1,"待审核"),
    check_success(2,"审核通过"),
    check_error(3,"审核失败"),
    ;

    private Integer value;
    private String text;

    UserConfirmEnum(Integer value, String text) {
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

    public static UserConfirmEnum findByValue(Integer value){
        if (null==value){
            return null;
        }
        for (UserConfirmEnum e : UserConfirmEnum.values()){
            if (value.intValue()==e.getValue().intValue()){
                return e;
            }
        }
        return null;
    }

}
