package com.park.spring.skeleton.base.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mysema.query.types.FactoryExpressionBase;
import com.mysema.query.types.Predicate;
import com.park.spring.skeleton.base.entity.BaseEntity;

/**
 * データベースの基本操作
 * 全てのDAOはこのInterfaceを継承する必要がある。
 * CRUD
 * @author park
 *
 * @param <T>
 * @param <PK>
 */
public interface EntityDao<T extends BaseEntity, PK extends Serializable> {
	/**
	 * 新しいレコードを登録する
	 * @param entity 登録するEntity
	 * @return　T 登録されたEntity
	 */
	T   create(T entity);
	
	/**
	 * 新しいレコードリストを登録する
	 * ループしながらentityManager.persistするだけ
	 * @param entitys 登録するEntityリスト
	 * @return 登録されたEntityリスト
	 */
	List<T>   create(List<T> entitys);
	
	/**
	 * 全データを検索する
	 * @return List<T> 検索されたリスト
	 */
	List<T> findAll();
	
	/**
	 * 全データを検索する。(ページング処理)
	 * @param pageable ページング情報
	 * @return Page<T> 検索された結果
	 */
	Page<T> findAll(Pageable pageable);
	
	/**
	 * 検索条件のリストを検索する(ページング処理)
	 * @param predicate 検索情報
	 * @param pageable ページング情報
	 * @return Page<T> 検索された結果
	 */
	Page<T> findAll(Predicate predicate, Pageable pageable);
	
	/**
	 * 検索条件のリストを検索する(ページング処理)
	 * @param predicate 検索情報
	 * @param pageable ページング情報
	 * @param factoryExpressionBase 
	 * 							DTOのコンストラクタを利用して、検索結果をマッチングさせるとき
	 * @return 検索された結果
	 */
	Page<T> findAll(Predicate predicate, Pageable pageable, FactoryExpressionBase<T> factoryExpressionBase);
	
	/**
	 * PKのデータを検索する
	 * @param pk キー値
	 * @return T 検索されたデータ
	 */
	T findByPk(PK pk);
	
	/**
	 * テーブルに登録されているレコード数を検索する
	 * @return
	 */
	int countAll();
	
	/**
	 * アップデートする。
	 * @param entity
	 */
    void update(T entity);
    
    /**
     * 削除する。
     * @param entity
     */
    void delete(T entity);

}