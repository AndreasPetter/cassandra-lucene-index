/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.cassandra.lucene.schema.mapping;

import com.stratio.cassandra.lucene.IndexException;
import com.stratio.cassandra.lucene.schema.mapping.builder.BigIntegerMapperBuilder;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DocValuesType;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;

public class BigIntegerMapperTest extends AbstractMapperTest {

    @Test
    public void testConstructorWithoutArgs() {
        BigIntegerMapper mapper = new BigIntegerMapperBuilder().build("field");
        assertEquals("Field is not properly set", "field", mapper.field);
        assertEquals("Indexed is not set to default value", Mapper.DEFAULT_INDEXED, mapper.indexed);
        assertEquals("Sorted is not set to default value", Mapper.DEFAULT_SORTED, mapper.sorted);
        assertEquals("Column is not set to default value", "field", mapper.column);
        assertEquals("Mapped columns are not properly set", 1, mapper.mappedColumns.size());
        assertTrue("Mapped columns are not properly set", mapper.mappedColumns.contains("field"));
        assertEquals("Digits is not set to default value", BigIntegerMapper.DEFAULT_DIGITS, mapper.digits);
    }

    @Test
    public void testConstructorWithAllArgs() {
        BigIntegerMapper mapper = new BigIntegerMapperBuilder().indexed(false)
                                                               .sorted(true)
                                                               .column("column")
                                                               .digits(6)
                                                               .build("field");
        assertEquals("Field is not properly set", "field", mapper.field);
        assertFalse("Indexed is not properly set", mapper.indexed);
        assertTrue("Sorted is not properly set", mapper.sorted);
        assertEquals("Column is not properly set", "column", mapper.column);
        assertEquals("Digits is not properly set", 6, mapper.digits);
    }

    @Test
    public void testJsonSerialization() {
        BigIntegerMapperBuilder builder = new BigIntegerMapperBuilder().indexed(false)
                                                                       .sorted(true)
                                                                       .column("column")
                                                                       .digits(6);
        testJson(builder, "{type:\"bigint\",indexed:false,sorted:true,column:\"column\",digits:6}");
    }

    @Test
    public void testJsonSerializationDefaults() {
        BigIntegerMapperBuilder builder = new BigIntegerMapperBuilder();
        testJson(builder, "{type:\"bigint\"}");
    }

    @Test()
    public void testValueNull() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 10);
        String parsed = mapper.base("test", null);
        assertNull("Base value is not properly parsed", parsed);
    }

    @Test(expected = IndexException.class)
    public void testValueDigitsZero() {
        new BigIntegerMapper("field", null, null, null, 0);
    }

    @Test(expected = IndexException.class)
    public void testValueDigitsNegative() {
        new BigIntegerMapper("field", null, null, null, -1);
    }

    @Test(expected = IndexException.class)
    public void testValueBooleanTrue() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 10);
        mapper.base("test", true);
    }

    @Test(expected = IndexException.class)
    public void testValueBooleanFalse() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 10);
        mapper.base("test", false);
    }

    @Test(expected = IndexException.class)
    public void testValueUUID() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 100);
        mapper.base("test", UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
    }

    @Test(expected = IndexException.class)
    public void testValueDate() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 100);
        mapper.base("test", new Date());
    }

    @Test(expected = IndexException.class)
    public void testValueStringInvalid() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 10);
        mapper.base("test", "0s0");
    }

    @Test
    public void testValueStringMinPositive() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        String parsed = mapper.base("test", "1");
        assertEquals("Base value is not properly parsed", "01njchs", parsed);
    }

    @Test
    public void testValueStringMaxPositive() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        String parsed = mapper.base("test", "99999999");
        assertEquals("Base value is not properly parsed", "03b2ozi", parsed);
    }

    @Test
    public void testValueStringMinNegative() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        String parsed = mapper.base("test", "-1");
        assertEquals("Base value is not properly parsed", "01njchq", parsed);
    }

    @Test
    public void testValueStringMaxNegative() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        String parsed = mapper.base("test", "-99999999");
        assertEquals("Base value is not properly parsed", "0000000", parsed);
    }

    @Test
    public void testValueStringZero() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        String parsed = mapper.base("test", "0");
        assertEquals("Base value is not properly parsed", "01njchr", parsed);
    }

    @Test
    public void testValueStringLeadingZeros() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        String parsed = mapper.base("test", "000042");
        assertEquals("Base value is not properly parsed", "01njcix", parsed);
    }

    @Test
    public void testValueIntegerMinPositive() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        String parsed = mapper.base("test", 1);
        assertEquals("Base value is not properly parsed", "01njchs", parsed);
    }

    @Test
    public void testValueIntegerMaxPositive() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        String parsed = mapper.base("test", 99999999);
        assertEquals("Base value is not properly parsed", "03b2ozi", parsed);
    }

    @Test
    public void testValueIntegerMinNegative() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        String parsed = mapper.base("test", -1);
        assertEquals("Base value is not properly parsed", "01njchq", parsed);
    }

    @Test
    public void testValueIntegerMaxNegative() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        String parsed = mapper.base("test", -99999999);
        assertEquals("Base value is not properly parsed", "0000000", parsed);
    }

    @Test
    public void testValueIntegerZero() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        String parsed = mapper.base("test", 0);
        assertEquals("Base value is not properly parsed", "01njchr", parsed);
    }

    @Test
    public void testValueLongMinPositive() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 10);
        String parsed = mapper.base("test", 1L);
        assertEquals("Base value is not properly parsed", "04ldqpds", parsed);
    }

    @Test
    public void testValueLongMaxPositive() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 10);
        String parsed = mapper.base("test", 9999999999L);
        assertEquals("Base value is not properly parsed", "096rheri", parsed);
    }

    @Test
    public void testValueLongMinNegative() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 10);
        String parsed = mapper.base("test", -1L);
        assertEquals("Base value is not properly parsed", "04ldqpdq", parsed);
    }

    @Test
    public void testValueLongMaxNegative() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 10);
        String parsed = mapper.base("test", -9999999999L);
        assertEquals("Base value is not properly parsed", "00000000", parsed);
    }

    @Test
    public void testValueLongZero() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 10);
        String parsed = mapper.base("test", 0L);
        assertEquals("Base value is not properly parsed", "04ldqpdr", parsed);
    }

    @Test
    public void testValueBigIntegerMinPositive() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 20);
        String parsed = mapper.base("test", new BigInteger("1"));
        assertEquals("Base value is not properly parsed", "00l3r41ifs0q5ts", parsed);
    }

    @Test
    public void testValueBigIntegerMaxPositive() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 20);
        String parsed = mapper.base("test", new BigInteger("99999999999999999999"));
        assertEquals("Base value is not properly parsed", "0167i830vk1gbni", parsed);
    }

    @Test
    public void testValueBigIntegerMinNegative() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 20);
        String parsed = mapper.base("test", new BigInteger("-1"));
        assertEquals("Base value is not properly parsed", "00l3r41ifs0q5tq", parsed);
    }

    @Test
    public void testValueBigIntegerMaxNegative() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 20);
        String parsed = mapper.base("test", new BigInteger("-99999999999999999999"));
        assertEquals("Base value is not properly parsed", "000000000000000", parsed);
    }

    @Test
    public void testValueBigIntegerZero() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 20);
        String parsed = mapper.base("test", new BigInteger("0"));
        assertEquals("Base value is not properly parsed", "00l3r41ifs0q5tr", parsed);
    }

    @Test(expected = IndexException.class)
    public void testValueFloatMinPositive() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        mapper.base("test", 1.0f);
    }

    @Test(expected = IndexException.class)
    public void testValueFloatMaxPositive() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        mapper.base("test", 99999999.0f);
    }

    @Test(expected = IndexException.class)
    public void testValueFloatMinNegative() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        mapper.base("test", -1.0f);
    }

    @Test(expected = IndexException.class)
    public void testValueFloatMaxNegative() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        mapper.base("test", -99999999.0f);
    }

    @Test(expected = IndexException.class)
    public void testValueFloatZero() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        mapper.base("test", 0.0f);
    }

    @Test(expected = IndexException.class)
    public void testValueDoubleMinPositive() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 10);
        mapper.base("test", 1.0d);
    }

    @Test(expected = IndexException.class)
    public void testValueDoubleMaxPositive() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 10);
        mapper.base("test", 9999999999.0d);
    }

    @Test(expected = IndexException.class)
    public void testValueDoubleMinNegative() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 10);
        mapper.base("test", -1.0d);
    }

    @Test(expected = IndexException.class)
    public void testValueDoubleMaxNegative() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 10);
        mapper.base("test", -9999999999.0d);
    }

    @Test(expected = IndexException.class)
    public void testValueDoubleZero() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 10);
        mapper.base("test", 0.0d);
    }

    @Test(expected = IndexException.class)
    public void testValueTooBig() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        mapper.base("test", 100000000);
    }

    @Test(expected = IndexException.class)
    public void testValueTooSmall() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        mapper.base("test", -100000000);
    }

    @Test
    public void testValueNegativeMaxSort() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        String lower = mapper.base("test", -99999999);
        String upper = mapper.base("test", -99999998);
        int compare = lower.compareTo(upper);
        assertEquals("Cassandra ordering is not preserved", -1, compare);
    }

    @Test
    public void testValueNegativeMinSort() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        String lower = mapper.base("test", -2);
        String upper = mapper.base("test", -1);
        int compare = lower.compareTo(upper);
        assertEquals("Cassandra ordering is not preserved", -1, compare);
    }

    @Test
    public void testValuePositiveMaxSort() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        String lower = mapper.base("test", 99999998);
        String upper = mapper.base("test", 99999999);
        int compare = lower.compareTo(upper);
        assertEquals("Cassandra ordering is not preserved", -1, compare);
    }

    @Test
    public void testValuePositiveMinSort() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        String lower = mapper.base("test", 1);
        String upper = mapper.base("test", 2);
        int compare = lower.compareTo(upper);
        assertEquals("Cassandra ordering is not preserved", -1, compare);
    }

    @Test
    public void testValueNegativeZeroSort() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        String lower = mapper.base("test", -1);
        String upper = mapper.base("test", 0);
        int compare = lower.compareTo(upper);
        assertEquals("Cassandra ordering is not preserved", -1, compare);
    }

    @Test
    public void testValuePositiveZeroSort() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        String lower = mapper.base("test", 0);
        String upper = mapper.base("test", 1);
        int compare = lower.compareTo(upper);
        assertEquals("Cassandra ordering is not preserved", -1, compare);
    }

    @Test
    public void testValueExtremeSort() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        String lower = mapper.base("test", -99999999);
        String upper = mapper.base("test", 99999999);
        int compare = lower.compareTo(upper);
        assertEquals("Cassandra ordering is not preserved", -3, compare);
    }

    @Test
    public void testValueNegativePositiveSort() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 8);
        String lower = mapper.base("test", -1);
        String upper = mapper.base("test", 1);
        int compare = lower.compareTo(upper);
        assertEquals("Cassandra ordering is not preserved", -2, compare);
    }

    @Test
    public void testIndexedField() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, true, null, 10);
        String base = mapper.base("name", "4243");
        Field field = mapper.indexedField("name", base);
        assertNotNull("Indexed field is not created", field);
        assertEquals("Indexed field value is wrong", base, field.stringValue());
        assertEquals("Indexed field name is wrong", "name", field.name());
        assertEquals("Indexed field type is wrong", false, field.fieldType().stored());
    }

    @Test
    public void testSortedField() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, true, 10);
        String base = mapper.base("name", "4243");
        Field field = mapper.sortedField("name", base);
        assertNotNull("Sorted field is not created", field);
        assertEquals("Sorted field type is wrong", DocValuesType.SORTED, field.fieldType().docValuesType());
    }

    @Test
    public void testExtractAnalyzers() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, null, null, 10);
        assertEquals("Analyzer must be keyword", Mapper.KEYWORD_ANALYZER, mapper.analyzer);
    }

    @Test
    public void testToString() {
        BigIntegerMapper mapper = new BigIntegerMapper("field", null, false, false, 8);
        assertEquals("Method #toString is wrong",
                     "BigIntegerMapper{field=field, indexed=false, sorted=false, column=field, digits=8}",
                     mapper.toString());
    }
}
