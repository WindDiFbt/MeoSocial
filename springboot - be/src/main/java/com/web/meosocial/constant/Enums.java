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
        FOLLOWER(2),
        FRIENDS(3),
        PRIVATE(4);
        private final Integer value;

        private VisibilityLevel(Integer value) {
            this.value = value;
        }

        public static VisibilityLevel fromValue(int value) {
            for (VisibilityLevel level : VisibilityLevel.values()) {
                if (level.getValue() == value) {
                    return level;
                }
            }
            throw new IllegalArgumentException("Unexpected value: " + value);
        }
    }

    @Getter
    public enum MediaType {
        IMAGE(1),
        VIDEO(2);
        private final Integer value;

        private MediaType(Integer value) {
            this.value = value;
        }
    }

    public enum FolderCloudinary {
        Avatar, PostMedia, CommentMedia
    }

    @Getter
    public enum RelationshipStatus {
        UNFOLLOW(0),
        FOLLOW(1),
        BLOCKED(2);
        private final Integer value;

        private RelationshipStatus(Integer value) {
            this.value = value;
        }

        public static RelationshipStatus fromValue(int value) {
            for (RelationshipStatus status : RelationshipStatus.values()) {
                if (status.getValue() == value) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unexpected value: " + value);
        }
    }
}
