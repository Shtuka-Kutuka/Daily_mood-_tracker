package com.example.dailymoodtracker.cache;

import java.util.Objects;

public class MoodEntryQueryKey {

    private final Long userId;
    private final String moodName;
    private final int page;
    private final int size;

    public MoodEntryQueryKey(Long userId, String moodName, int page, int size) {
        this.userId = userId;
        this.moodName = moodName;
        this.page = page;
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MoodEntryQueryKey)) {
            return false;
        }
        MoodEntryQueryKey that = (MoodEntryQueryKey) o;
        return page == that.page &&
            size == that.size &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(moodName, that.moodName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, moodName, page, size);
    }
}