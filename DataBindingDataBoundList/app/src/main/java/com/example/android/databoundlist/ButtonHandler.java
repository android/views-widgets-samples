/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.databoundlist;

import android.databinding.ObservableInt;

import java.util.List;

/**
 * This class handles the button presses in activity_main
 */
public class ButtonHandler {
    private static final String[] FIRST_NAMES = {
            "Avery",
            "Patsy",
            "Andie",
            "Devin",
            "Hayden",
            "Stacey",
            "Sage",
            "Martie",
            "Hadley",
            "Arden",
            "Johnnie",
            "Phoenix",
            "Cheyenne",
            "Jody",
            "Morgan",
            "Parker",
    };
    private static final String[] LAST_NAMES = {
            "Nowell",
            "Matthews",
            "Bryan",
            "Lindsay",
            "Derrickson",
            "Weaver",
            "Elder",
            "Hilton",
            "Southgate",
            "Brewster",
            "Walsh",
            "Darrell",
            "Baxter",
            "Hicks",
            "Peterson",
            "Bonner",
    };

    private final List<User> users;
    public final ObservableInt layout = new ObservableInt(R.layout.item_small);

    public ButtonHandler(List<User> users) {
        this.users = users;
    }

    /**
     * Change to the layout with large font.
     */
    public void useLarge() {
        layout.set(R.layout.item_large);
    }

    /**
     * Change to the layout with small font.
     */
    public void useSmall() {
        layout.set(R.layout.item_small);
    }

    /**
     * Add a user to the start of the list.
     */
    public void addToStart() {
        users.add(0, randomUser());
    }

    /**
     * Add a user to the end of the list.
     */
    public void addToEnd() {
        users.add(randomUser());
    }

    /**
     * Delete a user from the start of the list.
     */
    public void deleteFromStart() {
        if (!users.isEmpty()) {
            users.remove(0);
        }
    }

    /**
     * Delete a user from the end of the list.
     */
    public void deleteFromEnd() {
        if (!users.isEmpty()) {
            users.remove(users.size() - 1);
        }
    }

    /**
     * @return A User with a random first and last name chosen from FIRST_NAMES and LAST_NAMES.
     */
    private static User randomUser() {
        String firstName = FIRST_NAMES[(int)(Math.random() * FIRST_NAMES.length)];
        String lastName = LAST_NAMES[(int)(Math.random() * LAST_NAMES.length)];
        return new User(firstName, lastName);
    }
}
