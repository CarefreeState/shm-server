package com.macaron.homeschool.common.context;

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
