package com.capstone.bidmarkit.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBid is a Querydsl query type for Bid
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBid extends EntityPathBase<Bid> {

    private static final long serialVersionUID = 331278298L;

    public static final QBid bid = new QBid("bid");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath memberId = createString("memberId");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Integer> productId = createNumber("productId", Integer.class);

    public QBid(String variable) {
        super(Bid.class, forVariable(variable));
    }

    public QBid(Path<? extends Bid> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBid(PathMetadata metadata) {
        super(Bid.class, metadata);
    }

}

