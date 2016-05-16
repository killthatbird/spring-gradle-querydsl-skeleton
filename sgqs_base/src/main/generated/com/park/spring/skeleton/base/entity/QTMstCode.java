package com.park.spring.skeleton.base.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QTMstCode is a Querydsl query type for TMstCode
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTMstCode extends EntityPathBase<TMstCode> {

    private static final long serialVersionUID = 647008876L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTMstCode tMstCode = new QTMstCode("tMstCode");

    public final StringPath mstCodeDesc = createString("mstCodeDesc");

    public final BooleanPath mstCodeIsEnabled = createBoolean("mstCodeIsEnabled");

    public final StringPath mstCodeNm = createString("mstCodeNm");

    public final NumberPath<Short> mstCodePriority = createNumber("mstCodePriority", Short.class);

    public final StringPath mstCodeVarNm = createString("mstCodeVarNm");

    public final QTMstCodePK tMstCodePK;

    public QTMstCode(String variable) {
        this(TMstCode.class, forVariable(variable), INITS);
    }

    public QTMstCode(Path<? extends TMstCode> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QTMstCode(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QTMstCode(PathMetadata<?> metadata, PathInits inits) {
        this(TMstCode.class, metadata, inits);
    }

    public QTMstCode(Class<? extends TMstCode> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.tMstCodePK = inits.isInitialized("tMstCodePK") ? new QTMstCodePK(forProperty("tMstCodePK")) : null;
    }

}

