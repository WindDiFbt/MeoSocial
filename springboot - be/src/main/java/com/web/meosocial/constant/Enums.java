package com.web.meosocial.constant;

import lombok.Getter;

public class Enums {
    @Getter
    public enum RoleNames {
        ROLE_ADMIN(1),
        ROLE_USER(3),
        ROLE_MODERATOR(2);
        private final Integer value;

        private RoleNames(Integer value) {
            this.value = value;
        }
    }

    @Getter
    public enum UserStatus {
        AVAILABLE(1),
        NOT_AVAILABLE(2),
        RESTRICTIVE(3);
        private final Integer value;

        private UserStatus(Integer value) {
            this.value = value;
        }

    }

    @Getter
    public enum VisibilityLevel {
        PUBLIC(1),
        PRIVATE(2),
        FRIENDS(3),
        FOLLOWER(4);
        private final Integer value;

        private VisibilityLevel(Integer value) {
            this.value = value;
        }
    }
}
