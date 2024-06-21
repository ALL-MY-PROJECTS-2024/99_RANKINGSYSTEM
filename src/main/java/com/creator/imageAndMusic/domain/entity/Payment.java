package com.creator.imageAndMusic.domain.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Payment {
    public boolean success;
    @Id
    public String imp_uid;
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
    public String custom_data;
    public String status;
    public int paid_at;
    public String receipt_url;
    public String card_name;
    public String bank_name;
    public int card_quota;
    public String card_number;
}
