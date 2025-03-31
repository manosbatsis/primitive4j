/**
 * Copyright (C) 2024-2025 Manos Batsis
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License along with this program. If not, see
 * <a href="https://www.gnu.org/licenses/lgpl-3.0.html">https://www.gnu.org/licenses/lgpl-3.0.html</a>.
 */
package com.github.manosbatsis.domainprimitives.sample.sampleentity;

import com.github.manosbatsis.domainprimitives.test.common.example.*;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SampleEntity {

    @Id
    @GeneratedValue
    private UUID id;

    // Network value types
    @Column(columnDefinition = "varchar(500)", nullable = false)
    private UriBean uriBean;

    @Column(columnDefinition = "varchar(500)", nullable = false)
    private UriRecord uriRecord;

    @Column(columnDefinition = "varchar(500)", nullable = false)
    private UrlBean urlBean;

    @Column(columnDefinition = "varchar(500)", nullable = false)
    private UrlRecord urlRecord;

    // Number value types
    @Column(columnDefinition = "decimal(10,2)", nullable = false)
    private BigDecimalBean bigDecimalBean;

    @Column(columnDefinition = "decimal(10,2)", nullable = false)
    private BigDecimalRecord bigDecimalRecord;

    @Column(columnDefinition = "bigint", nullable = false)
    private BigIntegerBean bigIntegerBean;

    @Column(columnDefinition = "bigint", nullable = false)
    private BigIntegerRecord bigIntegerRecord;

    @Column(columnDefinition = "decimal(10,2)", nullable = false)
    private DoubleBean doubleBean;

    @Column(columnDefinition = "decimal(10,2)", nullable = false)
    private DoubleRecord doubleRecord;

    @Column(columnDefinition = "decimal(10,2)", nullable = false)
    private FloatBean floatBean;

    @Column(columnDefinition = "decimal(10,2)", nullable = false)
    private FloatRecord floatRecord;

    @Column(columnDefinition = "int", nullable = false)
    private IntegerBean integerBean;

    @Column(columnDefinition = "int", nullable = false)
    private IntegerRecord integerRecord;

    @Column(columnDefinition = "bigint", nullable = false)
    private LongBean longBean;

    @Column(columnDefinition = "bigint", nullable = false)
    private LongRecord longRecord;

    @Column(columnDefinition = "smallint", nullable = false)
    private ShortBean shortBean;

    @Column(columnDefinition = "smallint", nullable = false)
    private ShortRecord shortRecord;
    // String value types
    @Column(columnDefinition = "varchar(255)", nullable = false)
    private StringBean stringBean;

    @Column(columnDefinition = "varchar(255)", nullable = false)
    private StringRecord stringRecord;

    @Column(columnDefinition = "varchar(36)", nullable = false)
    private UuidBean uuidBean;

    @Column(columnDefinition = "varchar(36)", nullable = false)
    private UuidRecord uuidRecord;
}
