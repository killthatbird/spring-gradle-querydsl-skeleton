package com.park.spring.skeleton.base.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTMstCodePK is a Querydsl query type for TMstCodePK
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QTMstCodePK extends BeanPath<TMstCodePK> {

    private static final long serialVersionUID = -994725529L;

    public static final QTMstCodePK tMstCodePK = new QTMstCodePK("tMstCodePK");

    public final NumberPath<Short> mstCodeMasterKey = createNumber("mstCodeMasterKey", Short.class);

    public final NumberPath<Short> mstCodeSubKey = createNumber("mstCodeSubKey", Short.class);

    public QTMstCodePK(String variable) {
        super(TMstCodePK.class, forVariable(variable));
    }

    public QTMstCodePK(Path<? extends TMstCodePK> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTMstCodePK(PathMetadata<?> metadata) {
        super(TMstCodePK.class, metadata);
    }

}

