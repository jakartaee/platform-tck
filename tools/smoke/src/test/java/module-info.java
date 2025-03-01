module jakarta.smoke.test {
    requires jakarta.cdi;
    requires org.junit.jupiter.api;

    exports jpms to  org.junit.platform.commons;
}