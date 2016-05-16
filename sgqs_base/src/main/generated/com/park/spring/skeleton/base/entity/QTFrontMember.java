package com.park.spring.skeleton.base.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTFrontMember is a Querydsl query type for TFrontMember
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTFrontMember extends EntityPathBase<TFrontMember> {

    private static final long serialVersionUID = -699514508L;

    public static final QTFrontMember tFrontMember = new QTFrontMember("tFrontMember");

    public final DateTimePath<java.util.Date> frontMemberApprovalDt = createDateTime("frontMemberApprovalDt", java.util.Date.class);

    public final StringPath frontMemberEmail = createString("frontMemberEmail");

    public final BooleanPath frontMemberIsLocked = createBoolean("frontMemberIsLocked");

    public final NumberPath<Integer> frontMemberKey = createNumber("frontMemberKey", Integer.class);

    public final DateTimePath<java.util.Date> frontMemberLoginDt = createDateTime("frontMemberLoginDt", java.util.Date.class);

    public final NumberPath<Short> frontMemberLoginFailCnt = createNumber("frontMemberLoginFailCnt", Short.class);

    public final DateTimePath<java.util.Date> frontMemberLoginFailDt = createDateTime("frontMemberLoginFailDt", java.util.Date.class);

    public final StringPath frontMemberLoginId = createString("frontMemberLoginId");

    public final StringPath frontMemberLoginPassword = createString("frontMemberLoginPassword");

    public final StringPath frontMemberName = createString("frontMemberName");

    public final DateTimePath<java.util.Date> frontMemberRegDt = createDateTime("frontMemberRegDt", java.util.Date.class);

    public final NumberPath<Short> frontMemberStatusTp = createNumber("frontMemberStatusTp", Short.class);

    public final NumberPath<Short> frontMemberTp = createNumber("frontMemberTp", Short.class);

    public final DateTimePath<java.util.Date> frontMemberUpdDt = createDateTime("frontMemberUpdDt", java.util.Date.class);

    public QTFrontMember(String variable) {
        super(TFrontMember.class, forVariable(variable));
    }

    public QTFrontMember(Path<? extends TFrontMember> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTFrontMember(PathMetadata<?> metadata) {
        super(TFrontMember.class, metadata);
    }

}

