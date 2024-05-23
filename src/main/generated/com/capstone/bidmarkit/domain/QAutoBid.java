package com.capstone.bidmarkit.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAutoBid is a Querydsl query type for AutoBid
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAutoBid extends EntityPathBase<AutoBid> {

    private static final long serialVersionUID = -1466152661L;

    public static final QAutoBid autoBid = new QAutoBid("autoBid");

    public final NumberPath<Integer> ceilingPrice = createNumber("ceilingPrice", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath memberId = createString("memberId");

    public final NumberPath<Integer> productId = createNumber("productId", Integer.class);

    public QAutoBid(String variable) {
        super(AutoBid.class, forVariable(variable));
    }

    public QAutoBid(Path<? extends AutoBid> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAutoBid(PathMetadata metadata) {
        super(AutoBid.class, metadata);
    }

}

