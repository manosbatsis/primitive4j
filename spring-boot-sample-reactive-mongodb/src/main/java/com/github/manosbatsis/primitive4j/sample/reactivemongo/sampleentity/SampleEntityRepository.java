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
package com.github.manosbatsis.primitive4j.sample.reactivemongo.sampleentity;

import com.github.manosbatsis.primitive4j.sample.reactivemongo.sampleentity.network.UriBean;
import com.github.manosbatsis.primitive4j.sample.reactivemongo.sampleentity.network.UriRecord;
import com.github.manosbatsis.primitive4j.sample.reactivemongo.sampleentity.network.UrlBean;
import com.github.manosbatsis.primitive4j.sample.reactivemongo.sampleentity.network.UrlRecord;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface SampleEntityRepository extends ReactiveMongoRepository<SampleEntity, UUID> {

    // Network
    Flux<SampleEntity> findAllByUriRecord(UriRecord param);

    Flux<SampleEntity> findAllByUriBean(UriBean param);

    Flux<SampleEntity> findAllByUrlRecord(UrlRecord param);

    Flux<SampleEntity> findAllByUrlBean(UrlBean param);

    // Numbers
    Flux<SampleEntity> findAllByBigDecimalBean(BigDecimalBean param);

    Flux<SampleEntity> findAllByBigDecimalRecord(BigDecimalRecord param);

    Flux<SampleEntity> findAllByBigIntegerBean(BigIntegerBean param);

    Flux<SampleEntity> findAllByBigIntegerRecord(BigIntegerRecord param);

    Flux<SampleEntity> findAllByDoubleBean(DoubleBean param);

    Flux<SampleEntity> findAllByDoubleRecord(DoubleRecord param);

    Flux<SampleEntity> findAllByFloatBean(FloatBean param);

    Flux<SampleEntity> findAllByFloatRecord(FloatRecord param);

    Flux<SampleEntity> findAllByIntegerBean(IntegerBean param);

    Flux<SampleEntity> findAllByIntegerRecord(IntegerRecord param);

    Flux<SampleEntity> findAllByLongBean(LongBean param);

    Flux<SampleEntity> findAllByLongRecord(LongRecord param);

    Flux<SampleEntity> findAllByShortBean(ShortBean param);

    Flux<SampleEntity> findAllByShortRecord(ShortRecord param);
    //    // Strings
    Flux<SampleEntity> findAllByStringBean(StringBean param);

    Flux<SampleEntity> findAllByStringRecord(StringRecord param);

    Flux<SampleEntity> findAllByUuidBean(UuidBean param);

    Flux<SampleEntity> findAllByUuidRecord(UuidRecord param);
}
