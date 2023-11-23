package dev.uptc.loginfx.model;

import javafx.scene.control.ListCell;

public class PersonListCell extends ListCell<Person> {

        @Override
        protected void updateItem(Person item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                setText("ID: " + item.getId() + ", Name: " + item.getFullName() + ", Country: " + item.getCountry());
            }
        }
    }
