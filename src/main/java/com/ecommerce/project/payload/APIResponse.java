package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.convert.DataSizeUnit;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse {
    public String message;
    public String status;
}
