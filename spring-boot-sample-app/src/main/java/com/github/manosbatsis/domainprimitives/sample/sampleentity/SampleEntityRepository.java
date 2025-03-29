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
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleEntityRepository extends JpaRepository<SampleEntity, UUID> {

    // Network
    List<SampleEntity> findAllByUriRecord(UriRecord param);

    List<SampleEntity> findAllByUriBean(UriBean param);

    List<SampleEntity> findAllByUrlRecord(UrlRecord param);

    List<SampleEntity> findAllByUrlBean(UrlBean param);

    // Numbers
    List<SampleEntity> findAllByBigDecimalBean(BigDecimalBean param);

    List<SampleEntity> findAllByBigDecimalRecord(BigDecimalRecord param);

    List<SampleEntity> findAllByBigIntegerBean(BigIntegerBean param);

    List<SampleEntity> findAllByBigIntegerRecord(BigIntegerRecord param);

    List<SampleEntity> findAllByDoubleBean(DoubleBean param);

    List<SampleEntity> findAllByDoubleRecord(DoubleRecord param);

    List<SampleEntity> findAllByFloatBean(FloatBean param);

    List<SampleEntity> findAllByFloatRecord(FloatRecord param);

    List<SampleEntity> findAllByIntegerBean(IntegerBean param);

    List<SampleEntity> findAllByIntegerRecord(IntegerRecord param);

    List<SampleEntity> findAllByLongBean(LongBean param);

    List<SampleEntity> findAllByLongRecord(LongRecord param);

    List<SampleEntity> findAllByShortBean(ShortBean param);

    List<SampleEntity> findAllByShortRecord(ShortRecord param);
    // Strings
    List<SampleEntity> findAllByStringBean(StringBean param);

    List<SampleEntity> findAllByStringRecord(StringRecord param);

    List<SampleEntity> findAllByUuidBean(UuidBean param);

    List<SampleEntity> findAllByUuidRecord(UuidRecord param);
}
