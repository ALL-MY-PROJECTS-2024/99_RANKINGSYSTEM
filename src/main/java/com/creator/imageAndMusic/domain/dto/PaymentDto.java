package com.creator.imageAndMusic.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    public boolean success;
    public String imp_uid;
    public Long tradingid;
    public String pay_method;
    public String merchant_uid;
    public String name;
    public int paid_amount;
    public String currency;
    public String pg_provider;
    public String pg_type;
    public String pg_tid;
    public String apply_num;
    public String buyer_name;
    public String buyer_email;
    public String buyer_tel;
    public String buyer_addr;
    public String buyer_postcode;
    public Object custom_data;
    public String status;
    public int paid_at;
    public String receipt_url;
    public Object card_name;
    public Object bank_name;
    public int card_quota;
    public String card_number;
}
