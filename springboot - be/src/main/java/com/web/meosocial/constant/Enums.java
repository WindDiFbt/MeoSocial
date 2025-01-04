package com.web.meosocial.constant;

public class Enums {
    public enum RoleNames {
        ROLE_ADMIN(1),
        ROLE_USER(3),
        ROLE_MODERATOR(2);
        private Integer value;

        private RoleNames(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    public enum UserStatus {
        AVAILABLE(1),
        NOT_AVAILABLE(2),
        RESTRICTIVE(3);
        private Integer value;

        private UserStatus(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }
}
