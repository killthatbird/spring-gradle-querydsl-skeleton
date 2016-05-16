package com.park.spring.skeleton.base.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTFrontMemberLoginLog is a Querydsl query type for TFrontMemberLoginLog
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTFrontMemberLoginLog extends EntityPathBase<TFrontMemberLoginLog> {

    private static final long serialVersionUID = -1276475217L;

    public static final QTFrontMemberLoginLog tFrontMemberLoginLog = new QTFrontMemberLoginLog("tFrontMemberLoginLog");

    public final NumberPath<Integer> frontMemberLoginLogKey = createNumber("frontMemberLoginLogKey", Integer.class);

    public final DateTimePath<java.util.Date> frontMemberLoginLogLoginDt = createDateTime("frontMemberLoginLogLoginDt", java.util.Date.class);

    public final StringPath frontMemberLoginLogLoginId = createString("frontMemberLoginLogLoginId");

    public final StringPath frontMemberLoginLogLoginIp = createString("frontMemberLoginLogLoginIp");

    public QTFrontMemberLoginLog(String variable) {
        super(TFrontMemberLoginLog.class, forVariable(variable));
    }

    public QTFrontMemberLoginLog(Path<? extends TFrontMemberLoginLog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTFrontMemberLoginLog(PathMetadata<?> metadata) {
        super(TFrontMemberLoginLog.class, metadata);
    }

}

