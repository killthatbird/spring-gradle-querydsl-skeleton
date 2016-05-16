package com.park.spring.skeleton.base.entity;

import javax.persistence.MappedSuperclass;

/**
 * Entityのベース
 * 共通モジュールを利用するDTOは必ず継承する必要がある
 * @author park
 */
@MappedSuperclass
public interface BaseEntity {
    
}