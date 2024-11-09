package com.macaron.homeschool.interceptor;

import com.macaron.homeschool.common.enums.UserType;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserHelper {

    Long userId;

    UserType role;
}
