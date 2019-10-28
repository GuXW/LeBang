package com.ruoyi.project.content;

public enum FileTypeEnum {

    ROTATION(0,"轮播图","/rotation"),
    QRCODE(1,"二维码","/qrCode"),
    IDENTITY(2,"身份证","/identity"),
    RECEIVABLES(3,"收款码","/receivables"),
    VOUCHER(4,"凭证","/voucher"),
    MAKE_MONEY_CHART(5,"收款凭证","/make_money_chart")
    ;

    private Integer value;
    private String text;
    private String path;

    FileTypeEnum(Integer value, String text, String path) {
        this.value = value;
        this.text = text;
        this.path = path;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static FileTypeEnum findByValue(Integer value){
        if (null==value){
            return null;
        }
        for (FileTypeEnum e :FileTypeEnum.values()){
            if (value.intValue()==e.getValue().intValue()){
                return e;
            }
        }
        return null;
    }

}
