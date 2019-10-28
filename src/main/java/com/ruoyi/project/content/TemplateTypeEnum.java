package com.ruoyi.project.content;

public enum TemplateTypeEnum {

    msg_1(1,"消息模板1");

    private Integer value;
    private String text;

    TemplateTypeEnum(Integer value, String text) {
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

    public static TemplateTypeEnum findByValue(Integer value){
        if (null==value){
            return null;
        }
        for (TemplateTypeEnum e : TemplateTypeEnum.values()){
            if (value.intValue()==e.getValue().intValue()){
                return e;
            }
        }
        return null;
    }

}
