package com.ruoyi.project.content;

public enum TaskStatusEnum {
//（1待付款，2已付款(待确认）,3已确认）
    PENDING_PAYMENT(1,"待付款"),
    NO_CONFIRM(2,"待确认"),
    CONFIRM(3,"已确认");

    private Integer value;
    private String text;

    TaskStatusEnum(Integer value, String text) {
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

    public static TaskStatusEnum findByValue(Integer value){
        if (null==value){
            return null;
        }
        for (TaskStatusEnum e : TaskStatusEnum.values()){
            if (value.intValue()==e.getValue().intValue()){
                return e;
            }
        }
        return null;
    }

}
