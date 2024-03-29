package com.github.nbsllc;

import org.json.JSONObject;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DottedJSONTest {
    @Test
    public void testBooleanDataType() {
        List<String> paths = Collections.singletonList(
            "married:Boolean(true)"
        );

        DottedJSON dottedJSON = new DottedJSON(paths);
        assertThat(dottedJSON.toJSONObject().getBoolean("married")).isTrue();
    }

    @Test
    public void testFloatDataType() {
        List<String> paths = Collections.singletonList(
            "height:Float(5.667)"
        );

        DottedJSON dottedJSON = new DottedJSON(paths);
        assertThat(dottedJSON.toJSONObject().getFloat("height")).isEqualTo(5.667f);
    }

    @Test
    public void testImproperValueDataType() {
        List<String> paths = Collections.singletonList(
            "height:Integer(5.667)"
        );

        DottedJSON dottedJSON = new DottedJSON(paths);
        assertThatThrownBy(dottedJSON::toJSONObject)
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Unable to parse data type: Integer(5.667).");
    }

    @Test
    public void testIntegerDataType() {
        List<String> paths = Collections.singletonList(
            "age:Integer(42)"
        );

        DottedJSON dottedJSON = new DottedJSON(paths);
        assertThat(dottedJSON.toJSONObject().getInt("age")).isEqualTo(42);
    }

    @Test
    public void testInvalidDataType() {
        List<String> paths = Collections.singletonList(
            "height:Invalid(5.667)"
        );

        DottedJSON dottedJSON = new DottedJSON(paths);
        assertThatThrownBy(dottedJSON::toJSONObject)
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Unable to parse data type: Invalid(5.667).");
    }

    @Test
    public void testTestToString() {
        List<String> paths = Arrays.asList(
            "person.firstName:Sally",
            "person.lastName:Smith",
            "person.age:Integer(42)",
            "person.address.street1:1234 Test Ave",
            "person.address.street2:Appt 123",
            "person.address.city:San Diego",
            "person.address.state:CA",
            "comments:This is extra info :} Hurray!"
        );

        String expected = ""
            + "{\n"
            + "  \"comments\": \"This is extra info :} Hurray!\",\n"
            + "  \"person\": {\n"
            + "    \"firstName\": \"Sally\",\n"
            + "    \"lastName\": \"Smith\",\n"
            + "    \"address\": {\n"
            + "      \"city\": \"San Diego\",\n"
            + "      \"street1\": \"1234 Test Ave\",\n"
            + "      \"street2\": \"Appt 123\",\n"
            + "      \"state\": \"CA\"\n"
            + "    },\n"
            + "    \"age\": 42\n"
            + "  }\n"
            + "}";

        DottedJSON dottedJSON = new DottedJSON(paths);
        assertThat(dottedJSON.toString()).isEqualTo(new JSONObject(expected).toString());
    }

    @Test
    public void testThatValuesCanContainPeriods() {
        List<String> paths = Collections.singletonList(
            "extra.data.comment:This sentence has two periods. Yep two."
        );

        String expected = "{\"extra\":{\"data\":{\"comment\":\"This sentence has two periods. Yep two.\"}}}";

        DottedJSON dottedJSON = new DottedJSON(paths);
        assertThat(dottedJSON.toJSONObject())
            .usingRecursiveComparison()
            .isEqualTo(new JSONObject(expected));
    }

    @Test
    public void testToJSONObject() {
        List<String> paths = Arrays.asList(
            "person.firstName:Sally",
            "person.lastName:Smith",
            "person.age:Integer(42)",
            "person.address.street1:1234 Test Ave",
            "person.address.street2:Appt 123",
            "person.address.city:San Diego",
            "person.address.state:CA",
            "comments:This is extra info :} Hurray!"
        );

        String expected = ""
            + "{\n"
            + "  \"comments\": \"This is extra info :} Hurray!\",\n"
            + "  \"person\": {\n"
            + "    \"firstName\": \"Sally\",\n"
            + "    \"lastName\": \"Smith\",\n"
            + "    \"address\": {\n"
            + "      \"city\": \"San Diego\",\n"
            + "      \"street1\": \"1234 Test Ave\",\n"
            + "      \"street2\": \"Appt 123\",\n"
            + "      \"state\": \"CA\"\n"
            + "    },\n"
            + "    \"age\": 42\n"
            + "  }\n"
            + "}";

        DottedJSON dottedJSON = new DottedJSON(paths);
        assertThat(dottedJSON.toJSONObject())
            .usingRecursiveComparison()
            .isEqualTo(new JSONObject(expected));
    }
}