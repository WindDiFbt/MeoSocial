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

    @Getter
    public enum NotificationType {
        SYSTEM_NOTI(0),
        COMMENT_ON_POST(1),
        LIKE_POST(2),
        LIKE_COMMENT(3),
        REPLY_COMMENT(4),
        FOLLOW_REQUEST(5),
        FOLLOW_REQUEST_ACCEPTED(6),
        NEW_FOLLOWER(7),
        SECURITY_ALERT(8),
        NEW_FEATURE(9),
        NEW_FOLLOW_SUGGESTION(10),
        SHARE_POST(11);
        private final Integer value;

        private NotificationType(Integer value) {
            this.value = value;
        }

        public static NotificationType fromValue(int value) {
            for (NotificationType type : NotificationType.values()) {
                if (type.getValue() == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unexpected value: " + value);
        }
    }

    @Getter
    public enum ReferenceType {
        SYSTEM_TO_USER(0),
        USER_TO_USER(1);
        private final Integer value;

        private ReferenceType(Integer value) {
            this.value = value;
        }

        public static ReferenceType fromValue(int value) {
            for (ReferenceType type : ReferenceType.values()) {
                if (type.getValue() == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unexpected value: " + value);
        }
    }
}
