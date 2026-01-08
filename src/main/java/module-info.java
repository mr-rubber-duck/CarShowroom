module showroom {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires static lombok;
    requires java.naming;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires org.slf4j;

    opens showroom to javafx.graphics, javafx.fxml;
    opens showroom.model to org.hibernate.orm.core, javafx.base;
    opens showroom.controller to javafx.fxml;

    exports showroom;
    exports showroom.model;
    exports showroom.controller;
    exports showroom.util;
}
